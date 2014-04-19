package app.android.homeBase;


import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.ViewGroup;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

public class BillCreateActivity extends HomeBaseActivity {
    public ParseBase parse;
    private BootstrapEditText headerBar;
    private BootstrapEditText infoContainer;
    private BootstrapEditText dollarAmountField;
    private BootstrapButton creatorField;

    private ArrayList<String> userNames;
    private HashMap<String, BootstrapButton> responsibleUsers;
    private HashMap<BootstrapButton, Boolean> selectedResponsibleUsers;

    private final String k_alertType = "Bill";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_create);
        parse = new ParseBase(this);

        headerBar = (BootstrapEditText) this.findViewById(R.id.billCreate_header_field);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Resources resources = this.getResources();

        int navBarHeight = 0;

        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            navBarHeight = resources.getDimensionPixelSize(resourceId);
        }

        int statusBarHeight = 0;

        resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = resources.getDimensionPixelSize(resourceId);
        }

        int height = ((size.y - headerBar.getLayoutParams().height * 4) - navBarHeight - statusBarHeight);

        infoContainer = (BootstrapEditText) this.findViewById(R.id.billCreate_body_field);
        ViewGroup.LayoutParams rlp = infoContainer.getLayoutParams();
        rlp.height = height;
        infoContainer.setLayoutParams(rlp);

        //set up "responsible for" options

        userNames = ApplicationManager.getInstance().getHomeUsers();
        responsibleUsers = new HashMap<String, BootstrapButton>();
        selectedResponsibleUsers = new HashMap<BootstrapButton, Boolean>();

        for (int i = 0; i < userNames.size(); i++) {
            LayoutInflater inflater = LayoutInflater.from(this);
            LinearLayout template = (LinearLayout) inflater.inflate(R.layout.user_select_template, null, false);
            RelativeLayout btnContainer = (RelativeLayout) template.findViewById(R.id.userSelection_template_container);
            template.removeView(btnContainer);
            BootstrapButton userBtn = (BootstrapButton)btnContainer.findViewById(R.id.userSelection_button);
            userBtn.setText(userNames.get(i));
            responsibleUsers.put(userNames.get(i), userBtn);
            selectedResponsibleUsers.put(userBtn, false);
            LinearLayout responsibleContainer = (LinearLayout) this.findViewById(R.id.billCreate_responsible_container);
            responsibleContainer.addView(btnContainer);
        }

        creatorField = (BootstrapButton) this.findViewById(R.id.billCreate_creator_field);
        creatorField.setText(parse.getCurrentUser().getUsername());

        dollarAmountField = (BootstrapEditText) this.findViewById(R.id.billCreate_dollars_field);
    }

    public void onUserSelected(View view)
    {
        BootstrapButton button = (BootstrapButton)view;
        if (!selectedResponsibleUsers.get(button)) {
            button.setBootstrapType("info");
            selectedResponsibleUsers.put(button, true);
        } else {
            button.setBootstrapType("default");
            selectedResponsibleUsers.put(button, false);
        }
    }

    public void onBillCreateSubmitClick(View view)
    {
        String title = headerBar.getText().toString();
        String type = k_alertType;
        String desc = infoContainer.getText().toString();
        String dollarAmountStr = "0";
        if (!dollarAmountField.getText().toString().equals("")) {
            dollarAmountStr = dollarAmountField.getText().toString();
        }

        Double amount = Double.parseDouble(dollarAmountStr);
        String creator = parse.getCurrentUser().getUsername();
        List<String> responsibleUsers = new LinkedList<String>();

        for(String user : userNames) {
            BootstrapButton userButton = this.responsibleUsers.get(user);
            if (selectedResponsibleUsers.get(userButton)) {
                responsibleUsers.add(user);
            }
        }

        parse.createBill(title, type, desc, amount, responsibleUsers, creator, BillCreateActivity.this);
    }

    public void onBillCreateCancelClick(View view)
    {
        onBackPressed();
    }

    @Override
    public void onCreateAlertSuccess(HomeBaseAlert alert)
    {
        Toast.makeText(BillCreateActivity.this, alert.getTitle() + ": " + alert.getDescription() + ": " + alert.getAmount(), Toast.LENGTH_LONG).show();
        int numUsers = alert.getResponsibleUsers().size();
        double splitAmount = (alert.getAmount() / numUsers );

        Intent email = new Intent(Intent.ACTION_SEND);
        email.setType("message/rfc822");

        //for(String user : alert.getResponsibleUsers()) {
        //    //TODO GET EMAILS
        //}
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{"smits2010@gmail.com","rosuemanuel@gmail.com"});
        email.putExtra(Intent.EXTRA_CC, new String[]{"request@square.com"});
        email.putExtra(Intent.EXTRA_SUBJECT, "$"+splitAmount);
        email.putExtra(Intent.EXTRA_TEXT, alert.getTitle()+"\n"+alert.getDescription());
        try {
            startActivity(Intent.createChooser(email, "Send mail..."));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(BillCreateActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreateAlertFailure(String e)
    {
        Toast.makeText(BillCreateActivity.this, e, Toast.LENGTH_LONG).show();
    }
}
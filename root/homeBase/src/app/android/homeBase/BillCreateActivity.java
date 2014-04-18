package app.android.homeBase;


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

import java.util.LinkedList;
import java.util.List;

public class BillCreateActivity extends HomeBaseActivity {
    public ParseBase parse;
    private BootstrapEditText headerBar;
    private BootstrapEditText infoContainer;
    private BootstrapButton creatorField;
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
        for (int i = 0; i < 2; i++) {
            LayoutInflater inflater = LayoutInflater.from(this);
            LinearLayout template = (LinearLayout) inflater.inflate(R.layout.user_select_template, null, false);
            RelativeLayout btnContainer = (RelativeLayout) template.findViewById(R.id.userSelection_template_container);
            template.removeView(btnContainer);
            LinearLayout responsibleContainer = (LinearLayout) this.findViewById(R.id.billCreate_responsible_container);
            responsibleContainer.addView(btnContainer);
        }

        creatorField = (BootstrapButton) this.findViewById(R.id.billCreate_creator_field);
        creatorField.setText(parse.getCurrentUser().getUsername());
    }

    public void onBillCreateSubmitClick(View view)
    {
        String title = headerBar.getText().toString();
        String type = k_alertType;
        String desc = infoContainer.getText().toString();
        String creator = parse.getCurrentUser().getUsername();
        List<String> responsibleUsers = new LinkedList<String>();
        parse.createAlert(title,type, desc, responsibleUsers, creator, BillCreateActivity.this);
    }

    public void onBillCreateCancelClick(View view)
    {
        onBackPressed();
    }

    @Override
    public void onCreateAlertSuccess(HomeBaseAlert alert)
    {
        Toast.makeText(BillCreateActivity.this, alert.getTitle() + ": " + alert.getDescription(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreateAlertFailure(String e)
    {
        Toast.makeText(BillCreateActivity.this, e, Toast.LENGTH_LONG).show();
    }
}
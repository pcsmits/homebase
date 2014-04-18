package app.android.homeBase;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.view.View;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.parse.ParseUser;

public class BillsActivity extends HomeBaseActivity {
    private ParseBase parse;
    private ArrayList<BootstrapButton> billContainers;
    private ArrayList<String> billTitles;
    private HashMap<BootstrapButton, HomeBaseAlert> billDescriptions;
    private LinearLayout layout;
    private boolean startCalled = false;

    private BootstrapButton selectedFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Init parse
        parse = new ParseBase(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills);

        layout = (LinearLayout) findViewById(R.id.bills_billContainer_button);
        billContainers = new ArrayList<BootstrapButton>();
        billTitles = new ArrayList<String>();
        billDescriptions = new HashMap<BootstrapButton, HomeBaseAlert>();

        startCalled = true;
        selectedFilter = (BootstrapButton) findViewById(R.id.bills_allFilter_button);
        selectedFilter.setEnabled(false);

        parse.getAlerts(this, "Bill");
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (startCalled) {
            startCalled = false;
            return;
        }

        parse.refreshAlerts(this, "Bill");
    }

    public void onBillContainerClick(View view)
    {
        /*BootstrapButton thisButton = (BootstrapButton) view.findViewById(R.id.bill_container_button);
        thisButton.setText("Clicked");
        Intent intent = new Intent(BillsActivity.this, ChoreInfoActivity.class);
        intent.putExtra("title", billDescriptions.get(thisButton).getTitle());
        intent.putExtra("info", billDescriptions.get(thisButton).getDescription());
        startActivity(intent);*/
    }

    @Override
    public void onGetBillsFailure(String e)
    {

    }

    @Override
    public void onGetAlertListByTypeSuccess(ArrayList<HomeBaseAlert> alerts)
    {
        // Fetch all the bills from parse
        for (HomeBaseAlert alert : alerts)
        {
            LayoutInflater inflater = LayoutInflater.from(this);
            LinearLayout buttonCont = (LinearLayout) inflater.inflate(R.layout.bill_container, null, false);

            BootstrapButton myButton = (BootstrapButton) buttonCont.findViewById(R.id.bill_container_button);
            buttonCont.removeView(myButton);
            layout.addView(myButton);
            String text = alert.getDescription();
            myButton.setText(text + " Amount: " + alert.getAmount());
            billContainers.add(myButton);
            billDescriptions.put(myButton, alert);
        }
    }

    @Override
    public void onGetAlertListByTypeFailure(String e)
    {

    }

    @Override
    public void onUpdateAlertListByTypeSuccess(ArrayList<HomeBaseAlert> alerts)
    {
        for (int i = 0; i < alerts.size(); i++) {
            if (!billTitles.contains(alerts.get(i).getTitle())) {
                LayoutInflater inflater = LayoutInflater.from(this);
                LinearLayout buttonCont = (LinearLayout) inflater.inflate(R.layout.chore_container, null, false);

                BootstrapButton myButton = (BootstrapButton) buttonCont.findViewById(R.id.login_test_button);
                buttonCont.removeView(myButton);
                layout.addView(myButton);
                String text = alerts.get(i).getTitle();
                myButton.setText(text + " Amount: " + alerts.get(i).getAmount());
                billContainers.add(myButton);
                String title = text;
                billTitles.add(title);
                billDescriptions.put(myButton, alerts.get(i));
            }
        }
    }

    @Override
    public void onUpdateAlertListByTypeFailure(String e)
    {

    }
}

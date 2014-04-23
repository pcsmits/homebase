package app.android.homeBase;

import android.content.Intent;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.view.View;
import com.beardedhen.androidbootstrap.BootstrapButton;

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

    public void onChoreContainerClick(View view)
    {
        /*BootstrapButton thisButton = (BootstrapButton) view.findViewById(R.id.bill_container_button);
        thisButton.setText("Clicked");
        Intent intent = new Intent(BillsActivity.this, ChoreInfoActivity.class);
        intent.putExtra("title", billDescriptions.get(thisButton).getTitle());
        intent.putExtra("info", billDescriptions.get(thisButton).getDescription());
        startActivity(intent);*/
    }

    public void onBillAddClick(View view)
    {
        Intent intent = new Intent(BillsActivity.this, BillCreateActivity.class);
        startActivity(intent);
    }

    public void onAllFilterClick(View view)
    {
        selectedFilter.setEnabled(true);

        BootstrapButton clicked = (BootstrapButton) view;
        clicked.setBootstrapButtonEnabled(false);

        selectedFilter = clicked;

        layout.removeAllViews();
        for(int i = 0; i < billContainers.size(); i++) {
            BootstrapButton billContainer = billContainers.get(i);
            if (billDescriptions.get(billContainer).getCreatorID() != parse.getCurrentUser().getUsername()) {
                layout.addView(billContainer);
            }
        }
    }

    public void onCreatedFilterClick(View view)
    {
        selectedFilter.setEnabled(true);

        BootstrapButton clicked = (BootstrapButton) view;
        clicked.setBootstrapButtonEnabled(false);

        selectedFilter = clicked;

        layout.removeAllViews();
        for(int i = 0; i < billContainers.size(); i++) {
            BootstrapButton billContainer = billContainers.get(i);
            if (billDescriptions.get(billContainer).getCreatorID().equals(parse.getCurrentUser().getUsername().toString())) {
                layout.addView(billContainer);
            }
        }
    }

    @Override
    public void onGetAlertListByTypeSuccess(ArrayList<HomeBaseAlert> alerts)
    {
        // Fetch all the bills from parse
        for (HomeBaseAlert alert : alerts)
        {
            LayoutInflater inflater = LayoutInflater.from(this);
            LinearLayout buttonCont = (LinearLayout) inflater.inflate(R.layout.alert_container, null, false);

            BootstrapButton myButton = (BootstrapButton) buttonCont.findViewById(R.id.alertContainer_container);
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
                LinearLayout buttonCont = (LinearLayout) inflater.inflate(R.layout.alert_container, null, false);

                BootstrapButton myButton = (BootstrapButton) buttonCont.findViewById(R.id.alertContainer_container);
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

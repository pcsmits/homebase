package app.android.homeBase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import java.util.ArrayList;
import java.util.HashMap;


public class SuppliesActivity extends HomeBaseActivity {
    private ArrayList<BootstrapButton> supplyContainers;
    private ArrayList<String> supplyIDs;
    private HashMap<BootstrapButton, HomeBaseAlert> supplyDescriptions;
    private LinearLayout layout;
    private ApplicationManager mApplication;
    private BootstrapButton selectedFilter;
    boolean startCalled = false;
    private BootstrapButton clickedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplication = (ApplicationManager)getApplicationContext();
        myIntent = getIntent();
        myClassName = "SuppliesActivity";
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
        setContentView(R.layout.activity_supplies);

        layout = (LinearLayout) findViewById(R.id.supply_supplyContainer_button);
        supplyContainers = new ArrayList<BootstrapButton>();
        supplyIDs = new ArrayList<String>();
        supplyDescriptions = new HashMap<BootstrapButton, HomeBaseAlert>();

        selectedFilter = (BootstrapButton) findViewById(R.id.supply_allFilter_button);
        selectedFilter.setEnabled(false);
        startCalled = true;

        mApplication.parse.getAlerts(this, "Supply");
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (startCalled) {
            startCalled = false;
            return;
        }

        mApplication.parse.refreshAlerts(this, "Supply");
    }

    public void onAllFilterClick(View view)
    {
        selectedFilter.setEnabled(true);

        BootstrapButton clicked = (BootstrapButton) view;
        clicked.setBootstrapButtonEnabled(false);

        selectedFilter = clicked;

        layout.removeAllViews();
        for(int i = 0; i < supplyContainers.size(); i++) {
            BootstrapButton supplyContainer = supplyContainers.get(i);
            if (!supplyDescriptions.get(supplyContainer).getCreatorID().equals(mApplication.parse.getCurrentUser().getUsername())) {
                layout.addView(supplyContainer);
            }
        }
    }

    public void onSupplyAddClick(View view)
    {
        Intent intent = new Intent(SuppliesActivity.this, SupplyCreateActivity.class);
        intent.putExtra("caller", myClassName);
        startActivity(intent);
    }

    public void onCreatedFilterClick(View view)
    {
        selectedFilter.setEnabled(true);

        BootstrapButton clicked = (BootstrapButton) view;
        clicked.setBootstrapButtonEnabled(false);

        selectedFilter = clicked;

        layout.removeAllViews();
        for(int i = 0; i < supplyContainers.size(); i++) {
            BootstrapButton supplyContainer = supplyContainers.get(i);
            if (supplyDescriptions.get(supplyContainer).getCreatorID().equals(mApplication.parse.getCurrentUser().getUsername())) {
                layout.addView(supplyContainer);
            }
        }
    }

    @Override
    public void onGetAlertListByTypeSuccess(ArrayList<HomeBaseAlert> alerts)
    {
        if (alerts.size() == 0){
            LayoutInflater inflater = LayoutInflater.from(this);
            LinearLayout buttonCont = (LinearLayout) inflater.inflate(R.layout.alert_container, null, false);

            BootstrapButton myButton = (BootstrapButton) buttonCont.findViewById(R.id.alertContainer_container);
            BootstrapButton header = (BootstrapButton)myButton.findViewById(R.id.alertContainer_header);

            buttonCont.removeView(myButton);
            layout.addView(myButton);

            myButton.setText("No supplies are needed");
            header.setText("Welcome");
            header.setBootstrapType("supply");
        }

        // Fetch all the bills from parse
        for (HomeBaseAlert alert : alerts)
        {
            LayoutInflater inflater = LayoutInflater.from(this);
            LinearLayout buttonCont = (LinearLayout) inflater.inflate(R.layout.alert_container, null, false);

            BootstrapButton myButton = (BootstrapButton) buttonCont.findViewById(R.id.alertContainer_container);
            BootstrapButton headerBar = (BootstrapButton) myButton.findViewById(R.id.alertContainer_header);

            buttonCont.removeView(myButton);
            layout.addView(myButton);

            String title = alert.getTitle();
            String information = alert.getDescription();

            headerBar.setText(title);
            headerBar.setBootstrapType("supply");
            myButton.setText(information);

            supplyContainers.add(myButton);
            supplyDescriptions.put(myButton, alert);
            supplyIDs.add(alert.getId());
        }
    }

    @Override
    public void onGetAlertListByTypeFailure(String e)
    {

    }

    @Override
    public void onUpdateAlertListByTypeSuccess(ArrayList<HomeBaseAlert> alerts)
    {
        for (HomeBaseAlert alert: alerts) {
            if (!supplyIDs.contains(alert.getId())) {
                LayoutInflater inflater = LayoutInflater.from(this);
                LinearLayout buttonCont = (LinearLayout) inflater.inflate(R.layout.alert_container, null, false);

                BootstrapButton myButton = (BootstrapButton) buttonCont.findViewById(R.id.alertContainer_container);
                BootstrapButton headerBar = (BootstrapButton) myButton.findViewById(R.id.alertContainer_header);

                buttonCont.removeView(myButton);
                layout.addView(myButton);

                String title = alert.getTitle();
                String information = alert.getDescription();

                headerBar.setText(title);
                headerBar.setBootstrapType("bill");
                myButton.setText(information);

                supplyContainers.add(myButton);
                supplyDescriptions.put(myButton, alert);
                supplyIDs.add(alert.getId());
            }
        }
    }

    public void onChoreContainerClick(View v)
    {
        clickedButton = (BootstrapButton) v.findViewById(R.id.alertContainer_container);
        mApplication.parse.getUsername(supplyDescriptions.get(clickedButton).getCreatorID(), SuppliesActivity.this);
    }

    @Override
    public void onGetUsernameSuccess(String username)
    {
        Intent intent = new Intent(SuppliesActivity.this, SupplyInfoActivity.class);
        intent.putExtra("caller", myClassName);
        intent.putExtra("title", supplyDescriptions.get(clickedButton).getTitle());
        intent.putExtra("info", supplyDescriptions.get(clickedButton).getDescription());
        intent.putExtra("creator", username);
        intent.putExtra("id", supplyDescriptions.get(clickedButton).getId());
        startActivity(intent);
    }

    @Override
    public void onGetUsernameFailure(String e)
    {
        Toast.makeText(SuppliesActivity.this, "Error: "+e, Toast.LENGTH_LONG).show();
    }
}

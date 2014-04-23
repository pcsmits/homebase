package app.android.homeBase;


import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import java.util.ArrayList;
import java.util.HashMap;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.view.View;

import com.beardedhen.androidbootstrap.BootstrapButton;

public class ChoresActivity extends HomeBaseActivity {
    private ParseBase parse;
    private ArrayList<BootstrapButton> choreContainers;
    private ArrayList<String> choreTitles;
    private HashMap<BootstrapButton, ChoreInfo> choreDescriptions;
    private LinearLayout layout;
    private boolean startCalled = false;

    private BootstrapButton selectedFilter;

    class ChoreInfo {
        public String title;
        public String information;
        public String creator;
        ChoreInfo(String title, String information, String creator) {
            this.title = title;
            this.information = information;
            this.creator = creator;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chores);
        parse = new ParseBase(this);

        layout = (LinearLayout) findViewById(R.id.chores_choreContainer_button);
        layout.removeAllViews();
        choreContainers = new ArrayList<BootstrapButton>();
        choreTitles = new ArrayList<String>();
        choreDescriptions = new HashMap<BootstrapButton, ChoreInfo>();

        startCalled = true;
        selectedFilter = (BootstrapButton) findViewById(R.id.chores_allFilter_button);
        selectedFilter.setEnabled(false);

        parse.getAlerts(this, "Chore");

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (startCalled) {
            startCalled = false;
            return;
        }

        parse.refreshAlerts(this, "Chore");
    }

    public void onChoreContainerClick(View view)
    {
        BootstrapButton thisButton = (BootstrapButton) view.findViewById(R.id.choreContainer_container);

        Intent intent = new Intent(ChoresActivity.this, ChoreInfoActivity.class);
        intent.putExtra("title", choreDescriptions.get(thisButton).title);
        intent.putExtra("info", choreDescriptions.get(thisButton).information);
        intent.putExtra("creator", choreDescriptions.get(thisButton).creator);
        startActivity(intent);
    }

    public void onChoreAddClick(View view)
    {
        Intent intent = new Intent(ChoresActivity.this, ChoreCreateActivity.class);
        startActivity(intent);
    }


    public void onAllFilterClick(View view)
    {
        selectedFilter.setEnabled(true);

        BootstrapButton clicked = (BootstrapButton) view;
        clicked.setBootstrapButtonEnabled(false);

        selectedFilter = clicked;

        layout.removeAllViews();
        for(int i = 0; i < choreContainers.size(); i++) {
            BootstrapButton choreContainer = choreContainers.get(i);
            if (choreDescriptions.get(choreContainer).creator != parse.getCurrentUser().getUsername()) {
                layout.addView(choreContainer);
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
        for(int i = 0; i < choreContainers.size(); i++) {
            BootstrapButton choreContainer = choreContainers.get(i);
            if (choreDescriptions.get(choreContainer).creator.equals(parse.getCurrentUser().getUsername().toString())) {
                layout.addView(choreContainer);
            }
        }
    }

    public void onResponsibleFilterClick(View view)
    {

    }

    @Override
    public void onGetAlertListByTypeSuccess(ArrayList<HomeBaseAlert> alerts)
    {
        //this will eventually run through chores from parse and populate view accordingly, but this is a good framework
        //for creating bootstrap buttons programatically from xml frameworks
        for (int i = 0; i < alerts.size(); i++) {
            LayoutInflater inflater = LayoutInflater.from(this);
            LinearLayout buttonCont = (LinearLayout) inflater.inflate(R.layout.chore_container, null, false);

            BootstrapButton myButton = (BootstrapButton) buttonCont.findViewById(R.id.choreContainer_container);
            BootstrapButton headerBar = (BootstrapButton) myButton.findViewById(R.id.choreContainer_header);

            buttonCont.removeView(myButton);
            layout.addView(myButton);

            String title = alerts.get(i).getTitle();
            String information = alerts.get(i).getDescription();
            String creator = alerts.get(i).getCreatorID();

            headerBar.setText(title);
            headerBar.setBootstrapType("chore");
            myButton.setText(information);

            choreContainers.add(myButton);
            choreTitles.add(title);
            choreDescriptions.put(myButton, new ChoreInfo(title, information, creator));
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
            if (!choreTitles.contains(alerts.get(i).getTitle())) {
                LayoutInflater inflater = LayoutInflater.from(this);
                LinearLayout buttonCont = (LinearLayout) inflater.inflate(R.layout.chore_container, null, false);

                BootstrapButton myButton = (BootstrapButton) buttonCont.findViewById(R.id.choreContainer_container);
                BootstrapButton headerBar = (BootstrapButton) myButton.findViewById(R.id.choreContainer_header);

                buttonCont.removeView(myButton);
                layout.addView(myButton);

                String title = alerts.get(i).getTitle();
                String information = alerts.get(i).getDescription();
                String creator = alerts.get(i).getCreatorID();

                headerBar.setText(title);
                headerBar.setBootstrapType("chore");
                myButton.setText(information);

                choreContainers.add(myButton);
                choreTitles.add(title);
                choreDescriptions.put(myButton, new ChoreInfo(title, information, creator));
            }
        }
    }

    @Override
    public void onUpdateAlertListByTypeFailure(String e)
    {

    }
}

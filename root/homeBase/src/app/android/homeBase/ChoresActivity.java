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
    private HashMap<BootstrapButton, ChoreInfo> choreDescriptions;
    private LinearLayout layout;

    class ChoreInfo {
        public String title;
        public String information;
        ChoreInfo(String title, String information) {
            this.title = title;
            this.information = information;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chores);
        parse = new ParseBase(this);

        layout = (LinearLayout) findViewById(R.id.chores_choreContainer_button);
        choreContainers = new ArrayList<BootstrapButton>();
        choreDescriptions = new HashMap<BootstrapButton, ChoreInfo>();

        parse.getAlerts(this, "Chore");
    }

    @Override
    protected void onResume() {
        super.onResume();
        for(int i = 0; i < choreContainers.size(); i++) {
            layout.removeView(choreContainers.get(i));
        }

        choreContainers.clear();
        parse.getAlerts(this, "Chore");
    }

    public void onChoreContainerClick(View view)
    {
        BootstrapButton thisButton = (BootstrapButton) view.findViewById(R.id.login_test_button);
        thisButton.setText("Clicked");
        Intent intent = new Intent(ChoresActivity.this, ChoreInfoActivity.class);
        intent.putExtra("title", choreDescriptions.get(thisButton).title);
        intent.putExtra("info", choreDescriptions.get(thisButton).information);
        startActivity(intent);
    }

    public void onChoreAddClick(View view)
    {
        Intent intent = new Intent(ChoresActivity.this, ChoreCreateActivity.class);
        startActivity(intent);
    }

    @Override
    public void onGetAlertListByTypeSuccess(ArrayList<HomeBaseAlert> alerts)
    {
        //this will eventually run through chores from parse and populate view accordingly, but this is a good framework
        //for creating bootstrap buttons programmaticaly from xml frameworks
        for (int i = 0; i < alerts.size(); i++) {
            LayoutInflater inflater = LayoutInflater.from(this);
            LinearLayout buttonCont = (LinearLayout) inflater.inflate(R.layout.chore_container, null, false);

            BootstrapButton myButton = (BootstrapButton) buttonCont.findViewById(R.id.login_test_button);
            buttonCont.removeView(myButton);
            layout.addView(myButton);
            String text = alerts.get(i).getTitle();
            myButton.setText(text);
            choreContainers.add(myButton);
            String title = text;
            String information = alerts.get(i).getDescription();
            choreDescriptions.put(myButton, new ChoreInfo(title, information));
        }
    }

    @Override
    public void onGetAlertListByTypeFailure(String e)
    {

    }
}

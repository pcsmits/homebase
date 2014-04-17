package app.android.homeBase;


import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import com.beardedhen.androidbootstrap.BootstrapButton;

public class NewsFeedActivity extends HomeBaseActivity {
    public ParseBase parse;
    public ArrayList<String> alertTitles;
    private LinearLayout globalLayout;
    private boolean expand = true;
    private boolean startCalled = false;
    private int menuHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed);

        alertTitles = new ArrayList<String>();

        parse = new ParseBase(this);
        globalLayout = (LinearLayout)this.findViewById(R.id.newsfeed_menu_container);
        menuHeight = globalLayout.getLayoutParams().height;

        startCalled = true;
        parse.getAlerts(NewsFeedActivity.this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if (startCalled) {
            startCalled = false;
            return;
        }

        parse.refreshAlerts(this);
    }

    public void onMenuButtonClick(View view)
    {
        LinearLayout relativeLayout = globalLayout;

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) (relativeLayout.getLayoutParams());

        ResizeAnimation a = new ResizeAnimation(relativeLayout);
        a.setDuration(250);

        if (expand) {
            a.setParams(lp.height, menuHeight + menuHeight * 3); //this will be times the number of modules we have
        } else {
            a.setParams(lp.height, menuHeight);
        }

        expand = !expand;

        relativeLayout.startAnimation(a);
    }

    public void onChoresButtonClick(View view)
    {
        Intent intent = new Intent(NewsFeedActivity.this, ChoresActivity.class);
        startActivity(intent);
    }

    public void onBillsButtonClick(View view)
    {
        Intent intent = new Intent(NewsFeedActivity.this, ChoresActivity.class);
        startActivity(intent);
    }

    public void onUsersHomeButtonClick(View view)
    {
        Intent intent = new Intent(NewsFeedActivity.this, GPSActivity.class);
        startActivity(intent);
    }

    @Override
    public void onGetAlertListSuccess(ArrayList<HomeBaseAlert> alerts)
    {
        LinearLayout layout = (LinearLayout) findViewById(R.id.newsfeed_newsfeedItem_container);

        for (int i = 0; i < alerts.size(); i++) {
            alertTitles.add(alerts.get(i).getTitle());

            LayoutInflater inflater = LayoutInflater.from(this);
            LinearLayout buttonCont = (LinearLayout) inflater.inflate(R.layout.newsfeed_item_template, null, false);

            BootstrapButton myButton = (BootstrapButton) buttonCont.findViewById(R.id.newsfeed_template_button);
            buttonCont.removeView(myButton);
            layout.addView(myButton);
            String text = alerts.get(i).getDescription();
            myButton.setText(text);
        }
    }

    @Override
    public void onUpdateAlertListSuccess(ArrayList<HomeBaseAlert> alerts)
    {
        LinearLayout layout = (LinearLayout) findViewById(R.id.newsfeed_newsfeedItem_container);

        for (int i = 0; i < alerts.size(); i++) {
            if (!alertTitles.contains(alerts.get(i).getTitle())) {
                alertTitles.add(alerts.get(i).getTitle());

                LayoutInflater inflater = LayoutInflater.from(this);
                LinearLayout buttonCont = (LinearLayout) inflater.inflate(R.layout.newsfeed_item_template, null, false);

                BootstrapButton myButton = (BootstrapButton) buttonCont.findViewById(R.id.newsfeed_template_button);
                buttonCont.removeView(myButton);
                layout.addView(myButton);
                String text = alerts.get(i).getDescription();
                myButton.setText(text);
            }
        }
    }

    @Override
    public void onUpdateAlertListFailure(String e)
    {

    }
}

package app.android.homeBase;


import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import com.beardedhen.androidbootstrap.BootstrapButton;

public class NewsFeedActivity extends HomeBaseActivity {
    public ParseBase parse;
    public List<HomeBaseAlert> alerts;
    private LinearLayout globalLayout;
    private boolean expand = true;
    private int menuHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed);

        parse = new ParseBase(this);
        globalLayout = (LinearLayout)this.findViewById(R.id.newsfeed_menu_container);
        menuHeight = globalLayout.getLayoutParams().height;

        parse.getAlerts(NewsFeedActivity.this);
    }

    public void onMenuButtonClick(View view)
    {
        LinearLayout relativeLayout = globalLayout;

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) (relativeLayout.getLayoutParams());

        ResizeAnimation a = new ResizeAnimation(relativeLayout);
        a.setDuration(250);

        if (expand) {
            a.setParams(lp.height, menuHeight + menuHeight * 2); //this will be times the number of modules we have
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

    @Override
    public void onGetAlertListSuccess(ArrayList<HomeBaseAlert> alerts)
    {
        LinearLayout layout = (LinearLayout) findViewById(R.id.newsfeed_newsfeedItem_container);

        for (int i = 0; i < alerts.size(); i++) {
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

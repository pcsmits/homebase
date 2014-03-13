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

public class ChoresActivity extends ActionBarActivity {
    ArrayList<BootstrapButton> choreContainers;
    HashMap<BootstrapButton, ChoreInfo> choreDescriptions;

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

        LinearLayout layout = (LinearLayout) findViewById(R.id.chores_choreContainer_button);
        choreContainers = new ArrayList<BootstrapButton>();
        choreDescriptions = new HashMap<BootstrapButton, ChoreInfo>();

        //this will eventually run through chores from parse and populate view accordingly, but this is a good framework
        //for creating bootstrap buttons programmaticaly from xml frameworks
        for (int i = 0; i < 5; i++) {
            LayoutInflater inflater = LayoutInflater.from(this);
            LinearLayout buttonCont = (LinearLayout) inflater.inflate(R.layout.chore_container, null, false);

            BootstrapButton myButton = (BootstrapButton) buttonCont.findViewById(R.id.login_test_button);
            buttonCont.removeView(myButton);
            layout.addView(myButton);
            String text = "Button " + i;
            myButton.setText(text);
            choreContainers.add(myButton);
            String title = "Issue #: " + i;
            String information = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam et imperdiet diam, " +
                    "dignissim tincidunt mauris. Donec euismod vehicula augue. Ut velit augue, commodo in malesuada in, " +
                    "volutpat at turpis. Fusce vitae lectus metus. Etiam at dui nisi. Aenean gravida ligula sit amet adipiscing porta. " +
                    "Fusce non accumsan dolor, sed iaculis quam. Mauris tincidunt nulla vitae eros semper pulvinar.";
            choreDescriptions.put(myButton, new ChoreInfo(title, information));
        }
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
}

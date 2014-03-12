package app.android.homeBase;


import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.graphics.Point;
import android.view.LayoutInflater;

import com.beardedhen.androidbootstrap.BootstrapButton;

public class ChoresActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chores);

        LinearLayout layout = (LinearLayout) findViewById(R.id.chores_choreContainer_button);

        /*RelativeLayout container = new RelativeLayout(this);
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        params.height = (int)(height * 0.25f);
        params.width = (int)(width * 0.50f);

        rlp.height = 50;
        rlp.width = 70;

        BootstrapButton runtimeButton = new BootstrapButton(this);
        //container.addView(runtimeButton);

        String buttonText = "test";
        runtimeButton.setText(buttonText);

        runtimeButton.setRightIcon("fa-heart");

        runtimeButton.setBootstrapType("success");

        //layout.addView(container, rlp);
        //layout.addView(runtimeButton, params);*/

        //this will eventually run through chores from parse and populate view accordingly, but this is a good framework
        //for creating bootstrap buttons programmaticaly from xml frameworks
        for (int i = 0; i < 5; i++) {
            LayoutInflater inflater = LayoutInflater.from(this);
            LinearLayout buttonCont = (LinearLayout) inflater.inflate(R.layout.chore_container, null, false);

            BootstrapButton myButton = (BootstrapButton) buttonCont.findViewById(R.id.login_testChores_button);
            buttonCont.removeView(myButton);

            layout.addView(myButton);
            String text = "Button " + i;
            myButton.setText(text);
            myButton.setRightIcon("fa-heart");
        }
    }
}

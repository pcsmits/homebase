package app.android.homeBase;


import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.view.View;

import com.beardedhen.androidbootstrap.BootstrapButton;

public class ChoreInfoActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
        setContentView(R.layout.activity_chore_info);

        Bundle extras = getIntent().getExtras();
        String title = "";
        String info = "";
        if (extras != null) {
            title = extras.getString("title");
            info = extras.getString("info");
        }

        BootstrapButton headerBar = (BootstrapButton) this.findViewById(R.id.chore_info_header_button);
        BootstrapButton body = (BootstrapButton) this.findViewById(R.id.chore_info_body_button);

        headerBar.setText(title);
        body.setText(info);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_in_back, R.anim.anim_out_back);
    }

    public void onChoreContainerClick(View view)
    {
        BootstrapButton thisButton = (BootstrapButton) view.findViewById(R.id.login_testChores_button);
        thisButton.setText("Clicked");
    }
}
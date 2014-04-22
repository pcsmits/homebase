package app.android.homeBase;


import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.beardedhen.androidbootstrap.BootstrapButton;

public class ChoreInfoActivity extends HomeBaseActivity {
    private ParseBase parse;
    private String title;
    private String info;
    private String creator;

    private List <String> responsibleUsers;
    private ArrayList<String> userNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
        setContentView(R.layout.activity_chore_info);
        parse = new ParseBase(this);

        Bundle extras = getIntent().getExtras();
        title = "";
        info = "";
        creator = "";
        if (extras != null) {
            title = extras.getString("title");
            info = extras.getString("info");
            creator = extras.getString("creator");
        }


        BootstrapButton headerBar = (BootstrapButton) this.findViewById(R.id.chore_info_header_button);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Resources resources = this.getResources();

        int navBarHeight = 0;

        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            navBarHeight = resources.getDimensionPixelSize(resourceId);
        }

        int statusBarHeight = 0;

        resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = resources.getDimensionPixelSize(resourceId);
        }

        int height = (int)((size.y - headerBar.getLayoutParams().height * 3) - navBarHeight - statusBarHeight);

        BootstrapButton infoContainer = (BootstrapButton) this.findViewById(R.id.chore_info_body_button);
        ViewGroup.LayoutParams rlp = infoContainer.getLayoutParams();
        rlp.height = height;
        infoContainer.setLayoutParams(rlp);
        BootstrapButton body = (BootstrapButton) this.findViewById(R.id.chore_info_body_button);

        BootstrapButton creatorField = (BootstrapButton) this.findViewById(R.id.chore_info_creator_field);

        userNames = ApplicationManager.getInstance().getHomeUsers();
        for (int i = 0; i < userNames.size(); i++) {
            LayoutInflater inflater = LayoutInflater.from(this);
            LinearLayout template = (LinearLayout) inflater.inflate(R.layout.user_select_template, null, false);
            RelativeLayout btnContainer = (RelativeLayout) template.findViewById(R.id.userSelection_template_container);
            template.removeView(btnContainer);
            BootstrapButton userBtn = (BootstrapButton)btnContainer.findViewById(R.id.userSelection_button);
            userBtn.setText(userNames.get(i));
            LinearLayout responsibleContainer = (LinearLayout) this.findViewById(R.id.chore_info_responsible_container);
            responsibleContainer.addView(btnContainer);
        }

        creatorField.setText(creator);
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
        BootstrapButton thisButton = (BootstrapButton) view.findViewById(R.id.login_test_button);
        thisButton.setText("Clicked");
    }

    public void onChoreInfoConfirmClick(View view)
    {
        parse.updateAlertResponsibleUsers(creator, title, responsibleUsers, responsibleUsers, this);
    }

    public void onChoreInfoCancelClick(View view)
    {
        onBackPressed();
    }
}
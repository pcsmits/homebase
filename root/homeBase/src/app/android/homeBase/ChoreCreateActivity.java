package app.android.homeBase;


import android.content.res.Resources;
import android.os.Bundle;
import android.view.ViewGroup;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;

public class ChoreCreateActivity extends HomeBaseActivity {
    public ParseBase parse;
    private BootstrapEditText headerBar;
    private BootstrapEditText infoContainer;
    private BootstrapButton ownerField;
    private final String k_alertType = "Chore";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chore_create);
        parse = new ParseBase(this);

        headerBar = (BootstrapEditText) this.findViewById(R.id.chore_create_header_field);

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

        int height = ((size.y - headerBar.getLayoutParams().height * 3) - navBarHeight - statusBarHeight);

        infoContainer = (BootstrapEditText) this.findViewById(R.id.chore_create_body_field);
        ViewGroup.LayoutParams rlp = infoContainer.getLayoutParams();
        rlp.height = height;
        infoContainer.setLayoutParams(rlp);

        //set up "responsible for" options
        for (int i = 0; i < 2; i++) {
            LayoutInflater inflater = LayoutInflater.from(this);
            LinearLayout template = (LinearLayout) inflater.inflate(R.layout.user_select_template, null, false);
            RelativeLayout btnContainer = (RelativeLayout) template.findViewById(R.id.userSelection_template_container);
            template.removeView(btnContainer);
            LinearLayout responsibleContainer = (LinearLayout) this.findViewById(R.id.chore_create_responsible_container);
            responsibleContainer.addView(btnContainer);
        }

        ownerField = (BootstrapButton) this.findViewById(R.id.chore_create_creator_field);
        ownerField.setText(parse.getCurrentUser().getUsername());
    }

    public void onChoreCreateSubmitClick(View view)
    {
        String title = headerBar.getText().toString();
        String type = k_alertType;
        String desc = infoContainer.getText().toString();
        String creator = parse.getCurrentUser().getUsername();
        String owner = "Parker";
        parse.createAlert(title,type, desc, owner, creator, ChoreCreateActivity.this);
    }

    public void onChoreCreateCancelClick(View view)
    {
        onBackPressed();
    }

    @Override
    public void onCreateAlertSuccess(HomeBaseAlert alert)
    {
        Toast.makeText(ChoreCreateActivity.this, alert.getTitle() + ": " +alert.getDescription(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreateAlertFailure(String e)
    {
        Toast.makeText(ChoreCreateActivity.this, e, Toast.LENGTH_LONG).show();
    }
}
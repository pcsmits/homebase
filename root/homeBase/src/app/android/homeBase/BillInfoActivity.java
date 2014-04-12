package app.android.homeBase;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.ViewGroup;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import com.beardedhen.androidbootstrap.BootstrapButton;

public class BillInfoActivity extends HomeBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
        setContentView(R.layout.activity_bill_info);

        Bundle extras = getIntent().getExtras();
        String title = "";
        String info = "";
        if (extras != null) {
            title = extras.getString("title");
            info = extras.getString("info");
        }

        BootstrapButton headerBar = (BootstrapButton) this.findViewById(R.id.bill_info_header_button);

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

        BootstrapButton infoContainer = (BootstrapButton) this.findViewById(R.id.bill_info_body_button);
        ViewGroup.LayoutParams rlp = infoContainer.getLayoutParams();
        rlp.height = height;
        infoContainer.setLayoutParams(rlp);
        BootstrapButton body = (BootstrapButton) this.findViewById(R.id.bill_info_body_button);

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
        BootstrapButton thisButton = (BootstrapButton) view.findViewById(R.id.bill_container_button);
        thisButton.setText("Clicked");
    }
}
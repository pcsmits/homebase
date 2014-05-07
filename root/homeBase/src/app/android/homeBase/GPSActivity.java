package app.android.homeBase;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import java.util.ArrayList;
import android.widget.Button;
import android.view.ViewGroup.LayoutParams;

import com.beardedhen.androidbootstrap.BootstrapButton;


public class GPSActivity extends HomeBaseActivity {
    private LinearLayout globalLayout;
    private boolean expand = true;
    private int menuHeight;
    private ApplicationManager mApplication;

    ArrayList<HomeBaseUser> stats = new ArrayList<HomeBaseUser>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplication = ApplicationManager.getInstance();
        myIntent = getIntent();
        myClassName = "GPSActivity";
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
        setContentView(R.layout.activity_gps);

        //get users
        mApplication.parse.getUsersOfHouse(GPSActivity.this);
    }

    @Override
    public void onGetHomeUsersSuccess(String user, Boolean home) {
        HomeBaseUser userStatus = new HomeBaseUser(user, home);
        Log.d("Found User", userStatus.username + " " + userStatus.isHome);
        stats.add(userStatus);

        if (mApplication.parse.getCurrentUser().getUsername().equals(user)){return;}

        LinearLayout layout = (LinearLayout) findViewById(R.id.GPSuser_container);

        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout buttonCont = (LinearLayout) inflater.inflate(R.layout.gps_user_template, null, false);

        BootstrapButton myButton = (BootstrapButton) buttonCont.findViewById(R.id.newsfeed_template_button);
        buttonCont.removeView(myButton);
        layout.addView(myButton);
        String text = user + " " + String.valueOf(home);
        myButton.setText(text);

    }

    @Override
    public void onGetHomeUsersFailure()
    {
        Log.d("Found User","FAILURE");
    }

    @Override
    public void onGetAlertListSuccess(ArrayList<HomeBaseAlert> alerts)
    {

    }

    @Override
    public void onReturnUsersSuccess() {
        Log.d("Found House", "yes, yes i did");
    }

    @Override
    public void onReturnUsersFailure() {
        Log.d("Found all users","FAILURE");

    }

}


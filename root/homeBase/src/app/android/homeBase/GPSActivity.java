package app.android.homeBase;


import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import java.util.ArrayList;



public class GPSActivity extends HomeBaseActivity {
    public ParseBase parse;
    private LinearLayout globalLayout;
    private boolean expand = true;
    private int menuHeight;

    ArrayList<HomeBaseUserStatus> stats = new ArrayList<HomeBaseUserStatus>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        //get users
        parse.getUsersOfHouse(GPSActivity.this);
    }

    @Override
    public void onGetHomeUsersSuccess(String user, Boolean home)
    {
        Log.d("Found User","Success");
       HomeBaseUserStatus userStatus = new HomeBaseUserStatus(user, home);
        stats.add(userStatus);
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
        Log.d("Found all users","Success");
        for (int i = 0; i < stats.size(); i++){
            Log.d(stats.get(i).username, "is user Home? " +stats.get(i).isHome.toString());
        }
    }

    @Override
    public void onReturnUsersFailure() {
        Log.d("Found all users","FAILURE");

    }

}


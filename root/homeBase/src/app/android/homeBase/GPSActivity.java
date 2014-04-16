package app.android.homeBase;


import android.os.Bundle;
import android.widget.LinearLayout;

import java.util.ArrayList;



public class GPSActivity extends HomeBaseActivity {
    public ParseBase parse;
    private LinearLayout globalLayout;
    private boolean expand = true;
    private int menuHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

    }



    @Override
    public void onGetAlertListSuccess(ArrayList<HomeBaseAlert> alerts)
    {

    }
}

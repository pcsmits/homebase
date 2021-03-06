package app.android.homeBase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.FontAwesome;
import com.beardedhen.androidbootstrap.FontAwesomeText;

public class NewsFeedActivity extends HomeBaseActivity {
    private FontAwesomeText refreshIcon;
    private BootstrapButton refreshButton;
    private ArrayList<BootstrapButton> newsFeedContainers = new ArrayList<BootstrapButton>();
    private ArrayList<HomeBaseAlert> newsFeedAlerts = new ArrayList<HomeBaseAlert>();
    private HashMap<BootstrapButton, HomeBaseAlert> buttonToAlertMap = new HashMap<BootstrapButton, HomeBaseAlert>();
    private HashMap<HomeBaseAlert, BootstrapButton> alertToButtonMap = new HashMap<HomeBaseAlert, BootstrapButton>();
    private LinearLayout feedContainerLayout;
    private LinearLayout globalLayout;
    private boolean expand = true;
    private boolean startCalled = false;
    private int menuHeight;
    private ApplicationManager mApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mApplication = ApplicationManager.getInstance();
        myIntent = getIntent();
        myClassName = "NewsFeedActivity";
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
        setContentView(R.layout.activity_newsfeed);

        refreshIcon = (FontAwesomeText)this.findViewById(R.id.newsfeed_refresh_icon);
        refreshButton = (BootstrapButton)this.findViewById(R.id.newsfeed_refresh_button);

        globalLayout = (LinearLayout)this.findViewById(R.id.newsfeed_menu_container);
        menuHeight = globalLayout.getLayoutParams().height;

        feedContainerLayout = (LinearLayout) findViewById(R.id.newsfeed_newsfeedItem_container);
        startCalled = true;
        mApplication.parse.getAlerts(NewsFeedActivity.this);

        inflateProgressBar();

        feedContainerLayout.addView(loadingProgress);
        refreshHouse();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if (startCalled) {
            startCalled = false;
            return;
        }

        closeMenu();
        mApplication.parse.refreshAlerts(this);
        refreshHouse();
    }

    private void closeMenu() {
        LinearLayout relativeLayout = globalLayout;

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) (relativeLayout.getLayoutParams());

        ResizeAnimation a = new ResizeAnimation(relativeLayout);
        a.setDuration(250);


        lp.height = menuHeight;

        expand = false;

        relativeLayout.setLayoutParams(lp);
    }

    public void onRefreshNewsfeed(View view)
    {
        refreshIcon.setIcon("fa-spinner");
        refreshIcon.startRotate(this, true, FontAwesomeText.AnimationSpeed.MEDIUM);
        mApplication.parse.refreshAlerts(this);
        refreshHouse();
        refreshButton.setBootstrapType("danger");
    }

    public void onMenuButtonClick(View view)
    {
        LinearLayout relativeLayout = globalLayout;

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) (relativeLayout.getLayoutParams());

        ResizeAnimation a = new ResizeAnimation(relativeLayout);
        a.setDuration(250);

        if (expand) {
            // Remember to set the container linear layout to height = (50)*modules
            a.setParams(lp.height, menuHeight + menuHeight * 5); //this will be times the number of modules we have
        } else {
            a.setParams(lp.height, menuHeight);
        }

        expand = !expand;

        relativeLayout.startAnimation(a);
    }

    public void onChoresButtonClick(View view)
    {
        Intent intent = new Intent(NewsFeedActivity.this, ChoresActivity.class);
        intent.putExtra("caller", myClassName);
        startActivity(intent);
    }

    public void onBillsButtonClick(View view)
    {
        Intent intent = new Intent(NewsFeedActivity.this, BillsActivity.class);
        intent.putExtra("caller", myClassName);
        startActivity(intent);
    }

    public void onUsersHomeButtonClick(View view)
    {
        Intent intent = new Intent(NewsFeedActivity.this, GPSActivity.class);
        intent.putExtra("caller", myClassName);
        startActivity(intent);
    }

    public void onSettingsButtonClick(View view)
    {
        Intent intent = new Intent(NewsFeedActivity.this, SettingsActivity.class);
        intent.putExtra("caller", myClassName);
        startActivity(intent);
    }

    public void onSuppliesButtonClick(View view)
    {
        Intent intent = new Intent(NewsFeedActivity.this, SuppliesActivity.class);
        intent.putExtra("caller", myClassName);
        startActivity(intent);
    }

    public void onNewsFeedItemClick(View view)
    {
        BootstrapButton thisButton = (BootstrapButton) view.findViewById(R.id.newsfeed_template_button);
        HomeBaseAlert correspondingAlert = buttonToAlertMap.get(thisButton);
        if (correspondingAlert.getType().equals("Chore")) {
            HandleChoreClick(correspondingAlert);
        } else if (correspondingAlert.getType().equals("Bill")) {
            HandleBillClick(correspondingAlert);
        } else {

        }
    }

    public void onChoreContainerClick(View view)
    {
        BootstrapButton thisButton = (BootstrapButton) view.findViewById(R.id.alertContainer_container);
        HomeBaseAlert correspondingAlert = buttonToAlertMap.get(thisButton);
        HandleBillClick(correspondingAlert);
    }

    private void HandleChoreClick(HomeBaseAlert clicked)
    {
        Intent intent = new Intent(NewsFeedActivity.this, ChoreInfoActivity.class);
        intent.putExtra("caller", myClassName);
        intent.putExtra("title", clicked.getTitle());
        intent.putExtra("info", clicked.getDescription());
        intent.putExtra("creator", clicked.getCreatorID());
        intent.putExtra("alertID", clicked.getId());
        startActivity(intent);
    }

    private void HandleBillClick(HomeBaseAlert clicked)
    {
        Intent intent = new Intent(NewsFeedActivity.this, BillInfoActivity.class);
        intent.putExtra("caller", myClassName);
        intent.putExtra("amount", clicked.getAmount().toString());
        intent.putExtra("creator", clicked.getCreatorID());
        intent.putExtra("title", clicked.getTitle());
        intent.putExtra("info", clicked.getDescription());
        intent.putExtra("alertID", clicked.getId());
        startActivity(intent);
    }

    @Override
    public void onGetAlertListSuccess(ArrayList<HomeBaseAlert> alerts)
    {
        feedContainerLayout.removeView(loadingProgress);

        if (alerts.size() == 0){
            LayoutInflater inflater = LayoutInflater.from(this);
            LinearLayout buttonCont = (LinearLayout) inflater.inflate(R.layout.newsfeed_item_template, null, false);

            BootstrapButton myButton = (BootstrapButton) buttonCont.findViewById(R.id.newsfeed_template_button);
            buttonCont.removeView(myButton);
            myButton.setClickable(false);
            feedContainerLayout.addView(myButton);
            myButton.setText("You have no alerts at this time.");
            myButton.setTextGravity("Center");

            BootstrapButton header = (BootstrapButton)myButton.findViewById(R.id.newsfeed_template_button_header);
            myButton.removeView(header);
        } else {
            for (int i = 0; i < alerts.size(); i++) {
                newsFeedAlerts.add(alerts.get(i));

                LayoutInflater inflater = LayoutInflater.from(this);
                if (alerts.get(i).getType().equals("Bill")) {
                    LinearLayout buttonCont = (LinearLayout) inflater.inflate(R.layout.bill_container, null, false);

                    BootstrapButton myButton = (BootstrapButton) buttonCont.findViewById(R.id.alertContainer_container);
                    BootstrapButton header = (BootstrapButton) buttonCont.findViewById(R.id.alertContainer_header);
                    BootstrapButton moneyField = (BootstrapButton) buttonCont.findViewById(R.id.alertContainer_money);

                    newsFeedContainers.add(myButton);
                    alertToButtonMap.put(alerts.get(i), myButton);
                    buttonToAlertMap.put(myButton, alerts.get(i));

                    buttonCont.removeView(myButton);
                    feedContainerLayout.addView(myButton);
                    String text = alerts.get(i).getDescription();

                    myButton.fitLine(1);
                    myButton.setText(text);

                    header.setText(alerts.get(i).getTitle());
                    moneyField.setText("$" + alerts.get(i).getAmount());
                    moneyField.setBootstrapType("bill");
                    header.setBootstrapType("bill");
                } else {
                    LinearLayout buttonCont = (LinearLayout) inflater.inflate(R.layout.newsfeed_item_template, null, false);

                    BootstrapButton myButton = (BootstrapButton) buttonCont.findViewById(R.id.newsfeed_template_button);

                    newsFeedContainers.add(myButton);
                    alertToButtonMap.put(alerts.get(i), myButton);
                    buttonToAlertMap.put(myButton, alerts.get(i));

                    buttonCont.removeView(myButton);
                    feedContainerLayout.addView(myButton);
                    String text = alerts.get(i).getDescription();

                    myButton.fitLine(1);
                    myButton.setText(text);
                    BootstrapButton header = (BootstrapButton)myButton.findViewById(R.id.newsfeed_template_button_header);
                    header.setText(alerts.get(i).getTitle());

                    if (alerts.get(i).getType().equals("Supply")) {
                        header.setBootstrapType("supply");
                    }
                }
            }
        }
    }

    @Override
    public void onUpdateAlertListSuccess(ArrayList<HomeBaseAlert> alerts)
    {
        refreshIcon.setIcon("fa-refresh");
        refreshIcon.stopAnimation();
        refreshButton.setBootstrapType("success");

        feedContainerLayout.removeAllViews();
        if (alerts.size() == 0){
            LayoutInflater inflater = LayoutInflater.from(this);
            LinearLayout buttonCont = (LinearLayout) inflater.inflate(R.layout.newsfeed_item_template, null, false);

            BootstrapButton myButton = (BootstrapButton) buttonCont.findViewById(R.id.newsfeed_template_button);
            buttonCont.removeView(myButton);
            myButton.setClickable(false);
            feedContainerLayout.addView(myButton);

            myButton.setText("You have no alerts at this time.");
            myButton.setTextGravity("Center");

            BootstrapButton header = (BootstrapButton)myButton.findViewById(R.id.newsfeed_template_button_header);
            myButton.removeView(header);
        } else {
            for (int i = 0; i < alerts.size(); i++) {
                newsFeedAlerts.add(alerts.get(i));

                LayoutInflater inflater = LayoutInflater.from(this);

                if (alerts.get(i).getType().equals("Bill")) {
                    LinearLayout buttonCont = (LinearLayout) inflater.inflate(R.layout.bill_container, null, false);

                    BootstrapButton myButton = (BootstrapButton) buttonCont.findViewById(R.id.alertContainer_container);
                    BootstrapButton header = (BootstrapButton) buttonCont.findViewById(R.id.alertContainer_header);
                    BootstrapButton moneyField = (BootstrapButton) buttonCont.findViewById(R.id.alertContainer_money);

                    newsFeedContainers.add(myButton);
                    alertToButtonMap.put(alerts.get(i), myButton);
                    buttonToAlertMap.put(myButton, alerts.get(i));

                    buttonCont.removeView(myButton);
                    feedContainerLayout.addView(myButton);
                    String text = alerts.get(i).getDescription();

                    myButton.fitLine(1);
                    myButton.setText(text);

                    header.setText(alerts.get(i).getTitle());
                    moneyField.setText("$" + alerts.get(i).getAmount());
                    moneyField.setBootstrapType("bill");
                    header.setBootstrapType("bill");
                } else {
                    LinearLayout buttonCont = (LinearLayout) inflater.inflate(R.layout.newsfeed_item_template, null, false);

                    BootstrapButton myButton = (BootstrapButton) buttonCont.findViewById(R.id.newsfeed_template_button);

                    newsFeedContainers.add(myButton);
                    alertToButtonMap.put(alerts.get(i), myButton);
                    buttonToAlertMap.put(myButton, alerts.get(i));

                    buttonCont.removeView(myButton);
                    feedContainerLayout.addView(myButton);
                    String text = alerts.get(i).getDescription();

                    myButton.fitLine(1);
                    myButton.setText(text);
                    BootstrapButton header = (BootstrapButton)myButton.findViewById(R.id.newsfeed_template_button_header);
                    header.setText(alerts.get(i).getTitle());

                    if (alerts.get(i).getType().equals("Supply")) {
                        header.setBootstrapType("supply");
                    }
                }
            }
        }
    }

    @Override
    public void onUpdateAlertListFailure(String e)
    {

    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressedEndpoint();
    }

    void refreshHouse()
    {
        if (mApplication.hasHouse()) {
            mApplication.parse.getHouse(mApplication.getHouse(), NewsFeedActivity.this);
        }

        if (mApplication.parse.getCurrentUser() != null) {
            mApplication.parse.RefreshCurrentUser();
        }
    }

    @Override
    public void onGetHouseSuccess(HomeBaseHouse house)
    {
        mApplication.setHouse(house);
    }
}

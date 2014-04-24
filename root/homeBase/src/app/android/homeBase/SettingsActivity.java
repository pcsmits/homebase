package app.android.homeBase;

import android.os.Bundle;
import android.view.View;

import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.parse.ParseUser;

public class SettingsActivity extends HomeBaseActivity {
    private ParseBase parse;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parse = new ParseBase(this);
        ParseUser currUser = parse.getCurrentUser();
        setContentView(R.layout.activity_settings);
        BootstrapEditText username = (BootstrapEditText) findViewById(R.id.settings_username_et);
        BootstrapEditText email = (BootstrapEditText) findViewById(R.id.settings_email_et);
        username.setText(currUser.getUsername());
        email.setText(currUser.getEmail());
    }


    public void onLogoutClick(View v)
    {

    }

    public void onDeleteAccountClick(View v)
    {

    }
}
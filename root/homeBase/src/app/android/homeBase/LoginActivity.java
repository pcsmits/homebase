package app.android.homeBase;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.parse.Parse;
import com.parse.ParseUser;

/**
 * This activity is the default launcher activity for the app
 * If there is a cached user session it will simple hand off the instance to the feed activity
 * Otherwise it presents the nessecary login features and has a button leading to signup
 *
 * TODO handle a logged in user who has none or multiple houses
 */
public class LoginActivity extends HomeBaseActivity{
    private parseBase parse;
    private Animation animTranslate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parse = new parseBase(this);
        setContentView(R.layout.activity_login);
        // Intenet to feed activity here
        if(parse.userLoggedIn())
        {
            //TODO ADD INTENET HERE
            Toast.makeText(this, "User logged in already!", Toast.LENGTH_LONG).show();
        }
        animTranslate = AnimationUtils.loadAnimation(this, R.anim.anim_translate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * on click method for the login button
     * Gets the entered information and calls the loginUser method in parseBase class
     * @param v
     */
    public void loginOnClick(View v)
    {
        BootstrapEditText usernameText= (BootstrapEditText) findViewById(R.id.login_username_etext);
        BootstrapEditText passwordText = (BootstrapEditText) findViewById(R.id.login_password_etext);

        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();

        if(!username.isEmpty() && !password.isEmpty())
        {
            parse.loginUser(username, password, v.getContext(), this);
        }
        else
        {
            Toast.makeText(v.getContext(), "Please enter your username and password", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void OnParseSuccess() {
        Intent intent = new Intent(LoginActivity.this, ChoresActivity.class);
        startActivity(intent);
    }

    /**
     * Simple onclick method for the signup button
     * Just starts the signUp activity
     * @param view
     */
    public void onSignUpClick(View view)
    {
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
    }

    public void onTestChoresClick(View view)
    {
        //animation test
        //view.startAnimation(animTranslate);
        Intent intent = new Intent(LoginActivity.this, ChoresActivity.class);
        startActivity(intent);
    }

    @Override
    public void homeBaseCallbackAction()
    {
        Intent intent = new Intent(LoginActivity.this, ChoresActivity.class);
        startActivity(intent);
    }
}
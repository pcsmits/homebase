package app.android.homeBase;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.BootstrapThumbnail;
import com.parse.ParseUser;

/**
 * This activity class handles the signup "flow" for the application
 * It is started (usually) from the login activity. It's main purpose is to validate the user input
 * entered into the edittext boxes. It does this using the Listeners made within onCreate.
 */
public class SignupActivity extends ActionBarActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final parseSignUp parse = new parseSignUp(this);
        setContentView(R.layout.activity_signup);

        //Get references to the three edit texts
        final BootstrapEditText usernameEditText = (BootstrapEditText) findViewById(R.id.signup_username_etext);
        final BootstrapEditText passwordEditText = (BootstrapEditText) findViewById(R.id.signup_password_etext);
        final BootstrapEditText passwordVerEditText = (BootstrapEditText) findViewById(R.id.signup_passwordV_etext);

        //Username listener, detects when focus for the edit text changes from having to not having
        //TODO maybe change to when done button pressed listener
        usernameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                // When the edittext loses focus
                if (!hasFocus) {
                    String username = usernameEditText.getText().toString();
                    // If a username was submitted, verify that username isnt already in use on parse
                    if (!username.isEmpty()) {
                        //TODO have checkusername return a boolean and handle the checkboxes out here
                        parse.checkUserName(username, view.getRootView());
                    }
                }
            }
        });

        // Same idea for password, but now jsut check for a reasonable size password
        passwordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    CheckBox passWordCheckBox = (CheckBox) findViewById(R.id.password_checkbox);
                    String password = passwordEditText.getText().toString();
                    // If the password is valid the checkbox gets checked
                    if (password.length() < 5) {
                        Toast.makeText(view.getContext(), "Please eneter a password of at least 6 characters", Toast.LENGTH_LONG).show();
                        passWordCheckBox.setChecked(false);
                    } else {
                        passWordCheckBox.setChecked(true);
                    }
                }
            }
        });

        // Again, just make sure it matches the first password box
        passwordVerEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    String passwordVer = passwordVerEditText.getText().toString();
                    String password = passwordEditText.getText().toString();
                    CheckBox passWordVerCheckBox = (CheckBox) findViewById(R.id.passwordVer_checkbox);
                    if (password.equals(passwordVer)) {
                        passWordVerCheckBox.setChecked(true);
                    } else {
                        Toast.makeText(view.getContext(), "Passwords do not match", Toast.LENGTH_LONG).show();
                        passWordVerCheckBox.setChecked(false);
                    }
                }
            }
        });

        //Set up listener for the signUp Botton
        BootstrapButton signupButton = (BootstrapButton) findViewById(R.id.signup_signup_button);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ref all three checkboxes make sure they are checked
                // TODO maybe just check the actual values!
                CheckBox usernameCheckBox = (CheckBox) findViewById(R.id.username_checkbox);
                CheckBox passWordCheckBox = (CheckBox) findViewById(R.id.password_checkbox);
                CheckBox passWordVerCheckBox = (CheckBox) findViewById(R.id.passwordVer_checkbox);

                if (usernameCheckBox.isChecked() && passWordCheckBox.isChecked() && passWordVerCheckBox.isChecked()) {
                    // Fire up a nifty spinner to indicate the user is being signed up
                    ProgressBar progressSpinner = (ProgressBar) findViewById(R.id.signup_progressbar);
                    progressSpinner.setVisibility(View.VISIBLE);
                    progressSpinner.setIndeterminate(true);
                    // addUser will handle the intent firing upon succsessful signup
                    // TODO handle the error case here so the spinner gets shut down
                    parse.addUser(usernameEditText.getText().toString(), passwordEditText.getText().toString(), view.getContext());
                }

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.signup, menu);
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
}
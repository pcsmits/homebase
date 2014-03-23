package app.android.homeBase;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * This activity class handles the signup "flow" for the application
 * It is started (usually) from the login activity. It's main purpose is to validate the user input
 * entered into the edittext boxes. It does this using the Listeners made within onCreate.
 */
public class SignupActivity extends HomeBaseActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ParseBase parse = new ParseBase(this);
        setContentView(R.layout.activity_signup);

        //Get references to the three edit texts
        final BootstrapEditText usernameEditText = (BootstrapEditText) findViewById(R.id.signup_username_etext);
        final BootstrapEditText passwordEditText = (BootstrapEditText) findViewById(R.id.signup_password_etext);
        final BootstrapEditText passwordVerEditText = (BootstrapEditText) findViewById(R.id.signup_passwordV_etext);

        usernameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String username = usernameEditText.getText().toString();
                    if (!username.isEmpty()) {
                        parse.checkUserName(username, SignupActivity.this);
                    }
                }
                return false;
            }
        });

        usernameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                // When the edittext loses focus
                if (!hasFocus) {
                    String username = usernameEditText.getText().toString();
                    // If a username was submitted, verify that username isnt already in use on parse
                    if (!username.isEmpty()) {
                        parse.checkUserName(username, SignupActivity.this);
                    }
                }
            }
        });

        // Same idea for password, but now jsut check for a reasonable size password
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE)
                {
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
                return false;
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
        passwordVerEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE) {
                    String passwordVer = passwordVerEditText.getText().toString();
                    String password = passwordEditText.getText().toString();
                    CheckBox passWordVerCheckBox = (CheckBox) findViewById(R.id.passwordVer_checkbox);
                    CheckBox passWordCheckBox = (CheckBox) findViewById(R.id.password_checkbox);
                    if (password.equals(passwordVer) && passWordCheckBox.isChecked()) {
                        passWordVerCheckBox.setChecked(true);
                    } else {
                        Toast.makeText(view.getContext(), "Passwords do not match", Toast.LENGTH_LONG).show();
                        passWordVerCheckBox.setChecked(false);
                    }
                }
                return false;
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
                    CheckBox passWordCheckBox = (CheckBox) findViewById(R.id.password_checkbox);
                    if (password.equals(passwordVer) && passWordCheckBox.isChecked()) {
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
                CheckBox usernameCheckBox = (CheckBox) findViewById(R.id.username_checkbox);
                CheckBox passWordCheckBox = (CheckBox) findViewById(R.id.password_checkbox);
                CheckBox passWordVerCheckBox = (CheckBox) findViewById(R.id.passwordVer_checkbox);

                if (usernameCheckBox.isChecked() && passWordCheckBox.isChecked() && passWordVerCheckBox.isChecked()) {
                    // Fire up a nifty spinner to indicate the user is being signed up
                    ProgressBar progressSpinner = (ProgressBar) findViewById(R.id.signup_progressbar);
                    progressSpinner.setVisibility(View.VISIBLE);
                    progressSpinner.setIndeterminate(true);

                    // addUser will handle the intent firing upon succsessful signup
                    parse.addUser(usernameEditText.getText().toString(), passwordEditText.getText().toString(), SignupActivity.this);
                }

            }
        });
    }

    @Override
    public void onSignupSuccess(ParseUser user)
    {
        Intent newHouse = new Intent(SignupActivity.this, NewHouseActivity.class);
        startActivity(newHouse);
    }

    @Override
    public void onSignupError(ParseException e)
    {
        ProgressBar progressSpinner = (ProgressBar) findViewById(R.id.signup_progressbar);
        progressSpinner.setIndeterminate(false);
        progressSpinner.setVisibility(View.INVISIBLE);
        Toast.makeText(SignupActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCheckUserSuccess()
    {
        CheckBox usernameCheckBox = (CheckBox) findViewById(R.id.username_checkbox);
        usernameCheckBox.setChecked(true);
    }

    @Override
    public void onCheckUserFailure()
    {
        CheckBox usernameCheckBox = (CheckBox) findViewById(R.id.username_checkbox);
        usernameCheckBox.setChecked(false);
        Toast.makeText(SignupActivity.this, "This username is taken!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCheckUserError(ParseException e)
    {
        CheckBox usernameCheckBox = (CheckBox) findViewById(R.id.username_checkbox);
        usernameCheckBox.setChecked(false);
        Toast.makeText(SignupActivity.this, "Error occured: "+e.getMessage().toString(), Toast.LENGTH_LONG).show();
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
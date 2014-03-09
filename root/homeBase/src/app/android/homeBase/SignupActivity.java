package app.android.homeBase;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.BootstrapThumbnail;
import com.parse.ParseUser;

public class SignupActivity extends ActionBarActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final parseSignUp parse = new parseSignUp(this);
        setContentView(R.layout.activity_signup);

        //Set up listener for the three edit texts
        final BootstrapEditText usernameEditText = (BootstrapEditText) findViewById(R.id.signup_username_etext);
        final BootstrapEditText passwordEditText = (BootstrapEditText) findViewById(R.id.signup_password_etext);
        final BootstrapEditText passwordVerEditText = (BootstrapEditText) findViewById(R.id.signup_passwordV_etext);

        usernameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    String username = usernameEditText.getText().toString();
                    if (!username.isEmpty()) {
                        parse.checkUserName(username, view.getRootView());
                    }
                }
            }
        });

        passwordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    CheckBox passWordCheckBox = (CheckBox) findViewById(R.id.password_checkbox);
                    String password = passwordEditText.getText().toString();
                    if (password.length() < 5) {
                        Toast.makeText(view.getContext(), "Please eneter a password of at least 6 characters", Toast.LENGTH_LONG).show();
                        passWordCheckBox.setChecked(false);
                    } else {

                        passWordCheckBox.setChecked(true);
                    }
                }
            }
        });

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
                CheckBox usernameCheckBox = (CheckBox) findViewById(R.id.username_checkbox);
                CheckBox passWordCheckBox = (CheckBox) findViewById(R.id.password_checkbox);
                CheckBox passWordVerCheckBox = (CheckBox) findViewById(R.id.passwordVer_checkbox);

                if (usernameCheckBox.isChecked() && passWordCheckBox.isChecked() && passWordVerCheckBox.isChecked()) {
                    ProgressBar progressSpinner = (ProgressBar) findViewById(R.id.signup_progressbar);
                    progressSpinner.setVisibility(View.VISIBLE);
                    progressSpinner.setIndeterminate(true);
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

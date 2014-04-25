package app.android.homeBase;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingsActivity extends HomeBaseActivity {
    private ParseBase parse;
    private BootstrapEditText usernameEditText;
    private BootstrapEditText emailEditText;
    private String oldEmail;
    private String oldUsername;
    private ParseUser currUser;
    private String newUsername;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parse = new ParseBase(this);
        currUser = parse.getCurrentUser();
        setContentView(R.layout.activity_settings);

        usernameEditText = (BootstrapEditText) findViewById(R.id.settings_username_et);
        emailEditText = (BootstrapEditText) findViewById(R.id.settings_email_et);

        oldEmail = currUser.getEmail();
        oldUsername = currUser.getUsername();

        usernameEditText.setText(oldUsername);
        emailEditText.setText(oldEmail);

    }

    public void onChangeEmailClick(View v)
    {
        final String newEmail;
        try{
            newEmail = emailEditText.getText().toString();
            EmailValidator emailVal = new EmailValidator();
            if(!newEmail.equalsIgnoreCase(oldEmail))
            {
                if(emailVal.validate(newEmail))
                {
                    currUser.setEmail(newEmail);
                    currUser.saveInBackground(new SaveCallback()
                    {
                        @Override
                        public void done(ParseException e) {
                            Toast.makeText(SettingsActivity.this, "Email updated successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(SettingsActivity.this, "Please enter a valid email!", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(SettingsActivity.this, "New email matches old email!", Toast.LENGTH_LONG).show();
            }
        } catch (NullPointerException e) {
            //Do something useful
        }
    }

    public void onChangeUsernameClick(View v)
    {
        try{
            newUsername = usernameEditText.getText().toString();
            if(!newUsername.equals(oldUsername))
            {
                parse.checkUserName(newUsername, SettingsActivity.this);
            } else {
                Toast.makeText(SettingsActivity.this, "New username matches old username", Toast.LENGTH_LONG).show();
            }
        } catch (NullPointerException e){
            //Maybe empty edit texts should be empty string
            // not null.... just a thought
        }

    }

    @Override
    public void onCheckUserSuccess()
    {
        currUser.setUsername(newUsername);
        currUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Toast.makeText(SettingsActivity.this, "Username updated successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void onLogoutClick(View v)
    {
        ParseUser.logOut();
        Intent loginIntent = new Intent(SettingsActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    public void onDeleteAccountClick(View v)
    {
        new AlertDialog.Builder(this)
                .setTitle("Delete Accout")
                .setMessage("Are you sure you want to delete your account?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        currUser.deleteInBackground(new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e == null)
                                {
                                    Toast.makeText(SettingsActivity.this, "Account Deleted :(", Toast.LENGTH_LONG).show();
                                    Intent loginIntent = new Intent(SettingsActivity.this, LoginActivity.class);
                                    startActivity(loginIntent);
                                    finish();
                                } else {
                                    Toast.makeText(SettingsActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    // I hate you java, I hate you so much
    class EmailValidator {

        private Pattern pattern;
        private Matcher matcher;

        private static final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        public EmailValidator() {
            pattern = Pattern.compile(EMAIL_PATTERN);
        }

        public boolean validate(final String hex)
        {
            matcher = pattern.matcher(hex);
            return matcher.matches();
        }
    }
}
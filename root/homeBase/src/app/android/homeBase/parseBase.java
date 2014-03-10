package app.android.homeBase;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.parse.*;
import java.util.List;

/**
 * A custom class/wrapper for interacting with Parse
 * note that Parse.init should be called in every activity (I think) so every activity should make
 * a parse base in its onCreate()
 */
public class parseBase
{
    // Default constructor
    public  parseBase()
    {

    }

    // This is the constructor which calls the parse init
    // TODO figue out if this should be a singleton or not
    public parseBase(Context context)
    {
        //TODO maybe store these keys in xml
        Parse.initialize(context, "dD0N7G0DiCBySn8gXbYtcOxfvM8OGKUZOBRPy8wl", "tt6FH3ugfJOhYY41bCiPb7URHrnzQtV8drwEKQDJ");
    }

    // Simple wrapper for parse method
    // should move to parseUser subclass if one is ever made
    public boolean userLoggedIn()
    {
        ParseUser curUser = ParseUser.getCurrentUser();
        return (curUser != null);
    }

    /**
     * Method to start a user session via parse
     * Should be set up for boolean return values instead of context sensetive stuff like
     * Toasting and firing intenets
     *
     * @param username
     * @param password
     * @param context
     */
    public void loginUser(final String username, final  String password, final Context context)
    {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                // Make sure we get a valid user back!
                if(e != null || parseUser == null)
                {
                    Toast.makeText(context, "Login error occured: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //TODO make intenet, stash parseUser
                    Toast.makeText(context, "login worked!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}

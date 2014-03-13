package app.android.homeBase;

import android.content.Context;
import android.util.Log;
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
public class ParseBase
{
    // Default constructor
    public ParseBase()
    {

    }

    // This is the constructor which calls the parse init
    // TODO figue out if this should be a singleton or not
    public ParseBase(Context context)
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

    public ParseUser getCurrentUser(){
        return ParseUser.getCurrentUser();
    }

    public ParseObject getHouse(){

        return null;
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
    public void loginUser(final String username, final  String password, final Context context, final HomeBaseActivity caller)
    {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                // Make sure we get a valid user back!
                if(e != null || parseUser == null)
                {
                    onLoginError(caller, context, parseUser, e);
                }
                else
                {
                    onLoginSuccess(caller, context, parseUser, e);
                }
            }
        });

    }

    public void updateLocation(Double Lat, Double Long) {
        //getCurrentUser().put("lat", Lat);

        ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
        userQuery.whereEqualTo("username", "new");
        userQuery.findInBackground(new FindCallback<ParseUser>() {
            Double Lat;
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e)
            {

                if (e == null) {
                    if (parseUsers.isEmpty()) {
                        Log.d("GPS service", "user list null");
                    }
                    parseUsers.get(0).put("lat", Lat);
                } else {
                    Log.d("GPS service", "parse exception");
                }
            }
        });

    }

    private void onLoginError(HomeBaseActivity caller, Context context, ParseUser parseUser, ParseException e) {
        Toast.makeText(context, "Login error occured: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
        caller.onLoginError();
    }

    private void onLoginSuccess(HomeBaseActivity caller, Context context, ParseUser parseUser, ParseException e) {
        Toast.makeText(context, "login worked!", Toast.LENGTH_LONG).show();
        caller.onLoginSuccess();
    }

    public void createHouse(String housename, String address, int zipcode, final HomeBaseActivity caller)
    {
        final House newHouse = new House(housename, address, zipcode);
        ParseObject house = new ParseObject("House");
        house.put("houseName", newHouse.getHousename());
        house.put("address", newHouse.getAddress());
        house.put("zipcode", newHouse.getZipCode());
        house.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null)
                {
                    caller.onSaveSuccess(newHouse);
                }
                else
                {
                    caller.onSaveError(e.getMessage());
                }
            }
        });
    }

    /**
     * Method for adding a newUser into the parse "DB"
     * @param username - preverifed as not in use in parse already
     * @param password
     * @param context
     *
     * Doesn't return anything at the moment, it toasts/fires intents as nessecary
     * TODO make it not do all that
     */
    public void addUser(final String username, final String password, final Context context)
    {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.signUpInBackground(new SignUpCallback()
        {
            @Override
            public void done(ParseException e)
            {
                if(e == null)
                {
                    //TODO return true
                    Toast.makeText(context, "SUCCSESS REPALCE WITH INTENT", Toast.LENGTH_LONG).show();
                }
                else
                {
                    //TODO return false
                    Toast.makeText(context, "Sign up failed: "+e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * This is the method for checking that a username is not already in use on parse
     * @param username
     * @param view
     *
     * This method is should only be called from the signup activity
     * It toasts on failure, which is fine, but it is currently modifying the checkbox
     * within the view :( sorry
     */
    public void checkUserName(final String username, final View view)
    {
        ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
        userQuery.whereEqualTo("username", username);
        userQuery.findInBackground(new FindCallback<ParseUser>()
        {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e)
            {
                if (e == null)
                {
                    // Make sure we didn't have errors and we got an empty list back
                    if (parseUsers.isEmpty())
                    {
                        // TODO I think this is bad...
                        CheckBox userBox = (CheckBox) view.findViewById(R.id.username_checkbox);
                        userBox.setChecked(true);
                    }
                    else
                    {
                        Toast.makeText(view.getContext(), "That username is already in use", Toast.LENGTH_LONG).show();
                    }

                }
                else
                {
                    Toast.makeText(view.getContext(), "Parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

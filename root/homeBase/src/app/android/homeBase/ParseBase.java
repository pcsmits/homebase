package app.android.homeBase;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.parse.*;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A custom class/wrapper for interacting with Parse
 * note that Parse.init should be called in every activity (I think) so every activity should make
 * a parse base in its onCreate()
 */
public class ParseBase
{
    // Default constructor
    public  ParseBase()
    {

    }

    // This is the constructor which calls the parse init
    // TODO figue out if this should be a singleton or not
    public ParseBase(Context context)
    {
        //TODO maybe store these keys in xml
        Parse.initialize(context, "dD0N7G0DiCBySn8gXbYtcOxfvM8OGKUZOBRPy8wl", "tt6FH3ugfJOhYY41bCiPb7URHrnzQtV8drwEKQDJ");
    }



    /********************************************************************************************************
     *   USER BASED METHODS AND WRAPPERS
     **********************************************************************************************************/
    public boolean userLoggedIn()
    {
        ParseUser curUser = ParseUser.getCurrentUser();
        return (curUser != null);
    }

    public ParseUser getCurrentUser(){
        return ParseUser.getCurrentUser();
    }

    public void addUser(final String username, final String password, final HomeBaseActivity caller)
    {
        final ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.signUpInBackground(new SignUpCallback()
        {
            @Override
            public void done(ParseException e)
            {
                if(e == null)
                {
                    caller.onSignupSuccess(user);
                }
                else
                {
                    caller.onSignupError(e);
                }
            }
        });
    }

    /**
     * This is the method for checking that a username is not already in use on parse
     * @param username
     * @param caller
     *
     */
    public void checkUserName(final String username, final HomeBaseActivity caller)
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
                        caller.onCheckUserSuccess();
                    }
                    else
                    {
                        caller.onCheckUserFailure();
                    }

                }
                else
                {
                    caller.onCheckUserError(e);
                }
            }
        });
    }

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

    private void onLoginError(HomeBaseActivity caller, Context context, ParseUser parseUser, ParseException e) {
        Toast.makeText(context, "Login error occured: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
        caller.onLoginError();
    }

    private void onLoginSuccess(HomeBaseActivity caller, Context context, ParseUser parseUser, ParseException e) {
        //Toast.makeText(context, "login worked!", Toast.LENGTH_LONG).show();
        caller.onLoginSuccess();
    }


    /******************************************************************************************************
     *
     * GPS Events and Wrappersbfor Parse
     ********************************************************************************************************/

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

    /**************************************************************************************************************
     *  House Event and Wrappers for PARSE
     *************************************************************************************************************/

    public void createHouse(String housename, String address, String city, String state, int zipcode, final HomeBaseActivity caller)
    {
        final House newHouse = new House(housename, address, city, state, zipcode);
        final ParseObject house = new ParseObject("House");
        house.put("housename", newHouse.getHousename());
        house.put("address", newHouse.getAddress());
        house.put("city", newHouse.getCity());
        house.put("state", newHouse.getState());
        house.put("zipcode", newHouse.getZipCode());
        house.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null)
                {
                    newHouse.setId(house.getObjectId());
                    caller.onSaveSuccess(newHouse);
                }
                else
                {
                    caller.onSaveError(e.getMessage());
                }
            }
        });
    }

    public ParseObject getHouse(){

        return null;
    }

    /***********************************************************************************************
     *  ALERT METHODS
     **********************************************************************************************/
    public void createAlert(String type, String description, final HomeBaseActivity caller)
    {
        JSONArray seen = new JSONArray();
        seen.put("none");
        final ParseObject alert = new ParseObject("Alert");
        alert.put("type", type);
        alert.put("description", description);
        alert.put("seen", seen);
        alert.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                List<String> seenList = convertJSON(alert.getJSONArray("seen"));

                if(seenList == null)
                {
                    caller.onSaveError("Error occured parsing from parse");
                }
                else if(e == null)
                {
                    HomeBaseAlert hbAlert = new HomeBaseAlert(alert.getObjectId(), alert.getString("type"), seenList, alert.getString("description"));
                    caller.onSaveSuccess(hbAlert);
                }
                else
                {
                    caller.onSaveError(e.getMessage());
                }
            }
        });
    }

    public List<String> convertJSON(JSONArray array)
    {
        List<String> result = new ArrayList<String>();
        for(int i = 0; i < array.length(); i++)
        {
            try {
                Object seen = array.get(i);
                result.add(seen.toString());
                return result;
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

    public HomeBaseAlert buildAlert(ParseObject alert)
    {
        String objectID = alert.getObjectId();
        String type = alert.getString("type");
        String description = alert.getString("description");
        //TODO
    }

    public void getAlert(String objectID, final HomeBaseActivity caller)
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Alert");
        query.whereEqualTo("objectId", objectID);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if(e == null)
                {

                }
            }
        });
    }

    public void getUserAlerts()
    {

    }

    public void updateAlert()
    {

    }

    public void deleteAlert()
    {

    }

}

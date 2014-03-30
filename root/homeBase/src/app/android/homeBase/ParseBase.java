package app.android.homeBase;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;
import android.location.*;

import com.parse.*;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * A custom class/wrapper for interacting with Parse
 * note that Parse.init should be called in every activity (I think) so every activity should make
 * a parse base in its onCreate()
 */
public class ParseBase
{
    // Default constructor
    public  ParseBase() { }

    // This is the constructor which calls the parse init
    // TODO figue out if this should be a singleton or not
    public ParseBase(Context context)
    {
        //TODO maybe store these keys in xml
        Parse.initialize(context, "dD0N7G0DiCBySn8gXbYtcOxfvM8OGKUZOBRPy8wl", "tt6FH3ugfJOhYY41bCiPb7URHrnzQtV8drwEKQDJ");
    }

    /********************************************************************************************************
     *   GENERAL AND/OR PRIVATE METHODS
     **********************************************************************************************************/
    private List<String> convertJSON(JSONArray array)
    {
        List<String> list = new LinkedList<String>();
        for(int i = 0; i < array.length(); i++)
        {
            try {
                Object seenValue = array.get(i);
                list.add(seenValue.toString());
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return list;
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
        getCurrentUser().put("lat", Lat);

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

    public void createHouse(String housename, String address, String city, String state, int zipcode, String userid double lat, double lng, final HomeBaseActivity caller)
    {
        // Add the creator to the user list, also get added as admin in constructor
        List<String> memberList = new LinkedList<String>();
        memberList.add(userid);

        final House newHouse = new House(housename, address, city, state, zipcode, memberList, lat, lng);
        final ParseObject house = new ParseObject("House");

        house.put("admin", newHouse.getAdmin());
        house.put("members", newHouse.getMembers());
        house.put("housename", newHouse.getHousename());
        house.put("address", newHouse.getAddress());
        house.put("city", newHouse.getCity());
        house.put("state", newHouse.getState());
        house.put("zipcode", newHouse.getZipCode());
        house.put("latitude", newHouse.getLatitude());
        house.put("longitude", newHouse.getLongitude());

        house.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    newHouse.setId(house.getObjectId());
                    caller.onSaveSuccess(newHouse);
                } else {
                    caller.onSaveError(e.getMessage());
                    caller.onCreateHouseSuccess(newHouse);
                }
                else
                {
                    caller.onCreateHouseFailure("Could not create house, please try again.");
                }
            }
        });
    }

    // Get via admin username
    public void getHouse(final String adminUsername, final HomeBaseActivity caller)
    {
        ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
        userQuery.whereEqualTo("username", adminUsername);
        userQuery.getFirstInBackground(new GetCallback<ParseUser>()
        {
            @Override
            public void done(ParseUser parseUser, ParseException e)
            {
                if (e == null)
                {
                    ParseQuery<ParseObject> houseQuery = ParseQuery.getQuery("House");
                    houseQuery.whereEqualTo("admin", parseUser.getObjectId());
                    houseQuery.getFirstInBackground(new GetCallback<ParseObject>()
                    {
                        @Override
                        public void done(ParseObject parseHouse, ParseException e)
                        {
                            if (e == null)
                            {
                                String housename = parseHouse.getString("housename");
                                String address = parseHouse.getString("address");
                                String city = parseHouse.getString("city");
                                String state = parseHouse.getString("state");
                                int zipcode = parseHouse.getInt("zipcode");
                                int lat = parseHouse.getInt("latitude");
                                int longitude = parseHouse.getInt("longitude");
                                String admin = parseHouse.getString("admin");
                                List<String> members = convertJSON(parseHouse.getJSONArray("members"));
                                String id = parseHouse.getObjectId();

                                // Create House instance
                                House house = new House(housename, address, city, state, admin, members, zipcode);
                                house.setLatitude(lat);
                                house.setLongitude(longitude);
                                house.setId(id);

                                caller.onGetHouseSuccess(house);
                            }
                            else
                            {
                                caller.onGetHouseFailure("Could not fetch house information, please try again");
                            }
                        }
                    });
                }
                else
                {
                    caller.onGetHouseFailure("Could not find a house associated with admin: "+adminUsername);
                }
            }
        });
    }

    // Get via application model
    public void getHouse(House house, final HomeBaseActivity caller)
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("House");
        query.whereEqualTo("objectId", house.getId());
        query.getFirstInBackground(new GetCallback<ParseObject>()
        {
            @Override
            public void done(ParseObject parseHouse, ParseException e) {
                if (e == null)
                {
                    String housename = parseHouse.getString("housename");
                    String address = parseHouse.getString("address");
                    String city = parseHouse.getString("city");
                    String state = parseHouse.getString("state");
                    int zipcode  = parseHouse.getInt("zipcode");
                    int lat = parseHouse.getInt("latitude");
                    int longitude = parseHouse.getInt("longitude");
                    String admin = parseHouse.getString("admin");
                    List<String> members = convertJSON(parseHouse.getJSONArray("members"));
                    String id = parseHouse.getObjectId();

                    // Create House instance
                    House house = new House(housename, address, city, state, admin, members, zipcode);
                    house.setLatitude(lat);
                    house.setLongitude(longitude);
                    house.setId(id);

                    caller.onGetHouseSuccess(house);
                }
                else
                {
                    caller.onGetHouseFailure("Could not fetch house information, please try again");
                }
            }
        });
    }

    // General Update
    public void updateHouse(final House house, final HomeBaseActivity caller)
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("House");
        query.whereEqualTo("objectId", house.getId());
        query.getFirstInBackground(new GetCallback<ParseObject>()
        {
            @Override
            public void done(ParseObject parseHouse, ParseException outerException)
            {
                if(outerException == null)
                {
                    // Update al fields, for simplicity
                    parseHouse.put("housename", house.getHousename());
                    parseHouse.put("address", house.getAddress());
                    parseHouse.put("city", house.getCity());
                    parseHouse.put("state", house.getState());
                    parseHouse.put("zipcode", house.getZipCode());
                    parseHouse.put("latitude", house.getLatitude());
                    parseHouse.put("longitude", house.getLongitude());
                    parseHouse.put("admin", house.getAdmin());
                    parseHouse.put("members", house.getMembers());

                    // Save and callback
                    parseHouse.saveInBackground(new SaveCallback()
                    {
                        @Override
                        public void done(ParseException innerException)
                        {
                            if(innerException == null)
                            {
                                caller.onUpdateHouseSuccess(house);
                            }
                            else
                            {
                                caller.onUpdateHouseFailure("Could not update house information");
                            }
                        }
                    });
                }
                else
                {
                    caller.onUpdateHouseFailure("Could not locate the requested house: "+house.getId());
                }
            }
        });
    }

    /***********************************************************************************************
     *  ALERT METHODS
     **********************************************************************************************/
    private HomeBaseAlert buildAlert(ParseObject alert)
    {
        String objectID = alert.getObjectId();
        String type = alert.getString("type");
        String description = alert.getString("description");
        String owner = alert.getString("owner");
        String creator = alert.getString("creator");
        JSONArray array = alert.getJSONArray("seen");

        List<String> seen = convertJSON(array);

        return new HomeBaseAlert(objectID,type,seen,description,owner,creator);
    }

    public void createAlert(String type, String description, String ownerID, String creatorID, final HomeBaseActivity caller)
    {
        JSONArray seen = new JSONArray();
        final ParseObject alert = new ParseObject("Alert");
        alert.put("type", type);
        alert.put("description", description);
        alert.put("seen", seen);
        alert.put("owner", ownerID);
        alert.put("creator", creatorID);
        alert.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    HomeBaseAlert hbAlert = buildAlert(alert);
                    caller.onCreateAlertSuccess(hbAlert);
                } else {
                    caller.onCreateAlertFailure(e.getMessage());
                }
            }
        });
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
                    HomeBaseAlert hbAlert = buildAlert(parseObject);
                    caller.onGetAlertSuccess(hbAlert);
                }
                else
                {
                    caller.onGetAlertFailure(e.getMessage());
                }
            }
        });
    }

    public void updateAlert(final HomeBaseAlert alert, final HomeBaseActivity caller)
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Alert");
        query.whereEqualTo("objectId", alert.getId());
        query.getFirstInBackground(new GetCallback<ParseObject>()
        {
            @Override
            public void done(final ParseObject parseAlert, ParseException outerException)
            {
                if(outerException == null)
                {
                    parseAlert.put("type", alert.getType());
                    parseAlert.put("description", alert.getDescription());
                    parseAlert.put("owner", alert.getOwnerID());
                    parseAlert.put("creator", alert.getCreatorID());
                    JSONArray seenArray = new JSONArray(alert.getSeen());
                    parseAlert.put("seen", seenArray);
                    parseAlert.saveInBackground(new SaveCallback()
                    {
                        @Override
                        public void done(ParseException innerException)
                        {
                            if(innerException == null)
                            {
                                HomeBaseAlert updated = buildAlert(parseAlert);
                                caller.onUpdateAlertSuccess(updated);
                            }
                            else
                            {
                                caller.onUpdateAlertFailure(innerException.getMessage());
                            }
                        }
                    });
                }
                else
                {
                    caller.onGetAlertFailure(outerException.getMessage());
                }
            }
        });
    }

    public void deleteAlert(final HomeBaseAlert alert, final HomeBaseActivity caller)
    {
        ParseObject deleteAlert = ParseObject.createWithoutData("Alert", alert.getId());
        deleteAlert.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e)
            {
                if(e == null)
                {
                    caller.onDeleteAlertSuccess();
                }
                else
                {
                    caller.onDeleteAlertFailure(e.getMessage());
                }
            }
        });
    }
}

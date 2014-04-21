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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import android.app.AlertDialog;
import android.util.Log;
import java.util.ListIterator;

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
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    caller.onSignupSuccess(user);
                } else {
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
                if(e != null || parseUser == null) {
                    if (e != null) {
                        caller.onLoginError(e.getMessage());
                    } else {
                        caller.onLoginError("No account found");
                    }
                }
                else
                {
                    caller.onLoginSuccess();
                }
            }
        });

    }

    /******************************************************************************************************
     *
     * GPS Events and Wrappersbfor Parse
     ********************************************************************************************************/

    public void updateLocation(boolean home) {
        ParseUser jesus = getCurrentUser();
        jesus.put("isHome", home);
        jesus.saveEventually();

    }

    public boolean getUserLocation() {
        ParseUser jesus = this.getCurrentUser();
        Boolean isHome = false;
        try {
            isHome = jesus.getBoolean("isHome");
        } catch (Exception E){
            Log.d("Getting user Local","couldn't get isHome");
            jesus.put("isHome", false);
            return false;
        }
        return isHome;
    }

    /**************************************************************************************************************
     *  House Event and Wrappers for PARSE
     *************************************************************************************************************/

    public void createHouse(String housename, String address, String city, String state, int zipcode, String userid, double lat, double lng, final HomeBaseActivity caller)
    {
        // Add the creator to the user list, also get added as admin in constructor
        List<String> memberList = new LinkedList<String>();
        memberList.add(userid);

        final HomeBaseHouse newHouse = new HomeBaseHouse(housename, address, city, state, zipcode, userid, memberList, lat, lng);
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
                                HomeBaseHouse house = new HomeBaseHouse(housename, address, city, state, zipcode, admin, members, lat, longitude);
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


    public void getHouse(final GPSservice caller)
    {
        //get the current user, then the user part of that house

        ParseQuery<ParseObject> houseQuery = ParseQuery.getQuery("House");
        houseQuery.whereEqualTo("admin", getCurrentUser().getObjectId());
        houseQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseHouse, ParseException e) {
                if (e == null) {
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
                    HomeBaseHouse house = new HomeBaseHouse(housename, address, city, state, zipcode, admin, members, lat, longitude);
                    house.setId(id);
                    caller.onGetHouseSuccess(house);
                } else {
                    caller.onGetHouseFailure("Could not fetch house information, please try again");
                }
            }
        });

    }

    // Get via application model
    public void getHouse(HomeBaseHouse house, final HomeBaseActivity caller)
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
                    HomeBaseHouse house = new HomeBaseHouse(housename, address, city, state, zipcode, admin, members, lat, longitude);
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
    public void updateHouse(final HomeBaseHouse house, final HomeBaseActivity caller)
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
        String title = alert.getString("title");
        String type = alert.getString("type");
        String description = alert.getString("description");
        String owner = alert.getString("owner");
        String creator = alert.getString("creator");
        JSONArray array = alert.getJSONArray("seen");
        List<String> seen = convertJSON(array);

        return new HomeBaseAlert(title,objectID,type,seen,description,owner,creator);
    }

    public void createAlert(String title, String type, String description, String ownerID, String creatorID, final HomeBaseActivity caller)
    {
        JSONArray seen = new JSONArray();
        final ParseObject alert = new ParseObject("Alert");
        alert.put("title", title);
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

    public void getAlerts(final HomeBaseActivity caller)
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Alert");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    ArrayList<HomeBaseAlert> alertList = new ArrayList<HomeBaseAlert>();
                    for (int i = 0; i < objects.size(); i++) {
                        HomeBaseAlert alert = buildAlert(objects.get(i));
                        alertList.add(alert);
                    }
                    caller.onGetAlertListSuccess(alertList);
                } else {
                    caller.onGetAlertListFailure(e.getMessage());
                }
            }
        });
    }

    public void getAlerts(final HomeBaseActivity caller, String type)
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Alert");
        query.whereEqualTo("type", type);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    ArrayList<HomeBaseAlert> alertList = new ArrayList<HomeBaseAlert>();
                    for (int i = 0; i < objects.size(); i++) {
                        HomeBaseAlert alert = buildAlert(objects.get(i));
                        alertList.add(alert);
                    }
                    caller.onGetAlertListByTypeSuccess(alertList);
                } else {
                    caller.onGetAlertListByTypeFailure(e.getMessage());
                }
            }
        });
    }

    public void refreshAlerts(final HomeBaseActivity caller, String type)
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Alert");
        query.whereEqualTo("type", type);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    ArrayList<HomeBaseAlert> alertList = new ArrayList<HomeBaseAlert>();
                    for (int i = 0; i < objects.size(); i++) {
                        HomeBaseAlert alert = buildAlert(objects.get(i));
                        alertList.add(alert);
                    }
                    caller.onUpdateAlertListByTypeSuccess(alertList);
                } else {
                    caller.onUpdateAlertListByTypeFailure(e.getMessage());
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
                    parseAlert.put("title", alert.getTitle());
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

    /***********************************************************************************************
     *  BILL METHODS
     **********************************************************************************************/
    private HomeBaseBill buildBill(ParseObject bill)
    {
        String title = bill.getString("title");
        String objectID = bill.getObjectId();
        String description = bill.getString("description");
        String owner = bill.getString("owner");
        String creator = bill.getString("creator");
        double amount = bill.getInt("amount");
        List<String> seen = convertJSON(bill.getJSONArray("seen"));
        List<String> paid = convertJSON(bill.getJSONArray("paid"));
        List<String> toPay = convertJSON(bill.getJSONArray("toPay"));

        return new HomeBaseBill(title, objectID, amount, toPay, paid, seen, description, owner, creator);
    }

    public void createBill(String title, Double amount, String description, String ownerID, String creatorID, final HomeBaseActivity caller)
    {
        final ParseObject bill = new ParseObject("Bill");
        JSONArray seen = new JSONArray();
        JSONArray paid = new JSONArray();
        JSONArray toPay = new JSONArray();
        bill.put("title", title);
        bill.put("type", "bill");
        bill.put("description", description);
        bill.put("seen", seen);
        bill.put("owner", ownerID);
        bill.put("creator", creatorID);
        bill.put("amount", amount);
        bill.put("toPay", toPay);
        bill.put("paid", paid);

        bill.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    HomeBaseBill hbBill = buildBill(bill);
                    caller.onCreateBillSuccess(hbBill);
                } else {
                    caller.onCreateBillFailure(e.getMessage());
                }
            }
        });
    }

    public void getBill(String objectID, final HomeBaseActivity caller)
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Bill");
        query.whereEqualTo("objectId", objectID);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if(e == null)
                {
                    HomeBaseBill hbBill = buildBill(parseObject);
                    caller.onGetBillSuccess(hbBill);
                }
                else
                {
                    caller.onGetBillFailure(e.getMessage());
                }
            }
        });
    }

    public void getUnpaidBillsByUserID(String userID, final HomeBaseActivity caller)
    {
        ParseQuery<ParseObject> billsQuery = ParseQuery.getQuery("Bill");
        billsQuery.whereEqualTo("toPay", userID);
        billsQuery.orderByDescending("updatedAt");
        billsQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e)
            {
                if(e == null)
                {
                    List<HomeBaseBill> bills = new LinkedList<HomeBaseBill>();
                    for(ParseObject object : parseObjects)
                    {
                        HomeBaseBill newBill = buildBill(object);
                        bills.add(newBill);
                    }
                    caller.onGetBillsSuccess(bills);
                }
                else
                {
                    caller.onGetBillsFailure(e.getMessage());
                }
            }
        });
    }
    public void deleteBill(final HomeBaseBill bill, final HomeBaseActivity caller)
    {
        ParseObject deleteAlert = ParseObject.createWithoutData("Bill", bill.getId());
        deleteAlert.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e)
            {
                if(e == null)
                {
                    caller.onDeleteBillSuccess();
                }
                else
                {
                    caller.onDeleteBillFailure(e.getMessage());
                }
            }
        });
    }

    public void updateBill(final HomeBaseBill bill, final HomeBaseActivity caller)
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Bill");
        query.whereEqualTo("objectId", bill.getId());
        query.getFirstInBackground(new GetCallback<ParseObject>()
        {
            @Override
            public void done(final ParseObject parseBill, ParseException outerException)
            {
                if(outerException == null)
                {
                    //Set scalars
                    parseBill.put("amount", bill.getAmount());
                    parseBill.put("type", bill.getType());
                    parseBill.put("description", bill.getDescription());
                    parseBill.put("owner", bill.getOwnerID());
                    parseBill.put("creator", bill.getCreatorID());
                    parseBill.put("title", bill.getTitle());

                    // Build and set arrays
                    JSONArray seenArray = new JSONArray(bill.getSeen());
                    JSONArray paidArray = new JSONArray(bill.getPaid());
                    JSONArray toPayArray = new JSONArray(bill.getToPay());
                    parseBill.put("seen", seenArray);
                    parseBill.put("paid", paidArray);
                    parseBill.put("toPay", toPayArray);

                    parseBill.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException innerException) {
                            if (innerException == null) {
                                HomeBaseBill updated = buildBill(parseBill);
                                caller.onUpdateBillSuccess(updated);
                            } else {
                                caller.onUpdateBillFailure(innerException.getMessage());
                            }
                        }
                    });
                }
                else
                {
                    caller.onGetBillFailure(outerException.getMessage());
                }
            }
        });
    }
}

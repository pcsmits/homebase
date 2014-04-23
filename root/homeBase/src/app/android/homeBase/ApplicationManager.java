package app.android.homeBase;

import android.content.Context;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.util.Log;

import com.parse.ParseUser;

public class ApplicationManager {

    private Context context;

    private static ApplicationManager instance;

    public ArrayList<String> users;
    public ParseBase parse;
    public HashMap<String, ParseUser> usersObjects = new HashMap<String, ParseUser>();

    public static boolean tryGetInstance() {
        return (instance != null);
    }

    public static ApplicationManager getInstance() {
        if (instance == null) throw new RuntimeException("Reference to Application Manager was null");
        return instance;
    }

    public static ApplicationManager createInstance(Context context) {
        if (instance != null) {
            return instance;
        }
        return instance = new ApplicationManager(context.getApplicationContext());
    }

    // notice the constructor is private
    private ApplicationManager(Context context) {
        this.context = context;
        this.users = new ArrayList<String>();
        parse = new ParseBase();
        parse.getUsersOfHouse(this);

    }

    public void debugHomeUsers()
    {
        for(String user : users) {
            Log.d("User: ", user);
        }
    }

    public ArrayList<String> getHomeUsers()
    {
        return users;
    }

    public void onGetHomeUsersSuccess(List<ParseUser> parseUsers)
    {
        for(ParseUser user : parseUsers)
        {
            users.add(user.getUsername());
            usersObjects.put(user.getUsername(), user);
        }
    }

    public void onGetHomeUsersFailure()
    {

    }
}
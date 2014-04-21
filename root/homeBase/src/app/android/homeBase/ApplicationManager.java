package app.android.homeBase;

import android.content.Context;
import java.util.ArrayList;
import android.util.Log;

public class ApplicationManager {

    private Context context;

    private static ApplicationManager instance;

    public ArrayList<String> users;
    public ParseBase parse;

    public static boolean tryGetInstance() {
        return (instance != null);
    }

    public static ApplicationManager getInstance() {
        if (instance == null) throw new RuntimeException("Reference to AppliationManager was null");
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

    public void onGetHomeUsersSuccess(String user)
    {
        users.add(user);
    }

    public void onGetHomeUsersFailure()
    {

    }
}
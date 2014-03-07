package app.android.homeBase;

import android.content.Context;
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

    public parseBase(Context context)
    {
        Parse.initialize(context, "dD0N7G0DiCBySn8gXbYtcOxfvM8OGKUZOBRPy8wl", "tt6FH3ugfJOhYY41bCiPb7URHrnzQtV8drwEKQDJ");
    }

    public void loginUser(final String username, final  String password, final Context context)
    {

    }

    public void addUser(final String username, final String password, final Context context)
    {
        ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
        userQuery.whereEqualTo("username", username);
        userQuery.findInBackground(new FindCallback<ParseUser>()
        {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e)
            {
                if(e == null)
                {
                    if(parseUsers.isEmpty())
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
                                    //TODO
                                    Toast.makeText(context, "SUCCSESS REPALCE WITH INTENT", Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(context, "Sign up failed: "+e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(context, username+" is already in use please choose another username", Toast.LENGTH_LONG).show();
                    }
                    Toast.makeText(context, "Error contacting the parse cloud", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

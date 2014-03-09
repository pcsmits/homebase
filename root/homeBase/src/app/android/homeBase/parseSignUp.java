package app.android.homeBase;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.List;

/**
 * Created by kyle on 3/8/14.
 * This is a custom subclass of the parseBase wrapper class.
 * It handles all the parse operations required for signup of a new user
 *
 * TODO at the moment it handles a bit to much
 * should have it return booleans and do less intenet firing, view manipulation
 */
public class parseSignUp extends parseBase
{
    //Default Constructor
    public parseSignUp(Context context)
    {

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

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
 */
public class parseSignUp extends parseBase
{
    public parseSignUp(Context context)
    {

    }

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

    // This method is should only be called from the signup activity
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
                    if (parseUsers.isEmpty())
                    {
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

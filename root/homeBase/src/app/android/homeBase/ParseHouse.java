package app.android.homeBase;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * Created by kyle on 3/10/14.
 */
//TODO lower case
public class ParseHouse extends parseBase
{
    public ParseHouse()
    {
        super();
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
}


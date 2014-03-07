package app.android.homeBase;

import android.content.Context;

import com.parse.*;
/**
 * Created by kyle on 3/6/14.
 */
public class parseBase extends ParseObject {
    public parseBase(Context context)
    {
        Parse.initialize(context, );
    }
}

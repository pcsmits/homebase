package app.android.homeBase;

import java.util.List;

/**
 * Created by kyle on 3/24/14.
 * Generic alert class for wrapping Chores, Messages, Groceries
 */
public class HomeBaseAlert {

    // These can only be set on creation
    private final String id;
    private final String type;
    private final String description;

    private List<String> seen;
    //TODO private Picture picture;

    public HomeBaseAlert(String id, String type, List<String> seen, String description)
    {
        this.id = id;
        this.type = type;
        this.seen = seen;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public List<String> getSeen() {
        return seen;
    }

    public void addSeen(String user) {
        this.seen.add(user);
    }

    public String getDescription() {
        return description;
    }

}

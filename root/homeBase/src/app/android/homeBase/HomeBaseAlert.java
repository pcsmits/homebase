package app.android.homeBase;

/**
 * Created by kyle on 3/24/14.
 * Generic alert class for wrapping Chores, Messages, Groceries
 */
public class HomeBaseAlert {

    // These can only be set on creation
    private final String id;
    private final String type;
    private final String description;

    private boolean seen;
    //TODO private Picture picture;

    public HomeBaseAlert(String id, String type, boolean seen, String description)
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

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public String getDescription() {
        return description;
    }

}

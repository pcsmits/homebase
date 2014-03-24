package app.android.homeBase;

/**
 * Created by kyle on 3/24/14.
 */
public class HomeBaseAlert {
    
    private String id;
    private String type;
    private boolean seen;
    private String description;

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

    //TODO private Picture picture;
}

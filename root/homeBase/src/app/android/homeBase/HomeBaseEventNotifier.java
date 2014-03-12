package app.android.homeBase;


public class HomeBaseEventNotifier {
    private HomeBaseCallback homeBaseCallback;
    private boolean triggered;

    public HomeBaseEventNotifier(HomeBaseCallback callback) {
        homeBaseCallback = callback;
        triggered = true;
    }

    public void HandleCallback()
    {
        if(triggered) {
            homeBaseCallback.homeBaseCallbackAction();
        }
    }
}

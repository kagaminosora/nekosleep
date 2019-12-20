package xjtlu.eevee.nekosleep.menu;

public interface LockScreenListener {
    String Tag="LockScreenListener";
    public void onScreenOn();
    public void onScreenOff();
    public void onUserPresent();
}

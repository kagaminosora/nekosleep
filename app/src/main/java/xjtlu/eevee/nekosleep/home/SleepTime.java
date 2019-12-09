package xjtlu.eevee.nekosleep.home;

public class SleepTime {
    private int hour(String time){
        return Integer.parseInt(time.split(":")[0]);
    }

    private int min(String time){
        return Integer.parseInt(time.split(":")[1]);
    }

    public int sleepLast(String sleeptime,String waketime){
        int sleephour = hour(sleeptime);
        int sleepmin = min(sleeptime);
        int wakehour = hour(waketime);
        int wakemin = hour(waketime);
        if (sleephour >= wakehour){
            return (12-sleephour-1)*60+(60-sleepmin)+wakehour*60+wakemin;
        }else{
            return (wakehour-sleephour)*60+(wakemin-sleepmin);
        }
    }
}

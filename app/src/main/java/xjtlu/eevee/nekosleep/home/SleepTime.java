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
        int wakemin = min(waketime);
        if (sleephour > wakehour){
            return (12-sleephour-1)*60+(60-sleepmin)+wakehour*60+wakemin;
        }else{
            return (wakehour-sleephour)*60+(wakemin-sleepmin);
        }
    }

    public boolean within1h(String time_act, String time_set){
        if (hour(time_set)==23){
            if (hour(time_act)==0){
                if(min(time_set)>=min(time_act)){return true;}
                else return false;
            }else{
                if(sleepLast(time_act,time_set)<=60){return true;}
                else return false;
            }
        }
        else if (hour(time_set)==0){
            if (hour(time_act)==23){
                if(min(time_act)>=min(time_set)){
                    return true;
                }else return false;
            }else{
                if(sleepLast(time_act,time_set)<=60){return true;}
                else return false;
            }
        }
        else if(sleepLast(time_act,time_set)<=60||sleepLast(time_set,time_set)<=60){
            return true;
        }else return false;
    }
}

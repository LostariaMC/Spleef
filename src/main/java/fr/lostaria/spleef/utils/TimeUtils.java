package fr.lostaria.spleef.utils;

public class TimeUtils {

    public static String formatTime(int time){
        int minutes = time / 60;
        int seconds = time % 60;

        String minutesString = minutes < 10 ? "0" + minutes : String.valueOf(minutes);
        String secondsString = seconds < 10 ? "0" + seconds : String.valueOf(seconds);

        return minutesString + ":" + secondsString;
    }

}

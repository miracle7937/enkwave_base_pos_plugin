package Var;

public class DateUtils {

   static public boolean hourPassed(long hour, long timeMillis ){
       long currentTimeMillis = System.currentTimeMillis();

// Add 7 hours (in milliseconds) to the specific point in time
       long sevenHoursInMillis = hour * 60 * 60 * 1000; // 7 hours in milliseconds
       long specificTimePlusSevenHours = timeMillis + sevenHoursInMillis;

       boolean hasPassedSevenHours = currentTimeMillis >= specificTimePlusSevenHours;
        return  hasPassedSevenHours;
   }
}

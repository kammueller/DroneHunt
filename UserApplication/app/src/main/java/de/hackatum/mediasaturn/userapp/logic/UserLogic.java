package de.hackatum.mediasaturn.userapp.logic;

import android.widget.ImageView;

import com.google.gson.Gson;

import de.hackatum.mediasaturn.userapp.activities.MainActivity;
import de.hackatum.mediasaturn.userapp.common.*;

/**
 * UserLogic-Class
 *
 * @author Matthias Kammueller
 */
public class UserLogic {

    /**
     * @return the seconds the user is still activated
     */
//    public static int remainingActiveTime() {
//        Gson gson = new Gson();
//        MainActivity.enableStrictMode();
//        RemainingActive time = gson.fromJson(new RecieveTask().doInBackground("isActive"), RemainingActive.class);
//        return time.getSecondsLeft();
//    }

    /**
     * @param filename the filename (incl. ".jpg" etc.) of the last hint
     * @return null if there's no new hint, a file otherwise
     */
    public static ImageView getNewHint(String filename) {
        Gson gson = new Gson();
     //   NewHintAvailable hint = gson.fromJson(new RecieveTask().doInBackground("newHintAvailable/" + filename), NewHintAvailable.class);
//        if (hint.isHintAvailable()) {
//            // GET /currentHint -> 200 & picture ; in what way ever the pic will come down and will be returned...
//            return null; // TODO: 12.11.2016 !!!
//        } else {
            return null; // no new hint
//        }
    }

    /**
     * @param coordinates the GPS-Coordinates of the user
     * @return is the user near to the goal
     */
//    public static boolean isGoalInRange(GPS coordinates) {
//        Gson gson = new Gson();
//        String request = "inGoalRange/" + coordinates.getLongitude() + "/" + coordinates.getLatitude();
//        MainActivity.enableStrictMode();
//      //  GoalInRange inRange = gson.fromJson(new RecieveTask().doInBackground(request), GoalInRange.class);
//        return inRange.isInGoalRange();
//    }

    /**
     * Does the player win the game?
     *
     * @param code the QR-code from the goal
     * @return was the code correct?
     */
    public static boolean check(String code) {

        // get request ("/check/code")
        // TODO: 12.11.2016 what was the code for that? -.-

        // returns 403 if wrong, 200 if true

        return true; // won?
    }

}

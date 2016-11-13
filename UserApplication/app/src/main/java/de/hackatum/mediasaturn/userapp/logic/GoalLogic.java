package de.hackatum.mediasaturn.userapp.logic;

import com.google.gson.Gson;

import de.hackatum.mediasaturn.userapp.activities.MainActivity;
import de.hackatum.mediasaturn.userapp.common.GPS;

/**
 * logic for the running-around gadget which will be Mr.X aka the goal
 *
 * @author Matthias Kammueller
 */
public class GoalLogic {

    /**
     * @param coordinates the GPS-Coordinates of the user
     */
    public static void sendGPS(GPS coordinates) {
        Gson gson = new Gson();
        gson.toJson(coordinates);
        String[] params = {"??", gson.toString()};
        //new SendTask().doInBackground(params); // TODO: 12.11.2016 api link
    }

}

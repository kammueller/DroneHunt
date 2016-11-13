package hackatum.user.logic;

import com.google.gson.Gson;
import hackatum.common.GPS;

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
        HttpClient.postRequest("updateGPS", gson);
    }

}

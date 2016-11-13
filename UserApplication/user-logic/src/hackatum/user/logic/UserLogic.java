package hackatum.user.logic;

import com.google.gson.Gson;
import hackatum.common.*;
import javafx.scene.image.ImageView;

/**
 * UserLogic-Class
 *
 * @author Matthias Kammueller
 */
public class UserLogic {

    /**
     * register the user
     *
     * @param username the self-selected user-name
     * @return the auth-token; null if username isn't unique
     */
    public static String getToken(String username) {
        Gson gson = new Gson();
        gson.toJson(new Username(username));

        String response = HttpClient.postRequest("register", gson);
        if (response == null) {
            // username already exists
            return null;
        }

        Token token = gson.fromJson(response, Token.class);
        return token.getToken();
    }

    /**
     * @return the seconds the user is still activated
     */
    public static int remainingActiveTime() {
        Gson gson = new Gson();
        RemainingActive time = gson.fromJson(HttpClient.getRequest("isActive"), RemainingActive.class);
        return time.getSecondsLeft();
    }

    /**
     * @param filename the filename (incl. ".jpg" etc.) of the last hint
     * @return null if there's no new hint, a file otherwise
     */
    public static ImageView getNewHint(String filename) {
        Gson gson = new Gson();
        NewHintAvailable hint = gson.fromJson(HttpClient.getRequest("newHintAvailable/" + filename), NewHintAvailable.class);
        if (hint.isHintAvailable()) {
            // GET /currentHint -> 200 & picture ; in what way ever the pic will come down and will be returned...
            return null; // TODO: 12.11.2016 !!!
        } else {
            return null; // no new hint
        }
    }

    /**
     * @param coordinates the GPS-Coordinates of the user
     * @return is the user near to the goal
     */
    public static boolean isGoalInRange(GPS coordinates) {
        Gson gson = new Gson();
        String request = "inGoalRange/" + coordinates.getLongitude() + "/" + coordinates.getLatitude();
        GoalInRange inRange = gson.fromJson(HttpClient.getRequest(request), GoalInRange.class);
        return inRange.isInGoalRange();
    }

    /**
     * Does the player win the game?
     *
     * @param code the QR-code from the goal
     * @return was the code correct?
     */
    public static boolean check(String code) {

        return HttpClient.getRequest("check/" + code) != null; // check returns 403 if false], 200 if true

    }

}

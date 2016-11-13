package hackatum.user.logic;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HttpClient {
    private static final String SERVER = "http://localhost:8080/"; // TODO: 12.11.2016 real server

    // TODO: 12.11.2016 receive image function?

    /**
     * @param request the request-link at the API
     * @return null if 403, json-string otherwise
     */
    public static String getRequest(String request) {
        try {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet getRequest = new HttpGet(
                    SERVER + request);
            getRequest.addHeader("accept", "application/json");

            HttpResponse response = httpClient.execute(getRequest);

            if (response.getStatusLine().getStatusCode() == 403) {
                return null;
            }

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }

            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));

            String output = "";
            while ((br.readLine()) != null) {
                output += output;
            }

            httpClient.getConnectionManager().shutdown();

            return output;

        } catch (IOException e) {

            e.printStackTrace();
        }

        return null; // TODO: 12.11.2016 not nice =(
    }

    /**
     *
     * @param request the request-link at the API
     * @param send the Json (Gson-Object) which will be the data
     * @return null if 403, json-string otherwise
     */
    public static String postRequest(String request, Gson send) {
        try {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(
                    SERVER + request);
            postRequest.addHeader("accept", "application/json");

            StringEntity input = new StringEntity(send.toString());
            input.setContentType("application/json");
            postRequest.setEntity(input);

            HttpResponse response = httpClient.execute(postRequest);

            if (response.getStatusLine().getStatusCode() == 403) {
                return null;
            }

            if (response.getStatusLine().getStatusCode() != 201) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }

            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));

            String output = "";
            while ((br.readLine()) != null) {
                output += output;
            }

            httpClient.getConnectionManager().shutdown();

            return output;

        } catch (IOException e) {

            e.printStackTrace();
        }

        return null; // TODO: 12.11.2016 not nice =(
    }

}
package controller;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class GetOnlineResources {
    public static String[] getSynonyms(String word) {
        try {
            String url = "https://wordsapiv1.p.mashape.com/words/" + word + "/synonyms"; // Set URL string
            URL obj = new URL(url); // Create URL object with provided URL string
            HttpsURLConnection connection = (HttpsURLConnection) obj.openConnection(); // Open a HTTPS connection with URL object
            connection.setRequestMethod("GET"); // Set Request Method to GET
            connection.setRequestProperty("X-Mashape-Key", "cvJ5h3BqYUmshyXX8zkn3CEcImfPp19IPBQjsnk0jL8yBqx5cQ"); // Set API authorize key
            int responseCode = connection.getResponseCode(); // Get response code
            System.out.println("\nSending 'GET' request to URL: " + url); // Print URL
            System.out.println("Response Code: " + responseCode); // Print response code

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream())); // Create Buffered Reader object from object InputStreamReader of connection
            String inputLine; // Initalize temp String to get response
            StringBuffer response = new StringBuffer(); // Create StringBuffer object

            while ((inputLine = in.readLine()) != null) { // Loop while BufferedReader still has next line
                response.append(inputLine); // Append temp string to response buffer
            }
            in.close(); // Close buffer
            JSONObject JSONobj = new JSONObject(response.toString());
            JSONArray synonyms = JSONobj.getJSONArray("synonyms");
            String[] syn = new String[synonyms.length()];
            for (int i = 0; i < synonyms.length(); i++)
                syn[i] = synonyms.getString(i);
            return syn;
        }
        catch (Exception e) {
            e.printStackTrace(); // Print error
            return null; // Return nothing :D
        }
    }
}

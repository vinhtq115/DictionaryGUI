package controller;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;

class GetOnlineResources {
    static boolean checkConnection() {
        try {
            URL url = new URL("http://80.211.88.124/");
            URLConnection connection = url.openConnection();
            connection.connect();
            return true; // Can connect to mirror
        }
        catch (Exception e) {
            return false; // Can't
        }
    }
    static boolean checkConnectionToGoogle() {
        try {
            URL url = new URL("https://www.google.com/");
            URLConnection connection = url.openConnection();
            connection.connect();
            return true; // Can connect to Google
        }
        catch (Exception e) {
            return false; // Can't
        }
    }
    static Vector<Vector<String>> grabData(String word) {
        try {
            String url = "http://80.211.88.124/?define=" + word + "&lang=en"; // Set URL string
            URL obj = new URL(url); // Create URL object with provided URL string
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection(); // Open a HTTPS connection with URL object
            connection.setRequestMethod("GET"); // Set Request Method to GET
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
            JSONObject JSONobj = new JSONObject(response.toString()); // Save response to JSONObject

            Vector<Vector<String>> data = new Vector<>();
            data.add(getPronunciation(JSONobj.getJSONArray("phonetic")));
            data.add(getExample(JSONobj.getJSONObject("meaning")));
            data.add(getSynonyms(JSONobj.getJSONObject("meaning")));
            return data;
        }
        catch (Exception e) {
            e.printStackTrace(); // Print error
            return null;
        }
    }
    private static Vector<String> getPronunciation(JSONArray phonetic) {
        Vector<String> pronunciation = new Vector<>();
        for (int i = 0; i < phonetic.length(); i++) {
            pronunciation.add(phonetic.getString(i));
        }
        return pronunciation;
    }
    private static Vector<String> getSynonyms(JSONObject meaning) {
        Vector<String> synonyms = new Vector<>();
        JSONArray type = meaning.names(); // Noun, verb, etc.
        for (int i = 0; i < type.length(); i++) {
            JSONArray temp = meaning.getJSONArray(type.getString(i)); // Sub type
            for (int j = 0; j < temp.length(); j++) {
                JSONObject tempIn = temp.getJSONObject(j);
                if (tempIn.has("synonyms")) {
                    JSONArray syn = tempIn.getJSONArray("synonyms");
                    for (int k = 0; k < syn.length(); k++) {
                        synonyms.add(syn.getString(k));
                    }
                }
            }
        }
        removeDuplicate(synonyms);
        return synonyms;
    }
    private static Vector<String> getExample(JSONObject meaning) {
        Vector<String> example = new Vector<>();
        JSONArray type = meaning.names();
        for (int i = 0; i < type.length(); i++) {
            JSONArray temp = meaning.getJSONArray(type.getString(i));
            for (int j = 0; j < temp.length(); j++) {
                JSONObject tempIn = temp.getJSONObject(j);
                if (tempIn.has("example")) {
                    example.add(tempIn.getString("example"));
                }
            }
        }
        removeDuplicate(example);
        return example;
    }
    private static void removeDuplicate(Vector<String> string) {
        for (int i = 0; i < string.size(); i++) {
            for (int j = i+1; j < string.size(); j++) {
                if (string.elementAt(i).equals(string.elementAt(j)))
                    string.remove(j--);
            }
        }
    }
}

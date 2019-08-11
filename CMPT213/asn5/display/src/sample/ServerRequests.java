package sample;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import sample.model.Tokimon;
import sample.model.TokimonTeam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static sample.Main.feedBack;


/**
 * Handles all Server requests and responses.
 */
public class ServerRequests {

    public static void addToki(Tokimon toki) {
        try {
            URL url = new URL("http://localhost:8080/api/tokimon/add");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // POST
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());

            wr.write("{\"name\":\"" + toki.name + "\",\"ability\":\"" + toki.ability + "\",\"color\":\"" + toki.color + "\",\"id\":\"" + toki.id + "\",\"strength\" : \"" + toki.strength + "\", \"weight\" :\" " + toki.weight + "\", \"height\" : \"" + toki.height + "\", \"health\":\"" + toki.health + "\"}");
            wr.flush();
            wr.close();

            connection.connect();

            System.out.println(connection.getResponseCode());
            connection.disconnect();
        } catch (IOException e) {

        }
    }
        public static TokimonTeam deleteToki(TokimonTeam theTeam, int indexRem){
            try {
                URL url = new URL("http://localhost:8080/api/tokimon/"+theTeam.team.get(indexRem).id);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("DELETE");
                connection.connect();

                System.out.println(connection.getResponseCode());
                if(connection.getResponseCode() == 404){
                    feedBack.setText("Tokimon does not exist in Server, Removing from local display");
                    feedBack.setStyle("-fx-border-color:black; -fx-background-color: #ff6666;");

                }
                connection.disconnect();
            }
            catch (IOException e){



            }
        return theTeam;
    }
    public static String showAll(String jsonTokimonTeam){
        try {
            URL url = new URL("http://localhost:8080/api/tokimon/all");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");


            connection.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            System.out.println(connection.getResponseCode());
            String output;
            while ((output = br.readLine()) != null) {
                jsonTokimonTeam = jsonTokimonTeam +output;
                //System.out.println(output);
            }


            System.out.println(connection.getResponseCode());
            //System.out.println("this is also it" +jsonTokimonTeam);
            connection.disconnect();
        }
        catch (NullPointerException npe){
            System.out.println("server not running");
        }
        catch (IOException e){
            System.out.println("could not get existing tokimons");

        }
        return jsonTokimonTeam;
    }
    public static Tokimon showInfo(TokimonTeam theTeam, int indexInfo){
        String jsonTokimon = "";
        try {
            URL url = new URL("http://localhost:8080/api/tokimon/"+theTeam.team.get(indexInfo).id);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            //connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type","application/json");

            connection.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            System.out.println(connection.getResponseCode());
            String output;

            while ((output = br.readLine()) != null) {
                jsonTokimon = jsonTokimon +output;

            }
            System.out.println(connection.getResponseCode());
            connection.disconnect();
        }
        catch (IOException e){
            System.out.println("could not get more info on tokimon");

        }
        Gson gson = new Gson();
        Tokimon theTokimon= null ;
        try {
            theTokimon = gson.fromJson(jsonTokimon, Tokimon.class);
            System.out.println("we made it " + theTokimon.name);
        }
        catch(JsonSyntaxException jse){
            System.out.println("Error: Could not convert Json to Tokimon class" );
            System.exit(-1);
        }
        return theTokimon;
    }

    public static void trainToki(int tokiId) {
        try {
            URL url = new URL("http://localhost:8080/api/train/tokimon/"+tokiId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            connection.connect();
            System.out.println(connection.getResponseCode());
            connection.disconnect();
        } catch (IOException e) {

        }
    }

}

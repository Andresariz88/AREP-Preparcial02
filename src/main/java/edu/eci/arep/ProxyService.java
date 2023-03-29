package edu.eci.arep;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import static spark.Spark.*;

public class ProxyService {

    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String GET_URL = "http://127.0.0.1:5000/palindrome?value=";

    private static final List<String> mathServices = new ArrayList<>(Arrays.asList("http://ec2-54-224-110-244.compute-1.amazonaws.com:5000/palindrome?value=", "http://ec2-18-212-11-58.compute-1.amazonaws.com:5000/palindrome?value="));

    public static void main(String... args){
        port(getPort());
        staticFileLocation("/");
        get("espalindromo", (req,res) -> isPalindrome(req.queryParams("value")));
    }

    private static String isPalindrome(String word) throws IOException {
        URL obj = new URL(roundRobin()+word);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);

        //The following invocation perform the connection implicitly before getting the code
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());
            return response.toString();
        } else {
            System.out.println("GET request not worked");
        }
        System.out.println("GET DONE");

        return "";
    }

    private static String roundRobin() {
        Random random = new Random();
        return mathServices.get(random.nextInt(2));
    }

    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567;
    }

}

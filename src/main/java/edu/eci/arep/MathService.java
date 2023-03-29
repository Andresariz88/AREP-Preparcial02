package edu.eci.arep;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class MathService {

    public static void main(String... args){
        port(getPort());
        get("palindrome", (req,res) -> {
            res.type("application/json");
            return makeResponse(req.queryParams("value"));
        });
    }
    private static String makeResponse(String word) {
        Map<String, String> response = new HashMap<>();
        response.put("operation", "palíndromo");
        response.put("input", word);
        if (isPalindrome(word)) {
            response.put("output", "Si es palíndromo");
        } else {
            response.put("output", "No es palíndromo");
        }
        Gson gson = new Gson();
        System.out.println(gson.toJson(response));
        return gson.toJson(response);
   }

    private static boolean isPalindrome(String word) {
        return word.equals(reverse(word));
    }

    private static String reverse(String word) {
        String drow = "";
        char ch;
        for (int i = 0; i < word.length(); i++) {
            ch = word.charAt(i);
            drow = ch + drow;
        }
        return drow;
    }

    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 5000;
    }
}

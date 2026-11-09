import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ReadJson {

    public static void main(String[] args) {
        try {
            JSONObject pokemon = fetchPokemon("ditto");
            System.out.println(describe(pokemon));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public static JSONObject fetchPokemon(String name) throws IOException, ParseException {
        String clean = name.trim().toLowerCase();
        if (clean.isEmpty()) {
            throw new IOException("Type a Pokemon name first.");
        }

        URL url = new URL("https://pokeapi.co/api/v2/pokemon/" + URLEncoder.encode(clean, "UTF-8"));
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
        conn.setConnectTimeout(8000);
        conn.setReadTimeout(8000);

        int code = conn.getResponseCode();
        if (code == 404) {
            throw new IOException("No Pokemon named \"" + clean + "\" was found.");
        }

        StringBuilder total = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                total.append(line);
            }
        } finally {
            conn.disconnect();
        }

        return (JSONObject) new JSONParser().parse(total.toString());
    }

    public static String describe(JSONObject pokemon) {
        String name = (String) pokemon.get("name");
        Long height = (Long) pokemon.get("height");
        Long weight = (Long) pokemon.get("weight");

        StringBuilder abilities = new StringBuilder();
        JSONArray abilityList = (JSONArray) pokemon.get("abilities");
        if (abilityList != null) {
            for (Object entry : abilityList) {
                JSONObject ability = (JSONObject) ((JSONObject) entry).get("ability");
                if (abilities.length() > 0) {
                    abilities.append(", ");
                }
                abilities.append(ability.get("name"));
            }
        }
//
        return "Name: " + name + "\n"
                + "Height: " + height + "\n"
                + "Weight: " + weight + "\n"
                + "Abilities: " + abilities;
    }
}

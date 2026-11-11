import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ReadJson {

    public PokemonData fetchPokemon(String query) throws Exception {

        // Ask the API for one Pokemon (query can be a name or an ID number)
        URL url = new URL("https://pokeapi.co/api/v2/pokemon/" + query.toLowerCase());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");

        JSONParser parser = new JSONParser();
        InputStreamReader reader = new InputStreamReader(conn.getInputStream());
        JSONObject json = (JSONObject) parser.parse(reader);
        reader.close();

        PokemonData data = new PokemonData();
        data.name = (String) json.get("name");
        data.id = (long) json.get("id");

        // base_experience can be null for some Pokemon, so check first
        if (json.get("base_experience") != null) {
            data.baseExperience = (long) json.get("base_experience");
        }

        // Loop through the abilities and join the names into one String
        data.abilities = "";
        JSONArray abilitiesArray = (JSONArray) json.get("abilities");
        for (Object item : abilitiesArray) {
            JSONObject nestedObj = (JSONObject) item;
            JSONObject abilityinfo = (JSONObject) nestedObj.get("ability");
            data.abilities += abilityinfo.get("name") + " ";
        }

        // Same idea for the types
        data.types = "";
        JSONArray typesArray = (JSONArray) json.get("types");
        for (Object item : typesArray) {
            JSONObject nestedObj = (JSONObject) item;
            JSONObject typeinfo = (JSONObject) nestedObj.get("type");
            data.types += typeinfo.get("name") + " ";
        }

        // The picture link is inside "sprites"
        JSONObject sprites = (JSONObject) json.get("sprites");
        data.imageUrl = (String) sprites.get("front_default");

        return data;
    }
}
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ReadJson {

    public PokemonData fetchPokemon(String query) throws Exception {

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
        data.height = (long) json.get("height");
        data.weight = (long) json.get("weight");

        List<String> Abilities = new ArrayList<>();
        JSONArray abilitiesArray = (JSONArray) json.get("abilities");
        if (abilitiesArray != null) {
            for (Object item : abilitiesArray) {
                JSONObject nestedObj = (JSONObject) item;
                JSONObject abilityinfo = (JSONObject) nestedObj.get("ability");
                Abilities.add((String) abilityinfo.get("name"));
            }
        }
        data.abilities = Abilities;

        return data;

    }
}

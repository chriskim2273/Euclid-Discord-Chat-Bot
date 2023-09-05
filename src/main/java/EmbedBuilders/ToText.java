package EmbedBuilders;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import APIs.MathPixAPI;
import net.dv8tion.jda.api.EmbedBuilder;

public class ToText {
	
	public static EmbedBuilder toText(String link) throws IOException, ParseException {
		HttpURLConnection conn = MathPixAPI.getConn("text");
		OutputStream op = conn.getOutputStream();
		EmbedBuilder Embed = new EmbedBuilder();
		
		op.write(("{\"src\": \""+ link + "\", \"formats\": [\"text\"]}").getBytes("UTF-8"));
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject)jsonParser.parse(
		      new InputStreamReader(conn.getInputStream(), "UTF-8"));
		
		Embed.setTitle("Here is your image converted to text: ");
		if(jsonObject.containsKey("text"))
			Embed.setDescription(jsonObject.get("text").toString());
		else
			Embed.setDescription("Unable to convert image to text.. Sorry! Maybe provide better quality? :(");
		return Embed;
	}
}

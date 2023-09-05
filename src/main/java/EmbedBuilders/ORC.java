package EmbedBuilders;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import APIs.MathPixAPI;
import APIs.WolframAPI;
import net.dv8tion.jda.api.EmbedBuilder;

public class ORC {
	//private static String appid = "8TLUYJ-AEHGEV2PPW";
	public static EmbedBuilder OCR(String link) throws IOException, ParseException  {
		HttpURLConnection conn = MathPixAPI.getConn("latex");
		OutputStream op = conn.getOutputStream();
		
		op.write(("{\"src\": \""+ link + "\", \"ocr\": [\"math\"], \"formats\": [\"wolfram\",\"mathml\"], \"data_options\": {\"include_asciimath\": true,\"include_latex\": true,\"include_svg\": true}}").getBytes("UTF-8"));
		
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject)jsonParser.parse(
		      new InputStreamReader(conn.getInputStream(), "UTF-8"));
		
		System.out.println(jsonObject.toJSONString());
		return WolframAPI.Solve(jsonObject.get("wolfram").toString());
	}
}

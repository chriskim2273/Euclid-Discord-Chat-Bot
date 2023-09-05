package APIs;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import MainComponent.Bot;

public class MathPixAPI {
	public static boolean checkType(String type) {
		if(type.equals("text") || type.equals("latex"))
			return true;
		return false;
	}
	
	public static HttpURLConnection getConn(String type) throws IOException {
		if(checkType(type)!=true)
			return null;
		String urllink = "https://api.mathpix.com/v3/" + type;
		URL url = new URL(urllink);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestMethod("POST");
		conn.setRequestProperty("app_id", Bot.app_id);
		conn.setRequestProperty("app_key", Bot.app_key);
		conn.setRequestProperty("Content-type", "application/json");
		conn.setUseCaches(false);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.connect();
		return conn;
	}
}

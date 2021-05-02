package me.robbyblue.scratch4j;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.json.JSONObject;

public class Scratch4j {

	public static void main(String[] args) {
	}

	public ScratchSession login(String username, String password) {
		JSONObject loginJson = new JSONObject();
		loginJson.put("username", username);
		loginJson.put("password", password);
		loginJson.put("useMessages", true);

		try {
			String sessionid = null;
			String csrftoken = null;
			
			URL url = new URL("https://scratch.mit.edu/accounts/login");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("PUT");

			con.addRequestProperty("referer", "https://scratch.mit.edu");
			con.addRequestProperty("x-requested-with", "XMLHttpRequest");
			con.addRequestProperty("x-csrftoken", "abc");
			con.addRequestProperty("cookie", "scratchcsrftoken=abc; scratchlanguage=en;");
			con.setDoOutput(true);

			DataOutputStream os = new DataOutputStream(con.getOutputStream());
			byte[] input = loginJson.toString().getBytes();
			os.write(input);
			os.flush();
			os.close();

			try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				List<String> cookies = con.getHeaderFields().get("Set-Cookie");
				if (cookies != null) {
					for (String s : cookies) {
						for (String cookie : s.split("; ")) {
							if (cookie.startsWith("scratchsessionsid")) {
								sessionid = cookie.split("=")[1];
							}
							if (cookie.startsWith("scratchcsrftoken")) {
								csrftoken = cookie.split("=")[1];
							}
						}
					}
				}
			}
			
			return new ScratchSession(username, sessionid, csrftoken);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}

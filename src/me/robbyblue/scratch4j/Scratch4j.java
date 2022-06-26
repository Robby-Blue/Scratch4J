package me.robbyblue.scratch4j;

/*
Copyright (c)
Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
The Software shall be used for Good, not Evil.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class Scratch4j {

	public static void main(String[] args) {
	}

	public ScratchSession login(String username, String password) throws JSONException {
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

			con.addRequestProperty("user-agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.128 Safari/537.36 OPR/75.0.3969.285");
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

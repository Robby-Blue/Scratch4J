package me.robbyblue.scratch4j.cloud;

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

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Map;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

public class ScratchCloudSocket extends WebSocketClient {

	ScratchCloudSession scratchCloudSession;

	public ScratchCloudSocket(URI serverUri, Draft draft, Map<String, String> header,
			ScratchCloudSession scratchCloudSession) {
		super(serverUri, draft, header);
		this.scratchCloudSession = scratchCloudSession;
	}

	@Override
	public void onOpen(ServerHandshake handshakedata) {
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		this.scratchCloudSession.reconnect();
	}

	@Override
	public void onMessage(String message) {
		try {
			JSONObject messageJson = new JSONObject(message);
			
			ScratchCloudEvent event = new ScratchCloudEvent(messageJson.getString("name"), messageJson.getString("value"));
			for (ScratchCloudListener listener : scratchCloudSession.getListeners()) {
				listener.onScratchCloudEvent(event);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onMessage(ByteBuffer message) {
	}

	@Override
	public void onError(Exception ex) {
	}

}

package me.robbyblue.scratch4j.cloud;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Map;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
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
		JSONObject messageJson = new JSONObject(message);

		ScratchCloudEvent event = new ScratchCloudEvent(messageJson.getString("name"), messageJson.getString("value"));
		for (ScratchCloudListener listener : scratchCloudSession.getListeners()) {
			listener.onScratchCloudEvent(event);
		}
	}

	@Override
	public void onMessage(ByteBuffer message) {
	}

	@Override
	public void onError(Exception ex) {
	}

}

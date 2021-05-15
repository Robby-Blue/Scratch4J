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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.java_websocket.drafts.Draft_6455;
import org.json.JSONObject;

import me.robbyblue.scratch4j.ScratchSession;
import me.robbyblue.scratch4j.cloud.packet.Packet;
import me.robbyblue.scratch4j.cloud.packet.packets.PacketHandshake;
import me.robbyblue.scratch4j.cloud.packet.packets.PacketSetVariable;

public class ScratchCloudSession {

	private ScratchSession session;
	private int projectID;
	private ScratchCloudSocket socket;
	private ArrayList<ScratchCloudListener> listeners = new ArrayList<ScratchCloudListener>();

	public ScratchCloudSession(ScratchSession session, int projectID) {
		this.session = session;
		this.projectID = projectID;
		connect();
	}

	private void connect() {
		try {
			Map<String, String> header = new HashMap<String, String>();
			header.put("cookie", "scratchsessionsid=" + this.session.getSessionid() + ";");
			header.put("origin", "https://scratch.mit.edu");

			ScratchCloudSocket socket = new ScratchCloudSocket(URI.create("ws://clouddata.scratch.mit.edu:80"),
					new Draft_6455(), header, this);
			socket.connectBlocking();
			this.socket = socket;
			sendPacket(new PacketHandshake());
		} catch (InterruptedException e) {
			e.printStackTrace();
			this.connect();
		}
	}

	public void reconnect() {
		ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
		executor.schedule(() -> {
			if (!this.socket.isOpen()) {
				this.socket = null;
				connect();
			}
		}, 1, TimeUnit.SECONDS);
	}

	public void disconnect() {
		this.socket.close();
	}

	private void sendPacket(Packet packet) {
		JSONObject packetJson = new JSONObject();
		packetJson.put("user", this.session.getUsername());
		packetJson.put("project_id", this.projectID);
		packetJson.put("method", packet.getMethod());

		for (Entry<String, String> option : packet.getOptions().entrySet()) {
			packetJson.put(option.getKey(), option.getValue());
		}

		String jsonString = packetJson.toString() + "\n";
		this.socket.send(jsonString);
	}

	public void setVariable(String variable, String value) {
		if (!variable.startsWith("☁")) {
			variable = "☁ " + variable;
		}
		sendPacket(new PacketSetVariable(variable, value));
	}

	public void addListener(ScratchCloudListener listener) {
		this.listeners.add(listener);
	}

	public void removeListener(ScratchCloudListener listener) {
		this.listeners.remove(listener);
	}

	public ArrayList<ScratchCloudListener> getListeners() {
		return listeners;
	}

}

package me.robbyblue.scratch4j.cloud.packet.packets;

import me.robbyblue.scratch4j.cloud.packet.Packet;

public class PacketHandshake extends Packet {

	public PacketHandshake() {
		this.method = "handshake";
	}

}

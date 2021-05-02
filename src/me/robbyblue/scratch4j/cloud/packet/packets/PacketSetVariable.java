package me.robbyblue.scratch4j.cloud.packet.packets;

import me.robbyblue.scratch4j.cloud.packet.Packet;

public class PacketSetVariable extends Packet {

	public PacketSetVariable(String variable, String value) {
		this.method = "set";
		this.addOption("name", variable);
		this.addOption("value", value);
	}

}

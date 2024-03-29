/* $Id$
 * Created on 28.10.2003
 */
package com.key.tools;

import com.key.socketdata.*;

import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * 
 * �� OSCMessages ��ǲ��ע��� listeners
 * Dispatch OSCMessages to registered listeners.
 * 
 * @author 
 *
 */

public class OSCPacketDispatcher {
	// use Hashtable for JDK1.1 compatability
	private Hashtable<String, OSCListener> addressToClassTable = new Hashtable<String, OSCListener>();
	
	/**
	 * 
	 */
	public OSCPacketDispatcher() {
		super();
	}

	public void addListener(String address, OSCListener listener) {
		addressToClassTable.put(address, listener);
	}
	
	public void dispatchPacket(OSCPacket packet) {
		if (packet instanceof OSCBundle)
			dispatchBundle((OSCBundle) packet);
		else
			dispatchMessage((OSCMessage) packet);
	}
	
	public void dispatchPacket(OSCPacket packet, Date timestamp) {
		if (packet instanceof OSCBundle)
			dispatchBundle((OSCBundle) packet);
		else
			dispatchMessage((OSCMessage) packet, timestamp);
	}
	
	private void dispatchBundle(OSCBundle bundle) {
		Date timestamp = bundle.getTimestamp();
		OSCPacket[] packets = bundle.getPackets();
		for (int i = 0; i < packets.length; i++) {
			dispatchPacket(packets[i], timestamp);
		}
	}
	
	private void dispatchMessage(OSCMessage message) {
		dispatchMessage(message, null);
	}
	
	private void dispatchMessage(OSCMessage message, Date time) {
		Enumeration<String> keys = addressToClassTable.keys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			// this supports the OSC regexp facility, but it
			// only works in JDK 1.4, so don't support it right now
			// if (key.matches(message.getAddress())) {
			if (key.equals(message.getAddress())) {
				OSCListener listener = addressToClassTable.get(key);
				listener.acceptMessage(time, message);
			}
		}
	}
}

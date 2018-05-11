/**
 * 
 */
package com.skc.snmp.agent;

import java.io.IOException;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.PDUv1;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.IpAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.transport.DefaultUdpTransportMapping;

/**
 * @author sitakant
 *
 */
public class SnmpTrapSender {
	
	private static final String COMMUNITY = "public";
	private static final String IPADDRESS = "127.0.0.1";
	private static final String OID = ".1.3.6.1.2.1.1.8";
	private static final String PORT = "162";
	
	
	public static void main(String[] args) throws IOException {
		SnmpTrapSender sender = new SnmpTrapSender();
		sender.sendTrapV1();
	}

	private void sendTrapV1() throws IOException {
		TransportMapping transportMapping = new DefaultUdpTransportMapping();
		transportMapping.listen();
		
		
		CommunityTarget communityTarget = new CommunityTarget();
		communityTarget.setCommunity(new OctetString(COMMUNITY));
		communityTarget.setVersion(SnmpConstants.version1);
		communityTarget.setAddress(new UdpAddress(IPADDRESS+"/"+PORT));
		communityTarget.setTimeout(5000);
		communityTarget.setRetries(2);
		
		PDUv1 pdUv1 = new PDUv1();
		pdUv1.setType(PDU.V1TRAP);
		pdUv1.setEnterprise(new OID(OID));
		pdUv1.setGenericTrap(PDUv1.ENTERPRISE_SPECIFIC);
		pdUv1.setSpecificTrap(1);
		pdUv1.setAgentAddress(new IpAddress(IPADDRESS));
		
		
		Snmp snmp = new Snmp(transportMapping);
		System.out.println("Sending v1 trap ---> ");
		snmp.send(pdUv1, communityTarget);
		snmp.close();
	}
}

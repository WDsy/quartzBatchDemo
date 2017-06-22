package com.sightcorner.quartz.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * @author wdsy
 * @date 2017年1月22日
 */
public class ScheduleTaskNetwork {

	private final static ScheduleTaskNetwork instance = new ScheduleTaskNetwork();
	private Set<String> ips = new HashSet<>();
	

	public static Set<String> getIPs() {
		return new HashSet<String>(instance.ips);
	}
	

	public static boolean isExist(String ip) {
		return instance.ips.contains(ip);
	}
	
	
	private ScheduleTaskNetwork() {
		this.initialNetworkInterface();
	}

	private void initialNetworkInterface()  {
		
		try {
			this.proceed();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}

	private void proceed() throws SocketException {
		Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
		NetworkInterface networkInterface;
		Enumeration<InetAddress> inetAddresses;
		InetAddress inetAddress;
		
		while(networkInterfaces.hasMoreElements()) {
			networkInterface = networkInterfaces.nextElement();
			inetAddresses = networkInterface.getInetAddresses();
			
			while(inetAddresses.hasMoreElements()) {
				inetAddress = inetAddresses.nextElement();
				ips.add(inetAddress.getHostAddress());
			}
		}
	}
	
}

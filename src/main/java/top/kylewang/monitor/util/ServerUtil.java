package top.kylewang.monitor.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @author KyleWang
 * @version 1.0
 * @date 2019年06月18日
 */
public class ServerUtil {

	public static String getIp() {
		// 根据网卡取本机配置的IP
		try {
			String siteLocalIp = null;
			Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = netInterfaces.nextElement();
				if (!ni.isLoopback() && !ni.isVirtual() && ni.isUp()) {
					Enumeration<InetAddress> ips = ni.getInetAddresses();
					while (ips.hasMoreElements()) {
						InetAddress ia = ips.nextElement();
						if (ia instanceof Inet4Address) {
							if (!ia.isAnyLocalAddress() && !ia.isLinkLocalAddress() && !ia.isLoopbackAddress()
									&& !ia.isSiteLocalAddress()) {
								return ia.getHostAddress();
							} else if (siteLocalIp == null && ia.isSiteLocalAddress()) {
								siteLocalIp = ia.getHostAddress();
							}
						}
					}
				}
			}
			if (siteLocalIp != null) {
				return siteLocalIp;
			}
		} catch (Exception e) {
		}
		// 获取本地的IP地址
		try {
			InetAddress address = InetAddress.getLocalHost();
			return address.getHostAddress();
		} catch (Exception e) {
		}
		return null;
	}

}

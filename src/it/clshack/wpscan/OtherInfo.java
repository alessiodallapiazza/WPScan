package it.clshack.wpscan;

public class OtherInfo {
	public OtherInfo() {
	}

	public String getServerName(String site) {
		return Util.getHeaders(site, "Server");
	}

	public String getPhpVersion(String site) {
		return Util.getHeaders(site, "X-Powered-By");
	}

	public boolean getFileRobots(String site) {
		if (Util.getResponseCode(site + "/robots.txt")) {
			return true;
		}
		return false;
	}

	public boolean getBkFileWpConfig(String site) {
		if (Util.getResponseCode(site + "/wp-config.php~")
				|| Util.getResponseCode(site + "/wp-config.php.bak")) {
			return true;
		}
		return false;
	}

}

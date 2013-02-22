package it.clshack.wpscan;

public class Server {
	public Server() {
	}

	public String getServerName(String site) {
		return Util.getHeadersServer(site);
	}

}

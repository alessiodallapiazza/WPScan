package it.clshack.wpscan;

import android.content.Context;

public class WordpressVulnerabilities {

	private String version;

	public WordpressVulnerabilities() {
	}

	public WordpressVulnerabilities(String version) {
		this.version = version;
	}

	public String getVuln(Context ctx) {
		String csv = Util.readTextFile(ctx, "wp_vuln.csv");

		String[] lines = csv.split("\n");

		String header = "<table border=1><tbody><tr><td>Title:</td><td>Reference:</td><td>Type:</td></tr>";
		String footer = "</tbody></table>";

		boolean add = false;
		StringBuffer r = new StringBuffer();
		for (String line : lines) {
			String[] data = line.split(";");
			if (data[0].equals(version)) {
				r.append("<tr><td>" + data[1] + "</td><td>" + data[2] + "</td><td>"
						+ data[3] + "</td></tr>");
				if (!add)
					add = true;
			}
		}
		if (add)
			return header + r.toString() + footer;
		else
			return null;
	}
}

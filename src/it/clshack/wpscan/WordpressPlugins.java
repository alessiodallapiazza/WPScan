package it.clshack.wpscan;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;

public class WordpressPlugins {
	
	public WordpressPlugins()
	{}
	
	public ArrayList<String> enumPluginsHTML(String site) {
		Pattern p;
		Matcher m;
		String html = Util.getHtml(site);
		ArrayList<String> plugins = new ArrayList<String>();
		if (html != null && !html.equals("")) {
			p = Pattern.compile("wp-content/plugins/(.*?)/");
			m = p.matcher(html);
			while (m.find()) {
				String r = (String) m.group(1).trim();
				if (!plugins.contains(r)) {
					plugins.add(r);
				}
			}
		}
		return plugins;
	}

	public ArrayList<String> enumPluginsBruteFroce(Context ctx, String site) {
		String plugins = Util.readTextFile(ctx, "plugins.txt");
		String[] lines = plugins.split("\n");
		ArrayList<String> r = new ArrayList<String>();
		for (String line : lines) {
			if (Util.getResponseCode(site + "/wp-content/plugins/" + line)) {
				if (!r.contains(line)) {
					r.add(line);
				}
			}
		}
		return r;
	}

	public String enumPluginsVuln(Context ctx, ArrayList<String> plugins) {
		String csv = Util.readTextFile(ctx, "plugins_vuln.csv");
		String[] lines = csv.split("\n");
		StringBuffer result = new StringBuffer();

		String header = "<table border=1><tbody><tr><td>Title:</td><td>Reference:</td><td>Type:</td></tr>";
		String footer = "</tbody></table>";
		boolean add = false;

		if (plugins != null && !plugins.isEmpty()) {
			for (String r : plugins) {
				for (String line : lines) {
					String[] data = line.split(";");
					if (data[0].equals(r)) {
						result.append("<tr><td>" + data[1] + "</td><td>"
								+ data[2] + "</td><td>" + data[4]
								+ "</td></tr>");
						if (!add)
							add = true;
					}
				}
			}
		}
		if (add)
			return header + result.toString() + footer;
		else
			return null;
	}
}

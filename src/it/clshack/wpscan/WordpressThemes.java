package it.clshack.wpscan;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;

public class WordpressThemes {

	public WordpressThemes() {
	}

	public ArrayList<String> enumThemesHTML(String site) {
		Pattern p;
		Matcher m;
		String html = Util.getHtml(site);
		ArrayList<String> plugins = new ArrayList<String>();
		if (html != null && !html.equals("")) {
			p = Pattern.compile("wp-content/themes/(.*?)/");
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

	public ArrayList<String> enumThemesBruteFroce(Context ctx, String site) {
		String plugins = Util.readTextFile(ctx, "themes.txt");
		String[] lines = plugins.split("\n");
		ArrayList<String> r = new ArrayList<String>();
		for (String line : lines) {
			if (Util.getResponseCode(site + "/wp-content/themes/" + line)) {
				if (!r.contains(line)) {
					r.add(line);
				}
			}
		}
		return r;
	}

	public String enumThemesVuln(Context ctx, ArrayList<String> themes) {
		String csv = Util.readTextFile(ctx, "themes_vuln.csv");
		String[] lines = csv.split("\n");
		StringBuffer result = new StringBuffer();

		String header = "<table border=1><tbody><tr><td>Theme:</td><td>Title:</td><td>Url:</td></tr>";
		String footer = "</tbody></table>";
		boolean add = false;

		if (themes != null && !themes.isEmpty()) {
			for (String r : themes) {
				for (String line : lines) {
					String[] data = line.split(";");
					if (data[0].equals(r)) {
						result.append("<tr><td>" + data[5] + "</td><td>"
								+ data[6] + "</td><td>" + data[7]
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

	public ArrayList<String> enumTimthumbs(Context ctx, String site) {
		String timthumbs = Util.readTextFile(ctx, "timthumbs.txt");
		String[] lines = timthumbs.split("\n");
		ArrayList<String> r = new ArrayList<String>();
		for (String line : lines) {
			if (Util.getResponseCode(site + "/wp-content/" + line)) {
				if (!r.contains(line)) {
					r.add(line);
				}
			}
		}
		return r;
	}
}

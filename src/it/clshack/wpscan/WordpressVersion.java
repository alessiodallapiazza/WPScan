package it.clshack.wpscan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordpressVersion {

	public String getWordpressVersion(String site) {
		String html;
		Pattern p;
		Matcher m;

		html = Util.getHtml(site + "/wp-links-opml.php");
		if (html != null && ! html.equals("") ) {
			p = Pattern.compile("<!-- generator=\"WordPress\\/(.*?)\" -->");
			m = p.matcher(html);
			if (m.find()) {
				return (m.group(1).toString());
			}
		}

		html = Util.getHtml(site);
		if (html != null && ! html.equals("")) {
			p = Pattern
					.compile("<meta name=\"generator\" content=\"WordPress (.*?)\" />");
			m = p.matcher(html);
			if (m.find()) {
				return (m.group(1).toString());
			}
		}

		html = Util.getHtml(site + "/feed/");
		if (html != null && ! html.equals("")) {
			p = Pattern
					.compile("<generator>http://wordpress.org/?v=(.*?)</generator>");
			m = p.matcher(html);
			if (m.find()) {
				return (m.group(1).toString());
			}
		}
		
		if (Util.getResponseCode(site + "/readme.html")) {
			html = Util.getHtml(site + "/readme.html");
			if (html != null && ! html.equals("")) {
				p = Pattern.compile("<br /> Version (.*?)</h1>");
				m = p.matcher(html);
				if (m.find()) {
					return (m.group(1).toString());
				}
			}
		}
		
		html = Util.getHtml(site + "/sitemap.xml");
		if (html != null && ! html.equals("")) {
			p = Pattern.compile("<!-- generator=\"wordpress/(.*?)\" -->");
			m = p.matcher(html);
			if (m.find()) {
				return (m.group(1).toString());
			}
		}

		if (Util.getResponseCode((site + "/wp-links-opml.php"))) {
			return "unknown";
		}
		return null;
	}
}

package it.clshack.wpscan;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.os.Environment;

public class Util {

	public static String getLocationRedirect(String siteAndPage) {
		HttpURLConnection con = null;
		URL url;
		try {
			url = new URL(siteAndPage);
			con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.14 Safari/537.17");
			con.setInstanceFollowRedirects(false);
			con.setReadTimeout(10000);
			con.setConnectTimeout(15000);
			con.setRequestMethod("GET");
			con.setDoInput(true);
			con.addRequestProperty("Referer", "https://www.google.com");
			con.connect();
			if (con.getResponseCode() == 301) {
				Map<String, List<String>> headers = con.getHeaderFields();
				if (headers.containsKey("Location"))
					return con.getHeaderField("Location");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static boolean getResponseCode(String siteAndPage) {
		HttpURLConnection con = null;
		URL url;
		try {
			url = new URL(siteAndPage);
			con = (HttpURLConnection) url.openConnection();
			con.setInstanceFollowRedirects(false);
			con.setReadTimeout(10000);
			con.setConnectTimeout(15000);
			con.setRequestMethod("GET");
			con.setDoInput(true);
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.14 Safari/537.17");
			con.addRequestProperty("Referer", "https://www.google.com");
			con.connect();
			if (con.getResponseCode() == 200)
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static String getHtml(String site) {
		HttpURLConnection con = null;
		URL url;
		InputStream is = null;
		String line = new String();
		StringBuilder total = new StringBuilder();
		BufferedReader rd = null;
		try {
			url = new URL(site);
			con = (HttpURLConnection) url.openConnection();
			con.setInstanceFollowRedirects(false);
			con.setReadTimeout(10000);
			con.setConnectTimeout(15000);
			con.setRequestMethod("GET");
			con.setDoInput(true);
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.14 Safari/537.17");
			con.addRequestProperty("Referer", "https://www.google.com");
			con.connect();
			is = con.getInputStream();
			if (is != null) {
				rd = new BufferedReader(new InputStreamReader(is));
				while ((line = rd.readLine()) != null) {
					total.append(line);
				}
			}
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return total.toString();
	}

	public static String readTextFile(Context context, String fileName) {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(context.getAssets()
					.open(fileName)));
			String line;
			StringBuilder buffer = new StringBuilder();
			while ((line = in.readLine()) != null)
				buffer.append(line).append('\n');
			in.close();
			return buffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static boolean isOnline(Context ctx) {
		ConnectivityManager cm = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		return cm.getActiveNetworkInfo() != null
				&& cm.getActiveNetworkInfo().isConnectedOrConnecting();
	}

	public static boolean writeResult(String line, String site, boolean append) {
		File directory = new File(Environment.getExternalStorageDirectory()
				+ File.separator + "WPScan");
		File output = new File(Environment.getExternalStorageDirectory()
				+ File.separator + "WPScan" + File.separator + site + ".html");
		FileWriter fWriter;

		if (!directory.exists()) {
			directory.mkdirs();
		}
		try {
			fWriter = new FileWriter(output, append);
			fWriter.write(line);
			fWriter.flush();
			fWriter.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean fileExist(String file)
	{
		File output = new File(file);
		if(output.exists())
			return true;
		return false;
	}
	public static void savePreferences(String propriety, boolean value,
			Context ctx) {
		SharedPreferences userDetails = ctx.getSharedPreferences("wpScan",
				Context.MODE_PRIVATE);
		Editor edit = userDetails.edit();
		edit.putBoolean(propriety, value);
		edit.commit();
	}

	public static boolean getPreferences(String propriety, Context ctx) {
		SharedPreferences prefs = ctx.getSharedPreferences("wpScan",
				Context.MODE_PRIVATE);
		return prefs.getBoolean(propriety, false);
	}
}

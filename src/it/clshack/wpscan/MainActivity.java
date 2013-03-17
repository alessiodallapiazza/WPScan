package it.clshack.wpscan;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			mViewPager.setCurrentItem(3);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(R.string.title_section1);
			case 1:
				return getString(R.string.title_section2);
			case 2:
				return getString(R.string.title_section3);
			}
			return null;
		}
	}

	public static class DummySectionFragment extends Fragment {
		public static final String ARG_SECTION_NUMBER = "section_number";
		public ProgressBar myProgressBar;
		public String version = "", site = "", site_file = "";
		public ProgressDialog progressBar;
		public ArrayList<String> plugins, themes, timthumbs;
		public WordpressThemes wordpressThemes = new WordpressThemes();
		public WordpressPlugins wordpressPlugins = new WordpressPlugins();
		public OtherInfo otherinfo = new OtherInfo();

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
			case 1: {
				return loadScan(inflater.inflate(R.layout.activity_scan,
						container, false));
			}
			case 2:
				return loadSettings(inflater.inflate(
						R.layout.activity_settings, container, false));
			case 3:
				return inflater.inflate(R.layout.activity_about, container,
						false);
			default:
				return inflater.inflate(R.layout.activity_about, container,
						false);
			}
		}

		public View loadScan(View scan_view) {
			((Button) scan_view.findViewById(R.id.search_button))
					.setOnClickListener(OnclickListenerActivity);
			((Button) scan_view.findViewById(R.id.see_report))
					.setVisibility(View.VISIBLE);
			((Button) scan_view.findViewById(R.id.send_report))
					.setVisibility(View.VISIBLE);
			((Button) scan_view.findViewById(R.id.see_report))
					.setOnClickListener(OnclickListenerActivity);
			((Button) scan_view.findViewById(R.id.send_report))
					.setOnClickListener(OnclickListenerActivity);
			return scan_view;
		}

		public View loadSettings(View settings_view) {
			((CheckBox) settings_view.findViewById(R.id.enum_autor))
					.setOnClickListener(OnclickListenerActivity);
			((CheckBox) settings_view.findViewById(R.id.enum_timthumbs))
					.setOnClickListener(OnclickListenerActivity);
			((CheckBox) settings_view.findViewById(R.id.enum_plugins))
					.setOnClickListener(OnclickListenerActivity);
			((CheckBox) settings_view.findViewById(R.id.enum_themes))
					.setOnClickListener(OnclickListenerActivity);
			((CheckBox) settings_view.findViewById(R.id.enum_autor))
					.setChecked(Util.getPreferences("enum_autor", getActivity()
							.getApplicationContext()));
			((CheckBox) settings_view.findViewById(R.id.enum_timthumbs))
					.setChecked(Util.getPreferences("enum_timthumbs",
							getActivity().getApplicationContext()));
			((CheckBox) settings_view.findViewById(R.id.enum_plugins))
					.setChecked(Util.getPreferences("enum_plugins",
							getActivity().getApplicationContext()));
			((CheckBox) settings_view.findViewById(R.id.enum_themes))
					.setChecked(Util.getPreferences("enum_themes",
							getActivity().getApplicationContext()));
			return settings_view;
		}

		View.OnClickListener OnclickListenerActivity = new View.OnClickListener() {
			public void onClick(View view) {
				switch (view.getId()) {
				case R.id.search_button: {
					launch();
					break;
				}
				case R.id.enum_autor: {
					Util.savePreferences(
							"enum_autor",
							((CheckBox) getActivity().findViewById(
									R.id.enum_autor)).isChecked(),
							getActivity().getApplicationContext());
					break;
				}
				case R.id.enum_timthumbs: {
					Util.savePreferences(
							"enum_timthumbs",
							((CheckBox) getActivity().findViewById(
									R.id.enum_timthumbs)).isChecked(),
							getActivity().getApplicationContext());
					break;
				}
				case R.id.enum_plugins: {
					Util.savePreferences(
							"enum_plugins",
							((CheckBox) getActivity().findViewById(
									R.id.enum_plugins)).isChecked(),
							getActivity().getApplicationContext());
					break;
				}
				case R.id.enum_themes: {
					Util.savePreferences(
							"enum_themes",
							((CheckBox) getActivity().findViewById(
									R.id.enum_themes)).isChecked(),
							getActivity().getApplicationContext());
					break;
				}
				case R.id.see_report: {
					startBrowser();
					break;
				}
				case R.id.send_report: {
					startEmail();
					break;
				}
				}
			}
		};

		private void mLockScreenRotation(boolean lock) {
			if (lock) {
				switch (getResources().getConfiguration().orientation) {
				case Configuration.ORIENTATION_PORTRAIT:
					getActivity().setRequestedOrientation(
							ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
					break;
				case Configuration.ORIENTATION_LANDSCAPE:
					getActivity().setRequestedOrientation(
							ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
					break;
				}
			} else {
				getActivity().setRequestedOrientation(
						ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
			}
		}

		public void startEmail() {
			site = ((EditText) getActivity().findViewById(R.id.search_info))
					.getText().toString();
			try {
				site_file = new URI(site).getHost();
			} catch (Exception e) {
				e.printStackTrace();
			}
			String file = Environment.getExternalStorageDirectory()
					+ File.separator + "WPScan" + File.separator + site_file
					+ ".html";
			if (Util.fileExist(file)) {
				Intent i = new Intent(Intent.ACTION_SEND);
				i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
				i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(file)));
				i.setType("text/html");
				startActivity(Intent.createChooser(i, "Send mail"));
			}
		}

		public void startBrowser() {
			site = ((EditText) getActivity().findViewById(R.id.search_info))
					.getText().toString();
			try {
				site_file = new URI(site).getHost();
			} catch (Exception e) {
				e.printStackTrace();
			}
			String file = Environment.getExternalStorageDirectory()
					+ File.separator + "WPScan" + File.separator + site_file
					+ ".html";
			Log.d("FILE", file + "");
			if (Util.fileExist(file)) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(new File(file)), "text/html");
				startActivity(intent);
			}
		}

		public void launch() {
			((EditText) getActivity().findViewById(R.id.search_info))
					.setError(null);
			((TextView) getActivity().findViewById(R.id.error_text))
					.setText("");
			site = ((EditText) getActivity().findViewById(R.id.search_info))
					.getText().toString();
			if (site.length() == 0) {
				((EditText) getActivity().findViewById(R.id.search_info))
						.setError(getString(R.string.error_search_editext));
				return;
			}
			if (!URLUtil.isValidUrl(site)) {
				((EditText) getActivity().findViewById(R.id.search_info))
						.setError(getString(R.string.error_url_not_valid));
				return;
			}
			if (!Util.isOnline(getActivity().getApplicationContext())) {
				((TextView) getActivity().findViewById(R.id.error_text))
						.setText(getString(R.string.error_search_notonline));
				return;
			}
			version = "";
			try {
				site_file = new URI(site).getHost();
			} catch (Exception e) {
				e.printStackTrace();
				((EditText) getActivity().findViewById(R.id.search_info))
						.setError(getString(R.string.error_url_not_valid));
				return;
			}
			progressBar = new ProgressDialog(getActivity());
			progressBar.setCancelable(false);
			progressBar.setMessage(getString(R.string.progress));
			progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressBar.setProgress(0);
			progressBar.setMax(100);
			progressBar.show();
			Thread start_scan = new Thread(start_check);
			start_scan.start();
			((TextView) getActivity().findViewById(R.id.error_text))
					.setText(getString(R.string.finish));
		}

		private Runnable start_check = new Runnable() {
			@Override
			public void run() {
				try {
					mLockScreenRotation(true);
					progressBar.setProgress(10);
					if (!Util.getResponseCode(site)) {
						return;
					}
					progressBar.setProgress(20);
					if (!checkVersion()) {
						return;
					}
					progressBar.setProgress(30);
					checkVuln();
					progressBar.setProgress(35);
					enumAuthor();
					progressBar.setProgress(50);
					enumTimthumbs();
					enumThemes();
					enumThemesVuln();
					progressBar.setProgress(60);
					enumPluginsBruteFroce();
					enumPluginsVuln();
					progressBar.setProgress(70);
					enumPluginsHTML();
					enumPluginsVuln();
					progressBar.setProgress(80);
					enumThemesHTML();
					enumThemesVuln();
					enumServer();
					enumRobots();
					enumPhp();
					enumBkWpconfig();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					myHandle.sendEmptyMessage(1035);
				}
			}

			Handler myHandle = new Handler(new Handler.Callback() {
				@Override
				public boolean handleMessage(Message msg) {
					switch (msg.what) {
					case 1035: {
						mLockScreenRotation(false);
						progressBar.dismiss();
						break;
					}
					}
					return false;
				}
			});

			public boolean checkVersion() {
				String v = new WordpressVersion().getWordpressVersion(site);
				if (v != null && !v.equals("")) {
					version = v;
					Util.writeResult(getString(R.string.html_head_version)
							+ "<h2>" + v + "</h2>", site_file, false);
					return true;
				}
				Util.writeResult(getString(R.string.no_wordpress), site_file,
						false);
				return false;
			}

			public void checkVuln() {
				if (!version.equals("unknown")) {
					String vuln = new WordpressVulnerabilities(version)
							.getVuln(getActivity().getApplicationContext());
					if (vuln != null && !vuln.equals("")) {
						Util.writeResult(vuln, site_file, true);
					}
				}
			}

			public void enumAuthor() {
				if (Util.getPreferences("enum_autor", getActivity()
						.getApplicationContext())) {
					StringBuffer r = new StringBuffer();
					for (int i = 1; i <= 10; i++) {
						String author = Util.getLocationRedirect(site
								+ "/?author=" + i);
						if (!author.equals("")) {
							try {
								author = new URI(author).getPath()
										.replace("/author/", "")
										.replace("/", "");
							} catch (Exception e) {
								e.printStackTrace();
							}
							if (!author.trim().equals(""))
								r.append("<li>" + author + "</li>");
						}
					}
					if (!r.toString().equals("")) {
						Util.writeResult(getString(R.string.html_author),
								site_file, true);
						Util.writeResult(r.toString(), site_file, true);
					}
				}
			}

			public void enumThemes() {
				if (Util.getPreferences("enum_themes", getActivity()
						.getApplicationContext())) {
					themes = wordpressThemes.enumThemesBruteFroce(getActivity()
							.getApplicationContext(), site);
					if (themes != null && !themes.isEmpty()) {
						Util.writeResult(getString(R.string.html_themes),
								site_file, true);
						for (String r : timthumbs) {
							Util.writeResult("<li>" + r + "</li>", site_file,
									true);
						}
					}
				}
			}

			public void enumTimthumbs() {
				if (Util.getPreferences("enum_timthumbs", getActivity()
						.getApplicationContext())) {
					timthumbs = wordpressThemes.enumTimthumbs(getActivity()
							.getApplicationContext(), site);
					if (timthumbs != null && !timthumbs.isEmpty()) {
						Util.writeResult("<br>", site_file, true);
						Util.writeResult(getString(R.string.html_timthumbs),
								site_file, true);
						for (String r : timthumbs) {
							Util.writeResult("<li>" + r + "</li>", site_file,
									true);
						}
					}
				}
			}

			public void enumPluginsBruteFroce() {
				if (Util.getPreferences("enum_plugins", getActivity()
						.getApplicationContext())) {
					plugins = wordpressPlugins.enumPluginsBruteFroce(
							getActivity().getApplicationContext(), site);
					if (plugins != null && !plugins.isEmpty()) {
						Util.writeResult(getString(R.string.html_plugins),
								site_file, true);
						for (String r : plugins) {
							Util.writeResult("<li>" + r + "</li>", site_file,
									true);
						}
					}
				}
			}

			public void enumPluginsHTML() {
				plugins = wordpressPlugins.enumPluginsHTML(site);
				if (plugins != null && !plugins.isEmpty()) {
					Util.writeResult("<br>", site_file, true);
					Util.writeResult(getString(R.string.html_plugins),
							site_file, true);
					for (String r : plugins) {
						Util.writeResult("<li>" + r + "</li>", site_file, true);
					}
				}
			}

			public void enumPluginsVuln() {
				String r = wordpressPlugins.enumPluginsVuln(getActivity()
						.getApplication(), plugins);
				if (r != null && !r.equals("")) {
					Util.writeResult("<br>", site_file, true);
					Util.writeResult("<li>"
							+ getString(R.string.html_plugins_vuln) + r
							+ "</li>", site_file, true);
				}
			}

			public void enumThemesHTML() {
				themes = wordpressThemes.enumThemesHTML(site);
				if (themes != null && !themes.isEmpty()) {
					Util.writeResult("<br>", site_file, true);
					Util.writeResult(getString(R.string.html_themes),
							site_file, true);
					for (String r : themes) {
						Util.writeResult("<li>" + r + "</li>", site_file, true);
					}
				}
			}

			public void enumThemesVuln() {
				String r = wordpressThemes.enumThemesVuln(getActivity()
						.getApplication(), themes);
				if (r != null && !r.equals("")) {
					Util.writeResult("<br>", site_file, true);
					Util.writeResult("<li>"
							+ getString(R.string.html_themes_vuln) + r
							+ "</li>", site_file, true);
				}
			}

			public void enumServer() {
				String serverName = otherinfo.getServerName(site);
				if (serverName != null && !serverName.trim().equals("")) {
					Util.writeResult("<br>", site_file, true);
					Util.writeResult(getString(R.string.html_server_version)
							+ serverName, site_file, true);
				}
			}
			
			public void enumPhp() {
				String phpversion = otherinfo.getPhpVersion(site);
				if (phpversion != null && !phpversion.trim().equals("")) {
					Util.writeResult("<br>", site_file, true);
					Util.writeResult(getString(R.string.html_php_version)
							+ phpversion, site_file, true);
				}
			}
			
			public void enumBkWpconfig() {
				if (otherinfo.getBkFileWpConfig(site)) {
					Util.writeResult("<br>", site_file, true);
					Util.writeResult(getString(R.string.html_bk_founs),
							site_file, true);
				}
			}

			public void enumRobots() {
				if (otherinfo.getFileRobots(site)) {
					Util.writeResult("<br>", site_file, true);
					Util.writeResult(getString(R.string.html_robots_founs),
							site_file, true);
				}
			}
		};
	}
}

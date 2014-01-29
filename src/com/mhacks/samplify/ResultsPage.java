package com.mhacks.samplify;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.webkit.WebView;

public class ResultsPage extends Activity {

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_results_page);
		WebView webview = new WebView(this);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.setHorizontalScrollBarEnabled(false);
		webview.getSettings().setUserAgentString(MainActivity.userAgent);

		webview.loadUrl("file:"
				+ Environment.getExternalStorageDirectory().getPath()
				+ File.separator + "text.html");
		webview.setInitialScale(100);
		setContentView(webview);
	}

	protected void onStop() {
		super.onStop();
		MainActivity.setResultsShown(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.results_page, menu);
		return true;
	}

}

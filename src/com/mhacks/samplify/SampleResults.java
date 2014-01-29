package com.mhacks.samplify;

import java.util.ArrayList;

import com.mhacks.samplify.R;

import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SampleResults extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sample_results);
		Intent intent = getIntent();
		//ArrayList<Card> cards = intent.getParcelableArrayListExtra(MainActivity.EXTRA_SONG_INFO);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sample_results, menu);
		return true;
	}

}

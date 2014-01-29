package com.mhacks.samplify;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.gracenote.mmid.MobileSDK.GNConfig;
import com.gracenote.mmid.MobileSDK.GNOperations;
import com.gracenote.mmid.MobileSDK.GNSearchResponse;
import com.gracenote.mmid.MobileSDK.GNSearchResult;
import com.gracenote.mmid.MobileSDK.GNSearchResultReady;

public class MainActivity extends Activity {

	public static final String EXTRA_SONG_INFO = "com.example.samplify2.SONG_INFO";

	public static String PACKAGE_NAME;
	public static String userAgent = "";
	public static boolean resultsShown = false;

	private Random rand;
	private String songUrl;
	private ImageButton button;
	private ImageView mutedButton;
	private ImageView spinImage;
	private GNConfig config;
	private ImageButton record;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		PACKAGE_NAME = getApplicationContext().getPackageName();
		setContentView(R.layout.activity_main);

		config = GNConfig.init("14950656-FC18FFA77D7818DC59F64E26E2C1B4C5",
				this.getApplicationContext());
		record = (ImageButton) findViewById(R.id.record);
		spinImage = (ImageView) findViewById(R.id.pressedrecord);
		button = (ImageButton) findViewById(R.id.button); // Record button
		button.setClickable(true);
		mutedButton = (ImageView) findViewById(R.id.muted_button);
		mutedButton.setVisibility(View.INVISIBLE);
		// enable cover Art
		config.setProperty("content.coverArt", "1");
		rand = new Random();
		if (!isNetworkAvailable()) {
			noNetworkToast();
		}
		// The below code is for getting current song
		IntentFilter iF = new IntentFilter();
		iF.addAction("com.android.music.metachanged");
		iF.addAction("com.android.music.playstatechanged");

		registerReceiver(mReceiver, iF);

	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (!resultsShown) {
				resultsShown = true;
				record.setClickable(false);
				button.setClickable(false);
				intent.getAction();
				String artist = intent.getStringExtra("artist");
				String track = intent.getStringExtra("track");
				// do something with that info from above^
				randomGenerateUserAgent();
				Title task = new Title();
				task.execute(track, artist);
				record.setClickable(true);
				button.setClickable(true);
			}
		}
	};

	private void randomGenerateUserAgent() {
		StringBuilder sb = new StringBuilder();
		sb.append("Mozilla/5.0 ");
		// add a device name
		int deviceName = rand.nextInt(5);
		if (deviceName == 0)
			sb.append("(Windows NT 6.1) ");
		else if (deviceName == 1 || deviceName == 2) {
			sb.append("(Macintosh; Intel Mac OS X 10_");
			sb.append(rand.nextInt(10) + "_");
			sb.append(rand.nextInt(10) + ") ");
		} else if (deviceName == 3)
			sb.append("(X11; Linuz x86_64) ");
		else if (deviceName == 4)
			sb.append("(Windows NT 6.2; WOW64) ");
		// add that next weird part, more random too
		sb.append("AppleWebKit/535." + rand.nextInt(100) + " ");
		// always the same
		sb.append("(KHTML, like Gecko) ");
		// This part changes a lot
		sb.append("Chrome/" + (rand.nextInt(10) + 20) + ".0."
				+ (rand.nextInt(1000) + 1000) + "." + (rand.nextInt(90) + 10)
				+ " ");
		// This part is mostly the same
		sb.append("Safari/537." + rand.nextInt(50));
		/*
		 * //the below code works but might not be best way for( int i = 0; i <
		 * (rand.nextInt(40)+23); i++) {
		 * sb.append(AB.charAt(rand.nextInt(AB.length()))); }
		 */
		userAgent = sb.toString();
	}

	private void noNetworkToast() {
		Context context = getApplicationContext();
		CharSequence text = "This app requires network connection.";
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent browserIntent;
		switch (item.getItemId()) {
		case R.id.action_about:
			browserIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://www.samplifymobile.com/app/about.html"));
			startActivity(browserIntent);
			return true;
		case R.id.action_legal:
			browserIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://www.samplifymobile.com/app/legal.html"));
			startActivity(browserIntent);
			return true;
		}
		return false;
	}

	public void fingerprint(View view) {
		if (!isNetworkAvailable()) {
			noNetworkToast();
		} else if (!resultsShown) {
			// mutedButton = (ImageView) findViewById(R.id.muted_button);
			record.setClickable(false);

			button.setClickable(false);
			button.setVisibility(View.INVISIBLE);
			mutedButton.setVisibility(View.VISIBLE);
			// mutedButton.setVisibility(VISIBLE);
			spinImage.startAnimation(AnimationUtils.loadAnimation(this,
					R.anim.rotate_indefinitely));
			RecognizeFromMic task = new RecognizeFromMic();
			task.doFingerprint();
		}
	}

	public void fromLibrary(View view) {
		if (!isNetworkAvailable()) {
			noNetworkToast();
		} else {
			AudioManager mAudioManager = (AudioManager) this
					.getSystemService(Context.AUDIO_SERVICE);
			if (mAudioManager.isMusicActive()) {
				Intent i = new Intent("com.android.music.musicservicecommand");
				i.putExtra("command", "pause");
				MainActivity.this.sendBroadcast(i);
				Intent play = new Intent(
						"com.android.music.musicservicecommand");
				play.putExtra("command", "play");
				MainActivity.this.sendBroadcast(play);
			} else {
				Context context = getApplicationContext();
				CharSequence text = "No Song playing.";
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			}

			// this will be grab current playing song right now
			// Intent i = new Intent(Intent.ACTION_PICK,
			// MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
			// startActivityForResult(i, REQUEST_MEDIA);//REQUEST_MEDIA is some
			// const int to operate with in onActivityResult
		}
	}

	/*
	 * @Override protected void onActivityResult(int requestCode, int
	 * resultCode, Intent data) { super.onActivityResult(requestCode,
	 * resultCode, data); if (requestCode == REQUEST_MEDIA && resultCode ==
	 * RESULT_OK) { audioID = data.getDataString(); MyUri = Uri.parse(audioID);
	 * String[] proj = { MediaStore.Audio.Media._ID,
	 * MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.TITLE,
	 * MediaStore.Audio.Artists.ARTIST }; Cursor tempCursor =
	 * managedQuery(MyUri, null, null, null, null); tempCursor.moveToFirst();
	 * //reset the cursor int col_index=-1; int numSongs=tempCursor.getCount();
	 * int currentNum=0; do{ col_index =
	 * tempCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
	 * artist_name = tempCursor.getString(col_index); col_index =
	 * tempCursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST);
	 * artist_band = tempCursor.getString(col_index); //do something with artist
	 * name here //we can also move into different columns to fetch the other
	 * values currentNum++; }while(tempCursor.moveToNext()); Title task = new
	 * Title(); //new GN.. make a GN Object and put it on something
	 * task.execute(artist_name, artist_band); } }
	 */

	class RecognizeFromMic implements GNSearchResultReady {
		void doFingerprint() {
			GNOperations.recognizeMIDStreamFromMic(this, config);
		}

		public void GNResultReady(GNSearchResult result) {
			// mutedButton.setVisibility(GONE);
			if (result.isFingerprintSearchNoMatchStatus()) {
				Context context = getApplicationContext();
				CharSequence text = "No match found";
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(context, text, duration);

				spinImage.clearAnimation();
				spinImage.setVisibility(View.GONE);
				record.setVisibility(View.VISIBLE);
				mutedButton.setVisibility(View.INVISIBLE);
				button.setVisibility(View.VISIBLE);
				button.setClickable(true);
				record.setClickable(true);
				toast.show();
			} else if (!resultsShown) {
				resultsShown = true;
				randomGenerateUserAgent();
				System.out.println(userAgent);
				GNSearchResponse response = result.getBestResponse();
				songUrl = response.getCoverArt().getUrl();
				Title task = new Title();
				task.execute(response.getTrackTitle(), response.getArtist());
			}
		}
	}

	class Title extends AsyncTask<String, Void, ArrayList<Card>> {

		public String urlEncoder(String aArtist, String aTrack) {
			String artist = cleanString(aArtist);
			String track = cleanString(aTrack);
			
			return "http://whosampled.com/" + artist + "/" + track;
		}
		
		public String cleanString(String s) {
			int paren = s.indexOf(" (");
			int feat = s.indexOf(" feat");
			
			String aString = s;
			
			if (paren < feat && paren != -1) {
				aString = s.substring(0, paren);
			} else if (feat != -1) {
				aString = aString.substring(0, feat);
			}
			
			aString = s.replaceAll("\\s+", "-");
			
			return aString;
		}

		@Override
		protected ArrayList<Card> doInBackground(String... params) {
			SampleCrawler sample = new SampleCrawler();
			SongCrawler s = new SongCrawler();

			System.out.println(urlEncoder(params[0], params[1]));

			sample.set(urlEncoder(params[0], params[1]));

			ArrayList<Card> cards = new ArrayList<Card>();
			ArrayList<Result> results = new ArrayList<Result>();
			Result r1 = new Result();
			String link = "";

			for (Result r : sample.getResults()) {
				results.add(r);
			}

			for (Result r : results) {
				try {
					s.set(r);
					if (!sample.getResults().isEmpty()) {
						link = s.getCard().getOriginalYoutubeLink();
					}
				} catch (Exception e) {
					System.out.println("Error");
				}
				cards.add(s.getCard());
			}

			Card c = new Card(r1);
			if (c.getTitle().equals("")) {
				// fill information from gracenote
				c.setTitle(params[0]);
				c.addArtist(params[1]);
				c.setImageURL(songUrl);
				// add gracenote here for album art
				// in page generator look at the card for a youtube link
			}
			c.setOriginalYoutubeLink(link);
			cards.add(0, c);
			return cards;
		}

		@Override
		protected void onPostExecute(ArrayList<Card> results) {
			// resultsShown = true;
			Intent intent = new Intent(getBaseContext(), ResultsPage.class);
			// intent.putParcelableArrayListExtra(EXTRA_SONG_INFO, results);
			try {
				PageGenerator.generatePage(results);
			} catch (IOException e) {
				e.printStackTrace();
			}
			spinImage.clearAnimation();
			spinImage.setVisibility(View.GONE);
			record.setVisibility(View.VISIBLE);
			button.setVisibility(View.VISIBLE);
			button.setClickable(true);
			mutedButton.setVisibility(View.INVISIBLE);
			record.setClickable(true);
			// startActivity(intent);
			// WebView myWebView = (WebView)findViewById(R.id.webview);
			// myWebView.setVisibility(0);
			// File externalDir = Environment.getExternalStorageDirectory();
			startActivity(intent);
			// myWebView.loadUrl(externalDir.getAbsolutePath() + File.separator
			// + "text.html");

		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}

	}

	public static void setResultsShown(boolean b) {
		resultsShown = b;
	}

	public static boolean getResultsShown() {
		return resultsShown;
	}
}

package com.key;

import java.util.Vector;

import com.key.R;
import com.key.keyboard.GlobalVariables;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

/*
 * 主界面，输入主机IP连接
 * menu(Intent首选项，Dialog帮助)
 * To-do
 * 
 * DNS lookup
 * arrow keys, esc, win key
 */

public class ActRemoteDroid extends Activity {
	private static final String TAG = "ActRemoteDroid";
	// menu item(s)
	public static final int MENU_PREFS = 0;
	public static final int MENU_HELP = 1;

	//
	private EditText tbIp;
	//
	private ViewHelpDialog dlHelp;
	//
	private ThreadDiscoverSocket discover;
	private Handler handler;
	private SimpleAdapter adapter;
	private Vector<String> hostlist;

	public ActRemoteDroid() {
		super();
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE); // added to save screen
														// space, the Title was
														// shown twice, in
														// Standard Android bar,
														// then below in Bolder
														// larger text, this
														// gets rid of the
														// standard android bar
		setContentView(R.layout.main);
		//
		this.handler = new Handler();
		// set some listeners
		Button but = (Button) this.findViewById(R.id.btnConnect);
		but.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onConnectButton();
			}
		});

		// check SharedPreferences for IP
		DataSettings.init(this.getApplicationContext());

		//
		this.tbIp = (EditText) this.findViewById(R.id.etIp);
		if (DataSettings.ip != null) {
			this.tbIp.setText(DataSettings.ip);
		}
		//
		if (this.dlHelp == null) {
			this.dlHelp = new ViewHelpDialog(this);
		}
		// discover some servers
		this.hostlist = new Vector<String>();
		((ListView) this.findViewById(R.id.lvHosts))
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView adapter, View v,
							int position, long id) {
						onHostClick(position);
					}
				});
	}

	private void updateHostList() {
		HostsListAdapter adapter = new HostsListAdapter(this.hostlist,
				this.getApplication());
		((ListView) this.findViewById(R.id.lvHosts)).setAdapter(adapter);
	}

	/** OS kills process */
	public void onDestroy() {
		super.onDestroy();
	}

	/** App starts anything it needs to start */
	public void onStart() {
		super.onStart();
	}

	/** App kills anything it started */
	public void onStop() {
		super.onStop();
	}

	/** App starts displaying things */
	public void onResume() {
		super.onResume();
		this.discover = new ThreadDiscoverSocket(
				/**
				 * 实现ThreadDiscoverSocket的监听接口DiscoverListener
				 */
				new ThreadDiscoverSocket.DiscoverListener() {
					public void onAddressReceived(String address) {
						hostlist.add(address);
						Log.d(TAG, "Got host back, " + address);
						handler.post(new Runnable() {
							public void run() {
								updateHostList();
							}
						});
					}
				});
		this.discover.start();
	}

	/** App goes into background */
	public void onPause() {
		super.onPause();
		this.discover.closeSocket();
	}

	// menu
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		//
		menu.add(0, MENU_PREFS, 0, R.string.txt_preferences)
				.setShortcut('0', 'p').setIcon(R.drawable.icon_prefs);
		menu.add(0, MENU_HELP, 0, R.string.txt_help).setShortcut('1', 'h')
				.setIcon(R.drawable.icon_help);
		//
		return true;
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_PREFS:
			//
			this.onPrefs();
			break;
		case MENU_HELP:
			//
			this.onHelp();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void onConnectButton() {
		String ip = this.tbIp.getText().toString();
		if (ip.length()<20 && ip.matches("^[0-9]{1,4}\\.[0-9]{1,4}\\.[0-9]{1,4}\\.[0-9]{1,4}$")) {
			try {
				DataSettings.setIp(ip);
				//
				Intent i = new Intent(this, ActMainMenu.class);
				this.startActivity(i);
//				this.finish();
				GlobalVariables.KeycodesQueueBeginListen();
			} catch (Exception ex) {
				// this.tvError.setText("Invalid IP   l");
				// this.tvError.setVisibility(View.VISIBLE);
				Toast.makeText(this,
						this.getResources().getText(R.string.toast_invalidIP),
						Toast.LENGTH_LONG).show();
				Log.d(TAG, ex.toString());

			}
		} else {
			// this.tvError.setText("Invalid IP address");
			// this.tvError.setVisibility(View.VISIBLE);
			Toast.makeText(this,
					this.getResources().getText(R.string.toast_invalidIP),
					Toast.LENGTH_LONG).show();
		}
	}

	private void onHostClick(int item) {
		this.tbIp.setText(this.hostlist.get(item));
		this.onConnectButton();
	}

	private void onHelp() {
		this.dlHelp.show();
	}

	private void onPrefs() {
		Intent i = new Intent(ActRemoteDroid.this, ActPreferences.class);
		this.startActivity(i);
	}

}

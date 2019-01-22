package com.turnyur.gasdatalogger;

import java.io.IOException;
import java.util.UUID;

import org.apache.commons.logging.Log;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;

public class ConnectingBT extends Activity {
	private boolean amConnected = false;
	private AnimationDrawable frameAnimation;
	private AnimationDrawable conframeAnimation;
	static BluetoothSocket datmmSocket;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connecting_bt);
		// Show the Up button in the action bar.
		setupActionBar();
		ImageView frameImg = (ImageView) findViewById(R.id.bt_devices_frame);
		TextView btDeviceNmae = (TextView) findViewById(R.id.devName);
		TextView btMACaddress = (TextView) findViewById(R.id.devMAC);
		ProgressBar connectingPBar = (ProgressBar) findViewById(R.id.connectProgressBar);
		//Used in changing progress bar color
		connectingPBar.getIndeterminateDrawable().setColorFilter(
				Color.parseColor("#0069F5"),
				android.graphics.PorterDuff.Mode.SRC_ATOP);
		
		ImageView ic_img_con=(ImageView) findViewById(R.id.ic_connectingIMG);
		//Used in animating the images in frames continously
		ic_img_con.setBackgroundResource(R.drawable.bt_frame_animation);
		frameAnimation=(AnimationDrawable) ic_img_con.getBackground();
		frameAnimation.start();
		
		frameImg.setBackgroundResource(R.drawable.frame_anim_bt_devices);
		conframeAnimation=(AnimationDrawable) frameImg.getBackground();
		conframeAnimation.start();
		
		String gottenName = getIntent().getExtras()
				.get(MainActivity.transitName).toString();
		btDeviceNmae.setText(MainActivity.transitName);
		btMACaddress.setText(gottenName);
		BluetoothDevice BTooth = (BluetoothDevice) getIntent().getParcelableExtra("BTObj") /*.get(
				"BTObj")*/;
		if(!amConnected){
			if (MainActivity.mBluetoothAdapter != null
				&& MainActivity.mBluetoothAdapter.isEnabled()) {
			Thread mConn = new ConnectThread(/* MainActivity.mBTDevice */BTooth);
			mConn.start();
		}
		
		}
		/*
		 * btDeviceNmae.setText(BTooth.getName());
		 * btMACaddress.setText(BTooth.getAddress());
		 * 
		 * used formally for testing the Parcelable object BTObj
		 */
		/*
		 * test.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { // TODO Auto-generated
		 * method stub Intent i=new Intent(ConnectingBT.this,DataStream.class);
		 * startActivity(i); } });
		 */// Above code snippet was used to verify possibility of Buetooth
			// connection
	}

	public void calltoDataStream() {
		if (amConnected) {
			Intent i = new Intent(ConnectingBT.this, MainActivity.class);
			startActivity(i);
			
		}
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.connecting_bt, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// Connect to Bluetooth from different Thread
	private class ConnectThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final BluetoothDevice mmDevice;
		private final UUID MY_UUID = UUID
				.fromString("00001101-0000-1000-8000-00805F9B34FB");

		public ConnectThread(BluetoothDevice device) {
			BluetoothSocket tmp = null;
			mmDevice = device;

			try {
				tmp = mmDevice.createRfcommSocketToServiceRecord(MY_UUID);
			} catch (IOException e) {
			}
			mmSocket = tmp;
			datmmSocket = mmSocket;
		}

		public void run() {
			MainActivity.mBluetoothAdapter.cancelDiscovery();
			android.util.Log.d("Turnyur", "got it");
			try {
				mmSocket.connect();
				amConnected = true;
				calltoDataStream();
			} catch (IOException connectException) {
				try {
					mmSocket.close();
				} catch (IOException closeException) {
				}
				//return;
			}
		}

		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) {
			}
		}
	}

}

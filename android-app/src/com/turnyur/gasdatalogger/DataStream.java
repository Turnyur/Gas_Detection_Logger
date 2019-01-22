package com.turnyur.gasdatalogger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cm.turnyur.gasdatalogger.DB.ObjectGasOperations;

import com.turnyur.gasdatalogger.MODEL.ObjectGas;
import com.turnyur.gasdatalogger.R.color;
import com.turnyur.gasdatalogger.StoredData.DisplayStoredListAdapter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothSocket;
import android.graphics.Color;
import android.os.Build;

public class DataStream extends Activity {

	private String LOGTAG = "MNGMNT_SYS";
	TextView tempValue;
	TextView gasValue;

	protected String old_writeMessage;
	public Handler mhandler;
	protected String extended_write_msg;
	protected Object writeMessage;
	private Handler bluetoothIn;
	private StringBuilder recDataString = new StringBuilder();
	private TextView stream_confirm_text;

	@SuppressLint("HandlerLeak")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_data_stream);
		// Show the Up button in the action bar.
		setupActionBar();

		tempValue = (TextView) findViewById(R.id.tempValue);
		gasValue = (TextView) findViewById(R.id.gas_int_value);
		stream_confirm_text = (TextView) findViewById(R.id.author);
		
		 
		bluetoothIn = new Handler() {
			public void handleMessage(android.os.Message msg) {
				if (msg.what == 1) { // if message is what we want
					String readMessage = (String) msg.obj; // msg.arg1 = bytes
															// from connect
															// thread
					recDataString.append(readMessage); // keep appending to
														// string until ~
					int endOfLineIndex = recDataString.indexOf("~"); // determine
																		// the
																		// end-of-line
					if (endOfLineIndex > 0) { // make sure there data before ~
						String dataInPrint = recDataString.substring(0,
								endOfLineIndex); // extract string
						int dataLength = dataInPrint.length(); // get length of
																// data received
						// txtStringLength.setText("String Length = " +
						// String.valueOf(dataLength));

						if (recDataString.charAt(0) == '#') // if it starts with
															// # we know it is
															// what we are
															// looking for
						{
							String sensor0 = recDataString.substring(1, 3); // get
																			// sensor
																			// value
																			// from
																			// string
																			// between
																			// indices
																			// 1-5
							String sensor1 = recDataString.substring(8, 13); // same
																				// again...

							tempValue.setText(sensor0 + "°C"); // update the
																// textviews
																// with sensor
																// values
							gasValue.setText(sensor1 + "mV");
							stream_confirm_text
									.setText("Live Streaming From Controller Commenced");

							// This was added newly to carter for the database

							//Date myDate = new Date();
							ObjectGas feed_obj_gas = new ObjectGas();
							ObjectGasOperations operateDB = new ObjectGasOperations(
									DataStream.this);

							
					        
					        Date myDate=new Date();
							SimpleDateFormat sdf_time = new SimpleDateFormat(
									"h:mm:ss:a", Locale.US);
							SimpleDateFormat

							sdf_date = new SimpleDateFormat("dd/MM/yyyy",
									Locale.US);
							String date_stamp = sdf_date.format(myDate);
							String time_stamp = sdf_time.format(myDate);
							
							
							
							
							
							
							feed_obj_gas.setGasIntensity(gasValue.getText().toString());
							feed_obj_gas.setTemperature(tempValue.getText().toString());
							// Will be changed later
							feed_obj_gas
									.setRec_date( date_stamp);
							feed_obj_gas
									.setRec_time(time_stamp);
							operateDB.open();

							operateDB.addGasObjectToDB(feed_obj_gas);

							// End of This wa newly added to carter for the
							// database

						}
						recDataString.delete(0, recDataString.length()); // clear
																			// all
																			// string
																			// data
						// strIncom =" ";
						dataInPrint = " ";
					}
				}
			}
		};

		if (MainActivity.mBluetoothAdapter != null
				&& MainActivity.datmmSocket != null) {

			Thread mConnectedThread = new ConnectedThread(
					MainActivity.datmmSocket);
			mConnectedThread.start();
		}

	}

	@SuppressLint("NewApi")
	@Override
	protected void onPause() {
		super.onPause();
		if (MainActivity.mBluetoothAdapter.isEnabled()
				&& MainActivity.datmmSocket.isConnected()) {
			try {
				MainActivity.datmmSocket.close();
				MainActivity.amConnected = false;
			} catch (IOException e) {

			}
		}
	}

	@SuppressLint("NewApi")
	@Override
	protected void onStop() {
		super.onStop();
		if (MainActivity.mBluetoothAdapter.isEnabled()
				&& MainActivity.datmmSocket.isConnected()) {
			try {
				MainActivity.datmmSocket.close();
				MainActivity.amConnected = false;
			} catch (IOException e) {

			}
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub

		super.onResume();
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
		getMenuInflater().inflate(R.menu.data_stream, menu);
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

	// Inner class used in Streaming and writing data to Microcontroller

	private class ConnectedThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final InputStream mmInStream;
		private final OutputStream mmOutStream;

		public ConnectedThread(BluetoothSocket socket) {
			mmSocket = socket;
			InputStream tmpIn = null;
			OutputStream tmpOut = null;
			try {
				tmpIn = socket.getInputStream();
				tmpOut = socket.getOutputStream();
			} catch (IOException e) {
			}
			mmInStream = tmpIn;
			mmOutStream = tmpOut;
		}

		@Override
		public void run() {
			/*
			 * byte[] buffer = new byte[1024]; int begin = 0; int bytes = 0;
			 * while(true){
			 * 
			 * try {
			 * 
			 * bytes = mmInStream.read(buffer); for(int i = begin; i < bytes;
			 * i++) { if(buffer[i] == "#".getBytes()[0]) {
			 * mhandler.obtainMessage(1, begin, i, buffer).sendToTarget(); begin
			 * = i + 1; if(i == bytes - 1) { bytes = 0; begin = 0; } } } } catch
			 * (IOException e) { break; } }
			 */
			/*
			 * byte[] buffer = new byte[1024]; int bytes = 0; while (true) {
			 * 
			 * try {
			 * 
			 * int byteCount = mmInStream.available(); if (byteCount > 0) {
			 * bytes = mmInStream.read(buffer); String strReceived = new
			 * String(buffer, 0, 3); final String msgReceived = strReceived;
			 * runOnUiThread(new Runnable() {
			 * 
			 * @Override public void run() { tempValue.setText(msgReceived); }
			 * });
			 * 
			 * } } catch (IOException e) { // TODO Auto-generated catch block
			 * e.printStackTrace();
			 * 
			 * final String msgConnectionLost = " " + e.getMessage();
			 * runOnUiThread(new Runnable() {
			 * 
			 * @Override public void run() {
			 * tempValue.setText(msgConnectionLost); } });
			 * 
			 * } }
			 */

			byte[] buffer = new byte[256];
			int bytes;

			// Keep looping to listen for received messages
			while (true) {
				try {
					bytes = mmInStream.read(buffer); // read bytes from input
														// buffer
					String readMessage = new String(buffer, 0, bytes);
					// Send the obtained bytes to the UI Activity via handler
					bluetoothIn.obtainMessage(1, bytes, -1, readMessage)
							.sendToTarget();
				} catch (IOException e) {
					break;
				}
			}

		}

		public void write(byte[] bytes) {
			try {
				mmOutStream.write(bytes);
			} catch (IOException e) {
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

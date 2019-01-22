package com.turnyur.gasdatalogger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

//import com.turnyur.gasdatalogger.ConnectingBT.ConnectThread;
import com.turnyur.gasdatalogger.MODEL.*;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
	static BluetoothAdapter mBluetoothAdapter;
	TextView BTSupport, BT_Enbled;
	static String transitName;
	ToggleButton BT_ON_OFF;
	ImageButton ConnectToArduino;
	boolean found = false;
	boolean showBonded = true;
	private Animation animfadein2;
	private Animation animfadeinCon;
	private Animation animfadein3;
	public static boolean amConnected = false;
	public static BluetoothSocket datmmSocket;
	private static final String isON = "YES";
	private static final String isOFF = "NO";
	private final String DEVICE_ADDRESS = "20:16:11:29:92:93"; // Arduino
																// Bluetooth
																// Module MAC
																// Address
	static BluetoothDevice mBTDevice; // Formally used static to pass
										// Bluetooth
	// Object between activities

	List<BluetoothBondedDeviceHelper> dataModel = new ArrayList<BluetoothBondedDeviceHelper>();
	MyArrayAdapter myBondedDevicesArrayAdapter = null;
	Animation animfadein;
	ImageButton connectBImg;
	ImageButton realTime_loggingImg;
	ImageButton storeDataImg;
	public static int global_flag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		TextView projTitle = (TextView) findViewById(R.id.projTitle);
		TextView uControl = (TextView) findViewById(R.id.uControllertxt);
	//Introducced for test initially and now, has been destroyed
		Button testingofBTaftExaminMain = (Button) findViewById(R.id.testingofBTafterExam);
testingofBTaftExaminMain.setVisibility(View.GONE);
		testingofBTaftExaminMain.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// conAppToBTModule();
			}
		});
		fadeIn(projTitle);
		connectBImg = (ImageButton) findViewById(R.id.ConnectButton);
		realTime_loggingImg = (ImageButton) findViewById(R.id.realTime_logging);
		storeDataImg = (ImageButton) findViewById(R.id.storeDataB);
		storeDataImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(MainActivity.this, StoredData.class);
				startActivity(i);
			}
		});
		realTime_loggingImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (amConnected) {
					Intent i = new Intent(MainActivity.this, DataStream.class);
					startActivity(i);
				} else {
					Toast.makeText(
							getApplicationContext(),
							"Connect to Microcontroller before attempting to Stream data",
							Toast.LENGTH_LONG).show();
				}
			}
		});
		realTime_loggingImg.setVisibility(View.INVISIBLE);
		storeDataImg.setVisibility(View.INVISIBLE);
		fadeInOnebyOne(connectBImg, realTime_loggingImg, storeDataImg);

		// delay(300);
		// fadeIn(uControl);
		// fadeIn(storeDataImg);

		BTSupport = (TextView) findViewById(R.id.BTSBool);
		BT_Enbled = (TextView) findViewById(R.id.BT_ON_Bool);
		BT_ON_OFF = (ToggleButton) findViewById(R.id.BT_Enabled_toggleB);
		ConnectToArduino = (ImageButton) findViewById(R.id.ConnectButton);
		ListView showingBondedDevices = (ListView) findViewById(R.id.listView1);
		/*
		 * ListView clickableList=(ListView)
		 * findViewById(R.id.bondedDevicesView);
		 */
		showingBondedDevices.setOnItemClickListener(CustormList);

		myBondedDevicesArrayAdapter = new MyArrayAdapter();
		showingBondedDevices.setAdapter(myBondedDevicesArrayAdapter);

		BT_ON_OFF.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Check Enable toggle button
				if (BT_ON_OFF.isChecked()) {
					Intent enableBtIntent = new Intent(
							BluetoothAdapter.ACTION_REQUEST_ENABLE);
					startActivityForResult(enableBtIntent, 1);
					checkBTEnabled();
				} else {

					blueToothDiolog();
				}

			}

			public void blueToothDiolog() {
				// Custorm Dialog adopted
				final Dialog dialog = new Dialog(MainActivity.this);
				dialog.setContentView(R.layout.pop_up_dialog);
				dialog.setTitle("Microcontroller Based Data Logging Sytem");
				Button yesButton = (Button) dialog
						.findViewById(R.id.yes_button);
				Button noButton = (Button) dialog.findViewById(R.id.no_button);
				yesButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						mBluetoothAdapter.disable();
						BT_ON_OFF.setChecked(false);
						BT_Enbled.setText(isOFF);
						BT_Enbled.setTextColor(Color.RED);
						dialog.cancel();
						fadeIn(BT_Enbled);

					}
				});

				noButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						BT_ON_OFF.setChecked(true);
						BT_Enbled.setText(isON);
						BT_Enbled.setTextColor(Color.GREEN);

						dialog.cancel();
						fadeIn(BT_Enbled);
					}
				});
				dialog.show();

				// Default Dialog bus used before

				/*
				 * AlertDialog.Builder builder = new AlertDialog.Builder(
				 * MainActivity.this); builder.setMessage(
				 * "Turning off Bluetooth will stop realtime acquisition of data"
				 * ); builder.setTitle("Gas Data Logger");
				 * 
				 * // Message builder.setIcon(R.drawable.bt_dialog); // Icon
				 * builder.setPositiveButton("Yes", new
				 * DialogInterface.OnClickListener() {
				 * 
				 * @Override public void onClick(DialogInterface dialog, int
				 * which) { // Yes Button mBluetoothAdapter.disable();
				 * BT_ON_OFF.setChecked(false); BT_Enbled.setText(isOFF);
				 * BT_Enbled.setTextColor(Color.RED); fadeIn(BT_Enbled); } });
				 * builder.setNegativeButton("No", new
				 * DialogInterface.OnClickListener() {
				 * 
				 * @Override public void onClick(DialogInterface dialog, int
				 * which) { // Yes Button BT_ON_OFF.setChecked(true);
				 * BT_Enbled.setText(isON); BT_Enbled.setTextColor(Color.GREEN);
				 * 
				 * dialog.cancel(); fadeIn(BT_Enbled); } });
				 * 
				 * AlertDialog alert = builder.create();
				 * 
				 * alert.show();
				 */
			}
		});
		ConnectToArduino.setOnClickListener(new OnClickListener() {

			// private int count = 0;

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (mBluetoothAdapter.isEnabled()) {
					conAppToBTModule();
				}
				if (mBluetoothAdapter.isEnabled()) {
					showBondedDevicesList();
				}

			}
		});

		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter.isEnabled()) {

			showBondedDevicesList();

			BT_Enbled.setTextColor(Color.GREEN);
		}
		if (mBluetoothAdapter == null) {
			// Device does not support Bluetooth
			Toast.makeText(getApplicationContext(),
					"Device doesnt Support Bluetooth", Toast.LENGTH_SHORT)
					.show();
			BTSupport.setText(isOFF);
			BTSupport.setTextColor(Color.RED);
			fadeIn(BT_Enbled);
		} else {
			BTSupport.setText(isON);
			BTSupport.setTextColor(Color.GREEN);
			checkBTEnabled();
			fadeIn(BT_Enbled);
		}

	}

	private AdapterView.OnItemClickListener CustormList = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			BluetoothBondedDeviceHelper r = dataModel.get(position);
			// student_name.setText( r.getName());
			// student_dept.setText( r.getMAC_address());
			transitName = r.getName();

			Intent i = new Intent(MainActivity.this, ConnectingBT.class);
			i.putExtra(r.getName(), r.getMAC_address());
			i.putExtra("BTObj", mBTDevice);
			startActivity(i);
		}

	};

	private void delay(int d) {
		// TODO Auto-generated method stub
		for (int i = 0; i < d; i++) {
			for (int k = 0; k < 1257; k++) {
				// Stays here until delay complete
			}
		}
	}

	public void fadeIn(View view) {
		animfadeinCon = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.fade_in);
		view.startAnimation(animfadeinCon);
		animfadeinCon.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	public void fadeInOnebyOne(ImageButton btnStart, final ImageButton btnEnd,
			final ImageButton btnThird) {
		animfadein = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.fade_in_fast);
		animfadein2 = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.fade_in);
		animfadein3 = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.fade_in);
		btnStart.startAnimation(animfadein);
		animfadein.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub

				realTime_loggingImg.setVisibility(View.VISIBLE);
				btnEnd.setAnimation(animfadein2);
			}
		});
		animfadein2.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				storeDataImg.setVisibility(View.VISIBLE);
				btnThird.setAnimation(animfadein3);

			}
		});
	}

	public boolean checkBTEnabled() {
		if (!mBluetoothAdapter.isEnabled()) {
			BT_Enbled.setText("NO"); // Bluetooth is disabled
			// Then Enable it below

			return false;

		} else {
			// Bluetooth Enabled
			BT_Enbled.setText("YES");
			BT_ON_OFF.setChecked(true);

			return true;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (!(requestCode == 1 && resultCode == RESULT_OK)) {
			BT_ON_OFF.setChecked(false);
			BT_Enbled.setTextColor(Color.RED);
			BT_Enbled.setText("NO");
			BTSupport.setText(isON);

		} else {
			BT_Enbled.setText(isON);
			BT_Enbled.setTextColor(Color.GREEN);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void showBondedDevicesList() {
		if (!mBluetoothAdapter.isEnabled()) {
			Toast.makeText(
					getApplicationContext(),
					"Turn on Bluetooth before attempting to connect to a Microcontroller",
					Toast.LENGTH_SHORT).show();
			BTSupport.setText(isON);
		} else {
			if (showBonded == true && mBluetoothAdapter.isEnabled()) {

				Set<BluetoothDevice> bondedDevices = mBluetoothAdapter
						.getBondedDevices();
				BTSupport.setText(isON);
				if (bondedDevices.isEmpty()) {

					Toast.makeText(getApplicationContext(),
							"Please Pair the Device first", Toast.LENGTH_SHORT)
							.show();
				} else {
					for (BluetoothDevice iterator : bondedDevices) {
						// count++;
						BluetoothBondedDeviceHelper bond = new BluetoothBondedDeviceHelper();

						bond.setName(iterator.getName());
						bond.setMAC_address(iterator.getAddress());
						myBondedDevicesArrayAdapter.add(bond);

						// showBonded=true; //Used in making sure List
						// of bonded devices does not duplicate on each
						// click

						// Below Code to be removed later and when
						// physical
						// Bluetooth device is used
						/*
						 * if (count == 2) { mBTDevice = iterator; }
						 */
						// Above Code to be removed later and when
						// physical
						// Bluetooth device is used
						if (iterator.getAddress().equals(DEVICE_ADDRESS))
						// Replace with iterator.getName() if comparing
						// Device names.
						{
							mBTDevice = (BluetoothDevice) iterator; // device is
																	// an
							// object
							// of type
							// BluetoothDevice
							found = true;
							break;

						}
					}
				}

				showBonded = false;
			} else {
				Toast.makeText(getApplicationContext(),
						"Already showing bonded Devices", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	public void conAppToBTModule() {
		if (!amConnected) {
			if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
				Thread mConn = new ConnectThread(mBTDevice);
				mConn.start();
			}
			if (amConnected) {
				Toast.makeText(getApplicationContext(),
						"Connected to Bluetooth!", Toast.LENGTH_SHORT).show();
				Intent i = new Intent(MainActivity.this, DataStream.class);
				startActivity(i);
			}
		} else {
			Toast.makeText(getApplicationContext(),
					"Connected to Micocontroller Already!", Toast.LENGTH_SHORT)
					.show();
		}
	}

	// Inner Class Implementing Array Adapter for the Custorm List for Bonded
	// Devices
	class MyArrayAdapter extends ArrayAdapter<BluetoothBondedDeviceHelper> {

		public MyArrayAdapter() {
			super(MainActivity.this, R.layout.bonded_device_list, dataModel);
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int position, View Convertview, ViewGroup parent) {
			ViewHolder holder;
			View row = Convertview;

			if (row == null) {
				LayoutInflater inflater = MainActivity.this.getLayoutInflater();
				row = inflater.inflate(R.layout.bonded_device_list, parent,
						false);
				holder = new ViewHolder();
				holder.deviceName = (TextView) row
						.findViewById(R.id.DeviceNamefromList);
				holder.deviceMAC = (TextView) row
						.findViewById(R.id.deviceMACfromList);
				row.setTag(holder);

			} else {
				holder = (ViewHolder) row.getTag();
			}
			BluetoothBondedDeviceHelper blueT_data = dataModel.get(position);

			/*
			 * TextView devName = (TextView) row
			 * .findViewById(R.id.DeviceNamefromList); TextView devMAC =
			 * (TextView) row .findViewById(R.id.deviceMACfromList);
			 */
			if (blueT_data != null) {

				holder.deviceName.setText(blueT_data.getName());
				holder.deviceMAC.setText(blueT_data.getMAC_address());

			}
			return row;

		}
	}

	static class ViewHolder {
		// For List in MainActivity class
		TextView deviceName;
		TextView deviceMAC;

		// For List StoreData class
		TextView obID;
		TextView temperature;
		TextView gasIntensity;
		TextView rec_time;
		TextView rec_date;

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
			mBluetoothAdapter.cancelDiscovery();
			android.util.Log.d("Turnyur", "got it");
			try {
				mmSocket.connect();
				amConnected = true;
			} catch (IOException connectException) {
				try {
					mmSocket.close();
				} catch (IOException closeException) {
				}
				// return;
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

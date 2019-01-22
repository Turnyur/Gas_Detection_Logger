package com.turnyur.gasdatalogger;

import java.util.ArrayList;
import java.util.List;

import cm.turnyur.gasdatalogger.DB.ObjectGasOperations;

import com.turnyur.gasdatalogger.MODEL.BluetoothBondedDeviceHelper;
import com.turnyur.gasdatalogger.MODEL.ObjectGas;
import com.turnyur.gasdatalogger.MainActivity.MyArrayAdapter;
import com.turnyur.gasdatalogger.MainActivity.ViewHolder;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class StoredData extends Activity {
	List<ObjectGas> gas_data_model = new ArrayList<ObjectGas>();
	DisplayStoredListAdapter tx_dataAdapter = null;
	private Animation animfadein;
	private Animation animfadein2;
	private String LOGTAG="MNGMNT_SYS";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stored_data);
		// Show the Up button in the action bar.
		setupActionBar();
		
		ListView data_cn = (ListView) findViewById(R.id.data_cen_list);

		tx_dataAdapter = new DisplayStoredListAdapter();
		data_cn.setAdapter(tx_dataAdapter);
		Log.i(LOGTAG, "testing");
		// Used Temporary to feed data?dummy text to the Data center list
		/*
		 * for (int i = 1; i <= 50; i++) {
		 * 
		 * ObjectGas txData_test = new ObjectGas(); txData_test.setObect_ID(i);
		 * txData_test.setGasIntensity("0.418mV");
		 * txData_test.setTemperature("27.84°C");
		 * txData_test.setRec_date("1/7/2017");
		 * txData_test.setRec_time("4:00 PM");
		 * 
		 * tx_dataAdapter.add(txData_test);
		 * 
		 * 
		 * }
		 */
		ObjectGasOperations tx_exchange = new ObjectGasOperations(
				getApplicationContext());
		tx_exchange.openRead();
			
		 for (int i = 1; i <= tx_exchange.getGasDataStoredRowCount(); i++) {
		 	
			ObjectGas txData_test = tx_exchange.getGasData(i);
			tx_dataAdapter.add(txData_test);
		 	Log.i(LOGTAG, "Pulling List"); 
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
		getMenuInflater().inflate(R.menu.stored_data, menu);
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

	public void fadeIn(TextView view1, final TextView view2) {
		animfadein2 = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.list_fade);
		animfadein = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.list_fade);
		view1.startAnimation(animfadein);
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
				// view2.startAnimation(animfadein2);
			}
		});

	}

	// Inner Class Implementing Array Adapter for the Custorm List for Bonded
	// Devices
	class DisplayStoredListAdapter extends ArrayAdapter<ObjectGas> {

		public DisplayStoredListAdapter() {
			super(StoredData.this, R.layout.store_list, gas_data_model);
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int position, View Convertview, ViewGroup parent) {
			ViewHolder holderItem;
			View row = Convertview;

			if (row == null) {
				LayoutInflater inflater = StoredData.this.getLayoutInflater();
				row = inflater.inflate(R.layout.store_list, parent, false);

				holderItem = new ViewHolder();

				holderItem.obID = (TextView) row.findViewById(R.id.obj_gasID);

				holderItem.gasIntensity = (TextView) row
						.findViewById(R.id.d_cn_gasIntV);
				holderItem.temperature = (TextView) row
						.findViewById(R.id.d_cn_tempV);
				holderItem.rec_date = (TextView) row
						.findViewById(R.id.d_cn_date);
				holderItem.rec_time = (TextView) row
						.findViewById(R.id.d_cn_time);

				row.setTag(holderItem);

			} else {
				holderItem = (ViewHolder) row.getTag();
			}
			ObjectGas sent_data = gas_data_model.get(position);

			if (sent_data != null) {
				fadeIn(holderItem.temperature, null);
				fadeIn(holderItem.gasIntensity, null);
				fadeIn(holderItem.rec_time, null);
				fadeIn(holderItem.rec_date, null);
				holderItem.obID.setText(sent_data.getObect_ID() + "");
				holderItem.gasIntensity.setText(sent_data.getGasIntensity());
				holderItem.temperature.setText(sent_data.getTemperature());
				holderItem.rec_date.setText(sent_data.getRec_date());
				holderItem.rec_time.setText(sent_data.getRec_time());

			}
			return row;

		}

	}

}

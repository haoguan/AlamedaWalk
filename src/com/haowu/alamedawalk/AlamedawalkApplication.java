package com.haowu.alamedawalk;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class AlamedawalkApplication extends Activity {
	
	//message setting
	public final static String EXTRA_FROM = "com.haowu.alamedawalk.FROM";
	public final static String EXTRA_TO = "com.haowu.alamedawalk.TO";
	
	private static AlamedawalkApplication instance;
	EditText etFrom;
	EditText etTo;
	Button bSubmit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		//set action bar stuff
		ActionBar ab = getActionBar(); 
		ab.setDisplayShowTitleEnabled(false); 
		ab.setDisplayShowHomeEnabled(false);
		ab.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#C1D4F1")));
		setContentView(R.layout.home);
		
		//initialize all vars
		etFrom = (EditText) findViewById(R.id.et_from);
		etTo = (EditText) findViewById(R.id.et_to);
		bSubmit = (Button) findViewById(R.id.b_submit);
		//set edittexts to end
		etFrom.setSelection(etFrom.getText().length());
		etTo.setSelection(etTo.getText().length());
		
		//onclick listeners
		bSubmit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				String from = etFrom.getText().toString();
				String to = etTo.getText().toString();
				Intent intent = new Intent(instance, AlamedawalkMap.class);
				intent.putExtra(EXTRA_FROM, from);
				intent.putExtra(EXTRA_TO, to);
				startActivity(intent);
			}
			
		});
		
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
//		if (savedInstanceState == null) {
//			getFragmentManager().beginTransaction()
//					.add(R.id.container, new PlaceholderFragment()).commit();
//		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

//	/**
//	 * A placeholder fragment containing a simple view.
//	 */
//	public static class PlaceholderFragment extends Fragment {
//
//		public PlaceholderFragment() {
//		}
//
//		@Override
//		public View onCreateView(LayoutInflater inflater, ViewGroup container,
//				Bundle savedInstanceState) {
//			View rootView = inflater.inflate(R.layout.fragment_main, container,
//					false);
//			return rootView;
//		}
//	}

}

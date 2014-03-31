package com.haowu.alamedawalk;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class MainActivity extends Activity {
	
	MapFragment mapFrag;
	GoogleMap map;
	MongoClient mongoClient;
	DB db;
	
	final static String GEOSPATIAL_INDEX = "geo";
	final static String DB_NAME = "CrimeDB";
	final static String DB_COLLECTION_NAME = "Alameda";
	final static String DB_HOST = "192.168.1.145";
	final static int DB_PORT = 27017;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
//		mapFrag = (MapFragment) getFragmentManager().findFragmentById(R.id.frag_map);
//		map = mapFrag.getMap();
		
		Log.d("test", "----------------");
		System.out.println("--------------------------");
		LoadMongoDb loadDb = new LoadMongoDb();
		loadDb.execute();
//		if (savedInstanceState == null) {
//			getFragmentManager().beginTransaction()
//					.add(R.id.container, new PlaceholderFragment()).commit();
//		}
	}
	
	private class LoadMongoDb extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			try {
				System.out.print("-----------------");
				mongoClient = new MongoClient(DB_HOST, DB_PORT);
				db = mongoClient.getDB(DB_NAME);
//				DBCollection collection = db.getCollection(DB_COLLECTION_NAME);
//				DBCursor cursor = collection.find();
//				System.out.println(cursor.count());
				GetGeoSpatialLocations g = new GetGeoSpatialLocations();
				g.execute();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
	}
	
	private class GetGeoSpatialLocations extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			DBCollection collection = db.getCollection(DB_COLLECTION_NAME);
			System.out.println("HELLOOOOOOOOOOOO");
			// find within circle
//			List circle = new ArrayList();
//			circle.add(new double[] {-121.9,37.5}); // center of circle
//			circle.add(100/3959); // radius
//			BasicDBObject query = new BasicDBObject("loc", new BasicDBObject("$geoWithin", 
//					new BasicDBObject("$centerSphere", circle)));
			BasicDBObject filter = new BasicDBObject("$near", new double[] { -121.9, 37.5 });

			BasicDBObject query = new BasicDBObject("loc", filter);
			DBCursor cursor = collection.find(query);
			System.out.println("---------------------" + cursor.count());
			while(cursor.hasNext()) {
				DBObject result = cursor.next();
				System.out.println(result.get("name") + ": " + result.get("loc"));
			}
			
			return null;
		}
		
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

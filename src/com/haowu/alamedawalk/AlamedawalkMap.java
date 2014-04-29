package com.haowu.alamedawalk;

import java.math.BigDecimal;
import java.math.MathContext;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ThreadPoolExecutor;

import org.w3c.dom.Document;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.haowu.alamedawalk.widget.CrimeMarker;
import com.haowu.alamedawalk.widget.GMapV2Direction;
import com.haowu.alamedawalk.widget.MyItem;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class AlamedawalkMap extends Activity {
	MapFragment mapFrag;
	GoogleMap map;
	MongoClient mongoClient;
	DB db;
	
	Button bChangeRoute;
	
	// address strings
	String fromAddress;
	String toAddress;
	LatLng fromLocation;
	LatLng toLocation;
	
	// points
	ArrayList<ArrayList<LatLng>> routes;
	int numRoutes = 0;
	int curRoute = 0;
	
	int cameraChange = 2; // fix camera to move each time.
	
//	HashSet<CrimeMarker> crimeLocations;
//	HashSet<CrimeMarker> crimeLocations;
//	ClusterManager<CrimeMarker> mClusterManager;
	ClusterManager<MyItem> mClusterManager;
	
	private static final int MARKER_A = 0;
	private static final int MARKER_B = 1;
	private static final int MARKER_POI = 2;
	
	//mongodb
	final static String DB_NAME = "CrimeDB";
	final static String DB_COLLECTION_NAME = "Alameda";
	final static String DB_HOST = "192.168.1.125";
	final static int DB_PORT = 27017;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//set action bar stuff
		ActionBar ab = getActionBar(); 
		ab.setDisplayShowTitleEnabled(false); 
		ab.setDisplayShowHomeEnabled(false);
		ab.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#C1D4F1")));
		
		//receive info
		Intent intent = getIntent();
		fromAddress = intent.getStringExtra(AlamedawalkApplication.EXTRA_FROM);
		toAddress = intent.getStringExtra(AlamedawalkApplication.EXTRA_TO);
		
		setContentView(R.layout.map);
		
		mapFrag = (MapFragment) getFragmentManager().findFragmentById(R.id.frag_map);
		map = mapFrag.getMap();
		bChangeRoute = (Button) findViewById(R.id.bChangeRoute);
		
		mClusterManager = new ClusterManager<MyItem>(this, map);
		map.setOnCameraChangeListener(mClusterManager);
		map.setOnMarkerClickListener(mClusterManager);
		
		LoadMongoDb loadDb = new LoadMongoDb();
		loadDb.execute();
		
		bChangeRoute.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int nextRoute = getNextRoute();
//				new DrawRoute(routes.get(nextRoute)).execute();
				mClusterManager = new ClusterManager<MyItem>(getApplicationContext(), map);
				map.setOnCameraChangeListener(mClusterManager);
				map.setOnMarkerClickListener(mClusterManager);
				drawRoute(routes.get(nextRoute));
			}
			
		});
	}
	
	private class LoadMongoDb extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			try {
				System.out.print("-----------------");
				mongoClient = new MongoClient(DB_HOST, DB_PORT);
				db = mongoClient.getDB(DB_NAME);
				new FindDirections().execute();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
	}
	
	private class FindDirections extends AsyncTask<Void, Void, ArrayList<ArrayList<LatLng>>> {

		FindDirections() {
			super();
		}

		protected ArrayList<ArrayList<LatLng>> doInBackground(Void... params) {
			// does everything to get the correct lat longs.
			GMapV2Direction md = new GMapV2Direction();
			Document doc;
			doc = md.getDocument(fromAddress, toAddress, GMapV2Direction.MODE_WALKING); // default to walking for now.
//			calculation = new Calculation(doc);
//			midpoint = calculation.calcStep();
			ArrayList<ArrayList<LatLng>> directionPoint = md.getDirection(doc);
			return directionPoint;
		}
		
		protected void onPostExecute(ArrayList<ArrayList<LatLng>> points) {
			// reset the map.
			routes = points;
			curRoute = 0;
			numRoutes = routes.size();
			// default display first route.
			ArrayList<LatLng> route = routes.get(curRoute);
			drawRoute(route);
		}
	}
	
	private int getNextRoute() {
		if (curRoute < numRoutes - 1) {
			curRoute += 1;
		}
		else {
			curRoute = 0;
		}
		return curRoute;
			
	}
	
	private void drawRoute(ArrayList<LatLng> route) {
		map.clear();
		fromLocation = new LatLng(route.get(0).latitude, route.get(0).longitude);
		toLocation = new LatLng(route.get(route.size() - 1).latitude, route.get(route.size() - 1).longitude);
		// add markers and draw polylines for route
		double from_lat = route.get(0).latitude;
		double from_lng = route.get(0).longitude;
		double to_lat = route.get(route.size() - 1).latitude;
		double to_lng = route.get(route.size() - 1).longitude;
		addMarker(map, from_lat, from_lng, "Start", "My Location", MARKER_A);
		addMarker(map, to_lat, to_lng, "End", "Destination", MARKER_B);
		
		// draw the route path.
		PolylineOptions rectLine = new PolylineOptions().width(15).color(getResources().getColor(R.color.alamedaWalkRoutePurple));

		// handle route and crime locations.
		for (int j = 0; j < route.size(); j++) {
			rectLine.add(route.get(j));
//			new GetGeoSpatialLocations(route.get(j)).execute();
		}
		map.addPolyline(rectLine);
		new GetGeoSpatialLocations(route).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}
	
	private void addMarker(GoogleMap map, double lat, double lon, String string, String string2, int idx) {
		Marker marker;
		switch (idx) {
		case MARKER_A:
			map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_a)).position(new LatLng(lat, lon)).title(string).snippet(string2));
			break;
		case MARKER_B:
			map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_b)).position(new LatLng(lat, lon)).title(string).snippet(string2));
			break;
//		case MARKER_MP:
//			map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_mp)).position(new LatLng(lat, lon)).title(string).snippet(string2));
//			break;
		case MARKER_POI:
			marker = map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_poi)).position(new LatLng(lat, lon)).title(string).snippet(string2));
			break;
		}
	}
	
	
	private class GetGeoSpatialLocations extends AsyncTask<Void, Void, HashSet<MyItem>> {

		ArrayList<LatLng> route;
//		LatLng point;
		
		GetGeoSpatialLocations(ArrayList<LatLng> route) {
			this.route = route;
		}
//		GetGeoSpatialLocations(LatLng point) {
//			this.point = point;
//		}
		
		@Override
		protected HashSet<MyItem> doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			
			DBCollection collection = db.getCollection(DB_COLLECTION_NAME);
			HashSet<MyItem> crimeLocs = new HashSet<MyItem>();
		    BasicDBList or = new BasicDBList();
		    try {
				for (int i = 0; i < route.size(); i++) {
					LatLng center = route.get(i);
					BasicDBList geoCoord = new BasicDBList();
					geoCoord.add(center.longitude);
					geoCoord.add(center.latitude);
	
				    BasicDBList geoParams = new BasicDBList();
				    geoParams.add(geoCoord);
				    double radius = (double)0.1/3959; //need to make sure it is double!
//				    BigDecimal bd = new BigDecimal(radius);
//				    bd = bd.round(new MathContext(3));
//				    double rounded_radius = bd.doubleValue();
//				    geoParams.add(rounded_radius);
				    geoParams.add(radius);
	
				    BasicDBObject getLoc = new BasicDBObject("loc", 
				        new BasicDBObject("$geoWithin", 
				            new BasicDBObject("$centerSphere", geoParams)
				        )
				    );
				    or.add(getLoc);
				}
				DBObject query = new BasicDBObject("$or", or);
				DBCursor cursor = collection.find(query);
				ArrayList<DBObject> list = (ArrayList<DBObject>) cursor.toArray();
				for (int j = 0; j < list.size(); j++) {
					BasicDBObject next = (BasicDBObject) list.get(j);
					BasicDBObject loc = (BasicDBObject) next.get("loc");
	//					CrimeMarker cm = new CrimeMarker(next.getString("Block"), 
	//							next.getString("CrimeDescription"), loc.getDouble("y"), loc.getDouble("x"));
					MyItem cm = new MyItem(loc.getDouble("y"), loc.getDouble("x"));
					
					crimeLocs.add(cm);
				}
				return crimeLocs;
		    } catch (MongoException e) {
		    	e.printStackTrace();
		    	System.out.println("MONGOEXCEPTION");
		    	return null;
		    }
		}

		@Override
		protected void onPostExecute(HashSet<MyItem> cms) {
			if (cms != null) {
				Log.d("TEST", Integer.toString(cms.size()));
				Iterator<MyItem> it = cms.iterator();
	//////			for (int i = 0; i < cms.size(); i++) {
				while (it.hasNext()) {
					MyItem cm = it.next();
	//				System.out.println(cm.getPosition().latitude + ", " + cm.getPosition().longitude);
					mClusterManager.addItem(cm);
	//				addMarker(map, cm.getLat(), cm.getLng(), cm.getTitle(), cm.getDescription(), MARKER_POI);
				}
				// correct the zoom level
				LatLngBounds.Builder b = new LatLngBounds.Builder();
				b.include(route.get(0));
				b.include(route.get(route.size() - 1));
	
				LatLngBounds bounds = b.build();
				CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 900+cameraChange, 900+cameraChange, 5);
				cameraChange = -cameraChange;
				map.animateCamera(cu);
			}
			else {
				Toast.makeText(getBaseContext(), "MONGO EXCEPTION", Toast.LENGTH_LONG).show();
			}
		}
	}
	
}

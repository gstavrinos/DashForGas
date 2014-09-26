package com.gstavrinos.dashforgas;

import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gstavrinos.helpers.GoogleMapsSnatcher;
import com.gstavrinos.helpers.ServerConnection;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
	public static Map<Integer, Integer> distance_amount;
	public static ServerConnection connection;
	private Button ok,all,mixed,closest, xy, pie1, pie2;
	private final LatLng Athens = new LatLng(37.983913, 23.729362);
	private boolean stats_shown = false;
	private boolean stations_shown = false;
	private TextView wait;
	private boolean xy_clicked, pie1_clicked;
	static int mix = 0;
	static int norm = 0;
	static int ATTIKIS = 0;
	static int KENTRIKIS_MAKEDONIAS = 0;
	static int THESSALIAS = 0;
	static int KRITIS = 0;
	static int ANATOLIKIS_MAKEDONIAS_K_THRAKIS = 0;
	static int DYTIKIS_ELLADAS = 0;
	static int DYTIKIS_MAKEDONIAS = 0;
	static int IPEIROY = 0;
	static int PELOPONNISOY = 0;
	private static LocationManager locationManager;
	private static LatLng user_location;
	private static Handler UIHandler = new Handler(Looper.getMainLooper());
	private static GoogleMap map;
	private static LocationListener locationListener;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setIcon(R.drawable.dashforgas);
	    MapFragment mapFrag = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
	    map = mapFrag.getMap();
	    if(map!=null){
	    	map.setMyLocationEnabled(true);
	    	map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
	    }
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		locationListener = new CustomLocationListener();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 10,locationListener);
        ok = (Button) findViewById(R.id.ok);
        all = (Button) findViewById(R.id.all);
        mixed = (Button) findViewById(R.id.mixed);
        closest = (Button) findViewById(R.id.closest);
        xy = (Button) findViewById(R.id.xy);
        pie1 = (Button) findViewById(R.id.pie1);
        pie2 = (Button) findViewById(R.id.pie2);
        wait = (TextView) findViewById(R.id.wait);
        ok.setVisibility(Button.INVISIBLE);
        all.setVisibility(Button.INVISIBLE);
        mixed.setVisibility(Button.INVISIBLE);
        closest.setVisibility(Button.INVISIBLE);
        xy.setVisibility(Button.INVISIBLE);
        pie1.setVisibility(Button.INVISIBLE);
        pie2.setVisibility(Button.INVISIBLE);
        xy_clicked = false;
        pie1_clicked = false;
		locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null);
		distance_amount = new TreeMap<Integer,Integer>();
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		connection = new ServerConnection();
		
		xy.setOnClickListener(new OnClickListener(){
			@Override
            public void onClick(View v){
		        xy.setVisibility(Button.INVISIBLE);
		        pie1.setVisibility(Button.INVISIBLE);
		        pie2.setVisibility(Button.INVISIBLE);
		        xy_clicked = true;
		        pie1_clicked = false;
				stats_shown = false;
				wait.setText("Please wait...");
				new Thread(new Runnable(){
				@Override
				public void run(){
    				try {
    					MainActivity.connection.resultSet = MainActivity.connection.statement.executeQuery("SELECT DIEYTHYNSI,LAT,LON FROM GASSTATIONS2 WHERE PERIFEREIA = 'ATTIKIS'");
    					while(MainActivity.connection.resultSet.next()){
    				        double d = LatLngDistance(Athens.latitude, Athens.longitude, Double.parseDouble(MainActivity.connection.resultSet.getString("LAT")), Double.parseDouble(MainActivity.connection.resultSet.getString("LON")));	
    				        if(d<100){//else the google API has given us the wrong Lat-Lng values
    								if(distance_amount.containsKey((int)Math.floor(d))){
    									distance_amount.put((int)Math.floor(d), distance_amount.get((int)Math.floor(d))+1);
    								}
    								else{
    									distance_amount.put((int)Math.floor(d), 1);
    								}
    							}
    					}
    					runOnUiThread(new Runnable(){
    						@Override
    						public void run(){
    	    					wait.setText("");
    							ok.setText("READY!");
    							ok.setVisibility(Button.VISIBLE);
    						}
        					});
    				} 
    				catch (SQLException e) {
    					e.printStackTrace();
    				}
				}
				}).start();
			}
		});
		pie1.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
		        xy.setVisibility(Button.INVISIBLE);
		        pie1.setVisibility(Button.INVISIBLE);
		        pie2.setVisibility(Button.INVISIBLE);
		        xy_clicked = false;
		        pie1_clicked = true;
				stats_shown = false;
				wait.setText("Please wait...");
				mix = 0;
				norm = 0;
				new Thread(new Runnable(){
					@Override
					public void run(){
	    				try {
	    					MainActivity.connection.resultSet = MainActivity.connection.statement.executeQuery("SELECT COUNT(*) AS mixed FROM GASSTATIONS WHERE TYPOS = 'MIKTO'");
	    					if(MainActivity.connection.resultSet.next()){
	    						mix = Integer.parseInt(MainActivity.connection.resultSet.getString("mixed"));
		    					MainActivity.connection.resultSet = MainActivity.connection.statement.executeQuery("SELECT COUNT(*) AS normal FROM GASSTATIONS WHERE TYPOS = 'YGRON KAYSIMON'");
		    					if(MainActivity.connection.resultSet.next()){
		    						norm = Integer.parseInt(MainActivity.connection.resultSet.getString("normal"));
		    					}
	    					}
	    				}
	    				catch (SQLException e) {
	    					e.printStackTrace();
	    				}
	    				runOnUiThread(new Runnable(){
    						@Override
    						public void run(){
    	    					wait.setText("");
    							ok.setText("READY!");
    							ok.setVisibility(Button.VISIBLE);
    						}
        					});
					}
					}).start();
			}
			
		});
		pie2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
		        xy.setVisibility(Button.INVISIBLE);
		        pie1.setVisibility(Button.INVISIBLE);
		        pie2.setVisibility(Button.INVISIBLE);
		        xy_clicked = false;
		        pie1_clicked = false;
				stats_shown = false;
				wait.setText("Please wait...");
				ATTIKIS = 0;
				KENTRIKIS_MAKEDONIAS = 0;
				THESSALIAS = 0;
				KRITIS = 0;
				ANATOLIKIS_MAKEDONIAS_K_THRAKIS = 0;
				DYTIKIS_ELLADAS = 0;
				DYTIKIS_MAKEDONIAS = 0;
				IPEIROY = 0;
				PELOPONNISOY = 0;
				new Thread(new Runnable(){
					@Override
					public void run(){
	    				try {
	    					MainActivity.connection.resultSet = MainActivity.connection.statement.executeQuery("SELECT COUNT(*) AS found FROM GASSTATIONS WHERE PERIFEREIA = 'ATTIKIS'");
	    					if(MainActivity.connection.resultSet.next()){
	    						ATTIKIS = Integer.parseInt(MainActivity.connection.resultSet.getString("found"));
		    					MainActivity.connection.resultSet = MainActivity.connection.statement.executeQuery("SELECT COUNT(*) AS found FROM GASSTATIONS WHERE PERIFEREIA = 'KENTRIKIS MAKEDONIAS'");
		    					if(MainActivity.connection.resultSet.next()){
		    						KENTRIKIS_MAKEDONIAS = Integer.parseInt(MainActivity.connection.resultSet.getString("found"));

			    					MainActivity.connection.resultSet = MainActivity.connection.statement.executeQuery("SELECT COUNT(*) AS found FROM GASSTATIONS WHERE PERIFEREIA = 'THESSALIAS'");
			    					if(MainActivity.connection.resultSet.next()){
			    						THESSALIAS = Integer.parseInt(MainActivity.connection.resultSet.getString("found"));

				    					MainActivity.connection.resultSet = MainActivity.connection.statement.executeQuery("SELECT COUNT(*) AS found FROM GASSTATIONS WHERE PERIFEREIA = 'KRITIS'");
				    					if(MainActivity.connection.resultSet.next()){
				    						KRITIS = Integer.parseInt(MainActivity.connection.resultSet.getString("found"));
					    					MainActivity.connection.resultSet = MainActivity.connection.statement.executeQuery("SELECT COUNT(*) AS found FROM GASSTATIONS WHERE PERIFEREIA = 'ANATOLIKIS MAKEDONIAS K THRAKIS'");
					    					if(MainActivity.connection.resultSet.next()){
					    						ANATOLIKIS_MAKEDONIAS_K_THRAKIS = Integer.parseInt(MainActivity.connection.resultSet.getString("found"));
						    					MainActivity.connection.resultSet = MainActivity.connection.statement.executeQuery("SELECT COUNT(*) AS found FROM GASSTATIONS WHERE PERIFEREIA = 'DYTIKIS ELLADAS'");
						    					if(MainActivity.connection.resultSet.next()){
						    						DYTIKIS_ELLADAS = Integer.parseInt(MainActivity.connection.resultSet.getString("found"));
							    					MainActivity.connection.resultSet = MainActivity.connection.statement.executeQuery("SELECT COUNT(*) AS found FROM GASSTATIONS WHERE PERIFEREIA = 'DYTIKIS MAKEDONIAS'");
							    					if(MainActivity.connection.resultSet.next()){
							    						DYTIKIS_MAKEDONIAS = Integer.parseInt(MainActivity.connection.resultSet.getString("found"));
								    					MainActivity.connection.resultSet = MainActivity.connection.statement.executeQuery("SELECT COUNT(*) AS found FROM GASSTATIONS WHERE PERIFEREIA = 'IPEIROY'");
								    					if(MainActivity.connection.resultSet.next()){
								    						IPEIROY = Integer.parseInt(MainActivity.connection.resultSet.getString("found"));
									    					MainActivity.connection.resultSet = MainActivity.connection.statement.executeQuery("SELECT COUNT(*) AS found FROM GASSTATIONS WHERE PERIFEREIA = 'PELOPONNISOY'");
									    					if(MainActivity.connection.resultSet.next()){
									    						PELOPONNISOY = Integer.parseInt(MainActivity.connection.resultSet.getString("found"));
									    					}
								    					}
							    					}
						    					}
					    					}
				    					}
			    					}
		    					}
	    					}
	    				}
	    				catch (SQLException e) {
	    					e.printStackTrace();
	    				}
	    				runOnUiThread(new Runnable(){
    						@Override
    						public void run(){
    	    					wait.setText("");
    							ok.setText("READY!");
    							ok.setVisibility(Button.VISIBLE);
    						}
        					});
					}
					}).start();
			}
			
		});
		ok.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(xy_clicked){
			        xy_clicked = false;
					Intent i=new Intent(MainActivity.this, StatsActivity.class);
					startActivity(i);
				}
				else if(pie1_clicked){
			        pie1_clicked = false;
					Intent i = new Intent(MainActivity.this, PieActivity.class);
					i.putExtra("mixed",mix+"");
					i.putExtra("normal",norm+"");
					startActivity(i);
				}
				else{
					Intent i = new Intent(MainActivity.this, Pie2Activity.class);
					i.putExtra("a",ATTIKIS+"");
					i.putExtra("km",KENTRIKIS_MAKEDONIAS+"");
					i.putExtra("t",THESSALIAS+"");
					i.putExtra("k",KRITIS+"");
					i.putExtra("amkt",ANATOLIKIS_MAKEDONIAS_K_THRAKIS+"");
					i.putExtra("de",DYTIKIS_ELLADAS+"");
					i.putExtra("dm",DYTIKIS_MAKEDONIAS+"");
					i.putExtra("i",IPEIROY+"");
					i.putExtra("p",PELOPONNISOY+"");
					startActivity(i);
				}
			}
			
		});
		
		all.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				map.clear();
				addMyMarker();
	        	stations_shown = !stations_shown;
		        all.setVisibility(Button.INVISIBLE);
		        mixed.setVisibility(Button.INVISIBLE);
		        closest.setVisibility(Button.INVISIBLE);
				wait.setText("Please wait...");
				new Thread(new Runnable(){
					@Override
					public void run(){
	    				try {
	    					MainActivity.connection.resultSet = MainActivity.connection.statement.executeQuery("SELECT ONOMATEPONYMO,DIEYTHYNSI,TK,PERIOXI,TYPOS,LAT,LON FROM GASSTATIONS2");
	    					while(MainActivity.connection.resultSet.next()){
	    						double lat = Double.parseDouble(MainActivity.connection.resultSet.getString("LAT"));
	    						double lon = Double.parseDouble(MainActivity.connection.resultSet.getString("LON"));
		    					String title = MainActivity.connection.resultSet.getString("DIEYTHYNSI") +", "+ MainActivity.connection.resultSet.getString("TK") +", "+ MainActivity.connection.resultSet.getString("PERIOXI");
	    						String snippet = MainActivity.connection.resultSet.getString("ONOMATEPONYMO");
		    					if(LatLngDistance(user_location.latitude, user_location.longitude, lat, lon) <= 10){
		    						if(MainActivity.connection.resultSet.getString("TYPOS").equals("MIKTO")){
		    							addStationMarker(new LatLng(lat,lon),title,snippet,true);
		    						}
		    						else{
		    							addStationMarker(new LatLng(lat,lon),title,snippet,false);
		    						}
		    					}
	    					}
	    				}
	    				catch (SQLException e) {
	    					e.printStackTrace();
	    				}
	    				runOnUiThread(new Runnable(){
    						@Override
    						public void run(){
    	    					wait.setText("");
    						}
        					});
					}
					}).start();
			}
			
		});
		
		mixed.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				map.clear();
				addMyMarker();
	        	stations_shown = !stations_shown;
		        all.setVisibility(Button.INVISIBLE);
		        mixed.setVisibility(Button.INVISIBLE);
		        closest.setVisibility(Button.INVISIBLE);
				wait.setText("Please wait...");
				new Thread(new Runnable(){
					@Override
					public void run(){
	    				try {
	    					MainActivity.connection.resultSet = MainActivity.connection.statement.executeQuery("SELECT ONOMATEPONYMO,DIEYTHYNSI,TK,PERIOXI,TYPOS,LAT,LON FROM GASSTATIONS2 WHERE TYPOS='MIKTO'");
	    					while(MainActivity.connection.resultSet.next()){
	    						double lat = Double.parseDouble(MainActivity.connection.resultSet.getString("LAT"));
	    						double lon = Double.parseDouble(MainActivity.connection.resultSet.getString("LON"));
		    					String title = MainActivity.connection.resultSet.getString("DIEYTHYNSI") +", "+ MainActivity.connection.resultSet.getString("TK") +", "+ MainActivity.connection.resultSet.getString("PERIOXI");
	    						String snippet = MainActivity.connection.resultSet.getString("ONOMATEPONYMO");
		    					if(LatLngDistance(user_location.latitude, user_location.longitude, lat, lon) <= 10){
		    						addStationMarker(new LatLng(lat,lon),title,snippet,true);
		    					}
	    					}
	    				}
	    				catch (SQLException e) {
	    					e.printStackTrace();
	    				}
	    				runOnUiThread(new Runnable(){
    						@Override
    						public void run(){
    	    					wait.setText("");
    						}
        					});
					}
					}).start();
			}
			
		});
		
		closest.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				map.clear();
				addMyMarker();
	        	stations_shown = !stations_shown;
		        all.setVisibility(Button.INVISIBLE);
		        mixed.setVisibility(Button.INVISIBLE);
		        closest.setVisibility(Button.INVISIBLE);
				wait.setText("Please wait...");
				new Thread(new Runnable(){
					@Override
					public void run(){
	    				try {
	    					double min_d = 1000;
	    					String title="";
	    					String snippet="";
	    					boolean mxd = false;
	    					double lat = 0;
	    					double lon = 0;
	    					double selected_lat = 0;
	    					double selected_lon = 0;
	    					MainActivity.connection.resultSet = MainActivity.connection.statement.executeQuery("SELECT ONOMATEPONYMO,DIEYTHYNSI,TK,PERIOXI,TYPOS,LAT,LON FROM GASSTATIONS2");
	    					while(MainActivity.connection.resultSet.next()){
	    						lat = Double.parseDouble(MainActivity.connection.resultSet.getString("LAT"));
	    						lon = Double.parseDouble(MainActivity.connection.resultSet.getString("LON"));
	    						double dis = LatLngDistance(user_location.latitude, user_location.longitude, lat, lon);
		    					if( dis < min_d){
			    					min_d = dis;
			    					selected_lat = lat;
			    					selected_lon = lon;
		    						title = MainActivity.connection.resultSet.getString("DIEYTHYNSI") +", "+ MainActivity.connection.resultSet.getString("TK") +", "+ MainActivity.connection.resultSet.getString("PERIOXI");
		    						snippet = MainActivity.connection.resultSet.getString("ONOMATEPONYMO");
		    						if(MainActivity.connection.resultSet.getString("TYPOS").equals("MIKTO")){
		    							mxd = true;
		    						}
		    						else{
		    							mxd = false;
		    						}
		    					}
	    					}
	    					addStationMarker(new LatLng(selected_lat,selected_lon),title,snippet,mxd);
	    				}
	    				catch (SQLException e) {
	    					e.printStackTrace();
	    				}
	    				runOnUiThread(new Runnable(){
    						@Override
    						public void run(){
    	    					wait.setText("");
    						}
        					});
					}
					}).start();
			}
			
		});
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.stats) {
        	if(!stats_shown){
        		xy.setVisibility(Button.VISIBLE);
        		pie1.setVisibility(Button.VISIBLE);
        		pie2.setVisibility(Button.VISIBLE);
        		all.setVisibility(Button.INVISIBLE);
        		mixed.setVisibility(Button.INVISIBLE);
        		closest.setVisibility(Button.INVISIBLE);
        	}
        	else{
                xy.setVisibility(Button.INVISIBLE);
                pie1.setVisibility(Button.INVISIBLE);
                pie2.setVisibility(Button.INVISIBLE);
        		all.setVisibility(Button.INVISIBLE);
        		mixed.setVisibility(Button.INVISIBLE);
        		closest.setVisibility(Button.INVISIBLE);
        	}
			stations_shown = false;
        	stats_shown = !stats_shown;
            return true;
        }
        else if(id == R.id.stations){
        	if(!stations_shown){
        		all.setVisibility(Button.VISIBLE);
        		mixed.setVisibility(Button.VISIBLE);
        		closest.setVisibility(Button.VISIBLE);
                xy.setVisibility(Button.INVISIBLE);
                pie1.setVisibility(Button.INVISIBLE);
                pie2.setVisibility(Button.INVISIBLE);
        	}
        	else{
        		all.setVisibility(Button.INVISIBLE);
        		mixed.setVisibility(Button.INVISIBLE);
        		closest.setVisibility(Button.INVISIBLE);
                xy.setVisibility(Button.INVISIBLE);
                pie1.setVisibility(Button.INVISIBLE);
                pie2.setVisibility(Button.INVISIBLE);
        	}
			stats_shown = false;
        	stations_shown = !stations_shown;
        }
        else if(id == R.id.db){
			stats_shown = false;
        	stations_shown = false;
    		all.setVisibility(Button.INVISIBLE);
    		mixed.setVisibility(Button.INVISIBLE);
    		closest.setVisibility(Button.INVISIBLE);
            xy.setVisibility(Button.INVISIBLE);
            pie1.setVisibility(Button.INVISIBLE);
            pie2.setVisibility(Button.INVISIBLE);
        	new Thread(new Runnable(){
				@Override
				public void run(){
    				try {
    					MainActivity.connection.resultSet = MainActivity.connection.statement.executeQuery("SELECT * FROM GASSTATIONS");
    					GoogleMapsSnatcher gms = new GoogleMapsSnatcher();
    					while(MainActivity.connection.resultSet.next()){
    						String o = MainActivity.connection.resultSet.getString("ONOMATEPONYMO");
    						String prf = MainActivity.connection.resultSet.getString("PERIFEREIA");
    						String d = MainActivity.connection.resultSet.getString("DIEYTHYNSI");
    						String ty = MainActivity.connection.resultSet.getString("TYPOS");
    						String til = MainActivity.connection.resultSet.getString("TIL");
    						String prx = MainActivity.connection.resultSet.getString("PERIOXI");
    						String tk = MainActivity.connection.resultSet.getString("TK");
    						String n = MainActivity.connection.resultSet.getString("NOMOS");
    						String adrs = d+" "+prx;
    						LatLng st = gms.getLatLng(gms.getLocationFormGoogle(adrs));
    						if(st.latitude!=-90){
    							MainActivity.connection.statement2.executeUpdate("INSERT INTO GASSTATIONS2(ONOMATEPONYMO,PERIFEREIA,DIEYTHYNSI,TYPOS,TIL,PERIOXI,TK,NOMOS,LAT,LON) VALUES ('"+o+"','"+prf+"','"+d+"','"+ty+"','"+til+"','"+prx+"','"+tk+"','"+n+"','"+st.latitude+"','"+st.longitude+"');");
    						}
    					}
    					runOnUiThread(new Runnable(){
    						@Override
    						public void run(){
    							Toast toast = Toast.makeText(getApplicationContext(), "Database was updated with lat and lon values!", Toast.LENGTH_LONG);
    							toast.show();
    						}
    							
    					});
    				}
    				catch(SQLException e){
    					e.printStackTrace();
    				}
				}
        	}).start();
        }
        return super.onOptionsItemSelected(item);
    }
    
    private class CustomLocationListener implements LocationListener {  
        
		 @Override  
	     public void onLocationChanged(Location loc) {
			 user_location = new LatLng(loc.getLatitude(), loc.getLongitude());
			 addMyMarker();
	        }  
	  
	        @Override  
	        public void onProviderDisabled(String provider) {           
	        }  
	  
	        @Override  
	        public void onProviderEnabled(String provider) {           
	        }  
	  
	        @Override  
	        public void onStatusChanged(String provider,int status, Bundle extras) {           
	        }  
	    } 
    private static void addMyMarker(){

		UIHandler.post(new Runnable(){
			@Override
			public void run(){
				 map.clear();
		         map.addMarker(new MarkerOptions().position(user_location).title("You are here").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
		         // Move the camera instantly to me with a zoom of 15.
		         map.moveCamera(CameraUpdateFactory.newLatLngZoom(user_location, 15));
		
		         // Zoom in, animating the camera.
		         map.animateCamera(CameraUpdateFactory.zoomTo(15), 2500, null);
			}
		});
 }
    private static void addStationMarker(final LatLng ll, final String first, final String second, final boolean mixd){

		UIHandler.post(new Runnable(){
			@Override
			public void run(){
				 //map.clear();
				 if(mixd){
					 map.addMarker(new MarkerOptions().position(ll).snippet(second).title(first).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
				 }
				 else{
					 map.addMarker(new MarkerOptions().position(ll).snippet(second).title(first).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
				 }
			}
		});
 }
    
    //Haversine Formula
    private double LatLngDistance(double lat1, double lon1, double lat2, double lon2){
    	final double R = 6373; // In kilometers
    	lat1 = Math.toRadians(lat1);
    	lon1 = Math.toRadians(lon1);
    	lat2 = Math.toRadians(lat2);
    	lon2 = Math.toRadians(lon2);
		double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;
        double a = ((Math.sin(dLat/2))*(Math.sin(dLat/2))) + Math.cos(lat1) * Math.cos(lat2) * ((Math.sin(dLon/2))*(Math.sin(dLon/2))) ;
        double c = 2 * Math.atan2( Math.sqrt(a), Math.sqrt(1-a)); 
        return R * c;
    }
    
    
}

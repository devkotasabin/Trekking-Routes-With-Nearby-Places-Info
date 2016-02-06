package com.example.maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	GoogleMap map;
    ArrayList<LatLng> markerPoints;
    SearchResponse response;
    int sent_route_id;
    
    // KEY Strings
    public static String KEY_ROUTE_ID = "route_id"; // id of the place
    
    
    //accomodate the locations on map
  	private LatLngBounds.Builder latlngbuilder;
  	
  	// Links marker id and place object
    HashMap<String, Locations> mHMReference = new HashMap<String, Locations>();
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Intent i = getIntent();
		
		// Route Details
        response = (SearchResponse) i.getSerializableExtra("response");
        sent_route_id = Integer.valueOf(i.getStringExtra(KEY_ROUTE_ID));
		
		 // Initializing
        markerPoints = new ArrayList<LatLng>();
 
        // Getting reference to SupportMapFragment of the activity_main
        //SupportMapFragment fm = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
 
        // Getting Map for the SupportMapFragment
        //map = fm.getMap();
        
        
        map = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
 
        
        if(map!=null){
 
            // Enable MyLocation Button in the Map
            map.setMyLocationEnabled(true);
            
            // markerPoints.clear();
			// map.clear();
		
			// -debug-
	     	Log.e("Final", "Reached MainActivity befor drawpolyline");
	        
			
			populateMarkers();

            Button btn_draw_route = (Button) findViewById(R.id.btn_draw_route);
            btn_draw_route.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					// markerPoints.clear();
					// map.clear();
	                // latlngbuilder = new LatLngBounds.Builder();
	                
					// Getting URL to the Google Directions API
                    String url = getDirectionsUrl(true);
                    
                    // -debug-
                    Log.e("Final", url);
                    
					DownloadTask downloadTask = new DownloadTask();
					// Start downloading json data from Google Directions API
                    downloadTask.execute(url);
					
				}
			});
            
           // Setting a custom info window adapter for the google map
            map.setInfoWindowAdapter(new InfoWindowAdapter() {
     
                // Use default InfoWindow frame
                @Override
                public View getInfoWindow(Marker arg0) {
                    return null;
                }
     
                // Defines the contents of the InfoWindow
                @Override
                public View getInfoContents(Marker select_marker) {
     
                    // Getting view from the layout file info_window_layout
                    View v = getLayoutInflater().inflate(R.layout.info_window_layout, null);
     
                    Locations select_location = mHMReference.get(select_marker.getId());   
                    
                    
                 
                    // Getting reference to the TextView to set name
                    TextView tvname = (TextView) v.findViewById(R.id.tv_marker_name);
     
                    // Getting reference to the TextView to set address
                    TextView tvaddress = (TextView) v.findViewById(R.id.tv_marker_address);
     
                    // Getting reference to the TextView to set location
                    TextView tvlatlong = (TextView) v.findViewById(R.id.tv_marker_latlong);
     
                    // Getting reference to the TextView to set address
                    TextView tvsnippet = (TextView) v.findViewById(R.id.tv_marker_snippet);
     
                    // Setting the name
                    tvname.setText(select_location.name);
     
                    // Setting the address
                    tvaddress.setText(select_location.address);
     
                    // Setting the latlong
                    tvlatlong.setText("Lat: " + select_location.lat + " Lng: " + select_location.lng);
     
                    // Setting the snippet
                    tvsnippet.setText(select_location.snippet);
     
                    
                    // Returning the view containing InfoWindow contents
                    return v;
     
                }
            });
            
       }
	}

	private String getDirectionsUrl(boolean displaywaypoints){
		
		
        // Origin of route
        String str_origin = "origin="+ markerPoints.get(0).latitude +","+ markerPoints.get(0).longitude;
 
        // Destination of route
        String str_dest = "destination="+ markerPoints.get(markerPoints.size()-1).latitude + "," + markerPoints.get(markerPoints.size()-1).longitude;
 
        //Logic quick fix for same origin and destination
        // String str_dest = "destination="+ markerPoints.get(markerPoints.size()-2).latitude + "," + markerPoints.get(markerPoints.size()-2).longitude;
        
        // Sensor enabled
        String sensor = "sensor=false";
        
        // Waypoints
        String waypoints = "";
        for(int i=1;i < markerPoints.size() && i<=8 ;i++){
        	     LatLng point  = (LatLng) markerPoints.get(i);
                 if(i==1)
                     waypoints = "waypoints=";
                 waypoints += "via:" + point.latitude + "," + point.longitude + "|";
     
        }
        String parameters = null;
        // Building the parameters to the web service
        if(displaywaypoints){
        parameters = str_origin+"&"+str_dest+"&"+sensor+"&"+waypoints;
        }
        else {parameters = str_origin+"&"+str_dest+"&"+sensor;}
 
        // Output format
        String output = "json";
 
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
 
        // Hard Coded Value of Direction API request URL 
        // String url = "http://maps.googleapis.com/maps/api/directions/json?origin=Toronto&destination=Montreal&sensor=false";
        
        return url;
    }
	
	
    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);
 
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
 
            // Connecting to url
            urlConnection.connect();
 
            // Reading data from url
            iStream = urlConnection.getInputStream();
 
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
 
            StringBuffer sb = new StringBuffer();
 
            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }
 
            data = sb.toString();
 
            br.close();
 
        }catch(Exception e){
            Log.e("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
 
    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String>{
 
        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {
 
            // For storing data from web service
            String data = "";
 
            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.e("Background Task",e.toString());
            }
            return data;
        }
 
        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
 
            ParserTask parserTask = new ParserTask();
            
            try{
            JSONObject jObject = new JSONObject(result);
            
            if(!jObject.getString("status").equals("OK")) {
            	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
        				MainActivity.this);
            	// set title
    			alertDialogBuilder.setTitle("Sorry the route directions are not available for this route");
    			// set dialog message
    			alertDialogBuilder
    				.setMessage("Show anyway (without waypoints)?")
    				.setCancelable(false)
    				.setNegativeButton("Yes",new DialogInterface.OnClickListener() {
    					public void onClick(DialogInterface dialog,int id) {
    						
    						String url = getDirectionsUrl(false);
							DownloadTask downloadTask = new DownloadTask();
							// Start downloading json data from Google Directions API
		                    downloadTask.execute(url);
    					}
    				  })
    				  .setNeutralButton("No", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
							drawpolyline();
							
						}
					})
					.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
							
						}
					});
    			
    				         
    				// create alert dialog
    				AlertDialog alertDialog = alertDialogBuilder.create();
     
    				// show it
    				alertDialog.show();
    				
    				// -debug-
                    Log.e("Final", "Got inside MainActivity.DownloadTask.onpostexecute " + jObject.getString("status"));
                    
                    //Toast.makeText(MainActivity.this, "Showing route with no direction", Toast.LENGTH_SHORT).show();
    			
            	
            	}
            else {
            	// Invokes the thread for parsing the JSON data
                parserTask.execute(result);
            	}
            }
            catch(JSONException je){
            	MainActivity.this.finish();
            }
    
            
        }
    }
 
    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{
 
        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
 
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
 
            try{
                jObject = new JSONObject(jsonData[0]);
                
                // -debug-
                Log.e("Final", "Got inside MainActivity.ParserTask.doInBackground.statuscheck" + jObject.getString("status"));
                               
                
                /* A little hack to auto zoom map to the given source and destination pair */
                JSONArray jRoutes = jObject.getJSONArray("routes");
                JSONObject jBounds = ( (JSONObject)jRoutes.get(0)).getJSONObject("bounds");
                
                JSONObject jlatlng = jBounds.getJSONObject("northeast");
                LatLng ltlng = new LatLng(jlatlng.getDouble("lat"), jlatlng.getDouble("lng"));
                latlngbuilder = new LatLngBounds.Builder().include(ltlng);
                
                
                jlatlng = jBounds.getJSONObject("southwest");
                ltlng = new LatLng(jlatlng.getDouble("lat"), jlatlng.getDouble("lng"));
                latlngbuilder = latlngbuilder.include(ltlng);
                
                                
                DirectionsJSONParser parser = new DirectionsJSONParser();
 
                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }
 
        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            // MarkerOptions markerOptions = new MarkerOptions();
 
            
            
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(latlngbuilder.build(), 20);
			map.animateCamera(cu);
			
			// -debug-
            Log.e("Final", "Got inside MainActivity.ParserTask.onPostExecute" );
            
			
			// Commented for debugging 
			 //* 
			 
            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();
 
                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);
 
                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);
 
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
 
                    points.add(position);
                }
 
                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(5);
                lineOptions.color(Color.RED);
            }
        
            map.clear();
    		markerPoints.clear();
    		mHMReference.clear();
    		
    		populateMarkers();
            // Drawing polyline in the Google Map for the i-th route
            map.addPolyline(lineOptions);
            
            
            
        }
    }
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void drawpolyline(){
		
		latlngbuilder = new LatLngBounds.Builder();
		for (LatLng latlng : markerPoints ){
			latlngbuilder.include(latlng);
		}
		
		
		PolylineOptions straightlineoptions = new PolylineOptions();
		straightlineoptions.addAll(markerPoints);
		straightlineoptions.width(5);
		straightlineoptions.color(Color.RED);
		
		
		
		CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(latlngbuilder.build(), 20);
		map.animateCamera(cu);
		
		map.clear();
		markerPoints.clear();
		mHMReference.clear();
		populateMarkers();
		
		map.addPolyline(straightlineoptions);
        
	}

	public void populateMarkers(){
		List<Locations> recvlocations = response.locations;
		Marker usermarker = null;
		
		
		for(Locations loc: recvlocations){
							
			if(loc.route_id == sent_route_id) {
				LatLng ltlng = new LatLng(loc.lat, loc.lng);
				
				// Adding new item to the ArrayList
                markerPoints.add(ltlng);

                // Creating MarkerOptions
                MarkerOptions options = new MarkerOptions();

                // Setting the position of the marker
                options.position(ltlng);

                String icon;
                
                if(markerPoints.size()==1){
                	icon = "letter_s";
                } else {
                	icon = "number_" + String.valueOf(markerPoints.size() - 1);
                }
                	
                
                if(loc.type.equalsIgnoreCase("city")) {
                	icon = "citysquare";
                } else if(loc.type.equalsIgnoreCase("panorama")) {
                	icon = "panoramicview";
                } else if(loc.type.equalsIgnoreCase("smallcity")) {
                	icon = "smallcity";
                } else if(loc.type.equalsIgnoreCase("forest")) {
                	icon = "forest";
                } else if(loc.type.equalsIgnoreCase("spring")) {
                	icon = "fountain_2";
                }
                // * For the start location, marker is S and
                // * for the end location, the marker is E
                // * For the rest of markers, the marker is a number
                	                
                
                options.icon(BitmapDescriptorFactory.fromResource(getResources().getIdentifier(icon,"drawable", getPackageName())));
                
                // options.title(String.valueOf(loc.lat) + String.valueOf(loc.lng) );
                // options.snippet(loc.name + String.valueOf(loc.route_id));

                // Add new marker to the Google Map Android API V2
                usermarker = map.addMarker(options);
                
                // Linking Marker id and place reference
                mHMReference.put(usermarker.getId(), loc);
                
                
                // -debug- line
                // Toast.makeText(getBaseContext(), loc.name, Toast.LENGTH_SHORT).show();
                
			}

		}
		
		
		
		Locations loc = mHMReference.get(usermarker.getId());
		mHMReference.remove(usermarker.getId());
		String icon = "letter_e";
		
		if(loc.type.equalsIgnoreCase("city")) {
        	icon = "citysquare";
        } else if(loc.type.equalsIgnoreCase("panorama")) {
        	icon = "panoramicview";
        } else if(loc.type.equalsIgnoreCase("smallcity")) {
        	icon = "smallcity";
        } else if(loc.type.equalsIgnoreCase("forest")) {
        	icon = "forest";
        } else if(loc.type.equalsIgnoreCase("spring")) {
        	icon = "fountain_2";
        }
		
		
		MarkerOptions destoption= new MarkerOptions().position(usermarker.getPosition())
						.icon(BitmapDescriptorFactory.fromResource(getResources().getIdentifier(icon,"drawable", getPackageName())));
		usermarker.remove();
		usermarker = map.addMarker(destoption);
		
		// Linking Marker id and place reference
        mHMReference.put(usermarker.getId(), loc);

	}

}




package com.example.maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
 
import org.json.JSONObject;
 
import android.app.Dialog;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
 
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
public class NearbySearch extends FragmentActivity implements LocationListener{
 
    GoogleMap mGoogleMap;
    Spinner mSprPlaceType;
 
    String[] mPlaceType=null;
    String[] mPlaceTypeName=null;
 
    // Stores near by places
    Place[] mPlaces = null;
    
    // The location at which user touches the Google Map
    LatLng mLocation=null;
    double mLatitude=0;
    double mLongitude=0;
 
    HashMap<String, String> mMarkerPlaceLink = new HashMap<String, String>();
    Place[] placesimage = null;
    // Links marker id and place object
    HashMap<String, Place> mHMReference = new HashMap<String, Place>();
 
    // Specifies the drawMarker() to draw the marker with default color
    private static final float UNDEFINED_COLOR = -1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
 
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nearby_search);
 
        // Array of place types
        mPlaceType = getResources().getStringArray(R.array.place_type);
 
        // Array of place type names
        mPlaceTypeName = getResources().getStringArray(R.array.place_type_name);
 
        // Creating an array adapter with an array of Place types
        // to populate the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, mPlaceTypeName);
 
        // Getting reference to the Spinner
        mSprPlaceType = (Spinner) findViewById(R.id.spr_place_type);
 
        // Setting adapter on Spinner to set place types
        mSprPlaceType.setAdapter(adapter);
 
        Button btnFind;
 
        // Getting reference to Find Button
        btnFind = ( Button ) findViewById(R.id.btn_find);
 
        // Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
        if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available
 
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
        }else { // Google Play Services are available
 
            // Getting reference to the SupportMapFragment
             SupportMapFragment fragment = ( SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.the_map);
 
            // Getting Google Map
             mGoogleMap = fragment.getMap();
        	
        	// mGoogleMap = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
        	 
 
            // Enabling MyLocation in Google Map
            mGoogleMap.setMyLocationEnabled(true);
            
            
         // Handling screen rotation
            if(savedInstanceState !=null) {
 
                // Removes all the existing links from marker id to place object
                mHMReference.clear();
                
                mMarkerPlaceLink.clear();
 
                //If near by places are already saved
                if(savedInstanceState.containsKey("places")){
 
                    // Retrieving the array of place objects
                    mPlaces = (Place[]) savedInstanceState.getParcelableArray("places");
 
                    // Traversing through each near by place object
                    for(int i=0;i<mPlaces.length;i++){
 
                        // Getting latitude and longitude of the i-th place
                        LatLng point = new LatLng(Double.parseDouble(mPlaces[i].mLat),
                        Double.parseDouble(mPlaces[i].mLng));
 
                        // Drawing the marker corresponding to the i-th place
                        Marker m = drawMarker(point,UNDEFINED_COLOR);
 
                        // Linkng i-th place and its marker id
                        mHMReference.put(m.getId(), mPlaces[i]);
                    }
                }
 
                // If a touched location is already saved
                if(savedInstanceState.containsKey("location")){
 
                    // Retrieving the touched location and setting in member variable
                    mLocation = (LatLng) savedInstanceState.getParcelable("location");
 
                   // Drawing a marker at the touched location
                   drawMarker(mLocation, BitmapDescriptorFactory.HUE_GREEN);
               }
           }
 
            // Getting LocationManager object from System Service LOCATION_SERVICE
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
 
            // Creating a criteria object to retrieve provider
            Criteria criteria = new Criteria();
 
            // Getting the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);
 
            // Getting Current Location From GPS
            Location location = locationManager.getLastKnownLocation(provider);
 
            if(location!=null){
                onLocationChanged(location);
            }
 
            locationManager.requestLocationUpdates(provider, 20000, 0, this);
 
            mGoogleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
 
                @Override
                public void onInfoWindowClick(Marker arg0) {
                    Intent intent = new Intent(getBaseContext(), PlaceDetailsActivity.class);
                    String reference = mMarkerPlaceLink.get(arg0.getId());
                    intent.putExtra("reference", reference);
 
                    // Starting the Place Details Activity
                    startActivity(intent);
                }
            });
 
            // Setting click event lister for the find button
            btnFind.setOnClickListener(new OnClickListener() {
 
                @Override
                public void onClick(View v) {
 
                    int selectedPosition = mSprPlaceType.getSelectedItemPosition();
                    String type = mPlaceType[selectedPosition];
 
                    StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                    sb.append("location="+mLatitude+","+mLongitude);
                    sb.append("&radius=5000");
                    sb.append("&types="+type);
                    sb.append("&sensor=true");
                    sb.append("&key=AIzaSyCAxBRjL5673k9T0VaLd6Q2AY8sGEdn9bw");
 
                    // Creating a new non-ui thread task to download Google place json data
                    PlacesTask placesTask = new PlacesTask();
 
                    // Invokes the "doInBackground()" method of the class PlaceTask
                    placesTask.execute(sb.toString());
                }
            });
            
            // Marker click listener
            mGoogleMap.setOnMarkerClickListener(new OnMarkerClickListener() {
 
                @Override
                public boolean onMarkerClick(Marker marker) {
 
                    // If touched at User input location
                    if(!mHMReference.containsKey(marker.getId()))
                        return false;
 
                    // Getting place object corresponding to the currently clicked Marker
                    Place place = mHMReference.get(marker.getId());
 
                    // Creating an instance of DisplayMetrics
                    DisplayMetrics dm = new DisplayMetrics();
 
                    // Getting the screen display metrics
                    getWindowManager().getDefaultDisplay().getMetrics(dm);
 
                    // Creating a dialog fragment to display the photo
                    PlaceDialogFragment dialogFragment = new PlaceDialogFragment(place,dm);
 
                    // Getting a reference to Fragment Manager
                    FragmentManager fm = getSupportFragmentManager();
 
                    // Starting Fragment Transaction
                    FragmentTransaction ft = fm.beginTransaction();
 
                    // Adding the dialog fragment to the transaction
                    ft.add(dialogFragment, "TAG");
 
                    // Committing the fragment transaction
                    ft.commit();
 
                    return false;
                }
            });
        }
    }
 
    
    /**
     * A callback function, executed on screen rotation
     */
     @Override
     protected void onSaveInstanceState(Bundle outState) {
  
         // Saving all the near by places objects
         if(mPlaces!=null)
             outState.putParcelableArray("places", mPlaces);
  
         // Saving the touched location
         if(mLocation!=null)
             outState.putParcelable("location", mLocation);
  
         super.onSaveInstanceState(outState);
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
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
    /** A class, to download Google Places */
    private class PlacesTask extends AsyncTask<String, Integer, String>{
 
        String data = null;
 
        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(String... url) {
            try{
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }
 
        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result){
            ParserTask parserTask = new ParserTask();
 
            // Start parsing the Google places in JSON format
            // Invokes the "doInBackground()" method of the class ParseTask
            parserTask.execute(result);
        }
    }
 
    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{
 
        JSONObject jObject;
 
        // Invoked by execute() method of this object
        @Override
        protected List<HashMap<String,String>> doInBackground(String... jsonData) {
 
            List<HashMap<String, String>> places = null;
            PlaceJSONParser placeJsonParser = new PlaceJSONParser();
 
            
            PlaceJSONImageParser placeJsonImageParser = new PlaceJSONImageParser();
            
            try{
                jObject = new JSONObject(jsonData[0]);
 
                /** Getting the parsed data as a List construct */
                places = placeJsonParser.parse(jObject);
                placesimage = placeJsonImageParser.parse(jObject);
 
            }catch(Exception e){
                Log.d("Exception",e.toString());
            }
            return places;
        }
 
        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(List<HashMap<String,String>> list){
 
            // Clears all the existing markers
            mGoogleMap.clear();
 
            mPlaces = placesimage;
            
            for(int i=0;i<list.size();i++){
 
            	Place place = placesimage[i];
            	
            	// Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();
 
                // Getting a place from the places list
                HashMap<String, String> hmPlace = list.get(i);
 
                // Getting latitude of the place
                double lat = Double.parseDouble(hmPlace.get("lat"));
 
                // Getting longitude of the place
                double lng = Double.parseDouble(hmPlace.get("lng"));
 
                // Getting name
                String name = hmPlace.get("place_name");
 
                // Getting vicinity
                String vicinity = hmPlace.get("vicinity");
 
                LatLng latLng = new LatLng(lat, lng);
 
                // Setting the position for the marker
                markerOptions.position(latLng);
 
                int selectedPosition = mSprPlaceType.getSelectedItemPosition();
                String icon;
                
                switch(selectedPosition) {
                	case 0 : 
                		icon = "restaurant1";
                		break;
                
                	case 1 :
                		icon = "airport";
                		break;
                	
                	case 2:
                		icon = "atm_2";
                		break;
                	
                	case 3:
                		icon = "bank";
                		break;
                		
                	case 4 :
                		icon = "busstop";
                		break;
                		
                	case 5 :
                		icon = "hospital_building";
                		break;
                	
                	case 6 :
                		icon = "travel_agency";
                		break;
                	
                	case 7 :
                		icon = "taxi";
                		break;
                		
                	case 8 :
                		icon = "embassy";
                		break;
                		
                	case 9 :
                		icon = "bar";
                		break;
                		
                	default :
                		icon = "hotelicon1";
                		break;
                
                }
                
                		
                // Setting the title for the marker.
                //This will be displayed on taping the marker
                markerOptions.title(name + " : " + vicinity)
                	.icon(BitmapDescriptorFactory.fromResource(getResources().getIdentifier(icon, "drawable", getPackageName())));
 
                // Placing a marker on the touched position
                Marker m = mGoogleMap.addMarker(markerOptions);
 
                // Linking Marker id and place reference
                mMarkerPlaceLink.put(m.getId(), hmPlace.get("reference"));
                
                // Adding place reference to HashMap with marker id as HashMap key
                // to get its reference in infowindow click event listener
                mHMReference.put(m.getId(), place);
                
            }
        }
    }
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
 
    @Override
    public void onLocationChanged(Location location) {
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
        LatLng latLng = new LatLng(mLatitude, mLongitude);
 
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
    }
 
    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }
 
@Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }
 
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }
    
    /**
     * Drawing marker at latLng with color
     */
     private Marker drawMarker(LatLng latLng,float color){
         // Creating a marker
         MarkerOptions markerOptions = new MarkerOptions();
  
         // Setting the position for the marker
         markerOptions.position(latLng);
  
         if(color != UNDEFINED_COLOR)
             markerOptions.icon(BitmapDescriptorFactory.defaultMarker(color));
  
         // Placing a marker on the touched position
         Marker m = mGoogleMap.addMarker(markerOptions);
  
         return m;
     }
}
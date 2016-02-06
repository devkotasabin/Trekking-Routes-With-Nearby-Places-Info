package com.example.maps;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

public class AndroidJsonProject extends Activity {

	String url = "http://10.100.2.95/jsonscript/route_search_region.php";
	SearchResponse response;
	String region;
	boolean data_not_available = false;
	
	// KEY Strings
    public static String KEY_ROUTE_ID = "route_id"; // id of the place
    public static String KEY_NAME = "name"; // name of the place
    public static String KEY_ADDRESS = "address"; // Place area name
	public static String KEY_ESTIMATE_COST = "estimate_cost"; //estimate cost of the route
	public static String KEY_TIME_DURATION = "time_duration"; //time duration of the route
	public static String KEY_SNIPPET = "snippet"; //snipppet of the route
	public static String KEY_RATING = "rating"; //rating of the route
	public static String KEY_REGION = "region"; //region of the route
    
	// ListItems data
    ArrayList<HashMap<String, String>> routeListItems = new ArrayList<HashMap<String,String>>();
	
    // Progress dialog
    ProgressDialog pDialog;
     
    // Places Listview
    ListView lv;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_android_json_project);
		
		Intent i = getIntent();
		// Route Details
        region = i.getStringExtra("region");
		url += "?region=" + region;
		
		// Getting listview
        lv = (ListView) findViewById(R.id.listView1);
        
        
        /**
         * ListItem click event
         * On selecting a listitem SinglePlaceActivity is launched
         * */
        
        lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				
				// getting values from selected ListItem        
        		String route_id = ((TextView) view.findViewById(R.id.tv_route_id)).getText().toString();
                // Starting new intent
                Intent in = new Intent(AndroidJsonProject.this,
                        MainActivity.class);
                // Sending place route id to single place activity
                // route id used to get "Place full details"
                            
                in.putExtra(KEY_ROUTE_ID, route_id);
             // passing route data to map activity
        		in.putExtra("response", response);
                startActivity(in);
                
                
			}
        });
        
        new ParseJson().execute();
		
		//Button btn_show_in_map = (Button) findViewById(R.id.btn_show_in_map);
		//btn_show_in_map.setOnClickListener(new OnClickListener() {
			
		//	@Override
		//	public void onClick(View v) {
		//		Intent i = new Intent(getApplicationContext(),
        //                MainActivity.class);
                 
                // passing route data to map activity
		//         i.putExtra("response", response);
                // staring activity
        //        startActivity(i);
				
		//	}
		// });
	
	}
	
	private InputStream retrieveStream(String url) {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet getRequest = new HttpGet(url);

		try {
			           HttpResponse getResponse = client.execute(getRequest);
			           final int statusCode = getResponse.getStatusLine().getStatusCode();
			           if (statusCode != HttpStatus.SC_OK) {
			        	   Log.w(getClass().getSimpleName(), "Error " + statusCode + " for URL " + url);
			               return null;
			           }
			
			           HttpEntity getResponseEntity = getResponse.getEntity();
			           return getResponseEntity.getContent();
			
			 }
		catch (IOException e) {
		              getRequest.abort();
			          Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
			 }
		return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.activity_android_json_project, menu);
		return true;
	}
	
	private class ParseJson extends AsyncTask<String, String, String> {	
		
		/**
         * Before starting background thread Show Progress Dialog
         * */
		/*
        @Override
        protected void onPreExecute() {
            // super.onPreExecute();
            pDialog = new ProgressDialog(AndroidJsonProject.this);
            pDialog.setMessage(Html.fromHtml("<b>Search</b><br/>Loading Routes..."));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 		*/
		
		
		@Override
		protected String doInBackground(String... urls) {
						
			try {
				InputStream source = retrieveStream(url);
				Reader reader = new InputStreamReader(source);
				Gson gson = new Gson();
				
				// -debug-
	            Log.e("Final", "Reached before response");
	            
				response = gson.fromJson(reader, SearchResponse.class);
				
			}
			catch(Exception e) {
				Log.e("JSONPARSE", "Unable to parse JSON "+e.toString());	
				e.printStackTrace();
				data_not_available = true;
				
				
			}
			
			// -debug-
            Log.e("Final", "Reached inside doInBackground");
            
			return null;
			
		}
		
		
		/**
         * After completing background task Dismiss the progress dialog
         * and show the data in UI
         * Always use runOnUiThread(new Runnable()) to update UI from background
         * thread, otherwise you will get error
         * **/
		//process data retrieved from doInBackground
		@Override
		protected void onPostExecute(String result) {
			
			// dismiss the dialog after getting all products
			// pDialog.dismiss();
			//Toast.makeText(AndroidJsonProject.this, "Data Loaded", Toast.LENGTH_SHORT).show();
			
            // -debug-
            Log.e("Final", "Reached inside onpostexecute of parseJSON and dismissed dialog");
            
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                
                if(data_not_available){
                	AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(AndroidJsonProject.this);
    				alertdialogbuilder.setTitle("No Route Information found")
    				.setMessage("Sorry there is no route information for this region")
    				.setNegativeButton("OK", new OnClickListener() {
    					
    					@Override
    					public void onClick(DialogInterface dialog, int which) {
    						AndroidJsonProject.this.finish();
    						
    					}
    				});
    				
    				alertdialogbuilder.setCancelable(false);
    				alertdialogbuilder.show();
                }
                	
				if (response.routes != null){
				
					for (Routes r : response.routes) {
		                HashMap<String, String> map = new HashMap<String, String>();
		                 
		                // Place id won't display in listview - it will be hidden
		                // Place id is used to get "place full details"
		                map.put(KEY_ROUTE_ID, String.valueOf(r.route_id));
		                 
		                // Place name
		                map.put(KEY_NAME, r.name);
		                map.put(KEY_ADDRESS,r.address);
		                map.put(KEY_ESTIMATE_COST,String.valueOf(r.estimate_cost));
		                map.put(KEY_RATING, String.valueOf(r.rating));
		                map.put(KEY_REGION, r.region);
		                map.put(KEY_SNIPPET, r.snippet);
		                map.put(KEY_TIME_DURATION,String.valueOf(r.time_duration));
		                 
		                // adding HashMap to ArrayList
		                routeListItems.add(map);
		            }
				
				
		            		             
		            // Adding data into listview
		            lv.setAdapter(new MyAdapter(AndroidJsonProject.this, routeListItems));
					
				}
            
            }
         });
            
        		
		}	
	}
		
}

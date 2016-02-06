package com.example.maps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ListSelectActivity extends Activity {

	// Declare the UI components
    ListView regionsListView;	
    ArrayAdapter<String> arrayAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_select_activity);
		
		//storing string resources into array
		String[] regions = getResources().getStringArray(R.array.regions);
		
		// Initialize the UI components
        regionsListView = (ListView) findViewById(R.id.list_regions);
        // For this moment, you have ListView where you can display a list.
        // But how can we put this data set to the list?
        // This is where you need an Adapter
 
        // context - The current context.
        // resource - The resource ID for a layout file containing a layout 
                // to use when instantiating views.
        // Third parameter gives the layout resource id for individual list item
        // From the fourth parameter, you plugged the data set to adapter
        
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.label, regions);
 
        // By using setAdapter method, you plugged the ListView with adapter
        regionsListView.setAdapter(arrayAdapter);
	    
		//listening to single list item on click
		regionsListView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
						//selected item
						String region = ((TextView) view.findViewById(R.id.label)).getText().toString();
						
						// Launching new Activity on selecting single List Item
			            Intent i = new Intent(getApplicationContext(), AndroidJsonProject.class);
			            // sending data to new activity
			            i.putExtra("region", region);
			            startActivity(i);
			             
						
					}
					
		});
		
		
		
		
		
		Button btn_local_search = (Button) findViewById(R.id.btn_nearby_search);
		btn_local_search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), NearbySearch.class);
				startActivity(i);
			}
		});
		
	}
}

package com.example.maps;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {
	
	private LayoutInflater layoutInflater;
	private ArrayList<HashMap<String, String>> listdata;

	// KEY Strings
    public static String KEY_ROUTE_ID = "route_id"; // id of the place
    public static String KEY_NAME = "name"; // name of the place
    public static String KEY_ADDRESS = "address"; // Place area name
	public static String KEY_ESTIMATE_COST = "estimate_cost"; //estimate cost of the route
	public static String KEY_TIME_DURATION = "time_duration"; //time duration of the route
	public static String KEY_SNIPPET = "snippet"; //snipppet of the route
	public static String KEY_RATING = "rating"; //rating of the route
	public static String KEY_REGION = "region"; //region of the route
	
	public MyAdapter(Context context, ArrayList<HashMap<String, String>> data) {
		
		layoutInflater = LayoutInflater.from(context);
		this.listdata = data;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if (convertView == null){
			
			convertView = layoutInflater.inflate(R.layout.list_route_item, null);
            holder = new ViewHolder();
            
            holder.addressview = (TextView) convertView.findViewById(R.id.tv_address);
            holder.costview = (TextView) convertView.findViewById(R.id.tv_cost);
            holder.durationview = (TextView) convertView.findViewById(R.id.tv_duration);
            holder.nameview = (TextView) convertView.findViewById(R.id.tv_name);
            holder.ratingbarView = (RatingBar) convertView.findViewById(R.id.rating_route);
            holder.regionView = (TextView) convertView.findViewById(R.id.tv_region);
            holder.route_idview = (TextView) convertView.findViewById(R.id.tv_route_id);
            holder.snippetview = (TextView) convertView.findViewById(R.id.tv_snippet);
        
            convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		 	holder.addressview.setText(listdata.get(position).get(KEY_ADDRESS));
	        holder.costview.setText("Rs. " + listdata.get(position).get(KEY_ESTIMATE_COST)+ "(approx)");
	        holder.durationview.setText(listdata.get(position).get(KEY_TIME_DURATION) + " days");
	        holder.nameview.setText(listdata.get(position).get(KEY_NAME));
	        holder.ratingbarView.setRating(Float.valueOf(listdata.get(position).get(KEY_RATING)));
	        holder.regionView.setText(listdata.get(position).get(KEY_REGION));
	        holder.route_idview.setText(listdata.get(position).get(KEY_ROUTE_ID));
	        holder.snippetview.setText(listdata.get(position).get(KEY_SNIPPET));
	        
	        
	        return convertView;
		
		
	}
	
	static class ViewHolder {
        TextView route_idview; 
        TextView addressview;
        TextView costview;
        TextView nameview;
        TextView snippetview;
        TextView durationview;
        TextView regionView;
        RatingBar ratingbarView;
       
    }

	@Override
	public int getCount() {
		
		return listdata.size();
	}

	@Override
	public Object getItem(int position) {
		
		return listdata.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}
			 
}
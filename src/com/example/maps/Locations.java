package com.example.maps;

import java.io.Serializable;

public class Locations implements Serializable {

	
	public int location_id;
	public int route_id;
	public String name;
	public int way_order;
	public String address;
	public Double lat;
	public Double lng;
	public String snippet;
	public String type;
}

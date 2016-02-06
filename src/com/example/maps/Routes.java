package com.example.maps;

import java.io.Serializable;

public class Routes implements Serializable {

	public int route_id;
	public String name;
	public String address;
	public Double estimate_cost;
	public int time_duration;
	public String snippet;
	public Double rating;
	public String region;
}

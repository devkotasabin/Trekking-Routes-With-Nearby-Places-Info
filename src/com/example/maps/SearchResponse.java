package com.example.maps;

import java.io.Serializable;
import java.util.List;

public class SearchResponse implements Serializable {

	public List<Routes> routes;	
	public List<Locations> locations;
	
}

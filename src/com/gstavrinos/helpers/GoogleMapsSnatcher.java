package com.gstavrinos.helpers;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;

public class GoogleMapsSnatcher {
	
	public  JSONObject getLocationFormGoogle(String ad) {

	    HttpGet httpGet = new HttpGet("http://maps.google.com/maps/api/geocode/json?address=" +ad+"&ka&sensor=false");
	    HttpClient client = new DefaultHttpClient();
	    HttpResponse response;
	    StringBuilder stringBuilder = new StringBuilder();

	    try {
	        response = client.execute(httpGet);
	        HttpEntity entity = response.getEntity();
	        InputStream stream = entity.getContent();
	        int b;
	        while ((b = stream.read()) != -1) {
	            stringBuilder.append((char) b);
	        }
	    } catch (ClientProtocolException e) {
	    } catch (IOException e) {
	    }

	    JSONObject jsonObject = new JSONObject();
	    try {
	        jsonObject = new JSONObject(stringBuilder.toString());
	    } catch (JSONException e) {

	        e.printStackTrace();
	    }

	    return jsonObject;
	}

	public  LatLng getLatLng(JSONObject jsonObject) {

	    double lng = 0;
	    double lat = 0;
	    try {
	    	if(/*arr.length() > 0*/!jsonObject.getString("status").contains("ZERO")){
	    		lng = jsonObject.getJSONArray("results").getJSONObject(0)
	            .getJSONObject("geometry").getJSONObject("location")
	            .getDouble("lng");

	    		lat = jsonObject.getJSONArray("results").getJSONObject(0)
	            .getJSONObject("geometry").getJSONObject("location")
	            .getDouble("lat");
			}
	    	else{
	    		lat = -90;
	    		lng = 81;
	    	}
	    } 
	    catch (JSONException e) {
	        e.printStackTrace();
	        lat = -90;
    		lng = 81;
	    }

	    return new LatLng(lat,lng);
	}
}

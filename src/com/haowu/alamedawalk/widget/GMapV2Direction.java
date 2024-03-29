package com.haowu.alamedawalk.widget;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.google.android.gms.maps.model.LatLng;
import android.util.Log;

public class GMapV2Direction {
        public final static String MODE_DRIVING = "driving";
        public final static String MODE_WALKING = "walking";
        public final static String MODE_BIKING = "bicycling";
        public final static String MODE_TRANSIT = "transit";

        public GMapV2Direction() {
        }

        public Document getDocument(LatLng start, String end, String mode) {
        		end = end.replace(' ', '+');
        		String url;
        		if (mode.equals(MODE_TRANSIT)) {
	                url = "http://maps.googleapis.com/maps/api/directions/xml?"
	                                + "origin=" + start.latitude + "," + start.longitude
	                                + "&destination=" + end
	                                + "&sensor=false&units=metric&mode=" + mode 
	                                + "&departure_time=" + System.currentTimeMillis()/1000 //epoch time
	                                + "&alternatives=true"; 
        		}
        		else {
        			url = "http://maps.googleapis.com/maps/api/directions/xml?"
                            + "origin=" + start.latitude + "," + start.longitude
                            + "&destination=" + end
                            + "&sensor=false&units=metric&mode=" + mode
                            + "&alternatives=true"; 
        		}

                try {
                        HttpClient httpClient = new DefaultHttpClient();
                        HttpContext localContext = new BasicHttpContext();
                        HttpPost httpPost = new HttpPost(url);
                        HttpResponse response = httpClient.execute(httpPost, localContext);
                        InputStream in = response.getEntity().getContent();
                        DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                                        .newDocumentBuilder();
                        Document doc = builder.parse(in);
                        return doc;
                } catch (Exception e) {
                        e.printStackTrace();
                }
                return null;
        }
        public Document getDocument(String start, LatLng end, String mode) {
        	start = start.replace(' ', '+');
        	String url;
        	if (mode.equals(MODE_TRANSIT)) {
        		url = "http://maps.googleapis.com/maps/api/directions/xml?"
        				+ "origin=" + start 
        				+ "&destination=" + end.latitude + "," + end.longitude
        				+ "&sensor=false&units=metric&mode=" + mode 
        				+ "&departure_time=" + System.currentTimeMillis()/1000 //epoch time
        				+ "&alternatives=true"; 
        	}
        	else {
        		url = "http://maps.googleapis.com/maps/api/directions/xml?"
        				+ "origin=" + start 
        				+ "&destination=" + end.latitude + "," + end.longitude
        				+ "&sensor=false&units=metric&mode=" + mode
        				+ "&alternatives=true"; 
        	}
        	
        	try {
        		HttpClient httpClient = new DefaultHttpClient();
        		HttpContext localContext = new BasicHttpContext();
        		HttpPost httpPost = new HttpPost(url);
        		HttpResponse response = httpClient.execute(httpPost, localContext);
        		InputStream in = response.getEntity().getContent();
        		DocumentBuilder builder = DocumentBuilderFactory.newInstance()
        				.newDocumentBuilder();
        		Document doc = builder.parse(in);
        		return doc;
        	} catch (Exception e) {
        		e.printStackTrace();
        	}
        	return null;
        }
        
        public Document getDocument(String start, String end, String mode) {
        	start = start.replace(' ', '+');
        	end = end.replace(' ', '+');
        	String url;
        	if (mode == MODE_TRANSIT) {
	        	url = "http://maps.googleapis.com/maps/api/directions/xml?"
	        			+ "origin=" + start
	        			+ "&destination=" + end
	        			+ "&sensor=false&units=metric&mode=" + mode 
	        			+ "&departure_time=" + System.currentTimeMillis()/1000 //epoch time
	        			+ "&alternatives=true"; 
        	}
        	else {
	        	url = "http://maps.googleapis.com/maps/api/directions/xml?"
	        			+ "origin=" + start
	        			+ "&destination=" + end
	        			+ "&sensor=false&units=metric&mode=" + mode
	        			+ "&alternatives=true"; 
        	}
        	
        	try {
        		HttpClient httpClient = new DefaultHttpClient();
        		HttpContext localContext = new BasicHttpContext();
        		HttpPost httpPost = new HttpPost(url);
        		HttpResponse response = httpClient.execute(httpPost, localContext);
        		InputStream in = response.getEntity().getContent();
        		DocumentBuilder builder = DocumentBuilderFactory.newInstance()
        				.newDocumentBuilder();
        		Document doc = builder.parse(in);
        		return doc;
        	} catch (Exception e) {
        		e.printStackTrace();
        	}
        	return null;
        }
        
        public Document getDocument(LatLng start, LatLng end, String mode) {
        	
    		String url;
    		if (mode == MODE_TRANSIT) {
                url = "http://maps.googleapis.com/maps/api/directions/xml?"
                                + "origin=" + start.latitude + "," + start.longitude
                                + "&destination=" + end.latitude + "," +end.longitude
                                + "&sensor=false&units=metric&mode=" + mode 
                                + "&departure_time=" + System.currentTimeMillis()/1000 //epoch time
                                + "&alternatives=true"; 
    		}
    		else {
    			url = "http://maps.googleapis.com/maps/api/directions/xml?"
                        + "origin=" + start.latitude + "," + start.longitude
                        + "&destination=" + end.latitude + "," +end.longitude
                        + "&sensor=false&units=metric&mode=" + mode
                        + "&alternatives=true"; 
    		}

            try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpContext localContext = new BasicHttpContext();
                    HttpPost httpPost = new HttpPost(url);
                    HttpResponse response = httpClient.execute(httpPost, localContext);
                    InputStream in = response.getEntity().getContent();
                    DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                                    .newDocumentBuilder();
                    Document doc = builder.parse(in);
                    return doc;
            } catch (Exception e) {
                    e.printStackTrace();
            }
            return null;
        }

        public String getDurationText(Document doc) {
                NodeList nl1 = doc.getElementsByTagName("duration");
                Node node1 = nl1.item(0);
                NodeList nl2 = node1.getChildNodes();
                Node node2 = nl2.item(getNodeIndex(nl2, "text"));
                Log.i("DurationText", node2.getTextContent());
                return node2.getTextContent();
        }

        public int getDurationValue(Document doc) {
                NodeList nl1 = doc.getElementsByTagName("duration");
                Node node1 = nl1.item(0);
                NodeList nl2 = node1.getChildNodes();
                Node node2 = nl2.item(getNodeIndex(nl2, "value"));
                Log.i("DurationValue", node2.getTextContent());
                return Integer.parseInt(node2.getTextContent());
        }

        public String getDistanceText(Document doc) {
                NodeList nl1 = doc.getElementsByTagName("distance");
                Node node1 = nl1.item(0);
                NodeList nl2 = node1.getChildNodes();
                Node node2 = nl2.item(getNodeIndex(nl2, "text"));
                Log.i("DistanceText", node2.getTextContent());
                return node2.getTextContent();
        }

        public int getDistanceValue(Document doc) {
                NodeList nl1 = doc.getElementsByTagName("distance");
                Node node1 = nl1.item(0);
                NodeList nl2 = node1.getChildNodes();
                Node node2 = nl2.item(getNodeIndex(nl2, "value"));
                Log.i("DistanceValue", node2.getTextContent());
                return Integer.parseInt(node2.getTextContent());
        }

        public String getStartAddress(Document doc) {
                NodeList nl1 = doc.getElementsByTagName("start_address");
                Node node1 = nl1.item(0);
                Log.i("StartAddress", node1.getTextContent());
                return node1.getTextContent();
        }

        public String getEndAddress(Document doc) {
                NodeList nl1 = doc.getElementsByTagName("end_address");
                Node node1 = nl1.item(0);
                Log.i("StartAddress", node1.getTextContent());
                return node1.getTextContent();
        }

        public String getCopyRights(Document doc) {
                NodeList nl1 = doc.getElementsByTagName("copyrights");
                Node node1 = nl1.item(0);
                Log.i("CopyRights", node1.getTextContent());
                return node1.getTextContent();
        }

        public ArrayList<ArrayList<LatLng>> getDirection(Document doc) {
			ArrayList<ArrayList<LatLng>> listRoutes = new ArrayList<ArrayList<LatLng>>();
			NodeList rl1, rl2, rl3, nl1, nl2, nl3;
			rl1 = doc.getElementsByTagName("route");
			// for each route...
			if (rl1.getLength() > 0) {
				for (int k = 0; k < rl1.getLength(); k++) {
					Node rnode1 = rl1.item(k);
					rl2 = rnode1.getChildNodes();
					Node rnode2 = rl2.item(getNodeIndex(rl2, "leg"));
					// finally at steps.
					rl3 = rnode2.getChildNodes();
					// get each step index
					ArrayList<Integer> stepIndices = getStepIndices(rl3);
					ArrayList<LatLng> listGeopoints = new ArrayList<LatLng>();
					if (stepIndices.size() > 0) {
						for (int i = 0; i < stepIndices.size(); i++) {
							Node node1 = rl3.item(stepIndices.get(i));
	                        nl2 = node1.getChildNodes();
	
	                        Node locationNode = nl2
	                                        .item(getNodeIndex(nl2, "start_location"));
	                        nl3 = locationNode.getChildNodes();
	                        Node latNode = nl3.item(getNodeIndex(nl3, "lat"));
	                        double lat = Double.parseDouble(latNode.getTextContent());
	                        Node lngNode = nl3.item(getNodeIndex(nl3, "lng"));
	                        double lng = Double.parseDouble(lngNode.getTextContent());
	                        listGeopoints.add(new LatLng(lat, lng));
	
	                        locationNode = nl2.item(getNodeIndex(nl2, "polyline"));
	                        nl3 = locationNode.getChildNodes();
	                        latNode = nl3.item(getNodeIndex(nl3, "points"));
	                        ArrayList<LatLng> arr = decodePoly(latNode.getTextContent());
	                        for (int j = 0; j < arr.size(); j++) {
	                                listGeopoints.add(new LatLng(arr.get(j).latitude, arr
	                                                .get(j).longitude));
	                        }
	
	                        locationNode = nl2.item(getNodeIndex(nl2, "end_location"));
	                        nl3 = locationNode.getChildNodes();
	                        latNode = nl3.item(getNodeIndex(nl3, "lat"));
	                        lat = Double.parseDouble(latNode.getTextContent());
	                        lngNode = nl3.item(getNodeIndex(nl3, "lng"));
	                        lng = Double.parseDouble(lngNode.getTextContent());
	                        listGeopoints.add(new LatLng(lat, lng));
						}
					}
					listRoutes.add(listGeopoints);
				}
			}
			return listRoutes;
        }
        
        private ArrayList<Integer> getStepIndices(NodeList n) {
        	ArrayList<Integer> indices = new ArrayList<Integer>();
        	for (int i = 0; i < n.getLength(); i++) {
        		if (n.item(i).getNodeName().equals("step"))
        			indices.add(i);
        	}
        	return indices;
        }

        private int getNodeIndex(NodeList nl, String nodename) {
                for (int i = 0; i < nl.getLength(); i++) {
                        if (nl.item(i).getNodeName().equals(nodename))
                                return i;
                }
                return -1;
        }
        

        public ArrayList<LatLng> decodePoly(String encoded) {
                ArrayList<LatLng> poly = new ArrayList<LatLng>();
                int index = 0, len = encoded.length();
                int lat = 0, lng = 0;
                while (index < len) {
                        int b, shift = 0, result = 0;
                        do {
                                b = encoded.charAt(index++) - 63;
                                result |= (b & 0x1f) << shift;
                                shift += 5;
                        } while (b >= 0x20);
                        int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                        lat += dlat;
                        shift = 0;
                        result = 0;
                        do {
                                b = encoded.charAt(index++) - 63;
                                result |= (b & 0x1f) << shift;
                                shift += 5;
                        } while (b >= 0x20);
                        int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                        lng += dlng;

                        LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
                        poly.add(position);
                }
                return poly;
        }
}

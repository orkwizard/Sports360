package sports365.searchAPI;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.TypeResolutionContext.Basic;

import models.data.events.Datum;
import models.data.events.Events;
import models.searcher.Searcher;
import sports365.searchAPI.SearchEvent.EndPoints;

public class SearchEvent {

	static URIBuilder url;
	HttpGet request;
//	String perPage = "20";
	String user,pwd,api_key;
	ObjectMapper om = new ObjectMapper();
	String api_url;
	
	
	
	public SearchEvent(String api_url,String usr,String pw,String apikey) {
		super();
		this.api_url = api_url;
		this.user=usr;  //justgonow-en
		this.pwd=pw;	//FgUPL^j7E65H5Of&
		this.api_key=apikey;  //"8954ab8892fd484b2b3cb463e8a565a2"
	}
	
	

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return pwd;
	}

	public void setPassword(String pwd) {
		this.pwd = pwd;
	}



	public enum EndPoints{
		byId,
		byCountry,
		byCity,
		byVenue,
		byParticipant,
		byCityAndParticipant,
		byVenueAndParticipant,
		byCountryAndParticipant,
		byTournament,
		byCityAndTournament,
		byVenueAndTournament,
		byCountryAndTournament,
		
	};
	
	public static  String getEndpoint(EndPoints endpoint) {
		String result="";
		switch(endpoint) {
		case byId: result = "https://api-v2.sportsevents365.com/events/{id}";break;
		case byCountry: result = "https://api-v2.sportsevents365.com/events/country/{id}";break;
		
		case byCity: result = "https://api-v2.sportsevents365.com/events/city/{id}";break;
		case byVenue:result = "https://api-v2.sportsevents365.com/events/venue/{id}";break;
		case byParticipant:result = "https://api-v2.sportsevents365.com/events/participant/{id}";break;
		case byTournament :result = "https://api-v2.sportsevents365.com/events/tournament/{id}";break;

		
		case byCityAndParticipant :result = "https://api-v2.sportsevents365.com/events/participant/{id}/city/{cityId}";break;
		case byVenueAndParticipant :result = "https://api-v2.sportsevents365.com/events/participant/{id}/venue/{venueId}";break;
		case byCountryAndParticipant :result = "https://api-v2.sportsevents365.com/events/participant/{id}/city/{countryId}";break;
		
		case byCityAndTournament : result = "https://api-v2.sportsevents365.com/events/tournament/{id}/city/{cityId}";break;
		case byVenueAndTournament : result = "https://api-v2.sportsevents365.com/events/tournament/{id}/venue/{venueId}";break;
		case byCountryAndTournament : result = "https://api-v2.sportsevents365.com/events/tournament/{id}/country/{countryId}";break;
		
		}
		return result;
	}
	
	
	
	private String getEncodedCredentials() {
		 return  Base64.getEncoder().encodeToString((this.user + ":" + this.pwd).getBytes(StandardCharsets.UTF_8));
	}
	
	
	public  Events getEvents(Searcher aSearch,HashMap<String,String> map) throws URISyntaxException, ClientProtocolException, IOException {
		String json;
		String endpoint="";
		switch(aSearch.type) {
		case "performer": endpoint = getEndpoint(EndPoints.byParticipant);break;
		case "venue": endpoint = SearchEvent.getEndpoint(SearchEvent.EndPoints.byVenue);break;
		case "competitor" : endpoint=  SearchEvent.getEndpoint(SearchEvent.EndPoints.byParticipant);break;
		case "city": endpoint=  SearchEvent.getEndpoint(SearchEvent.EndPoints.byCity);break;
		}
		String uri = endpoint.replace("{id}",aSearch.id+"");

		url = new URIBuilder(uri);
		url.setParameter("apiKey", api_key);
		CloseableHttpClient client = HttpClientBuilder.create().build();
		request = new HttpGet(url.build());
		request.setHeader("Authorization", "Basic "+ getEncodedCredentials()); //anVzdGdvbm93LWVuOkZnVVBMXmo3RTY1SDVPZiY=
		CloseableHttpResponse response = client.execute(request);
		HttpEntity entity = response.getEntity();
		if(entity!=null) {
			 json =  EntityUtils.toString(entity);
			 return om.readValue(json, Events.class);
		}
		client.close();
		return null;
	}
	
	public  Events getEvents(EndPoints eventsParameters,HashMap<String,String> map) throws URISyntaxException, IOException {
		String uri="";
		String json="";
		ArrayList<NameValuePair> params = new ArrayList<>();
		
		if(!map.isEmpty()) {
			uri = getEndpoint(eventsParameters);
				
			for(Map.Entry<String, String> entry : map.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				if(key.contains("{")){
					String t = uri.replace(key,value);
					uri = t;
					
				}else {
					NameValuePair param = new BasicNameValuePair(key, value);
					params.add(param);
					
				}
			}
		}
		else {
			uri = getEndpoint(eventsParameters);
		};
		url = new URIBuilder(uri);
		url.setParameter("apiKey", this.api_key);
		url.addParameters(params);
		CloseableHttpClient client = HttpClientBuilder.create().build();
		request = new HttpGet(url.build());
		request.setHeader("Authorization", "Basic "+ getEncodedCredentials()); //anVzdGdvbm93LWVuOkZnVVBMXmo3RTY1SDVPZiY=
		CloseableHttpResponse response = client.execute(request);
		HttpEntity entity = response.getEntity();
		if(entity!=null) {
			 json =  EntityUtils.toString(entity);
			 return om.readValue(json, Events.class);
		}
		client.close();
		return null;
	}
	
	
	
	public String getTickets(String id,HashMap<String,String> parameters) throws URISyntaxException, ClientProtocolException, IOException {
		String uri = this.api_url+"tickets/"+id;
		url = new URIBuilder(uri);
		url.setParameter("apiKey", "8954ab8892fd484b2b3cb463e8a565a2");
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

		parameters.entrySet().forEach((set)->{
			params.add(new BasicNameValuePair(set.getKey(), set.getValue()));

		});
		if(params.size()>0)
			url.addParameters(params);
		
		CloseableHttpClient client = HttpClientBuilder.create().build();
		request = new HttpGet(url.build());
		request.setHeader("Authorization", "Basic anVzdGdvbm93LWVuOkZnVVBMXmo3RTY1SDVPZiY=");
		CloseableHttpResponse response = client.execute(request);
		HttpEntity entity = response.getEntity();
		if(entity!=null) {
			 return EntityUtils.toString(entity);
		}
		client.close();
		return null;
		
	}
	
	public static void main(String[] args) throws URISyntaxException, IOException {
		SearchEvent api = new SearchEvent("https://api-v2.sportsevents365.com/","justgonow-en","FgUPL^j7E65H5Of&","8954ab8892fd484b2b3cb463e8a565a2");
		HashMap<String,String> parameters = new HashMap<String,String>();
		//parameters.put("{cityId}", "2603");
		parameters.put("{id}", "3432");
		parameters.put("page", "1");
		
		Events events = api.getEvents(EndPoints.byParticipant,parameters);
		
		int how_many_pages = events.meta.last_page;
		
		
		Iterator<Datum> data =events.data.iterator();
		while(data.hasNext()) {
			Datum event = data.next();
			System.out.println(event.toString());
		}
		
	}

	
	
}

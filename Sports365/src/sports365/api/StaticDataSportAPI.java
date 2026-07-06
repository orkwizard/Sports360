package sports365.api;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;



public class StaticDataSportAPI {
	URIBuilder url;
	HttpGet request;
	String perPage = "200";
	String authorization;
	String apikey;
	
	public enum EndPoints{
		sports,
		performers,
		competitors,
		venues,
		tournements,
		country,
		city
	};
	
	public StaticDataSportAPI() {
		this.authorization = "Basic anVzdGdvbm93LWVuOkZnVVBMXmo3RTY1SDVPZiY=";
		this.apikey = "8954ab8892fd484b2b3cb463e8a565a2";
	}
	public StaticDataSportAPI(String language) {
		// TODO Auto-generated constructor stub
		if(language.equals("en")) {
			this.authorization = "Basic anVzdGdvbm93LWVuOkZnVVBMXmo3RTY1SDVPZiY=";
			this.apikey = "8954ab8892fd484b2b3cb463e8a565a2";
		}
		if(language.equals("es")) {
			this.authorization = "Basic anVzdGdvbm93LXNwOnhsYnVHODFiNyFRajZsank=";
			this.apikey="ca0255a896c4ca5ffc542353193e7111";
			
		}
	}
	
	private String getEndpoint(EndPoints endpoint) {
		String result="";
		switch(endpoint) {
		case sports:
			result ="https://api-v2.sportsevents365.com/event-types";
			break;
		case performers:
			result = "https://api-v2.sportsevents365.com/performers";
			break;
		case competitors:
			result = "https://api-v2.sportsevents365.com/competitors";
			break;
		case venues:
			result = "https://api-v2.sportsevents365.com/venues";
			break;
		case tournements:
			result = "https://api-v2.sportsevents365.com/tournaments";
			break;
		case country:
			result = "https://api-v2.sportsevents365.com/countries";
			break;
		case city:
			result = "https://api-v2.sportsevents365.com/countries/{id}/city";
			break;
		
		}
		return result;
	}
	
	
	public String getEntity(EndPoints endpoint,String page,int... parameter) throws URISyntaxException, IOException {
		String uri="";
		if(parameter.length>0) {
			String original = getEndpoint(endpoint);
			uri = original.replace("{id}", parameter[0]+"");
		}else {
			uri = getEndpoint(endpoint);
		};
		
		url = new URIBuilder(uri);
		url.setParameter("apiKey",this.apikey);
		url.addParameter("perPage", perPage);
		url.addParameter("page", page);
		CloseableHttpClient client = HttpClientBuilder.create().build();
		request = new HttpGet(url.build());
		request.setHeader("Authorization",this.authorization);
		CloseableHttpResponse response = client.execute(request);
		HttpEntity entity = response.getEntity();
		if(entity!=null) {
			 return EntityUtils.toString(entity);
		}
		client.close();
		return null;
	}
	
	public static void main(String[] args) throws URISyntaxException, IOException {
		StaticDataSportAPI api = new StaticDataSportAPI();
		String page = api.getEntity(EndPoints.country,"1");
		System.out.println(page);
	}

}

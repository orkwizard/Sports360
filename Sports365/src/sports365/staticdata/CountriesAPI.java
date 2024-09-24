package sports365.staticdata;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import elastic.ElasticConnector;
import models.cities.Cities;
import models.countries.Countries;
import models.searcher.Searcher;
import sports365.api.StaticDataSportAPI;
import sports365.api.StaticDataSportAPI.EndPoints;

public class CountriesAPI {
	StaticDataSportAPI sports = new StaticDataSportAPI();
	ElasticConnector connector = new ElasticConnector("http://157.245.218.120:9200","ZWxhc3RpYzo0MXJkdzVwWGNOU2V6RjR1Mm0wWA==");
	ElasticsearchClient client;
	ObjectMapper om = new ObjectMapper();
	
	public CountriesAPI() throws ElasticsearchException, IOException {
		// TODO Auto-generated constructor stub
		super();
		client = connector.getElasticClient();

	}
	private Countries getData(String page) throws URISyntaxException, IOException {
		String entity = sports.getEntity(StaticDataSportAPI.EndPoints.country, page);
		if(entity!=null) {
			Countries v =  om.readValue(entity, Countries.class);
			int size= v.data.size();
			for(int i=0;i<size;i++) {
				v.data.get(i).type = "country";
			}
			return v;
			}
		return null;
	}
	
	public Cities getDataCities(String page, int country) throws URISyntaxException, IOException {
		String entity = sports.getEntity(EndPoints.city, page, country);
		
		if(entity!=null) {
			Cities c =  om.readValue(entity, Cities.class);
			int size= c.data.size();
			for(int i=0;i<size;i++) {
				c.data.get(i).type = "city";
			}
			return c;
			}
		return null;
		
	}
	
	public ArrayList<models.cities.Datum> getCities(int country) throws URISyntaxException, IOException{
		Cities first = getDataCities("1", country);
		return first.data;
	}
	
	public ArrayList<models.countries.Datum> getCountries() throws URISyntaxException, IOException{
		Countries first = getData("1");
		int lastPage = first.meta.last_page;
        ArrayList<models.countries.Datum> countries = new ArrayList<models.countries.Datum>();
        countries.addAll(first.data);
        for(int nextPage=2;nextPage<=lastPage;nextPage++) {
        	System.out.println("Adding page :" + nextPage);
        	Countries page = getData(nextPage+"");
        	countries.addAll(page.data);
        }
		return countries;
	}
	
	public boolean addCitiesToElastic(ArrayList<models.cities.Datum> c)  throws ElasticsearchException, IOException {
		Iterator<models.cities.Datum> cities = c.iterator();
		while(cities.hasNext()) {
			models.cities.Datum city = cities.next();
			Searcher s = new Searcher();
			s.id = city.id;
			s.sid = city.id;
			s.name = city.name;
			s.type = city.type;
			s.ancestor = city.country.name;
			IndexResponse response = client.index(i -> i
					.index("sports365")
					.document(s)
			);
			System.out.println("Added City -: " + city.name + " Country ->" + city.country.name);
		}
		return true;	
	}
	
	public boolean addCountriesToElastic(ArrayList<models.countries.Datum> c) throws ElasticsearchException, IOException {
		Iterator<models.countries.Datum> countries = c.iterator();
		while(countries.hasNext()) {
			models.countries.Datum country = countries.next();
			Searcher s = new Searcher();
			s.id=country.id;
			s.sid = country.id;
			s.name = country.name;
			s.type = country.type;
			IndexResponse response = client.index(i -> i
					.index("sports365")
					.document(s)
			);
			System.out.println("Added Country " + country.name);
		}
		return true;	
	}
	
	public static void main(String[] args) throws IOException, URISyntaxException {
		CountriesAPI api = new CountriesAPI();
		ArrayList<models.countries.Datum> countries = api.getCountries();
		api.addCountriesToElastic(countries);
		Iterator<models.countries.Datum> iterator = countries.iterator();	
		while(iterator.hasNext()) {
			ArrayList<models.cities.Datum> cities = api.getCities(iterator.next().id);
			api.addCitiesToElastic(cities);
		}	
		api.connector.close();
	}

}

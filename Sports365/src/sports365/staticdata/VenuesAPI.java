package sports365.staticdata;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchPhrasePrefixQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import elastic.ElasticConnector;
import models.searcher.Searcher;
import models.venues.Venues;
import sports365.api.StaticDataSportAPI;

public class VenuesAPI {
	StaticDataSportAPI sports = new StaticDataSportAPI();
	ElasticConnector connector = new ElasticConnector("http://157.245.218.120:9200","ZWxhc3RpYzo0MXJkdzVwWGNOU2V6RjR1Mm0wWA==");
	ElasticsearchClient client;
	ObjectMapper om = new ObjectMapper();
	
	public VenuesAPI() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	private Venues getData(String page) throws URISyntaxException, IOException {
		String entity = sports.getEntity(StaticDataSportAPI.EndPoints.venues, page);
		if(entity!=null) {
			Venues v =  om.readValue(entity, Venues.class);
			int size= v.data.size();
			for(int i=0;i<size;i++) {
				v.data.get(i).type = "venue";
			}
			return v;
			}
		return null;
	}
	
	public ArrayList<models.venues.Datum> getVenues() throws URISyntaxException, IOException{
		Venues first = getData("1");
		int lastPage = first.meta.last_page;
        ArrayList<models.venues.Datum> venues = new ArrayList<models.venues.Datum>();
        
        venues.addAll(first.data);
        for(int nextPage=2;nextPage<=lastPage;nextPage++) {
        	System.out.println("Adding page :" + nextPage);
        	Venues page = getData(nextPage+"");
        	venues.addAll(page.data);
        }
		return venues;
	}
	
	public boolean addVenuestoElastic(ArrayList<models.venues.Datum> c) throws ElasticsearchException, IOException {
		client = connector.getElasticClient();
		Iterator<models.venues.Datum> venues = c.iterator();
		while(venues.hasNext()) {
			models.venues.Datum venue = venues.next();
			Searcher s = new Searcher();
			s.sid = venue.id;
			s.name = venue.name;
			s.type = venue.type;
			IndexResponse response = client.index(i -> i
					.index("sports365")
					.document(s)
			);
			System.out.println("Added " + venue.name);
		}
		return true;	
	}
	
	public List<Hit<models.venues.Datum>> autocomplete(String token) throws ElasticsearchException, IOException{
		client = connector.getElasticClient();
		MatchPhrasePrefixQuery query = QueryBuilders.matchPhrasePrefix()
				.field("name")
				.query(token)
				.build();
		SearchResponse<models.venues.Datum> response = client.search(s -> s
				.index("sports365")
				.query(q -> q
						.matchPhrasePrefix(query)
				)
				,models.venues.Datum.class);
		
		List<Hit<models.venues.Datum>> hits = response.hits().hits();
		
		for(Hit<models.venues.Datum> venues:hits) {
			models.venues.Datum v = venues.source();
			System.out.println("Venue : "+v.id + "---> " +v.name + " type: " + v.type);
		}
		
		return hits;
	}
	
	public static void main(String[] args) throws IOException, URISyntaxException {
		VenuesAPI api = new VenuesAPI();
		ArrayList<models.venues.Datum> venues = api.getVenues();
		api.addVenuestoElastic(venues);
		//api.autocomplete("Madison");
		api.connector.close();
	}
	
	
}

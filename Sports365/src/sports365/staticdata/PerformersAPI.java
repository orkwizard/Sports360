package sports365.staticdata;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchPhrasePrefixQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import elastic.ElasticConnector;
import models.performers.Converter;
import models.performers.Datum;
import models.performers.Performers;
import models.searcher.Searcher;
import sports365.api.StaticDataSportAPI;

public class PerformersAPI {
	StaticDataSportAPI sports = new StaticDataSportAPI();
	ElasticConnector connector = new ElasticConnector("http://157.245.218.120:9200","ZWxhc3RpYzo0MXJkdzVwWGNOU2V6RjR1Mm0wWA==");
	ElasticsearchClient client;
	
	
	public PerformersAPI() throws URISyntaxException, ElasticsearchException, IOException {
		super();
	}

	
	private Performers getData(String page) throws ClientProtocolException, IOException, URISyntaxException {
		String entity = sports.getEntity(StaticDataSportAPI.EndPoints.performers,page);
		if(entity!=null) {
            return Converter.fromJsonString(entity);   
		}
		return null;
		
	}
	
	
	public ArrayList<Datum> getPerformers() throws ParseException, IOException, URISyntaxException {
        Performers first = getData("1");
        int lastPage = first.getMeta().getLastPage().intValue();
        ArrayList<Datum> performers = new ArrayList<Datum>();
        performers.addAll(Arrays.asList(first.getData()));
        for(int nextPage=2;nextPage<=lastPage;nextPage++) {
        	Performers page = getData(nextPage+"");
        	performers.addAll(Arrays.asList(page.getData()));
        }
		return performers;
	}
	
	public boolean addPerformerstoElastic(ArrayList<Datum> p) throws ElasticsearchException, IOException {
		client = connector.getElasticClient();
		Iterator<Datum> performers = p.iterator();
		while(performers.hasNext()) {
			Datum performer = performers.next();
			Searcher s = new Searcher();
			s.sid = performer.getID().intValue();
			s.name = performer.getName();
			s.type = "performer";
			IndexResponse response = client.index(i -> i
					.index("sports365")
					.document(s)
			);
			System.out.println("Added " + performer.getName());
		}
		return true;
		
	}
	
	public List<Hit<Datum>> autocomplete(String token) throws ElasticsearchException, IOException{
		client = connector.getElasticClient();
		MatchPhrasePrefixQuery query = QueryBuilders.matchPhrasePrefix()
				.field("name")
				.query(token)
				.build();
		SearchResponse<Datum> response = client.search(s -> s
				.index("sports365")
				.query(q -> q
						.matchPhrasePrefix(query)
				)
				,Datum.class);
		
		List<Hit<Datum>> hits = response.hits().hits();
		
		for(Hit<Datum> performer:hits) {
			Datum p = performer.source();
			System.out.println("Candidate : " + p.getName());
		}
		
		return hits;
	}
	
	public static void main(String[] args) throws IOException, URISyntaxException {
		PerformersAPI api = new PerformersAPI();
		ArrayList<Datum>  performers = api.getPerformers();	
		System.out.println("Performers :" + performers.size());
		api.addPerformerstoElastic(performers);  
		//api.autocomplete("Ad");
		api.connector.close();
		

	}
	
}

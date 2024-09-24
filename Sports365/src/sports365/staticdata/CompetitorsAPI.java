package sports365.staticdata;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.http.ParseException;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchPhrasePrefixQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import elastic.ElasticConnector;
import models.competitors.Competitors;
import models.competitors.Datum;
import models.searcher.Searcher;
import sports365.api.StaticDataSportAPI;

public class CompetitorsAPI {
	StaticDataSportAPI sports = new StaticDataSportAPI();
	ElasticConnector connector = new ElasticConnector("http://157.245.218.120:9200","ZWxhc3RpYzo0MXJkdzVwWGNOU2V6RjR1Mm0wWA==");
	ElasticsearchClient client;
	
	public CompetitorsAPI() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	private Competitors getData(String page) throws IOException, URISyntaxException {
		String entity = sports.getEntity(StaticDataSportAPI.EndPoints.competitors,page);
		if(entity!=null) {
			Competitors competitors = models.competitors.Converter.fromJsonString(entity);
			int size = competitors.getData().length;
			for(int i=0;i<size;i++) {
				competitors.getData()[i].setType("competitor");
			}
            return competitors;
		}
		return null;
	}
	
	
	
	public ArrayList<models.competitors.Datum> getCompetitors() throws ParseException, IOException, URISyntaxException {
        Competitors first = getData("1");
        int lastPage = first.getMeta().getLastPage().intValue();
        ArrayList<Datum> competitors = new ArrayList<Datum>();
        List<Datum> list = Arrays.asList(first.getData());
        competitors.addAll(list);
        for(int nextPage=2;nextPage<=lastPage;nextPage++) {
        	Competitors page = getData(nextPage+"");
        	competitors.addAll(Arrays.asList(page.getData()));
        }
		return competitors;
	}
	
	public boolean addCompetitorstoElastic(ArrayList<models.competitors.Datum> c) throws ElasticsearchException, IOException {
		client = connector.getElasticClient();
		Iterator<models.competitors.Datum> competitors = c.iterator();
		
		while(competitors.hasNext()) {
			models.competitors.Datum competitor = competitors.next();
			Searcher s = new Searcher();
			s.sid = competitor.getID().intValue();
			s.name = competitor.getName();
			s.type = competitor.getType();
			s.eventTypeId = competitor.geteventTypeId();
			s.eventType = getEventType(competitor.geteventTypeId());
			
			IndexResponse response = client.index(i -> i
					.index("sports365")
					.document(s)
			);
			System.out.println("Added " + competitor.getName());
		}
		return true;
		
	}
	
	private String getEventType(int eventTypeId) throws ElasticsearchException, IOException {
		// TODO Auto-generated method stub}
		GetResponse<models.eventsType.Datum> response = client.get(g->g.index("sports365eventstypes").id(""+eventTypeId), models.eventsType.Datum.class);		
		if(response.found()) {
			return response.source().name;
		}
		return "Performer";
	}

	public List<Hit<models.competitors.Datum>> autocomplete(String token) throws ElasticsearchException, IOException{
		client = connector.getElasticClient();
		MatchPhrasePrefixQuery query = QueryBuilders.matchPhrasePrefix()
				.field("name")
				.query(token)
				.build();
		SearchResponse<models.competitors.Datum> response = client.search(s -> s
				.index("sports365")
				.query(q -> q
						.matchPhrasePrefix(query)
				)
				,models.competitors.Datum.class);
		
		List<Hit<models.competitors.Datum>> hits = response.hits().hits();
		
		for(Hit<models.competitors.Datum> competitor:hits) {
			models.competitors.Datum c = competitor.source();
			System.out.println("Candidate : "+c.getID() + "---> " +c.getName());
		}
		
		return hits;
	}
	
	public static void main(String[] args) throws IOException, URISyntaxException {
		CompetitorsAPI api = new CompetitorsAPI();
		ArrayList<models.competitors.Datum> competitors = api.getCompetitors();
		api.addCompetitorstoElastic(competitors);
		//api.autocomplete("Bulls");
		api.connector.close();
	}
	
	
}

package sports365.staticdata;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import elastic.ElasticConnector;
import models.searcher.Searcher;
import models.tournements.Tournements;
import sports365.api.StaticDataSportAPI;

public class TournementsAPI {
	StaticDataSportAPI sports = new StaticDataSportAPI();
	ElasticConnector connector = new ElasticConnector("http://157.245.218.120:9200","ZWxhc3RpYzo0MXJkdzVwWGNOU2V6RjR1Mm0wWA==");
	ElasticsearchClient client;
	ObjectMapper om = new ObjectMapper();
	
	public TournementsAPI() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	private Tournements getData(String page) throws URISyntaxException, IOException {
		String entity = sports.getEntity(StaticDataSportAPI.EndPoints.tournements, page);
		if(entity!=null) {
			Tournements t =  om.readValue(entity, Tournements.class);
			int size= t.data.size();
			for(int i=0;i<size;i++) {
				t.data.get(i).type = "tournament";
			}
			return t;
			}
		return null;
	}
	
	public ArrayList<models.tournements.Datum> getTournaments() throws URISyntaxException, IOException{
		Tournements first = getData("1");
		int lastPage = first.meta.last_page;
        ArrayList<models.tournements.Datum> tournaments = new ArrayList<models.tournements.Datum>();
        
        tournaments.addAll(first.data);
        for(int nextPage=2;nextPage<=lastPage;nextPage++) {
        	System.out.println("Adding page :" + nextPage);
        	Tournements page = getData(nextPage+"");
        	tournaments.addAll(page.data);
        }
		return tournaments;
	}
	
	public boolean addTournamentstoElastic(ArrayList<models.tournements.Datum> c) throws ElasticsearchException, IOException {
		client = connector.getElasticClient();
		Iterator<models.tournements.Datum> tournaments = c.iterator();
		while(tournaments.hasNext()) {
			models.tournements.Datum tournament = tournaments.next();
			Searcher s = new Searcher();
			s.sid = tournament.id;
			s.name = tournament.name;
			s.type = tournament.type;
			s.eventTypeId = tournament.eventTypeId;
			s.eventType = getEventType(tournament.eventTypeId);
			s.logo = tournament.logo;
			IndexResponse response = client.index(i -> i
					.index("sports365")
					.document(s)
			);
			System.out.println("Added " + s.name + " Event -->" + s.eventType );
		}
		return true;
	}
	
	private String getEventType(int eventTypeId) throws ElasticsearchException, IOException {
		// TODO Auto-generated method stub}
		GetResponse<models.eventsType.Datum> response = client.get(g->g.index("sports365eventstypes").id(""+eventTypeId), models.eventsType.Datum.class);		
		if(response.found()) {
			return response.source().name;
		}
		return "Tournement";
	}

	public static void main(String[] args) throws IOException, URISyntaxException {
		TournementsAPI api = new TournementsAPI();
		ArrayList<models.tournements.Datum> tournaments = api.getTournaments();
		api.addTournamentstoElastic(tournaments);
		api.connector.close();
	}
	
}

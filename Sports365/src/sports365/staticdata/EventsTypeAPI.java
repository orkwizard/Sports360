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
import models.eventsType.EventsType;
import models.searcher.Searcher;
import sports365.api.StaticDataSportAPI;

public class EventsTypeAPI {
	StaticDataSportAPI sports = new StaticDataSportAPI();
	ElasticConnector connector = new ElasticConnector("http://157.245.218.120:9200","ZWxhc3RpYzo0MXJkdzVwWGNOU2V6RjR1Mm0wWA==");
	ElasticsearchClient client;
	ObjectMapper om = new ObjectMapper();

	
	private EventsType getData(String page) throws IOException, URISyntaxException {
		String entity = sports.getEntity(StaticDataSportAPI.EndPoints.sports, page);
		if(entity!=null) {
			EventsType v =  om.readValue(entity, EventsType.class);
			int size= v.data.size();
			for(int i=0;i<size;i++) {
				v.data.get(i).type = "sport";
			}
			return v;
			}
		return null;
	}
	
	public ArrayList<models.eventsType.Datum> getEventsType() throws URISyntaxException, IOException{
		EventsType first = getData("1");
		int lastPage = first.meta.last_page;
        ArrayList<models.eventsType.Datum> events = new ArrayList<models.eventsType.Datum>();
        
        events.addAll(first.data);
        for(int nextPage=2;nextPage<=lastPage;nextPage++) {
        	System.out.println("Adding page :" + nextPage);
        	EventsType page = getData(nextPage+"");
        	events.addAll(page.data);
        }
		return events;
	}
	
	public boolean addEventsTypeToElastic(ArrayList<models.eventsType.Datum> c) throws ElasticsearchException, IOException {
		client = connector.getElasticClient();
		Iterator<models.eventsType.Datum> eventsType = c.iterator();
		while(eventsType.hasNext()) {
			models.eventsType.Datum eventType = eventsType.next();
			Searcher s = new Searcher();
			s.sid = eventType.id;
			s.name = eventType.name;
			IndexResponse response = client.index(i -> i
					.index("sports365eventstypes")
					.id(eventType.id+"")
					.document(eventType) //id(eventType.id+"")
			);
			System.out.println("Added " + eventType.name);
		}
		return true;	
	}
	
	public static void main(String[] args) throws IOException, URISyntaxException {
		EventsTypeAPI api = new EventsTypeAPI();
		ArrayList<models.eventsType.Datum> venues = api.getEventsType();
		api.addEventsTypeToElastic(venues);
		api.connector.close();
	}
	
}

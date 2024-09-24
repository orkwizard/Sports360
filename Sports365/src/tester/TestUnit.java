package tester;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import models.data.events.Datum;
import models.data.events.Events;
import models.searcher.Searcher;
import sports365.autocomplete.Sports365Searcher;
import sports365.autocomplete.Sports365Searcher.Type;
import sports365.searchAPI.SearchEvent;
import sports365.searchAPI.SearchEvent.EndPoints;

public class TestUnit {
	
	public static void printEvents(Events e) {
		System.out.println("Eventos de : --->  " + e.meta.title);
		Iterator<Datum> data =e.data.iterator();
		while(data.hasNext()) {
			Datum event = data.next();
			System.out.println(event.toString());
		}
	}
	
	public static void printOptions(ArrayList<Searcher> options) {
		Iterator<Searcher> i = options.iterator();
		while(i.hasNext()) {
			Searcher s = i.next();
			System.out.println("Candidate : " + s.toString());
		}

	}
	
	public static void main(String[] args) throws ElasticsearchException, IOException, URISyntaxException {
		
		// Buscar por torneo
		// Ejemplo NFL
		
		//El buscador puede ser configurado para buscar por Torneos, Artista(competidor), Equipo (competitor) , Estadio, Ciudad, Pais
		ArrayList<Type> filters = new ArrayList<Type>();
		filters.add(Type.all);
		
		Sports365Searcher searcher = new Sports365Searcher("http://elastic-prod.justgonow.com:9200","ZWxhc3RpYzo0MXJkdzVwWGNOU2V6RjR1Mm0wWA==");
		//El Searcher encontrara todo los eventos con la palabra NFL
		//El objeto Searcher te da especificamente que tipo de Evento se encontro
		//esto sirve para buscar especificamente las opciones
		
		ArrayList<Searcher> options =  searcher.autocomplete("Las vegas", filters);
		
		TestUnit.printOptions(options);
		
		HashMap<String,String> parameters = new LinkedHashMap<String,String>();
		
		
		parameters.put("{id}", "37");
		parameters.put("page","1");
		parameters.put("perPage", "30");
		SearchEvent ev = new SearchEvent("https://api-v2.sportsevents365.com/","justgonow-sp","xlbuG81b7!Qj6ljy","ca0255a896c4ca5ffc542353193e7111");
		Events events = ev.getEvents(EndPoints.byTournament, parameters);
		
		TestUnit.printEvents(events);
		
		
		System.out.println("----------------------------------------------------------------------------------------------------------------------------------");
		// Eventos de Luis Miguel
		
		parameters.clear();
		options.clear();
		
		options = searcher.autocomplete("Luis Miguel", filters);
		parameters.put("page","1");
		parameters.put("perPage", "30");
		TestUnit.printOptions(options);

		parameters.put("{id}", "3803");
		
		
		events = ev.getEvents(EndPoints.byParticipant, parameters);
		
		TestUnit.printEvents(events);

		
		System.out.println("----------------------------------------------------------------------------------------------------------------------------------");
		// Eventos en Miami
		parameters.clear();
		options.clear();
		
		options = searcher.autocomplete("Nueva york", filters);
		TestUnit.printOptions(options);

		parameters.put("{id}", "1307");
		parameters.put("dateFrom", "2024-06-06");
		parameters.put("dateTo", "2024-07-01");
		parameters.put("page","1");
		parameters.put("perPage", "30");
		
		events = ev.getEvents(EndPoints.byCity, parameters);
		TestUnit.printEvents(events);
		
	
		System.out.println("----------------------------------------------------------------------------------------------------------------------------------");
		// Eventos de Adele en Las vegas
		filters.clear();
		filters.add(Type.all);
		options = searcher.autocomplete("Adele", filters);
		TestUnit.printOptions(options);

		parameters.put("{id}", "3945");
		filters.clear();
		events = ev.getEvents(EndPoints.byParticipant, parameters);
		TestUnit.printEvents(events);

		
		System.out.println("----------------------------------------------------------------------------------------------------------------------------------");
		filters.clear();
		parameters.clear();
		options.clear();
		filters.add(Type.all);
		options = searcher.autocomplete("Las Vegas", filters);
		TestUnit.printOptions(options);
		
		parameters.put("{id}", "3087");
		parameters.put("{cityId}", "1556");
		parameters.put("page","1");
		parameters.put("perPage", "30");
		events = ev.getEvents(EndPoints.byCityAndParticipant, parameters);
		TestUnit.printEvents(events);
		
		
		System.out.println("----------------------------------------------------------------------------------------------------------------------------------");
		// Eventos de Formula 1
		parameters.clear();
		options.clear();
		
		options = searcher.autocomplete("Formula 1", filters);
		TestUnit.printOptions(options);
		parameters.put("{id}", "11");
		parameters.put("page","1");
		parameters.put("perPage", "30");
		events = ev.getEvents(EndPoints.byTournament, parameters);
		TestUnit.printEvents(events);
		
		System.out.println("----------------------------------------------------------------------------------------------------------------------------------");

		parameters.clear();
		options.clear();
		
		options = searcher.autocomplete("Taylor", filters);
		TestUnit.printOptions(options);
		parameters.put("{id}", "3432");
		parameters.put("perPage","50");
		events = ev.getEvents(EndPoints.byParticipant, parameters);
		TestUnit.printEvents(events);

		parameters.clear();
		parameters.put("perPage","50");
		String tickets = ev.getTickets("341050", parameters);
		System.out.println("Tickets " + tickets);
		
		searcher.close();
		

		
		

	}
	
	
	
}

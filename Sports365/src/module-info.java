module Sports365 {
	exports elastic;
	exports models.performers;
	exports models.competitors;
	exports models.venues;
	exports models.tournements;
	exports models.searcher;
	exports models.eventsType;
	exports models.countries;
	exports models.cities;
	exports models.data.events;
	requires elasticsearch.java;
	requires elasticsearch.rest.client;
	requires org.apache.httpcomponents.httpasyncclient;
	requires org.apache.httpcomponents.httpclient;
	requires org.apache.httpcomponents.httpcore;
	requires com.fasterxml.jackson.databind;
	requires com.fasterxml.jackson.core;
}
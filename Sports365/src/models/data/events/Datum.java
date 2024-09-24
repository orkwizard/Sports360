package models.data.events; 
import java.util.ArrayList;

public class Datum{
    public int id;
    public String name;
    public HomeTeam homeTeam;
    public AwayTeam awayTeam;
    public ArrayList<Participant> participants;
    public EventType eventType;
    public Tournament tournament;
    public String dateOfEvent;
    public String timeOfEvent;
    public boolean finalDate;
    public boolean finalTime;
    public String dateTimeBoundaries;
    public Country country;
    public City city;
    public Venue venue;
    public String eventUrl;
    public int availableCategoriesQuantity;
    public MinTicketPrice minTicketPrice;
	@Override
	public String toString() {
		return "Events : [id=" + id + ", name=" + name + ", homeTeam=" + homeTeam + ", awayTeam=" + awayTeam
				+ ", participants=" + participants + ", eventType=" + eventType + ", tournament=" + tournament
				+ ", dateOfEvent=" + dateOfEvent + ", timeOfEvent=" + timeOfEvent + ", finalDate=" + finalDate
				+ ", finalTime=" + finalTime + ", dateTimeBoundaries=" + dateTimeBoundaries + ", country=" + country
				+ ", city=" + city + ", venue=" + venue + ", eventUrl=" + eventUrl + ", availableCategoriesQuantity="
				+ availableCategoriesQuantity + ", minTicketPrice=" + minTicketPrice + "]";
	}
    
    
}

package models.data.events; 
public class EventType{
    public int id;
    public String name;
    public int isTeamSport;
    public int concert;
	@Override
	public String toString() {
		return "EventType [id=" + id + ", name=" + name + ", isTeamSport=" + isTeamSport + ", concert=" + concert + "]";
	}
    
    
}

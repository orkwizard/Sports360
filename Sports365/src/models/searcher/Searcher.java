package models.searcher;

public class Searcher {
	public int id;
	public int sid;
	public String name;
	public String type;
	public String ancestor;
	public int eventTypeId;
	public String eventType;
	public String logo;

	@Override
	public String toString() {
		if(ancestor!=null)
			return "Searcher [id=" + sid + ", name=" + name + ", type=" + type + ", ancestor=" + ancestor + " eventTypeId=]" + eventTypeId + " event Type: " + eventType;
		
		return "Searcher [id=" + sid + ", name=" + name + ", type=" + type + "]" + " eventTypeId=" + eventTypeId + " eventType= "+eventType+  " logo = " + logo + "]";
	}
	
	
	
	

}

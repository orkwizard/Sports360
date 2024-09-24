package models.competitors;

import com.fasterxml.jackson.annotation.*;

public class Datum {
    private Long id;
    private String name;
    private String type;
    private int eventTypeId;

    @JsonProperty("id")
    public Long getID() { return id; }
    @JsonProperty("id")
    public void setID(Long value) { this.id = value; }

    @JsonProperty("name")
    public String getName() { return name; }
    @JsonProperty("name")
    public void setName(String value) { this.name = value; }
    @JsonProperty("type")
    public String getType() {return type;}
	public void setType(String type) {this.type = type;}
	 @JsonProperty("eventTypeId")
	public int geteventTypeId() {return eventTypeId;}
	public void seteventTypeId(int eventTypeId) {this.eventTypeId = eventTypeId;}
    
    
    
}

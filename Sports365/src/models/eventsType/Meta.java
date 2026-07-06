package models.eventsType; 
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Meta{
    public int current_page;
    public int from;
    public int last_page;
    public ArrayList<Link> links;
    public String path;
    public int per_page;
    @JsonProperty("to") 
    public int myto;
    public int total;
    public String title;
    public int apiId;
}

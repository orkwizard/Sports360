package models.countries; 
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List; 
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

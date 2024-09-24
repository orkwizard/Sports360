package models.data.events; 
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
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
    public String dateFrom;
    public String dateTo;
    public String title;
    public int apiId;
	@Override
	public String toString() {
		return "Meta [current_page=" + current_page + ", from=" + from + ", last_page=" + last_page + ", links=" + links
				+ ", path=" + path + ", per_page=" + per_page + ", myto=" + myto + ", total=" + total + ", dateFrom="
				+ dateFrom + ", dateTo=" + dateTo + ", title=" + title + ", apiId=" + apiId + "]";
	}
    
    
}

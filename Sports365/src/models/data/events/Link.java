package models.data.events; 
public class Link{
    public String url;
    public String label;
    public boolean active;
    public String first;
    public String last;
    public Object prev;
    public String next;
	@Override
	public String toString() {
		return "Link [url=" + url + ", label=" + label + ", active=" + active + ", first=" + first + ", last=" + last
				+ ", prev=" + prev + ", next=" + next + "]";
	}
    
    
}

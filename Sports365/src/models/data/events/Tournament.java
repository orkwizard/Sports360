package models.data.events; 
public class Tournament{
    public int id;
    public String name;
    public boolean isGroupEvent;
	@Override
	public String toString() {
		return "Tournament [id=" + id + ", name=" + name + ", isGroupEvent=" + isGroupEvent + "]";
	}
    
}

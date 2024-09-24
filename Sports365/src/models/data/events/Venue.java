package models.data.events; 
public class Venue{
    public int id;
    public String name;
    public String mapUrl;
    public String address;
    public double latitude;
    public double longitude;
	@Override
	public String toString() {
		return "Venue [id=" + id + ", name=" + name + ", mapUrl=" + mapUrl + ", address=" + address + ", latitude="
				+ latitude + ", longitude=" + longitude + "]";
	}
    
}

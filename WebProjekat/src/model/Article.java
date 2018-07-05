package model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Article {

	public int id;
	@JsonProperty("name")
	public String name;
	@JsonProperty("price")
	public double price;
	@JsonProperty("des")
	public String des;
	@JsonProperty("kol")
	public int kol;
	@JsonProperty("type")
	public String type;
	public int status;
	@JsonProperty("idParent")
	public int parentID;
	@JsonProperty("nameRest")
	public String nameRest;
	
	
	@Override
	public String toString() {
		String output = id + "/" + name + "/" + price + "/" + des + "/" + kol + "/" + type + "/" +parentID + "/" + nameRest + ";" + status;
		
		return output;
	}



	public void setFields(String id, String name, String price, String des, String kol, String type, String idParent, String nameRest,
			int st) {
		
		this.id = Integer.parseInt(id);
		this.des = des;
		this.kol = Integer.parseInt(kol);
		this.name = name;
		this.price = Double.parseDouble(price);
		this.type = type;
		this.parentID = Integer.parseInt(idParent);
		this.nameRest = nameRest;
		this.status = st;
	}
	
}

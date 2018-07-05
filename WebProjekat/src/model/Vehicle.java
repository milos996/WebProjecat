package model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Vehicle {

	public int id;
	@JsonProperty("mark")
	public String mark;
	@JsonProperty("model")
	public String model;
	@JsonProperty("categories")
	public String tip;
	@JsonProperty("regN")
	public String regM;
	@JsonProperty("yearP")
	public String yearP;
	@JsonProperty("inUse")
	public String inUse;
	@JsonProperty("des")
	public String des;
	public int status;
	
	public void setFields(String id, String mark, String model, String tip, String regM, String yearP,
			String inUse, String des, int status) {
		
		this.id = Integer.parseInt(id);
		this.mark = mark;
		this.des = des;
		this.inUse = inUse;
		this.model = model;
		this.regM = regM;
		this.status = status;
		this.tip = tip;
		this.yearP = yearP;
		
	}
	
	@Override
	public String toString() {
		String output = id + " " + mark + " " + model + " " + tip + " " + regM + " " + yearP + " " + inUse + " " + des + "," + status;
		
		return output;
	}

}

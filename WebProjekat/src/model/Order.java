package model;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {

	public int id;
	@JsonProperty("articles")
	public ArrayList<Issu> stavke;
	@JsonProperty("date")
	public String date;
	@JsonProperty("idBuyer")
	public String buyerID;
	@JsonProperty("idDeliver")
	public String delivUserID = "0";
	@JsonProperty("state")
	public String currState ;
	@JsonProperty("notice")
	public String napomena = " ";
	@JsonProperty("vehID")
	public int idVeh;
	
	public int status;
	
	
	 public Order() {
		 stavke = new ArrayList<Issu>();
	}
	@Override
	public String toString() {
		String output = id + ";" + date + ";" + buyerID + ";" + delivUserID + ";"
						+ currState + ";" + napomena + "-" + status + "-" ;
		
		for(Issu i : stavke){
			output += i.toString() + "%";
		}
		
		return output;
	}

	public boolean setOrder(String line) {
		String[] parts = line.split("-");
		if(Integer.parseInt(parts[1]) == 0){
			return false;
		}
		
		String[] firstPart = parts[0].split(";");
		this.id = Integer.parseInt(firstPart[0]);
		this.date = firstPart[1];
		this.buyerID = firstPart[2];
		this.delivUserID = firstPart[3];
		this.currState = firstPart[4];
		
		if(firstPart.length > 5)
			this.napomena = firstPart[5];
		
		this.status = Integer.parseInt(parts[1]);
		
		if(parts.length > 2){
			String[] stavkeAll = parts[2].split("%");
			for(int i = 0; i < stavkeAll.length; i++){
				if(!stavkeAll[i].trim().equals("")){
					Issu num1 = new Issu();
					num1.setFields(stavkeAll[i]);	
					this.stavke.add(num1);	
				}
			}
		}
		
		
		
		return true;
	}
	
}

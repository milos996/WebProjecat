package model;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BuyerUser {

	@JsonProperty("username")
	 public String username;
	 @JsonProperty("password")
	 public String password;
	 @JsonProperty("name")
	 public String name;
	 @JsonProperty("surname")
	 public String surname;
	 @JsonProperty("num")
	 public String num;
	 @JsonProperty("email")
	 public String email;
	 @JsonProperty("date")
	 public String date;
	 @JsonProperty("type")
	 public String type;
	 
	
	public ArrayList<Order> orders;
	public ArrayList<Restaurant> resta;
	@JsonProperty("vehicle")
	public Vehicle vehicle = null;
	
	public BuyerUser(){}
	
	public void setFields(String username,String pass, String name, String sur, String num, String email, String date, String type){
		this.date = date;
		this.email = email;
		this.username = username;
		this.password = pass;
		this.surname = sur;
		this.name = name;
		this.num = num;
		this.type = type;
		
		orders = new ArrayList<Order>();
		resta = new ArrayList<Restaurant>();
		
	}
	
	public void setType(){
		
			orders = new ArrayList<Order>();
			resta = new ArrayList<Restaurant>();
		
	}
	
	
	@Override
	public String toString() {
		String output = username + " " + password + " " + name + " " + surname + " " + num + " " + email + " " + date + " " + type;
		output += ",";
		

			for(Order o : orders){
				output += o.toString() + "$";
			}
			output += ",";
			
			for(Restaurant r : resta){
				output += r.name + " " + r.address + " " + r.cat + "$";
			}
			
			output+=",";
			
			if(vehicle != null)
				output+= vehicle.toString();
		
		
		return output;
	}
	
	

	public void addRestaurants(String restaurants) {
		String[] parts = restaurants.split("\\$");
		for(int i = 0 ; i < parts.length ; i++){
			if(!parts[i].trim().equals("")){
			String[] pieces = parts[i].split(" ");
			Restaurant r = new Restaurant();
			r.name = pieces[0];
			r.address = pieces[1];
			r.cat = pieces[2];
			r.status = 1;
			this.resta.add(r);
			}
		}
	}
	
	public void addVehicle(String vehicle){
		String[] parts = vehicle.split(" ");
		Vehicle veh = new Vehicle();
		veh.id = Integer.parseInt(parts[0]);
		veh.mark = parts[1];
		veh.model = parts[2];
		veh.tip = parts[3];
		veh.regM = parts[4];
		veh.yearP = parts[5];
		veh.inUse = parts[6];
		veh.des = parts[7];
		veh.status = 1;
		
		this.vehicle = veh;
	}

	public void setOrders(String orders2) {
		
		String[] parts = orders2.split("\\$");
		for(int i = 0 ; i < parts.length ; i++){
			if(!parts[i].trim().equals("")){
			Order newC = new Order();
			newC.setOrder(parts[i]);
			this.orders.add(newC);
			}
		}
	}

	public void removeOrder(int id){
		for(Order o : orders){
			if(o.id == id){
				orders.remove(o);
				break;
			}
		}
	}
	public Order getOrder(int id) {
		
		for(Order or : orders){
			if(or.id == id)
				return or;
		}
		
		return null;
	}
	
	public void addOrder(Order o){
		orders.add(o);
	}
	
}

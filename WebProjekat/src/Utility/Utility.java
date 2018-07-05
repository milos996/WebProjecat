package Utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import model.BuyerUser;
import model.Order;
import model.Restaurant;
import model.Vehicle;

public class Utility {

	
	public static void saveAllRestaurants(ArrayList<Restaurant> restList , String file) throws IOException {
		BufferedWriter writer = null;
		
		try {
			writer = new BufferedWriter(new FileWriter(new File(file), false));
			
			for(Restaurant r : restList){
				writer.write(r.toString());
				writer.newLine();
			}

		} catch (Exception e) {
				e.printStackTrace();
		}finally {
			writer.close();
		}
		
	}
	
	public static ArrayList<Vehicle> getVehicles(String file) {
		
		ArrayList<Vehicle> vehList = new ArrayList<>();
		BufferedReader reader = null;
		try {
			 reader = new BufferedReader(new FileReader(new File(file)));
			
			String line;
			while((line = reader.readLine()) != null){
				String[] parts = line.split(",");
				
				if(Integer.parseInt(parts[1]) == 0)
					break;
				
				Vehicle veh = new Vehicle();
				String[] data = parts[0].split(" ");
				veh.setFields(data[0], data[1], data[2], data[3],data[4],data[5],data[6],data[7], 1);
				
				vehList.add(veh);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		return vehList;
	}
	
	public static void saveAllVehicles(ArrayList<Vehicle> vehList, String file)  {
		BufferedWriter writer = null;
		
		try {
			writer = new BufferedWriter(new FileWriter(new File(file), false));
			
			for(Vehicle v : vehList){
				writer.write(v.toString());
				writer.newLine();
			}

		} catch (Exception e) {
				e.printStackTrace();
		}finally {
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	public static void saveAllUsers(ArrayList<BuyerUser> userList, String file)  {
		BufferedWriter writer = null;
		
		try {
			writer = new BufferedWriter(new FileWriter(new File(file), false));
			
			for(BuyerUser u : userList){
				writer.write(u.toString());
				writer.newLine();
			}

		} catch (Exception e) {
				e.printStackTrace();
		}finally {
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public static void saveUser(BuyerUser us, String file) {
		
		BufferedReader reader = null;
		
		try {
			 reader = new BufferedReader(new FileReader(new File(file)));
			
			String line;
			String toWrite = "";
			while((line = reader.readLine()) != null){
				String[] userPart = line.split(" ");
				
				if(userPart[0].equals(us.username)){
					toWrite += us.toString()+System.lineSeparator();
					continue;
					
				}
					toWrite += line+System.lineSeparator();
			}
			
			saveAllUsers(toWrite, file);
			
		} catch (Exception e) {
			e.printStackTrace();
		
		}finally {
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}
	public static void saveAllUsers(String allWrite, String file) {
		
		BufferedWriter writer = null;
		 
		try {
			writer = new BufferedWriter(new FileWriter(new File(file)));
			writer.write(allWrite);
			
		} catch (Exception e) {
				e.printStackTrace();
		}finally {
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static BuyerUser getUserObject(String line) {
		String orders = null;
		String restaurants = null;
		String vehicle = null;
		String[] groups = line.split(",");
		
		String[] first = groups[0].split(" ");
		if(groups.length > 1){
			 orders = groups[1];	
		}
		
		if(groups.length > 2){
			restaurants = groups[2];
		}
		
		if(groups.length > 3)
			vehicle = groups[3];
		
		BuyerUser newUser = new BuyerUser();
		newUser.setFields(first[0], first[1], first[2], first[3], first[4], first[5], first[6], first[7]);
		
		if(restaurants != null)
			newUser.addRestaurants(restaurants);
		
		if(vehicle != null)
			newUser.addVehicle(vehicle);
		
		if(orders != null)
			newUser.setOrders(orders);
		
		
		return newUser;
	}
	
	public static BuyerUser login(String username,String pass, String file)  {
		
		BufferedReader reader = null;
		try {
			 reader = new BufferedReader(new FileReader(new File(file)));
			
			String line;
			while((line = reader.readLine()) != null){
				String[] parts = line.split(" ");
				if(username.equals(parts[0]) ){
					if( pass != null && pass.equals(parts[1])){
						BuyerUser userN = getUserObject(line);
						return userN;
					}else{
						if(pass == null){
							BuyerUser userN = getUserObject(line);
							return userN;
						}
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	
		return null;

		}
	
	public static ArrayList<BuyerUser> getUsers(String file){
		
		ArrayList<BuyerUser> korisnici = new ArrayList<>();
			
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(file)));
				
			String line;
			
			while((line = reader.readLine()) != null){
				BuyerUser userCurr = getUserObject(line);
				korisnici.add(userCurr);
			}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		return korisnici;
		
	 }
	
	public static void addUser(BuyerUser buyer, String file)  {
		
		BufferedWriter writer = null;
		try {
			 writer = new BufferedWriter(new FileWriter(new File(file), true));
		
			writer.newLine();
			writer.write(buyer.toString());
	
			
		} catch (Exception e) {
				e.printStackTrace();
		}finally {
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void saveAllOrders(ArrayList<Order> orders, String file) {
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(file);
			writer.print("");
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}finally {
			writer.close();
		}
		
		for(Order curr : orders){
			saveOrder(curr, file);
		}
	
	}
	public static void saveOrder(Order r , String file)  {
		
		BufferedWriter writer = null;
		try {
			 writer = new BufferedWriter(new FileWriter(new File(file), true));
		
			
			writer.write(r.toString());
			writer.newLine();
			
		} catch (Exception e) {
				e.printStackTrace();
		}finally {
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public static ArrayList<Order> importOrders(String file){
		
		ArrayList<Order> orders = new ArrayList<Order>();
		BufferedReader reader = null;
		try {
			 reader = new BufferedReader(new FileReader(new File(file)));
			
			String line;
			while((line = reader.readLine()) != null){
				Order importOrder = new Order();
				importOrder.setOrder(line);
				if(importOrder.status != 0)
					orders.add(importOrder);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return orders;
	}
}

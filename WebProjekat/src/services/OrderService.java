package services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import Utility.Utility;
import model.Article;
import model.BuyerUser;
import model.Issu;
import model.Order;
import model.Restaurant;
import model.Vehicle;

@Path("/orders")
public class OrderService {

	
	@Context
	HttpServletRequest request;
	@Context
	ServletContext ctx;

	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String add(Order r) throws IOException{
		BuyerUser user = (BuyerUser) request.getSession().getAttribute("user");
		String fileOrder = ctx.getRealPath("")+"/orders.txt";
		String fileUsers = ctx.getRealPath("")+"/buyers.txt";
		int id = 0;
		
		if(ctx.getAttribute("idOrder") != null)
				id = (int) ctx.getAttribute("idOrder");
		
		ArrayList<Order> lista = (ArrayList<Order>) ctx.getAttribute("orders");
		
		if(lista == null)
			lista = new ArrayList<Order>();
		
		
		if(ctx.getAttribute("idOrder") == null)
			id = 0;
		
		r.status = 1;
		r.id = id;
		lista.add(r);
		
		user.orders.add(r);
		Utility.saveUser(user, fileUsers);
		Utility.saveOrder(r, fileOrder);
		
		id++;
		ctx.setAttribute("user", user);
		ctx.setAttribute("idOrder", id);
		ctx.setAttribute("orders", lista);
		return "true";
		
	}

	
	
	@GET
	@Path("/getTopJela")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Article> getTopJela() {
		ArrayList<Article> jela = getPop("Jelo");
		return jela;
	}
	
	@GET
	@Path("/getTopPica")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Article> getTopPica() {
		ArrayList<Article> pica = getPop("Pice");
		return pica;
	}
	
	
	private ArrayList<Article> getPop(String tip){
		importOrders();
		ArrayList<Order> orders = (ArrayList<Order>) ctx.getAttribute("orders");
		ArrayList<Article> popArt = new ArrayList<>();
		HashMap<String, Integer> pop = new HashMap<>();
		if(orders != null){
			
		for(Order order : orders){
			for(Issu iss : order.stavke){
				if(iss.article.type.equals(tip)){
					if(pop.containsKey(iss.article.name+" "+iss.article.parentID)){
						int currPojavljivanja = pop.get(iss.article.name+" "+iss.article.parentID) + 1;
						pop.put(iss.article.name+" "+iss.article.parentID, currPojavljivanja);
					}else{
						pop.put(iss.article.name+" "+iss.article.parentID, 1);
					}
				}
			}
		}
		int sizePopJela = pop.size();
		int k = 0;
		while(k < 10 && k < sizePopJela){
			int max = -1;
			String maxArt = null ;
			for(String art : pop.keySet()){
				if(max < pop.get(art)){
					max = pop.get(art);
					maxArt = art;
				}
			}
			
			String[] parts = maxArt.split(" ");
			for(Order o : orders){
				boolean flag = false;
				for(Issu i : o.stavke){
					if(i.article.name.equals(parts[0]) && i.article.parentID == Integer.parseInt(parts[1])){
						popArt.add(i.article);
						pop.remove(maxArt);
						k++;
						flag = true;
						break;
					}
				
				}
				if(flag)
					break;
			}
		}
		}
		return popArt;
	}
	@GET
	@Path("/getO")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Order> getOrders() throws IOException{
		
		importOrders();
		ArrayList<Order> orders = (ArrayList<Order>) ctx.getAttribute("orders");
		return orders;
	}
	
	private void importOrders(){
		String file = ctx.getRealPath("")+"/orders.txt";
		ArrayList<Order> orders ;
		int idOrder;
		
		orders = Utility.importOrders(file);
		
		
		
		if(orders.size() > 0){

			 idOrder = (orders.get(orders.size() - 1)).id + 1;
	
		}else{
			idOrder = 0;
		}
		
		ctx.setAttribute("idOrder", idOrder);
		ctx.setAttribute("orders", orders);
		
	}
		
	@POST
	@Path("/takeOrder")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String takeOrder(Order currO) throws IOException{
		BuyerUser userDost = (BuyerUser) request.getSession().getAttribute("user");
		String fileOrder = ctx.getRealPath("")+"/orders.txt";
		String fileUser = ctx.getRealPath("")+"/buyers.txt";
		ArrayList<Order> orders = (ArrayList<Order>) ctx.getAttribute("orders");
		
		for(Order o : orders){
			if(o.id == currO.id){
				o.delivUserID = userDost.username;
				o.currState = "Dostava u toku";
				userDost.orders.add(o);
				Utility.saveUser(userDost, fileUser);
				Utility.saveAllOrders(orders, fileOrder);
				break;
			}
		}
		ctx.setAttribute("user", userDost);
		ctx.setAttribute("orders", orders);
		return "true";
	}

	
	
	@POST
	@Path("/checkOrder")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String checkOrder(Order currO) throws IOException{
		BuyerUser userDost = (BuyerUser) request.getSession().getAttribute("user");
		String file = ctx.getRealPath("")+"/buyers.txt";
		String fileOrder = ctx.getRealPath("")+"/orders.txt";
		
		if(userDost.username.equals(currO.delivUserID) && (!currO.currState.equals("Dostavljeno"))){
			
			ArrayList<Order> orders = (ArrayList<Order>) ctx.getAttribute("orders");
			for(Order o : orders){
				if(o.id == currO.id){
					o.currState = "Dostavljeno";
					userDost.getOrder(o.id).currState = "Dostavljeno";
					BuyerUser kupac = Utility.login(o.buyerID , null, file);
					kupac.getOrder(o.id).currState = "Dostavljeno";
					Utility.saveUser(userDost, file);
					Utility.saveUser(kupac, file);
					Utility.saveAllOrders(orders, fileOrder);
					break;
				}
			}
		
			ctx.setAttribute("user", userDost);
			ctx.setAttribute("orders", orders);
			return "true";
		}
		
		return "false";
		
	}
	
	
	
	@POST
	@Path("/adminAdd")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String adminAdd(Order o){
		String fileVeh = ctx.getRealPath("")+"/vehicles.txt";
		String file = ctx.getRealPath("")+"/buyers.txt";
		String fileOrder =  ctx.getRealPath("")+"/orders.txt";
		BuyerUser buyerU = Utility.login(o.buyerID, null, file);
		
		BuyerUser deliverer = null;
		if(o.delivUserID != "0"){
			deliverer = Utility.login(o.delivUserID, null, file);
		}
		
		ArrayList<Order> lista = (ArrayList<Order>) ctx.getAttribute("orders");
		ArrayList<Vehicle> veh = Utility.getVehicles(fileVeh);
		int id = (int) ctx.getAttribute("idOrder");
		
		
		if(deliverer != null){
			for(Vehicle v : veh){
				if(v.id == o.idVeh){
					v.inUse = "da";
					
					o.currState = "Dostava u toku";
					o.status = 1;
					o.id = id;
					lista.add(o);
					
					buyerU.orders.add(o);
					deliverer.orders.add(o);
					deliverer.vehicle = v;
					break;
				}
					
			}
		}else{
			o.currState = "Poruceno";
			o.status = 1;
			o.id = id;
			lista.add(o);
			
			buyerU.orders.add(o);
		}
		
		if(deliverer != null){
			Utility.saveAllVehicles(veh, fileVeh);
			ctx.setAttribute("vehicles", veh);
			Utility.saveUser(deliverer, file);
			
		}
		Utility.saveOrder(o, fileOrder);
		ctx.setAttribute("orders", lista);
		Utility.saveUser(buyerU, file);
		ctx.setAttribute("idOrder", ++id);
		
		return "true";
	}
	
	
	@POST
	@Path("/cancelOrder")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String cancelOrder(Order currO) throws IOException{
			
			ArrayList<Order> orders = (ArrayList<Order>) ctx.getAttribute("orders");
			String file = ctx.getRealPath("")+"/buyers.txt";
			String fileOrder = ctx.getRealPath("")+"/orders.txt";
			
			
			for(Order o : orders){
				if(o.id == currO.id){
					o.currState = "Otkazano";
					BuyerUser kupac = Utility.login(o.buyerID, null, file);
					kupac.getOrder(o.id).currState = "Otkazano";
					Utility.saveUser(kupac, file);
					
					
					if(o.delivUserID != "0"){
						BuyerUser deliverer =  Utility.login(o.delivUserID, null, file);
						deliverer.getOrder(o.id).currState = "Otkazano";
						Utility.saveUser(deliverer, file);
						
					}
					
					Utility.saveAllOrders(orders, fileOrder);
					break;
				}
			}
			
			ctx.setAttribute("orders", orders);
		
		return "true";
	}
	
	@POST
	@Path("/editAdmin")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String editOrder(Order o){
		String fileVeh = ctx.getRealPath("")+"/vehicles.txt";
		String file = ctx.getRealPath("")+"/buyers.txt";
		String fileOrder =  ctx.getRealPath("")+"/orders.txt";
		BuyerUser buyerU = Utility.login(o.buyerID, null, file);
		BuyerUser deliverer = null;
		
		if(o.delivUserID != "0"){
			deliverer = Utility.login(o.delivUserID, null, file);
		}
		
		ArrayList<Order> lista = (ArrayList<Order>) ctx.getAttribute("orders");
		ArrayList<Vehicle> veh = Utility.getVehicles(fileVeh);
		int id = (int) ctx.getAttribute("idOrder");
		
		for(Order ord : lista){
			if(ord.id == o.id){
				
				
				if(!ord.buyerID.equals(o.buyerID)){
					BuyerUser oldBuyer = Utility.login(ord.buyerID, null, file);
					oldBuyer.removeOrder(ord.id);
					Utility.saveUser(oldBuyer, file);
					buyerU.addOrder(o);
					
				}
				
				if(ord.delivUserID != "0"){
					BuyerUser oldDeliver = Utility.login(ord.delivUserID, null, file);
					oldDeliver.removeOrder(ord.id);
					for(Vehicle v : veh){
						if(v.id == oldDeliver.vehicle.id){
							v.inUse = "ne";
						}
							
					}
					oldDeliver.vehicle = null;
					Utility.saveUser(oldDeliver, file);
				
				}
				
				o.status = 1;
				lista.remove(ord);
				lista.add(o);
				
			
				if(deliverer != null){
					deliverer.addOrder(o);
					deliverer.vehicle = getVehicle(o.idVeh, veh);
					deliverer.vehicle.inUse = "da";
				
				}
				
				break;
			}
		}
		
		if(deliverer != null){
			Utility.saveAllVehicles(veh, fileVeh);
			ctx.setAttribute("vehicles", veh);
			Utility.saveUser(deliverer, file);
			
		}
		
		Utility.saveAllOrders(lista, fileOrder);
		ctx.setAttribute("orders", lista);
		Utility.saveUser(buyerU, file);
		return "true";
	}
	
	
	
	@POST
	@Path("/delAdmin/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String deleteOrder(@PathParam("id") int id){
		String fileOrder =  ctx.getRealPath("")+"/orders.txt";
		ArrayList<Order> lista = (ArrayList<Order>) ctx.getAttribute("orders");
		
		for(Order ord : lista){
			if(ord.id == id){
				ord.status = 0;
				break;
			}
		}
		
		Utility.saveAllOrders(lista, fileOrder);
		ctx.setAttribute("orders", lista);
		
		return "true";
	}
	
	public Vehicle getVehicle(int id, ArrayList<Vehicle> vehLista){
		for(Vehicle v : vehLista){
			if(v.id == id){
				return v;
			}
		}
		
		return null;
	}
}

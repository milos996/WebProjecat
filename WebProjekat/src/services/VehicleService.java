package services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import Utility.Utility;
import model.BuyerUser;
import model.Restaurant;
import model.Vehicle;


@Path("/vehicles")
public class VehicleService {

	
	@Context
	HttpServletRequest request;
	@Context
	ServletContext ctx;
	
	
	@GET
	@Path("/getV")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Vehicle> getV() throws IOException{

		return getVehicles();
	}
	
	
	@POST
	@Path("/addV")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String addR(Vehicle v) throws IOException{
		
		ArrayList<Vehicle> vehList = (ArrayList<Vehicle>)ctx.getAttribute("vehicles");
		int idNum = (int) ctx.getAttribute("idVeh");
		BufferedWriter writer = null;
		String file = ctx.getRealPath("")+"/vehicles.txt";
		
		v.status = 1;
		v.id = idNum;
		
		vehList.add(v);
		idNum++;
		
		try {
			writer = new BufferedWriter(new FileWriter(new File(file), true));
			writer.write(v.toString());
			writer.newLine();


		} catch (Exception e) {
				e.printStackTrace();
		}finally {
			writer.close();
		}
		
		ctx.setAttribute("idVeh", idNum);
		ctx.setAttribute("vehicles", vehList);
		return "true";
	}
	
	@POST
	@Path("/editV")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String editR(Vehicle v) throws IOException{
		ArrayList<Vehicle> vehList = (ArrayList<Vehicle>)ctx.getAttribute("vehicles");
		
		for(Vehicle curr: vehList){
			if(curr.id == v.id){
				curr.des = v.des;
				curr.inUse = v.inUse;
				curr.mark = v.mark;
				curr.model = v.model;
				curr.regM = v.regM;
				curr.tip = v.tip;
				curr.yearP = v.yearP;
				break;
			}
		}
		
		String file = ctx.getRealPath("")+"/vehicles.txt";
		Utility.saveAllVehicles(vehList, file);
		
		ctx.setAttribute("vehicles", vehList);
		return "true";
	}


	
	
	@POST
	@Path("/deleteV")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String deleteR(Vehicle v) throws IOException{
		
		ArrayList<Vehicle> vehList = (ArrayList<Vehicle>)ctx.getAttribute("vehicles");
		
		for(Vehicle curr: vehList){
			if(curr.id == v.id){
				curr.status = 0;
				break;
			}
		}
		
		String file = ctx.getRealPath("")+"/vehicles.txt";
		Utility.saveAllVehicles(vehList, file);
		ctx.setAttribute("vehicles", vehList);
		
		return "true";
	}
	
	private ArrayList<Vehicle> getVehicles() throws IOException{
		
		ArrayList<Vehicle>  vehList ;
		if(ctx.getAttribute("idVeh") == null){
	
		String file = ctx.getRealPath("")+"/vehicles.txt";
		vehList = Utility.getVehicles(file);
		
		int idV;
		if(vehList.size() > 0){
			
		idV = (vehList.get(vehList.size() - 1)).id + 1 ;

		}else{
		idV = 0;	
			
		}
		ctx.setAttribute("idVeh", idV);
		ctx.setAttribute("vehicles",vehList);
		
		}else{
			
			vehList = (ArrayList<Vehicle>) ctx.getAttribute("vehicles");
		}

		return vehList;
	}
	
	@POST
	@Path("/takeVeh")
	@Produces(MediaType.TEXT_PLAIN)
	public String takeVeh() throws IOException{
		ArrayList<Vehicle> lista = getVehicles();
		boolean flag = false;
		BuyerUser currUser = (BuyerUser) request.getSession().getAttribute("user");
		
		if(currUser.vehicle == null){
		for(Vehicle veh : lista){
			if(veh.inUse.toLowerCase().equals("ne")){
				flag = true;
				currUser.vehicle = veh;
				veh.inUse = "da";
				break;
			}
		}
		
		if(flag){
			request.getSession().setAttribute("user", currUser);

			String fileUser = ctx.getRealPath("")+"/buyers.txt";
			Utility.saveUser(currUser, fileUser);
			
			String fileVeh = ctx.getRealPath("")+"/vehicles.txt";
			Utility.saveAllVehicles(lista, fileVeh);
			return "true";
		}
		
		return "false";
		}
		
		return "false";
	}
	
	
	
	@POST
	@Path("/backVeh")
	@Produces(MediaType.TEXT_PLAIN)
	public String backVeh() throws IOException{
		ArrayList<Vehicle> lista = getVehicles();
		boolean flag = false;
		BuyerUser currUser = (BuyerUser) request.getSession().getAttribute("user");
		
		if(currUser.vehicle != null){
			for(Vehicle veh : lista){
				if(veh.id == currUser.vehicle.id){
					flag = true;
					currUser.vehicle = null;
					veh.inUse = "ne";
					break;
				}
			}
		}
		
		if(flag){
			request.getSession().setAttribute("user", currUser);

			String fileUser = ctx.getRealPath("")+"/buyers.txt";
			Utility.saveUser(currUser, fileUser);
			
			String fileVeh = ctx.getRealPath("")+"/vehicles.txt";
			Utility.saveAllVehicles(lista, fileVeh);
			
			return "true";
		}
		
		return "false";
	}
}

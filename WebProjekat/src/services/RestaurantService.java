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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import Utility.Utility;
import model.Article;
import model.Restaurant;

@Path("/restaurants")
public class RestaurantService {

	@Context
	HttpServletRequest request;
	@Context
	ServletContext ctx;
	
	
	@GET
	@Path("/getR")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Restaurant> getR() throws IOException{
		
		ArrayList<Restaurant>  restList ;
		if(ctx.getAttribute("idRest") == null){
		 restList = new ArrayList<Restaurant>();
		
		String file = ctx.getRealPath("")+"/restaurants.txt";
		BufferedReader reader = null;
		try {
			 reader = new BufferedReader(new FileReader(new File(file)));
			
			String line;
			while((line = reader.readLine()) != null){
				String[] parts = line.split(",");
				
				if(Integer.parseInt(parts[1]) == 0)
					break;
				
				Restaurant rest = new Restaurant();
				String[] data = parts[0].split(" ");
				rest.setFields(data[0], data[1], data[2], data[3], 1);
				if(parts.length > 2){
					rest.addArticles(parts[2]);	
				}
				restList.add(rest);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			reader.close();
		}
		int idRes;
		if(restList.size() > 0){

			 idRes =(restList.get(restList.size()-1)).id + 1;
				
		}else{
			idRes = 0;
		}
		
		ctx.setAttribute("idRest", idRes);
		ctx.setAttribute("restaurants",restList);
		
		}else{
			
			restList = (ArrayList<Restaurant>) ctx.getAttribute("restaurants");
		}

		return restList;
	}
	
	@GET
	@Path("/filter/{restName},{restAdd},{restCat},{artType},{artName},{artPriceFrom},{artPriceTo}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Restaurant> filter(@PathParam("restName") String restName,
										@PathParam("restAdd") String restAdd,
										@PathParam("restCat") String restCat,
										@PathParam("artType") String artType,
										@PathParam("artName") String artName,
										@PathParam("artPriceFrom") String artPriceFrom,
										@PathParam("artPriceTo") String artPriceTo
										){
		ArrayList<Restaurant> restFilter = new ArrayList<>();
		ArrayList<Restaurant> restaurants = (ArrayList<Restaurant>) ctx.getAttribute("restaurants");
		for(Restaurant r: restaurants){
			Restaurant res = r.cloneThis();
			if(!restName.trim().equals("empty")){
				if(!r.name.startsWith(restName)){
					res = null;
				}
			}
			if(!restAdd.trim().equals("empty") && res != null){
				if(!r.address.startsWith(restAdd))
						res = null;
			}
			if(!restCat.trim().equals("empty") && res != null){
				if(!r.cat.equals(restCat))
						res = null;
			}
			
			
			if(res != null){
				res.offer = new ArrayList<>();
				for(int i =  r.offer.size() -1 ; i >= 0 ; i--){
					Article a = r.offer.get(i);
					if(!artType.trim().equals("empty")){
						if(!a.type.equals(artType)){
							a = null;
						}
					}
					
					if(!artName.trim().equals("empty") && a != null){
						if(!a.name.startsWith(artName)){
							a = null;
						}
					}
					
					if(!artPriceFrom.trim().equals("empty") && a != null){
						double priceFrom = Double.parseDouble(artPriceFrom);
						if(priceFrom > a.price)
							a = null;
					}
					if(!artPriceTo.trim().equals("empty") && a != null){
						double priceTo = Double.parseDouble(artPriceTo);
						if(priceTo < a.price)
							a = null;
					}
					
					if(a != null)
						res.offer.add(a);
				}
				
				restFilter.add(res);
			}
		}
		
		return restFilter;
	}
	
	@POST
	@Path("/addR")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String addR(Restaurant r) throws IOException{
		
		ArrayList<Restaurant> restList = (ArrayList<Restaurant>)ctx.getAttribute("restaurants");
		int idNum = (int) ctx.getAttribute("idRest");
		BufferedWriter writer = null;
		String file = ctx.getRealPath("")+"/restaurants.txt";
		
		r.setOtherFields(idNum, 1);
		boolean flag = false;
		for(Restaurant restCurr : restList){
			if(restCurr.name.equals(r.name)){
				flag = true;
				break;
			}
		}
		
		if(!flag){

			restList.add(r);
			idNum++;
			try {
				writer = new BufferedWriter(new FileWriter(new File(file), true));
				writer.write(r.toString());
				writer.newLine();


			} catch (Exception e) {
					e.printStackTrace();
			}finally {
				writer.close();
			}
			
			ctx.setAttribute("idRest", idNum);
			ctx.setAttribute("restaurants", restList);
		}
		
		return "true";
	}
	
	@POST
	@Path("/editR")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String editR(Restaurant r) throws IOException{
		ArrayList<Restaurant> restList = (ArrayList<Restaurant>)ctx.getAttribute("restaurants");
		
		for(Restaurant curr: restList){
			if(curr.id == r.id){
				curr.name = r.name;
				curr.address = r.address ;
				curr.cat = r.cat;
				break;
			}
		}
		
		String file = ctx.getRealPath("")+"/restaurants.txt";
		Utility.saveAllRestaurants(restList, file);
		ctx.setAttribute("restaurants", restList);
		return "true";
	}


	
	@POST
	@Path("/deleteR")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String deleteR(Restaurant r) throws IOException{
		
		ArrayList<Restaurant> restList = (ArrayList<Restaurant>)ctx.getAttribute("restaurants");
		
		for(Restaurant curr: restList){
			if(curr.id == r.id){
				curr.status = 0;
				break;
			}
		}
		String file = ctx.getRealPath("")+"/restaurants.txt";
		Utility.saveAllRestaurants(restList, file);
		ctx.setAttribute("restaurants", restList);
		
		return "true";
	}
}

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
import model.Article;
import model.Restaurant;

@Path("/articles")
public class ArticlesService {
	
	@Context
	HttpServletRequest request;
	@Context
	ServletContext ctx;
	
	
	
	@POST
	@Path("/addA")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String addR(Article a) throws IOException{
		
		ArrayList<Restaurant> restList = (ArrayList<Restaurant>)ctx.getAttribute("restaurants");
		int idNum = -1;
		BufferedWriter writer = null;
		String file = ctx.getRealPath("")+"/restaurants.txt";
		
		if(ctx.getAttribute("idArt") != null){
			idNum = (int) ctx.getAttribute("idArt");	
		}else{
			for(Restaurant r : restList){
				if(r.id == a.parentID){
					if(r.offer.size() > 0){

						Article endId = r.offer.get(r.offer.size()-1);
						idNum = endId.id + 1;	
					}else{
						idNum = 0;
					}
				}	
			}
		}
			
		a.id = idNum;
		a.status = 1;
		for(Restaurant r : restList){
			if(r.id == a.parentID){
				r.addArticle(a);
			}
		}
		
		idNum++;
		
		String fileRest = ctx.getRealPath("")+"/restaurants.txt";
		Utility.saveAllRestaurants(restList, fileRest);


		ctx.setAttribute("idArt", idNum);
		ctx.setAttribute("restaurants", restList);
		return "true";
	}
	
	@POST
	@Path("/editA")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String editR(Article a) throws IOException{
		ArrayList<Restaurant> restList = (ArrayList<Restaurant>)ctx.getAttribute("restaurants");
		
		for(Restaurant curr: restList){
			if(curr.id == a.parentID){
				for(Article currArt : curr.offer){
					if(currArt.id == a.id){
						currArt.des =a.des;
						currArt.kol = a.kol;
						currArt.name = a.name;
						currArt.price = a.price;
						currArt.type = a.type;
						break;
					}
				}
				break;
			}
		}
		
		
		String fileRest = ctx.getRealPath("")+"/restaurants.txt";
		Utility.saveAllRestaurants(restList, fileRest);

		ctx.setAttribute("restaurants", restList);
		return "true";
	}


	
	
	@POST
	@Path("/deleteA")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String deleteA(Article a) throws IOException{
		
		ArrayList<Restaurant> restList = (ArrayList<Restaurant>)ctx.getAttribute("restaurants");
		
		for(Restaurant curr: restList){
			if(curr.id == a.parentID){
				for(Article currArt : curr.offer){
					if(currArt.id == a.id){
						currArt.status = 0;
					}
					break;
				}
				break;
			}
		}
		
		String fileRest = ctx.getRealPath("")+"/restaurants.txt";
		Utility.saveAllRestaurants(restList, fileRest);
		ctx.setAttribute("restaurants", restList);
		
		return "true";
	}

}

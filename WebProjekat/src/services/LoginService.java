package services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
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
import model.BuyerUser;
import model.Restaurant;


@Path("/login")
public class LoginService {
	
	@Context
	HttpServletRequest request;
	@Context
	ServletContext ctx;
	

	
	
	@POST
	@Path("/checkUser/{username},{pass}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public BuyerUser checkUser(@PathParam("username") String username, @PathParam("pass") String pass) throws IOException{
		//citati iz file i provjeriti da li postoji korisnik
		BuyerUser userN = null;
				
		
		String file = ctx.getRealPath("")+"/buyers.txt";
		userN = Utility.login(username,pass, file);
	
		if(userN != null)
			request.getSession().setAttribute("user", userN);
		
			
		return userN;
	}
	

	@GET
	@Path("/logged")
	@Produces(MediaType.APPLICATION_JSON)
	public BuyerUser logged(){
		//citati iz file i provjeriti da li postoji korisnik
		
		BuyerUser user = (BuyerUser) request.getSession().getAttribute("user");
		
		return user;
	}
	
	@POST
	@Path("/logout")
	@Produces(MediaType.APPLICATION_JSON)
	public String logout(){
		//citati iz file i provjeriti da li postoji korisnik
		
	 request.getSession().setAttribute("user", null);
		
		return "";
	}
	
	@GET
	@Path("/getKorisnike")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<BuyerUser> getKorisnike(){
		ArrayList<BuyerUser> korisnici ;
		
			String file = ctx.getRealPath("")+"/buyers.txt";
			korisnici = Utility.getUsers(file);
				
			ctx.setAttribute("korisnici", korisnici);
		
		
		return korisnici;
	}
	
	@POST
	@Path("/changeKor/{username},{type}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<BuyerUser> changeKor(@PathParam("username") String username, @PathParam("type") String type){
		ArrayList<BuyerUser> korisnici = (ArrayList<BuyerUser>) ctx.getAttribute("korisnici");
		String file = ctx.getRealPath("")+"/buyers.txt";
		
		for(BuyerUser koris : korisnici){
			if(koris.username.equals(username)){
				koris.type = type;
				koris.orders.clear();
				koris.resta.clear();
				koris.vehicle = null;
				Utility.saveUser(koris, file);
				break;
			}
		}
		
	
		ctx.setAttribute("korisnici", korisnici);
		
		return korisnici;
	}
	
	@POST
	@Path("/add")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String add(BuyerUser u){
		
		String file = ctx.getRealPath("")+"/buyers.txt";
		
		boolean existUsrname = false;
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(file)));
			
			String line;
			while((line = reader.readLine()) != null){
				String[] parts = line.split(" ");
				if(u.username.equals(parts[0]) || u.email.equals(parts[5])){
					existUsrname = true;
					break;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		if(!existUsrname){
			try {
				u.type = "K";
				u.setType();
				Utility.addUser(u , file);
				
			} catch (Exception e) {
					e.printStackTrace();
			}
			
			return "true";
		}
		
		
		return "false";
	}
	
	
	@POST
	@Path("/addFavRest")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String addFavRest(Restaurant favRest) throws IOException{
		
		BuyerUser user = (BuyerUser) request.getSession().getAttribute("user");
		
		if(user != null){
			boolean flag = false;
			
			for(Restaurant rest : user.resta){
				if(rest.name.equals(favRest.name)){
					flag = true;
					break;
				}
			}
			
			if(!flag)
				user.resta.add(favRest);
		}
		
		String file = ctx.getRealPath("")+"/buyers.txt";
		Utility.saveUser(user, file);
		
		request.getSession().setAttribute("user", user);
		
		return "true";
	}
	
	
}

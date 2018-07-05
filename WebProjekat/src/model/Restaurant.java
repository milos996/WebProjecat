package model;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Restaurant implements Cloneable{

	
	public int id;
	@JsonProperty("address")
	public String address;
	@JsonProperty("name")
	public String name;
	@JsonProperty("categories")
	public String cat;
	public int status;
	public ArrayList<Article> offer;
	
	
	public Restaurant(){
		offer = new ArrayList<Article>();
	}


	public void setFields(String id, String name, String add, String cat, int st) {
		this.id = Integer.parseInt(id);
		this.name = name;
		this.address = add;
		this.cat = cat;
		this.status = st;
		
	}
	
	public void addArticles(String articles){
		
		String[] articlesList = articles.split("\\$");
		for(int i=0 ; i < articlesList.length; i++){
			String[] parts = articlesList[i].split(";");
			if(parts.length > 0){

				if(Integer.parseInt(parts[1]) == 0)
					break;
				
				String[] currA = parts[0].split("/");
				Article a = new Article();
				a.setFields(currA[0], currA[1], currA[2], currA[3], currA[4], currA[5], currA[6], currA[7], 1);
				offer.add(a);
			
			}
		}
		
	}


	@Override
	public String toString() {
		String output = id + " " + name + " " + address + " " + cat + "," + status + "," ;
		
		for(Article a : offer){
			output += a.toString();
			output += "$";
		}
		
		return output;
	}


	public void setOtherFields(int idNum, int i) {
		this.id = idNum;
		this.status = i;
	}


	public void addArticle(Article a) {
		this.offer.add(a);
		
	}


	public Restaurant cloneThis() {
		// TODO Auto-generated method stub
		try {
			return (Restaurant) this.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
}

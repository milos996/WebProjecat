package model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Issu {

	public int id;
	
	@JsonProperty("article")
	public Article article;
	@JsonProperty("kol")
	public int kol;
	
	@Override
	public String toString() {
		
		String out = article.toString() + "'" + kol;
		
		return out;
	}

	public void setFields(String line) {
		
		String[] parts = line.split("'");
		Article newArt = new Article();
		String[] statusArticle = parts[0].split(";");
		String[] articleParts = statusArticle[0].split("/");
		newArt.setFields(articleParts[0], articleParts[1], articleParts[2],articleParts[3]
				, articleParts[4],articleParts[5],
				articleParts[6], articleParts[7], Integer.parseInt(statusArticle[1]));
		this.article = newArt;
		this.kol = Integer.parseInt(parts[1]);
		
	}
}

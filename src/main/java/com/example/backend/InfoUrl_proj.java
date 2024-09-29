package com.example.backend;
import java.util.Set;

/**
 * Classe POJO que contem informacoes sobre os urls.
 * Esta classe armazena o titulo, o conjunto de urls associados e uma citacao.
 */
public class InfoUrl_proj {
	String title ;
    Set<String> urls;
    String cit ;
    
	public InfoUrl_proj(String title, Set<String> urls, String cit) {
		this.title = title;
		this.urls = urls;
		this.cit = cit;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Set<String> getUrls() {
		return urls;
	}
	public void setUrls(Set<String> urls) {
		this.urls = urls;
	}
	public String getCit() {
		return cit;
	}

	public void setCit(String cit) {
		this.cit = cit;
	}
}

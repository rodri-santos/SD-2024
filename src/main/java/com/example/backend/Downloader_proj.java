package com.example.backend;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Classe responsavel por carregar as paginas web, retirar informacoes como o titulo, uma citacao e as palavras e urls que contem.
 */
public class Downloader_proj extends UnicastRemoteObject implements Runnable {
    private IndexStorageBarrel_proj_Interface index;
   
    /**
     * Construtor da classe Downloader_proj.
     * 
     * @param index Interface do index storage barrel.
     * @throws RemoteException.
     */
    public Downloader_proj(IndexStorageBarrel_proj_Interface index) throws RemoteException {
    	super();
        this.index = index;
    }
    
    /**
     * Metodo principal, executa o processo de carregar as paginas, ir buscar a informacao e passa-la para o index storage barrel.
     */
    public void run() {
        while (true) {
            String url = index.getUrl();
            if (url != null && !index.isVisited(url)) {
                try {
                    Document doc = Jsoup.connect(url).get();
                    String title = doc.title();
                    Set<String> words = extractWords(doc);
                    Set<String> urls_ass = extractUrlsAss(doc);
                    String cit = extractCit(doc);
                    for (String word : words) {
                        index.updateIndex_inv(url, word);               
                    }
                    System.out.println("Success - Url : " + url);
                    index.updateIndex_urlK(url, title, urls_ass, cit);
                } catch (HttpStatusException e) {
                    System.err.println("Error - URL: " + e.getUrl());
                   System.err.println("Status HTTP: " + e.getStatusCode());
                } catch (IllegalArgumentException e) {
                    System.err.println("Error - Malformed URL: " + url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                                          
            }
            if (index.isEmpty()) {
                try {
                    Thread.sleep(1000); 
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * Extrai as palavras presentes num documento.
     * 
     * @param doc Documento.
     * @return Conjunto de palavras.
     */
    private Set<String> extractWords(Document doc) {
        Set<String> words = new HashSet<>();
        StringTokenizer tokens = new StringTokenizer(doc.text());
        while (tokens.hasMoreElements()) {
            String word = tokens.nextToken().toLowerCase().replaceAll("[^a-z]", "");
            if (word.length() != 0)
                words.add(word);
        }
        return words;
    }
    
    /**
     * Extrai os urls presents no documento e coloca-os na fila.
     * 
     * @param doc Documento.
     * @return Conjunto de urls associados.
     */
    private Set<String> extractUrlsAss(Document doc) {
        Set<String> urls = new HashSet<>();
        Elements links = doc.select("a[href]");
        for (Element link : links) {
            String url = link.absUrl("href");
            urls.add(url);
            index.indexUrl(url);
        }
        return urls;
    }
    
    /**
     * Extrai o primeiro parágrafo com texto do documento.
     * 
     * @param doc Documento.
     * @return citação.
     */
    private String extractCit(Document doc) {  // procurar o primeiro paragrafo com texto
    	Elements paragraphs = doc.select("p"); 
    	String cit = ""; 
    	for (Element p : paragraphs) {    	   
    	    if (!p.text().isEmpty()) {
    	        cit = p.text(); 
    	        break; 
    	    }
    	}
        return cit;
    }

}


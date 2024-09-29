package com.example.backend;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Classe que contem o nosso server.
 * 
 * Inicializa os nossos barrels e as threads de downoalders.
 * Seleciona um barrel aleatorio para responder aos pedidos dos clients.
 */
public class RMISearchModule_proj extends UnicastRemoteObject implements RMISearchModule_proj_Interface {
    private List<IndexStorageBarrel_proj_Interface> storageBarrels;
    

    /**
     * Construtor da classe RMISearchModule_proj.
     * 
     * @param storageBarrels Lista de barrels que vao ser usados para responder aos pedidos.
     * @throws RemoteException.
     */
    public RMISearchModule_proj(List<IndexStorageBarrel_proj_Interface> storageBarrels) throws RemoteException {
    	super();
        this.storageBarrels = storageBarrels;
    }
    
    /**
     * Metodo que inicia o servidor RMI.
     *
     * @param args.
     * @throws MalformedURLException.
     */
    public static void main(String[] args) throws MalformedURLException {
        try {
        	List<IndexStorageBarrel_proj_Interface> storageBarrels = new ArrayList<>();
        	
    		RMISearchModule_proj m = new RMISearchModule_proj(storageBarrels);
			Naming.rebind("rmi://localhost:7000/sd_full", m);
			System.out.println("RMI Server ready.");

            
            IndexStorageBarrel_proj_Interface barrel1 = new IndexStorageBarrel_proj();
            IndexStorageBarrel_proj_Interface barrel2 = new IndexStorageBarrel_proj();
            IndexStorageBarrel_proj_Interface barrel3 = new IndexStorageBarrel_proj();
            
            storageBarrels.add(barrel1);
            storageBarrels.add(barrel2);
            storageBarrels.add(barrel3);

            
            int numDownloaders = 3; 
            for (int i = 0; i < numDownloaders; i++) {
                IndexStorageBarrel_proj_Interface selectedBarrel = storageBarrels.get(i % storageBarrels.size());
                Downloader_proj downloader = new Downloader_proj(selectedBarrel);
                Thread downloaderThread = new Thread(downloader);
                downloaderThread.start();
            }
            
        } catch (RemoteException e) {
            System.err.println("RMISearchModule exception: " + e.getMessage());
            e.printStackTrace();
        }
        
    }
    
    /**
     * Indexa um url em todos os barrels disponiveis.
     *
     * @param url Url a ser indexado.
     * @throws RemoteException.
     */
    @Override
    public void index(String url) throws RemoteException {
    	System.out.println("Recebido pedido de indexação para URL: " + url);
        for (IndexStorageBarrel_proj_Interface barrel : storageBarrels) {
            barrel.indexUrl_(url,barrel);
            System.out.println("URL indexada com sucesso no barril.");
        }
    }
    
    /**
     * Realiza uma pesquisa de um conjunto de termos.
     * Seleciona aleatoriamente um barrel para realizar a pesquisa.
     *
     * @param query Conjunto de termos a pesquisar.
     * @return Lista de listas dos resultados da pesquisa (ordenados).
     * @throws RemoteException.
     */
    @Override
    public List<List<String>> search_query(String query) throws RemoteException {
        Random random = new Random();
        IndexStorageBarrel_proj_Interface selectedBarrel = storageBarrels.get(random.nextInt(storageBarrels.size()));
        return selectedBarrel.search_query(query);
    }
    
    /**
     * Obtem os urls associados a um especifico.
     *
     * @param url Url que vai servir de chave na pesquisa.
     * @return Conjunto de URLs associados.
     * @throws RemoteException.
     */
    @Override
    public Set<String> getPagAss(String url) throws RemoteException {
        Set<String> urlsAss = new HashSet<>();
        for (IndexStorageBarrel_proj_Interface barrel : storageBarrels) {
        	urlsAss.addAll(barrel.getPagAss(url));
        }
        return urlsAss;
    }
    
    /**
     * Obtem os IDs das top storys do Hacker News.
     *
     * @return Conjunto dos IDs.
     * @throws IOException.
     */
    public Set<String> getBestStoryIds() throws IOException {
        Set<String> storyIds = new HashSet<>();     
        Document doc = Jsoup.connect("https://hacker-news.firebaseio.com/v0/beststories.json?print=pretty").ignoreContentType(true).get();
        String bodyText = doc.body().text();

        bodyText = bodyText.replaceAll("[\\[\\],]", "");
        String[] ids = bodyText.split("\\s+");
        
        for (String id : ids) {
            if (!id.isEmpty()) {
                storyIds.add(id);
            }
        }
        return storyIds; 
    }
    
    /**
     * Indexa os urls correspondentes aos IDs das top storys do Hacker News se contiverem um conjunto de termos.
     *
     * @param query Conjunto de termos a pesquisar.
     * @throws IOException.
     */
    public void search_queryHN(String query) throws IOException {
    	Set<String> ids = getBestStoryIds();   	
    	String[] terms = query.split("\\s+");
    	System.out.println("IDs");
    	System.out.println(ids);
    	for (String id : ids) {
            String base =  "https://hacker-news.firebaseio.com/v0/item/"+ id +".json?print=pretty";
            try {
            	String json = Jsoup.connect(base).ignoreContentType(true).execute().body();
                JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
                String url = jsonObject.get("url").getAsString();
                System.out.println("URL STORY");
            	System.out.println(url);
                String text = Jsoup.connect(url).get().text(); 
                
                boolean hasResults = true;
                for (String term : terms) {
                    if (!text.contains(term)) {
                    	hasResults = false;
                        break;
                    }
                } if (hasResults) {                    
                    for (IndexStorageBarrel_proj_Interface barrel : storageBarrels) {
                        barrel.indexUrl(url);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error in URL: " + base);
                e.printStackTrace();
            }
        }
    }
    
    
	public String sayHello() throws RemoteException {
		System.out.println("print do lado do servidor...!.");

		return "Hello, World!";
	}
}


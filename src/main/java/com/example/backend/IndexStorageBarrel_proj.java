package com.example.backend;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Classe responsavel por armazenar a informacao em duas estruturas diferentes: o invertedIndex e o urlKeyIndex.
 * A classe contem metodos para indexar novos urls, verificar se um url ja foi ou nao visitado, realizar pesquisas etc.
 */
public class IndexStorageBarrel_proj implements IndexStorageBarrel_proj_Interface {

	private ArrayList<String> listaUrls; 
    private Map<String, Set<String>> invertedIndex; // palavra - urls q contém a palavra
    private Map<String, InfoUrl_proj> urlKeyIndex; // url - info do urls (titulo e urls associados, cit) - pojo

    /**
     * Construtor da classe IndexStorageBarrel_proj.
     */
    public IndexStorageBarrel_proj() {
    	listaUrls = new ArrayList<>();
        invertedIndex = new HashMap<>();
        urlKeyIndex = new HashMap<>();
    }
    
    /**
     * Adiciona um Url a fila para ser depois analisado pelo downloader.
     *
     * @param url Url a ser indexado.
     */
    public void indexUrl(String url) {
        //System.out.println("Olá entrei no ISB");
        listaUrls.add(url);
    }

    public void indexUrl_(String url, IndexStorageBarrel_proj_Interface barrel) {
        System.out.println("Olá entrei no ISB " + barrel);
        listaUrls.add(url);
    }
    
    /**
     * Devolve e remove o proximo url da fila.
     *
     * @return Url a ser indexado ou null caso a lista esteja vazia.
     */
    public String getUrl() {
        if (!listaUrls.isEmpty()) {
            String url = listaUrls.remove(0);
            return url;
        } else {
            return null; 
        }
    }
    
    /**
     * Verifica se a lista de urls esta vazia.
     *
     * @return true se a lista estiver vazia, false caso contrario.
     */
    public boolean isEmpty() {
        return listaUrls.isEmpty();
    }
    
    /**
     * Verifica se um urls ja foi visitado.
     *
     * @param url Url a ser verificado.
     * @return true se o url ja foi visitado, false caso contrario.
     */
    public boolean isVisited(String url) {
        return urlKeyIndex.containsKey(url); 
    }
    
    /**
     * Atualiza o indice invertido (tem como chave palavras e como valor os urls que a contem.
     *
     * @param url Url que contem a palavra.
     * @param word Palavra (key do map invertedIndex).
     * @throws RemoteException.
     */
    public void updateIndex_inv(String url, String word) throws RemoteException {      
        if (!invertedIndex.containsKey(word)) {
            invertedIndex.put(word, new HashSet<>());
        }
        invertedIndex.get(word).add(url);
        //System.out.println("Palavra-chave '" + word + "' associada ao URL: " + url);
    }
    
    /**
     * Atualiza o urlKeyIndex (tem como chave um url e como valor info do URL [titulo, urls associados e citacao]).
     *
     * @param url Url.
     * @param title Titulo do urç.
     * @param urls Urls associados.
     * @param cit Citacao (primiero paragrafo com texto do url).
     * @throws RemoteException.
     */
    public void updateIndex_urlK(String url, String title, Set<String> urls, String cit) throws RemoteException {
        InfoUrl_proj info = new InfoUrl_proj(title, urls, cit);
        urlKeyIndex.put(url, info);
    }
    
    /*   
    public void printAllUrls() {
        System.out.println("Todos os URLs no urlKeyIndex:");
        for (String url : urlKeyIndex.keySet()) {
            System.out.println(url);
        }
    }
    */  
    
    /**
     * Pesquisa que urls contem um determinado conjunto de termos e devolve os resultados ordenados e com a informação necessária.
     *
     * @param query Conjunto de termos a pesquisar.
     * @return Lista de listas com os resultados da pesquisa (ordenados).
     */
    public List<List<String>> search_query(String query) {
        List<List<String>> resultsFinal = new ArrayList<>();

        StringTokenizer terms = new StringTokenizer(query);
        List<Set<String>> results = new ArrayList<>();


        while (terms.hasMoreElements()) {
            String term = terms.nextToken().toLowerCase().replaceAll("[^a-z]", "");
            if (invertedIndex.containsKey(term)) {
                results.add(new HashSet<>(invertedIndex.get(term)));
            } else {
                return resultsFinal;
            }
        }

        Set<String> commonUrls = results.get(0);
        for (int i = 1; i < results.size(); i++) {
            commonUrls.retainAll(results.get(i));
        }

        if (commonUrls.isEmpty()) {
            return resultsFinal;
        }

        List<Entry<String, Integer>> aux = new ArrayList<>();
        for (String url : commonUrls) {
            int a = 0;
            for (String urlAux : urlKeyIndex.keySet()) {
                Set<String> listaAux = urlKeyIndex.get(urlAux).getUrls();
                if (listaAux.contains(url)) {
                    a++;
                }
            }
            aux.add(new AbstractMap.SimpleEntry<>(url, a));
        }

        Collections.sort(aux, (url1, url2) -> {
            Integer v1 = url1.getValue();
            Integer v2 = url2.getValue();
            return Integer.compare(v2, v1);
        });

        for (Entry<String, Integer> entry : aux) {
            String url = entry.getKey();
            InfoUrl_proj info = urlKeyIndex.get(url);
            List<String> resultDetails = new ArrayList<>();
            resultDetails.add(info.getTitle());
            resultDetails.add(url);
            resultDetails.add(info.getCit());
            resultsFinal.add(resultDetails);
        }

        return resultsFinal;
    }
    
    /**
     * Devolve um conjunto de urls associados a um url especifico utilizando o urlKeyIndex.
     *
     * @param url Url que vai servir de chave na pesquisa.
     * @return Conjunto de urls associados.
     */
    public Set<String> getPagAss(String url) {
    	//System.out.println("Entrei no getPasAss");
        Set<String> pags = new HashSet<>();
        if (urlKeyIndex.containsKey(url)) {
        	InfoUrl_proj urlInfo = urlKeyIndex.get(url);
        	Set<String> urlsAss = urlInfo.getUrls();
            pags.addAll(urlsAss); 
            //System.out.println("Entrei no if do getPasAss");
        }
        return pags;
    }
   
}


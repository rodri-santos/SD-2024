package com.example.backend;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Interface Index Storage Barrel
 */
public interface IndexStorageBarrel_proj_Interface extends Remote {
	void indexUrl_(String url, IndexStorageBarrel_proj_Interface barrel);
	void indexUrl(String url);
	//void printAllUrls();
	boolean isEmpty();
	String getUrl();
    List<List<String>> search_query(String query) throws RemoteException;
    Set<String> getPagAss(String url) throws RemoteException;
	void updateIndex_inv(String url, String word) throws RemoteException;
	void updateIndex_urlK(String url, String title, Set<String> urls, String cit) throws RemoteException;
	boolean isVisited(String url);
}



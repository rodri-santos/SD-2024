package com.example.backend;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;

import org.jsoup.nodes.Document;

/**
 * Interface RMI search module
 */
public interface RMISearchModule_proj_Interface extends Remote {
    void index(String url) throws RemoteException;
    List<List<String>> search_query(String query) throws RemoteException;
    Set<String> getPagAss(String url) throws RemoteException;
    public String sayHello() throws java.rmi.RemoteException;
    Set<String> getBestStoryIds() throws IOException;
	void search_queryHN(String query) throws IOException;
}

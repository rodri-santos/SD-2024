package com.example.servingwebcontent;

import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import com.example.servingwebcontent.beans.Number;
import com.example.servingwebcontent.forms.Project;
import com.example.servingwebcontent.thedata.Employee;
import com.example.backend.RMISearchModule_proj_Interface;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Controller atua como client
 */
@Controller
public class GreetingController {

    @Resource(name = "requestScopedNumberGenerator")
    private Number nRequest;

    @Resource(name = "sessionScopedNumberGenerator")
    private Number nSession;

    @Resource(name = "applicationScopedNumberGenerator")
    private Number nApplication;

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public Number requestScopedNumberGenerator() {
        return new Number();
    }

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public Number sessionScopedNumberGenerator() {
        return new Number();
    }

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_APPLICATION, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public Number applicationScopedNumberGenerator() {
        return new Number();
    }

    @GetMapping("/")
    public String redirect() {
        return "redirect:/start";
    }

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        model.addAttribute("othername", "SD");
        return "greeting";
    }

    @GetMapping("/givemeatable")
    public String atable(Model model) {
        Employee[] theEmployees = {
            new Employee(1, "José", "9199999", 1890),
            new Employee(2, "Marisa", "9488444", 2120),
            new Employee(3, "Hélio", "93434444", 2500)
        };
        List<Employee> le = new ArrayList<>();
        Collections.addAll(le, theEmployees);
        model.addAttribute("emp", le);
        return "table";
    }

    @GetMapping("/create-project")
    public String createProjectForm(Model model) {
        model.addAttribute("project", new Project());
        return "create-project";
    }

    @PostMapping("/save-project")
    public String saveProjectSubmission(@ModelAttribute Project project) {
        // TODO: save project in DB here
        return "result";
    }

    @GetMapping("/counters")
    public String counters(Model model) {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);
        Integer counter = (Integer) session.getAttribute("counter");
        int c = (counter == null) ? 1 : counter + 1;
        session.setAttribute("counter", c);
        model.addAttribute("sessioncounter", c);
        model.addAttribute("requestcounter2", this.nRequest.next());
        model.addAttribute("sessioncounter2", this.nSession.next());
        model.addAttribute("applicationcounter2", this.nApplication.next());
        return "counter";
    }

    /**
     * @return so estamos a dar return ao start.html.
     */
    @GetMapping("/start")
    public String addUrl(Model model) {
        return "start";
    }

    /**
     * @param url receber o url que o utilizador quer indexar e fazemos uma verficacao simples (nao ser null e nao ser vazio).
     * @return podemos ver os urls a serem indexados no backend (cmd)
     */
    @PostMapping("/start")
    public String addUrlSubmit(@RequestParam(name = "url", required = false) String url, Model model) throws RemoteException, NotBoundException {
        if (url != null && !url.trim().isEmpty()) {
            try {
                Registry registry = LocateRegistry.getRegistry("localhost", 7000);
                RMISearchModule_proj_Interface searchModule = (RMISearchModule_proj_Interface) registry.lookup("sd_full");
                searchModule.index(url);
                model.addAttribute("message", "URL indexed successfully!");
                return "start";
            } catch (RemoteException e) {
                model.addAttribute("error", "Error indexing URL: " + e.getMessage());
            }
        }
        return "start";
    }

    private String lastQuery;

    /**
     * @return so estamos a dar return ao search.html.
     */
    @GetMapping("/search")
    public String showSearchPage(Model model) {
        return "search";
    }
    
    /**
     * @param query a query que o utilizador que procurar nos urls indexados.
     * @param page o numero da pagina atual.
     * @param size o numero de resultados por pagina (10).
     * @return guardamos o a query com lastQuery = query para que possa ser utilizado para efetuar a pesquisa da query nas top stories do hacker news
     */
    @PostMapping("/search")
    public String search(@RequestParam(name = "query", required = false) String query, @RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "size", defaultValue = "10") int size, Model model) {
        if (query != null && !query.trim().isEmpty()) {
            lastQuery = query;
        }
        return searchWithPagination(query, page, size, model);
    }

    /**
     * @return aqui fazemos o controlo da página onde estamos, o valor de page vai alterar consoante o utilizador clica em "Next" ou "Back".
     */
    @GetMapping("/search/page")
    public String searchPage(@RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "size", defaultValue = "10") int size,
            Model model) {
        return searchWithPagination(lastQuery, page, size, model);
    }

    /**
     * @return aqui agrupamos os resultados da pesquisa de 10 em 10, obtemos o total de resultados da pesquisa, vamos buscar o numero da pagina que comeca em 1, o que faz com que o start seja o resultado de pesquisa 0, em end adicionamos ao numero do resultado de pesquisa o tamanho da pagina (10) para assim obtermos 10 resultados por pagina.
     */
    private String searchWithPagination(String query, int page, int size, Model model) {
        if (query != null && !query.trim().isEmpty()) {
            try {
                Registry registry = LocateRegistry.getRegistry("localhost", 7000);
                RMISearchModule_proj_Interface searchModule = (RMISearchModule_proj_Interface) registry.lookup("sd_full");
                List<List<String>> searchResults = searchModule.search_query(query);

                int totalResults = searchResults.size();
                int start = (page - 1) * size;
                int end = Math.min(start + size, totalResults);
                List<List<String>> paginatedResults = searchResults.subList(start, end);

                model.addAttribute("searchResults", paginatedResults);
                model.addAttribute("currentPage", page);
                model.addAttribute("totalPages", (int) Math.ceil((double) totalResults / size));
                model.addAttribute("query", query);
            } catch (RemoteException e) {
                model.addAttribute("error", "Error searching: " + e.getMessage());
            } catch (NotBoundException e) {
                model.addAttribute("error", "Search module not found!");
            }
        }
        return "search";
    }

    /**
     * @return aqui apenas temos getmapping e damos return ao search.html porque não queremos ver isto na webapp, apenas queremos que esteja a correr no backend.
     */
    @GetMapping("/index-hackernews")
    public String indexHackerNews(Model model) {
        if (lastQuery != null && !lastQuery.trim().isEmpty()) {
            try {
                Registry registry = LocateRegistry.getRegistry("localhost", 7000);
                RMISearchModule_proj_Interface searchModule = (RMISearchModule_proj_Interface) registry.lookup("sd_full");
                searchModule.search_queryHN(lastQuery);
                model.addAttribute("message", "Search completed and URLs indexed with the search query: " + lastQuery);
            } catch (NotBoundException | IOException e) {
                model.addAttribute("error", "Error: " + e.getMessage());
            }
        }
        return "search";
    }
}

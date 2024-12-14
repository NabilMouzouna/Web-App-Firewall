package ensaf.gtr2.firewall;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ensaf.gtr2.firewall.model.Log;
import ensaf.gtr2.firewall.model.Rule;
import ensaf.gtr2.firewall.repository.LogRepo;
import ensaf.gtr2.firewall.repository.RuleRepo;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class HttpRequestFiltering implements Filter {

    private final LogRepo logRepo;
    private final RuleRepo ruleRepo;

    @Autowired
    public HttpRequestFiltering(LogRepo logRepo, RuleRepo ruleRepo) {
        this.logRepo = logRepo;
        this.ruleRepo = ruleRepo;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
    
        String uri = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();
        Log newLog = new Log();
    
        // Log the request details
        System.out.println("Incoming request: " + method + " " + uri);
        newLog.setMethod(method);
        newLog.setTimestamp(LocalDateTime.now());
        newLog.setUrl(uri);
    
        // Fetch all rules from the database
        List<Rule> rules = ruleRepo.findAll();
    
        // Debug: Print out all rules
        System.out.println("Total Rules Found: " + rules.size());
        rules.forEach(rule -> {
            System.out.println("Rule Details - Type: '" + rule.getType() + 
                               "', Action: '" + rule.getAction() + 
                               "', Value: '" + rule.getValue() + "'");
        });
    
        // Check if the request matches any blocked route
        boolean isBlocked = rules.stream()
            .filter(rule -> "ROUTE".equalsIgnoreCase(rule.getType()))
            .anyMatch(rule -> {
                boolean typeMatch = "ROUTE".equalsIgnoreCase(rule.getType());
                boolean actionMatch = rule.getAction() != null && 
                                      ("BLOCKED".equalsIgnoreCase(rule.getAction()));
                boolean valueMatch = uri.equals(rule.getValue());
                
                System.out.println("Checking Rule - Type Match: " + typeMatch + 
                                   ", Action Match: " + actionMatch + 
                                   ", Value Match: " + valueMatch);
                
                return typeMatch && actionMatch && valueMatch;
            });
    
        if (isBlocked) {
            System.out.println("Access to " + uri + " is blocked by rule.");
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: This route is blocked.");
            newLog.setMalicious(true);
            logRepo.save(newLog);
            return;
        }
    
        // If not blocked, save as non-malicious log and continue
        newLog.setMalicious(false);
        logRepo.save(newLog);
    
        chain.doFilter(request, response);
    }
}
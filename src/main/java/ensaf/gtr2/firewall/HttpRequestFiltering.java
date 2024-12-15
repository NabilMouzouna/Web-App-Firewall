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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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

        // Check if the request matches any blocked rule
        boolean isBlocked = rules.stream().anyMatch(rule -> {
            if ("BLOCKED".equalsIgnoreCase(rule.getAction())) {
                // Handle "ROUTE" type rules
                if ("ROUTE".equalsIgnoreCase(rule.getType())) {
                    return uri.equals(rule.getValue());
                }
                // Handle "REQUEST-BODY" type rules
                else if ("REQUEST-BODY".equalsIgnoreCase(rule.getType())) {
                    return isBodyBlocked(httpRequest, rule);
                }
            }
            return false;
        });

        // Block the request if any rule matches
        if (isBlocked) {
            System.out.println("Access to " + uri + " is blocked by rule.");
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: This request is blocked.");
            newLog.setMalicious(true);
            logRepo.save(newLog);
            return;
        }

        // If not blocked, save as non-malicious log and continue
        newLog.setMalicious(false);
        logRepo.save(newLog);

        chain.doFilter(request, response);
    }

    /**
     * Check if the request body matches the blocked rule value.
     *
     * @param httpRequest the incoming HTTP request
     * @param rule        the blocking rule
     * @return true if the body contains the blocked value, false otherwise
     */
    private boolean isBodyBlocked(HttpServletRequest httpRequest, Rule rule) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(httpRequest.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder requestBody = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line).append("\n");
            }
            // Check if the request body contains the blocked value
            return requestBody.toString().contains(rule.getValue());
        } catch (IOException e) {
            // Log the exception
            System.err.println("Error reading the request body: " + e.getMessage());
            return false; // Return false if there's an error
        }
    }
}
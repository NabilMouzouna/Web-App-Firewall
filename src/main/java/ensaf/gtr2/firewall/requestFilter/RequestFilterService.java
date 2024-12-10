package ensaf.gtr2.firewall.requestFilter;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ensaf.gtr2.firewall.model.Log;
import ensaf.gtr2.firewall.model.Rule;
import ensaf.gtr2.firewall.repository.LogRepo;
import ensaf.gtr2.firewall.repository.RuleRepo;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class RequestFilterService {

    private final RuleRepo ruleRepository;
    private final LogRepo logRepo;

    @Autowired
    public RequestFilterService(RuleRepo ruleRepository, LogRepo logRepo) {
        this.ruleRepository = ruleRepository;
        this.logRepo = logRepo;
    }

    public boolean filterRequest(HttpServletRequest request) {
        List<Rule> rules = ruleRepository.findAll();
        for (Rule rule : rules) {
            switch (rule.getType()) {
                case "URL":
                    if (request.getRequestURI().matches(rule.getValue())) {
                        return handleRule(request, rule);
                    }
                    break;
                case "IP":
                    if (request.getRemoteAddr().equals(rule.getValue())) {
                        return handleRule(request, rule);
                    }
                    break;
                case "PARAMETER":
                    String[] params = request.getParameterMap().keySet().toArray(new String[0]);
                    for (String param : params) {
                        if (param.equals(rule.getValue())) {
                            return handleRule(request, rule);
                        }
                    }
                    break;
            }
        }
        return true;  // Allow request if no rules matched
    }

    private boolean handleRule(HttpServletRequest request, Rule rule) {
        Log log = new Log();
        log.setMethod(request.getMethod());
        log.setUrl(request.getRequestURI());
        log.setTimestamp(LocalDate.now());

        if ("ALLOW".equals(rule.getAction())) {
            log.setMalicious(false);
        } else {
            log.setMalicious(true);
        }

        // Save log for every decision, whether blocked or allowed
        logRepo.save(log);

        return !"BLOCK".equals(rule.getAction());
    }
}
package ensaf.gtr2.firewall.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ensaf.gtr2.firewall.model.Rule;
import ensaf.gtr2.firewall.repository.RuleRepo;

@RestController
@RequestMapping(path = "/api/admin/rules")
public class RuleController {

    @Autowired
    private RuleRepo ruleRepo;

    @GetMapping
    public ResponseEntity<List<Rule>> getRules() {
        try {
            List<Rule> RuleList = new ArrayList<>();
            ruleRepo.findAll().forEach(RuleList::add);
            if (RuleList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(RuleList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rule> getRuleById(@PathVariable Long id) {
        Optional<Rule> theRule = ruleRepo.findById(id);
        if (theRule.isPresent()) {
            return new ResponseEntity<>(theRule.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
public ResponseEntity<Rule> addRule(@RequestBody Rule rule) {
    boolean exists = ruleRepo.existsByTypeAndValueAndAction(rule.getType(), rule.getValue(), rule.getAction());
    if (exists) {
        return new ResponseEntity<>(HttpStatus.CONFLICT); // 409 Conflict
    }
    Rule savedRule = ruleRepo.save(rule);
    return new ResponseEntity<>(savedRule, HttpStatus.CREATED);
}

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteRuleById(@PathVariable Long id) {
        if (ruleRepo.existsById(id)) { 
            ruleRepo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
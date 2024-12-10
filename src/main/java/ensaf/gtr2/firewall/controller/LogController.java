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

import ensaf.gtr2.firewall.model.Log;
import ensaf.gtr2.firewall.repository.LogRepo;

@RestController
@RequestMapping(path = "/api/admin/logs")
public class LogController {

    @Autowired
    private LogRepo logRepo;

    @GetMapping
    public ResponseEntity<List<Log>> getLogs() {
        try {
            List<Log> logList = new ArrayList<>();
            logRepo.findAll().forEach(logList::add);
            if (logList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(logList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Log> getLogById(@PathVariable Long id) {
        Optional<Log> theLog = logRepo.findById(id);
        if (theLog.isPresent()) {
            return new ResponseEntity<>(theLog.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Log> addLog(@RequestBody Log log) {
        Log logObj = logRepo.save(log);
        return new ResponseEntity<>(logObj, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteLogById(@PathVariable Long id) {
        if (logRepo.existsById(id)) { 
            logRepo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
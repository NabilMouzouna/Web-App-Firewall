package ensaf.gtr2.firewall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ensaf.gtr2.firewall.model.Log;

@Repository
public interface LogRepo extends JpaRepository<Log,Long> {

    
}

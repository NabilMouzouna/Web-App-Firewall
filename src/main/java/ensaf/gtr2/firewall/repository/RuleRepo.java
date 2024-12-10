package ensaf.gtr2.firewall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ensaf.gtr2.firewall.model.Rule;

@Repository
public interface RuleRepo extends JpaRepository<Rule,Long> {

    
}

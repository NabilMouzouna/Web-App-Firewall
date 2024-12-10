package ensaf.gtr2.firewall.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="Rules")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Rule {
    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO )
    private Long id;
    private String type;
    private String value;
    private String action;
    
}
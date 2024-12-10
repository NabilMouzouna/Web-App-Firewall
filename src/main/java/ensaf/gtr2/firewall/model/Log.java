package ensaf.gtr2.firewall.model;


import java.time.LocalDate;
import java.time.LocalDateTime;

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
@Table(name="Logs")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Log {
    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO )
    private Long id;
    private String method;
    private String url;
    private boolean malicious;
    private LocalDateTime timestamp;
    
}
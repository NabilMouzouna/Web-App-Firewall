package ensaf.gtr2.firewall;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")               // Allow all endpoints
                .allowedOrigins("http://localhost:3000")  // Your Next.js frontend URL
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Allowed HTTP methods
                .allowedHeaders("*")            // Allow all headers
                .allowCredentials(true);        // Allow credentials (cookies, auth headers)
    }
}

package ensaf.gtr2.firewall.requestFilter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.logging.Logger;

@Component
public class RequestFilter implements HandlerInterceptor {

    private static final Logger logger = Logger.getLogger(RequestFilter.class.getName());

    private final RequestFilterService requestFilterService;

    @Autowired
    public RequestFilter(RequestFilterService requestFilterService) {
        this.requestFilterService = requestFilterService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!requestFilterService.filterRequest(request)) {
            // Blocked due to malicious content
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);  // 403 Forbidden
            response.setContentType("text/plain");  // Ensure response is plain text

            try {
                response.getWriter().write("Request blocked due to malicious content");
                response.getWriter().flush();  // Ensure content is sent to the client
            } catch (IOException e) {
                // Log exception
                logger.severe("IOException occurred while writing response: " + e.getMessage());

                // If writing fails, send internal server error response
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);  // 500 Internal Server Error
                response.getWriter().write("Error occurred while blocking the request");
                response.getWriter().flush();
            }
            return false;  // Prevent the request from reaching the controller
        }
        return true;  // Allow the request to continue
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // Not used currently
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // Not used currently
    }
}
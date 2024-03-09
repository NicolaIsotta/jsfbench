package jsf2jpa.beans;

import jakarta.faces.application.ResourceHandler;
import jakarta.faces.component.UIViewRoot;
import jakarta.faces.event.PhaseEvent;
import jakarta.faces.event.PhaseId;
import jakarta.faces.event.PhaseListener;
import jakarta.faces.push.PushContext;
import jakarta.inject.Inject;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Set;

/**
 * Simple demo strategy to check if a view can be accessed or not in public.
 *
 * @author Leonardo Uribe
 */
@WebFilter(servletNames = {"facesServlet"})
public class CheckUserFilter implements Filter {

    public static final Set<String> PUBLIC_VIEWS = Set.of("/home.jsf", "/home.xhtml", "/register.xhtml", "/register.jsf");

    @Inject
    private BookingSession session;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain fc) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String path = request.getRequestURI().substring(request.getContextPath().length());

        if (path.startsWith(ResourceHandler.RESOURCE_IDENTIFIER)) {
            fc.doFilter(request, response);
            return;
        }

        if (!PUBLIC_VIEWS.contains(path)) {
            if (request.getSession(false) == null || session == null || session.getUser() == null) {
                response.sendRedirect(request.getContextPath() + "/home.xhtml");
                return;
            }
        }
        
        fc.doFilter(req, res);
    }

}

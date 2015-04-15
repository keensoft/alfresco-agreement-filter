package es.keensoft.share.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.context.ApplicationContext;
import org.springframework.extensions.surf.RequestContextUtil;
import org.springframework.extensions.surf.site.AuthenticationUtil;
import org.springframework.extensions.surf.support.AlfrescoUserFactory;
import org.springframework.extensions.surf.util.URLEncoder;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.connector.Connector;
import org.springframework.extensions.webscripts.connector.ConnectorContext;
import org.springframework.extensions.webscripts.connector.ConnectorService;
import org.springframework.extensions.webscripts.connector.HttpMethod;
import org.springframework.extensions.webscripts.connector.Response;
import org.springframework.web.context.support.WebApplicationContextUtils;

// Unordered filter: chain until first user logged request arrives
@WebFilter(urlPatterns={"/page/*"})
public class AgreementFilter implements Filter {
	
	private static final String AGREEMENT_PAGE_PATH = "/agreement";
	private static final String AGREEMENT_REDIRECT_PAGE_PATH = "/agreement-redirect";
	public static String SESSION_ATTRIBUTE_KEY_AGREEMENT_CHECKED = "_alf_AGREEMENT_CHECKED";
	
	private ConnectorService connectorService;

	@Override
	public void init(FilterConfig config) throws ServletException {
        ApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
        this.connectorService = (ConnectorService)context.getBean("connector.service");
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession();
        
		String userId = AuthenticationUtil.getUserId(request);
        
		if (checkAgreement(request, userId)) {
			
			try {
				
				RequestContextUtil.initRequestContext(WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext()), request, true);
				Connector conn = connectorService.getConnector(AlfrescoUserFactory.ALFRESCO_ENDPOINT_ID, userId, session);
	            Response res = conn.call("/keensoft/agreement/" + URLEncoder.encode(userId), new ConnectorContext(HttpMethod.GET));
		        
		        if (Status.STATUS_OK == res.getStatus().getCode()) {
		        	
                    JSONObject userData = (JSONObject) new JSONParser().parse(res.getResponse());
                    if (userData.get("agreementChecked") != null) {
    		        	session.setAttribute(SESSION_ATTRIBUTE_KEY_AGREEMENT_CHECKED, Boolean.TRUE);
    		    		chain.doFilter(req, resp);
                    } else {
		                response.sendRedirect(request.getContextPath() + "/page/agreement");
                    }
                    
		        }
    
			} catch (Exception e) {
				throw new ServletException(e);
			}
			
		} else {
			
    		chain.doFilter(req, resp);
    		
		}
		

	}
	
	private boolean checkAgreement(HttpServletRequest request, String userId) {
		
        HttpSession session = request.getSession();
        
		boolean userLoggedIn = AuthenticationUtil.getUserId(request) != null;
		
		boolean agreementPreviouslyChecked = 
				session.getAttribute(SESSION_ATTRIBUTE_KEY_AGREEMENT_CHECKED) != null && 
				(Boolean) session.getAttribute(SESSION_ATTRIBUTE_KEY_AGREEMENT_CHECKED);
		
		boolean agreementPageRequested = 
				request.getPathInfo().endsWith(AGREEMENT_PAGE_PATH) || 
				request.getPathInfo().endsWith(AGREEMENT_REDIRECT_PAGE_PATH);
		
		return (userLoggedIn && !agreementPreviouslyChecked && !agreementPageRequested);

	}
	
	@Override
	public void destroy() {}
}

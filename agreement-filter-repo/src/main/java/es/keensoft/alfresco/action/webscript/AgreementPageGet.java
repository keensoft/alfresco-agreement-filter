package es.keensoft.alfresco.action.webscript;

import java.io.IOException;

import org.alfresco.model.QuickShareModel;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.repo.security.authentication.AuthenticationUtil.RunAsWork;
import org.alfresco.service.cmr.quickshare.QuickShareService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.SearchService;
import org.json.simple.JSONObject;
import org.springframework.extensions.webscripts.AbstractWebScript;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;
import org.springframework.http.MediaType;

public class AgreementPageGet extends AbstractWebScript {
	
	private NodeService nodeService;
	private SearchService searchService;
	private QuickShareService quickShareService;
	private String agreementPagePath;

	@SuppressWarnings("unchecked")
	@Override
	public void execute(WebScriptRequest req, final WebScriptResponse res) throws IOException {
		
		try {
			
			AuthenticationUtil.runAsSystem(new RunAsWork<Void>() {
		
				@Override
				public Void doWork() throws Exception {
					
					ResultSet rs = searchService.query(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE, 
							SearchService.LANGUAGE_FTS_ALFRESCO, 
							"PATH:\"/" + agreementPagePath + "\"");
					
					if (rs.getNumberFound() == 0) {
					
						throw new Exception("File " + agreementPagePath + " not found!");
						
					} else {
						
						NodeRef agreementFile = rs.getNodeRef(0);
						
						String sharedId = (String) nodeService.getProperty(agreementFile, QuickShareModel.PROP_QSHARE_SHAREDID);
						if (sharedId == null) {
							quickShareService.shareContent(agreementFile);
							sharedId = (String) nodeService.getProperty(agreementFile, QuickShareModel.PROP_QSHARE_SHAREDID);
						}
						
				    	JSONObject objProcess = new JSONObject();
				    	objProcess.put("sharedId", sharedId);
						
				    	String jsonString = objProcess.toString();
				    	res.setContentType(MediaType.APPLICATION_JSON.toString());
				    	res.setContentEncoding("UTF-8");
				    	res.getWriter().write(jsonString);
				    	
						return null;
						
					}
				}
		    	
			});
	    	
		} catch (Exception e) {
			throw new IOException(e);
		}
		
	}
	
	public void setNodeService(NodeService nodeService) {
		this.nodeService = nodeService;
	}

	public void setAgreementPagePath(String agreementPagePath) {
		this.agreementPagePath = agreementPagePath;
	}

	public void setQuickShareService(QuickShareService quickShareService) {
		this.quickShareService = quickShareService;
	}

	public void setSearchService(SearchService searchService) {
		this.searchService = searchService;
	}

}
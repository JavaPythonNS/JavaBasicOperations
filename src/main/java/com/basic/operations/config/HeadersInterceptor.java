package com.basic.operations.config;



import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.basic.operations.models.AppVersion;
import com.basic.operations.services.IUserServices;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class HeadersInterceptor implements HandlerInterceptor {
	
	@Autowired
	IUserServices userServices;
	
	String link = "";
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		System.out.println("request = "+request);
		if(request.getHeader("deviceType")==null) {
			setDeviceTypeNotFoundResponse(response);
			return false;	
		}else if(request.getHeader("appVersion")==null){
			setAppVersionNotFoundResponse(response);
			return false;
		}else if(request.getHeader("deviceType")!=null && request.getHeader("appVersion")!=null){
			List<AppVersion> listAppVersions = userServices.getAppVersionsList();
			AppVersion objAppVersions = listAppVersions.get(listAppVersions.size()-1);
			String version = "";
			int compulsory = 1;
			if(request.getHeader("deviceType").charAt(0) == 'A'){
				version = objAppVersions.getAppVersionAndroid();
				link = objAppVersions.getAppLinkAndroid();
				compulsory = objAppVersions.getCompulsoryForAndroid();
			}else{
				version = objAppVersions.getAppVersionIOS();
				link = objAppVersions.getAppLinkIOS();
				compulsory = objAppVersions.getCompulsoryForIOS();
			}
			if(version.equals(request.getHeader("appVersion")) || compulsory == 0){
				return true;
			} else {
				setResponseForNewUpdate(response);
				return false;
			}
			
		}else {
			return true;
		}
	}
	private void setDeviceTypeNotFoundResponse(HttpServletResponse response)
			throws IOException {
	    response.setContentType("application/json");
	    Map<String, Object> errorBean = new HashMap<String, Object>();
	    errorBean.put("result", "Fail");
	    errorBean.put("message", "Header 'deviceType' is missing");
	    errorBean.put("status", "400");
	    ObjectMapper mapper = new ObjectMapper();
	    response.setContentType("application/json");
	    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	    response.getWriter().write(mapper.writeValueAsString(errorBean));
	}
	
	private void setAppVersionNotFoundResponse(HttpServletResponse response)
			throws IOException {
	    response.setContentType("application/json");
	    Map<String, Object> errorBean = new HashMap<String, Object>();
	    errorBean.put("result", "Fail");
	    errorBean.put("message", "Header 'appVersion' is missing");
	    errorBean.put("status", "400");
	    ObjectMapper mapper = new ObjectMapper();
	    response.setContentType("application/json");
	    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	    response.getWriter().write(mapper.writeValueAsString(errorBean));
	}
	
	private void setResponseForNewUpdate(HttpServletResponse response) throws IOException{
		response.setContentType("application/json");
	    Map<String, Object> errorBean = new HashMap<String, Object>();
	    errorBean.put("status","Fail");
	    //errorBean.put("message", "Your app version is outdated."+link);
	    errorBean.put("message", "Your app version is outdated.");
	    errorBean.put("link",link);
	    errorBean.put("status","400");
	    ObjectMapper mapper = new ObjectMapper();
	    response.setContentType("application/json");
	    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	    response.getWriter().write(mapper.writeValueAsString(errorBean));
	}
	
}
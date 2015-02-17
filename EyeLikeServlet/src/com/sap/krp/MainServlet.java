package com.sap.krp;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private UsageData data = new UsageData();
	
    public MainServlet() {
        super();
    }

    /**
     * Update Client
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("application/json");        
        
		String jsonResponse;
		if (StringUtils.isEmpty(data.getValue())) {
			jsonResponse = data.toJsonRandomVal();
		} else {
			jsonResponse = data.toJSON();
		}
		response.getWriter().println(jsonResponse);
	}
	
	/**
	 * Receive data from Engine
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.setHeader("Access-Control-Allow-Origin", "*");
    	
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (parameterMap != null) {
            String value = null;
            if (parameterMap.containsKey("value")) {
                value = parameterMap.get("value")[0];
            }
            data = new UsageData(value);
        }
    }
}



package com.sap.krp;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        
        response.getWriter().println(data.toJSON());
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



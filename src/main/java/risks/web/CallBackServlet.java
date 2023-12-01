package risks.web;

import com.nimbusds.oauth2.sdk.token.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.models.*;
import risks.lists.*;
import risks.utils.*;

@WebServlet(urlPatterns = {"/callback"}, loadOnStartup = 1)
public class CallBackServlet extends TopServlet {
    static Logger logger = LogManager.getLogger(CallBackServlet.class);				
    boolean debug = false;
    public void doGet(HttpServletRequest request,
		      HttpServletResponse response)
	throws IOException {

	PrintWriter out = response.getWriter();
	String id = "";
	Enumeration values = request.getParameterNames();
	String name= "";
	String value = "";
	while (values.hasMoreElements()) {
	    name = ((String)values.nextElement()).trim();
	    value = request.getParameter(name).trim();
	    if (name.equals("id"))
		id = value;	
	}					
	String code = request.getParameter("code");
	String state = request.getParameter("state");
	String original_state = (String)request.getSession().getAttribute("state");
	// System.err.println(" state "+state);
	// System.err.println(" code "+code);	
	boolean error_flag = false;
	if(state == null || !original_state.equals(state)){
	    System.err.println(" invalid state "+state);
	    error_flag = true;
	    // 
	}
	if(!error_flag){
	    User user = RiskClient.getInstance().endAuthentication(code, config);
	    if(user != null){
		request.getSession().setAttribute("user", user);
		String str ="<head><title></title><META HTTP-EQUIV=\""+
		    "refresh\" CONTENT=\"0; URL=" + url +
		    "Starter?";
		if(!id.equals("")) str += "&id="+id;
		str += "\">";
		out.println(str);				
		out.println("<body>");
		out.println("</body>");
		out.println("</html>");
	    }
	    else{
		error_flag = true;
	    }
	}
	if(error_flag){
	    out.println("<head><title>Risktrack</title></head>");
	    out.println("<body><center>");
	    out.println("<p><font color=red>Unauthorized access, check with IT"+
			", or try again later.</font></p>");
	    out.println("</center>");
	    out.println("</body>");
	    out.println("</html>");
	}
	out.flush();
    }
    
}

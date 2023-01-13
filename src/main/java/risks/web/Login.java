package risks.web;

import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.models.*;
import risks.lists.*;
import risks.utils.*;
/**
 * All users login form.
 * needed for CAS login
 */
// comment out this line if you want to use ADFS
// To use CAS make sure you set the cas filter in web.xml
//
@WebServlet(urlPatterns = {"/Login","/login"}, loadOnStartup = 1)
public class Login extends TopServlet{

    String cookieName = "";// "cas_session";
    String cookieValue = "";// ".bloomington.in.gov";
    static Logger logger = LogManager.getLogger(Login.class);
    /**
     * Generates the login form for all users.
     *
     * @param req the request 
     * @param res the response
     */
    public void doGet(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException {
	String username = "", ipAddress = "", message="", id="";
	boolean found = false;
	
	res.setContentType("text/html");
	PrintWriter out = res.getWriter();
	Enumeration values = req.getParameterNames();
	String name= "";
	String value = "";
	String userid = null;
	//
	// sAMAccountName = usernamexs
	//
	while (values.hasMoreElements()) {
	    name = ((String)values.nextElement()).trim();
	    value = req.getParameter(name).trim();
	    if (name.equals("id"))
		id = value;	
	}
	AttributePrincipal principal = null;				
	if (req.getUserPrincipal() != null) {
	    principal = (AttributePrincipal) req.getUserPrincipal();
	    userid = principal.getName();
	}
	/**
	if(principal != null){
	    final Map attributes = principal.getAttributes();
	    Iterator attributeNames = attributes.keySet().iterator();
	    if (attributeNames.hasNext()) {
		for (; attributeNames.hasNext(); ) {
		    String attributeName = (String) attributeNames.next();
		    // System.err.println(" name "+attributeName);
		    final Object attributeValue = attributes.get(attributeName);
		    if (attributeValue instanceof List) {
                        final List vals = (List) attributeValue;
                        System.err.println("Multi-valued attribute: " + vals.size());
			int jj=1;
                        for (Object val : vals) {
                            System.err.println(jj+" "+val);
			    jj++;
                        }
                    } else {
                        System.err.println(" value "+attributeValue);
                    }
		}
	    }
	}
	*/
	if(userid == null || userid.isEmpty()){
	    userid = req.getRemoteUser();
	}
	HttpSession session = null;
	session = req.getSession(false);
	if(userid != null){
	    // setCookie(req, res);
	    User user = getUser(userid);
	    if(session != null && user != null){
		session.setAttribute("user",user);
		// String url2 = url+"Framer?";
		String str ="<head><title></title><META HTTP-EQUIV=\""+
		    "refresh\" CONTENT=\"0; URL=" + url +
		    "Starter?";
		if(!id.equals("")) str += "&id="+id;
		str += "\">";
		out.println(str);				
		out.println("<body>");
		out.println("</body>");
		out.println("</html>");
		out.flush();
		return;
	    }
	}
	out.println("<head><title>Risktrack</title></head>");
	out.println("<body><center>");
	out.println("<p><font color=red>Unauthorized access, check with IT"+
		    ", or try again later.</font></p>");
	out.println("</center>");
	out.println("</body>");
	out.println("</html>");
	out.flush();
    }
    //
    void setCookie(HttpServletRequest req, 
		   HttpServletResponse res){ 
	Cookie cookie = null;
	boolean found = false;
	Cookie[] cookies = req.getCookies();
	if(cookies != null){
	    for(int i=0;i<cookies.length;i++){
		String name = cookies[i].getName();
		if(name.equals(cookieName)){
		    found = true;
		}
	    }
	}
	//
	// if not found create one with 0 time to live;
	//
	if(!found){
	    cookie = new Cookie(cookieName,cookieValue);
	    res.addCookie(cookie);
	}
    }
    /**
     * Procesesses the login and check for authontication.
     * 
     * @param username
     */		
    User getUser(String username){
	
	boolean success = true;
	User user = null;
	String fullName="",role="",dept="", message="";
	try{
	    User user2 = new User(username);
	    String back = user2.doSelect();
	    if(!back.equals("")){
		message += back; // an error or no user found
		logger.error(back);
			      }
	    else{
		user = user2;
	    }
	}
	catch (Exception ex) {
	    logger.error(ex);
	    success = false;
	    message += ex;
	}
	return user;
    }
    
		      
}



















































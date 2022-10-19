package risks.web;

import java.util.*;
import java.sql.*;
import java.io.*;
import javax.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import javax.naming.*;
import javax.naming.directory.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.models.*;
import risks.lists.*;
import risks.utils.*;
/**
 * Used for testing phase only
 */
// @WebServlet(urlPatterns = {"/Login"}, loadOnStartup = 1)
public class AdLogin extends HttpServlet{

    //
    String url="";
    boolean debug = false, activeMail = false;  
    String passStr ="";
    static Logger logger = LogManager.getLogger(AdLogin.class);
    /**
     * Generates the login form.
     * @param req
     * @param res
     * @throws ServletException
     * @throws IOException
     */
    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException {
	String name, value, id="";
	res.setContentType("text/html");
	PrintWriter out = res.getWriter();
	if(url.equals("")){
	    url    = getServletContext().getInitParameter("url");
	    String str = getServletContext().getInitParameter("debug");
	    if(str != null && str.equals("true")) debug = true;
	}
	out.println("<HTML><HEAD><TITLE>User Login</TITLE>");
	out.println("</HEAD><BODY>");
	out.println("<br><br>");
	out.println("<center><h2>Welcome to RiskTrack </h2>");
	out.println("<form name=\"userForm\" method=\"post\" action=\""+url+"Login\" >");
	out.println("<table border=0><tr><td>Username</td><td><input name=\"username\" type=\"text\" value=\"\" size=\"10\" /></td><tr>");
	out.println("<tr><td>&nbsp;</td><td><input type=submit value=Submit></td></tr>");
	out.println("</table> ");
	out.println("</form> ");	

	out.println("</body></html>");
	out.close();
    }									
    /**
     * Processes the login operation and validate user's authonticity.
     *
     * @param req
     * @param res
     * @throws ServletException
     * @throws IOException
     */			
    public void doPost(HttpServletRequest req, HttpServletResponse res) 
	throws ServletException, IOException{
	
	String message = "";
	String username = "", password = "", role="";
	PrintWriter os = null;
	res.setStatus(HttpServletResponse.SC_OK);
	res.setContentType("text/html");
	os = res.getWriter();
	// 
	if(url.equals("")){
	    url    = getServletContext().getInitParameter("url");
	    String str = getServletContext().getInitParameter("debug");
	    if(str != null && str.equals("true")) debug = true;
	}
	Enumeration values = req.getParameterNames();
	String name, value, id="";
	os.println("<html>");
	
	while (values.hasMoreElements()) {
	    name = ((String)values.nextElement()).trim();
	    value = (req.getParameter(name)).trim();
	    if (name.equals("username")) {
		username = value;
	    }
	}
	try {
	    //
	    User user = getUser(username);
	    //
	    // add the user to the session
	    //
	    if(user != null){
		HttpSession session = req.getSession(true);
		if(session != null){
		    session.setAttribute("user",user);
		}
		os.println("<head><title></title><META HTTP-EQUIV=\""+
			   "refresh\" CONTENT=\"0; URL=" + url +
			   "Starter?\"></head>");
		os.println("<body>");
	    }
	    else{
		message = "Unauthorized to access this system";
		os.println("<head><title></title><body><h2>Unauthorized access </h2>");
		os.println("<h3>Please try again or check with ITS</h3>");
	    }
	    os.println("</body>");
	    os.println("</html>");
	}
	catch (Exception ex) {
	    System.err.println(""+ex);
	    os.println(ex);
	}
	os.flush();
	//
    }

    User getUser(String username){
		
	boolean success = true;
	User user = null;
	String fullName="",role="",dept="", message="";
	try{
	    User user2 = new User(username);
	    String back = user2.doSelect();
	    if(back.equals("")){
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























































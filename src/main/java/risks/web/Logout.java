package risks.web;
import java.util.*;
import java.sql.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

/**
 * Logout page.
 *
 * @author W. Sibo
 * 
 */
@WebServlet(urlPatterns = {"/Logout"}, loadOnStartup = 1)
public class Logout extends TopServlet{

    /**
     * Deletes the sesion info.
     * @param req
     * @param res
     */
    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException{
	
	res.setContentType("text/html");
	PrintWriter out = res.getWriter();
	Enumeration values = req.getParameterNames();
	String name= "";
	String value = "";
	HttpSession session = null;
	session = req.getSession(false);
	if(session != null){
	    session.removeAttribute("user");
	    session.invalidate();
	}
	res.sendRedirect(url);
	return;
    }

}























































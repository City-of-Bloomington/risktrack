package risks.web;
import java.util.*;
import java.sql.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import risks.models.*;
import risks.lists.*;
import risks.utils.*;
/**
 *
 */
@WebServlet(urlPatterns = {"/Starter"}, loadOnStartup = 1)
public class Starter extends TopServlet{


    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException{

	res.setContentType("text/html");
	PrintWriter out = res.getWriter();
	Enumeration values = req.getParameterNames();
	String name, value, id="", unicID="";
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    value = (req.getParameter(name)).trim();
	    if(name.equals("id")){
		id = value;
	    }
	}
	HttpSession session = req.getSession(false);
	User user = null;
	if(session != null){
	    user = (User)session.getAttribute("user");
	}
	if(user == null){
	    res.sendRedirect(url+"Login");
	    return;
	}
	//
	// check for the user
	//
	// Inserts
	out.println(Inserts.xhtmlHeaderInc);
	out.println(Inserts.banner(url));
	out.println(Inserts.menuBar(url,true));
	out.println(Inserts.sideBar(url));
	//
	out.println("<div id=\"mainContent\">");
	out.println("<h2>RiskTrack Introduction</h2>");
	out.println("<fieldset><legend>Introduction </legend>");
	out.println("Select one of the following actions:<br />");
	out.println("<ul>");
	out.println("<li>To add a new Claimant, Defendant, select 'Claimant/Defendant'</li>");
	out.println("<li>To add a new tort claim select 'Tort Claim'</li>");
	out.println("<li>To add a new legal Versus, select 'Recovery Action'</li>");
	out.println("<li>To add a new Worker's comp, select 'Work's Comp'</li>");
	out.println("<li>To add a new Safety, select 'Internal Accident'</li>");
	out.println("<li>To search for claimant, defendant, select 'Search Claimant/Defendant'</li>");
	out.println("<li>To search for tort claims, select 'Search Claims'</li>");
	out.println("<li>To search for Legal, VS, Recovery Action, select 'Search Recovery Action'</li>");
	out.println("<li>To search for Worker's Comp, select 'Search Worker's Comp'</li>");
	out.println("<li>To search for safety, select 'Search Internal Accident'</li>");
	out.println("<li>To generate reports, select 'Reports'</li>");
	out.println("<li>To create labels, mail letters, select 'Send Out'</li>");
	out.println("<li>When done please click on 'Logout' from the menu bar</li>");
	out.println("</ul>");
	out.println("</fieldset>");
	out.println("</div>");
	out.println("</html>");
	out.close();
    }				   
    /**
     * @link #doGet
     */		  
    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException{
	doGet(req, res);
    }

}


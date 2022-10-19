package risks.web;

import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.models.*;
import risks.lists.*;
import risks.utils.*;
//
@WebServlet(urlPatterns = {"/LawFirmServ","/LawFirm"})
public class LawFirmServ extends TopServlet{

    static Logger logger = LogManager.getLogger(LawFirmServ.class);
    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException {
	doPost(req,res);

    }
    /**
     * @link #doGet
     * @see #doGet
     */
    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException {
    
	res.setContentType("text/html");
	PrintWriter out = res.getWriter();
	String name, value;

	String action="", message="",id="";
	boolean success = true;
	String [] vals;
	List<LawFirm> firms = null;
	Enumeration values = req.getParameterNames();
	LawFirm firm = new LawFirm(debug);
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();	
	    if (name.equals("id")){
		firm.setId(value);
		id = value;
	    }
	    else if (name.equals("name")){
		firm.setName(value);
	    }
	    else if (name.equals("address")){
		firm.setAddress(value);
	    }
	    else if (name.equals("contact")){
		firm.setContact(value);
	    }						
	    else if (name.equals("phones")) {
		firm.setPhones(value);
	    }
	    else if(name.equals("action")){
		action = value;  
	    }
	}
	if(action.startsWith("Save")){
	    String back = firm.doSave();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }
	    else{
		message = "Saved Successfully";
	    }
	}
	if(action.startsWith("Update")){
	    String back = firm.doUpdate();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }
	    else{
		message = "Updated Successfully";
	    }
	}
	else if(!id.equals("")){
	    String back = firm.doSelect();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }
	}
	if(firms == null){
	    LawFirmList dl = new LawFirmList(debug);
	    String msg = dl.lookFor();
	    if(msg.equals("")){
		firms = dl.getFirms();
	    }
	    else {
		logger.error(msg);
		message += msg;
		success = false;
	    }
	}
	//
	// Inserts
	out.println(Inserts.xhtmlHeaderInc);
	out.println(Inserts.banner(url));
	out.println(Inserts.menuBar(url, true));
	out.println(Inserts.sideBar(url));
	out.println("<div id=\"mainContent\">");
	out.println("<h3 class=\"titleBar\">Law Firm Info</h3>");		
	if(success){
	    if(!message.equals(""))
		out.println("<h3>"+message+"</h3>");
	}
	else{
	    if(!message.equals(""))
		out.println("<h3 class=\"errorMessages\">"+message+"</h3>");
	}
	if(!id.equals("")){
	    out.println("<center><h3>Edit Law Firm</h3>");
	}
	else{
	    out.println("<center><h3>New Law Firm </h3>");
	}
	//
	out.println("<table border width=\"90%\">");
	out.println("<tr><td>");
	out.println("<table>");
	out.println("<form name=myForm method=post>");
	out.println("<tr><td>");
	//
	out.println("<tr><td><label>Law Firm:</label></td><td>");
	if(!id.equals("")){
	    out.println("<input type=\"hidden\" name=\"id\" value=\""+id+"\" />");
	}
	out.println("<tr><td><label>Name:</label></td><td>");
	out.println("<input type=\"text\" name=\"name\" id=\"name\" value=\""+firm.getName()+"\" size=\"30\" maxlength=\"80\" /></td></tr>");

	out.println("<tr><td><label>Address:</label></td><td>");
	out.println("<input type=\"text\" name=\"address\" id=\"address\" value=\""+firm.getAddress()+"\" size=\"30\" maxlength=\"80\" /></td></tr>");
	out.println("<tr><td><label>Contact:</label></td><td>");
	out.println("<input type=\"text\" name=\"contact\" id=\"contact\" value=\""+firm.getContact()+"\" size=\"30\" maxlength=\"80\" /></td></tr>");
	out.println("<tr><td><label>Phone(s):</label></td><td>");
	out.println("<input type=\"text\" name=\"phones\" id=\"phones\" value=\""+firm.getPhones()+"\" size=\"30\" maxlength=\"80\" /></td></tr>");
				
	if(id.equals("")){
	    out.println("<tr><td colspan=2 align=right><input "+
			"type=\"submit\" "+
			"name=\"action\" value=\"Save\">");
	    out.println("</td></tr>");
	}
	else{
	    out.println("<tr><td colspan=2 align=right><input "+
			"type=\"submit\" "+
			"name=\"action\" value=\"Update\">");
	    out.println("</td>");
	    out.println("</tr>");
	}
	//
	out.println("</table>");
	out.println("</form>");
	if(firms != null && firms.size() > 0){
	    out.println("<table><caption>Current Law Firms</caption>");
	    out.println("<tr><th></th><th>Name</th><th>Address</th>"+
			"<th>Contact</th><th>Phone(s)</th></tr>");
	    for(LawFirm one:firms){
		out.println("<tr>");
		out.println("<td><a href=\""+url+"LawFirmServ?id="+one.getId()+
			    "\">Edit</a></td>");
		out.println("<td>"+one.getName()+"</td>");
		out.println("<td>"+one.getAddress()+"</td>");
		out.println("<td>"+one.getContact()+"</td>");
		out.println("<td>"+one.getPhones()+"</td>");
		out.println("</tr>");	
	    }
	}
	out.println("</div>");
	out.print("</body></html>");
	out.close();
    }

}























































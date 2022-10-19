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
/**
 *
 */
@WebServlet(urlPatterns = {"/NoteServ","/Note"})
public class NoteServ extends TopServlet{

    static Logger logger = LogManager.getLogger(NoteServ.class);
    /**
     * Generates the main form with the view of
     * previously entered information.
     *
     * @param req
     * @param res
     * @throws ServletException
     * @throws IOException
     */
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
	String action="", id="", message="", risk_id="";
	String opener = "";
	String [] vals;
	HttpSession session = session = req.getSession(false);
	Note note = null;
	User user = null;
	//
	note = new Note(debug);
	Enumeration values = req.getParameterNames();
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();	
	    if (name.equals("user_id")){
		note.setUser_id(value);
	    }						
	    else if (name.equals("risk_id")){
		note.setRisk_id(value);
		risk_id = value;
	    }
	    else if (name.equals("id")){
		note.setId(value);
		id = value;
	    }
	    else if (name.equals("noteText")) {
		note.setNoteText(value);
	    }
	    else if (name.equals("date")) {
		note.setDate(value);
	    }
	    else if(name.equals("action")){
		action = value;  
	    }
	    else if(name.equals("opener")){
		opener = value;  
	    }						
	}
	if(session != null){
	    user = (User)session.getAttribute("user");
	}
	if(user == null){
	    res.sendRedirect(url+"Login");
	    return;
	}				
	if(action.equals("Save")){
	    String back = note.doSave();
	    if(!back.equals("")){
		message += back;
	    }
	    else{
		id = note.getId();
		message += "Saved Successfully";
	    }
	}
	else if(action.equals("Update")){
	    String back = note.doUpdate();
	    if(!back.equals("")){
		message += back;
	    }
	    else{
		message += "Updated Successfully";
	    }
	}
	else if(action.equals("Delete")){
	    String back = note.doDelete();
	    if(!back.equals("")){
		message += back;
	    }
	    else{
		note = new Note();
		note.setRisk_id(risk_id);
		id="";
		message += "Deleted Successfully";
	    }
	}				
	else if(!id.equals("")){
	    String back = note.doSelect();
	    if(!back.equals("")){
		message += back;
	    }
	    else{
		risk_id = note.getRisk_id();
	    }
	}
	else {
	    note.setRisk_id(risk_id);
	    note.setUser(user);
	}
	//
	out.println("<html><head><title>Risk Notes</title>");
	out.println("<script type='text/javascript'>");
	out.println(" function validateForm(){ ");
	out.println("  if(document.froms[0].noteText.value.trim() == ''){ ");
	out.println("   alert (\"No text to be saved or updated \"); ");
	out.println("  return false; ");
	out.println("  } ");
	out.println("  return true; ");
	out.println(" } ");
	out.println(" window.onunload = refreshParent; ");
	out.println(" function refreshParent() { ");
        out.println("   window.opener.location =\""+url+opener+"?id="+risk_id+"\";");
	out.println("  } ");				
	out.println(" </script>		                               ");
	out.println(" </head><body> ");
	out.println("<h3>Notes </h3>");
	if(!message.equals("")){
	    out.println("<p>"+message+"</p>");
	}
	out.println("<form name=\"myForm\" method=\"post\" "+
		    "onsubmit=\"return validateForm()\">");
	if(!id.equals(""))
	    out.println("<input type=\"hidden\" name=\"id\" "+
			"value=\""+id+"\" />");
	else
	    out.println("<input type=\"hidden\" name=\"user_id\" "+
			"value=\""+user.getId()+"\" />");						
	if(!risk_id.equals(""))
	    out.println("<input type=\"hidden\" name=\"risk_id\" "+
			"value=\""+risk_id+"\" />");
	if(!opener.equals(""))
	    out.println("<input type=\"hidden\" name=\"opener\" "+
			"value=\""+opener+"\" />");				
	//
	// username
	out.println("<table border=\"1\">");
	out.println("<tr><td><Label>Added By: </label></td><td>");
	if(!id.equals(""))
	    out.println(note.getUser());
	else
	    out.println(user);
	out.println("</td></tr>");
	//
	// Full Name
	out.println("<tr><td><label>Added Date:</label></td><td>");
	out.println(note.getDate()+"</td></tr>");
	//
	out.println("<tr><td><label>Related Risk ID:</label></td><td>");
	out.println(note.getRisk_id()+"</td></tr>");
	//
	out.println("<tr><td colspan=\"2\"><label>Notes:</label></td></tr>");
	out.println("<tr><td colspan=\"2\">");				
	out.println("<textarea name=\"noteText\" rows=\"5\" cols=\"50\" "+
		    "wrap=\"wrap\">");
	out.println(note.getNoteText());
	out.println("</textarea></td></tr>");
	out.println("<tr>");				
	if(id.equals("")){
	    out.println("<td colspan=\"2\">");
	    out.println("<input type=\"submit\" name=\"action\" value=\"Save\" />");
	}
	else{
	    out.println("<td>");
	    out.println("<input type=\"submit\" name=\"action\" value=\"Update\" />");
	    out.println("</td><td>");
	    out.println("<input type=\"submit\" name=\"action\" value=\"Delete\" />");						
	}
	out.println("</td></tr>");
	out.println("</table>");
	out.println("</form>");
	out.println("<li><a href=javascript:window.close();>"+
		    "Close This Window</a></li>");
	out.println("</body></html>");
	out.close();

    }

}























































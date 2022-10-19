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
@WebServlet(urlPatterns = {"/RelatedDeptServ","/RelatedDept"})
public class RelatedDeptServ extends TopServlet{

    static Logger logger = LogManager.getLogger(RelatedDeptServ.class);
    //
    /**
     * Generates the Case form and processes view, add, update and delete
     * operations.
     * @param req
     * @param res
     *
     */
    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException {
	doPost(req,res);
    }
    /**
     * @link #doGet
     */

    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException {

	String  id="", dept_id="";
	boolean success = true;
       	String message = "", action = "", opener="",
	    prevAction="", related_id="";
	
	res.setContentType("text/html");
	PrintWriter out = res.getWriter();
	String name, value;
	Enumeration values = req.getParameterNames();
	String [] vals;
	HttpSession session = session = req.getSession(false);
	User user = null;
	if(session != null){
	    user = (User)session.getAttribute("user");
	}
	RelatedDept rdept = new RelatedDept(debug);
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();	
	    if (name.equals("id")) {
		id = value;
		rdept.setId(value);
	    }
	    else if (name.equals("dept_id")) {
		rdept.setDept_id(value);
		dept_id = value;
	    }
	    else if (name.equals("opener")) {
		opener = value;
	    }						
	    else if (name.equals("related_id")) {
		rdept.setRelated_id(value);
		related_id = value;
	    }
	    else if (name.equals("phone")) {
		rdept.setPhone(value);
	    }
	    else if (name.equals("type")) { // default: Tort
		rdept.setType(value);
	    }			
	    else if (name.equals("supervisor")) {
		rdept.setSupervisor(value);
	    }
	    else if (name.equals("action")){ 
		action = value;  
	    }
	    else if (name.equals("action2")){
		if(!value.equals(""))
		    action = value;  
	    }						
	}
	if(!action.equals("") &&
	   !action.equals("Update") &&
	   action.equals(prevAction)){
	    action = "";
	}
	else if(!prevAction.equals("")){
	    //
	    // this senario should not happen
	    // acion     prevAction
	    // ======    ========
	    // zoom,     update
	    // save,     update
	    // Generate, Save
	    //
	    if(action.equals("zoom"))
		action = "";
	}
	if(action.equals("") && id.equals("") && !dept_id.equals("")){
	    Department dept = new Department(debug, dept_id);
	    String back = dept.doSelect();
	    if(back.equals("")){
		rdept.setDept_id(dept.getId());
		rdept.setPhone(dept.getPhone());
		rdept.setDept(dept);
	    }
	}
	//
	if(action.equals("Save")){
	    //
	    String back = "";
	    back = rdept.doSave();
	    if(back.equals("")){
		id = rdept.getId();
		message += "Data saved successfully";
	    }
	    else{
		message += back;
		logger.error(back);
		success = false;
	    }
	}
	else if(action.equals("Update")){
	    //
	    String back;
	    back = rdept.doUpdate();
	    if(!back.equals("")){
		message += back;
		logger.error(back);
		success = false;
	    }
	    else{
		message += "Data updated successfully";
	    }
	}
	else if(action.equals("Delete")){
	    //
	    // System.err.println("delete record");
	    //
	    String back = rdept.doDelete();
	    if(!back.equals("")){
		message += back;
		logger.error(back);
		success = false;
	    }
	    else{
		id="";
	    }
	}
	else if(!id.equals("")){	
	    //
	    String back = rdept.doSelect();
	    if(!back.equals("")){
		message += back;
		logger.error(back);
		success = false;
	    }
	    else{
		related_id = rdept.getRelated_id();
		dept_id = rdept.getDept_id();
	    }
	}
	List<Department> deptList = null;
	DeptList dl = new DeptList(debug);
	String back = dl.lookFor();
	if(back.equals("")){
	    deptList = dl.getDepartments();
	}
	//
	// Inserts
	out.println(Inserts.xhtmlHeaderInc);
	String bodyAction = "";
	out.println(Inserts.banner2(url, bodyAction));
	out.println("<div>");
	out.println("<h3 class=\"titleBar\">Related Departmento</h3>");
	//
	// if we have any message, it will be shown here
	if(success){
	    if(!message.equals(""))
		out.println("<h3>"+message+"</h3>");
	}
	else{
	    if(!message.equals(""))
		out.println("<h3 class=\"errorMessages\">"+message+"</h3>");
	}
	out.println("<script type=\"text/javascript\">");
	out.println("  function validateForm(){	              ");
	out.println("  return true;                           ");
	out.println("  }                                      ");
	out.println(" window.onunload = refreshParent; ");
	out.println(" function refreshParent() { ");
        out.println("   window.opener.location =\""+url+opener+"?id="+related_id+"\";");
	out.println("  } ");				
	out.println("</script>                                ");
	out.println("<form id=\"myForm\" method=\"post\">");
	out.println("<fieldset>");
	if(!id.equals("")){
	    out.println("<input type=\"hidden\" name=\"id\" value=\""+id+"\" />");
	}
	if(!opener.equals("")){
	    out.println("<input type=\"hidden\" name=\"opener\" value=\""+opener+"\" />");
	}				
	out.println("<input type=\"hidden\" name=\"related_id\" value=\""+related_id+"\">");
	out.println("<input type=\"hidden\" name=\"type\" value=\""+rdept.getType()+"\">");
	out.println("<input type=\"hidden\" name=\"action2\" value=\"\">");				
	out.println("<table>");
	out.println("<tr><td>You can edit the phone and supervisor name for this instance</td></tr>"); 
	out.println("<tr><td>");
	out.println("<label>Related Tort Claim ID: </label>"+
		    related_id);
	out.println("</td></tr>");		
	out.println("<tr><td>");
	//
	out.println("<label for=\"dept_id\"> Department: "+
		    "</label><select name=\"dept_id\" id=\"dept_id\">");
	out.println("<option value=\"\">Pick Department</option>");		
	if(deptList != null){
	    for(Department dept:deptList){
		String selected = "";
		if(dept.getId().equals(rdept.getDept_id()))
		    selected = "selected=\"selected\"";
		out.println("<option "+selected+" value=\""+dept.getId()+"\">"+dept.getName()+"</option>");
	    }
	}
	out.println("</select>");
	out.println("</td></tr>");
	out.println("<tr><td>");	
	out.println("<label for=\"phone\">Phone:</label>"+
		    "<input name=\"phone\" id=\"phone\" value=\""+
		    rdept.getPhone()+"\" size=\"15\" maxlength=\"30\" />");	
	out.println("</td></tr>");
		
	out.println("<tr><td><label for=\"sent\">Supervisor:</label>"+
		    "<input name=\"supervisor\" id=\"supervisor\" value=\""+
		    rdept.getSupervisor()+"\" size=\"30\" maxlength=\"50\" />");
	out.println("</td></tr>");		
	out.println("</table>");
	out.println("</fieldset>");
	//
	out.println("<fieldset>");
	if(id.equals("")){
	    out.println("<table class=\"control\"><tr><td>");
	    out.println("<input type=\"submit\" tabindex=\"31\" "+
			"accesskey=\"s\" id=\"action\" value=\"Save\" "+
			"name=\"action\" class=\"submit\">");
	    out.println("</td></tr></table>"); 
	    out.println("</form>");
	}
	else{ // save, update
	    //
	    out.println("<table class=\"control\">");
	    out.println("<td valign=top><input accesskey=\"u\" "+
			"tabindex=\"31\" "+
			"type=submit name=\"action\" id=\"action\" "+
			"class=\"submit\" value=\"Update\" />");
	    out.println("</form></td>");
	    out.println("<td>");
	    out.println("<form id=\"myForm2\" onsubmit=\"return "+
			"validateDelete();\">");
	    out.println("<input type=hidden name=\"id\" value=\""+
			id+"\" />");
	    out.println("<input type=\"submit\" name=\"action\" "+
			"id=\"action\" accesskey=\"e\" tabindex=\"33\" "+
			" value=\"Delete\" />");
	    out.println("</form>");
	    out.println("</td></tr></table>");
	}
	out.println("<p><a href=javascript:window.close();>Close This Window</a></p>");
	out.println("</fieldset>");
	out.println("</div>");
	out.print("</body></html>");
	out.flush();
	out.close();

    }
}























































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
@WebServlet(urlPatterns = {"/DeptServ","/Dept"})
public class DeptServ extends TopServlet{

    static Logger logger = LogManager.getLogger(DeptServ.class);
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

	String action="", message="";
	String deptName="", phone="", division="",
	    dept="";
	boolean success = true;
	String [] vals;
	List<Department> departments = null;
	Enumeration values = req.getParameterNames();
	Department dpp = new Department(debug);
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();	
	    if (name.equals("dept_id")){
		dpp.setId(value);
	    }
	    if (name.equals("id")){
		dpp.setId(value);
	    }
	    else if (name.equals("name")){
		dpp.setName(value);
	    }
	    else if (name.equals("division")) {
		dpp.setDivision(value);
	    }
	    else if (name.equals("phone")) {
		dpp.setPhone(value);
	    }
	    else if(name.equals("action")){
		action = value;  
	    }
	}
	if(action.startsWith("Save")){
	    dpp.doSave();
	    message = "Saved Successfully";
	}
	else if(action.equals("Update")){
	    dpp.doUpdate();
	    message = "Updated Successfully";
	}
	else if(action.startsWith("New")){
	    dpp = new Department(debug);
	}
	if(departments == null){
	    DeptList dl = new DeptList(debug);
	    String msg = dl.lookFor();
	    if(msg.equals("")){
		departments = dl.getDepartments();
	    }
	    else {
		logger.error(msg);
		message += msg;
		success = false;
	    }
	}
	if(dpp.hasId()){
	    String back = dpp.doSelect();
	    if(!back.equals("")){
		logger.error(back);
		message += " "+back;
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
	out.println("<h3 class=\"titleBar\">Departments Info</h3>");		
	if(success){
	    if(!message.equals(""))
		out.println("<h3>"+message+"</h3>");
	}
	else{
	    if(!message.equals(""))
		out.println("<h3 class=\"errorMessages\">"+message+"</h3>");
	}
	if(dpp.hasId()){
	    out.println("<center><h3>Edit Department </h3>");
	}
	else{
	    out.println("<center><h3>Add New Department </h3>");
	}
	//
	out.println("<table border width=\"90%\">");
	out.println("<tr><td>");
	out.println("<table>");
	out.println("<form name=myForm method=post>");
	out.println("<tr><td>");
	//
	out.println("<tr><td><label>Department:</label></td><td>");
	if(dpp.hasId()){
	    out.println("<input type=\"hidden\" name=\"id\" value=\""+dpp.getId()+"\" />");
	}
	out.println("<tr><td><label>Name:</label></td><td>");
	out.println("<input type=\"text\" name=\"name\" id=\"name\" value=\""+dpp.getName()+"\" size=\"30\"></td></tr>");
	out.println("<td><label>Division:</label></td><td>");
	out.println("<input type=\"text\" name=\"division\" id=\"division\" value=\""+dpp.getDivision()+"\" size=\"30\"></td></tr>");
	out.println("<tr><td><label>Dept. Phone:</label></td><td>");
	out.println("<input type=\"text\" name=\"phone\" id=\"phone\" value=\""+dpp.getPhone()+"\" size=\"20\"></td></tr>");
	//
	if(!dpp.hasId()){
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
	    out.println("<td colspan=2 align=right><input "+
			"type=\"submit\" "+
			"name=\"action\" value=\"New Department\">");
	    out.println("</td>");
	    out.println("</tr>");
	}
	//
	out.println("</table>");
	out.println("</form>");
	if(departments != null){
	    out.println("<table><caption>Current Departments</caption>");
	    out.println("<tr><th></th><th>Name</th><th>Division</th>"+
			"<th>Dept. Phone</th></tr>");
	    for(Department dp:departments){
		String str = dp.getId();
		if(str.equals("0") || str.equals("")) continue;
		out.println("<tr>");
		out.println("<td><a href='"+url+"DeptServ?id="+dp.getId()+
			    "&action=zoom'>Edit</a></td>");
		out.println("<td>"+dp.getName()+"</td>");
		out.println("<td>"+dp.getDivision()+"</td>");
		out.println("<td>"+dp.getPhone()+"</td>");
		out.println("</tr>");	
	    }
	}
	out.println("</div>");
	out.print("</body></html>");
	out.close();
    }

}























































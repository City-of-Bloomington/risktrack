package risks.web;

import java.util.*;
import java.sql.*;
import java.io.*;
import javax.sql.*;
import javax.naming.*;
import javax.naming.directory.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.models.*;
import risks.lists.*;
import risks.utils.*;

@WebServlet(urlPatterns = {"/DepartmentServ","/Department"})
public class DepartmentServ extends TopServlet{

    static Logger logger = LogManager.getLogger(DepartmentServ.class);
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
	String deptName="", busCat="", phone="", division="",
	    dept="", id="";
	boolean success = true;
	String [] vals;
	List departments = null;
	Enumeration values = req.getParameterNames();
	Department dpp = new Department(debug);
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();	
	    if (name.equals("id")){
		dpp.setId(value);
		id = value;
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
	    message = dpp.doSave();
	    if(message.equals(""))
		message = "Updated Successfully";
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
	out.println("<html><head><title>Department Info</title>");
	out.println("<script language=Javascript>");
	if(departments != null){
	    out.println(" var deptName   = []; ");
	    out.println(" var deptPhone  = []; ");
	    out.println(" var deptDiv    = []; ");
	    for(int i=0;i<departments.size();i++){
		Department dp = (Department)departments.get(i);
		String idd = dp.getId();
		out.println(" deptName["+idd+"]='"+dp.getName()+"';");
		out.println(" deptDiv["+idd+"]='"+dp.getDivision()+"';");
		out.println(" deptPhone["+idd+"]='"+dp.getPhone()+"';");
	    }
	}
	out.println("  function resetDept(obj){	              ");
	out.println("  var ind = obj.selectedIndex;           ");
	out.println("  var id = obj.options[ind].value;      ");
	out.println("  if(id != ''){                         ");
        out.println(" document.getElementById('division').value= deptDiv[id]; ");   
        out.println(" document.getElementById('phone').value= deptPhone[id];");
	out.println("  }}                                      ");  
	out.println("  function copyDataToForm(){                         ");
	out.println("  var str = document.forms[0].id.options.selectedIndex;   ");
	out.println("  var str2 = document.forms[0].division.value;         ");
	out.println("  var str4 = document.forms[0].phone.value;         ");
	out.println("  var str5 = document.forms[0].name.value;         ");
        out.println(" opener.document.getElementById(\"id\").options.selectedIndex = str; ");
	out.println(" opener.document.getElementById(\"id\").options[str].text = str5; ");		
        out.println(" opener.document.getElementById(\"mDivision\").innerHTML = str2; ");  
        out.println(" opener.document.getElementById(\"mDeptPhone\").innerHTML = str4; ");
	out.println(" window.close();                               "); 
	out.println(" }                                             ");
	out.println(" function updateField(str){                       ");
	out.println("  document.forms[0].empid.value=str;           ");
	out.println(" }                                             ");
	out.println(" function getDept(){                       ");
	out.println("var x=opener.document.getElementById(\"id\").selectedIndex;");
	out.println("var id=opener.document.getElementById(\"id\").options[x].value;");
	out.println(" if(id != ''){                               ");
	out.println("  document.forms[0].id.options.selectedIndex = x;       ");
	out.println("  document.forms[0].name.value=deptName[id];          ");
	out.println("  document.forms[0].division.value=deptDiv[id];           ");
	out.println("  document.forms[0].phone.value=deptPhone[id];            ");
	out.println(" }}                                                       ");
	out.println(" </script>		                                           ");
	if(action.equals(""))
	    out.println(" </head><body onload=\"getDept()\">  ");
	else
	    out.println(" </head><body> ");
	//
	if(success){
	    if(!message.equals(""))
		out.println("<h3>"+message+"</h3>");
	}
	else{
	    if(!message.equals(""))
		out.println("<h3 class=\"errorMessages\">"+message+"</h3>");
	}
	out.println("<center><h3>Edit Department Info </h3>");
	//
	out.println("<table border width=\"90%\">");
	out.println("<tr><td>");
	out.println("<table>");
	out.println("<form name=myForm method=post "+
		    "onSubmit=\"return copyDataToForm()\">");
	if(!id.equals("")){
	    out.println("<input type=\"hidden\" name=\"id\" value=\""+id+"\" />");
	}
	out.println("<tr><td>");
	//
	out.println("<tr><td><label>Department:</label></td><td>");
	out.println("<select name=\"id\" id=\"id\" onchange=\"resetDept(this)\">");
	out.println("<option value=\"\">&nbsp;</option>");
	if(departments != null && departments.size() > 0){
	    for(int i=0;i<departments.size();i++){
		Department dp = (Department)departments.get(i);
		if(dpp.getId().equals(dp.getId()))
		    out.println("<option selected=\"selected\" value=\""+dp.getId()+"\">"+dp.getName()+"</option>");
		else
		    out.println("<option value=\""+dp.getId()+"\">"+dp.getName()+"</option>");
	    }
	}
	out.println("</select></td></tr>");
	out.println("<tr><td><label>Name:</label></td><td>");
	out.println("<input type=\"text\" name=\"name\" id=\"name\" value=\""+dpp.getName()+"\" size=\"30\"></td></tr>");
	out.println("<td><label>Division:</label></td><td>");
	out.println("<input type=\"text\" name=\"division\" id=\"division\" value=\""+dpp.getDivision()+"\" size=\"30\"></td></tr>");
	out.println("<tr><td><label>Phone:</label></td><td>");
	out.println("<input type=\"text\" name=\"phone\" id=\"phone\" value=\""+dpp.getPhone()+"\" size=\"20\"></td></tr>");
	out.println("</table>");
	//
	out.println("<tr><td colspan=2 align=right><input "+
		    "type=\"submit\" "+
		    "name=\"action\" value=\"Save/Update\">");
	out.println("</td></tr>");
	//
	out.println("</table>");
	out.println("</form>");
	out.println("<li><a href=javascript:window.close();>"+
		    "Close This Window</a>");
	out.print("</center></body></html>");
	out.close();

    }

}























































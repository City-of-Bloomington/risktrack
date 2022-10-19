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
 * Handles getting employee info
 *
 */
@WebServlet(urlPatterns = {"/GetEmpInfoServ","/GetEmpInfo"})
public class GetEmpInfoServ extends TopServlet{

    static Logger logger = LogManager.getLogger(GetEmpInfoServ.class);
    static EnvBean bean = null;	
    /**
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
	String action="", username="", id="", message="", dept_id="";
	String usrid="", deptName="", phone="", fname="",
	    empTitle="", relatedId="", supervisor="", deptPhone="";
	String [] vals;
	String [] empArr = null;
	String opener = "";
	List<Department> deptList = null;
	Employee emp = null;
	//
	if(bean == null && context != null){
	    bean = new EnvBean();
	    String str = context.getInitParameter("ldap_url");
	    if(str != null)
		bean.setUrl(str);
	    str = context.getInitParameter("ldap_principle");
	    if(str != null)
		bean.setPrinciple(str);
	    str = context.getInitParameter("ldap_password");
	    if(str != null)
		bean.setPassword(str);			
	}
	if(true){
	    DeptList dl = new DeptList(debug);
	    String back = dl.lookFor();
	    if(back.equals("")){
		deptList = dl.getDepartments();
	    }
	}
	emp = new Employee(debug);
	Enumeration values = req.getParameterNames();
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();	
	    if (name.equals("userid")){
		emp.setUserid(value);
	    }						
	    else if (name.equals("dept_id")){
		emp.setDept_id(value);
		dept_id = value;
	    }
	    else if (name.equals("id")){
		emp.setId(value);
		id = value;
	    }
	    else if (name.equals("relatedId")){
		emp.setRelatedId(value);
		relatedId = value;
	    }	
	    else if (name.equals("fname")) {
		emp.setFullName(value);
	    }
	    else if (name.equals("supervisor")) {
		emp.setSupervisor(value);
	    }
	    else if (name.equals("phone")) {
		emp.setPhone(value);
	    }
	    else if (name.equals("deptPhone")) {
		emp.setDeptPhone(value);
	    }	
	    else if (name.equals("empTitle")) {
		emp.setTitle(value);
	    }
	    else if(name.equals("action")){
		action = value;  
	    }
	    else if(name.equals("opener")){
		opener = value;  
	    }						
	}
	if(action.equals("Save")){
	    String back = emp.doSave();
	    if(!back.equals("")){
		message += back;
	    }
	    else{
		id = emp.getId();
		message += "Saved Successfully";
	    }
	}
	else if(action.equals("Update")){
	    String back = emp.doUpdate();
	    if(!back.equals("")){
		message += back;
	    }
	    else{
		message += "Updated Successfully";
	    }
	}
	else if(action.equals("Delete")){
	    String back = emp.doDelete();
	    if(!back.equals("")){
		message += back;
	    }
	    else{
		emp = new Employee();
		id="";
		message += "Deleted Successfully";
	    }
	}				
	else if(!id.equals("")){
	    String back = emp.doSelect();
	    if(!back.equals("")){
		message += back;
	    }
	}

	//
	out.println("<html><head><title>Employee Info</title>");
	out.println("<script type='text/javascript'>");
	out.println("var APPLICATION_URL='"+url+"';");
	out.println("</script>");										
	out.println("<link rel=\"stylesheet\" href=\""+url+"js/jquery-ui.min-1.13.2.css\" type=\"text/css\" media=\"all\" />");
	out.println("<link rel=\"stylesheet\" href=\""+url+"js/jquery-ui.theme.min-1.13.2.css\" type=\"text/css\" media=\"all\" />");
	out.println("<script type=\"text/javascript\" src=\""+url+"js/jquery-3.6.1.min.js\"></script>");
	out.println("<script type=\"text/javascript\" src=\""+url+"js/jquery-ui.min-1.13.2.js\"></script>");	
	out.println("<script type=\"text/javascript\" src=\""+url+"js/risk.js\"></script>");	
	out.println("<script type='text/javascript'>");
	out.println("  function validateForm(){		                  ");
	out.println("    return true;                                     ");
	out.println("  }                                                ");
	out.println(" window.onunload = refreshParent; ");
	out.println(" function refreshParent() { ");
        out.println("   window.opener.location =\""+url+opener+"?id="+relatedId+"\";");
	out.println("  } ");
	out.println(" </script>		                               ");
	out.println(" </head><body> ");
	//
	out.println("<center><h3>Employee Info </h3>");
	if(!message.equals("")){
	    out.println("<p>"+message+"</p>");
	}
	out.println("<form name=\"myForm\" method=\"post\" "+
		    "onSubmit=\"return validateForm()\">");
	if(!relatedId.equals(""))
	    out.println("<input type=\"hidden\" name=\"relatedId\" "+
			"value=\""+relatedId+"\" />");
	if(!opener.equals(""))
	    out.println("<input type=\"hidden\" name=\"opener\" "+
			"value=\""+opener+"\" />");				
	if(id.equals(""))
	    out.println("Start typing employee last name then pick from the list<br/>");
	out.println("<table border width=\"90%\">");
	out.println("<tr><td>");
	out.println("<table>");
	//
	// Item title
	if(id.equals("")){
	    out.println("<tr><td><b>Last Name:</b></td><td>");
	    out.println("<div class=\"ui-widget\">");
	    out.println("<input size=\"30\" maxlength=\"70\" name=\"emp_name\" "+
			"value=\"\" id=\"emp_name\" /></div></td></tr>");
	}
	//
	// username
	out.println("<tr><td><b>Emp. Username</b></td><td>");
	out.println("<input size=\"30\" maxlength=\"70\" name=\"userid\" value=\""+emp.getUserid()+"\" id=\"userid_id\" /></td></tr>");
	//
	// Full Name
	out.println("<tr><td><b>Full Name:</b></td><td>");
	out.println("<input size=\"30\" maxlength=\"30\" name=\"fname\" value=\""+emp.getFullName()+"\" id=\"full_name_id\" /></td></tr> ");
	//
	// empTitle
	out.println("<tr><td><b>Job Title:</b></td><td>");
	out.println("<input size=\"30\" maxlength=\"70\" name=\"empTitle\" value=\""+emp.getTitle()+"\" id=\"title_id\" /></td></tr>");
	// Phone
	out.println("<tr><td><b>Phone:</b></td><td>");
	out.println("<input size=\"20\" maxlength=\"30\" name=\"phone\" value=\""+emp.getPhone()+"\" id=\"phone_id\" /></td></tr>");
	//
	// dept
	out.println("<tr><td><b>Department</b></td><td>");
	out.println("<select name=\"dept_id\">");
	out.println("<option></option>");
	if(deptList != null){
	    for(Department dp: deptList){
		String selected = "";
		if(dp.getId().equals(dept_id)){
		    selected = "selected=\"selected\"";
		}
		out.println("<option value=\""+dp.getId()+"\" "+
			    selected+">"+
			    dp.getInfo()+
			    "</option>");
	    }
	}
	out.println("</select></td></tr>");
	out.println("<tr><td><b>Dept. Phone:</b></td><td>");
	out.println("<input size=20 maxlength=30 name=\"deptPhone\" "+
		    "value=\""+emp.getDeptPhone()+"\"></td></tr>");
	out.println("<tr><td><b>Supervisor:</b></td><td>");
	out.println("<input size=30 maxlength=30 name=\"supervisor\" "+
		    "value=\""+emp.getSupervisor()+"\"></td></tr>");
	//
	//
	out.println("<tr><td>&nbsp;</td><td>");
	if(id.equals("")){
	    out.println("<input type=\"submit\" name=\"action\" value=\"Save\" />");
	}
	else{
	    out.println("<input type=\"submit\" name=\"action\" value=\"Update\" />");
	    out.println("<input type=\"submit\" name=\"action\" value=\"Delete\" />");						
	}
	out.println("</td></tr>");
	out.println("</table></td></tr>");
	out.println("</table>");
	out.println("</form>");
	out.println("<li><a href=javascript:window.close();>"+
		    "Close This Window</a>");
	out.print("</body></html>");
	out.close();

    }

}























































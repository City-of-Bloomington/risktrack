package risks.web;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.models.*;
import risks.lists.*;
import risks.utils.*;

@WebServlet(urlPatterns = {"/EmployeeService"})
public class EmployeeService extends HttpServlet{

    static final long serialVersionUID = 1200L;
    static Logger logger = LogManager.getLogger(EmployeeService.class);
    boolean debug = false;
    static EnvBean envBean = null;
		
    public void doGet(HttpServletRequest req,
		      HttpServletResponse res)
	throws ServletException, IOException {
	doPost(req,res);
    }

    /**
     * Generates the Group form and processes view, add, update and delete operations.
     *
     * @param req The request input stream
     * @param res The response output stream
     */
    public void doPost(HttpServletRequest req,
		       HttpServletResponse res)
	throws ServletException, IOException {

	String id = "";

	//
	String message="", action="";
	res.setContentType("application/json");
	PrintWriter out = res.getWriter();
	String name, value;
	String term ="", type="";
	boolean success = true;
	HttpSession session = null;
	if(envBean == null){
	    envBean = new EnvBean();
	    String str = getServletContext().getInitParameter("ldap_url");
	    envBean.setUrl(str);
	    str = getServletContext().getInitParameter("ldap_principle");
	    envBean.setPrinciple(str);
	    str = getServletContext().getInitParameter("ldap_password");
	    envBean.setPassword(str);
	}
	Enumeration<String> values = req.getParameterNames();
	String [] vals = null;
	while (values.hasMoreElements()){
	    name = values.nextElement().trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();
	    if (name.equals("term")) { // this is what jquery sends
		term = value;
	    }
	    else if (name.equals("type")) {
		type = value;
	    }
	    else if (name.equals("action")){
		action = value;
	    }
	    else{
		// System.err.println(name+" "+value);
	    }
	}
	EmpList empList =  null;
	List<Employee> emps = null;
	if(term.length() > 1){
	    //
	    empList = new EmpList(envBean);
	    empList.setName(term);
	    String back = empList.find();
	    if(back.equals("")){
		emps = empList.getEmps();
	    }
	}
	if(emps != null && emps.size() > 0){
	    String json = writeJson(emps);
	    out.println(json);
	}
	out.flush();
	out.close();
    }

    /**
     * Creates a JSON array string for a list of users
     *
     * @param users The users
     * @param type unused
     * @return The json string
     */
    String writeJson(List<Employee> emps){
	String json="";
	for(Employee one:emps){
	    if(!json.equals("")) json += ",";
	    json += "{\"id\":\""+one.getUserid()+"\",\"value\":\""+one.getName()+"\",\"fullname\":\""+one.getName()+"\",\"userid\":\""+one.getUserid()+"\",\"title\":\""+one.getTitle()+"\",\"phone\":\""+one.getPhone()+"\"}";
	}
	json = "["+json+"]";
	return json;
    }

}

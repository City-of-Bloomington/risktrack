package risks.web;

import java.util.*;
import javax.sql.*;
import java.sql.*;
import java.io.*;
import java.text.NumberFormat;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.models.*;
import risks.lists.*;
import risks.utils.*;

@WebServlet(urlPatterns = {"/OffDutyReport"})
public class OffDutyReport extends TopServlet{

    //
    static Logger logger = LogManager.getLogger(OffDutyReport.class);
    /**
     * Generates the request form.
     * @param req
     * @param res
     */
    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException {
	res.setContentType("text/html");
	PrintWriter out = res.getWriter();
	boolean success = true;
	String name, value;
	String dept="", username="", req_from="", req_to="";
	String message = "";
	Enumeration values = req.getParameterNames();
	String [] vals;
	List<Department> depts = null;
	while (values.hasMoreElements()){

	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();	
	}
	HttpSession session = session = req.getSession(false);
	User user2 = null;
	if(session != null){
	    user2 = (User)session.getAttribute("user");
	}
	if(user2 == null){
	    res.sendRedirect(url+"Login");
	    return;
	}
	if(depts == null){
	    DeptList dl = new DeptList(debug);
	    String msg = dl.lookFor();
	    if(msg.equals("")){
		depts = dl.getDepartments();
	    }
	    else {
		logger.error(msg);
		message += " "+msg;
		success = false;
	    }
	}
	// Inserts
	out.println(Inserts.xhtmlHeaderInc);
	out.println(Inserts.banner(url));
	out.println(Inserts.menuBar(url, true));
	out.println(Inserts.sideBar(url));
	//
	out.println("<div id=\"mainContent\">");
	out.println("<h3 class=\"titleBar\">Off Duty Report</h3>");
	if(!success){
	    if(!message.equals(""))
		out.println("<font color='red'>"+message+"</font><br>");
	}
	//
	out.println("<script type=\"text/javascript\">          ");
	out.println("  function validateForm(){	                ");
	out.println(" with (document.forms[0]){                 ");
	out.println(" if(req_from.value.length > 0){            ");
	out.println("  if(!checkDate(req_from)){                ");
	out.println("  return false;                            ");
	out.println("     }}                                    ");  
	out.println(" if(req_to.value.length > 0){              ");
	out.println("  if(!checkDate(req_to)){                  ");
	out.println("  return false;                            ");
	out.println("     }}                                    ");  
	out.println("  return true;                             ");
	out.println("   }                                       ");  
	out.println("  }                                        ");  
	out.println("  function checkPeriod(item){	        ");
	out.println(" if(item.value.length > 0){                ");
	out.println(" document.forms[0].period[2].checked=true; ");
	out.println("  }}                                       ");
	out.println("</script>                                  ");
	out.println("<fieldset><legend>Tort Claim Report</legend>  ");
	out.println("<center><table align='center' width='90%'> ");
	out.println("<tr><td>");
	out.println("<table width='100%'>");
	// 
	out.println("<form name=myForm method=post "+
		    "onSubmit=\"return validateForm()\">");
	//
	out.println("<tr><th>Department:</th><td>");
	out.println("<select name=\"dept_id\">");
	out.println("<option value=\"\">All");
	if(depts != null){
	    for(Department dep:depts){
		out.println("<option value=\""+dep.getId()+"\">"+dep.getInfo());
	    }
	}
	out.println("</select></td></tr>");
	out.println("<tr><td valign=\"middle\"><b>Date Range</b>"+
		    "</td><td>");
	out.println("<table><tr><td rowspan=\"2\" valign=\"middle\"><b>from:</b>"+
		    "</td><td>mm/dd/yyyy</td><td rowspan=\"2\" valign=\"middle\">"+
		    "<b>to:</b></td><td>mm/dd/yyyy</td></tr>");
	out.println("<tr>");
	out.println("<td><input type=text name=req_from value=\""+
		    req_from+"\" size=10 maxlength=10 "+
		    "onchange=\"checkPeriod(this)\" ></td>");
	out.println("<td><input type=text name=req_to value=\""+
		    req_to +"\" size=10 maxlength=10 "+
		    "onchange=\"checkPeriod(this)\" >"+
		    "</td></tr></table>");
	out.println("</td></tr>");
	out.println("<tr><th>Status</th>");
	out.println("<td><input type=\"radio\" "+
		    "name=\"status\" value=\"\" checked=\"checked\" /> All ");
	out.println("<input type=\"radio\" name=\"status\" "+
		    "value=\"Open\" /> Open");
	out.println("<input type=\"radio\" name=\"status\" "+
		    "value=\"Closed\" /> Closed");
	
	out.println("</td></tr>");
	out.println("<tr><th>On/Off Duty</th>");	
	out.println("<td><input type=\"radio\" "+
		    "name=\"onOffDuty\" value=\"\" checked=\"checked\" /> All ");
	out.println("<input type=\"radio\" name=\"onOffDuty\" "+
		    "value=\"off\" />Off Duty Only");
	out.println("<input type=\"radio\" name=\"onOffDuty\" "+
		    "value=\"on\" />On Duty Only");
	out.println("</td></tr>");
	
	out.println("<tr><th>Output Format</th>");
	out.println("<td><input type=\"radio\" "+
		    "name=\"output\" value=\"html\" checked=\"checked\" /> HTML ");
	out.println("<input type=\"radio\" name=\"output\" "+
		    "value=\"csv\" /> CSV (Spreadsheet)");
	out.println("</td></tr>");
	//
	out.println("<tr><td colspan=2><hr /></td></tr>");
	out.println("<tr><td colspan=2 align=right><input type=\"submit\" " +
		    "value=\"Submit\" /></td></tr>");
	out.println("</table></td></tr>");
	// 
	out.println("</table>");
	out.println("</fieldset>");
	//
	out.println("</form>");
	out.println("</div>");
	out.print("</center></body></html>");
	out.close();

    }
    /**
     * Generates the report according to users request.
     * @param req
     * @param res
     */  
    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException{
	res.setContentType("text/html");

	// fields to be shown
	String name, value;
	String message="";
	String action="";
	String req_to="", req_from="", status="", dept_id="", onOffDuty="";
	String output="html";
	Enumeration values = req.getParameterNames();
	String [] vals;
	while (values.hasMoreElements()){

	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();	
	    if (name.equals("req_from")) {
		req_from=value;
	    }
	    else if (name.equals("req_to")) {
		req_to=value;
	    }
	    else if (name.equals("output")){
		output = value;
	    }
	    else if (name.equals("status")){
		status = value;
	    }	    
	    else if (name.equals("dept_id")){
		dept_id = value;
	    }
	    else if (name.equals("onOffDuty")){
		onOffDuty = value;
	    }	    
	}
	OnOffList tcl = new OnOffList(debug);
	if(!dept_id.isEmpty()){
	    tcl.setDept_id(dept_id);
	}
	if(!req_from.isEmpty()){
	    tcl.setDateFrom(req_from);
	}
	if(!req_to.isEmpty()){
	    tcl.setDateTo(req_to);
	}
	if(!status.isEmpty()){
	    tcl.setStatus(status);
	}
	if(!onOffDuty.isEmpty()){
	    tcl.setOnOffDuty(onOffDuty);
	}
	List<List<String>> lists = null;
	String back = tcl.find();
	if(!back.isEmpty()){
	    message = "Error "+back;
	}
	else{
	    lists = tcl.getLists();
	    if(lists == null || lists.size() < 1){
		message = "No Match found ";
	    }
	}
	if(!message.isEmpty() || output.equals("html")){
	    PrintWriter out = res.getWriter();
	    out.println(Inserts.xhtmlHeaderInc);
	    out.println(Inserts.banner(url));
	    out.println(Inserts.menuBar(url, true));
	    out.println(Inserts.sideBar(url));
	    //
	    out.println("<div id=\"mainContent\">");
	    out.println("<h3 class=\"titleBar\">Off Duty Report</h3>");
	    if(!message.isEmpty()){
		out.println("<p>"+message+"</p>");
		out.println("<br />");
	    }
	    else if(lists.size() > 0){
		out.println("<table><tr>");
		out.println("<th>Type</th>");
		out.println("<th>Id</th>");
		out.println("<th>Status</th>");
		out.println("<th>Incident Date</th>");
		out.println("<th>Off Duty?</th>");		
		out.println("<th>Employee(s)</th>");
		out.println("<th>Department</th>");		
		out.println("</tr>");
		for(List<String> one:lists){
		    out.println("<tr>");
		    out.println("<td>"+one.get(0)+"</td>");
		    out.println("<td>"+one.get(1)+"</td>");
		    out.println("<td>"+one.get(2)+"</td>");
		    out.println("<td>"+one.get(3)+"</td>");
		    out.println("<td>"+one.get(4)+"</td>");
		    out.println("<td>"+one.get(5)+"</td>");
		    out.println("<td>"+one.get(6)+"</td>");		    
		    out.println("</tr>");
		}
		out.println("<table>");
	    }
	    out.println("</body>");
	    out.println("</html>");
	    out.close();
	}
	else if(output.equals("csv")){
	    res.setHeader("Expires", "0");
	    res.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
	    res.setHeader("Pragma", "public");
	    res.setHeader("Content-Disposition","inline; filename=off_duty.csv");
	    res.setContentType("application/csv");
	    PrintWriter out = res.getWriter();
	    out.print("\"Type\",");	    
	    out.print("\"Id\",");
	    out.print("\"Status\",");
	    out.print("\"Incident Date\",");
	    out.print("\"Off Duty\",");
	    out.print("\"Employee(s)\",");	    
	    out.print("\"Department\"\n");
	    for(List<String> one:lists){
 		out.print("\""+one.get(0)+"\",");
		out.print("\""+one.get(1)+"\",");
		out.print("\""+one.get(2)+"\",");
		out.print("\""+one.get(3)+"\",");
		out.print("\""+one.get(4)+"\",");
		out.print("\""+one.get(5)+"\",");		
		out.print("\""+one.get(6)+"\"\n");		
	    }
	    out.close();
	}
    }

}























































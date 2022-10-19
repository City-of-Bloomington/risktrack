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

@WebServlet(urlPatterns = {"/Report"})
public class Report extends TopServlet{

    //
    static Logger logger = LogManager.getLogger(Report.class);
    static NumberFormat format = NumberFormat.getCurrencyInstance();
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
	List<Employee> legalEmployees = null;
	List<Employee> tortEmployees = null;
	List<Employee> safetyEmployees = null;
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
	if(legalEmployees == null){
	    EmployeeList el = new EmployeeList(debug);
	    el.setLegalRelatedOnly();
	    String msg = el.lookFor();
	    if(msg.equals("")){
		legalEmployees = el.getEmployees();
	    }
	    else{
		logger.error(msg);
		message += msg;
		success = false;
	    }
	}
	if(tortEmployees == null){
	    EmployeeList el = new EmployeeList(debug);
	    el.setTortRelatedOnly();
	    String msg = el.lookFor();
	    if(msg.equals("")){
		tortEmployees = el.getEmployees();
	    }
	    else{
		logger.error(msg);
		message += msg;
		success = false;
	    }

	}
	if(safetyEmployees == null){
	    EmployeeList el = new EmployeeList(debug);
	    el.setSafetyRelatedOnly();
	    String msg = el.lookFor();
	    if(msg.equals("")){
		safetyEmployees = el.getEmployees();
	    }
	    else{
		logger.error(msg);
		message += msg;
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
	out.println("<h3 class=\"titleBar\">Risk Reports</h3>");
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
	out.println("<fieldset><legend>Report Options</legend>  ");
	out.println("<center><table align='center' width='90%'> ");
	out.println("<tr><td>");
	out.println("<table width='100%'>");
	// 
	out.println("<form name=myForm method=post "+
		    "onSubmit=\"return validateForm()\">");
	//
	// first report
	out.println("<tr><td colspan=2><input type=radio name=report "+
		    "value=statistics checked><b>Statistics Report</b>"+
		    "</td></tr>");
	out.println("<tr><td colspan=2><center>Report statistics; "+
		    " number of claims, types, total cost, etc "+
		    "</td></tr>");
	out.println("<tr><td></td><td>Department: ");
	out.println("<select name=\"dept\">");
	out.println("<option value=\"\">All");
	if(depts != null){
	    for(Department dep:depts){
		out.println("<option value=\""+dep.getId()+"\">"+dep.getInfo());
	    }
	}
	out.println("</select></td></tr>");
	out.println("<tr><td></td><td><br />For certain employee involved in accident, select from: ");
	out.println("</td></tr>");
	out.println("<tr><td></td><td>Recovery Action ");
	out.println("<select name=\"legalEmpid\">");
	out.println("<option value=\"\">All");
	if(legalEmployees != null){
	    for(int i=0;i<legalEmployees.size();i++){
		Employee one = legalEmployees.get(i);
		if(one != null) 
		    out.println("<option value=\""+one.getUserid()+"\">"+
				one.getFullName());
	    }
	}
	out.println("</select></td></tr>");
	out.println("<tr><td></td><td>Tort Claim: ");
	out.println("<select name=\"tortEmpid\">");
	out.println("<option value=\"\">All");
	if(tortEmployees != null){
	    for(Employee emp: tortEmployees){
		out.println("<option value=\""+emp.getUserid()+"\">"+
			    emp.getFullName());
	    }
	}
	out.println("</select></td></tr>");
	out.println("<tr><td></td><td>Internal Accident: ");
	out.println("<select name=\"safetyEmpid\">");
	out.println("<option value=\"\">All");
	if(safetyEmployees != null){
	    for(int i=0;i<safetyEmployees.size();i++){
		Employee one = safetyEmployees.get(i);
		if(one != null)
		    out.println("<option value=\""+one.getUserid()+"\">"+
				one.getFullName());
	    }
	}
	out.println("</select><br /><br /></td></tr>");
	//
	out.println("<tr><td>&nbsp;</td><td><input type=\"checkbox\" "+
		    "name=\"tort\" value=\"y\" checked>Tort Claims ");
	out.println("<input type=\"checkbox\" name=\"legal\" "+
		    "value=\"y\" checked>Recovery Actions ");
	out.println("<input type=\"checkbox\" name=\"intern\" "+
		    "value=\"y\" checked>Internal Accident");
	out.println("<input type=\"checkbox\" name=\"wcomp\" "+
		    "value=\"y\" >Workers Comp</td></tr>");
	out.println("<tr><td colspan=2><input type=radio name=report "+
		    "value=\"RecordOnly\"><b>Record Only Claims & Recovery "+
		    "Actions</b></td></tr>");
	out.println("<tr><td colspan=2><input type=radio name=report "+
		    "value=\"autoType\"><b>Auto Claims, Recovery "+
		    "Actions & Internal Accidents</b></td></tr>");
	//	
	out.println("<tr><td colspan=\"2\" align=center><table width=100%>");
	//
	// Date request
	//
	out.println("<tr><td align=right><b> Period: </b></td><td>");
	out.println("<input type=\"radio\" name=\"period\" checked=\"checked\" "+
		    "value=week>Week,");
	out.println("<input type=\"radio\" name=\"period\" "+
		    "value=month>Month,");
	out.println("<input type=radio name=period "+
		    "value=specified>specified below");
	out.println("</td><td>");
	//
	out.println("<tr><td align=right valign=middle><b>Period</b>"+
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
	//
	out.println("<tr><td colspan=2><hr></td></tr>");
	out.println("<tr><td colspan=2 align=right><input type=submit " +
		    "value=Submit></td></tr>");
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
	boolean show[] = { true, // 
	    true,false,false,false,false};
	Connection con = null;
	Statement stmt = null;
	ResultSet rs = null;
	PrintWriter out = res.getWriter();			  
	boolean connectDbOk = false, success = true;
	String name, value;
	String  id="", message="";
	String action="";
	String req_to="", req_from="";
	String period="", report="", law_firm_id="";
	String dept="", tortEmpid="",legalEmpid="",safetyEmpid="";

	boolean week=false, month=false, period2=false, surveyNotes=false;
	boolean tort=false,legal=false,intern=false,wcomp=false;
	Enumeration values = req.getParameterNames();
	String [] vals;
	String legalFullName="",tortFullName="",safetyFullName="";
	boolean empTbl = false;
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
	    else if (name.equals("period")){
		period = value;
	    }
	    else if (name.equals("dept")){
		dept = value;
	    }
	    else if (name.equals("law_firm_id")){
		law_firm_id = value;
	    }						
	    else if (name.equals("tortEmpid")){
		tortEmpid = value;
	    }
	    else if (name.equals("legalEmpid")){
		legalEmpid = value;
	    }
	    else if (name.equals("safetyEmpid")){
		safetyEmpid = value;
	    }
	    else if (name.equals("tort")){
		tort = true;
	    }
	    else if (name.equals("legal")){
		legal = true;
	    }
	    else if (name.equals("intern")){
		intern = true;
	    }
	    else if (name.equals("wcomp")){
		wcomp = true;
	    }
	    else if (name.equals("report")){
		report = value;
	    }
	}
	try{
	    con = Helper.getConnection();
	    if(con != null){
		stmt = con.createStatement();
		success = true;
	    }
	    if(!legalEmpid.equals("")){
		Employee emp = new Employee(debug, legalEmpid);
		String msg = emp.doSelect();
		if(msg.equals("")){
		    legalFullName = emp.getFullName();
		}
		else{
		    logger.error(msg);
		    message += msg;
		    success = false;
		}
	    }
	    if(!tortEmpid.equals("")){
		Employee emp = new Employee(debug, tortEmpid);
		String msg = emp.doSelect();
		if(msg.equals("")){
		    tortFullName = emp.getFullName();
		}
		else{
		    logger.error(msg);
		    message += msg;
		    success = false;
		}
	    }
	    if(!safetyEmpid.equals("")){
		Employee emp = new Employee(debug, safetyEmpid);
		String msg = emp.doSelect();
		if(msg.equals("")){
		    safetyFullName = emp.getFullName();
		}
		else{
		    logger.error(msg);
		    message += msg;
		    success = false;
		}
	    }
	}catch(Exception ex){
	    System.err.println(ex);
	    logger.error(ex);
	}
	//
	// Check if this user is allowed to 
	// access these data
	//
	if(period.equals("week")) week = true;
	else if(period.equals("month")) month = true;
	else if(period.equals("specified")) period2 = true;
	GregorianCalendar current_cal = new GregorianCalendar();
	GregorianCalendar temp_cal = new GregorianCalendar();
	String startDate = "", endDate="";
	String today = Helper.getToday();
		
	//
	if(week){
	    //
	    // find the start of the week
	    temp_cal.add(Calendar.DATE, -7);
	    //
	    startDate = (temp_cal.get(Calendar.MONTH)+1)+"/"+
		temp_cal.get(Calendar.DATE)+"/"+
		temp_cal.get(Calendar.YEAR);
	    endDate = today;
	}
	if(month){
	    // find the start of the month
	    temp_cal.add(Calendar.MONTH, -1);
	    startDate = (temp_cal.get(Calendar.MONTH)+1)+"/"+
		temp_cal.get(Calendar.DATE)+"/"+
		temp_cal.get(Calendar.YEAR);
	    endDate = today;
	}
	if(period2){
	    //
	    // if only year is set
	    if(!req_from.equals("") && req_to.equals("")){
		req_to = today;
	    }
	    if(!req_from.equals(""))
		startDate = req_from;
	    if(!req_to.equals(""))
		endDate = req_to;
	}
	out.println("<html><head><title>Report</title></head><body>");
	if(report.startsWith("stat")){
	    //
	    // Statistics report
	    //
	    out.println("<center><h3>Risk Reports</h3>");
	    if(!tortFullName.equals(""))
		out.println("<h4>"+tortFullName+"</h4>");
	    if(!legalFullName.equals("") && 
	       !legalFullName.equals(tortFullName))
		out.println("<h4>"+legalFullName+"</h4>");
	    if(!safetyFullName.equals("") &&
	       !safetyFullName.equals(legalFullName) &&
	       !safetyFullName.equals(tortFullName))
		out.println("<h4>"+safetyFullName+"</h4>");
	    //
	    // statistics for all periods
	    //
	    String qw = "", str="",str2="", all="";
	    double t1=0, t2=0, t3=0, t4=0, t5=0, t6=0,
		d=0, d2=0, d3=0, d4=0, d5=0, d6=0;
	    String qc = "select count(*), t.typeDesc type from tortClaims v left join riskUnifiedTypes t on t.type=v.type ";
	    String qq = "", q = " select count(*),"+
		"t.typeDesc type,"+
		"round(sum(v.requestAmount),2),"+
		"round(sum(v.paidByCity),2),"+
		"round(sum(v.paidByRisk),2),"+
		"round(sum(v.miscByCity),2),"+
		"round(sum(v.paidByInsur),2) "+
		" from tortClaims v left join riskUnifiedTypes t on t.type=v.type ";
	    int cnt = 0, total = 0, s1=0, s2=0;
	    // qw = " v.type = t.type ";
	    if(!startDate.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " str_to_date('"+startDate+
		    "','%m/%d/%Y') <= v.incidentDate ";
	    }
	    if(!endDate.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " str_to_date('"+endDate+
		    "','%m/%d/%Y') >= v.incidentDate ";
	    }
	    if(!dept.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " (e.dept_id = "+dept+" or dr.dept_id="+dept+") ";														empTbl = true;
	    }
	    if(!tortEmpid.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " e.userid like '"+tortEmpid+"'";
		empTbl = true;
	    }
	    if(empTbl){
		qc += " left join empRelated er on er.risk_id=v.id left join employees e on e.id = er.employee_id left join deptRelated dr on dr.related_id=v.id ";
		q += " left join empRelated er on er.risk_id=v.id left join employees e on e.id = er.employee_id left join deptRelated dr on dr.related_id=v.id ";
	    }
	    if(!qw.equals("")){
		qc += " where "+qw;
		q += " where "+qw;
	    }
	    q += " group by type order by type";
	    qq = qc;
	    if(debug){
		logger.debug(qc);
	    }
	    try{
		if(tort){
		    rs = stmt.executeQuery(qc);
		    if(rs.next()){
			cnt = rs.getInt(1);
		    }
		    if(cnt == 0){
			all += " No match found for Tort Claims <br /><br />";
		    }
		    else{
			qq = q;
			if(debug){
			    logger.debug(qq);
			}
			rs = stmt.executeQuery(qq);
			all += " <table border width='95%'>"+
			    "<caption>Tort Claims by Type </caption>\n";
			all += " <tr><td><b>Type</b></td>"+
			    "<td align='right'><b>Count</b></td>"+
			    "<td align='right'>%</td>"+
			    "<td align='right'>Requested </td>"+
			    "<td align='right'>Paid By City </td>"+
			    "<td align='right'>Paid By Risk </td>"+
			    "<td align='right'>Misc Paid By City </td>"+
			    "<td align='right'>Paid By Insurance </td>"+
			    "</tr>";
			while(rs.next()){ 
			    s1 = rs.getInt(1);
			    str = rs.getString(2);
			    d = rs.getDouble(3);
			    d2 = rs.getDouble(4);
			    d3 = rs.getDouble(5);
			    d4 = rs.getDouble(6);
			    d5 = rs.getDouble(7);
			    t1 = t1 +d;
			    t2 = t2 +d2;
			    t3 = t3 +d3;
			    t4 = t4 +d4;
			    t5 = t5 +d5;
			    if(str == null) str = "Unspecified";
			    else if(str.equals("")) str = "Unspecified";
			    all += "<tr><td>"+str+
				"</td><td align=right>"+s1+
				"</td><td align=right>"+
				Helper.findPercent(s1,cnt)+
				"</td><td align=right>"+
				format.format(d)+
				"</td><td align=right>"+
				format.format(d2)+
				"</td><td align=right>"+
				format.format(d3)+
				"</td><td align=right>"+
				format.format(d4)+
				"</td><td align=right>"+
				format.format(d5)+
				"</td></tr>";
			}
			all += "<tr><td>Total</td><td align=right>"+cnt+
			    "</td><td>&nbsp;"+
			    "</td><td align='right'>"+format.format(t1)+
			    "</td><td align='right'>"+format.format(t2)+
			    "</td><td align='right'>"+format.format(t3)+
			    "</td><td align='right'>"+format.format(t4)+
			    "</td><td align='right'>"+format.format(t5)+"</td></tr>";
			all += "</table><br><br>\n";
		    }
		    //
		    qq = " select count(*),"+
			"concat_ws(', ',d.name,d.division) dp,"+
			" t.typeDesc type,"+
			"round(sum(v.requestAmount),2),"+
			"round(sum(v.paidByCity),2),"+
			"round(sum(v.paidByRisk),2),"+
			"round(sum(v.miscByCity),2),"+
			"round(sum(v.paidByInsur),2) "+
			" from tortClaims v "+
			" left join riskUnifiedTypes t on v.type=t.type "+
			" left join empRelated er on v.id=er.risk_id "+
			" left join deptRelated dr on dr.related_id=v.id "+
			" left join employees e on e.id=er.employee_id "+
			" left join departments d on d.id=e.dept_id ";
										
		    s1=0;t1=0;t2=0;t3=0;t4=0;t5=0;

		    if(!startDate.equals("")){
			if(!qw.equals("")) qw += " and ";
			qw += " str_to_date('"+startDate+
			    "','%m/%d/%Y') <= v.incidentDate ";
		    }
		    if(!endDate.equals("")){
			if(!qw.equals("")) qw += " and ";						
			qw += " str_to_date('"+endDate+
			    "','%m/%d/%Y') >= v.incidentDate ";
		    }
		    if(!law_firm_id.equals("")){
			if(!qw.equals("")) qw += " and ";
			qw += " v.law_firm_id="+law_firm_id;
		    }												
		    if(!dept.equals("")){
			if(!qw.equals("")) qw += " and ";
			qw += " (e.dept = "+dept+" or dr.dept_id="+dept+") ";																										
		    }
		    if(!tortEmpid.equals("")){
			if(!qw.equals("")) qw += " and ";						
			qw += " e.userid like '"+tortEmpid+"'";
		    }
		    if(!qw.equals("")){
			qq += " where "+qw;
		    }
		    qq += " group by dp, type order by dp, type ";
		    if(cnt == 0){
			all += " No match found for Tort Claims <br /><br />";
		    }
		    else{
			if(debug){
			    logger.debug(qq);
			}
			rs = stmt.executeQuery(qq);
			all += " <table border width='95%'>"+
			    "<caption>Tort Claims by Department</caption>\n";
			all += " <tr>"+
			    "<td><b>Dept</b></td>"+
			    "<td><b>Type</b></td>"+
			    "<td align='right'><b>Count</b></td>"+
			    "<td align='right'>%</td>"+
			    "<td align='right'>Requested </td>"+
			    "<td align='right'>Paid By City </td>"+
			    "<td align='right'>Paid By Risk </td>"+
			    "<td align='right'>Misc Paid By City </td>"+
			    "<td align='right'>Paid By Insurance </td>"+
			    "</tr>";
			while(rs.next()){ 
			    s1 = rs.getInt(1);
			    str = rs.getString(2);
			    str2 = rs.getString(3);
			    d = rs.getDouble(4);
			    d2 = rs.getDouble(5);
			    d3 = rs.getDouble(6);
			    d4 = rs.getDouble(7);
			    d5 = rs.getDouble(8);
			    t1 = t1 +d;
			    t2 = t2 +d2;
			    t3 = t3 +d3;
			    t4 = t4 +d4;
			    t5 = t5 +d5;
			    if(str == null) str = "Unspecified";
			    else if(str.trim().equals("")) str = "Unspecified";
			    if(str2 == null) str2 = "Unspecified";
			    else if(str2.equals("")) str2 = "Unspecified";
			    // 
			    // dept
			    //
			    all += "<tr>"+
				"<td>"+str+"</td>"+
				"<td>"+str2+
				"</td><td align=right>"+s1+
				"</td><td align=right>"+
				Helper.findPercent(s1,cnt)+
				"</td><td align=right>"+
				format.format(d)+
				"</td><td align=right>"+
				format.format(d2)+
				"</td><td align=right>"+
				format.format(d3)+
				"</td><td align=right>"+
				format.format(d4)+
				"</td><td align=right>"+
				format.format(d5)+
				"</td></tr>";
			}
			all += "<tr><td>Total</td>"+
			    "<td>&nbsp;</td>"+							
			    "<td align=right>"+cnt+
			    "</td><td>&nbsp;"+
			    "</td><td align='right'>"+format.format(t1)+
			    "</td><td align='right'>"+format.format(t2)+
			    "</td><td align='right'>"+format.format(t3)+
			    "</td><td align='right'>"+format.format(t4)+
			    "</td><td align='right'>"+format.format(t5)+
			    "</td></tr>";
			all += "</table><br><br>\n";
		    }
		    //
		    // Tort claims by Status
		    //
		    t1=0;t2=0;t3=0;t4=0;t5=0;
		    qq = " select count(*),"+
			"status,"+
			"round(sum(requestAmount),2),"+
			"round(sum(paidByCity),2),"+
			"round(sum(paidByRisk),2),"+
			"round(sum(miscByCity),2),"+
			"round(sum(paidByInsur),2) "+
			" from tortClaims  v ";
		    s1=0; qw = "";
		    if(!startDate.equals("")){
						
			qw = " str_to_date('"+startDate+
			    "','%m/%d/%Y') <= incidentDate ";
		    }
		    if(!endDate.equals("")){
			if(!qw.equals("")) qw += " and ";
			qw += " str_to_date('"+endDate+
			    "','%m/%d/%Y') >= incidentDate ";
		    }
		    if(!dept.equals("") || !tortEmpid.equals("")){
			qq += " left join empRelated er on er.risk_id=v.id left join employees e on e.id=er.employee_id left join deptRelated dr on v.id=dr.related_id ";
			if(!dept.equals("")){
			    if(!qw.equals("")) qw += " and ";
			    qw += " (e.dept_id="+dept+" or dr.dept_id="+dept+")";
			}
			if(!tortEmpid.equals("")){
			    if(!qw.equals("")) qw += " and ";
			    qw += " e.userid like '"+tortEmpid+"'";
			}
		    }
		    if(!qw.equals("")){
			qq += " where "+qw;
		    }
		    qq += " group by status ";
		    if(cnt == 0){
			all += " No match found for Tort Claims <br /><br />";
		    }
		    else{
			if(debug){
			    logger.debug(qq);
			}
			rs = stmt.executeQuery(qq);
			all += " <table border width='95%'>"+
			    "<caption>Tort Claims by Status </caption>\n";
			all += " <tr><td><b>Status</b></td>"+
			    "<td align='right'><b>Count</b></td>"+
			    "<td align='right'>%</td>"+
			    "<td align='right'>Requested </td>"+
			    "<td align='right'>Paid By City </td>"+
			    "<td align='right'>Paid By Risk </td>"+
			    "<td align='right'>Misc Paid By City </td>"+
			    "<td align='right'>Paid By Insurance </td>"+
			    "</tr>";
			while(rs.next()){ 
			    s1 = rs.getInt(1);
			    str = rs.getString(2);
			    d = rs.getDouble(3);
			    d2 = rs.getDouble(4);
			    d3 = rs.getDouble(5);
			    d4 = rs.getDouble(6);
			    d5 = rs.getDouble(7);
			    t1 = t1 +d;
			    t2 = t2 +d2;
			    t3 = t3 +d3;
			    t4 = t4 +d4;
			    t5 = t5 +d5;
			    if(str == null || str.equals("")) str = "&nbsp";
			    all += "<tr><td>"+str+
				"</td><td align=right>"+s1+
				"</td><td align=right>"+
				Helper.findPercent(s1,cnt)+
				"</td><td align=right>"+
				format.format(d)+
				"</td><td align=right>"+
				format.format(d2)+
				"</td><td align=right>"+
				format.format(d3)+
				"</td><td align=right>"+
				format.format(d4)+
				"</td><td align=right>"+
				format.format(d5)+
				"</td></tr>";
			}
			all += "<tr><td>Total</td>"+
			    "<td align=right>"+cnt+"</td><td>&nbsp;"+
			    "</td><td align='right'>"+format.format(t1)+
			    "</td><td align='right'>"+format.format(t2)+
			    "</td><td align='right'>"+format.format(t3)+
			    "</td><td align='right'>"+format.format(t4)+
			    "</td><td align='right'>"+format.format(t5)+
			    "</td></tr>";
			all += "</table><br><br>\n";
		    }
		    //
		    // tort claim by insurance status
		    //
		    t1=0;t2=0;t3=0;t4=0;t5=0;
		    qq = " select count(*),"+
			"i.policy,i.status,"+
			"round(sum(v.requestAmount),2),"+
			"round(sum(v.paidByCity),2),"+
			"round(sum(v.paidByRisk),2),"+
			"round(sum(v.miscByCity),2),"+
			"round(sum(i.amountPaid),2) "+
			" from tortClaims v "+
			" left join insuranceRelated ir on v.id=ir.risk_id "+
			" left join insurances i on i.id=ir.insurance_id ";
		    s1=0; qw = "";
		    if(!startDate.equals("")){
			qw += " str_to_date('"+startDate+
			    "','%m/%d/%Y') <= v.incidentDate ";
		    }
		    if(!endDate.equals("")){
			if(!qw.equals("")) qw += " and ";
			qw += " str_to_date('"+endDate+
			    "','%m/%d/%Y') >= v.incidentDate ";
		    }
		    if(!dept.equals("") || !tortEmpid.equals("")){
			qq += "left join empRelated er on v.id=er.risk_id ";
			qq += "left join employees e on er.employee_id=e.id ";
			qq += "left join deptRelated dr on v.id=dr.related_id ";												
			if(!dept.equals("")){
			    if(!qw.equals("")) qw += " and ";
			    qw += " (e.dept_id="+dept+" or dr.dept_id="+dept+")";														
			}
			if(!tortEmpid.equals("")){
			    if(!qw.equals("")) qw += " and ";		
			    qw += " e.userid like '"+tortEmpid+"'";
			}
		    }
		    if(!qw.equals(""))
			qq += " where "+qw;
		    qq += " group by i.policy,i.status ";
		    if(cnt == 0){
			all += " No match found for Tort Claims <br /><br />";
		    }
		    else{
			if(debug){
			    logger.debug(qq);
			}
			rs = stmt.executeQuery(qq);
			all += " <table border width='95%'>"+
			    "<caption>Tort Claims by Policy & Insurance Status "+
			    "</caption>\n";
			all += " <tr><td><b>Policy</b></td>"+
			    "<td><b>Insur. Status</b></td>"+
			    "<td align='right'><b>Count</b></td>"+
			    "<td align='right'>%</td>"+
			    "<td align='right'>Requested </td>"+
			    "<td align='right'>Paid By City </td>"+
			    "<td align='right'>Paid By Risk </td>"+
			    "<td align='right'>Misc Paid By City </td>"+
			    "<td align='right'>Paid By Insurance </td>"+
			    "</tr>";
			while(rs.next()){ 
			    s1 = rs.getInt(1);
			    str = rs.getString(2);
			    str2 = rs.getString(3);
			    d = rs.getDouble(4);
			    d2 = rs.getDouble(5);
			    d3 = rs.getDouble(6);
			    d4 = rs.getDouble(7);
			    d5 = rs.getDouble(8);
			    t1 = t1 +d;
			    t2 = t2 +d2;
			    t3 = t3 +d3;
			    t4 = t4 +d4;
			    t5 = t5 +d5;
			    if(str == null || str.equals("")) str = "Unspecified";
			    if(str2 == null || str2.equals("")) str2 = "Open";
			    all += "<tr><td>"+str+
				"</td><td>"+str2+
				"</td><td align=right>"+s1+
				"</td><td align=right>"+
				Helper.findPercent(s1, cnt)+
				"</td><td align=right>"+
				format.format(d)+
				"</td><td align=right>"+
				format.format(d2)+
				"</td><td align=right>"+
				format.format(d3)+
				"</td><td align=right>"+
				format.format(d4)+
				"</td><td align=right>"+
				format.format(d5)+
				"</td></tr>";
			}
			all += "</table><br><br>\n";
		    }
		    //
		    // potential claims
		    //
		    t1=0;t2=0;t3=0;t4=0;
		    qc = " select count(*) from tortClaims v left join riskUnifiedTypes t on v.type=t.type ";
		    q = " select count(*),t.typeDesc type "+
												
			" from tortClaims v left join riskUnifiedTypes t on v.type=t.type ";
		    s1=0; qw = "";
		    qw += " str_to_date('"+today+
			"','%m/%d/%Y') <= v.expires and v.filed is null "+
			" and v.subInsur is null";
		    if(!dept.equals("") || !tortEmpid.equals("") ){
			qc += " left join empRelated er on v.id=er.risk_id ";
			qc += " left join employees e on er.employee_id=e.id ";
			qc += " left join deptRelated dr on v.id=dr.related_id ";
												
			q += " left join empRelated er on v.id=er.risk_id ";
			q += " left join employees e on er.employee_id=e.id ";
			q += " left join deptRelated dr on v.id=dr.related_id ";
			if(!dept.equals("")){
			    if(!qw.equals("")) qw += " and ";
			    qw += " (e.dept_id="+dept+" or dr.dept_id="+dept+")";
			}
			if(!tortEmpid.equals("")){
			    if(!qw.equals("")) qw += " and ";
			    qw += " e.userid like '"+tortEmpid+"'";
			}
		    }
		    if(!qw.equals("")){
			qc += " where "+qw;
			q += " where "+qw;
		    }
		    q += " group by type order by type ";
		    qq = qc;
		    if(debug){
			logger.debug(qc);
		    }
		    rs = stmt.executeQuery(qc);
		    if(rs.next()){
			cnt = rs.getInt(1);
		    }
		    if(cnt == 0){
			all += " No match found for Potential Claims <br /><br />";
		    }
		    else{
			qq = q;
			if(debug){
			    logger.debug(qq);
			}
			rs = stmt.executeQuery(qq);
			all += " <table border width='80%'>"+
			    "<caption>Potential Tort Claims by Type </caption>\n";
			all += " <tr><td><b>Type</b></td>"+
			    "<td align='right'><b>Count</b></td>"+
			    "<td align='right'>%</td>"+
			    "</tr>";
			while(rs.next()){ 
			    s1 = rs.getInt(1);
			    str = rs.getString(2);
			    if(str == null) str = "Unspecified";
			    else if(str.equals("")) str = "Unspecified";
			    all += "<tr><td>"+str+
				"</td><td align=right>"+s1+
				"</td><td align=right>"+
				Helper.findPercent(s1, cnt)+
				"</td></tr>";
			}
			all += "<tr><td>Total</td><td align=right>"+cnt+
			    "</td><td>&nbsp;"+
			    "</td></tr>";
			all += "</table><br><br>\n";
		    }
		}
		//
		// Recovey Action
		//
		if(legal){
		    qw = ""; str="";str2="";
		    qc = "select count(*) from vslegals v "+
			" left join riskUnifiedTypes t on v.type=t.type "+
			" left join insuranceRelated ir on v.id=ir.risk_id "+
			" left join insurances i on ir.insurance_id=i.id ";
										
		    q = " select count(*),t.typeDesc type,round(sum(v.damageAmnt),2),"+
			" round(sum(v.paidByCity),2),"+
			" round(sum(v.paidByRisk),2),"+
			" round(sum(v.miscByCity),2),"+
			" round(sum(i.amountPaid),2) "+
			" from vslegals v "+
			" left join riskUnifiedTypes t on v.type=t.type "+
			" left join insuranceRelated ir on v.id=ir.risk_id "+
			" left join insurances i on ir.insurance_id=i.id ";
										
		    cnt = 0; total = 0; s1=0; s2=0;
		    d=0; t1=0;t2=0;t3=0;t4=0;t5=0;
		    // qw += " and v.id = ir.id2 and i.id=ir.id ";
		    if(!startDate.equals("")){
			if(!qw.equals("")) qw += " and ";
			qw += " str_to_date('"+startDate+
			    "','%m/%d/%Y') <= v.doi ";
		    }
		    if(!endDate.equals("")){
			if(!qw.equals("")) qw += " and ";			
			qw += " str_to_date('"+endDate+
			    "','%m/%d/%Y') >= v.doi ";
		    }
		    if(!dept.equals("") || !legalEmpid.equals("")){
			qc += " left join empRelated er on er.risk_id=v.id left join employees e on e.id = er.employee_id left join deptRelated dr on dr.related_id=v.id ";
			q += " left join empRelated er on er.risk_id=v.id left join employees e on e.id = er.employee_id left join deptRelated dr on dr.related_id=v.id ";												
			if(!dept.equals("")){
			    if(!qw.equals("")) qw += " and ";						
			    qw += " (e.dept_id = "+dept+" or dr.dept_id="+dept+") ";																	

			}
			if(!legalEmpid.equals("")){
			    if(!qw.equals("")) qw += " and ";
			    qw += " e.userid like '"+legalEmpid+"'";
			}
		    }
		    if(!qw.equals("")){
			qc += " where "+qw;
			q += " where "+qw;
		    }
		    q += " group by type order by type ";
		    qq = qc;
		    if(debug){
			logger.debug(qc);
		    }
		    rs = stmt.executeQuery(qc);
		    if(rs.next()){
			cnt = rs.getInt(1);
		    }
		    if(cnt == 0){
			all += " No match found for Recovery Actions.<br /><br />";
		    }
		    else{
			qq = q;
			if(debug){
			    logger.debug(qq);
			}
			rs = stmt.executeQuery(qq);
			all += " <table border width='95%'>"+
			    "<caption>Recovery Action by Type </caption>\n";
			all += " <tr><td><b>Type</b></td>"+
			    "<td align='right'><b>Count</b></td>"+
			    "<td align='right'><b>%</b></td>"+
			    "<td align='right'><b>Damage</b></td>"+
			    "<td align='right'><b>Paid By City</b></td>"+
			    "<td align='right'><b>Paid By Risk</b></td>"+
			    "<td align='right'><b>Misc By City</b></td>"+
			    "<td align='right'><b>Paid By Insurance</b></td>"+
			    "</tr>";
			while(rs.next()){ 
			    s1 = rs.getInt(1);
			    str = rs.getString(2);
			    d = rs.getDouble(3);
			    t1 = t1 +d;
			    d2 = rs.getDouble(4);
			    t2 = t2 +d2;
			    d3 = rs.getDouble(5);
			    t3 = t3 +d3;
			    d4 = rs.getDouble(6);
			    t4 = t4 +d4;
			    d5 = rs.getDouble(7);
			    t5 = t5 +d5;
			    if(str == null) str = "Unspecified";
			    else if(str.trim().equals("")) str = "Unspecified";
			    all += "<tr><td>"+str+
				"</td><td align=right>"+s1+
				"</td><td align=right>"+
				Helper.findPercent(s1, cnt)+
				"</td><td align=right>"+
				format.format(d)+
				"</td><td align=right>"+
				format.format(d2)+
				"</td><td align=right>"+
				format.format(d3)+
				"</td><td align=right>"+
				format.format(d4)+
				"</td><td align=right>"+
				format.format(d5)+
				"</td></tr>";
			}
			all += "<tr><td>Total</td><td align=right>"+cnt+
			    "</td><td>&nbsp;"+
			    "</td><td align=right>"+
			    format.format(t1)+
			    "</td><td align=right>"+
			    format.format(t2)+
			    "</td><td align=right>"+
			    format.format(t3)+
			    "</td><td align=right>"+
			    format.format(t4)+
			    "</td><td align=right>"+
			    format.format(t5)+
			    "</td></tr>";
			all += "</table><br><br>\n";
		    }
		    //
		    // Recovery action by dept
		    //
		    qq = " select count(*),"+
			" concat_ws(', ',d.name,d.division) dp,"+
			" round(sum(v.damageAmnt),2),"+
			" round(sum(v.paidByCity),2),"+
			" round(sum(v.paidByRisk),2),"+
			" round(sum(v.miscByCity),2),"+
			" round(sum(i.amountPaid),2) "+
			" from vslegals v "+
			" left join insuranceRelated ir on v.id=ir.risk_id "+	
			" left join insurances i on ir.insurance_id=i.id "+
			" left join empRelated er on v.id=er.risk_id "+				
			" left join employees e on er.employee_id=e.id "+
			" left join deptRelated dr on dr.related_id=v.id "+
			" left join departments d on e.dept_id=d.id or dr.dept_id=d.id";

		    qw="";
		    s1=0; s2=0;
		    d=0; t1=0;t2=0;t3=0;t4=0;t5=0;
		    if(!startDate.equals("")){
			if(!qw.equals("")) qw += " and ";						
			qw += " str_to_date('"+startDate+
			    "','%m/%d/%Y') <= v.doi ";
		    }
		    if(!endDate.equals("")){
			if(!qw.equals("")) qw += " and ";
			qw += " str_to_date('"+endDate+
			    "','%m/%d/%Y') >= v.doi ";
		    }
		    if(!dept.equals("")){
			if(!qw.equals("")) qw += " and ";						
			qw += " (e.dept_id = "+dept+" or dr.dept_id="+dept+") ";
		    }					
		    if(!legalEmpid.equals("")){
			if(!qw.equals("")) qw += " and ";						
			qw += "  e.userid like '"+legalEmpid+"'";
		    }
		    if(!qw.equals("")) qq += " where "+qw;					
		    qq += " group by dp order by dp";
		    if(cnt == 0){
			all += " No match found for Recovery Actions.<br /><br />";
		    }
		    else{
			if(debug){
			    logger.debug(qq);
			}
			rs = stmt.executeQuery(qq);
			all += " <table border width='95%'>"+
			    "<caption>Recovery Action by Department </caption>\n";
			all += " <tr><td><b>Dept.</b></td>"+
			    "<td align='right'><b>Count</b></td>"+
			    "<td align='right'><b>%</b></td>"+
			    "<td align='right'><b>Damage</b></td>"+
			    "<td align='right'><b>Paid By City</b></td>"+
			    "<td align='right'><b>Paid By Risk</b></td>"+
			    "<td align='right'><b>Misc By City</b></td>"+
			    "<td align='right'><b>Paid By Insurance</b></td>"+
			    "</tr>";
			while(rs.next()){ 
			    s1 = rs.getInt(1);
			    str = rs.getString(2);
			    d = rs.getDouble(3);
			    t1 = t1 +d;
			    d2 = rs.getDouble(4);
			    t2 = t2 +d2;
			    d3 = rs.getDouble(5);
			    t3 = t3 +d3;
			    d4 = rs.getDouble(6);
			    t4 = t4 +d4;
			    d5 = rs.getDouble(7);
			    t5 = t5 +d5;
			    if(str == null) str = "&nbsp";
			    else if(str.trim().equals("")) str = "Unspecified";
			    all += "<tr><td>"+str+
				"</td><td align=right>"+s1+
				"</td><td align=right>"+
				Helper.findPercent(s1,cnt)+
				"</td><td align=right>"+
				format.format(d)+
				"</td><td align=right>"+
				format.format(d2)+
				"</td><td align=right>"+
				format.format(d3)+
				"</td><td align=right>"+
				format.format(d4)+
				"</td><td align=right>"+
				format.format(d5)+
				"</td></tr>";
			}
			all += "<tr><td>Total</td><td align=right>"+cnt+
			    "</td><td>&nbsp;"+
			    "</td><td align=right>"+
			    format.format(t1)+
			    "</td><td align=right>"+
			    format.format(t2)+
			    "</td><td align=right>"+
			    format.format(t3)+
			    "</td><td align=right>"+
			    format.format(t4)+
			    "</td><td align=right>"+
			    format.format(t5)+
			    "</td></tr>";
			all += "</table><br><br>\n";
		    }
		}
		//
		// Internal accidents
		//
		if(intern){
		    qw = ""; str="";str2="";
		    qc = "select count(*) from riskSafety v left join riskUnifiedTypes t on t.type=v.type "+
			" left join insuranceRelated ir on v.id=ir.risk_id left join insurances i on i.id=ir.insurance_id ";										
		    q = " select count(*),t.typeDesc type,"+
			" round(sum(v.totalCost),2),"+
			" round(sum(v.totalCostP),2), "+
			" round(sum(v.paidByCity),2),"+
			" round(sum(v.paidByRisk),2),"+
			" round(sum(v.miscByCity),2),"+
			" round(sum(i.amountPaid),2) "+
			" from riskSafety v left join riskUnifiedTypes t on t.type=v.type "+
			" left join insuranceRelated ir on v.id=ir.risk_id left join insurances i on i.id=ir.insurance_id ";
		    cnt = 0; total = 0; s1=0; s2=0;
		    d=0; d2=0; t1=0; t2=0; t3=0; t4=0; d5=0; t5=0;
		    //
		    if(!startDate.equals("")){
			if(!qw.equals("")) qw += " and ";
			qw += " str_to_date('"+startDate+
			    "','%m/%d/%Y') <= accidDate ";
		    }
		    if(!endDate.equals("")){
			if(!qw.equals("")) qw += " and ";
			qw += " str_to_date('"+endDate+
			    "','%m/%d/%Y') >= accidDate ";
		    }
		    if(!dept.equals("") || safetyEmpid.equals("")){

			qc += " left join empRelated er on er.risk_id=v.id left join employees e on e.id=er.employee_id ";
			q += " left join empRelated er on er.risk_id=v.id left join employees e on e.id=er.employee_id ";
			qc += " left join deptRelated dr on dr.related_id=v.id ";
			q += " left join deptRelated dr on dr.related_id=v.id ";																										
			if(!dept.equals("")){
			    if(!qw.equals("")) qw += " and ";						
			    qw += " (e.dept_id="+dept+" or dr.dept_id="+dept+")";
														
			}
			if(!safetyEmpid.equals("")){
			    if(!qw.equals("")) qw += " and ";
			    qw += " (v.empid like '"+safetyEmpid+"' or e.userid like '"+safetyEmpid+"')";
			}
		    }
		    if(!qw.equals("")){
			qc += " where "+qw;
			q += " where "+qw;
		    }
		    q += " group by type order by type";
		    qq = qc;
		    if(debug){
			logger.debug(qc);
		    }
		    rs = stmt.executeQuery(qc);
		    if(rs.next()){
			cnt = rs.getInt(1);
		    }
		    if(cnt == 0){
			all += " No match found for Internal Accident.<br /><br />";
		    }
		    else{
			qq = q;
			if(debug){
			    logger.debug(qq);
			}
			rs = stmt.executeQuery(qq);
			all += " <table border width='95%'>"+
			    "<caption>Internal Accident by Type </caption>\n";
			all += " <tr><td><b>Type</b></td>"+
			    "<td align='right'><b>Count</b></td>"+
			    "<td align='right'><b>%</b></td>"+
			    "<td align='right'><b>Auto. Damage</b></td>"+
			    "<td align='right'><b>Prop. Damage</b></td>"+
			    "<td align='right'><b>Paid By City</b></td>"+
			    "<td align='right'><b>Paid By Risk</b></td>"+
			    "<td align='right'><b>Misc By City</b></td>"+
			    "<td align='right'><b>Paid By Insurance</b></td>"+
			    "</tr>";
			while(rs.next()){ 
			    s1 = rs.getInt(1);
			    str = rs.getString(2);
			    d = rs.getDouble(3);
			    d2 = rs.getDouble(4);
			    d3 = rs.getDouble(5);
			    d4 = rs.getDouble(6);
			    d5 = rs.getDouble(7);
			    d6 = rs.getDouble(8);
			    t1 = t1 +d;
			    t2 = t2 +d2;
			    t3 = t3 +d3;
			    t4 = t4 +d4;
			    t5 = t5 +d5;
			    t6 = t6 +d6;
			    if(str == null) str = "Unspecified";
			    else if(str.equals("")) str = "Unspecified";
			    all += "<tr><td>"+str+
				"</td><td align=right>"+s1+
				"</td><td align=right>"+
				Helper.findPercent(s1, cnt)+
				"</td><td align=right>"+
				format.format(d)+
				"</td><td align=right>"+
				format.format(d2)+
				"</td><td align=right>"+
				format.format(d3)+
				"</td><td align=right>"+
				format.format(d4)+
				"</td><td align=right>"+
				format.format(d5)+
				"</td><td align=right>"+
				format.format(d6)+
				"</td></tr>";
			}
			all += "<tr><td>Total</td><td align=right>"+cnt+
			    "</td><td>&nbsp;"+
			    "</td><td align=right>"+
			    format.format(t1)+
			    "</td><td align=right>"+
			    format.format(t2)+
			    "</td><td align=right>"+
			    format.format(t3)+
			    "</td><td align=right>"+
			    format.format(t4)+
			    "</td><td align=right>"+
			    format.format(t5)+
			    "</td><td align=right>"+
			    format.format(t6)+
			    "</td></tr>";
			all += "</table><br><br>\n";
			//
			// Internal accident by dept
			//
			qq = " select count(*),"+
			    " concat_ws(', ',d.name,d.division) dp,"+
			    " round(sum(totalCost),2),"+
			    " round(sum(totalCostP),2), "+
			    " round(sum(v.paidByCity),2),"+
			    " round(sum(v.paidByRisk),2),"+
			    " round(sum(v.miscByCity),2),"+
			    " round(sum(i.amountPaid),2) "+
														
			    " from riskSafety v "+
			    " left join empRelated er on er.risk_id=v.id left join employees e on e.id=er.employee_id "+
			    " left join deptRelated dr on dr.related_id=v.id "+												
			    " left join departments d on d.id=e.dept_id or d.id=dr.dept_id "+
			    " left join insuranceRelated ir on ir.risk_id=v.id "+
			    " left join insurances i on i.id=ir.insurance_id ";
			s1=0; s2=0;
			d=0; t1=0;t2=0;t3=0;t4=0;t5=0;t6=0;
			if(!startDate.equals("")){
			    if(!qw.equals("")) qw += " and "; 
			    qw += " str_to_date('"+startDate+
				"','%m/%d/%Y') <= v.accidDate ";
			}
			if(!endDate.equals("")){
			    if(!qw.equals("")) qw += " and ";
			    qw += " str_to_date('"+endDate+
				"','%m/%d/%Y') >= v.accidDate ";
			}
			if(!dept.equals("")){
			    if(!qw.equals("")) qw += " and ";
			    qw += " d.id="+dept;
			}
			if(!safetyEmpid.equals("")){
			    if(!qw.equals("")) qw += " and ";
			    qw += " e.userid like '"+safetyEmpid+"'";
			}
			if(!qw.equals("")){
			    qc += " where "+qw;
			    qq += " where "+qw;
			}
			qq += " group by dp order by dp";
			all += " <table border width='95%'>"+
			    "<caption>Internal Accident by Dept </caption>\n";
			all += " <tr><td><b>Dept</b></td>"+
			    "<td align='right'><b>Count</b></td>"+
			    "<td align='right'><b>%</b></td>"+
			    "<td align='right'><b>Auto. Damage</b></td>"+
			    "<td align='right'><b>Prop. Damage</b></td>"+
			    "<td align='right'><b>Paid By City</b></td>"+
			    "<td align='right'><b>Paid By Risk</b></td>"+
			    "<td align='right'><b>Misc By City</b></td>"+
			    "<td align='right'><b>Paid By Insurance</b></td>"+
			    "</tr>";
			if(debug){
			    logger.debug(qq);
			}
			rs = stmt.executeQuery(qq);
			while(rs.next()){ 
			    s1 = rs.getInt(1);
			    str = rs.getString(2);
			    d = rs.getDouble(3);
			    d2 = rs.getDouble(4);
			    d3 = rs.getDouble(5);
			    d4 = rs.getDouble(6);
			    d5 = rs.getDouble(7);
			    d6 = rs.getDouble(8);
			    t1 = t1 +d;
			    t2 = t2 +d2;
			    t3 = t3 +d3;
			    t4 = t4 +d4;
			    t5 = t5 +d5;
			    t6 = t6 +d6;
			    if(str == null || 
			       str.equals(", ") ||
			       str.trim().equals("")) str = "Unspecified";
			    all += "<tr><td>"+str+
				"</td><td align=right>"+s1+
				"</td><td align=right>"+
				Helper.findPercent(s1, cnt)+
				"</td><td align=right>"+
				format.format(d)+
				"</td><td align=right>"+
				format.format(d2)+
				"</td><td align=right>"+
				format.format(d3)+
				"</td><td align=right>"+
				format.format(d4)+
				"</td><td align=right>"+
				format.format(d5)+
				"</td><td align=right>"+
				format.format(d6)+
				"</td></tr>";
			}
			all += "<tr><td>Total</td><td align=right>"+cnt+
			    "</td><td>&nbsp;"+
			    "</td><td align=right>"+
			    format.format(t1)+
			    "</td><td align=right>"+
			    format.format(t2)+
			    "</td><td align=right>"+
			    format.format(t3)+
			    "</td><td align=right>"+
			    format.format(t4)+
			    "</td><td align=right>"+
			    format.format(t5)+
			    "</td><td align=right>"+
			    format.format(t6)+
			    "</td></tr>";
			all += "</table><br><br>\n";
		    }
		}
		if(wcomp){
		    qw = ""; str="";str2="";
		    cnt = 0; s1=0; s2=0;
		    d=0; d2=0; t1=0; t2=0; t3=0;t4=0;
		    qc = "select count(*) from workerComps v ";
		    q = " select count(*),status,"+
			" round(sum(payTtd),2),"+
			" round(sum(payPpi),2), "+
			" round(sum(payMed),2) "+
			" from workerComps v ";
		    if(!startDate.equals("")){
			qw = " str_to_date('"+startDate+
			    "','%m/%d/%Y') <= accidentDate ";
		    }
		    if(!endDate.equals("")){
			if(!qw.equals("")) qw += " and ";
			qw += " str_to_date('"+endDate+
			    "','%m/%d/%Y') >= accidentDate ";
		    }
		    if(!dept.equals("")){
			if(!qw.equals("")) qw += " and ";												
			qw += " dept="+dept;
			qc += ", employees e, empRelated er ";
			q += ", employees e, empRelated er ";
			if(!qw.equals("")) qw += " and ";
			qw += " v.id = er.risk_id2 and e.id=er.employee_id ";
			qw += " and e.dept_id="+dept;						
		    }
		    if(!qw.equals("")){
			qc += " where "+qw;
			q += " where "+qw;
		    }
		    q += " group by status order by status";
		    qq = qc;
		    if(debug){
			logger.debug(qc);
		    }
		    rs = stmt.executeQuery(qc);
		    if(rs.next()){
			cnt = rs.getInt(1);
		    }
		    if(cnt == 0){
			all += " No match found for Worker Comp.<br /><br />";
		    }
		    else{
			qq = q;
			if(debug){
			    logger.debug(qq);
			}
			rs = stmt.executeQuery(qq);
			all += " <table border width='95%'>"+
			    "<caption>Workers Comp. by Status </caption>\n";
			all += " <tr><td><b>Status</b></td>"+
			    "<td align='right'><b>Count</b></td>"+
			    "<td align='right'><b>%</b></td>"+
			    "<td align='right'><b>TTD Pay</b></td>"+
			    "<td align='right'><b>PPI Pay</b></td>"+
			    "<td align='right'><b>Med Pay</b></td>"+
			    "</tr>";
			while(rs.next()){ 
			    s1 = rs.getInt(1);
			    str = rs.getString(2);
			    d = rs.getDouble(3);
			    d2 = rs.getDouble(4);
			    d3 = rs.getDouble(5);
			    t1 = t1 +d;
			    t2 = t2 +d2;
			    t3 = t3 +d3;
			    if(str == null) str = "&nbsp";
			    else if(str.equals("")) str = "Unknown";
			    all += "<tr><td>"+str+
				"</td><td align=right>"+s1+
				"</td><td align=right>"+
				Helper.findPercent(s1,cnt)+
				"</td><td align=right>"+
				format.format(d)+
				"</td><td align=right>"+
				format.format(d2)+
				"</td><td align=right>"+
				format.format(d3)+
				"</td></tr>";
			}
			all += "<tr><td>Total</td><td align=right>"+cnt+
			    "</td><td>&nbsp;"+
			    "</td><td align=right>"+
			    format.format(t1)+
			    "</td><td align=right>"+
			    format.format(t2)+
			    "</td><td align=right>"+
			    format.format(t3)+
			    "</td></tr>";
			all += "</table><br><br>\n";
		    }

		}
	    }catch(Exception ex){
		logger.error(ex+":"+qq);
	    }
	    out.println("<font size=+1><b>");
	    out.println("Statistics for the period "+startDate+" to "+
			endDate);
	    out.println("</b></font><br>");
	    if(!all.equals("")){
		out.println(all);
	    }
	    //
	}
	else if(report.startsWith("Record")){
	    String qw = "", str="", all="", qq="";
	    int cnt = 0;
	    String qc = "select count(*) from tortClaims v where v.recordOnly='y'";
	    String q = "select v.id,e.name,date_format(v.incidentDate,"+
		"'%m/%d/%Y'),v.incident from tortClaims v,"+
		"employees e,"+
		"empRelated er "+
		"where v.recordOnly='y' and "+
		" v.id = er.risk_id and e.id=er.employee_id ";
	    if(!startDate.equals("")){
		qw = " and str_to_date('"+startDate+
		    "','%m/%d/%Y') <= v.incidentDate ";
	    }
	    if(!endDate.equals("")){
		qw += " and str_to_date('"+endDate+
		    "','%m/%d/%Y') >= v.incidentDate ";
	    }
	    if(!qw.equals("")){
		qc += qw;
		q += qw;
	    }
	    q += " order by v.type";
	    qq = qc;
	    if(debug){
		logger.debug(qc);
	    }
	    try{
		rs = stmt.executeQuery(qc);
		if(rs.next()){
		    cnt = rs.getInt(1);
		}
		if(cnt == 0){
		    all += " No match found for Tort Claims <br /><br />";
		}
		else{
		    all += "<table width='70%' border='1'><caption>Record "+
			"Only Tort Claims "+
			"</caption>";
		    all += "<tr><th>ID</th><th>Employee</th><th>DOI</th>"+
			"<th>Incident</th></tr>";
		    qq = q;
		    if(debug){
			logger.debug(qq);
		    }
		    rs = stmt.executeQuery(qq);
		    while(rs.next()){
			str = rs.getString(1);
			all += "<td valign='top'><a href='"+url+
			    "TortClaimServ?action=zoom"+
			    "&id="+str+"'>"+str+"</a>";
			str = rs.getString(2);
			if(str == null || str.equals("")) str = "&nbsp;";
			all += "<td valign='top'>"+str+"</td>";
			str = rs.getString(3);
			if(str == null || str.equals("")) str = "&nbsp;";
			all += "<td valign='top'>"+str+"</td>";
			str = rs.getString(4);
			if(str == null || str.equals("")) str = "&nbsp;";
			all += "<td valign='top'>"+str+"</td>";
			all += "</tr>";
		    }
		    all += "</table><br />";
		}
		qc = "select count(*) from vslegals v where v.recordOnly='y' ";
		q = "select v.id,e.name,date_format(v.doi,"+
		    "'%m/%d/%Y'),v.description from vslegals v, "+
		    "employees e,"+
		    "empRelated er "+
		    " where v.recordOnly='y' and "+
		    " v.id = er.risk_id and e.id=er.employee_id ";
		if(!startDate.equals("")){
		    qw = " and str_to_date('"+startDate+
			"','%m/%d/%Y') <= v.doi ";
		}
		if(!endDate.equals("")){
		    qw += " and str_to_date('"+endDate+
			"','%m/%d/%Y') >= v.doi ";
		}
		if(!qw.equals("")){
		    qc += qw;
		    q += qw;
		}
		q += " order by type";
		qq = qc;
		if(debug){
		    logger.debug(qc);
		}
		rs = stmt.executeQuery(qc);
		if(rs.next()){
		    cnt = rs.getInt(1);
		}
		if(cnt == 0){
		    all += " No match found for Recovery Actions <br /><br />";
		}
		else{
		    all += "<table width='70%' border='1'><caption>Record"+
			" Only Recovery Actions "+
			"</caption>";
		    all += "<tr><th>ID</th><th>Employee</th><th>DOI</th>"+
			"<th>Notes</th></tr>";
		    qq = q;
		    if(debug){
			logger.debug(qq);
		    }
		    rs = stmt.executeQuery(qq);
		    while(rs.next()){
			str = rs.getString(1);
			all += "<td valign='top'><a href='"+url+
			    "LegalServ?action=zoom"+
			    "&id="+str+"'>"+str+"</a>";
			str = rs.getString(2);
			if(str == null || str.equals("")) str = "&nbsp;";
			all += "<td valign='top'>"+str+"</td>";
			str = rs.getString(3);
			if(str == null || str.equals("")) str = "&nbsp;";
			all += "<td valign='top'>"+str+"</td>";
			str = rs.getString(4);
			if(str == null || str.equals("")) str = "&nbsp;";
			all += "<td valign='top'>"+str+"</td>";
			all += "</tr>";
		    }
		    all += "</table><br />";
		}
		// Internal accident
		//
		qc = "select count(*) from riskSafety v where v.recordOnly='y' ";
		q = "select v.id,e.name,date_format(v.accidDate,"+
		    "'%m/%d/%Y'),v.damage from riskSafety v, "+
		    "employees e,"+
		    "empRelated er "+
		    " where v.recordOnly='y' and "+
		    " v.id = er.risk_id and e.id=er.employee_id ";		
		if(!startDate.equals("")){
		    qw = " and str_to_date('"+startDate+
			"','%m/%d/%Y') <= v.accidDate ";
		}
		if(!endDate.equals("")){
		    qw += " and str_to_date('"+endDate+
			"','%m/%d/%Y') >= v.accidDate ";
		}
		if(!qw.equals("")){
		    qc += qw;
		    q += qw;
		}
		q += " order by type";
		qq = qc;
		if(debug){
		    logger.debug(qc);
		}
		rs = stmt.executeQuery(qc);
		if(rs.next()){
		    cnt = rs.getInt(1);
		}
		if(cnt == 0){
		    all += " No match found for Internal Accidents<br /><br />";
		}
		else{
		    all += "<table width='70%' border='1'><caption>Record"+
			" Only Internal Accidents "+
			"</caption>";
		    all += "<tr><th>ID</th><th>Employee</th><th>DOI</th>"+
			"<th>Damages</th></tr>";
		    qq = q;
		    if(debug){
			logger.debug(qq);
		    }
		    rs = stmt.executeQuery(qq);
		    while(rs.next()){
			str = rs.getString(1);
			all += "<td valign='top'><a href='"+url+
			    "SafetyServ?action=zoom"+
			    "&id="+str+"'>"+str+"</a>";
			str = rs.getString(2);
			if(str == null || str.equals("")) str = "&nbsp;";
			all += "<td valign='top'>"+str+"</td>";
			str = rs.getString(3);
			if(str == null || str.equals("")) str = "&nbsp;";
			all += "<td valign='top'>"+str+"</td>";
			str = rs.getString(4);
			if(str == null || str.equals("")) str = "&nbsp;";
			all += "<td valign='top'>"+str+"</td>";
			all += "</tr>";
		    }
		    all += "</table>";
		}
	    }
	    catch(Exception ex){
		logger.error(ex+":"+qq);
	    }
	    out.println("<font size=+1><b><center>");
	    out.println("Record Only Claims & Recovery Actions <br />"+
			"For the Period "+startDate+" to "+
			endDate+"<br /></font>");
	    out.println("</b></font><br>");
	    if(!all.equals("")){
		out.println(all);
	    }
	    out.println("</center>");
	}
	else if(report.startsWith("autoType")){
	    String qw = "", str="", all="", qq="";
	    String stats="<table width='60%' border>"+
		"<caption>Auto Related Statistics</caption>"+
		"<tr><td>Item</td><td>Count</td></tr>";
	    int cnt = 0;
	    String qc = "select count(*) from tortClaims v where v.type='1'";//auto
	    String q = "select v.id,e.name,date_format(v.incidentDate,"+
		"'%m/%d/%Y'),v.incident from tortClaims v, "+
		"employees e,"+
		"empRelated er "+
		" where "+
		" v.id = er.risk_id and e.id=er.employee_id "+		
		"and v.type='1'";
	    if(!startDate.equals("")){
		qw = " and str_to_date('"+startDate+
		    "','%m/%d/%Y') <= v.incidentDate ";
	    }
	    if(!endDate.equals("")){
		qw += " and str_to_date('"+endDate+
		    "','%m/%d/%Y') >= v.incidentDate ";
	    }
	    if(!qw.equals("")){
		qc += qw;
		q += qw;
	    }
	    q += " order by v.incidentDate ";
	    qq = qc;
	    if(debug){
		logger.debug(qc);
	    }
	    try{
		rs = stmt.executeQuery(qc);
		if(rs.next()){
		    cnt = rs.getInt(1);
		    stats += "<tr><td>Tort Claims</td><td>"+cnt+"</td></tr>";
		}
		if(cnt == 0){
		    all += " No match found for Tort Claims <br /><br />";
		}
		else{
		    all += "<table width='70%' border='1'><caption>Auto "+
			" Related Tort Claims "+
			"</caption>";
		    all += "<tr><th>ID</th><th>Employee</th><th>DOI</th>"+
			"<th>Incident</th></tr>";
		    qq = q;
		    if(debug){
			logger.debug(qq);
		    }
		    rs = stmt.executeQuery(qq);
		    while(rs.next()){
			str = rs.getString(1);
			all += "<td valign='top'><a href='"+url+
			    "TortClaimServ?action=zoom"+
			    "&id="+str+"'>"+str+"</a>";
			str = rs.getString(2);
			if(str == null || str.equals("")) str = "&nbsp;";
			all += "<td valign='top'>"+str+"</td>";
			str = rs.getString(3);
			if(str == null || str.equals("")) str = "&nbsp;";
			all += "<td valign='top'>"+str+"</td>";
			str = rs.getString(4);
			if(str == null || str.equals("")) str = "&nbsp;";
			all += "<td valign='top'>"+str+"</td>";
			all += "</tr>";
		    }
		    all += "</table><br />";
		}
		qc = "select count(*) from vslegals v where v.type='1'";//auot
		q = "select v.id,e.name,date_format(v.doi,"+
		    "'%m/%d/%Y'),v.description from vslegals v, "+
		    "employees e,"+
		    "empRelated er "+
		    " where "+
		    " v.id = er.risk_id and e.id=er.employee_id "+		
		    "and v.type='1'";				
		if(!startDate.equals("")){
		    qw = " and str_to_date('"+startDate+
			"','%m/%d/%Y') <= v.doi ";
		}
		if(!endDate.equals("")){
		    qw += " and str_to_date('"+endDate+
			"','%m/%d/%Y') >= v.doi ";
		}
		if(!qw.equals("")){
		    qc += qw;
		    q += qw;
		}
		q += " order by v.doi";
		qq = qc;
		if(debug){
		    logger.debug(qc);
		}
		rs = stmt.executeQuery(qc);
		if(rs.next()){
		    cnt = rs.getInt(1);
		    stats += "<tr><td>Recovery Action</td><td>"+cnt+"</td></tr>";
		}
		if(cnt == 0){
		    all += " No match found for Recovery Actions <br /><br />";
		}
		else{
		    all += "<table width='70%' border='1'><caption>Auto "+
			" Related Recovery Actions "+
			"</caption>";
		    all += "<tr><th>ID</th><th>Employee</th><th>DOI</th>"+
			"<th>Notes</th></tr>";
		    qq = q;
		    if(debug){
			logger.debug(qq);
		    }
		    rs = stmt.executeQuery(qq);
		    while(rs.next()){
			str = rs.getString(1);
			all += "<td valign='top'><a href='"+url+
			    "LegalServ?action=zoom"+
			    "&id="+str+"'>"+str+"</a>";
			str = rs.getString(2);
			if(str == null || str.equals("")) str = "&nbsp;";
			all += "<td valign='top'>"+str+"</td>";
			str = rs.getString(3);
			if(str == null || str.equals("")) str = "&nbsp;";
			all += "<td valign='top'>"+str+"</td>";
			str = rs.getString(4);
			if(str == null || str.equals("")) str = "&nbsp;";
			all += "<td valign='top'>"+str+"</td>";
			all += "</tr>";
		    }
		    all += "</table><br />";
		}
		// Internal accident
		//
		qc = "select count(*) from riskSafety v where v.type='1'";//auto
		q = "select v.id,e.name,date_format(v.accidDate,"+
		    "'%m/%d/%Y'),v.damage from riskSafety v,"+
		    "employees e,"+
		    "empRelated er "+
		    " where "+
		    " v.id = er.risk_id and e.id=er.employee_id "+		
		    " and v.type='1'";						
		if(!startDate.equals("")){
		    qw = " and str_to_date('"+startDate+
			"','%m/%d/%Y') <= v.accidDate ";
		}
		if(!endDate.equals("")){
		    qw += " and str_to_date('"+endDate+
			"','%m/%d/%Y') >= v.accidDate ";
		}
		if(!qw.equals("")){
		    qc += qw;
		    q += qw;
		}
		q += " order by v.accidDate ";
		qq = qc;
		if(debug){
		    logger.debug(qc);
		}
		rs = stmt.executeQuery(qc);
		if(rs.next()){
		    cnt = rs.getInt(1);
		    stats += "<tr><td>Internal Accidents</td><td>"+cnt+
			"</td></tr>";
		}
		if(cnt == 0){
		    all += " No match found for Internal Accidents<br /><br />";
		}
		else{
		    all += "<table width='70%' border='1'><caption>Auto "+
			" Related Internal Accidents "+
			"</caption>";
		    all += "<tr><th>ID</th><th>Employee</th><th>DOI</th>"+
			"<th>Damages</th></tr>";
		    qq = q;
		    if(debug){
			logger.debug(qq);
		    }
		    rs = stmt.executeQuery(qq);
		    while(rs.next()){
			str = rs.getString(1);
			all += "<td valign='top'><a href='"+url+
			    "SafetyServ?action=zoom"+
			    "&id="+str+"'>"+str+"</a>";
			str = rs.getString(2);
			if(str == null || str.equals("")) str = "&nbsp;";
			all += "<td valign='top'>"+str+"</td>";
			str = rs.getString(3);
			if(str == null || str.equals("")) str = "&nbsp;";
			all += "<td valign='top'>"+str+"</td>";
			str = rs.getString(4);
			if(str == null || str.equals("")) str = "&nbsp;";
			all += "<td valign='top'>"+str+"</td>";
			all += "</tr>";
		    }
		    all += "</table>";
		}
	    }
	    catch(Exception ex){
		logger.error(ex+":"+qq);
	    }
	    stats += "</table><br />";
	    out.println("<font size=+1><b><center>");
	    out.println("Auto Related Claims, Recovery Actions & Internal "+
			"Accidents  <br />"+
			"For the Period "+startDate+" to "+
			endDate+"<br /></font>");
	    out.println("</b></font><br>");
	    out.println(stats);
	    if(!all.equals("")){
		out.println(all);
	    }
	    out.println("</center>");
	}
	//
	out.println("<br>");
	out.println("</body>");
	out.println("</html>");
	Helper.databaseDisconnect(con, stmt, rs);
		
    }

}























































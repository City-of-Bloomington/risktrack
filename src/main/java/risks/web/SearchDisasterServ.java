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
@WebServlet(urlPatterns = {"/SearchDisasterServ","/SearchDisaster"})
public class SearchDisasterServ extends TopServlet{

    static Logger logger = LogManager.getLogger(SearchDisasterServ.class);
    /**
     * search the disaster accidents 
     *
     * The user can check a selection of these and clicks the add defendents
     * @param req
     * @param res
     * @throws ServletException
     * @throws IOException
     */
    
    /**
     * @link Case#doGet
     * @see #doGet
     */
    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException {
    
	res.setContentType("text/html");
	PrintWriter out = res.getWriter();
	String name, value;
	String username = "";
	boolean success = true, showAll=false;

	String action="", message ="";

	String  dateFrom="", dateTo="", whichDate="",
	    amountFrom="",amountTo="";
	String 
	    type="", id="", tortId="", vsId="",
	    status="", empid="", empInjured="",
	    empName="", deptPhone="", dept_id="", empSuper="";
	
	String damage = "", 
	    autoPlate="", vin="",
	    autoMake ="", autoModel="", autoYear="", autoNum="",
	    accidLocation="", anyEstPlace="", accidTime="", 
	    chosenDealer="", recordOnly="",
	    insurance="",adjuster="",adjusterPhone="",attorney="",
	    attorneyPhone="",claimNum="",insuranceStatus="", policy="";
	Enumeration values = req.getParameterNames();
	List departments = null;
	List<RType> types = null;
	String [] vals;
		
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();	
	    if (name.equals("id")){
		id = value;
	    }
	    else if (name.equals("type")) {
		type =value;
	    }
	    else if (name.equals("status")) {
		status =value;
	    }
	    else if (name.equals("empName")) {
		empName = value.toUpperCase();
	    }
	    else if (name.equals("deptPhone")) {
		deptPhone = value;
	    }
	    else if (name.equals("dept_id")) {
		dept_id = value;
	    }
	    else if (name.equals("empid")) {
		empid = value;
	    }
	    else if (name.equals("empInjured")) {
		empInjured = value;
	    }
	    else if (name.equals("empSuper")) {
		empSuper = value;
	    }
	    else if (name.equals("vin")) {
		vin = value;
	    }
	    else if (name.equals("autoPlate")) {
		autoPlate =value;
	    }
	    else if (name.equals("autoMake")) {
		autoMake =value;
	    }
	    else if (name.equals("autoNum")) {
		autoNum =value;
	    }
	    else if (name.equals("autoModel")) {
		autoModel =value;
	    }
	    else if (name.equals("autoYear")) {
		autoYear =value;
	    }
	    else if (name.equals("whichDate")) {
		whichDate = value;
	    }
	    else if (name.equals("dateFrom")) {
		dateFrom = value;
	    }
	    else if (name.equals("dateTo")) {
		dateTo = value;
	    }
	    else if (name.equals("amountFrom")) {
		amountFrom = value;
	    }
	    else if (name.equals("amountTo")) {
		amountTo = value;
	    }
	    else if (name.equals("damage")) {
		damage = value;
	    }
	    else if (name.equals("accidTime")) {
		accidTime = value;
	    }
	    else if (name.equals("anyEstPlace")) {
		anyEstPlace = value;
	    }
	    else if (name.equals("chosenDealer")) {
		chosenDealer =value;
	    }
	    else if (name.equals("recordOnly")) {
		recordOnly = value;
	    }
	    else if (name.equals("action")){
		action = value;  
	    }
	}
	HttpSession session = session = req.getSession(false);
	User user = null;
	if(session != null){
	    user = (User)session.getAttribute("user");
	}
	if(user == null){
	    res.sendRedirect(url+"Login");
	    return;
	}
	if(types == null){
	    DisasterTypeList rtl = new DisasterTypeList(debug);
	    String msg = rtl.lookFor();
	    if(msg.equals("")){
		types = rtl.getTypes();
	    }
	    else {
		message += msg;
		success = false;
		logger.error(msg);
	    }
	}
	if(departments == null){
	    DeptList dl = new DeptList(debug);
	    String msg = dl.lookFor();
	    if(msg.equals("")){
		departments = dl.getDepartments();
	    }
	    else {
		message += msg;
		success = false;
		logger.error(msg);
	    }
	}
	// Inserts
	out.println(Inserts.xhtmlHeaderInc);
	out.println(Inserts.banner(url));
	out.println(Inserts.menuBar(url, true));
	out.println(Inserts.sideBar(url));
	//
	out.println("<div id=\"mainContent\">");
	out.println("<h3 class=\"titleBar\">Search Disaster Accidents </h3>");
	if(success){
	    if(!message.equals(""))
		out.println("<h2>"+message+"</h2>");
	}
	else{
	    if(!message.equals(""))
		out.println("<h2 class=\"errorMessages\">"+message+"</h2>");
	}
	out.println("<form name=\"myForm\" method=\"post\" >");
	//
	// 1st block
	out.println("<fieldset><legend>Status</legend>");
	out.println("<table>");
	out.println("<tr><td><label for=\"id\">Disaster ID:</label>");
	out.println("<input name=\"id\" id=\"id\" value=\""+id+"\""+
		    " size=\"8\" maxlength=\"8\" /></td></tr>");
	out.println("<tr><td>");
	out.println("<label for=\"type\">Type:</label>");
	out.println("<select name=\"type\" id=\"type\">");
	out.println("<option value=\"\">All</option>");
	if(types != null){
	    for(RType rtype: types){
		out.println("<option value='"+rtype.getId()+"'>"+
			    rtype.getType()+"</option>");
	    }
	}
	out.println("</select><label for='otherType'>Other Type: </label>");
	out.println("<input type='test' name='otherType' size='15'>");
	out.println("<label for=\"status\"> Status:</label>"+
		    "<select name=\"status\" id=\"status\">");
	out.println("<option value=\"\">All</option>");				
	for(int i=0;i<Inserts.searchStatusArr.length;i++){
	    if(status.equals(Inserts.searchStatusArr[i]))
		out.println("<option selected=\"selected\">"+status+
			    "</option>");
	    else
		out.println("<option>"+Inserts.searchStatusArr[i]+"</option>");
	}
	out.println("</select>");
	out.println("</td></tr>");
	out.println("<tr><td><label for=\"damage\">Accident Details (use key words):</label>");
	out.println("<input name=\"damage\" id=\"damage\" size=\"30\" "+
		    "value=\""+damage+"\" "+
		    "maxlength=\"30\" />");
	out.println("</td></tr>");
	out.println("</table>");
	out.println("</fieldset>");
	out.println("<fieldset><legend>Employee</legend>");
	out.println("<table>");
	out.println("<tr><td>");
	out.println("<label for=\"empName\">Name:</label>"+
		    "<input name=\"empName\" id=\"empName\" value=\""+
		    empName+"\" size=\"20\" maxlength=\"30\" /></td></tr>");
	//
	out.println("<tr><td><label for=\"dept_id\">Department:</label>");
	out.println("<select name=\"dept_id\" id=\"dept_id\">");
	out.println("<option value=\"\">All</option>");
	if(departments != null && departments.size() > 0){
	    for(int i=0;i<departments.size();i++){
		Department dpp = (Department)departments.get(i);
		out.println("<option value=\""+dpp.getId()+"\">"+dpp.getInfo()+"</option>");
	    }
	}
	out.println("</select></td></tr>");
	out.println("<tr><td><label for=\"deptPhone\">Department Phone:</label>"+
		    "<input name=\"deptPhone\" id=\"deptPhone\" value=\""+
		    deptPhone+"\" size=\"10\" maxlength=\"10\"/ >");
	out.println("<label for=\"empInjured\">Employee Injured? </label>"+
		    "<input type=\"checkbox\" name=\"empInjured\" "+
		    "id=\"empInjured\" value=\"y\" "+
		    ">Yes</td></tr>");
	out.println("</table>");
	out.println("</fieldset>");
	out.println("<fieldset><legend>Damages</legend>");
	out.println("<table>");
	out.println("<tr><td><label for=\"autoDamage\">Vehicle Damaged?</label>");
	out.println("<input type=\"checkbox\" name=\"autoDamage\" "+
		    "onchange=\"refreshPage()\" "+
		    "id=\"autoDamage\" value=\"y\">Yes,");
	out.println("<label for=\"propDamage\">Property Damaged?</label>");
	out.println("<input type=\"checkbox\" name=\"propDamage\" "+
		    "onchange=\"refreshPage()\" "+
		    "id=\"workComp\" />Yes </td></tr>");
	//
	out.println("<tr><td><label for=\"vin\">Vehicle, VIN #:</label>"+
		    "<input name=\"vin\" id=\"vin\" value=\""+
		    vin+"\" size=\"15\" maxlength=\"20\" />");
	out.println("<label for=\"autoPlate\">Plate #:</label>"+
		    "<input name=\"autoPlate\" id=\"autoPlate\" value=\""+
		    autoPlate+"\" size=\"10\" maxlength=\"20\" />");
	out.println("<label for=\"autoNum\">Number:</label>"+
		    "<input name=\"autoNum\" id=\"autoNum\" value=\""+
		    autoNum+"\" size=\"10\" maxlength=\"20\" />");
	out.println("<label for=\"autoMake\">Make:</label>"+
		    "<input name=\"autoMake\" id=\"autoMake\" value=\""+
		    autoMake+"\" size=\"10\" maxlength=\"20\" />");
	out.println("<label for=\"autoModel\">Model:</label>"+
		    "<input name=\"autoModel\" id=\"autoModel\" value=\""+
		    autoModel+"\" size=\"10\" maxlength=\"20\" />");
	out.println("<label for=\"autoYear\">Year:</label>"+
		    "<input name=\"autoYear\" id=\"autoYear\" value=\""+
		    autoYear+"\" size=\"4\" maxlength=\"4\" /></td></tr>");
	//
	out.println("</table>");
	out.println("</fieldset>");
	out.println("<fieldset><legend>Insurance</legend>");
	out.println("<table>");
	out.println("<tr><td><label for=\"insurance\">Insurance:</label>");
	out.println("<input name=\"insurance\" id=\"insurance\" size=\"30\" "+
		    "maxlength=\"50\">");
	out.println("</td></tr>");
	out.println("<tr><td><label for=\"adjuster\">Adjuster's Name:</label>"+
		    "<input name=\"adjuster\" id=\"adjuster\" value=\""+
		    adjuster+"\" size=\"20\" maxlength=\"30\"/ >");
	out.println("<label for=\"adjusterPhone\"> Phone: </label>"+
		    "<input name=\"adjusterPhone\" "+
		    "id=\"adjusterPhone\" value=\""+
		    adjusterPhone+"\" size=\"16\" maxlength=\"16\"/ >"+
		    "</td></tr>");
	out.println("<tr><td><label for=\"claimNum\">Claim #:</label>"+
		    "<input name=\"claimNum\" id=\"claimNum\" value=\""+
		    claimNum+"\" size=\"20\" maxlength=\"20\"/ >");
	out.println("<label for=\"insurStatus\"> Insurance Status: "+
		    "</label><select name=\"insurStatus\" "+
		    "id=\"insurStatus\">");
	out.println("<option value=\"\">All</option>");				
	for(int i=0;i<Inserts.insurStatusArr.length;i++){
	    if(insuranceStatus.equals(Inserts.insurStatusArr[i]))
		out.println("<option selected=\"selected\">"+insuranceStatus+
			    "</option>");
	    else
		out.println("<option>"+Inserts.insurStatusArr[i]+"</option>");
	}
	out.println("</select><label for=\"policy\">Policy:</label>");
	out.println("<select name=\"policy\" id=\"policy\">");
	out.println("<option value=\"\">All</option>");
	for(int i=0;i<Inserts.policyArr.length;i++){
	    if(policy.equals(Inserts.policyArr[i]))
		out.println("<option selected=\"selected\">"+policy+
			    "</option>");
	    else
		out.println("<option>"+Inserts.policyArr[i]+"</option>");
	}
	out.println("</select></td></tr>");
	out.println("</table>");
	out.println("</fieldset>");
	out.println("<fieldset><legend>Other</legend>");
	out.println("<table>");
	//
	out.println("<tr><td><label>Dates (select one):</label>");
	out.println("<input type=\"radio\" name=\"whichDate\" checked "+
		    "id=\"whichDate\" value=\"d.accidDate\">Accident Date</input>");
	out.println("<input type=\"radio\" name=\"whichDate\"  "+
		    "id=\"whichDate\" value=\"d.reported\">Reported</input>");
	out.println("</td></tr>");
	out.println("<tr><td><label for=\"dateFrom\">Date, from:</label>");
	out.println("<input name=\"dateFrom\" size='10' maxlength='10' "+
		    "id='dateFrom' value='"+dateFrom+"' />");
	out.println("<label for=\"dateTo\"> to:</label>");
	out.println("<input name='dateTo' size='10' maxlength='10' "+
		    "id='dateTo' value='"+dateTo+"' />");
	out.println("</td></tr>");
	//
	out.println("<tr><td><label>Monetary Amount (choose one):</label>");
	out.println("<input type=\"radio\" name=\"whichAmount\" checked "+
		    "id=\"whichAmount\" value=\"d.totalCost\">Total Vehicle Repair</input>");
	out.println("<input type=\"radio\" name=\"whichAmount\" "+
		    "id=\"whichAmount\" value=\"d.totalCostP\">Total Property Repair</input>");
	out.println("</td></tr>");
	out.println("<tr><td><label for=\"amountFrom\">From:</label>");
	out.println("<input name=\"amountFrom\" "+
		    "id=\"amountFrom\" value=\""+amountFrom+"\" />");
	out.println("<label for=\"amountTo\"> to:</label>");
	out.println("<input name=\"amountTo\" "+
		    "id=\"amountTo\" value=\""+amountTo+"\" />");
	out.println("</td></tr>");
	out.println("<tr><td><label for=\"orderBy\">Sort by:</label>");
	out.println("<select name=\"orderBy\" id=\"orderBy\">");
	out.println("<option value=\"\">\n</option>");
	out.println("<option value=\"t.type\">Type</option>");
	out.println("<option value=\"d.status\">Status</option>");
	out.println("<option value=\"d.reported DESC\">Reported Date</option>");
	out.println("<option value=\"d.accidDate DESC\">Accident Date</option>");
	out.println("</select></td></tr>");
	//
	out.println("</table>");
	out.println("</fieldset>");
	out.println("<fieldset>");
	out.println("<table class=\"control\">");
	out.println("<tr><td>");
	out.println("<input type=\"submit\" "+
		    "name=\"action\" value=\"Search\" />");
	out.println("</td></tr></table>");
	out.println("</fieldset>");
	//
	out.println("</form>");
	out.println("</div>");
	//
	out.print("</body></html>");
	out.close();

    }

    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException {

	res.setContentType("text/html");
	PrintWriter out = res.getWriter();
	String name, value;
	boolean success = true, showAll=false;
	String message ="";
	//
	String [] titles = {"ID",
	    "Status",
	    "Type",
	    "Accident Date",
	    "Accident Location",
							
	    "Damages ",
	    "Employee Injured",
	    "Department",
	    "Employees ",
	    "Vehicles"
	};
	//
	boolean [] show = {true, true, true, true, true,
	    true, true, true, true, true
	};
	Enumeration values = req.getParameterNames();
	String [] vals;
	DisasterList dl = new DisasterList(debug);
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();	
	    if (name.equals("id")){
		dl.setId(value);
	    }
	    else if (name.equals("type")) {
		dl.setType(value);
	    }
	    else if (name.equals("otherType")) {
		dl.setOtherType(value);
	    }
	    else if (name.equals("status")) {
		dl.setStatus(value);
	    }
	    else if (name.equals("dept_id")) {
		dl.setDept_id(value);
	    }
	    else if (name.equals("empid")) {
		dl.setEmpid(value);
	    }
	    else if (name.equals("empName")) {
		dl.setEmpName(value.toUpperCase());
	    }
	    else if (name.equals("deptPhone")) {
		dl.setDeptPhone(value);
	    }
	    else if (name.equals("empInjured")) {
		dl.setEmpInjured(value);
	    }
	    else if (name.equals("vin")) {
		dl.setVin(value);
	    }
	    else if (name.equals("autoPlate")) {
		dl.setAutoPlate(value);
	    }
	    else if (name.equals("autoNum")) {
		dl.setAutoNum(value);
	    }
	    else if (name.equals("autoMake")) {
		dl.setAutoMake(value);
	    }
	    else if (name.equals("autoModel")) {
		dl.setAutoModel(value);
	    }
	    else if (name.equals("autoYear")) {
		dl.setAutoYear(value); 
	    }
	    else if (name.equals("whichDate")) {
		dl.setWhichDate(value);
	    }
	    else if (name.equals("dateFrom")) {
		dl.setDateFrom(value);
	    }
	    else if (name.equals("dateTo")) {
		dl.setDateTo(value);
	    }
	    else if (name.equals("whichAmount")) {
		dl.setWhichAmount(value);
	    }
	    else if (name.equals("amountFrom")) {
		dl.setAmountFrom(value);
	    }
	    else if (name.equals("amountTo")) {
		dl.setAmountTo(value);
	    }
	    else if (name.equals("damage")) {
		dl.setDamage(value.toUpperCase());
	    }
	    else if (name.equals("autoDamage")) {
		dl.setAutoDamage(value);
	    }
	    else if (name.equals("propDamage")) {
		dl.setPropDamage(value);
	    }
	    else if (name.equals("whichAmount")) {
		dl.setWhichAmount(value);
	    }
	    else if (name.equals("insurance")) {
		dl.setInsurance(value.toUpperCase());
	    }
	    else if (name.equals("adjuster")) {
		dl.setAdjuster(value.toUpperCase());
	    }
	    else if (name.equals("adjusterPhone")) {
		dl.setAdjusterPhone(value);
	    }
	    else if (name.equals("claimNum")) {
		dl.setClaimNum(value);
	    }
	    else if (name.equals("insurStatus")) {
		dl.setInsurStatus(value);
	    }
	    else if (name.equals("policy")) {
		dl.setPolicy(value);
	    }
	    else if (name.equals("orderBy")){
		dl.setOrderBy(value);  
	    }
	}
	//
	// Inserts
	out.println(Inserts.xhtmlHeaderInc);
	out.println(Inserts.banner(url));
	out.println(Inserts.menuBar(url, true));
	out.println(Inserts.sideBar(url));

	out.println("<div id=\"mainContent\">");
	List<Disaster> disasters = null;

	String msg = dl.lookFor();
	if(msg.equals("")){
	    disasters = dl.getDisasters();
	}
	else{
	    message += msg;
	    success = false;
	}
	int ncnt = 0;
	String str = "";
	if(disasters != null){
	    ncnt = disasters.size();
	    if(ncnt == 1){
		Disaster one = disasters.get(0);
		res.sendRedirect(url+"Disaster?id="+one.getId());
		return;
	    }	
	}
	if(success){
	    if(!message.equals(""))
		out.println("<h2>"+message+"</h2>");
	}
	else{
	    if(!message.equals(""))
		out.println("<h2 class=\"errorMessages\">"+message+"</h2>");
	}
	out.println("<h4>Total Matching Records "+ ncnt +" </h4>");
	if(ncnt > 0){
	    out.println("<table class='box'>");
	    out.println("<tr>");
	    for (int c = 0; c < titles.length; c++){ 
		if(show[c] || showAll)
		    out.println("<th>"+titles[c]+"</th>");
	    }	   
	    out.println("</tr>");
	    for(Disaster dis:disasters){
		if(dis != null){
		    out.println("<tr>");
		    // 
		    out.println("<td><a href=\""+url+"Disaster?"+
				"action=zoom&id="+dis.getId()+"\">"+
				dis.getId()+"</a></td>"); 
		    //
		    // status
		    if(show[1]){
			str = dis.getStatus();
			if(str == null || str.equals("")) str = "&nbsp;";
			out.println("<td>"+str+"</td>");
		    }
		    // Type
		    if(show[2]){
			String str2 = "&nbsp;";
			str = dis.getType();
			if(str != null && !str.equals("")){
			    DisasterType dt = new DisasterType(debug, str);
			    if(dt != null){
				str = dt.getType();
				if(str != null) str2 = str;
			    }
			}
			out.println("<td>"+str2+"</td>");
		    }
		    //
		    if(show[3]){
			str = dis.getAccidDate();
			if(str.equals("")) str = "&nbsp;";
			out.println("<td>"+str+"</td>");
		    }
		    if(show[4]){
			str = dis.getAccidLocation();
			if(str.equals("")) str = "&nbsp;";
			out.println("<td>"+str+"</td>");
		    }
		    if(show[5]){
			str = dis.getDamage();
			if(str.equals("")) str = "&nbsp;";
			out.println("<td>"+str+"</td>");
		    }
		    if(show[6]){
			str = dis.getEmpInjured();
			if(str.equals("")) str = "&nbsp;";
			out.println("<td>"+str+"</td>");
		    }
		    if(show[7]){
			Department dp = dis.getDepartment();
			if(dp == null) str = "&nbsp;";
			else
			    str = dp.getName();
			out.println("<td>"+str+"</td>");
		    }
		    if(show[8]){
			List emps = dis.getEmployees();
			str = "";
			if(emps != null && emps.size() > 0){
			    for(int j=0;j<emps.size();j++){
				Employee emp = (Employee)emps.get(j);
				if(emp != null){
				    if(!str.equals("")) str += ", ";
				    str += emp.getFullName();
				}
			    }
			}
			if(str.equals("")) str = "&nbsp;";
			out.println("<td>"+str+"</td>");
		    }
		    if(show[9]){
			str = "";						
			List autos = dis.getVehicles();
			if(autos != null && autos.size() > 0){
			    for(int j=0;j<autos.size();j++){
				Auto car = (Auto) autos.get(j);
				if(car != null){
				    if(!str.equals("")) str += ", ";
				    str += car.getAutoInfo();
				}
			    }
			}
			if(str.equals("")) str = "&nbsp;";
			out.println("<td>"+str+"</td>");
		    }
		    out.println("</tr>");
		}
	    }
	    out.println("</table>");
	}
	//
	out.print("</div>");
	out.print("</body></html>");
	out.close();
    }

}























































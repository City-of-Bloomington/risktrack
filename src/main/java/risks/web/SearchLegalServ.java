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
@WebServlet(urlPatterns = {"/SearchLegalServ","/SearchLegal"})
public class SearchLegalServ extends TopServlet{

    static Logger logger = LogManager.getLogger(SearchLegalServ.class);	

    /**
     * Generates the search defendants form and then list the matching records.
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

	String dateFrom="", dateTo="", 
	    whichDate="", whichAmount="",
	    amountFrom="",amountTo="";

	String claimNum="",  caseNum="",
	    adjuster="",  attorney="",attorneyPhone="",
	    adjusterPhone="",
	    type="", id="", status="",  location="", tortId="",
	    insurStatus="", insured="",
	    insurance="", recordOnly="",
	    empid="", empName="", empPhone="", dept="",
	    description="";
	
	String cityAutoInc="", vin="", autoPlate="", autoNum="",
	    autoMake ="", autoModel="", autoYear="";
	String lname="",fname="",phone="",dob="",defInsur="",defClaimNum="",
	    defAdjuster="",defInsurPhone="",defAttorney="",
	    defAttorneyPhone="";

	Enumeration values = req.getParameterNames();
	String [] vals;
	List<Department> depts = null;
	RiskTypeList types = null;
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
	    else if (name.equals("tortId")) {
		tortId =value;
	    }
	    else if (name.equals("claimNum")) {
		claimNum =value;
	    }
	    else if (name.equals("adjuster")) {
		adjuster =value.toUpperCase();
	    }
	    else if (name.equals("adjusterPhone")) {
		adjusterPhone =value;  
	    }
	    else if (name.equals("cityAutoInc")) {
		cityAutoInc = value;
	    }
	    else if (name.equals("empName")) {
		empName = value.toUpperCase();
	    }
	    else if (name.equals("empPhone")) {
		empPhone = value;
	    }
	    else if (name.equals("dept")) {
		dept = value;
	    }
	    else if (name.equals("empid")) {
		empid = value;
	    }
	    else if (name.equals("vin")) {
		vin = value;
	    }
	    else if (name.equals("autoNum")) {
		autoNum = value;
	    }
	    else if (name.equals("recordOnly")) {
		recordOnly = value;
	    }
	    else if (name.equals("whichDate")) {
		whichDate = value;
	    }
	    else if (name.equals("whichAmount")) {
		whichAmount = value;
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
	    else if (name.equals("insured")) {
		insured = value;
	    }
	    else if (name.equals("insurance")) {
		insurance = value.toUpperCase();
	    }
	    else if (name.equals("description")) {
		description = value.toUpperCase();
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
	if(types == null){
	    types = new RiskTypeList(debug, "unified");
	    String msg = types.lookFor();
	    if(!msg.equals("")){
		types = null;
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
	out.println("<h3 class=\"titleBar\">Search Recovery Action</h3>");
	//
	if(success){
	    if(!message.equals(""))
		out.println("<h3>"+message+"</h3>");
	}
	else{
	    if(!message.equals(""))
		out.println("<h3 class=\"errorMessages\">"+message+"</hd>");
	}
	out.println("<form name=\"myForm\" method=\"post\" >");
	//	    "onSubmit=\"return validateForm()\">");
	//
	// 1st block
	out.println("<fieldset>");
	out.println("<table>");
	out.println("<tr><td><label for=\"id\">ID:</label>");
	out.println("<input name=\"id\" id=\"id\" value=\""+id+"\""+
		    " size=\"8\" maxlength=\"8\" />");
	out.println("<label for=\"caseNum\">Case #:</label>");
	out.println("<input name=\"caseNum\" id=\"caseNum\" value=\""+
		    caseNum+"\""+
		    " size=\"30\" maxlength=\"50\" />&nbsp;&nbsp;");
	out.println("<input type=\"checkbox\" name=\"recordOnly\" value=\"y\">");
	out.println("<label> Record Only </label>");
	out.println("</td></tr>");
	out.println("<tr><td>");
	out.println("<label for=\"type\">Type:</label>");
	out.println("<select name=\"type\" id=\"type\">");
	out.println("<option value=\"\">&nbsp;</option>");
	if(types != null){
	    for(RiskType rtype:types){
		out.println("<option value=\""+rtype.getId()+
			    "\">"+rtype.getName()+"</option>");
	    }
	}		
	out.println("</select><label for=\"otherType\">Other Type: </label>");
	out.println("<input type=\"test\" name=\"otherType\" size=\"15\">");
	out.println("<label for=\"status\"> Status:</label>"+
		    "<select name=\"status\" id=\"status\">");
	for(int i=0;i<Inserts.searchLegalStatusArr.length;i++){
	    if(status.equals(Inserts.searchLegalStatusArr[i]))
		out.println("<option selected=\"selected\">"+status+
			    "</option>");
	    else
		out.println("<option>"+Inserts.searchLegalStatusArr[i]+"</option>");
	}
	out.println("</select>");
	out.println("<label for=\"tortId\">Related Tort Claim ID:</label>");
	out.println("<input name=\"tortId\" id=\"tortId\" "+
		    "value=\""+tortId+"\" size=\"8\" maxlength=\"8\" />");
	out.println("</td></tr>");
	out.println("<tr><td><label for=\"location\">Location:</label>");
	out.println("<input name=\"location\" id=\"location\" size=\"30\" "+
		    "value=\""+location+"\" "+
		    "maxlength=\"50\" />");
	out.println("</td></tr></table>");
	out.println("</fieldset>");

	out.println("<fieldset><legend>Defendant</legend>");
	out.println("<table>");
	out.println("<tr><td><label for=\"lname\">Last Name:</label>");
	out.println("<input name=\"lname\" id=\"lname\" size=\"20\" "+
		    "value=\""+lname+"\" "+
		    "maxlength=\"30\" /></td><td>");
	out.println("<label for=\"fname\">First Name:</label>");
	out.println("<input name=\"fname\" id=\"fname\" size=\"20\" "+
		    "value=\""+lname+"\" "+
		    "maxlength=\"30\" /></td></tr>");
	out.println("<tr><td><label for=\"phone\">Phone:</label>");
	out.println("<input name=\"phone\" id=\"phone\" size=\"15\" "+
		    "value=\""+phone+"\" "+
		    "maxlength=\"20\" /></td><td>");
	out.println("<label for=\"dob\">DOB:</label>");
	out.println("<input name=\"dob\" id=\"dob\" size=\"10\" "+
		    "value=\""+dob+"\" "+
		    "maxlength=\"10\" /></td></tr>");
	out.println("<tr><td><label for=\"defInsur\">Defendant\"s Insurance: "+
		    "</label>"+
		    "<input name=\"defInsur\" id=\"defInsur\" value=\""+
		    defInsur+"\" size=\"30\" maxlength=\"50\" /></td>");

	out.println("<td><label for=\"defClaimNum\"> Claim #:</label>"+
		    "<input name=\"defClaimNum\" id=\"defClaimNum\" value=\""+
		    defClaimNum+"\" size=\"10\" maxlength=\"15\" />");
	out.println("</td></tr><tr><td>");
	out.println("<label for=\"defAdjuster\">Adjuster:</label>"+
		    "<input name=\"defAdjuster\" id=\"defAdjuster\" value=\""+
		    defInsur+"\" size=\"20\" maxlength=\"50\" /></td>");
	out.println("<td><label for=\"defInsurPhone\">"+
		    " Insurance Phone #:</label>"+
		    "<input name=\"defInsurPhone\" id=\"defInsurPhone\" "+
		    "value=\""+
		    defInsurPhone+"\" size=\"10\" maxlength=\"15\" />");
	out.println("</td></tr><tr><td>");
	out.println("<label for=\"defAttorney\">Defendant\"s Attorney "+
		    ":</label>"+
		    "<input name=\"defAttorney\" id=\"defAttorney\" value=\""+
		    defAttorney+"\" size=\"30\" maxlength=\"50\" /></td>");
	out.println("<td><label for=\"defAttorneyPhone\">Defendant\"s "+
		    " Attorney Phone #:</label>"+
		    "<input name=\"defAttorneyPhone\" id=\"defAttorneyPhone\""+
		    " value=\""+
		    defAttorneyPhone+"\" size=\"10\" maxlength=\"15\" />");
	out.println("</td></tr>");
	out.println("</table>");
	out.println("</fieldset>");
	//
	out.println("<fieldset><legend>Insurance </legend>");
	out.println("<table>");
	out.println("<tr><td><label for=\"claimNum\">Claim #:</label>"+
		    "<input name=\"claimNum\" id=\"claimNum\" value=\""+
		    claimNum+"\" size=\"20\" maxlength=\"20\" />");
	out.println("<label for=\"insurStatus\"> Insurance Status: "+
		    "</label><select name=\"insurStatus\" "+
		    "id=\"insurStatus\">");
	for(int i=0;i<Inserts.insurStatusArr.length;i++){
	    if(insurStatus.equals(Inserts.insurStatusArr[i]))
		out.println("<option selected=\"selected\">"+insurStatus+
			    "</option>");
	    else
		out.println("<option>"+Inserts.insurStatusArr[i]+"</option>");
	}
	out.println("</select></td></tr>");
	out.println("<tr><td><label for=\"insurance\">Insurance:</label>"+
		    "<input name=\"insurance\" id=\"insurance\" value=\""+
		    insurance+"\" size=\"20\" maxlength=\"50\" /></td></tr>");
	out.println("<tr><td><label for=\"adjuster\">Adjuster\"s Name:</label>"+
		    "<input name=\"adjuster\" id=\"adjuster\" value=\""+
		    adjuster+"\" size=\"20\" maxlength=\"30\" />");
	out.println("<label for=\"adjusterPhone\"> Phone: </label>"+
		    "<input name=\"adjusterPhone\" "+
		    "id=\"adjusterPhone\" value=\""+
		    adjusterPhone+"\" size=\"16\" maxlength=\"16\" />"+
		    "</td></tr>");
	out.println("<tr><td><label for=\"attorney\">Attorney:</label>"+
		    "<input name=\"attorney\" id=\"attorney\" value=\""+
		    attorney+"\" size=\"20\" maxlength=\"30\" />");
	out.println("<label for=\"attorneyPhone\"> Phone: </label>"+
		    "<input name=\"attorneyPhone\" "+
		    "id=\"attorneyPhone\" value=\""+
		    adjusterPhone+"\" size=\"16\" maxlength=\"16\" />"+
		    "</td></tr>");

	out.println("</table>");
	out.println("</fieldset>");
	//
	out.println("<fieldset><legend>City </legend>");
	out.println("<table>");
	out.println("<tr><td>");
	out.println("<label for=\"empName\">Employee Name:</label>"+
		    "<input name=\"empName\" id=\"empName\" value=\""+
		    empName+"\" size=\"20\" maxlength=\"30\"/ ></td></tr>");
	//
	out.println("<tr><td><label for=\"dept_id\">Employee Department:</label>");
	out.println("<select name=\"dept_id\" id=\"dept_id\">");
	out.println("<option value=\"\">All");
	if(depts != null){
	    for(Department dep:depts){
		out.println("<option value=\""+dep.getId()+"\">"+dep.getInfo());
	    }
	}		
	out.println("</select></td></tr>");
	out.println("<tr><td><label for=\"empPhone\">Employee Work Phone:"+
		    "</label>"+
		    "<input name=\"empPhone\" id=\"empPhone\" value=\""+
		    empPhone+"\" size=\"10\" maxlength=\"10\" />");
	out.println("<label for=\"cityAutoInc\">City Vehichle Included? "+
		    "</label><input type=\"checkbox\" name=\"cityAutoInc\" "+
		    "id=\"cityAutoInc\" value=\"y\" checked=\""+
		    cityAutoInc+"\" />Yes</td></tr>");
	//
	out.println("<tr><td><label for=\"vin\">Vehicle, VIN #:</label>"+
		    "<input name=\"vin\" id=\"vin\" value=\""+
		    vin+"\" size=\"15\" maxlength=\"20\" />");
	out.println("<label for=\"autoPlate\">Plate #:</label>"+
		    "<input name=\"autoPlate\" id=\"autoPlate\" value=\""+
		    autoPlate+"\" size=\"10\" maxlength=\"20\" />");
	out.println("<label for=\"autoNum\">Number:</label>"+
		    "<input name=\"autoNum\" id=\"autoNum\" value=\""+
		    autoNum+"\" size=\"10\" maxlength=\"10\" />");
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
	//
	out.println("<fieldset><legend>Dates & Fines </legend>");
	out.println("<table>");
	//
	out.println("<tr><td>Dates (select one):");
	out.println("<input type=\"radio\" name=\"whichDate\" "+
		    "id=\"whichDate\" checked value=\"l.doi\">DOI</input>");
	out.println("<input type=\"radio\" name=\"whichDate\" "+
		    "id=\"whichDate\" value=\"l.filed\">Filed</input>");
	out.println("<input type=\"radio\" name=\"whichDate\" "+
		    "id=\"whichDate\" value=\"l.judgment\">Judgment </input>");
	out.println("<input type=\"radio\" name=\"whichDate\" "+
		    "id=\"whichDate\" value=\"l.proSupp\">Pro Supp </input>");
	out.println("<input type=\"radio\" name=\"whichDate\" "+
		    "id=\"whichDate\" value=\"l.closed\">Closed</input>");
	//
	out.println("</td></tr>");
	out.println("<tr><td><label for=\"dateFrom\">Date, from:</label>");
	out.println("<input name=\"dateFrom\" size=\"10 "+
		    "id=\"dateFrom\" value=\""+dateFrom+"\" />");
	out.println("<label for=\"dateTo\"> to:</label>");
	out.println("<input name=\"dateTo\" size=\"10\" "+
		    "id=\"dateTo\" value=\""+dateTo+"\" />");
	out.println("</td></tr>");
	//
	out.println("<tr><td><label>Monitary Amount (select one):</labeL>");
	out.println("<input type=\"radio\" name=\"whichAmount\" "+
		    "id=\"whichAmount\" value=\"l.damageAmnt\" />"+
		    "Damage Amount, ");
	out.println("<input type=\"radio\" name=\"whichAmount\" "+
		    "id=\"whichAmount\" value=\"l.paidByCity\" />"+
		    "Paid By City, ");
	out.println("<input type=\"radio\" name=\"whichAmount\" "+
		    "id=\"whichAmount\" value=\"l.paidByRisk\" />"+
		    "Paid By Risk ");
	out.println("<input type=\"radio\" name=\"whichAmount\" "+
		    "id=\"whichAmount\" value=\"l.paidByDef\" />"+
		    "Paid By Defendant (or his/her Insurance) ");
	out.println("</td></tr>");
	out.println("<tr><td><label for=\"amountFrom\">Amount, from:</label>");
	out.println("<input name=\"amountFrom\" "+
		    "id=\"amountFrom\" value=\""+amountFrom+"\" />");
	out.println("<label for=\"amountTo\"> to:</label>");
	out.println("<input name=\"amountTo\" "+
		    "id=\"amountTo\" value=\""+amountTo+"\" />");
	out.println("</td></tr>");
	out.println("<tr><td><label for=\"orderBy\">Sort by:</label>");
	out.println("<select name=\"orderBy\" id=\"orderBy\">");
	out.println("<option value=\"\">\n</option>");
	out.println("<option value=\"type\">Type</option>");
	out.println("<option value=\"status\">Status</option>");
	out.println("<option value=\"caseNum\">Case #</option>");
	out.println("<option value=\"claimNum\">Claim #</option>");
	out.println("<option value=\"doi DESC\">DOI</option>");
	out.println("<option value=\"filed DESC\">Filed Date</option>");
	out.println("<option value=\"closed DESC\">Closed Date</option>");
	out.println("<option value=\"empName\">Employee Name</option>");
	out.println("<option value=\"adjuster\">Adjuster</option>");
	out.println("<option value=\"attorney\">Attorney</option>");
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
	String username = "";
	boolean success = true, showAll=false;

	String [] titles = {"ID",
	    "Case Num",
	    "Type",
	    "Status",
	    "Defendants",

	    "Damage Amount",
	    "location",
	    "Claim #",
	    "Insurance",
	    "Adjuster Name",

	    "Employee Info",
	    "Vehicle VIN, Make, Model, Year",
	    "DOI",
	    "Filed",
	    "Judgment",
	    "Pro Supp",
	    "Closed"
			    
	};
	boolean [] show = {true, true, true, true, true,
	    true, true, true, true, true,
	    true, true, true, true, true, 
	    true, true
	};
	String action=""; 
	String  dateFrom="", dateTo="", whichDate="", whichAmount="",
	    amountFrom="",amountTo="", orderBy="";
	String claimNum="", caseNum="", location="", tortId="", 
	    adjuster="", 
	    adjusterPhone="",
	    attorney="", otherType="",
	    attorneyPhone="",
	    type="", id="", insured="",
	    insurance="", 
	    status="",  insurStatus="", empid="", recordOnly="",
	    empName="", empPhone="", dept="";
	String cityAutoInc = "", vin = "", autoPlate ="", autoNum="",
	    autoMake = "", autoModel = "", autoYear ="";
	//
	// defendant related
	String lname="",fname="",phone="",dob="",
	    defInsur="",defClaimNum="",
	    defAdjuster="",defInsurPhone="",defAttorney="",
	    defAttorneyPhone="";

	String message = "";
	Enumeration values = req.getParameterNames();
	String [] idArr = null;
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
	    else if (name.equals("otherType")) {
		otherType =value;
	    }
	    else if (name.equals("status")) {
		status =value;
	    }
	    else if (name.equals("tortId")) {
		tortId =value;
	    }
	    else if (name.equals("caseNum")) {
		caseNum = value;
	    }
	    else if (name.equals("insurance")) {
		insurance =value.toUpperCase();
	    }
	    else if (name.equals("insured")) {
		insured =value;
	    }
	    else if (name.equals("insurStatus")) {
		insurStatus =value;
	    }
	    else if (name.equals("claimNum")) {
		claimNum =value;
	    }
	    else if (name.equals("adjuster")) {
		adjuster =value.toUpperCase();
	    }
	    else if (name.equals("lname")) {
		lname =value.toUpperCase();
	    }
	    else if (name.equals("fname")) {
		fname =value.toUpperCase();
	    }
	    else if (name.equals("pone")) {
		phone =value;
	    }
	    else if (name.equals("dob")) {
		dob =value;
	    }
	    else if (name.equals("defInsur")) {
		defInsur =value.toUpperCase();
	    }
	    else if (name.equals("defAdjuster")) {
		defAdjuster =value.toUpperCase();
	    }
	    else if (name.equals("defAttorney")) {
		defAttorney =value.toUpperCase();
	    }
	    else if (name.equals("defClaimNum")) {
		defClaimNum =value;
	    }
	    else if (name.equals("defInsurPhone")) {
		defInsurPhone =value;
	    }
	    else if (name.equals("defAttorneyPhone")) {
		defAttorneyPhone =value;
	    }
	    else if (name.equals("adjusterPhone")) {
		adjusterPhone =value;  
	    }
	    else if (name.equals("attorney")) {
		attorney =value.toUpperCase();
	    }
	    else if (name.equals("attorneyPhone")) {
		attorneyPhone =value;  
	    }
	    else if (name.equals("cityAutoInc")) {
		cityAutoInc = value;
	    }
	    else if (name.equals("empName")) {
		empName = value.toUpperCase();
	    }
	    else if (name.equals("empPhone")) {
		empPhone = value;
	    }
	    else if (name.equals("dept")) {
		dept = value;
	    }
	    else if (name.equals("empid")) {
		empid = value;
	    }
	    else if (name.equals("vin")) {
		vin = value;
	    }
	    else if (name.equals("autoNum")) {
		autoNum = value;
	    }
	    else if (name.equals("autoMake")) {
		autoMake = value;
	    }
	    else if (name.equals("autoPlate")) {
		autoPlate = value;
	    }
	    else if (name.equals("autoModel")) {
		autoModel = value;
	    }
	    else if (name.equals("autoYear")) {
		autoYear = value;
	    }
	    else if (name.equals("whichDate")) {
		whichDate = value;
	    }
	    else if (name.equals("whichAmount")) {
		whichAmount = value;
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
	    else if (name.equals("location")) {
		location = value.toUpperCase();
	    }
	    else if (name.equals("orderBy")) {
		orderBy = value;
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
	//
	// Inserts
	out.println(Inserts.xhtmlHeaderInc);
	out.println(Inserts.banner(url));
	out.println(Inserts.menuBar(url, true));
	out.println(Inserts.sideBar(url));
	//
	out.println("<div id=\"mainContent\">");
	out.println("<h1>Search  Recovery Actions</h1>");

	boolean needJoin = false, match = true;
	SearchLegal sc = new SearchLegal(debug);
	Auto auto = null;
	List<Legal> legals = null;
	if(match){
	    if(!lname.equals("")){
		sc.setPersonName(lname);
	    }
	    else if(!fname.equals("")){
		sc.setPersonName(fname);
	    }
	    if(!phone.equals("")){
		sc.setPersonPhone(phone);
	    }
	    if(!dob.equals("")){
		sc.setPersonDob(dob);
	    }						
	    if(!id.equals("")){
		sc.setId(id);
	    }
	    if(!tortId.equals("")){
		// sc.setTortId(tortId);
	    }
	    if(!caseNum.equals("")){
		// sc.setCaseNum(caseNum);
	    }
	    if(!status.equals("")){
		sc.setStatus(status);
	    }
	    if(!type.equals("")){
		sc.setType(type);
	    }
	    if(!otherType.equals("")){
		sc.setOtherType(otherType);
	    }
	    if(!dateFrom.equals("")){
		sc.setDateFrom(dateFrom);
	    }
	    if(!dateTo.equals("")){
		sc.setDateTo(dateTo);
	    }
	    if(!amountFrom.equals("")){
		sc.setAmountFrom(amountFrom);
	    }
	    if(!amountTo.equals("")){
		sc.setAmountTo(amountTo);
	    }
	    if(!whichDate.equals("")){
		sc.setWhichDate(whichDate);
	    }
	    if(!whichAmount.equals("")){
		sc.setWhichAmount(whichAmount);
	    }
	    if(!claimNum.equals("")){
		sc.setClaimNum(claimNum);
	    }
	    if(!empid.equals("")){
		sc.setEmpid(empid);
	    }
	    if(!empName.equals("")){
		sc.setEmpName(empName);
	    }
	    if(!dept.equals("") && !dept.equals("0")){
		sc.setDept_id(dept);
	    }
	    if(!vin.equals("")){
		if(auto == null) auto = new Auto(debug);
		auto.setVin(vin);
	    }
	    if(!autoPlate.equals("")){
		if(auto == null) auto = new Auto(debug);
		auto.setAutoPlate(autoPlate);
	    }
	    if(!autoMake.equals("")){
		if(auto == null) auto = new Auto(debug);
		auto.setAutoMake(autoMake);
	    }
	    if(!autoNum.equals("")){
		if(auto == null) auto = new Auto(debug);
		auto.setAutoNum(autoNum);
	    }
	    if(!autoModel.equals("")){
		if(auto == null) auto = new Auto(debug);
		auto.setAutoModel(autoModel);
	    }
	    if(!autoYear.equals("")){
		if(auto == null) auto = new Auto(debug);
		auto.setAutoYear(autoYear);
	    }
	    if(!location.equals("")){
		sc.setLocation(location);
	    }
	    if(!insurance.equals("")){
		sc.setInsurance(insurance);
	    }
	    if(!insurStatus.equals("")){
		sc.setInsurStatus(insurStatus);
	    }
	    if(!insured.equals("")){
		sc.setInsured(insured);
	    }
	    if(!adjuster.equals("")){
		sc.setAdjuster(adjuster);
	    }
	    if(!adjusterPhone.equals("")){
		sc.setAdjusterPhone(adjusterPhone);
	    }
	    if(!attorney.equals("")){
		sc.setAttorney(attorney);
	    }
	    if(!attorneyPhone.equals("")){
		sc.setAttorneyPhone(attorneyPhone);
	    }
	    if(!cityAutoInc.equals("")){
		sc.setCityAutoInc(cityAutoInc);
	    }
	    if(!orderBy.equals("")){
		sc.setOrderBy(orderBy);
	    }
	    if(!recordOnly.equals("")){
		sc.setRecordOnly(recordOnly);
	    }
	    if(!defInsur.equals("")){
		sc.setDefInsur(defInsur);
	    }
	    if(!defClaimNum.equals("")){
		sc.setDefClaimNum(defClaimNum);
	    }
	    if(!defAdjuster.equals("")){
		sc.setDefAdjuster(defAdjuster);
	    }
	    if(!defInsurPhone.equals("")){
		sc.setDefInsurPhone(defInsurPhone);
	    }
	    if(!defAttorney.equals("")){
		sc.setDefAttorney(defAttorney);
	    }
	    if(!defAttorneyPhone.equals("")){
		sc.setDefAttorneyPhone(defAttorneyPhone);
	    }
	    if(auto != null){
		sc.setAuto(auto);
	    }
	    String msg = sc.lookFor();
	    if(match && msg.equals("")){
		legals = sc.getLegals();
		message += msg;
		success = false;
	    }
	    sc = null;
	}
	int ncnt = 0;
	if(legals != null){
	    ncnt = legals.size();
	    if(ncnt == 1){
		Legal one = legals.get(0);
		res.sendRedirect(url+"Legal?id="+one.getId());
		return;
	    }						
	}
	if(success){
	    if(!message.equals(""))
		out.println("<h3>"+message+"</h3>");
	}
	else{
	    if(!message.equals(""))
		out.println("<h3 class=\"errorMessages\">"+message+"</h3>");
	}
	out.println("<h4>Total Matching Records "+ ncnt +" </h4>");
	if(ncnt > 0){
	    out.println("<table class=\"box\">");
	    out.println("<tr>");
	    for (int c = 0; c < titles.length; c++){ 
		if(show[c] || showAll)
		    out.println("<th>"+titles[c]+"</th>");
	    }	   
	    out.println("</tr>");
	    for(Legal one:legals){
		String str = "";
		List<Employee> emps = null;
		List<Insurance> insurances = null;
		List<Auto> cars = null;					
		emps = one.getEmployees();
		insurances = one.getInsurances();
		cars = one.getAutos();
		out.println("<tr>");
		// ID
		out.println("<td><a href=\""+url+"Legal?"+
			    "action=zoom&id="+one.getId()+"\">"+
			    one.getId()+"</a></td>"); 
		if(show[1]){
		    str = one.getCaseNum();
		    if(str.equals("")) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		// Type
		if(show[2]){
		    str = one.getType();
		    String str2 = "&nbsp;";
		    if(!(str.equals("")||str.equals("0"))){
			RiskType rt = new RiskType(debug, str);
			rt.doSelect("unified");
			str2 = rt.getName();
			if(str2 == null || str2.equals(""))  str2="&nbsp;";
		    }
		    out.println("<td>"+str2+"</td>");
		}
		//
		// status
		if(show[3]){
		    str = one.getStatus();
		    if(str.equals("")) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(show[4]){
		    //
		    str = "";
		    String all = "", back="";
		    /*
		      String [] defArr = null;
		      HandlePerson hp = new HandlePerson(debug);
		      back = hp.generatePidList(one.getId(), "defendant");
		      if(back.equals("")){
		      defArr = hp.getPidArr();
		      }
		      if(defArr != null && defArr.length >0){
		      for(int j=0;j<defArr.length;j++){
		      RiskPerson rp = new RiskPerson(debug, defArr[j]);
		      back = rp.doSelect();
		      if(back.equals("")){
		    */
		    if(one.hasDefendants()){
			List<RiskPerson> defs = one.getDefendants();
			for(RiskPerson rp:defs){
			    if(!all.equals("")) all += "<br />";
			    str = rp.getFullName();
			    all += "<a href=\""+url+"RiskPersonServ?"+
				"id="+rp.getId()+
				"&action=zoom&inType=legal&risk_id="+
				one.getId()+"\">"+str+"</a> ";
			}
		    }
		    if(all.equals("")) all = "&nbsp;&nbsp;";
		    out.println("<td>"+all+"</td>");
		}
		if(show[5]){
		    str = one.getDamageAmnt();
		    if(str.equals("")) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(show[6]){
		    str = one.getLocation();
		    if(str.equals("")) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(show[7]){
		    String all = "";
		    if(insurances != null){
			for(Insurance ins:insurances){
			    str = ins.getClaimNum();
			    if(!str.equals("")){
				if(!all.equals("")) all += ", ";
				all += str;
				str = "&nbsp;";
			    }
			}
		    }
		    if(all.equals(" ")) all = "&nbsp;";
		    out.println("<td>"+all+"</td>");	
										
		}
		if(show[8]){
		    String all = "";
		    if(insurances != null){
			for(Insurance ins:insurances){
			    str = ins.getCompany();
			    if(!str.equals("")){
				if(!all.equals("")) all += ", ";
				all += str;
				str = "&nbsp;";
			    }
			}
		    }
		    if(all.equals(" ")) all = "&nbsp;";
		    out.println("<td>"+all+"</td>");
		}
		if(show[9]){
		    String all = "";
		    if(insurances != null){
			for(Insurance ins:insurances){
			    str = ins.getAdjuster()+" "+ins.getAdjusterPhone();;
			    if(!str.equals("")){
				if(!all.equals("")) all += ", ";
				all += str;
				str = "&nbsp;";
			    }
			}
		    }
		    if(all.equals(" ")) all = "&nbsp;";
		    out.println("<td>"+all+"</td>");
		}
		if(show[10]){
		    String all = "";
		    if(emps != null){
			for(Employee emp:emps){
			    str = emp.getName();
			    if(!all.equals("")) all += ", ";
			    all += str;
			}
		    }
		    out.println("<td>"+all+"</td>");
		}
		if(show[11]){
		    if(cars != null){
			for(Auto car: cars){
			    if(str.equals("")) str += "<br />";
			    str = car.getAutoInfo();
			}
		    }
		    if(str.equals("")) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(show[12]){
		    str = one.getDoi();
		    if(str.equals("")) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(show[13]){
		    str = one.getFiled();
		    if(str.equals("")) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(show[14]){
		    str = one.getJudgment();
		    if(str.equals("")) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(show[15]){
		    str = one.getProSupp();
		    if(str.equals("")) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(show[16]){
		    str = one.getClosed();
		    if(str.equals("")) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		out.println("</tr>");
	    }
	}
	out.println("</table>");
	out.println("</div>");
	out.println("</body></html>");
	out.close();
	//	
    }

}























































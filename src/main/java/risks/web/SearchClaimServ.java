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

@WebServlet(urlPatterns = {"/SearchClaimServ","/SearchClaim"})
public class SearchClaimServ extends TopServlet{

    static Logger logger = LogManager.getLogger(SearchClaimServ.class);
    static List<Department> deptList = null;
    static RiskTypeList types = null;
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

	String  dateFrom="", dateTo="", whichDate="", whichAmount="",
	    amountFrom="",amountTo="";
	String claimNum="", 
	    adjuster="", 
	    adjusterPhone="", attorney="",attorneyPhone="",
	    type="", id="",  law_firm_id="",
	    status="",  insuranceStatus="", empid="",
	    empName="", empPhone="", dept_id="", policy="",
	    insurer="";
	
	String incident = "", vin="", 
	    cityAutoInc="", autoPlate="", autoNum="",
	    autoMake ="", autoModel="", autoYear="";

	List<LawFirm> firms = null;
	Enumeration values = req.getParameterNames();
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
	    else if (name.equals("claimNum")) {
		claimNum =value;
	    }
	    else if (name.equals("adjuster")) {
		adjuster =value;
	    }
	    else if (name.equals("adjusterPhone")) {
		adjusterPhone =value;  
	    }
	    else if (name.equals("policy")) {
		policy =value;
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
	    else if (name.equals("dept_id")) {
		dept_id = value;
	    }
	    else if (name.equals("law_firm_id")) {
		law_firm_id = value;
	    }						
	    else if (name.equals("empid")) {
		empid = value;
	    }
	    else if (name.equals("vin")) {
		vin = value;
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
	    else if (name.equals("incident")) {
		incident = value;
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
	if(deptList == null){
	    DeptList dpl = new DeptList(debug);
	    String msg = dpl.lookFor();
	    if(msg.equals("")){
		deptList = dpl.getDepartments();
	    }
	    else {
		message += " "+msg;
		success = false;
		logger.error(msg);
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
	if(firms == null){
	    LawFirmList lfl = new LawFirmList(debug);
	    String msg = lfl.lookFor();
	    if(!msg.equals("")){
		message += msg;
		success = false;
		logger.error(msg);
	    }
	    else{
		firms = lfl.getFirms();
	    }
	}				
	// Inserts
	out.println(Inserts.xhtmlHeaderInc);
	out.println(Inserts.banner(url));
	out.println(Inserts.menuBar(url, true));
	out.println(Inserts.sideBar(url));
	//
	out.println("<div id=\"mainContent\">");
	out.println("<h3 class=\"titleBar\">Search Tort Claims</h3>");
	//
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
	out.println("<fieldset><legend>Claim Status</legend>");
	out.println("<table>");
	out.println("<tr><td><label for=\"id\">Claim ID:</label>");
	out.println("<input name=\"id\" id=\"id\" value=\""+id+"\""+
		    " size=\"8\" maxlength=\"8\" /></td></tr>");
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
	out.println("</select><label for='otherType'>Other Type: </label>");
	out.println("<input type='test' name='otherType' size='15'>");
	out.println("<label for=\"status\"> Status:</label>"+
		    "<select name=\"status\" id=\"status\">");
	for(int i=0;i<Inserts.searchStatusArr.length;i++){
	    if(status.equals(Inserts.searchStatusArr[i]))
		out.println("<option selected=\"selected\">"+status+
			    "</option>");
	    else
		out.println("<option>"+Inserts.searchStatusArr[i]+"</option>");
	}
	out.println("</select> &nbsp;&nbsp;");
	out.println("<input type='checkbox' name='recordOnly' value='y'>");
	out.println("<label>Record Only</label>");
	out.println("</td></tr>");
	out.println("<tr><td>");
	out.println("<label>Claim:</label>");
	out.println("<input type='radio' name='filed' value='y'>");
	out.println("<label>Filed Only</label>");
	out.println("<input type='radio' name='filed' value='n'>");
	out.println("<label>Potential Only</label>");
	out.println("<input type='radio' name='filed' checked value='all'>");
	out.println("<label>All</label></td></tr>");
	out.println("<tr><td><label for=\"incident\">Incident:</label>");
	out.println("<input name=\"incident\" id=\"insident\" size=\"30\" "+
		    "value=\""+incident+"\" "+
		    "maxlength=\"50\">");
	out.println("</td></tr>");
	out.println("</table>");
	out.println("</fieldset>");
	out.println("<fieldset><legend>Insurance</legend>");
	out.println("<table>");
	out.println("<tr><td><label for=\"insurer\">Insurance:</label>");
	out.println("<input name=\"insurer\" id=\"insurer\" size=\"30\" "+
		    "value=\""+insurer+"\" maxlength=\"50\">");
	out.println("</td></tr>");
	out.println("<tr><td><label for=\"adjuster\">Adjuster's Name:</label>"+
		    "<input name=\"adjuster\" id=\"adjuster\" value=\""+
		    adjuster+"\" size=\"20\" maxlength=\"30\"/ >");
	out.println("<label for=\"adjusterPhone\"> Phone: </label>"+
		    "<input name=\"adjusterPhone\" "+
		    "id=\"adjusterPhone\" value=\""+
		    adjusterPhone+"\" size=\"16\" maxlength=\"16\"/ >"+
		    "</td></tr>");
	out.println("<tr><td><label for=\"adjuster\">Attorney's Name:</label>"+
		    "<input name=\"attorney\" id=\"attorney\" value=\""+
		    attorney+"\" size=\"20\" maxlength=\"30\"/ >");
	out.println("<label for=\"attorneyPhone\"> Phone: </label>"+
		    "<input name=\"attorneyPhone\" "+
		    "id=\"attorneyPhone\" value=\""+
		    attorneyPhone+"\" size=\"16\" maxlength=\"16\"/ >"+
		    "</td></tr>");
	out.println("<tr><td><label for=\"claimNum\">Claim #:</label>"+
		    "<input name=\"claimNum\" id=\"claimNum\" value=\""+
		    claimNum+"\" size=\"20\" maxlength=\"20\"/ >");
	out.println("<label for=\"insuranceStatus\"> Insurance Status: "+
		    "</label><select name=\"insuranceStatus\" "+
		    "id=\"insuranceStatus\">");
	for(int i=0;i<Inserts.insurStatusArr.length;i++){
	    if(insuranceStatus.equals(Inserts.insurStatusArr[i]))
		out.println("<option selected=\"selected\">"+insuranceStatus+
			    "</option>");
	    else
		out.println("<option>"+Inserts.insurStatusArr[i]+"</option>");
	}
	out.println("</select><label for=\"policy\">Policy:</label>");
	out.println("<select name=\"policy\" id=\"policy\">");
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
	out.println("<fieldset><legend>City</legend>");
	out.println("<table>");
	out.println("<tr><td>");
	out.println("<label for=\"empName\">Employee Name:</label>"+
		    "<input name=\"empName\" id=\"empName\" value=\""+
		    empName+"\" size=\"20\" maxlength=\"30\"/ ></td></tr>");
	//
	out.println("<tr><td><label for=\"dept_id\">Employee Department:</label>");
	out.println("<select name=\"dept_id\" id=\"dept_id\">");
	out.println("<option value=\"\">&nbsp;</option>");
	if(deptList != null){
	    for(Department dp:deptList){
		if(dept_id.equals(dp.getId()))
		    out.println("<option value=\""+dept_id+"\""+
				" selected=\"selected\">"+dp.getInfo()+
				"</option>");
		else
		    out.println("<option value=\""+dp.getId()+
				"\" >"+dp.getInfo()+"</option>");
	    }
	}
	out.println("</select></td></tr>");
	out.println("<tr><td><label for=\"empPhone\">Employee Work Phone:"+
		    "</label>"+
		    "<input name=\"empPhone\" id=\"empPhone\" value=\""+
		    empPhone+"\" size=\"10\" maxlength=\"10\"/ >");
	out.println("<label for=\"cityAutoInc\">City Vehichle Included? "+
		    "</label>"+
		    "<input type=\"checkbox\" name=\"cityAutoInc\" "+
		    "id=\"cityAutoInc\" value=\"y\" "+cityAutoInc+
		    ">Yes</td></tr>");
	//
	out.println("<tr><td><label for=\"vin\">Vehicle, VIN #:</label>"+
		    "<input name=\"vin\" id=\"vin\" value=\""+
		    vin+"\" size=\"15\" maxlength=\"20\" />");
	out.println("<label for=\"autoPlate\">Plate:</label>"+
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
	out.println("<fieldset><legend>Other</legend>");
	out.println("<table>");
	out.println("<tr><td><label>Dates (select one):</label>");
	out.println("<input type=\"radio\" name=\"whichDate\" checked "+
		    "id=\"whichDate\" value=\"incidentDate\">Date of "+
		    "Incident</input>");
	out.println("<input type=\"radio\" name=\"whichDate\" "+
		    "id=\"whichDate\" value=\"expires\"> Expires </input>");
	out.println("<input type=\"radio\" name=\"whichDate\" "+
		    "id=\"whichDate\" value=\"sent\">Sent to Insurance</input>");
	out.println("<input type=\"radio\" name=\"whichDate\" "+
		    "id=\"whichDate\" value=\"received\">Received</input>");
	out.println("<input type=\"radio\" name=\"whichDate\" "+
		    "id=\"whichDate\" value=\"closed\">Closed</input>");
	//
	out.println("</td></tr>");
	out.println("<tr><td><label for=\"dateFrom\">Date, from:</label>");
	out.println("<input name=\"dateFrom\" size=\"10\" "+
		    "id=\"dateFrom\" value=\""+dateFrom+"\" />");
	out.println("<label for=\"dateTo\"> to:</label>");
	out.println("<input name=\"dateTo\" size=\"10\" "+
		    "id=\"dateTo\" value=\""+dateTo+"\" />");
	out.println("</td></tr>");
	//
	out.println("<tr><td><label>Monetary Amount (select one):</label>");
	out.println("<input type=\"radio\" name=\"whichAmount\" "+
		    "id=\"whichAmount\" value=\"requestAmount\"/ >Claimant Requested, ");
	out.println("<input type=\"radio\" name=\"whichAmount\" "+
		    "id=\"whichAmount\" value=\"deductible\"/ >Deductible, ");
	out.println("<input type=\"radio\" name=\"whichAmount\" "+
		    "id=\"whichAmount\" value=\"paidByCity\"/ >Paid by City to Claimant, ");
	out.println("<input type=\"radio\" name=\"whichAmount\" "+
		    "id=\"whichAmount\" value=\"paidByRisk\"/ >Paid by Risk to Claimant, ");
	out.println("<input type=\"radio\" name=\"whichAmount\" "+
		    "id=\"whichAmount\" value=\"paidByInsur\"/ >Paid by Insurance to Claimant, ");
	out.println("<input type=\"radio\" name=\"whichAmount\" "+
		    "id=\"whichAmount\" value=\"paidByInsur2City\"/ >Paid by Insurance to City, ");
	out.println("<input type=\"radio\" name=\"whichAmount\" "+
		    "id=\"whichAmount\" value=\"cityTotalCost\"/ >City Damage Total Cost, ");
	out.println("</td></tr>");
	out.println("<tr><td><label for=\"amountFrom\">Amount, from:</label>");
	out.println("<input name=\"amountFrom\" "+
		    "id=\"amountFrom\" value=\""+amountFrom+"\" size=\"8\" />");
	out.println("<label for=\"amountTo\"> to:</label>");
	out.println("<input name=\"amountTo\" "+
		    "id=\"amountTo\" value=\""+amountTo+"\" size=\"8\" />");
	out.println("</td></tr>");
	if(firms != null && firms.size() > 0){
	    out.println("<tr><td><label for=\"law_firm_id\">Law Firm:</label>");
	    out.println("<select name=\"law_firm_id\" >");
	    out.println("<option value=\"\">All</option>");
	    for(LawFirm one:firms){
		out.println("<option value=\""+one.getId()+"\">"+one.getName()+"</option>");
	    }
	    out.println("</select></td></tr>");
	}
	out.println("<tr><td><label for=\"orderBy\">Sort by:</label>");
	out.println("<select name=\"orderBy\" id=\"orderBy\">");
	out.println("<option value=\"\">\n</option>");
	out.println("<option value=\"claimNum\">Claim #</option>");
	out.println("<option value=\"doi DESC\">Incident Date</option>");
	out.println("<option value=\"sent DESC\">Sent Date</option>");
	out.println("<option value=\"received DESC\">Received Date</option>");
	out.println("<option value=\"type\">Type</option>");
	out.println("<option value=\"status\">Status</option>");
	out.println("<option value=\"empName\">Employee Name</option>");
	out.println("<option value=\"adjuster\">Adjuster</option>");
	out.println("<option value=\"attorney\">Attorney</option>");
	out.println("</select></td></tr>");
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
	String [] titles = {"Claim ID",
	    "Claim Num",
	    "Claimant",

	    "Type",
	    "Status",
	    "Policy",

	    "Incident",
	    "Adjuster",
	    "Employee Name",
	    "Auto VIN, make, model, year",
	    "City Auto Included",

	    "Incident Date",
	    "Claim Sent",
	    "Claim Received",
	    "Closed",
	    "Amount Requested",

	    "Amount Settled",
	    "Paid by City",
	    "Paid by Insurance",
	    "Misc Paid by City",
	};
	boolean [] show = {true, true, true, true, true,
	    true, true, true, true, true,
	    true, true, true, true, true,
	    true, true, true, true, true
	};
	String action=""; 
	String  dateFrom="", dateTo="", whichDate="", whichAmount="",
	    amountFrom="",amountTo="", orderBy="";
	String claimNum="", 
	    adjuster="", 
	    adjusterPhone="",
	    attorney="",attorneyPhone="",
	    type="", id="", filed="", insurer="",
	    status="",  insuranceStatus="", empid="", otherType="",
	    empName="", empPhone="", dept_id="", policy="", reocrdOnly="",
	    comments="", recordOnly="";
	String incident = "", 
	    cityAutoInc="", vin="", autoPlate="", autoNum="",
	    autoMake ="", autoModel="", autoYear="";

	String message = "";
	Enumeration values = req.getParameterNames();
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
	    else if (name.equals("policy")) {
		policy =value;
	    }
	    else if (name.equals("filed")) {
		filed =value;
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
	    else if (name.equals("attorney")) {
		attorney =value.toUpperCase();
	    }
	    else if (name.equals("attorneyPhone")) {
		attorneyPhone =value;  
	    }
	    else if (name.equals("policy")) {
		policy =value;
	    }
	    else if (name.equals("recordOnly")) {
		recordOnly =value;
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
	    else if (name.equals("dept_id")) {
		dept_id = value;
	    }
	    else if (name.equals("empid")) {
		empid = value;
	    }
	    else if (name.equals("vin")) {
		vin = value;
	    }
	    else if (name.equals("autoPlate")) {
		autoPlate = value.toUpperCase();
	    }
	    else if (name.equals("autoMake")) {
		autoMake = value.toUpperCase();
	    }
	    else if (name.equals("autoModel")) {
		autoModel = value.toUpperCase();
	    }
	    else if (name.equals("autoNum")) {
		autoNum = value;
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
	    else if (name.equals("incident")) {
		incident = value.toUpperCase();
	    }
	    else if (name.equals("insurer")) {
		insurer = value.toUpperCase();
	    }
	    else if (name.equals("orderBy")) {
		orderBy = value;
	    }
	    else if (name.equals("action")){
		action = value;  
	    }
	    //  System.err.println(name+" "+value);
	}
	//
	// Inserts
	out.println(Inserts.xhtmlHeaderInc);
	out.println(Inserts.banner(url));
	out.println(Inserts.menuBar(url, true));
	out.println(Inserts.sideBar(url));
	//
	out.println("<div id=\"mainContent\">");
	out.println("<h1>Search Tort Claims</h1>");

	TortClaimList sc = new TortClaimList(debug);
	Auto auto = null;
	if(!id.equals("")){
	    sc.setId(id);
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
	if(!policy.equals("")){
	    sc.setPolicy(policy);
	}
	if(!recordOnly.equals("")){
	    sc.setRecordOnly(recordOnly);
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
	if(!dept_id.equals("")){
	    sc.setDept_id(dept_id);
	}
	if(!vin.equals("")){
	    sc.setVin(vin);
	}
	if(!autoNum.equals("")){
	    sc.setAutoNum(autoNum);
	}
	if(!filed.equals("all")){
	    sc.setFiled(filed);
	}
	if(!autoMake.equals("")){
	    sc.setAutoMake(autoMake);
	}
	if(!autoPlate.equals("")){
	    sc.setAutoPlate(autoPlate);
	}
	if(!autoModel.equals("")){
	    sc.setAutoModel(autoModel);
	}
	if(!autoYear.equals("")){
	    sc.setAutoYear(autoYear);
	}
	if(!policy.equals("")){
	    sc.setPolicy(policy);
	}
	if(!incident.equals("")){
	    sc.setIncident(incident);
	}
	if(!cityAutoInc.equals("")){
	    sc.setCityAutoInc(cityAutoInc);
	}
	if(!insurer.equals("")){
	    sc.setInsurer(insurer);
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
	if(!orderBy.equals("")){
	    sc.setOrderBy(orderBy);
	}
	int ncnt = 0;				
	List<TortClaim> torts = null;
	String msg = sc.lookFor();
	if(!msg.equals("")){
	    logger.error(msg);
	    message += msg;
	    success = false;
	}
	else{
	    torts = sc.getTorts();
	    if(torts != null && torts.size() > 0){
		ncnt = torts.size();
		if(ncnt == 1){
		    TortClaim tort = torts.get(0);
		    res.sendRedirect(url+"TortClaim?id="+tort.getId());
		    return;
		}
	    }
	}
				
	String str = "";
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
	    out.println("<table class='box'>");
	    out.println("<tr>");
	    for (int c = 0; c < titles.length; c++){ 
		if(show[c] || showAll)
		    out.println("<th>"+titles[c]+"</th>");
	    }	   
	    out.println("</tr>");
	    for(TortClaim tc:torts){
		List<Employee> emps = null;
		List<Insurance> insurances = null;
		List<Auto> cars = null;
		emps = tc.getEmployees();
		insurances = tc.getInsurances();
		cars = tc.getAutos();
		out.println("<tr>");
		// ID 
		out.println("<td><a href=\""+url+
			    "TortClaim?"+
			    "action=zoom&id="+tc.getId()+"\">"+
			    tc.getId()+"</a></td>"); 
		//
		if(show[1]){
		    String all="";
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
		    if(all.equals("")) all = "&nbsp;";
		    out.println("<td>"+all+"</td>");						
		}
		if(show[2]){
										
		    str = "";
		    String all = "";
		    /*
		      String [] defArr = null;
		      HandlePerson hp = new HandlePerson(debug);
		      back = hp.generatePidList(tc.getId(), "claimant");
		      if(back.equals("")){
		      defArr = hp.getPidArr();
		      }
		      if(defArr != null && defArr.length >0){
		    */
		    if(tc.hasClaiments()){
			List<RiskPerson> claiments = tc.getClaiments();
			// RiskPerson rp = new RiskPerson(debug);
			for(RiskPerson rp:claiments){
			    // for(int j=0;j<defArr.length;j++){
			    // rp = new RiskPerson(debug, defArr[j]);
			    // back = rp.doSelect();
			    // if(back.equals("")){
			    if(!all.equals("")) all += "<br />";
			    str = rp.getFullName();
			    all += "<a href=\""+url+"RiskPersonServ?"+
				"id="+rp.getId()+
				"&action=zoom&inType=tort&risk_id="+
				tc.getId()+"\">"+str+"</a> ";
			}
		    }
		    if(all.equals("")) all = "&nbsp;&nbsp;";
		    out.println("<td>"+all+"</td>");
		}
		// Type
		if(show[3]){
		    String str2 = "&nbsp;";
		    str = tc.getType();
		    if(!(str.equals("")||str.equals("0"))){
			if(types != null){
			    for(RiskType rtype:types){
				if(str.equals(rtype.getId())){
				    str2 = rtype.getName();
				    break;
				}
			    }
			}
		    }
		    out.println("<td>"+str2+"</td>");
		}
		//
		// status
		if(show[4]){
		    str = tc.getStatus();
		    if(str.equals("")) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(show[5]){
		    String all="";
		    if(insurances != null){
			for(Insurance ins:insurances){
			    str = ins.getPolicy();
			    if(!str.equals("")){
				if(!all.equals("")) all += ", ";
				all += str;
				str = "&nbsp;";
			    }
			}
		    }
		    if(all.equals("")) all = "&nbsp;";
		    out.println("<td>"+all+"</td>");
		}
		if(show[6]){
		    str = tc.getIncident();
		    if(str.equals("")) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(show[7]){
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
		if(show[8]){
		    String all = "";
		    if(emps != null){
			for(Employee emp:emps){
			    str = emp.getFullName();
			    if(!all.equals("")) all += ", ";
			    all += str;
			}
		    }
		    if(all.equals("")) str = "&nbsp;";
		    out.println("<td>"+all+"</td>");
		}
		if(show[9]){
		    str = "";
		    String all = "";
		    if(cars != null){
			for(Auto car:cars){
			    if(car != null){
				if(!all.equals("")) all += "<br />";
				all += car.getAutoInfo();
			    }
			}
		    }
		    if(all.equals("")) all = "&nbsp;";
		    out.println("<td>"+all+"</td>");
		}
		if(show[10]){
		    str = tc.getCityAutoInc();
		    if(str.equals("")) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(show[11]){
		    str = tc.getIncidentDate();
		    if(str.equals("")) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(show[12]){
		    str = tc.getSent();
		    if(str.equals("")) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(show[13]){
		    str = tc.getReceived();
		    if(str.equals("")) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(show[14]){
		    str = tc.getClosed();
		    if(str.equals("")) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(show[15]){
		    str = tc.getRequestAmount();
		    if(str.equals("")|| str.equals("0")) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(show[16]){
		    str = tc.getSettled();
		    if(str.equals("")|| str.equals("0")) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(show[17]){
		    str = tc.getPaidByCity();
		    if(str.equals("")||str.equals("0")) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(show[18]){
		    str = tc.getPaidByInsur();
		    if(str.equals("")|| str.equals("0")) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(show[19]){
		    str = tc.getMiscByCity();
		    if(str.equals("")||str.equals("0")) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		out.println("</tr>");
	    }
	    out.println("</table>");
	}
	//
	out.println("</div>");
	out.println("</body></html>");
	out.close();

    }

}























































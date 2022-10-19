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

@WebServlet(urlPatterns = {"/SearchSafetyServ","/SearchSafety"})
public class SearchSafetyServ extends TopServlet{

    static Logger logger = LogManager.getLogger(SearchSafetyServ.class);
    String typeIdArr[] = null;
    String typeArr[] = null;
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
	RiskTypeList types = null;
	String action="", message ="";

	String  dateFrom="", dateTo="", whichDate="",
	    amountFrom="",amountTo="";
	String 
	    type="", id="", tortId="", vsId="",
	    status="", empid="", empInjured="",
	    empName="", deptPhone="", dept="", empSuper="";
	
	String damage = "", 
	    autoPlate="", vin="",
	    autoMake ="", autoModel="", autoYear="", autoNum="",
	    accidLocation="", anyEstPlace="", accidTime="", 
	    chosenDealer="", recordOnly="",
	    insurance="",adjuster="",adjusterPhone="",attorney="",
	    attorneyPhone="",claimNum="",insuranceStatus="", policy="";
	Enumeration values = req.getParameterNames();
	String [] vals;
	List<Department> depts = null;
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
	    else if (name.equals("dept")) {
		dept = value;
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
		message += msg;
		success = false;
		logger.error(msg);
		types = null;
	    }
	}
	// Inserts
	out.println(Inserts.xhtmlHeaderInc);
	out.println(Inserts.banner(url));
	out.println(Inserts.menuBar(url, true));
	out.println(Inserts.sideBar(url));
	//
	out.println("<div id=\"mainContent\">");
	out.println("<h3 class=\"titleBar\">Search Internal Accidents (Safety)</h3>");
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
	out.println("<tr><td><label for=\"id\">Safety Record ID:</label>");
	out.println("<input name=\"id\" id=\"id\" value=\""+id+"\""+
		    " size=\"8\" maxlength=\"8\" /></td></tr>");
	out.println("<tr><td><label for=\"tortId\">Related to Tort Claim ID:</label>");
	out.println("<input name=\"tortId\" id=\"tortId\" value=\""+tortId+"\""+
		    " size=\"8\" maxlength=\"8\" />");
	out.println("<label for=\"vsId\">, Related to Recovery Actions ID:</label>");
	out.println("<input name=\"vsId\" id=\"vsId\" value=\""+vsId+"\""+
		    " size=\"8\" maxlength=\"8\" /></td></tr>");

	out.println("<tr><td>");
	out.println("<label for=\"type\">Type:</label>");
	out.println("<select name=\"type\" id=\"type\">");
	if(types != null){
	    for(RiskType rt: types){
		out.println("<option value='"+rt.getId()+
			    "'>"+rt.getName()+"</option>");
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
	out.println("</select>");
	out.println("</td></tr>");
	out.println("<tr><td><label>Record Only? </label>"+
		    "<input type='checkbox' name='recordOnly' value='y'> Yes");
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
	out.println("<tr><td><label for=\"dept\">Department:</label>");
	out.println("<select name=\"dept\" id=\"dept\">");
	out.println("<option value=''>");
	if(depts != null){
	    for(Department dep:depts){
		out.println("<option value='"+dep.getId()+"'>"+dep.getInfo());
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
	out.println("<tr><td><label for=\"autoDamage\">City Vehicle Damaged?</label>");
	out.println("<input type=\"checkbox\" name=\"autoDamage\" "+
		    "onchange=\"refreshPage()\" "+
		    "id=\"autoDamage\" value=\"y\">Yes,");
	out.println("<label for=\"propDamage\">City Property Damaged?</label>");
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
	out.println("<label for=\"insurStatus\"> Insurance Status: "+
		    "</label><select name=\"insurStatus\" "+
		    "id=\"insurStatus\">");
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
	out.println("<fieldset><legend>Other</legend>");
	out.println("<table>");
	//
	out.println("<tr><td><label>Dates (select one):</label>");
	out.println("<input type=\"radio\" name=\"whichDate\"  "+
		    "id=\"whichDate\" value=\"reported\">Reported</input>");
	out.println("<input type=\"radio\" name=\"whichDate\" checked "+
		    "id=\"whichDate\" value=\"accidDate\">Accident Date</input>");
	out.println("</td></tr>");
	out.println("<tr><td><label for=\"dateFrom\">Date, from:</label>");
	out.println("<input name=\"dateFrom\" size='10' maxlength='10' "+
		    "id='dateFrom' value='"+dateFrom+"' />");
	out.println("<label for=\"dateTo\"> to:</label>");
	out.println("<input name='dateTo' size='10' maxlength='10' "+
		    "id='dateTo' value='"+dateTo+"' />");
	out.println("</td></tr>");
	//
	out.println("<tr><td><label>Monetary Amount (select one):</label>");
	out.println("<input type=\"radio\" name=\"whichAmount\" checked "+
		    "id=\"whichAmount\" value=\"totalCost\">Total Vehicle Repair</input>");
	out.println("<input type=\"radio\" name=\"whichAmount\" "+
		    "id=\"whichAmount\" value=\"totalCostP\">Total Property Repair</input>");
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
	out.println("<option value=\"type\">Type</option>");
	out.println("<option value=\"status\">Status</option>");
	out.println("<option value=\"reported DESC\">Reported Date</option>");
	out.println("<option value=\"accidDate DESC\">Accident Date</option>");
	out.println("<option value=\"empName\">Employee Name</option>");
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
	String action="", message ="", orderBy="";

	String  dateFrom="", dateTo="", whichDate="",
	    amountFrom="",amountTo="";
	String 
	    type="", id="", tortId="", vsId="", otherType="",
	    status="", empid="", empInjured="",
	    empName="", deptPhone="", dept="", empSuper="";
	
	String damage = "", 
	    autoPlate="", vin="",
	    autoMake ="", autoModel="", autoYear="", autoNum="",
	    accidLocation="", anyEstPlace="", accidTime="", 
	    chosenDealer="", recordOnly="";
	String autoDamage="",propDamage="", whichAmount="",
	    insurance="",adjuster="",adjusterPhone="",attorney="",
	    attorneyPhone="",claimNum="",insurStatus="", policy="";

	//
	String [] titles = {"ID",
	    "Status",
	    "Type",

	    "Employee Name",
	    "Employee Injured",
	    "Department",
	    "Supervisor",
	    "Damage Details",

	    "Auto VIN, make, model, year",
	    "Reported",
	    "Accident Date",
	    "Accident Location",
	    "Estimated Costs",
	    "Chosen Dealer"
	};

	boolean [] show = {true, true, true, true, true,
	    true, true, true, true, true,
	    true, true, true, true
	};


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
	    else if (name.equals("tortId")){
		tortId = value;
	    }
	    else if (name.equals("vsId")){
		vsId = value;
	    }
	    else if (name.equals("dept")) {
		dept = value;
	    }
	    else if (name.equals("empid")) {
		empid = value;
	    }
	    else if (name.equals("empName")) {
		empName = value.toUpperCase();
	    }
	    else if (name.equals("deptPhone")) {
		deptPhone = value;
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
	    else if (name.equals("autoNum")) {
		autoNum =value;
	    }
	    else if (name.equals("autoMake")) {
		autoMake =value;
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
	    else if (name.equals("whichAmount")) {
		whichAmount = value;
	    }
	    else if (name.equals("amountFrom")) {
		amountFrom = value;
	    }
	    else if (name.equals("amountTo")) {
		amountTo = value;
	    }
	    else if (name.equals("damage")) {
		damage = value.toUpperCase();
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
	    else if (name.equals("autoDamage")) {
		autoDamage = value;
	    }
	    else if (name.equals("propDamage")) {
		propDamage = value;
	    }
	    else if (name.equals("insurance")) {
		insurance = value.toUpperCase();
	    }
	    else if (name.equals("adjuster")) {
		adjuster = value.toUpperCase();
	    }
	    else if (name.equals("adjusterPhone")) {
		adjusterPhone = value;
	    }
	    else if (name.equals("attorney")) {
		attorney = value.toUpperCase();
	    }
	    else if (name.equals("attorneyPhone")) {
		attorneyPhone = value;
	    }
	    else if (name.equals("claimNum")) {
		claimNum = value;
	    }
	    else if (name.equals("insurStatus")) {
		insurStatus = value;
	    }
	    else if (name.equals("policy")) {
		policy = value;
	    }
	    else if (name.equals("orderBy")){
		orderBy = value;  
	    }
	    else if (name.equals("recordOnly")){
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
	// Inserts@version %I% %
	out.println(Inserts.xhtmlHeaderInc);
	out.println(Inserts.banner(url));
	out.println(Inserts.menuBar(url, true));
	out.println(Inserts.sideBar(url));

	out.println("<div id=\"mainContent\">");
	SearchSafety sc = new SearchSafety(debug);
	Auto auto = null;
	if(!id.equals("")){
	    sc.setId(id);
	}
	if(!tortId.equals("")){
	    sc.setTortId(tortId);
	}
	if(!vsId.equals("")){
	    sc.setVsId(vsId);
	}
	if(!insurance.equals("")){
	    sc.setInsurance(insurance);
	}
	if(!insurStatus.equals("")){
	    sc.setInsurStatus(insurStatus);
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
	if(!claimNum.equals("")){
	    sc.setClaimNum(claimNum);
	}
	if(!policy.equals("")){
	    sc.setPolicy(policy);
	}
	if(!recordOnly.equals("")){
	    sc.setRecordOnly(recordOnly);
	}
	if(!autoDamage.equals("")){
	    sc.setAutoDamage(autoDamage);
	}
	if(!propDamage.equals("")){
	    sc.setPropDamage(propDamage);
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
	    sc.setAmountFrom(dateFrom);
	}
	if(!amountTo.equals("")){
	    sc.setAmountTo(dateFrom);
	}
	if(!whichDate.equals("")){
	    sc.setWhichDate(whichDate);
	}
	if(!whichAmount.equals("")){
	    sc.setWhichAmount(whichAmount);
	}
	if(!empid.equals("")){
	    sc.setEmpid(empid);
	}
	if(!empName.equals("")){
	    sc.setEmpName(empName);
	}
	if(!empInjured.equals("")){
	    sc.setEmpInjured(empInjured);
	}
	if(!dept.equals("")){
	    sc.setDept(dept);
	}
	if(!deptPhone.equals("")){
	    sc.setDeptPhone(deptPhone);
	}
	if(!accidLocation.equals("")){
	    sc.setAccidLocation(accidLocation);
	}
	if(!accidTime.equals("")){
	    sc.setAccidTime(accidTime);
	}
	if(!vin.equals("")){
	    if(auto == null) auto =new Auto(debug);
	    auto.setVin(vin);
	}
	if(!autoMake.equals("")){
	    if(auto == null) auto =new Auto(debug);
	    auto.setAutoMake(autoMake);
	}
	if(!autoModel.equals("")){
	    if(auto == null) auto =new Auto(debug);
	    auto.setAutoModel(autoModel);
	}
	if(!autoYear.equals("")){
	    if(auto == null) auto =new Auto(debug);
	    auto.setAutoYear(autoYear);
	}
	if(!autoPlate.equals("")){
	    if(auto == null) auto =new Auto(debug);
	    auto.setAutoPlate(autoPlate);
	}
	if(!autoNum.equals("")){
	    if(auto == null) auto =new Auto(debug);
	    auto.setAutoNum(autoNum);
	}
	if(!damage.equals("")){
	    sc.setDamage(damage);
	}
	if(!orderBy.equals("")){
	    sc.setOrderBy(orderBy);
	}
	if(auto != null){
	    sc.setAuto(auto);
	}
	List<Safety> safetys = null;
	int ncnt = 0;
	String msg = sc.lookFor();
	if(msg.equals("")){
	    safetys = sc.getSafetyList();
	    if(safetys != null){
		ncnt = safetys.size();
		if(ncnt == 1){
		    Safety one = safetys.get(0);
		    res.sendRedirect(url+"Safety?id="+one.getId());
		    return;
		}
	    }
	    else{
		message = "No Match found ";
	    }
	}
	else{
	    message += msg;
	    success = false;
	    logger.error(msg);
	}

	String str = "";

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
	    for(Safety sf:safetys){
		List<Employee> emps = null;
		List<Insurance> insurances = null;
		List<Auto> cars = null;						
		emps = sf.getEmployees();
		insurances = sf.getInsurances();
		cars = sf.getAutos();
		out.println("<tr>");
		// 
		// ID
		out.println("<td><a href=\""+url+"SafetyServ?"+
			    "action=zoom&id="+sf.getId()+"\">"+
			    sf.getId()+"</a></td>"); 
		//
		// status
		if(show[1]){
		    str = sf.getStatus();
		    if(str.equals("")) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		// Type
		if(show[2]){
		    String str2 = "&nbsp;";
		    str = sf.getType();
		    if(!(str.equals("")||str.equals("0"))){
			RiskType rt = new RiskType(debug, str);
			str = rt.doSelect("unified");
			if(str.equals("")){
			    str2 = rt.getName();
			}
		    }
		    out.println("<td>"+str2+"</td>");
		}
		//
		// EmpName
		if(show[3]){
		    str = "";
		    String all = "";
		    if(emps != null){
			for(Employee emp:emps){
			    str = emp.getFullName();
			    if(str != null && !str.equals("")){
				if(!all.equals("")) all += ", ";
				all += str;
			    }
			}
		    }
		    if(all.equals("")) all = "&nbsp;";
		    out.println("<td>"+all+"</td>");						
		}
		if(show[4]){
		    str = sf.getEmpInjured();
		    if(str.equals("")) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(show[5]){
		    str = "";
		    String all = "";
		    if(emps != null){
			for(Employee emp:emps){
			    Department dp = emp.getDepartment();
			    if(dp != null){
				if(!all.equals("")) all += ", ";
				all += dp.getName();
			    }
			}
		    }
		    if(all.equals("")) all = "&nbsp;";
		    out.println("<td>"+all+"</td>");
		}
		if(show[6]){
		    str = "";
		    String all = "";
		    if(emps != null){
			for(Employee emp:emps){
			    str = emp.getSupervisor();
			    if(str != null && !str.equals("")){
				if(!all.equals("")) all += ", ";
				all += str;
			    }
			}
		    }
		    if(all.equals("")) all = "&nbsp;";
		    out.println("<td>"+all+"</td>");							

		}
		if(show[7]){
		    str = sf.getDamage();
		    if(str.equals("")) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(show[8]){
		    str = "";
		    if(cars != null){
			for(Auto car: cars){
			    if(!str.equals("")) str += "<br />";
			    str = car.getAutoInfo();
			}
		    }
		    if(str.equals("")) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(show[9]){
		    str = sf.getReported();
		    if(str.equals("")) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(show[10]){
		    str = sf.getAccidDate();
		    if(str.equals("")) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(show[11]){
		    str = sf.getAccidLocation();
		    if(str.equals("")) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(show[12]){
		    str = sf.getEstCosts();
		    if(str.equals("")) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		if(show[13]){
		    str = sf.getChosenDealer();
		    if(str.equals("")) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		out.println("</tr>");
	    }
	    out.println("</table>");
	}
	//
	out.print("</div>");
	out.print("</body></html>");
	out.close();

    }

}























































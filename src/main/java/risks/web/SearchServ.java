package risks.web;

import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import org.javatuples.Quintet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.models.*;
import risks.lists.*;
import risks.utils.*;
/**
 *
 */
@WebServlet(urlPatterns = {"/SearchServ","/Search"})
public class SearchServ extends TopServlet{

    static Logger logger = LogManager.getLogger(SearchServ.class);	

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
	    else if (name.equals("claimNum")) {
		claimNum = value;
	    }						
	    else if (name.equals("adjuster")) {
		adjuster =value.toUpperCase();
	    }
	    else if (name.equals("adjusterPhone")) {
		adjusterPhone =value;  
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
	    else if (name.equals("dateFrom")) {
		dateFrom = value;
	    }
	    else if (name.equals("dateTo")) {
		dateTo = value;
	    }
	    else if (name.equals("insured")) {
		insured = value;
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
	out.println("<h3 class=\"titleBar\">Search All Incidents</h3>");
	out.println("<p>For more search options, use the incident related search options in the left side menu</p>");
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
	//
	// 1st block
	out.println("<fieldset>");
	out.println("<table>");
	out.println("<tr><td><label for=\"id\">ID:</label>");
	out.println("<input name=\"id\" id=\"id\" value=\""+id+"\""+
		    " size=\"8\" maxlength=\"8\" />");
	out.println("</td></tr>");
	out.println("<tr><td><label for=\"status\">Status:</label>");
	out.println("<select name=\"status\" id=\"status\">");
	out.println("<option value=\"\"> All</option>");
	out.println("<option value=\"Open\">Open</option>");
	out.println("<option value=\"Closed\">Closed</option>");				
	out.println("</td></tr>");

	out.println("<tr><td>");
	out.println("<label for=\"type\">Type:</label>");
	out.println("<select name=\"type\" id=\"type\">");
	out.println("<option value=\"\">All</option>");
	if(types != null){
	    for(RiskType rtype:types){
		if(rtype.getName() != null){
		    out.println("<option value=\""+rtype.getId()+
				"\">"+rtype.getName()+"</option>");
		}
	    }
	}		
	out.println("</select> (limited to Legal, Tort and Internal Accidents)");
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
	out.println("<tr><td><label for=\"adjuster\">Adjuster's Name:</label>"+
		    "<input name=\"adjuster\" id=\"adjuster\" value=\""+
		    adjuster+"\" size=\"20\" maxlength=\"30\" />");
	out.println("</td></tr>");
	out.println("<tr><td><label for=\"attorney\">Attorney:</label>"+
		    "<input name=\"attorney\" id=\"attorney\" value=\""+
		    attorney+"\" size=\"20\" maxlength=\"30\" />"+
		    "</td></tr>");

	out.println("</table>");
	out.println("</fieldset>");
	//
	out.println("<fieldset><legend>Employee </legend>");
	out.println("<table>");
	out.println("<tr><td>");
	out.println("<label for=\"empName\">Employee Name:</label>"+
		    "<input name=\"empName\" id=\"empName\" value=\""+
		    empName+"\" size=\"20\" maxlength=\"30\"/ ></td></tr>");
	//
	out.println("<tr><td><label for=\"dept_id\">Employee Department:</label>");
	out.println("<select name=\"dept_id\" id=\"dept_id\">");
	out.println("<option value=\"\">All</option>");
	if(depts != null){
	    for(Department dep:depts){
		out.println("<option value=\""+dep.getId()+"\">"+dep.getInfo()+"</option>");
	    }
	}		
	out.println("</select></td></tr>");
	out.println("<tr><td>");
	out.println("<b>City Vehichle Involved </b></td></tr>");
	out.println("<tr><td><label>Vehicle, VIN #:</label>"+
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
	out.println("<fieldset><legend>Date of Incident (DOI)</legend>");
	out.println("<table>");
	//
	out.println("<tr><td><label for=\"dateFrom\">Date, from:</label>");
	out.println("<input name=\"dateFrom\" size=\"10\" "+
		    "id=\"dateFrom\" value=\""+dateFrom+"\" />");
	out.println("<label for=\"dateTo\"> to:</label>");
	out.println("<input name=\"dateTo\" size=\"10\" "+
		    "id=\"dateTo\" value=\""+dateTo+"\" />(mm/dd/yyyy)");
	out.println("</td></tr>");
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
	    "Type",
	    "Status",
	    "Incident Date",
	    "Related Incident"};
			    

	String action=""; 
	String dateFrom="", dateTo="";
				
	String  
	    adjuster="", 
	    type="", id="",
	    insurance="", claimNum="",
	    status="",  empid="", 
	    empName="", dept_id="";
	String vin = "", autoPlate ="", autoNum="",
	    autoMake = "", autoModel = "", autoYear ="";
	//
	// defendant related

	String message = "";
	Enumeration values = req.getParameterNames();
	String [] vals;
	Search sr = new Search(debug);
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();	
	    if (name.equals("id")){
		sr.setId(value);
	    }
	    else if (name.equals("type")) {
		sr.setType(value);
	    }
	    else if (name.equals("status")) {
		sr.setStatus(value);
	    }
	    else if (name.equals("insurance")) {
		sr.setInsurance(value);
	    }
	    else if (name.equals("claimNum")) {
		sr.setClaimNum(value);
	    }						
	    else if (name.equals("adjuster")) {
		sr.setAdjuster(value);
	    }
	    else if (name.equals("empName")) {
		sr.setEmpName(value);
	    }
	    else if (name.equals("dept_id")) {
		sr.setDept_id(value);
	    }
	    else if (name.equals("empid")) {
		sr.setEmpid(value);
	    }
	    else if (name.equals("vin")) {
		sr.setVin(value);
	    }
	    else if (name.equals("autoNum")) {
		sr.setAutoNum(value);
	    }
	    else if (name.equals("autoMake")) {
		sr.setAutoMake(value);
	    }
	    else if (name.equals("autoPlate")) {
		sr.setAutoPlate(value);
	    }
	    else if (name.equals("autoModel")) {
		sr.setAutoModel(value);
	    }
	    else if (name.equals("autoYear")) {
		sr.setAutoYear(value);
	    }
	    else if (name.equals("dateFrom")) {
		sr.setDateFrom(value);
	    }
	    else if (name.equals("dateTo")) {
		sr.setDateTo(value);
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
	out.println("<h1>Search All Incidents</h1>");

	List<Quintet<String, String, String, String, String>> data = null;
	String back = sr.lookFor();
	int cnt = 0;
	if(back.equals("")){
	    data = sr.getData();
	    if(data != null){
		cnt = data.size();
	    }
	}
	else{
	    message += back;
	    success = false;
	}
	if(success){
	    if(!message.equals(""))
		out.println("<h3>"+message+"</h3>");
	}
	else{
	    if(!message.equals(""))
		out.println("<h3 class=\"errorMessages\">"+message+"</h3>");
	}
	out.println("<h4>Total Matching Records "+ cnt +" </h4>");
	if(cnt > 0){
	    out.println("<table border=\"1\">");
	    out.println("<tr>");
	    for (int c = 0; c < titles.length; c++){ 
		out.println("<th>"+titles[c]+"</th>");
	    }	   
	    out.println("</tr>");
	    for(Quintet<String, String, String, String, String> qt:data){
		String str = qt.getValue0();
		String str2 = qt.getValue1();
		String str3 = qt.getValue2();
		String str4 = qt.getValue3();
		String str5 = qt.getValue4();
		out.println("<tr>");
		out.println("<td>"+prepareLink(str, str5)+"</td>");
		out.println("<td>"+str2+"</td>");
		out.println("<td>"+str3+"</td>");
		out.println("<td>"+str4+"</td>");
		out.println("<td>"+str5+"</td>");
		out.println("</tr>");
	    }
	    out.println("</table>");						
	}
	out.println("</div>");
	out.println("</body></html>");
	out.close();
	//	
    }
    String prepareLink(String id, String category){
	String str = "<a href=\""+url;
	if(id == null || category == null) return "";
	if(category.startsWith("Tort")){
	    str += "TortClaimServ?id="+id+"\">"+id+"</a>";
	}
	else if(category.startsWith("Legal")){
	    str += "LegalServ?id="+id+"\">"+id+"</a>";
	}
	else if(category.startsWith("Internal")){
	    str += "SafetyServ?id="+id+"\">"+id+"</a>";
	}
	else if(category.startsWith("Disaster")){
	    str += "DisasterServ?id="+id+"\">"+id+"</a>";
	}
	else if(category.startsWith("Work")){
	    str += "WorkCompServ?id="+id+"\">"+id+"</a>";
	}
	else if(category.startsWith("Misc")){
	    str += "MiscAccidentServ?id="+id+"\">"+id+"</a>";
	}
	else if(category.startsWith("Auto")){
	    str += "AutoAccidentServ?id="+id+"\">"+id+"</a>";
	}		
	else{
	    str = "Unknown category "+category+" with ID: "+id;
	}
	return str;
    }

}























































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

@WebServlet(urlPatterns = {"/SearchWCompServ","/SearchWComp"})
public class SearchWCompServ extends TopServlet{

    static Logger logger = LogManager.getLogger(SearchWCompServ.class);
    /**
     *
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

	String action="", message="";
	String id="", tortId="",vsId="";
	String dateFrom="", dateTo="", whichDate="", status="";
	String amntFrom="", amntTo="", whichAmount="",
	    empid="",empName="",empPhone="",dept_id="",
	    injuryType="",  //  varchar(50),"+
	    compensable="", // enum('','Yes','No','Disputed'),"+ 
	    timeOffWork="", // varchar(30),"+ // days
	    mmi="", //  char(1),"+             // max reached flag
	    ableBackWork="", //  enum('','Yes','No','w/Restrictions'),"+ 
	    disputeReason="", //  varchar(50),"+
	    disputeTypeTtd="", // char(1),"+
	    disputeTypePpi="", // char(1),"+
	    disputeTypeMed=""; //  char(1) "+

	String [] idArr = null;
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
	    else if (name.equals("tortId")){
		tortId = value;
	    }
	    else if (name.equals("status")){
		status = value;
	    }						
	    else if (name.equals("vsId")){
		vsId = value;
	    }
	    else if (name.equals("dateFrom")){
		dateFrom=value;
	    }
	    else if (name.equals("dateTo")){
		dateTo=value;
	    }
	    else if (name.equals("whichDate")){
		whichDate=value;
	    }
	    else if (name.equals("amntFrom")) {
		amntFrom = value;
	    }
	    else if (name.equals("amntTo")) {
		amntTo = value;
	    }
	    else if (name.equals("whichAmount")) {
		whichAmount = value;
	    }
	    else if (name.equals("empid")) {
		empid = value;
	    }
	    else if (name.equals("empName")){
		empName = value;
	    }
	    else if (name.equals("empPhone")){
		empPhone = value;
	    }
	    else if (name.equals("dept_id")){
		dept_id = value;
	    }
	    else if (name.equals("injuryType")){
		injuryType = value;
	    }
	    else if (name.equals("compensable")){
		compensable = value;
	    }
	    else if (name.equals("timeOffWork")){
		timeOffWork = value;
	    }
	    else if (name.equals("mmi")){
		mmi = value;
	    }
	    else if (name.equals("ableBackWork")){
		ableBackWork = value;
	    }
	    else if (name.equals("ableBackWork")){
		ableBackWork = value;
	    }
	    else if (name.equals("disputeReason")){
		disputeReason = value.toUpperCase();
	    }
	    else if (name.equals("disputeTypeTtd")){
		disputeTypeTtd = value;
	    }
	    else if (name.equals("disputeTypePpi")){
		disputeTypePpi = value;
	    }
	    else if (name.equals("disputeTypeMed")){
		disputeTypeMed = value;
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
	// Inserts
	out.println(Inserts.xhtmlHeaderInc);
	out.println(Inserts.banner(url));
	out.println(Inserts.menuBar(url, true));
	out.println(Inserts.sideBar(url));
	// After the browsing  
	//
	out.println("<div id=\"mainContent\">");
	out.println("<h3 class=\"titleBar\">Search Worker's Comp.</h3>");
	if(success){
	    if(!message.equals(""))
		out.println("<h2>"+message+"</h2>");
	}
	else{
	    if(!message.equals(""))
		out.println("<h2 class=\"errorMessages\">"+message+"</h2>");
	}

	out.println("<form id=\"myForm\" method=\"post\" >");
	//	    "onSubmit=\"return validateForm()\">");
	//
	// 1st block
	out.println("<fieldset><legend>Employee </legend>");
	out.println("<table>");
	out.println("<tr><td><label for=\"id\">Worker Comp ID:</label>");
	out.println("<input name=\"id\" id=\"id\" value=\""+id+"\""+
		    " size=\"8\" maxlength=\"8\" /></td></tr>");
	out.println("<tr><td><label for=\"tortId\">Related to Tort Claim ID:</label>");
	out.println("<input name=\"tortId\" id=\"tortId\" value=\""+tortId+"\""+
		    " size=\"8\" maxlength=\"8\" />");
	out.println("<label for=\"vsId\">, Related to Recovery Actions ID:</label>");
	out.println("<input name=\"vsId\" id=\"vsId\" value=\""+vsId+"\""+
		    " size=\"8\" maxlength=\"8\" /></td></tr>");

	out.println("<tr><td>");
	out.println("<tr><td><label for=\"empName\">Employee Name:</lable>");
	out.println("<input name=\"empName\" id=\"empName\" value=\""+
		    empName+"\""+
		    " size=\"50\" maxlength=\"50\" /></td></tr>");
	//
	out.println("<tr><td><label for=\"dept_id\">Empl. Department:</label>");
	out.println("<select name=\"dept_id\" id=\"dept_id\">");
	out.println("<option value=''>All");
	if(depts != null){
	    for(Department dep:depts){
		out.println("<option value='"+dep.getId()+"'>"+dep.getInfo());
	    }
	}
	out.println("</select></td></tr>");
	out.println("<tr><td><label>Injury type:</label>");
	out.println("<input name=\"injuryType\" id=\"injuryType\" value=\""+
		    injuryType+"\" size=\"20\" maxlength=\"30\" /></td></tr>");
	out.println("<tr><td>");
	out.println("<label for=\"compensable\">Compensable?</label>");
	out.println("<select name=\"compensable\" id=\"compensable\">");
	for(int i=0;i<Inserts.compensableArr.length;i++){
	    if(compensable.equals(Inserts.compensableArr[i]))
		out.println("<option selected=\"selected\">"+
			    Inserts.compensableArr[i]+"</option>");
	    else
		out.println("<option>"+Inserts.compensableArr[i]+"</option>");
	}
	out.println("</select></td></tr>");
	out.println("<tr><td>");
	out.println("<label for=\"ableBackWork\">Able to Go Back to Work?</label>");
	out.println("<select name=\"ableBackWork\" id=\"ableBackWork\">");
	for(int i=0;i<Inserts.ableBackWorkArr.length;i++){
	    if(ableBackWork.equals(Inserts.ableBackWorkArr[i]))
		out.println("<option selected=\"selected\">"+
			    Inserts.ableBackWorkArr[i]+"</option>");
	    else
		out.println("<option>"+Inserts.ableBackWorkArr[i]+"</option>");
	}
	out.println("</select></td></tr>");
	out.println("</table>");
	out.println("</fieldset>");
	out.println("<fieldset><legend>Other</legend>");
	out.println("<table>");
	out.println("<tr><td>");
	out.println("<label>Status:</label>");
	out.println("<input type=\"radio\" name=\"status\" "+
		    " value=\"\" />All");
	out.println("<input type=\"radio\" name=\"status\" "+
		    " value=\"Open\" />Open");
	out.println("<input type=\"radio\" name=\"status\" "+
		    " value=\"Closed\" />Closed");
	out.println("</td></tr>");
	out.println("<tr><td>");
	out.println("<label>Dispute Type:</label>");
	out.println("<input type=\"checkbox\" name=\"disputeTypeTtd\" "+
		    "id=\"disputeTypeTtd\" value=\"y\" />Ttd");
	out.println("<input type=\"checkbox\" name=\"disputeTypePpi\" "+
		    "id=\"disputeTypePpi\" value=\"y\" />Ppi");
	out.println("<input type=\"checkbox\" name=\"disputeTypeMed\" "+
		    "id=\"disputeTypeMed\" value=\"y\" />Medical</td></tr>");
	//
	out.println("<tr><td><label for=\"disputeReason\">Dispute Reason:"+
		    "</label>");
	out.println("<input name=\"disputeReason\" id=\"disputeReason\" "+
		    "value=\""+disputeReason+"\" size=\"30\" "+
		    "maxlength=\"30\"/ ></td></tr>");
	out.println("<tr><td><label>");
	out.println("Amount type (select one):</label>");
	out.println("<input type=\"radio\" name=\"whichAmount\" "+
		    "id=\"whichAmount\" value=\"w.disputeAmount\" />"+
		    "Dispute Amount,");
	out.println("<input type=\"radio\" name=\"whichAmount\" "+
		    "id=\"whichAmount\" value=\"w.payTtd\" />Ttd payment,");
	out.println("<input type=\"radio\" name=\"whichAmount\" "+
		    "id=\"whichAmount\" value=\"w.payPpi\" />Ppi payment,");
	out.println("<input type=\"radio\" name=\"whichAmount\" "+
		    "id=\"whichAmount\" value=\"w.payMed\" />Medical payment");
	out.println("</td></tr>");
	out.println("<tr><td><label for=\"amntFrom\">");
	out.println("Amount value, from:</label>");
	out.println("<input name=\"amntFrom\" id=\"amntFrom\" value=\""+
		    amntFrom+"\" size=\"8\" maxlength=\"8\"/ >");
	out.println("<label for=\"amntTo\"> to: </label>");
	out.println("<input name=\"amntTo\" id=\"amntTo\" value=\""+
		    amntTo+"\" size=\"8\" maxlength=\"8\"/ >");
	out.println("</td></tr>");
	out.println("<tr><td><label>");
	out.println("Date type (select one):</label>");
	out.println("<input type=\"radio\" name=\"whichDate\" "+
		    "id=\"whichDate\" value=\"w.accidentDate\" />Accident Date,");
	out.println("<input type=\"radio\" name=\"whichDate\" "+
		    "id=\"whichDate\" value=\"w.back2WorkDate\" />Back to Work Date");
	out.println("</td></tr>");
	out.println("<tr><td>");
	out.println("<label for=\"dateFrom\">Date, from:</label>");
	out.println("<input name=\"dateFrom\" id=\"dateFrom\" value=\""+
		    dateFrom+"\" size=\"10\" maxlength=\"10\"/ >");
	out.println("<label for=\"dateTo\"> to: </label>");
	out.println("<input name=\"dateTo\" id=\"dateTo\" value=\""+
		    dateTo+"\" size=\"10\" maxlength=\"10\"/ >");
	out.println("</td></tr>");
	out.println("</table>");
	out.println("</fieldset>");
	//
	out.println("<fieldset>");
	out.println("<table class='control'>");
	out.println("<tr><td>");
	out.println("<input type=\"submit\" "+
		    "name=\"action\" value=\"Search\" />");
	out.println("</td></tr></table>");
	out.println("</fieldset>");
	out.println("</form></div>");
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
	    "Status",
	    "Employee",
	    "Accident Date",
	    "Injury Type",

	    "Compenable",
	    "Time Off Work",
	    "Ttd payment",
	    "Ppi payemnt",
	    "Medical payment",

	    "Total Payment",
	    "MMI",
	    "Able to Go Back to Work?",
	    "Date Return to Work",
	    "Dispute Amount",

	    "Dipute Reason",
	    "Type of Payment in Dispute"
	};
	boolean [] show = {true, true, true, true, true,
	    true, true, true, true, true,
	    true, true, true, true, true,
	    true, true
	};
	String action="";
	String message = "";
	String dateFrom="", dateTo="", whichDate="";
	String amntFrom="", amntTo="", whichAmount="", status="",
	    empid="",empName="",empPhone="",dept_id="", 
	    id="", tortId="",vsId="",
	    injuryType="",//  varchar(50),"+
	    compensable="", // enum('','Yes','No','Disputed'),"+ 
	    timeOffWork="", // varchar(30),"+ // days
	    mmi="", //  char(1),"+             // max reached flag
	    ableBackWork="", //  enum('','Yes','No','w/Restrictions'),"+ 
	    disputeReason="", //  varchar(50),"+
	    disputeTypeTtd="", // char(1),"+
	    disputeTypePpi="", // char(1),"+
	    disputeTypeMed=""; //  char(1) "+
	String [] idArr = null;

	Enumeration values = req.getParameterNames();
	String [] vals;

	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();	
	    if (name.equals("id")){
		id = value;
	    }
	    else if (name.equals("empName")){
		empName = value.toUpperCase();
	    }
	    else if (name.equals("empid")){
		empid = value;
	    }
	    else if (name.equals("status")){
		status = value;
	    }						
	    else if (name.equals("mmi")){
		mmi=value;
	    }
	    else if (name.equals("timeOffWork")){
		timeOffWork=value; 
	    }
	    else if (name.equals("ableBackWork")){
		ableBackWork=value;
	    }
	    else if (name.equals("disputeReason")){
		disputeReason=value.toUpperCase();
	    }
	    else if (name.equals("disputeTypeTtd")){
		disputeTypeTtd=value;
	    }
	    else if (name.equals("disputeTypePpi")){
		disputeTypePpi=value;
	    }
	    else if (name.equals("disputeTypeMed")){
		disputeTypeMed=value;
	    }
	    else if (name.equals("dept_id")) {
		dept_id = value;
	    }
	    else if (name.equals("dateFrom")) {
		dateFrom = value;
	    }
	    else if (name.equals("dateTo")) {
		dateTo = value;
	    }
	    else if (name.equals("amntFrom")) {
		amntFrom = value;
	    }
	    else if (name.equals("amntTo")) {
		amntTo = value;
	    }
	    else if (name.equals("whichDate")) {
		whichDate = value;
	    }
	    else if (name.equals("whichAmount")) {
		whichAmount = value;
	    }
	    else if (name.equals("compensable")) {
		compensable = value;
	    }
	    else if (name.equals("injuryType")){ 
		injuryType = value.toUpperCase();  
	    }
	    else if (name.equals("tortId")){
		tortId = value;
	    }
	    else if (name.equals("vsId")){
		vsId = value;
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
	out.println("<div id=\"mainContent\">");
	out.println("<h2>Search Worker Comp</h2>");

	SearchWComp sw = new SearchWComp(debug);
	if(!id.isEmpty()){
	    sw.setId(id);
	}
	if(!dateFrom.isEmpty()){
	    sw.setDateFrom(dateFrom);
	}
	if(!status.isEmpty()){
	    sw.setStatus(status);
	}				
	if(!dateTo.isEmpty()){
	    sw.setDateTo(dateTo);
	}
	if(!amntFrom.isEmpty()){
	    sw.setAmntFrom(amntFrom);
	}
	if(!amntTo.isEmpty()){
	    sw.setAmntTo(amntTo);
	}
	if(!whichAmount.isEmpty()){
	    sw.setWhichAmount(whichAmount);
	}
	if(!whichDate.isEmpty()){
	    sw.setWhichDate(whichDate);
	}
	if(!empid.isEmpty()){
	    sw.setEmpid(empid);
	}
	if(!empName.isEmpty()){
	    sw.setEmpName(empName);
	}
	if(!dept_id.isEmpty()){
	    sw.setDept_id(dept_id);
	}
	if(!injuryType.isEmpty()){
	    sw.setInjuryType(injuryType);
	}
	if(!compensable.isEmpty()){
	    sw.setCompensable(compensable);
	}
	if(!timeOffWork.isEmpty()){
	    sw.setTimeOffWork(timeOffWork);
	}
	if(!mmi.isEmpty()){
	    sw.setMmi(mmi);
	}
	if(!tortId.isEmpty()){
	    sw.setTortId(tortId);
	}
	if(!vsId.isEmpty()){
	    sw.setVsId(vsId);
	}
	if(!ableBackWork.isEmpty()){
	    sw.setAbleBackWork(ableBackWork);
	}
	if(!disputeReason.isEmpty()){
	    sw.setDisputeReason(disputeReason);
	}
	if(!disputeTypeTtd.isEmpty()){
	    sw.setDisputeTypeTtd(disputeTypeTtd);
	}
	if(!disputeTypePpi.isEmpty()){
	    sw.setDisputeTypePpi(disputeTypePpi);
	}
	if(!disputeTypeMed.isEmpty()){
	    sw.setDisputeTypeMed(disputeTypeMed);
	}
	int ncnt = 0;
	List<WorkComp> comps = null;				
	String msg = sw.lookFor();
	if(msg.isEmpty()){
	    if(sw.hasComps()){
		comps = sw.getComps();
		ncnt = comps.size();
		if(ncnt == 1){
		    WorkComp one = comps.get(0);
		    res.sendRedirect(url+"WorkComp?id="+one.getId());
		    return;
		}
	    }
	    else{
		message = "No records found ";
		success = false;
	    }
	}
	else{
	    logger.error(msg);
	    message += msg;
	    success = false;
	}
	if(success){
	    if(!message.isEmpty())
		out.println("<h2>"+message+"</h2>");
	}
	else{
	    if(!message.isEmpty())
		out.println("<h2 class=\"errorMessages\">"+message+"</h2>");
	}
	out.println("<h4>Total Matching Records "+ ncnt +" </h4>");
	if(comps != null && comps.size() > 0){
	    out.println("<table class='box'>");
	    out.println("<tr>");
	    for (int c = 0; c < titles.length; c++){ 
		if(show[c] || showAll)
		    out.println("<th>"+titles[c]+"</th>");
	    }	   
	    out.println("</tr>");
	    for(WorkComp wc:comps){
		String str="";
		out.println("<tr>");
		// 
		// ID
		out.println("<td><a href=\""+url+"WorkComp?"+
			    "id="+wc.getId()+"\">"+
			    wc.getId()+"</a></td>"); 
		//
		// status
		if(show[1]){
		    str = wc.getStatus();
		    if(str.isEmpty()) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		//
		// Emp name
		if(show[2]){
		    str = wc.getEmpName();
		    str = str.trim();
		    if(str.isEmpty()) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		//
		// Accident date
		if(show[3]){
		    str = wc.getAccidentDate();
		    str = str.trim();
		    if(str.isEmpty()) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		//
		// Injury Type
		if(show[4]){
		    str = wc.getInjuryType();
		    if(str.isEmpty()) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		//
		// Compansable
		if(show[5]){
		    str = wc.getCompensable();
		    if(str.equals(",")) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		//
		// Time off work
		if(show[6]){
		    str = wc.getTimeOffWork();
		    if(str.isEmpty()) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		// 
		double total = 0;
		// Ttd payment
		if(show[7]){
		    str = wc.getPayTtd();
		    if(str.isEmpty()) str = "&nbsp;";
		    else{
			total += Double.valueOf(str).doubleValue();
		    }
		    out.println("<td>"+str+"</td>");
		}
		// Ppi payment
		if(show[8]){
		    str = wc.getPayPpi();
		    if(str.isEmpty()) str = "&nbsp;";
		    else{
			total += Double.valueOf(str).doubleValue();
		    }
		    out.println("<td>"+str+"</td>");
		}
		//
		// Med payment
		if(show[9]){
		    str = wc.getPayMed();
		    if(str.isEmpty()) str = "&nbsp;";
		    else{
			total += Double.valueOf(str).doubleValue();
		    }
		    out.println("<td>"+str+"</td>");
		}
		//
		// total payment
		if(show[10]){
		    if(total > 0){
			str = Helper.formatNumber(total);
		    }
		    else 
			str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		//
		// mmi
		if(show[11]){
		    str = wc.getMmi();
		    if(str.isEmpty()) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		//
		// able back to work
		if(show[12]){
		    str = wc.getAbleBackWork();
		    if(str.isEmpty()) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		//
		// date return to work
		if(show[13]){
		    str = wc.getBack2WorkDate();
		    if(str.isEmpty()) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		//
		// Dispute amount
		if(show[14]){
		    str = wc.getDisputeAmount();
		    if(str.isEmpty()) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		//
		// Dispute reason
		if(show[15]){
		    str = wc.getDisputeReason();
		    if(str.isEmpty()) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		//
		if(show[16]){
		    String all = "";
		    str = wc.getDisputeTypeTtd();
		    if(!str.isEmpty()){
			all += " TTD";
		    }
		    str = wc.getDisputeTypeTtd();
		    if(!str.isEmpty()){
			if(all.isEmpty()) all += ", ";
			all += " PPI";
		    }
		    str = wc.getDisputeTypeMed();
		    if(!str.isEmpty()){
			if(all.isEmpty()) all += ", ";
			all += " Medical";
		    }
		    if(!all.isEmpty()){
			str = all;
		    }
		    else{
			str = "&nbsp;";
		    }
		    out.println("<td>"+str+"</td>");
		}
		out.println("</tr>");
	    }
	    out.println("</table>");
	}
	out.println("</div>");
	//
	out.print("</body></html>");
	out.close();
    }

}























































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
@WebServlet(urlPatterns = {"/WorkCompServ","/WorkComp"})
public class WorkCompServ extends TopServlet{

    static Logger logger = LogManager.getLogger(WorkCompServ.class);
    /**
     * Generates the Case form and processes view, add, update and delete
     * operations.
     * @param req
     * @param res
     *
     */
    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException {
	doPost(req,res);
    }
    /**
     * @link #doGet
     */

    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException {
    
	String id="", status="", tortId="", vsId="",
	    empid="",empName="",empPhone="",dept_id="", empTitle="",
	    accidentDate="",   //  date
	    injuryType="",     //  varchar(50),"+
	    compensable="",    // enum('','Yes','No','Disputed'),"+ 
	    timeOffWork="",    // varchar(30),"+ // days
	    payTtd="",payPpi="", payMed="", // double,"+
	    mmi="",            //  char(1),"+        // max reached flag
	    ableBackWork="",   //  enum('','Yes','No','w/Restrictions'),"+ 
	    disputeAmount="",  // double,"+
	    disputeReason="",  //  varchar(50),"+
	    disputeTypeTtd="", // char(1),"+
	    disputeTypePpi="", // char(1),"+
	    disputeTypeMed="", //  char(1) "+
	    back2WorkDate="",
	    deptName="";
	boolean success = true;
       	String username = "", message = "", action = "",entry_time = "", 
	    entry_date = "", relatedId="";
	
	res.setContentType("text/html");
	PrintWriter out = res.getWriter();
	String name, value;
	Enumeration values = req.getParameterNames();
	String [] vals;
	String [] delRelated = null;

	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();	
	    value = Helper.replaceSpecialChars(value);

	    if (name.equals("id")) {
		id =value;
	    }
	    else if (name.equals("status")) {
		status =value;
	    }
	    else if (name.equals("accidentDate")) {
		accidentDate =value;
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
	    else if (name.equals("relatedId")) {
		relatedId = value;
	    }
	    else if (name.equals("empTitle")) {
		empTitle = value;
	    }
	    else if (name.equals("back2WorkDate")) {
		back2WorkDate = value;
	    }
	    else if (name.equals("injuryType")) {
		injuryType = value;
	    }
	    else if (name.equals("compensable")) {
		compensable = value;
	    }
	    else if (name.equals("timeOffWork")) {
		timeOffWork = value;
	    }
	    else if (name.equals("payTtd")) {
		payTtd =value;
	    }
	    else if (name.equals("payPpi")) {
		payPpi =value;
	    }
	    else if (name.equals("payMed")) {
		payMed =value;
	    }
	    else if (name.equals("mmi")) {
		mmi = value;
	    }
	    else if (name.equals("tortId")) {
		tortId = value;
	    }
	    else if (name.equals("vsId")) {
		vsId = value;
	    }
	    else if (name.equals("ableBackWork")) {
		ableBackWork =value;
	    }
	    else if (name.equals("disputeAmount")) {
		disputeAmount =value;
	    }
	    else if (name.equals("disputeReason")){
		disputeReason =value;
	    }
	    else if (name.equals("disputeTypeTtd")){   
		disputeTypeTtd =value;
	    }
	    else if (name.equals("disputeTypePpi")){   
		disputeTypePpi =value;
	    }
	    else if (name.equals("disputeTypeMed")){   
		disputeTypeMed =value;
	    }
	    else if (name.equals("deptName")) {
		deptName = value;
	    }
	    else if (name.equals("delRelated")) {
		delRelated = vals;
	    }
	    else if (name.equals("action")){ 
		// Get, Save, zoom, edit, delete, New, Refresh
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
	entry_date = Helper.getToday();
	//
	if(action.equals("Generate")){
	    //
	    // We are coming from TortClaimServ or VSLegalServ
	    // Using the tortId, or vsId to find if any workcomp is 
	    // associated with this record
	    //
	    // 
	    //  first time 
	    //  let us get tortClaim info data that is useful to us
	    //  and change the action to '' for a new record
	    //
	    if(!tortId.equals("")){
		TortClaim tc = new TortClaim(debug, tortId);
		// tc.setId(tortId);
		String back = tc.doSelect();
		if(back.equals("")){
		    accidentDate = tc.getIncidentDate();
		    status = tc.getStatus();
		}
		action = "Save";
		relatedId = tortId;
	    }
	    else if(!vsId.equals("")){
		Legal tc = new Legal(debug);
		tc.setId(vsId);
		String back = tc.doSelect();
		if(back.equals("")){
		    status = tc.getStatus();
		    if(status.equals("Pending")) status = "Open";
		    else if(status.equals("Court")) status ="Open";
		    accidentDate = tc.getDoi();
		}
		action = "Save";
		relatedId = vsId;
	    }
	    if(!(dept_id.equals("") || dept_id.equals("0"))){
		Department dp = new Department(debug, dept_id);
		String back = dp.doSelect();
		if(back.equals("")){
		    deptName = dp.getName();
		}
		dp = null;
	    }
	}
	WorkComp wc = new WorkComp(debug);
	//
	if(action.equals("Save")){
	    //
	    // get the dept id first
	    if(!deptName.equals("")){
		Department dp = new Department(debug);
		dp.setName(deptName);
		String str = dp.findOrAddDept();
		if(!str.equals("")) dept_id = str;
		dp = null;
	    }
	    wc.setStatus(status);

	    wc.setAccidentDate(accidentDate);

	    wc.setInjuryType(injuryType);
	    wc.setCompensable(compensable);
	    wc.setTimeOffWork(timeOffWork);
	    wc.setPayTtd(payTtd);
	    wc.setPayPpi(payPpi);
	    wc.setPayMed(payMed);
	    wc.setMmi(mmi);
	    wc.setAbleBackWork(ableBackWork);
	    wc.setDisputeAmount(disputeAmount);
	    wc.setDisputeReason(disputeReason);
	    wc.setDisputeTypeTtd(disputeTypeTtd);
	    wc.setDisputeTypePpi(disputeTypePpi);
	    wc.setDisputeTypeMed(disputeTypeMed);
	    wc.setBack2WorkDate(back2WorkDate);

	    String back = wc.doSave();
	    if(back.equals("")){
		id = wc.getId();
		message += "Data saved successfully";
	    }
	    else{
		message += back;
		logger.error(back);
		success = false;
	    }
	    if(!relatedId.equals("")){
		Related rel = new Related(id,relatedId,"Worker Comp", debug);
		back = rel.doSave();
		if(!back.equals("")){
		    message += back;
		    logger.error(back);
		    success = false;
		}	
	    }
	}
	else if(action.equals("Update")){
	    //
	    // get the dept id first
	    if(!deptName.equals("")){
		Department dp = new Department(debug);
		dp.setName(deptName);
		String str = dp.findOrAddDept();
		if(!str.equals("")) dept_id = str;
		dp = null;
	    }
	    wc.setId(id);
	    wc.setStatus(status);
	    wc.setAccidentDate(accidentDate);

	    wc.setInjuryType(injuryType);
	    wc.setCompensable(compensable);
	    wc.setTimeOffWork(timeOffWork);
	    wc.setPayTtd(payTtd);
	    wc.setPayPpi(payPpi);
	    wc.setPayMed(payMed);
	    wc.setMmi(mmi);
	    wc.setAbleBackWork(ableBackWork);
	    wc.setDisputeAmount(disputeAmount);
	    wc.setDisputeReason(disputeReason);
	    wc.setDisputeTypeTtd(disputeTypeTtd);
	    wc.setDisputeTypePpi(disputeTypePpi);
	    wc.setDisputeTypeMed(disputeTypeMed);
	    wc.setBack2WorkDate(back2WorkDate);
	    wc.setTortId(tortId);
	    wc.setVsId(vsId);
	    String back = wc.doUpdate();
	    if(!back.equals("")){
		message += back;
		logger.error(back);
		success = false;
	    }
	    else{
		message += "Data updated successfully";
	    }
	    if(!relatedId.equals("")){
		Related rel = new Related(id,relatedId,"Worker Comp", debug);
		back = rel.doSave();
		if(!back.equals("")){
		    message += back;
		    logger.error(back);
		    success = false;
		}	
	    }
	    if(delRelated != null){
		for(String str: delRelated){
		    Related rel = new Related(id, str, debug);
		    rel.doDelete();
		}
	    }
	}
	else if(action.equals("Delete")){
	    //
	    // System.err.println("delete record");
	    //
	    wc.setId(id);
	    String back = wc.doDelete();
	    if(!back.equals("")){
		message += back;
		logger.error(back);
		success = false;
	    }
	    if(success){
		Related rel = new Related(id, debug);
		rel.doDelete();
	    }
	}
	else if(!id.equals("")){	
	    //
	    wc.setId(id);
	    String back = wc.doSelect();
	    if(back.equals("")){
		status = wc.getStatus();
		accidentDate = wc.getAccidentDate();
		injuryType = wc.getInjuryType();
		compensable = wc.getCompensable();
		timeOffWork = wc.getTimeOffWork();
		payTtd = wc.getPayTtd();

		payPpi = wc.getPayPpi();
		payMed = wc.getPayMed();
		mmi = wc.getMmi();
		ableBackWork = wc.getAbleBackWork();
		disputeAmount = wc.getDisputeAmount();

		disputeReason = wc.getDisputeReason();
		disputeTypeTtd = wc.getDisputeTypeTtd();
		disputeTypePpi = wc.getDisputeTypePpi();
		disputeTypeMed = wc.getDisputeTypeMed();
		back2WorkDate = wc.getBack2WorkDate();
		if(!(dept_id.equals("") || dept_id.equals("0"))){
		    Department dp = new Department(debug, dept_id);
		    back = dp.doSelect();
		    if(back.equals("")){
			deptName = dp.getName();
		    }
		    dp = null;
		}
	    }
	    else{
		message += back;
		logger.error(back);
		success = false;
	    }
	}
	if(!disputeTypeTtd.equals("")) disputeTypeTtd = "checked";
	if(!disputeTypePpi.equals("")) disputeTypePpi = "checked";
	if(!disputeTypeMed.equals("")) disputeTypeMed = "checked";
	if(!mmi.equals("")) mmi = "checked";
	//
	// Inserts
	out.println(Inserts.xhtmlHeaderInc);
	out.println(Inserts.banner(url));
	out.println(Inserts.menuBar(url, true));
	out.println(Inserts.sideBar(url));
	out.println("<div id=\"mainContent\">");
	out.println("<h3 class=\"titleBar\">Worker's Comp "+id+"</h3>");
	//
	// if we have any message, it will be shown here
	if(success){
	    if(!message.equals(""))
		out.println("<h3>"+message+"</h3>");
	}
	else{
	    if(!message.equals(""))
		out.println("<h3 class=\"errorMessages\">"+message+"</h3>");
	}
	//
	out.println("<script type=\"text/javascript\">");
	out.println("  function validateForm(){	              ");
	//
	out.println("  return true;                           ");
	out.println("  }                                      ");  
	out.println("</script>                                ");
	out.println("<form id=\"myForm\" method=\"post\" "+
		    "onSubmit=\"return validateForm();\">");
	if(!id.equals("")){
	    out.println("<input type=\"hidden\" name=\"id\" id=\"id\" "+
			"value=\""+id+"\"/>");
	}
	if(id.equals("")){
	    out.println("<fieldset><legend>Employee Info </legend>");			
	    out.println("<p>You will be able to enter employee info after"+
			" you fill this form and Save </p>");
	}
	else{
	    out.println("<fieldset><legend>Employee Info </legend>");

	    out.println("<table>");			
	    out.println("<tr><td><label>Employees:</label>"+
			"</td><td>");
	    out.println("<input name=\"action\" "+
			"type=\"button\" value=\"Add Employee\" "+
			"tabindex=\"33\" onclick=\"window.open('"+
			url+
			"GetEmpInfoServ?legalType=WorkerComp"+
			"&relatedId="+id+"&opener=WorkCompServ"+
			"','Employee','toolbar=0,location=0,"+
			"directories=0,status=0,menubar=0,"+
			"scrollbars=0,top=200,left=200,"+
			"resizable=1,width=500,height=400');return false;\" />");
	    out.println("</td></tr>");
	    out.println("<tr id=empinfo><td>&nbsp;</td></tr>");
	    EmployeeList el = new EmployeeList(debug);
	    el.setRelatedId(id);
	    el.lookFor();
	    List<Employee> ls = el.getEmployees();
	    if(ls != null){

		for(Employee empo: ls){
		    Helper.printEmployee(out,empo,url,id,"WorkCompServ");
		}

	    }
	    out.println("</table>");			
	    out.println("</fieldset>");	
	}			
	//
	out.println("<fieldset>");
	out.println("<legend>Related Records</legend>");
	out.println("<table>");
	out.println("<tr><td>");
	out.println("In the field below enter the record ID of related Tort Claim, Recovery Action, Internal Accident, Worker's Comp, or Natural Disaster");
	out.println("</td></tr>");
	out.println("<tr><td>");
	String related=""; // if we are coming from tort, or recovery 
	if(!vsId.equals("")){
	    related = vsId;
	}
	else if(!tortId.equals("")){
	    related = tortId;
	}
	out.println("<label for=\"relatedId\">Related Record ID:</label>"+
		    "<input name=\"relatedId\" id=\"relatedId\" value=\""+related+
		    "\" size=\"8\" maxlength=\"8\" /> Enter one at a time. ");
	out.println("</td></tr>");
	if(!id.equals("")){
	    List<Related> dups = null;
	    RelatedList rl = new RelatedList(debug, id);
	    String back = rl.lookFor();
	    if(back.equals("")){
		dups = rl.getRelatedList();
	    }
	    if(dups != null && dups.size() > 0){
		out.println("<tr><td>");
		out.println("<table><caption>Current Related Records</caption>");
		out.println("<tr><th></th><th>Related ID</th><th>Type</th></tr>");
		for(Related rel: dups){
		    String str = rel.getId2();
		    String str2 = rel.getType2();
		    String str3 = rel.createLinkForType2(url);
		    out.println("<tr><td><input type=\"checkbox\" name=\"delRelated\" value=\""+str+"\">*</td><td>"+str3+"</td><td>"+str2+"</td></tr>");
		}
		out.println("</table></td></tr>");
	    }
	}
	out.println("</table>");
	out.println("</fieldset>");
		
	out.println("<fieldset><legend>Details </legend>");
	out.println("<table>");
	out.println("<tr><td><label for=\"status\">Status: </label>"+
		    "<select name=\"status\" id=\"status\">");
	for(int i=0;i<Inserts.statusArr.length;i++){
	    if(status.equals(Inserts.statusArr[i]))
		out.println("<option selected=\"selected\">"+status+
			    "</option>");
	    else
		out.println("<option>"+Inserts.statusArr[i]+"</option>");
	}
	out.println("</select>&nbsp;&nbsp;");

	out.println("<label for=\"accidentDate\">Date of Accident: "+
		    "</label>"+
		    "<input name=\"accidentDate\" id=\"accidentDate\" value="+
		    "\""+accidentDate+"\" size=\"10\" maxlength=\"10\" class=\"date\" />");
	out.println("</td></tr>");
	//
	out.println("<tr><td><label for=\"injuryType\">Injury Type:</label>"+
		    "<input name=\"injuryType\" id=\"injuryType\" value=\""+
		    injuryType+"\" size=\"50\" maxlength=\"80\"/ >");
	out.println("</td></tr>");
	//
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
	//
	out.println("<tr><td><label for=\"payTtd\">Payment Made, TTD: $</label>");
	out.println("<input name=\"payTtd\" id=\"payTtd\" value=\""+
		    payTtd+"\" size=\"8\" maxlength=\"8\" "+
		    "onchange=\"checkNumber(this)\"/>");
	out.println("<label for=\"payPpi\">PPI:$</label>");
	out.println("<input name=\"payPpi\" id=\"payPpi\" value=\""+
		    payPpi+"\" size=\"8\" maxlength=\"8\" "+
		    "onchange=\"checkNumber(this)\"/>");
	out.println("<label for=\"payMed\">Medical:$</label>");
	out.println("<input name=\"payMed\" id=\"payMed\" value=\""+
		    payMed+"\" size=\"8\" maxlength=\"8\" "+
		    "onchange=\"checkNumber(this)\" "+
		    " /></td></tr>");
	out.println("<tr><td><label>Total Payments:</label>");
	double totalPay = 0;
	if(!payTtd.equals("")){
	    totalPay += Double.valueOf(payTtd).doubleValue();
	}
	if(!payPpi.equals("")){
	    totalPay += Double.valueOf(payPpi).doubleValue();
	}
	if(!payMed.equals("")){
	    totalPay += Double.valueOf(payMed).doubleValue();
	}
	String str ="&nbsp;$";
	str += Helper.formatNumber(totalPay);
	out.println("<span id=\"totalPay\">"+str+"</span></td></tr>");
	out.println("<tr><td>");
	out.println("<input type=\"checkbox\" name=\"mmi\" id=\"mmi\" "+
		    "value=\"y\" "+mmi+" /><label>MMI Reached</label>"+
		    "</td></tr>");
	//
	out.println("<tr><td>");
	out.println("<label for=\"ableBackWork\"> Able to Go Back to Work?</label>");
	out.println("<select name=\"ableBackWork\" id=\"ableBackWork\">");
	for(int i=0;i<Inserts.ableBackWorkArr.length;i++){
	    if(ableBackWork.equals(Inserts.ableBackWorkArr[i]))
		out.println("<option selected=\"selected\">"+
			    Inserts.ableBackWorkArr[i]+"</option>");
	    else
		out.println("<option>"+Inserts.ableBackWorkArr[i]+"</option>");
	}
	out.println("</select><label for=\"back2WorkDate\">");
	out.println("Date Return to Work:</label>");
	out.println("<input name=\"back2WorkDate\" id=\"back2WorkDate\" "+
		    "value=\""+back2WorkDate+"\" size=\"10\" maxlength=\"10\""+
		    " class=\"date\" "+
		    "/></td></tr>");
	//
	out.println("<tr><td>");
	out.println("<label for=\"disputeAmount\">Disputed Amount: $</label>");
	out.println("<input name=\"disputeAmount\" id=\"disputeAmount\" "+
		    "value=\""+disputeAmount+
		    "\" size=\"10\" maxlength=\"10\" "+
		    "onchange=\"checkNumber(this)\" "+
		    "/></td></tr>");
	out.println("<tr><td><label>Type of Payment in Dispute:</label>");
	out.println("<input type=\"checkbox\" name=\"disputeTypeTtd\" "+
		    "id=\"disputeTypeTtd\" value=\"y\" "+
		    disputeTypeTtd+" /> TTD");
	out.println("<input type=\"checkbox\" name=\"disputeTypePpi\" "+
		    "id=\"disputeTypePpi\" value=\"y\" "+
		    disputeTypePpi+" /> PPI");
	out.println("<input type=\"checkbox\" name=\"disputeTypeMed\" "+
		    "id=\"disputeTypeMed\" value=\"y\" "+
		    disputeTypeMed+" />Medical</td></tr>");
	out.println("<tr><td><label for=\"disputeReason\">Dispute Reason:"+
		    "</label><br />"+
		    "<textarea name=\"disputeReason\" rows=\"5\" "+
		    "cols=\"70\" wrap id=\"disputeReason\">");
	out.println(disputeReason);
	out.println("</textarea></td></tr>");

	out.println("</table>");
	out.println("</fieldset>");
	//
	out.println("<fieldset><table class=\"control\">");
	if(id.equals("")){
	    out.println("<tr><td>");
	    out.println("<input type=\"submit\" tabindex=\"31\" "+
			"accesskey=\"s\" "+
			"name=\"action\" class=\"submit\" "+
			"value=\"Save\"/>");
	    out.println("</td></tr>"); 
	    out.println("</form>");
	}
	else{ // save, update
	    //
	    out.println("<tr><td valign=top><input accesskey=\"u\" "+
			"tabindex=\"31\" "+
			"type=\"submit\" name=\"action\" "+
			"class=\"submit\" value=\"Update\" />");
	    out.println("</td>");
	    out.println("<td valign=top><input accesskey=\"p\" "+
			"tabindex=\"32\" "+
			"type=\"submit\" name=\"action\" "+
			"class=\"submit\" value=\"Printable\" />");
	    out.println("</td>");
	    out.println("<td valign=top><input "+
			"type=button name=\"action\" id=\"action\" "+
			"onclick=\"window.location='"+url+"RiskFileServ?risk_id="+id+"';\" "+
			"class=\"submit\" value=\"Upload File\" />");
	    out.println("</td>");		
	    out.println("<td>");
	    out.println("<input type=\"button\" value=\"Add Notes\" "+
			"onclick=\"window.open('" + url + 
			"NoteServ?risk_id="+id+"&opener=WorkComp','Notes',"+
			"'location=0:0,menubar=1,width=500,height=400,toolbar=1,scrollbars=1');return false;\"></input>");
						
	    out.println("</form></td><td valign=top>");
	    out.println("<form id=\"myForm2\" onsubmit=\"return "+
			"validateDelete();\">");
	    out.println("<input type=hidden name=\"id\" value=\""+id+
			"\">");
	    out.println("<input type=\"submit\" name=\"action\" "+
			"accesskey=\"e\" tabindex=\"36\" "+
			"value=\"Delete\" />");
	    out.println("</form>");
	    out.println("</td></tr>");
	}
	out.println("</table></fieldset>");
	if(!id.equals("")){
	    if(wc.hasNotes()){
		Helper.printNotes(out,
				  url,
				  "WorkComp",
				  wc.getNotes());									
	    }
	    if(wc.hasFiles()){
		Helper.printFiles(out,
				  url,
				  wc.getFiles());									
	    }						
	    out.println("<center>* Click in check box to delete the item from this record </center>");
	}
	out.println("</div>");
	out.print("</body></html>");
	out.flush();
	out.close();

    }

}























































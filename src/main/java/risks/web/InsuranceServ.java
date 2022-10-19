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
@WebServlet(urlPatterns = {"/InsuranceServ","/Insurance"})
public class InsuranceServ extends TopServlet{

    static Logger logger = LogManager.getLogger(DisasterServ.class);
    String deptIdArr[] = null;
    String deptArr[] = null;
    List types = null;
    //
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

	String  id="";
	boolean success = true;
       	String message = "", action = "", type = "",  legalType="",
	    entry_date = "", prevAction="", relatedId="", opener="";
	
	res.setContentType("text/html");
	PrintWriter out = res.getWriter();
	String name, value;
	Enumeration values = req.getParameterNames();
	String [] vals;
	String [] delAutoAid = null;
	String [] delEmp = null;
	HttpSession session = session = req.getSession(false);
	User user = null;
	if(session != null){
	    user = (User)session.getAttribute("user");
	}
	if(user == null){
	    res.sendRedirect(url+"Login");
	    return;
	}
	Insurance insur = new Insurance(debug);
	String [] delRelated = null;
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();	
	    value = Helper.replaceSpecialChars(value);
	    if (name.equals("id")) {
		id = value;
		insur.setId(value);
	    }
	    else if (name.equals("legalType")) {
		legalType = value; // tort, vslegal, disaster, miscAccident,autoAccident
	    }
	    else if (name.equals("type")) {
		insur.setType(value); // City, Defendant
		type = value;
	    }
	    else if (name.equals("relatedId")) {
		insur.setRelatedId(value);
		relatedId = value;
	    }
	    else if (name.equals("company")) {
		insur.setCompany(value);
	    }
	    else if (name.equals("insurStatus")) {
		insur.setStatus(value);
	    }
	    else if (name.equals("adjuster")) {
		insur.setAdjuster(value);
	    }
	    else if (name.equals("adjusterPhone")) {
		insur.setAdjusterPhone(value);
	    }
	    else if (name.equals("adjusterEmail")) {
		insur.setAdjusterEmail(value);
	    }
	    else if (name.equals("deductible")) {
		insur.setDeductible(value);
	    }
	    else if (name.equals("claimNum")) {
		insur.setClaimNum(value);
	    }
	    else if (name.equals("policy")) {
		insur.setPolicy(value);
	    }
	    else if (name.equals("policy_num")) {
		insur.setPolicy_num(value);
	    }	
	    else if (name.equals("sent")) {
		insur.setSent(value);
	    }
	    else if (name.equals("decisionDate")) {
		insur.setDecisionDate(value);
	    }
	    else if (name.equals("amountPaid")) {
		insur.setAmountPaid(value);
	    }
	    else if (name.equals("attorney")) {
		insur.setAttorney(value);
	    }
	    else if (name.equals("attorneyPhone")) {
		insur.setAttorneyPhone(value);
	    }
	    else if (name.equals("phone")) {
		insur.setPhone(value);
	    }	
	    else if (name.equals("attorneyEmail")) {
		insur.setAttorneyEmail(value);
	    }
	    else if (name.equals("action")){ 
		action = value;  
	    }
	    else if (name.equals("opener")){ 
		opener = value;  
	    }						
	}
	if(!action.equals("") &&
	   !action.equals("Update") &&
	   action.equals(prevAction)){
	    action = "";
	}
	else if(!prevAction.equals("")){
	    //
	    // this senario should not happen
	    // acion     prevAction
	    // ======    ========
	    // zoom,     update
	    // save,     update
	    // Generate, Save
	    //
	    if(action.equals("zoom"))
		action = "";
	}
	//
	if(action.equals("Save")){
	    //
	    String back = "";
	    if(!insur.isEmpty()){
		back = insur.doSave();
		if(back.equals("")){
		    id = insur.getId();
		    message += "Data saved successfully";
		}
		else{
		    message += back;
		    logger.error(back);
		    success = false;
		}
	    }
	}
	else if(action.equals("Update")){
	    //
	    // get the dept id first
	    //
	    String back;
	    back = insur.doUpdate();
	    if(!back.equals("")){
		message += back;
		logger.error(back);
		success = false;
	    }
	    else{
		message += "Data updated successfully";
	    }
	}
	else if(action.equals("Delete")){
	    //
	    // System.err.println("delete record");
	    //
	    String back = insur.doDelete();
	    if(!back.equals("")){
		message += back;
		logger.error(back);
		success = false;
	    }
	    else{
		id="";
	    }
	}
	else if(action.equals("zoom") || action.startsWith("Print")){	
	    //
	    String back = insur.doSelect();
	    if(!back.equals("")){
		message += back;
		logger.error(back);
		success = false;
	    }
	}
	//
	// Inserts
	out.println(Inserts.xhtmlHeaderInc);
	out.println(Inserts.banner(url));
	// out.println("<div id=\"mainContent\">");
	out.println("<div>");
	out.println("<h3 class=\"titleBar\">Insurance Info</h3>");
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
	out.println("<script type=\"text/javascript\">");
	out.println("  function validateForm(){	              ");
	out.println("  return true;                           ");
	out.println("  }                                      ");
	out.println(" window.onunload = refreshParent; ");
	out.println(" function refreshParent() { ");
        out.println("   window.opener.location =\""+url+opener+"?id="+relatedId+"\";");
	out.println("  } ");				
	out.println("</script>                                ");
	out.println("<form id=\"myForm\" method=\"post\" "+
		    "onSubmit=\"return validateForm();\">");
	out.println("<fieldset>");
	if(!id.equals("")){
	    out.println("<input type='hidden' name='id' value='"+id+"'>");
	}
	out.println("<input type='hidden' name='legalType' value='"+legalType+"'>");
	out.println("<input type='hidden' name='type' value='"+type+"'>");		
	out.println("<input type='hidden' name='relatedId' value='"+relatedId+"'>");
	out.println("<table>");
	out.println("<tr><td>");
	if(!id.equals("")){
	    out.println("<label>Insurance ID:</label>"+id+"&nbsp;&nbsp;");
	}
	out.println("<label>Related "+legalType+" ID: </label>"+
		    relatedId);
		
	out.println("</td></tr>");		
	out.println("<tr><td>");
	//
	out.println("<label for='insurStatus'> Insurance Status: "+
		    "</label><select name='insurStatus' "+
		    "id='insurStatus'>");
		
	for(int i=0;i<Inserts.insurStatusArr.length;i++){
	    if(insur.getStatus().equals(Inserts.insurStatusArr[i]))
		out.println("<option selected='selected'>"+Inserts.insurStatusArr[i]+
			    "</option>");
	    else
		out.println("<option>"+Inserts.insurStatusArr[i]+"</option>");
	}
	out.println("</select>");
	out.println("</td></tr>");
	out.println("<tr><td>");	
	out.println("<label for='policy'> Policy:</label>");
	out.println("<select name='policy' id='policy' "+
		    "onchange='setDeductible(this.selectedIndex)'>");
	for(int i=0;i<Inserts.policyArr.length;i++){
	    if(insur.getPolicy().equals(Inserts.policyArr[i]))
		out.println("<option selected='selected'>"+insur.getPolicy()+
			    "</option>");
	    else
		out.println("<option>"+Inserts.policyArr[i]+"</option>");
	}
	out.println("</select>");
	out.println("<label for='policy_num'> Policy #:</label>");
	out.println("<input name='policy_num' id='policy_num' value='"+
		    insur.getPolicy_num()+"' size='20' maxlength='30' />");	
	out.println("</td></tr>");
	out.println("<tr><td>");
	out.println("<label for='claimNum'>Claim #:</label>"+
		    "<input name='claimNum' id='claimNum' value='"+
		    insur.getClaimNum()+"' size='20' maxlength='20' />");
	out.println("<label for='deductible'> Deductible:"+
		    "</label><input name='deductible' "+
		    "id='deductible' value='"+
		    insur.getDeductible()+"' size='8' maxlength='8' "+
		    "onchange='checkNumber(this)' />");
	out.println("</td></tr>");
	out.println("<tr><td><label for='company'>Insurance Company:</label>"+
		    "<input name='company' id='company' value='"+
		    insur.getCompany()+"' size='30' maxlength='80' />");
	out.println("<label for='phone'>Phone:</label>"+
		    "<input name='phone' id='phone' value='"+
		    insur.getPhone()+"' size='15' maxlength='30' />");	
	out.println("</td></tr>");
		
	out.println("<tr><td><label for='sent'>Date Sent to Insurance:</label>"+
		    "<input name='sent' id='sent' value='"+
		    insur.getSent()+"' size='10' maxlength='10' />");
	out.println("<label for='decisionDate'>Decision Date:</label>"+
		    "<input name='decisionDate' id='decisionDate' value='"+
		    insur.getDecisionDate()+"' size='10' maxlength='10' />");	
	out.println("</td></tr>");		
	//
	out.println("<tr><td><label for='adjuster'>Adjuster's Name:</label>"+
		    "<input name='adjuster' id='adjuster' value='"+
		    insur.getAdjuster()+"' size='20' maxlength='30' />");
	out.println("<label for='adjusterPhone'> Phone: </label>"+
		    "<input name='adjusterPhone' "+
		    "id='adjusterPhone' value='"+
		    insur.getAdjusterPhone()+"' size='16' maxlength='30'/>"+
		    "</td></tr>");
	out.println("<tr><td><label for='adjusterEmail'>Adjuster Email:"+
		    "</label><input name='adjusterEmail' "+
		    "id='adjusterEmail' value='"+insur.getAdjusterEmail()+"' "+
		    "size='40' maxlength='60' /></td></tr>");
	out.println("<tr><td>");
	out.println("<label for='attorney'>Attorney:"+
		    "</label>"+
		    "<input name='attorney' id='attorney' value='"+
		    insur.getAttorney()+"' size='30' maxlength='50' />");
	out.println("<label for='phone'>Attorney Phone:"+
		    "</label>"+
		    "<input name='attorneyPhone' id='phone' value='"+
		    insur.getAttorneyPhone()+"' size='10' maxlength='30' />");
	out.println("<tr><td>");
	out.println("<label for='email'>Attorney Email:"+
		    "</label>"+
		    "<input name='attorneyEmail' id='attorneyEmail' value='"+
		    insur.getAttorneyEmail()+"' size='30' maxlength='50' />");
	out.println("</td></tr>");
	out.println("<tr><td>");
	out.println("<label for='amountPaid'>Amount Paid By Insurance:"+
		    "</label>"+
		    "<input name='amountPaid' id='amountPaid' value='"+
		    insur.getAmountPaid()+"' onchange='checkNumber(this)' "+
		    " size='10' maxlength='10' />");
	out.println("</td></tr>");
		
	out.println("</table>");
	out.println("</fieldset>");
	//
	out.println("<fieldset>");
	if(id.equals("")){
	    out.println("<table class='control'><tr><td>");
	    out.println("<input type='submit' tabindex='31' "+
			"accesskey='s' id='action' value='Save' "+
			"name='action' class='submit'>");
	    out.println("</td></tr></table>"); 
	    out.println("</form>");
	}
	else{ // save, update
	    //
	    out.println("<table class='control'>");
	    out.println("<td valign=top><input accesskey='u' "+
			"tabindex='31' "+
			"type=submit name='action' id='action' "+
			"class='submit' value='Update' />");
	    out.println("</form></td>");
	    out.println("<td>");
	    out.println("<form id='myForm2' onsubmit='return "+
			"validateDelete();'>");
	    out.println("<input type=hidden name='id' value='"+
			id+"'>");
	    out.println("<input type='submit' name='action' "+
			"id='action' accesskey='e' tabindex='33' "+
			" value='Delete'>");
	    out.println("</form>");
	    out.println("</td></tr></table>");
	}
	out.println("<p><a href=javascript:window.close();>Close This Window</a></p>");
	out.println("</fieldset>");
	out.println("</div>");
	out.print("</body></html>");
	out.flush();
	out.close();

    }
}























































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
@WebServlet(urlPatterns = {"/RiskPersonServ","/RiskPerson"})
public class RiskPersonServ extends TopServlet{

    static Logger logger = LogManager.getLogger(RiskPersonServ.class);
    final static String bgcolor = "silver";// #bfbfbf gray

    /**
     * Generates the Defendent form and processes view, add, update and delete
     * operations.
     * @param req
     * @param res
     */
    
    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException {
	doPost(req,res);
    }
    /**
     * @link #doGetost
     */

    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException {
    
	String fname = "", lname="", 
	    dob="",ssn="", id="", mi="", risk_id="";

	boolean idFound = false, success = true;
	//
	String username = "", message="", action="",entry_time="", 
	    entry_date="", inType="";
	String streetNum="",streetDir="",
	    streetName="",streetType="",sudType="",sudNum="",
	    city="Bloomington", state="IN", zip="", email="", nameTitle="",
	    phonec="",phonew="",phoneh="", addrUpdate="", todayUpdate="",
	    contact="", juvenile="";

	res.setContentType("text/html");
	PrintWriter out = res.getWriter();
	String name, value;
	Enumeration values = req.getParameterNames();
	String [] vals;

	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();	
	    value = Helper.replaceSpecialChars(value);
	    if (name.equals("fname")) {
		fname = value;
	    }
	    else if (name.equals("lname")) {
		lname = value;
	    }
	    else if (name.equals("dob")) {
		dob =value;
	    }
	    else if (name.equals("ssn")) {
		ssn =value;
	    }
	    else if (name.equals("mi")) {
		mi =value.toUpperCase();
	    }
	    else if (name.equals("id")){ // old pid
		id = value;
	    }
	    else if (name.equals("risk_id")){ // case id
		risk_id = value;
	    }
	    else if (name.equals("nameTitle")){ // case id
		nameTitle = value;
	    }
	    else if (name.equals("city")){ 
		city = value.toUpperCase();
	    }
	    else if (name.equals("state")){ 
		state = value.toUpperCase();
	    }
	    else if (name.equals("zip")){ 
		zip = value;
	    }
	    else if (name.equals("contact")){ 
		contact = value;
	    }
	    else if (name.equals("email")){   
		email =value;
	    }
	    else if (name.equals("username")){   
		username =value;
	    }
	    else if (name.equals("streetNum")) {
		streetNum = value;
	    }
	    else if (name.equals("streetDir")) {
		streetDir = value;
	    }
	    else if (name.equals("streetName")) {
		streetName = value;
	    }
	    else if (name.equals("streetType")) {
		streetType = value;
	    }
	    else if (name.equals("sudType")) {
		sudType = value;
	    }
	    else if (name.equals("sudNum")) {
		sudNum = value;
	    }
	    else if (name.equals("phonew")) {
		phonew = value;
	    }
	    else if (name.equals("phonec")) {
		phonec = value;
	    }
	    else if (name.equals("phoneh")) {
		phoneh = value;
	    }
	    else if (name.equals("addrUpdate")) {
		addrUpdate = value;
	    }
	    else if (name.equals("todayUpdate")) {
		todayUpdate = value;
	    }
	    else if (name.equals("inType")){ 
		inType = value;
	    }
	    else if (name.equals("juvenile")){ 
		juvenile = value;
	    }		
	    else if (name.equals("action")){ 
		// Get, Save, zoom, edit, delete, New, Refresh
		action = value;  
	    }
	}
	if(!todayUpdate.equals("")){
	    addrUpdate = Helper.getToday();
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
	if(action.equals("Save")){
	    //
	    PersonList sp = new PersonList(debug);
	    //
	    // we use this info to find out if the person is already
	    // in the system	    
	    sp.setLname(lname);
	    sp.setFname(fname);
	    sp.setDob(dob);
	    id = sp.lookFor();
	    if(!id.equals("")){
		message = " This claimant/defendant is already in the system <br>";
		message += " with <a href="+url+"RiskPersonServ?id="+id+
		    "&action=zoom";
		if(!risk_id.equals(""))
		    message += "&risk_id="+risk_id;
		message +=">"+id+"</a><br>";
		success = false;
		//
	    }
	    else {
		//
		if(addrUpdate.equals("")){
		    addrUpdate = Helper.getToday();
		}
		// set the rest of variables
		sp.setStreetNum(streetNum);
		sp.setStreetDir(streetDir);
		sp.setStreetName(streetName);
		sp.setStreetType(streetType);
		sp.setSudNum(sudNum);
		sp.setSudType(sudType);
		sp.setCity(city);
		sp.setState(state);
		sp.setZip(zip);
		sp.setSsn(ssn);
		sp.setMi(mi);
		sp.setPhonew(phonew);
		sp.setPhonec(phonec);
		sp.setPhoneh(phoneh);
		sp.setEmail(email);
		sp.setContact(contact);
		sp.setNameTitle(nameTitle);
		sp.setAddrUpdate(addrUpdate);
		sp.setJuvenile(juvenile);
		String back = sp.doSave();
		if(!back.equals("")){
		    message += back;
		    success = false;
		    logger.error(back);
		}
		else {
		    message += "Successfully Saved";
		    id = sp.getId();
		}
		if(!risk_id.equals("")){
		    sp.addToRelatedClaim(risk_id);
		}
	    }
	    sp = null;
	}
	else if(action.equals("Update")){
	    //
	    RiskPerson rp = new RiskPerson(debug);
	    rp.setLname(lname);
	    rp.setFname(fname);
	    rp.setDob(dob);
	    rp.setStreetNum(streetNum);
	    rp.setStreetDir(streetDir);
	    rp.setStreetName(streetName);
	    rp.setStreetType(streetType);
	    rp.setSudNum(sudNum);
	    rp.setSudType(sudType);
	    rp.setCity(city);
	    rp.setState(state);
	    rp.setZip(zip);
	    rp.setSsn(ssn);
	    rp.setMi(mi);
	    rp.setPhonew(phonew);
	    rp.setPhonec(phonec);
	    rp.setPhoneh(phoneh);
	    rp.setAddrUpdate(addrUpdate);
	    rp.setEmail(email);
	    rp.setContact(contact);
	    rp.setNameTitle(nameTitle);
	    rp.setJuvenile(juvenile);
	    rp.setId(id);

	    String back = rp.doUpdate();
	    if(!back.equals("")){
		success = false;
		logger.error(back);
		message += back;
	    }
	    else{
		message +="Successfully Updated";
	    }
	    rp = null;
	}
	else if(action.equals("Delete")){
	    //
	    RiskPerson rp = new RiskPerson(debug);
	    rp.setId(id);
	    String back = rp.doDelete();
	    rp = null;
	    if(!back.equals("")){
		System.err.println(back);
		success = false;
		message += back;
	    }
	    else{
		fname = ""; lname=""; dob="";ssn="";
		streetNum="";streetDir="";
		streetName="";streetType="";sudType="";sudNum="";
		city="Bloomington"; mi=""; nameTitle="";
		state="IN"; zip=""; addrUpdate=""; contact="";
		phonew=""; phonec=""; phoneh=""; id="";
		juvenile="";
	    }
	}
	else if(!id.equals("")){	
	    //
	    RiskPerson rp = new RiskPerson(debug);
	    rp.setId(id);
	    String back = rp.doSelect();
	    if(!back.equals("")){
		success = false;
		logger.error(back);
		message += back;
	    }
	    else{
		lname = rp.getLname();
		fname = rp.getFname();
		streetNum = rp.getStreetNum();
		streetDir = rp.getStreetDir();
		streetName = rp.getStreetName();
		streetType = rp.getStreetType();
		sudNum = rp.getSudNum();
		sudType = rp.getSudType();
		dob = rp.getDob();
		addrUpdate = rp.getAddrUpdate();
		city = rp.getCity();
		state = rp.getState();
		zip = rp.getZip();
		phoneh = rp.getPhoneh();
		phonec = rp.getPhonec();
		phonew = rp.getPhonew();
		ssn = rp.getSsn();
		mi = rp.getMi();
		email = rp.getEmail();
		nameTitle = rp.getNameTitle();
		contact = rp.getContact();
		juvenile = rp.getJuvenile();
	    }
	    rp = null;
	}
	//
	// Inserts
	out.println(Inserts.xhtmlHeaderInc);
	out.println(Inserts.banner(url));
	out.println(Inserts.menuBar(url, true));
	out.println(Inserts.sideBar(url));
	out.println("<div id=\"mainContent\">");
	out.println("<h3 class=\"titleBar\">Claimant/Defendant</h3>");
	//
	// if we have any message, it will be shown here
	if(success){
	    if(!message.equals(""))
		out.println("<h2>"+message+"</h2>");
	}
	else{
	    if(!message.equals(""))
		out.println("<h2 class=\"errorMessages\">"+message+"</h2>");
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
	//
	out.println("<fieldset><legend>Personal Info </legend>");
	//
	out.println("<table>");
	if(!id.equals("")){
	    out.println("<tr><td>ID:"+id+"</td></tr>");
	    out.println("<input type=\"hidden\" name=\"id\" value=\""+id+
			"\">");
	}
	// Name
	out.println("<tr><td><label for=\"nameTitle\">Title:</label>");
	out.println("<select name=\"nameTitle\" id=\"nameTitle\">");
	for(int i=0;i<Inserts.nameTitleArr.length;i++){
	    if(nameTitle.equals(Inserts.nameTitleArr[i]))
		out.println("<option selected=\"selected\">"+nameTitle+
			    "\n</option>");
	    else
		out.println("<option>"+Inserts.nameTitleArr[i]+"</option>");
	}
	out.println("</select>");
	out.println("<label for=\"fname\"> First Name: </label>"+
		    "<input name=\"fname\" id=\"fname\" value="+
		    "\""+fname+"\" size=\"20\" maxlength=\"30\" />");
	out.println("<label for=\"mi\"> MI: </label>"+
		    "<input name=\"mi\" id=\"mi\" value="+
		    "\""+mi+"\" size=\"2\" maxlength=\"2\" />");
	out.println("</td></tr>");
	out.println("<tr><td>");
	out.println("<label for=\"lname\"> Last Name/Business: </label>"+
		    "<input name=\"lname\" id=\"lname\" value="+
		    "\""+lname+"\" size=\"40\" maxlength=\"50\" />");
	out.println("</td></tr>");
	//
	// dob ssn
	out.println("<tr><td>");
	out.println("<label for=\"dob\">DOB: </label>"+
		    "<input name=\"dob\" id=\"dob\" value="+
		    "\""+dob+"\" size=\"10\" maxlength=\"10\" "+
		    "onchange=\"checkDate(this)\" />");
	out.println("<label for=\"ssn\">SSN: </label>"+
		    "<input name=\"ssn\" id=\"ssn\" value="+
		    "\""+ssn+"\" size=\"11\" maxlength=\"11\" />");
	if(!juvenile.equals("")){
	    juvenile = "checked=\"checked\"";
	}
	out.println("<input type=\"checkbox\" name=\"juvenile\" "+
		    "id=\"juvenile\" value=\"y\" "+juvenile+"/><label> Juvenile</label>");
	out.println("</td></tr>");
	//
	// Phones
	out.println("<tr><td>"+
		    "<label for=\"phoneh\">Phone, home: </label>"+
		    "<input name=\"phoneh\" id=\"phoneh\" value="+
		    "\""+phoneh+"\" size=\"25\" maxlength=\"30\" />");
	out.println("<label for=\"phonew\">, work: </label>"+
		    "<input name=\"phonew\" id=\"phonew\" value="+
		    "\""+phonew+"\" size=\"25\" maxlength=\"30\" />");
	out.println("<label for=\"phonec\">, cell: </label>"+
		    "<input name=\"phonec\" id=\"phonec\" value="+
		    "\""+phonec+"\" size=\"25\" maxlength=\"30\" />");
	out.println("</td></tr>");
	//
	// Email
	out.println("<tr><td>"+
		    "<label for=\"email\">Email: </label>"+
		    "<input name=\"email\" id=\"email\" value="+
		    "\""+email+"\" size=\"30\" maxlength=\"50\" />");
	out.println("</td></tr>");
	//
	// Ohter Contacts
	out.println("<tr><td>"+
		    "<label for=\"contact\">Other Contact: </label>"+
		    "<input name=\"contact\" id=\"contact\" value="+
		    "\""+contact+"\" size=\"50\" maxlength=\"70\" />");
	out.println("</td></tr>");
	out.println("</table></fieldset>");
	//
	out.println("<fieldset><legend>Address </legend>");
	//
	// Address
	out.println("<table><tr>"+
		    "<td></td><td>Num</td><td>Dir</td>"+
		    "<td>Street</td><td>Type</td></tr>");
	out.println("<td>&nbsp;&nbsp;</td>");
	out.println("<td>");
	out.println("<input name=\"streetNum\" id=\"streetNum\" value="+
		    "\""+streetNum+"\" size=\"8\" maxlength=\"8\" /></td>");
	out.println("<td>");
	out.println("<select name=\"streetDir\" id=\"streetDir\">");
	for(int i=0;i<Helper.dirArr.length;i++){
	    if(streetDir.equals(Helper.dirArr[i]))
		out.println("<option selected=\"selected\">"+streetDir+
			    "</option>");
	    else
		out.println("<option>"+Helper.dirArr[i]+"</option>");
	}
	out.println("</select></td>");
	out.println("<td>");
	out.println("<input name=\"streetName\" id=\"streetName\" value="+
		    "\""+streetName+"\" size=\"30\" maxlength=\"50\" /></td>");
	out.println("<td>");
	out.println("<select name=\"streetType\" id=\"streetType\">");
	for(int i=0;i<Helper.strArr.length;i++){
	    if(streetType.equals(Helper.strIdArr[i]))
		out.println("<option selected=\"selected\" value=\""+
			    streetType+"\">"+Helper.strArr[i]+
			    "</option>");
	    else
		out.println("<option value=\""+Helper.strIdArr[i]+"\">"+
			    Helper.strArr[i]+"</option>");
	}
	out.println("</select></td></tr>");
	out.println("<tr><td></td><td>Type</td><td>Num</td>"+
		    "<td></td><td></td></tr>");
	out.println("<tr><td>Sud</td><td>");
	//
	out.println("<select name=\"sudType\" id=\"sudType\">");
	for(int i=0;i<Helper.sudArr.length;i++){
	    if(sudType.equals(Helper.sudIdArr[i]))
		out.println("<option selected=\"selected\" value=\""+
			    sudType+"\">"+Helper.sudArr[i]+
			    "</option>");
	    else
		out.println("<option value=\""+Helper.sudIdArr[i]+"\">"+
			    Helper.sudArr[i]+"</option>");
	}
	out.println("</select></td><td>");
	out.println("<input name=\"sudNum\" id=\"sudNum\" value="+
		    "\""+sudNum+"\" size=\"8\" maxlength=\"10\" /></td>");
	out.println("</tr>");
	out.println("<tr><td colspan=\"5\"><label for=\"city\">City</lable>");
	out.println("<input name=\"city\" id=\"city\" value="+
		    "\""+city+"\" size=\"20\" maxlength=\"30\" />");
	out.println("<label for=\"state\">State</lable>");
	out.println("<input name=\"state\" id=\"state\" value="+
		    "\""+state+"\" size=\"2\" maxlength=\"2\" />");
	out.println("<label for=\"zip\">Zip</lable>");
	out.println("<input name=\"zip\" id=\"zip\" value="+
		    "\""+zip+"\" size=\"10\" maxlength=\"10\" />");
	out.println("</td></tr>");
	out.println("<tr><td colspan=\"5\"><label for=\"addrUpdate\">"+
		    "Address Last Update:</lable>");
	out.println("<input name=\"addrUpdate\" id=\"addrUpdate\" size=\"10\""+
		    " maxlength=\"10\" value=\""+addrUpdate+"\" "+
		    "onchange=\"checkDate(this)\" "+
		    " />&nbsp;, or ");
	out.println("<input name=\"todayUpdate\" id=\"todayUpdate\" "+
		    " type=\"checkbox\" value=\"y\" /> Today's Date.");
	out.println("</td></tr>");
	out.println("</table>");
	out.println("</fieldset>");
	//
	out.println("<fieldset>");
	if(id.equals("")){
	    out.println("<table class='control'><tr><td>");
	    out.println("<input type=\"submit\" value=\"Save\" "+
			"name=\"action\" />");
	    out.println("</form></td></tr></table>");
	}
	else{ // Save, Update
	    out.println("<table class='control'><tr><td>");
	    out.println("<input value=\"Update\" "+
			"type=\"submit\" name=\"action\" />");
	    out.println("</td><td>");
	    out.println("<input type=\"button\" name=\"action\" "+
			"accesskey=\"n\" value=\"Add Tort Claim\""+
			" tabindex=\"33\" onClick=\"document.location='"+
			url+"TortClaimServ?pid="+id+
			"'\" />");
	    out.println("</td><td>");
	    out.println("<input type=\"button\" name=\"action\" "+
			"accesskey=\"n\" value=\"Add Recovery Actions\""+
			" tabindex=\"33\" onClick=\"document.location='"+
			url+"LegalServ?pid="+id+
			"'\" />");
	    out.println("</td><td></form>");
	    out.println("<form id=\"myform\" onSubmit=\"return "+
			"validateDelete();\">");
	    out.println("<input type=hidden name=\"id\" value=\""+id+"\">");
	    out.println("<input type=\"submit\" name=\"action\" "+
			"value=\"Delete\">");
	    out.println("</form></td></tr>");
	    out.println("</table>");
	}
	out.println("</fieldset>");
	if(!id.equals("")){
						
	    String all = "", all2="", all3="", all4="",
		str="", str2="", back="";
	    String [] claimArr = null, witArr = null, defArr = null;
	    String [] claimAutoArr = null;
	    HandlePerson hp = new HandlePerson(debug);
	    back = hp.generateIdList(id, "claimant");
	    if(back.equals("")){
		claimArr = hp.getIdArr();
	    }
	    back = hp.generateIdList(id, "witness");
	    if(back.equals("")){
		witArr = hp.getIdArr();
	    }
	    back = hp.generateIdList(id, "defendant");
	    if(back.equals("")){
		defArr = hp.getIdArr();
	    }
	    back = hp.generateIdList(id, "claimantAuto");
	    if(back.equals("")){
		claimAutoArr = hp.getIdArr();
	    }						
	    if(claimArr != null && claimArr.length >0){
		TortClaim rp = new TortClaim(debug);
		for(int i=0;i<claimArr.length;i++){
		    rp.setId(claimArr[i]);
		    back = rp.doSelect();
		    if(back.equals("")){
			str = rp.getReceived();
			if(str == null) str = "&nbsp;";
			str2 = rp.getStatus();
			if(str2 == null) str2 = "&nbsp;";
			all += "<tr>";
			all += "<td><a href=\""+url+
			    "TortClaimServ?id="+claimArr[i]+
			    "&action=zoom\">"+claimArr[i]+
			    "</a></td>";	
			all += "<td>"+str+"</td>";
			all += "<td>"+str2+"</td></tr>";	
		    }
		}
	    }
	    if(witArr != null && witArr.length >0){
		TortClaim rp = new TortClaim(debug);
		for(int i=0;i<witArr.length;i++){
		    rp = new TortClaim(debug, witArr[i]);
		    // rp.setId(witArr[i]);
		    back = rp.doSelect();
		    if(back.equals("")){
			str = rp.getReceived();
			if(str == null) str = "&nbsp;";
			str2 = rp.getStatus();
			if(str2 == null) str2 = "&nbsp;";
			all2 += "<tr>";
			all2 += "<td><a href=\""+url+
			    "TortClaimServ?id="+witArr[i]+
			    "&action=zoom\">"+witArr[i]+
			    "</a></td>";	
			all2 += "<td>"+str+"</td>";
			all2 += "<td>"+str2+"</td></tr>";	
		    }
		}
	    }
	    if(defArr != null && defArr.length >0){
		Legal rp = null;
		for(int i=0;i<defArr.length;i++){
		    rp = new Legal(debug, defArr[i]);
		    // rp.setId(defArr[i]);
		    back = rp.doSelect();
		    if(back.equals("")){
			str = rp.getFiled();
			if(str == null) str = "&nbsp;";
			str2 = rp.getStatus();
			if(str2 == null) str2 = "&nbsp;";
			all3 += "<tr>";
			all3 += "<td><a href=\""+url+
			    "LegalServ?id="+defArr[i]+
			    "&action=zoom\">"+defArr[i]+
			    "</a></td>";	
			all3 += "<td>"+str+"</td>";
			all3 += "<td>"+str2+"</td></tr>";	
		    }
		}
	    }
	    if(claimAutoArr != null && claimArr.length >0){
		AutoAccident rp = new AutoAccident(debug);
		for(int i=0;i<claimAutoArr.length;i++){
		    rp.setId(claimAutoArr[i]);
		    back = rp.doSelect();
		    if(back.equals("")){
			str = rp.getReported();
			if(str == null) str = "&nbsp;";
			str2 = rp.getStatus();
			if(str2 == null) str2 = "&nbsp;";
			all4 += "<tr>";
			all4 += "<td><a href=\""+url+
			    "AutoAccidentServ?id="+rp.getId()+
			    "\">"+rp.getId()+
			    "</a></td>";	
			all4 += "<td>"+str+"</td>";
			all4 += "<td>"+str2+"</td></tr>";	
		    }
		}
	    }						
	    if(!all.equals("")){
		out.println("<fieldset><legend>Tort Claims As Claimant "+
			    "</legend>");
		out.println("<table><tr><th>Claim ID </th>"+
			    "<th>Received</th>"+
			    "<th>Status</th></tr>");
		out.println(all);
		out.println("</table>");
		out.println("</fieldset>");
	    }
	    if(!all2.equals("")){
		out.println("<fieldset><legend>Tort Claim As Witness "+
			    "</legend>");
		out.println("<table><tr><th>Claim ID </th>"+
			    "<th>Opened</th>"+
			    "<th>Status</th></tr>");
		out.println(all2);
		out.println("</table>");
		out.println("</fieldset>");
	    }
	    if(!all3.equals("")){
		out.println("<fieldset><legend>Recovery Actions</legend>");
		out.println("<table><tr><th>VS ID </th>"+
			    "<th>Filed</th>"+
			    "<th>Status</th></tr>");
		out.println(all3);
		out.println("</table>");
		out.println("</fieldset>");
	    }
	    if(!all4.equals("")){
		out.println("<fieldset><legend>Auto Accidents "+
			    "</legend>");
		out.println("<table><tr><th>ID </th>"+
			    "<th>Reported</th>"+
			    "<th>Status</th></tr>");
		out.println(all4);
		out.println("</table>");
		out.println("</fieldset>");
	    }						
	}
	out.println("</div>");
	out.print("</body></html>");
	out.flush();
	out.close();
    }

}























































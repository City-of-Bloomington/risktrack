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
@WebServlet(urlPatterns = {"/InsertPersonServ","/InsertPerson"})
public class InsertPersonServ extends TopServlet{

    static Logger logger = LogManager.getLogger(InsertPersonServ.class);
    /**
     * Generates the search defendants form and then list the matching records.
     * The user can check a selection of these and clicks the add defendents
     * @param req
     * @param res
     * @throws ServletException
     * @throws IOException
     */
    
    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException {
	doPost(req,res);
    }
    /**
     * @link Case#doGet
     * @see #doGet
     */
    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException {
    
	res.setContentType("text/html");
	PrintWriter out = res.getWriter();
	String name, value;
	String username = "", password = "";
	boolean success = true, showAll=false;
	String [] titles = {"Check",
	    "Person ID",
	    "Name",
	    "Address"
	};
	boolean [] show = {true,
	    true,true,true
	};
	String action="", inType=""; // starts with "tort","vs","comp","safe"
	String fname = "", lname="", 
	    dob="",ssn="", id="", risk_id="";
	String streetNum="",streetDir="",
	    streetName="",streetType="",sudType="",sudNum="",
	    city="", state="", zip="",
	    phonec="",phonew="",phoneh="", addrUpdate="";
	String message = "", phone="";
	String [] defList = null;
	Enumeration values = req.getParameterNames();
	String [] vals;

	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();	
	    if (name.equals("id")){
		id = value;
	    }
	    else if (name.equals("lname")){
		lname = value.toUpperCase();
	    }
	    else if (name.equals("fname")){
		fname = value.toUpperCase();
	    }
	    else if (name.equals("dob")){
		dob=value;
	    }
	    else if (name.equals("marked")){
		defList=vals; //  array
	    }
	    else if (name.equals("ssn")){
		ssn=value;
	    }
	    else if (name.equals("risk_id")){
		risk_id=value;
	    }
	    else if (name.equals("inType")){
		inType=value;
	    }
	    else if (name.equals("streetNum")) {
		streetNum = value;
	    }
	    else if (name.equals("streetDir")) {
		streetDir = value;
	    }
	    else if (name.equals("streetName")) {
		streetName = value.toUpperCase();
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
	    else if (name.equals("city")) {
		city = value.toUpperCase();
	    }
	    else if (name.equals("state")) {
		state = value.toUpperCase();
	    }
	    else if (name.equals("zip")) {
		zip = value;
	    }
	    else if (name.equals("phone")) {
		phone = value;
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
	System.err.println(" intype "+inType);
	System.err.println(" action "+action);
	System.err.println(" defs "+defList);
						
	if(action.startsWith("Add") && defList != null){
	    if(inType.equals("tort")){
		String back="";
		System.err.println(" add to tort ");
		HandlePerson hp = new HandlePerson(debug);
		hp.setPidArr(defList);
		if(action.endsWith("Claimant")){
		    back += hp.doClaimantInsert(risk_id);
		    if(!back.equals("")){
			message += back;
			success = false;
			logger.error(back);
		    }
		}
		else{
		    back += hp.doWitnessInsert(risk_id);
		    if(!back.equals("")){
			logger.error(back);
			message += back;
			success = false;
		    }
		}
	    }
	    else if(inType.equals("auto")){
		System.err.println(" add to auto ");
		String back="";
		HandlePerson hp = new HandlePerson(debug);
		hp.setPidArr(defList);
		back += hp.doClaimantAutoInsert(risk_id);
		if(!back.equals("")){
		    message += back;
		    success = false;
		    logger.error(back);
		}
	    }						
	    else if(inType.equals("legal")){
		System.err.println(" add to vs ");
		String back="";
		HandlePerson hp = new HandlePerson(debug);
		hp.setPidArr(defList);
		back += hp.doDefendantInsert(risk_id);
		if(!back.equals("")){
		    logger.error(back);
		    message += back;
		    success = false;
		}
	    }
	}
	//
	// Inserts
	out.println(Inserts.xhtmlHeaderInc);
	out.println(Inserts.banner(url));
	out.println(Inserts.menuBar(url, true));
	out.println(Inserts.sideBar(url));
	out.println("<div id=\"mainContent\">");
	if(inType.equals("tort"))
	    out.println("<h2>Search & Insert Claimant/Witness </h2>");
	else if(inType.equals("legal"))
	    out.println("<h2>Search & Insert Defendant </h2>");
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
	if(action.startsWith("Insert")){
	    if(success){
		out.println("<h3>The selected records were inserted "+
			    "successfully</h3>");
	    }
	    else {
		// warnig/error 
		out.println("<h3>Some/All of the selected records were "+
			    "NOT inserted </h3>");

	    }
	}
	out.println("<fieldset><legend>Search</legend>");
	out.println("<table>");
	out.println("<form name=\"myForm\" method=\"post\" >");
	out.println("<input type=\"hidden\" id=\"risk_id\" name=\"risk_id\" value=\""+
		    risk_id+"\" />");
	out.println("<input type=\"hidden\" id=\"inType\" name=\"inType\" "+
		    "value=\""+inType+"\" />");
	//
	// 1st block
	out.println("<tr><td>ID:</td><td>");
	out.println("<input name=\"id\" id=\"id\" value=\""+id+"\""+
		    " size=\"8\" maxlength=\"8\" /></td></tr>");
	out.println("<tr><td>");
	out.println("Last Name: </td><td>");
	out.println("<input name=\"lname\" id=\"lname\" value=\""+lname+"\""+
		    " size=\"30\" maxlength=\"50\" /></td></tr>");
	out.println("<tr><td>First Name:</td><td>");
	out.println("<input name=\"fname\" id=\"fname\" value=\""+fname+"\""+
		    " size=\"30\" maxlength=\"30\" /></td></tr>");
	//
	out.println("<tr><td>SS#:</td><td> ");
	out.println("<input name=\"ssn\" id=\"ssn\" value=\""+ ssn+"\""+
		    " size=\"11\" maxlength=\"11\" />");
	out.println(" DOB: ");
	out.println("<input name=\"dob\" id=\"dob\" value=\""+ dob+"\""+
		    " size=\"10\" maxlength=\"10\" /></td></tr>");
	//
	out.println("<tr><td>Phone:</td><td> ");
	out.println("<input name=\"phone\" id=\"phone\" value=\""+ phone+"\""+
		    " size=\"10\" maxlength=\"10\" /></td></tr>");
	// address
	out.println("<tr><td>Address:</td><td><table>");
	out.println("<tr><td>St. No. </td><td>Dir</td><td>"+
		    "St. Name</td></tr>");
	out.println("<tr>");
	out.println("<td><input name=\"streetNum\" size=\"8\" "+
		    "id=\"streetNum\" "+
		    "maxlength=\"8\" value=\""+streetNum+"\" />");
	out.println("</td><td>");
	out.println("<select name=\"streetDir\" id=\"streetDir\">");
	for(int i=0;i<Helper.dirArr.length;i++){
	    out.println("<option>"+Helper.dirArr[i]+"</option>");
	}
	out.println("</select></td><td>");
	out.println("<input name=\"streetName\" size=\"20\" "+
		    " id=\"streetName\" "+
		    "maxlength=\"20\" value=\""+streetName+"\">");
	out.println("</td></tr>");
	out.println("<tr><td>St. Type</td><td>Sud Type</td><td>"+
		    "Sud Num</td></tr>");
	//
	out.println("<tr><td>");
	out.println("<select name=\"streetType\" id=\"streetType\">");
	for(int i=0;i<Helper.strIdArr.length;i++){
	    out.println("<option value=\""+
			Helper.strIdArr[i]+"\">"+
			Helper.strArr[i]+"</option>");
	}
	out.println("</select></td><td>");
	out.println("<select name=\"sudType\" id=\"sudType\">");
	for(int i=0;i<Helper.sudArr.length;i++){
	    out.println("<option value=\""+
			Helper.sudIdArr[i]+"\">"+
			Helper.sudArr[i]+"</option>");
	}
	out.println("</select></td><td>");
	out.println("<input name=\"sudNum\" id=\"sudNum\" size=\"8\" "+
		    "maxlength=\"8\" value=\""+sudNum+"\" /></td></tr>");
	// 
	// city state zip
	out.println("<tr><td>City</td><td>State</td><td>Zip</td></tr>");
	out.println("<tr><td>");
	out.println("<input name=\"city\" size=\"20\" id=\"city\" "+
		    "maxlength=\"30\" value=\""+city+"\" /></td><td>");
	out.println("<input name=\"state\" size=\"2\" id=\"state\" "+
		    "maxlength=\"2\" value=\""+state+"\" /></td><td>");
	out.println("<input name=\"zip\" size=\"14\" id=\"zip\" "+
		    "maxlength=\"14\" value=\""+zip+"\" /></td></tr>");
	out.println("</table></td></tr>");
	out.println("<tr><td colspan=2>");
	// 
	// ToDo remove font and use css
	out.println("<font color=green size=-1>* You can use part of "+
		    "the name, multiple names space separated. </font><br>");
	out.println("</td></tr></table>");
	out.println("</fieldset>");

	out.println("<input type=\"submit\" "+
		    "name=\"action\" value=\"Search\" />");

	if(inType.startsWith("tort")){
	    out.println("<li><a href=\""+url+"TortClaimServ?id="+risk_id+
			"&action=zoom"+
			"\">Back to Related Tort Claim</a></li>");
	}
	else if(inType.startsWith("legal")){
	    out.println("<li><a href=\""+url+"LegalServ?id="+risk_id+
			"&action=zoom"+
			"\">Back to Related VS</a></li>");

	}
	else if(inType.startsWith("auto")){
	    out.println("<li><a href=\""+url+"AutoAccidentServ?id="+risk_id+
			"&action=zoom"+
			"\">Back to Related Auto Accident</a></li>");

	}				
	if(action.equals("Search")){
	    PersonList sp = new PersonList(debug);
	    if(!id.equals("")){
		sp.setId(id);
	    }
	    if(!ssn.equals("")){
		sp.setSsn(ssn);
	    }
	    if(!dob.equals("")){
		sp.setDob(dob);
	    }
	    if(!streetNum.equals("")){
		sp.setStreetNum(streetNum);
	    }
	    if(!streetDir.equals("")){
		sp.setStreetDir(streetDir);
	    }
	    if(!streetType.equals("")){
		sp.setStreetType(streetType);
	    }
	    if(!streetName.equals("")){
		sp.setStreetDir(streetDir);
	    }
	    if(!sudType.equals("")){
		sp.setSudType(sudType);
	    }
	    if(!sudNum.equals("")){
		sp.setSudNum(sudNum);
	    }
	    if(!city.equals("")){
		sp.setCity(city);
	    }
	    if(!state.equals("")){
		sp.setState(state);
	    }
	    if(!zip.equals("")){
		sp.setZip(zip);
	    }
	    if(!phone.equals("")){
		sp.setAnyPhone(phone);
	    }
	    if(!lname.equals("")){
		if(lname.indexOf(" ")  > -1){
		    String [] tokens = lname.split("\\s");
		    sp.setLnameArr(tokens);
		}
		else 
		    sp.setLname(lname);
	    }
	    if(!fname.equals("")){
		if(fname.indexOf(" ")  > -1){
		    String [] tokens = fname.split("\\s");
		    sp.setFnameArr(tokens);
		}
		else
		    sp.setFname(fname);
	    }
	    int ncnt = 0;						
	    message = sp.lookFor();
	    List<RiskPerson> persons = null;
	    if(message.equals("")){
		persons = sp.getPersons();
		ncnt = persons.size();
	    }
	    String str = "";
	    out.println("<h4>Total Matching Records "+ ncnt +" </h4>");
	    if(ncnt > 0){

		out.println("<table border>");
		out.println("<tr>");
		for (int c = 0; c < titles.length; c++){ 
		    if(show[c] || showAll)
			out.println("<th>"+titles[c]+"</th>");
		}	   
		out.println("</tr>");
		for(RiskPerson pp:persons){
		    out.println("<tr><td>");
		    out.println("<input type=\"checkbox\" name=\"marked\""+
				" value=\""+pp.getId()+"\" /></td>");
		    out.println("<th>"+pp.getId()+"</th>");
		    //
		    // name
		    str = pp.getFullName();
		    if(str.equals("")) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		    //
		    // address
		    str = pp.getAddress();
		    if(str.equals("")) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		    out.println("</tr>");	
		}
		out.println("</table>");
		if(ncnt > 0){
		    if(inType.equals("tort")){
			out.println("<table><tr>");
			out.println("<td>");
			out.println("<input type=\"submit\" name=\"action\" "+
				    "value=\"Add as Claimant\" /></td>");
			out.println("<td>");
			out.println("<input type=\"submit\" name=\"action\" "+
				    "value=\"Add as Witness\" /></td>");
			out.println("</tr></table>");
		    }
		    else if(inType.equals("auto")){
			out.println("<table><tr>");
			out.println("<td>");
			out.println("<input type=\"submit\" name=\"action\" "+
				    "value=\"Add as Claimant\" /></td>");
			out.println("</tr></table>");
		    }										
		    else if(inType.equals("legal")){
			out.println("<table><tr>");
			out.println("<td>");
			out.println("<input type=\"submit\" name=\"action\" "+
				    "value=\"Add as Defendant\" /></td>");
			out.println("</tr></table>");
		    }
		}
	    }
	}
	out.println("</form>");
	out.println("</div>");
	out.println("</body></html>");
	out.close();

    }

}























































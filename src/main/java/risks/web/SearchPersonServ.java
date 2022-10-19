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
@WebServlet(urlPatterns = {"/SearchPersonServ","/SearchPerson"})
public class SearchPersonServ extends TopServlet{

    static Logger logger = LogManager.getLogger(SearchPersonServ.class);
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
	String message = "";
	boolean success = true, showAll=false;

	String action="", inType=""; // starts with "tort","vs","comp","safe"
	String fname = "", lname="", 
	    dob="",ssn="", id="", risk_id="", mi="";
	String streetNum="",streetDir="",
	    streetName="",streetType="",sudType="",sudNum="",
	    city="", state="", zip="", sortby="",
	    phonec="",phonew="",phoneh="", addrUpdate="", contact="";
	String  from="", to="", phone="";
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
		defList=vals; // an array
	    }
	    else if (name.equals("risk_id")){
		risk_id=value;
	    }
	    else if (name.equals("ssn")){
		ssn=value;
	    }
	    else if (name.equals("mi")){
		mi=value;
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
	    else if (name.equals("from")) {
		from = value;
	    }
	    else if (name.equals("to")) {
		to = value;
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

	// Inserts
	out.println(Inserts.xhtmlHeaderInc);
	out.println(Inserts.banner(url));
	out.println(Inserts.menuBar(url, true));
	out.println(Inserts.sideBar(url));
	//
	out.println("<div id=\"mainContent\">");
	//
	out.println("<h3 class=\"titleBar\">Search Claimant/Defendant</h3>");
	if(success){
	    if(!message.equals(""))
		out.println("<h2>"+message+"</h2>");
	}
	else{
	    if(!message.equals(""))
		out.println("<h3 class=\"errorMessages\">"+message+"</h3>");
	}
	//
	//the real table
	out.println("<form name=\"myForm\" method=\"post\">");
	//   "onSubmit=\"return validateForm()\">");
	//
	out.println("<input type=\"hidden\" id=\"risk_id\" name=\"risk_id\" value=\""+risk_id+"\" />");
	out.println("<input type=\"hidden\" id=\"inType\" name=\"inType\" value=\""+inType+"\" />");
	//
	// 1st block
	out.println("<fieldset>");
	out.println("<table>");
	out.println("<tr><td><b>ID:</b></td><td>");
	out.println("<input name=\"id\" id=\"id\" value=\""+id+"\""+
		    " size=\"8\" maxlength=\"8\" /></td></tr>");
	out.println("<tr><td>");
	out.println("Name/Business: </td><td>");
	out.println("<input name=\"lname\" id=\"person_name\" value=\""+lname+"\""+
		    " size=\"30\" maxlength=\"30\" />*</td></tr>");
	out.println("<tr><td>");
	out.println("First Name: </td><td>");
	out.println("<input name=\"fname\" id=\"fname\" value=\""+fname+"\""+
		    " size=\"30\" maxlength=\"30\" /></td></tr>");				
	out.println("<tr><td>Phone:</td><td> ");
	out.println("<input name=\"phone\" id=\"phone\" value=\""+ phone+"\""+
		    " size=\"10\" maxlength=\"10\" /></td></tr>");
	//
	out.println("<tr><td>Other Contact:</td><td> ");
	out.println("<input name=\"contact\" id=\"contact\" value=\""+
		    contact+"\""+
		    " size=\"30\" maxlength=\"40\" /></td></tr>");
	//
	out.println("<tr><td>DOB:</td><td> ");
	out.println("<input name=\"dob\" id=\"dob\" value=\""+ dob+"\""+
		    " size=\"10\" maxlength=\"10\" /> SS#:");
	out.println("<input name=\"ssn\" id=\"ssn\" value=\""+ ssn+"\""+
		    " size=\"10\" maxlength=\"10\" /></td></tr>");
	out.println("<tr><td colspan=\"2\">* Start typing person first or last name "+
		    "then pick from the list.</td></tr>");
	out.println("</table></td></tr>");
	out.println("</fieldset>");
	//
	// address
	out.println("<fieldset><legend>Address</legend>");
	out.println("<table><tr><td><table>");
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
	out.println("<tr><td><table><tr><td></td><td>from</td><td>to</td></tr>");
	out.println("<tr><td>Address Last Update</td>");
	out.println("<td>");
	out.println("<input name=\"from\" id=\"from\" size=\"10\" value=\""+from+"\">");
	out.println("</td><td>");
	out.println("<input name=\"to\" id=\"from\" size=\"10\" value=\""+to+"\">");
	out.println("</td></tr>");
	out.println("<tr><td>Sort by:</td><td colspan=2>");
	out.println("<select name='sortby'>");
	out.println("<option selected value=''>\n");
	out.println("<option value='lname'>Last Name, First Name");
	out.println("<option value='fname'>First Name, Last Name");
	out.println("<option value=address'>Address");
	out.println("<option value='lastUpdate'>Update Date");
	out.println("</select>");
	out.println("</td></tr></table></td></tr>");
	out.println("</table>");
	out.println("</fieldset>");
	out.println("<fieldset>");
	out.println("<table class=\"control\">");
	out.println("<tr><td>");
	out.println("<button type=\"submit\" "+
		    "name=\"action\">Search</button>");
	out.println("</td></tr></table>");
	out.println("</fieldset>");
	//

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
	String message = "";
	boolean success = true, showAll=false;
	boolean connectDbOk = false;
	String [] titles = {"Person ID",
	    "Name",
	    "D.O.B",
	    "Address",
	    "City, State Zip",
	    "Phones",
	    "Address Last Update"
	};
	boolean [] show = {true, true, false, 
	    true, true, true, true
	};
	String action="", inType=""; // starts with "tort","vs","comp","safe"
	String fname = "", lname="", 
	    dob="",ssn="", risk_id="", id="";
	String streetNum="",streetDir="", 
	    streetName="",streetType="",sudType="",sudNum="",
	    city="Bloomington", state="IN", zip="", contact="", sortby="",
	    phonec="",phonew="",phoneh="", addrUpdate="";
	String phone="",to="",from="";
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
		// lname = Helper.escapeIt(value);
		lname = value;
	    }
	    else if (name.equals("fname")){
		fname = Helper.escapeIt(value);
	    }
	    else if (name.equals("contact")){
		contact = value;
	    }
	    else if (name.equals("dob")){
		dob=value;
	    }
	    else if (name.equals("marked")){
		defList=vals; // an array
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
		streetName = Helper.escapeIt(value.toUpperCase());
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
	    else if (name.equals("from")) {
		from = value;
	    }
	    else if (name.equals("to")) {
		to = value;
	    }
	    else if (name.equals("phone")) {
		phone = value;
	    }
	    else if (name.equals("sortby")) {
		sortby = value;
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
	out.println("<div id=\"mainContent\">");
	out.println("<h1>Search Claimant/Defendants</h1>");

	SearchPerson sp = new SearchPerson(debug);
	if(!id.equals("")){
	    sp.setId(id);
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
	if(!contact.equals("")){
	    sp.setContact(contact);
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
	if(!from.equals("")){
	    sp.setUpdateFrom(from);
	}
	if(!to.equals("")){
	    sp.setUpdateTo(to);
	}
	if(!sortby.equals("")){
	    sp.setSortBy(sortby);
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
	String msg = sp.lookFor();
	List<RiskPerson> persons = null;
	int ncnt = 0;
	if(msg.equals("")){
	    persons = sp.getPersons();
	    if(persons != null && persons.size() > 0){
		ncnt = persons.size();
	    }
	    else{
		message = "No match found ";
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
		out.println("<h2 class=\"errorMessages\">Error: "+message+"</h2>");
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
	    for(RiskPerson pp: persons){
		out.println("<tr>");
		// 
		// ID
		out.println("<td><a href=\""+url+"RiskPerson?"+
			    "id="+pp.getId()+"\">"+
			    pp.getId()+"</a></td>"); 
		//
		// name
		str = pp.getFullName();
		if(str.equals("")) str = "&nbsp;";
		out.println("<td>"+str+"</td>");
		//
		// dob
		if(show[2]){
		    str = pp.getDob();
		    str = str.trim();
		    if(str.equals("")) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		//
		// address
		if(show[3]){
		    str = pp.getAddress();
		    if(str.equals("")) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		//
		// city, state, zip
		if(show[4]){
		    str = pp.getCityStateZip();
		    if(str.equals(",")) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		//
		// phones
		if(show[5]){
		    str = pp.getPhones();
		    if(str.equals("")) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		// 
		// last addr update
		if(show[6]){
		    str = pp.getAddrUpdate();
		    if(str.equals("")) str = "&nbsp;";
		    out.println("<td>"+str+"</td>");
		}
		out.println("</tr>");
	    }
	    out.println("<table>");
	}
	//
	out.print("</div>");
	out.print("</body></html>");
	out.close();
    }

}























































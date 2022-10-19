package risks.web;

import java.util.*;
import java.sql.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.models.*;
import risks.lists.*;
import risks.utils.*;
/**
 *
 */
@WebServlet(urlPatterns = {"/ClaimLetterServ","/ClaimLetter"})
public class ClaimLetterServ extends TopServlet{

    String riskManager="",riskAttorney="",safetyDirector="",
	claimAdmin="",riskCounsel="";
    static Logger logger = LogManager.getLogger(ClaimLetterServ.class);
    String months[] = {"January","February","March","April","May","June",
	"July",
	"August","September","October",
	"November","December"};
    //
    // New subcategory list
    //
    static final String[] closedArr = {
	"",
	"Paid",
	"Complied",
	"Dismissed"
    };
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
    
	String id="";
	boolean connectDbOk = false, success = true;
       	String username = "", message = "", action = "";
	//
	Hashtable env = new Hashtable(11); 

	res.setContentType("text/html");
	PrintWriter out = res.getWriter();
	String name, value;
	Enumeration values = req.getParameterNames();
	String [] vals;
	String[] witList = null, clmntList = null;
	
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();	

	    if (name.equals("id")) {
		id = value;
	    }
	}
	if(riskManager.equals("")){
	    riskManager = getServletContext().getInitParameter("riskManager");
	    riskAttorney = getServletContext().getInitParameter("riskAttorney");
	    safetyDirector = getServletContext().getInitParameter("safetyDirector");
	    claimAdmin = getServletContext().getInitParameter("claimAdmin");
	    riskCounsel = getServletContext().getInitParameter("riskCounsel");
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
	Calendar cal = Calendar.getInstance();
	String letterDate = 
	    months[cal.get(Calendar.MONTH)]+", "+ 
	    cal.get(Calendar.DATE) + 
	    " " + cal.get(Calendar.YEAR);
	out.println(Inserts.xhtmlHeaderInc);
	out.println("<head><title> </title></head>");
	out.println("<body>");
	//
	// look for claimants on this claim and create one letter for each
	//
	if(id.equals("")){
	    out.println("Error Missing Claim ID<br /> ");
	}
	else{
	    String str="", str2="", back="";
	    List<RiskPerson> claiments = null;
	    TortClaim tort = new TortClaim(debug, id);
	    back = tort.doSelect();
	    if(back.equals("")){
		claiments = tort.getClaiments();
	    }
	    /*
	      String [] clmntArr = null;
	      HandlePerson hp = new HandlePerson(debug);
	      back = hp.generatePidList(id, "claimant");
	      if(back.equals("")){
	      clmntArr = hp.getPidArr();
	      }
	      if(clmntArr != null && clmntArr.length >0){
	      for(int i=0;i<clmntArr.length;i++){
	      RiskPerson rp = new RiskPerson(debug, clmntArr[i]);
	      back = rp.doSelect();
	      if(back.equals("")){
	    */
	    if(claiments != null && claiments.size() > 0){
		int ncnt = claiments.size();
		for(RiskPerson rp:claiments){
		    String fullName="",nameTitle="",lname="",address="",
			cityStateZip="", contact="";
		    fullName = rp.getFullName();
		    contact = rp.getContact();
		    nameTitle = rp.getNameTitle();
		    if(nameTitle == null) nameTitle = "";
		    lname = rp.getLname();
		    if(lname == null) lname = "";
		    address = rp.getAddress();
		    cityStateZip = rp.getCityStateZip();
		    writeClaim(out,
			       letterDate,
			       fullName,
			       nameTitle,
			       lname,
			       address,
			       cityStateZip,
			       contact,
			       ncnt
			       );
		}
	    }
	}
	out.println("</body></html>");
	out.flush();
	out.close();
    }
    //
    void writeClaim(PrintWriter out,
		    String letterDate,
		    String fullName,
		    String nameTitle,
		    String lname,
		    String address,
		    String cityStateZip,
		    String contact,
		    int count
		    ){
	out.println("<center>");	   
	out.println("<br /><br /><br /><br />");
	out.println("<table width='100%'>");
	//
	// line 1
	out.println("<tr><td align='left'>"+riskCounsel+"</td><td align='right'>"+
		    riskManager+"</td></tr>");
	// line 2
	out.println("<tr><td align='left'><b>Corporation Counsel</b></td>"+
		    "<td align='right'>"+
		    "<b>Assistant City Attorney/Risk Manager</b></td></tr>");
	//
	// line 3 empty space
	out.println("<tr><td>&nbsp;&nbsp;</td></tr>");
	//
	// line 5,6
	out.println("<tr><td colspan='2' align='right'>"+
		    riskAttorney+"</td></tr>");
	out.println("<tr><td colspan='2' align='right'>"+
		    "<b>Risk Attorney</b></td></tr>");
	//
	// line 7 empty space
	out.println("<tr><td>&nbsp;&nbsp;</td></tr>");
	//
	// line 8,9
	out.println("<tr><td colspan='2' align='right'>"+safetyDirector+
		    "</td></tr>");
	out.println("<tr><td colspan='2' align='right'>"+
		    "<b> Safety Director</b></td></tr>");
	//
	// line 10 empty space
	out.println("<tr><td>&nbsp;&nbsp;</td></tr>");

	// line 11,12
	out.println("<tr><td colspan='2' align='right'>"+
		    claimAdmin+"</td></tr>");
	out.println("<tr><td colspan='2' align='right'>"+
		    "<b> Claims Administrator</b></td></tr>");
	out.println("</table>");
	out.println("<br /><br /><br />");
	out.println(letterDate+"<br /><br /><br />");
	out.println("</center>");
	out.println(fullName+"<br />");
	if(!contact.equals("")){
	    out.println("Attn: "+contact+"<br />");
	}
	out.println(address+"<br />");
	out.println(cityStateZip+"<br />");
	//
	out.println("<br />");
	out.println("Dear "+nameTitle+" "+lname+",<br />");
	out.println("<p>");
	out.println("Enclosed please find the form necessary to file a claim against the City of Bloomington in regard to your incident.  Please complete the enclosed Tort Claim Notice and return it to the address on the form along with any supporting documentation pertaining to this claim.");
	out.println("</p>");
	out.println("<p>");
	out.println(" These documents will be submitted to the City's insurance company, which will contact you soon thereafter regarding its position on the City's liability for your loss. Please contact the Risk Management Office at 812-349-3438 with any questions regarding this claim."); 
	out.println("</p>");
	out.println("<br />");
	out.println("<table border='0'><tr><td align='left'>");
	out.println("Sincerely,<br />");
	out.println("<br /><br /><br /><br />");
	out.println(claimAdmin+"<br />");
	out.println("Claims Administrator<br />");
	out.println("<br /><br />");
	out.println("Enclosures <br />");
	out.println("</td></tr></table></center>");
	out.println("<br />");
	//
	if(count > 1){
	    // page breaker
	    out.println("<h3 style='page-break-after:always'>&nbsp;</h3>");
	}
	//
    }

}























































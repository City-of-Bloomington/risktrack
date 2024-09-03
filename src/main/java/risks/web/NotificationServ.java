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
@WebServlet(urlPatterns = {"/NotificationServ","/Notification"})
public class NotificationServ extends TopServlet{

    static Logger logger = LogManager.getLogger(NotificationServ.class);
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
    
       	String username = "", message = "", action = "",msg="";
	String prev_date="", next_date="";
	boolean success = true;
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

	    if (name.equals("action")){ 
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
	if(action.equals("Schedule") && user.isAdmin()){
	    String date = Helper.getToday();
	    // System.err.println(" scheduled date "+date);
	    NotificationScheduler sched = new NotificationScheduler(date);
	    QuartzMisc quartzMisc = new QuartzMisc(debug);
	    msg = quartzMisc.findScheduledDates();
	    if(msg.isEmpty()){
		prev_date = quartzMisc.getPrevScheduleDate();
		if(prev_date.startsWith("1969")) // 0 cuases 1969 schedule date
		    prev_date = "No Previous date found";
		next_date = quartzMisc.getNextScheduleDate();
	    }
	    try{
		msg = sched.run();
		if(!msg.isEmpty()){
		    message = msg;
		    success = false;
		}
		else{
		    message = "Successfully Scheduled";		    
		}
	    }
	    catch(Exception ex){
		msg += ex;
		success = false;
	    }
	}
	out.println(Inserts.xhtmlHeaderInc);
	out.println(Inserts.banner(url));
	out.println(Inserts.menuBar(url, true));
	out.println(Inserts.sideBar(url, user));
	out.println("<div id=\"mainContent\">");
	out.println("<h3 class=\"titleBar\">Notification Scheduler</h3>");
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
	out.println("<form id=\"myForm\" method=\"post\">");
	out.println("<fieldset><legend>Notification Scheduler </legend>");
	out.println("<p>This function is to be run only once by Admin only</p>");
	out.println("<table>");			
	out.println("<tr><td><label>To Start a Schedule:</label>"+
		    "</td><td>");
	out.println("<input name=\"action\" type=\"submit\" "+
		    "type=\"button\" value=\"Schedule\" />");
	out.println("</td></tr>");
	out.println("<tr><td><label>To See Notification Logs:</label>"+
		    "</td><td>");
	out.println("<input name=\"action\" type=\"submit\" "+
		    "type=\"button\" value=\"Logs\" />");
	out.println("</td></tr>");
	out.println("</table>");
	out.println("</fieldset>");
	if(action.equals("Schedule")){
	    out.println("<fieldset><legend>Schedule Info</legend>");
	    
	    out.println("<table>");
	    out.println("<tr><td>Previous Schedule Date:</td><td>"+prev_date+"</td></tr>"); 
	    out.println("<tr><td>Next Schedule Date:</td><td>"+next_date+"</td></tr>"); 
	    out.println("</table>");
	    out.println("</fieldset>");	    
	}
	if(true){
	    NotificationLogList nll = new NotificationLogList(debug);
	    String back = nll.find();
	    if(back.isEmpty()){
		List<NotificationLog> logs = nll.getLogs();
		if(logs != null && logs.size() > 0){
		    out.println("<fieldset><legend>Notification Logs</legend>");
		    out.println("<table>");
		    out.println("<tr><th>Date</th><th>Receiver</th><th>Message</th><th>Error Message</th></tr>");
		    for(NotificationLog one:logs){
			out.println("<tr>");
			out.println("<td>"+one.getDate()+"</td></tr>");
			out.println("<td>"+one.getReceiver()+"</td></tr>");		
			out.println("<td>"+one.getMessage()+"</td></tr>");
			out.println("<td>"+one.getErrorMsg()+"</td></tr>");
			out.println("</tr>");
		    }
		    out.println("</table>");
		    out.println("</fieldset>");	   		    
		}
	    }
	}
	out.print("</body></html>");
	out.flush();
	out.close();

    }

}























































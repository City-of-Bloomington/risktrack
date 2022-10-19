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
@WebServlet(urlPatterns = {"/PaymentServ","/Payment"})
public class PaymentServ extends TopServlet{

    static Logger logger = LogManager.getLogger(PaymentServ.class);
    static final String [] payMethod = {
	"",
	"Cash",
	"Check",
	"M.O.",
	"C.C.",
	"B.C.",
	"Other"
    };

    /**
     * Generates the payment form and process it.
     * @param req
     * @param res
     * @throws ServletException
     * @throws IOException
     */
    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException{

	res.setContentType("text/html");
	PrintWriter out = res.getWriter();

	String name, value;
	String action="";
	String username = "";
	double amount = 0, balance=0, total_paid=0, total_due=0;
	String amountStr = "0", balanceStr="0", receiptDate="";
	String paidDate="",paidMethod="",checkNo="",clerk="";
	String paidBy="",risk_id="",id="", message="",mccFlag="", type="";

	boolean connectDbOk = false, success = true;
	Enumeration values = req.getParameterNames();
	HttpSession session = null;

	out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
	String [] vals;
	while (values.hasMoreElements()){

	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();	
	    if (name.equals("amount")) {
		if(!value.equals("")){
		    int ind = value.indexOf(".");
		    int len = value.length();
		    if(ind < 0) amountStr = value+".00";
		    else if((len - ind) == 1) amountStr = value+"00";
		    else if((len - ind) == 2) amountStr = value+"0";
		    else if((len -ind) == 3) amountStr = value;
		    else if((len - ind) > 3) amountStr = 
						 value.substring(0,ind+3);
		    try{
			amount = Double.valueOf(value).doubleValue();
			
		    }catch(Exception ex){}; //just ignore
		}
	    }
	    else if (name.equals("clerk")) {
		clerk = value;
	    }
	    else if (name.equals("paidMethod")) {
		paidMethod = value;
	    }
	    else if (name.equals("paidBy")) {
		paidBy = value;
	    }
	    else if (name.equals("paidDate")) {
		paidDate =value;
	    }
	    else if (name.equals("checkNo")) {
		checkNo =value;
	    }
	    else if (name.equals("id")) {
		id=value;
	    }
	    else if (name.equals("risk_id")) {
		risk_id=value;
	    }
	    else if (name.equals("type")) {
		type=value;
	    }
	    else if (name.equals("action")){ 
		// bill, change pay status
		action = value;  
	    }
	}
	User user = null;
	session = req.getSession();
	if(session != null){
	    user = (User)session.getAttribute("user");
	}
	if(user == null){
	    res.sendRedirect(url+"Login");
	    return;
	}		
	//
	// we need these for default values of dates
	String today = Helper.getToday();
	//
	if(paidDate.equals(""))
	    paidDate = today;
	if(action.equals("Save")){
	    //
	    Payment pay = new Payment(debug);
	    pay.setRisk_id(risk_id);
	    pay.setType(type);
			
	    pay.setAmount(amountStr);
	    pay.setPaidMethod(paidMethod);
	    pay.setCheckNo(checkNo);
	    pay.setPaidBy(paidBy);
	    pay.setClerk(clerk);
	    pay.setPaidDate(paidDate);
	    pay.setReceiptDate(receiptDate);
	    String back = pay.doSave();
	    if(!back.equals("")){
		logger.error(back);
		message += " Could not save "+back;
		success = false;
	    }
	    else{
		message += " Data saved ";
		id = pay.getId();
	    }
	}
	else if(action.equals("Update")){
	    //
	    Payment pay = new Payment(debug);
	    pay.setId(id);
	    pay.setRisk_id(risk_id);
	    pay.setType(type);
	    pay.setAmount(amountStr);
	    pay.setPaidMethod(paidMethod);
	    pay.setCheckNo(checkNo);
	    pay.setPaidBy(paidBy);
	    pay.setClerk(clerk);
	    pay.setPaidDate(paidDate);
	    pay.setReceiptDate(receiptDate);
	    String back = pay.doUpdate();
	    if(!back.equals("")){
		message += " Could not update "+back;
		success = false;
		logger.error(back);
	    }
	    else{
		message += " Data updated ";
	    }

	}
	else if(!id.equals("")){
	    //
	    Payment pay = new Payment(debug);
	    pay.setId(id);
	    String back = pay.doSelect();
	    if(!back.equals("")){
		message += " Could not retreive data "+back;
		success = false;
		logger.error(back);
	    }
	    else{
		risk_id = pay.getRisk_id();
		amountStr = pay.getAmount();
		paidDate = pay.getPaidDate();
		receiptDate = pay.getReceiptDate();
		paidBy = pay.getPaidBy();
		paidMethod = pay.getPaidMethod();
		clerk = pay.getClerk();
		checkNo = pay.getCheckNo();
	    }
	}
	else if(action.equals("Delete")){
	    //
	    Payment pay = new Payment(debug);
	    pay.setId(id);
	    String back = pay.doDelete();
	    if(!back.equals("")){
		message += " Could not delete data "+back;
		success = false;
		logger.error(back);
	    }
	    else{
		message += " Deleted successfully ";
	    }
	    id = "";
	}
	if(true){
	    //
	    // Check the balance
	    // we always need this
	    //
	    Payment pay = new Payment(debug);
	    pay.setRisk_id(risk_id);
	    pay.setType(type);
	    balanceStr = pay.getTotalBalance();
	}
	//
	if(!clerk.equals("")) clerk = "checked";
	out.println("<html><head><title>Receipt</title>");
	out.println("<style type=\"text/css\">");
	out.println("#g_cell{ font-size: 20px; color:green; } ");
	out.println("#b_hd{ font-size: 1.5em; font-style:bold;color:white; }");
	out.println("</style>");
	out.println("<script type=\"text/javascript\">");
	out.println("  function checkDate(dt){                          ");
	out.println("    var dd = dt.value;                             ");
	out.println("   if(dd.length == 0) return true;                 ");
	out.println("   var dar = dd.split(\"/\");                      ");
	out.println(" if(dar.length < 3){                               ");
	out.println(" alert(\"Not a valid date: \"+dd);                 ");
	out.println("      dt.focus();                                  ");
	out.println("      return false;}                               ");
	out.println("   var m = dar[0];                                 ");
	out.println("   var d = dar[1];                                 ");
	out.println("   var y = dar[2];                                 ");
	out.println("   if(isNaN(m) || isNaN(d) || isNaN(y)){           ");
	out.println(" alert(\"Not a valid date: \"+dd);                 ");
	out.println("      dt.focus();                                  ");
	out.println("      return false; }                              ");
	out.println("   if( !((m > 0 && m < 13) && (d > 0 && d <32) &&  ");
	out.println("    (y > 1970 && y < 2099))){                      ");
	out.println(" alert(\"Not a valid date: \"+dd);                 ");
	out.println("      dt.focus();                                  ");
	out.println("      return false; }                              ");
	out.println("    return true;                                   ");
	out.println("    }                                              ");
	out.println("   function formatNumber(xx){                      ");
	out.println("     var  x = new String(xx);                      "); 
	out.println("     var  n = indexOf(x,\".\");                    ");
	//out.println("       alert(\" x idex \"+x+\" \"+n);            ");
	out.println("      if(n > -1){                                  ");
	out.println("       var  l = x.length;                          ");
	out.println("       var  r = n*1+3*1;                           ");
	out.println("        if( r < l ){                               ");
	out.println("        var y = x.substring(0,r);                  ");
	//out.println("       alert(\" x idex \"+x+\" \"+y);            ");
	out.println("        return y;                                  ");
	out.println("          }                                        ");
	out.println("        }                                          ");
	out.println("    return x;                                      ");
	out.println("     }                                             ");
	out.println("   function indexOf(xx,s){                         ");
	out.println("       var  l = xx.length;                         ");
	out.println("       var o = -1;                                 ");
	out.println("     for(var i=0; i<l; i++){                       ");
	out.println("        var c = xx.charAt(i);                      ");
	out.println("       if(c == s) o = i;                           ");
	out.println("      }                                            ");
	out.println("    return o;                                      ");
	out.println("   }                                               ");
	out.println("  function validateForm(){	                        ");
	out.println("  with(document.myForm){                           ");
	// checking dates and numeric values
	// check the numbers
	//
	out.println("if(!checkDate(paid_date))return false;     ");
	out.println(" }                                         ");
	out.println(" if(!checkNumber(document.myForm.amount)){ ");
	out.println("   return false;}			        ");
	// 
	// Everything Ok
	out.println("  return true;			        ");
	out.println(" }	         		                ");
	out.println(" function checkNumber(x){                  ");
	out.println(" if (x.value.length > 0){                  ");
	out.println("  if(isNaN(x.value)){                      ");
	out.println("  alert(x.value+\" Not a valid number\");  ");
	out.println("   x.focus();                              ");
	out.println("   return false;			        ");
	out.println("    }}				        ");
	out.println("  return true;		                ");
	out.println(" }	         		                ");
	out.println("  function validateDelete(){	        ");
	out.println("   var x = false;                          ");
	out.println("   x = confirm(\"Are you sure you want to delete this record\");");
	out.println("  return x;                                         ");
	out.println("	}			        	         ");
	out.println("  function firstFocus(){                            ");
	out.println("	}			       		         ");
	out.println("  function computeBalance(){                        ");
	out.println("  with(document.myForm){                            ");
	out.println("  var a = amount.value;                             ");
	out.println("  var b = prev_balance.value;                       ");
	out.println("  var c = prev_amount.value;                       ");
	out.println("  if(a.length > 0 && b.length > 0){                 ");
	out.println("  if(isNaN(a)){                                     ");
	out.println("    alert(a+\" Not a valid number\");               ");
	out.println("  amount.focus();                                   ");
	out.println("  return; }                                         ");
	out.println("  if(isNaN(b)){                                     ");
	out.println("    alert(b+\" Not a valid number\");               ");
	out.println("  return; }                                         ");
	out.println("  var c = b*1-a*1+c*1;                               ");
	out.println("  balance.value=formatNumber(c);                    ");
	out.println("  }}}                                               ");
	out.println(" </script>				                 ");
	out.println(" </head><body onload=\"firstFocus()\" >             ");
	out.println("<center>");
	//
	// show any error message
	//
	if(!message.equals("")){
	    if(!success)
		out.println("<h3><font color=red>"+message+"</font></h3>");
	}
	// delete or New
	if(id.equals("")){
	    out.println("<h2>New Receipt</h2>");
	}
	else if(action.equals("Save")){
	    out.println("<h2>Edit Receipt</h2>");
	}
	else if(action.equals("Update")){
	    out.println("<h2>Edit Receipt</h2>");
	}
	else if(!id.equals("") && !action.startsWith("Print")){
	    out.println("<h2>View/Edit Receipt</h2>");
	}
	if(action.startsWith("Print")){
	    //
	    out.println("<img src=\""+url+"images/citylogo.gif\" "+
			" width=70 height=70 alt=\"City Logo\"><br>");
	    out.println("<h2>RECEIPT</h2>");
	    out.println("<h3>CITY OF BLOOMINGTON, IN<br><i>"+
			"RISK DEPARTMENT</i></h3>");
	    out.println("<table>");
	    out.println("<tr><td align=right><b>Receipt No. </b></td><td> "+
			id+"</td></tr>");
	    out.println("<tr><td align=right><b>Receipt Date: </b></td><td>"+
			paidDate+"</td></tr>");
	    out.println("<tr><td align=right><b>Received From: </b></td><td>"+
			paidBy+"</td></tr>");
	    out.println("<tr><td align=right><b>The Sum of: </b></td><td>$"+
			Helper.formatNumber(amountStr)+"</td></tr>");
	    if(type.equals("legal"))
		out.println("<tr><td align=right><b>Recovery Action ID: </b></td><td>"+
			    risk_id+"</td></tr>");
	    else if(type.equals("safety"))
		out.println("<tr><td align=right><b>Internal Accident ID: </b></td><td>"+
			    risk_id+"</td></tr>");
	    else if(type.equals("tort"))
		out.println("<tr><td align=right><b>Tort Claim ID: </b></td><td>"+
			    risk_id+"</td></tr>");
	    out.println("<tr><td align=right><b>Balance: </b></td><td>$"+
			Helper.formatNumber(balanceStr)+"</td></tr>");
	    out.println("<tr><td align=right><b>Payment By: </b></td><td>"+
			paidMethod+"</td></tr>");
	    if(!checkNo.equals("")){
		out.println("<tr><td align=right><b>Check Number: </b></td><td>"+
			    checkNo+"</td></tr>");
	    }
	    if(!clerk.equals("") || !mccFlag.equals("")){
		out.println("<tr><td align=right><b> </b></td><td>");
		out.println("<form><input type=checkbox checked>MCC</form>");
		out.println("</td></tr>");
	    }
	    out.println("</table>");
	    out.println("<br><br>");
	    out.println("<b>Approved by the State Board of Accounts, "+
			"2004.<b>");
	    out.println("<br><br>");
	    out.println("<font size=+1>Thank you for your payment"+
			"</font><br>");
	}
	else{
	    //
	    // Add/Edit record
	    //
	    out.println("<table width=60% border>");
	    out.println("<tr><td><table width=100%>");
	    out.println("<form name=myForm method=post "+
			"onSubmit=\"return validateForm()\">");
	    out.println("<input type=\"hidden\" name=\"risk_id\" value=\""+risk_id+"\"/>");
	    out.println("<input type=\"hidden\" name=\"type\" value=\""+type+"\" />");
	    out.println("<input type=\"hidden\" name=\"prev_balance\" "+
			" value=\""+balanceStr+"\" />");
	    out.println("<input type=\"hidden\" name=\"prev_amount\" "+
			" value=\""+amountStr+"\" />");						
	    if(!id.equals("")){
		out.println("<input type=\"hidden\" name=\"id\" value=\""+id+"\" />");
		out.println("<tr><td>Receipt #:</td><td> "+id+"</td></tr>");
	    }

	    out.println("<tr><td>Related Risk:</td><td>");
	    out.println(risk_id+"</td></tr>");
	    out.println("<tr><td>Receipt date:</td><td>");
	    out.println("<input name=\"paidDate\" size=\"10\" "+
			"maxlength=\"10\" value=\""+paidDate+"\" /></td></tr>");
	    out.println("<tr><td>Paid by: </td><td>");
	    out.println("<input name=\"paidBy\" size=\"40\" "+
			"maxlength=\"50\" value=\""+paidBy+"\" /></td></tr>");
	    //
	    out.println("<tr><td>Amount Paid: </td><td>");
	    out.println("<input name=\"amount\" size=\"12\" "+
			" onchange=computeBalance() "+
			"maxlength=\"12\" value=\""+amountStr+"\"/></td></tr>");

	    out.println("<tr><td>Current Balance: </td><td>");
	    out.println("<input name=\"balance\" size=\"12\" disabled=\"disabled\"  "+
			" value=\""+balanceStr+"\" /></td></tr>");
	    //
	    out.println("<tr><td>Payment Method: </td><td>");
	    out.println("<select name=\"paidMethod\">");
	    for(int i=0;i<payMethod.length;i++){
		if(payMethod[i].equals(paidMethod))
		    out.println("<option selected=\"selected\">"+payMethod[i]+"</option>");
		else
		    out.println("<option>"+payMethod[i]+"</option>");
	    }
	    out.println("</select></td><td>");
	    out.println("<tr><td>Check No.:</td><td> ");
	    out.println("<input name=\"checkNo\" size=\"15\" "+
			"maxlength=\"15\" value=\""+checkNo+"\" /></td></tr>");
	    out.println("<tr><td>&nbsp;</td><td> ");
	    if(!mccFlag.equals("") || !clerk.equals("")) clerk ="checked=\"checked\"";
	    out.println("<input type=\"checkbox\" name=\"clerk\"  "+
			clerk+" value=\"y\" /> MCC</td></tr>");
	    out.println("</table></td></tr>");
	    if(id.equals("")){ 
		out.println("<tr><td align=right>");
		out.println("<input type=\"submit\" "+
			    "name=\"action\" value=\"Save\" />");
		out.println("</td></tr>"); 
		out.println("</form>");
	    }
	    else{ 
		out.println("<tr><td "+
			    " valign=\"top\" align=\"right\"><table width=\"60%\">");
		if(user.isAdmin()){
		    out.println("<td valign=\"top\"><input "+
				"type=\"submit\" name=\"action\" value=\"Update\" />");
		    out.println("</td>");
		}
		out.println("<td valign=top><input "+
			    "type=\"submit\" name=\"action\" value=\"Printable\" />");
		out.println("</td></form>");
		if(user.isAdmin()){
		    out.println("<td><form name=\"myform\" onSubmit=\"return "+
				"validateDelete();\">");
		    out.println("<input type=\"hidden\" name=\"id\" value=\""+id+"\" />");
		    out.println("<input type=\"hidden\" name=\"type\" value=\""+type+"\" />");
		    out.println("<input type=\"hidden\" name=\"risk_id\" value=\""+risk_id+"\" />");
		    out.println("<input type=\"submit\" name=\"action\" "+
				"value=\"Delete\" />");
		    out.println("</form></td>");
		}
		out.println("</tr></table></center></td></tr>");
	    }
	    out.println("</table><br>");
	    if(true){
		Payment pay = new Payment(debug);
		pay.setRisk_id(risk_id);
		pay.setType(type);
				
		int nct = 0;
		String back = pay.findPayments();
		if(back.equals("") && pay.hasPayments()){
		    List<Payment> payments = pay.getPayments();
		    out.println("<table border width=40%><tr><td>");
		    out.println("<table width=\"100%\"><tr><td>Payment</td>"+
				"<td>Amount</td><td>Date</td></tr>");
		    out.println("<caption>Payment History</caption>");
		    for(Payment one:payments){
			out.println("<tr><td>");
			out.println("<a href=\""+url+"PaymentServ?risk_id="+risk_id+
				    "&id="+one.getId()+
				    "&action=zoom"+
				    "&type="+type+
				    "\">"+
				    one.getId()+"</a></td>");
			out.println("<td>"+one.getAmount()+"</td>");
			out.println("<td>"+one.getPaidDate()+"</td></tr>");
		    }
		    out.println("</table></td></tr></table>");
		}
	    }
	}
	out.println("</body>");
	out.println("</html>");
		
    }
    /**
     * @see doPost.
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

}
























































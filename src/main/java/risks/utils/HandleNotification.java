package risks.utils;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.*;
import java.sql.*;
import java.text.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import javax.servlet.ServletContext;
import risks.models.*;
import risks.lists.*;
import javax.servlet.ServletContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HandleNotification{

    boolean debug = false, activeMail = false;
    static final long serialVersionUID = 53L;
    static String mail_from = "donotreply@bloomington.in.gov";
    static String mail_host = "";
    static Logger logger = LogManager.getLogger(HandleNotification.class);
    String receiver=""; // group
    List<TortClaim> torts = null;
 		
    public HandleNotification(){
	doInit();
    }
    // setting the parameter values 
    void doInit(){
	ServletContext ctx = SingleContextHolder.getContext();
	if(ctx != null){
	    System.err.println(" ctx is Ok");				
	    String val = ctx.getInitParameter("activeMail");
	    if(val != null && val.equals("true")){
		activeMail = true;
	    }
	    val = ctx.getInitParameter("receiver");
	    if(val != null && !val.isEmpty()){
		receiver = val;
	    }
	    val = ctx.getInitParameter("mail_host");
	    if(val != null && !val.isEmpty()){
		mail_host = val;
	    }	    
	    System.err.println(" activeMail "+activeMail);										
	}
	else{
	    System.err.println(" ctx is null, could not retreive activeMail flag ");
	}
    }
    //
    String findMatchingClaims(){
	TortClaimList tcl = new TortClaimList(debug);
	String back = tcl.findForNotification();
	if(back.isEmpty()){
	    List<TortClaim> ones = tcl.getTorts();
	    if(ones != null && ones.size()> 0){
		torts = ones;
	    }
	    if(torts == null || torts.size() == 0){
		System.err.println(" No matching record found ");
	    }
	}
	return back ;
    }
    public String process(){
	String back = "";
	if(torts == null){
	    back = findMatchingClaims();
	    if(!back.isEmpty()){
		return back;
	    }						
	}
	if(torts == null || torts.size() < 1){
	    back = "No claims to process";
	    System.err.println(" Notification: claims found ");
	    return back;
	}
	if(activeMail){
	    NotificationLog noteLog = new NotificationLog(debug);
	    String note_msg = "The notification included the following claims: ";
	    String subject = "RiskTrack 30 or 75 days claim notification ";
	    String body_text ="This is an automated message from the RT app.\n";	    
	    body_text += "RT app found the following clains has 30 or 75 days since the received date.\n\n";	    
	    for(TortClaim one:torts){
		body_text += " Claim id: "+one.getId()+"\n";
		body_text += " Type: "+one.getRiskType()+"\n";
		body_text += " Incident Date: "+one.getIncidentDate()+"\n";
		body_text += " Received : "+one.getReceived()+"\n";
		if(!one.getIncident().isEmpty()){
		    body_text += "Details: "+one.getIncident()+"\n";
		}
		body_text += " -----------\n\n ";
		if(!note_msg.isEmpty()) note_msg += ", ";
		note_msg += one.getId();
	    }
	    noteLog.setReceiver(receiver);
	    noteLog.setMessage(note_msg);
	    Properties props = new Properties();
	    props.put("mail.smtp.host", mail_host);
				
	    Session session = Session.getDefaultInstance(props, null);
	    try{
		Message message = new MimeMessage(session);						
		message.setSubject(subject);
		message.setText(body_text);
		message.setFrom(new InternetAddress(mail_from));
		InternetAddress[] addrArray = InternetAddress.parse(receiver);
		message.setRecipients(Message.RecipientType.TO, addrArray);
		Transport.send(message);
		
	    }
	    catch (MessagingException mex){
		//
		// Failure
		back += mex;
		//
		logger.error(mex);
		Exception ex = mex;
		do {
		    if (ex instanceof SendFailedException) {
			SendFailedException sfex = (SendFailedException)ex;
			javax.mail.Address [] invalid = sfex.getInvalidAddresses();
			if (invalid != null) {
			    logger.error("    ** Invalid Addresses");
			    if (invalid != null) {
				for (int i = 0; i < invalid.length; i++) 
				    logger.error("         " + invalid[i]);
			    }
			}
			javax.mail.Address [] validUnsent = sfex.getValidUnsentAddresses();
			if (validUnsent != null) {
			    logger.error("    ** ValidUnsent Addresses");
			    if (validUnsent != null) {
				for (int i = 0; i < validUnsent.length; i++) 
				    logger.error("         "+validUnsent[i]);
			    }
			}
			javax.mail.Address [] validSent = sfex.getValidSentAddresses();
			if (validSent != null) {
			    logger.error("    ** ValidSent Addresses");
			    if (validSent != null) {
				for (int i = 0; i < validSent.length; i++) 
				    logger.error("         "+validSent[i]);
			    }
			}
		    }
		    if (ex instanceof MessagingException)
			ex = ((MessagingException)ex).getNextException();
		    else { // any other exception
			logger.error(ex);
			ex = null;
		    }
		} while (ex != null);
	    } catch (Exception ex){
		logger.error(ex);
		noteLog.setErrorMsg("Error "+ex);
	    }
	    noteLog.doSave();
	}	    
	return back;
    }
    
}

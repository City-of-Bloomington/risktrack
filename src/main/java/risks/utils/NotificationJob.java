package risks.utils;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */

import java.util.*;
import java.sql.*;
import java.io.*;
import java.text.*;
import javax.sql.*;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobDataMap;
import risks.models.*;
import risks.lists.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NotificationJob implements Job{

    boolean debug = true;
    static final long serialVersionUID = 55L;		
    static Logger logger = LogManager.getLogger(NotificationJob.class);
    boolean activeMail = false;
    String mail_host = "";
    String mail_receiver = "";
    public NotificationJob(){

    }
    public void execute(JobExecutionContext context)
        throws JobExecutionException {
				
	try{
	    doInit();
	    doWork();
	    doDestroy();
	}
	catch(Exception ex){
	    logger.error(ex);
	    System.err.println(ex);
	}
    }
    public void doInit(){

    }
    public void doDestroy() {

    }	    
    public void doWork(){
	HandleNotification handle = new
	    HandleNotification();
	String msg = handle.process();
	if(!msg.isEmpty())
	    logger.error(msg);
	
    }

}























































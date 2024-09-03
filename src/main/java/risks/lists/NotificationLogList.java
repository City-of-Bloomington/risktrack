package risks.lists;
/**
 * @copyright Copyright (C) 2014-2022 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.models.*;
import risks.utils.*;

public class NotificationLogList{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(NotificationLogList.class);
    String id = "",
	date = "",
	note_text = "";
    List<NotificationLog> logs = null;
    public NotificationLogList(boolean deb){
	debug = deb;
    }
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    //
    // getters
    //
    public String getId(){
	return id;
    }
    public List<NotificationLog> getLogs(){
	return logs;
    }
    public String find(){
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;	
	String qq = "select id,receiver,message,date_format(process_date,'%m/%d/%y'),error_msg from notification_logs order by id desc limit 5";		
	String msg = "", qw="";

	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    return "Could not connect to DB ";
	}
	try{
	    stmt = con.prepareStatement(qq);
	    rs = stmt.executeQuery();
	    while(rs.next()){
		NotificationLog one =
		    new NotificationLog(debug,
			     rs.getString(1),
			     rs.getString(2),
			     rs.getString(3),
			     rs.getString(4),
			     rs.getString(5));
		if(logs == null)
		    logs = new ArrayList<>();
		if(!logs.contains(one))
		    logs.add(one);
	    }
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    msg += ex;
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	return msg;

    }
	
}

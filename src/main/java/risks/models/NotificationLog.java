package risks.models;
import java.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.utils.*;

public class NotificationLog{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(NotificationLog.class);
    String id = "", receiver="", message = "", date="", error_msg="";
    public NotificationLog(){

    }
    public NotificationLog(boolean deb){
	debug = deb;
    }
    public NotificationLog(boolean deb, String val){
	debug = deb;
	setId(val);
    }
    public NotificationLog(boolean deb,
			   String val,
			   String val2,
			   String val3
		){
	debug = deb;
	setReceiver(val);
	setMessage(val2);
	setErrorMsg(val3);
    }	    
    //
    public NotificationLog(boolean deb,
			   String val,
			   String val2,
			   String val3,
			   String val4,
			   String val5
		){
	debug = deb;
	setId(val);
	setReceiver(val2);
	setMessage(val3);
	setDate(val4);
	setErrorMsg(val5);
    }		
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setReceiver(String val){
	if(val != null)
	    receiver = val;
    }
    public void setMessage(String val){
	if(val != null)
	    message = val;
    }		
    public void setDate(String val){
	if(val != null)
	    date = val;
    }
    public void setErrorMsg(String val){
	if(val != null)
	    error_msg = val.trim();
    }
    //
    // getters
    //
    public String getId(){
	return id;
    }
    public String getReceiver(){
	return receiver;
    }
    public String getMessage(){
	return message;
    }		
    public String getDate(){
	if(id.equals("")){
	    return Helper.getToday();
	}
	return date;
    }
    public String getErrorMsg(){
	return error_msg;
    }

    public String toString(){
	return date+" "+message+" "+error_msg;
    }
				
    public boolean equals(Object o) {
	if (o instanceof NotificationLog) {
	    NotificationLog c = (NotificationLog) o;
	    if ( this.id.equals(c.getId())) 
		return true;
	}
	return false;
    }
    public int hashCode(){
	int seed = 37;
	if(!id.equals("")){
	    try{
		seed += Integer.parseInt(id)*31;
	    }catch(Exception ex){
		// we ignore
	    }
	}
	return seed;
    }		
	
    public String doSelect(){
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;	
		
	String msg = "";
	String qq = "select id,receiver,message,date_format(process_date,'%m/%d/%y'),error_msg from notification_logs ";
	if(!id.equals("")){
	    qq += " where id = ? ";
	}
	else{
	    msg = "  id not set ";
	    return msg;
	}

	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    return "Could not connect to DB ";
	}
	try{
	    stmt = con.prepareStatement(qq);
	    stmt.setString(1, id);
	    rs = stmt.executeQuery();
	    if(rs.next()){
		setId(rs.getString(1));
		setReceiver(rs.getString(2));
		setMessage(rs.getString(3));
		setDate(rs.getString(4));
		setErrorMsg(rs.getString(5));
	    }
	    else{
		msg = " no match found "+id;
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
    public String doSave(){

	String msg = "";
	Connection con = null;
	PreparedStatement stmt = null, stmt2=null;
	ResultSet rs = null;			
	String str = "", qq = " insert into notification_logs values(0,?,?,now(),?)";
	con = Helper.getConnection();
	if(con == null){
	    return "Could not connect to DB ";
	}
	try{
	    stmt = con.prepareStatement(qq);
	    stmt.setString(1, receiver);
	    stmt.setString(2, message);
	    if(error_msg.isEmpty())
		stmt.setNull(3, Types.VARCHAR);
	    else
		stmt.setString(3, error_msg);
	    stmt.executeUpdate();
	    qq = "select LAST_INSERT_ID() ";
	    if(debug){
		logger.debug(qq);
	    }
	    stmt2 = con.prepareStatement(qq);			
	    rs = stmt2.executeQuery();
	    if(rs.next()){
		id = rs.getString(1);
	    }
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    msg += ex;
	}
	finally{		
	    Helper.databaseDisconnect(con, rs, stmt, stmt2);
	}
	return msg;
    }
    /**
    create table notification_logs(
    id int not null auto_increment primary key,
    receiver varchar(160),
    message varchar(1024),
    process_date date,
    error_msg varchar(1024)
    )engine=InnoDB;

    */
}

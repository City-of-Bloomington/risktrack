package risks.models;
import java.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.utils.*;

public class Note{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(Note.class);
    String id = "",
	user_id="", risk_id="",
	date = "",
	note_text = "";
    User user = null;
    String errors = "";
    public Note(){

    }
    public Note(boolean deb){
	debug = deb;
    }
    public Note(boolean deb, String val){
	debug = deb;
	setId(val);
    }		
    //
    public Note(boolean deb,
		String val,
		String val2,
		String val3,
		String val4,
		String val5
		){
	debug = deb;
	setId(val);
	setRisk_id(val2);
	setUser_id(val3);
	setDate(val4);
	setNoteText(val5);
    }		
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setUser_id(String val){
	if(val != null)
	    user_id = val;
    }
    public void setRisk_id(String val){
	if(val != null)
	    risk_id = val;
    }		
    public void setDate(String val){
	if(val != null)
	    date = val;
    }
    public void setNoteText(String val){
	if(val != null)
	    note_text = val.trim();
    }
    public void setUser(User val){
	if(val != null){
	    user = val;
	    user_id = user.getId();
	}
    }
    public User getUser(){
	if(user == null && !user_id.equals("")){
	    User one = new User(debug, user_id);
	    String back = one.doSelect();
	    if(back.equals("")){
		user = one;
	    }
	}
	return user;
    }
    //
    // getters
    //
    public String getId(){
	return id;
    }
    public String getUser_id(){
	return user_id;
    }
    public String getRisk_id(){
	return risk_id;
    }		
    public String getDate(){
	if(id.equals("")){
	    return Helper.getToday();
	}
	return date;
    }
    public String getNoteText(){
	return note_text;
    }
    boolean isEmpty(){
		
	return note_text.trim().equals("");

    }
    public String toString(){
	return note_text;
    }
				
    public boolean equals(Object o) {
	if (o instanceof Note) {
	    Note c = (Note) o;
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
	String qq = "select risk_id,user_id,date_format(date,'%m/%d/%y'),note_text from notes ";
	if(!id.equals("")){
	    qq += " where id = ? ";
	}
	else{
	    msg = " note id not set ";
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
		setRisk_id(rs.getString(1));
		setUser_id(rs.getString(2));
		setDate(rs.getString(3));
		setNoteText(rs.getString(4));
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
	String str = "", qq = " insert into notes values(0,?,?,now(),?)";
	if(isEmpty()){
	    msg = "no date to save ";
	    return msg;
	}
	if(user_id.equals("")){
	    msg = "user info not set ";
	    return msg;
	}
	if(risk_id.equals("")){
	    msg = "related risk not set ";
	    return msg;
	}				
	con = Helper.getConnection();
	if(con == null){
	    return "Could not connect to DB ";
	}
	try{
	    stmt = con.prepareStatement(qq);
	    stmt.setString(1, risk_id);						
	    stmt.setString(2, user_id);
	    stmt.setString(3, note_text);

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
    //
    public String doUpdate(){
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;			
	String msg = "";
	String str = "", qq="";
	if(isEmpty()){
	    msg = "No text to be saved ";
	    return msg;
	}
	qq = " update notes set note_text=? where id=? ";
	if(debug)
	    logger.debug(qq);
	con = Helper.getConnection();
	if(con == null){
	    return "Could not connect to DB ";
	}				
	try{
	    stmt = con.prepareStatement(qq);
	    stmt.setString(1, note_text);
	    stmt.setString(2, id);
	    stmt.executeUpdate();
	}
	catch(Exception ex){
	    msg += ex+":"+qq;						
	    logger.error(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	return msg;
    }

    public String doDelete(){

	Connection con = null;
	PreparedStatement stmt = null, stmt2=null;
	ResultSet rs = null;			
	String msg = "";
	String str = "", qq="";
	qq = " delete from notes where id=?";
	if(debug)
	    logger.debug(qq);
	con = Helper.getConnection();
	if(con == null){
	    return "Could not connect to DB ";
	}				
	try{
	    stmt = con.prepareStatement(qq);
	    stmt.setString(1, id);
	    stmt.executeUpdate();
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
	
}

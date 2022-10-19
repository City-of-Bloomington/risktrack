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

public class NoteList{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(NoteList.class);
    String id = "",
	user_id="", risk_id="",
	date = "",
	note_text = "";
    List<Note> notes = null;
    public NoteList(boolean deb){
	debug = deb;
    }
    public NoteList(boolean deb, String val){
	debug = deb;
	setRisk_id(val);
    }		
    //
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
    public void setNoteText(String val){
	if(val != null)
	    note_text = val.trim();
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
    public String getNoteText(){
	return note_text;
    }
    public String toString(){
	return "";
    }
    public List<Note> getNotes(){
	return notes;
    }
    public String find(){
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;	
		
	String msg = "", qw="";
	String qq = "select id,risk_id,user_id,date_format(date,'%m/%d/%Y'),note_text from notes ";
	if(!id.equals("")){
	    qw = " id = ? ";
	}
	else if(!risk_id.equals("")){
	    qw = " risk_id = ? ";
	}
	if(!qw.equals("")){
	    qq += " where "+qw;
	}
	qq += " order by id desc ";
	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    return "Could not connect to DB ";
	}
	try{
	    stmt = con.prepareStatement(qq);
	    if(!id.equals(""))
		stmt.setString(1, id);
	    else if(!risk_id.equals(""))
		stmt.setString(1, risk_id);
	    rs = stmt.executeQuery();
	    while(rs.next()){
		Note one =
		    new Note(debug,
			     rs.getString(1),
			     rs.getString(2),
			     rs.getString(3),
			     rs.getString(4),
			     rs.getString(5));
		if(notes == null)
		    notes = new ArrayList<>();
		if(!notes.contains(one))
		    notes.add(one);
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

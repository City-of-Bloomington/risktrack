package risks.models;

import java.util.*;
import java.sql.*;
import javax.sql.*;
import java.io.File;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.utils.*;
/**
 *
 */

public class RiskFile{
	
    boolean debug;
    static Logger logger = LogManager.getLogger(RiskFile.class);
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    String id="", risk_id ="", errors="", 
	notes="", name="", old_name="", added_by_id="",
	date="";
    User addedBy = null;
    //
    //
    // basic constructor
    public RiskFile(boolean deb){

	debug = deb;
	//
	// initialize
	//
    }
    public RiskFile(boolean deb, String val){

	debug = deb;
	//
	// initialize
	//
	setId(val);
    }
    public RiskFile(boolean deb,
		    String val,
		    String val2,
		    String val3,
		    String val4,
		    String val5,
		    String val6,
		    String val7
		    ){

	debug = deb;
	setId(val);
	setRisk_id(val2);
	setAdded_by_id(val3);
	setDate(val4);		
	setName(val5);
	setOldName(val6);				
	setNotes(val7);
    }	
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setRisk_id(String val){
	if(val != null)		
	    risk_id = val;
    }
    public void setDate(String val){
	if(val != null)
	    date = val;
    }
    public void setNotes(String val){
	if(val != null)
	    notes = val;
    }
    public void setName(String val){
	if(val != null)
	    name = val;
    }
    public void setAdded_by_id(String val){
	if(val != null)
	    added_by_id = val;
    }	
    public void setOldName(String val){
	if(val != null)
	    old_name = val.replace(" ","_");
    }
    public void setAddedBy(User val){
	if(val != null){
	    addedBy = val;
	    added_by_id = addedBy.getId();
	}
    }
    //
    // getters
    //
    public String  getId(){
	return id;
    }
    public     String  getRisk_id(){
	return risk_id;
    }
    public     String  getNotes(){
	return notes;
    }
    public     String  getName(){
	return name;
    }
    public     String  getOldName(){
	return old_name;
    }
    public     String  getAdded_by_id(){
	return added_by_id;
    }
    public     User getAddedBy(){
	if(addedBy == null && !added_by_id.equals("")){
	    User one = new User(debug, added_by_id);
	    String back = one.doSelect();
	    if(back.equals(""))
		addedBy = one;
	}
	return addedBy;
    }
    public     String  getDate(){
	if(id.equals("")){
	    date = Helper.getToday(); // mm/dd/yyyy format
	}
	return date;
    }
    public     boolean hasNotes(){
	return !notes.equals("");
    }
    public     String  getErrors(){
	return errors;
    }
    public     String getFullPath(String dir, String ext){
	String path = dir;
	String yy="", separator="/"; // linux
	separator = File.separator;
	if(name.equals("")){
	    if(id.equals("")){
		composeName(ext);
	    }
	    else{
		doSelect();
	    }
	}
	if(date.equals("")){
	    date = Helper.getToday();
	}
	if(!date.equals("")){
	    yy = date.substring(6);
	}
	if(!yy.equals("")){
	    path += yy;
	}
	path += separator;
	File myDir = new File(path);
	if(!myDir.isDirectory()){
	    myDir.mkdirs();
	}
	return path;
    }

    /**
     * for download purpose
     */
    public     String getPath(String dir){
	String path = dir;
	String yy="", separator="/"; // linux
	separator = File.separator;
	if(!date.equals("")){
	    yy = date.substring(6); // year 4 digits
	}
	if(!yy.equals("")){
	    path += yy;
	}
	path += separator;
	return path;
    }	
    public     String composeName(String ext){
	String back = getNewIndex();
	if(back.equals("")){
	    name = "risk_"+risk_id+"_"+id+"."+ext;
	    date = Helper.getToday();
	}
	return back;
    }
    public     String getNewIndex(){
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null, pstmt2=null;
	ResultSet rs = null;
	date = Helper.getToday();
	String qq = "insert into risk_file_seq values(0)";
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    logger.error(back);
	    return back;
	}
	if(debug){
	    logger.debug(qq);
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.executeUpdate();
	    //
	    // get the id of the new record
	    //
	    qq = "select LAST_INSERT_ID() ";
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt2 = con.prepareStatement(qq);				
	    rs = pstmt2.executeQuery();
	    if(rs.next()){
		id = rs.getString(1);
	    }
	}
	catch(Exception ex){
	    back += ex;
	    logger.error(back);
	}
	finally{
	    Helper.databaseDisconnect(con, rs, pstmt, pstmt2);
	}
	return back;

    }
    public     String doSave(){

	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	date = Helper.getToday();
	String qq = "insert into risk_files values(?,?,?,now(),?,?,?)";
	if(name.equals("")){
	    back = "File name not set ";
	    logger.error(back);
	    return back;
	}
	if(risk_id.equals("")){
	    back = "Related risk accident type id not set ";
	    logger.error(back);
	    return back;
	}				
	if(old_name.equals("")){
	    old_name = name;
	}
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    logger.error(back);
	    return back;
	}
	if(debug){
	    logger.debug(qq);
	}				
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,id);		
	    pstmt.setString(2,risk_id);
	    if(added_by_id.equals(""))
		pstmt.setNull(3,Types.INTEGER);
	    else
		pstmt.setString(3,added_by_id);				
	    pstmt.setString(4,name);
	    pstmt.setString(5,old_name);								
	    if(notes.equals(""))
		pstmt.setNull(6,Types.VARCHAR);
	    else
		pstmt.setString(6,notes);

	    pstmt.executeUpdate();
	    //
	}
	catch(Exception ex){
	    back += ex;
	    logger.error(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return back;
    }
    public     String doUpdate(){
		
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	date = Helper.getToday();
	String qq = "update risk_files set notes=? ";
	if(!name.equals("")){
	    qq += ", name = ? ";
	}
	qq += " where id=? ";
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    logger.error(back);
	    return back;
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    if(debug){
		logger.debug(qq);
	    }
	    int jj=1;
	    pstmt.setString(jj++,notes);
	    if(!name.equals("")){
		pstmt.setString(jj++,name);
	    }
	    pstmt.setString(jj,id);
	    pstmt.executeUpdate();
	    back += doSelect();
	}
	catch(Exception ex){
	    back += ex;
	    logger.error(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return back;
    }
	
    public     String doDelete(){
		
	String back = "", qq = "";
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
		
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    return back;
	}
	try{
	    qq = "delete from risk_files where id=?";
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,id);
	    pstmt.executeUpdate();
	    name =  "";old_name = ""; notes="";
	}
	catch(Exception ex){
	    back += ex+":"+qq;
	    logger.error(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return back;

    }
	
    //
    public     String doSelect(){
	String back = "";
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select id,"+
	    " risk_id,added_by_id,"+
	    " date_format(date,'%m/%d/%Y'), "+						
	    " name,old_name,notes "+
	    " from risk_files where id=?";		
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    return back;
	}
	try{
	    if(debug){
		logger.debug(qq);
	    }				
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1,id);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		setRisk_id(rs.getString(2));
		setAdded_by_id(rs.getString(3));
		setDate(rs.getString(4));
		setName(rs.getString(5));								
		setOldName(rs.getString(6));
		setNotes(rs.getString(7));
	    }
	    else{
		return "Record "+id+" Not found";
	    }
	}
	catch(Exception ex){
	    back += ex+":"+qq;
	    logger.error(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);			
	}
	return back;
    }	

}























































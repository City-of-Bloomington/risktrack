package risks.models;

import java.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.utils.*;

public class Related{

    String id="", id2="", type="",type2="";
    static Logger logger = LogManager.getLogger(Related.class);	
    boolean debug = false;
    public Related(String _id, 
		   String _id2,
		   String _type,
		   String _type2,
		   boolean deb
		   ){

	setId(_id);
	setId2(_id2);
	setType(_type);
	setType2(_type2);
	debug = deb;
    }
    public Related(String _id, 
		   String _id2,
		   String _type,
		   boolean deb
		   ){
	setId(_id);
	setId2(_id2);
	setType(_type);
	debug = deb;
    }
    public Related(String _id, 
		   String _id2,
		   boolean deb
		   ){
	setId(_id);
	setId2(_id2);
	debug = deb;
    }
    public Related(String _id, 
		   boolean deb
		   ){
	setId(_id);
	debug = deb;
    }
	
    public Related(boolean deb){
	debug = deb;
    }
    //
    // getters
    //
    public String getId(){
	return id;
    }
    public String getId2(){
	return id2;
    }
    public String getType(){
	return type;
    }
    public String getType2(){
	return type2;
    }
    public String toString(){
	return id;
    }
    //
    // setters
    //
    public void setId (String val){
	if(val != null)
	    id = val;
    }
    public void setId2 (String val){
	if(val != null)
	    id2 = val;
    }
    public void setType (String val){
	if(val != null)
	    type = val;
    }
    public void setType2 (String val){
	if(val != null)
	    type2 = val;
    }
    public String doSelect(){
		
	String back = "";
	Connection con = null;
	PreparedStatement stmt = null;	
	ResultSet rs = null;
		
	if(id.equals("") && id2.equals("")){
	    back = "id or id2 not yet set ";
	    return back;
	}
	String qq = "select type,type2 from "+
	    "legalRelated where id =? and id2=?";
	if(debug)
	    logger.debug(qq);
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB ";
	    logger.error(back);
	    return back;
	}											
	try{
	    stmt = con.prepareStatement(qq);
	    stmt.setString(1, id);
	    stmt.setString(2, id2);
	    rs = stmt.executeQuery();
	    if (rs.next()) {
		String str = rs.getString(1);
		if(str != null) type = str;
		str = rs.getString(2);
		if(str != null) type2 = str;
	    }
	    else{
		back = "No match found";
	    }
	}catch(Exception ex){
	    logger.error(ex+":"+qq);
	    back += ex+":"+qq;
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	return back;
    }
    public String doSave(){
	String back = "", qq = "";
	Connection con = null;
	PreparedStatement stmt = null;	
	ResultSet rs = null;		
	if(id.equals("") && id2.equals("") && !type.equals("")){
	    back = "id or id2 not yet set ";
	    return back;
	}
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB ";
	    logger.error(back);
	    return back;
	}								
	if(foundRelatedType()){
	    qq = " insert into legalRelated values(?,?,?,?)";
	    if(debug)
		logger.debug(qq);						
	    try{
		stmt = con.prepareStatement(qq);
		stmt.setString(1, id);
		stmt.setString(2, id2);
		stmt.setString(3, type);
		stmt.setString(4, type2);
		stmt.executeUpdate();
	    }catch(Exception ex){
		logger.error(ex+":"+qq);
		back += ex;
	    }
	    finally{
		Helper.databaseDisconnect(con, stmt, rs);
	    }
	}
	else{
	    back += " id "+id2+" does not exist ";
	}
	return back;
    }
    //
    public String doSave(Statement stmt, ResultSet rs){
	return doSave();
    }
    //
    public String doDelete(){
		
	String back = "", qq="";
	Connection con = null;
	PreparedStatement stmt = null;	
	ResultSet rs = null;		
	if(id.equals("") && id2.equals("")){
	    back = "id or id2 not yet set ";
	    return back;
	}
	if(!id.equals("") && !id2.equals("")){
	    qq = "delete from "+
		"legalRelated where (id =? and id2=?)"+
		"or (id=? and id2=?)";
	}
	else {
	    qq = "delete from "+
		"legalRelated where id = ? or id2=?";
	}
	if(debug)
	    logger.debug(qq);
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB ";
	    logger.error(back);
	    return back;
	}									
	try{
	    stmt = con.prepareStatement(qq);
	    if(!id.equals("") && !id2.equals("")){
		stmt.setString(1, id);
		stmt.setString(2, id2);
		stmt.setString(3, id2);
		stmt.setString(4, id);
	    }
	    else{
		stmt.setString(1, id);
		stmt.setString(2, id);
	    }
	    stmt.executeUpdate();
	}catch(Exception ex){
	    logger.error(ex+":"+qq);
	    back += ex;
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	return back;
    }
    //
    // we need this to make sure that the user picked the right type
    //
    private boolean foundRelatedType(){
		
	boolean found = false;
	Connection con = null;
	PreparedStatement stmt = null;	
	ResultSet rs = null;
	String back = "";
	String qq = " select 'Tort Claim' from tortClaims where id=?"+
	    " union select 'Recovery Action' from vslegals where id=?"+
	    " union select 'Worker Comp' from workerComps where id=?"+
	    " union select 'Internal Accident' from riskSafety where id=?"+
	    " union select 'Natural Disaster Accident' from disasters where id=?"+
	    " union select 'Misc Accident' from misc_accidents where id=?";
	if(debug)
	    logger.debug(qq);
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB ";
	    logger.error(back);
	    return false;
	}						
	try{
	    stmt = con.prepareStatement(qq);
	    stmt.setString(1, id2);
	    stmt.setString(2, id2);
	    stmt.setString(3, id2);
	    stmt.setString(4, id2);
	    stmt.setString(5, id2);
	    stmt.setString(6, id2);						
	    rs = stmt.executeQuery();
	    if (rs.next()) {
		String str = rs.getString(1);
		if(str != null){
		    type2 = str;
		    found = true;
		}
	    }
	}catch(Exception ex){
	    logger.error(ex+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	return found;
    }
    public String createLinkForType2(String url){
	String str = "";
	if(type2.startsWith("Tort")){
	    str = "<a href=\""+url+"TortClaimServ?id="+id2+"\">"+id2+"</a>";
	}
	else if(type2.startsWith("Recovery")){
	    str = "<a href=\""+url+"LegalServ?id="+id2+"\">"+id2+"</a>";
	}
	else if(type2.startsWith("Internal")){
	    str = "<a href=\""+url+"SafetyServ?id="+id2+"\">"+id2+"</a>";
	}
	else if(type2.startsWith("Worker")){
	    str = "<a href=\""+url+"WorkCompServ?id="+id2+"\">"+id2+"</a>";
	}
	else if(type2.startsWith("Natural")){
	    str = "<a href=\""+url+"DisasterServ?id="+id2+"\">"+id2+"</a>";
	}
	else if(type2.startsWith("Misc")){
	    str = "<a href=\""+url+"MiscAccidentServ?id="+id2+"\">"+id2+"</a>";
	}				
	else{
	    System.err.println("Unknown type: "+type2);
	}
	return str;
    }
	
}

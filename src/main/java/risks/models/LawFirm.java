package risks.models;

import java.util.*;
import java.sql.*;
import java.io.*;
import javax.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.utils.Helper;

public class LawFirm{

    String name="",address="",contact="", phones = "", id="";
	
    static Logger logger = LogManager.getLogger(LawFirm.class);
    boolean debug = false;
    public LawFirm(boolean deb){
	
	debug = deb;
	//
    }
    public LawFirm(boolean deb, String val){
	
	debug = deb;
	setId(val);
	//
    }
    public LawFirm(boolean deb,
		   String val,
		   String val2,
		   String val3,
		   String val4,
		   String val5
		   ){
	
	debug = deb;
	setId(val);
	setName(val2);
	setAddress(val3);
	setContact(val4);
	setPhones(val5);
	//
    }		
    public void setId(String val){
	if(val != null)				
	    id = val;
    }
    public     void setName(String val){
	if(val != null)
	    name = val;
    }
    public     void setAddress(String val){
	if(val != null)
	    address = val;
    }
    public     void setContact(String val){
	if(val != null)
	    contact = val;
    }
    public     void setPhones(String val){
	if(val != null)
	    phones = val;
    }		
    //
    // getters
    //
    public     String  getId(){
	return id;
    }
    public     String  getName(){
	return name;
    }
    public     String  getContact(){
	return contact;
    }
    public     String  getPhones(){
	return phones;
    }
    public     String  getAddress(){
	return address;
    }
    public String toString(){
	return name;
    }
    public     String doSave(){
	//
	Connection con = null;
	PreparedStatement pstmt = null, pstmt2=null;
	ResultSet rs = null;
	String qq =  "", back="";				
	if(name.equals("")){
	    back = "name is required ";
	    return back;
	}
	qq = "insert into law_firms values (0,?,?,?,?)";
	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    return "Could not connect to DB ";
	}				
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, name);
	    if(address.equals(""))
		pstmt.setNull(2, Types.VARCHAR);
	    else
		pstmt.setString(2, address);
	    if(contact.equals(""))
		pstmt.setNull(3, Types.VARCHAR);
	    else
		pstmt.setString(3, contact);
	    if(phones.equals(""))
		pstmt.setNull(4, Types.VARCHAR);
	    else
		pstmt.setString(4, phones);
	    pstmt.executeUpdate();
						
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
	    logger.error(ex+":"+qq);
	    back = ex.toString()+":"+qq;
	}
	finally{
	    Helper.databaseDisconnect(con, rs, pstmt, pstmt2);
	}
	return back;
    }
    public     String doUpdate(){

	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;			
	String str="", back="";
	String qq = "update law_firms set name=?,address=?,contact=?,phones=?";

	qq += " where id=?";
	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    return "Could not connect to DB ";
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, name);
	    if(address.equals(""))
		pstmt.setNull(2, Types.VARCHAR);
	    else
		pstmt.setString(2, address);
	    if(contact.equals(""))
		pstmt.setNull(3, Types.VARCHAR);
	    else
		pstmt.setString(3, contact);
	    if(phones.equals(""))
		pstmt.setNull(4, Types.VARCHAR);
	    else
		pstmt.setString(4, phones);
	    pstmt.setString(5, id);
	    pstmt.executeUpdate();
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    back = ex+":"+qq;
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return back; // success
    }
    public     String doSelect(){
	//
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;			
	String back = "";
	String qq = " select id,name,address,contact,phones "+
	    " from law_firms where id=?";// +id;
	String str="";
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
		setName(rs.getString(2));
		setAddress(rs.getString(3));
		setContact(rs.getString(4));
		setPhones(rs.getString(5));
	    }
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    back = ex.toString()+":"+qq;
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	return back;
    }
    //	
    public     String doDelete(){
	//
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;			
	String back = "";
	String qq = "delete from law_firms where id=?";// +id;
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
	    stmt.executeUpdate();
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    back = ex.toString()+":"+qq;
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	return back;
    }

}
























































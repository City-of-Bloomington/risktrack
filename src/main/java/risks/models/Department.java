package risks.models;
/**
 * @copyright Copyright (C) 2014-2022 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.sql.*;
import java.io.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.utils.Helper;

public class Department{

    boolean debug = false;
    String id="", // dept
	name="", division="", phone="";

    String [] deptIdArr = null, deptArr = null;
    static Logger logger = LogManager.getLogger(Department.class);
	
    public Department(boolean deb){
	//
	// initialize
	//
	debug = deb;

    }
    public Department(boolean deb, String val){
	//
	// initialize
	//
	debug = deb;
	setId(val);
    }

    public Department(boolean deb, String _id, String _name,
		      String _division, String _phone){

	debug = deb;
	setId(_id);
	setName(_name);
	setDivision(_division);
	setPhone(_phone);
		
    }	
    //
    // getters
    //
    public String getId(){
	return id;
    }
    public String getName(){
	return name;
    }
    public String getDivision(){
	return division;
    }
    public String getPhone(){
	return phone;
    }
    public String getInfo(){
	String ret = name;
	if(!division.equals("")) ret += ", "+division;	
	return ret;
    }
    public String [] getDeptArr(){
	return deptArr;
    }
    public String [] getDeptIdArr(){
	return deptIdArr;
    }
    public String toString(){
	return getInfo();
    }
    //
    // setters
    //
    public void setId (String val){
	if(val != null)
	    id = val;
    }
    public void setName (String val){
	if(val != null)
	    name = val;
    }
    public void setDivision (String val){
	if(val != null)
	    division = val;
    }
    public void setPhone (String val){
	if(val != null)
	    phone = val;
    }
    public boolean isEmpty(){
	return name.equals("") && id.equals("");
    }
    public boolean hasId(){
	return !id.equals("");
    }
    public boolean equals(Object o) {
	if (o instanceof Department) {
	    Department c = (Department) o;
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
		
    public  		String doSave(){
		
	if(id.equals("")){
	    if(!name.equals("")){
		findOrAddDept();
	    }
	}
	else{
	    return doUpdate();
	}
	return "";
    }
    public  	String doUpdate(){
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;			
	String back = "";
	String qq = "update departments set name=?, division=?,phone=? where id=? "; 
	if(debug){
	    logger.debug(qq);
	}
	if(name.equals("")){
	    back = "Department name is required ";
	    return back;
	}
	con = Helper.getConnection();
	if(con == null){
	    return "Could not connect to DB ";
	}
	try{
	    stmt = con.prepareStatement(qq);
	    stmt.setString(1, name);
	    if(division.equals(""))
		stmt.setNull(2, Types.VARCHAR);
	    else
		stmt.setString(2, division);
	    if(phone.equals(""))
		stmt.setNull(3, Types.VARCHAR);
	    else
		stmt.setString(3, phone);
	    stmt.setString(4, id);// dept
	    stmt.executeUpdate();
	}
	catch (Exception ex) {
	    back =  ex+":"+qq;						
	    logger.error(back);
						
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	return back;
    }
    //
    // check if this dept match any in the DB (database)
    //

    public boolean findDeptByName(){

	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;			
	boolean success = true;
	String qq = " select id,division,phone from departments "+
	    "where name like ? ";
	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    return false;
	}				
	try{
	    stmt = con.prepareStatement(qq);
	    stmt.setString(1, name);
	    rs = stmt.executeQuery();
	    if(rs.next()){
		id = rs.getString(1);
		String str = rs.getString(2);
		if(str != null) division = str;
		str = rs.getString(3);
		if(str != null) phone = str;
	    }
	}
	catch (Exception ex) {
	    success = false;
	    logger.error(ex+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	return success;
    }
    public boolean findDept(){
	return findDeptByName();
    }
    //
    // add this as a new dept, if not in the system already
    //
    public String findOrAddDept(){

	boolean success = false;
	String qq = "";
	Connection con = null;
	PreparedStatement stmt = null, stmt2=null;
	ResultSet rs = null;
	String back = "";
	success = findDeptByName();
	if(success){
	    return id;
	}
	else{
	    if(name.equals("")){
		back = "Department name is required ";
		return back;
	    }
	    qq = "insert into departments values(0,?,?,?)";
	    if(debug){
		logger.debug(qq);
	    }
	    con = Helper.getConnection();
	    if(con == null){
		logger.error("Could not connect to DB ");
	    }
	    try{
		stmt = con.prepareStatement(qq);
		stmt.setString(1, name);
		if(division.equals(""))
		    stmt.setNull(2, Types.VARCHAR);
		else
		    stmt.setString(2, division);
		if(phone.equals(""))
		    stmt.setNull(3, Types.VARCHAR);
		else
		    stmt.setString(3, phone);
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
	    catch (Exception ex) {
		logger.error(ex+":"+qq);
	    }
	    finally{
		Helper.databaseDisconnect(con, rs, stmt, stmt2);			
	    }
	}
	return id;
    }
    //
    public String doSelect(){

	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;			
	String msg = "";
	String qq = " select name,division,phone from departments "+
	    "where id = ? ";
	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    logger.error("Could not connect to DB ");
	}				
	try{
	    stmt = con.prepareStatement(qq);
	    stmt.setString(1, id);
	    rs = stmt.executeQuery();
	    if(rs.next()){
		name = rs.getString(1);
		String str = rs.getString(2);
		if(str != null) division = str;
		str = rs.getString(3);
		if(str != null) phone = str;
	    }
	}
	catch (Exception ex) {
	    logger.error(ex+":"+qq);
	    msg += ex+":"+qq;
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	return msg;
    }

}

package risks.models;
/**
 * @copyright Copyright (C) 2014-2022 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.*;
import java.sql.*;
import javax.sql.*;
import java.io.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.utils.Helper;
/**
 *
 */

public class Auto{

    boolean debug;
    
    String id="", risk_id="",
	autoMake ="", autoModel="", autoYear="",
	autoPlate="", vin= "", autoNum="";
    String owner = "City"; // Citizen
    static Logger logger = LogManager.getLogger(Auto.class);

    String errors = "";
    //
    // basic constructor
    //
    public Auto(boolean deb){

	debug = deb;
    }

    public Auto(boolean deb, String val){

	debug = deb;
	setId(val);
		
    }
    public Auto(boolean deb,
		String val,
		String val2,
		String val3,
		String val4,
		String val5,
		String val6,
		String val7,
		String val8,
		String val9
		){
	debug = deb;
	setId(val);
	setRisk_id(val2);
	setVin(val3);
	setAutoMake(val4);
	setAutoModel(val5);
	setAutoYear(val6);				
	setAutoPlate(val7);
	setAutoNum(val8);
	setOwner(val9);
    }	
    //
    // setters
    //
    public      void setRisk_id(String val){
	if(val != null)
	    risk_id = val;
    }
    public      void setId(String val){
	if(val != null)
	    id = val;
    }
    public      void setVin(String val){
	if(val != null)
	    vin = val; 
    }
    public      void setAutoPlate(String val){
	if(val != null)
	    autoPlate = val; 
    }
    public      void setAutoNum(String val){
	if(val != null)
	    autoNum = val; 
    }
    public      void setAutoMake(String val){
	if(val != null)
	    autoMake = val; 
    }
    public      void setAutoModel(String val){
	if(val != null)
	    autoModel = val; 
    }
    public      void setAutoYear(String val){
	if(val != null)
	    autoYear = val; 
    }
    public  		void setOwner(String val){
	if(val != null && !val.isEmpty())
	    owner = val;
    }
    public      void setDebug(boolean val){
	debug = val;
    }
    //
    // getters
    //
    public      String  getRisk_id(){
	return risk_id;
    }
    public      String  getId(){
	return id;
    }
    public      String  getVin(){
	return vin;
    }
    public      String  getOwner(){
	return owner;
    }		
    public      String  getAutoPlate(){
	return autoPlate;
    }
    public      String  getAutoNum(){
	return autoNum;
    }
    public      String  getAutoMake(){
	return autoMake;
    }

    public      String  getAutoModel(){
	return autoModel;
    }
    public      String  getAutoYear(){
	return autoYear;
    }
    public      String getAutoInfo(){
	return (vin+" "+autoPlate+" "+autoMake+" "+autoModel+" "+
		autoYear).trim();
    }
    public  		String getErrors(){
	return errors;
    }
    public  		boolean isEmpty(){
	return vin.equals("") &&
	    autoPlate.equals("") &&
	    autoNum.equals("");
    }
    public  	boolean isCityVehicle(){
	return owner.equals("City");
    }

    public String doSave(){

	String msg = "";
	String str = "", qq="";
	if(id.equals("")){
	    if(!isEmpty()){
		msg = doInsert();
	    }
	}
	else{
	    if(isEmpty()){
		msg = doDelete();
	    }
	    else{
		msg = doUpdate();
	    }
	}
	return msg;
    }
    //
    // save a new record in the database
    // return "" or any exception thrown by DB
    //
    public      String doInsert(){

	Connection con = null;
	PreparedStatement stmt = null, stmt2=null;	
	ResultSet rs = null;

	String qq = "", back = "";
	qq = "insert into risk_autos values (0,?,?,?,?, ?,?,?,?)"; //auto
	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB ";
	    logger.error(back);
	    return back;
	}												
	try{
	    stmt = con.prepareStatement(qq);
	    stmt.setString(1, risk_id);
	    if(vin.equals("")){
		stmt.setNull(2, Types.VARCHAR);
	    }
	    else{
		stmt.setString(2, vin);
	    }
	    if(autoPlate.equals("")){
		stmt.setNull(3, Types.VARCHAR);
	    }
	    else {
		stmt.setString(3, autoPlate);
	    }
	    if(autoMake.equals("")){
		stmt.setNull(4, Types.VARCHAR);
	    }
	    else {
		stmt.setString(4, autoMake);
	    }
	    if(autoModel.equals("")){
		stmt.setNull(5, Types.VARCHAR);
	    }
	    else {
		stmt.setString(5, autoModel);
	    }
	    if(autoYear.equals("")){
		stmt.setNull(6, Types.VARCHAR);
	    }
	    else {
		stmt.setString(6, autoYear);
	    }
	    if(autoNum.equals("")){
		stmt.setNull(7, Types.VARCHAR);
	    }
	    else {
		stmt.setString(7,autoNum);
	    }
	    stmt.setString(8, owner); // never null
	    stmt.executeUpdate();
	    //
	    // get the id of the new record
	    //
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
	    back = ex.toString()+":"+qq;
	}
	finally{
	    Helper.databaseDisconnect(con, rs, stmt, stmt2);			
	}
	return back;
    }
    //
    public      String doUpdate(){

	Connection con = null;
	PreparedStatement stmt = null;	
	ResultSet rs = null;

	String str="", back="";
	String qq = "update risk_autos set vin=?,autoPlate=?,autoMake=?, autoModel=?,autoYear=?, autoNum=?, owner=? where id=?";
	//
	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB ";
	    logger.error(back);
	    return back;
	}				
	try{
	    stmt = con.prepareStatement(qq);
	    if(vin.equals("")){
		stmt.setNull(1, Types.VARCHAR);
	    }
	    else{
		stmt.setString(1, vin);
	    }
	    if(autoPlate.equals("")){
		stmt.setNull(2, Types.VARCHAR);
	    }
	    else {
		stmt.setString(2, autoPlate);
	    }
	    if(autoMake.equals("")){
		stmt.setNull(3, Types.VARCHAR);
	    }
	    else {
		stmt.setString(3, autoMake);
	    }
	    if(autoModel.equals("")){
		stmt.setNull(4, Types.VARCHAR);
	    }
	    else {
		stmt.setString(4, autoModel);
	    }
	    if(autoYear.equals("")){
		stmt.setNull(5, Types.VARCHAR);
	    }
	    else {
		stmt.setString(5, autoYear);
	    }
	    if(autoNum.equals("")){
		stmt.setNull(6, Types.VARCHAR);
	    }
	    else {
		stmt.setString(6,autoNum);
	    }
	    stmt.setString(7,owner);
	    stmt.setString(8, id);
	    stmt.executeUpdate();
	}
	catch(Exception ex){
	    back =  ex+": "+qq;						
	    logger.error(back);
	}
	finally{		
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	return back; // success
    }
    //
    public      String doDelete(){
	//
	// System.err.println("delete record");
	//
	String back = "";
	Connection con = null;
	PreparedStatement stmt = null;	
	ResultSet rs = null;
		
	String qq = "delete from  risk_autos where id=? ";
	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB ";
	    logger.error(back);
	    return back;
	}							
	try{
	    stmt = con.prepareStatement(qq);
	    stmt.setString(1, id);
	    stmt.executeUpdate();
	}
	catch(Exception ex){
	    back = ex.toString()+":"+qq;						
	    logger.error(back);
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	return back;
    }
    //
    public      String doDeleteAllFor(String id){
	//
	Connection con = null;
	PreparedStatement stmt = null;	
	ResultSet rs = null;
		
	String qq = "delete from  risk_autos where risk_id=?";
	String back = "";
	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB ";
	    logger.error(back);
	    return back;
	}							
	try{
	    stmt = con.prepareStatement(qq);
	    stmt.setString(1, risk_id);
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
    //
    public      String doSelect(){
	//
	Connection con = null;
	PreparedStatement stmt = null;	
	ResultSet rs = null;
		
	String qq = "select risk_id,"+
	    "vin,autoMake,autoModel,autoYear,"+
	    " autoPlate,autoNum,owner "+
	    " from risk_autos where id=?";
	String str="", back="";
	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB ";
	    logger.error(back);
	    return back;
	}				
	try{
	    stmt = con.prepareStatement(qq);
	    stmt.setString(1, id);
	    rs = stmt.executeQuery();
	    if(rs.next()){
								
		str = rs.getString(1);
		if(str != null) risk_id = str;
		str = rs.getString(2);								
		if(str != null) vin = str;
		str = rs.getString(3);
		if(str != null) autoPlate = str;
		str = rs.getString(4);  								
		if(str != null) autoMake = str;
		str = rs.getString(5);
		if(str != null) autoModel = str;
		str = rs.getString(6);
		if(str != null) autoYear = str; 
		str = rs.getString(7);
		if(str != null) autoNum = str;
		str = rs.getString(8);
		if(str != null) owner = str;								
	    }
	    else{
		return "Record "+id+" Not Found";
	    }
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    back =  ex.toString()+":"+qq;
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	return back;
    }
    
}























































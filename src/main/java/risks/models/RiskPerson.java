package risks.models;

import java.util.*;
import java.sql.*;
import java.io.*;
import java.text.SimpleDateFormat;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.utils.Helper;

public class RiskPerson{

    boolean debug;
    static Logger logger = LogManager.getLogger(RiskPerson.class);
    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");	
    String fname = "", lname="",mi="",
	streetNum="", streetDir="", id="", nameTitle="",
	streetName="", streetType="", sudType="", sudNum="",
	city="", state="", zip="", addrUpdate="", email="",
	dob="", ssn="", phoneh="", phonec="", phonew="",
	juvenile="", // a flag for under age 
	contact="";

    // 
    // basic constructor
    //
    public RiskPerson(boolean deb){

	debug=deb;

    }
    public RiskPerson(boolean deb, String val){

	debug=deb;
	if(val != null)
	    id = val;

    }
    public RiskPerson(boolean deb,
		      String val,
		      String val2,
		      String val3,
		      String val4,
		      String val5,
		      String val6,
		      String val7,
		      String val8,
		      String val9,
		      String val10,
		      String val11,
		      String val12,
		      String val13,
		      String val14,
		      String val15,
		      String val16,
		      String val17,
		      String val18,
		      String val19,
		      String val20,
		      String val21,
		      String val22,
		      String val23){
	setVals(val, val2, val3, val4, val5, val6, val7, val8, val9, val10,
		val11, val12, val13, val14, val15, val16, val17, val18, val19, val20,
		val21, val22, val23);
	debug=deb;
    }
    void setVals(
		 String val,
		 String val2,
		 String val3,
		 String val4,
		 String val5,
		 String val6,
		 String val7,
		 String val8,
		 String val9,
		 String val10,
		 String val11,
		 String val12,
		 String val13,
		 String val14,
		 String val15,
		 String val16,
		 String val17,
		 String val18,
		 String val19,
		 String val20,
		 String val21,
		 String val22,
		 String val23){
	setId(val);
	setLname(val2);
	setFname(val3);
	setStreetNum(val4);
	setStreetDir(val5);
	setStreetName(val6);
	setStreetType(val7);
	setSudType(val8);
	setSudNum(val9);
	setCity(val10);
	setState(val11);
	setZip(val12);
	setPhoneh(val13);
	setPhonew(val14);
	setPhonec(val15);
	setDob(val16);
	setSsn(val17);
	setAddrUpdate(val18);
	setMi(val19);
	setEmail(val20);
	setNameTitle(val21);
	setContact(val22);
	setJuvenile(val23);
    }
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setFname(String val){
	if(val != null)
	    fname = val;
    }
    public void setLname(String val){
	if(val != null)
	    lname = val;
    }
    public void setStreetNum(String val){
	if(val != null)
	    streetNum = val;
    }
    public void setStreetDir(String val){
	if(val != null)
	    streetDir = val;
    }
    public void setStreetName(String val){
	if(val != null)
	    streetName = val;
    }
    public void setStreetType(String val){
	if(val != null)
	    streetType = val;
    }
    public void setSudType(String val){
	if(val != null)
	    sudType = val;
    }
    public void setSudNum(String val){
	if(val != null)
	    sudNum = val;
    }
    public void setCity(String val){
	if(val != null)
	    city = val;
    }
    public void setState(String val){
	if(val != null)
	    state = val.toUpperCase();
    }
    public void setZip(String val){
	if(val != null)
	    zip = val;
    }
    public void setNameTitle(String val){
	if(val != null)
	    nameTitle = val;
    }
    public void setAddrUpdate(String val){
	if(val != null)
	    addrUpdate = val; // date
    }
    public void setDebug(boolean val){
	debug = val;
    }
    public void setDob(String val){
	if(val != null)
	    dob = val;
    }	
    public void setContact(String val){
	if(val != null)
	    contact = val;
    }
    public void setSsn(String val){
	if(val != null)
	    ssn = val;
    }	
    public void setMi(String val){
	if(val != null)
	    mi = val;
    }	
    public void setPhoneh(String val){
	if(val != null)
	    phoneh = val;
    }
    public void setPhonec(String val){
	if(val != null)
	    phonec = val;
    }
    public void setPhonew(String val){
	if(val != null)
	    phonew = val;
    }
    public void setEmail(String val){
	if(val != null)
	    email = val;
    }
    public void setJuvenile(String val){
	if(val != null)
	    juvenile = val;
    }	
    //
    // getters
    //
    public String  getId(){
	return id;
    }
    public String  getFname(){
	return Helper.initCap(fname);
    }
    public String  getLname(){
	return Helper.initCap(lname);
    }
    public String  getStreetNum(){
	return streetNum;
    }
    public String  getStreetDir(){
	return streetDir;
    }
    public String  getStreetName(){
	if(streetName.startsWith("P.O")) return streetName;
	else return Helper.initCap(streetName);
    }
    public String  getStreetType(){
	return streetType;
    }
    public String  getSudType(){
	return sudType;
    }
    public String  getSudNum(){
	return sudNum;
    }
    public String  getCity(){
	return Helper.initCap(city);
    }
    public String  getState(){
	return state.toUpperCase();
    }
    public String  getZip(){
	return zip;
    }
    public String  getNameTitle(){
	return nameTitle;
    }
    public String  getAddrUpdate(){
	return addrUpdate;
    }
    public String  getDob(){
	return dob;
    }
    public String  getPhoneh(){
	return phoneh;
    }
    public String  getPhonec(){
	return phonec;
    }
    public String  getPhonew(){
	return phonew;
    }
    public String  getSsn(){
	return ssn;
    }
    public String  getMi(){
	return mi;
    }
    public String  getContact(){
	return contact;
    }
    public String  getEmail(){
	return email;
    }
    public String  getJuvenile(){
	return juvenile;
    }
    public boolean isJuvenile(){
	return !juvenile.equals("");
    }

    public String  getAddress(){
	return (streetNum+" "+streetDir+" "+
		getStreetName()+
		" "+Helper.initCap(streetType)+
		" "+Helper.initCap(sudType)+
		" "+sudNum).trim();
    }
    public String getFullName(){
	String str = "";
	if(!fname.equals("")) str += Helper.initCap(fname);
	if(!mi.equals("")) str += " "+mi;
	if(!lname.equals("")) str += " "+lname;
	return str.trim();
    }
    public String getPhones(){
	String str = "";
	if(!phoneh.equals("")){
	    str += phoneh;
	}
	if(phonew.equals("")){
	    if(!str.equals("")) str += ", ";
	    str += phonew;
	}
	if(phonec.equals("")){
	    if(!str.equals("")) str += ", ";
	    str += phonec;
	}				
	return str;
    }
    public String getCityStateZip(){
	return (Helper.initCap(city)+", "+state+" "+zip).trim();
    }
    public String toString(){
	return id;
    }
    public boolean equals(Object o) {
	if (o instanceof RiskPerson) {
	    RiskPerson c = (RiskPerson) o;
	    if ( this.id.equals(c.getId())) 
		return true;
	}
	return false;
    }
    public int hashCode(){
	int seed = 31;
	if(!id.equals("")){
	    try{
		seed += Integer.parseInt(id)*37;
	    }catch(Exception ex){
		// we ignore
	    }
	}
	return seed;
    }				
    //
    // save a new record in the database
    // return "" or any exception thrown by DB
    //
    public String doSave(){
	//
	String back = "";
	Connection con = null;
	PreparedStatement stmt = null, stmt2=null;
	ResultSet rs = null;
		
	String qq = "insert into people values (0,?,?,?,?, ?,?,?,?,?,"+
	    "?,?,?,?,?, ?,?,?,?,?,"+
	    "?,?,?)";
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
	    back = setParams(stmt);
	    if(back.equals("")){
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
	}
	catch(Exception ex){
	    back = ex+":"+qq;
	    logger.error(back);
	}
	finally{
	    Helper.databaseDisconnect(con, rs, stmt, stmt2);
	}
	return back;
    }
    //
    String setParams(PreparedStatement stmt){
	String back = "";
	int jj=1;
	try{
	    if(lname.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++, Helper.initCap(lname));
	    }
	    if(fname.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++,Helper.initCap(fname));
	    }
	    if(streetNum.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++,streetNum);
	    }
	    if(streetDir.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++,streetDir);
	    }
	    if(streetName.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		if(!streetName.startsWith("P.O")){
		    streetName = Helper.initCap(streetName);
		}
		stmt.setString(jj++,streetName);
	    }
	    if(streetType.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++,streetType);
	    }
	    if(sudType.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++,sudType);
	    }
	    if(sudNum.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++,sudNum);
	    }
	    if(city.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++,Helper.initCap(city));
	    }
	    if(state.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++,state);
	    }
	    if(zip.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++,zip);
	    }
	    if(phoneh.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++,phoneh);
	    }
	    if(phonew.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++,phonew);
	    }
	    if(phonec.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++,phonec);
	    }
	    if(dob.equals("")){
		stmt.setNull(jj++, Types.DATE);
	    }
	    else {
		java.util.Date date_tmp = df.parse(dob);
		stmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }
	    if(ssn.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++,ssn);
	    }
	    if(addrUpdate.equals("")){
		addrUpdate = Helper.getToday();
	    }
	    if(true){
		java.util.Date date_tmp = df.parse(addrUpdate);
		stmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }
	    if(mi.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++,mi);
	    }
	    if(email.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++,email);
	    }
	    if(nameTitle.equals(""))
		stmt.setNull(jj++, Types.INTEGER);
	    else
		stmt.setString(jj++,nameTitle); 
	    if(contact.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++,contact);
	    }
	    if(juvenile.equals("")){
		stmt.setNull(jj++, Types.CHAR);
	    }
	    else {
		stmt.setString(jj++, "y");
	    }
	}catch(Exception ex){
	    back += ex;
	}
	return back;
    }
    public String doUpdate(){

	String str="", back="";
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;		
	String qq = "";
	qq = "update people set "+
	    "lname=?,fname=?,streetNum=?,streetDir=?,streetName=?,"+
	    "streetType=?,sudType=?,sudNum=?,city=?,state=?,"+
	    "zip=?,phoneh=?,phonew=?,phonec=?,dob=?,"+
	    "ssn=?,addrUpdate=?,mi=?,email=?,nameTitle=?,"+
	    "contact=?,juvenile=? where id=? ";

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
	    back = setParams(stmt);
	    if(back.equals("")){
		stmt.setString(23, id);
		stmt.executeUpdate();
	    }
	}
	catch(Exception ex){
	    back = ex+":"+qq;
	    logger.error(ex+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	return back; 
    }
    //
    public String doDelete(){
	//
	// System.err.println("delete record");
	//
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;		
	String back = "";
	String qq = "delete from  people where id=?";
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
	    back = ex+":"+qq;
	    logger.error(ex+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);		
	}
	lname="";fname="";
	streetNum="";streetDir="";
	streetName="";streetType="";sudType="";sudNum="";
	city="Bloomington";
	state="IN";zip=""; nameTitle = "";
	dob="";ssn=""; mi=""; juvenile="";
	phonew="";phonec="";phoneh="";

	return back; 
    }
    public String addToRelatedClaim(String risk_id){
	//
	// System.err.println("delete record");
	//
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;		
	String back = "";
	String qq = " insert into claimPerson values(?,?)";
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
	    stmt.setString(2, risk_id);
	    stmt.executeUpdate();
	}
	catch(Exception ex){
	    back = ex+":"+qq;
	    logger.error(ex+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);		
	}
	return back; 
    }		
		
    public String doSelect(){
	//
	String back = "";
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;		
	lname="";fname="";
	streetNum="";streetDir="";
	streetName="";streetType="";sudType="";sudNum="";
	city="Bloomington";
	state="IN";zip=""; nameTitle = "";
	dob="";ssn=""; mi="";
	phonew="";phonec="";phoneh="";
	String qq = "select "+
	    "lname,fname,"+
	    "streetNum,streetDir,streetName,streetType,"+
	    "sudType,sudNum,"+
	    "city,state,zip,"+
	    "phonew,phoneh,phonec, "+
	    "date_format(dob,'%m/%d/%Y'),"+
	    "ssn, "+
	    "date_format(addrUpdate,'%m/%d/%Y'), "+
	    " mi, email, nameTitle, contact, juvenile"+
	    " from people where id=?";
	if(id.equals("")){
	    back = "person id not set ";
	    return back;
	}
	String str="";
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
		setVals(id,
			rs.getString(1),
			rs.getString(2),
			rs.getString(3),
			rs.getString(4),
			rs.getString(5),
			rs.getString(6),  
			rs.getString(7),
			rs.getString(8),
			rs.getString(9),
			rs.getString(10),
			rs.getString(11),
			rs.getString(12),
			rs.getString(13),
			rs.getString(14),
			rs.getString(15),
			rs.getString(16),
			rs.getString(17),
			rs.getString(18),
			rs.getString(19),
			rs.getString(20),
			rs.getString(21),
			rs.getString(22));
	    }
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    return ex.toString();
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	return back; 		
    }

}























































package risks.models;

import java.util.*;
import java.sql.*;
import java.io.*;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.utils.Helper;

public class WorkComp extends Risk{

    static Logger logger = LogManager.getLogger(WorkComp.class);
    public String status="", vsId="",tortId="",
	empid="",empName="",empPhone="",dept_id="", empTitle="",
	accidentDate="",//  date
	injuryType="",//  varchar(50),"+
	compensable="", // enum('','Yes','No','Disputed'),"+ 
	timeOffWork="", // varchar(30),"+ // days
	payTtd="",payPpi="", payMed="", // double,"+
	mmi="", //  char(1),"+             // max reached flag
	ableBackWork="", //  enum('','Yes','No','w/Restrictions'),"+ 
	disputeAmount="", // double,"+
	disputeReason="", //  varchar(50),"+
	disputeTypeTtd="", // char(1),"+
	disputeTypePpi="", // char(1),"+
	disputeTypeMed="", //  char(1) "+
	back2WorkDate="";
    //
    // basic constructor
    public WorkComp(boolean deb){

	debug = deb;

    }
    public WorkComp(boolean deb, String val){

	debug = deb;
	setId(val);

    }
    public WorkComp(boolean deb,
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
		    String val18
		    ){
	debug = deb;
	setVals(val,val2, val3, val4, val5, val6, val7, val8, val9, val10,
		val11,val12, val13, val14, val15, val16, val17, val18);

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
		 String val18
		 ){
	setId(val);
	setStatus(val2);
	setEmpPhone(val3);
	setAccidentDate(val4);
	setInjuryType(val5);
				
	setCompensable(val6);
	setTimeOffWork(val7);
	setPayTtd(val8);
	setPayPpi(val9);
	setPayMed(val10);

	setMmi(val11);
	setAbleBackWork(val12);
	setDisputeAmount(val13);
	setDisputeReason(val14);
	setDisputeTypeTtd(val15);
				
	setDisputeTypePpi(val16);
	setDisputeTypeMed(val17);
	setBack2WorkDate(val18);

    }
    //
    // setters
    //
    public void setVsId(String val){
	if(val != null)
	    vsId = val;
    }
    public void setTortId(String val){
	if(val != null)
	    tortId = val;
    }
    public void setStatus(String val){
	if(val != null)
	    status = val;
    }
    public void setDept_id(String val){
	if(val != null)
	    dept_id = val;
    }
    public void setEmpid(String val){
	if(val != null)
	    empid = val;
    }
    public void setEmpName(String val){
	if(val != null)
	    empName = val;
    }
    public void setEmpPhone(String val){
	if(val != null)
	    empPhone = val;
    }

    public void setAccidentDate(String val){
	if(val != null)
	    accidentDate = val;
    }
    public void setInjuryType(String val){
	if(val != null)
	    injuryType = val;
    }
    public void setCompensable(String val){
	if(val != null)
	    compensable = val;
    }
    public void setTimeOffWork(String val){
	if(val != null)
	    timeOffWork = val;
    }
    public void setPayTtd(String val){
	if(val != null)
	    payTtd = Helper.cleanNumber(val);
    }
    public void setPayPpi(String val){
	if(val != null)
	    payPpi = Helper.cleanNumber(val);
    }
    public void setPayMed(String val){
	if(val != null)
	    payMed = Helper.cleanNumber(val);
    }
    public void setMmi(String val){
	if(val != null)
	    mmi = val; 
    }
    public void setEmpTitle(String val){
	if(val != null)
	    empTitle = val; 
    }
    public void setAbleBackWork(String val){
	if(val != null)
	    ableBackWork = val; 
    }
    public void setDisputeAmount(String val){
	if(val != null)
	    disputeAmount = Helper.cleanNumber(val);
    }
    public void setDisputeReason(String val){
	if(val != null)
	    disputeReason = val;
    }
    public void setDisputeTypeTtd(String val){
	if(val != null)
	    disputeTypeTtd = val;
    }
    public void setDisputeTypePpi(String val){
	if(val != null)
	    disputeTypePpi = val;
    }
    public void setDisputeTypeMed(String val){
	if(val != null)
	    disputeTypeMed = val;
    }
    public void setBack2WorkDate(String val){
	if(val != null)
	    back2WorkDate = val;
    }
    //
    // getters
    //
    public String  getVsId(){
	return vsId;
    }
    public String  getTortId(){
	return tortId;
    }
    public String  getStatus(){
	return status;
    }
    public String  getEmpid(){
	return empid;
    }

    public String  getEmpName(){
	return empName;
    }
    public String  getEmpTitle(){
	return empTitle;
    }
    public String  getEmpPhone(){
	return empPhone;
    }
    public String  getDept_id(){
	return dept_id;
    }
    public String  getAccidentDate(){
	return accidentDate;
    }
    public String  getInjuryType(){
	return injuryType;
    }
    public String  getCompensable(){
	return compensable;
    }
    public String  getTimeOffWork(){
	return timeOffWork;
    }
    public String  getPayTtd(){
	return payTtd;
    }
    public String  getPayPpi(){
	return payPpi;
    }
    public String  getPayMed(){
	return payMed;
    }
    public String  getMmi(){
	return mmi;
    }
    public String  getAbleBackWork(){
	return ableBackWork;
    }
    public String  getDisputeAmount(){
	return disputeAmount;
    }
    public String  getDisputeReason(){
	return disputeReason;
    }
    public String  getDisputeTypeTtd(){
	return disputeTypeTtd;
    }
    public String  getDisputeTypePpi(){
	return disputeTypePpi;
    }
    public String  getDisputeTypeMed(){
	return disputeTypeMed;
    }
    public String getBack2WorkDate(){
	return back2WorkDate;
    }
		
    public String toString(){
	return id;
    }
    public boolean equals(Object o) {
	if (o instanceof WorkComp) {
	    WorkComp c = (WorkComp) o;
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

	Connection con = null;
	PreparedStatement stmt = null, stmt2=null, stmt3=null;
	ResultSet rs = null;
	String qq = "insert into risk_sequences values(0,'wcomp')";
	String msg = "";

	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    logger.error(msg);
	    return msg;
	}				
	try{
						
	    stmt = con.prepareStatement(qq);			
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
	    qq = "insert into workerComps values (?,?,?,?,?, ?,?,?,?,?,"+
		"?,?,?,?,?, ?,?,?)";
	    if(debug){
		logger.debug(qq);
	    }

	    stmt3 = con.prepareStatement(qq);
	    msg = setParams(stmt3, true);
	    if(msg.equals(""))
		stmt3.executeUpdate();
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    msg =  ex.toString()+":"+qq;
	}
	finally{
	    Helper.databaseDisconnect(con, rs, stmt, stmt2, stmt3);
	}
	return msg;
    }
    String setParams(PreparedStatement stmt, boolean forSave){
	String back="";
	int jj=1;
	try{
	    if(forSave)
		stmt.setString(jj++, id);
	    stmt.setString(jj++, status);
	    if(empPhone.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++, empPhone);
	    }
	    if(accidentDate.equals(""))
		stmt.setNull(jj++, Types.DATE);
	    else{
		java.util.Date date_tmp = df.parse(accidentDate);
		stmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }
	    if(injuryType.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++, injuryType);
	    }
	    if(compensable.equals(""))
		stmt.setNull(jj++, Types.INTEGER);
	    else
		stmt.setString(jj++, compensable);
	    if(timeOffWork.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++, timeOffWork);
	    }
	    if(payTtd.equals("")){
		stmt.setString(jj++,"0"); 								
	    }
	    else {
		stmt.setString(jj++, payTtd);
	    }
	    if(payPpi.equals("")){
		stmt.setString(jj++,"0"); 	
	    }
	    else {
		stmt.setString(jj++, payPpi);
	    }
	    if(payMed.equals("")){
		stmt.setString(jj++,"0"); 	// 10
	    }
	    else {
		stmt.setString(jj++,payMed);
	    }
	    if(mmi.equals("")){
		stmt.setNull(jj++, Types.CHAR);
	    }
	    else {
		stmt.setString(jj++, "y");
	    }
	    stmt.setString(jj++, ableBackWork); // ok ''
						
	    if(disputeAmount.equals("")){
		stmt.setString(jj++,"0"); 	
	    }
	    else {
		stmt.setString(jj++,disputeAmount);
	    }
	    if(disputeReason.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++, disputeReason);
	    }
	    if(disputeTypeTtd.equals("")){
		stmt.setNull(jj++, Types.CHAR);
	    }
	    else {
		stmt.setString(jj++, "y");
	    }
	    if(disputeTypePpi.equals("")){
		stmt.setNull(jj++, Types.CHAR);
	    }
	    else {
		stmt.setString(jj++, "y");
	    }
	    if(disputeTypeMed.equals("")){
		stmt.setNull(jj++, Types.CHAR);
	    }
	    else {
		stmt.setString(jj++, "y");
	    }
	    if(back2WorkDate.equals("")) // 18
		stmt.setNull(jj++, Types.DATE);
	    else{
		java.util.Date date_tmp = df.parse(back2WorkDate);
		stmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }
	}catch(Exception ex){
	    back += ex;
	}						
	return back;

    }
    public String doUpdate(){

	String str="", msg="";
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;		
	String qq = "";
	qq = "update workerComps set "+
	    "status=?,empPhone=?,accidentDate=?,injuryType=?,compensable=?,"+
	    "timeOffWork=?,payTtd=?,payPpi=?,payMed=?,mmi=?,"+
	    "ableBackWork=?,disputeAmount=?,disputeReason=?,disputeTypeTtd=?,"+
	    "disputeTypePpi=?,disputeTypeMed=?,back2WorkDate=? where id=?";
	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    logger.error(msg);
	    return msg;
	}				
	try{
	    stmt = con.prepareStatement(qq);
	    msg = setParams(stmt, false);
	    if(msg.equals("")){
		stmt.setString(18, id);
		stmt.executeUpdate();
	    }
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    msg = ex.toString()+":"+qq;
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	return msg; // success
    }
    //
    public String doDelete(){
	//
	// System.err.println("delete record");
	//
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;		
	String msg = "";
	String qq = "delete from  workerComps where id=?";
	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    logger.error(msg);
	    return msg;
	}				
	try{
	    stmt = con.prepareStatement(qq);
	    stmt.setString(1, id);
	    stmt.executeUpdate();
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    msg = ex.toString()+":"+qq;
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	return msg;
    }
    // 
    public String doSelect(){
	//
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;		
	String msg = "";
	String qq = "select "+
	    "status,"+
	    "empPhone,"+
	    "date_format(accidentDate,'%m/%d/%Y'),"+						
	    "injuryType,"+
	    "compensable,"+
	    "timeOffWork,"+
	    "payTtd,"+
	    "payPpi,"+
	    "payMed,"+
	    "mmi, "+
						
	    "ableBackWork,"+
	    "disputeAmount,"+
	    "disputeReason,"+
	    "disputeTypeTtd,"+
	    "disputeTypePpi,"+
	    "disputeTypeMed, "+						
	    "date_format(back2WorkDate,'%m/%d/%Y')"+

	    " from workerComps where id=?";// +id;
	String str="";
	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    logger.error(msg);
	    return msg;
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
			rs.getString(17));				
	    }
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    msg = ex.toString()+":"+qq;
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	return msg;
    }

}























































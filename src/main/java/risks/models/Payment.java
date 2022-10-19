package risks.models;

import java.util.*;
import java.sql.*;
import java.io.*;
import javax.sql.*;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.utils.*;

public class Payment{

    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");		
    static Logger logger = LogManager.getLogger(Payment.class);
		
    double amount = 0, balance = 0;
    String paidDate="",paidMethod="",checkNo="", clerk="";
    String paidBy="",id="", risk_id="", mccFlag="", receiptDate="";
    String type=""; // legal, safety, tort, workerComp (where we came from)
    boolean debug = false;

    List<Payment> payments = null;
    //
    // basic constructor
    public Payment(boolean deb){
	
	debug = deb;
	//
    }
    public Payment(boolean deb,
		   String val,
		   String val2,
		   String val3,
		   String val4,
		   String val5,
		   String val6,
		   String val7,
		   String val8,
		   String val9){
	debug = deb;
	setId(val);
	setRisk_id(val2);
	setPaidDate(val3);
	setAmount(val4);
	setPaidBy(val5);
	setPaidMethod(val6);
	setCheckNo(val7);
	setReceiptDate(val8);
	setClerk(val9);
    }		
    public void setAmount(String val){
	if(val != null && !val.equals("")){
	    try{
		amount = Double.valueOf(val).doubleValue();
	    }catch(Exception ex){
		System.err.println(ex);
	    }
	}
    }
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setRisk_id(String val){
	if(val != null)				
	    risk_id = val;
    }
    public void setType(String val){
	if(val != null)
	    type = val;
    }
    public void setPaidDate(String val){
	if(val != null)
	    paidDate = val;
    }
    public void setReceiptDate(String val){
	if(val != null)
	    receiptDate = val;
    }
    public void setPaidBy(String val){
	if(val != null)
	    paidBy = val;
    }
    public void setPaidMethod(String val){
	if(val != null)
	    paidMethod = val;
    }
    public void setCheckNo(String val){
	if(val != null)
	    checkNo = val;
    }
    public void setMccFlag(String val){
	if(val != null)
	    mccFlag = val;
    }
    public void setClerk(String val){
	if(val != null)
	    clerk = val;
    }
    //
    // getters
    //0
    public    String  getId(){
	return id;
    }
    public      String  getRisk_id(){
	return risk_id;
    }
    public      String  getType(){
	return type;
    }
    public      String  getPaidDate(){
	return paidDate;
    }
    public      String  getReceiptDate(){
	return receiptDate;
    }
    public      String  getPaidMethod(){
	return paidMethod;
    }
    public      String  getAmount(){
	return ""+amount;
    }
    public      String  getCheckNo(){
	return checkNo;
    }
    public      String  getPaidBy(){
	return paidBy;
    }
    public      String  getClerk(){
	return clerk;
    }
    public      String  getMccFlag(){
	return mccFlag;
    }
    public      List<Payment> getPayments(){
	return payments;
    }
    public      boolean hasPayments(){
	return payments != null && payments.size() > 0;
    }
    public String toString(){
	return id;
    }
    public boolean equals(Object o) {
	if (o instanceof Payment) {
	    Payment c = (Payment) o;
	    if ( this.id.equals(c.getId())) 
		return true;
	}
	return false;
    }
    public int hashCode(){
	int seed = 29;
	if(!id.equals("")){
	    try{
		seed += Integer.parseInt(id)*37;
	    }catch(Exception ex){
		// we ignore
	    }
	}
	return seed;
    }				
		
    public      String doSave(){
	//
	Connection con = null;
	PreparedStatement stmt = null, stmt2=null;
	ResultSet rs = null;	
		
	String qq =  "", back="";
	qq = "insert into payments values (0,?,?,?,?, ?,?,?,?)";
	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    return "Could not connect to DB ";
	}
	try{
	    stmt = con.prepareStatement(qq);
	    stmt.setString(1, risk_id);
	    if(paidDate.equals("")){
		paidDate = Helper.getToday();
	    }
	    java.util.Date date_tmp = df.parse(paidDate);
	    stmt.setDate(2, new java.sql.Date(date_tmp.getTime()));
	    stmt.setString(3, ""+amount);
	    if(paidBy.equals(""))
		stmt.setNull(4, Types.VARCHAR);
	    else
		stmt.setString(4, paidBy);
	    if(paidMethod.equals(""))
		stmt.setNull(5, Types.INTEGER);
	    else
		stmt.setString(5, paidMethod);
	    if(checkNo.equals(""))
		stmt.setNull(6, Types.VARCHAR);
	    else
		stmt.setString(6, checkNo);
	    if(receiptDate.equals(""))
		stmt.setNull(7, Types.DATE);
	    else{
		date_tmp = df.parse(receiptDate);
		stmt.setDate(7, new java.sql.Date(date_tmp.getTime()));
	    }
	    if(clerk.equals(""))
		stmt.setNull(8, Types.CHAR);
	    else
		stmt.setString(8, "y");
	    stmt.executeUpdate();
	    qq = "select LAST_INSERT_ID() ";
	    if(debug){
		logger.debug(qq);
	    }
	    stmt2 = con.prepareStatement(qq);
	    rs = stmt2.executeQuery(qq);
	    if(rs.next()){
		id = rs.getString(1);
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
    public      String doUpdate(){

	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;			
	String str="", back="";
	String qq = "update payments set paidDate=?,amount=?,paidBy=?,"+
	    "paidMethod=?,checkNo=?,receiptDate=?,clerk=? where id=? ";

	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    return "Could not connect to DB ";
	}
	try{
	    stmt = con.prepareStatement(qq);
	    if(paidDate.equals("")){
		paidDate = Helper.getToday();
	    }
	    java.util.Date date_tmp = df.parse(paidDate);
	    stmt.setDate(1, new java.sql.Date(date_tmp.getTime()));
	    stmt.setString(2, ""+amount);
	    if(paidBy.equals(""))
		stmt.setNull(3, Types.VARCHAR);
	    else
		stmt.setString(3, paidBy);
	    if(paidMethod.equals(""))
		stmt.setNull(4, Types.INTEGER);
	    else
		stmt.setString(4, paidMethod);
	    if(checkNo.equals(""))
		stmt.setNull(5, Types.VARCHAR);
	    else
		stmt.setString(5, checkNo);
	    if(receiptDate.equals(""))
		stmt.setNull(6, Types.DATE);
	    else{
		date_tmp = df.parse(receiptDate);
		stmt.setDate(6, new java.sql.Date(date_tmp.getTime()));
	    }
	    if(clerk.equals(""))
		stmt.setNull(7, Types.CHAR);
	    else
		stmt.setString(7, "y");
	    stmt.setString(8, id);
	    stmt.executeUpdate();
										
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    back = ex+":"+qq;
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	return back; // success
    }
    public      String doSelect(){
	//
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;			
	String back = "";
	String qq = " select risk_id,date_format(paidDate,'%m/%d/%Y'),"+
	    "amount,paidBy,paidMethod,checkNo,clerk,"+
	    "date_format(receiptDate,'%M %d, %Y') from payments "+
	    " where id=?";
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
		str = rs.getString(1);
		if(str != null) risk_id = str;
		str = rs.getString(2);
		if(str != null) paidDate = str;
		amount = rs.getDouble(3);
		str = rs.getString(4);
		if(str != null) paidBy = str;
		str = rs.getString(5);
		if(str != null) paidMethod = str;
		str = rs.getString(6);
		if(str != null) checkNo = str;
		str = rs.getString(7);
		if(str != null) clerk = str;
		str = rs.getString(8);
		if(str != null) receiptDate = str; 
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
    public      String doDelete(){
	//
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;			
	String back = "";
	String qq = "delete from payments where id=?";
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
    //
    public      String getTotalBalance(){

	String qq = "";
	Connection con = null;
	PreparedStatement stmt = null, stmt2=null;
	ResultSet rs = null;
	String str="";
	if(type.equals("legal")){
	    qq = " select damageAmnt,deductible from vslegals where id=?";
	}
	else if(type.equals("safety")){ // internal
	    qq = " select totalCost+totalCostP,deductible from riskSafety where id=?";
	}
	else if(type.equals("tort")){
	    qq = " select paidByInsur,deductible from tortClaims where id=?";
	}
	else if(type.startsWith("workerCcmp")){ // we do not need this
	    str = "0";
	}

	double dbl = 0, deduct=0;
	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    return "Could not connect to DB ";
	}
	try{
	    stmt = con.prepareStatement(qq);
	    stmt.setString(1, risk_id);
	    rs = stmt.executeQuery();
	    if(rs.next()){
		balance = rs.getDouble(1);
		deduct = rs.getDouble(2);
		if(deduct < balance){
		    balance = balance - deduct;
		}
	    }
	    qq = " select sum(amount) from payments where risk_id=?";// +id;
	    if(debug){
		logger.debug(qq);
	    }
	    stmt2 = con.prepareStatement(qq);
	    stmt2.setString(1, risk_id);
	    rs = stmt2.executeQuery();
	    if(rs.next()){
		dbl = rs.getDouble(1);
	    }
	    if(dbl > 0){
		balance = balance - dbl;
	    }
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(con, rs, stmt, stmt2);
	}
	if(balance > 0){
	    balance += 0.005;
	    str = Helper.formatNumber(balance);
	}
	else if(balance < 0){
	    balance = 0;
	}
	else
	    str = ""+balance;
	return str;		
    }
    //
    public String getTotalPayment(){

	String qq = "";
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;			
	qq = " select sum(amount) from payments where risk_id=?";		
	String str="";
	double dbl = 0;
	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    return "Could not connect to DB ";
	}				
	try{
						
	    stmt = con.prepareStatement(qq);
	    stmt.setString(1, risk_id);
	    rs = stmt.executeQuery();
	    if(rs.next()){
		dbl = rs.getDouble(1);
	    }
	}
	catch(Exception ex){
	    System.err.println(ex);
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	if(dbl > 0){
	    dbl += 0.005;
	    str = Helper.formatNumber(dbl);
	}
	else
	    str = "";

	return str;
    }
    //
    public      String findPayments(){
	String back = "";
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;
	String qq = "select id,risk_id,date_format(paidDate,'%m/%d/%Y'),"+
	    "amount,paidBy,paidMethod,checkNo,"+
	    "date_format(receiptDate,'%M %d, %Y'),clerk from payments "+
	    " where risk_id=? order by id desc " ;
	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    return "Could not connect to DB ";
	}				
	try{
	    stmt = con.prepareStatement(qq);
	    stmt.setString(1, risk_id);
	    rs = stmt.executeQuery();
	    while(rs.next()){
		if(payments == null) payments = new ArrayList<>();
		Payment one = new  Payment(debug,
					   rs.getString(1),
					   rs.getString(2),
					   rs.getString(3),
					   rs.getString(4),
					   rs.getString(5),
					   rs.getString(6),
					   rs.getString(7),
					   rs.getString(8),
					   rs.getString(9));
		if(!payments.contains(one))
		    payments.add(one);
	    }
	}catch(Exception ex){
	    logger.error(ex+":"+qq);
	    back = ""+ex+":"+qq;
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	return back;
    }

}
























































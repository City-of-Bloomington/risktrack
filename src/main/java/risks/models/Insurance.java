package risks.models;
import java.sql.*;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.utils.Helper;

public class Insurance{

    boolean debug = false;
    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");		
    static Logger logger = LogManager.getLogger(Insurance.class);
    String id = "";
    String company="", phone="",
	status="",
	adjuster = "",
	adjusterPhone="",
	adjusterEmail = "",
	deductible = "",
	claimNum = "",
	policy = "", policy_num="";
    String relatedId="", amountPaid="", // amountPaid
	attorney="", attorneyPhone="", type="City", // default
	attorneyEmail="", sent="", decisionDate="";
	
    String errors = "";
    public Insurance(boolean deb){
	debug = deb;
    }
    //
    public Insurance(boolean deb, String id){
	debug = deb;
	setId(id);
	errors = doSelect();
		
    }
    public Insurance(boolean deb,
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
	setId(val);
	setCompany(val2);
	setStatus(val3);
	setAdjuster(val4);
	setAdjusterPhone(val5);
	setAdjusterEmail(val6);
	setDeductible(val7);
	setClaimNum(val8);
	setPolicy(val9);
	setAmountPaid(val10);
	setAttorney(val11);
	setAttorneyPhone(val12);
	setAttorneyEmail(val13);
	setSent(val14);
	setDecisionDate(val15);
	setType(val16);
	setPhone(val17);
	setPolicy_num(val18);
		
    }		
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setCompany(String val){
	if(val != null)
	    company = val;
    }
    public void setPhone(String val){
	if(val != null)
	    phone = val;
    }
    public void setStatus(String val){
	if(val != null)
	    status = val;
    }
    public void setAdjuster(String val){
	if(val != null)
	    adjuster = val;
    }
    public void setAdjusterPhone(String val){
	if(val != null)
	    adjusterPhone = val;
    }
    public void setAdjusterEmail(String val){
	if(val != null)
	    adjusterEmail = val;
    }
    public void setDeductible(String val){
	if(val != null)
	    deductible = val;
    }
    public void setClaimNum(String val){
	if(val != null)
	    claimNum = val;
    }
    public void setPolicy(String val){
	if(val != null)
	    policy = val;
    }
    public void setRelatedId(String val){
	if(val != null)
	    relatedId = val;
    }	
    public void setAmountPaid(String val){
	if(val != null)
	    amountPaid = val;
    }
    public void setAttorney(String val){
	if(val != null)
	    attorney = val;
    }
    public void setAttorneyPhone(String val){
	if(val != null)
	    attorneyPhone = val;
    }
    public void setAttorneyEmail(String val){
	if(val != null)
	    attorneyEmail = val;
    }
    public void setSent(String val){
	if(val != null)
	    sent = val;
    }
    public void setDecisionDate(String val){
	if(val != null)
	    decisionDate = val;
    }
    public void setType(String val){
	if(val != null)
	    type = val;
    }
    public void setPolicy_num(String val){
	if(val != null)
	    policy_num = val;
    }	
    //
    // getters
    //
    public String getId(){
	return id;
    }
    public String getCompany(){
	return company;
    }
    public String getStatus(){
	return status;
    }
    public String getAdjuster(){
	return adjuster;
    }
    public String getAdjusterPhone(){
	return adjusterPhone;
    }
    public String getAdjusterEmail(){
	return adjusterEmail;
    }
    public String getClaimNum(){
	return claimNum;
    }
    public String getDeductible(){
	return deductible;
    }
    public String getPolicy(){
	return policy;
    }
	
    public String getAmountPaid(){
	return amountPaid;
    }
    public String getSent(){
	return sent;
    }
    public String getDecisionDate(){
	return decisionDate;
    }
    public String getAttorney(){
	return attorney;
    }
    public boolean hasAdjuster(){
	return !adjuster.equals("") || !adjusterPhone.equals("");
    }	
    public String getAttorneyPhone(){
	return attorneyPhone;
    }
    public String getAttorneyEmail(){
	return attorneyEmail;
    }
    public boolean hasAttorney(){
	return !attorney.equals("") || !attorneyPhone.equals("");
    }
    public String getType(){
	return type;
    }
    public String getPhone(){
	return phone;
    }
    public String getErrors(){
	return errors;
    }
    public String getPolicy_num(){
	return policy_num;
    }	
    public boolean isEmpty(){
	if(company.equals("") && adjuster.equals("") &&
	   policy.equals("") && claimNum.equals("")){
	    return true;
	}
	return false;
    }
    public boolean isValid(){
	return !isEmpty();
    }
    public String doSelect(){
	String msg = "";
	Connection con = null;
	PreparedStatement stmt = null;	
	ResultSet rs = null;	
		
	String qq = "select id,company,status,adjuster,adjusterPhone, "+
	    "adjusterEmail,deductible,claimNum,policy,amountPaid,"+
	    "attorney,attorneyPhone,attorneyEmail,"+
	    "date_format(sent,'%m/%d/%Y'),"+
	    "date_format(decisionDate,'%m/%d/%Y'),type,phone, "+
	    "policy_num "+
	    " from insurances where id=? ";
	if(id.equals("")){
	    msg = " Unknown id ";
	    return msg;
	}
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
		String str = rs.getString(2);
		if(str != null)
		    company = str;
		str = rs.getString(3);
		if(str != null)
		    status = str;
		str = rs.getString(4);
		if(str != null)
		    adjuster = str;
		str = rs.getString(5);
		if(str != null)
		    adjusterPhone = str;
		str = rs.getString(6);
		if(str != null)
		    adjusterEmail = str;
		str = rs.getString(7);
		if(str != null)
		    deductible = str;
		str = rs.getString(8);
		if(str != null)
		    claimNum = str;
		str = rs.getString(9);
		if(str != null)
		    policy = str;
		str = rs.getString(10);
		if(str != null)
		    amountPaid = str;
		str = rs.getString(11);
		if(str != null)
		    attorney = str;
		str = rs.getString(12);
		if(str != null)
		    attorneyPhone = str;
		str = rs.getString(13);
		if(str != null)
		    attorneyEmail = str;
		str = rs.getString(14);
		if(str != null)
		    sent = str;
		str = rs.getString(15);
		if(str != null)
		    decisionDate = str;
		str = rs.getString(16);
		if(str != null)
		    type = str;
		str = rs.getString(17);
		if(str != null)
		    phone = str;
		str = rs.getString(18);
		if(str != null)
		    policy_num = str;	
	    }
	    else{
		msg = " Invalid id "+id;
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
	String str = "", qq="";
	if(id.equals("")){
	    if(isValid()){
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
    public String doInsert(){
		
	String msg = "";
	Connection con = null;
	PreparedStatement stmt = null, stmt2=null, stmt3=null;	
	ResultSet rs = null;		
	String str = "", qq="";
	qq = " insert into insurances values(0,?,?,?,?, ?,?,?,?,?, "+
	    "?,?,?,?,?, ?,?,?) ";
	con = Helper.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    logger.error(msg);
	    return msg;
	}						
	try{
	    stmt = con.prepareStatement(qq);
	    msg = setParams(stmt);
	    if(msg.equals("")){
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
		if(!relatedId.equals("")){
		    qq = "insert into insuranceRelated values(?,?)";
		    if(debug)
			logger.debug(qq);
		    stmt3 = con.prepareStatement(qq);
		    stmt3.setString(1, id);
		    stmt3.setString(2, relatedId);
		    stmt3.executeUpdate();
		}
	    }
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    msg += ex+":"+qq;
	}
	finally{
	    Helper.databaseDisconnect(con, rs, stmt, stmt2, stmt3);
	}
	return msg;
    }
    String setParams(PreparedStatement stmt){
	String back = "";
	try{
	    int jj=1;
	    if(company.equals(""))
		stmt.setNull(jj++, Types.VARCHAR);
	    else
		stmt.setString(jj++, company);
	    if(status.equals(""))
		stmt.setNull(jj++, Types.VARCHAR);
	    else
		stmt.setString(jj++, status);
	    if(adjuster.equals(""))
		stmt.setNull(jj++, Types.VARCHAR);
	    else
		stmt.setString(jj++, adjuster);
	    if(adjusterPhone.equals(""))
		stmt.setNull(jj++, Types.VARCHAR);
	    else
		stmt.setString(jj++, adjusterPhone);
	    if(adjusterEmail.equals(""))
		stmt.setNull(jj++, Types.VARCHAR);
	    else
		stmt.setString(jj++, adjusterEmail);
	    if(deductible.equals(""))
		stmt.setNull(jj++, Types.VARCHAR);
	    else
		stmt.setString(jj++, deductible);
	    if(claimNum.equals(""))
		stmt.setNull(jj++, Types.VARCHAR);
	    else
		stmt.setString(jj++, claimNum);
	    if(policy.equals(""))
		stmt.setNull(jj++, Types.VARCHAR);
	    else
		stmt.setString(jj++, policy);
	    if(amountPaid.equals(""))
		stmt.setNull(jj++, Types.VARCHAR);
	    else
		stmt.setString(jj++,amountPaid);
	    if(attorney.equals(""))
		stmt.setNull(jj++, Types.VARCHAR);
	    else
		stmt.setString(jj++,attorney);
	    if(attorneyPhone.equals(""))
		stmt.setNull(jj++, Types.VARCHAR);
	    else
		stmt.setString(jj++,attorneyPhone);
	    if(attorneyEmail.equals(""))
		stmt.setNull(jj++, Types.VARCHAR);
	    else
		stmt.setString(jj++,attorneyEmail);
	    if(sent.equals(""))
		stmt.setNull(jj++, Types.DATE);
	    else{
		java.util.Date date_tmp = df.parse(sent);
		stmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }
	    if(decisionDate.equals(""))
		stmt.setNull(jj++, Types.DATE);
	    else{
		java.util.Date date_tmp = df.parse(decisionDate);
		stmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }
	    if(type.equals(""))
		stmt.setNull(jj++, Types.VARCHAR);
	    else
		stmt.setString(jj++,type);
	    if(phone.equals(""))
		stmt.setNull(jj++, Types.VARCHAR);
	    else
		stmt.setString(jj++, phone);
	    if(policy_num.equals(""))
		stmt.setNull(jj++, Types.VARCHAR);
	    else
		stmt.setString(jj++, policy_num);
	}
	catch(Exception ex){
	    logger.error(ex);
	    back += ex;
	}
	return back;
    }
    public String doUpdate(){
		
	String msg = "";
	String str = "", qq="";
	Connection con = null;
	PreparedStatement stmt = null;	
	ResultSet rs = null;		
	qq = " update insurances set company=?, status=?, adjuster=?, "+
	    "adjusterPhone=?,adjusterEmail=?,deductible=?,claimNum=?, "+
	    "policy=?,amountPaid=?,attorney=?,attorneyPhone=?,attorneyEmail=?,"+
	    "sent=?,decisionDate=?,type=?,phone=?,policy_num=? where id=? ";
	con = Helper.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    logger.error(msg);
	    return msg;
	}
	try{
	    stmt = con.prepareStatement(qq);
	    setParams(stmt);
	    stmt.setString(18, id);
	    stmt.executeUpdate();
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    msg += ex+":"+qq;
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	return msg;
    }
    public String doDelete(){
		
	String msg = "";
	String str = "", qq="";
	Connection con = null;
	PreparedStatement stmt = null, stmt2=null;	
	ResultSet rs = null;		
	qq = " delete from insuranceRelated where id=?";
	if(debug)
	    logger.debug(qq);
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
	    qq = " delete from insurances where id=?";// +id;			
	    if(debug)
		logger.debug(qq);
	    stmt2 = con.prepareStatement(qq);
	    stmt2.setString(1, id);
	    stmt2.executeUpdate();	
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    msg += ex+":"+qq;
	}
	finally{
	    Helper.databaseDisconnect(con, rs, stmt, stmt2);
	}
	return msg;
    }

}

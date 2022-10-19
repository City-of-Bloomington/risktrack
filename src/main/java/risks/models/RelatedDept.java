package risks.models;

import java.sql.*;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.utils.*;

public class RelatedDept{

    String id="", dept_id="", related_id="", phone="", supervisor="";
    String type="Tort";// Tort, Legal
    static Logger logger = LogManager.getLogger(RelatedDept.class);	
    boolean debug = false;
    Department dept = null;
    public RelatedDept(String val, 
		       String val2,
		       String val3,
		       String val4,
		       String val5,
		       String val6,
		       boolean deb
		       ){
	setId(val);
	setDept_id(val2);
	setRelated_id(val3);
	setPhone(val4);
	setSupervisor(val5);
	setType(val6);
	debug = deb;
    }
    public RelatedDept(boolean deb, String val){
	debug = deb;
	setId(val);
    }
    public RelatedDept(boolean deb){
	debug = deb;
    }
	
    //
    // getters
    //
    public String getId(){
	return id;
    }	
    public String getDept_id(){
	return dept_id;
    }
    public String getRelated_id(){
	return related_id;
    }
    public String getPhone(){
	return phone;
    }
    public String getType(){
	return type;
    }	
    public String getSupervisor(){
	return supervisor;
    }
    public String toString(){
	String ret = "";
	getDept();
	if(dept !=  null){
	    ret = dept.toString();
	}
	return ret;
    }
    public Department getDept(){
	if(dept == null && !dept_id.equals("")){
	    dept = new Department(debug, dept_id);
	    dept.doSelect();
	}
	return dept;
    }
    //
    // setters
    //
    public void setId (String val){
	if(val != null)
	    id = val;
    }	
    public void setDept_id (String val){
	if(val != null)
	    dept_id = val;
    }
    public void setRelated_id (String val){
	if(val != null)
	    related_id = val;
    }
    public void setPhone (String val){
	if(val != null)
	    phone = val;
    }
    public void setType(String val){
	if(val != null)
	    type = val;
    }	
    public void setSupervisor(String val){
	if(val != null)
	    supervisor = val;
    }
    public void setDept(Department val){
	if(val != null)
	    dept = val;
    }	
    public String doSelect(){
		
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;	
	ResultSet rs = null;
		
	if(dept_id.equals("") && related_id.equals("")){
	    back = "dept not set ";
	    return back;
	}
	String qq = "select r.dept_id,r.related_id,r.phone,r.supervisor,"+
	    "r.type,"+
	    "d.id,d.name,d.division,d.phone from "+
	    "deptRelated r, departments d where r.dept_id=d.id and r.id = ?";
	try{
	    if(debug)
		logger.debug(qq);
	    con = Helper.getConnection();
	    if(con == null){
		back = "Could not connect to DB ";
		logger.error(back);
		return back;
	    }										
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, id);
	    rs = pstmt.executeQuery();
	    if (rs.next()) {
		String str = "", str2="", str3="", str4="",str5="";
		str = rs.getString(1);
		setDept_id(str);
		str = rs.getString(2);
		setRelated_id(str);				
		str = rs.getString(3);
		setPhone(str);
		str = rs.getString(4);
		setSupervisor(str);
		str = rs.getString(5);
		setType(str);
								
		str = rs.getString(6);
		str2 = rs.getString(7);
		str3 = rs.getString(8);
		str4 = rs.getString(9);
		dept = new Department(debug, str, str2, str3, str4);
	    }
	    else{
		back = "No match found";
	    }
	}catch(Exception ex){
	    logger.error(ex+":"+qq);
	    back += ex+":"+qq;
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return back;
    }
	
    public String doSave(){
		
	String back = "", qq = "";
	Connection con = null;
	PreparedStatement pstmt = null;	
	ResultSet rs = null;		
	if(dept_id.equals("") && related_id.equals("")){
	    back = "dept id or tort not set ";
	    return back;
	}
	qq = " insert into deptRelated values(0,?,?,?,?,?) ";
	if(debug)
	    logger.debug(qq);
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB ";
	    logger.error(back);
	    return back;
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, dept_id);
	    pstmt.setString(2, related_id);
	    if(phone.equals(""))
		pstmt.setNull(3,Types.VARCHAR);
	    else
		pstmt.setString(3, phone);
	    if(supervisor.equals(""))
		pstmt.setNull(4,Types.VARCHAR);
	    else
		pstmt.setString(4, supervisor);
	    pstmt.setString(5, type);
	    pstmt.executeUpdate();
	    qq = "select LAST_INSERT_ID() ";
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		id = rs.getString(1);
	    }			
	}catch(Exception ex){
	    logger.error(ex+":"+qq);
	    back += ex;
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return back;
    }
    public String doUpdate(){
		
	String back = "", qq = "";
	Connection con = null;
	PreparedStatement pstmt = null;	
	ResultSet rs = null;		
	if(id.equals("")){
	    back = "id not set ";
	    return back;
	}
	qq = "update deptRelated set dept_id = ?, phone = ?, supervisor=? ";
	qq += " where id=? ";
	if(debug)
	    logger.debug(qq);
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB ";
	    logger.error(back);
	    return back;
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, dept_id);
	    if(phone.equals(""))
		pstmt.setNull(2,Types.VARCHAR);
	    else
		pstmt.setString(2, phone);
	    if(supervisor.equals(""))
		pstmt.setNull(3,Types.VARCHAR);
	    else
		pstmt.setString(3, supervisor);
	    pstmt.setString(4, id);
			
	    pstmt.executeUpdate();
	}catch(Exception ex){
	    logger.error(ex+":"+qq);
	    back += ex;
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return back;
    }	
    //
    public String doDelete(){
		
	String back = "", qq="";
	Connection con = null;
	PreparedStatement pstmt = null;	
	ResultSet rs = null;		
	if(id.equals("")){
	    back = " id not set ";
	    return back;
	}
	qq = "delete from deptRelated where id = ? ";
	if(debug)
	    logger.debug(qq);
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB ";
	    logger.error(back);
	    return back;
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, id);
	    pstmt.executeUpdate();
	}catch(Exception ex){
	    logger.error(ex+":"+qq);
	    back += ex;
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return back;
    }

	
}

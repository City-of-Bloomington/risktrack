package risks.models;
import java.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.utils.Helper;

public class Employee{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(Employee.class);
    String id = "",
	userid="", busCat="",
	name = "",
	title = "",
	dept_id = "",
	supervisor = "",
	deptPhone = "", deptName="",
	phone = "", relatedId="";
    Department department = null;
    String errors = "";
    public Employee(){

    }
    public Employee(boolean deb){
	debug = deb;
    }
    public Employee(String val){ // for abbrev list
	setId(val);
    }
    public Employee(boolean deb, String val){
	debug = deb;
	setUserid(val);
    }		
    //
    public Employee(String val, String val2){ // for abbrev list
	setId(val);
	setName(val2);
    }		
    public Employee(boolean deb,
		    String val,
		    String val2,
		    String val3,
		    String val4,
		    String val5,
		    String val6,
		    String val7,
		    String val8
		    ){
	debug = deb;
	setId(val);
	setUserid(val2);
	setName(val3);
	setTitle(val4);
	setPhone(val5);
	setDept_id(val6);
	setDeptPhone(val7);
	setSupervisor(val8);
    }		
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setUserid(String val){
	if(val != null)
	    userid = val;
    }
    public void setFullName(String val){
	if(val != null)
	    name = val;
    }
    public void setName(String val){
	if(val != null)
	    name = val;
    }
    public void setTitle(String val){
	if(val != null)
	    title = val;
    }
    public void setDept_id(String val){
	if(val != null)
	    dept_id = val;
    }
    public void setDeptName(String val){
	if(val != null)
	    deptName = val;
    }
    public void setBusCat(String val){
	if(val != null)
	    busCat = val;
    }
    public void setDeptPhone(String val){
	if(val != null)
	    deptPhone = val;
    }
    public void setSupervisor(String val){
	if(val != null)
	    supervisor = val;
    }
    public void setPhone(String val){
	if(val != null)
	    phone = val;
    }
    public void setRelatedId(String val){
	if(val != null)
	    relatedId = val;
    }
    public void setDepartment(Department val){
	if(val != null){
	    department = val;
	    dept_id = department.getId();
	}
    }
    public void setDepartment(){
	if(!dept_id.equals("") && !dept_id.equals("0")){
	    department = new Department(debug, dept_id);
	    String back = department.doSelect();
	    if(!back.equals(""))
		logger.error("Dept "+back);
	}
    }
    //
    // getters
    //
    public String getId(){
	return id;
    }
    public String getUserid(){
	return userid;
    }
    public String getTitle(){
	return title;
    }
    public String getSupervisor(){
	return supervisor;
    }
    public String getPhone(){
	return phone;
    }
    public String getDept_id(){
	return dept_id;
    }
    public String getDeptName(){
	if(department == null){
	    setDepartment();
	}
	return department.getName();
    }	
    public String getDeptPhone(){
	String str = deptPhone;
	if(str.equals("")){
	    if(department == null){
		setDepartment();
	    }
	    if(department != null)
		str = department.getPhone();
	}
	return str;
    }
    public Department getDepartment(){
	if(department == null){
	    if(!dept_id.equals("") && !dept_id.equals("0")){
		Department dd = new Department(debug, dept_id);
		String back = dd.doSelect();
		if(back.equals("")){
		    department = dd;
		}
	    }
	    if(department == null){
		department = new Department(debug);
	    }
	}
	return department;
    }
    public String getFullName(){
	return name;
    }
    public String getName(){
	return name;
    }
    public String getBusCat(){
	return busCat;
    }		
    public boolean isEmpty(){
		
	return userid.equals("") && name.equals("");

    }
    public String getErrors(){
	return errors;
    }
    public String toString(){
	return id;
    }
    public String getInfo(){
	String str = "Username: "+userid;
	if(!name.equals("")){
	    if(!str.equals("")) str += ", ";						
	    str = "Name: "+name;
	}
	if(!title.equals("")){
	    if(!str.equals("")) str += ", ";
	    str += "Title: "+title;
	}
	if(!phone.equals("")){
	    if(!str.equals("")) str += ", ";
	    str += "Phone: "+phone;
	}
	return str;
    }
				
    public boolean equals(Object o) {
	if (o instanceof Employee) {
	    Employee c = (Employee) o;
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
	
    public String doSelect(){
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;	
		
	String msg = "";
	String qq = "select * from employees ";
	if(!id.equals("")){
	    qq += " where id = ? ";
	}
	else if(!userid.equals("")){
	    qq += " where userid like ? ";
	}
	else{
	    msg = " Either Employee ID or username is required ";
	    return msg;
	}

	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    return "Could not connect to DB ";
	}
	try{
	    stmt = con.prepareStatement(qq);
	    if(!id.equals(""))
		stmt.setString(1, id);
	    else
		stmt.setString(1, userid);
	    rs = stmt.executeQuery();
	    if(rs.next()){
		String str = rs.getString(2);
		if(str != null)
		    userid = str;
		str = rs.getString(3);
		if(str != null)
		    name = str;
		str = rs.getString(4);
		if(str != null)
		    title = str;
		str = rs.getString(5);
		if(str != null)
		    phone = str;
		str = rs.getString(6);
		if(str != null)
		    dept_id = str;
		str = rs.getString(7);
		if(str != null)
		    deptPhone = str;				
		str = rs.getString(8);
		if(str != null)
		    supervisor = str;
		if(!dept_id.equals("") && !dept_id.equals("0")){
		    department = new Department(debug, dept_id);
		    str = department.doSelect();
		    if(!str.equals("")){
			msg += str;
		    }
		}
	    }
	    else{
		if(!id.equals(""))
		    msg = " Invalid id "+id;
		else
		    msg = " Invalid username "+userid;
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
	if(department != null){
	    str = department.doSave();
	    if(str.equals("")){
		dept_id = department.getId();
	    }
	    else{
		msg += str;
		logger.error(str);
	    }
	}
	else if(dept_id.equals("") && !deptName.equals("")){
	    Department depp = new Department(debug);
	    depp.setName(deptName);
	    depp.setPhone(deptPhone);
	    str = depp.doSave();
	    if(str.equals("")){
		dept_id = department.getId();
	    }
	    else{
		msg += str;
		logger.error(str);
	    }	
	}
	if(id.equals("")){
	    if(!isEmpty()){
		msg += doInsert();
	    }
	}
	else{
	    msg += doUpdate();
	}
	return msg;
    }
    public String doInsert(){
	Connection con = null;
	PreparedStatement stmt = null, stmt2=null;
	ResultSet rs = null;			
	String msg = "";
	String str = "", qq = " insert into employees values(0,?,?,?,?, ?,?,?)";
	if(name.equals("")){
	    msg = "name is required";
	}
	con = Helper.getConnection();
	if(con == null){
	    return "Could not connect to DB ";
	}
	try{
	    stmt = con.prepareStatement(qq);
	    if(userid.equals(""))
		stmt.setNull(1, Types.VARCHAR);
	    else
		stmt.setString(1, userid);
	    stmt.setString(2, name);;
	    if(title.equals(""))
		stmt.setNull(3, Types.VARCHAR);
	    else
		stmt.setString(3, title);;
	    if(phone.equals(""))
		stmt.setNull(4, Types.VARCHAR);
	    else
		stmt.setString(4, phone);;
	    if(dept_id.equals(""))
		stmt.setNull(5, Types.VARCHAR);
	    else
		stmt.setString(5, dept_id);;
	    if(deptPhone.equals(""))
		stmt.setNull(6, Types.VARCHAR);
	    else
		stmt.setString(6, deptPhone);;						
	    if(supervisor.equals(""))
		stmt.setNull(7, Types.VARCHAR);
	    else
		stmt.setString(7, supervisor);;
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
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    msg += ex;
	}
	finally{		
	    Helper.databaseDisconnect(con, rs, stmt, stmt2);
	}
	if(!relatedId.equals("")){
	    String back = addRelatedRecord();
	    if(!back.equals("")){
		logger.error(back);
		msg += back;
	    }
	}
	return msg;
    }
    //
    public String addRelatedRecord(){
	String msg = "";
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;			
	String str = "", qq="";
	qq = " insert into empRelated values(?,?)";
	if(debug)
	    logger.debug(qq);
	con = Helper.getConnection();
	if(con == null){
	    return "Could not connect to DB ";
	}				
	try{
	    stmt = con.prepareStatement(qq);
	    stmt.setString(1, id);
	    stmt.setString(2, relatedId);
	    stmt.executeUpdate();
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    msg += ex;
	}
	Helper.databaseDisconnect(con, stmt, rs);			
	return msg;
    }
    public String doUpdate(){
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;			
	String msg = "";
	String str = "", qq="";
	if(name.equals("")){
	    msg = "Name is required ";
	    return msg;
	}
	qq = " update employees set userid=?, name=?, title=?,phone=?,dept_id=?, deptPhone=?,supervisor=? where id=? ";
	if(debug)
	    logger.debug(qq);
	con = Helper.getConnection();
	if(con == null){
	    return "Could not connect to DB ";
	}				
	try{
	    stmt = con.prepareStatement(qq);
	    int jj=1;
	    if(userid.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else{
		stmt.setString(jj++, userid);
	    }
	    stmt.setString(jj++, name);
	    if(title.equals(""))
		stmt.setNull(jj++, Types.VARCHAR);
	    else
		stmt.setString(jj++, title);
	    if(phone.equals(""))
		stmt.setNull(jj++, Types.VARCHAR);
	    else
		stmt.setString(jj++, phone);
	    if(dept_id.equals(""))
		stmt.setNull(jj++, Types.VARCHAR);
	    else
		stmt.setString(jj++, dept_id);
	    if(deptPhone.equals(""))
		stmt.setNull(jj++, Types.VARCHAR);
	    else
		stmt.setString(jj++, deptPhone);						
	    if(supervisor.equals(""))
		stmt.setNull(jj++, Types.VARCHAR);
	    else
		stmt.setString(jj++, supervisor);
	    stmt.setString(jj++, id);
	    stmt.executeUpdate();
	}
	catch(Exception ex){
	    msg += ex+":"+qq;						
	    logger.error(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	msg += updateDeptPhone();
	return msg;
    }

    public String doDelete(){

	Connection con = null;
	PreparedStatement stmt = null, stmt2=null;
	ResultSet rs = null;			
	String msg = "";
	String str = "", qq="", qq2="";
	qq = " delete from empRelated where employee_id=?";
	qq2 = " delete from employees where id=?";
	if(debug)
	    logger.debug(qq);
	con = Helper.getConnection();
	if(con == null){
	    return "Could not connect to DB ";
	}				
	try{
	    stmt = con.prepareStatement(qq);
	    stmt.setString(1, id);
	    stmt.executeUpdate();
	    qq = qq2;
	    if(debug)
		logger.debug(qq2);
	    stmt2 = con.prepareStatement(qq);
	    stmt2.setString(1, id);
	    stmt2.executeUpdate();						
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    msg += ex;
	}
	finally{
	    Helper.databaseDisconnect(con, rs, stmt, stmt2);
	}
	return msg;
    }
    String updateDeptPhone(){
	String msg = "";
	if(!deptPhone.equals("") &&
	   !(dept_id.equals("") || dept_id.equals("0"))){
	    department = new Department(debug, dept_id);
	    msg = department.doSelect();
	    if(msg.equals("")){
		String str = department.getPhone();
		if(str.equals("")){
		    department.setPhone(deptPhone);
		    msg = department.doUpdate();
		}
	    }
	}
	return msg;
    }
	
}

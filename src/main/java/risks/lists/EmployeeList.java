package risks.lists;
/**
 * @copyright Copyright (C) 2014-2022 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.models.*;
import risks.utils.*;

public class EmployeeList{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(EmployeeList.class);
    String userid = "", id="",
	risk_id="", fullName="", relatedId="";
    ;
    boolean legalRelated = false,
	tortRelated=false,
	safetyRelated=false;
    String errors = "";
	
    List<Employee> employees = null;
    List<String> idList = null;
    public EmployeeList(boolean deb){
	debug = deb;
    }
    public EmployeeList(boolean deb, String val){
	debug = deb;
	if(val != null)
	    relatedId = val;
    }	
    //
    // setters
    //
    public void setEmpid(String val){
	if(val != null){
	    id = val;
	}
    }
    public void setRelatedId(String val){
	if(val != null)
	    relatedId = val;
    }
    public void setLegalRelatedOnly(){
	legalRelated = true;
    }
    public void setTortRelatedOnly(){
	tortRelated = true;
    }
    public void setSafetyRelatedOnly(){
	safetyRelated = true;
    }		
    public List<Employee> getEmployees(){
	return employees;
    }
	
    public List<String> getIdList(){
	return idList;
    }
    public String getErrors(){
	return errors;
    }
    public String lookFor(){
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;

	String qq = " select e.id, e.userid,e.name,e.title, e.phone, e.dept_id, e.deptPhone,e.supervisor,r.risk_id from employees e join empRelated r on r.employee_id=e.id ";
	String qw = "", msg="";
	if(!id.equals("")){
	    qw += " e.id  = ? ";
	}
	if(!relatedId.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += " r.risk_id = ? ";
	}
	if(legalRelated){
	    if(!qw.equals("")) qw += " and ";						
	    qw += " r.risk_id in (select id from vslegals)";
	}
	else if(tortRelated){
	    if(!qw.equals("")) qw += " and ";						
	    qw += " r.risk_id in (select id from tortClaims)";
	}
	else if(safetyRelated){
	    if(!qw.equals("")) qw += " and ";						
	    qw += " r.risk_id in (select id from riskSafety)";
	}
	if(!qw.equals("")){
	    qq += " where "+qw;
	}
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
	    int jj=1;
	    if(!id.equals("")){
		stmt.setString(jj++, id);
	    }
	    if(!relatedId.equals(""))
		stmt.setString(jj++, relatedId);
								
	    rs = stmt.executeQuery();
	    employees = new ArrayList<Employee>(5);
	    while(rs.next()){
		Employee one = new Employee(debug,
					    rs.getString(1),
					    rs.getString(2),
					    rs.getString(3),
					    rs.getString(4),
					    rs.getString(5),
					    rs.getString(6),
					    rs.getString(7),
					    rs.getString(8));
		if(!employees.contains(one)) 
		    employees.add(one);
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
    // ToDo probably not needed
    public String find(){

	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;
		
	String qq = " select employee_id from empRelated ";
	String qw = "", msg="";
	if(!id.equals(""))
	    qw += " employee_id = ? ";
	if(!relatedId.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += " related_id = ? ";
	}
	if(!qw.equals("")){
	    qq += " where "+qw;
	}
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
	    int jj=1;
	    if(!id.equals(""))
		stmt.setString(jj++, id);
	    if(!relatedId.equals(""))
		stmt.setString(jj++, relatedId);
	    rs = stmt.executeQuery(qq);
	    idList = new ArrayList<String>(2);
	    while(rs.next()){
		String str = rs.getString(1);
		if(str != null)
		    idList.add(str);
	    }
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

    public String addLinks(String related){
	String back = "", qq="";
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;
	qq = " insert into empRelated values(?,?)";
	if(employees != null && employees.size() > 0){
	    con = Helper.getConnection();
	    if(con == null){
		back = "Could not connect to DB ";
		logger.error(back);
		return back;
	    }											
	    try{
		stmt = con.prepareStatement(qq);
		for(Employee one:employees){
		    stmt.setString(1, one.getId());
		    stmt.setString(2, related);
		    stmt.executeUpdate();
		}
	    }
	    catch(Exception ex){
		logger.error(ex+":"+qq);
		back += ex+":"+qq;
	    }
	    finally{
		Helper.databaseDisconnect(con, stmt, rs);			
	    }
	}
	return back;
    }
	
}

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

public class DeptList{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(DeptList.class);		
    String name = "", id="", division="", phone="";
    String errors = "";
    List<Department> departments = null;

    public DeptList(boolean deb){
	debug = deb;
    }
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setName(String val){
	if(val != null)
	    name = val;
    }
    public void setDivision(String val){
	if(val != null)
	    division = val;
    }
    public void setPhone(String val){
	if(val != null)
	    phone = val;
    }
    public List<Department> getDepartments(){
	return departments;
    }
    public String getErrors(){
	return errors;
    }
    public String lookFor(){
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;		
				
	String qq = " select id,name,division,phone from departments ";
	String qw = "", msg="";
	if(!id.equals("")){
	    qw += " id = ? ";
	}
	else{
	    if(!name.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " name like ? ";
	    }
	    if(!division.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " division like ? ";
	    }
	    if(!phone.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " phone like ? ";
	    }
	}
	if(!qw.equals("")){
	    qq += " where "+qw;
	}
	qq += " order by name,division ";
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
		stmt.setString(jj, id);
	    }
	    else{
		if(!name.equals("")){
		    stmt.setString(jj++, "%"+name+"%");
		}
		if(!division.equals("")){
		    stmt.setString(jj++, "%"+division+"%");
		}
		if(!phone.equals("")){
		    stmt.setString(jj++, "%"+phone+"%");
		}
	    }						
	    rs = stmt.executeQuery();
	    departments = new ArrayList<Department>(5);			
	    while(rs.next()){
		Department dp = new Department(debug, 
					       rs.getString(1),
					       rs.getString(2),
					       rs.getString(3),
					       rs.getString(4));
		if(!departments.contains(dp))
		    departments.add(dp);
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
	
}

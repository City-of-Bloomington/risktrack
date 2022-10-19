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

public class RelatedDeptList extends ArrayList<RelatedDept>{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(RelatedDeptList.class);
    String related_id="", dept_id="", type="Tort";
	
    public RelatedDeptList(boolean deb){
	debug = deb;
    }
    public RelatedDeptList(boolean deb, String val){
	debug = deb;
	setRelated_id(val);
    }
    //
    // setters
    //
    public void setRelated_id(String val){
	if(val != null)
	    related_id = val;
    }
    public void setDept_id(String val){
	if(val != null)
	    dept_id = val;
    }
    public void setType(String val){
	if(val != null)
	    type = val;
    }	
    public String find(){

	Connection con = null;
	PreparedStatement pstmt = null;	
	ResultSet rs = null;
		
	String msg = "";
	//
	// we want the other type to be always the second
	//
	String qq =" select r.id, r.dept_id, r.related_id, r.phone,"+
	    "r.supervisor,r.type,"+
	    "d.id,d.name,d.division,d.phone "+
	    " from deptRelated r,departments d where r.dept_id=d.id ";
		
	String str="",str2="",str3="",str4="", str5="", str6="", back="";
	if(!related_id.equals("")){
	    qq += " and r.related_id = ? ";
	}
	if(!dept_id.equals("")){
	    qq += " and r.dept_id = ? ";
	}
	if(!type.equals("")){
	    qq += " and r.type = ? ";
	}		
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
	    int jj=1;
	    if(!related_id.equals("")){
		pstmt.setString(jj++, related_id);
	    }
	    if(!dept_id.equals("")){
		pstmt.setString(jj++, dept_id);
	    }
	    if(!type.equals("")){
		pstmt.setString(jj++, type);
	    }			
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		str = rs.getString(1);
		str2 = rs.getString(2);
		str3 = rs.getString(3);
		str4 = rs.getString(4);
		str5 = rs.getString(5);
		str6 = rs.getString(6);
		RelatedDept rel = new RelatedDept(str, str2, str3, str4, str5, str6, debug);
		str = rs.getString(7);
		str2 = rs.getString(8);
		str3 = rs.getString(9);
		str4 = rs.getString(10);
		Department dept = new Department(debug, str, str2, str3, str4);
		rel.setDept(dept);
		add(rel);
	    }
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    msg += ex+":"+qq;
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return msg;
    }

}

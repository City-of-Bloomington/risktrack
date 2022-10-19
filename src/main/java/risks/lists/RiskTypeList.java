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


public class RiskTypeList extends ArrayList<RiskType>{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(RiskTypeList.class);
    String errors = "", which="";
    public RiskTypeList(boolean deb){
	debug = deb;
    }
    public RiskTypeList(boolean deb, String val){
	debug = deb;
	if(val != null)
	    which = val;
    }	
    //
    // setters
    //

    public String lookFor(){
		
	String msg = "";
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;
	String qq = " select * from ", str="", str2="";
	if(which.equals("safety")){
	    qq += " riskSafetyTypes ";
	}
	else if(which.equals("claim")){
	    qq += " riskClaimTypes ";
	}
	else if(which.equals("legal")){
	    qq += " riskLegalTypes ";
	}
	else if(which.equals("unified")){
	    qq += " riskUnifiedTypes ";
	}
	else {
	    msg = "Unknown type "+which;
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
	    rs = stmt.executeQuery();
	    if(which.equals("unified")){
		while(rs.next()){
		    str = rs.getString(1);
		    str2 = rs.getString(2);
		    if(str != null){
			RiskType type = new RiskType(debug, str, str2);
			this.add(type);
		    }
		}
	    }
	    else{
		while(rs.next()){
		    str = rs.getString(1);
		    if(str != null){
			RiskType type = new RiskType(debug, "", str);
			this.add(type);
		    }
		}
	    }			
	}
	catch(Exception ex){
	    logger.error(ex);
	    msg += ex+":"+qq;
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	return msg;
    }

	
}

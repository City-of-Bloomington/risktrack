package risks.models;
import java.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.utils.Helper;
/**
 *
 */


public class RiskType{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(RiskType.class);
    String id="", name = "";
    String otherType="";
    public RiskType(boolean deb){
	debug = deb;
    }
    public RiskType(boolean deb, String val){
	debug = deb;
	if(val != null)
	    id = val;
    }	
    public RiskType(boolean deb, String val, String val2){
	debug = deb;
	if(val != null)
	    id = val;
	if(val2 != null)
	    name = val2;
    }
    public String getId(){
	return id;
    }
    public String getName(){
	return name;
    }
    //
    // setters
    //	
    public void setOtherType(String val){
	if(val != null)
	    otherType = val;
    }
    public String doSelect(String which){
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;
	String qq = " select * from ", str="", str2="", msg="";
	if(which.equals("safety")){
	    qq += " riskSafetyTypes ";
	}
	else if(which.equals("claim")){
	    qq += " riskClaimTypes ";
	}
	else if(which.equals("legal")){
	    qq += " riskLegalTypes ";
	}
	else { // the default
	    qq += " riskUnifiedTypes ";
	}
	qq += " where type=?";// +id;
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
	    if(which.equals("unified") || which.equals("")){
		if(rs.next()){
		    name = rs.getString(2);
		}
	    }
	    else{
		if(rs.next()){
		    str = rs.getString(1);
		    name = str;
		    id = str;
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

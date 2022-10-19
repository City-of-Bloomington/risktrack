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

public class DisasterTypeList implements RTypeList{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(DisasterTypeList.class);
    String type = "";
    String errors = "";
    List<RType> types = null;
	
    public DisasterTypeList(boolean deb){
	debug = deb;
    }
    //
    // setters
    //
    public void setType(String val){
	if(val != null)
	    type = val;
    }
    public List<RType> getTypes(){
	return types;
    }

    public String getErrors(){
	return errors;
    }
    public String lookFor(){

	Connection con = null;
	PreparedStatement stmt = null;	
	ResultSet rs = null;	

	String qq = " select id,type from disaster_types ";
	String qw = "", msg="";
	if(!type.equals(""))
	    qw += " type like ? ";
	if(!qw.equals("")){
	    qq += " where "+qw;
	}
	qq += " order by type ";
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
	    if(!type.equals("")){
		stmt.setString(1, type);
	    }
	    rs = stmt.executeQuery();
	    types = new ArrayList<RType>();			
	    while(rs.next()){
		DisasterType rtype =
		    new DisasterType(debug,rs.getString(1),
				     rs.getString(2));
																				 
		types.add(rtype);
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
	
}

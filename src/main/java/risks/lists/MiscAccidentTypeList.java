package risks.lists;
/**
 * @copyright Copyright (C) 2014-2022 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.sql.*;
import javax.sql.*;
import java.util.List;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.models.*;
import risks.utils.*;
/**
 *
 */


public class MiscAccidentTypeList implements RTypeList{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(MiscAccidentTypeList.class);
    String type = "";
    String errors = "";
    List<RType> types = null;
	
    public MiscAccidentTypeList(boolean deb){
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

	String qq = " select id,type from misc_accident_types ";
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
	    if(!type.equals(""))
		stmt.setString(1, type);
	    rs = stmt.executeQuery();
	    types = new ArrayList<RType>();			
	    while(rs.next()){
		String str = rs.getString(1);
		String str2 = rs.getString(2);				
		if(str != null){
		    RType rtype = new MiscAccidentType(debug, str, str2);
		    types.add(rtype);
		}
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

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

public class RelatedList{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(RelatedList.class);
    String id="";
    List<Related> list = null;
	
    public RelatedList(boolean deb){
	debug = deb;
    }
    public RelatedList(boolean deb, String val){
	setId(val);
    }
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public List<Related> getRelatedList(){
	return list;
    }
    public String lookFor(){

	Connection con = null;
	PreparedStatement stmt = null;	
	ResultSet rs = null;
		
	String msg = "";
	if(id.equals("")){
	    msg = " Error: ID not yet set ";
	    return msg;
	}
	//
	// we want the other type to be always the second
	//
	String qq =
	    " select id, id2, type,type2 from legalRelated where "+
	    " id=? "+
	    " union select id2,id,type2,type from legalRelated where id2=?";
		
	String str="",str2="",str3="",str4="", back="";
	if(debug)
	    logger.debug(qq);
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB ";
	    logger.error(back);
	    return back;
	}						
	try{
	    stmt = con.prepareStatement(qq);
	    stmt.setString(1, id);
	    stmt.setString(2, id);
	    rs = stmt.executeQuery();
	    while(rs.next()){
		if(list == null)
		    list = new ArrayList<>();
		str = rs.getString(1);
		str2 = rs.getString(2);
		str3 = rs.getString(3);
		str4 = rs.getString(4);
		Related rel = new Related(str, str2, str3, str4, debug);
		if(rel != null){
		    list.add(rel);
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

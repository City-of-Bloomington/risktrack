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

public class LawFirmList{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(LawFirmList.class);
    String id="";
    List<LawFirm> firms = new ArrayList<>();
	
    public LawFirmList(boolean deb){
	debug = deb;
    }
    public List<LawFirm> getFirms(){
	return firms;
    }
    public String lookFor(){

	Connection con = null;
	PreparedStatement stmt = null;	
	ResultSet rs = null;
		
	String back = "";
	//
	// we want the other type to be always the second
	//
	String qq =
	    " select id, name, address,contact,phones from law_firms order by name";
		
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
	    rs = stmt.executeQuery();
	    while(rs.next()){
		LawFirm one = new LawFirm(debug,
					  rs.getString(1),
					  rs.getString(2),
					  rs.getString(3),
					  rs.getString(4),
					  rs.getString(5));
		firms.add(one);
	    }
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    back += ex+":"+qq;
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	return back;
    }

}

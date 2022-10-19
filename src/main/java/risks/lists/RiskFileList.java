package risks.lists;
/**
 * @copyright Copyright (C) 2014-2022 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.*;
import java.sql.*;
import javax.sql.*;
import java.io.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.models.RiskFile;
import risks.utils.*;
/**
 * 
 */

public class RiskFileList{

    boolean debug;
    static final long serialVersionUID = 23L;
    static Logger logger = LogManager.getLogger(RiskFileList.class);
    String risk_id="";
    List<RiskFile> files = null;
    //
    public RiskFileList(boolean deb){

	debug = deb;
    }
    public RiskFileList(boolean deb,
			String val){

	debug = deb;
	setRisk_id(val);

    }
    //
    // setters
    //
    public void setRisk_id(String val){
	if(val != null){
	    risk_id = val;
	}
    }
    public List<RiskFile> getFiles(){
	return files;
    }
    //
    // save a new record in the database
    // return "" or any exception thrown by DB
    //
    public String find(){
	//
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq ="", qo = "";
	String qf =	"from risk_files ";
	qq = "select id,risk_id,added_by_id,"+
	    "date_format(date,'%m/%d/%Y'),name, "+
	    "old_name,notes ";

	String back="", qw = "";
		
	if(!risk_id.equals("")){
	    qw = " risk_id = ? ";
	}
	if(!qw.equals("")){
	    qw = " where "+qw;
	}
	qq = qq + qf + qw;			
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    return back;
	}		
	try{
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt = con.prepareStatement(qq);
	    if(!risk_id.equals(""))
		pstmt.setString(1, risk_id);
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		RiskFile one = new RiskFile(debug,
					    rs.getString(1),
					    rs.getString(2),
					    rs.getString(3),
					    rs.getString(4),
					    rs.getString(5),
					    rs.getString(6),
					    rs.getString(7));
		if(files == null)
		    files = new ArrayList<>();
		if(!files.contains(one))
		    files.add(one);
	    }
	}
	catch(Exception ex){
	    logger.error(ex+" : "+qq);
	    return ex.toString();
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return "";
    }

	

}























































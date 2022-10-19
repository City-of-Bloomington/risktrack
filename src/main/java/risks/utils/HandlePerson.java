package risks.utils;

import java.util.*;
import java.sql.*;
import javax.sql.*;
import java.io.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.models.*;
import risks.utils.Helper;


/**
 * TODO after handling Person records
 */

public class HandlePerson{

    // 
    // search related variables
    //
    String pidArr[] = null; // needed for list of persons
    String idArr[] = null; // needed for list of claims/vs
    boolean debug = false;

    static Logger logger = LogManager.getLogger(HandlePerson.class);
    //
    // basic constructor
    //
    public HandlePerson(boolean deb){

	debug = deb;
    }
    //
    public void setPidArr(String[] vals){
	pidArr = vals;
    }
    public void setPid(String val){
	pidArr = new String[1];  // array with one pid
	pidArr[0] = val;
    }
    public String [] getPidArr(){
	return pidArr;
    }
    public String [] getIdArr(){
	return idArr;
    }
    //
    // set the some of the available values
    // and do search
    public String doClaimantInsert(String id){
	//
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;		
		
	String msg = "";
	String qq = "";
	String query = "",str="";
	qq = "insert into claim_people values (?,?)";
	con = Helper.getConnection();
	if(con == null){
	    logger.error("Could not connect to DB ");
	    return "Could not connect to DB ";
	}				
	try{
	    if(pidArr != null){
		if(pidArr.length > 0){
		    stmt = con.prepareStatement(qq);	
		    for(int i=0;i<pidArr.length; i++){
			str = pidArr[i];
			if(str != null){
			    stmt.setString(1, str);
			    stmt.setString(2, id);
			    stmt.executeUpdate();
			}
		    }
		}
	    }
	}
	catch(Exception ex){
	    logger.error(ex+":"+query);
	    msg += ex;
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	return msg;
    }
    //
    // set the some of the available values
    // and do search
    public String doClaimantAutoInsert(String id){
	//
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;		
		
	String msg = "";
	String qq = "";
	String query = "",str="";
	qq = "insert into claim_auto_people values (?,?)";
	con = Helper.getConnection();
	if(con == null){
	    logger.error("Could not connect to DB ");
	    return "Could not connect to DB ";
	}				
	try{
	    if(pidArr != null){
		if(pidArr.length > 0){
		    stmt = con.prepareStatement(qq);	
		    for(int i=0;i<pidArr.length; i++){
			str = pidArr[i];
			if(str != null){
			    stmt.setString(1, str);
			    stmt.setString(2, id);
			    stmt.executeUpdate();
			}
		    }
		}
	    }
	}
	catch(Exception ex){
	    logger.error(ex+":"+query);
	    msg += ex;
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	return msg;
    }		
    public String doWitnessInsert(String id){
	//
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;			
	String msg = "";
	String qq = "";
	String query = "",str="";
	qq = "insert into witness_people values (?,?)";
	con = Helper.getConnection();
	if(con == null){
	    logger.error("Could not connect to DB ");
	    return "Could not connect to DB ";
	}				
	try{
	    if(pidArr != null){
		if(pidArr.length > 0){								
		    stmt = con.prepareStatement(qq);
		    for(int i=0;i<pidArr.length; i++){
			str = pidArr[i];
			if(str != null){
			    stmt.setString(1, str);
			    stmt.setString(2, id);
			    stmt.executeUpdate();
			}
		    }
		}
	    }
	}
	catch(Exception ex){
	    logger.error(ex+":"+query);
	    msg += ex;
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	return msg;
    }
    //
    public String doDefendantInsert(String id){
	//
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;			
	String msg = "";
	String qq = "";
	String query = "",str="";
	qq = "insert into vslegal_people values (?,?)";
	con = Helper.getConnection();
	if(con == null){
	    logger.error("Could not connect to DB ");
	    return "Could not connect to DB ";
	}
	try{
	    if(pidArr != null){
		if(pidArr.length > 0){
		    stmt = con.prepareStatement(qq);					
		    for(int i=0;i<pidArr.length; i++){
			str = pidArr[i];
			if(str != null){
			    stmt.setString(1, str);
			    stmt.setString(2, id);
			    stmt.executeUpdate();
			}
		    }
		}
	    }
	}
	catch(Exception ex){
	    logger.error(ex+":"+query);
	    msg += ex;
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	return msg;
    }
    public String doClaimantDelete(String id){
	//
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;			
	String qq = "", msg="";
	qq = " delete from claim_people where risk_id=? "+
	    " and person_id=? ";
	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    logger.error("Could not connect to DB ");
	    return "Could not connect to DB ";
	}				
	try{
	    if(pidArr != null){
		stmt = con.prepareStatement(qq);														
		for(int i=0;i<pidArr.length;i++){
		    stmt.setString(1, id);
		    stmt.setString(2, pidArr[i]);
		    stmt.executeUpdate();
		}
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
    public String doClaimantAutoDelete(String id){
	//
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;			
	String qq = "", msg="";
	qq = " delete from claim_auto_people where risk_id=? "+
	    " and person_id=? ";
	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    logger.error("Could not connect to DB ");
	    return "Could not connect to DB ";
	}				
	try{
	    if(pidArr != null){
		stmt = con.prepareStatement(qq);														
		for(int i=0;i<pidArr.length;i++){
		    stmt.setString(1, id);
		    stmt.setString(2, pidArr[i]);
		    stmt.executeUpdate();
		}
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

    public String doWitnessDelete(String id){
	//
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;			
	String qq = "", msg="";
	qq = " delete from witness_people where risk_id=? and person_id=? ";
	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    logger.error("Could not connect to DB ");
	    return "Could not connect to DB ";
	}				
	try{
	    if(pidArr != null){
		stmt = con.prepareStatement(qq);												
		for(int i=0;i<pidArr.length;i++){
		    stmt.setString(1, id);
		    stmt.setString(2, pidArr[i]);
		    stmt.executeUpdate();
		}
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
    public String doClaimantAndWitnessDelete(String id){
	//
	String qq = "", qq2="", msg="";
	Connection con = null;
	PreparedStatement stmt = null, stmt2=null;
	ResultSet rs = null;			
	qq = " delete from claim_people where risk_id=?";
	qq2 = " delete from witness_people where risk_id=?";
	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    logger.error("Could not connect to DB ");
	    return "Could not connect to DB ";
	}
	try{
	    stmt = con.prepareStatement(qq);
	    stmt.setString(1, id);
	    stmt.executeUpdate();
	    qq = qq2;
	    if(debug){
		logger.debug(qq2);
	    }
	    stmt2 = con.prepareStatement(qq);
	    stmt2.setString(1, id);						
	    stmt2.executeUpdate();
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    msg += ex;
	}
	finally{
	    Helper.databaseDisconnect(con, rs, stmt, stmt2);
	}
	return msg;
    }
    public String doDefendantDelete(String id){
	//
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;			
	String qq = "", msg="";
	qq = " delete from vslegal_people where risk_id=? and person_id=?";
	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    logger.error("Could not connect to DB ");
	    return "Could not connect to DB ";
	}
	try{				
	    if(pidArr != null && pidArr.length > 0){												
		stmt = con.prepareStatement(qq);										
		for(int i=0;i<pidArr.length;i++){
		    stmt.setString(1, id);
		    stmt.setString(2, pidArr[i]);
		    stmt.executeUpdate();
		}
	    }
	    else{
		qq = " delete from vsLegal_people where risk_id=?";
		stmt = con.prepareStatement(qq);										
		stmt.setString(1, id);
		stmt.executeUpdate();
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
    //
    // get list of claims/vs related to certain person
    public String generateIdList(String pid, String type){

	Connection con = null;
	PreparedStatement stmt = null, stmt2=null;
	ResultSet rs = null;		
	String qc = "", qq = "", q = "", msg="";
	if(type.equals("claimant")){
	    qc = "select count(*) from claim_people where person_id=? ";// +pid;
	    q = "select c.risk_id from claim_people c,people r "+
		" where r.id=c.person_id and r.id=?";// +pid;
	}
	else if(type.equals("claimantAuto")){
	    qc = "select count(*) from claim_auto_people where person_id=? ";
	    q = "select c.risk_id from claim_auto_people c,people r "+
		" where r.id=c.person_id and r.id=?";// +pid;
	}				
	else if(type.equals("witness")){
	    qc = "select count(*) from witness_people where person_id=?";//+pid;
	    q = "select c.risk_id from witness_people c,people r "+
		" where r.id=c.person_id and r.id=?";//+pid;
	}
	else if(type.equals("defendant")){
	    qc = "select count(*) from vslegal_people where person_id=?";//+pid;
	    q = "select c.risk_id from vslegal_people c,people r "+
		" where r.id=c.person_id and r.id=?";// +pid;
	}
	qq += " order by c.id ";
	int cnt = 0, i=0;
	qq = qc;
	if(debug){
	    logger.debug(qc);
	}
	try{
	    idArr = null;
	    con = Helper.getConnection();
	    if(con == null){
		logger.error("Could not connect to DB ");
		return "Could not connect to DB ";
	    }
	    stmt = con.prepareStatement(qc);
	    stmt.setString(1, pid);
	    rs = stmt.executeQuery();
	    if(rs.next()){
		cnt = rs.getInt(1);
		if(cnt > 0){
		    idArr = new String[cnt];
		    for(i=0;i<cnt;i++) idArr[i] = "";
		    qq = q;
		    if(debug){
			logger.debug(qq);
		    }
		    stmt2 = con.prepareStatement(qq);
		    stmt2.setString(1, pid);
		    rs = stmt2.executeQuery();
		    i = 0;
		    while(rs.next() && i < cnt){
			idArr[i] = rs.getString(1);
			i++;
		    }
		}
	    }
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    msg += ex;
	}
	finally{
	    Helper.databaseDisconnect(con, rs, stmt, stmt2);
	}
	return msg;
    }
}






















































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

public class EstimateList{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(EstimateList.class);
    String type = "", cost = "", company = "", risk_id="";
    String errors = "";
    List<Estimate> estimates = null;
	
    public EstimateList(boolean deb){
	debug = deb;
    }
    //
    // setters
    //
    public void setType(String val){
	if(val != null)
	    type = val;
    }
    public void setRisk_id(String val){
	if(val != null)
	    risk_id = val;
    }
    public void setCompany(String val){
	if(val != null)
	    company = val;
    }
    public List<Estimate> getEstimateList(){
	return estimates;
    }
    public Estimate[] getEstimates(){
	if(estimates == null) return null;
	Estimate[] estArr = new Estimate[3];
	for(int i=0;i<estimates.size() && i<3;i++){
	    estArr[i] = (Estimate)estimates.get(i);
	}
	//
	// since we need to always return 3
	//
	if(estimates.size() < 3){
	    for(int i=estimates.size();i<3;i++){
		estArr[i] = new Estimate(debug);
	    }
	}
	return estArr;
    }
	
    public String getErrors(){
	return errors;
    }
    public String lookFor(){

	Connection con = null;
	PreparedStatement stmt = null;	
	ResultSet rs = null;		
	String qq = " select * from risk_estimates ";
	String qw = "", msg="";
	if(!type.equals(""))
	    qw += " type = ? "; // '"+type+"'";
	if(!risk_id.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += " risk_id = ? ";
	}
	if(!company.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += " company like ? ";
	}
	if(!qw.equals("")){
	    qq += " where "+qw;
	}
	qq += " order by id ";
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
	    if(!type.equals("")){
		stmt.setString(jj++, type);
	    }
	    if(!risk_id.equals("")){
		stmt.setString(jj++, risk_id);						
	    }
	    if(!company.equals("")){
		stmt.setString(jj++, "%"+company+"%");								
	    }
	    rs = stmt.executeQuery();
	    while(rs.next()){
		if(estimates == null)
		    estimates = new ArrayList<Estimate>(3);
		Estimate est = new Estimate(debug, 								
					    rs.getString(1),
					    rs.getString(2),
					    rs.getString(3),
					    rs.getString(4),
					    rs.getString(5),
					    rs.getString(6));
		estimates.add(est);
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

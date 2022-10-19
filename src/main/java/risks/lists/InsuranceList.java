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

public class InsuranceList{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(InsuranceList.class);
	
    String company = "", relatedId="", // tortId, vsId, ..
	adjuster="", type="City"; //City or Defendant, Claimant
    String errors = "";
    List<Insurance> insurances = null;
    List<String> idList = null;
	
    public InsuranceList(boolean deb){
	debug = deb;
    }
    public InsuranceList(boolean deb, String val){
	debug = deb;
	relatedId = val;
    }	
    //
    // setters
    //
    public void setCompany(String val){
	if(val != null)
	    company = val;
    }
    public void setRelatedId(String val){
	if(val != null)
	    relatedId = val;
    }
    public void setAdjuster(String val){
	if(val != null)
	    adjuster = val;
    }
    public void setType(String val){
	if(val != null){

	    if(val.equals("All"))
		type="";
	    else
		type = val;
	}
    }
    public List<Insurance> getInsurances(){
	return insurances;
    }
    public List<String> getIdList(){
	return idList;
    }
    public String getErrors(){
	return errors;
    }

    public String lookFor(){
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;
		
	String back = "";
	String qq = "select i.id,i.company,i.status,i.adjuster,i.adjusterPhone,"+
	    "i.adjusterEmail,i.deductible,i.claimNum,i.policy,i.amountPaid,"+
	    "i.attorney,i.attorneyPhone,i.attorneyEmail,"+
	    "date_format(i.sent,'%m/%d/%Y'),"+
	    "date_format(i.decisionDate,'%m/%d/%Y'),i.type,i.phone, "+
	    "i.policy_num "+
	    "from insurances i join insuranceRelated r on r.insurance_id=i.id  ";				
	String qw = "";
	if(!relatedId.equals(""))
	    qw += " r.risk_id = ? ";
	if(!type.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += " i.type like ? ";
	}
	if(!company.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += " i.company like ? "; 
	}
	if(!adjuster.equals("")){
	    if(!qw.equals("")) qw += " and ";
	    qw += " i.adjuster like ? ";
	}
	if(!qw.equals("")){
	    qq += " where "+qw;
	}
	if(debug)
	    logger.debug(qq);
	con = Helper.getConnection();
	if(con == null){
	    return "Could not connect to DB ";
	}				
	try{
	    stmt = con.prepareStatement(qq);
	    int jj=1;
	    if(!relatedId.equals("")){
		stmt.setString(jj++, relatedId);
	    }
	    if(!type.equals("")){
		stmt.setString(jj++, type);
	    }
	    if(!company.equals("")){
		stmt.setString(jj++, "%"+company+"%");								
	    }
	    if(!adjuster.equals("")){
		stmt.setString(jj++, "%"+adjuster+"%");								
	    }
	    rs = stmt.executeQuery();
	    insurances = new ArrayList<Insurance>(5);
	    while(rs.next()){
		Insurance one = new Insurance(debug,
					      rs.getString(1),
					      rs.getString(2),
					      rs.getString(3),
					      rs.getString(4),
					      rs.getString(5),
					      rs.getString(6),
					      rs.getString(7),
					      rs.getString(8),
					      rs.getString(9),
					      rs.getString(10),
					      rs.getString(11),
					      rs.getString(12),
					      rs.getString(13),
					      rs.getString(14),
					      rs.getString(15),
					      rs.getString(16),
					      rs.getString(17),
					      rs.getString(18));
		insurances.add(one);
	    }
	}
	catch(Exception ex){
	    back += ex+":"+qq;						
	    logger.error(back);

	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	return back;
    }
    // ToDo probably not needed
    public String find(){
	Connection con = null;
	Statement stmt = null;
	ResultSet rs = null;

	String qq = " select r.insurance_id from insuranceRelated r,insurances i ";
	String qw = "where r.insurance_id=i.id ", msg="";
		
	if(!relatedId.equals(""))
	    qw += " and r.id2 = "+relatedId+"";
	if(!type.equals(""))
	    qw += " and i.type = '"+type+"'";
	if(!company.equals(""))
	    qw += " and i.company like '%"+Helper.escapeIt(company)+"%'";
	if(!adjuster.equals(""))
	    qw += " and i.adjuster like '%"+Helper.escapeIt(adjuster)+"%'";
	qq += qw;
	if(debug)
	    logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		return "Could not connect to DB ";
	    }
	    stmt = con.createStatement();				
	    rs = stmt.executeQuery(qq);
	    idList = new ArrayList<String>(5);
	    while(rs.next()){
		String str = rs.getString(1);
		if(str != null)
		    idList.add(str);
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
    public String addLinks(String relatedId){
	String back = "", qq="";
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;
	qq = " insert into insuranceRelated values(?,?)";
				
	if(insurances != null && insurances.size() > 0){
	    con = Helper.getConnection();
	    if(con == null){
		return "Could not connect to DB ";
	    }						
	    try{
		stmt = con.prepareStatement(qq);
		for(Insurance one:insurances){
		    stmt.setString(1, one.getId());
		    stmt.setString(2, relatedId);
		    stmt.executeUpdate();		
		}
	    }
	    catch(Exception ex){
		logger.error(ex+":"+qq);
		back += ex;
	    }
	    finally{
		Helper.databaseDisconnect(con, stmt, rs);	
	    }
	}
	return back;
    }
}

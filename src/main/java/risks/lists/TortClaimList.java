package risks.lists;
/**
 * @copyright Copyright (C) 2014-2022 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import javax.sql.*;
import java.io.*;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.models.*;
import risks.utils.*;

public class TortClaimList{

    static Logger logger = LogManager.getLogger(TortClaimList.class);
    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");			
    boolean debug = false;
    String dateFrom="", dateTo="";
    String whichDate="";
    String amountFrom="", amountTo="";
    String whichAmount="", orderBy="c.id desc";
    String adjuster="", policy="", 
	adjusterPhone="", adjusterEmail="",
	attorney="",attorneyPhone="",attorneyEmail="",
	empid="", empTitle="", law_firm_id = "",
	empName="", empPhone="", dept_id="";
    String vin="", autoMake="", autoPlate="",autoModel="", autoYear="",
	autoNum= "", id="",
	type="",otherType="", status="", filed="", recordOnly="", claimNum="",
	incident="", insuranceStatus="", insurer="", cityAutoInc="";
    boolean activeOnly = false;
    List<TortClaim> torts = null;
    //		
    // basic constructor
    public TortClaimList(boolean deb){

	debug = deb;
	// super(deb);
	//
    }
    //
    // setters
    //
    public void setDateFrom(String val){
	dateFrom = val;
    }
    public void setDateTo(String val){
	dateTo = val;
    }
    public void setWhichDate(String val){
	whichDate = val;
    }
    public void setAmountFrom(String val){
	amountFrom = val;
    }
    public void setAmountTo(String val){
	amountTo = val;
    }
    public void setWhichAmount(String val){
	whichAmount = val;
    }
    public void setOrderBy(String val){
	orderBy = val;
    }
    public void setAdjuster(String val){
	adjuster = val;
    }
    public void setAdjusterPhone(String val){
	adjusterPhone = val;
    }
    public void setAdjusterEmail(String val){
	adjusterEmail = val;
    }
    public void setAttorney(String val){
	attorney = val;
    }
    public void setAttorneyPhone(String val){
	attorneyPhone = val;
    }
    public void setAttorneyEmail(String val){
	attorneyEmail = val;
    }

    public void setPolicy(String val){
	policy = val;
    }
    public void setLawFirmId(String val){
	law_firm_id = val;
    }		
    public void setEmpName(String val){
	empName = val;
    }
    public void setEmpTitle(String val){
	empTitle = val;
    }
    public void setEmpPhone(String val){
	empPhone = val;
    }
    public void setDept_id(String val){
	dept_id = val;
    }
    public void setEmpid(String val){
	empid = val;
    }
    public void setVin(String val){
	vin = val; 
    }
    public void setAutoPlate(String val){
	autoPlate = val; 
    }
    public void setAutoNum(String val){
	autoNum = val; 
    }	
    public void setAutoMake(String val){
	autoMake = val; 
    }
    public void setAutoModel(String val){
	autoModel = val; 
    }
    public void setAutoYear(String val){
	autoYear = val; 
    }
    public void setId(String val){
	id = val; 
    }
    public void setType(String val){
	type = val; 
    }
    public void setOtherType(String val){
	otherType = val; 
    }
    public void setRecordOnly(String val){
	recordOnly = val; 
    }	
    public void setStatus(String val){
	status = val; 
    }
    public void setClaimNum(String val){
	claimNum = val; 
    }
    public void setFiled(String val){
	filed = val; 
    }
    public void setIncident(String val){
	incident = val; 
    }
    public void setCityAutoInc(String val){
	cityAutoInc = val; 
    }
    public void setInsurer(String val){
	insurer = val; 
    }
    public void setActiveOnly(){
	activeOnly = true;
    }
    public List<TortClaim> getTorts(){
	return torts;
    }
    //
    // search
    //
    public String lookFor(){

	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;
	String back = "";
	String qf = " from tortClaims c ";
	String qq = "select c.id,"+
	    "c.type,c.status,"+
	    "c.deductible,"+
	    "c.paidByCity,c.paidByInsur,"+
	    "c.requestAmount,"+ //
	    "c.settled,"+						
	    "c.miscByCity,"+ //						
	    "c.cityAutoInc,"+
	    "date_format(c.incidentDate,'%m/%d/%Y'),"+
						
	    "c.incident, "+
	    "c.comments,"+
	    "date_format(c.opened,'%m/%d/%Y'), "+   
	    "date_format(c.received,'%m/%d/%Y'), "+
	    "date_format(c.closed,'%m/%d/%Y'), "+
	    "c.filed, "+
	    "c.subInsur, "+
	    "date_format(c.expires,'%m/%d/%Y'), "+
	    "c.cityTotalCost,c.paidByCity2City,c.paidByInsur2City,"+
	    "c.deductible2, "+
	    "c.otherType,c.recordOnly,"+
	    "c.paidByRisk,c.law_firm_id, "+
	    "date_format(c.denialLetterDate,'%m/%d/%Y'), "+
	    "date_format(c.deadlineDate,'%m/%d/%Y'), "+
	    "c.lawsuit,c.bodilyInvolved ";
	String qw = "";
	String str="";
	boolean insurFlag = false, empFlag = false;
	if(!id.equals("")){
	    qw += " c.id =? ";// +id;
	}
	else{
	    if(!type.equals("")){
		qw += " c.type = ?";// '"+type+"'";
	    }
	    if(!law_firm_id.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " c.law_firm_id = ?";// +law_firm_id;
	    }				
	    if(!otherType.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " lower(c.otherType) like ? ";// '%"+otherType.toLowerCase()+"%'";
	    }
	    if(!filed.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " c.filed is not null ";
	    }
	    if(activeOnly){
		if(!qw.equals("")) qw += " and ";
		qw += " c.closed is null and c.status = 'Open' ";
	    }
	    if(!recordOnly.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " c.recordOnly is not null ";
	    }
	    if(!status.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " (c.status = ? ";
		if(status.equals("Open"))
		qw += " and c.closed is null) ";
		else
		qw += " or c.closed is not null) ";		    
	    }
	    if(!claimNum.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " i.claimNum like ? ";// '"+claimNum+"%'";
		insurFlag = true;
	    }
	    if(!adjuster.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " upper(i.adjuster) like ?";// '%"+adjuster+"%'";
		insurFlag = true;
	    }
	    if(!adjusterPhone.equals("")){
		if(!qw.equals("")) qw += " and ";
		insurFlag = true;
		qw += " i.adjusterPhone like ? ";//'%"+adjusterPhone+"%'";
	    }
	    if(!attorney.equals("")){
		if(!qw.equals("")) qw += " and ";
		insurFlag = true;
		qw += " upper(i.attorney) like ?";//'%"+attorney+"%'";
	    }
	    if(!attorneyPhone.equals("")){
		if(!qw.equals("")) qw += " and ";
		insurFlag = true;
		qw += " i.attorneyPhone like ? ";//'%"+attorneyPhone+"%'";
	    }
	    if(!empid.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " e.empid like ? ";
		empFlag = true;
	    }
	    if(!empName.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " e.name like ? ";
		empFlag = true;
	    }
	    if(!empPhone.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " e.phone like ? ";
		empFlag = true;
	    }
	    if(!(dept_id.equals("")|| dept_id.equals("0"))){ // dept_id
		if(!qw.equals("")) qw += " and ";
		qw += " (e.dept_id = ? or dr.dept_id=?) ";						
		empFlag = true;
	    }
	    if(!incident.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " upper(c.incident) like ? ";// '%"+Helper.replaceIt(incident)+"%'";
	    }
	    if(!insuranceStatus.equals("")){
		if(!qw.equals("")) qw += " and ";
		insurFlag = true;
		qw += " i.insuranceStatus = ? ";
	    }
	    if(!insurer.equals("")){
		insurFlag = true;
		if(!qw.equals("")) qw += " and ";
		qw += " i.company like ? ";
	    }
	    if(!cityAutoInc.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " c.cityAutoInc ";
	    }
	    if(!dateFrom.equals("")){
		if(!whichDate.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += "c."+whichDate+" >= ? ";
		}
	    }
	    if(!dateTo.equals("")){
		if(!whichDate.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += "c."+whichDate+" <= ? ";
		}
	    }
	    if(!amountFrom.equals("")){
		if(!whichAmount.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += "c."+whichAmount+" >= ? ";
		}
	    }
	    if(!amountTo.equals("")){
		if(!whichAmount.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += "c."+whichAmount+" <= ? ";
		    qw += " and c."+whichAmount+" > 0 ";
		}
	    }
	    if(empFlag){
		qf += " left join empRelated er on er.risk_id=c.id left join employees e on e.id = er.employee_id left join deptRelated dr on dr.related_id=c.id ";						
	    }	
	    if(insurFlag){
		qf += ", insurances i, insuranceRelated ir ";
		if(!qw.equals("")) qw += " and ";
		qw += " c.id = ir.risk_id and i.id=ir.insurance_id ";
	    }
	    if(!vin.equals("") ||
	       !autoPlate.equals("") ||
	       !autoMake.equals("") ||
	       !autoModel.equals("") ||
	       !autoYear.equals("") ||
	       !autoNum.equals("")
	       ){
		qf += ", risk_autos a ";
		if(!qw.equals("")) qw += " and ";
		qw += " a.risk_id=c.id ";
		str = vin;
		if(str != null && !str.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += "a.vin like ? ";//'%"+str+"%'";
		}
		str = autoPlate;
		if(str != null && !str.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += "a.autoPlate like ? ";
		}
		str = autoMake;
		if(str != null && !str.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += "a.autoMake like ? ";
		}
		str = autoModel;
		if(str != null && !str.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += "a.autoModel like ? ";
		}
		str = autoYear;
		if(str != null && !str.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += "a.autoYear like ? ";
		}
		str = autoNum;
		if(str != null && !str.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += "a.autoNum like ? ";
		}
	    }
	}
	qq = qq + qf;
	if(!qw.equals("")){
	    qq += " where "+qw;
	}
	if(!orderBy.equals("")){
	    qq += " order by "+orderBy;
	}
	// System.err.println(" qq "+qq);
	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB ";
	    logger.debug(back);
	    return back;
	}
	try{
	    stmt = con.prepareStatement(qq);

	    if(!id.equals("")){
		stmt.setString(1, id);
	    }
	    else{
		int jj=1;						
		if(!type.equals("")){
		    stmt.setString(jj++, type);
		}
		if(!law_firm_id.equals("")){
		    stmt.setString(jj++, law_firm_id);
		}				
		if(!otherType.equals("")){
		    stmt.setString(jj++,"%"+otherType.toLowerCase()+"%");
		}
		if(!status.equals("")){
		    stmt.setString(jj++, status);
		}
		if(!claimNum.equals("")){
		    stmt.setString(jj++,claimNum+"%");
		}
		if(!adjuster.equals("")){
		    stmt.setString(jj++, "%"+adjuster+"%");
		}
		if(!adjusterPhone.equals("")){
		    stmt.setString(jj++,"%"+adjusterPhone+"%");
		}
		if(!attorney.equals("")){
		    stmt.setString(jj++,"%"+attorney+"%");
		}
		if(!attorneyPhone.equals("")){
		    stmt.setString(jj++,"%"+attorneyPhone+"%");
		}
		if(!empid.equals("")){
		    stmt.setString(jj++,empid);
		}
		if(!empName.equals("")){
		    stmt.setString(jj++,empName);
		}
		if(!empPhone.equals("")){
		    stmt.setString(jj++,empPhone);
		}
		if(!(dept_id.equals("")|| dept_id.equals("0"))){ // dept_id
		    stmt.setString(jj++,dept_id);
		    stmt.setString(jj++,dept_id);										
		}
		if(!incident.equals("")){
		    stmt.setString(jj++,incident+"%");
		}
		if(!insuranceStatus.equals("")){
		    stmt.setString(jj++,insuranceStatus);
		}
		if(!insurer.equals("")){
		    stmt.setString(jj++, insurer+"%");										
		}
		if(!dateFrom.equals("")){
		    if(!whichDate.equals("")){
			java.util.Date date_tmp = df.parse(dateFrom);
			stmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
		    }
		}
		if(!dateTo.equals("")){
		    if(!whichDate.equals("")){
			java.util.Date date_tmp = df.parse(dateTo);
			stmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
		    }
		}
		if(!amountFrom.equals("")){
		    if(!whichAmount.equals("")){
			stmt.setString(jj++,amountFrom);
		    }
		}
		if(!amountTo.equals("")){
		    if(!whichAmount.equals("")){
			stmt.setString(jj++,amountTo);												
		    }
		}
		if(!vin.equals("") ||
		   !autoPlate.equals("") ||
		   !autoMake.equals("") ||
		   !autoModel.equals("") ||
		   !autoYear.equals("") ||
		   !autoNum.equals("")
		   ){
		    str = vin;
		    if(str != null && !str.equals("")){
			stmt.setString(jj++,"%"+str+"%");
		    }
		    str = autoPlate;
		    if(str != null && !str.equals("")){
			stmt.setString(jj++,"%"+str+"%");
		    }
		    str = autoMake;
		    if(str != null && !str.equals("")){
			stmt.setString(jj++,"%"+str+"%");
		    }
		    str = autoModel;
		    if(str != null && !str.equals("")){
			stmt.setString(jj++,"%"+str+"%");
		    }
		    str = autoYear;
		    if(str != null && !str.equals("")){
			stmt.setString(jj++,"%"+str+"%");
		    }
		    str = autoNum;
		    if(str != null && !str.equals("")){
			stmt.setString(jj++,"%"+str+"%");
		    }
		}
	    }

	    rs = stmt.executeQuery();
	    while(rs.next()){
		TortClaim one =
		    new TortClaim(debug,
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
				  rs.getString(18),
				  rs.getString(19),
				  rs.getString(20),
				  rs.getString(21),
				  rs.getString(22),
				  rs.getString(23),
				  rs.getString(24),
				  rs.getString(25),
				  rs.getString(26),
				  rs.getString(27),
				  rs.getString(28),
				  rs.getString(29),
				  rs.getString(30) != null,
				  rs.getString(31) != null);
				  
		if(torts == null)
		    torts =new ArrayList<>();
		if(!torts.contains(one))
		    torts.add(one);
	    }
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    return ex.toString();
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	return "";
    }
    /**
       select c.id,datediff(curdate(), c.received) from tortClaims c where datediff(curdate(), c.received) in (10,18,21,28) and c.status = 'Open' and c.closed is null and c.recordOnly is null ;
     */
    /**
     * we want all records that has 75 or 30 day of the received date
     */
    public String findForNotification(){

	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;
	String back = "";
	String qf = " from tortClaims c ";
	String qq = "select c.id,"+
	    "c.type,c.status,"+
	    "c.deductible,"+
	    "c.paidByCity,c.paidByInsur,"+
	    "c.requestAmount,"+ //
	    "c.settled,"+						
	    "c.miscByCity,"+ //						
	    "c.cityAutoInc,"+
	    "date_format(c.incidentDate,'%m/%d/%Y'),"+
						
	    "c.incident, "+
	    "c.comments,"+
	    "date_format(c.opened,'%m/%d/%Y'), "+   
	    "date_format(c.received,'%m/%d/%Y'), "+
	    "date_format(c.closed,'%m/%d/%Y'), "+
	    "c.filed, "+
	    "c.subInsur, "+
	    "date_format(c.expires,'%m/%d/%Y'), "+
	    "c.cityTotalCost,c.paidByCity2City,c.paidByInsur2City,"+
	    "c.deductible2, "+
	    "c.otherType,c.recordOnly,"+
	    "c.paidByRisk,c.law_firm_id, "+
	    "date_format(c.denialLetterDate,'%m/%d/%Y'), "+
	    "date_format(c.deadlineDate,'%m/%d/%Y'), "+
	    "c.lawsuit,c.bodilyInvolved ";
	String qw = "datediff(CURDATE(), c.received) in (30,75) and c.status = 'Open' and c.closed is null and c.recordOnly is null ";
	String str="";
	qq = qq + qf;
	if(!qw.equals("")){
	    qq += " where "+qw;
	}
	if(!orderBy.equals("")){
	    qq += " order by "+orderBy;
	}
	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB ";
	    logger.debug(back);
	    return back;
	}
	try{    
	    stmt = con.prepareStatement(qq);
	    rs = stmt.executeQuery();
	    while(rs.next()){
		TortClaim one =
		    new TortClaim(debug,
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
				  rs.getString(18),
				  rs.getString(19),
				  rs.getString(20),
				  rs.getString(21),
				  rs.getString(22),
				  rs.getString(23),
				  rs.getString(24),
				  rs.getString(25),
				  rs.getString(26),
				  rs.getString(27),
				  rs.getString(28),
				  rs.getString(29),
				  rs.getString(30) != null,
				  rs.getString(31) != null);
				  
		if(torts == null)
		    torts =new ArrayList<>();
		if(!torts.contains(one))
		    torts.add(one);
	    }
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    return ex.toString();
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	return "";
    }

}























































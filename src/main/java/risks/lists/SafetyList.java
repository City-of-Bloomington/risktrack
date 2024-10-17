package risks.lists;

import java.util.*;
import java.sql.*;
import javax.sql.*;
import java.io.*;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.utils.Helper;
import risks.models.*;


public class SafetyList extends Safety{

    static Logger logger = LogManager.getLogger(SafetyList.class);
    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");	
    static boolean debug = false;
    String dateFrom="", dateTo="", id="";
    String whichDate="";
    String amountFrom="", amountTo="";
    String whichAmount="", orderBy="r.id desc";
    String anyEstPlace="";
    String onOffDuty = "";
    List<Safety> safetyList = null;
    Auto auto = null;
    //
    // basic constructor
    public SafetyList(boolean deb){
	super(deb);
	debug = deb;
    }
    //
    // setters
    //
    @Override
    public void setId(String val){
	if(val != null)
	    id = val;
    }        
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
    public void setAnyEstPlace(String val){
	anyEstPlace = val;
    }
    public void setAuto(Auto val){
	auto = val;
    }
    public void setOnOffDuty(String val){
	if(val != null)
	    onOffDuty = val;
    }        
    //
    public List<Safety> getSafetyList(){
	return safetyList;
    }
    public String lookFor(){

	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;
	boolean relatedFlag = false, insurFlag = false, empFlag = false;
	String qq = "select r.id,"+
	    "r.type,r.status,"+
	    "date_format(r.accidDate,'%m/%d/%Y'),"+
	    "r.accidTime,"+ 
	    "date_format(r.reported,'%m/%d/%Y'),"+
						
	    "r.accidLocation,"+
	    "r.damage,"+
	    "r.estPlace,r.estCost,"+
	    "r.estPlace2,r.estCost2,"+
	    "r.estPlace3,r.estCost3,"+
	    "r.chosenDealer,"+
	    "r.totalCost,"+ // 16
						
	    "r.autoDamage,"+
	    "r.autoPaid,"+						
	    "r.estPlaceP,r.estCostP,"+
	    "r.estPlaceP2,r.estCostP2,"+
	    "r.estPlaceP3,r.estCostP3,"+
	    "r.chosenDealerP,"+
	    "r.totalCostP, "+ //26
						
	    "r.propDamage,"+
	    "r.propPaid,"+
	    "r.subToInsur, "+
	    "r.empInjured,"+
	    "r.workComp,"+ //31
						
	    "r.whatProp,"+
	    "r.repairInfo, "+
	    "r.deductible,"+
	    "r.otherType, "+
	    "r.paidByCity,r.paidByInsur,r.miscByCity, "+
	    "r.recordOnly,r.paidByRisk,r.outOfDuty ";
	String qf = " from riskSafety r ";
	String qw = "";
	String str="", back="";

	if(!id.equals("")){
	    qw += " r.id = ? ";
	}
	else{
	    if(!tortId.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "( lr.id = ? or lr.id2 = ?) and (r.id=lr.id or r.id=lr.id2)";
		relatedFlag = true;
	    }
	    if(!vsId.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "( lr.id = ? or lr.id2 = ?) and (r.id=lr.id or r.id=lr.id2)";
		relatedFlag = true;
								
	    }
	    if(!type.equals("") && !type.equals("0")){
		if(!qw.equals("")) qw += " and ";						
		qw += " r.type = ? ";
								
	    }
	    if(!otherType.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " lower(r.otherType) ? ";// '%"+otherType.toLowerCase()+"%'";
	    }
	    if(!status.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " r.status = ? ";
	    }
	    if(!insurStatus.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " i.insurStatus = ? ";
		insurFlag = true;
	    }
	    if(!insurance.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " upper(i.company) like  ?";// '%"+insurance+"%'";
		insurFlag = true;
	    }
	    if(!claimNum.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " upper(i.claimNum) like ?";// '%"+claimNum+"%'";
		insurFlag = true;
	    }
	    if(!attorney.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " upper(i.attorney) like ?";// '%"+attorney+"%'";
		insurFlag = true;
	    }
	    if(!attorneyPhone.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " i.attorneyPhone like ?";// '%"+attorneyPhone+"%'";
		insurFlag = true;
	    }
	    if(!adjuster.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " upper(i.adjuster) like  ? ";// '%"+adjuster+"%'";
		insurFlag = true;
	    }
	    if(!adjusterPhone.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " upper(i.adjusterPhone) like ?";// '%"+adjusterPhone+"%'";
		insurFlag = true;
	    }
	    if(!policy.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " i.policy = ? ";
		insurFlag = true;
	    }
	    if(!autoDamage.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " r.autoDamage is not null";
								
	    }
	    if(!propDamage.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " r.propDamage is not null";
								
	    }
	    if(!empid.equals("")){
		if(!qw.equals("")) qw += " and ";						
		qw += " e.id like ? ";
		empFlag = true;
	    }
	    if(!empName.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " e.name like ? ";// '%"+empName+"%'";
		empFlag = true;
	    }
	    if(!empInjured.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " r.empInjured is not null ";
	    }
	    if(!recordOnly.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " r.recordOnly is not null ";
	    }
	    if(!deptPhone.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " e.phone ? ";//  '%"+deptPhone+"%'";
		empFlag = true;
	    }
	    if(!(dept.equals("") || dept.equals("0"))){
		if(!qw.equals("")) qw += " and ";
		qw += " (e.dept_id = ? or dr.dept_id = ? ) ";			
	    }
	    if(!onOffDuty.isEmpty()){
		if(!qw.equals(""))
		    qw += " and ";
		if(onOffDuty.startsWith("on")){
		    qw += "r.outOfDuty is null";
		}
		else{
		    qw += "r.outOfDuty is not null ";
		}
	    }	    
	    if(empFlag){
		qf += " left join empRelated er on er.risk_id=r.id join employees e on e.id = er.employee_id left join deptRelated dr on dr.related_id=r.id ";
	    }	
	    if(insurFlag){
		qf += ", insurances i, insuranceRelated ir ";
		if(!qw.equals("")) qw += " and ";
		qw += " r.id = ir.risk_id and i.id=ir.insurance_id ";
	    }
	    if(relatedFlag){
		qf += ", legalRelated lr ";

	    }
	    if(auto != null){
		qf += ", risk_autos a ";
		str = auto.getVin();
		if(!qw.equals("")) qw += " and ";
		qw += " a.risk_id=r.id ";
		if(str != null && !str.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += "a.vin like ? ";
		}
		str = auto.getAutoPlate();
		if(str != null && !str.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += "a.autoPlate like ? ";
		}
		str = auto.getAutoMake();
		if(str != null && !str.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += "a.autoMake like ? ";
		}
		str = auto.getAutoModel();
		if(str != null && !str.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += "a.autoModel like ? ";
		}
		str = auto.getAutoYear();
		if(str != null && !str.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += "a.autoYear like ? ";
		}
		str = auto.getAutoNum();
		if(str != null && !str.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += "a.autoNum like ? ";
		}
								
	    }
	    if(!accidTime.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " r.accidTime like ? ";// '%"+accidTime+"%'";
	    }
	    if(!damage.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " r.damage like ? ";// '%"+Helper.replaceIt(damage)+"%'";
	    }
	    if(!anyEstPlace.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " (r.estPlace like ? or "+ //'%"+anyEstPlace+"%' or "+
		    " r.estPlace2 like ? or "+ // '%"+anyEstPlace+"%' or "+
		    " r.estPlace3 like ? or )";// '%"+anyEstPlace+"%' )";
	    }
	    if(!accidLocation.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " r.accidLocation ? ";//  '%"+Helper.replaceIt(accidLocation)+"%'";
	    }
	    if(!chosenDealer.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " r.chosenDealer like ? ";// '%"+Helper.replaceIt(chosenDealer)+"%'";
	    }
	    if(!dateFrom.equals("")){
		if(whichDate.equals("reported")){
		    if(!qw.equals("")) qw += " and ";
		    qw += " r.reported >= ? ";// str_to_date('"+dateFrom+"','%m/%d/%Y')";
		}
		else if(whichDate.equals("accidDate")){
		    if(!qw.equals("")) qw += " and ";
		    qw += " r.accidDate >= ? ";// str_to_date('"+dateFrom+"','%m/%d/%Y')";
		}
	    }
	    //
	    if(!dateTo.equals("")){
		if(whichDate.equals("reported")){
		    if(!qw.equals("")) qw += " and ";
		    qw += " r.reported <= ? ";// str_to_date('"+dateFrom+"','%m/%d/%Y')";
		}
		else if(whichDate.equals("accidDate")){
		    if(!qw.equals("")) qw += " and ";
		    qw += " r.accidDate <= ? ";//str_to_date('"+dateFrom+"','%m/%d/%Y')";
		}
	    }
	    if(!amountFrom.equals("")){
		if(whichAmount.equals("totalCost")){
		    if(!qw.equals("")) qw += " and ";
		    qw += " r.totalCost >= ?"; 
		}
		else if(whichAmount.equals("totalCostP")){
		    if(!qw.equals("")) qw += " and ";
		    qw += " r.totalCostP >= ?";
		}
	    }
	    if(!amountTo.equals("")){
		if(whichAmount.equals("totalCost")){
		    if(!qw.equals("")) qw += " and ";
		    qw += " r.totalCost <= ?"; 
		}
		else if(whichAmount.equals("totalCostP")){
		    if(!qw.equals("")) qw += " and ";
		    qw += " r.totalCostP <= ?"; 
		}
	    }
	}
	if(!qw.equals("")){
	    qw = " where "+qw;
	}
	qq += qf + qw;
	if(!orderBy.equals("")){
	    qq += " order by "+orderBy;
	}
	logger.debug(qq);
	con = Helper.getConnection();
	if(con == null){
	    return "Could not connect to DB ";
	}				
	try{
	    stmt = con.prepareStatement(qq);
	    int jj=1;
	    if(!id.equals("")){
		stmt.setString(jj++, id);
	    }
	    else{
		if(!tortId.equals("")){
		    stmt.setString(jj++, tortId);
		    stmt.setString(jj++, tortId);
		}
		if(!vsId.equals("")){
		    stmt.setString(jj++, vsId);
		    stmt.setString(jj++, vsId);
		}
		if(!type.equals("") && !type.equals("0")){
		    stmt.setString(jj++, type);
		}
		if(!otherType.equals("")){
		    stmt.setString(jj++, "%"+otherType+"%");
		}
		if(!status.equals("")){
		    stmt.setString(jj++, status);
		}
		if(!insurStatus.equals("")){
		    stmt.setString(jj++, insurStatus);
		}
		if(!insurance.equals("")){
		    stmt.setString(jj++, insurance);
		}
		if(!claimNum.equals("")){
		    stmt.setString(jj++, claimNum);
		}
		if(!attorney.equals("")){
		    stmt.setString(jj++, attorney);
		}
		if(!attorneyPhone.equals("")){
		    stmt.setString(jj++, attorneyPhone);
		}
		if(!adjuster.equals("")){
		    stmt.setString(jj++, adjuster);
		}
		if(!adjusterPhone.equals("")){
		    stmt.setString(jj++, adjusterPhone);
		}
		if(!policy.equals("")){
		    stmt.setString(jj++,policy);
		}
		if(!empid.equals("")){
		    stmt.setString(jj++, empid);
		}
		if(!empName.equals("")){
		    stmt.setString(jj++, empName);
		}
		if(!deptPhone.equals("")){
		    stmt.setString(jj++, deptPhone);
		}
		if(!(dept.equals("") || dept.equals("0"))){
		    stmt.setString(jj++, dept); // prob dept_id
		    stmt.setString(jj++, dept);
		}
		if(auto != null){
		    str = auto.getVin();
		    if(str != null && !str.equals("")){
			stmt.setString(jj++, str);
		    }
		    str = auto.getAutoPlate();
		    if(str != null && !str.equals("")){
			stmt.setString(jj++, str);
		    }
		    str = auto.getAutoMake();
		    if(str != null && !str.equals("")){
			stmt.setString(jj++, str);
		    }
		    str = auto.getAutoModel();
		    if(str != null && !str.equals("")){
			stmt.setString(jj++, str);
		    }
		    str = auto.getAutoYear();
		    if(str != null && !str.equals("")){
			stmt.setString(jj++, str);
		    }
		    str = auto.getAutoNum();
		    if(str != null && !str.equals("")){
			stmt.setString(jj++, str);
		    }
		}
		if(!accidTime.equals("")){
		    stmt.setString(jj++, accidTime);
		}
		if(!damage.equals("")){
		    stmt.setString(jj++, "%"+damage+"%");
		}
		if(!anyEstPlace.equals("")){
		    stmt.setString(jj++, "%"+anyEstPlace+"%");						
		    stmt.setString(jj++, "%"+anyEstPlace+"%");
		    stmt.setString(jj++, "%"+anyEstPlace+"%");						
		}
		if(!accidLocation.equals("")){
		    stmt.setString(jj++, "%"+accidLocation+"%");	
		}
		if(!chosenDealer.equals("")){
		    stmt.setString(jj++, chosenDealer);
		}
		if(!dateFrom.equals("")){
		    if(whichDate.equals("reported")){
			java.util.Date date_tmp = df.parse(dateFrom);
			stmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
		    }
		    else if(whichDate.equals("accidDate")){
			java.util.Date date_tmp = df.parse(dateFrom);
			stmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
		    }
		}
		//
		if(!dateTo.equals("")){
		    if(whichDate.equals("reported")){
			java.util.Date date_tmp = df.parse(dateTo);
			stmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
		    }
		    else if(whichDate.equals("accidDate")){
			java.util.Date date_tmp = df.parse(dateTo);
			stmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
		    }
		}
		if(!amountFrom.equals("")){
		    if(whichAmount.equals("totalCost")){
			stmt.setString(jj++, amountFrom);
		    }
		    else if(whichAmount.equals("totalCostP")){
			stmt.setString(jj++, amountFrom);
		    }
		}
		if(!amountTo.equals("")){
		    if(whichAmount.equals("totalCost")){
			stmt.setString(jj++, amountTo);
		    }
		    else if(whichAmount.equals("totalCostP")){
			stmt.setString(jj++, amountTo);
		    }
		}
	    }
	    rs = stmt.executeQuery();
	    while(rs.next()){
		if(safetyList == null)
		    safetyList = new ArrayList<>();
		Safety one = new Safety(debug,
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
					rs.getString(30),
					rs.getString(31),
					rs.getString(32),
					rs.getString(33),
					rs.getString(34),
					rs.getString(35),
					rs.getString(36),
					rs.getString(37),
					rs.getString(38),
					rs.getString(39),
					rs.getString(40),
					rs.getString(41));
		if(!safetyList.contains(one))
		    safetyList.add(one);
	    }
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    back = ex+":"+qq;
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	return back;
    }

}























































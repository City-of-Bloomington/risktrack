package risks.lists;
/**
 * @copyright Copyright (C) 2014-2022 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.models.*;
import risks.utils.*;


public class AutoAccidentList{

    boolean debug = false;
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    static Logger logger = LogManager.getLogger(AutoAccidentList.class);
	
    String type = "", id="", otherType="", status="", dept_id="",
	damage="",
	empInjured="",
	autoDamage="", propDamage="", // y/n
	whichDate="",dateFrom="", dateTo="",
	whichAmount="", amountFrom="", amountTo="";
	
    String empid="", empName="", // joins
	empDept="", deptPhone="",
	vin="", autoPlate="", autoNum="", autoMake="",autoModel="", autoYear="",
	insurance="",adjuster="",adjusterPhone="",claimNum="",insurStatus="",
	policy="";
    String orderBy="d.id desc";
	
    String errors = "";
    List<AutoAccident> autoAccidents = null;
	
    public AutoAccidentList(boolean deb){
	debug = deb;
    }
    //
    // setters
    //
    public void setType(String val){
	if(val != null)
	    type = val;
    }
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setOtherType(String val){
	if(val != null)
	    otherType = val;
    }
    public void setStatus(String val){
	if(val != null)
	    status = val;
    }
    public void setDept_id(String val){
	if(val != null)
	    dept_id = val;
    }
    public void setDamage(String val){
	if(val != null)
	    damage = val;
    }
    public void setEmpid(String val){
	if(val != null)
	    empid = val;
    }
    public void setEmpName(String val){
	if(val != null)
	    empName = val;
    }
    public void setEmpDept(String val){
	if(val != null)
	    empDept = val;
    }
    public void setDeptPhone(String val){
	if(val != null)
	    deptPhone = val;
    }
    public void setEmpInjured(String val){
	if(val != null)
	    empInjured = val;
    }
    public void setAutoDamage(String val){
	if(val != null)
	    autoDamage = val;
    }
    public void setPropDamage(String val){
	if(val != null)
	    propDamage = val;
    }
    public void setVin(String val){
	if(val != null)
	    vin = val;
    }
    public void setAutoPlate(String val){
	if(val != null)
	    autoPlate = val;
    }
    public void setAutoNum(String val){
	if(val != null)
	    autoNum = val;
    }
    public void setAutoMake(String val){
	if(val != null)
	    autoMake = val;
    }
    public void setAutoModel(String val){
	if(val != null)
	    autoModel = val;
    }
    public void setAutoYear(String val){
	if(val != null)
	    autoYear = val;
    }
    public void setInsurance(String val){
	if(val != null)
	    insurance = val;
    }
    public void setAdjuster(String val){
	if(val != null)
	    adjuster = val;
    }
    public void setAdjusterPhone(String val){
	if(val != null)
	    adjusterPhone = val;
    }
    public void setClaimNum(String val){
	if(val != null)
	    claimNum = val;
    }
    public void setInsurStatus(String val){
	if(val != null)
	    insurStatus = val;
    }
    public void setPolicy(String val){
	if(val != null)
	    policy = val;
    }
    public void setWhichDate(String val){
	if(val != null)
	    whichDate = val;
    }
    public void setDateFrom(String val){
	if(val != null)
	    dateFrom = val;
    }
    public void setDateTo(String val){
	if(val != null)
	    dateTo = val;
    }
    public void setWhichAmount(String val){
	if(val != null)
	    whichAmount = val;
    }
    public void setAmountFrom(String val){
	if(val != null)
	    amountFrom = val;
    }
    public void setAmountTo(String val){
	if(val != null)
	    amountTo = val;
    }
    public void setOrderBy(String val){
	if(val != null)
	    orderBy = val;
    }
    public List<AutoAccident> getAutoAccidents(){
	return autoAccidents;
    }
    //
    public String getErrors(){
	return errors;
    }
    //	
    public String lookFor(){

	Connection con = null;
	PreparedStatement pstmt = null;	
	ResultSet rs = null;
	String qq = "select d.id,"+
	    "d.type,d.status,d.dept_id,d.deptPhone,"+
						
	    "date_format(d.accidDate,'%m/%d/%Y'),"+
	    "d.accidTime,"+ 
	    "date_format(d.reported,'%m/%d/%Y'),"+
	    "d.accidLocation,"+
	    "d.empInjured,"+
						
	    "d.damage,"+
	    "d.totalCost,"+						
	    "d.autoDamage,"+
	    "d.autoPaid,"+
	    "d.totalCostP, "+
						
	    "d.propDamage,"+
	    "d.propPaid,"+
	    "d.subToInsur, "+
	    " d.workComp,"+
	    " d.whatProp,"+
						
	    " d.repairInfo, "+
	    " d.otherType, "+
	    " d.paidByCity,d.paidByInsur,d.miscByCity, "+
						
	    " d.paidByRisk, "+
	    " d.deptContact "+
	    " from auto_accidents d ";
	String qw = "", msg="";
	boolean empTbl = false, insurTbl = false, autoTbl = false;
	if(!id.equals("")){
	    qw += " d.id = ?";
	}
	else{
	    if(!type.equals("")){
		if(!qw.equals(""))
		    qw += " and ";
		qw += " d.type = ?";
	    }
	    if(!status.equals("")){
		if(!qw.equals(""))
		    qw += " and ";
		qw += " d.status = ? ";
	    }
	    if(!dept_id.equals("")){
		if(!qw.equals(""))
		    qw += " and ";
		qw += " d.dept_id = ?";
	    }
	    if(!otherType.equals("")){
		if(!qw.equals(""))
		    qw += " and ";
		qw += " d.otherType like ? ";
	    }
	    if(!damage.equals("")){
		if(!qw.equals(""))
		    qw += " and ";
		qw += " d.damage like ? ";
	    }
	    if(!whichDate.equals("")){
		if(!dateFrom.equals("")){
		    if(!qw.equals(""))
			qw += " and ";
		    qw += whichDate+" >= ? ";
		}
		if(!dateTo.equals("")){
		    if(!qw.equals("")){
			qw += " and ";
			qw += whichDate+" <= ? ";
		    }
		}
	    }
	    if(!whichAmount.equals("")){
		if(!amountFrom.equals("")){
		    if(!qw.equals(""))
			qw += " and ";
		    qw += whichAmount+" >= ? ";
		}
		if(!amountTo.equals("")){
		    if(!qw.equals(""))
			qw += " and ";
		    qw += whichAmount+" <= ? ";
		}
	    }
	    if(!empid.equals("")){
		empTbl = true;
		if(!qw.equals(""))
		    qw += " and ";
		qw += " e.id = ? ";
	    }
	    if(!empName.equals("")){
		empTbl = true;
		if(!qw.equals(""))
		    qw += " and ";
		qw += " e.name like ? ";
	    }
	    if(!empDept.equals("")){
		empTbl = true;
		if(!qw.equals(""))
		    qw += " and ";
		qw += " e.dept_id = ? ";
	    }
	    if(!deptPhone.equals("")){
		empTbl = true;
		if(!qw.equals(""))
		    qw += " and ";
		qw += " d.deptPhone like ? ";
	    }
	    if(!vin.equals("")){
		autoTbl = true;
		if(!qw.equals(""))
		    qw += " and ";
		qw += " a.vin like ? ";
	    }
	    if(!autoPlate.equals("")){
		autoTbl = true;
		if(!qw.equals(""))
		    qw += " and ";
		qw += " a.autoPlate like ? ";
	    }
	    if(!autoNum.equals("")){
		autoTbl = true;
		if(!qw.equals(""))
		    qw += " and ";
		qw += " a.autoNum like ? ";
	    }
	    if(!autoMake.equals("")){
		autoTbl = true;
		if(!qw.equals(""))
		    qw += " and ";
		qw += " a.autoMake like ? ";
	    }
	    if(!autoModel.equals("")){
		autoTbl = true;
		if(!qw.equals(""))
		    qw += " and ";
		qw += " a.autoModel like ? ";
	    }
	    if(!autoYear.equals("")){
		autoTbl = true;
		if(!qw.equals(""))
		    qw += " and ";
		qw += " a.autoYear like ? ";
	    }
	    if(!insurance.equals("")){
		insurTbl = true;
		if(!qw.equals(""))
		    qw += " and ";
		qw += " i.company like ? ";
	    }
	    if(!adjuster.equals("")){
		insurTbl = true;
		if(!qw.equals(""))
		    qw += " and ";
		qw += " i.adjuster like ? ";
	    }
	    if(!adjusterPhone.equals("")){
		insurTbl = true;
		if(!qw.equals(""))
		    qw += " and ";
		qw += " i.adjusterPhone like ? ";
	    }
	    if(!claimNum.equals("")){
		insurTbl = true;
		if(!qw.equals(""))
		    qw += " and ";
		qw += " i.claimNum like ? ";
	    }
	    if(!insurStatus.equals("")){
		insurTbl = true;
		if(!qw.equals(""))
		    qw += " and ";
		qw += " i.status = ? ";
	    }
	    if(!policy.equals("")){
		insurTbl = true;
		if(!qw.equals(""))
		    qw += " and ";
		qw += " i.policy = ? ";
	    }
	}
	if(empTbl){
	    qq += ", employees e, empRelated ed ";
	    qw += " and ed.risk_id=d.id and ed.employee_id=e.id ";
	}
	if(autoTbl){
	    qq += ", riskAuto a";
	    qw += " and a.id=d.id ";
	}
	if(insurTbl){
	    qq += ", insurances i, insuranceRelated ir ";
	    qw += " and ir.risk_id=d.id and ir.insurance_id=i.id ";
	}
	if(orderBy.indexOf("type") > -1){
	    qq += ", disasterTypes t ";
	    qw += " d.type=t.id ";
	}
	if(!qw.equals("")){
	    qq += " where ";
	    qq += qw;
	}
	if(orderBy.equals("")){
	    orderBy = " d.id desc";
	}
	if(!orderBy.equals(""))
	    qq += " order by "+orderBy;
	if(debug)
	    logger.debug(qq);
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Could not connect to DB ";
		logger.error(msg);
		return msg;
	    }
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    if(!id.equals("")){
		pstmt.setString(jj++, id);
	    }
	    else{
		if(!type.equals("")){
		    pstmt.setString(jj++, type);
		}
		if(!status.equals("")){
		    pstmt.setString(jj++, status);
		}
		if(!dept_id.equals("")){
		    pstmt.setString(jj++, dept_id);
		}
		if(!otherType.equals("")){
		    pstmt.setString(jj++, otherType);
		}
		if(!damage.equals("")){
		    pstmt.setString(jj++, "%"+damage+"%");
										
		}
		if(!whichDate.equals("")){
		    if(!dateFrom.equals("")){
			pstmt.setDate(jj++, new java.sql.Date(dateFormat.parse(dateFrom).getTime()));	
		    }
		    if(!dateTo.equals("")){
			pstmt.setDate(jj++, new java.sql.Date(dateFormat.parse(dateTo).getTime()));	
		    }
		}
		if(!whichAmount.equals("")){
		    if(!amountFrom.equals("")){
			pstmt.setString(jj++, amountFrom);
		    }
		    if(!amountTo.equals("")){
			pstmt.setString(jj++, amountTo);
		    }
		}
		if(!empid.equals("")){
		    pstmt.setString(jj++, empid);
		}
		if(!empName.equals("")){
		    pstmt.setString(jj++, "%"+empName+"%");
		}
		if(!empDept.equals("")){
		    pstmt.setString(jj++, empDept);
		}
		if(!deptPhone.equals("")){
		    pstmt.setString(jj++, deptPhone);
		}
		if(!vin.equals("")){
		    pstmt.setString(jj++, vin);
		}
		if(!autoPlate.equals("")){
		    pstmt.setString(jj++, autoPlate);
		}
		if(!autoNum.equals("")){
		    pstmt.setString(jj++, autoNum);
		}
		if(!autoMake.equals("")){
		    pstmt.setString(jj++, autoMake);
		}
		if(!autoModel.equals("")){
		    pstmt.setString(jj++, autoModel);						
		}
		if(!autoYear.equals("")){
		    pstmt.setString(jj++, autoYear);
		}
		if(!insurance.equals("")){
		    pstmt.setString(jj++, insurance);
		}
		if(!adjuster.equals("")){
		    pstmt.setString(jj++, adjuster);
		}
		if(!adjusterPhone.equals("")){
		    pstmt.setString(jj++, adjusterPhone);
		}
		if(!claimNum.equals("")){
		    pstmt.setString(jj++, claimNum);
		}
		if(!insurStatus.equals("")){
		    pstmt.setString(jj++, insurStatus);						
		}
		if(!policy.equals("")){
		    pstmt.setString(jj++, policy);
		}
	    }
	    rs = pstmt.executeQuery();
	    autoAccidents = new ArrayList<>();
	    while(rs.next()){
		AutoAccident one =
		    new AutoAccident(debug,
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
				     rs.getString(27)
				     );
		autoAccidents.add(one);
	    }
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    msg += ex;
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return msg;
    }
}

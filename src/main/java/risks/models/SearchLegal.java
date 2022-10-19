package risks.models;

import java.util.*;
import java.sql.*;
import javax.sql.*;
import java.io.*;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.utils.Helper;

public class SearchLegal extends Legal{

    static Logger logger = LogManager.getLogger(SearchLegal.class);
    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");			
    String idArr[] = null;
    String dateFrom="", dateTo="";
    String whichDate="", pidSet="";
    String amountFrom="", amountTo="";
    String whichAmount="", orderBy="l.id desc";
    //
    // person related parameters
    String personName="",personPhone="",personDob="";
				
    List<Legal> legals = null;
    Auto auto = null;
    //
    // basic constructor
    public SearchLegal(boolean deb){

	super(deb);
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
    public void setClaimNum(String val){
	claimNum = val;
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
	if(!orderBy.equals(""))
	    orderBy = "l."+orderBy;
    }

    public void setAuto(Auto val){
	auto = val;
    }
    public void setPersonName(String val){
	if(val != null)
	    personName = val;
    }
    public void setPersonPhone(String val){
	if(val != null)
	    personPhone = val;
    }
    public void setPersonDob(String val){
	if(val != null)
	    personDob = val;
    }
    public void setInsurStatus(String val){
	if(val != null)
	    insurStatus = val;
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
    public void setAttorney(String val){
	if(val != null)
	    attorney = val;
    }
    public void setAttorneyPhone(String val){
	if(val != null)
	    attorneyPhone = val;
    }
    public void setDefInsur(String val){
	if(val != null)
	    defInsur = val;
    }
    public void setDefClaimNum(String val){
	if(val != null)
	    defClaimNum = val;
    }
    public void setDefAdjuster(String val){
	if(val != null)
	    defAdjuster = val;
    }
    public void setDefInsurPhone(String val){
	if(val != null)
	    defInsurPhone = val;
    }
    public void setDefAttorney(String val){
	if(val != null)
	    defAttorney = val;
    }
    public void setDefAttorneyPhone(String val){
	if(val != null)
	    defAttorneyPhone = val;
    }
		
    //
    // getters
    //
    public List<Legal> getLegals(){
	return legals;
    }
    public String getAutoInfo(){
	return (vin+" "+autoMake+" "+autoModel+" "+autoYear).trim();
    }
    //
    public String lookFor(){

	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;			
	boolean insurFlag = false, empFlag=false, personFlag=false;
				
	String str="", back = "";
	String qf = " from vslegals l ";
	String qw = "";
	String qq = "select l.id,"+
	    "l.type,l.status,l.caseNum,l.location,"+
	    "l.damageAmnt,l.cityAutoInc,"+ // 7
	    "date_format(l.doi,'%m/%d/%Y'),"+
	    "date_format(l.filed,'%m/%d/%Y'), "+
	    "date_format(l.judgment,'%m/%d/%Y'), "+
	    "date_format(l.proSupp,'%m/%d/%Y'), "+   						
	    "date_format(l.closed,'%m/%d/%Y'), "+  // 12
	    "l.insured,l.description,l.allDocuments,"+ // 15
						
	    "date_format(l.deptRecoverDate,'%m/%d/%Y'), "+
	    "date_format(l.deptCollectDate,'%m/%d/%Y'), "+
	    " l.deptToRisk,"+
	    "date_format(l.deptToRiskDate,'%m/%d/%Y'), "+
	    "date_format(l.riskFirstDate,'%m/%d/%Y'),"+

	    "date_format(l.toProsecutorDate,'%m/%d/%Y'),"+	    
	    "date_format(l.convictionDate,'%m/%d/%Y'),"+
	    "date_format(l.collectDate,'%m/%d/%Y'),"+
	    "l.riskToInsur,"+
	    "date_format(l.riskToInsurDate,'%m/%d/%Y'),"+	    
	    "date_format(l.insurRecoveryDate,'%m/%d/%Y'),"+
	    "date_format(l.insurCollectDate,'%m/%d/%Y'),"+// 27
	    "l.deductible,l.otherDetails,l.otherType, "+ // 30
						
	    "l.paidByCity,l.paidByInsur,l.miscByCity, "+
	    "l.recordOnly, "+
	    "l.paidByRisk, "+
	    "l.paidByDef,l.unableToCollect ";

	if(!id.equals("")){
	    qw += " l.id = ?";//+id;
	}
	else{
	    if(!tortId.equals("")){
		// qw += " l.tortId = ? "+tortId;
	    }
	    if(!type.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " l.type = ? ";// '"+type+"'";
	    }
	    if(!otherType.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " lower(otherType) like ? ";// '%"+otherType.toLowerCase()+"%'";
	    }
	    if(!status.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " l.status = ? ";
	    }
	    if(!claimNum.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " i.claimNum = ? ";
		insurFlag = true;
	    }
	    if(!caseNum.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " l.caseNum = ? ";
	    }
	    if(!adjuster.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " upper(i.adjuster) like ? ";// '%"+Helper.replaceIt(adjuster)+"%'";
		insurFlag = true;
	    }
						
	    if(!adjusterPhone.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " i.adjusterPhone like ? ";// '%"+adjusterPhone+"%'";
		insurFlag = true;
	    }
	    if(!attorney.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " upper(i.attorney) like ? ";// '%"+Helper.replaceIt(attorney)+"%'";
		insurFlag = true;
	    }
	    if(!attorneyPhone.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "i.attorneyPhone like ? ";// '%"+attorneyPhone+"%'";
		insurFlag = true;
	    }
	    if(!empid.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " e.id like ? ";
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
	    if(!dept_id.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " (e.dept_id = ? or dr.dept_id = ? ) ";
		empFlag = true;
	    }
						
	    if(!recordOnly.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " l.recoOnly is not null ";
	    }
	    if(!location.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " l.location like ? ";//'%"+Helper.replaceIt(location)+"%'";
	    }
	    if(!insurStatus.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " i.insurStatus = ? ";
		insurFlag = true;
	    }
	    if(!description.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " l.description like ? ";// '%"+Helper.replaceIt(description)+"%'";
	    }
	    if(!insurance.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " i.company like ? ";// '%"+Helper.replaceIt(insurance)+"%'";
		insurFlag = true;
	    }
	    if(!dateFrom.equals("")){
		if(!whichDate.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += whichDate+" >= ? ";//str_to_date('"+dateFrom+"','%m/%d/%Y')";
		}
	    }
	    if(!dateTo.equals("")){
		if(!whichDate.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += whichDate+" <= ? ";// str_to_date('"+dateTo+"','%m/%d/%Y')";
		}
	    }
	    if(!amountFrom.equals("")){
		if(!whichAmount.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += whichAmount+" >= ? ";// +amountFrom;
		}
	    }
	    if(!amountTo.equals("")){
		if(!whichAmount.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += whichAmount+" <= ? ";// +amountFrom;
		    qw += " and "+whichAmount+" > 0 ";
		}
	    }
	    if(!personName.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "(p.lname like ? or p.fname like ?) ";
		personFlag = true;
	    }
	    if(!personPhone.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "(p.phonew like ? or p.phoneh like ? or p.phonec like ?) ";
		personFlag = true;
	    }
	    if(!personDob.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "p.dob = ? ";
		personFlag = true;
	    }						
	    if(empFlag){
		qf += " join empRelated er on er.risk_id=l.id join employees e on e.id = er.employee_id left join deptRelated dr on dr.related_id=l.id ";
								
	    }
	    if(personFlag){
		qf += " join vslegal_people lp on lp.risk_id = l.id join people p on p.id = lp.person_id ";
	    }
	    if(insurFlag){
		qf += ", insurances i, insuranceRelated ir ";
		if(!qw.equals("")) qw += " and ";
		qw += " l.id = ir.risk_id and i.id=ir.insurance_id ";
	    }
	    if(auto != null){
		qf += ", risk_autos a ";
		if(!qw.equals("")) qw += " and ";
		qw += " a.risk_id=l.id ";
		str = auto.getVin();
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
	}
	qq += qf ;
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
	    return "Could not connect to DB ";
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
		if(!otherType.equals("")){
		    stmt.setString(jj++, "%"+otherType.toLowerCase()+"%");
		}
		if(!status.equals("")){
		    stmt.setString(jj++, status);
		}
		if(!claimNum.equals("")){
		    stmt.setString(jj++, claimNum);
		}
		if(!caseNum.equals("")){
		    stmt.setString(jj++, caseNum);
		}
		if(!adjuster.equals("")){
		    stmt.setString(jj++,"%"+adjuster+"%");
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
		    stmt.setString(jj++, empid);

		}
		if(!empName.equals("")){
		    stmt.setString(jj++, "%"+empName+"%");
		}
		if(!empPhone.equals("")){
		    stmt.setString(jj++, empPhone);
		}
		if(!dept_id.equals("")){
		    stmt.setString(jj++, dept_id);
		    stmt.setString(jj++, dept_id);								
		}
		if(!location.equals("")){
		    stmt.setString(jj++,"%"+location+"%");
		}
		if(!insurStatus.equals("")){
		    stmt.setString(jj++, insurStatus);
		}
		if(!description.equals("")){
		    stmt.setString(jj++,"%"+description+"%");
		}
		if(!insurance.equals("")){
		    stmt.setString(jj++,"%"+insurance+"%");
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
		if(!personName.equals("")){
		    stmt.setString(jj++,personName);
		    stmt.setString(jj++,personName);
		}
		if(!personPhone.equals("")){
		    stmt.setString(jj++,personPhone);
		    stmt.setString(jj++,personPhone);
		    stmt.setString(jj++,personPhone);
		}
		if(!personDob.equals("")){
		    java.util.Date date_tmp = df.parse(personDob);
		    stmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
		}													
		if(auto != null){
		    str = auto.getVin();
		    if(str != null && !str.equals("")){
			stmt.setString(jj++,str);
		    }
		    str = auto.getAutoPlate();
		    if(str != null && !str.equals("")){
			stmt.setString(jj++,str);
		    }
		    str = auto.getAutoMake();
		    if(str != null && !str.equals("")){
			stmt.setString(jj++,str);
		    }
		    str = auto.getAutoModel();
		    if(str != null && !str.equals("")){
			stmt.setString(jj++,str);
		    }
		    str = auto.getAutoYear();
		    if(str != null && !str.equals("")){
			stmt.setString(jj++,str);
		    }
		    str = auto.getAutoNum();
		    if(str != null && !str.equals("")){
			stmt.setString(jj++,str);
		    }
		}
	    }
	    rs = stmt.executeQuery();
	    while(rs.next()){
		if(legals == null)
		    legals = new ArrayList<>();
		Legal one =
		    new Legal(debug,
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
			      rs.getString(37));
		if(!legals.contains(one)){
		    legals.add(one);
		}
	    }
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    back = ex.toString()+":"+qq;
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	return back;
    }

}























































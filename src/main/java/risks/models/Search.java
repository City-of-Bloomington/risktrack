package risks.models;

import java.util.*;
import java.sql.*;
import javax.sql.*;
import java.io.*;
import java.text.SimpleDateFormat;
import org.javatuples.Quintet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.utils.Helper;
    

public class Search{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(Search.class);
    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");			
    String dateFrom="", dateTo="", id="", empid="", dept_id="", empName="";
    String insurance="", adjuster="", attorney="";
    String vin="", autoPlate="", autoMake="",autoModel="", autoYear="",
	autoNum="", type="", status="",claimNum="";
    List<Quintet<String, String, String, String, String>> data = null;
    //
    //
    public Search(boolean deb){
	debug = deb;
	//
    }
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setStatus(String val){
	if(val != null)
	    status = val;
    }
    public void setType(String val){
	if(val != null)
	    type = val;
    }		
    public void setDateFrom(String val){
	if(val != null)
	    dateFrom = val;
    }
    public void setDateTo(String val){
	if(val != null)
	    dateTo = val;
    }
    public void setEmpid(String val){
	if(val != null)
	    empid = val;
    }
    public void setEmpName(String val){
	if(val != null)
	    empName = val;
    }
    public void setDept_id(String val){
	if(val != null)
	    dept_id = val;
    }		
    public void setInsurance(String val){
	if(val != null)
	    insurance = val;
    }
    public void setAdjuster(String val){
	if(val != null)
	    adjuster = val;
    }
    public void setAttorney(String val){
	if(val != null)
	    attorney = val;
    }
    public void setVin(String val){
	if(val != null)
	    vin = val;
    }
    public void setClaimNum(String val){
	if(val != null)
	    claimNum = val;
    }		
    public void setAutoMake(String val){
	if(val != null)
	    autoMake = val;
    }
    public void setAutoModel(String val){
	if(val != null)
	    autoModel = val;
    }
    public void setAutoNum(String val){
	if(val != null)
	    autoNum = val;
    }
    public void setAutoPlate(String val){
	if(val != null)
	    autoPlate = val;
    }
    public void setAutoYear(String val){
	if(val != null)
	    autoYear = val;
    }

		
    public     boolean hasAuto(){
	if(vin.equals("") &&
	   autoMake.equals("") &&
	   autoModel.equals("") &&
	   autoYear.equals("") &&
	   autoPlate.equals("")){
	    return false;
	}
	return true;
    }
		
    //
    // getters
    //
    public List<Quintet<String, String, String, String, String>> getData(){
	return data;
    }
    //
    public String lookFor(){

	Connection con = null;
	PreparedStatement
	    stmt = null,stmt2=null,stmt3=null,
	    stmt4=null,stmt5=null,stmt6=null, stmt7=null;
	ResultSet rs = null;			
	String back = "", qqw="";
	String qw = "", qw2="", qw3="", qw4="",qw5="",qw6="", qw7="";
	boolean empFlag=false, insurFlag=false;
	String qq = "select la.id,tp.typeDesc,la.status,"+
	    "date_format(la.doi,'%m/%d/%Y'),'Legal Action' from vslegals la left join riskUnifiedTypes tp on la.type=tp.type ";
	String qq2 = "select tc.id,tp.typeDesc,tc.status,"+
	    "date_format(tc.received,'%m/%d/%Y'),'Tort Claim' from tortClaims tc  left join riskUnifiedTypes tp on tp.type=tc.type ";
	String qq3 = "select ds.id,tp.type,ds.status,"+
	    "date_format(ds.accidDate,'%m/%d/%Y'),'Disaster' from disasters ds left join disaster_types tp on tp.id=ds.type ";
	String qq4 = "select rs.id,tp.typeDesc,rs.status,"+
	    "date_format(rs.reported,'%m/%d/%Y'),'Internal Accident' from riskSafety rs left join riskUnifiedTypes tp on tp.type=rs.type ";
	String qq5 = "select ma.id,tp.type,ma.status,"+
	    "date_format(ma.reported,'%m/%d/%Y'),'Misc Accident' from misc_accidents ma left join misc_accident_types tp on tp.id=ma.type ";
	String qq6 = "select wc.id,'Worker Comp',wc.status,"+
	    "date_format(wc.accidentDate,'%m/%d/%Y'),'Worker Comp' from workerComps wc ";
	String qq7 = "select aa.id,'Auto Accident',aa.status,"+
	    "date_format(aa.reported,'%m/%d/%Y'),'Auto Accident' from auto_accidents aa ";				
	if(!id.equals("")){
	    qw  += " la.id = ?";
	    qw2 += " tc.id = ? ";
	    qw3 += " ds.id = ? ";
	    qw4 += " rs.id = ? ";
	    qw5 += " ma.id = ? ";
	    qw6 += " wc.id = ? ";
	    qw7 += " aa.id = ? ";						
	}
	else{
	    if(!status.equals("")){
		if(!qw.equals("")){
		    qw += " and ";
		    qw2 += " and ";
		    qw3 += " and ";
		    qw4 += " and ";
		    qw5 += " and ";
		    qw6 += " and ";
		    qw7 += " and ";										
		}
		qw += " la.status like ? ";
		qw2 += " tc.status like ? ";
		qw3 += " ds.status like ? ";
		qw4 += " rs.status like ? ";
		qw5 += " ma.status like ? ";
		qw6 += " wc.status like ? ";
		qw7 += " aa.status like ? ";								
	    }
	    if(!adjuster.equals("")){
		if(!qw.equals("")){
		    qw += " and ";
		    qw2 += " and ";
		    qw3 += " and ";
		    qw4 += " and ";
		    qw5 += " and ";
		    qw6 += " and ";
		    qw7 += " and ";										
		}
		qw += " upper(i.adjuster) like ? ";
		qw2 += " upper(i.adjuster) like ? ";
		qw3 += " upper(i.adjuster) like ? ";
		qw4 += " upper(i.adjuster) like ? ";
		qw5 += " upper(i.adjuster) like ? ";
		qw6 += " upper(i.adjuster) like ? ";
		qw7 += " upper(i.adjuster) like ? ";								
		insurFlag = true;
	    }
						
	    if(!attorney.equals("")){
		if(!qw.equals("")){
		    qw += " and ";
		    qw2 += " and ";
		    qw3 += " and ";
		    qw4 += " and ";
		    qw5 += " and ";										
		    qw6 += " and ";
		    qw7 += " and ";										
		}
		qw += " upper(i.attorney) like ? ";
		qw2 += " upper(i.attorney) like ? ";
		qw3 += " upper(i.attorney) like ? ";
		qw4 += " upper(i.attorney) like ? ";
		qw5 += " upper(i.attorney) like ? ";
		qw6 += " upper(i.attorney) like ? ";
		qw7 += " upper(i.attorney) like ? ";								
		insurFlag = true;
	    }
	    if(!insurance.equals("")){
		if(!qw.equals("")){
		    qw += " and ";
		    qw2 += " and ";
		    qw3 += " and ";
		    qw4 += " and ";
		    qw5 += " and ";										
		    qw6 += " and ";
		    qw7 += " and ";										
		}
		qw += " i.company like ? ";
		qw2 += " i.company like ? ";
		qw3 += " i.company like ? ";
		qw4 += " i.company like ? ";
		qw5 += " i.company like ? ";
		qw6 += " i.company like ? ";
		qw7 += " i.company like ? ";								
		insurFlag = true;
	    }
	    if(!claimNum.equals("")){
		if(!qw.equals("")){
		    qw += " and ";
		    qw2 += " and ";
		    qw3 += " and ";
		    qw4 += " and ";
		    qw5 += " and ";										
		    qw6 += " and ";
		    qw7 += " and ";										
		}
		qw += " i.claimNum like ? ";
		qw2 += " i.claimNum like ? ";
		qw3 += " i.claimNum like ? ";
		qw4 += " i.claimNum like ? ";
		qw5 += " i.claimNum like ? ";
		qw6 += " i.claimNum like ? ";
		qw7 += " i.claimNum like ? ";								
		insurFlag = true;
	    }						
	    if(!dateFrom.equals("")){
		if(!qw.equals("")){
		    qw += " and ";
		    qw2 += " and ";
		    qw3 += " and ";
		    qw4 += " and ";
		    qw5 += " and ";										
		    qw6 += " and ";
		    qw7 += " and ";	
		}
		qw += "la.doi >= ? ";
		qw2 += "tc.received >= ? ";
		qw3 += "ds.accidDate >= ? ";
		qw4 += "rs.reported >= ? ";
		qw5 += "ma.reported >= ? ";
		qw6 += "wc.accidentDate >= ? ";
		qw7 += "aa.reported >= ? ";								
	    }
	    if(!dateTo.equals("")){
		if(!qw.equals("")){
		    qw += " and ";
		    qw2 += " and ";
		    qw3 += " and ";
		    qw4 += " and ";
		    qw5 += " and ";										
		    qw6 += " and ";
		    qw7 += " and ";	
		}
		qw += "la.doi <= ? ";
		qw2 += "tc.received <= ? ";
		qw3 += "ds.accidDate <= ? ";
		qw4 += "rs.reported <= ? ";
		qw5 += "ma.reported <= ? ";
		qw6 += "wc.accidentDate <= ? ";
		qw7 += "aa.reported <= ? ";
	    }
	    if(!empid.equals("")){
		if(!qw.equals("")){
		    qw += " and ";
		    qw2 += " and ";
		    qw3 += " and ";
		    qw4 += " and ";
		    qw5 += " and ";										
		    qw6 += " and ";
		    qw7 += " and ";											
		}
		qw += " e.id = ? ";
		qw2 += " e.id = ? ";
		qw3 += " e.id = ? ";
		qw4 += " e.id = ? ";
		qw5 += " e.id = ? ";
		qw6 += " e.id = ? ";
		qw7 += " e.id = ? ";								
		empFlag = true;
	    }
	    if(!empName.equals("")){
		if(!qw.equals("")){
		    qw += " and ";
		    qw2 += " and ";
		    qw3 += " and ";
		    qw4 += " and ";
		    qw5 += " and ";										
		    qw6 += " and ";
		    qw7 += " and ";
		}								
		qw += " e.name like ? ";
		qw2 += " e.name like ? ";
		qw3 += " e.name like ? ";
		qw4 += " e.name like ? ";
		qw5 += " e.name like ? ";
		qw6 += " e.name like ? ";
		qw7 += " e.name like ? ";								
		empFlag = true;
	    }
	    if(!dept_id.equals("")){
		if(!qw.equals("")){
		    qw += " and ";
		    qw2 += " and ";
		    qw3 += " and ";
		    qw4 += " and ";
		    qw5 += " and ";										
		    qw6 += " and ";
		    qw7 += " and ";										
		}					
		qw += " (e.dept_id = ? or dr.dept_id = ?) ";
		qw2 += " (e.dept_id = ? or dr.dept_id = ?) ";
		qw3 += " (e.dept_id = ? or dr.dept_id = ?) ";
		qw4 += " (e.dept_id = ? or dr.dept_id = ?) ";
		qw5 += " (e.dept_id = ? or dr.dept_id = ?) ";
		qw6 += " (e.dept_id = ? or dr.dept_id = ?) ";
		qw7 += " (e.dept_id = ? or dr.dept_id = ?) ";								
		empFlag = true;
	    }
	    if(empFlag){
		qq += " join empRelated er on er.risk_id=la.id join employees e on e.id = er.employee_id left join deptRelated dr on dr.related_id=la.id ";
		qq2 += " join empRelated er on er.risk_id=tc.id join employees e on e.id = er.employee_id left join deptRelated dr on dr.related_id=tc.id ";
		qq3 += " join empRelated er on er.risk_id=ds.id join employees e on e.id = er.employee_id left join deptRelated dr on dr.related_id=ds.id ";
		qq4 += " join empRelated er on er.risk_id=rs.id join employees e on e.id = er.employee_id left join deptRelated dr on dr.related_id=rs.id ";
		qq5 += " join empRelated er on er.risk_id=ma.id join employees e on e.id = er.employee_id left join deptRelated dr on dr.related_id=ma.id ";
		qq6 += " join empRelated er on er.risk_id=wc.id join employees e on e.id = er.employee_id left join deptRelated dr on dr.related_id=wc.id ";
		qq7 += " join empRelated er on er.risk_id=aa.id join employees e on e.id = er.employee_id left join deptRelated dr on dr.related_id=aa.id ";
	    }
	    if(insurFlag){
		qq += "join insuranceRelated ir on ir.risk_id=la.id join insurances i on i.id=ir.insurance_id ";
		qq2 += "join insuranceRelated ir on ir.risk_id=tc.id join insurances i on i.id=ir.insurance_id ";
		qq3 += "join insuranceRelated ir on ir.risk_id=ds.id join insurances i on i.id=ir.insurance_id ";
		qq4 += "join insuranceRelated ir on ir.risk_id=rs.id join insurances i on i.id=ir.insurance_id ";
		qq5 += "join insuranceRelated ir on ir.risk_id=ma.id join insurances i on i.id=ir.insurance_id ";
		qq6 += "join insuranceRelated ir on ir.risk_id=wc.id join insurances i on i.id=ir.insurance_id ";
		qq7 += "join insuranceRelated ir on ir.risk_id=aa.id join insurances i on i.id=ir.insurance_id ";								
	    }
	    if(hasAuto()){
		qq += " join risk_autos a on a.risk_id=la.id ";
		qq2 += " join risk_autos a on a.risk_id=tc.id ";								
		qq3 += " join risk_autos a on a.risk_id=ds.id ";
		qq4 += " join risk_autos a on a.risk_id=rs.id ";
		qq5 += " join risk_autos a on a.risk_id=ma.id ";
		qq6 += " join risk_autos a on a.risk_id=wc.id ";
		qq7 += " join risk_autos a on a.risk_id=aa.id ";								
		if(!vin.equals("")){
		    if(!qw.equals("")){
			qw += " and ";
			qw2 += " and ";
			qw3 += " and ";
			qw4 += " and ";
			qw5 += " and ";
			qw6 += " and ";
			qw7 += " and ";												
		    }
		    qw += "a.vin like ? ";
		    qw2 += "a.vin like ? ";
		    qw3 += "a.vin like ? ";
		    qw4 += "a.vin like ? ";
		    qw5 += "a.vin like ? ";
		    qw6 += "a.vin like ? ";
		    qw7 += "a.vin like ? ";										
		}
		if(!autoPlate.equals("")){
		    if(!qw.equals("")){
			qw += " and ";
			qw2 += " and ";
			qw3 += " and ";
			qw4 += " and ";
			qw5 += " and ";
			qw6 += " and ";
			qw7 += " and ";														
		    }
		    qw += "a.autoPlate like ? ";
		    qw2 += "a.autoPlate like ? ";
		    qw3 += "a.autoPlate like ? ";
		    qw4 += "a.autoPlate like ? ";
		    qw5 += "a.autoPlate like ? ";
		    qw6 += "a.autoPlate like ? ";
		    qw7 += "a.autoPlate like ? ";										
		}
		if(!autoMake.equals("")){
		    if(!qw.equals("")){
			qw += " and ";
			qw2 += " and ";
			qw3 += " and ";
			qw4 += " and ";
			qw5 += " and ";
			qw6 += " and ";
			qw7 += " and ";												
		    }
		    qw += "a.autoMake like ? ";
		    qw2 += "a.autoMake like ? ";
		    qw3 += "a.autoMake like ? ";
		    qw4 += "a.autoMake like ? ";
		    qw5 += "a.autoMake like ? ";
		    qw6 += "a.autoMake like ? ";
		    qw7 += "a.autoMake like ? ";										
		}
		if(!autoModel.equals("")){
		    if(!qw.equals("")){
			qw += " and ";
			qw2 += " and ";
			qw3 += " and ";
			qw4 += " and ";
			qw5 += " and ";
			qw6 += " and ";
			qw7 += " and ";												
		    }
		    qw += "a.autoModel like ? ";
		    qw2 += "a.autoModel like ? ";
		    qw3 += "a.autoModel like ? ";
		    qw4 += "a.autoModel like ? ";
		    qw5 += "a.autoModel like ? ";
		    qw6 += "a.autoModel like ? ";
		    qw7 += "a.autoModel like ? ";										
		}
		if(!autoYear.equals("")){
		    if(!qw.equals("")){
			qw += " and ";
			qw2 += " and ";
			qw3 += " and ";
			qw4 += " and ";
			qw5 += " and ";
			qw6 += " and ";
			qw7 += " and ";												
		    }
		    qw += "a.autoYear like ? ";
		    qw2 += "a.autoYear like ? ";
		    qw3 += "a.autoYear like ? ";
		    qw4 += "a.autoYear like ? ";
		    qw5 += "a.autoYear like ? ";
		    qw6 += "a.autoYear like ? ";
		    qw7 += "a.autoYear like ? ";
		}
		if(!autoNum.equals("")){
		    if(!qw.equals("")){
			qw += " and ";
			qw2 += " and ";
			qw3 += " and ";
			qw4 += " and ";
			qw5 += " and ";
			qw6 += " and ";
			qw7 += " and ";
		    }
		    qw += "a.autoNum like ? ";
		    qw2 += "a.autoNum like ? ";
		    qw3 += "a.autoNum like ? ";
		    qw4 += "a.autoNum like ? ";
		    qw5 += "a.autoNum like ? ";
		    qw6 += "a.autoNum like ? ";
		    qw7 += "a.autoNum like ? ";										
		}
	    }
	    if(!type.equals("")){
		if(!qw.equals("")){
		    qw += " and ";
		    qw2 += " and ";
		    qw4 += " and ";
		}
		qw += " la.type = ? ";
		qw2 += " tc.type = ? ";
		qw4 += " rs.type = ? ";
		//
	    }
	}
	if(!qw5.equals("")){
	    qq5 += " where "+qw5;				
	}				
	if(!qw.equals("")){
	    qq += " where "+qw;
	    qq2 += " where "+qw2;
	    qq3 += " where "+qw3;
	    qq4 += " where "+qw4;
	    qq6 += " where "+qw6;
	    qq7 += " where "+qw7;						
	}
	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    return "Could not connect to DB ";
	}				
	try{
	    qqw = qq;
	    stmt = con.prepareStatement(qq);
	    qqw = qq2;
	    stmt2 = con.prepareStatement(qq2);
	    qqw = qq3;
	    stmt3 = con.prepareStatement(qq3);
	    qqw = qq4;
	    stmt4 = con.prepareStatement(qq4);
	    qqw = qq5;
	    stmt5 = con.prepareStatement(qq5);
	    qqw = qq6;
	    stmt6 = con.prepareStatement(qq6);
	    qqw = qq7;
	    stmt7 = con.prepareStatement(qq7);						
	    int jj=1;						
	    if(!id.equals("")){
		stmt.setString(jj, id);
		stmt2.setString(jj, id);
		stmt3.setString(jj, id);
		stmt4.setString(jj, id);
		stmt5.setString(jj, id);
		stmt6.setString(jj, id);
		stmt7.setString(jj, id);
	    }
	    else{
		if(!status.equals("")){
		    stmt.setString(jj, status);
		    stmt2.setString(jj, status);
		    stmt3.setString(jj, status);
		    stmt4.setString(jj, status);
		    stmt5.setString(jj, status);
		    stmt6.setString(jj, status);
		    stmt7.setString(jj, status);										
		    jj++;
		}
		if(!adjuster.equals("")){
		    stmt.setString(jj,"%"+adjuster+"%");
		    stmt2.setString(jj,"%"+adjuster+"%");
		    stmt3.setString(jj,"%"+adjuster+"%");
		    stmt4.setString(jj,"%"+adjuster+"%");
		    stmt5.setString(jj,"%"+adjuster+"%");
		    stmt6.setString(jj,"%"+adjuster+"%");
		    stmt7.setString(jj,"%"+adjuster+"%");										
		    jj++;
		}
		if(!attorney.equals("")){
		    stmt.setString(jj,"%"+attorney+"%");								
		    stmt2.setString(jj,"%"+attorney+"%");
		    stmt3.setString(jj,"%"+attorney+"%");
		    stmt4.setString(jj,"%"+attorney+"%");
		    stmt5.setString(jj,"%"+attorney+"%");
		    stmt6.setString(jj,"%"+attorney+"%");
		    stmt7.setString(jj,"%"+attorney+"%");										
		    jj++;
		}
		if(!insurance.equals("")){
		    stmt.setString(jj,"%"+insurance+"%");
		    stmt2.setString(jj,"%"+insurance+"%");
		    stmt3.setString(jj,"%"+insurance+"%");
		    stmt4.setString(jj,"%"+insurance+"%");
		    stmt5.setString(jj,"%"+insurance+"%");
		    stmt6.setString(jj,"%"+insurance+"%");
		    stmt7.setString(jj,"%"+insurance+"%");										
		    jj++;
		}
		if(!claimNum.equals("")){
		    stmt.setString(jj,claimNum+"%");
		    stmt2.setString(jj,claimNum+"%");
		    stmt3.setString(jj,claimNum+"%");
		    stmt4.setString(jj,claimNum+"%");
		    stmt5.setString(jj,claimNum+"%");
		    stmt6.setString(jj,claimNum+"%");
		    stmt7.setString(jj,claimNum+"%");										
		    jj++;
		}								
		if(!dateFrom.equals("")){
		    java.util.Date date_tmp = df.parse(dateFrom);
		    stmt.setDate(jj, new java.sql.Date(date_tmp.getTime()));
		    stmt2.setDate(jj, new java.sql.Date(date_tmp.getTime()));
		    stmt3.setDate(jj, new java.sql.Date(date_tmp.getTime()));
		    stmt4.setDate(jj, new java.sql.Date(date_tmp.getTime()));
		    stmt5.setDate(jj, new java.sql.Date(date_tmp.getTime()));
		    stmt6.setDate(jj, new java.sql.Date(date_tmp.getTime()));
		    stmt7.setDate(jj, new java.sql.Date(date_tmp.getTime()));
		    jj++;
		}
		if(!dateTo.equals("")){
		    java.util.Date date_tmp = df.parse(dateTo);
		    stmt.setDate(jj, new java.sql.Date(date_tmp.getTime()));
		    stmt2.setDate(jj, new java.sql.Date(date_tmp.getTime()));
		    stmt3.setDate(jj, new java.sql.Date(date_tmp.getTime()));
		    stmt4.setDate(jj, new java.sql.Date(date_tmp.getTime()));
		    stmt5.setDate(jj, new java.sql.Date(date_tmp.getTime()));
		    stmt6.setDate(jj, new java.sql.Date(date_tmp.getTime()));
		    stmt7.setDate(jj, new java.sql.Date(date_tmp.getTime()));
		    jj++;
		}
		if(!empid.equals("")){
		    stmt.setString(jj, empid);
		    stmt2.setString(jj, empid);
		    stmt3.setString(jj, empid);
		    stmt4.setString(jj, empid);
		    stmt5.setString(jj, empid);
		    stmt6.setString(jj, empid);
		    stmt7.setString(jj, empid);										
		    jj++;
		}
		if(!empName.equals("")){
		    stmt.setString(jj, "%"+empName+"%");
		    stmt2.setString(jj, "%"+empName+"%");
		    stmt3.setString(jj, "%"+empName+"%");
		    stmt4.setString(jj, "%"+empName+"%");
		    stmt5.setString(jj, "%"+empName+"%");
		    stmt6.setString(jj, "%"+empName+"%");
		    stmt7.setString(jj, "%"+empName+"%");										
		    jj++;
		}
		if(!dept_id.equals("")){
		    stmt.setString(jj, dept_id);
		    stmt2.setString(jj, dept_id);
		    stmt3.setString(jj, dept_id);
		    stmt4.setString(jj, dept_id);
		    stmt5.setString(jj, dept_id);
		    stmt6.setString(jj, dept_id);
		    stmt7.setString(jj, dept_id);										
		    jj++;
		    stmt.setString(jj, dept_id);
		    stmt2.setString(jj, dept_id);
		    stmt3.setString(jj, dept_id);
		    stmt4.setString(jj, dept_id);
		    stmt5.setString(jj, dept_id);
		    stmt6.setString(jj, dept_id);
		    stmt7.setString(jj, dept_id);
		    jj++;
		}
		if(!vin.equals("")){
		    stmt.setString(jj,vin);
		    stmt2.setString(jj,vin);
		    stmt3.setString(jj,vin);
		    stmt4.setString(jj,vin);
		    stmt5.setString(jj,vin);
		    stmt6.setString(jj,vin);
		    stmt7.setString(jj,vin);										
		    jj++;
		}
		if(!autoPlate.equals("")){
		    stmt.setString(jj,autoPlate);
		    stmt2.setString(jj,autoPlate);
		    stmt3.setString(jj,autoPlate);
		    stmt4.setString(jj,autoPlate);
		    stmt5.setString(jj,autoPlate);
		    stmt6.setString(jj,autoPlate);
		    stmt7.setString(jj,autoPlate);										
		    jj++;
		}
		if(!autoMake.equals("")){
		    stmt.setString(jj,autoMake);
		    stmt2.setString(jj,autoMake);
		    stmt3.setString(jj,autoMake);
		    stmt4.setString(jj,autoMake);
		    stmt5.setString(jj,autoMake);
		    stmt6.setString(jj,autoMake);
		    stmt7.setString(jj,autoMake);										
		    jj++;
		}
		if(!autoModel.equals("")){
		    stmt.setString(jj,autoModel);
		    stmt2.setString(jj,autoModel);
		    stmt3.setString(jj,autoModel);
		    stmt4.setString(jj,autoModel);
		    stmt5.setString(jj,autoModel);
		    stmt6.setString(jj,autoModel);
		    stmt7.setString(jj,autoModel);										
		    jj++;
		}
		if(!autoYear.equals("")){
		    stmt.setString(jj,autoYear);
		    stmt2.setString(jj,autoYear);
		    stmt3.setString(jj,autoYear);
		    stmt4.setString(jj,autoYear);
		    stmt5.setString(jj,autoYear);
		    stmt6.setString(jj,autoYear);
		    stmt7.setString(jj,autoYear);										
		    jj++;
		}
		if(!autoNum.equals("")){
		    stmt.setString(jj,autoNum);
		    stmt2.setString(jj,autoNum);
		    stmt3.setString(jj,autoNum);
		    stmt4.setString(jj,autoNum);
		    stmt5.setString(jj,autoNum);
		    stmt6.setString(jj,autoNum);
		    stmt7.setString(jj,autoNum);										
		    jj++;
		}
		if(!type.equals("")){
		    stmt.setString(jj,type);
		    stmt2.setString(jj,type);
		    stmt4.setString(jj,type);
		}
	    }
	    qqw = qq;
	    rs = stmt.executeQuery();
	    addToList(rs);
	    qqw = qq2;
	    rs = stmt2.executeQuery();
	    addToList(rs);
	    if(type.equals("")){
		qqw = qq3;
		rs = stmt3.executeQuery();						
		addToList(rs);
	    }
	    qqw = qq4;						
	    rs = stmt4.executeQuery();
	    addToList(rs);
	    if(type.equals("")){
		qqw = qq5;
		rs = stmt5.executeQuery();
		addToList(rs);
		qqw = qq6;
		rs = stmt6.executeQuery();
		addToList(rs);
	    }
	    qqw = qq7;						
	    rs = stmt7.executeQuery();
	    addToList(rs);						
	}
	catch(Exception ex){
	    logger.error(ex+":"+qqw);
	    back = ex.toString()+":"+qqw;
	}
	finally{
	    Helper.databaseDisconnect(con, rs, stmt, stmt2, stmt3, stmt4, stmt5, stmt6, stmt7);
	}
	return back;
    }
   String addToList(ResultSet rs){
	String back = "";
	if(rs == null) return back;
	try{
	    while(rs.next()){
		String str = rs.getString(1);
		String str2 = rs.getString(2);
		String str3 = rs.getString(3);
		String str4 = rs.getString(4);
		if(str4 == null) str4 = "";
		String str5 = rs.getString(5);
		if(data == null){
		    data = new ArrayList<>();
		}
		Quintet<String, String, String, String, String>
		    qt = Quintet.with(str, str2, str3, str4, str5);
		data.add(qt);
	    }
	}catch(Exception ex){
	    back += ex;
	}
	return back;
    }

}























































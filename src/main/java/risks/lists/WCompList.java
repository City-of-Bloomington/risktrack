package risks.lists;

import java.util.*;
import java.sql.*;
import java.io.*;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.utils.Helper;
import risks.models.*;


public class WCompList extends WorkComp{

    // 
    // search related variables
    //
    static SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");	
    static Logger logger = LogManager.getLogger(WCompList.class);
    static boolean debug = false;
    String idArr[] = null; // needed for search purpose
    String whichDate = "", dateFrom="", dateTo="", id="";
    String whichAmount = "", amntFrom="", amntTo="";
    List<WorkComp> comps = null;
    //
    // basic constructor
    //
    public WCompList(boolean deb){

	super(deb);
	debug = deb;
    }
    //
    //
    // these are needed for search purpose
    //
    @Override
    public void setId(String val){
	if(val != null)
	    id = val;
    }        
    public void setWhichDate(String val){
	whichDate = val;
    }    
    public void setDateFrom(String val){
	dateFrom = val;
    }
    public void setDateTo(String val){
	dateTo = val;
    }

    public void setWhichAmount(String val){
	whichAmount = val;
    }
    public void setAmntFrom(String val){
	amntFrom = val;
    }
    public void setAmntTo(String val){
	amntTo = val;
    }

    //
    // getters
    //
    public List<WorkComp> getComps(){
	return comps;
    }
    public boolean hasComps(){
	return comps != null && comps.size() > 0;
    }
    //
    // set the some of the available values
    // and do search
    //
    /*
      select w.id,w.status,w.empPhone,date_format(w.accidentDate,'%m/%d/%Y'),w.injuryType,w.compensable,w.timeOffWork,w.payTtd,w.payPpi,w.payMed,w.mmi, w.ableBackWork,w.disputeAmount,w.disputeReason,w.disputeTypeTtd,w.disputeTypePpi,w.disputeTypeMed, date_format(w.back2WorkDate,'%m/%d/%Y') from workerComps w;

    */
    public String lookFor(){
	//
	boolean empFlag = false;
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;
	String back = "";
	String qq = "select "+
	    "w.id,"+
	    "w.status,"+
	    "w.empPhone,"+
	    "date_format(w.accidentDate,'%m/%d/%Y'),"+						
	    "w.injuryType,"+
						
	    "w.compensable,"+
	    "w.timeOffWork,"+
	    "w.payTtd,"+
	    "w.payPpi,"+
	    "w.payMed,"+
						
	    "w.mmi, "+
	    "w.ableBackWork,"+
	    "w.disputeAmount,"+
	    "w.disputeReason,"+
	    "w.disputeTypeTtd,"+
						
	    "w.disputeTypePpi,"+
	    "w.disputeTypeMed, "+						
	    "date_format(w.back2WorkDate,'%m/%d/%Y'),"+
	    "e.name ";
	//
	String qf = " from workerComps w ";
	qf += " left join empRelated er on er.risk_id=w.id ";
	qf += " left join employees e on e.id=er.employee_id ";
	String qw = "";
	if(!id.equals("")){
	    qw += " w.id = ? ";
	}
	else{
	    if(!status.equals("")){
		qw += " w.status = ? ";
	    }
	    if(!empid.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " e.userid = ? "; // empid
	    }
	    if(!empName.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " e.name like ? ";
	    }
	    if(!empPhone.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " e.phone like ? ";
	    }
	    if(!(dept_id.equals("") || dept_id.equals("0"))){
		if(!qw.equals("")) qw += " and ";
		qw += " e.dept_id = ? ";
	    }
	    if(!injuryType.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " w.injuryType like ? ";// '%"+injuryType+"%'";
	    }
	    if(!compensable.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " w.compensable = ? ";
	    }
	    if(!ableBackWork.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " w.ableBackWork = ? ";
	    }
	    if(!mmi.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " w.mmi is not null ";
	    }
	    if(!disputeTypeTtd.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " w.disputeTypeTtd is not null ";
	    }
	    if(!disputeTypePpi.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " w.disputeTypePpi is not null ";
	    }
	    if(!disputeTypeMed.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " w.disputeTypeMed is not null ";
	    }
	    if(!whichAmount.equals("")){
		if(!amntFrom.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += whichAmount+" >= ? ";
		}
		if(!amntTo.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += whichAmount+" <= ? ";
		}
	    }
	    if(!whichDate.equals("")){
		if(!dateFrom.equals("")){
		    if(!qw.equals("")) qw += " and ";
		    qw += whichDate+" >=  ? ";
		}
		if(!dateTo.equals("")){
		    if(!qw.equals("")) qw += " and ";		    
		    qw += whichDate+" <= ? ";
		} 
	    }
	}
	qq += qf;
	if(!qw.equals("")){
	    qq += " where "+qw;
	}
	qq += " order by w.id desc ";
	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB ";
	    logger.error(back);
	    return back;
	}				
	try{
	    stmt = con.prepareStatement(qq);
	    if(!id.equals("")){
		stmt.setString(1, id);
	    }
	    else{
		int jj=1;
		if(!status.isEmpty()){
		    stmt.setString(jj++, status);
		}
		if(!empid.isEmpty()){
		    stmt.setString(jj++, empid);
		}
		if(!empName.isEmpty()){
		    stmt.setString(jj++, "%"+empName+"%");
		}
		if(!empPhone.isEmpty()){
		    stmt.setString(jj++, "%"+empPhone+"%");
		}
		if(!(dept_id.isEmpty() || dept_id.equals("0"))){
		    stmt.setString(jj++, dept_id);
		}
		if(!injuryType.isEmpty()){
		    stmt.setString(jj++, "%"+injuryType+"%");										
		}
		if(!compensable.isEmpty()){
		    stmt.setString(jj++, compensable);
		}
		if(!ableBackWork.isEmpty()){
		    stmt.setString(jj++, ableBackWork);
		}
		if(!whichAmount.isEmpty()){
		    if(!amntFrom.isEmpty()){
			stmt.setString(jj++, amntFrom);
		    }
		    if(!amntTo.isEmpty()){
			stmt.setString(jj++, amntTo);
		    }
		}
		if(!whichDate.isEmpty()){
		    if(!dateFrom.isEmpty()){
			java.util.Date date_tmp = df.parse(dateFrom);
			stmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
		    }
		    if(!dateTo.isEmpty()){
			java.util.Date date_tmp = df.parse(dateTo);
			stmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
		    } 
		}
	    }
	    rs = stmt.executeQuery();
	    while(rs.next()){
		if(comps == null) comps = new ArrayList<>();
		WorkComp one =
		    new WorkComp(debug,
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
		one.setEmpName(rs.getString(19));
		if(!comps.contains(one)){
		    comps.add(one);
		}
	    }
	}
	catch(Exception ex){
	    back =  ex+": "+qq;						
	    logger.error(qq);
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	return back;
    }

}























































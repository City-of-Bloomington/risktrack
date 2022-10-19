package risks.models;

import java.util.*;
import java.sql.*;
import java.io.*;
import javax.sql.*;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.utils.Helper;


public class SearchPerson extends RiskPerson{

    // 
    // search related variables
    //
    static Logger logger = LogManager.getLogger(SearchPerson.class);
    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");	
    String phone = "", updateFrom="", updateTo="", sortBy="";
    String risk_id="", name=""; // tort, vslegal, etc
    String[] lnameArr = null; // could be used for search multiple
    String[] fnameArr = null; // =  =
    boolean claimPersonOnly=false,
	vsLegalPersonOnly=false,
	witnessPersonOnly=false,
	claimAutoOnly=false;
		
    List<RiskPerson> persons = null;
    //
    // basic constructor
    //
    public SearchPerson(boolean deb){

	super(deb);
    }
    //
    //
    // these are needed for search purpose
    //
    public void setAnyPhone(String val){
	if(val != null)
	    phone = val;
    }
    public void setName(String val){
	if(val != null)
	    lname = val;
    }
    public void setLnameArr(String[] val){
	if(val != null)
	    lnameArr = val;
    }
    public void setFnameArr(String[] val){
	if(val != null)
	    fnameArr = val;
    }
    public void setUpdateFrom(String val){
	if(val != null)
	    updateFrom = val;
    }
    public void setUpdateTo(String val){
	if(val != null)
	    updateTo = val;
    }
    public void setSortBy(String val){
	if(val != null)
	    sortBy = val;
    }
    public void setRisk_id(String val){
	if(val != null)
	    risk_id = val;
    }		
    public void setClaimPersonOnly(){
	claimPersonOnly = true;
    }
    public void setClaimAutoOnly(){
	claimAutoOnly = true;
    }		
    public void setVslegalPersonOnly(){
	vsLegalPersonOnly = true;
    }
    public void setWitnessPersonOnly(){
	witnessPersonOnly = true;
    }
    public List<RiskPerson> getPersons(){
	return persons;
    }
    //
    // set some of the available values
    // and do search
    public String lookFor(){
	//
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;
	String back = "";
	boolean andFlag = false;

	String qf = " from people p ";
	String qw = "", qo="";
	String qq = "select p.id,"+
	    "p.fname,p.lname,"+
	    "p.streetNum,p.streetDir,p.streetName,p.streetType,"+
	    "p.sudType,p.sudNum,"+
	    "p.city,p.state,p.zip,"+
	    "p.phonew,p.phoneh,p.phonec, "+
	    "date_format(p.dob,'%m/%d/%Y'),"+
	    "p.ssn, "+
	    "date_format(p.addrUpdate,'%m/%d/%Y'), "+
	    " p.mi, p.email, p.nameTitle, p.contact, p.juvenile ";
				
	if(!id.equals("")){
	    qw += " p.id = ?";
	}
	else{
	    if(lnameArr != null){
		String all = "(";
		for (int i=0;i<lnameArr.length;i++){
		    if(i > 0) all += " or ";
		    all += " p.lname like ?  or p.fname like ? ";
		}
		all += ")";
		qw += all;
	    }
	    if(!lname.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " (p.lname like ? or p.fname like ?)";
	    }
	    if(!contact.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " upper(p.contact) like ? ";

	    }
	    if(!dob.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " p.dob = ? ";
	    }
	    if(!addrUpdate.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " p.addrUpdate = ? ";
	    }
	    if(!updateFrom.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " p.addrUpdate >= ? ";
	
	    }
	    if(!updateTo.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " p.addrUpdate <= ? ";
	
	    }
	    if(!ssn.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " p.ssn like ? ";
	    }
	    if(!streetNum.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " p.streetNum = ? ";
	    }
	    if(!streetDir.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " p.streetDir = ? ";
	    }
	    if(!streetName.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " upper(p.streetName) like ? ";
	    }
	    if(!streetType.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " p.streetType = ? ";
	    }
	    if(!sudType.equals("")){
		if(!qw.equals("")) qw += " and ";
		if(andFlag) qw += " and ";
		qw += " p.sudType = ? ";
	    }
	    if(!sudNum.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " p.sudNum = ? ";
	    }
	    if(!city.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " p.city = ? ";
	    }
	    if(!state.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " p.state = ? ";
	    }
	    if(!zip.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += " p.zip = ? ";
	    }
	    if(!phone.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "( p.phoneh like ?  or "+
		    " p.phonew like ?  or "+
		    " p.phonec like ? )";

	    }
	    if(!risk_id.equals("")){
		if(!qw.equals("")) qw += " and ";
		qw += "p.id in ("+
		    "select c.person_id from claim_people c where c.risk_id = ?  union "+
		    "select l.person_id from vslegal_people l where l.risk_id = ?  union "+
		    "select w.person_id from witness_people w where w.risk_id = ? union "+
		    "select w.person_id from claim_auto_people w where w.risk_id = ? )";
	    }
	    if(claimPersonOnly){
		qf += " join claim_people c on c.person_id=p.id ";
	    }
	    else if(claimAutoOnly){
		qf += " join claim_auto_people c on c.person_id=p.id ";
	    }						
	    else if(vsLegalPersonOnly){
		qf += " join vslegal_people l on l.person_id=p.id ";
	    }
	    else if(witnessPersonOnly){
		qf += " join witness_people w on w.person_id=p.id ";
	    }
	}
	String str="";
	qq += qf;
	if(!qw.equals("")){
	    qq += " where "+qw;
	}
	if(!sortBy.equals("")){
	    if(sortBy.startsWith("lname"))
		qo = " order by lname,fname ";
	    else if(sortBy.startsWith("fname"))
		qo = " order by fname,lname ";
	    else if(sortBy.startsWith("addr"))
		qo = " order by streetName,streetDir,streetType,streetNum ";
	    else if(sortBy.startsWith("last"))
		qo = " order by addrUpdate ";
	}
	logger.debug(qq);
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
		if(lnameArr != null){
		    for (int i=0;i<lnameArr.length;i++){
			stmt.setString(jj++, lnameArr[i]);
			stmt.setString(jj++, lnameArr[i]);
		    }
		}
		if(!lname.equals("")){
		    stmt.setString(jj++, lname+"%");
		    stmt.setString(jj++, lname+"%");
		}
		if(!contact.equals("")){
		    stmt.setString(jj++, contact);
		}
		if(!dob.equals("")){
		    java.util.Date date_tmp = df.parse(dob);
		    stmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
		}
		if(!addrUpdate.equals("")){
		    java.util.Date date_tmp = df.parse(addrUpdate);
		    stmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
		}
		if(!updateFrom.equals("")){
		    java.util.Date date_tmp = df.parse(updateFrom);
		    stmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
		}
		if(!updateTo.equals("")){
		    java.util.Date date_tmp = df.parse(updateTo);
		    stmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
		}
		if(!ssn.equals("")){
		    stmt.setString(jj++,ssn);
		}
		if(!streetNum.equals("")){
		    stmt.setString(jj++,streetNum);
		}
		if(!streetDir.equals("")){
		    stmt.setString(jj++,streetDir);
		}
		if(!streetName.equals("")){
		    stmt.setString(jj++,streetName);
		}
		if(!streetType.equals("")){
		    stmt.setString(jj++,streetType);
		}
		if(!sudType.equals("")){
		    stmt.setString(jj++,sudType);
		}
		if(!sudNum.equals("")){
		    stmt.setString(jj++,sudNum);
		}
		if(!city.equals("")){
		    stmt.setString(jj++,city);
		}
		if(!state.equals("")){
		    stmt.setString(jj++,state);
		}
		if(!zip.equals("")){
		    stmt.setString(jj++,zip);
		}
		if(!phone.equals("")){
		    stmt.setString(jj++,phone);
		    stmt.setString(jj++,phone);
		    stmt.setString(jj++,phone);
		}
		if(!risk_id.equals("")){
		    stmt.setString(jj++,risk_id);
		    stmt.setString(jj++,risk_id);
		    stmt.setString(jj++,risk_id);
		    stmt.setString(jj++,risk_id);										
		}								
	    }
	    rs = stmt.executeQuery();
	    while(rs.next()){
		if(persons == null)
		    persons = new ArrayList<>();
		RiskPerson one =
		    new RiskPerson(debug,
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
				   rs.getString(23));
		if(!persons.contains(one))
		    persons.add(one);
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























































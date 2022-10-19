package risks.lists;
/**
 * @copyright Copyright (C) 2014-2022 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.models.*;
import risks.utils.*;

public class RiskTypes{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(RiskTypes.class);
    String id="", name = "";
    String otherType="";
    String typeArr[] = null; // can hold any type array
    String typeIdArr[] = null; 
    public RiskTypes(boolean deb){
	debug = deb;
    }
    public RiskTypes(boolean deb, String val, String val2){
	debug = deb;
	if(val != null)
	    id = val;
	if(val2 != null)
	    name = val2;
    }	
    //
    // setters
    //
    public void setOtherType(String val){
	otherType = val;
    }
    public String[] getTypeArr(){
	return typeArr;
    }
    public String[] getTypeIdArr(){
	return typeIdArr;
    }
    public String addNewType(Statement stmt, String which){
		
	String msg = "";
	String str = "", qq = "insert into ";

	if(which.equals("safety"))
	    qq += " riskSafetyTypes ";
	else if(which.equals("claim"))
	    qq += " riskClaimTypes ";
	else if(which.equals("legal"))
	    qq += " riskLegalTypes ";
	else 
	    msg = "Unknown type "+which;
	qq += " values('"+otherType+"')";
	if(debug){
	    logger.debug(qq);
	}
	try{
	    stmt.executeUpdate(qq);
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    msg += ex;
	}
	return msg;
    }
    //
    public String generateTypes(Statement stmt,ResultSet rs, String which){
	String msg = "";
	String str = "", str2="", qc = "", qq="";
	int cnt = 0, j=1;
	qc = " select count(*) from ";
	String q = " select * from ";

	if(which.equals("safety")){
	    qc += " riskSafetyTypes ";
	    q += " riskSafetyTypes ";
	}
	else if(which.equals("claim")){
	    qc += " riskClaimTypes ";
	    q += " riskClaimTypes ";
	}
	else if(which.equals("legal")){
	    qc += " riskLegalTypes ";
	    q += " riskLegalTypes ";
	}
	else if(which.equals("unified")){
	    qc += " riskUnifiedTypes ";
	    q += " riskUnifiedTypes ";
	}
	else 
	    msg = "Unknown type "+which;
	qq = qc;
	if(debug){
	    logger.debug(qc);
	}
	try{
	    rs = stmt.executeQuery(qc);
	    if(rs.next()){
		cnt = rs.getInt(1);
	    }
	    if(cnt > 0){
		cnt++; // for the empty option
		typeArr = new String[cnt];
		typeIdArr = new String[cnt];
		for(int i=0;i<cnt;i++){
		    typeArr[i] = "";
		    typeIdArr[i] = "";
		}
		qq = q;
		if(debug){
		    logger.debug(qq);
		}
		rs = stmt.executeQuery(qq);
		if(which.equals("unified")){
		    while(rs.next()){
			str = rs.getString(1);
			str2 = rs.getString(2);
			if(str != null){
			    typeIdArr[j] = str;
			    if(str2 != null) 
				typeArr[j] = str2;
			    j++;
			}
		    }
		}
		else{
		    while(rs.next()){
			str = rs.getString(1);
			if(str != null){
			    typeArr[j] = str;
			    j++;
			}
		    }
		}
	    }
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    msg += ex;
	}
	return msg;
    }

}

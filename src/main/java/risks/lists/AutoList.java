package risks.lists;
/**
 * @copyright Copyright (C) 2014-2022 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.models.*;
import risks.utils.*;

public class AutoList{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(AutoList.class);
    String id="", // auto id 
	risk_id ="",  
	autoMake ="", autoModel="", autoYear="",
	autoPlate="", vin= "", autoNum="", owner="";
    String errors = "";
    List<Auto> autos = null;
    public AutoList(boolean deb){
	debug = deb;
    }
    public AutoList(boolean deb, String val){
	debug = deb;
	if(val != null)
	    risk_id = val;
    }
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setRisk_id(String val){
	if(val != null)
	    risk_id = val;
    }
    public void setVin(String val){
	if(val != null)
	    vin = val; 
    }
    public void setOwner(String val){
	if(val != null)
	    owner = val; 
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
    //
    // setters
    //
    public List<Auto> getAutos(){
	return autos;
    }
	
    public String getErrors(){
	return errors;
    }

    public String lookFor(){
		
	String back = "";
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;
	String qq = "select id,risk_id,"+
	    "vin,autoMake,autoModel,autoYear,"+
	    " autoPlate,autoNum,owner "+
	    " from risk_autos ";
	String qw = "", msg="";
	boolean andFlag = false;
	if(!risk_id.equals("")){
	    qw += " risk_id = ?";
	}
	if(!vin.equals("")){
	    if(qw.equals("")) qw += " and ";
	    qw += " vin like ? "; // '%"+vin+"%'";
	}
	if(!autoPlate.equals("")){
	    if(qw.equals("")) qw += " and ";
	    qw += " autoPlate like ? "; // '%"+autoPlate+"%'";
	}
	if(!autoMake.equals("")){
	    if(qw.equals("")) qw += " and ";
	    qw += " autoMake like ? ";// '%"+autoMake+"%'";
	}
	if(!autoModel.equals("")){
	    if(qw.equals("")) qw += " and ";
	    qw += " autoModel like ? "; // '%"+autoModel+"%'";
	}
	if(!autoNum.equals("")){
	    if(qw.equals("")) qw += " and ";
	    qw += " autoNum like ? ";// '"+autoNum+"'";
	}
	if(!autoYear.equals("")){
	    if(qw.equals("")) qw += " and ";
	    qw += " autoYear = ? ";//+autoYear;
	}
	if(!owner.equals("")){
	    if(qw.equals("")) qw += " and ";
	    qw += " owner = ? ";
	}				
	if(!qw.equals("")){
	    qq += " where "+qw;
	}
	if(debug)
	    logger.debug(qq);
	con = Helper.getConnection();
	if(con == null){
	    back = " Could not connect to DB ";
	    return back;
	}
	try{
	    stmt = con.prepareStatement(qq);
	    int jj=1;
	    if(!risk_id.equals("")){
		stmt.setString(jj++, risk_id);
	    }
	    if(!vin.equals("")){
		stmt.setString(jj++, "%"+vin+"%");
	    }
	    if(!autoPlate.equals("")){
		stmt.setString(jj++, "%"+autoPlate+"%");						
	    }
	    if(!autoMake.equals("")){
		stmt.setString(jj++, "%"+autoMake+"%");
	    }
	    if(!autoModel.equals("")){
		stmt.setString(jj++, "%"+autoModel+"%");
	    }
	    if(!autoNum.equals("")){
		stmt.setString(jj++, autoNum);
	    }
	    if(!autoYear.equals("")){
		stmt.setString(jj++, autoYear);
	    }
	    if(!owner.equals("")){
		stmt.setString(jj++, owner);
	    }						
	    rs = stmt.executeQuery();
	    autos = new ArrayList<Auto>(2);
	    while(rs.next()){
		Auto auto = new Auto(debug,
				     rs.getString(1),
				     rs.getString(2),
				     rs.getString(3),
				     rs.getString(4),
				     rs.getString(5),
				     rs.getString(6),
				     rs.getString(7),
				     rs.getString(8),
				     rs.getString(9));
		autos.add(auto);
	    }
	}
	catch(Exception ex){
	    back = ex+":"+qq;
	    logger.error(back);
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	return back;
    }

	
}

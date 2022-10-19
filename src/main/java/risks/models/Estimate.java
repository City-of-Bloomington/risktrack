package risks.models;
import java.sql.*;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.utils.Helper;

public class Estimate{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(Estimate.class);

    String id = "",
	risk_id="",
	type = "",
	company = "",
	cost = "",
	chosen = "";
    String errors = "";
    public Estimate(boolean deb){
	debug = deb;
    }
    //
    public Estimate(boolean deb, String _id){
	debug = deb;
	setId(_id);
	errors = doSelect();
    }
    public Estimate(boolean deb, String _id, String val,
		    String val2, String val3,
		    String val4, String val5){
	debug = deb;
	setId(_id);
	setRisk_id(val);
	setType(val2);				
	setCompany(val3);
	setCost(val4);
	setChosen(val5);
    }	
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setRisk_id(String val){
	if(val != null)
	    risk_id = val;
    }
    public void setCompany(String val){
	if(val != null)
	    company = val;
    }
    public void setType(String val){
	if(val != null)
	    type = val;
    }
    public void setCost(String val){
	if(val != null)
	    cost = val;
    }
    public void setChosen(String val){
	if(val != null)
	    chosen = val;
    }
    //
    // getters
    //
    public String getId(){
	return id;
    }
    public String getRisk_id(){
	return risk_id;
    }
    public String getType(){
	return type;
    }
    public String getCompany(){
	return company;
    }
    public String getCost(){
	return cost;
    }
    public String getChosen(){
	return chosen;
    }
    public boolean isChosen(){
	return chosen.equals("y");
    }
	
    public String getErrors(){
	return errors;
    }
    public boolean isEmpty(){
	return company.equals("") && cost.equals("");
		
    }
    public String doSelect(){
		
	String msg = "";
	Connection con = null;
	PreparedStatement stmt = null;	
	ResultSet rs = null;
		
	String qq = "select * from risk_estimates where id=?";
	if(id.equals("")){
	    msg = " Unknown id ";
	    return msg;
	}
	qq += id;
	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    logger.error(msg);
	    return msg;
	}						
	try{
	    stmt = con.prepareStatement(qq);
	    stmt.setString(1, id);
	    rs = stmt.executeQuery();
	    if(rs.next()){
		String str = rs.getString(2);
		if(str != null)
		    risk_id = str;
		str = rs.getString(3);
		if(str != null)
		    type = str;
		str = rs.getString(4);
		if(str != null)
		    company = str;
		str = rs.getString(5);
		if(str != null)
		    cost = str;
		str = rs.getString(6);
		if(str != null)
		    chosen = str;
	    }
	    else{
		msg = " Invalid id "+id;
	    }
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    msg += ex+":"+qq;
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	return msg;

    }
    public String doSave(){

	String msg = "";
	String str = "", qq="";
	if(id.equals("")){
	    if(!isEmpty())
		msg = doInsert();
	}
	else{
	    msg = doUpdate();
	}
		
	return msg;
    }
    public String doInsert(){

	Connection con = null;
	PreparedStatement stmt = null, stmt2=null;	
	ResultSet rs = null;		
	String msg = "";
	String str = "", qq="";
	qq = " insert into risk_estimates values(0,?,?,?,?,?)";
	con = Helper.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    logger.error(msg);
	    return msg;
	}
	if(debug)
	    logger.debug(qq);
	try{
	    stmt = con.prepareStatement(qq);
	    stmt.setString(1, risk_id);
	    if(type.equals(""))
		stmt.setNull(2, Types.INTEGER);
	    else
		stmt.setString(2, type);
	    if(company.equals(""))
		stmt.setNull(3, Types.VARCHAR);
	    else
		stmt.setString(3, company);
	    if(cost.equals(""))
		stmt.setNull(4, Types.VARCHAR);
	    else
		stmt.setString(4, cost);
	    if(chosen.equals(""))
		stmt.setNull(5, Types.CHAR);
	    else
		stmt.setString(5, "y");
	    stmt.executeUpdate();
	    qq = "select LAST_INSERT_ID() ";
	    if(debug){
		logger.debug(qq);
	    }
	    stmt2 = con.prepareStatement(qq);
	    rs = stmt2.executeQuery();
	    if(rs.next()){
		id = rs.getString(1);
	    }
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    msg += ex+":"+qq;
	}
	finally{
	    Helper.databaseDisconnect(con, rs, stmt, stmt2);
	}
	return msg;
    }
    public String doUpdate(){

	Connection con = null;
	PreparedStatement stmt = null;	
	ResultSet rs = null;		
	String msg = "";
	String str = "", qq="";
	qq = " update risk_estimates set type=?,company=?,cost=?,chosen=? where id =? ";

	if(debug)
	    logger.debug(qq);
	con = Helper.getConnection();
	if(con == null){
	    msg = "Could not connect to DB ";
	    logger.error(msg);
	    return msg;
	}				
	try{
	    stmt = con.prepareStatement(qq);
	    if(type.equals(""))
		stmt.setNull(1, Types.INTEGER);
	    else
		stmt.setString(1, type);
	    if(company.equals(""))
		stmt.setNull(2, Types.VARCHAR);
	    else
		stmt.setString(2, company);
	    if(cost.equals(""))
		stmt.setNull(3, Types.VARCHAR);
	    else
		stmt.setString(3, cost);
	    if(chosen.equals(""))
		stmt.setNull(4, Types.CHAR);
	    else
		stmt.setString(4, "y");
	    stmt.setString(5, id);
	    stmt.executeUpdate();
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

	

}

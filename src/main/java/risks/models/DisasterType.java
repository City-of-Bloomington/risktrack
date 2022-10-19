package risks.models;
import java.sql.*;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.utils.Helper;

public class DisasterType implements RType{

    boolean debug = false;
    static Logger logger = LogManager.getLogger(DisasterType.class);		
    String id = "";
    String type = "";
    String errors = "";

    public DisasterType(boolean deb){
	debug = deb;
    }
    //
    public DisasterType(boolean deb, String id){
	debug = deb;
	setId(id);
	errors = doSelect();
		
    }
    public DisasterType(boolean deb, String val, String val2){
	debug = deb;
	setId(val);
	setType(val2);
		
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
    public String getType(){
	return type;
    }
    public String getId(){
	return id;
    }
    public String getErrors(){
	return errors;
    }
	
    public String doSelect(){
	String back = "";
	Connection con = null;
	PreparedStatement stmt = null;	
	ResultSet rs = null;
	
	String qq = "select type from disaster_types where id=?";
	if(id.equals("")){
	    back = " id not set";
	    return back;
	}
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
	    stmt.setString(1, id);
	    rs = stmt.executeQuery();
	    if(rs.next()){
		String str = rs.getString(1);
		if(str != null)
		    type = str;
	    }
	    else{
		back = " Invalid id "+id;
	    }
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    back += ex+":"+qq;
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	return back;
    }
	
    public String doSave(){

	Connection con = null;
	PreparedStatement stmt = null, stmt2=null;	
	ResultSet rs = null;		
	String back = "";
	String str = "", qq="";
	if(!type.equals("")){
	    if(id.equals("")){
		qq = " insert into disaster_types values(0,?)";
	    }
	    else{
		qq = " update disaster_types set type=? where id=?";
	    }
	    if(debug)
		logger.debug(qq);
	    con = Helper.getConnection();
	    if(con == null){
		back = "Could not connect to DB ";
		logger.error(back);
		return back;
	    }														
	    try{
		stmt = con.prepareStatement(qq);
		if(id.equals("")){
		    stmt.setString(1, type);
		}
		else{
		    stmt.setString(1, type);
		    stmt.setString(2, id);
		}								
		stmt.executeUpdate();
		if(id.equals("")){
		    qq = "select LAST_INSERT_ID() ";
		    if(debug){
			System.err.println(qq);
		    }
		    stmt2 = con.prepareStatement(qq);
		    rs = stmt2.executeQuery();
		    if(rs.next()){
			id = rs.getString(1);
		    }
		}
	    }
	    catch(Exception ex){
		logger.error(ex+":"+qq);
		back += ex+":"+qq;
	    }
	    finally{
		Helper.databaseDisconnect(con, rs, stmt, stmt2);
	    }
	}
	else{
	    back = " Type value not set ";
	}
	return back;
    }

}

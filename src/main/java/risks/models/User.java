package risks.models;

import java.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.utils.Helper;
    

public class User implements java.io.Serializable{

    static Logger logger = LogManager.getLogger(User.class);
    boolean debug = false;
    String id="", userid="", name="", role="";
		
    public User(String _id,
		String _empid, 
		String _name,
		String _role
		){
	id=_id;
	userid=_empid;
	name=_name;
	role = _role;
    }

    public User(){
				
    }
    public User(String val){
	//
	setUserid(val);
    }
    public User(boolean deb, String val){
	//
	debug = deb;
	setId(val);
    }
    public User(boolean deb, String val, String val2){
	//
	debug = deb;
	if(val != null)
	    id = val;
	if(val2 != null)
	    name = val2;
    }		
    //
    // getters
    //
    public String getId(){
	return id;
    }		
    public String getUserid(){
	return userid;
    }
    public String getName(){
	return name;
    }
    public String toString(){
	if(!name.equals(""))
	    return name;
	else
	    return userid;
    }
    //
    // setters
    //
    public void setUserid (String val){
	if(val != null)
	    userid = val;
    }
    public void setName (String val){
	if(val != null)
	    name = val;
    }
    public void setId (String val){
	if(val != null)
	    id = val;
    }
    public boolean isAdmin (){
	return hasRole("Admin");
    }
    public boolean canEdit (){
	return hasRole("Edit");
    }
    public boolean canDelete (){
	return hasRole("Delete");
    }
    public boolean hasRole(String val){
	return role.indexOf(val) > -1;
    }
    public String doSelect(){
	Connection con = null;
	PreparedStatement stmt = null, stmt2=null, stmt3=null;	
	ResultSet rs = null;			
	String back = "";
	String qq = "select * from users where ";
	if(!id.equals(""))
	    qq += " id = ? ";
	else
	    qq += "userid like ? ";
						
	if(debug)
	    logger.debug(qq);
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB ";
	    System.err.println(back);
	    logger.error(back);
	    return back;
	}							
	try{
	    stmt = con.prepareStatement(qq);
	    if(!id.equals(""))
		stmt.setString(1, id);
	    else
		stmt.setString(1, userid);								
	    rs = stmt.executeQuery();
	    if (rs.next()) {
		String str = rs.getString(1);
		if(str != null) id = str;
		str = rs.getString(2);
		if(str != null) userid = str;								
		str = rs.getString(3);
		if(str != null) name = str;
		str = rs.getString(4);
		if(str != null) role = str;
	    }
	    else{
		back = "No match found";								
	    }
	}catch(Exception ex){
	    logger.error(ex+":"+qq);
	    back += ex;
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}				
	return back;
    }

}

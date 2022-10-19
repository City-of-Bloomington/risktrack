package risks.models;

import java.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.utils.*;

public class RelatedUtil{

    String id="", url="", type="", link="";
    static Logger logger = LogManager.getLogger(RelatedUtil.class);	
    boolean debug = false;
    public RelatedUtil(
		       boolean deb,
		       String val, 
		       String val2
		       ){

	setId(val);
	setUrl(val2);
	debug = deb;
	find();
    }
    //
    // getters
    //
    public String getId(){
	return id;
    }
    public String getUrl(){
	return url;
    }
    public String getType(){
	return type;
    }
    public String getLink(){
	return link;
    }
    public String toString(){
	return id;
    }
    public boolean foundType(){
	return !type.equals("");
    }
    //
    // setters
    //
    public void setId (String val){
	if(val != null)
	    id = val;
    }
    public void setUrl (String val){
	if(val != null)
	    url = val;
    }
    public void setType (String val){
	if(val != null)
	    type = val;
    }
    //
    // we need this to make sure that the user picked the right type
    //
    public      String find(){
		
	Connection con = null;
	PreparedStatement stmt = null;	
	ResultSet rs = null;
	String back = "";
	String qq = " select 'Tort Claim' from tortClaims where id=?"+
	    " union select 'Recovery Action' from vslegals where id=?"+
	    " union select 'Worker Comp' from workerComps where id=?"+
	    " union select 'Internal Accident' from riskSafety where id=?"+
	    " union select 'Natural Disaster Accident' from disasters where id=?"+
	    " union select 'Misc Accident' from misc_accidents where id=?"+
	    " union select 'Auto Accident' from auto_accidents where id=?";
				
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
	    stmt.setString(1, id);
	    stmt.setString(2, id);
	    stmt.setString(3, id);
	    stmt.setString(4, id);
	    stmt.setString(5, id);
	    stmt.setString(6, id);
	    stmt.setString(7, id);						
	    rs = stmt.executeQuery();
	    if (rs.next()) {
		String str = rs.getString(1);
		if(str != null){
		    type = str;
		}
	    }
	}catch(Exception ex){
	    back = ex+" "+qq;
	    logger.error(back);
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	if(!type.equals("")){
	    back = createLink();
	}
	return back;
    }
    public String createLink(){
	String back = "";
	if(type.startsWith("Tort")){
	    link = "<a href=\""+url+"TortClaimServ?id="+id+"\">"+id+"</a>";
	}
	else if(type.startsWith("Recovery")){
	    link = "<a href=\""+url+"LegalServ?id="+id+"\">"+id+"</a>";
	}
	else if(type.startsWith("Internal")){
	    link = "<a href=\""+url+"SafetyServ?id="+id+"\">"+id+"</a>";
	}
	else if(type.startsWith("Worker")){
	    link = "<a href=\""+url+"WorkCompServ?id="+id+"\">"+id+"</a>";
	}
	else if(type.startsWith("Natural")){
	    link = "<a href=\""+url+"DisasterServ?id="+id+"\">"+id+"</a>";
	}
	else if(type.startsWith("Misc")){
	    link = "<a href=\""+url+"MiscAccidentServ?id="+id+"\">"+id+"</a>";
	}
	else if(type.startsWith("Auto")){
	    link = "<a href=\""+url+"AutoAccidentServ?id="+id+"\">"+id+"</a>";
	}				
	else{
	    back = "Unknown type "+type;
	    System.err.println(back);
	}
	return back;
    }
	
}

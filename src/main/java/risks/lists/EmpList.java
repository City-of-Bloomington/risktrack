package risks.lists;
/**
 * @copyright Copyright (C) 2014-2022 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.*;
import java.sql.*;
import java.io.*;
import javax.sql.*;
import javax.naming.*;
import javax.naming.directory.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.models.*;
import risks.utils.*;

public class EmpList{

    static Logger logger = LogManager.getLogger(EmpList.class);
    static final long serialVersionUID = 1100L;
    static EnvBean bean = null;
    String name = "", dept_name="", group_name="";
    String id_code="";
    Hashtable<String, String> deptTable = null; // name, id

    List<Employee> emps = null;
    public EmpList(){
	super();
    }
    public EmpList(EnvBean val){
	setEnvBean(val);
    }
    public EmpList(EnvBean val, String val2){
	setEnvBean(val);
	setName(val2);
    }
    // we do not tables here
    public EmpList(EnvBean val, String val2, boolean simple){
	setEnvBean(val);
	setName(val2);
    }		
		
    public List<Employee> getEmps(){
	return emps;
    }
    public void setName(String val){
	if(val != null){
	    name = val;
	}
    }
    public void setDept_name(String val){
	if(val != null)
	    dept_name = val;
    }
    public void setEnvBean(EnvBean val){
	if(val != null)
	    bean = val;
    }		
    public String getName(){
	return name;
    }

    public void setGroup_name(String val){
	if(val != null){
	    group_name = val;
	}
    }		

    public boolean connectToServer(Hashtable<String, String> env){

	if(env != null && bean != null){
	    env.put(Context.INITIAL_CONTEXT_FACTORY, 
		    "com.sun.jndi.ldap.LdapCtxFactory");
	    env.put(Context.PROVIDER_URL, bean.getUrl());
	    env.put(Context.SECURITY_AUTHENTICATION, "simple"); 
	    env.put(Context.SECURITY_PRINCIPAL, bean.getPrinciple());
	    env.put(Context.SECURITY_CREDENTIALS, bean.getPassword());
	    // env.put("java.naming.ldap.attributes.binary","objectSID");
	}
	else{
	    return false;
	}
	try {
	    DirContext ctx = new InitialDirContext(env);
	} catch (NamingException ex) {
	    System.err.println(" ldap "+ex);
	    return false;
	}
	return true;
    }		
    public String find(){
	Hashtable<String, String> env = new Hashtable<String, String>(11);
	String back = "", str="";
	Employee emp = null;
	if (!connectToServer(env)){
	    System.err.println("Unable to connect to ldap");
	    return null;
	}
	try{
	    //
	    DirContext ctx = new InitialDirContext(env);
	    SearchControls ctls = new SearchControls();
	    ctls.setCountLimit(2000);
	    String[] attrIDs = {// "objectSID",
		"givenName",
		"department", // not accurate use dn instead
		"telephoneNumber",
		"mail",
		"cn",
		"sAMAccountName",
		"sn",
		"distinguishedName",
		"dn",
		"businessCategory",
		"employeeNumber",
		"employeeId", // id_code
		"title"};
	    //
	    ctls.setReturningAttributes(attrIDs);
	    ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
	    String filter = "";
	    if(!name.equals("")){
		filter = "(sAMAccountName=*"+name+"*)";
	    }
	    else if (!dept_name.equals("")){
		// we are excluding disabled users and any user that
		// givenName starts with * (code \2a)
		filter ="(&(objectCategory=person)(objectCategory=user)(!(givenName=\2a*)))";
	    }
	    else{ // all
		// we are excluding disabled users and any user that
		// givenName (first name) that starts with * code \2a 				
		filter ="(&(objectCategory=person)(objectCategory=user)(!(givenName=\2a*)))";
	    }
	    //`	    System.err.println(" filter "+filter);
	    NamingEnumeration<SearchResult> answer = ctx.search("", filter, ctls);
	    int jj=1;
	    while(answer.hasMore()){
		//
		emp = new Employee();
		String fullName= "";
		SearchResult sr = answer.next();
		Attributes atts = sr.getAttributes();
		Attribute dn = atts.get("distinguishedName");
		if (dn != null){
		    str = dn.get().toString();
		    if(!dept_name.equals("")){
			if(str.indexOf(dept_name) == -1) continue;
		    }
		    String strArr[] = setDn(str);

		}
		//
		Attribute cn = atts.get("sAMAccountName");
		if (cn != null){
		    str = cn.get().toString();
		    emp.setUserid(str);
		}
																
		Attribute givenname = atts.get("givenName");
		if (givenname != null){
		    str = givenname.get().toString();
		    if(str.indexOf("*") > -1) continue;
		    fullName += str; // fist name
		}
		Attribute sn = atts.get("sn");
		if (sn != null){
		    str = sn.get().toString();
		    fullName += " "+str; // last name
		    emp.setName(fullName);
		}
		Attribute phone = atts.get("telephoneNumber");
		if (phone != null){
		    str = phone.get().toString();
		    emp.setPhone(str);
		}
		Attribute title = atts.get("title");
		if (title != null){
		    str = title.get().toString();
		    emp.setTitle(str);
		}
		if(emps == null){
		    emps = new ArrayList<>();
		}
		emps.add(emp);
		jj++;
	    }
	}
	catch(Exception ex){
	    logger.error(ex);
	}
	return back;
    }
    String[] setDn(String val){
	String retArr[] = {"",""};
	if(val != null){
	    String dept="", grp = "", sub_grp="";
	    try{
		String val2 = val.substring(val.indexOf("OU"),val.indexOf("DC")-1);
		String strArr[] = val2.split(",");
		if(strArr != null){
		    if(strArr.length == 2){
			dept = strArr[0]; // transit
		    }
		    if(strArr.length == 3){
			if(val2.indexOf("City Hall") > 0){
			    dept = strArr[0];
			}
			else if(val2.indexOf("Utilities") > 0){
			    dept = strArr[0];
			    // strArr[1]; // utilities
			    // strArr[2] // Departments
			}
		    }
		    else if(strArr.length == 4){
			if(val2.indexOf("City Hall") > 0){
			    grp = strArr[0];
			    dept = strArr[1];
			}
			else{
			    // example maintenance,Dillman Plant,Utilities
			    if(strArr[3].indexOf("Utilities")>-1){
				sub_grp = strArr[0];
				grp = strArr[1];
				dept = strArr[2]; // ignore Uitilities
			    }
			    else if(strArr[2].indexOf("Utilities")>-1){
				grp = strArr[0];
				dept = strArr[1];
				// dept = strArr[2]; // ignore Uitilities
			    }
			    else if(strArr[1].indexOf("Utilities")> -1){
				dept = strArr[0];
			    }
			    else{
				sub_grp = strArr[0];
				grp = strArr[1];
				dept = strArr[2];
			    }
			}
		    }
		    if(!dept.equals("")){
			dept = dept.substring(dept.indexOf("=")+1);
			retArr[0] = dept;
		    }
		}
	    }catch(Exception ex){
		System.err.println(ex);
	    }
	}
	return retArr;
    }
}























































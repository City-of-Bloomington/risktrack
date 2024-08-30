package risks.utils;
import java.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.models.*;
/**
 * 
 */

public class Inserts{

    boolean debug;
    static Logger logger = LogManager.getLogger(Inserts.class);
    //
    // Person title
    public final static String nameTitleArr[] ={"","Mr.","Mrs.","Ms."}; 
    // torts, work_comp, safety
    public final static String statusArr[] ={"Open","Closed"}; 
    public final static String searchStatusArr[] ={"","Open","Closed"}; 
    //
    // torts only
    public final static String tortTypeArr[] = {"","Pot Holes","Side Walks","Sewer Backups","Mailboxes", "Parking Garage Vandalism","Other"};
    public final static String tortCatsArr[] ={"Incident","Tort"};
    public final static String searchTortCatsArr[] ={"","Incident","Tort"};
    public final static String insurStatusArr[] = {"","Pending","Accepted","Denied","Closed"};
    public final static String policyArr[] ={"","Gen","Auto","Prop","Med","B.I."};
    //
    // work comp
    public final static String compensableArr[] = {"","Yes","No","Disputed"};
    public final static String ableBackWorkArr[] = {"","Yes","Yes w/Restrictions","No"};
    // Safety
    public final static String safetyTypeArr[] = {"","Backing","Runaway","Collision","Sprain/Strain","Slips/Trips","Exposure","Other"};
    //
    // Legal
    public final static String legalTypeArr[] = {"","Lamp Posts","Signs","Trees","Parking Meters","Parking Garages","Other"};
    public final static String legalStatusArr[] ={"Open","Pending","Court","Closed"};
    public final static String searchLegalStatusArr[] ={"","Open","Pending","Court","Closed"};
    //
    // xhtmlHeaderInc
    public final static String xhtmlHeaderInc = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
	"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">\n"+
	"<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">";
    //
    // basic constructor
    public Inserts(boolean deb){
	//
	// initialize
	//
	debug = deb;
    }
    //
    // main page banner
    //
    public final static String banner2(String url, String action){
	String dateStr = "{ nextText: \"Next\",prevText:\"Prev\", buttonText: \"Pick Date\", showOn: \"both\", navigationAsDateFormat: true, buttonImage: \""+url+"js/calendar.gif\"}";
	String banner = "<head>\n"+
	    "<meta http-equiv=\"no-cache\">"+
	    "<meta http-equiv=\"Content-Type\" content=\"application/xhtml+xml; charset=utf-8\" />\n"+
	    "<meta http-equiv=\"Content-Script-Type\" content=\"text/javascript\" />\n"+
	    "<style type=\"text/css\" media=\"all\">\n"+
	    "@import url(\"/risktrack/css/screen.css\");\n"+
	    "</style>\n"+
	    "<style type=\"text/css\">"+  
	    "table.wide {width:100%;border:none}"+
	    "table.widesolid {width:100%;border-style:solid}"+
	    "table.box {width:100%;border:2px solid}"+
	    "table.box th {border:2px solid; text-align:center; vertical-align:bottom; } "+
	    "table.box td {border:2px solid} "+
	    "table.control {width:100%} "+
	    "table.control td {text-align:center; padding:.5em} "+
	    "tr.solid {border-style:solid}\n"+
	    "td.right {text-align:right}\n"+
	    "</style>\n"+
	    "<script type='text/javascript'>\n"+
	    "var APPLICATION_URL='"+url+"';\n"+
	    "</script>\n";
	banner +="<link rel=\"stylesheet\" href=\""+url+"js/jquery-ui.min-1.13.2.css\" type=\"text/css\" media=\"all\" />\n";
	banner += "<link rel=\"stylesheet\" href=\""+url+"js/jquery-ui.theme.min-1.13.2.css\" type=\"text/css\" media=\"all\" />\n";
	banner +="<script type=\"text/javascript\" src=\""+url+"js/jquery-3.6.1.min.js\"></script>\n";
	banner += "<script type=\"text/javascript\" src=\""+url+"js/jquery-ui.min-1.13.2.js\"></script>\n";	
	banner += "<script type=\"text/javascript\" src=\""+url+"js/risk.js\"></script>\n";		
	banner +=  "<script>\n"+
	    " var icons = { header:\"ui-icon-circle-plus\","+
	    "               activeHeader:\"ui-icon-circle-minus\"};\n"+
	    " $(function(){ \n"+
	    "  $( \".date\" ).datepicker("+dateStr+");\n "+
	    " });"+
	    "</script>"+								
	    //
	    // Java Script common for all pages
	    //
	    "<script type=\"text/javascript\">\n"+
	    "  function checkDate(dt){ \n                       "+     
	    "    var dd = dt.value;                             \n"+
	    "   if(dd.length == 0) return true;                 \n"+
	    "   var dar = dd.split(\"/\");                      \n"+
	    " if(dar.length < 3){                               \n"+
	    " alert(\"Not a valid date: \"+dd);                 \n"+
	    "      dt.select();                                 \n"+
	    "      dt.focus();                                  \n"+
	    "      return false;}                               \n"+
	    "   var m = dar[0];                                 \n"+
	    "   var d = dar[1];                                 \n"+
	    "   var y = dar[2];                                 \n"+
	    "   if(isNaN(m) || isNaN(d) || isNaN(y)){           \n"+
	    " alert(\"Not a valid date: \"+dd);                 \n"+
	    "      dt.select();                                 \n"+
	    "      dt.focus();                                  \n"+
	    "      return false; }                              \n"+
	    "   if( !((m > 0 && m < 13) && (d > 0 && d <32) &&  \n"+
	    "    (y > 1950 && y < 2099))){                      \n"+
	    " alert(\"Not a valid date: \"+dd);                 \n"+
	    "      dt.select();                                 \n"+
	    "      dt.focus();                                  \n"+
	    "      return false;                                \n"+
	    "      }                                            \n"+
	    "    return true;                                   \n"+
	    "    }                                              \n"+
	    "  function checkNumber(dt){                        \n"+     
	    "    var dd = dt.value;                             \n"+
	    "   if(dd.length == 0) return false;                \n"+
	    "     if(isNaN(dd)){                                \n"+
	    "      alert(\"Not a valid Number: \"+dd);          \n"+
	    "      dt.select();                                 \n"+
	    "      dt.focus();                                  \n"+
	    "      return false;                                \n"+
	    "        }                                          \n"+
	    "    return true;                                   \n"+
	    "   }                                               \n"+
	    "  function validateDelete(){	                    \n"+
	    "   var x = false;                                  \n"+
	    " x = confirm(\"Are you sure you want to delete this record\");\n"+
	    "   return x;                                       \n"+
	    "	}	             		                        \n"+
	    "  function refreshPage(){	                        \n"+
	    "  document.forms[0].submit();                      \n"+
	    "  }                                                \n"+
	    " </script>				                            \n"+
	    "<title>RiskTrack - City of Bloomington, Indiana</title>\n"+
	    "</head>\n"+
	    "<body "+action+">\n"+
	    "<div id=\"banner\">\n"+
	    "  <div id=\"application_name\">RiskTrack</div>\n"+
	    "  <div id=\"location_name\">City Of Bloomington, IN</div>\n"+
	    "  <div id=\"navigation\"></div>\n"+
	    "</div>";
	return banner;
    }
    public final static String banner(String url){
	// for no action
	return banner2(url, "");
    }
    //
    public final static String menuBar(String url, boolean logged){
	String menu = "<div class='menuBar'>\n<ul>";
	if(logged){
	    menu += "<li><a href=\""+url+"/Logout\">Logout</a></li>\n";
	}
	menu += "</ul></div>\n";
	return menu;
    }
    //
    // sidebar
    //
    public final static String sideBar(String url){
	return sideBar(url, null);
    }
    public final static String sideBar(String url, User user){

	String ret = "<div id='leftSidebar' class='left sidebar'>"+
	    "<h3 class=\"titleBar\">New Action</h3>\n"+
	    "<ul>\n"+
	    "<li><a href=\""+url+"RiskPerson\">Claimant/Defendant</a></li>\n"+
	    "<li><a href=\""+url+"TortClaim\">Tort Claim</a></li>\n"+
	    "<li><a href=\""+url+"Legal\">Recovery Action</a></li>\n"+
	    "<li><a href=\""+url+"WorkComp\">Worker's Comp</a></li>\n"+
	    "<li><a href=\""+url+"Safety\">Internal Accident</a></li>\n"+
	    "<li><a href=\""+url+"Disaster\">Natural Disaster</a></li>\n"+
	    "<li><a href=\""+url+"MiscAccident\">Misc Accident</a></li>\n"+
	    "<li><a href=\""+url+"AutoAccident\">Auto Accident</a></li>\n"+						
	    "</ul>\n"+
	    "<h3 class=\"titleBar\">Search</h3>\n"+
	    "<ul>\n"+
	    "<li><a href=\""+url+"SearchPerson\">Claimants/Defendants</a></li>\n"+
	    "<li><a href=\""+url+"Search\">Global Search</a></li>\n"+
	    "<li><a href=\""+url+"SearchClaim\">Tort Claims</a></li>\n"+
	    "<li><a href=\""+url+"SearchLegal\">Recovery Actions</a></li>\n"+
	    "<li><a href=\""+url+"SearchWComp\">Worker's Comp</a></li>\n"+
	    "<li><a href=\""+url+"SearchSafety\">Internal Accidents</a></li>\n"+
	    "<li><a href=\""+url+"SearchDisaster\">Natural Disasters</a></li>\n"+
	    "<li><a href=\""+url+"SearchMisc\">Misc Accidents</a></li>\n"+
	    "<li><a href=\""+url+"SearchAutoAccident\">Auto Accidents</a></li>\n"+				
	    "<li><a href=\""+url+"Dept\">Departments</a></li>\n"+
	    "<li><a href=\""+url+"LawFirm\">Law Firms</a></li>\n"+				
	    "</ul>"+
	    "<h3 class=\"titleBar\">Reports</h3>\n"+
	    "<ul>\n"+
	    "<li><a href=\""+url+"Report\">Reports</a></li>\n"+
	    "<li><a href=\""+url+"TortReport\">Tort Report</a></li>\n"+
	    "</ul>";
	if(user != null && user.isAdmin()){
	   ret += "<h3 class=\"titleBar\">Misc</h3>\n"+
	       "<ul>\n"+
	       "<li><a href=\""+url+"Notification\">Notification Scheduler</a></li>\n"+
	       "</ul>";	    
	}
	ret += "</div>";
	return ret;

    }
    //
    public final static double getTotalPayments(Statement stmt, 
						String id, 
						boolean debug){
	double ret = 0;
	String qq = " select sudm(amount) from riskPayments where id="+id;
	ResultSet rs = null;
	try{
	    if(debug){
		logger.debug(qq);
	    }
	    rs = stmt.executeQuery(qq);
	    if(rs.next()){
		ret = rs.getDouble(1);
	    }
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	}
	return ret;
    }


}























































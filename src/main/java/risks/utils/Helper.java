package risks.utils;
import java.io.*;
import java.sql.*;
import java.util.*;
import javax.sql.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.naming.*;
import javax.naming.directory.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.models.*;
import risks.lists.*;

/**
 *
 */

public class Helper{

    static int c_con = 0;
    public final static String dirArr[] = {"","E","N","S","W"};

    public final static String sudIdArr[] = { "",
	"A", "B", "D", "F", 
	"L", "R", "S", "U"};

    public final static String sudArr[] = { "",
	"Apartment", "Building", "Dept", "Floor",
	"Lot", "Room", "Suite",  "Unit"};

    public final static String strIdArr[] = {"",
	"AVE", "BND", "BLVD", 
	"CTR","CIR", "CT", "CRST",
	"DR",  "EXPY", "LN", "PIKE",
	"PKY", "PL",  "RD", "RDG","RUN",
	"SQ","ST",
	"TER", "TPKE", "TURN",
	"VLY","WAY"};
									  
    //
    public final static String strArr[] = {"",
	"Avenue","Bend", "Boulevard", 
	"Center","Circle", "Court","Crest",
	"Drive", "Expressway", "Lane", "Pike" ,
	"Parkway" ,"Place" ,"Road" ,"Ridge","Run",
	"Square","Street",
	"Terrace","Turnpike","Turn",
	"Valley","Way"};   
									
    //
    // xhtmlHeader.inc
    public final static String xhtmlHeaderInc = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
	"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">\n"+
	"<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">";
    static Logger logger = LogManager.getLogger(Helper.class);
    //
    // Non static variables
    //
    boolean debug;
    String [] deptIdArr = null;
    String [] deptArr = null;
    public final static Map<String, String>       mimeTypes = new HashMap<>();
    static {
        mimeTypes.put("image/gif",       "gif");
        mimeTypes.put("image/jpeg",      "jpg");
        mimeTypes.put("image/png",       "png");
        mimeTypes.put("image/tiff",      "tiff");
        mimeTypes.put("image/bmp",       "bmp");
        mimeTypes.put("text/plain",      "txt");
        mimeTypes.put("audio/x-wav",     "wav");
        mimeTypes.put("application/pdf", "pdf");
        mimeTypes.put("audio/midi",      "mid");
        mimeTypes.put("video/mpeg",      "mpeg");
        mimeTypes.put("video/mp4",       "mp4");
        mimeTypes.put("video/x-ms-asf",  "asf");
        mimeTypes.put("video/x-ms-wmv",  "wmv");
        mimeTypes.put("video/x-msvideo", "avi");
        mimeTypes.put("text/html",       "html");

        mimeTypes.put("application/mp4",               "mp4");
        mimeTypes.put("application/x-shockwave-flash", "swf");
        mimeTypes.put("application/msword",            "doc");
        mimeTypes.put("application/xml",               "xml");
        mimeTypes.put("application/vnd.ms-excel",      "xls");
        mimeTypes.put("application/vnd.ms-powerpoint", "ppt");
        mimeTypes.put("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "docx");
    }
		
    //
    // basic constructor
    public Helper(boolean deb){
	//
	// initialize
	//
	debug = deb;
    }
    public final static String findFileType(File file)
    {
        String fileType = "";
        try {
            String pp   = file.getAbsolutePath();
            Path   path = Paths.get(pp);
            fileType = Files.probeContentType(path);
            System.err.println(fileType);
        }
        catch (Exception ex) {
            System.err.println(" fle type excep " + ex);
        }
        return fileType;
    }
    public final static String getFileExtension(File file)
    {
        String ext = "";
        try {
            // name does not include path
            String name     = file.getName();
            String pp       = file.getAbsolutePath();
            Path   path     = Paths.get(pp);
            String fileType = Files.probeContentType(path);
            if (fileType != null) {
                // application/pdf
                if (fileType.endsWith("pdf")) {
                    ext = "pdf";
                }
                // image/jpeg
                else if (fileType.endsWith("jpeg")) {
                    ext = "jpg";
                }
                // image/gif
                else if (fileType.endsWith("gif")) {
                    ext = "gif";
                }
                // image/bmp
                else if (fileType.endsWith("bmp")) {
                    ext = "bmp";
                }
                // application/msword
                else if (fileType.endsWith("msword")) {
                    ext = "doc";
                }
                // application/vnd.ms-excel
                else if (fileType.endsWith("excel")) {
                    ext = "csv";
                }
                // application/vnd.openxmlformats-officedocument.wordprocessingml.document
                else if (fileType.endsWith(".document")) {
                    ext = "docx";
                }
                // text/plain
                else if (fileType.endsWith("plain")) {
                    ext = "txt";
                }
                // application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
                else if (fileType.endsWith(".sheet")) {
                    ext = "xlsx";
                }
                // audio/wav
                else if (fileType.endsWith("wav")) {
                    ext = "wav";
                }
                // text/xml
                else if (fileType.endsWith("xml")) {
                    ext = "xml";
                }
                else if (fileType.endsWith("html")) {
                    ext = "html";
                }
                // video/mng
                else if (fileType.endsWith("mng")) {
                    ext = "mng";
                }
                else if (fileType.endsWith("mpeg")) {
                    ext = "mpg";
                }
                // video/mp4
                else if (fileType.endsWith("mp4")) {
                    ext = "mp4";
                }
                else if (fileType.endsWith("avi")) {
                    ext = "avi";
                }
                else if (fileType.endsWith("mov")) {
                    ext = "mov";
                }
                // quick time video
                else if (fileType.endsWith("quicktime")) {
                    ext = "qt";
                }
                else if (fileType.endsWith("wmv")) {
                    ext = "wmv";
                }
                else if (fileType.endsWith("asf")) {
                    ext = "asf";
                }
                // flash video
                else if (fileType.endsWith("flash")) {
                    ext = "swf";
                }
                else if (fileType.startsWith("image")) {
                    ext = "jpg";
                } // if non of the above we check the file name
                else if (name.indexOf(".") > -1) {
                    ext = name.substring(name.lastIndexOf(".") + 1);
                }
            }
        }
        catch (Exception e) {
            System.err.println(e);
            System.err.println(" fle ext excep " + e);
        }
        return ext;
    }
    public final static String getFileExtensionFromName(String name)
    {
        String ext = "";
        try {
            if (name.indexOf(".") > -1) {
                ext = name.substring(name.lastIndexOf(".") + 1);
            }
        }
        catch (Exception ex) {

        }
        return ext;
    }		
    //
    /**
     * Adds escape character before certain characters
     *
     */
    public final static String escapeIt(String s) {
	StringBuffer safe = new StringBuffer(s);
	int len = s.length();
	int c = 0;
	boolean noSlashBefore = true;
	while (c < len) {                           
	    if ((safe.charAt(c) == '\'' ||
		 safe.charAt(c) == '"' ||
		 safe.charAt(c) == '%') && noSlashBefore){ 
		safe.insert(c, '\\');
		c += 2;
		len = safe.length();
	    }
	    else if(safe.charAt(c) == '\\'){
		noSlashBefore = false;
		c++;
	    }
	    else {
		noSlashBefore = true; // reset
		c++;
	    }
	}
	return safe.toString();
    }
    public final static String replaceIt(String s) {
	StringBuffer safe = new StringBuffer(s);
	int len = s.length();
	int c = 0;
	while (c < len) {                           
	    if (safe.charAt(c) == '\'' ||
		safe.charAt(c) == '"' ||
		safe.charAt(c) == '_') {
		safe.setCharAt(c,'%');
		// safe.insert(c, '%'); // replace by % for search purpose 
		c ++;
		// len = safe.length();
	    }
	    else {
		c++;
	    }
	}
	return safe.toString();
    }
    //
    // users are used to enter comma in numbers such as xx,xxx.xx
    // as we can not save this in the DB as a valid number
    // so we remove it 
    public final static String cleanNumber(String s) {

	if(s == null) return null;
	String ret = "";
	int len = s.length();
	int c = 0;
	int ind = s.indexOf(",");
	if(ind > -1){
	    ret = s.substring(0,ind);
	    if(ind < len)
		ret += s.substring(ind+1);
	}
	else
	    ret = s;
	return ret;
    }
    /**
     * replaces the special chars that has certain meaning in html
     *
     * @param s the passing string
     * @returns string the modified string
     */
    public final static String replaceSpecialChars(String s) {
	char ch[] ={'\'','\"','>','<'};
	String entity[] = {"&#39;","&#34;","&gt;","&lt;"};
	//
	// &#34; = &quot;

	String ret ="";
	int len = s.length();
	int c = 0;
	boolean in = false;
	while (c < len) {             
	    for(int i=0;i< entity.length;i++){
		if (s.charAt(c) == ch[i]) {
		    ret+= entity[i];
		    in = true;
		}
	    }
	    if(!in) ret += s.charAt(c);
	    in = false;
	    c ++;
	}
	return ret;
    }
    /**
     * adds another apostrify to the string if there is any next to it
     *
     * @param s the passing string
     * @returns string the modified string
     */
    public final String doubleApostrify(String s) {
	StringBuffer apostrophe_safe = new StringBuffer(s);
	int len = s.length();
	int c = 0;
	while (c < len) {                           
	    if (apostrophe_safe.charAt(c) == '\'') {
		apostrophe_safe.insert(c, '\'');
		c += 2;
		len = apostrophe_safe.length();
	    }
	    else {
		c++;
	    }
	}
	return apostrophe_safe.toString();
    }
    /**
     * Connect to Oracle database
     *
     * @param dbStr database connect string
     * @param dbUser database user string
     * @param dbPass database password string
     */
    public final static Connection databaseConnect(String dbStr, 
						   String dbUser, 
						   String dbPass) {
	Connection con=null;
	try {
	    Class.forName("oracle.jdbc.driver.OracleDriver");
	    con = DriverManager.getConnection(dbStr,
					      dbUser,dbPass);
	    c_con++;
	    logger.debug("Got connection: "+c_con);
	}
	catch (Exception sqle) {
	    System.err.println(sqle);
	}
	return con;
    }
    public final static Connection getConnection(){
	return getConnectionProd();
    }
    //	
    public final static Connection getConnectionProd(){

	Connection con=null;
	int trials = 0;
	boolean pass = false;
	while(trials < 3 && !pass){
		
	    try {
		trials++;
		Context initCtx = new InitialContext();
		Context envCtx = (Context) initCtx.lookup("java:comp/env");
		DataSource ds = (DataSource)envCtx.lookup("jdbc/MySQL_risk");
		con = ds.getConnection();
		if(con == null){
		    String str = " Could not connect to DB ";
		    logger.error(str);
		}
		else{
		    pass = testCon(con);
		    if(pass){
			c_con++;
			logger.debug("Got connection: "+c_con);
			logger.debug("Got connection at try "+trials);
		    }
		}
	    }
	    catch (Exception sqle) {
		logger.error(sqle);
	    }
	}
	return con;
    }
    //
    final static boolean testCon(Connection con){
		
	boolean pass = false;
	Statement stmt  = null;
	ResultSet rs = null;
	String qq = "select 1+1";		
	try{
	    if(con != null){
		stmt = con.createStatement();
		logger.debug(qq);
		rs = stmt.executeQuery(qq);
		if(rs.next()){
		    pass = true;
		}
	    }
	    rs.close();
	    stmt.close();
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	}
	return pass;
    }	
    /**
     * Disconnect the database and related statements and result sets
     * 
     * @param con
     * @param stmt
     * @param rs
     */
    public final static void databaseDisconnect(Connection con,Statement stmt,
						ResultSet rs) {
	try {
	    if(rs != null) rs.close();
	    rs = null;
	    if(stmt != null) stmt.close();
	    stmt = null;
	    if(con != null) con.close();
	    con = null;
	    logger.debug("Closed Connection "+c_con);
	    c_con--;
	    if(c_con < 0) c_con = 0;
	}
	catch (Exception e) {
	    e.printStackTrace();
	    logger.error(e);
	}
	finally{
	    if (rs != null) {
		try { rs.close(); } catch (SQLException e) { ; }
		rs = null;
	    }
	    if (stmt != null) {
		try { stmt.close(); } catch (SQLException e) { ; }
		stmt = null;
	    }
	    if (con != null) {
		try { con.close(); } catch (SQLException e) { ; }
		con = null;
	    }
	}
    }
    public final static void databaseDisconnect(Connection con,
						ResultSet rs,
						Statement... stmt) {
	try {
	    if(rs != null) rs.close();
	    rs = null;
	    if(stmt != null){
		for(Statement one:stmt){
		    if(one != null)
			one.close();
		    one = null;
		}
	    }
	    if(con != null) con.close();
	    con = null;
	    logger.debug("Closed Connection "+c_con);
	    c_con--;
	    if(c_con < 0) c_con = 0;
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	finally{
	    if (rs != null) {
		try { rs.close(); } catch (SQLException e) { }
		rs = null;
	    }
	    if (stmt != null) {
		try {
		    for(Statement one:stmt){										
			if(one != null)
			    one.close(); 
			one = null;
		    }
		} catch (SQLException e) { }
	    }
	    if (con != null) {
		try { con.close(); } catch (SQLException e) { }
		con = null;
	    }
	}
    }		
    /**
     * Write the number in bbbb.bb format needed for currency.
     * = toFixed(2)
     * @param dd the input double number
     * @returns the formated number as string
     */
    public final static String formatNumber(double dd){
	//
	String str = ""+dd;
	String ret="";
	int l = str.length();
	int i = str.indexOf('.');
	int r = i+3;  // required length to keep only two decimal
	if(i > -1 && r<l){
	    ret = str.substring(0,r);
	}
	else{
	    ret = str;
	}
	return ret;
    }
    //
    // format a number with only 2 decimal
    // usefull for currency numbers
    //
    public final static String formatNumber(String that){

	int ind = that.indexOf(".");
	int len = that.length();
	String str = "";
	if(ind == -1){  // whole integer
	    str = that + ".00";
	}
	else if(len-ind == 2){  // one decimal
	    str = that + "0";
	}
	else if(len - ind > 3){ // more than two
	    str = that.substring(0,ind+3);
	}
	else str = that;

	return str;
    }

    //
    // main page banner
    //
    public final static String banner(String url){

	String banner = "<head>\n"+
	    "<meta http-equiv=\"Content-Type\" content=\"application/xhtml+xml; charset=utf-8\" />\n"+
	    "<meta http-equiv=\"Content-Script-Type\" content=\"text/javascript\" />\n"+
	    "<link rel=\"SHORTCUT ICON\" href=\"/favicon.ico\" />\n"+
	    "<style type=\"text/css\" media=\"screen\">\n"+
	    "@import url(\"/skins/default/skin.css\");\n"+
	    "</style>\n"+
	    "<style type=\"text/css\" media=\"print\">@import url(\"/skins/default/print.css\");</style>\n"+
	    "<script src=\"/functions.js\" type=\"text/javascript\"></script>\n"+
	    "<title>RiskTrack - City of Bloomington, Indiana</title>\n"+
	    "</head>\n"+
	    "<body>\n"+
	    "<div id=\"banner\">\n"+
	    "<h1><a href=\""+url+"RiskTrack\">RiskTrack</a></h1><h2>City of Bloomington, Indiana</h2>\n"+
	    "</div>";
	return banner;
    }
    //
    public final static String menuBar(String url, boolean logged){
	String menu = "<div class=\"menuBar\">\n"+
	    "<a href=\""+url+"RiskTrack\">Home</a>\n"+
	    "<a href=\""+url+"RiskTrack/status.html\">Status</a>\n";
	if(logged){
	    menu += "<a href=\""+url+"RiskTrack/Logout\">Logout</a>\n";
	}
	menu += "</div>\n";
	return menu;
    }
    //
    // Non static methods and variables
    //
    public String[] getDeptIdArr(){
	return deptIdArr;
    }
    public String[] getDeptArr(){
	return deptArr;
    }
    /**
     * given dept id, returns the dept name.
     *
     * @param array of departments ids
     * @param array of departments names
     * @param specific dept id
     * @returns dept name
     */
    public final static String getDeptName(String[] deptIdArr, String[] deptArr, String id){
	String ret = "";
	if(deptArr != null){
	    for(int i=0;i<deptArr.length;i++){
		if(deptIdArr[i].equals(id)){
		    ret = deptArr[i];
		    break;
		}
	    }
	}
	return ret;
    }
    //
    public final static String getToday(){

	String day="",month="",year="";
	Calendar current_cal = Calendar.getInstance();
	int mm =  (current_cal.get(Calendar.MONTH)+1);
	int dd =   current_cal.get(Calendar.DATE);
	year = ""+ current_cal.get(Calendar.YEAR);
	if(mm < 10) month = "0";
	month += mm;
	if(dd < 10) day = "0";
	day += dd;
	return month+"/"+day+"/"+year;
    }
    /**
     * gets matching list from ldap for a given substring of a user name.
     * @param subid the substring of the userid, subid has to be at least 
     * two characters otherwise the ldap will hang for some reason.
     * @return a list of matching caases.
     */
    public final static String[] getMatchList(String subid, EnvBean bean){
	Vector vec = new Vector();
	String userid = "";
	vec.add(userid);		
	//
	Hashtable env = new Hashtable();
	if (!connectToServer(env, bean)){
	    System.err.println("Error Connecting to LDAp Server");
	    return null;
	}		
	try{
	    DirContext ctx = new InitialDirContext(env);
	    SearchControls ctls = new SearchControls();
	    String[] attrIDs = {"cn"};
	    //
	    ctls.setReturningAttributes(attrIDs);
	    ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
	    String filter = "(cn="+subid+"*)";
	    NamingEnumeration answer = ctx.search("", filter, ctls);
	    while(answer.hasMore()){
		//
		SearchResult sr = (SearchResult)(answer.next());
		Attributes atts = sr.getAttributes();

		Attribute user = (Attribute)(atts.get("cn"));
		if (user != null){
		    userid = user.get().toString();
		    vec.add(userid);
		}
	    }
	}
	catch(Exception ex){
	    logger.error(ex);
	}
	int size = vec.size();
	String str[] = new String[size];
	for(int i=0;i<size;i++){
	    str[i] = (String) vec.get(i);
	}
	vec = null;
	return str;		
    }
    /*
     *
     */
	
    public final static boolean connectToServer(Hashtable env, EnvBean bean){

	if(env != null && bean != null){
	    env.put(Context.INITIAL_CONTEXT_FACTORY, 
		    "com.sun.jndi.ldap.LdapCtxFactory");
	    env.put(Context.PROVIDER_URL, bean.getUrl());
	    env.put(Context.SECURITY_AUTHENTICATION, "simple"); 
	    env.put(Context.SECURITY_PRINCIPAL, bean.getPrinciple());
	    env.put(Context.SECURITY_CREDENTIALS, bean.getPassword());
	    return true;
	}
	return false;
    }
	
    //
    // initial cap a word
    //
    public final static String initCapWord(String str_in){
	String ret = "";
	if(str_in !=  null){
	    if(str_in.length() == 0) return ret;
	    else if(str_in.length() > 1){
		ret = str_in.substring(0,1).toUpperCase()+
		    str_in.substring(1).toLowerCase();
	    }
	    else{
		ret = str_in.toUpperCase();   
	    }
	}
	// System.err.println("initcap "+str_in+" "+ret);
	return ret;
    }
    //
    // init cap a phrase
    //
    public final static String initCap(String str_in){
	String ret = "";
	if(str_in != null){
	    if(str_in.indexOf(" ") > -1){
		String[] str = str_in.split("\\s"); // any space character
		for(int i=0;i<str.length;i++){
		    if(i > 0) ret += " ";
		    ret += initCapWord(str[i]);
		}
	    }
	    else
		ret = initCapWord(str_in);// it is only one word
	}
	return ret;
    }
    public final static User findUserFromList(List users, String empid){
	User foundUser = null;
	if(users != null && users.size() > 0){ 
	    for (int i=0;i<users.size();i++){
		User user = (User)users.get(i);
		if(user != null && user.getUserid().equals(empid)){
		    foundUser = user;
		    break;
		}
	    }
	}
	return foundUser;
    }

    public final static String findPercent(int s1, int total){
	String ret = "0";
	if(total > 0){
	    ret = ""+(int)(s1*100./total+0.5);
	}
	return ret;
    }
    public final static void printInsurance(PrintWriter out,
				     Insurance insr,
				     String url,
				     String relatedId,
				     String legalType,
				     String opener){
		
	out.println("<tr><th colspan=3>Insurance Info</th>");
	out.println("<tr><td>");
	out.println("<label> Status: </label>"+
		    "</td><td> "+insr.getStatus());
	out.println("</td><td><label>Policy: </label>");
	out.println(insr.getPolicy());
	out.println("<label>Policy #</label>");
	out.println(insr.getPolicy_num());
	out.println("&nbsp;&nbsp;<input name='action' "+
		    "type='button' value='Edit Insurance' "+
		    "tabindex='33' onClick=\"window.open('"+
		    url+
		    "InsuranceServ?type="+insr.getType()+"&id="+insr.getId()+
		    "&relatedId="+relatedId+
		    "&opener="+opener+
		    "&legalType="+legalType+
		    "&action=zoom"+
		    "','Insurance','toolbar=0,location=0,"+
		    "directories=0,status=0,menubar=0,"+
		    "scrollbars=0,top=200,left=200,"+
		    "resizable=1,width=600,height=600');return false;\" />");
	out.println("</td></tr>");
	out.println("<tr><td><label> Insurance Company: </label></td><td>"+
		    insr.getCompany());
	out.println("</td><td><label>Phone:</label>"+insr.getPhone());
	out.println("</td></tr>");
	out.println("<tr><td>");				
	out.println("<label>Claim #: </label></td><td>"+
		    insr.getClaimNum());
	out.println("</td></tr>");
	out.println("<tr><td><label>Deductible</label></td><td>"+insr.getDeductible());
	out.println("</td><td><label>Amount Paid by Insurance: </label>");
	out.println(insr.getAmountPaid());
	out.println("</td></tr>");
	out.println("<tr><td><label>Date, Sent to Insurance</label></td><td>"+insr.getSent());
	out.println("</td><td><label>Insurance Decision Date</label></td><td>");
	out.println(insr.getDecisionDate());
	out.println("</td></tr>");		
	//
	if(insr.hasAdjuster()){
	    out.println("<tr><td><label>Adjuster's Name: </label>"+
			"</td><td>"+insr.getAdjuster());
	    out.println("</td><td><label> Phone: </label>"+
			insr.getAdjusterPhone());
	    out.println("</td></tr>");
	}
	if(!insr.getAdjusterEmail().equals("")){
	    out.println("<tr><td><label>Adjuster "+
			"Email: </label></td><td colspan=2>"+
			insr.getAdjusterEmail());
	    out.println("</td></tr>");
	}
	if(insr.hasAttorney()){
	    out.println("<tr><td><label>Attorney:</label></td><td>"+
			insr.getAttorney());
	    out.println("</td><td><label>Phone: </label>"+
			insr.getAttorneyPhone());
	    out.println("</td></tr>");
	}
	if(!insr.getAttorneyEmail().equals("")){
	    out.println("<tr><td><label>Attorney Eamil: </label>"+
			"</td><td colspan=2>"+
			insr.getAttorneyEmail());
	    out.println("</td></tr>");
	}
	out.println("<tr><td>&nbsp;</td></tr>");
    }
    public final static void printEmployee(PrintWriter out,
				    Employee emp,
				    String url,
				    String relatedId,
				    String opener){
	Department dept = emp.getDepartment();
	out.println("<tr><th colspan=3>Employee Info</th>");
	out.println("<tr><td>");
	out.println("<label> Name: </label>"+
		    "</td><td> "+emp.getFullName());
	out.println("</td><td>");
	out.println("&nbsp;&nbsp;<input name='action' "+
		    "type='button' value='Edit/Delete Employee' "+
		    "tabindex='33' onClick=\"window.open('"+
		    url+
		    "GetEmpInfoServ?id="+emp.getId()+
		    "&relatedId="+relatedId+
		    "&opener="+opener+
		    "','Employee','toolbar=0,location=0,"+
		    "directories=0,status=0,menubar=0,"+
		    "scrollbars=0,top=200,left=200,"+
		    "resizable=1,width=500,height=400');return false;\" />");
	out.println("</td></tr>");
	out.println("<tr><td><label>Job Title: </label>"+
		    emp.getTitle());
	out.println("</td><td><label>Phone: </label>"+emp.getPhone());
	out.println("</td></tr>");
	out.println("<tr><td><label>Department: </label>"+dept.getName());
	out.println("</td><td><label>Supervisor: </label>");
	out.println(emp.getSupervisor());
	out.println("</td></tr>");
	out.println("<tr><td><label>Dept Phone</label></td><td>"+emp.getDeptPhone());
	out.println("</td></tr>");		
	//
	out.println("<tr><td>&nbsp;</td></tr>");
    }
    public final static void printNotes(PrintWriter out,
				 String url,
				 String opener,
				 List<Note> notes){
	if(notes == null || notes.size() == 0) return;
	out.println("<fieldset><legend>Related Notes</legend>");
	out.println("<table>");
	out.println("<tr><th>Date</th><th>Added by</th><th>Notes</th><th>Action<th></tr>");
	for(Note one:notes){
	    out.println("<tr><td>"+one.getDate()+"</td><td>"+one.getUser()+"</td><td>"+one.getNoteText()+"</td><td>");
	    out.println("<input type=\"button\" value=\"Edit Note\" "+
			"onclick=\"window.open('" + url + 
			"NoteServ?id="+one.getId()+"&opener="+opener+"','Notes',"+
			"'location=0:0,menubar=1,width=500,height=400,toolbar=1,scrollbars=1');return false;\"></input>");

	    out.println("</td></tr>");
	}
	out.println("</table>");
	out.println("</fieldset>");
    }
    public final static void printFiles(PrintWriter out,
				 String url,
				 List<RiskFile> files){
	if(files == null || files.size() == 0) return;
	out.println("<fieldset><legend>Related Files</legend>");
	out.println("<table>");
	out.println("<tr><th>ID</th><th>Date</th><th>Added by</th><th>File Name</th><th>Notes<th></tr>");
	for(RiskFile one:files){
	    out.println("<tr>"+
			"<td><a href=\""+url+"RiskFileServ?id="+one.getId()+"\">"+one.getId()+"</a></td>"+			
			"<td>"+one.getDate()+"</td>"+
			"<td>"+one.getAddedBy()+"</td>"+
			"<td><a href=\""+url+"RiskFileServ?action=download&id="+one.getId()+"\">"+one.getOldName()+"</a></td>"+
			"<td>"+one.getNotes()+"</td></tr>");
	}
	out.println("</table>");
	out.println("</fieldset>");
    }		

	
}























































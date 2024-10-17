package risks.models;

import java.util.*;
import java.sql.*;
import java.io.*;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.lists.*;
import risks.utils.Helper;

public class TortClaim extends Risk{

    static Logger logger = LogManager.getLogger(TortClaim.class);
    // insurance variables are not needed here, will be deleted
    String claimNum="", 
	sent="", 
	insurDecision="", insurer="",
	insuranceStatus="";
	
    String	type="", 
	deductible="", recovered="", potential="", expires="",
	status="Open",
	comments="",  law_firm_id="",
	cityTotalCost="",paidByCity2City="",paidByInsur2City="",
	deductible2="",

	opened="",
	recordOnly="",
	otherType="", closed=""; // dates
    
    String incident = "", // instead of location
	paidByCity = "",paidByInsur = "",settled = "",    // double
	requestAmount = "", miscByCity = "", cityAutoInc="",
	filed="", subInsur="", paidByRisk="";

    String received="",
	incidentDate="";	

    // added on 8/22/2024
    String denialLetterDate="", deadlineDate=""; // dates
    String lawsuit="", bodilyInvolved=""; // checkbox
    RiskType riskType = null;
    List<Employee> employees = null;
    List<Insurance> insurances = null;
    List<Auto> autos = null;
    RelatedDeptList relatedDepts = null;
    List<RiskPerson> claiments = null;
    List<RiskPerson> witnesses = null;
    //
    // basic constructor
    //
    public TortClaim(boolean deb){

	debug = deb;
	//
	// initialize
	//
    }
    public TortClaim(boolean deb, String val){

	debug = deb;
	setId(val);
    }
    public TortClaim(boolean deb,
		     String val,
		     String val2,
		     String val3,
		     String val4,
		     String val5,
		     String val6,
		     String val7,
		     String val8,
		     String val9,
		     String val10,
		     String val11,
		     String val12,
		     String val13,
		     String val14,
		     String val15,
		     String val16,
		     String val17,
		     String val18,
		     String val19,
		     String val20,
		     String val21,
		     String val22,
		     String val23,
		     String val24,
		     String val25,
		     String val26,
		     String val27,

		     String val28,
		     String val29,
		     boolean val30,
		     boolean val31
		     ){
	debug = deb;
	setVals(val, val2, val3, val4, val5, val6, val7, val8, val9, val10,
		val11, val12, val13, val14, val15, val16, val17, val18, val19, val20,
		val21, val22, val23, val24, val25, val26, val27,
		val28,val29,val30,val31
		);
    }
    void setVals(
		 String val,
		 String val2,
		 String val3,
		 String val4,
		 String val5,
		 String val6,
		 String val7,
		 String val8,
		 String val9,
		 String val10,
		 String val11,
		 String val12,
		 String val13,
		 String val14,
		 String val15,
		 String val16,
		 String val17,
		 String val18,
		 String val19,
		 String val20,
		 String val21,
		 String val22,
		 String val23,
		 String val24,
		 String val25,
		 String val26,
		 String val27,

		 String val28,
		 String val29,
		 boolean val30,
		 boolean val31		 
		 ){
		
	setId(val);
	setType(val2);
	setStatus(val3);
	setDeductible(val4);
	setPaidByCity(val5);
				
	setPaidByInsur(val6);
	setRequestAmount(val7);
	setSettled(val8);						
	setMiscByCity(val9);
	setCityAutoInc(val10);
	setIncidentDate(val11);
						
	setIncident(val12);
	setComments(val13);
	setOpened(val14);   
	setReceived(val15);
				
	setClosed(val16);
	setFiled(val17);
	setSubInsur(val18);
	setExpires(val19);
	setCityTotalCost(val20);
				
	setPaidByCity2City(val21);
	setPaidByInsur2City(val22);
	setDeductible2(val23);
	setOtherType(val24);
	setRecordOnly(val25);
				
	setPaidByRisk(val26);
	setLawFirmId(val27);

	setDenialLetterDate(val28);
	setDeadlineDate(val29);
	setLawsuit(val30);
	setBodilyInvolved(val31);
    }		
		
    //
    // setters
    //
    public void setType(String val){
	if(val != null)
	    type = val;
    }
    public void setStatus(String val){
	if(val != null)
	    status = val;
    }
    public void setClaimNum(String val){
	if(val != null)
	    claimNum = val;
    }
    public void setClosed(String val){
	if(val != null)
	    closed = val;
    }
    public void setDeductible(String val){
	if(val != null)
	    deductible = Helper.cleanNumber(val);
    }
    public void setDeductible2(String val){
	if(val != null)
	    deductible2 = Helper.cleanNumber(val);
    }
    public void setRecovered(String val){
	if(val != null)
	    recovered = Helper.cleanNumber(val);
    }
    public void setRequestAmount(String val){
	if(val != null)
	    requestAmount = Helper.cleanNumber(val);
    }
    public void setSettled(String val){
	if(val != null)
	    settled = val;
    }
    public void setExpires(String val){
	if(val != null)
	    expires = val;
    }
    public void setPaidByCity(String val){
	if(val != null)
	    paidByCity = Helper.cleanNumber(val);
    }
    public void setPaidByInsur(String val){
	if(val != null)
	    paidByInsur = Helper.cleanNumber(val);
    }
    public void setPaidByCity2City(String val){
	if(val != null)
	    paidByCity2City = Helper.cleanNumber(val);
    }
    public void setPaidByInsur2City(String val){
	if(val != null)
	    paidByInsur2City = Helper.cleanNumber(val);
    }
    public void setMiscByCity(String val){
	if(val != null)
	    miscByCity = Helper.cleanNumber(val);
    }
    public void setCityAutoInc(String val){
	if(val != null)
	    cityAutoInc = val; 
    }
    public void setCityTotalCost(String val){
	if(val != null)
	    cityTotalCost = Helper.cleanNumber(val);
    }
    public void setDebug(boolean val){
	debug = val;
    }
    public void setIncidentDate(String val){
	if(val != null)
	    incidentDate = val;
    }	
    public void setIncident(String val){
	if(val != null)
	    incident = val;
    }
    public void setComments(String val){
	if(val != null)
	    comments = val;
    }
    public void setOpened(String val){
	if(val != null)
	    opened = val;
    }
    public void setSent(String val){
	if(val != null)
	    sent = val;
    }
    public void setInsuranceStatus(String val){
	if(val != null)
	    insuranceStatus = val;
    }
    public void setReceived(String val){
	if(val != null)
	    received = val;
    }
    public void setOtherType(String val){
	if(val != null)
	    otherType = val; 
    }
    public void setFiled(String val){
	if(val != null)
	    filed = val;
    }
    public void setInsurer(String val){
	if(val != null)
	    insurer = val;
    }
    public void setSubInsur(String val){
	if(val != null)
	    subInsur = val;
    }
    public void setInsurDecision(String val){
	if(val != null)
	    insurDecision = val;
    }
    public void setRecordOnly(String val){
	if(val != null)
	    recordOnly = val; 
    }
    public void setPaidByRisk(String val){
	if(val != null)
	    paidByRisk = val;
    }
    public void setLawFirmId(String val){
	if(val != null)
	    law_firm_id = val;
    }
    public void setDenialLetterDate(String val){
	if(val != null)
	    denialLetterDate = val;
    }
    public void setDeadlineDate(String val){
	if(val != null)
	    deadlineDate = val;
    }
    public void setLawsuit(boolean val){
	if(val)
	    lawsuit = "y";
    }
    public void setBodilyInvolved(boolean val){
	if(val)
	    bodilyInvolved = "y";
    }
    //
    // getters
    //
    public String  getLawFirmId(){
	return law_firm_id;
    }		
    public String  getPaidByRisk(){
	return paidByRisk;
    }
    public String  getType(){
	return type;
    }
    public String  getStatus(){
	return status;
    }
    public String  getPaidByCity(){
	return paidByCity;
    }
    public String  getPaidByInsur(){
	return paidByInsur;
    }
    public String  getMiscByCity(){
	return miscByCity;
    }
    public String  getCityAutoInc(){
	return cityAutoInc;
    }
    public String  getPaidByCity2City(){
	return paidByCity2City;
    }
    public String  getPaidByInsur2City(){
	return paidByInsur2City;
    }
    public String  getCityTotalCost(){
	return cityTotalCost;
    }
    public String  getClaimNum(){
	return claimNum;
    }
    public String  getDeductible(){
	return deductible;
    }
    public String  getDeductible2(){
	return deductible2;
    }
	
    public String  getRecovered(){
	return recovered;
    }
    public String  getRequestAmount(){
	return requestAmount;
    }
    public String  getSettled(){
	return settled;
    }
    public String  getIncidentDate(){
	return incidentDate;
    }
    public String  getIncident(){
	return incident;
    }
    public String  getComments(){
	return comments;
    }
    public String  getOpened(){
	return opened;
    }
    public String  getSent(){
	return sent;
    }
    public String  getReceived(){
	return received;
    }
    public String  getInsuranceStatus(){
	return insuranceStatus;
    }
    public String  getClosed(){
	return closed;
    }
    public String  getOtherType(){
	return otherType;
    }
    public String  getFiled(){
	return filed;
    }
    public String  getInsurer(){
	return insurer;
    }
    public String  getSubInsur(){
	return subInsur;
    }
    public String  getInsurDecision(){
	return insurDecision;
    }
    public String  getExpires(){
	return expires;
    }
    public String  getRecordOnly(){
	return recordOnly;
    }
    public String getDenialLetterDate(){
	return denialLetterDate;
    }
    public String getDeadlineDate(){
	return deadlineDate;
    }
    public boolean getLawsuit(){
	return !lawsuit.equals("");
    }
    public boolean getBodilyInvolved(){
	return !bodilyInvolved.isEmpty();
    }
    
    public String toString(){
	return id;
    }
    public boolean equals(Object o) {
	if (o instanceof TortClaim) {
	    TortClaim c = (TortClaim) o;
	    if ( this.id.equals(c.getId())) 
		return true;
	}
	return false;
    }
    public int hashCode(){
	int seed = 37;
	if(!id.equals("")){
	    try{
		seed += Integer.parseInt(id)*31;
	    }catch(Exception ex){
		// we ignore
	    }
	}
	return seed;
    }		
    public List<Employee>  getEmployees(){
	if(employees == null){
	    EmployeeList el = new EmployeeList(debug, id);
	    String back = el.lookFor();
	    if(back.equals("")){
		employees = el.getEmployees();
	    }
	}
	return employees;
    }
    public List<Insurance>  getInsurances(){
	if(insurances == null){
	    InsuranceList il = new InsuranceList(debug, id);
	    String back = il.lookFor();
	    if(back.equals("")){
		insurances = il.getInsurances();
	    }
	}
	return insurances;
    }
    public List<Auto>  getAutos(){
	if(autos == null){
	    AutoList il = new AutoList(debug, id);
	    String back = il.lookFor();
	    if(back.equals("")){
		autos = il.getAutos();
	    }
	}
	return autos;
    }
    public List<RelatedDept> getRelatedDepts(){
	if(relatedDepts == null && !id.equals("")){
	    RelatedDeptList rdl = new RelatedDeptList(debug, id);
	    String back = rdl.find();
	    if(back.equals("") && rdl.size() > 0){
		relatedDepts = rdl;
	    }
	    else{
		System.err.println(back);
	    }
	}
	return relatedDepts;
    }
    public List<RiskPerson> getClaiments(){
	if(claiments == null && !id.equals("")){
	    PersonList sp = new PersonList(debug);
	    sp.setRisk_id(id);
	    sp.setClaimPersonOnly();
	    String back = sp.lookFor();
	    if(back.equals("")){
		List<RiskPerson> ones = sp.getPersons();
		if(ones != null &&  ones.size() > 0){
		    claiments = ones;
		}
	    }
	}
	return claiments;
    }
    public String getClaimentNames(){
	String all = "";
	getClaiments();
	if(claiments != null && claiments.size() > 0){
	    for(RiskPerson one:claiments){
		if(!all.isEmpty()) all += ";";
		all += one.getFullName();
	    }
	}
	return all;
    }
    public RiskType getRiskType(){
	if(!type.isEmpty() && riskType == null){
	    RiskType rt = new RiskType(debug);
	    rt.setId(type);
	    String back = rt.doSelect();
	    if(back.isEmpty()){
		riskType = rt;
	    }
	}
	return riskType;
    }
    public boolean hasClaiments(){
	getClaiments();
	return claiments != null && claiments.size() > 0;
    }
    public List<RiskPerson> getWitnesses(){
	if(witnesses == null && !id.equals("")){
	    PersonList sp = new PersonList(debug);
	    sp.setRisk_id(id);
	    sp.setWitnessPersonOnly();
	    String back = sp.lookFor();
	    if(back.equals("")){
		List<RiskPerson> ones = sp.getPersons();
		if(ones != null &&  ones.size() > 0){
		    witnesses = ones;
		}
	    }
	}
	return witnesses;
    }
    public boolean hasWitnesses(){
	getWitnesses();
	return witnesses != null && witnesses.size() > 0;
    }
    /**
     * all records updated for deadlineDate with 90 day from date received
       update tortClaims set deadlineDate = DATE_ADD(received, INTERVAL 90 DAY) where recordOnly is null;
       
     */
    //
    // save a new record in the database
    // return "" or any exception thrown by DB
    //
    public String doSave(){

	String back = "";
	Connection con = null;
	PreparedStatement stmt = null, stmt2=null, stmt3=null, stmt4=null;
	ResultSet rs = null;
		
	String qq = "insert into risk_sequences values(0,'claim')";
	String qq2 = "update tortClaims set deadlineDate = DATE_ADD(received, INTERVAL 90 DAY) where recordOnly is null and id=? ";
	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not cnnnect to DB ";
	    return back;
	}											
	try{
	    stmt = con.prepareStatement(qq);
	    stmt.executeUpdate();
	    //
	    // get the id of the new record
	    //
	    qq = "select LAST_INSERT_ID() ";
	    if(debug){
		logger.debug(qq);
	    }
	    stmt2 = con.prepareStatement(qq);
	    rs = stmt2.executeQuery();
	    if(rs.next()){
		id = rs.getString(1);
	    }
	    qq = "insert into tortClaims values ("+
		"?,?,?,?,?, ?,?,?,?,?,"+
		"?,?,?,?,?, ?,?,?,?,?,"+
		"?,?,?,?,?, ?,?, ?,?,?,?)";
	    logger.debug(qq);
	    stmt3 = con.prepareStatement(qq);
	    back = setParams(stmt3, true);
	    if(back.equals(""))
		stmt3.executeUpdate();
	    if(deadlineDate.isEmpty()){
		qq = qq2;
		stmt4 = con.prepareStatement(qq);
		stmt4.setString(1, id);
		stmt4.executeUpdate();
		doSelect();
	    }
	}
	catch(Exception ex){
	    logger.error(ex+" : "+qq);
	    back =  ex.toString()+":"+qq;
	}
	finally{
	    Helper.databaseDisconnect(con, rs, stmt, stmt2, stmt3, stmt4);
	}
	return back;
    }						
    String setParams(PreparedStatement stmt, boolean forSave){
	String back = "";
	int jj=1;
	try{
	    if(forSave){
		stmt.setString(jj++, id);
	    }
	    stmt.setString(jj++, type);
	    if(type.equals("6")){ // bodilyInjury
		bodilyInvolved = "y";
	    }
	    if(!closed.isEmpty()){ // closed date
		status="Closed";
	    }
	    stmt.setString(jj++, status);						
	    if(deductible.equals("")){
		stmt.setString(jj++, "0");								
	    }
	    else {
		stmt.setString(jj++, deductible);		
	    }
	    if(paidByCity.equals("")){
		stmt.setString(jj++, "0");	
	    }
	    else {
		stmt.setString(jj++,	paidByCity);
	    }
	    if(paidByInsur.equals("")){
		stmt.setString(jj++, "0");	
	    }
	    else {
		stmt.setString(jj++,paidByInsur);
	    }
	    if(requestAmount.equals("")){
		stmt.setString(jj++, "0");	
	    }
	    else {
		stmt.setString(jj++, requestAmount);
	    }
	    if(settled.equals("")){
		stmt.setString(jj++, "0");	
	    }
	    else {
		stmt.setString(jj++, settled);
	    }
	    if(miscByCity.equals("")){
		stmt.setString(jj++, "0");	
	    }
	    else {
		stmt.setString(jj++, miscByCity);
	    }
	    if(cityAutoInc.equals("")){
		stmt.setNull(jj++, Types.CHAR);
	    }
	    else {
		stmt.setString(jj++, "y");
	    }
	    if(incidentDate.equals(""))
		stmt.setNull(jj++, Types.DATE);
	    else{
		java.util.Date date_tmp = df.parse(incidentDate);
		stmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }
	    if(incident.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++,incident);
	    }
	    if(comments.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++,comments);
	    }
	    if(opened.equals(""))
		stmt.setNull(jj++, Types.DATE);
	    else{
		java.util.Date date_tmp = df.parse(opened);
		stmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));

	    }
	    if(received.equals(""))
		stmt.setNull(jj++, Types.DATE);
	    else{
		java.util.Date date_tmp = df.parse(received);
		stmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }
	    if(closed.equals(""))
		stmt.setNull(jj++, Types.DATE);
	    else{
		java.util.Date date_tmp = df.parse(closed);
		stmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }
	    if(filed.equals("")){
		stmt.setNull(jj++, Types.CHAR);
	    }
	    else {
		stmt.setString(jj++,"y");
	    }
	    if(subInsur.equals("")){
		stmt.setNull(jj++, Types.CHAR);
	    }
	    else {
		stmt.setString(jj++,"y");
	    }
	    if(expires.equals(""))
		stmt.setNull(jj++, Types.DATE);
	    else{
		java.util.Date date_tmp = df.parse(expires);
		stmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }
	    if(cityTotalCost.equals("")){
		stmt.setString(jj++, "0");	
	    }
	    else {
		stmt.setString(jj++,cityTotalCost);
	    }
	    if(paidByCity2City.equals("")){
		stmt.setString(jj++, "0");									
	    }
	    else {
		stmt.setString(jj++,paidByCity2City);
	    }
	    if(paidByInsur2City.equals("")){
		stmt.setString(jj++, "0");	
	    }
	    else {
		stmt.setString(jj++, paidByInsur2City);
	    }
	    if(deductible2.equals("")){
		stmt.setString(jj++, "0");	
	    }
	    else {
		stmt.setString(jj++, deductible2);
	    }
	    if(otherType.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++, otherType);
	    }
	    if(recordOnly.equals("")){
		stmt.setNull(jj++, Types.CHAR);
	    }
	    else {
		stmt.setString(jj++, "y");
	    }
	    if(paidByRisk.equals("")){
		stmt.setString(jj++, "0");									
	    }
	    else {
		stmt.setString(jj++,paidByRisk);
	    }
	    if(law_firm_id.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++, law_firm_id);
	    }						
	    if(denialLetterDate.isEmpty())
		stmt.setNull(jj++, Types.DATE);
	    else{
		java.util.Date date_tmp = df.parse(denialLetterDate);
		stmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }
	    if(deadlineDate.equals(""))
		stmt.setNull(jj++, Types.DATE);
	    else{
		java.util.Date date_tmp = df.parse(deadlineDate);
		stmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }
	    if(lawsuit.isEmpty()){
		stmt.setNull(jj++, Types.CHAR);
	    }
	    else {
		stmt.setString(jj++, "y");
	    }
	    if(bodilyInvolved.isEmpty()){
		stmt.setNull(jj++, Types.CHAR);
	    }
	    else {
		stmt.setString(jj++, "y");
	    }	    	    
	}
	catch(Exception ex){
	    logger.error(ex);
	}
	return back;
    }

    public String doUpdate(){
	Connection con = null;
	PreparedStatement stmt = null, stmt2=null;		
	ResultSet rs = null;
	String str="", back="";
	String qq = "";
	qq = "update tortClaims set "+
	    "type=?,status=?,deductible=?,paidByCity=?,paidByInsur=?,"+
	    "requestAmount=?,settled=?,miscByCity=?,cityAutoInc=?,incidentDate=?,"+
	    "incident=?,comments=?,opened=?,received=?,closed=?,"+
	    "filed=?,subInsur=?,expires=?,cityTotalCost=?,paidByCity2City=?,"+
	    "paidByInsur2City=?,deductible2=?,otherType=?,recordOnly=?,paidByRisk=?,"+
	    "law_firm_id=?, "+
	    "denialLetterDate=?,deadlineDate=?,"+
	    "lawsuit=?,bodilyInvolved=? "+
	    "where id=? ";
	String qq2 = "update tortClaims set deadlineDate = DATE_ADD(received, INTERVAL 90 DAY) where recordOnly is null and id=? ";	
	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not cnnnect to DB ";
	    return back;
	}										
	try{
	    stmt = con.prepareStatement(qq);
	    back = setParams(stmt, false);
	    stmt.setString(31, id);
	    stmt.executeUpdate();
	    if(deadlineDate.isEmpty()){
		qq = qq2;
		stmt2 = con.prepareStatement(qq);
		stmt2.setString(1, id);
		stmt2.executeUpdate();
		doSelect();
	    }
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    back =  ex.toString()+":"+qq;
	}
	finally{
	    Helper.databaseDisconnect(con, rs, stmt, stmt2);
	}
	return back; // success
    }
    //
    public String doDelete(){
	//
	// System.err.println("delete record");
	//
	Connection con = null;
	PreparedStatement stmt = null;		
	ResultSet rs = null;		
	String back = "";
	String qq = "delete from  tortClaims where id=?";
	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not cnnnect to DB ";
	    return back;
	}							
	try{
	    stmt = con.prepareStatement(qq);
	    stmt.setString(1, id);
	    stmt.executeUpdate();
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    back =  ex.toString();
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	return back;
    }
    //
    public String doSelect(){
	//
	Connection con = null;
	PreparedStatement stmt = null;		
	ResultSet rs = null;		
	String back = "";
	String qq = "select "+
	    "type,status,"+
	    "deductible,"+
	    "paidByCity,paidByInsur,"+
	    "requestAmount,"+ //
	    "settled,"+						
	    "miscByCity,"+ //						
	    "cityAutoInc,"+
	    "date_format(incidentDate,'%m/%d/%Y'),"+
						
	    "incident, "+
	    "comments,"+
	    "date_format(opened,'%m/%d/%Y'), "+   
	    "date_format(received,'%m/%d/%Y'), "+
	    "date_format(closed,'%m/%d/%Y'), "+
	    "filed, "+
	    "subInsur, "+
	    "date_format(expires,'%m/%d/%Y'), "+
	    "cityTotalCost,paidByCity2City,paidByInsur2City,deductible2, "+
	    "otherType,recordOnly,"+
	    "paidByRisk,law_firm_id, "+
	    "date_format(denialLetterDate,'%m/%d/%Y'), "+
	    "date_format(deadlineDate,'%m/%d/%Y'), "+
	    "lawsuit,bodilyInvolved "+
	    " from tortClaims where id=?";
	String str="";
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not cnnnect to DB ";
	    return back;
	}
	if(debug){
	    logger.debug(qq);
	}
	try{
	    stmt = con.prepareStatement(qq);
	    stmt.setString(1, id);
	    rs = stmt.executeQuery();
	    if(rs.next()){
		setVals(
			id,
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
			rs.getString(23),
			rs.getString(24),
			rs.getString(25),
			rs.getString(26),
			rs.getString(27),
			rs.getString(28),
			rs.getString(29) != null,
			rs.getString(30) != null
			);
	    }
	    else{
		return "Record "+id+" Not Found";
	    }
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    back = ex.toString();
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	return back;
    }

    public String createEmployeeList(){
	//
	Connection con = null;
	PreparedStatement stmt = null;		
	ResultSet rs = null;		
	String qq = "select distinct "+
	    "e.id,e.name "+
	    " from employees e,empRelated i where e.id=i.employee_id and i.risk_id = ? ";
	String str="",str2="",str3="",str4="", back="";
	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not cnnnect to DB ";
	    return back;
	}		
	try{
	    stmt = con.prepareStatement(qq);
	    stmt.setString(1, id);
	    rs = stmt.executeQuery();
	    employees = new ArrayList<Employee>();
	    while(rs.next()){
		str = rs.getString(1);
		str2 = rs.getString(2);
		if(!(str==null || str.equals("") || str2 == null 
		     || str2.equals(""))){
		    Employee user = new Employee(debug);
		    user.setId(str);
		    user.setName(str2);
		    employees.add(user);
		}
	    }
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    back =  ex.toString()+":"+qq;
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	return back;
    }
    //
    public String getLegalId(){

	Connection con = null;
	PreparedStatement stmt = null;		
	ResultSet rs = null;		
	String qq = " select lid from legalTorts where tid=?"; //+id;
	String vsId = "", back="";
	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not cnnnect to DB ";
	    logger.error(back+":"+qq);
	    return "";
	}
	try{
	    stmt = con.prepareStatement(qq);
	    stmt.setString(1, id);
	    rs = stmt.executeQuery();
	    if(rs.next()){  // one is good enough
		String str = rs.getString(1);
		if(str != null && !str.equals("0")){
		    vsId = str;
		}
	    }
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	return vsId;
    }
    public String doClaimantInsert(List<RiskPerson> persons){
	//
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;		
		
	String msg = "";
	String qq = "";
	String query = "",str="";
	qq = "insert into claim_people values (?,?)";
	con = Helper.getConnection();
	if(con == null){
	    logger.error("Could not connect to DB ");
	    return "Could not connect to DB ";
	}				
	try{
	    if(persons != null && persons.size() > 0){
		stmt = con.prepareStatement(qq);	
		for(RiskPerson pp:persons){
		    stmt.setString(1, pp.getId());
		    stmt.setString(2, id);
		    stmt.executeUpdate();
		}
	    }
	}
	catch(Exception ex){
	    logger.error(ex+":"+query);
	    msg += ex;
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	return msg;
    }		
	
}
/**
alter table tortClaims add denialLetterDate date;
alter table tortClaims add deadlineDate date;
alter table tortClaims add lawsuit char(1);
alter table tortClaims add bodilyInvolved char(1);

update tortclaims set status='Closed' where closed is not null;
update tortclaims set bodilyInvolved='y' where type=6;

 */






















































package risks.models;

import java.util.*;
import java.sql.*;
import java.io.*;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.utils.Helper;
import risks.lists.*;

public class Legal extends Risk{

    static Logger logger = LogManager.getLogger(Legal.class);
   
    public String 
	type="", caseNum = "", status="", tortId="",
	location="", damageAmnt="", description="", otherType="",
    //
    // employee info
	empid="", empName="", empPhone="", dept_id="", empTitle="",
    //
    // auto related
	cityAutoInc="", insured="",
	vin="",autoMake="", autoNum="", autoModel="", autoYear="", 
	adjuster="", adjusterPhone="", adjusterEmail="",
	attorney="", attorneyPhone="", attorneyEmail="",
	insurance="", policy="",
	claimNum="", insurStatus="",
    //
    // dates
	doi="", filed="", judgment="", proSupp="", closed="",
    //
    // Defendant Insurance
	defInsur="", defInsurPhone="", defClaimNum="",
	defAdjuster="", defAdjusterPhone="", defAdjusterEmail="",
	defAttorney="", defAttorneyPhone="";
    //
    public String  allDocuments="", deptRecoverDate="", deptCollectDate="",
	deptToRisk="",  // ?
	deptToRiskDate="",
	riskFirstDate="",
	toProsecutorDate="",
	convictionDate="",
	collectDate="",
	riskToInsur="", // ?
	riskToInsurDate="",
	insurRecoveryDate="",
	insurCollectDate="",
	deductible="", otherDetails="", recordOnly="", unableToCollect="";
    public String paidByCity="",paidByInsur="",miscByCity="", paidByRisk="",
	paidByDef="", outOfDuty="";
    List<Employee> workers  = null;
    List<Employee> employees = null;
    List<Insurance> insurances = null;
    List<Auto> autos = null;
    RelatedDeptList relatedDepts = null;
    List<RiskPerson> defendants = null;
    //
    // basic constructor
    public Legal(boolean deb){

	debug = deb;
	//
    }
    public Legal(boolean deb, String val){

	debug = deb;
	setId(val);
	//
    }
    public Legal(boolean deb,
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
		 String val30,
		 String val31,
		 String val32,
		 String val33,
		 String val34,
		 String val35,
		 String val36,
		 String val37,
		 String val38
		 ){
	debug = deb;
	setVals(val, val2, val3, val4, val5, val6, val7, val8,val9,val10,
		val11, val12, val13, val14, val15, val16, val17, val18,val19,val20,
		val21, val22, val23, val24, val25, val26, val27, val28,val29,val30,
		val31, val32, val33, val34, val35, val36, val37, val38);
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
		 String val30,
		 String val31,
		 String val32,
		 String val33,
		 String val34,
		 String val35,
		 String val36,
		 String val37,
		 String val38
		 ){
	setId(val);
	setType(val2);
	setStatus(val3);
	setCaseNum(val4);
	setLocation(val5);
	setDamageAmnt(val6);
	setCityAutoInc(val7);
	setDoi(val8);
	setFiled(val9);
	setJudgment(val10);

	setProSupp(val11);
	setClosed(val12);
	setInsured(val13);
	setDescription(val14);
	setAllDocuments(val15);
	setDeptRecoverDate(val16);
	setDeptCollectDate(val17);
	setDeptToRisk(val18);
	setDeptToRiskDate(val19);
	setRiskFirstDate(val20);

	setToProsecutorDate(val21);
	setConvictionDate(val22);
	setCollectDate(val23);
	setRiskToInsur(val24);
	setRiskToInsurDate(val25);
	setInsurRecoveryDate(val26);
	setInsurCollectDate(val27);
	setDeductible(val28);
	setOtherDetails(val29);
	setOtherType(val30);
				
	setPaidByCity(val31);
	setPaidByInsur(val32);
	setMiscByCity(val33);
	setRecordOnly(val34);
	setPaidByRisk(val35);
	setPaidByDef(val36);
	setUnableToCollect(val37);
	setOutOfDuty(val38);
    }
    //
    // setters
    //
    public void setTortId(String val){
	if(val != null && !val.equals(""))
	    tortId = val;
    }
    public void setCaseNum(String val){
	if(val != null && !val.equals(""))
	    caseNum = val;
    }
    public void setType(String val){
	if(val != null && !val.equals(""))
	    type = val;
    }
    public void setUnableToCollect(String val){
	if(val != null && !val.equals(""))
	    unableToCollect = val;
    }
    public     String getUnableToCollect(){
	return unableToCollect;
    }			
    public void setOtherType(String val){
	if(val != null && !val.equals(""))
	    otherType = val;
    }
    public void setStatus(String val){
	if(val != null)
	    status = val;
    }
    public void setEmpName(String val){
	if(val != null)
	    empName = val;
    }
    public void setEmpPhone(String val){
	if(val != null)
	    empPhone = val;
    }
    public void setDept_id(String val){
	if(val != null)
	    dept_id = val;
    }
    public void setOutOfDuty(String val){
	if(val != null)
	    outOfDuty = val;
    }    
    public void setEmpid(String val){
	if(val != null)
	    empid = val;
    }
    public void setEmpTitle(String val){
	if(val != null)
	    empTitle = val;
    }
    //
    public void setCityAutoInc(String val){
	if(val != null)
	    cityAutoInc = val;
    }

    public void setInsured(String val){
	if(val != null)
	    insured = val;
    }

    public void setDebug(boolean val){
	debug = val;
    }
    public void setDoi(String val){
	if(val != null)
	    doi = val;
    }	
    public void setFiled(String val){
	if(val != null)
	    filed = val;
    }
    public void setJudgment(String val){
	if(val != null)
	    judgment = val;
    }
    public void setProSupp(String val){
	if(val != null)
	    proSupp = val;
    }
    public void setClosed(String val){
	if(val != null)
	    closed = val;
    }
    public void setLocation(String val){
	if(val != null)
	    location = val;
    }
    public void setDamageAmnt(String val){
	if(val != null)
	    damageAmnt = Helper.cleanNumber(val);
    }
    public void setPaidByCity(String val){
	if(val != null)
	    paidByCity = Helper.cleanNumber(val);
    }
    public void setPaidByInsur(String val){
	if(val != null)
	    paidByInsur = Helper.cleanNumber(val);
    }
    public void setMiscByCity(String val){
	if(val != null)
	    miscByCity = Helper.cleanNumber(val);
    }

    public void setDescription(String val){
	if(val != null)
	    description = val;
    }	
    public void setAllDocuments(String val){
	if(val != null)
	    allDocuments = val;
    }	
    public void setDeptRecoverDate(String val){
	if(val != null)
	    deptRecoverDate = val;
    }	
    public    void setDeptCollectDate(String val){
	if(val != null)
	    deptCollectDate = val;
    }	
    public void setDeptToRisk(String val){
	if(val != null)
	    deptToRisk = val;
    }	
    public void setDeptToRiskDate(String val){
	if(val != null)
	    deptToRiskDate = val;
    }
    public void setRiskFirstDate(String val){
	if(val != null)
	    riskFirstDate = val;
    }	
    public void setToProsecutorDate(String val){
	if(val != null)
	    toProsecutorDate = val;
    }
    public void setConvictionDate(String val){
	if(val != null)
	    convictionDate = val;
    }
    public void setCollectDate(String val){
	if(val != null)
	    collectDate = val;
    }
    public void setRiskToInsur(String val){
	if(val != null)
	    riskToInsur = val;
    }
    public void setRiskToInsurDate(String val){
	if(val != null)
	    riskToInsurDate = val;
    }
    public void setInsurRecoveryDate(String val){
	if(val != null)
	    insurRecoveryDate = val;
    }
    public void setInsurCollectDate(String val){
	if(val != null)
	    insurCollectDate = val;
    }
    public void setDeductible(String val){
	if(val != null)
	    deductible = Helper.cleanNumber(val);
    }	
    public void setOtherDetails(String val){
	if(val != null)
	    otherDetails = val;
    }	
    public void setRecordOnly(String val){
	if(val != null)
	    recordOnly = val;
    }
    public void setPaidByRisk(String val){
	if(val != null)
	    paidByRisk = Helper.cleanNumber(val);
    }
    public void setPaidByDef(String val){
	if(val != null)
	    paidByDef = Helper.cleanNumber(val);
    }
    //
    // getters
    //
    public String getId(){
	return id;
    }
    public String getTortId(){
	return tortId;
    }
    public String getCaseNum(){
	return caseNum;
    }
    public String getType(){
	return type;
    }
    public String getOtherType(){
	return otherType;
    }
    public String getStatus(){
	return status;
    }
    public String getEmpid(){
	return empid;
    }
    public String getEmpName(){
	return empName;
    }
    public String getEmpPhone(){
	return empPhone;
    }
    public String getDept_id(){
	return dept_id;
    }
    public String getCityAutoInc(){
	return cityAutoInc;
    }
    public String getInsured(){
	return insured;
    }	
    //
    public String getClosed(){
	return closed;
    }
    public String getDoi(){
	return doi;
    }
    public String getLocation(){
	return location;
    }
    public String getDamageAmnt(){
	return damageAmnt;
    }
    public String getFiled(){
	return filed;
    }
    public String getJudgment(){
	return judgment;
    }
    public String getProSupp(){
	return proSupp;
    }

    public String getAllDocuments(){
	return allDocuments;
    }
    public String getDescription(){
	return description;
    }
    public String getDeptRecoverDate(){
	return deptRecoverDate;
    }
    public String getDeptCollectDate(){
	return deptCollectDate;
    }
    public String getDeptToRisk(){
	return deptToRisk;
    }
    public String getDeptToRiskDate(){
	return deptToRiskDate;
    }
    public String getRiskFirstDate(){
	return riskFirstDate;
    }
    public String getToProsecutorDate(){
	return toProsecutorDate;
    }
    public String getConvictionDate(){
	return convictionDate;
    }
    public String getCollectDate(){
	return collectDate;
    }
    public String getRiskToInsur(){
	return riskToInsur;
    }
    public String getRiskToInsurDate(){
	return riskToInsurDate;
    }
    public String getInsurRecoveryDate(){
	return insurRecoveryDate;
    }
    public String getInsurCollectDate(){
	return insurCollectDate;
    }
    public String getEmpTitle(){
	return empTitle;
    }
    public String getDeductible(){
	return deductible;
    }
    public String getOtherDetails(){
	return otherDetails;
    }
    public String getPaidByCity(){
	return paidByCity;
    }
    public String getPaidByInsur(){
	return paidByInsur;
    }
    public String getMiscByCity(){
	return miscByCity;
    }
    public String getRecordOnly(){
	return recordOnly;
    }
    public String getPaidByRisk(){
	return paidByRisk;
    }
    public String getPaidByDef(){
	return paidByDef;
    }
    public String getOutOfDuty(){
	return outOfDuty;
    }

    public String toString(){
	return id;
    }
    public boolean equals(Object o) {
	if (o instanceof Legal) {
	    Legal c = (Legal) o;
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
    public List<Employee> getWorkers(){
	return workers;
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
	    rdl.setType("Legal");
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
    public  boolean isOffDuty(){
	return !outOfDuty.isEmpty();
    }        
    public List<RiskPerson> getDefendants(){
	if(defendants == null && !id.equals("")){
	    PersonList sp = new PersonList(debug);
	    sp.setRisk_id(id);
	    sp.setVslegalPersonOnly(); // defendant
	    String back = sp.lookFor();
	    if(back.equals("")){
		List<RiskPerson> ones = sp.getPersons();
		if(ones != null &&  ones.size() > 0){
		    defendants = ones;
		}
	    }
	}
	return defendants;
    }
    public boolean hasDefendants(){
	getDefendants();
	return defendants != null && defendants.size() > 0;
    }
    public void setDefendans(List<RiskPerson> vals){
	defendants = vals;
    }
    public String getEmployeeNames(){
	String names = "";
	getEmployees();
	if(employees != null){
	    for(Employee one:employees){
		if(!names.isEmpty()) names += "; ";
		names += one.getFullName();
	    }
	}
	return names;
    }
    public String getDeptNames(){
	String names = "";
	getEmployees();
	if(employees != null){
	    for(Employee one:employees){
		if(!names.isEmpty()) names += "; ";
		names += one.getDeptName();
	    }
	}
	return names;
    }        
    //
    // save a new record in the database
    // return "" or any exception thrown by DB
    //
    public String doSave(){

	Connection con = null;
	PreparedStatement stmt = null, stmt2=null, stmt3=null;
	ResultSet rs = null;
		
	String message = "";
	String qq = "insert into risk_sequences values(0,?)";
	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    message = "Could not connect to DB ";
	    return message;
	}
				
	try{
	    stmt = con.prepareStatement(qq);
	    stmt.setString(1, "legal");
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
	    qq = " insert into vslegals values (?,?,?,?,?, ?,?,?,?,?,"+
		"?,?,?,?,?, ?,?,?,?,?,"+
		"?,?,?,?,?, ?,?,?,?,?,"+
		"?,?,?,?,?, ?,?,?)";
	    if(debug){
		logger.debug(qq);
	    }
	    stmt3 = con.prepareStatement(qq);
	    message = setParams(stmt3, true);// forSave
	    if(message.equals(""))
		stmt3.executeUpdate();
	    //
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    message += ex.toString()+":"+qq;
	}
	finally{
	    Helper.databaseDisconnect(con, rs, stmt, stmt2, stmt3);
	}
	return message;
    }
    String setParams(PreparedStatement stmt, boolean forSave){
	String back = "";
	int jj=1;
	try{
	    if(forSave){
		stmt.setString(jj++, id);								
	    }
	    stmt.setString(jj++, type);
	    if(status.equals(""))
		status = "Pending";
	    stmt.setString(jj++, status);
	    if(caseNum.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++,caseNum);
	    }
	    if(location.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++, location);
	    }
	    if(damageAmnt.equals("")){
		stmt.setString(jj++,"0");
	    }
	    else {
		stmt.setString(jj++,damageAmnt);
	    }
	    if(cityAutoInc.equals("")){
		stmt.setNull(jj++, Types.CHAR);
	    }
	    else {
		stmt.setString(jj++,"y");
	    }
	    if(doi.equals("")) // 12
		stmt.setNull(jj++, Types.DATE);
	    else{
		java.util.Date date_tmp = df.parse(doi);
		stmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }
	    if(filed.equals(""))
		stmt.setNull(jj++, Types.DATE);
	    else{
		java.util.Date date_tmp = df.parse(filed);
		stmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }
	    if(judgment.equals(""))
		stmt.setNull(jj++, Types.DATE);
	    else{
		java.util.Date date_tmp = df.parse(judgment);
		stmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }								
	    if(proSupp.equals(""))
		stmt.setNull(jj++, Types.DATE);
	    else{
		java.util.Date date_tmp = df.parse(proSupp);
		stmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }				
	    if(closed.equals(""))
		stmt.setNull(jj++, Types.DATE);
	    else{
		java.util.Date date_tmp = df.parse(closed);
		stmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }		
	    if(insured.equals("")){
		stmt.setNull(jj++, Types.CHAR);
	    }
	    else {
		stmt.setString(jj++,"y");
	    }
	    if(description.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++,description);
	    }
	    if(allDocuments.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++,allDocuments);
	    }
	    if(deptRecoverDate.equals("")) // 20
		stmt.setNull(jj++, Types.DATE);
	    else{
		java.util.Date date_tmp = df.parse(deptRecoverDate);
		stmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }
	    if(deptCollectDate.equals(""))
		stmt.setNull(jj++, Types.DATE);
	    else{
		java.util.Date date_tmp = df.parse(deptCollectDate);
		stmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }
	    if(deptToRisk.equals("")){
		stmt.setNull(jj++, Types.CHAR);
	    }
	    else {
		stmt.setString(jj++,"y");
	    }
	    if(deptToRiskDate.equals(""))
		stmt.setNull(jj++, Types.DATE);
	    else{
		java.util.Date date_tmp = df.parse(deptToRiskDate);
		stmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }
	    if(riskFirstDate.equals(""))
		stmt.setNull(jj++, Types.DATE);
	    else{
		java.util.Date date_tmp = df.parse(riskFirstDate);
		stmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }
	    if(toProsecutorDate.equals(""))
		stmt.setNull(jj++, Types.DATE);
	    else{
		java.util.Date date_tmp = df.parse(toProsecutorDate);
		stmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }
	    if(convictionDate.equals(""))
		stmt.setNull(jj++, Types.DATE);
	    else{
		java.util.Date date_tmp = df.parse(convictionDate);
		stmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }
	    if(collectDate.equals("")) // 30
		stmt.setNull(jj++, Types.DATE);
	    else{
		java.util.Date date_tmp = df.parse(collectDate);
		stmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }
	    if(riskToInsur.equals("")){
		stmt.setNull(jj++, Types.CHAR);
	    }
	    else {
		stmt.setString(jj++,"y");
	    }
	    if(riskToInsurDate.equals(""))
		stmt.setNull(jj++, Types.DATE);
	    else{
		java.util.Date date_tmp = df.parse(riskToInsurDate);
		stmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }
	    if(insurRecoveryDate.equals(""))
		stmt.setNull(jj++, Types.DATE);
	    else{
		java.util.Date date_tmp = df.parse(insurRecoveryDate);
		stmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }
	    if(insurCollectDate.equals(""))
		stmt.setNull(jj++, Types.DATE);
	    else{
		java.util.Date date_tmp = df.parse(insurCollectDate);
		stmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }
	    if(deductible.equals("")){
		stmt.setString(jj++,"0");
	    }
	    else {
		stmt.setString(jj++,deductible);
	    }
	    if(otherDetails.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++,otherDetails);
	    }
	    if(otherType.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++,otherType);
	    }
	    if(paidByCity.equals("")){
		stmt.setString(jj++,"0");
	    }
	    else {
		stmt.setString(jj++,paidByCity);
	    }
	    if(paidByInsur.equals("")){
		stmt.setString(jj++,"0");
	    }
	    else {
		stmt.setString(jj++,paidByInsur);
	    }
	    if(miscByCity.equals("")){
		stmt.setString(jj++,"0");
	    }
	    else {
		stmt.setString(jj++,miscByCity);
	    }
	    if(recordOnly.equals("")){
		stmt.setNull(jj++, Types.CHAR);
	    }
	    else {
		stmt.setString(jj++,"y");
	    }
	    if(paidByRisk.equals("")){
		stmt.setString(jj++,"0");
	    }
	    else {
		stmt.setString(jj++,paidByRisk);
	    }
	    if(paidByDef.equals("")){
		stmt.setString(jj++,"0");
	    }
	    else {
		stmt.setString(jj++,paidByDef);
	    }
	    if(unableToCollect.equals("")){
		stmt.setNull(jj++, Types.CHAR);
	    }
	    else {
		stmt.setString(jj++,"y");
	    }
	    if(outOfDuty.equals("")){
		stmt.setNull(jj++, Types.CHAR);
	    }
	    else {
		stmt.setString(jj++,"y");
	    }	    
	}
	catch(Exception ex){
	    back += ex;
	    logger.error(back);
	}
	return back;
    }
    public String doUpdate(){

	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;		
	String str="", message="";
	String qq = "";
	qq = "update vslegals set "+
	    "type=?,status=?,caseNum=?,location=?,damageAmnt=?,"+
	    "cityAutoInc=?,doi=?,filed=?,judgment=?,proSupp=?,"+
	    "closed=?,insured=?,description=?,allDocuments=?,deptRecoverDate=?,"+
	    "deptCollectDate=?,deptToRisk=?,deptToRiskDate=?,riskFirstDate=?,toProsecutorDate=?,"+
	    "convictionDate=?,collectDate=?,riskToInsur=?,riskToInsurDate=?,insurRecoveryDate=?,"+
	    "insurCollectDate=?,deductible=?,otherDetails=?,otherType=?,paidByCity=?,"+
	    "paidByInsur=?,miscByCity=?,recordOnly=?,paidByRisk=?,paidByDef=?,"+
	    "unableToCollect=?,outOfDuty=? where id=? ";

	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    message = "Could not connect to DB ";
	    return message;
	}
	try{
						
	    stmt = con.prepareStatement(qq);
	    message = setParams(stmt, false);
	    if(message.equals("")){
		stmt.setString(38, id);
		stmt.executeUpdate();
	    }
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    message += ex.toString()+":"+qq;
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	return message; 
    }
    //
    public String doDelete(){
	//
	Connection con = null;
	PreparedStatement stmt = null, stmt2=null;
	ResultSet rs = null;		
	String qq = "delete from  vslegals where id=?";// +id;
	String back = "";
	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB ";
	    return back;
	}
	try{
	    stmt = con.prepareStatement(qq);
	    stmt.setString(1, id);
	    stmt.executeUpdate();
	    deleteLegalRelated();
	    qq = "delete from insuranceRelated values where risk_id=?";//+id;
	    if(debug)
		logger.debug(qq);
	    stmt2 = con.prepareStatement(qq);
	    stmt2.setString(1, id);
	    stmt.executeUpdate();
	}
	catch(Exception ex){
	    logger.debug(ex);
	    back += ex+":"+qq;
	}
	finally{
	    Helper.databaseDisconnect(con, rs, stmt, stmt2);
	}
	return back;
    }
    public String deleteLegalRelated(){
	//
	String back = "";
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;		
	String qq = " delete from legalRelated where id=? or id2=?";		
	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB ";
	    return back;
	}
	try{
	    stmt = con.prepareStatement(qq);
	    stmt.setString(1,id);
	    stmt.setString(2,id);
	    stmt.executeUpdate();
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    back = ex.toString()+":"+qq;
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	return back;
    }
    public String doDefendantInsert(String pid){
	//
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;			
	String msg = "";
	String qq = "";
	String query = "",str="";
	qq = "insert into vslegal_people values (?,?)";
	con = Helper.getConnection();
	if(con == null){
	    logger.error("Could not connect to DB ");
	    return "Could not connect to DB ";
	}
	if(pid.equals("")){
	    return "No defendants to add ";
	}
	try{
	    stmt = con.prepareStatement(qq);					
	    stmt.setString(1, pid);
	    stmt.setString(2, id);
	    stmt.executeUpdate();
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
    public String doDefendantInsert(List<RiskPerson> persons){
	//
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;			
	String msg = "";
	String qq = "";
	String query = "",str="";
	qq = "insert into vslegal_people values (?,?)";
	con = Helper.getConnection();
	if(con == null){
	    logger.error("Could not connect to DB ");
	    return "Could not connect to DB ";
	}
	if(persons == null || persons.size() == 0){
	    return "No defendants to add ";
	}
	try{
	    stmt = con.prepareStatement(qq);					
	    for(RiskPerson rp:persons){
		stmt.setString(1, rp.getId());
		stmt.setString(2, id);
		stmt.executeUpdate();
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
    //
    public String doSelect(){
	//
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;		
	String qq = "select "+
	    "id,type,status,caseNum,location,"+
	    "damageAmnt,cityAutoInc,"+ // 7
	    "date_format(doi,'%m/%d/%Y'),"+
	    "date_format(filed,'%m/%d/%Y'), "+
	    "date_format(judgment,'%m/%d/%Y'), "+
	    "date_format(proSupp,'%m/%d/%Y'), "+   						
	    "date_format(closed,'%m/%d/%Y'), "+  // 12
	    "insured,description,allDocuments,"+ // 15
						
	    "date_format(deptRecoverDate,'%m/%d/%Y'), "+
	    "date_format(deptCollectDate,'%m/%d/%Y'), "+
	    " deptToRisk,"+
	    "date_format(deptToRiskDate,'%m/%d/%Y'), "+
	    "date_format(riskFirstDate,'%m/%d/%Y'),"+

	    "date_format(toProsecutorDate,'%m/%d/%Y'),"+	    
	    "date_format(convictionDate,'%m/%d/%Y'),"+
	    "date_format(collectDate,'%m/%d/%Y'),"+
	    "riskToInsur,"+
	    "date_format(riskToInsurDate,'%m/%d/%Y'),"+	    
	    "date_format(insurRecoveryDate,'%m/%d/%Y'),"+
	    "date_format(insurCollectDate,'%m/%d/%Y'),"+// 27
	    "deductible,otherDetails,otherType, "+ // 30
						
	    "paidByCity,paidByInsur,miscByCity, "+
	    "recordOnly, "+
	    "paidByRisk, "+
	    "paidByDef,unableToCollect,outOfDuty "+
	    " from vslegals where id=?";// +id;
	String str="", message="";
	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    message = "Could not connect to DB ";
	    return message;
	}				
	try{

	    stmt = con.prepareStatement(qq);
	    stmt.setString(1, id);
	    rs = stmt.executeQuery();
	    if(rs.next()){
		setVals(rs.getString(1),
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
			rs.getString(29),
			rs.getString(30),
			rs.getString(31),
			rs.getString(32),
			rs.getString(33),
			rs.getString(34),
			rs.getString(35),
			rs.getString(36),
			rs.getString(37),
			rs.getString(38));
	    }
	    else{
		message += "Record "+id+" Not found";
	    }
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    message += ex.toString()+":"+qq;
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	return message;
		
    }

}























































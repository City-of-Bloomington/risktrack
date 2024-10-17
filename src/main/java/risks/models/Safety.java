package risks.models;

import java.util.*;
import java.sql.*;
import java.io.*;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.utils.Helper;
import risks.lists.*;


public class Safety extends Risk{

    static Logger logger = LogManager.getLogger(Safety.class);
    public String type="", 
	status="",
	accidDate="",accidTime="", reported="", // date
	accidLocation="",
	empid="", empName="", deptPhone="", dept="", empSuper="", 
	empTitle="", empInjured="";
    public String damage="", autoDamage="",
	estPlace="", estCost="",
	estPlace2="",estCost2="",
	estPlace3="", estCost3="", 
	totalCost="", chosenDealer="";
    public String propDamage="",
	estPlaceP="", estCostP="",
	estPlaceP2="",estCostP2="",
	estPlaceP3="", estCostP3="", 
	totalCostP="", chosenDealerP="", 
	subToInsur="",autoPaid="",propPaid="", recordOnly="",
	workComp="",whatProp="",repairInfo="",tortId="", vsId="";

    public String vin="", autoNum="",
	autoMake="", autoModel="", autoYear="";
    public String insurance="",insurStatus="", adjuster="",adjusterPhone="",
	adjusterEmail="", attorney="", attorneyPhone="", attorneyEmail="",
	deductible="",claimNum="", policy="", otherType="";
    public String paidByCity="",paidByInsur="",miscByCity="", paidByRisk="",
	outOfDuty="";
    List<Employee> workers = null;
    List<Employee> employees = null;
    List<Insurance> insurances = null;
    List<Auto> autos = null;
    //
    // basic constructor
    public Safety(boolean deb){

	debug = deb;
    }
    public Safety(boolean deb, String val){

	debug = deb;
	setId(val);
    }
    public Safety(boolean deb,
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
		  String val38,
		  String val39,
		  String val40,
		  String val41
		  ){
	setVals(val,
		val2,
		val3,
		val4,
		val5,
		val6,
		val7,
		val8,
		val9,
		val10,
								
		val11,
		val12,
		val13,
		val14,
		val15,
		val16,
		val17,
		val18,
		val19,
		val20,
									
		val21,
		val22,
		val23,
		val24,
		val25,
		val26,
		val27,
		val28,
		val29,
		val30,
								
		val31,
		val32,
		val33,
		val34,
		val35,
		val36,
		val37,
		val38,
		val39,
		val40,
		val41);
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
		 String val38,
		 String val39,
		 String val40,
		 String val41
		 ){
	setId(val);
	setType(val2);
	setStatus(val3);
	setAccidDate(val4);
	setAccidTime(val5);
	setReported(val6);
	setAccidLocation(val7);
	setDamage(val8);
	setEstPlace(val9);
	setEstCost(val10);
				
	setEstPlace2(val11);
	setEstCost2(val12);
	setEstPlace3(val13);
	setEstCost3(val14);
	setChosenDealer(val15);
	setTotalCost(val16);
	setAutoDamage(val17);
	setAutoPaid(val18);
	setEstPlaceP(val19);
	setEstCostP(val20);
				
	setEstPlaceP2(val21);
	setEstCostP2(val22);
	setEstPlaceP3(val23);
	setEstCostP3(val24);
	setChosenDealerP(val25);
	setTotalCostP(val26);
	setPropDamage(val27);
	setPropPaid(val28);
	setSubToInsur(val29);
	setEmpInjured(val30);

	setWorkComp(val31);
	setWhatProp(val32);
	setRepairInfo(val33);
	setDeductible(val34);
	setOtherType(val35);
	setPaidByCity(val36);
	setPaidByInsur(val37);
	setMiscByCity(val38);
	setRecordOnly(val39);
	setPaidByRisk(val40);
	setOutOfDuty(val41);
    }
		
    //
    // setters
    //
    public void setVsId(String val){
	if(val != null)
	    vsId = val;
    }
    public void setType(String val){
	if(val != null)
	    type = val;
    }
    public void setOtherType(String val){
	if(val != null)
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
    public void setDeptPhone(String val){
	if(val != null)
	    deptPhone = val;
    }
    public void setDept(String val){
	if(val != null)
	    dept = val;
    }
    public void setEmpid(String val){
	if(val != null)
	    empid = val;
    }
    public void setOutOfDuty(String val){
	if(val != null)
	    outOfDuty = val;
    }    
    public void setEmpSuper(String val){
	if(val != null)
	    empSuper = val;
    }
    public void setEmpInjured(String val){
	if(val != null)
	    empInjured = val;
    }
    public void setVin(String val){
	if(val != null)
	    vin = val; 
    }
    public void setAutoMake(String val){
	if(val != null)
	    autoMake = val; 
    }
    public void setAutoNum(String val){
	if(val != null)
	    autoNum = val; 
    }
    public void setAutoModel(String val){
	if(val != null)
	    autoModel = val; 
    }
    public void setAutoYear(String val){
	if(val != null)
	    autoYear = val; 
    }
    public void setDebug(boolean val){
	debug = val;
    }
    public void setAccidDate(String val){
	if(val != null)
	    accidDate = val;
    }	
    public void setAccidTime(String val){
	if(val != null)
	    accidTime = val;
    }	
    public void setReported(String val){
	if(val != null)
	    reported = val;
    }
    public void setAccidLocation(String val){
	if(val != null)
	    accidLocation = val;
    }
    public void setDamage(String val){
	if(val != null)
	    damage = val;
    }
    public void setAutoDamage(String val){
	if(val != null)
	    autoDamage = val;
    }
    public void setPropDamage(String val){
	if(val != null)
	    propDamage = val;
    }
    public void setEstPlace(String val){
	if(val != null)
	    estPlace = val;
    }
    public void setEstPlace2(String val){
	if(val != null)
	    estPlace2 = val;
    }
    public void setEstPlace3(String val){
	if(val != null)
	    estPlace3 = val;
    }
    public void setEstCost(String val){
	if(val != null)
	    estCost = Helper.cleanNumber(val);
    }
    public void setEstCost2(String val){
	if(val != null)
	    estCost2 = Helper.cleanNumber(val);
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
    public void setEstCost3(String val){
	if(val != null)
	    estCost3 = Helper.cleanNumber(val);
    }
    public void setChosenDealer(String val){
	if(val != null)
	    chosenDealer = val;
    }
    public void setEstPlaceP(String val){
	if(val != null)
	    estPlaceP = val;
    }
    public void setEstPlaceP2(String val){
	if(val != null)
	    estPlaceP2 = val;
    }
    public void setEstPlaceP3(String val){
	if(val != null)
	    estPlaceP3 = val;
    }
    public void setEstCostP(String val){
	if(val != null)
	    estCostP = val;
    }
    public void setEstCostP2(String val){
	if(val != null)
	    estCostP2 = val;
    }
    public void setEstCostP3(String val){
	if(val != null)
	    estCostP3 = val;
    }
    public void setChosenDealerP(String val){
	if(val != null)
	    chosenDealerP = val;
    }
    public void setTotalCost(String val){
	if(val != null)
	    totalCost = Helper.cleanNumber(val);
    }
    public void setTotalCostP(String val){
	if(val != null)
	    totalCostP = val;
    }
    public void setSubToInsur(String val){
	if(val != null)
	    subToInsur = val;
    }
    public void setAutoPaid(String val){
	if(val != null)
	    autoPaid = val;
    }
    public void setPropPaid(String val){
	if(val != null)
	    propPaid = val;
    }
    public void setWorkComp(String val){
	if(val != null)
	    workComp = val;
    }
    public void setWhatProp(String val){
	if(val != null)
	    whatProp = val;
    }
    public void setRepairInfo(String val){
	if(val != null)
	    repairInfo = val;
    }
    public void setTortId(String val){
	if(val != null)
	    tortId = val;
    }
    public void setInsurance(String val){
	if(val != null)
	    insurance = val;
    }
    public void setInsurStatus(String val){
	if(val != null)
	    insurStatus = val;
    }
    public void setAdjuster(String val){
	if(val != null)
	    adjuster = val;
    }
    public void setAdjusterPhone(String val){
	if(val != null)
	    adjusterPhone = val;
    }
    public void setAdjusterEmail(String val){
	if(val != null)
	    adjusterEmail = val;
    }
    public void setAttorney(String val){
	if(val != null)
	    attorney = val;
    }
    public void setAttorneyPhone(String val){
	if(val != null)
	    attorneyPhone = val;
    }
    public void setAttorneyEmail(String val){
	if(val != null)
	    attorneyEmail = val;
    }
    public void setDeductible(String val){
	if(val != null)
	    deductible = Helper.cleanNumber(val);
    }
    public void setClaimNum(String val){
	if(val != null)
	    claimNum = val;
    }
    public void setPolicy(String val){
	if(val != null)
	    policy = val;
    }
    public void setEmpTitle(String val){
	if(val != null)
	    empTitle = val;
    }
    public void setRecordOnly(String val){
	if(val != null)
	    recordOnly = val;
    }
    public void setPaidByRisk(String val){
	if(val != null)
	    paidByRisk = val;
    }
    //
    // getters
    //
    public String  getPaidByRisk(){
	return paidByRisk;
    }
    public String  getVsId(){
	return vsId;
    }
    public String  getType(){
	return type;
    }
    public String  getOtherType(){
	return otherType;
    }
    public String  getStatus(){
	return status;
    }
    public String  getEmpid(){
	return empid;
    }
    public String  getOutOfDuty(){
	return outOfDuty;
    }    
    public String  getEmpSuper(){
	return empSuper;
    }
    public String  getEmpInjured(){
	return empInjured;
    }
    public String  getEmpName(){
	return empName;
    }
    public String  getDeptPhone(){
	return deptPhone;
    }
    public String  getDept(){
	return dept;
    }
    public String  getVin(){
	return vin;
    }
    public String  getAutoMake(){
	return autoMake;
    }
    public String  getAutoModel(){
	return autoModel;
    }
    public String  getAutoYear(){
	return autoYear;
    }
    public String  getAutoNum(){
	return autoNum;
    }
    public String  getAccidDate(){
	return accidDate;
    }
    public String  getAccidTime(){
	return accidTime;
    }
    public String  getAccidLocation(){
	return accidLocation;
    }
    public String  getReported(){
	return reported;
    }
    public String  getDamage(){
	return damage;
    }
    public String  getAutoDamage(){
	return autoDamage;
    }
    public String  getPropDamage(){
	return propDamage;
    }
    public String  getEstPlace(){
	return estPlace;
    }
    public String  getEstPlace2(){
	return estPlace2;
    }
    public String getEstPlace3(){
	return estPlace3;
    }
    public String  getEstCost(){
	return estCost;
    }
    public String  getEstCost2(){
	return estCost2;
    }
    public String  getEstCost3(){
	return estCost3;
    }
    public String  getChosenDealer(){
	return chosenDealer;
    }
    public String  getEstPlaceP(){
	return estPlaceP;
    }
    public String  getEstPlaceP2(){
	return estPlaceP2;
    }
    public String getEstPlaceP3(){
	return estPlaceP3;
    }
    public String  getEstCostP(){
	return estCostP;
    }
    public String  getEstCostP2(){
	return estCostP2;
    }
    public String  getEstCostP3(){
	return estCostP3;
    }
    public String  getChosenDealerP(){
	return chosenDealerP;
    }
    public String  getTotalCost(){
	return totalCost;
    }
    public String  getTotalCostP(){
	return totalCostP;
    }
    public String  getSubToInsur(){
	return subToInsur;
    }
    public String  getAutoPaid(){
	return autoPaid;
    }
    public String  getPropPaid(){
	return propPaid;
    }
    public String  getWorkComp(){
	return workComp;
    }
    public String  getRepairInfo(){
	return repairInfo;
    }
    public String  getWhatProp(){
	return whatProp;
    }
    public String  getTortId(){
	return tortId;
    }

    public String  getInsurance(){
	return insurance;
    }
    public String  getInsurStatus(){
	return insurStatus;
    }
    public String  getAdjuster(){
	return adjuster;
    }
    public String  getAdjusterPhone(){
	return adjusterPhone;
    }
    public String  getAdjusterEmail(){
	return adjusterEmail;
    }
    public String  getAttorney(){
	return attorney;
    }
    public String  getAttorneyPhone(){
	return attorneyPhone;
    }
    public String  getAttorneyEmail(){
	return attorneyEmail;
    }
    public String  getDeductible(){
	return deductible;
    }
    public String  getClaimNum(){
	return claimNum;
    }
    public String  getPolicy(){
	return policy;
    }
    public String  getEmpTitle(){
	return empTitle;
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
    public String  getRecordOnly(){
	return recordOnly;
    }
    public List<Employee>  getWorkers(){
	return workers;
    }
    public List<Employee>  getEmployees(){
	if(employees == null && !id.equals("")){
	    EmployeeList el = new EmployeeList(debug, id);
	    String back = el.lookFor();
	    if(back.equals("")){
		employees = el.getEmployees();
	    }
	}
	return employees;
    }
    public List<Insurance>  getInsurances(){
	if(insurances == null && !id.equals("")){
	    InsuranceList il = new InsuranceList(debug, id);
	    String back = il.lookFor();
	    if(back.equals("")){
		insurances = il.getInsurances();
	    }
	}
	return insurances;
    }
    public List<Auto>  getAutos(){
	if(autos == null && !id.equals("")){
	    AutoList il = new AutoList(debug, id);
	    String back = il.lookFor();
	    if(back.equals("")){
		autos = il.getAutos();
	    }
	}
	return autos;
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
    public  boolean isOffDuty(){
	return !outOfDuty.isEmpty();
    }    
    public String toString(){
	return id;
    }
    public boolean equals(Object o) {
	if (o instanceof Safety) {
	    Safety c = (Safety) o;
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

    public String getEstCosts(){
	String ret = "";
	if(!estCost.equals("")) ret += "$"+estCost;
	if(!estCost2.equals("")){
	    if(!ret.equals("")) ret += ", ";
	    ret += "$"+estCost2;
	}
	if(!estCost3.equals("")){
	    if(!ret.equals("")) ret += ", ";
	    ret += "$"+estCost3;
	}
	return ret;
    }	
    //
    // save a new record in the database
    // return "" or any exception thrown by DB
    //
    public String doSave(){

	String back = "";
	Connection con = null;
	PreparedStatement stmt = null, stmt2=null, stmt3=null;
	ResultSet rs = null;		
		
	String qq = "insert into risk_sequences values(0,'safety')";
	logger.debug(qq);
	con = Helper.getConnection();
	if(con == null){
	    return "Could not connect to DB ";
	}				
	try{
	    stmt = con.prepareStatement(qq);				
	    stmt.executeUpdate();
	    //
	    // get the id of the new record
	    //
	    qq = "select LAST_INSERT_ID() ";
	    logger.debug(qq);
	    stmt2 = con.prepareStatement(qq);
	    rs = stmt2.executeQuery();
	    if(rs.next()){
		id = rs.getString(1);
	    }
	    qq = "insert into riskSafety values "+
		"(?,?,?,?,?, ?,?,?,?,?,"+
		" ?,?,?,?,?, ?,?,?,?,?,"+
		" ?,?,?,?,?, ?,?,?,?,?,"+
		" ?,?,?,?,?, ?,?,?,?,?, ?)";
	    logger.debug(qq);
	    stmt3 = con.prepareStatement(qq);
	    int jj=1;
	    back = setData(stmt3, true);
	    if(back.equals(""))
		stmt3.executeUpdate();
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    back = ex.toString()+":"+qq;
	}
	finally{
	    Helper.databaseDisconnect(con, rs, stmt, stmt2, stmt3);
	}
	return back;
    }
    String setData(PreparedStatement stmt, boolean forSave){
	String back = "";
	try{
	    int jj=1;
	    if(forSave)
		stmt.setString(jj++, id);
	    stmt.setString(jj++,type);
	    stmt.setString(jj++, status);
	    if(accidDate.equals(""))
		stmt.setNull(jj++, Types.DATE);
	    else{
		java.util.Date date_tmp = df.parse(accidDate);
		stmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }
	    if(accidTime.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++, accidTime);
	    }
	    if(reported.equals(""))
		stmt.setNull(jj++, Types.DATE);
	    else{
		java.util.Date date_tmp = df.parse(reported);
		stmt.setDate(jj++, new java.sql.Date(date_tmp.getTime()));
	    }
	    if(accidLocation.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++, accidLocation);
	    }
	    if(damage.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++, damage);
	    }
	    if(estPlace.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++, estPlace);
	    }
	    if(estCost.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++, estCost);
	    }
	    if(estPlace2.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++, estPlace2);
	    }
	    if(estCost2.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++, estCost2);
	    }
	    if(estPlace3.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++, estPlace3);
	    }
	    if(estCost3.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++, estCost3);
	    }
	    if(chosenDealer.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++, chosenDealer);
	    }
	    if(totalCost.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++, totalCost);
	    }
	    if(autoDamage.equals("")){
		stmt.setNull(jj++, Types.CHAR);
	    }
	    else {
		stmt.setString(jj++, "y");
	    }
	    if(autoPaid.equals("")){
		stmt.setNull(jj++, Types.CHAR);
	    }
	    else {
		stmt.setString(jj++, "y");
	    }
	    if(estPlaceP.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++, estPlaceP);
	    }
	    if(estCostP.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++, estCostP);
	    }
	    if(estPlaceP2.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++, estPlaceP2);
	    }
	    if(estCostP2.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++, estCostP2);
	    }
	    if(estPlaceP3.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++, estPlaceP3);
	    }
	    if(estCostP3.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++, estCostP3);
	    }
	    if(chosenDealerP.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++, chosenDealerP);
	    }
	    if(totalCostP.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++, totalCostP);
	    }
	    if(propDamage.equals("")){
		stmt.setNull(jj++, Types.CHAR);
	    }
	    else {
		stmt.setString(jj++, "y");
	    }
	    if(propPaid.equals("")){
		stmt.setNull(jj++, Types.CHAR);
	    }
	    else {
		stmt.setString(jj++, "y");
	    }
	    if(subToInsur.equals("")){
		stmt.setNull(jj++, Types.CHAR);
	    }
	    else {
		stmt.setString(jj++, "y");
	    }
	    if(empInjured.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++, empInjured);
	    }
	    if(workComp.equals("")){
		stmt.setNull(jj++, Types.CHAR);
	    }
	    else {
		stmt.setString(jj++, "y");
	    }
	    if(whatProp.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++, whatProp);
	    }
	    if(repairInfo.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++, repairInfo);
	    }
	    if(deductible.equals(""))
		stmt.setNull(jj++, Types.VARCHAR);
	    else
		stmt.setString(jj++, deductible);
	    if(otherType.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++, otherType);
	    }
	    if(paidByCity.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++, paidByCity);
	    }
	    if(paidByInsur.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++, paidByInsur);
	    }
	    if(miscByCity.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++, miscByCity);
	    }
	    if(recordOnly.equals("")){
		stmt.setNull(jj++, Types.CHAR);
	    }
	    else {
		stmt.setString(jj++, "y");
	    }
	    if(paidByRisk.equals("")){
		stmt.setNull(jj++, Types.VARCHAR);
	    }
	    else {
		stmt.setString(jj++, paidByRisk);
	    }
	    if(outOfDuty.equals("")){
		stmt.setNull(jj++, Types.CHAR);
	    }
	    else {
		stmt.setString(jj++, "y");
	    }
	}
	catch(Exception ex){
	    back += ex;
	}
	return back;
    }
				
    public String doUpdate(){

	String str="", back="";
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;			
	String qq = "";
	con = Helper.getConnection();
	if(con == null){
	    return "Could not connect to DB ";
	}				
	qq = "update riskSafety set "+
	    " type=?,status=?,accidDate=?,accidTime=?,reported=?,"+
	    " accidLocation=?,damage=?,estPlace=?,estCost=?,estPlace2=?,"+
	    " estCost2=?,estPlace3=?,estCost3=?,chosenDealer=?,totalCost=?,"+
	    " autoDamage=?,autoPaid=?,estPlaceP=?,estCostP=?,estPlaceP2=?,"+
	    " estCostP2=?,estPlaceP3=?,estCostP3=?,chosenDealerP=?,totalCostP=?,"+
	    "propDamage=?,propPaid=?,subToInsur=?,empInjured=?,workComp=?,"+
	    "whatProp=?,repairInfo=?,deductible=?,otherType=?,paidByCity=?,"+
	    "paidByInsur=?,miscByCity=?,recordOnly=?,paidByRisk=?,outOfduty=? where id=? ";


	//
	if(debug){
	    logger.debug(qq);
	}
	try{

	    stmt = con.prepareStatement(qq);
	    back = setData(stmt, false);
	    if(back.equals("")){
		stmt.setString(41, id);
		stmt.executeUpdate();
	    }
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    back =  ex+":"+qq;
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	return back;
    }
    //
    public String doDelete(){
	//
	// System.err.println("delete record");
	//
	Connection con = null;
	PreparedStatement stmt = null, stmt2=null;
	ResultSet rs = null;	
	String qq = "delete from  riskSafety where id=?", back="";
	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    return "Could not connect to DB ";
	}				
	try{

	    stmt = con.prepareStatement(qq);
	    stmt.setString(1, id);
	    stmt.executeUpdate();
	    qq = "delete from insuranceRelated where risk_id=?";
	    if(debug)
		logger.debug(qq);
	    stmt2 = con.prepareStatement(qq);
	    stmt2.setString(1, id);
	    stmt2.executeUpdate();
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    back = ex+":"+qq;
	}
	finally{
	    Helper.databaseDisconnect(con, rs, stmt, stmt2);
	}
	if(back.equals("")){
	    Auto auto = new Auto(debug);
	    auto.doDeleteAllFor(id);
	    Related rel = new Related(id, debug);
	    rel.doDelete();
	}
	return back;
    }
    //
    public String doSelect(){
	//
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;			
	String qq = "select "+
	    "type,status,"+
	    "date_format(accidDate,'%m/%d/%Y'),"+
	    "accidTime,"+ 
	    "date_format(reported,'%m/%d/%Y'),"+
						
	    "accidLocation,"+
	    "damage,"+
	    "estPlace,estCost,"+
	    "estPlace2,estCost2,"+
	    "estPlace3,estCost3,"+
	    "chosenDealer,"+
	    "totalCost,"+ // 15
						
	    "autoDamage,"+
	    "autoPaid,"+						
	    "estPlaceP,estCostP,"+
	    "estPlaceP2,estCostP2,"+
	    "estPlaceP3,estCostP3,"+
	    "chosenDealerP,"+
	    "totalCostP, "+ //25
						
	    "propDamage,"+
	    "propPaid,"+
	    "subToInsur, "+
	    "empInjured,"+
	    " workComp,"+ //30
						
	    " whatProp,"+
	    " repairInfo, "+
	    " deductible,"+
	    " otherType, "+
	    " paidByCity,paidByInsur,miscByCity, "+
	    " recordOnly,paidByRisk,outOfDuty "+ 
	    " from riskSafety where id=?";// 40
	String str="", back="";
	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    return "Could not connect to DB ";
	}				
	try{
	    stmt = con.prepareStatement(qq);
	    stmt.setString(1,id);
	    rs = stmt.executeQuery();
	    if(rs.next()){
		setVals(id,
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
			rs.getString(29),
			rs.getString(30),
			rs.getString(31),
			rs.getString(32),
			rs.getString(33),
			rs.getString(34),
			rs.getString(35),
			rs.getString(36),
			rs.getString(37),
			rs.getString(38),
			rs.getString(39),
			rs.getString(40));
	    }
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    back = ex+":"+qq;
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
	    " from employees e,empRelated i where e.id=i.employee_id and i.risk_id in (select distinct id from riskSafety)";
	String str="",str2="",str3="",str4="", back="";
	if(debug){
	    logger.debug(qq);
	}
	con = Helper.getConnection();
	if(con == null){
	    return "Could not connect to DB ";
	}				
	try{

	    stmt = con.prepareStatement(qq);				
	    rs = stmt.executeQuery();
	    workers = new ArrayList<Employee>(20);
	    while(rs.next()){
		str = rs.getString(1);
		str2 = rs.getString(2);
		if(!(str==null || str.equals("") || str2 == null 
		     || str2.equals(""))){
		    Employee emp = new Employee(str, str2);
		    workers.add(emp);
		}
	    }
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    back =  ex+":"+qq;
	}
	finally{
	    Helper.databaseDisconnect(con, stmt, rs);
	}
	return back;
    }
}























































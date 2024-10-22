package risks.models;

import java.util.*;
import java.sql.*;
import java.io.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.utils.Helper;
import risks.lists.*;


public class MiscAccident extends Risk{

    static Logger logger = LogManager.getLogger(MiscAccident.class);
    String type="", 
	status="", dept_id="",
	deptContact="", empInjured="", deptPhone="",
	accidDate="",accidTime="", reported="", // dates
	accidLocation="";

    String damage="", autoDamage="", 
	totalCost="";
    String propDamage="",
	totalCostP="", 
	subToInsur="", autoPaid="", propPaid="", 
	workComp="", whatProp="", repairInfo="";

    String otherType="";
    String paidByCity="",paidByInsur="",miscByCity="", paidByRisk="";
    String errors = "", outOfDuty="";
	
    Estimate[] autoEstimates = null;
    Estimate[] propEstimates = null;
    List<Auto> vehicles = null;
    Insurance insurance = null;
    Department department = null;
    Employee employee = null;
    List<Employee> employees = null;
    //
    // basic constructor
    public MiscAccident(boolean deb){

	debug = deb;

    }
    public MiscAccident(boolean deb, String _id){

	debug = deb;
	id=_id;
	errors = doSelect();
    }
    public MiscAccident(boolean deb,
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
			String val28){
	setVals(val, val2, val3, val4, val5,
		val6, val7, val8, val9, val10,
		val11, val12, val13, val14, val15,
		val16, val17, val18, val19, val20,
		val21, val22, val23, val24, val25,
		val26, val27, val28);
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
		 String val28
		 ){
	setId(val);
	setType(val2);
	setStatus(val3);
	setDept_id(val4);
	setDeptPhone(val5);
				
	setAccidDate(val6);
	setAccidTime(val7);
	setReported(val8);
	setAccidLocation(val9);
	setEmpInjured(val10);

	setDamage(val11);
	setTotalCost(val12);
	setAutoDamage(val13);
	setAutoPaid(val14);
	setTotalCostP(val15);

	setPropDamage(val16);
	setPropPaid(val17);
	setSubToInsur(val18);
	setWorkComp(val19);
	setWhatProp(val20);

	setRepairInfo(val21);
	setOtherType(val22);
	setPaidByCity(val23);
	setPaidByInsur(val24);
	setMiscByCity(val25);

	setPaidByRisk(val26);
	setDeptContact(val27);
	setOutOfDuty(val28);
    }		
		
    //
    // setters
    //
    public void setType(String val){
	if(val != null && !val.equals(""))
	    type = val;
    }
    public void setDeptPhone(String val){
	if(val != null)
	    deptPhone = val;
    }
    public void setOtherType(String val){
	if(val != null)
	    otherType = val;
    }
    public void setStatus(String val){
	if(val != null)
	    status = val;
    }
    public void setDept_id(String val){
	if(val != null)
	    dept_id = val;
    }
    public void setDeptContact(String val){
	if(val != null)
	    deptContact = val;
    }
    public void setEmpInjured(String val){
	if(val != null)
	    empInjured = val;
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
    public void setOutOfDuty(String val){
	if(val != null)
	    outOfDuty = val;
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
    public void setTotalCost(String val){
	if(val != null)
	    totalCost = Helper.cleanNumber(val);
    }
    public void setTotalCostP(String val){
	if(val != null)
	    totalCostP = Helper.cleanNumber(val);
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
    public void setPaidByRisk(String val){
	if(val != null)
	    paidByRisk = val;
    }
    public void setInsurance(Insurance val){
	if(val != null)
	    insurance = val;
    }
    public void setVehilces(List<Auto> val){
	if(val != null)
	    vehicles = val;
    }
    public void add(Auto val){
	if(vehicles == null)
	    vehicles = new ArrayList<Auto>();
	vehicles.add(val);
    }
    public void setEmployee(Employee val){
	if(val != null && !val.isEmpty()){
	    employee = val;
	}
    }
    public void setDepartment(Department val){
	if(val != null && !val.isEmpty()){
	    department = val;
	    dept_id = department.getId();
	}
    }
    public void addEmployee(Employee val){
	if(employees == null)
	    employees = new ArrayList();
	if(val != null && !val.isEmpty()){
	    employees.add(val);
	}
    }
    public void removeEmployee(Employee val){
	if(val != null){
	    if(employees != null && employees.size() > 0){
		for(int i=0;i<employees.size();i++){
		    Employee emp = (Employee) employees.get(i);
		    if(val.getId().equals(emp.getId())){
			employees.remove(i);
		    }
		}
	    }
	}
    }
    public void setEmployees(List vals){
	employees = vals;
    }
    public void setAutoEstimates(Estimate[] vals){
	autoEstimates = vals;
    }
    public void setPropEstimates(Estimate[] vals){
	propEstimates = vals;
    }
    public void addAutoEstimate(Estimate val){
	if(val != null){
	    if(autoEstimates == null)
		autoEstimates = new Estimate[3];
	    for(int i=0;i<3;i++){
		if(autoEstimates[i] == null){
		    autoEstimates[i] = val;
		    break;
		}
	    }
	}
    }
    public void addPropEstimate(Estimate val){
	if(val != null){
	    if(propEstimates == null)
		propEstimates = new Estimate[3];
	    for(int i=0;i<3;i++){
		if(propEstimates[i] == null){
		    propEstimates[i] = val;
		    break;
		}
	    }
	}
    }
    //
    // getters
    //
    public String getPaidByRisk(){
	return paidByRisk;
    }
    public String getType(){
	return type;
    }
    public String getDept_id(){
	return dept_id;
    }
    public String getDeptContact(){
	return deptContact;
    }
    public String getDeptPhone(){
	return deptPhone;
    }
    public String getOtherType(){
	return otherType;
    }
    public String getStatus(){
	return status;
    }
    public String getEmpInjured(){
	return empInjured;
    }
    public String getAccidDate(){
	return accidDate;
    }
    public String getAccidTime(){
	return accidTime;
    }
    public String getAccidLocation(){
	return accidLocation;
    }
    public String getReported(){
	return reported;
    }
    public String getDamage(){
	return damage;
    }
    public String getOutOfDuty(){
	return outOfDuty;
    }    
    public String getAutoDamage(){
	return autoDamage;
    }
    public String getPropDamage(){
	return propDamage;
    }
    public String getTotalCost(){
	return totalCost;
    }
    public String getTotalCostP(){
	return totalCostP;
    }
    public String getSubToInsur(){
	return subToInsur;
    }
    public String getAutoPaid(){
	return autoPaid;
    }
    public String getPropPaid(){
	return propPaid;
    }
    public String getWorkComp(){
	return workComp;
    }
    public String getRepairInfo(){
	return repairInfo;
    }
    public String getWhatProp(){
	return whatProp;
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
    public String getErrors(){
	return errors;
    }
    public boolean hasErrors(){
	return !errors.equals("");
    }
    public Department  getDepartment(){
	return department;
    }
    public List  getEmployees(){
	return employees;
    }
    public List<Auto>  getVehicles(){
	return vehicles;
    }
    public Estimate[] getAutoEstimates(){
	if(autoEstimates == null){
	    autoEstimates = new Estimate[3];
	}
	for(int i=0;i<3;i++){
	    if(autoEstimates[i] == null){
		autoEstimates[i] = new Estimate(debug);
	    }
	}
	return autoEstimates;
    }
    public Estimate[] getPropEstimates(){
	if(propEstimates == null){
	    propEstimates = new Estimate[3];
	}
	for(int i=0;i<3;i++){
	    if(propEstimates[i] == null){
		propEstimates[i] = new Estimate(debug);
	    }
	}
	return propEstimates;
    }
    public Insurance getInsurance(){
	return insurance;
    }
    public List<Note> getNotes(){
	if(notes == null && !id.equals("")){
	    NoteList sp = new NoteList(debug, id);
	    String back = sp.find();
	    if(back.equals("")){
		List<Note> ones = sp.getNotes();
		if(ones != null &&  ones.size() > 0){
		    notes = ones;
		}
	    }
	}
	return notes;
    }
    public boolean hasNotes(){
	getNotes();
	return notes != null && notes.size() > 0;
    }
    public  boolean isOffDuty(){
	return !outOfDuty.isEmpty();
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
    public String toString(){
	return id;
    }
    public boolean equals(Object o) {
	if (o instanceof MiscAccident) {
	    MiscAccident c = (MiscAccident) o;
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
    //
    // save a new record in the database
    // return "" or any exception thrown by DB
    //
    public String doSave(){

	String msg = "", back="", qq = "";
	Connection con = null;
	PreparedStatement pstmt = null, pstmt2=null, pstmt3=null, pstmt4=null;	
	ResultSet rs = null;	
	System.err.println(" type "+type);
	try{
	    if(employee != null){
		if(!employee.isEmpty()){
		    back = employee.doSave();
		    if(back.equals("")){
			addEmployee(employee);
		    }
		    else{
			logger.error(back);
			msg += " could not save employee data "+back;
		    }
		}
	    }
	    qq = "insert into risk_sequences values(0,?)";
	    if(debug){
		logger.debug(qq);
	    }
	    con = Helper.getConnection();
	    if(con != null){
		pstmt = con.prepareStatement(qq);
	    }
	    else{
		back = "Could not connect to DB ";
		logger.error(back);
		return back;
	    }							
	    pstmt.setString(1, "miscAccident");
	    pstmt.executeUpdate();
	    //
	    // get the id of the new record
	    //
	    qq = "select LAST_INSERT_ID() ";
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt2 = con.prepareStatement(qq);						
	    rs = pstmt2.executeQuery();
	    if(rs.next()){
		id = rs.getString(1);
	    }
	    qq = "insert into misc_accidents values "+
		"(?,?,?,?,?, "+
		"?,?,?,?,?,"+
		"?,?,?,?,?,"+
		"?,?,?,?,?,"+
		"?,?,?,?,?,"+
		"?,?,?)";
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt3 = con.prepareStatement(qq);
	    back = setParam(pstmt3, true);
	    if(back.equals("")){
		pstmt3.executeUpdate();
		//
		// adding employee list
		//
		if(employee != null && !employee.isEmpty()){
		    qq = "insert into empRelated values(?,?)";
		    if(debug){
			logger.debug(qq);
		    }								
		    pstmt4 = con.prepareStatement(qq);
		    pstmt4.setString(1, employee.getId());
		    pstmt4.setString(2, id);
		    pstmt4.executeUpdate();								
		}
		//
		// saving estimates
		//
		for(int i=0;i<3;i++){
		    if(autoEstimates != null){
			if(autoEstimates[i] != null && !autoEstimates[i].isEmpty()){
			    autoEstimates[i].setType("Auto");
			    autoEstimates[i].setRisk_id(id);
			    back = autoEstimates[i].doSave();
			    if(!back.equals("")){
				msg += " could not save auto estiamtes "+back;
				logger.error(back);
			    }
			}
		    }
		    if(propEstimates != null){
			if(propEstimates[i] != null && !propEstimates[i].isEmpty()){
			    propEstimates[i].setType("Prop");
			    propEstimates[i].setRisk_id(id);
			    back = propEstimates[i].doSave();
			    if(!back.equals("")){
				msg += " could not save prop estiamtes "+back;
				logger.error(back);
			    }
			}
		    }
		}
	    }
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    msg += " "+ex.toString();
	}
	finally{
	    Helper.databaseDisconnect(con,rs,  pstmt, pstmt2, pstmt3, pstmt4);
	}
	return msg;
    }
    String setParam(PreparedStatement pstmt, boolean saveAction){
	String back = "";
	try{
	    System.err.println(" type "+type);
	    int jj = 1;
	    if(saveAction)
		pstmt.setString(jj++, id);
	    if(type.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++, type);
	    pstmt.setString(jj++, status);						
						
	    if(dept_id.equals("0") || dept_id.isEmpty())
		pstmt.setNull(jj++, Types.INTEGER);
	    else
		pstmt.setString(jj++, dept_id);
	    if(deptPhone.equals("")){
		pstmt.setNull(jj++,Types.VARCHAR);
	    }
	    else {
		pstmt.setString(jj++, deptPhone);
	    }
	    if(accidDate.equals(""))
		pstmt.setNull(jj++,Types.DATE);
	    else{
		pstmt.setDate(jj++, new java.sql.Date(df.parse(accidDate).getTime()));									
	    }
	    if(accidTime.equals("")){
		pstmt.setNull(jj++,Types.VARCHAR);
	    }
	    else {
		pstmt.setString(jj++, accidTime);
	    }
	    if(reported.equals(""))
		pstmt.setNull(jj++,Types.DATE);
	    else
		pstmt.setDate(jj++, new java.sql.Date(df.parse(reported).getTime()));											
	    if(accidLocation.equals("")){
		pstmt.setNull(jj++,Types.VARCHAR);
	    }
	    else {
		pstmt.setString(jj++, accidLocation);								
	    }
	    if(empInjured.equals("")){
		pstmt.setNull(jj++,Types.CHAR);
	    }
	    else {
		pstmt.setString(jj++, "y");								
	    }
	    if(damage.equals("")){
		pstmt.setNull(jj++,Types.VARCHAR);
	    }
	    else {
		pstmt.setString(jj++, damage);									
	    }
	    if(totalCost.equals("")){
		totalCost="0";
	    }
	    pstmt.setString(jj++, totalCost);
	    if(autoDamage.equals("")){
		pstmt.setNull(jj++,Types.CHAR);
	    }
	    else {
		pstmt.setString(jj++, "y");	
	    }
	    if(autoPaid.equals("")){
		pstmt.setNull(jj++,Types.CHAR);
	    }
	    else {
		pstmt.setString(jj++, "y");	
	    }
	    if(totalCostP.equals("")){
		totalCostP = "0";
	    }
	    pstmt.setString(jj++, totalCostP);
	    if(propDamage.equals("")){
		pstmt.setNull(jj++,Types.CHAR);
	    }
	    else {
		pstmt.setString(jj++, "y");	
	    }
	    if(propPaid.equals("")){
		pstmt.setNull(jj++,Types.CHAR);
	    }
	    else {
		pstmt.setString(jj++, "y");	
	    }
	    if(subToInsur.equals("")){
		pstmt.setNull(jj++,Types.CHAR);
	    }
	    else {
		pstmt.setString(jj++, "y");	
	    }
	    if(workComp.equals("")){
		pstmt.setNull(jj++,Types.CHAR);
	    }
	    else {
		pstmt.setString(jj++, "y");	
	    }
	    if(whatProp.equals("")){
		pstmt.setNull(jj++,Types.VARCHAR);
	    }
	    else {
		pstmt.setString(jj++, whatProp);	
	    }
	    if(repairInfo.equals("")){
		pstmt.setNull(jj++,Types.VARCHAR);
	    }
	    else {
		pstmt.setString(jj++, repairInfo);
	    }
	    if(otherType.equals("")){
		pstmt.setNull(jj++,Types.VARCHAR);
	    }
	    else {
		pstmt.setString(jj++, otherType);
	    }
	    if(paidByCity.equals("")){
		paidByCity = "0";
	    }
	    pstmt.setString(jj++, paidByCity);
	    if(paidByInsur.equals("")){
		paidByInsur =  "0";
	    }
	    pstmt.setString(jj++, paidByInsur);
	    if(miscByCity.equals("")){
		miscByCity = "0";
	    }
	    pstmt.setString(jj++, miscByCity);						
						
	    if(paidByRisk.equals("")){
		paidByRisk = "0";
	    }
	    pstmt.setString(jj++, paidByRisk);
	    if(deptContact.equals("")){
		pstmt.setNull(jj++,Types.VARCHAR);
	    }
	    else {
		pstmt.setString(jj++, deptContact);
	    }
	    if(outOfDuty.equals("")){
		pstmt.setNull(jj++,Types.CHAR);
	    }
	    else {
		pstmt.setString(jj++, "y");								
	    }	    
	    if(!saveAction){ // update
		pstmt.setString(jj++, id);
	    }
	}
	catch(Exception ex){
	    logger.error(ex);
	    back += ex;
	}
	return back;
    }
	
    public     String doUpdate(){

	Connection con = null;
	PreparedStatement pstmt = null;	
	ResultSet rs = null;	

	String str="", msg="", back="";
	String qq = "";
	qq = "update misc_accidents set type=?,status=?,dept_id=?,"+
	    "deptPhone=?,accidDate=?,accidTime=?,reported=?,"+
	    "accidLocation=?,empInjured=?,damage=?,totalCost=?,"+
	    "autoDamage=?,autoPaid=?,totalCostP=?,propDamage=?,"+
	    "propPaid=?,subToInsur=?,workComp=?,whatProp=?,repairInfo=?,"+
	    "otherType=?,paidByCity=?,paidByInsur=?,miscByCity=?,paidByRisk=?,"+
	    "deptContact=?,outOfDuty=? where id=? ";
	if(debug){
	    logger.debug(qq);
	}
	try{
	    con = Helper.getConnection();
	    if(con == null){
		msg = "Unable to connect to DB ";
		return msg;
	    }
	    pstmt	= con.prepareStatement(qq);				
	    back = setParam(pstmt, false);
	    pstmt.executeUpdate();
	    if(insurance != null){
		back = insurance.doSave();
		if(!back.equals("")){
		    msg += " Could not update insurance "+back;
		}
	    }
	    if(employee != null && !employee.isEmpty()){
		back = employee.doSave(); // save / update
		if(!back.equals("")){
		    msg += " Could not save/update employee data "+back;
		}
		qq = "insert into empRelated values(?,?)";
		if(debug){
		    logger.debug(qq);
		}								
		pstmt = con.prepareStatement(qq);
		pstmt.setString(1, employee.getId());
		pstmt.setString(2, id);
		pstmt.executeUpdate();								
	    }
	    if(autoEstimates != null){
		for(int i=0;i<autoEstimates.length;i++){
		    if(!autoEstimates[i].isEmpty()){
			autoEstimates[i].setRisk_id(id);
			back = autoEstimates[i].doSave(); // or update
			if(!back.equals("")){
			    logger.error(back);
			    msg += " Could not update auto estimate data "+back;
			}
		    }
		}
	    }
	    if(propEstimates != null){
		for(int i=0;i<propEstimates.length;i++){
		    if(!propEstimates[i].isEmpty()){
			propEstimates[i].setRisk_id(id);
			back = propEstimates[i].doSave();
			if(!back.equals("")){
			    msg += " Could not update property estimate data "+back;
			    logger.error(back);
			}
		    }
		}
	    }
	    // employee list
	    EmployeeList eml = new EmployeeList(debug);
	    eml.setRelatedId(id);
	    back = eml.lookFor();
	    if(back.equals("")){
		employees = eml.getEmployees();
	    }
	    else{
		msg += back;
	    }
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    msg += " "+ex.toString();
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return msg;
    }
    //
    public String doDelete(){
	//
	// System.err.println("delete record");
	//
	Connection con = null;
	PreparedStatement stmt = null, stmt2=null,stmt3=null,stmt4=null;	
	ResultSet rs = null;	
		
	String back = "";
	String qq = "delete from risk_estimates where risk_id=?";
	String qq2 = "delete from risk_autos where risk_id=?";
	String qq3 = "delete from empRelated where risk_id=?";
	String qq4 = "delete from misc_accidents where id=?";
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
	    stmt.executeUpdate();
	    qq = qq2;
	    if(debug)
		logger.debug(qq);
	    stmt2 = con.prepareStatement(qq);
	    stmt2.setString(1, id);
	    stmt2.executeUpdate();						
	    qq = qq3;
	    if(debug)
		logger.debug(qq3);
	    stmt3 = con.prepareStatement(qq);
	    stmt3.setString(1, id);
	    stmt3.executeUpdate();
	    qq = qq4;
	    if(debug)
		logger.debug(qq4);
	    stmt4 = con.prepareStatement(qq);
	    stmt4.setString(1, id);
	    stmt4.executeUpdate();
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    back =  ex.toString()+":"+qq;
	}
	finally{
	    Helper.databaseDisconnect(con, rs, stmt, stmt2, stmt3, stmt4);
	}
	return back;
    }
    //
    public String doSelect(){
	//
	Connection con = null;
	PreparedStatement pstmt = null;	
	ResultSet rs = null;	

	String msg = "", back="";
	String qq = "select id,"+
	    "type,status,dept_id,deptPhone,"+
						
	    "date_format(accidDate,'%m/%d/%Y'),"+
	    "accidTime,"+ 
	    "date_format(reported,'%m/%d/%Y'),"+
	    "accidLocation,"+
	    "empInjured,"+
						
	    "damage,"+
	    "totalCost,"+						
	    "autoDamage,"+
	    "autoPaid,"+
	    "totalCostP, "+
						
	    "propDamage,"+
	    "propPaid,"+
	    "subToInsur, "+
	    " workComp,"+
	    " whatProp,"+
						
	    " repairInfo, "+
	    " otherType, "+
	    " paidByCity,paidByInsur,miscByCity, "+
						
	    " paidByRisk, "+
	    " deptContact, "+
	    " outOfDuty "+
	    " from misc_accidents where id=?";
	String str="";
	try{
	    if(debug){
		logger.debug(qq);
	    }
	    con = Helper.getConnection();
	    if(con == null){
		back = "Could not connect to DB ";
		logger.error(back);
		return back;
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, id);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		setVals(
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
			rs.getString(28));
		back = doRefresh();
		if(!back.equals("")){
		    msg += back;
		}
	    }
	}
	catch(Exception ex){
	    logger.error(ex+":"+qq);
	    return msg += " "+ex.toString()+":"+qq;
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return msg;
    }
    public String doRefresh(){
		
	String msg="", back="";
	EstimateList el = new EstimateList(debug);
	el.setRisk_id(id);
	el.setType("Auto");
	back = el.lookFor();
	if(back.equals("")){
	    autoEstimates = el.getEstimates();
	}
	else{
	    logger.error(back);
	    msg += back;
	}
	el = new EstimateList(debug);
	el.setRisk_id(id);
	el.setType("Prop");
	back = el.lookFor();
	if(back.equals("")){
	    propEstimates = el.getEstimates();
	}
	else{
	    logger.error(back);
	    msg += back;
	}
	// employee list
	EmployeeList eml = new EmployeeList(debug,id);
	back = eml.lookFor();
	if(back.equals("")){
	    employees = eml.getEmployees();
	}
	else{
	    logger.error(back);
	    msg += back;
	}
	if(!dept_id.equals("")){
	    department = new Department(debug, dept_id);
	    department.doSelect();
	}
	// look for autos
	AutoList sa = new AutoList(debug, id);
	back = sa.lookFor();
	if(back.equals("")){
	    vehicles = sa.getAutos();
	}
	return msg;
    }
		
}























































package risks.web;

import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.models.*;
import risks.lists.*;
import risks.utils.*;
/**
 * 
 */
@WebServlet(urlPatterns = {"/SafetyServ","/Safety"})
public class SafetyServ extends TopServlet{

    static Logger logger = LogManager.getLogger(SafetyServ.class);
    RiskTypeList types = null;
    //
    /**
     * Generates the Case form and processes view, add, update and delete
     * operations.
     * @param req
     * @param res
     *
     */
    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException {
	doPost(req,res);
    }
    /**
     * @link #doGet
     */

    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException {
    
	String  
	    type="", id="", status="", relatedId="",
	    empid="", empSuper="", empInjured="",
	    empName="", deptPhone="", dept_id="", 
	    vin="", empPhone="",
	    accidDate="",reported="", accidLocation="", accidTime="",
	    autoPlate="", autoMake ="", autoModel="", autoYear="",
	    estPlace="", estPlace2="", estPlace3="",
	    estCost="", estCost2="", estCost3="", 
	    estPlaceP="", estPlaceP2="", estPlaceP3="",
	    estCostP="", estCostP2="", estCostP3="", 

	    otherType="", empTitle="",
	    damage="", chosenDealer="", chosenDealerP="",
	    subToInsur="", autoPaid="", propPaid="", autoNum="",
	    deptName="";
	String insurance="",insurStatus="", adjuster="",adjusterPhone="",
	    adjusterEmail="", attorney="", attorneyPhone="", attorneyEmail="",
	    deductible="",claimNum="", policy="";
	String paidByCity="",paidByInsur="",miscByCity="",paidByRisk="";
	String // newly added
	    est_1="",est_2="",est_3="",totalCost="", totalCostP="",
	    estp_1="",estp_2="",estp_3="",
	    repairInfo="", safeId="",
	    propDamage="",autoDamage="",workComp="",whatProp="", 
	    tortId="", vsId="", recordOnly="", balance="", outOfDuty="";
	
	boolean connectDbOk = false, success = true;
       	String message = "", action = "",entry_time = "", 
	    entry_date = "", prevAction="";
	
	res.setContentType("text/html");
	PrintWriter out = res.getWriter();
	String name, value;
	Enumeration values = req.getParameterNames();
	String [] vals;
	String [] delAutoAid = null;
	String [] delRelated = null;
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();	
	    value = Helper.replaceSpecialChars(value);

	    if (name.equals("type")) {
		type =value;
	    }
	    else if (name.equals("id")) {
		id =value;
	    }
	    else if (name.equals("relatedId")) {
		relatedId =value;
	    }
	    else if (name.equals("status")) {
		status =value;
	    }
	    else if(name.equals("delAutoAid")){
		delAutoAid = vals; // this is array
	    }
	    else if(name.equals("delRelated")){
		delRelated = vals; // this is array
	    }
	    else if (name.equals("autoDamage")) {
		autoDamage =value;
	    }
	    else if (name.equals("propDamage")) {
		propDamage =value;
	    }
	    else if (name.equals("autoPlate")) {
		autoPlate =value;
	    }
	    else if (name.equals("empName")) {
		empName = value.toUpperCase();
	    }
	    else if (name.equals("deptPhone")) {
		deptPhone = value;
	    }
	    else if (name.equals("dept_id")) {
		dept_id = value;
	    }
	    else if (name.equals("paidByCity")) {
		paidByCity = value;
	    }
	    else if (name.equals("paidByRisk")) {
		paidByRisk = value;
	    }
	    else if (name.equals("miscByCity")) {
		miscByCity = value;
	    }
	    else if (name.equals("paidByInsur")) {
		paidByInsur = value;
	    }
	    else if (name.equals("workComp")) {
		workComp = value;
	    }
	    else if (name.equals("whatProp")) {
		whatProp = value;
	    }
	    else if (name.equals("repairInfo")) {
		repairInfo = value;
	    }
	    else if (name.equals("tortId")) {
		tortId = value;
	    }
	    else if (name.equals("vsId")) {
		vsId = value;
	    }
	    else if (name.equals("safeId")) {
		safeId = value;
	    }
	    else if (name.equals("otherType")) {
		otherType = value;
	    }
	    else if (name.equals("deptName")) {
		deptName = value;
	    }
	    else if (name.equals("empid")) {
		empid = value;
	    }
	    else if (name.equals("empPhone")) {
		empPhone = value;
	    }
	    else if (name.equals("empSuper")) {
		empSuper = value;
	    }
	    else if (name.equals("empTitle")) {
		empTitle = value;
	    }
	    else if (name.equals("empInjured")) {
		empInjured = value;
	    }
	    else if (name.equals("subToInsur")) {
		subToInsur = value;
	    }
	    else if (name.equals("autoPaid")) {
		autoPaid = value;
	    }
	    else if (name.equals("propPaid")) {
		propPaid = value;
	    }
	    else if (name.equals("outOfDuty")) {
		outOfDuty = value;
	    }	    
	    else if (name.equals("vin")) {
		vin = value;
	    }
	    else if (name.equals("autoNum")) {
		autoNum = value;
	    }
	    else if (name.equals("autoMake")) {
		autoMake = value.toUpperCase();
	    }
	    else if (name.equals("autoModel")) {
		autoModel = value.toUpperCase();
	    }
	    else if (name.equals("autoYear")) {
		autoYear = value;
	    }
	    else if (name.equals("accidDate")) {
		accidDate =value;
	    }
	    else if (name.equals("accidTime")) {
		accidTime =value;
	    }
	    else if (name.equals("accidLocation")) {
		accidLocation =value;
	    }
	    else if (name.equals("damage")) {
		damage = value;
	    }
	    else if (name.equals("est_1")) {
		est_1 = value;
	    }
	    else if (name.equals("est_2")) {
		est_2 = value;
	    }
	    else if (name.equals("est_3")) {
		est_3 = value;
	    }
	    else if (name.equals("estp_1")) {
		estp_1 = value;
	    }
	    else if (name.equals("estp_2")) {
		estp_2 = value;
	    }
	    else if (name.equals("estp_3")) {
		estp_3 = value;
	    }
	    else if (name.equals("reported")) {
		reported = value;
	    }
	    else if (name.equals("estPlace")) {
		estPlace = value;
	    }
	    else if (name.equals("estPlace2")) {
		estPlace2 = value;
	    }
	    else if (name.equals("estPlace3")) {
		estPlace3 = value;
	    }
	    else if (name.equals("estPlaceP")) {
		estPlaceP = value;
	    }
	    else if (name.equals("estPlaceP2")) {
		estPlaceP2 = value;
	    }
	    else if (name.equals("estPlaceP3")) {
		estPlaceP3 = value;
	    }
	    else if (name.equals("estCost")) {
		estCost = value;
	    }
	    else if (name.equals("estCost2")) {
		estCost2 = value;
	    }
	    else if (name.equals("estCost3")) {
		estCost3 = value;
	    }
	    else if (name.equals("estCostP")) {
		estCostP = value;
	    }
	    else if (name.equals("estCostP2")) {
		estCostP2 = value;
	    }
	    else if (name.equals("estCostP3")) {
		estCostP3 = value;
	    }
	    else if (name.equals("totalCost")) {
		totalCost = value;
	    }
	    else if (name.equals("totalCostP")) {
		totalCostP = value;
	    }
	    else if (name.equals("chosenDealer")) {
		chosenDealer = value;
	    }
	    else if (name.equals("prevAction")){ 
		prevAction = value;  
	    }
	    else if (name.equals("insurance")) {
		insurance = value;
	    }
	    else if (name.equals("insurStatus")) {
		insurStatus = value;
	    }
	    else if (name.equals("adjuster")) {
		adjuster = value;
	    }
	    else if (name.equals("adjusterPhone")) {
		adjusterPhone = value;
	    }
	    else if (name.equals("adjusterEmail")) {
		adjusterEmail = value;
	    }
	    else if (name.equals("attorney")) {
		attorney = value;
	    }
	    else if (name.equals("attorneyPhone")) {
		attorneyPhone = value;
	    }
	    else if (name.equals("attorneyEmail")) {
		attorneyEmail = value;
	    }
	    else if (name.equals("deductible")) {
		deductible = value;
	    }
	    else if (name.equals("claimNum")) {
		claimNum = value;
	    }
	    else if (name.equals("policy")) {
		policy = value;
	    }
	    else if (name.equals("recordOnly")) {
		recordOnly =value;
	    }
	    else if (name.equals("action")){ 
		// Get, Save, zoom, edit, delete, New, Refresh
		action = value;  
	    }
	    else if (name.equals("action2")){
		if(!value.equals(""))
		    action = value;  
	    }						
	}
	HttpSession session = session = req.getSession(false);
	User user = null;
	if(session != null){
	    user = (User)session.getAttribute("user");
	}
	if(user == null){
	    res.sendRedirect(url+"Login");
	    return;
	}		
	Safety sf = new Safety(debug);
	if(!action.equals("") &&
	   !action.equals("Update") &&
	   action.equals(prevAction)){
	    action = "";
	}
	else if(!prevAction.equals("")){
	    //
	    // this senario should not happen
	    // acion     prevAction
	    // ======    ========
	    // zoom,     update
	    // save,     update
	    // Generate, Save
	    //
	    if(action.equals("zoom") || action.equals("Generate"))
		action = "";
	}
	if(types == null){
	    types = new RiskTypeList(debug, "unified");
	    String msg = types.lookFor();
	    if(!msg.equals("")){
		logger.error(msg);
		message += msg;
		success = false;
		types = null;
	    }
	}
	if(!est_1.equals("")){
	    chosenDealer="1";
	}
	else if(!est_2.equals("")){
	    chosenDealer="2";
	}
	else if(!est_3.equals("")){
	    chosenDealer="3";
	}
	if(!estp_1.equals("")){
	    chosenDealerP="1";
	}
	else if(!estp_2.equals("")){
	    chosenDealerP="2";
	}
	else if(!estp_3.equals("")){
	    chosenDealerP="3";
	}
	if(action.equals("Generate")){
	    //
	    // We are coming from TortClaimServ or VSLegalServ
	    // Using the tortId, or vsId to find if any safety is 
	    // associated with this record
	    //
	    // 
	    //  first time 
	    //  let us get the info that is useful 
	    //  and change the action to '' for a new record
	    //
	    if(!tortId.equals("")){
		TortClaim tc = new TortClaim(debug);
		tc.setId(tortId);
		String back = tc.doSelect();
		if(back.equals("")){
		    type = tc.getType();
		    deductible = tc.getDeductible();
		    paidByCity = tc.getPaidByCity();
		    paidByRisk = tc.getPaidByRisk();
		    paidByInsur = tc.getPaidByInsur();
		    miscByCity = tc.getMiscByCity();

		    accidDate = tc.getIncidentDate();
		    damage = tc.getIncident();
		    autoDamage = tc.getCityAutoInc();
					
		    otherType = tc.getOtherType();
		    subToInsur = tc.getSubInsur();
		    recordOnly = tc.getRecordOnly();
		}
		status = "Open";
		action = "Save";
		relatedId = tortId;
	    }
	    else if(!vsId.equals("")){
		Legal tc = new Legal(debug);
		tc.setId(vsId);
		String back = tc.doSelect();
		if(back.equals("")){
		    type = tc.getType();
		    accidDate = tc.getDoi();
		    accidLocation = tc.getLocation();
					
		    autoDamage = tc.getCityAutoInc();
		    deductible = tc.getDeductible();
		    damage = tc.getDescription();
		    recordOnly = tc.getRecordOnly();
		    paidByCity = tc.getPaidByCity();
		    paidByRisk = tc.getPaidByRisk();
		    paidByInsur = tc.getPaidByInsur();
		    miscByCity = tc.getMiscByCity();
		    subToInsur = tc.getRiskToInsur();
		}
		status = "Open";
		action = "Save";
		relatedId = vsId;
	    }
	    else if(!safeId.equals("")){
		sf.setId(safeId);
		String back = sf.doSelect();
		if(back.equals("")){
		    type = sf.getType();
		    status = sf.getStatus();
					
		    accidDate = sf.getAccidDate();
		    accidTime = sf.getAccidTime();
		    accidLocation = sf.getAccidLocation();
		    damage = sf.getDamage();
		    autoDamage = sf.getAutoDamage();
		    propDamage = sf.getPropDamage();
		    reported = sf.getReported();

		    estPlace = sf.getEstPlace();
		    estPlace2 = sf.getEstPlace2();
		    estPlace3 = sf.getEstPlace3();
					
		    estCost = sf.getEstCost();
		    estCost2 = sf.getEstCost2();
		    estCost3 = sf.getEstCost3();
		    totalCost = sf.getTotalCost();
		    chosenDealer = sf.getChosenDealer();
					
		    estPlaceP = sf.getEstPlaceP();
		    estPlaceP2 = sf.getEstPlaceP2();
		    estPlaceP3 = sf.getEstPlaceP3();
					
		    estCostP = sf.getEstCostP();
		    estCostP2 = sf.getEstCostP2();
		    estCostP3 = sf.getEstCostP3();
		    totalCostP = sf.getTotalCostP();
		    chosenDealerP = sf.getChosenDealerP();
		    subToInsur = sf.getSubToInsur();
		    autoPaid = sf.getAutoPaid();
		    propPaid = sf.getPropPaid();
		    workComp = sf.getWorkComp();
		    whatProp = sf.getWhatProp();
		    repairInfo = sf.getRepairInfo();
		    empInjured = sf.getEmpInjured();
		    deductible = sf.getDeductible();
		    paidByCity = sf.getPaidByCity();
		    paidByRisk = sf.getPaidByRisk();
		    miscByCity = sf.getMiscByCity();
		    paidByInsur = sf.getPaidByInsur();
		    recordOnly = sf.getRecordOnly();
		    outOfDuty = sf.getOutOfDuty();
		    status = "Open";
		    action = "Save";
		    relatedId = safeId;
		}
	    }
	    if(!(dept_id.equals("") || dept_id.equals("0"))){
		Department dp = new Department(debug, dept_id);
		String back = dp.doSelect();
		if(back.equals("")){
		    deptName = dp.getName();
		}
		dp = null;
	    }
	}
	if(action.equals("Save")){
	    //
	    // get the dept id first
	    if(!deptName.equals("")){
		Department dp = new Department(debug);
		dp.setName(deptName);
		String str = dp.findOrAddDept();
		if(!str.equals("")) dept_id = str;
		dp = null;
	    }
	    sf.setType(type);
	    sf.setOtherType(otherType);
	    sf.setStatus(status);
	    sf.setEmpInjured(empInjured);
	    sf.setEmpTitle(empTitle);

	    sf.setAccidDate(accidDate);
	    sf.setAccidTime(accidTime);
	    sf.setAccidLocation(accidLocation);
	    sf.setDamage(damage);
	    sf.setAutoDamage(autoDamage);
	    sf.setPropDamage(propDamage);
	    sf.setReported(reported);

	    sf.setEstPlace(estPlace);
	    sf.setEstPlace2(estPlace2);
	    sf.setEstPlace3(estPlace3);
	    sf.setEstCost(estCost);
	    sf.setEstCost2(estCost2);
	    sf.setEstCost3(estCost3);
	    sf.setTotalCost(totalCost);
	    sf.setChosenDealer(chosenDealer);
	    sf.setEstPlaceP(estPlaceP);
	    sf.setEstPlaceP2(estPlaceP2);
	    sf.setEstPlaceP3(estPlaceP3);
	    sf.setEstCostP(estCostP);
	    sf.setEstCostP2(estCostP2);
	    sf.setEstCostP3(estCostP3);
	    sf.setTotalCostP(totalCostP);
	    sf.setChosenDealerP(chosenDealerP);
	    sf.setSubToInsur(subToInsur);
	    sf.setAutoPaid(autoPaid);
	    sf.setPropPaid(propPaid);
	    sf.setWorkComp(workComp);
	    sf.setWhatProp(whatProp);
	    sf.setRepairInfo(repairInfo);
	    sf.setDeductible(deductible);
	    sf.setPaidByCity(paidByCity);
	    sf.setPaidByRisk(paidByRisk);
	    sf.setPaidByInsur(paidByInsur);
	    sf.setMiscByCity(miscByCity);
	    sf.setRecordOnly(recordOnly);
	    sf.setOutOfDuty(outOfDuty);
	    String back = sf.doSave();
	    if(back.equals("")){
		id = sf.getId();
		message += "Data saved successfully";
		if(!vin.equals("") || !autoPlate.equals("") || 
		   !autoMake.equals("")){
		    Auto auto = new Auto(debug);
		    auto.setId(id);
		    auto.setVin(vin);
		    auto.setAutoMake(autoMake);
		    auto.setAutoNum(autoNum);
		    auto.setAutoModel(autoModel);
		    auto.setAutoYear(autoYear);
		    auto.setAutoPlate(autoPlate);
		    back = auto.doSave();
		    if(!back.equals("")){
			message += back;
			logger.error(back);
			success = false;
		    }
		}
		if(!relatedId.equals("")){
		    Related rel = new Related(id,relatedId,"Internal Accident", debug);
		    back = rel.doSave();
		    if(!back.equals("")){
			message += back;
			logger.error(back);
			success = false;
		    }	
		    // make a link to the insurance id's
		    InsuranceList il = new InsuranceList(debug);
		    il.setRelatedId(relatedId);
		    il.find();
		    List<String> ls = il.getIdList();
		    if(ls != null){
			back = il.addLinks(id);
			if(!back.equals("")){
			    message += back;
			    System.err.println(back);
			    success = false;
			}		
		    }
		    // make a link to the employee's id's
		    EmployeeList el = new EmployeeList(debug);
		    el.setRelatedId(relatedId);
		    el.find();
		    ls = el.getIdList();
		    if(ls != null){
			back = el.addLinks(id);
			if(!back.equals("")){
			    message += back;
			    success = false;
			}		
		    }	
		}
		relatedId = "";
	    }
	    else{
		message += back;
		System.err.println(back);
		success = false;
	    }
	}
	else if(action.equals("Update")){
	    //
	    // get the dept id first
	    if(!deptName.equals("")){
		Department dp = new Department(debug);
		dp.setName(deptName);
		String str = dp.findOrAddDept();
		if(!str.equals("")) dept_id = str;
		dp = null;
	    }
	    sf.setId(id);
	    sf.setType(type);
	    sf.setOtherType(otherType);
	    sf.setStatus(status);

	    sf.setEmpInjured(empInjured);

	    sf.setAccidDate(accidDate);
	    sf.setAccidTime(accidTime);
	    sf.setAccidLocation(accidLocation);
	    sf.setDamage(damage);
	    sf.setAutoDamage(autoDamage);
	    sf.setPropDamage(propDamage);
	    sf.setReported(reported);

	    sf.setEstPlace(estPlace);
	    sf.setEstPlace2(estPlace2);
	    sf.setEstPlace3(estPlace3);
	    sf.setEstCost(estCost);
	    sf.setEstCost2(estCost2);
	    sf.setEstCost3(estCost3);
	    sf.setTotalCost(totalCost);
	    sf.setChosenDealer(chosenDealer);
	    sf.setEstPlaceP(estPlaceP);
	    sf.setEstPlaceP2(estPlaceP2);
	    sf.setEstPlaceP3(estPlaceP3);
	    sf.setEstCostP(estCostP);
	    sf.setEstCostP2(estCostP2);
	    sf.setEstCostP3(estCostP3);
	    sf.setTotalCostP(totalCostP);
	    sf.setChosenDealerP(chosenDealerP);
	    sf.setSubToInsur(subToInsur);
	    sf.setAutoPaid(autoPaid);
	    sf.setPropPaid(propPaid);
	    sf.setWorkComp(workComp);
	    sf.setWhatProp(whatProp);
	    sf.setRepairInfo(repairInfo);
	    sf.setVsId(vsId);
	    sf.setDeductible(deductible);
	    sf.setPaidByCity(paidByCity);
	    sf.setPaidByRisk(paidByRisk);
	    sf.setPaidByInsur(paidByInsur);
	    sf.setMiscByCity(miscByCity);
	    sf.setRecordOnly(recordOnly);
	    sf.setOutOfDuty(outOfDuty);
	    String back = sf.doUpdate();
	    if(!back.equals("")){
		message += back;
		logger.error(back);
		success = false;
	    }
	    else{
		message += "Data updated successfully";
	    }
	    if(!vin.equals("") || !autoPlate.equals("") || 
	       !autoMake.equals("")){
		Auto auto = new Auto(debug);
		auto.setId(id);
		auto.setVin(vin);
		auto.setAutoMake(autoMake);
		auto.setAutoModel(autoModel);
		auto.setAutoYear(autoYear);
		auto.setAutoPlate(autoPlate);
		auto.setAutoNum(autoNum);
		back = auto.doSave();
		if(!back.equals("")){
		    message += back;
		    logger.error(back);
		    success = false;
		}
	    }
	    if(delAutoAid != null){
		for(int i=0;i<delAutoAid.length;i++){
		    String aid = delAutoAid[i];
		    Auto auto = new Auto(debug);
		    auto.setId(aid);
		    auto.doDelete();
		}
	    }
	    if(!relatedId.equals("")){
		Related rel = new Related(id,relatedId,"Internal Accident", debug);
		back = rel.doSave();
		if(!back.equals("")){
		    message += back;
		    logger.error(back);
		    success = false;
		}	
	    }
	    if(delRelated != null){
		for(String str: delRelated){
		    Related rel = new Related(id, str, debug);
		    rel.doDelete();
		}
	    }
	}
	else if(action.equals("Delete")){
	    //
	    // System.err.println("delete record");
	    //
	    sf.setId(id);
	    String back = sf.doDelete();
	    if(!back.equals("")){
		message += back;
		logger.error(back);
		success = false;
	    }
	    //
	}
	else if(!id.equals("")){	
	    //
	    sf.setId(id);
	    String back = sf.doSelect();
	    if(back.equals("")){
		type = sf.getType();
		status = sf.getStatus();

		accidDate = sf.getAccidDate();
		accidTime = sf.getAccidTime();
		accidLocation = sf.getAccidLocation();
		damage = sf.getDamage();
		autoDamage = sf.getAutoDamage();
		propDamage = sf.getPropDamage();
		reported = sf.getReported();

		estPlace = sf.getEstPlace();
		estPlace2 = sf.getEstPlace2();
		estPlace3 = sf.getEstPlace3();

		estCost = sf.getEstCost();
		estCost2 = sf.getEstCost2();
		estCost3 = sf.getEstCost3();
		totalCost = sf.getTotalCost();
		chosenDealer = sf.getChosenDealer();

		estPlaceP = sf.getEstPlaceP();
		estPlaceP2 = sf.getEstPlaceP2();
		estPlaceP3 = sf.getEstPlaceP3();

		estCostP = sf.getEstCostP();
		estCostP2 = sf.getEstCostP2();
		estCostP3 = sf.getEstCostP3();
		totalCostP = sf.getTotalCostP();
		chosenDealerP = sf.getChosenDealerP();
		subToInsur = sf.getSubToInsur();
		autoPaid = sf.getAutoPaid();
		propPaid = sf.getPropPaid();
		workComp = sf.getWorkComp();
		whatProp = sf.getWhatProp();
		repairInfo = sf.getRepairInfo();
		empInjured = sf.getEmpInjured();
		deductible = sf.getDeductible();
		empTitle = sf.getEmpTitle();
		paidByCity = sf.getPaidByCity();
		paidByRisk = sf.getPaidByRisk();
		miscByCity = sf.getMiscByCity();
		paidByInsur = sf.getPaidByInsur();
		recordOnly = sf.getRecordOnly();
		outOfDuty = sf.getOutOfDuty();
		if(!chosenDealer.equals("")){
		    int dealer = Integer.parseInt(chosenDealer);
		    switch(dealer){
		    case 1: est_1="y";
			break;
		    case 2: est_2="y";
			break;
		    case 3: est_3="y";
		    }
		}
		if(!chosenDealerP.equals("")){
		    int dealer = Integer.parseInt(chosenDealerP);
		    switch(dealer){
		    case 1: estp_1="y";
			break;
		    case 2: estp_2="y";
			break;
		    case 3: estp_3="y";
		    }
		}
		if(!(dept_id.equals("") || dept_id.equals("0"))){
		    Department dp = new Department(debug, dept_id);
		    back = dp.doSelect();
		    if(back.equals("")){
			deptName = dp.getName();
		    }
		    dp = null;
		}
		Payment pay = new Payment(debug);
		pay.setRisk_id(id);
		pay.setType("safety");
		balance = pay.getTotalBalance();
		String tpay = pay.getTotalPayment();
		if(!tpay.equals("") && !tpay.equals("0.0")){
		    paidByInsur = tpay;
		}
	    }
	    else{
		message += back;
		logger.error(back);
		success = false;
	    }
	}
	if(!empInjured.equals("")) empInjured = "checked";
	if(!est_1.equals("")) est_1 = "checked";
	else if(!est_2.equals("")) est_2 = "checked";
	else if(!est_3.equals("")) est_3 = "checked";
	if(!estp_1.equals("")) estp_1 = "checked";
	else if(!estp_2.equals("")) estp_2 = "checked";
	else if(!estp_3.equals("")) estp_3 = "checked";
	if(!autoDamage.equals("")) autoDamage = "checked";
	if(!propDamage.equals("")) propDamage = "checked";
	if(!subToInsur.equals("")) subToInsur = "checked";
	if(!autoPaid.equals("")) autoPaid = "checked";
	if(!propPaid.equals("")) propPaid = "checked";
	if(!workComp.equals("")) workComp = "checked";
	if(!recordOnly.equals("")) recordOnly = "checked";
	if(!outOfDuty.equals("")) outOfDuty = "checked";
	
	if(!prevAction.equals("") && action.equals("")) 
	    action = prevAction;
	if(!prevAction.equals("")) action = prevAction;
	//
	// Inserts
	out.println(Inserts.xhtmlHeaderInc);
	out.println(Inserts.banner(url));
	out.println(Inserts.menuBar(url, true));
	out.println(Inserts.sideBar(url));
	out.println("<div id=\"mainContent\">");
	//
	out.println("<h3 class=\"titleBar\">Internal Accident "+id+"</h3>");
	//
	// if we have any message, it will be shown here
	if(success){
	    if(!message.equals(""))
		out.println("<h3>"+message+"</h3>");
	}
	else{
	    if(!message.equals(""))
		out.println("<h3 class=\"errorMessages\">"+message+"</h3>");
	}
	//
	out.println("<script type=\"text/javascript\">");
	out.println("  function validateForm(){	              ");
	//
	out.println("  return true;                           ");
				
	out.println("  }                                      ");
	out.println("  function doUpdate(){	              ");
	if(id.equals("")){
	    out.println("  document.getElementById('action_id').value='Save';");
	}
	else{
	    out.println("  document.getElementById('action_id').value='Update';");
	}
	out.println("  document.forms[0].submit(); ");
	out.println("  return true;                           ");
	out.println("  }                                      ");  				
	out.println("</script>                                ");
	out.println("<form id=\"myForm\" name=\"myForm\" method=\"post\" "+
		    "onsubmit=\"return validateForm();\">");
	if(!id.equals("")){
	    out.println("<input type=\"hidden\" name=\"id\" value=\""+id+
			"\">");
	}
	out.println("<input type=\"hidden\" name=\"action2\" value=\"\" id=\"action_id\" />");
	out.println("<fieldset><legend>Status </legend>");
	out.println("<table>");

	out.println("<tr><td><label for=\"status\">Status: </label>"+
		    "<select name=\"status\" id=\"status\">");
	for(int i=0;i<Inserts.statusArr.length;i++){
	    if(status.equals(Inserts.statusArr[i]))
		out.println("<option selected=\"selected\">"+status+
			    "</option>");
	    else
		out.println("<option>"+Inserts.statusArr[i]+"</option>");
	}
	out.println("</select> &nbsp;&nbsp;");
	out.println("<input type=\"checkbox\" name=\"recordOnly\" value=\"y\" "+
		    recordOnly+"><label>Record Only</label>");

	if(!action.equals("")){
	    out.println("<input type=\"hidden\" name=\"prevAction\" "+
			"value=\""+action+"\">");
	}
	out.println("</td></tr>");
	out.println("<tr><td><label for=\"type\">Type: </label>"+
		    "<select name=\"type\" id=\"type\">");
	out.println("<option value=\"\">All</option>");
	if(types != null){
	    for(RiskType rt:types){
		if(type.equals(rt.getId()))
		    out.println("<option value=\""+type+
				"\" selected=\"selected\">"+rt.getName()+
				"</option>");
		else
		    out.println("<option value=\""+rt.getId()+"\">"+
				rt.getName()+"</option>");
	    }
	}
	out.println("</select><label for=\"otherType\">Other (specify):"+
		    "<input name=\"otherType\" id=\"otherType\" "+
		    "value=\""+otherType+"\" "+
		    "size=\"25\" maxlength=\"30\" /></td></tr>");

	out.println("<tr><td><label for=\"accidDate\">Accident; Date: "+
		    "</label>"+
		    "<input name=\"accidDate\" id=\"accidDate\" value="+
		    "\""+accidDate+"\" size=\"10\" maxlength=\"10\" "+
		    "class=\"date\" "+
		    "/>");
	out.println("<label for=\"accidTime\"> Time: "+
		    "</label>"+
		    "<input name=\"accidTime\" id=\"accidTime\" value="+
		    "\""+accidTime+"\" size=\"10\" maxlength=\"10\" />");
	out.println("</td></tr><tr><td>");
	out.println("<label for=\"reported\">Reported Date: </label>"+
		    "<input name=\"reported\" id=\"reported\" value="+
		    "\""+reported+"\" size=\"10\" maxlength=\"10\" "+
		    "class=\"date\" "+
		    "/>, ");
	out.println("<input name=\"subToInsur\" id=\"subToInsur\" "+
		    "value=\"y\" type=\"checkbox\" "+subToInsur+" />"+
		    "<label>Submit to Insurance </label>");
	out.println("</td></tr>");
	out.println("<tr><td><label for=\"damage\">Accident Details "+
		    "</label><br />");
	out.println("<textarea name=\"damage\" id=\"damage\" rows=\"5\" "+
		    "cols=\"70\" wrap=\"wrap\">");
	out.println(damage);
	out.println("</textarea></td></tr>");
	out.println("<tr><td><label for=\"accidLocation\">Accident Location: "+
		    "</label>"+
		    "<input name=\"accidLocation\" id=\"accidLocation\" value="+
		    "\""+accidLocation+"\" size=\"60\" maxlength=\"80\" />");
	out.println("</td></tr>");
	out.println("</table>");
	out.println("</fieldset>");
	out.println("<fieldset>");
	out.println("<legend>Related Records</legend>");
	out.println("<table class='wide'>");
	out.println("<tr><td>");
	out.println("In the field below enter the record ID of related Tort Claim, Recovery Action, Internal Accident, Worker&apos;s Comp, or Natural Disaster");
	out.println("</td></tr>");
	out.println("<tr><td>");
	String related=""; // if we are coming from tort, or recovery
	out.println("<label for=\"relatedId\">Related Record ID:</label>"+
		    "<input name=\"relatedId\" id=\"relatedId\" value=\""+
		    "\" size=\"8\" maxlength=\"8\" /> Enter one at a time. ");
	out.println("</td></tr>");
	if(!id.equals("")){
	    List<Related> dups = null;
	    RelatedList rl = new RelatedList(debug, id);
	    String back = rl.lookFor();
	    if(back.equals("")){
		dups = rl.getRelatedList();
	    }
	    if(dups != null && dups.size() > 0){
		out.println("<tr><td>");
		out.println("<table><caption>Current Related Records</caption>");
		out.println("<tr><th></th><th>Related ID</th><th>Type</th></tr>");
		for(Related rel: dups){
		    String str = rel.getId2();
		    String str2 = rel.getType2();
		    String str3 = rel.createLinkForType2(url);
		    out.println("<tr><td><input type=\"checkbox\" name=\"delRelated\" value=\""+str+"\">*</td><td>"+str3+"</td><td>"+str2+"</td></tr>");
		}
		out.println("</table></td></tr>");
	    }
	}
	out.println("</table>");
	out.println("</fieldset>");
	//
	if(id.equals("")){
	    out.println("<fieldset><legend>Employee Info </legend>");			
	    out.println("<p>You will be able to enter employee info after"+
			" you fill this form and Save </p>");
	}
	else{
	    out.println("<fieldset><legend>Employee Info </legend>");

	    out.println("<table>");			
	    out.println("<tr><td><label>Employees:</label>"+
			"</td><td>");
	    out.println("<input name=\"action\" "+
			"type=\"button\" value=\"Add Employee\" "+
			"tabindex=\"33\" onClick=\"window.open('"+
			url+
			"GetEmpInfoServ?legalType=InternalAccident"+
			"&opener=SafetyServ"+
			"&relatedId="+id+
			"','Employee','toolbar=0,location=0,"+
			"directories=0,status=0,menubar=0,"+
			"scrollbars=0,top=200,left=200,"+
			"resizable=1,width=500,height=400');return false;\" />");
	    out.println("</td></tr>");
	    out.println("<tr id=empinfo><td>&nbsp;</td></tr>");
	    EmployeeList el = new EmployeeList(debug);
	    el.setRelatedId(id);
	    el.lookFor();
	    List<Employee> ls = el.getEmployees();
	    if(ls != null){

		for(Employee empo: ls){
		    Helper.printEmployee(out,empo,url,id,"SafetyServ");
		}

	    }
	    out.println("</table>");			
	    out.println("</fieldset>");	
	}
	out.println("<fieldset><legend>Damages </legend>");
	out.println("<table>");
	out.println("<tr><td>");
	out.println("<input type=\"checkbox\" name=\"empInjured\" "+
		    "id=\"empInjured\" value=\"y\" "+empInjured+" />");
	out.println("<label for=\"empInjured\">Employee Injured?</label>");
	out.println("</td></tr>");
	out.println("<tr><td>");
	out.println("<input type=\"checkbox\" name=\"workComp\" "+
		    "id=\"workComp\" value=\"y\" "+workComp+" />");
	out.println("<label for=\"workComp\">Worker's Comp</label>");
	out.println("</td></tr>");
	out.println("<tr><td>");	
	out.println("<input type=\"checkbox\" name=\"outOfDuty\" "+
		    "id=\"outofduty\" value=\"y\" "+outOfDuty+" />");
	out.println("<label for=\"outofduty\">Accident Off Duty Time</label>");
	out.println("</td></tr>");	
	out.println("<tr><td>");
	out.println("<input type=\"checkbox\" name=\"autoDamage\" "+
		    "onclick=\"doUpdate()\" "+
		    "id=\"autoDamage\" value=\"y\" "+autoDamage+" />");
	out.println("<label for=\"autoDamage\">City Vehicle Damaged?</label>");
	out.println("</td></tr>");
	out.println("<tr><td>");
	out.println("<input type=\"checkbox\" name=\"propDamage\" "+
		    "onclick=\"doUpdate()\" "+
		    "id=\"propDamage\" value=\"y\" "+propDamage+" /> ");
	out.println("<label for=\"propDamage\">City Property Damaged?</label>, ");
				
	out.println("<label for=\"whatProp\">What Property:</label>");
	out.println("<input name=\"whatProp\" size=\"40\" maxlength=\"50\" "+
		    "id=\"whatProp\" value=\""+whatProp+"\">");
	out.println("</td></tr>");
	out.println("<tr><td><label for=\"repairInfo\">Repair Info</label>"+
		    "<br />");
	out.println("<textarea name=\"repairInfo\" rows=\"5\" cols=\"70\" "+
		    " wrap>"+repairInfo);
	out.println("</textarea>");
	out.println("</td></tr>");
	out.println("</table>");
	if(!autoDamage.equals("")){
	    out.println("<fieldset><legend>Vehicle Repair</legend>"+
			"<table>");
	    out.println("<tr><td></td>"+
			"<td><label for=\"vin\">VIN #:</label></td>"+
			"<td><label for=\"autoPlate\">Plate #:</label></td>"+
			"<td><label for=\"autoNum\">Number:</label></td>"+
			"<td><label for=\"autoMake\">Make:</label></td>"+
			"<td><label for=\"autoModel\">Model:</label></td>"+
			"<td><label for=\"autoYear\">Year:</label></t><tr>");
	    out.println("<tr><td>Add Vehicle </td>");
	    out.println("<td><input name=\"vin\" id=\"vin\" value=\""+
			"\" size=\"15\" maxlength=\"20\"/></td>");
	    out.println("<td>"+
			"<input name=\"autoPlate\" id=\"autoPlate\" value=\""+
			"\" size=\"15\" maxlength=\"20\"/></td>");
	    out.println("<td>"+
			"<input name=\"autoNum\" id=\"autoNum\" value=\""+
			"\" size=\"15\" maxlength=\"20\"/></td>");
	    out.println("<td>"+
			"<input name=\"autoMake\" id=\"autoMake\" value=\""+
			"\" size=\"15\" maxlength=\"20\"/></td>");
	    out.println("<td>"+
			"<input name=\"autoModel\" id=\"autoModel\" value=\""+
			"\" size=\"15\" maxlength=\"20\"/></td>");
	    out.println("<td>"+
			"<input name=\"autoYear\" id=\"autoYear\" value=\""+
			"\" size=\"4\" maxlength=\"4\"/></td></tr>");
	    //
	    // Show existing Autos here
	    //
	    if(!id.equals("")){
		String str="", all="";
		AutoList sa = new AutoList(debug, id);
		message = sa.lookFor();
		if(message.equals("")){
		    List<Auto> list = sa.getAutos();
		    for(Auto auto: list){
			str = auto.getId();
			all += "<tr><td><input type=\"checkbox\" "+
			    "name=\"delAutoAid\" value=\""+str+"\">**</td>";
			str = auto.getVin();
			all += "<td>"+str+"</td>";
			str = auto.getAutoPlate();
			all += "<td>"+str+"</td>";
			str = auto.getAutoNum();
			all += "<td>"+str+"</td>";
			str = auto.getAutoMake();
			all += "<td>"+str+"</td>";
			str = auto.getAutoModel();
			all += "<td>"+str+"</td>";
			str = auto.getAutoYear();
			all += "<td>"+str+"</td></tr>";
		    }
		    if(!all.equals("")){
			all += "<tr><td colspan=\"6\"> "+
			    " ** Check here to delete this vehicle after"+
			    " clicking on \"Update\" button.</td></tr>";
			out.println(all);
		    }
		}
	    }
	    out.println("</table></td></tr>");
	    //
	    out.println("<tr><td>");
	    out.println("<table>");
	    out.println("<tr><th>Selected</th><th>Company</th><th>Cost "+
			"Estimate $</th></tr>");
	    out.println("<tr><td>1-");
	    out.println("<input type=\"checkbox\" name=\"est_1\" "+
			"value=\"y\" "+est_1+">");
	    out.println("</td><td>");
	    out.println("<input name=\"estPlace\" id=\"estPlace\" value=\""+
			estPlace+"\" size=\"30\" maxlength=\"50\" /></td><td>");
	    out.println("<input name=\"estCost\" id=\"estCost\" value=\""+
			estCost+"\" size=\"8\" maxlength=\"8\" />");
	    out.println("</td></tr>");
	    out.println("<tr><td>2-");
	    out.println("<input type=\"checkbox\" name=\"est_2\" "+
			"value=\"y\" "+est_2+">");
	    out.println("</td><td>");
	    out.println("<input name=\"estPlace2\" id=\"estPlace2\" value=\""+
			estPlace2+"\" size=\"30\" maxlength=\"50\" /></td><td>");
	    out.println("<input name=\"estCost2\" id=\"estCost2\" value=\""+
			estCost2+"\" size=\"8\" maxlength=\"8\" />");
	    out.println("</td></tr>");
	    out.println("<tr><td>3-");
	    out.println("<input type=\"checkbox\" name=\"est_3\" "+
			"value=\"y\" "+est_3+">");
	    out.println("</td><td>");
	    out.println("<input name=\"estPlace3\" id=\"estPlace3\" value=\""+
			estPlace3+"\" size=\"30\" maxlength=\"50\" /></td><td>");
	    out.println("<input name=\"estCost3\" id=\"estCost3\" value=\""+
			estCost3+"\" size=\"8\" maxlength=\"8\" />");
	    out.println("</td></tr>");
	    out.println("<tr><td><label for=\"totalCost\">Total Cost:</label>");
	    out.println("</td><td colspan=\"3\">");
	    out.println("<input name=\"totalCost\" id=\"totalCost\" value=\""+
			totalCost+"\" size=\"8\" maxlength=\"10\" />, ");
	    out.println("<input name=\"autoPaid\" id=\"autoPaid\" value=\"y"+
			"\" type=\"checkbox\" "+autoPaid+" />");
	    out.println("<label for=\"autoPaid\">Paid?</label>");						
	    out.println("</td></tr>");
	    out.println("</table>");
	    out.println("</td></tr></table>");
	    out.println("</fieldset>");
	}
	if(!propDamage.equals("")){
	    out.println("<fieldset><legend>Property Repair</legend>"+
			"<table>");
	    out.println("<tr><th>Selected</th><th>Company</th><th>Cost "+
			"Estimate $</th></tr>");
	    out.println("<tr><td>1-");
	    out.println("<input type=\"checkbox\" name=\"estp_1\" "+
			"value=\"y\" "+estp_1+">");
	    out.println("</td><td>");
	    out.println("<input name=\"estPlaceP\" id=\"estPlaceP\" value=\""+
			estPlaceP+"\" size=\"30\" maxlength=\"50\" /></td><td>");
	    out.println("<input name=\"estCostP\" id=\"estCostP\" value=\""+
			estCostP+"\" size=\"8\" maxlength=\"8\" />");
	    out.println("</td></tr>");
	    out.println("<tr><td>2-");
	    out.println("<input type=\"checkbox\" name=\"estp_2\" "+
			"value=\"y\" "+estp_2+">");
	    out.println("</td><td>");
	    out.println("<input name=\"estPlaceP2\" id=\"estPlaceP2\" value=\""+
			estPlaceP2+"\" size=\"30\" maxlength=\"50\" /></td><td>");
	    out.println("<input name=\"estCostP2\" id=\"estCostP2\" value=\""+
			estCostP2+"\" size=\"8\" maxlength=\"8\" />");
	    out.println("</td></tr>");
	    out.println("<tr><td>3-");
	    out.println("<input type=\"checkbox\" name=\"estp_3\" "+
			"value=\"y\" "+estp_3+">");
	    out.println("</td><td>");
	    out.println("<input name=\"estPlaceP3\" id=\"estPlaceP3\" value=\""+
			estPlaceP3+"\" size=\"30\" maxlength=\"50\" /></td><td>");
	    out.println("<input name=\"estCostP3\" id=\"estCostP3\" value=\""+
			estCostP3+"\" size=\"8\" maxlength=\"8\" />");
	    out.println("</td></tr>");
	    out.println("<tr><td><label for=\"totalCost\">Total Cost:</label>");
	    out.println("</td><td colspan=\"3\">");
	    out.println("<input name=\"totalCostP\" id=\"totalCostP\" value=\""+
			totalCostP+"\" size=\"8\" maxlength=\"10\" />");
	    out.println("<label for=\"propPaid\">Paid?</label>");
	    out.println("<input name=\"propPaid\" id=\"propPaid\" value=\"y"+
			"\" type=\"checkbox\" "+propPaid+" />Yes.");
	    out.println("</td></tr></table></td></tr>");
	    out.println("</td></tr></table>");
	    out.println("</fieldset>");
	}
	//
	// Insurance info
	if(!subToInsur.equals("")){
	    // Insurance
	    out.println("<fieldset><legend>City Insurance ");
	    out.println("<input name=\"action\" "+
			"type=\"button\" value=\"Add Insurance \" "+
			"tabindex=\"33\" onClick=\"window.open('"+
			url+
			"InsuranceServ?legalType=InternalAccident"+
			"&type=City"+
			"&opener=SafetyServ"+
			"&relatedId="+id+
			"','Insurance','toolbar=0,location=0,"+
			"directories=0,status=0,menubar=0,"+
			"scrollbars=0,top=200,left=200,"+
			"resizable=1,width=600,height=500');return false;\" />");
	    out.println("</legend>");
	    //
	    if(!id.equals("")){
		InsuranceList il = new InsuranceList(debug);
		il.setRelatedId(id);
		il.lookFor();
		List<Insurance> ls = il.getInsurances();
		if(ls != null){

		    out.println("<table>");
		    for(Insurance insr: ls){
			Helper.printInsurance(out,insr,url,id,"Safety","SafetyServ");
		    }
		    out.println("</table>");
		}
	    }
	    out.println("</fieldset>");	
	}
	out.println("<fieldset><legend>Charges </legend>");
	out.println("<table>");
	out.println("<tr><td><label for=\"paidByCity\">Amount Paid By City: $</label>");
	out.println("<input name=\"paidByCity\" size=\"10\" maxlength=\"10\" "+
		    "id=\"paidByCity\" value=\""+paidByCity+"\" />");
	out.println("<label for=\"paidByRisk\"> Paid By Risk: $</label>");
	out.println("<input name=\"paidByRisk\" size=\"10\" maxlength=\"10\" "+
		    "id=\"paidByRisk\" value=\""+paidByRisk+"\" />");
	out.println("<label for=\"miscByCity\"> Misc Paid By City: $</label>");
	out.println("<input name=\"miscByCity\" size=\"10\" maxlength=\"10\" "+
		    "id=\"miscByCity\" value=\""+miscByCity+"\" />");
	out.println("</td></tr>");
	out.println("<tr><td>");
	out.println("<label for=\"paidByInsur\">Amount Paid By Insurance:"+
		    " $</label><input name=\"paidByInsur\" id=\"paidByInsur\" "+
		    "value=\""+paidByInsur+"\" size=\"10\" maxlength=\"10\" /> ");
	out.println("</td></tr>");		
	if(!balance.equals("")){
	    out.println("<tr><td>");
	    out.println("<label>Balance:</label> ");			
	    out.println("$"+balance);
	    out.println("</td></tr>");					
	}
	out.println("</table>");
	out.println("</fieldset>");
	//
	//
	out.println("<fieldset>");
	if(id.equals("")){
	    out.println("<table class=\"control\"><tr><td>");
	    out.println("<input type=\"submit\" tabindex=\"31\" "+
			"accesskey=\"s\" id=\"action\" value=\"Save\" "+
			"name=\"action\" class=\"submit\">");
	    out.println("</td></tr></table>"); 
	    out.println("</form>");
	}
	else{ // save, update
	    //
	    out.println("<table class=\"control\">");
	    out.println("<td valign=top><input accesskey=\"u\" "+
			"tabindex=\"31\" "+
			"type=\"submit\" name=\"action\" id=\"action\" "+
			"class=\"submit\" value=\"Update\" />");
	    out.println("</td>");
	    out.println("<td>");
	    out.println("<input name=\"action\" accesskey=\"y\" "+
			"type=\"button\" value=\"Payment\" "+
			"tabindex=\"32\" onClick=\"window.open('"+
			url+
			"PaymentServ?type=safety"+
			"&risk_id="+id+"');return false;\" />");
	    out.println("</td>");
	    out.println("<td>");
	    out.println("<input type=\"button\" name=\"action\" "+
			"accesskey=\"n\" value=\"Generate Another Internal Accident\""+
			" tabindex=\"33\" onClick=\"document.location='"+
			url+"SafetyServ?safeId="+id+"&action=Generate"+
			"'\" /></td>");

	    out.println("<td valign=top><input "+
			"type=button name=\"action\" id=\"action\" "+
			"onclick=\"window.location='"+url+"RiskFileServ?risk_id="+id+"';\" "+
			"class=\"submit\" value=\"Upload File\" />");
	    out.println("</td>");	
	    out.println("<td>");
	    out.println("<input type=\"button\" value=\"Add Notes\" "+
			"onclick=\"window.open('" + url + 
			"NoteServ?risk_id="+id+"&opener=SafetyServ','Notes',"+
			"'location=0:0,menubar=1,width=500,height=400,toolbar=1,scrollbars=1');return false;\"></input>");
			
	    out.println("</form></td><td>");
	    //
	    out.println("<form id=\"myForm2\" onsubmit=\"return "+
			"validateDelete();\">");
	    out.println("<input type=hidden name=\"id\" value=\""+
			id+"\">");
	    out.println("<input type=\"submit\" name=\"action\" "+
			"id=\"action\" accesskey=\"e\" tabindex=\"34\" "+
			" value=\"Delete\" />");
	    out.println("</form>");
	    out.println("</td></tr></table>");
	}
	out.println("</fieldset>");
	if(!id.equals("")){
	    if(sf.hasNotes()){
		Helper.printNotes(out,
				  url,
				  "SafetyServ",
				  sf.getNotes());									
	    }
	    if(sf.hasFiles()){
		Helper.printFiles(out,
				  url,
				  sf.getFiles());									
	    }							
	    out.println("<center>* Click in check box to delete the item from this record </center>");
	}
	out.println("</div>");
	out.print("</body></html>");
	out.flush();
	out.close();
    }

}























































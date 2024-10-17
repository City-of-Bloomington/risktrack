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
@WebServlet(urlPatterns = {"/LegalServ","/Legal"})
public class LegalServ extends TopServlet{

    RiskTypeList types = null;
    String redColor = "#FF0000";
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
    
	String type="", id="", status="", caseNum="",
	    location="", damageAmnt="", otherType="",
	    //
	    // employee info
	    empid="", empName="", empPhone="", dept_id="", 
	    deptName="", empTitle="", balance="",
	    //
	    // auto related
	    vin="",autoMake="", autoModel="", autoYear="", 
	    cityAutoInc="", tortId="", relatedId="",
	    autoPlate="", autoNum="",
	    //
	    // city insurance
	    adjuster="", adjusterPhone="", adjusterEmail="",
	    attorney="", attorneyPhone="", attorneyEmail="",
	    insured="", insurance="",
	    claimNum="", insurStatus="", recordOnly="",
	    //
	    // dates
	    doi="", filed="",judgment="",proSupp="",closed="";
	//
	// Defendant Insurance
	/*
	  defInsur="", defInsurPhone="", defClaimNum="",
	  defAdjuster="", defAdjusterPhone="",
	  defAttorney="", defAttorneyPhone="";
	*/
	String  description="",
	    allDocuments="", deptRecoverDate="", deptCollectDate="",
	    deptToRisk="",  // ?
	    deptToRiskDate="",
	    riskFirstDate="",
	    toProsecutorDate="",
	    convictionDate="",
	    collectDate="",
	    riskToInsur="", // ?
	    riskToInsurDate="",
	    insurRecoveryDate="",
	    insurCollectDate="", unableToCollect="",
	    policy="", deductible="", otherDetails="",
	    outOfDuty="";

	boolean success = true;
       	String username = "", message = "",  pid="", // person id
	    action = "",entry_time="",
	    entry_date = "", prevAction="";
	String paidByCity="",paidByInsur="",miscByCity="",paidByRisk="", paidByDef="";

	res.reset();
	res.setContentType("text/html");
	PrintWriter out = res.getWriter();
	String name, value;
	String[] defList = null;
	Enumeration values = req.getParameterNames();
	String [] vals;
	String[] delAutoAid = null;
	String[] delRelated = null;
	String[] delRelatedDept = null;				
	Legal lgl = new Legal(debug);
		
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
		lgl.setId(value);
	    }
	    else if (name.equals("pid")) {
		pid =value;
	    }
	    else if (name.equals("tortId")) {
		tortId =value;
	    }
	    else if (name.equals("relatedId")) {
		relatedId =value;
	    }
	    else if (name.equals("delRelatedDept")) {
		delRelatedDept = vals;
	    }						
	    else if (name.equals("caseNum")) {
		caseNum =value;
	    }
	    else if (name.equals("status")) {
		status =value;
	    }
	    else if (name.equals("claimNum")) {
		claimNum =value;
	    }
	    else if(name.equals("delAutoAid")){
		delAutoAid = vals; // this is array
	    }
	    else if(name.equals("delRelated")){
		delRelated = vals; // this is array
	    }
	    else if (name.equals("location")) {
		location = value;
	    }
	    else if (name.equals("damageAmnt")) {
		damageAmnt = value;
	    }
	    else if (name.equals("empName")) {
		empName = value.toUpperCase();
	    }
	    else if (name.equals("empTitle")) {
		empTitle = value;
	    }
	    else if (name.equals("empPhone")) {
		empPhone = value;
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
	    else if (name.equals("paidByDef")) {
		paidByDef = value;
	    }
	    else if (name.equals("deptName")) {
		deptName = value;
	    }
	    else if (name.equals("empid")) {
		empid = value;
	    }
	    else if (name.equals("vin")) {
		vin = value;
	    }
	    else if (name.equals("outOfDuty")) {
		outOfDuty = value;
	    }	    
	    else if (name.equals("autoMake")) {
		autoMake = value.toUpperCase();
	    }
	    else if (name.equals("autoModel")) {
		autoModel = value.toUpperCase();
	    }
	    else if (name.equals("autoPlate")) {
		autoPlate = value;
	    }
	    else if (name.equals("autoNum")) {
		autoNum = value;
	    }
	    else if (name.equals("autoYear")) {
		autoYear = value;
	    }
	    else if (name.equals("adjuster")) {
		adjuster =value;
	    }
	    else if (name.equals("adjusterPhone")) {
		adjusterPhone =value;  
	    }
	    else if (name.equals("adjusterEmail")) {
		adjusterEmail =value;
	    }
	    else if (name.equals("attorney")) {
		attorney =value;
	    }
	    else if (name.equals("attorneyPhone")) {
		attorneyPhone =value;
	    }
	    else if (name.equals("attorneyEmail")) {
		attorneyEmail =value;
	    }
	    else if (name.equals("deptToRisk")) {
		deptToRisk =value;
	    }
	    else if (name.equals("riskToInsur")) {
		riskToInsur =value;
	    }
	    else if (name.equals("otherType")) {
		otherType =value;
	    }
	    else if (name.equals("defMark")){ // array
		defList =vals;
	    }
	    else if (name.equals("cityAutoInc")) {
		cityAutoInc = value;
	    }
	    else if (name.equals("insured")) {
		insured =value;
	    }
	    else if (name.equals("insurance")) {
		insurance =value;
	    }
	    else if (name.equals("insurStatus")) {
		insurStatus = value;
	    }
	    else if (name.equals("doi")) {
		doi =value;
	    }
	    else if (name.equals("filed")) {
		filed =value;
	    }
	    else if (name.equals("proSupp")) {
		proSupp =value;
	    }
	    else if (name.equals("judgment")) {
		judgment =value;
	    }
	    else if (name.equals("closed")){
		closed =value;
	    }
	    else if (name.equals("description")){
		description =value;
	    }
	    else if (name.equals("allDocuments")){
		allDocuments =value;
	    }
	    else if (name.equals("deptRecoverDate")){
		deptRecoverDate =value;
	    }
	    else if (name.equals("deptCollectDate")){
		deptCollectDate =value;
	    }
	    else if (name.equals("deptToRisk")){
		deptToRisk =value;
	    }
	    else if (name.equals("deptToRiskDate")){
		deptToRiskDate =value;
	    }
	    else if (name.equals("riskFirstDate")){
		riskFirstDate =value;
	    }
	    else if (name.equals("toProsecutorDate")){
		toProsecutorDate =value;
	    }
	    else if (name.equals("convictionDate")){
		convictionDate =value;
	    }
	    else if (name.equals("collectDate")){
		collectDate =value;
	    }
	    else if (name.equals("riskToInsur")){
		riskToInsur =value;
	    }
	    else if (name.equals("unableToCollect")){
		unableToCollect =value;
	    }			
	    else if (name.equals("riskToInsurDate")){
		riskToInsurDate =value;
	    }
	    else if (name.equals("insurRecoveryDate")){
		insurRecoveryDate =value;
	    }
	    else if (name.equals("insurCollectDate")){
		insurCollectDate =value;
	    }
	    else if (name.equals("policy")){
		policy =value;
	    }
	    else if (name.equals("recordOnly")) {
		recordOnly = value;
	    }
	    else if (name.equals("deductible")){
		deductible =value;
	    }
	    else if (name.equals("otherDetails")){
		otherDetails =value;
	    }
	    else if (name.equals("prevAction")){ 
		// Get, Save, zoom, edit, delete, New, Refresh
		prevAction = value;  
	    }
	    else if (name.equals("action2")){
		if(!value.equals(""))
		    action = value;  
	    }								
	    else if (name.equals("action")){ 
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
		message += msg;
		success = false;
		types = null;
	    }
	}
	if(action.equals("Generate")){
	    //
	    // We are coming from TortClaimServ
	    //
	    // 
	    //  first time 
	    //  let us get tortClaim info data that is useful to us
	    //  and change the action to '' for a new record
	    //
	    TortClaim tc = new TortClaim(debug);
	    tc.setId(tortId);
	    relatedId = tortId;
	    String back = tc.doSelect();
	    if(back.equals("")){
		type = tc.getType();
		deductible = tc.getDeductible();
		paidByCity = tc.getPaidByCity();
		paidByRisk = tc.getPaidByRisk();
		paidByInsur = tc.getPaidByInsur();
		miscByCity = tc.getMiscByCity();
		doi = tc.getIncidentDate();
		description = tc.getIncident();
		otherDetails = tc.getComments();
		cityAutoInc = tc.getCityAutoInc();
		riskToInsur = tc.getSubInsur();
		otherType = tc.getOtherType();
		recordOnly = tc.getRecordOnly();
		if(!(dept_id.equals("") || dept_id.equals("0"))){
		    Department dp = new Department(debug, dept_id);
		    back = dp.doSelect();
		    if(back.equals("")){
			deptName = dp.getName();
		    }
		    dp = null;
		}
	    }
	    action = "Save"; // change action to save
	}
	//
	if(action.equals("Save")){
	    //	   
	    // get the dept id first
	    //
	    if(!deptName.equals("")){
		Department dp = new Department(debug);
		dp.setName(deptName);
		String str = dp.findOrAddDept();
		if(!str.equals("")) dept_id = str;
		dp = null;
	    }
	    lgl.setCaseNum(caseNum);
	    lgl.setType(type);
	    lgl.setStatus(status);
	    lgl.setDamageAmnt(damageAmnt);
	    lgl.setLocation(location);
	    lgl.setDescription(description);
	    lgl.setInsured(insured);

	    lgl.setDoi(doi);
	    lgl.setProSupp(proSupp);
	    lgl.setJudgment(judgment);
	    lgl.setFiled(filed);
	    lgl.setClosed(closed);
	    lgl.setCityAutoInc(cityAutoInc);

	    lgl.setDescription(description);
	    lgl.setAllDocuments(allDocuments);
	    lgl.setDeptRecoverDate(deptRecoverDate);
	    lgl.setDeptCollectDate(deptCollectDate);
	    lgl.setDeptToRisk(deptToRisk);
	    lgl.setDeptToRiskDate(deptToRiskDate);
	    lgl.setRiskFirstDate(riskFirstDate);
	    lgl.setToProsecutorDate(toProsecutorDate);
	    lgl.setConvictionDate(convictionDate);
	    lgl.setCollectDate(collectDate);
	    lgl.setRiskToInsur(riskToInsur);  // 50
	    lgl.setRiskToInsurDate(riskToInsurDate);
	    lgl.setInsurRecoveryDate(insurRecoveryDate);
	    lgl.setInsurCollectDate(insurCollectDate);

	    lgl.setDeductible(deductible);
	    lgl.setOtherDetails(otherDetails);  // 56
	    lgl.setPaidByCity(paidByCity);
	    lgl.setPaidByRisk(paidByRisk);
	    lgl.setPaidByRisk(paidByRisk);
	    lgl.setPaidByDef(paidByDef);
	    lgl.setPaidByInsur(paidByInsur);
	    lgl.setMiscByCity(miscByCity);
	    lgl.setRecordOnly(recordOnly);
	    lgl.setUnableToCollect(unableToCollect);
	    lgl.setOutOfDuty(outOfDuty);
	    String back = lgl.doSave();
	    if(back.equals("")){
		id = lgl.getId();
		message += "Data saved successfully";
		//
		// if we have the defendant pid we attach it to this 
		// versus
		if(!pid.equals("")){
		    lgl.doDefendantInsert(pid);
		    if(!back.equals("")){
			message = back;
		    }
		}
	    }
	    else{
		message += back;
		out.println(back);
		success = false;
	    }
	    if(!vin.equals("") || !autoPlate.equals("") || 
	       !autoMake.equals("")){
		Auto auto = new Auto(debug);
		auto.setRisk_id(id);
		auto.setVin(vin);
		auto.setAutoMake(autoMake);
		auto.setAutoModel(autoModel);
		auto.setAutoYear(autoYear);
		auto.setAutoPlate(autoPlate);
		auto.setAutoNum(autoNum);
		back = auto.doSave();
		if(!back.equals("")){
		    message += back;
		    out.println(back);
		    success = false;
		}
	    }
	    if(!relatedId.equals("")){
		Related rel = new Related(id, relatedId,"Recovery Action", debug);
		back = rel.doSave();
		if(!back.equals("")){
		    message += back;
		    System.err.println(back);
		    success = false;
		}
		// 
		// check if there are claimants associated
		// get the list of their ID's and insert them as
		// defendants 
		//
		List<RiskPerson> claiments = null;
		TortClaim tort = new TortClaim(debug, relatedId);
		if(tort.hasClaiments()){
		    claiments = tort.getClaiments();
		}
		if(claiments != null && claiments.size() > 0){
		    back = lgl.doDefendantInsert(claiments);
		    if(!back.equals("")){
			message += back;
		    }
		}
		//
		// make a link to the insurance id's
		InsuranceList il = new InsuranceList(debug);
		il.setRelatedId(tortId);
		il.setType("All");
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
	    lgl.setCaseNum(caseNum);
	    lgl.setType(type);
	    lgl.setOtherType(otherType);
	    lgl.setStatus(status);
	    lgl.setDamageAmnt(damageAmnt);
	    lgl.setLocation(location);
	    lgl.setDescription(description);

	    lgl.setInsured(insured);

	    lgl.setDoi(doi);
	    lgl.setProSupp(proSupp);
	    lgl.setJudgment(judgment);
	    lgl.setFiled(filed);
	    lgl.setClosed(closed);

	    lgl.setCityAutoInc(cityAutoInc);

	    lgl.setDescription(description);
	    lgl.setAllDocuments(allDocuments);
	    lgl.setDeptRecoverDate(deptRecoverDate);
	    lgl.setDeptCollectDate(deptCollectDate);
	    lgl.setDeptToRisk(deptToRisk);
	    lgl.setDeptToRiskDate(deptToRiskDate);
	    lgl.setRiskFirstDate(riskFirstDate);
	    lgl.setToProsecutorDate(toProsecutorDate);
	    lgl.setConvictionDate(convictionDate);
	    lgl.setCollectDate(collectDate);
	    lgl.setRiskToInsur(riskToInsur);  // 50
	    lgl.setRiskToInsurDate(riskToInsurDate);
	    lgl.setInsurRecoveryDate(insurRecoveryDate);
	    lgl.setInsurCollectDate(insurCollectDate);

	    lgl.setDeductible(deductible);
	    lgl.setOtherDetails(otherDetails);  // 56
	    lgl.setTortId(tortId); // will take care of adding to link table
	    lgl.setPaidByCity(paidByCity);
	    lgl.setPaidByRisk(paidByRisk);
	    lgl.setPaidByInsur(paidByInsur);
	    lgl.setMiscByCity(miscByCity);
	    lgl.setPaidByDef(paidByDef);
	    lgl.setRecordOnly(recordOnly);
	    lgl.setUnableToCollect(unableToCollect);
	    lgl.setOutOfDuty(outOfDuty);
	    String back = lgl.doUpdate();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }
	    else{
		message += "Data updated successfully";
	    }
	    if(!vin.equals("") || !autoPlate.equals("") || 
	       !autoMake.equals("")){
		Auto auto = new Auto(debug);
		auto.setRisk_id(id);
		auto.setVin(vin);
		auto.setAutoMake(autoMake);
		auto.setAutoModel(autoModel);
		auto.setAutoYear(autoYear);
		auto.setAutoPlate(autoPlate);
		auto.setAutoNum(autoNum);
		back = auto.doSave();
		if(!back.equals("")){
		    message += back;
		    out.println(back);
		    success = false;
		}
	    }
	    //
	    // check if we need to delete some of the defendents
	    // from this case when the user marks the checkboxes
	    //
	    if(defList != null){
		HandlePerson hp = new HandlePerson(debug);
		hp.setPidArr(defList);
		message += hp.doDefendantDelete(id);
	    }
	    //
	    // check if we need to delete any of the vehicles
	    //
	    if(delAutoAid != null){
		for(int i=0;i<delAutoAid.length;i++){
		    String aid = delAutoAid[i];
		    Auto auto = new Auto(debug);
		    auto.setId(aid);
		    auto.doDelete();
		}
	    }
	    if(!relatedId.equals("")){
		Related rel = new Related(id, relatedId,"Recovery Action", debug);
		back = rel.doSave();
		if(!back.equals("")){
		    message += back;
		    System.err.println(back);
		    success = false;
		}
	    }
	    if(delRelated != null){
		for(int i=0;i<delRelated.length;i++){
		    String str = delRelated[i];
		    Related rel = new Related(id, str, debug);
		    rel.doDelete();
		}
	    }
	    if(delRelatedDept != null){
		for(int i=0;i<delRelatedDept.length;i++){
		    String str = delRelatedDept[i];
		    RelatedDept rel = new RelatedDept(debug, str);
		    rel.doDelete();
		}
	    }						
						
	}
	else if(action.equals("Delete")){
	    //
	    // System.err.println("delete record");
	    //
	    String back = "";
	    HandlePerson hp = new HandlePerson(debug);
	    back = hp.doDefendantDelete(id);
	    if(back.equals("")) message += " Deleted Successfully";
	    else message += back;
	    //
	    // delete any auto related
	    //
	    Auto auto =new Auto(debug);
	    auto.doDeleteAllFor(id);
	    Related rel = new Related(id, debug);
	    rel.doDelete();
	    back = lgl.doDelete();
	    if(!back.equals("")){
		message += back;
		out.println(back);
		success = false;
	    }

	}
	else if(!id.equals("")){	
	    //
	    String back = lgl.doSelect();
	    if(back.equals("")){

		caseNum = lgl.getCaseNum();
		type = lgl.getType();
		otherType = lgl.getOtherType();
		status = lgl.getStatus();
		insured = lgl.getInsured();

		doi = lgl.getDoi();
		closed = lgl.getClosed();
		judgment = lgl.getJudgment();
		proSupp = lgl.getProSupp();
		filed = lgl.getFiled();

		damageAmnt = lgl.getDamageAmnt();
		location = lgl.getLocation();

		cityAutoInc = lgl.getCityAutoInc();
		deductible = lgl.getDeductible();
		otherDetails = lgl.getOtherDetails();

		description = lgl.getDescription();
		allDocuments = lgl.getAllDocuments();
		deptRecoverDate = lgl.getDeptRecoverDate();
		deptCollectDate = lgl.getDeptCollectDate();
		deptToRisk = lgl.getDeptToRisk();
		deptToRiskDate = lgl.getDeptToRiskDate();
		riskFirstDate = lgl.getRiskFirstDate();
		toProsecutorDate = lgl.getToProsecutorDate();
		convictionDate = lgl.getConvictionDate();
		collectDate = lgl.getCollectDate();
		riskToInsur = lgl.getRiskToInsur();
		riskToInsurDate = lgl.getRiskToInsurDate();
		insurRecoveryDate = lgl.getInsurRecoveryDate();
		insurCollectDate = lgl.getInsurCollectDate();
		paidByCity = lgl.getPaidByCity();
		paidByRisk = lgl.getPaidByRisk();
		paidByInsur = lgl.getPaidByInsur();
		miscByCity = lgl.getMiscByCity();
		recordOnly = lgl.getRecordOnly();
		paidByDef = lgl.getPaidByDef();
		unableToCollect = lgl.getUnableToCollect();
		outOfDuty = lgl.getOutOfDuty();
		if(!(dept_id.equals("") || dept_id.equals("0"))){
		    Department dp = new Department(debug, dept_id);
		    back = dp.doSelect();
		    if(back.equals("")){
			deptName = dp.getName();
		    }
		    dp = null;
		}
	    }
	    else{
		message += back;
		System.err.println(back);
		success = false;
		action = "";
	    }
	}
	if(!id.equals("")){
	    Payment pay = new Payment(debug);
	    pay.setRisk_id(id);
	    pay.setType("legal");
	    balance = pay.getTotalBalance();
			
	}
	if(!cityAutoInc.equals("")) cityAutoInc = "checked=\"checked\"";
	if(!insured.equals("")) insured = "checked=\"checked\"";
	if(!prevAction.equals("") && action.equals("")) 
	    action = prevAction;
	if(!deptToRisk.equals("")) deptToRisk = "checked=\"checked\"";
	if(!riskToInsur.equals("")) riskToInsur = "checked=\"checked\"";
	if(!recordOnly.equals("")) recordOnly = "checked=\"checked\"";
	if(!unableToCollect.equals("")) unableToCollect="checked=\"checked\"";
	if(!outOfDuty.equals("")) outOfDuty = "checked=\"checked\"";
	//
	// Inserts
	out.println(Inserts.xhtmlHeaderInc);
	out.println(Inserts.banner(url));
	out.println(Inserts.menuBar(url, true));
	out.println(Inserts.sideBar(url));
	out.println("<div id=\"mainContent\">");
	out.println("<h3 class=\"titleBar\">Recovery Actions "+id+"</h3>"); // legal 
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
	out.println("  function setDeductible(optId){	      ");
	out.println("  if(optId == 1){                        ");
	out.println("    document.forms[0].deductible.value=\"5000\"; ");
	out.println("  }else if(optId == 2){                           "); 
	out.println("    document.forms[0].deductible.value=\"1000\";  ");
	out.println("  }else {                                         ");  
	out.println("    document.forms[0].deductible.value=\"10000\"; ");
	out.println("   }} ");
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
	out.println("  function checkInsurStatus(optId){	       ");
	out.println("  if(optId == 3){  // closed                      ");
	out.println("   if(document.forms[0].insurStatus){         ");
	out.println("   var jj=document.forms[0].insurStatus.selectedIndex; ");
	out.println("   if(jj == 1){   // pending        "); 
	out.println("    alert(\"Claim can not be closed as long insurance status is pending\"); ");
	out.println("    document.forms[0].status.selectedIndex=0; ");
	out.println("   }}}}                                       ");
	out.println("</script>                                ");
	out.println("<form id=\"myForm\" method=\"post\" >");
	if(!id.equals("")){
	    out.println("<input type=hidden name=\"id\" value=\""+id+"\">");
	}
	out.println("<input type=\"hidden\" name=\"action2\" value=\"\" id=\"action_id\" />");				
	if(!pid.equals("")){
	    out.println("<input type=\"hidden\" name=\"pid\" value=\""+pid+"\" />");
	}				
	out.println("<fieldset><legend>Status</legend>");
	out.println("<table>");
	out.println("<tr><td>");
	out.println("<label for=\"status\">Status: </label>"+
		    "<select name=\"status\" id=\"status\" "+
		    "onchange=\"checkInsurStatus(this.selectedIndex)\" >");
	for(int i=0;i<Inserts.legalStatusArr.length;i++){
	    if(status.equals(Inserts.legalStatusArr[i]))
		out.println("<option selected=\"selected\">"+status+
			    "</option>");
	    else
		out.println("<option>"+Inserts.legalStatusArr[i]+"</option>");
	}
	out.println("</select>");
	out.println("<label for=\"caseNum\">Case #:</label>"+
		    "<input name=\"caseNum\" id=\"caseNum\" value="+
		    "\""+caseNum+"\" size=\"30\" maxlength=\"50\" /> ");

	if(!action.equals("")){
	    out.println("<input type=\"hidden\" name=\"prevAction\" "+
			"value=\""+action+"\" />");
	}
	out.println("</td></tr>");
	out.println("<tr><td><label for=\"type\">Type: </label>"+
		    "<select name=\"type\" id=\"type\">");
	if(types != null){
	    for(RiskType rt:types){
		if(type.equals(rt.getId()))
		    out.println("<option value=\""+type+
				"\" selected=\"selected\">"+rt.getName()+
				"</option>");
		else
		    out.println("<option value=\""+rt.getId()+
				"\">"+rt.getName()+"</option>");
	    }
	}
	out.println("</select><label>, Other </label>(specify):");
	out.println("<input name=\"otherType\" id=\"otherType\" size=\"20\" "+
		    "value=\""+otherType+"\" "+
		    "maxlength=\"30\" /></td></tr>");
	out.println("<tr><td>");
	out.println("<input name=\"deptToRisk\" id=\"deptToRisk\" "+
		    "onclick=\"doUpdate()\" "+
		    "value=\"y\"  type=\"checkbox\" "+deptToRisk+"/> ");
	out.println("<label for=\"deptToRisk\">Matter Forwarded to Risk to "+
		    "Attempt Recovery</label>");
	out.println("</td></tr>");
	out.println("<tr><td>");
	out.println("<input name=\"riskToInsur\" id=\"riskToInsur\" "+
		    "value=\"y\"  onclick=\"doUpdate()\" "+
		    "type=\"checkbox\" "+riskToInsur+"/> ");
	out.println("<label for=\"riskToInsur\">Matter Forwarded to Insurance"+
		    " to Attempt Recovery</label>");
	out.println("</td></tr>");
	out.println("<tr><td>");
	out.println("<input name=\"unableToCollect\" id=\"unableToCollect\" "+
		    "value=\"y\" "+
		    "type=\"checkbox\" "+unableToCollect+"/> ");
	out.println("<label for=\"unableToCollect\">Unable to Collect"+
		    " </label>");
	out.println("</td></tr>");
	out.println("<tr><td>");
	out.println("<input name=\"outOfDuty\" id=\"outofduty\" "+
		    "value=\"y\" "+
		    "type=\"checkbox\" "+outOfDuty+" /><label>Accident out off duty time</label>");
	out.println("</td></tr>");	
	out.println("<tr><td>");
	out.println("<input name=\"recordOnly\" id=\"recordOnly\" "+
		    "value=\"y\" "+
		    "type=\"checkbox\" "+recordOnly+" /><label> Record Only</label>");
	out.println("</td></tr>");
	
	out.println("<tr><td>");
	out.println("<label for=\"closed\">Date Closed: "+
		    " </label><input name=\"closed\" id=\"closed\" "+
		    "value=\""+closed+"\" size=\"10\" maxlength=\"10\" "+
		    "class=\"date\" "+
		    "/> ");
	out.println("</td></tr>");
	out.println("</table>");
	out.println("</fieldset>");
	out.println("<fieldset>");
	out.println("<legend>Related Records</legend>");
	out.println("<table>");
	out.println("<tr><td>");
	out.println("In the field below enter the record ID of related Tort Claim, Recovery Action, Internal Accident, Worker\"s Comp, or Natural Disaster");
	out.println("</td></tr>");		
	out.println("<tr><td>"); 
	out.println("<label for=\"relatedId\">Related Record ID:</label>"+
		    "<input name=\"relatedId\" id=\"relatedId\" "+
		    "size=\"8\" maxlength=\"8\" /> Enter one at a time. ");
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
		out.println("<tr><th>*</th><th>Related ID</th><th>Type</th></tr>");
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
	if(!id.equals("")){
	    out.println("<fieldset><legend>Related Department ");
	    out.println("<input name=\"action\" "+
			"type=\"button\" value=\"Add Related Department \" "+
			"tabindex=\"33\" onClick=\"window.open('"+url+
			"RelatedDeptServ?type=Legal&related_id="+id+
			"&opener=LegalServ"+
			"','Department','toolbar=0,location=0,"+
			"directories=0,status=0,menubar=0,"+
			"scrollbars=0,top=200,left=200,"+
			"resizable=1,width=500,height=400');return false;\" />");
	    out.println("</legend>");
	    List<RelatedDept> rdepts = lgl.getRelatedDepts();
	    if(rdepts != null && rdepts.size() > 0){
		out.println("<table>");			
		out.println("<tr><th>&nbsp;</th><th>Department</th><th>Supervisor</th><th>Phone</th><th>Edit</th></tr>");
		int jj=1;
		for(RelatedDept rdept:rdepts){
		    out.println("<tr><td>"+(jj++)+" - <input type=\"checkbox\" name=\"delRelatedDept\" value=\""+rdept.getId()+"\" /></td><td>"+rdept+"</td><td>"+rdept.getSupervisor()+"</td><td>"+rdept.getPhone()+"</td><td>");
		    out.println("<input name=\"action\" "+
				"type=\"button\" value=\"Edit Department\" "+
				"tabindex=\"33\" onClick=\"window.open('"+url+
				"RelatedDeptServ?id="+rdept.getId()+"&related_id="+id+"&action=zoom"+
				"&opener=LegalServ"+
				"','Department','toolbar=0,location=0,"+
				"directories=0,status=0,menubar=0,"+
				"scrollbars=0,top=200,left=200,"+
				"resizable=1,width=500,height=400');return false;\" />");
		    out.println("</td></tr>");
		}
		out.println("</table>");				
	    }
	    out.println("</fieldset>");	
	}		
	//
	// list of defendants (if any)
	//
	out.println("<fieldset><legend>Defendants Info </legend>");
	String all = "";
	if(!id.equals("")){
	    String all2="", str="", str2="", str3="", back="";
	    if(lgl.hasDefendants()){
		List<RiskPerson> defs = lgl.getDefendants();
		for(RiskPerson pp:defs){
		    if(back.equals("")){
			str = pp.getFullName();
			str2 = pp.getAddress();
			str3 = pp.getPhones();
			if(pp.isJuvenile()){
														
			    all += "<tr style=\"background-color:"+redColor+";color:white\">";
			    str += " (Juvenile)";
			}
			else {
			    all += "<tr>";
			}			
			all += "<td><input type=\"checkbox\" "+
			    "name=\"defMark\" "+
			    "id=\"defMark\" "+
			    "value=\""+pp.getId()+"\" />*</td>";
			all += "<td><a href=\""+url+
			    "RiskPersonServ?id="+pp.getId()+
			    "&action=zoom&inType=legal&risk_id="+id+"\">"+str+
			    "</a> ";
			all += "</td>";
			all += "<td>"+str2+"</td><td>"+str3+"</td></tr>";
		    }
		}
	    }
	}
	else if(!tortId.equals("")){
	    // 
	    // this should happen only on attaching to tort claims cases
	    //
	    String all2="", str="", str2="", str3="", back="";
	    TortClaim tort = new TortClaim(debug, tortId);
	    if(tort.hasClaiments()){
		List<RiskPerson> claiments = tort.getClaiments();
		for(RiskPerson pp:claiments){
		    str = pp.getFullName();
		    str2 = pp.getAddress();
		    str3 = pp.getPhones();
		    if(pp.isJuvenile()){
			all += "<tr style=\"background-color:"+redColor+";color:white\">";
			str += " (Juvenile)";
		    }
		    else {
			all += "<tr>";
		    }				
		    all += "<td>&nbsp;</td>";
		    all += "<td>"+str+"</td>";
		    all += "<td>"+str2+"</td>";
		    all += "<td>"+str3+"</td></tr>";	
		}
	    }
	}
	else if(!pid.equals("")){
	    String str="", str2="", str3="", back="";
	    RiskPerson pp = new RiskPerson(debug, pid);
	    back = pp.doSelect();
	    if(back.equals("")){
		str = pp.getFullName();
		str2 = pp.getAddress();
		str3 = pp.getPhones();
		if(pp.isJuvenile()){
		    all += "<tr style=\"background-color:"+redColor+";color:white\">";
		    str += " (Juvenile)";
		}
		else {
		    all += "<tr>";
		}						
		all += "<td>&nbsp;</td>";
		all += "<td>"+str+"</td>";
		all += "<td>"+str2+"</td>";
		all += "<td>"+str3+"</td></tr>";	
	    }
	}
	if(!all.equals("")){
	    out.println("<table class=\"wide\"><tr><td></td><th>Name </th>"+
			"<th>Address</th><th>Phones</th></tr>");
	    out.println(all);
	    out.println("</table>");
	}
	if(!id.equals("")){
	    out.println("<fieldset><legend>Defendant Insurance ");
	    out.println("<input name=\"action\" "+
			"type=\"button\" value=\"Add Defendant Insurance \" "+
			"tabindex=\"33\" onClick=\"window.open('"+
			url+
			"InsuranceServ?legalType=Legal"+
			"&type=Defendant"+
			"&relatedId="+id+
			"','Insurance','toolbar=0,location=0,"+
			"directories=0,status=0,menubar=0,"+
			"scrollbars=0,top=200,left=200,"+
			"resizable=1,width=600,height=500');return false;\" />");	   
	    out.println("</legend>");
	    InsuranceList il = new InsuranceList(debug);
	    il.setRelatedId(id);
	    il.setType("Defendant");
	    il.lookFor();
	    List<Insurance> ls = il.getInsurances();
	    if(ls != null){
		out.println("<table>");
		for(Insurance insr: ls){
		    Helper.printInsurance(out,insr,url,id,"Recovery Action","LegalServ");
		}
		out.println("</table>");
	    }
	    out.println("</fieldset>");
	}
	out.println("</fieldset>");
	out.println("<fieldset><legend>Incident </legend>");
	//
	out.println("<table>");
	out.println("<tr><td><label for=\"doi\">Date of Incident: "+
		    "</label>"+
		    "<input name=\"doi\" id=\"doi\" value="+
		    "\""+doi+"\" size=\"10\" maxlength=\"10\" "+
		    "class=\"date\" "+
		    "/> ");
	out.println("</td></tr>");

	out.println("<tr><td>");
	out.println("<label for=\"location\">Location: </label>"+
		    "<input name=\"location\" id=\"location\" value="+
		    "\""+location+"\" size=\"50\" maxlength=\"80\" />"+
		    "</td></tr>");
	out.println("<tr><td><label for=\"description\">Description of "+
		    "Incident:</label><br />");
	out.println("<textarea name=\"description\" id=\"description\" "+
		    "rows=\"5\" cols=\"70\" wrap=\"wrap\">");
	out.println(description);
	out.println("</textarea></td></tr>");
	out.println("<tr><td><label for=\"allDocuments\">List All "+
		    "Documents Received Regarding Incident:</label><br />");
	out.println("<textarea name=\"allDocuments\" id=\"allDocuments\" "+
		    "rows=\"5\" cols=\"70\" wrap=\"wrap\">");
	out.println(allDocuments);
	out.println("</textarea></td></tr>");
	out.println("</table>");
	out.println("</fieldset>");
	if(id.equals("")){
	    out.println("<fieldset><legend>Employee Info </legend>");			
	    out.println("<p>You will be able to enter employee info after"+
			" you fill this form and Save </p>");
	}
	else{
	    out.println("<fieldset><legend>Employee Info </legend>");

	    out.println("<table>");			
	    out.println("<tr><td>&nbsp;");
	    out.println("</td><td>");			
	    out.println("<input name=\"action\" "+
			"type=\"button\" value=\"Add Employee \" "+
			"tabindex=\"33\" onClick=\"window.open('"+
			url+
			"GetEmpInfoServ?legalType=Recovery"+
			"&relatedId="+id+
			"&opener=LegalServ"+
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
		    Helper.printEmployee(out,empo,url,id,"LegalServ");
		}

	    }
	    out.println("</table>");			
	    out.println("</fieldset>");	
	}		
	//
	out.println("<fieldset><legend>City Vehicles </legend>");
	out.println("<table>");
	out.println("<tr><td><input type=\"checkbox\" name=\"cityAutoInc\" "+
		    "id=\"cityAutoInc\" value=\"y\" "+
		    cityAutoInc+" /><Label>City Vehicle Invloved</lable>"+
		    "</td></tr>");
	out.println("<tr><td><table><tr><td></td>"+
		    "<td><label for=\"vin\">VIN #:</label></td>"+
		    "<td><label for=\"autoPlate\">Plate #:</label></td>"+
		    "<td><label for=\"autoNum\">Number:</label></td>"+
		    "<td><label for=\"autoMake\">Make:</label></td>"+
		    "<td><label for=\"autoModel\">Model:</label></td>"+
		    "<td><label for=\"autoYear\">Year:</label></t><tr>");
	out.println("<tr><td>Add Vehicle </td>");
	out.println("<td><input name=\"vin\" id=\"vin\" value=\""+
		    "\" size=\"6\" maxlength=\"20\"/></td>");
	out.println("<td>"+
		    "<input name=\"autoPlate\" id=\"autoPlate\" value=\""+
		    "\" size=\"6\" maxlength=\"20\"/></td>");
	out.println("<td>"+
		    "<input name=\"autoNum\" id=\"autoNum\" value=\""+
		    "\" size=\"6\" maxlength=\"20\"/></td>");
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
	    String str="";
	    all = ""; 
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
	out.println("</table>");
	out.println("</fieldset>");
	//
	// History
	out.println("<fieldset><legend>History</legend>");
	//
	// Dept History
	out.println("<fieldset><legend>Department History</legend>");
	out.println("<table>");
	out.println("<tr><td>");
	out.println("<label for=\"deptRecoverDate\">Date Department first "+
		    " Attempted Recovery:</label>"+
		    "<input name=\"deptRecoverDate\" id=\"deptRecoverDate\" "+
		    "value=\""+
		    deptRecoverDate+"\" size=\"10\" maxlength=\"10\" "+
		    "class=\"date\" "+
		    " />");
	out.println("</td></tr>");
	out.println("<tr><td>");
	out.println("<label for=\"deptCollectDate\">Date Department Collected"+
		    " Total Amount Owed:</label>"+
		    "<input name=\"deptCollectDate\" id=\"deptCollectDate\" "+
		    "value=\""+
		    deptCollectDate+"\" size=\"10\" maxlength=\"10\" "+
		    "class=\"date\" "+
		    " />");
	out.println("</td></tr>");
	out.println("</table>");
	out.println("</fieldset>");
	//
	if(!deptToRisk.equals("")){
	    // Risk History
	    out.println("<fieldset><legend>Risk History</legend>");
	    out.println("<table>");
	    out.println("<tr><td>");
	    out.println("<label for=\"deptToRiskDate\">Date Department Forwarded"+
			" Matter to Risk to Attempt Recovery:</label>"+
			"<input name=\"deptToRiskDate\" id=\"deptToRiskDate\" "+
			"value=\""+
			deptToRiskDate+"\" size=\"10\" maxlength=\"10\" "+
			"class=\"date\" "+
			"/>");
	    out.println("</td></tr>");
	    out.println("<tr><td>");
	    out.println("<label for=\"riskFirstDate\">Date Risk First Attempted"+
			" Recovery:</label>"+
			"<input name=\"riskFirstDate\" id=\"riskFirstDate\" "+
			"value=\""+
			riskFirstDate+"\" size=\"10\" maxlength=\"10\" "+
			"class=\"date\" "+
			"/>");
	    out.println("</td></tr>");
	    out.println("<tr><td>");
	    out.println("<label for=\"filed\">Date Risk Filed Suit to "+
			" Attempt Recovery:</label>"+
			"<input name=\"filed\" id=\"filed\" "+
			"value=\""+filed+"\" size=\"10\" maxlength=\"10\" "+
			"class=\"date\" />");
	    out.println("</td></tr>");
	    out.println("<tr><td>");
	    out.println("<label for=\"judgment\">Date Risk Received a Judgment"+
			":</label>"+
			"<input name=\"judgment\" id=\"judgment\" "+
			"value=\""+judgment+"\" size=\"10\" maxlength=\"10\" "+
			"class=\"date\" "+
			"/>");
	    out.println("</td></tr>");
	    out.println("<tr><td>");
	    out.println("<label for=\"proSupp\">Date Risk Filed a Proceeding "+
			"Supplemental:</label>"+
			"<input name=\"proSupp\" id=\"proSupp\" "+
			"value=\""+proSupp+"\" size=\"10\" maxlength=\"10\" "+
			"class=\"date\" "+
			"/>");
	    out.println("</td></tr>");
	    out.println("<tr><td>");
	    out.println("<label for=\"toProsecutorDate\">Date Risk Forwarded "+
			"Matter to Prosecutor\"s Office to Attempt Recovery:"+
			"</label>"+
			"<input name=\"toProsecutorDate\" id=\"toProsecutorDate\""+
			" value=\""+toProsecutorDate+"\" size=\"10\" "+
			"class=\"date\" "+
			"maxlength=\"10\" />");
	    out.println("</td></tr>");
	    out.println("<tr><td>");
	    out.println("<label for=\"convictionDate\">Date Prosecutor\"s Office "+
			"Received a Conviction (which includes restitution):"+
			"</label>"+
			"<input name=\"convictionDate\" id=\"convictionDate\" "+
			"value="+
			"\""+convictionDate+"\" size=\"10\" maxlength=\"10\" "+
			"class=\"date\" />");
	    out.println("</td></tr>");
	    out.println("<tr><td>");
	    out.println("<label for=\"collectDate\">Date Risk Collected Total "+
			"Amount Owed:</label>"+
			"<input name=\"collectDate\" id=\"collectDate\" value="+
			"\""+collectDate+"\" size=\"10\" maxlength=\"10\" "+
			"class=\"date\" "+
			"/>");
	    out.println("</td></tr>");
	    out.println("</table>");
	    out.println("</fieldset>");
	}
	//
	if(!riskToInsur.equals("")){
	    // Insurance History
	    out.println("<fieldset><legend>Insurance History</legend>");
	    out.println("<table>");
	    out.println("<tr><td>"+
			"<input type=\"checkbox\" name=\"insured\" id=\"insured\""+
			" value=\"y\" "+insured+"><label>Insured</label></td></tr>");
	    out.println("<tr><td>");
	    out.println("<label for=\"riskToInsurDate\">Date Risk Forwarded "+
			"Matter to Insurance to Attempt Recovery:</label>"+
			"<input name=\"riskToInsurDate\" "+
			"id=\"riskToInsurDate\" value=\""+
			riskToInsurDate+"\" size=\"10\" maxlength=\"10\" "+
			"class=\"date\" "+
			"/>");
	    out.println("</td></tr>");
	    out.println("<tr><td>");
	    out.println("<label for=\"insurRecoveryDate\">Date Insurance "+
			" First Attempted Recovery:</label>"+
			"<input name=\"insurRecoveryDate\" "+
			"class=\"date\" "+
			"id=\"insurRecoveryDate\" value=\""+
			insurRecoveryDate+"\" size=\"10\" maxlength=\"10\" />");
	    out.println("</td></tr>");
	    out.println("<tr><td>");
	    out.println("<label for=\"collectDate\">Date Insurance Collected"+
			" Total (or Partial) Amount Owed:</label>"+
			"<input name=\"collectDate\" "+
			"class=\"date\" "+
			"id=\"collectDate\" value=\""+
			collectDate+"\" size=\"10\" maxlength=\"10\" />");
	    out.println("</td></tr>");
	    out.println("</table>");
	    out.println("</fieldset>");
	}
	out.println("</fieldset>");  //End history
	//
	if(!riskToInsur.equals("")){
	    // Insurance
	    out.println("<fieldset><legend>City Insurance ");
	    out.println("<input name=\"action\" "+
			"type=\"button\" value=\"Add Insurance \" "+
			"tabindex=\"33\" onClick=\"window.open('"+
			url+
			"InsuranceServ?legalType=Recovery+Action"+
			"&opener=LegalServ"+
			"&type=City"+
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
		il.setType("City");
		il.lookFor();
		List<Insurance> ls = il.getInsurances();
		if(ls != null){

		    out.println("<table>");
		    for(Insurance insr: ls){
			Helper.printInsurance(out,insr,url,id,"Recovery Action","LegalServ");
		    }
		    out.println("</table>");
		}
	    }
	    out.println("</fieldset>");					
	}
	out.println("<fieldset><legend>Damages and Charges </legend>");
	out.println("<table><tr><td>");
	out.println("<label for=\"damageAmnt\">Amount of Damage: </label>"+
		    "<input name=\"damageAmnt\" id=\"damageAmnt\" value="+
		    "\""+damageAmnt+"\" size=\"10\" maxlength=\"10\" "+
		    "onchange=\"checkNumber(this)\" />");
	if(!balance.equals("")){
	    out.println("<label>Balance: </label>$"+balance);
	}
	out.println("</td></tr>");
	out.println("<tr><td>");
	out.println("<label for=\"paidByDef\">Amount Paid By Defendant (his/her"+
		    "Insurance): $</label>"+
		    "<input name=\"paidByDef\" id=\"paidByDef\" value=\""+
		    paidByDef+"\" size=\"10\" maxlength=\"10\" "+
		    "onchange=\"checkNumber(this)\" /> ");
	out.println("</td></tr>");		
	out.println("<tr><td>");
	out.println("<label for=\"paidByInsur\"> Amount Paid By City Insurance:"+
		    "$</label><input name=\"paidByInsur\" "+
		    "id=\"paidByInsur\" value=\""+paidByInsur+
		    "\" size=\"8\" maxlength=\"8\" "+
		    "onchange=\"checkNumber(this)\" />");
	out.println("</td></tr>");
	out.println("<tr><td>");
	out.println("<label for=\"paidByCity\">Amount Paid By City: $"+
		    "</label>"+
		    "<input name=\"paidByCity\" id=\"paidByCity\" value="+
		    "\""+paidByCity+"\" size=\"10\" maxlength=\"10\" "+
		    "onchange=\"checkNumber(this)\" "+
		    "/></td></tr>");
	out.println("<tr><td><label for=\"paidByRisk\">Paid By Risk: $"+
		    "</label>"+
		    "<input name=\"paidByRisk\" id=\"paidByRisk\" value="+
		    "\""+paidByRisk+"\" size=\"10\" maxlength=\"10\" "+
		    "onchange=\"checkNumber(this)\" "+
		    "/> ");
	out.println("<label for=\"miscByCity\">Misc Paid By City: $</label>"+
		    "<input name=\"miscByCity\" id=\"miscByCity\" value="+
		    "\""+miscByCity+"\" size=\"10\" maxlength=\"10\" "+
		    "onchange=\"checkNumber(this)\" />");
	out.println("</td></tr>");
	out.println("</table>");
	out.println("</fieldset>");	
	//
	out.println("<fieldset><legend>Other Details </legend>");
	out.println("<table><tr><td>");
	out.println("<textarea name=\"otherDetails\" id=\"otherDetails\" "+
		    "rows=\"5\" cols=\"70\" wrap=\"wrap\">");
	out.println(otherDetails);
	out.println("</textarea></td></tr>");
	out.println("</table>");
	out.println("</fieldset>");
	//
	out.println("<fieldset>");
	out.println("<table class=\"control\">");
	if(id.equals("")){
	    out.println("<tr><td>");
	    out.println("<input type=\"submit\" tabindex=\"31\" "+
			"accesskey=\"s\" value=\"Save\" "+
			"name=\"action\" class=\"submit\" />");
	    out.println("</form></td></tr>");
	}
	else{ // save, update
	    //
	    out.println("<td><input accesskey=\"u\" "+
			"tabindex=\"31\" value=\"Update\" "+
			"type=submit name=\"action\" "+
			"class=\"submit\" />");
	    out.println("</td><td>");
	    out.println("<input type=\"button\" name=\"action\" "+
			"accesskey=\"n\" value=\"Insert Defendant\""+
			" tabindex=\"32\" onClick=\"document.location='"+
			url+"InsertPersonServ?inType=legal"+
			"&risk_id="+id+"'\"/>");
	    out.println("</td>");
	    out.println("<td>");
	    out.println("<input name=\"action\" accesskey=\"y\" "+
			"type=\"button\" value=\"Payment\" "+
			"tabindex=\"34\" onClick=\"window.open('"+
			url+
			"PaymentServ?type=legal"+
			"&risk_id="+id+"');return false;\" />");
	    out.println("</td>");
	    out.println("<td valign=top><input "+
			"type=button name=\"action\" id=\"action\" "+
			"onclick=\"window.location='"+url+"RiskFileServ?risk_id="+id+"';\" "+
			"class=\"submit\" value=\"Upload File\" />");
	    out.println("</td>");							
	    out.println("<td>");
	    out.println("<input type=\"button\" value=\"Add Notes\" "+
			"onclick=\"window.open('" + url + 
			"NoteServ?risk_id="+id+"&opener=LegalServ','Notes',"+
			"'location=0:0,menubar=1,width=500,height=400,toolbar=1,scrollbars=1');return false;\"></input>");
	    out.println("</td>");

						
	    out.println("</td></tr>");
	    //
	    // Second row
	    out.println("<tr>");
	    out.println("<td><input type=\"button\" name=\"action\" "+
			"accesskey=\"n\" value=\"Generate Tort Claim\""+
			" tabindex=\"35\" onClick=\"document.location='"+
			url+"TortClaimServ?vsId="+id+"&action=Generate"+
			"'\" />");
	    out.println("</td>");
	    //}
	    out.println("<td><input type=\"button\" name=\"action\" "+
			"accesskey=\"n\" value=\"Worker Comp\""+
			" tabindex=\"35\" onClick=\"document.location='"+
			url+"WorkCompServ?vsId="+id+"&action=Generate"+
			"'\" />");
	    out.println("</td><td>");
	    out.println("<input type=\"button\" name=\"action\" "+
			"accesskey=\"n\" value=\"Internal Accident\""+
			" tabindex=\"36\" onClick=\"document.location='"+
			url+"SafetyServ?vsId="+id+"&action=Generate"+
			"'\" />");
	    out.println("</form>");				
	    out.println("</td><td>");
	    out.println("<form id=\"myForm2\" onSubmit=\"return "+
			"validateDelete();\">");
	    out.println("<input type=hidden name=\"id\" value=\""+id+"\">");
	    out.println("<input type=\"submit\" name=\"action\" "+
			"value=\"Delete\" "+
			"accesskey=\"e\" tabindex=\"36\" "+
			" />");
	    out.println("</form>");
	    out.println("</td></tr>");
	}
	out.println("</table>");
	out.println("</fieldset>");
	if(!id.equals("")){
	    if(lgl.hasNotes()){
		Helper.printNotes(out,
				  url,
				  "LegalServ",
				  lgl.getNotes());									
	    }
	    if(lgl.hasFiles()){
		Helper.printFiles(out,
				  url,
				  lgl.getFiles());									
	    }									
	    out.println("<center>* Click in check box to delete the item from this record </center>");
	}
	out.println("</div>");
	out.println("</body></html>");
	out.flush();
	out.close();
	action="";
    }

}























































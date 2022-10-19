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
@WebServlet(urlPatterns = {"/TortClaimServ","/TortClaim"})
public class TortClaimServ extends TopServlet{

    static Logger logger = LogManager.getLogger(TortClaimServ.class);
    RiskTypeList types = null;	
    //
    // New subcategory list
    //
    static final String[] closedArr = {
	"",
	"Paid",
	"Complied",
	"Dismissed"
    };
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
    
	String claimNum="", 
	    adjuster="", 
	    adjusterPhone="",
	    type="", id="", status="", 
	    deductible="", recovered="",
	    insuranceStatus="", empid="", 
	    empName="", empPhone="", dept_id="", vin="", empTitle="",
	    comments="", deptName="", 
	    incidentDate="",sent="", expires="", // potential expires
	    received="", closed=""; // dates

	String incident = "", // instead of location
	    paidByCity = "",paidByInsur = "",   // double
	    cityTotalCost="",paidByCity2City="",paidByInsur2City="",
	    deductible2="", paidByRisk="", law_firm_id="",
	    requestAmount = "", miscByCity = "", cityAutoInc="",
	    autoMake ="", autoModel="", autoYear="", autoPlate="",
	    autoNum="", recordOnly="";
	
	boolean success = true;
       	String username = "", message = "", action = "",entry_time = "", 
	    entry_date = "", prevAction="", pid="", vsId="", relatedId="";
	//
	// newly added 
	String otherType="", policy="", filed="",
	    settled="", insurDecision=""; // date
	String insurer = "", subInsur="", adjusterEmail="",
	    attorney="", attorneyPhone="", attorneyEmail="";
	//
	// removed 
	// claimToInsurance agreedAmount
	//
	res.setContentType("text/html");
	PrintWriter out = res.getWriter();
	String name, value;
	Enumeration values = req.getParameterNames();
	String [] vals;
	String[] witList = null, clmntList = null;
	String[] delAutoAid = null;
	String[] delRelated = null;
	String[] delRelatedDept = null;
	List<LawFirm> firms = null;
	Legal tlegal = null;
	TortClaim tc = new TortClaim(debug);
	//
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();	
	    value = Helper.replaceSpecialChars(value);

	    if (name.equals("received")){
		received = value;
	    }
	    else if (name.equals("type")) {
		type = value;
	    }
	    else if (name.equals("law_firm_id")) {
		law_firm_id = value;
	    }						
	    else if(name.equals("delAutoAid")){
		delAutoAid = vals; // this is array
	    }
	    else if(name.equals("delRelated")){
		delRelated = vals; // this is array
	    }
	    else if(name.equals("delRelatedDept")){
		delRelatedDept = vals; // this is array
	    }			
	    else if (name.equals("id")) {
		id = value;
		tc.setId(value);
	    }
	    else if (name.equals("pid")) {
		pid = value;
	    }
	    else if (name.equals("relatedId")) {
		relatedId = value;
	    }
	    else if (name.equals("vsId")) {
		vsId = value;
	    }			
	    else if (name.equals("filed")) {
		filed = value;
	    }
	    else if (name.equals("expires")) {
		expires = value;
	    }
	    else if (name.equals("otherType")) {
		otherType = value;
	    }
	    else if (name.equals("status")) {
		status = value;
	    }
	    else if (name.equals("deductible")) {
		deductible = value;
	    }
	    else if (name.equals("deductible2")) {
		deductible2 = value;
	    }
	    else if (name.equals("recovered")) {
		recovered = value;
	    }
	    else if (name.equals("paidByCity")) {
		paidByCity =value;
	    }
	    else if (name.equals("paidByInsur")) {
		paidByInsur =value;
	    }
	    else if (name.equals("miscByCity")) {
		miscByCity =value;
	    }
	    else if (name.equals("paidByRisk")) {
		paidByRisk =value;
	    }
	    else if (name.equals("paidByCity2City")) {
		paidByCity2City =value;
	    }
	    else if (name.equals("paidByInsur2City")) {
		paidByInsur2City =value;
	    }
	    else if (name.equals("requestAmount")) {
		requestAmount =value;
	    }
	    else if (name.equals("settled")) {
		settled =value;
	    }
	    else if (name.equals("cityTotalCost")) {
		cityTotalCost =value;
	    }
	    else if (name.equals("cityAutoInc")) {
		cityAutoInc = value;
	    }
	    else if (name.equals("empName")) {
		empName = value.toUpperCase();
	    }
	    else if (name.equals("empPhone")) {
		empPhone = value;
	    }
	    else if (name.equals("dept_id")) {
		dept_id = value;
	    }
	    else if (name.equals("deptName")) {
		deptName = value;
	    }
	    else if (name.equals("empid")) {
		empid = value.toLowerCase();
	    }
	    else if (name.equals("empTitle")) {
		empTitle = value;
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
	    else if (name.equals("autoPlate")) {
		autoPlate = value.toUpperCase();
	    }
	    else if (name.equals("autoYear")) {
		autoYear = value;
	    }
	    else if (name.equals("incident")) {
		incident = value;
	    }
	    else if (name.equals("comments")) {
		comments = value;
	    }
	    else if (name.equals("incidentDate")) {
		incidentDate =value;
	    }
	    else if (name.equals("received")) {
		received =value;
	    }
	    else if (name.equals("closed")){
		closed =value;
	    }
	    else if (name.equals("clmntMark")){ // array
		clmntList =vals;
	    }
	    else if (name.equals("witMark")){ // array
		witList =vals;
	    }
	    else if (name.equals("subInsur")){   
		subInsur =value;
	    }
	    else if (name.equals("username")){   
		username =value;
	    }
	    else if (name.equals("prevAction")){ 
		prevAction = value;  
	    }
	    else if (name.equals("recordOnly")) {
		recordOnly = value;
	    }
	    else if (name.equals("action2")){
		if(!value.equals(""))
		    action = value;  
	    }								
	    else if (name.equals("action")){ 
		// Get, Save, zoom, edit, delete, New, Refresh
		action = value;  
	    }
	}

	// System.err.println(" action & prev :"+action+" "+prevAction);
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
	HttpSession session = session = req.getSession(false);
	User user = null;
	if(session != null){
	    user = (User)session.getAttribute("user");
	}
	if(user == null){
	    res.sendRedirect(url+"Login");
	    return;
	}		
	if(types == null){
	    types = new RiskTypeList(debug, "unified");
	    String msg = types.lookFor();
	    if(!msg.equals("")){
		types = null;
		message += msg;
		success = false;
		logger.error(msg);
	    }
	}
	if(firms == null){
	    LawFirmList lfl = new LawFirmList(debug);
	    String msg = lfl.lookFor();
	    if(!msg.equals("")){
		message += msg;
		success = false;
		logger.error(msg);
	    }
	    else{
		firms = lfl.getFirms();
	    }
	}								
	Calendar cal = Calendar.getInstance();
	entry_date = Helper.getToday();

	if(expires.equals("") && 
	   filed.equals("") && 
	   !incidentDate.equals("")){
	    String dd[] = null;
	    int m = 0, d=0,y=0;
	    dd = incidentDate.split("/");
	    try{
		m = Integer.parseInt(dd[0]);
		d = Integer.parseInt(dd[1]);
		y = Integer.parseInt(dd[2]);
	    }catch(Exception ex){}
	    if(m > 0) m--;
	    if(dd != null){
		cal.set(Calendar.YEAR, y);
		cal.set(Calendar.MONTH, m);
		cal.set(Calendar.DATE, d);
		cal.add(Calendar.DATE, 181);
		m = cal.get(Calendar.MONTH) + 1;    
		d = cal.get(Calendar.DATE);    
		y = cal.get(Calendar.YEAR);  
		expires = ""+m+"/"+d+"/"+y;
	    }
	}
	if(action.equals("Generate")){
	    //
	    // We are coming from LegalServ
	    // Using the vsId to find if any 
	    //
	    tlegal = new Legal(debug);
	    tlegal.setId(vsId);
	    String msg = tlegal.doSelect();
	    if(!msg.equals("")){
		message += msg;
		success = false;
		tlegal = null;
		logger.error(msg);
	    }
	    if(success){
		// 
		type = tlegal.getType();
		status = tlegal.getStatus();
		if(!status.equals("Closed")){
		    status = "Open";
		}
		paidByCity = tlegal.getPaidByCity();
		paidByRisk = tlegal.getPaidByRisk();
		paidByInsur = tlegal.getPaidByInsur();
		miscByCity = tlegal.getMiscByCity();
		deductible = tlegal.getDeductible();
				
		incidentDate = tlegal.getDoi();
		incident = tlegal.getDescription();
		comments = tlegal.getOtherDetails();
		cityAutoInc = tlegal.getCityAutoInc();
		subInsur = tlegal.getRiskToInsur();
		otherType = tlegal.getOtherType();
		recordOnly = tlegal.getRecordOnly();
		if(!(dept_id.equals("") || dept_id.equals("0"))){
		    Department dp = new Department(debug, dept_id);
		    String back = dp.doSelect();
		    if(back.equals("")){
			deptName = dp.getName();
		    }
		    dp = null;
		}
		action = "Save";
		relatedId = vsId;
	    }
	}
	//
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
	    tc.setType(type);
	    tc.setStatus(status);
	    tc.setDeductible(deductible);
	    tc.setDeductible2(deductible2);
	    tc.setPaidByCity2City(paidByCity2City);
	    tc.setPaidByInsur2City(paidByInsur2City);
	    tc.setCityTotalCost(cityTotalCost);

	    tc.setRecovered(recovered);
	    tc.setRequestAmount(requestAmount);
	    tc.setSettled(settled);
	    tc.setIncidentDate(incidentDate);
	    tc.setIncident(incident);
	    tc.setComments(comments);
	    tc.setReceived(received);
	    tc.setPaidByCity(paidByCity);
	    tc.setPaidByInsur(paidByInsur);
	    tc.setMiscByCity(miscByCity);
	    tc.setCityAutoInc(cityAutoInc);
	    tc.setClosed(closed);
	    tc.setOtherType(otherType);
	    tc.setFiled(filed);
	    tc.setSubInsur(subInsur);
	    tc.setInsurDecision(insurDecision);
	    tc.setExpires(expires);
	    tc.setRecordOnly(recordOnly);
	    tc.setPaidByRisk(paidByRisk);
	    tc.setLawFirmId(law_firm_id);
	    String back = tc.doSave();
	    if(back.equals("")){
		id = tc.getId();
		message += "Data saved successfully";
		//
		// if we have the claimant pid we attach it to this 
		// claim
		if(!pid.equals("")){
		    HandlePerson hp = new HandlePerson(debug);
		    hp.setPid(pid);
		    back += hp.doClaimantInsert(id);
		    if(!back.equals("")){
			message += back;
			success = false;
			logger.error(back);
		    }
		}
		if(!vin.equals("") || !autoPlate.equals("") || 
		   !autoNum.equals("")){
		    Auto auto = new Auto(debug);
		    auto.setRisk_id(id);
		    auto.setVin(vin);
		    auto.setAutoMake(autoMake);
		    auto.setAutoModel(autoModel);
		    auto.setAutoYear(autoYear);
		    auto.setAutoPlate(autoPlate);
		    auto.setAutoNum(autoNum);
		    back = auto.doSave();
		    if(back.equals("")){
			// aid = auto.getAid(); // we do not need it
		    }
		    else{
			message += back;
			logger.error(back);
			success = false;
		    }
		}
		if(!relatedId.equals("")){
		    // TODO after vslegal
		    // check if there are claimants associated
		    // get the list of their ID's and insert them as
		    // defendants 
		    //
		    List<RiskPerson> defs = null;
		    Legal lgl = new Legal(debug, relatedId);
		    if(lgl.hasDefendants()){
			defs = lgl.getDefendants();
		    }
		    if(defs != null){
			tc.doClaimantInsert(defs);
		    }
		    /*
		      String [] defArr = null;
		      HandlePerson hp = new HandlePerson(debug);
		      back = hp.generatePidList(relatedId, "defendant");
		      if(back.equals("")){
		      defArr = hp.getPidArr();
		      }
		      if(defArr != null){
		      back="";
		      back += hp.doClaimantInsert(id);
		      if(!back.equals("")){
		      message += back;
		      success = false;
		      }
		      }
		    */
		    Related rel = new Related(id, relatedId,"Tort Claim", debug);
		    back = rel.doSave();
		    if(!back.equals("")){
			message += back;
			logger.error(back);
			success = false;
		    }	
		    // make a link to the insurance id's
		    InsuranceList il = new InsuranceList(debug);
		    il.setRelatedId(relatedId);
		    il.lookFor();
		    List<Insurance> lss = il.getInsurances();
		    if(lss != null){
			back = il.addLinks(id);
			if(!back.equals("")){
			    message += back;
			    logger.error(back);
			}		
		    }
		    EmployeeList el = new EmployeeList(debug);
		    el.setRelatedId(relatedId);
		    el.lookFor();
		    List<Employee> eps = el.getEmployees();
		    if(eps != null){
			back = el.addLinks(id);
			if(!back.equals("")){
			    message += back;
			    success = false;
			}		
		    }	
		}
	    }
	    else{
		message += back;
		System.err.println(back);
		success = false;
	    }
	}
	else if(action.equals("Update")){
	    //
	    if(!deptName.equals("")){
		Department dp = new Department(debug);
		dp.setName(deptName);
		String str = dp.findOrAddDept();
		if(!str.equals("")) dept_id = str;
		dp = null;
	    }
	    tc.setType(type);
	    tc.setStatus(status);
	    tc.setDeductible(deductible);
	    tc.setDeductible2(deductible2);
	    tc.setPaidByCity2City(paidByCity2City);
	    tc.setPaidByInsur2City(paidByInsur2City);
	    tc.setCityTotalCost(cityTotalCost);
	    tc.setRecovered(recovered);
	    tc.setRequestAmount(requestAmount);
	    tc.setSettled(settled);
	    tc.setIncidentDate(incidentDate);
	    tc.setIncident(incident);
	    tc.setComments(comments);
	    tc.setReceived(received);
	    tc.setPaidByCity(paidByCity);
	    tc.setPaidByInsur(paidByInsur);
	    tc.setMiscByCity(miscByCity);
	    tc.setCityAutoInc(cityAutoInc);
	    tc.setClosed(closed);
	    tc.setOtherType(otherType);
	    tc.setFiled(filed);
	    tc.setSubInsur(subInsur);
	    tc.setInsurDecision(insurDecision);
	    tc.setExpires(expires);
	    tc.setRecordOnly(recordOnly);
	    tc.setPaidByRisk(paidByRisk);
	    tc.setLawFirmId(law_firm_id);
	    String back = tc.doUpdate();
	    if(!back.equals("")){
		message += back;
		logger.error(back);
		success = false;
	    }
	    else{
		message += "Data updated successfully";
	    }
	    vsId = tc.getLegalId();
	    // 
	    // check if we need to add an auto record 
	    //
	    if(!vin.equals("") || !autoPlate.equals("") || 
	       !autoNum.equals("")){
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
		    logger.error(back);
		    success = false;
		}
	    }
	    //
	    // check if we need to delete some of the claimant or 
	    // witnesses from this claim when the user marks 
	    // the checkboxes
	    //
	    if(clmntList != null){
		HandlePerson hp = new HandlePerson(debug);
		hp.setPidArr(clmntList);
		message += hp.doClaimantDelete(id);
	    }
	    if(witList != null){
		HandlePerson hp = new HandlePerson(debug);
		hp.setPidArr(clmntList);
		message += hp.doWitnessDelete(id);
	    }
	    if(delAutoAid != null){
		for(String aid: delAutoAid){
		    Auto auto = new Auto(debug);
		    auto.setId(aid);
		    auto.doDelete();
		}
	    }
	    if(!relatedId.equals("")){
		Related rel = new Related(id, relatedId,"Tort Claim", debug);
		back = rel.doSave();
		if(!back.equals("")){
		    message += back;
		    System.err.println(back);
		    success = false;
		}	
	    }
	    if(delRelated != null){
		for(String str: delRelated){
		    Related rel = new Related(id, str, debug);
		    rel.doDelete();
		}
	    }
	    if(delRelatedDept != null){
		for(String str: delRelatedDept){
		    RelatedDept rdept = new RelatedDept(debug, str);
		    rdept.doDelete();
		}
	    }			
	}
	else if(action.equals("Delete")){
	    //
	    // System.err.println("delete record");
	    //
	    String back = tc.doDelete();
	    if(!back.equals("")){
		message += back;
		logger.error(back);
		success = false;
	    }
	    else{
		message += " Record deleted";
	    }
	    //
	    // Delete all the claimant and witnesses from this
	    // claim (if any)
	    //
	    HandlePerson hp = new HandlePerson(debug);
	    back = hp.doClaimantAndWitnessDelete(id);
	    if(!back.equals("")){
		message += back;
		logger.error(back);
		success = false;
	    }
	    //
	    // delete any auto related
	    //
	    Auto auto =new Auto(debug);
	    auto.doDeleteAllFor(id);
	    Related rel = new Related(id, debug);
	    rel.doDelete();

	}
	else if(!id.equals("")){	
	    //
	    String back = tc.doSelect();
	    if(back.equals("")){
		type = tc.getType();
		status = tc.getStatus();
		deductible = tc.getDeductible();
		deductible2 = tc.getDeductible2();
		cityTotalCost = tc.getCityTotalCost();
		paidByCity2City = tc.getPaidByCity2City();
		paidByInsur2City = tc.getPaidByInsur2City();

		recovered = tc.getRecovered();
		requestAmount = tc.getRequestAmount();

		settled = tc.getSettled();
		paidByCity = tc.getPaidByCity();
		paidByInsur = tc.getPaidByInsur();
		miscByCity = tc.getMiscByCity();
		incidentDate = tc.getIncidentDate();
		incident = tc.getIncident();
		comments = tc.getComments();
		received = tc.getReceived();
		closed = tc.getClosed();

		cityAutoInc = tc.getCityAutoInc();

		otherType = tc.getOtherType();
		filed = tc.getFiled();
		subInsur = tc.getSubInsur();
		expires = tc.getExpires();
		recordOnly = tc.getRecordOnly();
		paidByRisk = tc.getPaidByRisk();
		law_firm_id = tc.getLawFirmId();
		if(!(dept_id.equals("") || dept_id.equals("0"))){
		    Department dp = new Department(debug, dept_id);
		    back = dp.doSelect();
		    if(back.equals("")){
			deptName = dp.getName();
		    }
		    dp = null;
		}
		vsId = tc.getLegalId();
	    }
	    else{
		message += back;
		logger.error(back);
		success = false;
		action = "";
	    }
	}
	if(!cityAutoInc.equals("")) cityAutoInc = "checked";
	if(!filed.equals("")) filed = "checked";
	if(!subInsur.equals("")) subInsur = "checked";
	if(!recordOnly.equals("")) recordOnly = "checked";
	if(!prevAction.equals("") && action.equals("")) action = prevAction;
	//
	// Inserts
	out.println(Inserts.xhtmlHeaderInc);
	out.println(Inserts.banner(url));
	out.println(Inserts.menuBar(url, true));
	out.println(Inserts.sideBar(url));
	out.println("<div id=\"mainContent\">");
	out.println("<h3 class=\"titleBar\">Tort Claims "+id+"</h3>");
	//
	// if we have any message, it will be shown here
	if(success){
	    if(!message.equals(""))
		out.println("<h4>"+message+"</h4>");
	}
	else{
	    if(!message.equals(""))
		out.println("<h4 class=\"errorMessages\">"+message+"</h4>");
	}
	//
	out.println("<script type=\"text/javascript\">");
	out.println("  function validateForm(){	              ");
	out.println("    return true;                         ");
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
	out.println("  function setDeductible(optId){	      ");
	out.println("  if(document.forms[0].cityAutoInc.checked){ ");
	out.println("  if(optId == 1){                            ");
	out.println("    document.forms[0].deductible2.value=\"5000\"; ");
	out.println("  }else if(optId == 2){                            "); 
	out.println("    document.forms[0].deductible2.value=\"1000\";  ");
	out.println("  }else if(optId == 3){                            ");  
	out.println("    document.forms[0].deductible2.value=\"10000\";    ");
	out.println("  }else {                                            ");  
	out.println("    document.forms[0].deductible2.value=\"\"; ");
	out.println("   }} ");
	out.println("  if(optId == 1){                        ");
	out.println("    document.forms[0].deductible.value=\"5000\"; ");
	out.println("  }else if(optId == 2){                           "); 
	out.println("    document.forms[0].deductible.value=\"1000\";  ");
	out.println("  }else if(optId == 3){                            ");  
	out.println("    document.forms[0].deductible.value=\"10000\"; ");
	out.println("  }else {                                         ");  
	out.println("    document.forms[0].deductible.value=\"\"; ");
	out.println("  }} ");
	out.println("  function checkInsurStatus(optId){	       ");
	out.println("  if(optId == 1){  // closed                      ");
	out.println("   if(document.forms[0].insuranceStatus){         ");
	out.println("   var jj=document.forms[0].insuranceStatus.selectedIndex; ");
	out.println("   if(jj == 1){   // pending        "); 
	out.println("    alert(\"Claim can not be closed as long insurance status is pending\"); ");
	out.println("    document.forms[0].status.selectedIndex=0; ");
	out.println("   }}}}                                       ");
	out.println("</script>                                     ");
	out.println("<form id=\"myForm\" name=\"myForm\" method=\"post\" "+
		    "onSubmit=\"return validateForm();\">");
	if(!id.equals("")){
	    out.println("<input type=\"hidden\" name=\"id\" id=\"id\" "+
			"value=\""+id+"\">");
	}
	out.println("<input type=\"hidden\" name=\"action2\" value=\"\" id=\"action_id\" />");					
	if(!pid.equals("")){
	    out.println("<input type=\"hidden\" name=\"pid\" id=\"pid\" "+
			"value=\""+pid+"\">");
	}				
	out.println("<fieldset><legend>Claim Status </legend>");
	out.println("<table>");
	out.println("<tr><td>");
	out.println("<label for=\"status\">Status: </label>"+
		    "<select name=\"status\" "+
		    "onchange=\"checkInsurStatus(this.selectedIndex)\" "+
		    "id=\"status\">");
	for(int i=0;i<Inserts.statusArr.length;i++){
	    if(status.equals(Inserts.statusArr[i]))
		out.println("<option selected=\"selected\">"+status+
			    "</option>");
	    else
		out.println("<option>"+Inserts.statusArr[i]+"</option>");
	}
	out.println("</select>, ");
	out.println("<input type=\"checkbox\" name=\"recordOnly\" value=\"y\" "+
		    recordOnly+"><label> Record Only </label>");

	if(!action.equals("")){
	    out.println("<input type=\"hidden\" name=\"prevAction\" "+
			"value=\""+action+"\">");
	}
	out.println("</td></tr>");
	out.println("<tr><td>");
	out.println("<label for=\"incidentDate\">Incident Date: "+
		    "</label>"+
		    "<input name=\"incidentDate\" id=\"incidentDate\" "+
		    "value=\""+incidentDate+"\" "+
		    "onchange=\"checkDate(this)\" class=\"date\" "+
		    "size=\"10\" maxlength=\"10\" />");

	out.println("<label for=\"expires\">Potential Expires:</labeL>"+
		    "<input  name=\"expires\" id=\"expires\" "+
		    "onchange=\"checkDate(this)\" class=\"date\" "+
		    "value=\""+expires+"\" size=\"10\" maxlength=\"10\"> ");
	out.println("</td></tr>");
	out.println("<tr><td>");
	out.println("<input type=\"checkbox\" name=\"filed\" "+
		    "value=\"y\" "+ filed+"><label> Filed</label> ");
	out.println("</td></tr>");
	out.println("<tr><td>");				
	out.println("<input type=\"checkbox\" name=\"subInsur\" "+
		    " onclick=\"doUpdate()\" "+
		    "value=\"y\" "+subInsur+"><label> Submitted to Insurance </label>, ");
	out.println("</td></tr>");
	out.println("<tr><td>");
	out.println("<label for=\"type\">Type: </label>"+
		    "<select name=\"type\" id=\"type\">");
	if(types != null){
	    for(RiskType rtype:types){
		if(type.equals(rtype.getId()))
		    out.println("<option value=\""+type+
				"\" selected=\"selected\">"+rtype.getName()+
				"</option>");
		else
		    out.println("<option value=\""+rtype.getId()+
				"\">"+rtype.getName()+"</option>");
	    }
	}
	out.println("</select>");
	out.println("<label for=\"otherType\">Other (specify):</label>");
	out.println("<input name=\"otherType\" id=\"otherType\" size=\"20\" "+
		    "value=\""+otherType+"\" "+
		    "maxlength=\"30\">");
	out.println("</td></tr>");
	out.println("<tr><td>");
	// 
	/// received 
	out.println("<label for=\"received\"> Received Date:</label>"+
		    "<input name=\"received\" id=\"received\" value=\""+
		    received+"\" size=\"10\" maxlength=\"10\" class=\"date\""+
		    "onchange=\"checkDate(this)\" "+
		    " />");
	out.println("<label for=\"closed\">Closed Date:</label>"+
		    "<input name=\"closed\" id=\"closed\" value=\""+
		    closed+"\" size=\"10\" maxlength=\"10\" "+
		    "onchange=\"checkDate(this)\" class=\"date\""+
		    " /></td></tr>");

	out.println("<tr><td><label for=\"incident\">Incident Details: "+
		    "</label><br / >");
	out.println("<textarea name=\"incident\" id=\"incident\" rows=\"5\" "+
		    "cols=\"70\" wrap=\"wrap\">");
	out.println(incident);
	out.println("</textarea></td></tr>");
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
	// List of claimants here (if any)
	// 
	String all="", all2="";
	if(!id.equals("")){
						
	    String str="", str2="", str3="",back="";
	    // String [] clmntArr = null, witArr = null;
	    List<RiskPerson> claiments = null;
	    List<RiskPerson> witnesses = null;
	    if(tc.hasClaiments()){
		claiments = tc.getClaiments();
	    }
	    if(tc.hasWitnesses()){
		witnesses = tc.getWitnesses();
	    }
	    if(claiments != null){
		for(RiskPerson rp:claiments){
		    str = rp.getFullName();
		    str2 = rp.getAddress();
		    str3 = rp.getPhones();
		    if(rp.isJuvenile()){
			all += "<tr style=\"background-color:red;color:white\">";
		    }
		    else {
			all += "<tr>";
		    }
		    all += "<td><input type=\"checkbox\" "+
			"name=\"clmntMark\" "+
			"id=\"clmntMark\" "+
			"value=\""+rp.getId()+"\" />*</td>";
		    all += "<td><a href=\""+url+
			"RiskPersonServ?id="+rp.getId()+
			"&action=zoom&risk_id="+id+"\">"+str+
			"</a> ";	
		    all += "</td><td>"+str2+"</td><td>"+str3+"</td></tr>";
		}
	    }
	    if(witnesses != null){
		for(RiskPerson rp:witnesses){
		    str = rp.getFullName();
		    str2 = rp.getAddress();
		    str3 = rp.getPhones();
		    if(rp.isJuvenile()){
			all += "<tr style=\"background-color:red;color:white\">";
			str += " (Juvenile)";
		    }
		    else {
			all += "<tr>";
		    }						
		    all2 += "<td><input type=\"checkbox\" "+
			"name=\"witMark\" "+
			"id=\"witMark\" "+
			"value=\""+rp.getId()+"\" />*</td>";
		    all2 += "<td><a href=\""+url+
			"RiskPersonServ?pid="+rp.getId()+
			"&action=zoom&risk_id="+id+"\">"+str+
			"</a></td>";
		    all2 += "<td>"+str2+"</td><td>"+str3+"</td></tr>";
		}
	    }
	}
	if(!pid.equals("")){
	    String str="", str2="", str3="", back="";
	    RiskPerson rp = new RiskPerson(debug, pid);
	    back = rp.doSelect();
	    if(back.equals("")){
		str = rp.getFullName();
		str2 = rp.getAddress();
		str3 = rp.getPhones();
		if(rp.isJuvenile()){
		    all += "<tr style=\"background-color:red;color:white\">";
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
	    out.println("<fieldset><legend>Claimant </legend>");
	    out.println("<table class=\"wide\"><tr><td></td><th>Claimant </th>"+
			"<th>Address</th><td>Phone</td></tr>");
	    out.println(all);
	    out.println("</table>");
	    out.println("</fieldset>");
	    out.println("<fieldset><legend>Claimant Insurance ");
	    out.println("<input name=\"action\" "+
			"type=\"button\" value=\"Add Claimant Insurance \" "+
			"tabindex=\"33\" onclick=\"window.open('"+
			url+
			"InsuranceServ?legalType=Tort"+
			"&type=Defendant"+
			"&relatedId="+id+
			"','Insurance','toolbar=0,location=0,"+
			"directories=0,status=0,menubar=0,"+
			"scrollbars=0,top=200,left=200,"+
			"resizable=1,width=600,height=500');return false;\" />");	   
	    out.println("</legend>");
	    if(!id.equals("")){
		InsuranceList il = new InsuranceList(debug);
		il.setRelatedId(id);
		il.setType("Defendant");
		il.lookFor();
		List<Insurance> ls = il.getInsurances();
		if(ls != null){
		    out.println("<table>");
		    for(Insurance insr: ls){
			Helper.printInsurance(out,insr,url,id,"Tort Claim","TortClaimSer");
		    }
		    out.println("</table>");
		}
		out.println("</fieldset>");
	    }
	}
	if(!all2.equals("")){
	    out.println("<fieldset><legend>Witness(es) </legend>");
	    out.println("<table class=\"wide\"><tr><td></td><th>Witness </th>"+
			"<th>Address</th><td>Phone</td></tr>");
	    out.println(all2);
	    out.println("</table>");
	    out.println("</fieldset>");
	}
	//
	if(id.equals("")){
	    out.println("<fieldset><legend>Employee Info </legend>");			
	    out.println("<p>You will be able to enter employee info after"+
			" you fill this form and Save </p>");
	}
	else{
	    out.println("<fieldset><legend>Employee Info </legend>");
	    out.println("<table>");			
	    out.println("<tr><td><label>Employees:</label></td><td> ");
	    out.println("<input name=\"action\" "+
			"type=\"button\" value=\"Add Employee \" "+
			"tabindex=\"33\" onclick=\"window.open('"+
			url+
			"GetEmpInfoServ?opener=TortClaimServ"+
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
		    Helper.printEmployee(out,empo,url,id,"TortClaimServ");
		}

	    }
	    out.println("</table>");			
	    out.println("</fieldset>");	
	}	
	out.println("<fieldset><legend>City Vehicle Involoved</legend>");
	out.println("<table>");
	out.println("<tr><td>");
	out.println("<input type=\"checkbox\" name=\"cityAutoInc\" "+
		    "id=\"cityAutoInc\" value=\"y\" "+
		    "onclick=\"doUpdate()\" "+
		    cityAutoInc+" /><label> City Vehicle Involved </label>");
	out.println("</td></tr>");
	if(!cityAutoInc.equals("")){
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
			"\" size=\"10\" maxlength=\"20\"/></td>");
	    out.println("<td>"+
			"<input name=\"autoModel\" id=\"autoModel\" value=\""+
			"\" size=\"10\" maxlength=\"20\"/></td>");
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
		// sa.setId(id);
		message = sa.lookFor();
		if(message.equals("")){
		    List<Auto> autos = sa.getAutos();
		    for(Auto auto:autos){
			str = auto.getId();
			all += "<tr><td><input type=\"checkbox\" "+
			    "name=\"delAutoAid\" value=\""+str+"\">*</td>";
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
			out.println(all);
		    }
		}
	    }
	    out.println("</table></td></tr>");
	}
	//
	// Comments
	//
	out.println("<tr><td><label for=\"comments\">Comments: </label><br/ >"+
		    "<textarea name=\"comments\" id=\"comments\" rows=\"5\" "+
		    " cols=\"70\" wrap=\"wrap\">"+comments+
		    "</textarea></td></tr>");
	out.println("</table>");
	out.println("</fieldset>");
	if(!id.equals("")){
	    out.println("<fieldset><legend>Related Department ");
	    out.println("<input name=\"action\" "+
			"type=\"button\" value=\"Add Related Department \" "+
			"tabindex=\"33\" onclick=\"window.open('"+url+
			"RelatedDeptServ?related_id="+id+
			"&opener=TortClaimServ"+
			"','Department','toolbar=0,location=0,"+
			"directories=0,status=0,menubar=0,"+
			"scrollbars=0,top=200,left=200,"+
			"resizable=1,width=500,height=400');return false;\" />");
	    out.println("</legend>");
	    List<RelatedDept> rdepts = tc.getRelatedDepts();
	    if(rdepts != null && rdepts.size() > 0){
		out.println("<table>");			
		out.println("<tr><th>&nbsp;</th><th>Department</th><th>Supervisor</th><th>Phone</th><th>Edit</th></tr>");
		int jj=1;
		for(RelatedDept rdept:rdepts){
		    out.println("<tr><td>"+(jj++)+" - <input type=\"checkbox\" name=\"delRelatedDept\" value=\""+rdept.getId()+"\" /></td><td>"+rdept+"</td><td>"+rdept.getSupervisor()+"</td><td>"+rdept.getPhone()+"</td><td>");
		    out.println("<input name=\"action\" "+
				"type=\"button\" value=\"Edit Department\" "+
				"tabindex=\"33\" onclick=\"window.open('"+url+
				"RelatedDeptServ?id="+rdept.getId()+"&related_id="+id+
				"&opener=TortClaimServ"+
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
	if(!subInsur.equals("")){
	    if(!id.equals("")){
		out.println("<fieldset><legend>Insurance ");
		out.println("<input name=\"action\" "+
			    "type=\"button\" value=\"Add Insurance\" "+
			    "tabindex=\"33\" onclick=\"window.open('"+
			    url+
			    "InsuranceServ?legalType=Tort"+
			    "&opener=TortClaimSer"+
			    "&type=City"+
			    "&relatedId="+id+
			    "','Insurance','toolbar=0,location=0,"+
			    "directories=0,status=0,menubar=0,"+
			    "scrollbars=0,top=200,left=200,"+
			    "resizable=1,width=600,height=500');return false;\" />");
		out.println("</legend>");
		InsuranceList il = new InsuranceList(debug);
		il.setRelatedId(id);
		il.lookFor();
		List<Insurance> ls = il.getInsurances();
		if(ls != null){

		    out.println("<table>");
		    for(Insurance insr: ls){
			Helper.printInsurance(out,insr,url,id,"Tort","TortClaimServ");
		    }
		    out.println("</table>");
		}
		out.println("</fieldset>");									
	    }
	}
	out.println("<fieldset><legend>Related Law Firm</legend>");
	out.println("<table>");
	out.println("<tr><td colspan=\"2\">Note:if the law firm is not in the list, you need to add it using \"Law Firm Search\" option from left side menu.</td></tr>");
	out.println("<tr><th width=\"20%\" align=\"left\">Law Firm</th>");
	out.println("<td align=\"left\">");
	if(firms != null && firms.size() > 0){
	    out.println("<select name=\"law_firm_id\" >");
	    out.println("<option value=\"\"></option>");
	    for(LawFirm one:firms){
		String selected = law_firm_id.equals(one.getId()) ? "selected=\"selected\"":"";
		out.println("<option value=\""+one.getId()+"\" "+selected+">"+one.getName()+"</option>");
	    }
	    out.println("</select>");
	}
	out.println("</td></tr>");
	out.println("</table></fieldset>");				
	out.println("<fieldset><legend>Damages and Charges</legend>");
	out.println("<table>");
	out.println("<tr><th>To Claimant</th><th>To City</th></tr>");
	out.println("<tr><td><fieldset><table class=\"wide\"><tr><td>");
	out.println("<label for=\"requestAmount\">Requested:</label></td><td>"+
		    "$<input name=\"requestAmount\" id=\"requestAmount\" value="+
		    "\""+requestAmount+"\" size=\"10\" maxlength=\"10\" "+
		    "onchange=\"checkNumber(this)\" />"+
		    "</td></tr><tr><td>");
	//
	//
	double totalPay = 0.0, total = 0.0;
	if(!paidByCity.equals("")){
	    try{
		totalPay = Double.valueOf(paidByCity).doubleValue();
	    }catch(Exception ex){}
	}
	if(!paidByRisk.equals("")){
	    try{
		totalPay += Double.valueOf(paidByRisk).doubleValue();
	    }catch(Exception ex){}
	}
	total = totalPay;
	if(!paidByInsur.equals("")){
	    try{
		totalPay += Double.valueOf(paidByInsur).doubleValue();
	    }catch(Exception ex){}
	}
	if(!miscByCity.equals("")){
	    try{
		total += Double.valueOf(miscByCity).doubleValue();
	    }catch(Exception ex){}
	}
	out.println("<label>Total Amount Paid:"+
		    "</label></td><td><span id=\"totalPay\">$"+
		    Helper.formatNumber(totalPay)+
		    "</span>");
	out.println("</td></tr><tr><td>");
	out.println("<label for=\"paidByCity\"> By City: </label>"+
		    "</td><td>$"+
		    "<input name=\"paidByCity\" id=\"paidByCity\" value=\""+
		    paidByCity+"\" size=\"10\" maxlength=\"10\" "+
		    "onchange=\"checkNumber(this)\" />");
	out.println("</td></tr><tr><td>");
	out.println("<label for=\"paidByRisk\"> By Risk: </label>"+
		    "</td><td>$"+
		    "<input name=\"paidByRisk\" id=\"paidByRisk\" value=\""+
		    paidByRisk+"\" size=\"10\" maxlength=\"10\" "+
		    "onchange=\"checkNumber(this)\" />");
	out.println("</td></tr><tr><td>");
	out.println("<label for=\"paidByInsur\"> By Insurance: </label>"+
		    "</td><td>$"+
		    "<input name=\"paidByInsur\" id=\"paidByInsur\" value=\""+
		    paidByInsur+"\" size=\"10\" maxlength=\"10\" "+
		    "onchange=\"checkNumber(this)\" />");
	out.println("</td></tr><tr><td>");
	out.println("<label for=\"deductible\"> Deductible: </label>"+
		    "</td><td>$"+
		    "<input name=\"deductible\" id=\"deductible\" value=\""+
		    deductible+"\" size=\"10\" maxlength=\"10\" "+
		    "onchange=\"checkNumber(this)\" />");
	out.println("</td></tr><tr><td>");
	out.println("<label for=\"miscByCity\">By City to Insurance:"+
		    "</label></td><td>$"+
		    "<input name=\"miscByCity\" id=\"miscByCity\" value=\""+
		    miscByCity+"\" size=\"10\" maxlength=\"10\" "+
		    "onchange=\"checkNumber(this)\" />");
	out.println("</td></tr><tr><td>");
	//
	// total = paidByCity + miscByCity
	//
	out.println("<label>Total Paid by City:"+
		    "</label></td><td><span id=\"totalByCity\">$"+
		    Helper.formatNumber(total)+"</span>");
	out.println("</td></tr></table></fieldset></td>");
	//
	// city table
	//
	out.println("<td><fieldset><table class=\"wide\"><tr><td>");
	out.println("<label for=\"cityTotalCost\">Total Cost:</label></td><td>"+
		    "$<input name=\"cityTotalCost\" id=\"cityTotalCost\" value="+
		    "\""+cityTotalCost+"\" size=\"10\" maxlength=\"10\" "+
		    "onchange=\"checkNumber(this)\" />");
	out.println("</td></tr><tr><td>&nbsp;"); // empty row
	out.println("</td></tr><tr><td>"); 
	out.println("<label for=\"paidByCity2City\"> By City: </label></td><td>"+
		    "$"+
		    "<input name=\"paidByCity2City\" id=\"paidByCity2City\" "+
		    "value=\""+paidByCity2City+"\" size=\"10\" maxlength=\"10\" "+
		    "onchange=\"checkNumber(this)\" />");
	out.println("</td></tr><tr><td>");
	out.println("<label for=\"paidByInsur2City\"> By Insurance: </label>"+
		    "</td><td>$"+
		    "<input name=\"paidByInsur2City\" id=\"paidByInsur2City\""+
		    " value=\""+
		    paidByInsur2City+"\" size=\"10\" maxlength=\"10\" "+
		    "onchange=\"checkNumber(this)\" />");
	out.println("</td></tr><tr><td>");
	out.println("<label for=\"deductible2\"> Deductible: </label>"+
		    "</td><td>$"+
		    "<input name=\"deductible2\" id=\"deductible2\" value=\""+
		    deductible2+"\" size=\"10\" maxlength=\"10\" "+
		    "onchange=\"checkNumber(this)\" />");
	// 2 empty rows
	out.println("</td></tr>"+
		    "<tr><td>&nbsp;</td></tr>"+
		    "<tr><td>&nbsp;</td></tr>"+
		    "</table></fieldset></td></tr>");
	//
	//
	out.println("</table>");
	out.println("</fieldset>");
	//
	out.println("<fieldset>");
	out.println("<table class=\"control\">");
	if(id.equals("")){  
	    out.println("<tr><td>");
	    out.println("<input type=\"submit\" tabindex=\"31\" "+
			"accesskey=\"s\" id=\"action\" value=\"Save\" "+
			"name=\"action\" class=\"submit\">");
	    out.println("</td></tr>"); 
	    out.println("</form>");
	}
	else{ // save, update
	    //
	    out.println("<tr>");
	    out.println("<td valign=top><input accesskey=\"u\" "+
			"tabindex=\"31\" "+
			"type=submit name=\"action\" id=\"action\" "+
			"class=\"submit\" value=\"Update\" />");
	    out.println("</td>");
	    //
	    out.println("<td><input type=\"button\" name=\"action\" "+
			"accesskey=\"n\" value=\"Insert Claimant/Witness\""+
			" tabindex=\"34\" onclick=\"document.location='"+
			url+"InsertPersonServ?inType=tort"+
			"&risk_id="+id+"'\" />");
	    out.println("</td>");
	    if(!status.equals("Closed")){
		out.println("<td>");
		out.println("<input type=\"button\" value=\"Claim Letter\" "+
			    "onclick=\"window.open('" + url + 
			    "ClaimLetterServ?id=" +id+"','Letter',"+
			    "'location=0:0,menubar=1,width=800,height=700,toolbar=1,scrollbars=1');\"></input>");  
		out.println("</td>");
	    }
	    out.println("<td valign=top><input "+
			"type=button name=\"action\" id=\"action\" "+
			"onclick=\"window.location='"+url+"RiskFileServ?risk_id="+id+"';\" "+
			"class=\"submit\" value=\"Upload File\" />");
	    out.println("</td>");								
	    out.println("<td>");
	    out.println("<input type=\"button\" value=\"Add Notes\" "+
			"onclick=\"window.open('" + url + 
			"NoteServ?risk_id="+id+"&opener=TortClaimServ','Notes',"+
			"'location=0:0,menubar=1,width=500,height=400,toolbar=1,scrollbars=1');return false;\"></input>");
	    out.println("</td>");
	    out.println("</tr>");
	    //
	    // Second row
	    //
	    out.println("<tr><td>");
	    out.println("<input type=\"button\" name=\"action\" "+
			"accesskey=\"n\" value=\"Worker Comp\""+
			" tabindex=\"35\" onclick=\"document.location='"+
			url+"WorkCompServ?tortId="+id+"&action=Generate"+
			"'\" />");
	    out.println("</td><td>");
	    out.println("<input type=\"button\" name=\"action\" "+
			"accesskey=\"n\" value=\"Internal Accident\""+
			" tabindex=\"36\" onclick=\"document.location='"+
			url+"SafetyServ?tortId="+id+"&action=Generate"+
			"'\" />");
	    //if(vsId.equals("")){
	    out.println("</td><td>");
	    out.println("<input type=\"button\" name=\"action\" "+
			"accesskey=\"n\" value=\"Recovery Actions\""+
			" tabindex=\"37\" onclick=\"document.location='"+
			url+"LegalServ?tortId="+id+"&action=Generate"+
			"'\" />");
	    //}
	    out.println("</form>");
	    out.println("</td><td>");
	    out.println("<form id=\"myForm2\" onsubmit=\"return "+
			"validateDelete();\">");
	    out.println("<input type=hidden name=\"id\" value=\""+
			id+"\">");
	    out.println("<input type=\"submit\" name=\"action\" "+
			"id=\"action\" accesskey=\"e\" tabindex=\"36\" "+
			" value=\"Delete\">");
	    out.println("</form>");
	    // 
	    out.println("</tr>");
	}
	out.println("</table>");
	out.println("</fieldset>");
	if(!id.equals("")){
	    if(tc.hasNotes()){
		Helper.printNotes(out,
				  url,
				  "TortClaimServ",
				  tc.getNotes());									
	    }
	    if(tc.hasFiles()){
		Helper.printFiles(out,
				  url,
				  tc.getFiles());									
	    }									
	    out.println("<center>* Click in check box to delete the item from this record </center>");
	}
	out.println("</div>");
	out.print("</body></html>");
	out.flush();
	out.close();
    }

}























































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

@WebServlet(urlPatterns = {"/AutoAccidentServ","/AutoAccident"})
public class AutoAccidentServ extends TopServlet{

    static Logger logger = LogManager.getLogger(AutoAccidentServ.class);
    String deptIdArr[] = null;
    String deptArr[] = null;
    List<RType> types = null;
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
	    type="", id="", status="",
	    empid="", userid="", supervisor="", empInjured="",
	    empName="", deptPhone="", dept_id="",  empDept="",
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
	    repairInfo="",
	    propDamage="",autoDamage="",workComp="",whatProp="", 
	    tortId="", vsId="", recordOnly="";
	String outOfDuty="";
	boolean connectDbOk = false, success = true;
       	String message = "", action = "",entry_time = "", 
	    entry_date = "", prevAction="", relatedId="";
	
	res.setContentType("text/html");
	PrintWriter out = res.getWriter();
	String name, value;
	Enumeration values = req.getParameterNames();
	String [] vals;
	String [] delAutoAid = null;
	String [] delEmp = null;
	HttpSession session = session = req.getSession(false);
	User user = null;
	if(session != null){
	    user = (User)session.getAttribute("user");
	}
	if(user == null){
	    res.sendRedirect(url+"Login");
	    return;
	}
	AutoAccident ds = new AutoAccident(debug);
	Auto newAuto = new Auto(debug);
		
	Estimate[] auto_est = {new Estimate(debug),
	    new Estimate(debug),
	    new Estimate (debug)};
		
	Estimate[] prop_est = {new Estimate(debug),
	    new Estimate(debug),
	    new Estimate (debug)};
	for(int i=0;i<3;i++){
	    auto_est[i].setType("Auto");
	    prop_est[i].setType("Prop");
	}
	String est_arr[] = {"","",""};
	String estp_arr[] = {"","",""};
	String[] clmntList = null;
	List<Employee> employees = null;
	List<Department> departments = null;
	Employee emp = new Employee(debug); // for new employee
	Department dp = new Department(debug);
	Department mainDept = new Department(debug);
	String [] delRelated = null;
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();	
	    value = Helper.replaceSpecialChars(value);

	    if (name.equals("type")) {
		ds.setType(value);
	    }
	    else if (name.equals("id")) {
		ds.setId(value);
		id = value;
	    }
	    else if (name.equals("relatedId")) {
		relatedId = value;
	    }
	    else if (name.equals("status")) {
		ds.setStatus(value);
	    }
	    else if (name.equals("delRelated")) {
		delRelated = vals; // array
	    }
	    else if (name.equals("deptContact")) {
		ds.setDeptContact(value);
	    }
	    else if(name.equals("delAutoAid")){
		delAutoAid = vals; // array
	    }
	    else if(name.equals("delEmp")){
		delEmp = vals; // array
	    }
	    else if (name.equals("autoDamage")) {
		ds.setAutoDamage(value);
	    }
	    else if (name.equals("propDamage")) {
		ds.setPropDamage(value);
	    }
	    else if (name.equals("autoPlate")) {
		newAuto.setAutoPlate(value);
	    }
	    else if (name.equals("dept_id")) {
		mainDept.setId(value);
		ds.setDept_id(value);
	    }
	    else if (name.equals("empDept")) {
		emp.setDept_id(value);
		dp.setId(value);
	    }
	    else if (name.equals("paidByCity")) {
		ds.setPaidByCity( value);
	    }
	    else if (name.equals("paidByRisk")) {
		ds.setPaidByRisk(value);
	    }
	    else if (name.equals("miscByCity")) {
		ds.setMiscByCity(value);
	    }
	    else if (name.equals("paidByInsur")) {
		ds.setPaidByInsur(value);
	    }
	    else if (name.equals("workComp")) {
		ds.setWorkComp(value);
	    }
	    else if (name.equals("whatProp")) {
		ds.setWhatProp(value);
	    }
	    else if (name.equals("repairInfo")) {
		ds.setRepairInfo(value);
	    }
	    else if (name.equals("otherType")) {
		ds.setOtherType(value);
	    }
	    else if (name.equals("deptName")) {
		dp.setName(value);
	    }
	    else if (name.equals("empInjured")) {
		ds.setEmpInjured(value);
	    }
	    else if (name.equals("subToInsur")) {
		ds.setSubToInsur(value);
	    }
	    else if (name.equals("autoPaid")) {
		ds.setAutoPaid(value);
	    }
	    else if (name.equals("propPaid")) {
		ds.setPropPaid(value);
	    }
	    else if (name.equals("vin")) {
		newAuto.setVin(value);
	    }
	    else if (name.equals("autoNum")) {
		newAuto.setAutoNum(value);
	    }
	    else if (name.equals("autoMake")) {
		newAuto.setAutoMake(value.toUpperCase());
	    }
	    else if (name.equals("autoModel")) {
		newAuto.setAutoModel(value.toUpperCase());
	    }
	    else if (name.equals("autoYear")) {
		newAuto.setAutoYear(value);
	    }
	    else if (name.equals("autoOwner")) {
		newAuto.setOwner(value);
	    }						
	    else if (name.equals("accidDate")) {
		ds.setAccidDate(value);
	    }
	    else if (name.equals("accidTime")) {
		ds.setAccidTime(value);
	    }
	    else if (name.equals("accidLocation")) {
		ds.setAccidLocation(value);
	    }
	    else if (name.equals("damage")) {
		ds.setDamage(value);
	    }
	    else if (name.equals("outOfDuty")) {
		ds.setOutOfDuty(value);
	    }	    
	    else if (name.equals("clmntMark")){ // array
		clmntList =vals;
	    }
	    else if (name.equals("est_arr")) {
		try{
		    int jj = Integer.parseInt(value);
		    auto_est[jj].setChosen("y");
		}catch(Exception ex){}
	    }
	    else if (name.equals("estp_arr")) {
		try{
		    int jj = Integer.parseInt(value);
		    prop_est[jj].setChosen("y");					
		}catch(Exception ex){}
	    }	
	    else if (name.equals("est_id")) {
		auto_est[0].setId(value);
	    }
	    else if (name.equals("est_id2")) {
		auto_est[1].setId(value);
	    }
	    else if (name.equals("est_id3")) {
		auto_est[2].setId(value);
	    }
	    else if (name.equals("estp_id")) {
		prop_est[0].setId(value);
	    }
	    else if (name.equals("estp_id2")) {
		prop_est[1].setId(value);
	    }
	    else if (name.equals("estp_id3")) {
		prop_est[2].setId(value);
	    }
	    else if (name.equals("reported")) {
		ds.setReported(value);
	    }
	    else if (name.equals("estPlace")) {
		auto_est[0].setCompany(value);
	    }
	    else if (name.equals("estPlace2")) {
		auto_est[1].setCompany(value);
	    }
	    else if (name.equals("estPlace3")) {
		auto_est[2].setCompany(value);
	    }
	    else if (name.equals("estPlaceP")) {
		prop_est[0].setCompany(value);
	    }
	    else if (name.equals("estPlaceP2")) {
		prop_est[1].setCompany(value);
	    }
	    else if (name.equals("estPlaceP3")) {
		prop_est[2].setCompany(value);
	    }
	    else if (name.equals("estCost")) {
		auto_est[0].setCost(value);
	    }
	    else if (name.equals("estCost2")) {
		auto_est[1].setCost(value);
	    }
	    else if (name.equals("estCost3")) {
		auto_est[2].setCost(value);
	    }
	    else if (name.equals("estCostP")) {
		prop_est[0].setCost(value);
	    }
	    else if (name.equals("estCostP2")) {
		prop_est[1].setCost(value);
	    }
	    else if (name.equals("estCostP3")) {
		prop_est[2].setCost(value);
	    }
	    else if (name.equals("totalCost")) {
		ds.setTotalCost(value);
	    }
	    else if (name.equals("totalCostP")) {
		ds.setTotalCostP(value);
	    }
	    else if (name.equals("chosenDealer")) {
		chosenDealer = value;
	    }
	    else if (name.equals("prevAction")){ 
		prevAction = value;  
	    }
	    else if (name.equals("action")){ 
		action = value;  
	    }
	    else if (name.equals("action2")){
		if(!value.equals(""))
		    action = value;  
	    }										
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
	    if(action.equals("zoom"))
		action = "";
	}
	//
	// for newly entered departments
	//
	if(mainDept.hasId()){
	    String back = mainDept.doSelect();
	    if(!back.equals("")){
		message += back;
		logger.error(back);
	    }
	}
	if(types == null){
	    MiscAccidentTypeList rtl = new MiscAccidentTypeList(debug);
	    String msg = rtl.lookFor();
	    if(msg.equals("")){
		types = rtl.getTypes();
	    }
	    else {
		logger.error(msg);
		message += msg;
		success = false;
	    }
	}
	if(departments == null){
	    DeptList dl = new DeptList(debug);
	    String msg = dl.lookFor();
	    if(msg.equals("")){
		departments = dl.getDepartments();
	    }
	    else {
		logger.error(msg);
		message += msg;
		success = false;
	    }
	}
	if(action.equals("Save")){
	    //
	    String back = "";
	    if(!dp.isEmpty()){
		if(dp.getId().equals("")){
		    dp.findDeptByName();
		}
		emp.setDepartment(dp);
	    }
	    if(!emp.isEmpty()){
		ds.setEmployee(emp);
	    }
	    for(int i=0;i<3;i++){
		if(auto_est[i] != null && !auto_est[i].isEmpty()){
		    ds.addAutoEstimate(auto_est[i]);
		}
		if(prop_est[i] != null && !prop_est[i].isEmpty()){
		    ds.addPropEstimate(prop_est[i]);
		}
	    }
	    //
	    back = ds.doSave();
	    if(back.equals("")){
		id = ds.getId();
		message += "Data saved successfully";
		// emp = new Employee(debug);
		dp = new Department(debug);
		if(!newAuto.isEmpty()){
		    newAuto.setId(id);
		    back = newAuto.doSave();
		    if(!back.equals("")){
			message += back;
			success = false;
			logger.error(back);
		    }
		    else{
			ds.add(newAuto);
		    }
		}
		if(!relatedId.equals("")){
		    Related rel = new Related(id,relatedId,"Auto Accidents", debug);
		    back = rel.doSave();
		    if(!back.equals("")){
			message += back;
			logger.error(back);
			success = false;
		    }	
		}
	    }
	    else{
		message += back;
		logger.error(back);
		success = false;
	    }
	}
	else if(action.equals("Update")){
	    //
	    // get the dept id first
	    //
	    String back;
	    if(!dp.isEmpty()){
		if(dp.getId().equals("")){
		    dp.findDeptByName();
		}
		// emp.setDepartment(dp);
	    }
	    if(!emp.isEmpty()){
		ds.setEmployee(emp);
	    }
	    ds.setAutoEstimates(auto_est);
	    ds.setPropEstimates(prop_est);
	    back = ds.doUpdate();
	    if(!back.equals("")){
		message += back;
		logger.error(back);
		success = false;
	    }
	    else{
		// emp = new Employee(debug);
		dp = new Department(debug);
		message += "Data updated successfully";
	    }
	    if(!newAuto.isEmpty()){
		newAuto.setRisk_id(id);
		back = newAuto.doSave();
		if(!back.equals("")){
		    message += back;
		    logger.error(back);
		    success = false;
		}
		else{
		    ds.add(newAuto);
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
		Related rel = new Related(id,relatedId,"Auto Accidents", debug);
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
	    if(clmntList != null){
		HandlePerson hp = new HandlePerson(debug);
		hp.setPidArr(clmntList);
		message += hp.doClaimantAutoDelete(id);
	    }						
	}
	else if(action.equals("Delete")){
	    //
	    // System.err.println("delete record");
	    //
	    String back = ds.doDelete();
	    if(!back.equals("")){
		message += back;
		logger.error(back);
		success = false;
	    }
	    else{
		emp = new Employee(debug);
		dp = new Department(debug);
	    }
	    //
	    // delete any auto related
	    //
	    Auto auto =new Auto(debug);
	    auto.doDeleteAllFor(id);
	    Related rel = new Related(id, debug);
	    rel.doDelete();
	    id="";
	}
	else if(!id.equals("")){	
	    //
	    String back = ds.doSelect();
	    if(!back.equals("")){
		message += back;
		logger.error(back);
		success = false;
	    }
	}
	empInjured = ds.getEmpInjured();
	autoDamage = ds.getAutoDamage();
	propDamage = ds.getPropDamage();
	subToInsur = ds.getSubToInsur();
	autoPaid = ds.getAutoPaid();
	propPaid = ds.getPropPaid();
	workComp = ds.getWorkComp();
	outOfDuty = ds.getOutOfDuty();
	if(action.equals("") && !prevAction.equals("")){
	    ds.doRefresh();
	}
	if(ds.getDepartment() != null){
	    mainDept = ds.getDepartment();
	}
	if(ds.getAutoEstimates() != null){
	    auto_est = ds.getAutoEstimates();
	    for(int i=0;i<auto_est.length;i++){
		if(auto_est[i].isChosen()){
		    est_arr[i]="checked=\"checked\"";
		}
	    }
	}
	if(ds.getPropEstimates() != null){
	    prop_est = ds.getPropEstimates();
	    for(int i=0;i<prop_est.length;i++){
		if(prop_est[i].isChosen()){
		    estp_arr[i]="checked=\"checked\"";
		}
	    }
	}
	if(!empInjured.equals("")) empInjured = "checked=\"checked\"";
	if(!autoDamage.equals("")) autoDamage = "checked=\"checked\"";
	if(!propDamage.equals("")) propDamage = "checked=\"checked\"";
	if(!subToInsur.equals("")) subToInsur = "checked=\"checked\"";
	if(!autoPaid.equals("")) autoPaid = "checked=\"checked\"";
	if(!propPaid.equals("")) propPaid = "checked=\"checked\"";
	if(!workComp.equals("")) workComp = "checked=\"checked\"";
	if(!outOfDuty.equals("")) outOfDuty = "checked=\"checked\"";
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
	out.println("<h3 class=\"titleBar\">Auto Accident "+id+"</h3>");
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
	if(departments != null){
	    out.println(" var deptName   = []; ");
	    out.println(" var deptPhone  = []; ");
	    out.println(" var deptDiv    = []; ");
	    for(Department dpp: departments){
		String idd = dpp.getId();
		out.println(" deptName["+idd+"]='"+dpp.getName()+"';");
		out.println(" deptDiv["+idd+"]='"+dpp.getDivision()+"';");
		out.println(" deptPhone["+idd+"]='"+dpp.getPhone()+"';");
	    }
	}
	out.println("  function resetDept(obj){	              ");
	out.println("  var ind = obj.selectedIndex;           ");
	out.println("  var id = obj.options[ind].value;      ");
	out.println("  if(id != ''){                         ");
        out.println(" document.getElementById('mDivision').innerHTML= deptDiv[id]; ");   
        out.println(" document.getElementById('mDeptPhone').innerHTML = deptPhone[id];");
	out.println("  }}                                      ");  
	//
	out.println("</script>                                ");
	out.println("<form id=\"myForm\" method=\"post\" "+
		    "onSubmit=\"return validateForm();\">");
	out.println("<input type=\"hidden\" name=\"action2\" value=\"\" id=\"action_id\" />");	
	if(!id.equals("")){
	    out.println("<input type=\"hidden\" name=\"id\" value=\""+id+"\" />");
	}
	out.println("<fieldset><legend>Status </legend>");
	out.println("<table>");

	out.println("<tr><td align=\"left\"><label for=\"status\">Status: </label>"+
		    "<select name=\"status\" id=\"status\">");
	for(int i=0;i<Inserts.statusArr.length;i++){
	    if(ds.getStatus().equals(Inserts.statusArr[i]))
		out.println("<option selected=\"selected\">"+ds.getStatus()+
			    "</option>");
	    else
		out.println("<option>"+Inserts.statusArr[i]+"</option>");
	}
	out.println("</select> &nbsp;&nbsp;");

	if(!action.equals("")){
	    out.println("<input type=\"hidden\" name=\"prevAction\" "+
			"value=\""+action+"\">");
	}
	out.println("</td></tr>");
	out.println("<tr><td><label for=\"type\">Type: </label>"+
		    "<select name=\"type\" id=\"type\">");
	out.println("<option value=\"\">&nbsp;</option>");
	if(types != null){
	    for(int i=0;i<types.size();i++){
		RType rtype = (RType)types.get(i);
		if(ds.getType().equals(rtype.getId()))
		    out.println("<option value=\""+rtype.getId()+
				"\" selected=\"selected\">"+rtype.getType()+
				"</option>");
		else
		    out.println("<option value=\""+rtype.getId()+"\">"+
				rtype.getType()+"</option>");
	    }
	}
	out.println("</select><label for=\"otherType\">Other (specify):"+
		    "<input name=\"otherType\" id=\"otherType\" "+
		    "value=\""+ds.getOtherType()+"\" "+
		    "size=\"25\" maxlength=\"30\" /></td></tr>");

	out.println("<tr><td align=\"left\"><label for=\"accidDate\">Accident; Date: "+
		    "</label>"+
		    "<input name=\"accidDate\" id=\"accidDate\" value="+
		    "\""+ds.getAccidDate()+"\" size=\"10\" maxlength=\"10\" "+
		    "onchange=\"checkDate(this)\" class=\"date\" />");
	out.println("<label for=\"accidTime\"> Time: </label>"+
		    "<input name=\"accidTime\" id=\"accidTime\" value="+
		    "\""+ds.getAccidTime()+"\" size=\"10\" maxlength=\"10\" />");
	out.println("</td></tr><tr><td align=\"left\">");
	out.println("<label for=\"reported\">Reported Date: </label>"+
		    "<input name=\"reported\" id=\"reported\" value="+
		    "\""+ds.getReported()+"\" size=\"10\" maxlength=\"10\" "+
		    "class=\"date\" onchange=\"checkDate(this)\" />");
					
	out.println("<input name=\"subToInsur\" id=\"subToInsur\" "+
		    "onchange=\"refreshPage()\" "+
		    "value=\"y\" type=\"checkbox\" "+subToInsur+" />");
	out.println("<label for=\"subToInsur\">Submitted to Insurance </label>");
	out.println("</td></tr>");

	out.println("<tr><td align=\"left\"><label for=\"damage\">Accident Details "+
		    "</label><br />");
	out.println("<textarea name=\"damage\" id=\"damage\" rows=\"5\" "+
		    "cols=\"70\" wrap=\"wrap\">");
	out.println(ds.getDamage());
	out.println("</textarea></td></tr>");
	out.println("<tr><td align=\"left\">");
	out.println("<label for=\"accidLocation\">Accident Location: </label>"+
		    "<input name=\"accidLocation\" id=\"accidLocation\" value="+
		    "\""+ds.getAccidLocation()+"\" size=\"60\" maxlength=\"80\" />");
	out.println("</td></tr>");
	out.println("</table>");
	out.println("</fieldset>");
	//
	out.println("<fieldset>");
	out.println("<legend>Related Records</legend>");		
	out.println("<tr><td>");
	out.println("In the field below enter the record ID of related Tort Claim, Recovery Action, Internal Accident, Worker\"s Comp, Natural Disaster, or Misc Accident");
	out.println("</td></tr>");
	out.println("<tr><td>");
	String related=""; // if we are coming from tort, or recovery 
	if(!vsId.equals("")){
	    related = vsId;
	}
	else if(!tortId.equals("")){
	    related = tortId;
	}
	out.println("<label for=\"relatedId\">Related Record ID:</label>"+
		    "<input name=\"relatedId\" id=\"relatedId\" value=\""+related+
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
	if(!id.isEmpty() && ds.hasClaimants()){
	    String str="", str2="", str3="",back="",all="";
	    List<RiskPerson> claimants = ds.getClaimants();
	    if(claimants != null){
		for(RiskPerson rp:claimants){
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
	    if(!all.isEmpty()){
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
			    "InsuranceServ?legalType=autoAccident"+
			    "&type=Claimant"+
			    "&relatedId="+id+
			    "&opener=AutoAccidentServ','Insurance','toolbar=0,location=0,"+
			    "directories=0,status=0,menubar=0,"+
			    "scrollbars=0,top=200,left=200,"+
			    "resizable=1,width=600,height=500');return false;\" />");
		out.println("</legend>");								
		InsuranceList il = new InsuranceList(debug);
		il.setRelatedId(id);
		il.setType("Claimant");
		il.lookFor();
		List<Insurance> ls = il.getInsurances();
		if(ls != null && ls.size() > 0){
		    out.println("<table>");
		    for(Insurance insr: ls){
			Helper.printInsurance(out,insr,url,id,"autoAccident","AutoAccidentServ");
		    }
		    out.println("</table>");
		}
		out.println("</fieldset>");									
	    }
	}
	//
	out.println("<fieldset><legend>Department </legend>");
	out.println("<table>");
	out.println("<tr><td><label>Department:</label></td><td>");
	out.println("<select name=\"dept_id\" id=\"dept_id\" onchange=\"resetDept(this)\">");
	out.println("<option value=\"\">&nbsp;</option>");
	if(departments != null && departments.size() > 0){
	    for(int i=0;i<departments.size();i++){
		Department dpp = (Department)departments.get(i);
		if(mainDept.getId().equals(dpp.getId()))
		    out.println("<option selected=\"selected\" value=\""+dpp.getId()+"\">"+dpp.getInfo()+"</option>");
		else
		    out.println("<option value=\""+dpp.getId()+"\">"+dpp.getInfo()+"</option>");
	    }
	}
	out.println("</select></td><td colspan=\"2\">");
	out.println("<input type=\"button\" value=\"Edit/Add New Dept\" "+
		    " onClick=\"window.open('" +url+
		    "DepartmentServ?id="+mainDept.getId()+
		    "','Department',"+
		    "'location=0,top=200,left=400,menubar=0,width=500,"+
		    "height=400');\" />");
	out.println("</td></tr>");
	out.println("<td><label>Division:</label></td><td>");
	out.println("<span id=\"mDivision\">"+mainDept.getDivision()+"&nbsp;</span></td></tr>");
	out.println("<tr><td><label>Dept. Phone:</label></td><td>");
	out.println("<span id=\"mDeptPhone\">"+mainDept.getPhone()+"&nbsp;</span></td>");
	out.println("<td><label for=\"deptContact\">Contact Person:</label></td><td>");
	out.println("<input type=\"text\" name=\"deptContact\" size=\"30\" id=\"deptContact\""+
		    "value=\""+ds.getDeptContact()+"\"></td></tr>");
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
	    out.println("<tr><td>&nbsp;"+
			"</td><td>");
	    out.println("&nbsp;&nbsp;</td><td>");			
	    out.println("<input name=\"action\" "+
			"type=\"button\" value=\"Add Employee\" "+
			"tabindex=\"33\" onClick=\"window.open('"+
			url+
			"GetEmpInfoServ?legalType=AutoAccident"+
			"&opener=AutoAccidentServ"+
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
		    Helper.printEmployee(out,empo,url,id,"AutoAccidentServ");
		}
	    }
	    out.println("</table>");			
	    out.println("</fieldset>");	
	}		
	//
	out.println("<fieldset><legend>Damages </legend>");
	out.println("<table>");
	out.println("<tr><td>");
	out.println("<input type=\"checkbox\" name=\"empInjured\" "+
		    "id=\"empInjured\" value=\"y\" "+empInjured+" />");
	out.println("<label for=\"empInjured\">Employee Injured</label>");
	out.println("</td></tr>");
	out.println("<tr><td>");
	out.println("<input type=\"checkbox\" name=\"outOfDuty\" "+
		    "id=\"outofduty\" value=\"y\" "+outOfDuty+" />");
	out.println("<label for=\"outofduty\">Accident out off duty time</label>");
	out.println("</td></tr>");	
	out.println("<tr><td>");
	out.println("<input type=\"checkbox\" name=\"workComp\" "+
		    "id=\"workComp\" value=\"y\" "+workComp+" />");
	out.println("<label for=\"workComp\">Worker\"s Comp?</label>");
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
		    "id=\"workComp\" value=\"y\" "+propDamage+" /> ");
	out.println("<label for=\"propDamage\">City Property Damaged?</label>");
	out.println("<label for=\"whatProp\">What Property:</label>");
	out.println("<input name=\"whatProp\" size=\"40\" maxlength=\"50\" "+
		    "id=\"whatProp\" value=\""+ds.getWhatProp()+"\">");
	out.println("</td></tr>");
	out.println("<tr><td><label for=\"repairInfo\">Repair Info</label>"+
		    "<br />");
	out.println("<textarea name=\"repairInfo\" rows=\"5\" cols=\"70\" "+
		    " wrap>"+ds.getRepairInfo());
	out.println("</textarea></td></tr>");
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
			"<td><label for=\"autoYear\">Year:</label></td>"+
			"<td><label for=\"owner\">Owner</td></tr>");
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
			"\" size=\"4\" maxlength=\"4\"/></td>");
	    out.println("<td><select name=\"autoOwner\" id=\"owner\">");
	    out.println("<option value=\"City\">City</option>");
	    out.println("<option value=\"Citizen\">Citizen</option>");
	    out.println("</select></td></tr>");
	    //
	    // Show existing Autos here
	    //
	    if(!id.isEmpty()){
		String str="", all="";
		AutoList sa = new AutoList(debug, id);
		message = sa.lookFor();
		if(message.isEmpty()){
		    List<Auto> list = sa.getAutos();
		    for(Auto auto:list){
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
			all += "<td>"+str+"</td>";
			str = auto.getOwner();
			all += "<td>"+str+"</td>";
			all+="</tr>";
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
	    out.println("<input type=\"radio\" name=\"est_arr\" value=\"0\" "+
			est_arr[0]+" />");
	    if(!auto_est[0].getId().equals("")){
		out.println("<input type=\"hidden\" name=\"est_id\" value=\""+
			    auto_est[0].getId()+"\" />");
	    }
	    out.println("</td><td>");
	    out.println("<input name=\"estPlace\" id=\"estPlace\" value=\""+
			auto_est[0].getCompany()+"\" size=\"30\" maxlength=\"50\" />");
	    out.println("</td><td>");
	    out.println("<input name=\"estCost\" id=\"estCost\" value=\""+
			auto_est[0].getCost()+"\" size=\"8\" maxlength=\"8\" />");
	    out.println("</td></tr>");
	    out.println("<tr><td>2-");
	    out.println("<input type=\"radio\" name=\"est_arr\" "+
			"value=\"1\" "+est_arr[1]+" />");
	    if(!auto_est[1].getId().equals("")){
		out.println("<input type=\"hidden\" name=\"est_id2\" value=\""+
			    auto_est[1].getId()+"\" />");
	    }
	    out.println("</td><td>");
	    out.println("<input name=\"estPlace2\" id=\"estPlace2\" value=\""+
			auto_est[1].getCompany()+"\" size=\"30\" maxlength=\"50\" />");
	    out.println("</td><td><input name=\"estCost2\" id=\"estCost2\" value=\""+
			auto_est[1].getCost()+"\" size=\"8\" maxlength=\"8\" />");
	    out.println("</td></tr>");
	    out.println("<tr><td>3-");
	    out.println("<input type=\"radio\" name=\"est_arr\" "+
			"value=\"2\" "+est_arr[2]+" />");
	    if(!auto_est[2].getId().equals("")){
		out.println("<input type=\"hidden\" name=\"est_id3\" value=\""+
			    auto_est[2].getId()+"\" />");
	    }
	    out.println("</td><td>");
	    out.println("<input name=\"estPlace3\" id=\"estPlace3\" value=\""+
			auto_est[2].getCompany()+"\" size=\"30\" maxlength=\"50\" />");
	    out.println("</td><td>");
	    out.println("<input name=\"estCost3\" id=\"estCost3\" value=\""+
			auto_est[2].getCost()+"\" size=\"8\" maxlength=\"8\" />");
	    out.println("</td></tr>");
	    out.println("<tr><td><label for=\"totalCost\">Total Cost:</label>");
	    out.println("</td><td colspan=\"3\">");
	    out.println("<input name=\"totalCost\" id=\"totalCost\" value=\""+
			ds.getTotalCost()+"\" size=\"8\" maxlength=\"10\" />");
	    out.println("<input name=\"autoPaid\" id=\"autoPaid\" value=\"y"+
			"\" type=\"checkbox\" "+autoPaid+" /> <label>Paid </label>");
	    out.println("</td></tr>");
	    out.println("</table></td></tr>");
	    out.println("</td></tr></table>");
	    out.println("</fieldset>");
	}
	if(!propDamage.equals("")){
	    out.println("<fieldset><legend>Property Repair</legend>"+
			"<table>");
	    out.println("<tr><th>Selected</th><th>Company</th><th>Cost "+
			"Estimate $</th></tr>");
	    out.println("<tr><td>1-");
	    out.println("<input type=\"radio\" name=\"estp_arr\" "+
			"value=\"0\" "+estp_arr[0]+" />");
	    if(!prop_est[0].getId().equals("")){
		out.println("<input type=\"hidden\" name=\"estp_id\" value=\""+
			    prop_est[0].getId()+"\" />");
	    }
	    out.println("</td><td>");
	    out.println("<input name=\"estPlaceP\" id=\"estPlaceP\" value=\""+
			prop_est[0].getCompany()+"\" size=\"30\" maxlength=\"50\" />");
	    out.println("</td><td>");
	    out.println("<input name=\"estCostP\" id=\"estCostP\" value=\""+
			prop_est[0].getCost()+"\" size=\"8\" maxlength=\"8\" />");
	    out.println("</td></tr>");
	    out.println("<tr><td>2-");
	    out.println("<input type=\"radio\" name=\"estp_arr\" "+
			"value=\"1\" "+estp_arr[1]+" />");
	    if(!prop_est[1].getId().equals("")){
		out.println("<input type=\"hidden\" name=\"estp_id2\" value=\""+
			    prop_est[1].getId()+"\">");
	    }
	    out.println("</td><td>");
	    out.println("<input name=\"estPlaceP2\" id=\"estPlaceP2\" value=\""+
			prop_est[1].getCompany()+"\" size=\"30\" maxlength=\"50\" />");
	    out.println("</td><td>");
	    out.println("<input name=\"estCostP2\" id=\"estCostP2\" value=\""+
			prop_est[1].getCost()+"\" size=\"8\" maxlength=\"8\" />");
	    out.println("</td></tr>");
	    out.println("<tr><td>3-");
	    out.println("<input type=\"radio\" name=\"estp_arr\" "+
			"value=\"2\" "+estp_arr[2]+" />");
	    if(!prop_est[2].getId().equals("")){
		out.println("<input type=\"hidden\" name=\"estp_id3\" value=\""+
			    prop_est[2].getId()+"\">");
	    }
	    out.println("</td><td>");
	    out.println("<input name=\"estPlaceP3\" id=\"estPlaceP3\" value=\""+
			prop_est[2].getCompany()+"\" size=\"30\" maxlength=\"50\" />");
	    out.println("</td><td>");
	    out.println("<input name=\"estCostP3\" id=\"estCostP3\" value=\""+
			prop_est[2].getCost()+"\" size=\"8\" maxlength=\"8\" />");
	    out.println("</td></tr>");
	    out.println("<tr><td><label for=\"totalCost\">Total Cost:</label>");
	    out.println("</td><td colspan=\"3\">");
	    out.println("<input name=\"totalCostP\" id=\"totalCostP\" value=\""+
			ds.getTotalCostP()+"\" size=\"8\" maxlength=\"10\" />, ");

	    out.println("<input name=\"propPaid\" id=\"propPaid\" value=\"y"+
			"\" type=\"checkbox\" "+propPaid+" />Yes.");
	    out.println("<label for=\"propPaid\">Paid </label>");						
	    out.println("</td></tr></table></td></tr>");
	    out.println("</td></tr></table>");
	    out.println("</fieldset>");
	}
	//
	// Insurance info
	//		
	if(!subToInsur.equals("")){
	    if(!id.equals("")){
		out.println("<fieldset><legend>Insurance ");
		out.println("<input name=\"action\" "+
			    "type=\"button\" value=\"Add Insurance Info\" "+
			    "tabindex=\"33\" onClick=\"window.open('"+
			    url+
			    "InsuranceServ?legalType=AutoAccident"+
			    "&opener=AutoAccidentServ"+
			    "&type=City&relatedId="+id+
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
			Helper.printInsurance(out,insr,url,id,"AutoAccident","AutoAccidentServ");
		    }
		    out.println("</table>");
		}
		out.println("</fieldset>");					
	    }
	}
	out.println("<fieldset><legend>Charges</legend>");
	out.println("<table>");
	out.println("<tr><td>");
	out.println("<label for=\"paidByInsur\">Amount Paid By City Insurance:"+
		    "$</label>"+
		    "<input name=\"paidByInsur\" id=\"paidByInsur\" value=\""+
		    ds.getPaidByInsur()+"\" size=\"10\" maxlength=\"10\" />");
	out.println("</td></tr>");
	out.println("<tr><td>");
	out.println("<label for=\"paidByCity\">Amount Paid By City: $</label>");
	out.println("<input name=\"paidByCity\" size=\"10\" maxlength=\"10\" "+
		    "id=\"paidByCity\" value=\""+ds.getPaidByCity()+"\" />");
	out.println("<label for=\"paidByRisk\"> Paid By Risk: $</label>");
	out.println("<input name=\"paidByRisk\" size=\"10\" maxlength=\"10\" "+
		    "id=\"paidByRisk\" value=\""+ds.getPaidByRisk()+"\" />");
	out.println("<label for=\"miscByCity\"> Misc Paid By City: $</label>");
	out.println("<input name=\"miscByCity\" size=\"10\" maxlength=\"10\" "+
		    "id=\"miscByCity\" value=\""+ds.getMiscByCity()+"\" />");
	out.println("</td></tr>");
	out.println("</table>");
	out.println("</fieldset>");
	//
	if(action.startsWith("Print")){
	    //
	    // Nothing here
	    //
	}
	else{
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
		out.println("<td valign=\"top\"><input accesskey=\"u\" "+
			    "tabindex=\"31\" "+
			    "type=\"submit\" name=\"action\" id=\"action\" "+
			    "class=\"submit\" value=\"Update\" />");
		out.println("</td>");
		out.println("<td><input type=\"button\" name=\"action\" "+
			    "accesskey=\"n\" value=\"Add Claimant\""+
			    " tabindex=\"34\" onclick=\"document.location='"+
			    url+"InsertPersonServ?inType=auto"+
			    "&risk_id="+id+"'\" />");
		out.println("</td>");								
		out.println("<td>");
		out.println("<input name=\"action\" accesskey=\"y\" "+
			    "type=\"button\" value=\"Payment\" "+
			    "tabindex=\"32\" onClick=\"window.open('"+
			    url+
			    "PaymentServ?type=safety"+
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
			    "NoteServ?risk_id="+id+"&opener=AutoAccidentServ','Notes',"+
			    "'location=0:0,menubar=1,width=500,height=400,toolbar=1,scrollbars=1');return false;\"></input>");
		out.println("</form></td>");
		out.println("<td><form id=\"myForm2\" onsubmit=\"return "+
			    "validateDelete();\">");
		out.println("<input type=hidden name=\"id\" value=\""+
			    id+"\" />");
		out.println("<input type=\"submit\" name=\"action\" "+
			    "id=\"action\" accesskey=\"e\" tabindex=\"33\" "+
			    " value=\"Delete\" />");
		out.println("</form>");
		out.println("</td></tr></table>");
	    }
	}
	out.println("</fieldset>");
	if(!id.equals("")){
	    if(ds.hasNotes()){
		Helper.printNotes(out,
				  url,
				  "AutoAccidentServ",
				  ds.getNotes());									
	    }
	    if(ds.hasFiles()){
		Helper.printFiles(out,
				  url,
				  ds.getFiles());									
	    }							
	    out.println("<center>* Click in check box to delete the item from this record </center>");
	}
	out.println("</div>");
	out.print("</body></html>");
	out.flush();
	out.close();
    }
}























































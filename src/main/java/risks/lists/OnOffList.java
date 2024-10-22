package risks.lists;
/**
 * @copyright Copyright (C) 2014-2022 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.sql.*;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.models.*;
import risks.utils.*;


public class OnOffList{

    boolean debug = false;
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    static Logger logger = LogManager.getLogger(OnOffList.class);
	
    String type = "", // all, auto, legal, safety, misc
	dept_id="",whichDate="", status="",
	dateFrom="", dateTo="";
	
    String onOffDuty = ""; //all, on, off
    String orderBy="d.id desc";
    Set<String> typeSet = new HashSet<>();
    String errors = "";
    
    List<AutoAccident> autos = null;
    List<Legal> legals = null;
    List<Safety> safetys = null;
    List<MiscAccident> miscs = null;
    // type,ud,status, incident date, employee, onOffDuty 
    List<List<String>> combined = null;
    public OnOffList(boolean deb){
	debug = deb;
    }
    //
    // setters
    //
    public void setType(String[] vals){
	if(vals != null){
	    for(String str:vals){
		typeSet.add(str);
	    }
	}
    }
    public void setDept_id(String val){
	if(val != null)
	    dept_id = val;
    }
    public void setStatus(String val){
	if(val != null)
	    status = val;
    }    
    public void setWhichDate(String val){
	if(val != null)
	    whichDate = val;
    }
    public void setDateFrom(String val){
	if(val != null)
	    dateFrom = val;
    }
    public void setDateTo(String val){
	if(val != null)
	    dateTo = val;
    }
    public void setOrderBy(String val){
	if(val != null)
	    orderBy = val;
    }
    public void setOnOffDuty(String val){
	if(val != null)
	    onOffDuty = val;
    }
    public List<List<String>> getLists(){
	return combined;
    }
    public String find(){
	String back = "";
	if(typeSet.size() == 0){
	    findAuto();
	    findSafety();
	    findMisc();
	    findLegal();
	}
	else if(typeSet.contains("auto")){
	    findAuto();
	}
	else if(typeSet.contains("legal")){
	    findLegal();
	}
	else if(typeSet.contains("safety")){
	    findSafety();
	}
	else if(typeSet.contains("misc")){
	    findMisc();
	}	
	return back;
    }
     void findAuto(){
	 AutoAccidentList aal = new AutoAccidentList(debug);
	 aal.setStatus(status);
	 aal.setDateFrom(dateFrom);
	 aal.setDateTo(dateTo);
	 aal.setWhichDate("d.accidDate");
	 aal.setOnOffDuty(onOffDuty);
	 String back = aal.lookFor();
	 if(back.isEmpty()){
	     autos = aal.getAutoAccidents();
	     if(autos != null && autos.size() > 0){
		 if(combined == null)
		     combined = new ArrayList<>();
		 for(AutoAccident one:autos){
		     List<String> ll = new ArrayList<>();
		     ll.add("AutoAccident");
		     ll.add(one.getId());
		     ll.add(one.getStatus());
		     ll.add(one.getAccidDate());
		     if(one.isOffDuty())
			 ll.add("Out Off Duty");
		     else ll.add("");
		     ll.add(one.getEmployeeNames());
		     ll.add(one.getDeptNames());		     
		     combined.add(ll);
		 }
	     }
	 }
	 
    }
     void findLegal(){
	 LegalList aal = new LegalList(debug);
	 aal.setStatus(status);
	 aal.setDateFrom(dateFrom);
	 aal.setDateTo(dateTo);
	 aal.setWhichDate("l.doi");
	 aal.setOnOffDuty(onOffDuty);
	 String back = aal.lookFor();
	 if(back.isEmpty()){
	     legals = aal.getLegals();
	     if(legals != null && legals.size() > 0){
		 if(combined == null)
		     combined = new ArrayList<>();
		 for(Legal one:legals){
		     List<String> ll = new ArrayList<>();
		     ll.add("Recovery Action");
		     ll.add(one.getId());
		     ll.add(one.getStatus());
		     ll.add(one.getDoi());
		     if(one.isOffDuty())
			 ll.add("Out Off Duty");
		     else ll.add("");		     
		     ll.add(one.getEmployeeNames());
		     ll.add(one.getDeptNames());		     
		     combined.add(ll);
		 }
	     }
	     
	 }
    }
    void findSafety(){
	 SafetyList aal = new SafetyList(debug);
	 aal.setStatus(status);
	 aal.setDateFrom(dateFrom);
	 aal.setDateTo(dateTo);
	 aal.setWhichDate("accidDate");
	 aal.setOnOffDuty(onOffDuty);
	 String back = aal.lookFor();
	 if(back.isEmpty()){
	     safetys = aal.getSafetyList();
	     if(safetys != null && safetys.size() >0){
		 if(combined == null)
		     combined = new ArrayList<>();
		 for(Safety one:safetys){
		     List<String> ll = new ArrayList<>();
		     ll.add("Internal");
		     ll.add(one.getId());
		     ll.add(one.getStatus());
		     ll.add(one.getAccidDate());
		     if(one.isOffDuty())
			 ll.add("Out Off Duty");
		     else ll.add("");
		     ll.add(one.getEmployeeNames());
		     ll.add(one.getDeptNames());		     
		     combined.add(ll);
		     
		 }
	     }
	 }
    }
    void findMisc(){
	 MiscAccidentList aal = new MiscAccidentList(debug);
	 aal.setStatus(status);
	 aal.setDateFrom(dateFrom);
	 aal.setDateTo(dateTo);
	 aal.setWhichDate("d.accidDate");
	 aal.setOnOffDuty(onOffDuty);
	 String back = aal.lookFor();
	 if(back.isEmpty()){
	     miscs = aal.getMiscAccidents();
	     if(miscs != null && miscs.size() > 0){
		 if(combined == null)
		     combined = new ArrayList<>();
		 for(MiscAccident one:miscs){
		     List<String> ll = new ArrayList<>();
		     ll.add("Misc");
		     ll.add(one.getId());
		     ll.add(one.getStatus());
		     ll.add(one.getAccidDate());
		     if(one.isOffDuty())
			 ll.add("Out Off Duty");
		     else ll.add("");		     
		     ll.add(one.getEmployeeNames());
		     ll.add(one.getDeptNames());
		     combined.add(ll);
		 }
	     }
	 }
    }
    //
    public String getErrors(){
	return errors;
    }
    //	
}

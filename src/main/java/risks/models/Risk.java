package risks.models;

import java.util.*;
import java.sql.*;
import java.io.*;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.utils.Helper;
import risks.lists.*;


public class Risk{

    boolean debug;
    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");		
    static Logger logger = LogManager.getLogger(Risk.class);
    String id="";
    List<Note> notes = null;
    List<RiskFile> files = null;
    //
    public Risk(){

    }
    public Risk(boolean deb, String val){

	debug = deb;
	setId(val);

    }
    public     void setId(String val){
	if(val != null)
	    id = val;
    }
    //
    // getters
    //
    public     String  getId(){
	return id;
    }
    public     List<Note> getNotes(){
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
    public     boolean hasNotes(){
	getNotes();
	return notes != null && notes.size() > 0;
    }
    public     List<RiskFile> getFiles(){
	if(files == null && !id.equals("")){
	    RiskFileList sp = new RiskFileList(debug, id);
	    String back = sp.find();
	    if(back.equals("")){
		List<RiskFile> ones = sp.getFiles();
		if(ones != null &&  ones.size() > 0){
		    files = ones;
		}
	    }
	}
	return files;
    }
    public     boolean hasFiles(){
	getFiles();
	return files != null && files.size() > 0;
    }				
		
    public String toString(){
	return id;
    }
    public boolean equals(Object o) {
	if (o instanceof Risk) {
	    Risk c = (Risk) o;
	    if ( this.id.equals(c.getId())) 
		return true;
	}
	return false;
    }
    public int hashCode(){
	int seed = 31;
	if(!id.equals("")){
	    try{
		seed += Integer.parseInt(id)*37;
	    }catch(Exception ex){
		// we ignore
	    }
	}
	return seed;
    }				

}























































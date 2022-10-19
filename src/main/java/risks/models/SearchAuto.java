package risks.models;

import java.util.*;
import java.sql.*;
import java.io.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


class SearchAuto extends Auto{

    String aidArr[] = null;
    String orderBy = "";
    static Logger logger = LogManager.getLogger(SearchAuto.class);
    List<Auto> list = null;
    //
    // basic constructor
    public SearchAuto(boolean deb){

	super(deb);

    }
    public void setOrderBy(String val){
	orderBy = val;

    }
    public List<Auto> getList(){
	return list;
    }
    //
    // getters
    //
    public String[] getAidArr(){
	return aidArr;
    }
    //

}























































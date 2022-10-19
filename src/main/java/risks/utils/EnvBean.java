package risks.utils;


public class EnvBean {

    String url = "", principle = "", method="", password = "", ctxFactory = "";

    public EnvBean(){};
    /**
     * setters
     */
    public void setUrl(String val){
	if(val != null)
	    url = val;
    }
    public void setPrinciple(String val){
	if(val != null)
	    principle = val;
    }	
    public void setMethod(String val){
	if(val != null)
	    method = val;
    }
    public void setPassword(String val){
	if(val != null)
	    password = val;
    }
    public void setCtxFactory(String val){
	if(val != null)
	    ctxFactory = val;
    }
    /**
     * getters
     */
    public String getUrl(){
	return url;
    }
    public String getPrinciple(){
	return principle;
    }	
    public String getMethod(){
	return method;
    }
    public String getPassword(){
	return password;
    }
    public String getCtxFactory(){
	return ctxFactory;
    }
    public String toString(){
	String ret = url+" "+principle+" "+password;;
	return ret;
    }
    

}

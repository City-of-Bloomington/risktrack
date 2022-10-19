package risks.utils;

public class Configuration {

    String auth_end_point = "";
    String token_end_point = "";
    String callback_uri = "";
    String client_id = "";
    String client_secret = "";
    String scope="openid";		
    String dicovery_uri = "";
    String username = "";

    public Configuration(String val, String val2, String val3, String val4,
		  String val5, String val6, String val7, String val8){
	setAuthEndPoint(val);
	setTokenEndPoint(val2);
	setCallbackUri(val3);
	setClientId(val4);
	setClientSecret(val5);
	setScope(val6);				
	setDicoveryUri(val7);
	setUsername(val8);
	
    }
    public void setAuthEndPoint(String val){
	if(val != null)
	    auth_end_point = val;
    }
    public void setTokenEndPoint(String val){
	if(val != null)
	    token_end_point = val;
    }
    public void setCallbackUri(String val){
	if(val != null)
	    callback_uri = val;
    }
    public void setClientId(String val){
	if(val != null)
	    client_id = val;
    }
    public void setClientSecret(String val){
	if(val != null)
	    client_secret = val;
    }
    public void setScope(String val){
	if(val != null)
	    scope = val;
    }
    public void setUsername(String val){
	if(val != null)
	    username = val;
    }		
    public void setDicoveryUri(String val){
	if(val != null)
	    dicovery_uri = val;
    }
    public String getAuthEndPoint(){
	return auth_end_point;
    }
    public String getTokenEndPoint(){
	return token_end_point;
    }
    public String getCallbackUri(){
	return callback_uri;
    }
    public String getClientId(){
	return client_id;
    }
    public String getClientSecret(){
	return client_secret;
    }
    public String getScope(){
	return scope;
    }
    public String getUsername(){
	return username;
    }		
    public String getDicoveryUri(){
	return dicovery_uri;
    }
    public String toString(){
	String ret = "";
	ret += " auth_end_point: "+auth_end_point;
	ret += " token_end_point: "+token_end_point;
	ret += " callback_uri: "+callback_uri;
	ret += " client_id: "+client_id;
	ret += " client_secret: "+client_secret;
	ret += " scope: "+scope;
	ret += " username: "+username;
	return ret;
    }
}




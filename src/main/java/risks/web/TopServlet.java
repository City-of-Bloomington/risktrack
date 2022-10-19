package risks.web;
import com.nimbusds.oauth2.sdk.*;
import com.nimbusds.oauth2.sdk.id.*;
import com.nimbusds.oauth2.sdk.token.*;
import com.nimbusds.openid.connect.sdk.Nonce;
import java.net.URI;
import javax.servlet.ServletContext;
import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risks.models.*;
import risks.lists.*;
import risks.utils.*;

public class TopServlet extends HttpServlet {
    static String url = "";
    static boolean debug = false;
    static Configuration config = null;
    static Logger logger = LogManager.getLogger(TopServlet.class);
    static ServletContext context = null;
    public void init(ServletConfig conf){
	try{
	    context = conf.getServletContext();
	    url = context.getInitParameter("url");
	    String adfs_username = context.getInitParameter("adfs_username");
	    String auth_end_point = context.getInitParameter("auth_end_point");
	    String token_end_point = context.getInitParameter("token_end_point");
	    String callback_uri = context.getInitParameter("callback_uri");
	    String client_id = context.getInitParameter("client_id");
	    String client_secret = context.getInitParameter("client_secret");
	    String scope= conf.getInitParameter("scope");
	    String discovery_uri = context.getInitParameter("discovery_uri");
	    String str = context.getInitParameter("debug");
	    if(str != null && str.equals("true")) debug = true;
	    config = new Configuration(auth_end_point, token_end_point, callback_uri, client_id, client_secret, scope, discovery_uri, adfs_username);
	}catch(Exception ex){
	    System.err.println(" top init "+ex);
	    logger.error(" "+ex);
	}
    }				

}

package risks.models;

import java.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class OidcUser {

    private final String accessToken;
    private final String idToken;
    private final JSONObject accessTokenPayload;
    private final JSONObject idTokenPayload;

    public OidcUser(String accessToken, JSONObject accessTokenPayload, String idToken, JSONObject idTokenPayload) {
        this.accessToken = accessToken;
        this.idToken = idToken;
        this.idTokenPayload = idTokenPayload;
        this.accessTokenPayload = accessTokenPayload;
    }

    public String getAccessToken() {
        return accessToken;
    }
    public String getIdToken() { return idToken; }
    public JSONObject getIdTokenPayload() { return idTokenPayload; }
    public JSONObject getAccessTokenPayload() { return accessTokenPayload; }

    public String getName() {
        try {
            return idTokenPayload.getString("name");
        } catch (Exception e) {
            return "No name (missing scope-access?)";
        }
   }

}



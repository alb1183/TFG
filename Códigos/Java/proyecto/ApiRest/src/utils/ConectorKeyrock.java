package utils;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

import org.json.JSONObject;

import modelo.AuthUser;
import modelo.Token;


public class ConectorKeyrock {
	private String KEYROCK_IP = "localhost";
	private String KEYROCK_PORT = "3500";
	
	
	public Token authenticateById(String id, String password) throws IdMUserException {		
		Token token = null;
    	try{    		
	    	/* 1. Solicitar la creación de token (formato JSON) */
    		AuthUser user = new AuthUser(id, password);
			String authenticationV3JSON = new JSONObject(user).toString();
			
			/* 3. Enviar solicitud de token */
			String httpURL = "http://" + KEYROCK_IP + ":" + KEYROCK_PORT + "/v3/auth/tokens";
    		HttpURLConnection connection = obtainConnection(httpURL, HttpVerbs.POST, authenticationV3JSON);
    		int responseCode = connection.getResponseCode();
			if(responseCode == 201){
				//OBTENER CABECERA X-Subject-Token
				String token_header = connection.getHeaderField("X-Subject-Token");
				if(token_header != null){
					System.out.println("The token for entity with id=\"" + id + "\" has been created.");
					token = new Token(token_header);
				}else{
					throw new IdMUserException("The token header for entity with id=\"" + id + "\" is NULL");
				}				
			}else{
				//throw new IdentityManagementException("The token for entity with id=\"" + id + "\" has NOT been created.");
				System.out.println("The token for entity with id=\"" + id + "\" has NOT been created.");
			}
    	} catch (UnknownHostException e) {
    		throw new IdMUserConnectivityException("No connectivity.");
    	} catch (Exception e) {
    		throw new IdMUserException(e.getMessage());
		}
		return token;
	}
	
	
	private HttpURLConnection obtainConnection(String url, HttpVerbs method, String body) throws Exception{

    	URL myurl = new URL(url);
    	HttpURLConnection conn;

		conn = (HttpURLConnection)myurl.openConnection();

	    conn.setRequestProperty("Accept", "application/json");
	    conn.setRequestProperty("Content-Type", "application/json");
	    
	    if(method.name().equals(HttpVerbs.GET.name())){
	    	conn.setRequestMethod("GET");
    	}else if(method.name().equals(HttpVerbs.DELETE.name())){
    		conn.setRequestMethod("DELETE");    		
    	}else if(method.name().equals(HttpVerbs.POST.name())){
    		conn.setRequestMethod("POST");
    		conn.setUseCaches(false);
    		conn.setDoInput(true);
    		conn.setDoOutput(true);
    		DataOutputStream wr = new DataOutputStream (conn.getOutputStream ());
    		wr.writeBytes (body);
         	wr.flush ();
         	wr.close ();
    	}else{
    		conn.setRequestMethod("PUT");
    		conn.setUseCaches(false);
    		conn.setDoInput(true);
    		conn.setDoOutput(true);
    		DataOutputStream wr = new DataOutputStream (conn.getOutputStream ());
    		wr.writeBytes (body);
         	wr.flush ();
         	wr.close ();
    	}
    	
    	return conn;
    }
}

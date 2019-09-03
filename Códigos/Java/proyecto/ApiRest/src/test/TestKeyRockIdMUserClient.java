package test;

import modelo.Token;
import utils.ConectorKeyrock;

public class TestKeyRockIdMUserClient {

	public static void main(String[] args) {
		
		try { 
    		System.out.println("##### USER AUTHENTICATION BY ID AND PASSWORD #####");
    		ConectorKeyrock identityManager = new ConectorKeyrock();
    		Token token_by_id = identityManager.authenticateById("alber1183@hotmail.com", "1234");
    		if(token_by_id != null)
    			System.out.println("\tTOKEN: " + token_by_id.getToken_id());
    		    		
		} catch (Exception e) {
			e.printStackTrace();
		}    	
    }
	
}

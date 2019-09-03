package stub;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

public class Database {
	
	private Map<String, String> tokens;
	
	public Database() {
		tokens = new HashMap<>();
	}
	
	public void addToken(String hash, String token) {
		tokens.put(hash, token);
	}
	
	public void removeToken(String token) {
		if(checkToken(token)) {
			for (Entry<String, String> entry : tokens.entrySet()) {
	            if (entry.getValue().equals(token)) {
	                tokens.remove(entry.getKey());
	                return;
	            }
	        }
		}
	}
	
	public String getToken(String hash) {
		if(tokens.containsKey(hash))
			return tokens.get(hash);
		
		String token = UUID.randomUUID().toString();
		addToken(hash, token);
		return token;
	}
	
	public boolean checkToken(String token) {
		return tokens.containsValue(token);
	}

}

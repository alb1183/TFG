package modelo;

public class Transaction {
	private String transactionId;
	private String cid;
	private String hash;
	private String firmaAPI;
	private String firmaUser;
	private String timestamp;
	
	
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getFirmaAPI() {
		return firmaAPI;
	}
	public void setFirmaAPI(String firmaAPI) {
		this.firmaAPI = firmaAPI;
	}
	public String getFirmaUser() {
		return firmaUser;
	}
	public void setFirmaUser(String firmaUser) {
		this.firmaUser = firmaUser;
	}
}

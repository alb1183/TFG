package rest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import alice.Alice;
import alice.AliceContextBuilder;
import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;
import modelo.Transaction;
import utils.Database;
import utils.Certificados;
import utils.ConectorREST;

@Path("/")
@Produces("application/json")
public class IoTRestApp {
	private IPFS ipfs;
	private ConectorREST conectorRest;
	private Alice alice;
	private static Database database = new Database();
	// Nombre de la transaccion
	private static final String TRANSACTION_NAME = "IoTTransaction";
	// Password hardcoded para el cifrado simetrico desde el proxy
	private static final String PASSWORD = "2019";
	// Token JWT de autenticación del proxy con Composer
	private static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0aW1lc3RhbXAiOjE1NTU1ODczNTgsInVzZXJuYW1lIjoiYWxiZXJ0byJ9.3SKZabM6pu5Ig6-zx_vA4Oy_QG6r2mi3dgoT8uLoYl0";
	// Clave privada del certificado del proxy en formato PEM
	private static final String CLAVEPRIVADA = 	"MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQC5T6DqhLzIpfKc"+
												"Ak6iq2XxKZ6SwWmE47RhJejcVvLeNxvvN7F1RqffNGyeai5yj8muHinxXF+QBTYB"+
												"ZUaAmINJNnkAD0AUSbl85EwYjQ42G5XbyqaKH6IXxlgcJ+xxYjtMBhNPzj80g5Fh"+
												"ZhM9MhUvgRLG5DLC/ukMBynJWdjXjhqoXOA1BcpSLiS+YmPHZe62HZsPs6fp7QGM"+
												"DEcMncqkE3sHfbQtD3CKyTFnvEGjLI9mXh+ytHtiS/pnEnfHX//fC9NR3MeCDU5U"+
												"ZZ0C8mvxPLcGUXGzBvU3WNEL6mqWsqFBBiygkZUfgqYrSymKbJhkqa5vc5PGDwab"+
												"2EccTZSzAgMBAAECggEABPQ0ARD7WwHUsob9QywB1aifLJ0vDPZpZA93YiyQ4Y/n"+
												"l0WN4Oy6oCTuom2EQFvHsHft9x3ZECiTgQYMFHP/Tn2+TloaBHDUHJqdzio3Lwsd"+
												"vnp0aUzfMHOE93u5vm4515Zx/bgtyGGcCVaJPSGRn2t77QU8tinKYR/v5goY2epO"+
												"8C/qIPmyD2dm9beikGMadsZxXmqFAC7xUcZB0jM4oltrbfhw6BSgI67RkKau7qru"+
												"mAja/gMSK2bWwCBdggDbounNMWjljc6o1U6MnWW5W76Qg8fbPu8qW+mkoVMfeRLE"+
												"nLQI/Pj2jBMUO8frSByl3CE2Hlfef0GiPaVUJdOzgQKBgQDo7V75uGuADiqUjHSE"+
												"Cb7TBVIIDYKOacz0AapV5orHfantBS15n+s6sLc3ha1fs9p0RGzsk0MSJ/nYLkGm"+
												"wy3TSoVV6Plwlmy28aywTt/sHGENYmoRogWttFojm2WqVMu2uwyDgmndNlg2YlUK"+
												"nJQSRYO7W0jX0Pre6PYhlo16mwKBgQDLqstMDimgm3iyg/YD+ZwryAqtLzZx8hso"+
												"UlW6JCslZ0f6hKuSJXcsdIYasO0LPDLxyS62Ne+hj4G/AyxXGXnTMF665RNq+2B/"+
												"bVVIeUhz7WrSNffh/x5Qadyy2cKWuyU1adNsL2d/EKwlI4hgYsBFtz9WgSxdZnKf"+
												"CXLVkqGDyQKBgQDWS+UQbOqCUwY7ywk7nVex/3gcLonKCm7ko0+aXlOZ+/RFO3k+"+
												"J7IcOg8mhtcecHLwey+XbFjNsHe9u7js+y8C8NHBW2l6evqkSa6uOrLrIKTt0nhx"+
												"ppYA3Sf9EHA1iHQvtuFpcKeDPQdlaYyCCdJ1CP2MdwyKehsE0GH6PvAFNwKBgQDF"+
												"/uRkq+AMIxjTczZX5vHMHk1C0AM+AwAj0udEbqUDV8TB5vhku8d/kEhO6nGH2p4s"+
												"MEkZGpY7mieoao1Qn5Ovm/Z7+vdFqdvGBaRDMKUfDlAqzAyPyFTQGBpWRRzHLGif"+
												"5KGFKxCSlsLD2CdcjKQfImaQ0kMvCcmkkqrwtfwPAQKBgQCraRPKSiAgQ0dsbeeb"+
												"vtiLstUiNLvkSyv+TVPcloT9vs0C/B3nKilcbmLYUPlceWsCVyaG5Wf5D5GLtlcS"+
												"BfI1Xwmttn0PhcTuUDD5w36t/pB5pixQR9kr8SZWO8VVq5TVkENRdO7ZdxwV3G1g"+
												"LTh7ChjsxFTYq5hJrMa9Xolgdg==";
	
	public IoTRestApp() {
		// Me conecto al IPFS
		ipfs = new IPFS("/ip4/127.0.0.1/tcp/5001");
		try {
			ipfs.refs.local();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Creo el conector con el servicio REST de Composer
		conectorRest = new ConectorREST("http://localhost:4200/", "iot");
		// Autentico el proxy
		conectorRest.auth(TOKEN);
		
		// Libreria de cifrado
		alice = new Alice(new AliceContextBuilder().build());
	}

	
	/**** Autenticación - Inicio ****/

	// Función privada de apoyo para validar un token
	private boolean isValidToken(String token) {
		boolean valido = false;
		try {
			valido = database.checkToken(token);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return valido;
	}
	
	
	// Método para dar de alta un usuario y certificado en la plataforma
	@POST
    @Consumes("application/json")
	@Path("/cert")
	public Response certficadoAdd(String json) {
		// Proceso el JSON
		JSONObject peticion = new JSONObject(json.toString());
		String cert = peticion.getString("cert");
		String user = peticion.getString("user");
		String hashCert = Certificados.getCertHash(cert);
		
		
		try {
			if(!database.checkUser(user))
				database.addCertificado(user, cert, hashCert);		
			return Response.ok("{\"ok\"}", MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return Response.ok("Error", MediaType.APPLICATION_JSON).build();
	}
	
	
	// Método para pedir el certificado público de un determinado usuario
	@GET
	@Path("/certificado/{user}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getCerft(@PathParam("user") String user) {
		try {
			String certificado = database.getCertf(user);
			
			return Response.ok(certificado, MediaType.TEXT_PLAIN).build();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return Response.ok("Error", MediaType.TEXT_PLAIN).build();
	}

	
	// Método para probar si un token es válido o no
	@GET
	@Path("/testAuth")
	public Response testAuth(@QueryParam("token") String token) {
		if(isValidToken(token)) {
			return Response.ok("Token '"+token+"' valido", MediaType.APPLICATION_JSON).build();
		} else {
			return Response.ok("Token '"+token+"' no registrado", MediaType.APPLICATION_JSON).build();
		}
	}

	
	// Método para autenticarse en la plataforma (devuelve un token cifrado)
	@POST
    @Consumes("application/json")
	@Path("/auth")
	public Response autenticar(String json) {
		// Proceso el JSON
		JSONObject peticion = new JSONObject(json.toString());
		String cert = peticion.getString("cert");
		String user = peticion.getString("user");
		String hashCert = Certificados.getCertHash(cert);
		
		
		try {
			String SessionToken = database.getToken(user, hashCert);
			
			String tokenEncrypt = Base64.getEncoder().encodeToString(Certificados.encrypt(Certificados.readPublicKeyString(cert), SessionToken.getBytes()));			
			return Response.ok("{\"token\": \""+tokenEncrypt+"\"}", MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return Response.ok("Error", MediaType.APPLICATION_JSON).build();
	}

	
	// Método para eliminar un token de sesión
	@GET
	@Path("/logout")
	public Response logout(@QueryParam("token") String token) {
		try {
			database.removeToken(token);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Response.ok("Ok", MediaType.APPLICATION_JSON).build();
	}
	
	/**** Autenticación - Final ****/
	

	/**** Transacciones - Inicio ****/
	
	// Método para obtener el listado de transacciones del sistema
	@GET
	@Path("/transactions")
	public Response getTransactions(@QueryParam("token") String token) {
		if(!isValidToken(token))
			return Response.ok("Token no registrado", MediaType.APPLICATION_JSON).build();
		
		String respuestaREST = conectorRest.enviar(TRANSACTION_NAME, false, "");

		// Proceso el JSON
		List<Transaction> txs = new LinkedList<Transaction>();
		JSONArray arr = new JSONArray(respuestaREST.toString());
		for (int i = 0; i < arr.length(); i++) {
			JSONObject obj = arr.getJSONObject(i);

			Transaction tx = new Transaction();
			tx.setTransactionId(obj.getString("transactionId"));
			tx.setCid(obj.getString("cid"));
			tx.setHash(obj.getString("hash"));
			tx.setFirmaAPI(obj.getString("firmaAPI"));
			tx.setFirmaUser(obj.getString("firmaUser"));
			tx.setTimestamp(obj.getString("timestamp"));
			txs.add(tx);
		}

		String respuestaJSON = new JSONArray(txs).toString();
		return Response.ok(respuestaJSON, MediaType.APPLICATION_JSON).build();
	}

	
	// Método para obtener una transacción
	@GET
	@Path("/transaction/{id}")
	public Response getTransaction(@PathParam("id") String id, @QueryParam("token") String token) {
		if(!isValidToken(token))
			return Response.ok("Token no registrado", MediaType.APPLICATION_JSON).build();
		
		String respuestaREST = conectorRest.enviar(TRANSACTION_NAME + "/" + id, false, "");

		// Proceso el JSON
		JSONObject obj = new JSONObject(respuestaREST.toString());

		// Creo el objeto de salida
		Transaction tx = new Transaction();
		tx.setTransactionId(obj.getString("transactionId"));
		tx.setCid(obj.getString("cid"));
		tx.setHash(obj.getString("hash"));
		tx.setFirmaAPI(obj.getString("firmaAPI"));
		tx.setFirmaUser(obj.getString("firmaUser"));
		tx.setTimestamp(obj.getString("timestamp"));

		// Transforma el objeto a JSON
		String respuestaJSON = new JSONObject(tx).toString();
		return Response.ok(respuestaJSON, MediaType.APPLICATION_JSON).build();
	}
	

	// Método para el envío y cifrado de los datos
	@PUT
    @Consumes("application/json")
	@Path("/transaction")
	public Response transactionPostData(String json) {
		return dataPush(json, true);
	}

	// Método para el envío de los datos ya cifrados por el cliente
	@POST
    @Consumes("application/json")
	@Path("/transaction")
	public Response transactionPutData(String json) {
		return dataPush(json, false);
	}
	
	// Método que realiza el envío de los datos (crea una transacción)
	private Response dataPush(String json, boolean encryptData) {
		// Proceso el JSON
		JSONObject peticion = new JSONObject(json.toString());
		String data = peticion.getString("data");
		String token = peticion.getString("token");
		//String hash = peticion.getString("hash");
		String hash = DigestUtils.sha256Hex(data);
		
		// Reviso el token de autenticación
		if(!isValidToken(token)) 
			return Response.ok("{\"error\" : \"Token Inválido\"}", MediaType.APPLICATION_JSON).build();

		try {
			byte[] datosEncriptados = data.getBytes();
			// Encripto los datos si es necesario
			if(encryptData)
				datosEncriptados = alice.encrypt(datosEncriptados, PASSWORD.toCharArray()); 
			
			//byte[] datosEncriptados2 = alice.decrypt(datosEncriptados, PASSWORD.toCharArray());
			
			// Envio los datos al IPFS
			NamedStreamable.ByteArrayWrapper file = new NamedStreamable.ByteArrayWrapper("file.tmp", datosEncriptados);
			
			List<MerkleNode> addFichero = ipfs.add(file);
			MerkleNode resultado = addFichero.get(0);
			// Saco el CID del resultado
			String cidIPDF = resultado.hash.toBase58();
			
			// El proxy firma el hash de los datos
			String firmaAPI = Base64.getEncoder().encodeToString(Certificados.encrypt(Certificados.readPrivateKeyString(CLAVEPRIVADA), hash.getBytes()));
			// Se recibe la firma que el usuario envia
			String firmaUser = peticion.getString("firma");
			
			// Se preparan los datos que se envian a Composer
			String postData = "{\"$class\": \"iot."+TRANSACTION_NAME+"\", \"cid\": \"" + cidIPDF + "\", \"hash\": \"" + hash + "\", \"firmaAPI\": \"" + firmaAPI + "\", \"firmaUser\": \"" + firmaUser + "\"}";
	
			String respuestaREST = conectorRest.enviar(TRANSACTION_NAME, true, postData);
	
			if (respuestaREST == null)
				return Response.ok("Error", MediaType.APPLICATION_JSON).build();
	
			JSONObject obj = new JSONObject(respuestaREST.toString());
	
			// Creo el objeto de salida
			Transaction tx = new Transaction();
			tx.setTransactionId(obj.getString("transactionId"));
			tx.setHash(obj.getString("hash"));
			tx.setCid(obj.getString("cid"));
			tx.setFirmaAPI(obj.getString("firmaAPI"));
			tx.setFirmaUser(obj.getString("firmaUser"));
			//tx.setTimestamp(obj.getString("timestamp")); // TODO : No devuelve la marca de tiempo
			
			// Transforma el objeto a JSON
			String respuestaJSON = new JSONObject(tx).toString();

			return Response.ok(respuestaJSON, MediaType.APPLICATION_JSON).build();
		} catch (IOException | GeneralSecurityException e) {
			e.printStackTrace();
		}
		return Response.ok("Error", MediaType.APPLICATION_JSON).build();		
	}

	/**** Transacciones - Final ****/
	

	/**** Dato - Inicio ****/
	
	// Método para buscar la transacción de un determinado dato (se le pasa el hash)
	@GET
    @Consumes("application/json")
	@Path("/find/{hash}")
	public Response getTransactionByHash(@PathParam("hash") String hash, @QueryParam("token") String token) {
		if(!isValidToken(token))
			return Response.ok("Token no registrado", MediaType.APPLICATION_JSON).build();
	
		String respuestaREST = conectorRest.enviar(TRANSACTION_NAME, false, "");
		Transaction tx = new Transaction();
		// Proceso el JSON
		JSONArray arr = new JSONArray(respuestaREST.toString());
		for (int i = 0; i < arr.length(); i++) {
			JSONObject obj = arr.getJSONObject(i);

			if(hash.equals(obj.getString("hash"))) {
				tx.setTransactionId(obj.getString("transactionId"));
				tx.setCid(obj.getString("cid"));
				tx.setHash(obj.getString("hash"));
				tx.setFirmaAPI(obj.getString("firmaAPI"));
				tx.setFirmaUser(obj.getString("firmaUser"));
				tx.setTimestamp(obj.getString("timestamp"));
			}
		}

		// Transforma el objeto a JSON
		String respuestaJSON = new JSONObject(tx).toString();
		return Response.ok(respuestaJSON, MediaType.APPLICATION_JSON).build();
	}
	

	// Método para buscar la transacción de un determinado dato (se le pasa el dato)
	@POST
    @Consumes("application/json")
	@Path("/find/")
	public Response getTransactionByData(String json) {
		// Proceso el JSON
		JSONObject peticion = new JSONObject(json.toString());
		String data = peticion.getString("data");
		String token = peticion.getString("token");
		String hash = DigestUtils.sha256Hex(data);
		
		
		if(!isValidToken(token))
			return Response.ok("Token no registrado", MediaType.APPLICATION_JSON).build();
	
		String respuestaREST = conectorRest.enviar(TRANSACTION_NAME, false, "");
		Transaction tx = new Transaction();
		// Proceso el JSON
		JSONArray arr = new JSONArray(respuestaREST.toString());
		for (int i = 0; i < arr.length(); i++) {
			JSONObject obj = arr.getJSONObject(i);

			if(hash.equals(obj.getString("hash"))) {
				tx.setTransactionId(obj.getString("transactionId"));
				tx.setCid(obj.getString("cid"));
				tx.setHash(obj.getString("hash"));
				tx.setFirmaAPI(obj.getString("firmaAPI"));
				tx.setFirmaUser(obj.getString("firmaUser"));
				tx.setTimestamp(obj.getString("timestamp"));
			}
		}

		// Transforma el objeto a JSON
		String respuestaJSON = new JSONObject(tx).toString();
		return Response.ok(respuestaJSON, MediaType.APPLICATION_JSON).build();
	}
	

	// Método para recibir los datos del IPFS de un determinado CID
	@GET
	@Path("/ipfs/{cid}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getIPFS(@PathParam("cid") String cid, @QueryParam("token") String token) {
		if(!isValidToken(token))
			return Response.ok("Token no registrado", MediaType.APPLICATION_JSON).build();
		
		try {
			Multihash filePointer = Multihash.fromBase58(cid);
			byte[] datosEncriptados = ipfs.cat(filePointer);
			
			if(false) // Si fuese necesario desencriptar
				datosEncriptados = alice.decrypt(datosEncriptados, PASSWORD.toCharArray());
			
			return Response.ok(new String(datosEncriptados, StandardCharsets.UTF_8), MediaType.TEXT_PLAIN).build();
		} catch (IOException | GeneralSecurityException e) {
			e.printStackTrace();
		}

		return Response.ok("Error", MediaType.TEXT_PLAIN).build();
	}
	

	/**** Dato - Final ****/

}

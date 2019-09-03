package test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONObject;

import utils.Certificados;

public class ClienteProxyTest {
	public static String clavePrivada = 	"MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQC2rGKHF78d0kJ6"+
											"EbqkShrhdKZXAnpl/hnwVxPWnPWHMJHTKizFZdfo69DOVgNNAG1cosAK3TyaVyFF"+
											"N74TgVc5y4aVzlecYo1RuavOPmNOb7wgQa+MeopeTrrwPbb7xi32SNOOCtAdb8bk"+
											"K2yg5+EGlySJKEHgpQD7XmiQPjH+i7RefvrC/W8juhP1y8MD1B3XwZ8k+WK7HzO0"+
											"Z96pa/bQf5dgGjIyPgeQDCWLpKLWZceIap/n5wY/LO9FFpdHgxQKEKvJltKjqPZh"+
											"ulx8xTTPyrDh9I5tLMV00EgP7lgNjdqX9mjJzfHjXUeut0uDEECYudInbwP3hIhK"+
											"upO6eunTAgMBAAECggEBAIE+7KNKkOUnm/NEUfrll6lG4F4VmcZKB849vgKNoUaa"+
											"fcKbR3wO97mMB3vF7aXwlHADkr7dE4Db/uU7cFfann182+doTCKQtA5LwRw2lgqs"+
											"Eb3825kYJohdSfSqpanz6RclJdGNIvyA1ocflkcuaM9B5w7IFfw8oro3bOIJr1O6"+
											"D/mgJqXKMU0Hhe3rmlrCSxafOgom1Vq3MBZex1d/iRIJlwS6X1WWS+INxvoNnrIU"+
											"7RpjjP3agkDl0TWvzi9CDn905eFOX+8W+Bvm+ranUEHdZepp2bepQ+P8C/WmSNTd"+
											"qzQX0miHr8REd/+qQkIl+ek99b8E7Z5cZXJnoJGNKBkCgYEA6LWv18YXiRenm3Zo"+
											"UI3W0yorA2JxpfXinp4pW5pDX3sYQLx53tap0suHyk52GWAvII0QATVdZJ5i1Xjk"+
											"wPZ6NgxWYKfOT9uSckeGStL/eLdRQCM0k8Lkhe/jCdLVC1QMIQ+No8eVjOTPGiIG"+
											"zQEZHm+YAVgJxFY1cyA4fvdQ5CUCgYEAyPS0ZEuYIVt2zuzh00B4skX1bqmvzjVC"+
											"Q7X4kDg2fsdkAk4h7G/Qr3DWkYK7LbM193bXsF+J6Y7bae/JvDgAXFnxDcExc41j"+
											"1Hzg5I2+E+TbB5KLjTtjkAj820tTLbDO+o3dIaXfBSJ7+r7HhyVLljcGAb4EdSX+"+
											"S8TmiNmXeJcCgYEAz027Pg77vGbUKsvKlaglbaus8bDq6RsoLIFc+ntr9UzlCBZe"+
											"wsKcOmqRpwlKb8SddvPzBACUGCR4iw7NSZDIwgkvkZ8rTTyfMyrAWTup1dIIkYhL"+
											"OC5wAVj3+jJVE2hl/bWrSfFsKj1SuUcCo/GMfO6QjPHukXby2oIjkgejifkCgYBw"+
											"BvL1oqrJlcqy6p2t/hgLEjMToScv5sLtKmnKRHwkyxU2r8X7oA4Pb/E+J0of3PMt"+
											"5KOzYF9qw8gP06x78CBLW5ylVVkzGNRZZdCduWMIhiGQpCKF9mC3sDr+KYzSQOny"+
											"BdoL4wNkQmgpTee/sIvlzwDbQcoMYMTWCouWKO9nOwKBgQCrzLa44b+E+U/Vt02Z"+
											"xP2RnKWYUFr1zOMmqbzU5yYH/u/x1p6L6elm+b5mRSJSA59lPj8mHiikSLKZKHV/"+
											"gSVRfIt+FCXV04kRYbB2t7sm4CgVDAs0Dtyr2LShDRBNqOuCIIyoOuShcPS5bvBM"+
											"abSDjwWeGgFcDveMGNhC+eFt3A==";
	public static String clavePublica = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtqxihxe/HdJCehG6pEoa4XSmVwJ6Zf4Z8FcT1pz1hzCR0yosxWXX6OvQzlYDTQBtXKLACt08mlchRTe+E4FXOcuGlc5XnGKNUbmrzj5jTm+8IEGvjHqKXk668D22+8Yt9kjTjgrQHW/G5CtsoOfhBpckiShB4KUA+15okD4x/ou0Xn76wv1vI7oT9cvDA9Qd18GfJPliux8ztGfeqWv20H+XYBoyMj4HkAwli6Si1mXHiGqf5+cGPyzvRRaXR4MUChCryZbSo6j2YbpcfMU0z8qw4fSObSzFdNBID+5YDY3al/Zoyc3x411HrrdLgxBAmLnSJ28D94SISrqTunrp0wIDAQAB";
	public static String usuarioID = "alberto";
	public static String certPost = "{\"user\": \""+usuarioID+"\", \"cert\": \""+clavePublica+"\"}";
	
	public static String conectar(String ruta, boolean get, String data) {
		try {
			URL url = new URL(ruta);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			if(get) {
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Content-Type", "application/json");
			} else {
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/json");
				conn.setDoOutput(true);
				OutputStream os = conn.getOutputStream();
				os.write(data.getBytes());
				os.flush();
			}
	
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK
					&& conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
				throw new IllegalStateException("Failed : HTTP error code : " + conn.getResponseCode() + " -> Data: "
						+ data + ", -> URL: " + url);
			}
	
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
	
			StringBuffer response = new StringBuffer();
			String readLine = null;
			while ((readLine = br.readLine()) != null) {
				response.append(readLine);
			}
			br.close();
			conn.disconnect();
			
			return response.toString();
		} catch (Exception e) {
			System.out.println("Error en la conexión");
			e.printStackTrace();
			return null;
		}
		
	}

	public static void main(String[] args) {
		
		// Leer las claves de los ficheros .PEM
		/*try {
			clavePrivada = new String(Files.readAllBytes(Paths.get("private_key_pkcs8.pem")));
			clavePublica = new String(Files.readAllBytes(Paths.get("key.pub")));
		} catch (Exception e) {
			System.out.println("Error al leer los ficheros de los certificados");
			e.printStackTrace();
		}*/
		
		try {
			// Doy de alta el certificado
			//conectar("http://localhost:8080/ApiRest/iot-bna/cert", false, certPost);
			
			// Me logueo
			String respuesta = conectar("http://localhost:8080/ApiRest/iot-bna/auth", false, certPost);
			System.out.println("Respuesta -> " + respuesta);
			JSONObject peticion = new JSONObject(respuesta.toString());
			String tokenEncrypt = peticion.getString("token");
			String token = new String(Certificados.decrypt(Certificados.readPrivateKeyString(clavePrivada), Base64.getDecoder().decode(tokenEncrypt.getBytes())), StandardCharsets.UTF_8);
			
			System.out.println("Token -> " + token);
			
			// Pruebo el logueo
			respuesta = conectar("http://localhost:8080/ApiRest/iot-bna/testAuth?token="+token, true, null);
			System.out.println("Respuesta testAuth -> " + respuesta);

			// Pido la lista de transacciones del sistema
			respuesta = conectar("http://localhost:8080/ApiRest/iot-bna/transactions?token="+token, true, null);
			System.out.println("Lista de transacciones -> " + respuesta);
			
			// Envio datos
			String datos = "Datos de prueba, aqui se pueden guardar cualquier cadena";
			String hash = DigestUtils.sha256Hex(datos);
			String firma = Base64.getEncoder().encodeToString(Certificados.encrypt(Certificados.readPrivateKeyString(clavePrivada), hash.getBytes()));
			String json = "{\"token\" : \""+token+"\", \"firma\" : \""+firma+"\", \"data\" : \""+datos+"\"}";
			
			respuesta = conectar("http://localhost:8080/ApiRest/iot-bna/transaction", false, json);
			System.out.println("Envío -> " + respuesta);

			JSONObject respuestaObj = new JSONObject(respuesta.toString());
			String CID = respuestaObj.getString("cid");
			
			// Pido la lista de transacciones del sistema de nuevo
			respuesta = conectar("http://localhost:8080/ApiRest/iot-bna/transactions?token="+token, true, null);
			System.out.println("Lista de transacciones -> " + respuesta);
			

			// Pido el dato del IPFS con el CID recibido
			respuesta = conectar("http://localhost:8080/ApiRest/iot-bna/ipfs/"+CID+"?token="+token, true, null);
			System.out.println("Datos del IPFS -> " + respuesta);
			
			// Hago un logout
			respuesta = conectar("http://localhost:8080/ApiRest/iot-bna/logout?token="+token, true, null);
			System.out.println("Token eliminado (logout)");

			// Vuelvo a probar el logueo
			respuesta = conectar("http://localhost:8080/ApiRest/iot-bna/testAuth?token="+token, true, null);
			System.out.println("Respuesta testAuth -> " + respuesta);

			// FIN

		} catch (Exception e) {
			System.out.println("Error en las pruebas");
			e.printStackTrace();
		}

	}

}

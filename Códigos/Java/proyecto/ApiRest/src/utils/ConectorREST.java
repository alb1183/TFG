package utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ConectorREST {
	private String direccion;
	private String namespace;
	private String accessToken;

	public ConectorREST(String url, String namespace) {
		this.direccion = url;
		this.namespace = namespace;
	}

	public void auth(String token) {
		try {
			URL url = new URL(direccion + "auth/jwt/callback?token=" + token);
			
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setInstanceFollowRedirects(false);
			conn.setRequestMethod("GET");
			if (conn.getResponseCode() != HttpURLConnection.HTTP_MOVED_TEMP) {
				throw new IllegalStateException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			
			// Listamos las cookies que devuelve la peticón
			List<String> cookies = conn.getHeaderFields().get("set-cookie");
			if (cookies != null) {
				// Buscamos la cookie "access_token"
				for (String cookie : cookies) {
					// Sacamos la primera parte de la cookie (tiene el nombre y el valor)
					String[] pairs = cookie.split(";");
					// Separamos el nombre y el valor
					String[] ck = pairs[0].split("=");
					// Miramos si es la cookie que necesitamos
					if(ck[0].equals("access_token"))
						// Sacamos el token almacenado, primero se hace un URLDecode, despues quitamos el s: y finalmente nos quedamos con la cadena antes del punto
						// Ejemplo: s:9IAE7H1uC9PEHgmCCZtIiv35LSlb8nXzqiurDHFX4futX9kYCA5wP46AL24AWSjS.HY6Cki1NKsYvyHvShwbrPgFlPeVI/f7UnLhBbQiBIMs
						accessToken = java.net.URLDecoder.decode(ck[1], StandardCharsets.UTF_8.name()).split(":")[1].split("\\.")[0];				
				}
			}
			
			conn.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String enviar(String accion, boolean post, String postData) {
		try {
			//URL url = new URL(direccion + "api/" + namespace + "." + accion);
			URL url = new URL(direccion + "api/" + accion + "?access_token="+accessToken);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			if (post) {
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/json");
				conn.setDoOutput(true);
				OutputStream os = conn.getOutputStream();
				os.write(postData.getBytes());
				os.flush();
			} else {
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Accept", "application/json");
			}

			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK
					&& conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
				throw new IllegalStateException("Failed : HTTP error code : " + conn.getResponseCode() + " -> Data: "
						+ postData + ", -> URL: " + url);
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			StringBuffer response = new StringBuffer();
			String readLine = null;
			while ((readLine = br.readLine()) != null) {
				response.append(readLine);
			}
			br.close();
			conn.disconnect();

			return response.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}

package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.digest.DigestUtils;

public class Certificados {
	public static byte[] readFileBytes(String filename) throws IOException {
	    Path path = Paths.get(filename);
	    return Files.readAllBytes(path);        
	}

	public static PublicKey readPublicKeyString(String key) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] byteKey = Base64.getDecoder().decode(key.replace("\n", "").replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "").getBytes());
	    X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(byteKey);
	    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	    return keyFactory.generatePublic(publicSpec);       
	}
	
	public static PublicKey readPublicKeyFile(String filename) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
	    X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(readFileBytes(filename));
	    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	    return keyFactory.generatePublic(publicSpec);       
	}

	public static PrivateKey readPrivateKeyString(String key) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] byteKey = Base64.getDecoder().decode(key.replace("\n", "").replace("-----BEGIN PRIVATE KEY-----", "").replace("-----BEGIN PRIVATE KEY-----", "").getBytes());
	    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(byteKey);
	    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	    return keyFactory.generatePrivate(keySpec);     
	}

	public static PrivateKey readPrivateKeyFile(String filename) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
	    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(readFileBytes(filename));
	    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	    return keyFactory.generatePrivate(keySpec);     
	}
	
	public static byte[] encrypt(PublicKey key, byte[] plaintext) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
	    //Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");   
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	    cipher.init(Cipher.ENCRYPT_MODE, key);  
	    return cipher.doFinal(plaintext);
	}
	
	public static byte[] encrypt(PrivateKey key, byte[] plaintext) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	    cipher.init(Cipher.ENCRYPT_MODE, key);  
	    return cipher.doFinal(plaintext);
	}

	public static byte[] decrypt(PublicKey key, byte[] ciphertext) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
	    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	    cipher.init(Cipher.DECRYPT_MODE, key);  
	    return cipher.doFinal(ciphertext);
	}
	
	public static byte[] decrypt(PrivateKey key, byte[] ciphertext) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
	    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	    cipher.init(Cipher.DECRYPT_MODE, key);  
	    return cipher.doFinal(ciphertext);
	}
	
	public static String getCertHash(String key) {
		return DigestUtils.sha256Hex(key.replace("\n", "").replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", ""));
	}
}

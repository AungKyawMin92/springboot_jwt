package mm.com.spring.ak.config;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoderConfig {

	// convert aes encrypt to bcrypt encrypt
	public PasswordEncoder passwordEncoder() {
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
        return new PasswordEncoder() {
        	
        	@Value("${security.aes.key}")
        	private String secret;
        	
            @Override
            public String encode(CharSequence rawPassword) {            	
            	return encoder.encode(rawPassword);
            }
            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
            	try {     
	        		String cipherText = rawPassword.toString();	
	        		byte[] cipherData = Base64.getDecoder().decode(cipherText.replace(" ", "+"));
	        		byte[] saltData = Arrays.copyOfRange(cipherData, 8, 16);	
	        		MessageDigest md5 = MessageDigest.getInstance("MD5");

	        		final byte[][] keyAndIV = EVP_BytesToKey(32, 16, md5, saltData, secret.getBytes(StandardCharsets.UTF_8), 1);
	        		SecretKeySpec key = new SecretKeySpec(keyAndIV[0], "AES");
	        		IvParameterSpec iv = new IvParameterSpec(keyAndIV[1]);
	
	        		byte[] encrypted = Arrays.copyOfRange(cipherData, 16, cipherData.length);
	        		Cipher aesCBC = Cipher.getInstance("AES/CBC/PKCS5Padding");
	        		aesCBC.init(Cipher.DECRYPT_MODE, key, iv);
	        		byte[] decryptedData = aesCBC.doFinal(encrypted);
	        		String decryptedText = new String(decryptedData, StandardCharsets.UTF_8);
	
            		return encoder.matches(decryptedText, encodedPassword);
            	}catch(Exception e) {
            		e.printStackTrace();
            		return false;
            	}
            }
        };	
	}
	public byte[][] EVP_BytesToKey(int key_len, int iv_len, MessageDigest md, byte[] salt, byte[] data, int count) {
	       byte[][] both = new byte[2][];
	       byte[] key = new byte[key_len];
	       int key_ix = 0;
	       byte[]  iv = new byte[iv_len];
	       int iv_ix = 0;
	       both[0] = key;
	       both[1] = iv;
	       byte[] md_buf = null;
	       int nkey = key_len;
	       int niv = iv_len;
	       int i = 0;
	       if(data == null) {
	           return both;
	       }
	       int addmd = 0;
	       for(;;) {
	           md.reset();
	           if(addmd++ > 0) {
	               md.update(md_buf);
	           }
	           md.update(data);
	           if(null != salt) {
	               md.update(salt,0,8);
	           }
	           md_buf = md.digest();
	           for(i=1;i<count;i++) {
	               md.reset();
	               md.update(md_buf);
	               md_buf = md.digest();
	           }
	           i=0;
	           if(nkey > 0) {
	               for(;;) {
	                   if(nkey == 0) break;
	                   if(i == md_buf.length) break;
	                   key[key_ix++] = md_buf[i];
	                   nkey--;
	                   i++;
	               }
	           }
	           if(niv > 0 && i != md_buf.length) {
	               for(;;) {
	                   if(niv == 0) break;
	                   if(i == md_buf.length) break;
	                   iv[iv_ix++] = md_buf[i];
	                   niv--;
	                   i++;
	               }
	           }
	           if(nkey == 0 && niv == 0) {
	               break;
	           }
	       }
	       for(i=0;i<md_buf.length;i++) {
	           md_buf[i] = 0;
	       }
	       return both;
	   }

}

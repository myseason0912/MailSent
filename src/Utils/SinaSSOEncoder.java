package Utils;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Hex;

public class SinaSSOEncoder {
	
	   public static String rsaCrypt(String modeHex, String exponentHex, String messageg) {
			KeyFactory factory = null;
			try {
				factory = KeyFactory.getInstance("RSA");
			} catch (NoSuchAlgorithmException e1) {
				e1.printStackTrace();
			}
			
			BigInteger m = new BigInteger(modeHex, 16); /* public exponent */
			BigInteger e = new BigInteger(exponentHex, 16); /* modulus */
			RSAPublicKeySpec spec = new RSAPublicKeySpec(m,e);
			
			RSAPublicKey pub;
			Cipher enc = null;
			byte[] encryptedContentKey = null;
			try {
				pub = (RSAPublicKey) factory.generatePublic(spec);//根据pubkey和'10001'生成公钥
				enc = Cipher.getInstance("RSA");
				enc.init(Cipher.ENCRYPT_MODE, pub);
				encryptedContentKey = enc.doFinal(messageg.getBytes());//根据密码串生成秘药
			} catch (InvalidKeySpecException e1) {
				e1.printStackTrace();
			} catch (NoSuchAlgorithmException e2) {
				e2.printStackTrace();
			} catch (NoSuchPaddingException e3) {
				e3.printStackTrace();
			} catch (InvalidKeyException e4) {
				e4.printStackTrace();
			} catch (IllegalBlockSizeException e5) {
				e5.printStackTrace();
			} catch (BadPaddingException e6) {
				e6.printStackTrace();
			}
			return new String(Hex.encodeHex(encryptedContentKey));
		}
}

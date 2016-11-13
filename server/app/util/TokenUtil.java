package util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

public class TokenUtil {

	public static String createToken(int id, String username) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(new Token(id, username));
		oos.close();
		return Base64.getEncoder().encodeToString(baos.toByteArray());
	}
	
	public static Token extractToken(String token) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bais = new ByteArrayInputStream(Base64.getDecoder().decode(token));
		ObjectInputStream ois = new ObjectInputStream(bais);
		return (Token)ois.readObject();
	}
	
}

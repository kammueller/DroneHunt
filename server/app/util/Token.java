package util;

import java.io.Serializable;

public class Token implements Serializable {
	private static final long serialVersionUID = 1L;
	private String username;
	private int id;
	
	public Token(int id, String username) {
		this.username = username;
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}
	
	public int getId() {
		return id;
	}
}
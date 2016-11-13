package controllers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import configuration.Config;
import play.db.DB;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import util.Token;
import util.TokenUtil;


public class UserController extends Controller {
	
	private static File hintDir = new File("hints");
	private SecureRandom random = new SecureRandom();
	
	public UserController() {
		if(!hintDir.exists())
			if(!hintDir.mkdirs())
				throw new IllegalStateException("Could not create directory 'hints'");
	}
    
    public synchronized Result register() {
    	
    	System.out.println("REGISTER");
    	System.out.println("BODY " + request().body().asText());
    	System.out.println("BODY " + request().body().asJson());
    	System.out.println("BODY " + request().body().asRaw());
    	JsonNode json = request().body().asJson();
    	
    	if(!json.has("username"))
    		return internalServerError("No username given");
    	
    	String username = json.get("username").asText();
    	
    	Connection con = DB.getConnection();
    	try {
			PreparedStatement ps = con.prepareStatement("INSERT INTO users (name) VALUES (?)", PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setString(1, username);
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			String token = TokenUtil.createToken(rs.getInt(1), username);
			JsonNode obj = Json.newObject().put("token", token);
	    	return ok(obj);
		} catch(SQLException ex) {
			// duplicate user [name has unique-index]
			ex.printStackTrace();
			return forbidden();
		} catch(Exception ex) {
			// could not create token
			ex.printStackTrace();
			return internalServerError();
		} finally {
			try {
				con.close();
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
    }
    
    public synchronized Result getToken(String username) {
    	System.out.println("GET TOKEN");
    	Connection con = DB.getConnection();
    	try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE name=?");
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				JsonNode obj = Json.newObject().put("token", TokenUtil.createToken(rs.getInt("id"), username));
		    	return ok(obj);
			}
		} catch(Exception ex) {
			// could not create token
			ex.printStackTrace();
			return internalServerError();
		} finally {
			try {
				con.close();
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
    	return forbidden();
    }
    
    public synchronized Result resolveToken() {
    	try {
	    	JsonNode json = request().body().asJson();
	    	if(!json.has("token"))
	    		return internalServerError("No token given");
	    	
	    	Token token = TokenUtil.extractToken(json.get("token").asText());
	    	JsonNode obj = Json.newObject().put("username", token.getUsername());
	    	return ok(obj);
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		return internalServerError();
    	}
    }
    
    public synchronized Result activateUser() {
    	
    	if(!existsActiveChallenge())
    		return forbidden();
    	
    	JsonNode json = request().body().asJson();
    	if(!json.has("token"))
    		return internalServerError("No token given");
    	
    	Connection con = DB.getConnection();
    	try {
    		Token user = TokenUtil.extractToken(json.get("token").asText());
    		System.out.println("ACTIVATE USER = " + user.getUsername() + "(ID=" + user.getId()+")");
			PreparedStatement ps = con.prepareStatement("UPDATE users SET validUntil = ? WHERE id = ?");
			ps.setLong(1, System.currentTimeMillis()/1000 + Config.SECONDS_ACTIVATION_VALID);
			ps.setInt(2, user.getId());
			ps.executeUpdate();
			int affected = ps.getUpdateCount();
			if(affected > 0)
				return ok();
		} catch(Exception ex) {
			// invalid token, do nothing
		} finally {
			try {
				con.close();
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
    	return forbidden();
    }
    
    public synchronized Result getRemainingActivationSeconds() {
    	Connection con = DB.getConnection();
    	try {
    		JsonNode json = request().body().asJson();
        	if(!json.has("token"))
        		return internalServerError("No token given");
    		Token user = TokenUtil.extractToken(json.get("token").asText());
    		System.out.println("CHECK USER="+user.getUsername() + " (id="+user.getId()+")");
    		PreparedStatement ps = con.prepareStatement("SELECT validUntil FROM users WHERE id = ?");
    		ps.setInt(1, user.getId());
    		ResultSet rs = ps.executeQuery();
    		if(rs.next()) {
    			long remaining = Math.max(0,  rs.getLong("validUntil") - System.currentTimeMillis()/1000);
    			System.out.println("REMAINING SECONDS: " + remaining);
    			JsonNode obj = Json.newObject().put("secondsValid", remaining);
    	    	return ok(obj);
    		}
		} catch(Exception ex) {
			// invalid token, do nothing
		} finally {
			try {
				con.close();
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
    	return forbidden();
    }
    
    public synchronized Result isNewHintAvailable(String currentHint) {
    	
    	if(!existsActiveChallenge())
    		return forbidden();
    	
    	try {
    		long currentHintTimestamp = Long.parseLong(currentHint.split("\\.")[0]);
    		List<Long> hints = loadAvailableHintNumbers();
    		
    		JsonNode obj = Json.newObject().put("hintAvailable",
    				currentHintTimestamp < hints.get(hints.size() - 1));
	    	return ok(obj);
    	} catch(Exception ex) {
    		// invalid format of file (not [0-9]*.[a-z]+)
    		return forbidden();
    	}
    }
    
    public synchronized Result getCurrentHint() {
    	
    	if(!existsActiveChallenge())
    		return forbidden();
    	
    	try {
    		BufferedInputStream bos = new BufferedInputStream(
    			new FileInputStream(loadCurrentHintFile()));
    		return ok(bos);
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		return internalServerError();
    	}
    }
    
    public synchronized Result checkGoalSecret() {
    	
    	JsonNode json = request().body().asJson();
    	
    	if(!json.has("token") || !json.has("goalSecret"))
    		return internalServerError("No goalSecret or token given");
    	
    	String goalSecret = json.get("goalSecret").asText();
    	String token = json.get("token").asText();
    	
    	Connection con = DB.getConnection();
    	try {
    		ResultSet rs = getActiveChallenge(con);
    		if(rs != null) {
    			// Already a winner? (getInt == 0 if NULL in DB)
    			if(rs.getInt("winner") != 0)
    				return forbidden();
    			
    			int challengeId = rs.getInt("id");
    			
    			rs.close();
    			
    			// check whether user is activated
    			PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE id=?");
    			ps.setInt(1, TokenUtil.extractToken(token).getId());
    			rs = ps.executeQuery();
    			if(!rs.next())
    				return forbidden();
    			boolean valid = rs.getLong("validUntil") - System.currentTimeMillis()/1000 > 0;
    			if(!valid) {
    				System.out.println("User inactive!");
    				return forbidden();
    			}
    			
    			ps.close();
    			
    			ps = con.prepareStatement("UPDATE challenges SET winner=? WHERE id=?");
    			ps.setInt(1, TokenUtil.extractToken(token).getId());
    			ps.setInt(2, challengeId);
    			ps.executeUpdate();
    			return ok();
    		}
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				con.close();
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
    	
    	return forbidden();
    }
    
    public synchronized Result createChallenge() {
    	
    	System.out.println("CLEAN: "+cleanHints());
    	
    	System.out.println("create challenge");
    	
    	Connection con = DB.getConnection();
    	try {
    		String secret = generateChallengeSecret();
    		PreparedStatement ps = con.prepareStatement("UPDATE challenges SET active = 0");
    		ps.executeUpdate();
    		ps.close();
    		ps = con.prepareStatement("INSERT INTO challenges (startTimestamp, secret) VALUES (?,?)");
    		ps.setLong(1, System.currentTimeMillis()/1000);
    		ps.setString(2, secret);
    		ps.executeUpdate();
    		JsonNode obj = Json.newObject().put("secret", secret + "/sdf83h");
    		System.out.println("send secret = "+secret + "/sdf83h");
	    	return ok(obj);
    		//return rs.next();
		} catch(Exception ex) {
			// invalid token, do nothing
			ex.printStackTrace();
		} finally {
			try {
				con.close();
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
    	return forbidden();
    }
    
    public synchronized Result destroyChallenge() {
    	
    	System.out.println("CLEAN HINTS: " + cleanHints());
    	
    	JsonNode json = request().body().asJson();
    	
    	if(!json.has("secret"))
    		return internalServerError("No secret given");
    	
    	String secret = json.get("secret").asText();
    	Connection con = DB.getConnection();
    	try {
    		PreparedStatement ps = con.prepareStatement("UPDATE challenges SET active = 0 WHERE secret = ?");
    		ps.setString(1, secret);
    		ps.executeUpdate();
    		return ok();
		} catch(Exception ex) {
			// invalid secret, do nothing
		} finally {
			try {
				con.close();
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
    	return forbidden();
    }
    
    public synchronized Result winnerExists() {
    	Connection con = DB.getConnection();
    	try {
	    	ResultSet rs = getActiveChallenge(con);
	    	boolean winnerExists = false;
	    	if(rs != null) {
	    		winnerExists = rs.getInt("winner") != 0;
	    	}
	    	JsonNode obj = Json.newObject().put("winnerExists", winnerExists);
	    	try { /*rs.close();*/ } catch(Exception ex) {
	    		ex.printStackTrace();
	    	}
	    	return ok(obj);
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		return internalServerError();
    	} finally {
    		try {
    			con.close();
    		} catch(Exception ex) {
    			ex.printStackTrace();
    		}
    	}
    }
    
    public synchronized Result uploadNewHint() {
    	System.out.println("UPLOAD NEW HINT");
    	try {
			File tmp = request().body().asRaw().asFile().getAbsoluteFile();
//			String[] split = tmp.getName().split("\\.");
//			String suffix = split[split.length - 1];
			String suffix = "jpg";
			String prefix = "" + System.currentTimeMillis()/1000;
			File target = new File(hintDir, prefix + "." + suffix);
			System.out.println(tmp.getAbsolutePath() + " -> " + target.getAbsolutePath());
			tmp.renameTo(target);
			return ok();
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		return internalServerError();
    	}
	}
    
    private ResultSet getActiveChallenge(Connection con) {
    	try {
    		PreparedStatement ps = con.prepareStatement("SELECT * FROM challenges WHERE active = 1");
    		ResultSet rs = ps.executeQuery();
    		if(rs.next()) {
    			return rs;
    		}
		} catch(Exception ex) {
		}
    	return null;
    }
    
    private boolean existsActiveChallenge() {
    	Connection con = DB.getConnection();
    	ResultSet rs = getActiveChallenge(con);
    	boolean exists = rs != null;
    	try {
    		rs.close();
    	} catch(Exception ex) {
    		ex.printStackTrace();
    	} finally {
			try {
				con.close();
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
    	return exists;
    }
    
    private File loadCurrentHintFile() {
    	List<File> hints = new ArrayList<>();
    	for(File file : hintDir.listFiles()) {
    		if(!file.isDirectory()) {
    			hints.add(file);
    		}
    	}
    	Collections.sort(hints);
    	return hints.get(hints.size() - 1);
    }
    
    private List<Long> loadAvailableHintNumbers() {
    	List<Long> hints = new ArrayList<>();
    	for(File file : hintDir.listFiles()) {
    		if(!file.isDirectory()) {
    			try {
    			long hintTimestamp = Long.parseLong(file.getName().split("\\.")[0]);
    				hints.add(hintTimestamp);
    			} catch(Exception ex) {}
    		}
    	}
    	Collections.sort(hints);
    	return hints;
    }
    
    private String generateChallengeSecret() {
    	byte[] rand = new byte[32];
    	random.nextBytes(rand);
    	return "SECRET_" + System.currentTimeMillis() + "_" + Base64.getEncoder().encodeToString(rand);
    }
    
    private boolean cleanHints() {
    	return hintDir.delete() && hintDir.mkdirs();
    }

}

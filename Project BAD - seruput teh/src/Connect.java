
import java.sql.*;

public class Connect {
	
	private final String USERNAME = "root";
	private final String PASSWORD = "";	
	private final String DATABASE = "seruput_teh";	
	private final String HOST = "localhost:3306";	
	private final String CONNECTION = String.format("jdbc:mysql://%s/%s", HOST, DATABASE);
	
	public ResultSet rs; 
	public ResultSetMetaData rsm; 
	
	private Connection con; 
	private Statement st; 
	
	
	private static Connect instance;
	
	public static Connect getInstance() {
		
		if(instance == null) {
			return new Connect();
		}
		return instance;
	}
	
	private Connect() {
		try {
			con = DriverManager.getConnection(CONNECTION, USERNAME, PASSWORD);
			st = con.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public ResultSet execQuery(String query) {
		try {
			rs = st.executeQuery(query);
			rsm = rs.getMetaData();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}

  public void execUpdate(String query) {
    // kalau ada cacing merah, surround with trycatch aja
    try {
      st.executeUpdate(query);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
	
	
	
	
	
}

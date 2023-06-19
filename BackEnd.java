import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String username = request.getParameter("username");
    String password = request.getParameter("password");

    // Validate the username and password against the database
    boolean isValid = validateLogin(username, password);
    
    if (isValid) {
      response.sendRedirect("welcome.html");
    } else {
      response.sendRedirect("error.html");
    }
  }

  private boolean validateLogin(String username, String password) {
    String dbUrl = "jdbc:mysql://localhost:3306/mydatabase";
    String dbUser = "username";
    String dbPassword = "password";

    try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
      String query = "SELECT * FROM users WHERE username = ? AND password = ?";
      try (PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, username);
        stmt.setString(2, password);
        try (ResultSet rs = stmt.executeQuery()) {
          return rs.next(); // User found in the database
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }
}

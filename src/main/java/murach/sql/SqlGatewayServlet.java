package murach.sql;

import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.sql.*;

public class SqlGatewayServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String sqlStatement = request.getParameter("sqlStatement");
        String sqlResult = "";
        try {
            // Load the driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Get a connection
            String dbURL = "jdbc:mysql://azure-server-murach-123.mysql.database.azure.com:3306/murach?zeroDateTimeBehavior=CONVERT_TO_NULL";
            String username="ductho";
            String password="Tho0411@";
            Connection connection = DriverManager.getConnection(
                    dbURL, username, password);

            // Parse the SQL string
            sqlStatement = sqlStatement.trim();
            if (sqlStatement.length() >= 6) {
                String sqlType = sqlStatement.substring(0, 6);

                // Use PreparedStatement for SELECT statements
                if (sqlType.equalsIgnoreCase("select")) {
                    PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    sqlResult = SQLUtil.getHtmlTable(resultSet);
                    resultSet.close();
                    preparedStatement.close();
                } else if (sqlType.equalsIgnoreCase("delete")) {
                    // Use PreparedStatement for DELETE
                    PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
                    int rowsAffected = preparedStatement.executeUpdate();
                    sqlResult = "<p>The DELETE statement executed successfully.<br>"
                            + rowsAffected + " row(s) deleted.</p>";
                    preparedStatement.close();
                } else {
                    // Use PreparedStatement for DDL/DML (INSERT, UPDATE)
                    PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected == 0) { // a DDL statement (e.g., CREATE, ALTER)
                        sqlResult = "<p>The statement executed successfully.</p>";
                    } else { // an INSERT, UPDATE statement
                        sqlResult = "<p>The statement executed successfully.<br>"
                                + rowsAffected + " row(s) affected.</p>";
                    }
                    preparedStatement.close();
                }
            }
            connection.close();
        } catch (ClassNotFoundException e) {
            sqlResult = "<p>Error loading the database driver: <br>"
                    + e.getMessage() + "</p>";
        } catch (SQLException e) {
            sqlResult = "<p>Error executing the SQL statement: <br>"
                    + e.getMessage() + "</p>";
        }

        HttpSession session = request.getSession();
        session.setAttribute("sqlResult", sqlResult);
        session.setAttribute("sqlStatement", sqlStatement);

        String url = "/index.jsp";
        getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);
    }
}

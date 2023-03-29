package equation_cheking;

import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;

public class DBWorker {

    final static Logger logger = Logger.getLogger(DBWorker.class);
    public Connection connection = null;
    final private String insertExpr = "insert into public.equations (expression) values(?);";
    final private String updateRootCount = "update public.equations set root_count = root_count + 1 where id = ?;";
    final private String getId = "select id from public.equations where \"expression\" = ?;";
    final private String insertRoot = "insert into public.roots (equation_id, root) values (?, ?);";
    final private String getEqWithRootCount = "select expression from public.equations where root_count = ?;";
    final private String getAllEquation = "select expression from public.equations;";

    public DBWorker(String url, String login, String pass) {

        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(url, login, pass);
        } catch (ClassNotFoundException e) {
            logger.error("Driver not found");
        } catch (SQLException e) {
            logger.error(e.getMessage());
            logger.error("Can not connect to db");
            System.exit(1);
        }
        logger.debug("Connection successful");
    }

    public DBWorker() {
        this("jdbc:postgresql://localhost:5432/db_equation", "postgres", "1234");
    }

    public void  insertEquation(String equation){
        try {
            PreparedStatement stmt = connection.prepareStatement(insertExpr);
            stmt.setString(1, equation);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Can not add to db");
        }

    }

    public void updateRootCount(int id){
        try {
            PreparedStatement stmt = connection.prepareStatement(updateRootCount);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Can not update root_count");
        }

    }

    public int getEquationId(String expression){

        ResultSet res = null;
        int id = 0;
        try {
            PreparedStatement stmt = connection.prepareStatement(getId);
            stmt.setString(1, expression);
            res = stmt.executeQuery();
            res.next();
            id = res.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id;

    }

    public void insertRoot(int equation_id, double root){
        try {
            PreparedStatement stmt = connection.prepareStatement(insertRoot);
            stmt.setInt(1,equation_id);
            stmt.setDouble(2, root);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<String> getAllEquations(){
        ResultSet set = null;
        ArrayList<String> arr = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            set = statement.executeQuery(getAllEquation);
            while (set.next()){
                arr.add(set.getString("expression"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return arr;
    }

    public ArrayList<String> getEquationWithRoots(int count){
        ArrayList<String> arr = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(getEqWithRootCount);
            preparedStatement.setInt(1, count);
            ResultSet set = preparedStatement.executeQuery();
            while (set.next()){
                arr.add(set.getString("expression"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return arr;
    }
}

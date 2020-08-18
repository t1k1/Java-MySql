import java.sql.*;
import java.util.Random;

public class Testing{

    static final String DATABASE_URL = "jdbc:mysql://localhost/test?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    static final String USER = "root";
    static final String PASSWORD = "qwerty";

    public static void createTable(Connection connection) throws SQLException {
        System.out.println("Creating table in selected database...");
        Statement statement = connection.createStatement();

        String SQL = "CREATE TABLE Person " +
                "(FIO VARCHAR(60), " +
                " Birthday VARCHAR(11), " +
                " Gender VARCHAR(10))";

        statement.executeUpdate(SQL);
        System.out.println("Table successfully created...");

        connection.close();
        statement.close();
    }

    public static void createRecord(Connection connection, String FIO, String date, String Gender) throws SQLException {
        System.out.println("Adding records in table...");
        Statement statement = connection.createStatement();

        String SQL = "INSERT Person(FIO, Birthday, Gender) " +
                "VALUES ('" + FIO + "', '" + date + "', '" + Gender + "')";

        statement.executeUpdate(SQL);
        System.out.println("Records successfully added...");

        connection.close();
        statement.close();
    }

    public static void getRecords(Connection connection) throws SQLException {
        System.out.println("Taking records from table...");
        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery("SELECT  DISTINCT FIO, Birthday, Gender FROM Person ORDER BY FIO");
        while (resultSet.next()) {
            String FIO = resultSet.getString(1);
            String date = resultSet.getString(2);
            String gender = resultSet.getString(3);
            String year = date.substring(0, 4);
            int age = 2020 - Integer.parseInt(year);

            System.out.printf("%s, %s, %s, %d \n", FIO, date, gender, age);
        }

        connection.close();
        statement.close();
    }

//    Заполнение автоматически  100 строк в которых
//    пол мужской и ФИО начинается с "F".

    public static String randomFIO() {
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder(20);
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = sb.toString();

        return output;
    }

    public static void fill(Connection connection) throws SQLException{
        System.out.println("Filling the table with records...");
        Statement statement = connection.createStatement();
        int     y = 1990 + (int) (Math.random() * 11),
                m = 1 + (int) (Math.random() * 11),
                d = 1 + (int) (Math.random() * 27);

        String SQL;
        for (int i = 0; i<100; i++){
            SQL = "INSERT Person(FIO, Birthday, Gender)" +
                    "VALUES ('F" + randomFIO() + "', '" + y + "-" + m + "-" + d + "', 'male')";

            statement.executeUpdate(SQL);
        }

        System.out.println("The table is filled with records successfully...");

        connection.close();
        statement.close();
    }

//    Результат выборки из таблицы по критерию: пол мужской, ФИО  начинается с "F".

    public static void select(Connection connection) throws SQLException{
        System.out.println("Select begin");
        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery("SELECT FIO, Gender FROM Person WHERE FIO LIKE 'F%' AND Gender='male'");
        while (resultSet.next()) {
            String gender = resultSet.getString(1);
            String FIO = resultSet.getString(2);

            System.out.printf("%s, %s \n", gender, FIO);
        }
        System.out.println("Select end");

        connection.close();
        statement.close();
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Connection connection = null;
        Statement statement = null;
        try {
            System.out.println("Registering JDBC driver...");
            Class.forName(JDBC_DRIVER);

            System.out.println("Creating connection to database...");
            connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);

//            createTable(connection);
//            createRecord(connection, "Enter your FIO", "Enter your birthday", "Enter your gender");
//            fill(connection);
//            select(connection);
//            getRecords(connection);

        }finally {
            if(statement!=null){
                statement.close();
            }
            if(connection!=null){
                connection.close();
            }
        }
    }
}
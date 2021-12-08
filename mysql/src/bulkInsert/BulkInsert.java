package bulkInsert;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BulkInsert {
    private final static String url = "jdbc:mysql://url:port/employees?allowLoadLocalInfile=true";
    private final static String user = "user";
    private final static String password = "password";
    private final static BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));

    public static void deleteEveryRowsFromTables() throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        List<String> batchTables = Arrays.asList("employees_batch_1000", "employees_batch_10000", "employees_batch_50000", "employees_batch_100000", "employees_batch_300000");
        List<String> loadTables = Arrays.asList("employees_load_1000", "employees_load_10000", "employees_load_50000", "employees_load_100000", "employees_load_300000");

        for (String table : batchTables) {
            String sql = "delete from " + table;
            try (
                    Connection con = DriverManager.getConnection(url, user, password);
                    PreparedStatement query = con.prepareStatement(sql)
            ) {
                query.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        for (String table : loadTables) {
            String sql = "delete from " + table;
            try (
                    Connection con = DriverManager.getConnection(url, user, password);
                    PreparedStatement query = con.prepareStatement(sql)
            ) {
                query.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<Employee> getEmployees(int count) throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String sql = "select * from employees order by emp_no limit " + count;
        List<Employee> employees = new ArrayList<>();
        try (
                Connection con = DriverManager.getConnection(url, user, password);
                PreparedStatement query = con.prepareStatement(sql);
                ResultSet resultSet = query.executeQuery()
        ) {
            while (resultSet.next()) {
                int empNo = resultSet.getInt("emp_no");
                LocalDate birthDate = resultSet.getDate("birth_date").toLocalDate();
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                char gender = resultSet.getString("gender").charAt(0);
                LocalDate hireDate = resultSet.getDate("hire_date").toLocalDate();
                employees.add(new Employee(empNo, birthDate, firstName, lastName, gender, hireDate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    public static void batchInsert(String table, List<Employee> employees) throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String sql = "insert into " + table + " values (?, ?, ?, ?, ?, ?)";
        try (
                Connection con = DriverManager.getConnection(url, user, password);
                PreparedStatement query = con.prepareStatement(sql)
        ) {
            con.setAutoCommit(false);

            int rowCount = 0;
            for (Employee employee : employees) {
                query.setInt(1, employee.getEmpNo());
                query.setDate(2, Date.valueOf(employee.getBirthDate()));
                query.setString(3, employee.getFirstName());
                query.setString(4, employee.getLastName());
                query.setString(5, String.valueOf(employee.getGender()));
                query.setDate(6, Date.valueOf(employee.getHireDate()));

                query.addBatch();
                query.clearParameters();
                rowCount++;

                if (rowCount % 1000 == 0 || rowCount == employees.size()) {
                    query.executeBatch();
                    query.clearBatch();
                    con.commit();
                }
            }

            query.executeBatch();
            con.commit();

            con.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void exportEmployeesIntoCSV(List<Employee> employees, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Employee employee : employees)
                writer.write(employee.toString() + "\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadDataIntoTable(String table, String fileName) throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String sql = "load data local infile '" + fileName + "' INTO TABLE " + table + " \n" +
                "CHARACTER SET utf8 \n" +
                "FIELDS TERMINATED BY ',' \n" +
                "LINES TERMINATED BY '\r\n' \n" +
                "(emp_no, birth_date, first_name, last_name, gender, hire_date)";
        try (
                Connection con = DriverManager.getConnection(url, user, password);
                PreparedStatement query = con.prepareStatement(sql)
        ) {
            query.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws ClassNotFoundException, IOException {
        deleteEveryRowsFromTables();

        for (int count : Arrays.asList(1000, 10000, 50000, 100000, 300000)) {
            List<Employee> employees = getEmployees(count);

            {
                long startAt = System.currentTimeMillis();
                String batchTable = "employees_batch_" + count;
                batchInsert(batchTable, employees);
                long endAt = System.currentTimeMillis();
                writer.write(employees.size() + " rows를 batch insert 하는데 걸린 시간: " + (endAt - startAt) + "\n");
            }

            {
                long startAt = System.currentTimeMillis();
                String fileName = "./mysql/files/employees_load_" + count + ".csv";
                exportEmployeesIntoCSV(employees, fileName);

                String loadTable = "employees_load_" + count;
                loadDataIntoTable(loadTable, fileName);
                long endAt = System.currentTimeMillis();
                writer.write(count + " rows를 data load 하는데 걸린 시간: " + (endAt - startAt) + "\n\n");
            }
            writer.flush();
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.daoImplementacion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import models.Account;
import models.Customer;
import models.DAO;
import models.Movement;
import models.Type;
import controller.utilidades.DataNotFoundException;

/**
 *
 * @author iorit
 */
public class DAOImplementacionBD implements DAO {

    // SQL Query
    // Customer
    private final String CREATE_CUSTOMER
            = "INSERT INTO CUSTOMER"
            + "(firstName,lastName,middleInitial,city,state,street,zip,phone,email)"
            + "values(?,?,?,?,?,?,?,?,?)";
    private final String SEARCH_CUSTOMER_ID = "SELECT * FROM CUSTOMER WHERE id = ?";
    private final String SEARCH_CUSTOMER_NAME = "SELECT * FROM CUSTOMER WHERE firstName = ? AND lastName = ?";
    private final String SEARCH_ACCOUNT_CUSTOMER
            = "SELECT ACCOUNT.* FROM (ACCOUNT "
            + "INNER JOIN customer_account ON customer_account.accounts_id = account.id)"
            + "WHERE customer_account.customers_id = ?";
    private final String CREATE_ACCOUNT = "INSERT INTO ACCOUNT"
            + "(description,balance,creditLine,beginBalance,beginBalanceTimestamp,type)"
            + "values(?,?,?,?,?,?)";
    private final String SEARCH_ACCOUNT_DATA = "SELECT * FROM ACCOUNT WHERE id = ?";

    // Config
    private final String URL = ResourceBundle.getBundle("controller.config").getString("url");
    private final String USER = ResourceBundle.getBundle("controller.config").getString("user");
    private final String PASS = ResourceBundle.getBundle("controller.config").getString("pass");
    private static Connection con = null;

    private void openConnection() {
        try {
            // Establecemos conexión a la base de datos
            con = DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            System.err.println(e);
        }

    }

    private void closeConnection() {
        try {
            con.close();
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    private void rollback() {
        try {
            con.rollback();
            con.setAutoCommit(true);
            System.err.println("Procediendo a revertir los cambios, iniciando rollback...");
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    @Override
    public Boolean createMovement(Customer cust, Movement mov) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Set<Movement> checkMovement(Account ac) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean createAccount(Account ac, Customer cus) {
        this.openConnection();
        try ( PreparedStatement stat = con.prepareStatement(CREATE_ACCOUNT)) {
            con.setAutoCommit(false);

            stat.setString(1, ac.getDescription());
            stat.setString(2, ac.getBalance().toString());
            stat.setString(3, ac.getCreditLine().toString());
            stat.setString(4, ac.getBeginBalance().toString());
            stat.setString(5, ac.getBeginBalanceTimestamp().toString());
            stat.setString(6, ac.getType().toString());

        } catch (SQLException e) {
            rollback();
            System.err.println(e);
            return false;
        }
        return null;
    }

    @Override
    public Boolean addCustomer(Account ac) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean checkAccountData(Account ac) {
        this.openConnection();
        ResultSet rs;
        try ( PreparedStatement stat = con.prepareStatement(SEARCH_ACCOUNT_DATA)) {
            con.setAutoCommit(false);

            stat.setLong(1, ac.getId());

            rs = stat.executeQuery();

            stat.setLong(1, ac.getId());
            stat.setString(2, ac.getDescription());
            stat.setDouble(3, ac.getBalance());
            stat.setDouble(4, ac.getCreditLine());
            stat.setDouble(5, ac.getBeginBalance());
            stat.setTimestamp(6, ac.getBeginBalanceTimestamp());
            stat.setString(7, ac.getType().toString());

        } catch (SQLException e) {
            rollback();
            System.err.println(e);
            return false;
        }
        return true;
    }

    @Override
    public Boolean createCustomer(Customer cus) {
        this.openConnection();

        // Try catch con recursos
        try ( PreparedStatement stat = con.prepareStatement(CREATE_CUSTOMER)) {
            con.setAutoCommit(false);

            stat.setString(1, cus.getFirstName());
            stat.setString(2, cus.getLastName());
            stat.setString(3, cus.getMiddleName());
            stat.setString(4, cus.getCity());
            stat.setString(5, cus.getState());
            stat.setString(6, cus.getStreetName());
            stat.setInt(7, cus.getZip());
            stat.setLong(8, cus.getPhone());
            stat.setString(9, cus.getEmail());

            stat.executeUpdate();

            con.commit();
            con.setAutoCommit(true);

            return true;

        } catch (SQLException e) {
            rollback();
            System.err.println(e);
            return false;
        } finally {
            this.closeConnection();
        }

    }

    @Override
    public Customer checkCustomerData(Customer cus) throws DataNotFoundException {
        this.openConnection();
        Customer customer = null;
        ResultSet rs;

        // Try catch con recursos
        try ( PreparedStatement stat = (cus.getId() >= 0L
                ? con.prepareStatement(SEARCH_CUSTOMER_ID)
                : con.prepareStatement(SEARCH_CUSTOMER_NAME))) {

            if (cus.getId() >= 0L) {
                stat.setLong(1, cus.getId());
            } else {
                stat.setString(1, cus.getFirstName());
                stat.setString(2, cus.getLastName());
            }

            rs = stat.executeQuery();

            if (rs.next()) {
                customer = new Customer();
                customer.setId(rs.getLong(1));
                customer.setCity(rs.getString(2));
                customer.setEmail(rs.getString(3));
                customer.setFirstName(rs.getString(4));
                customer.setLastName(rs.getString(5));
                customer.setMiddleName(rs.getString(6));
                customer.setPhone(rs.getLong(7));
                customer.setState(rs.getString(8));
                customer.setStreetName(rs.getString(9));
                customer.setZip(rs.getInt(10));
            }
        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            this.closeConnection();
            if (customer == null) {
                throw new DataNotFoundException("No se ha encontrado al cliente con los datos introducidos.");
            }
        }
        return customer;
    }

    @Override
    public Set<Account> checkCustomerAccounts(Customer cus) throws DataNotFoundException {

        this.openConnection();
        Set<Account> accounts = new HashSet<>();
        Account acc = null;
        ResultSet rs;

        // Try catch con recursos
        try ( PreparedStatement stat = con.prepareStatement(SEARCH_ACCOUNT_CUSTOMER)) {

            stat.setLong(1, cus.getId());

            rs = stat.executeQuery();

            while (rs.next()) {
                acc = new Account();
                acc.setId(rs.getLong(1));
                acc.setBalance(rs.getDouble(2));
                acc.setBeginBalance(rs.getDouble(3));
                acc.setBeginBalanceTimestamp(rs.getTimestamp(4));
                acc.setCreditLine(rs.getDouble(5));
                acc.setDescription(rs.getString(6));
                acc.setType(rs.getInt(7) == 0 ? Type.STANDAR : Type.CREDIT);
                accounts.add(acc);
            }

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            this.closeConnection();
            if (acc == null) {
                throw new DataNotFoundException("No se ha encontrado al cliente con los datos introducidos.");
            }
        }
        return accounts;

    }

}

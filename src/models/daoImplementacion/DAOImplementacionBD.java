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

    // Account
    private final String CREATE_ACCOUNT = "INSERT INTO ACCOUNT"
            + "(description,balance,creditLine,beginBalance,beginBalanceTimestamp,type)"
            + "values(?,?,?,?,?,?)";
    private final String SEARCH_ACCOUNT_DATA = "SELECT * FROM ACCOUNT WHERE id = ?";

    // Movement
    private final String SEARCH_MOVEMENTS = "SELECT * FROM MOVEMENT WHERE acount_id = ?";
    private final String CREATE_MOVEMENT = "INSERT INTO movement VALUES(?,?,?,?,?,?)";
    private final String SEARCH_MOVEMENT_ID = "SELECT MAX(movement.id) FROM moveent";

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
        Boolean created = false;
        this.openConnection();
        PreparedStatement stat = null;
        Long movementID = searchMovementId();

        try {
            if (movementID > 0) {
                stat = con.prepareStatement(CREATE_MOVEMENT);

                stat.setLong(1, movementID + 1);
                stat.setDouble(2, mov.getAmount());
                stat.setDouble(3, mov.getBalance());
                stat.setString(4, mov.getDescription());
                stat.setTimestamp(5, mov.getDate());
                stat.setLong(6, mov.getAccount_id());

                if (stat.executeUpdate() > 0) {
                    created = true;
                }
            }

        } catch (SQLException e) {
            created = false;
        } finally {
            this.closeConnection();
        }

        return created;
    }

    @Override
    public Set<Movement> checkMovement(Account ac) throws DataNotFoundException {
        this.openConnection();
        Set<Movement> movements = new HashSet<>();
        Movement mov = null;
        ResultSet rs;

        // Try catch con recursos
        try ( PreparedStatement stat = con.prepareStatement(SEARCH_MOVEMENTS)) {

            stat.setLong(1, mov.getAccount_id());

            rs = stat.executeQuery();

            while (rs.next()) {
                mov = new Movement();

                mov.setId(rs.getLong(1));
                mov.setAmount(rs.getDouble(2));
                mov.setBalance(rs.getDouble(3));
                mov.setDescription(rs.getString(4));
                mov.setDate(rs.getTimestamp(5));
                mov.setAccount_id(ac.getId());

                movements.add(mov);
            }

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            this.closeConnection();
            if (mov == null) {
                throw new DataNotFoundException("No se ha encontrado al cliente con los datos introducidos.");
            }
        }

        return movements;
    }

    @Override
    public Boolean createAccount(Account ac, Customer cus) {
        this.openConnection();
        try ( PreparedStatement stat = con.prepareStatement(CREATE_ACCOUNT)) {
            stat.setString(1, ac.getDescription());
            stat.setDouble(2, ac.getBalance());
            stat.setDouble(3, ac.getCreditLine());
            stat.setDouble(4, ac.getBeginBalance());
            stat.setTimestamp(5, ac.getBeginBalanceTimestamp());

            if (ac.getType().equals(Type.STANDAR)) {
                stat.setInt(6, Type.STANDAR.getType());
            }
            if (ac.getType().equals(Type.CREDIT)) {
                stat.setInt(6, Type.CREDIT.getType());
            }

            stat.executeUpdate();

        } catch (SQLException e) {
            rollback();
            System.err.println(e);
            return false;
        } finally {
            this.closeConnection();
        }
        return true;
    }

    @Override
    public Boolean addAccountToCustomer(Customer cus, Account ac) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Account checkAccountData(Account ac) throws DataNotFoundException {
        this.openConnection();
        ResultSet rs;
        Account account = null;
        try ( PreparedStatement stat = con.prepareStatement(SEARCH_ACCOUNT_DATA)) {
            con.setAutoCommit(false);

            stat.setLong(1, ac.getId());

            rs = stat.executeQuery();

            account = new Account();

            account.setId(ac.getId());
            account.setBalance(rs.getDouble(2));
            account.setBeginBalance(rs.getDouble(3));
            account.setBeginBalanceTimestamp(rs.getTimestamp(4));
            account.setCreditLine(rs.getDouble(5));
            account.setDescription(rs.getString(6));
            if (rs.getInt(7) == 0) {
                account.setType(Type.valueOf("STANDAR"));
            }
            if (rs.getInt(7) == 1) {
                account.setType(Type.valueOf("CREDIT"));
            }

        } catch (SQLException e) {
            System.err.println(e);

        } finally {
            this.closeConnection();
            if (account == null) {
                throw new DataNotFoundException("No se ha encontrado la cuenta con los datos introducidos");
            }
        }
        return account;
    }

    @Override
    public Boolean createCustomer(Customer cus) {
        this.openConnection();

        // Try catch con recursos
        try ( PreparedStatement stat = con.prepareStatement(CREATE_CUSTOMER)) {

            stat.setString(1, cus.getFirstName());
            stat.setString(2, cus.getLastName());
            stat.setString(3, cus.getMiddleName());
            stat.setString(4, cus.getCity());
            stat.setString(5, cus.getState());
            stat.setString(6, cus.getStreetName());
            stat.setInt(7, cus.getZip());
            stat.setLong(8, cus.getPhone());
            stat.setString(9, cus.getEmail());

            return stat.executeUpdate() > 0;

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

            if (cus.getId() != null) {
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
                throw new DataNotFoundException("No se ha encontrado ninguna cuenta del cliente con los datos introducidos.");
            }
        }
        return accounts;

    }

    /**
     * This method search for the lastest movement id
     *
     * @return Returns the id of the last movement, if there has been any type
     * of exception it will return -1
     */
    private Long searchMovementId() {
        Long id = null;

        this.openConnection();
        ResultSet rs;

        // Try catch con recursos
        try ( PreparedStatement stat = con.prepareStatement(SEARCH_MOVEMENTS)) {
            rs = stat.executeQuery();

            if (rs.next()) {
                id = rs.getLong(1);
            }

        } catch (SQLException e) {
            System.err.println(e);
            id = (long) -1;
        } finally {
            this.closeConnection();

        }

        return id;
    }

}

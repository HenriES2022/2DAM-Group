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
    private final String ADD_CUSTOMER_ACCOUNT = "INSERT INTO CUSTOMER_ACCOUNT values(?,?)";

    // Movement
    private final String SEARCH_MOVEMENTS = "SELECT * FROM MOVEMENT WHERE account_id = ?";
    private final String CREATE_MOVEMENT = "INSERT INTO movement(amount, balance, description, timestamp, account_id) VALUES(?,?,?,?,?)";
    private final String UPDATE_BALANCE = "UPDATE account set balance = ? where id = ?";

    // Config
    private final String URL = ResourceBundle.getBundle("controller.config").getString("url");
    private final String USER = ResourceBundle.getBundle("controller.config").getString("user");
    private final String PASS = ResourceBundle.getBundle("controller.config").getString("pass");
    private static Connection con = null;

    //Metodo para abrir la conexion con la base de datos
    private void openConnection() {
        try {
            // Establecemos conexión a la base de datos
            con = DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            System.err.println(e);
        }

    }

    //Metodo para cerrar la conexion con la base de datos
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

        try ( PreparedStatement statCreate = con.prepareStatement(CREATE_MOVEMENT);  PreparedStatement statUpdate = con.prepareStatement(UPDATE_BALANCE);) {
            con.setAutoCommit(false);

            statCreate.setDouble(1, mov.getAmount());
            statCreate.setDouble(2, mov.getBalance());
            statCreate.setString(3, mov.getDescription());
            statCreate.setTimestamp(4, mov.getDate());
            statCreate.setLong(5, mov.getAccount_id());
            statCreate.executeUpdate();

            if (mov.getDescription().equalsIgnoreCase("Deposit")) {
                statUpdate.setDouble(1, mov.getBalance() + mov.getAmount());
            } else if (mov.getDescription().equalsIgnoreCase("Payment")) {
                statUpdate.setDouble(1, mov.getBalance() - mov.getAmount());
            }
            statUpdate.setLong(2, mov.getAccount_id());
            statUpdate.executeUpdate();
            con.commit();
            con.setAutoCommit(true);
            created = true;

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

            stat.setLong(1, ac.getId());

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
                throw new DataNotFoundException("No se ha encontrado movimientos en esta cuenta.");
            }
        }

        return movements;
    }

    @Override
    public Boolean createAccount(Account ac, Customer cus) {
        this.openConnection();
        ResultSet rs;

        try ( PreparedStatement stat = con.prepareStatement(CREATE_ACCOUNT, PreparedStatement.RETURN_GENERATED_KEYS);  PreparedStatement statCusAcc = con.prepareStatement(ADD_CUSTOMER_ACCOUNT)) {
            con.setAutoCommit(false);

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
            rs = stat.getGeneratedKeys();

            if (rs.next()) {
                statCusAcc.setLong(1, cus.getId());
                statCusAcc.setLong(2, rs.getLong(1));
                statCusAcc.executeUpdate();
                con.commit();
                con.setAutoCommit(true);
            } else {
                return false;
            }

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
        this.openConnection();
        try ( PreparedStatement stat = con.prepareStatement(ADD_CUSTOMER_ACCOUNT)) {

            stat.setLong(1, cus.getId());
            stat.setLong(2, ac.getId());

            stat.executeUpdate();

        } catch (SQLException e) {
            System.err.println(e);
            return false;
        } finally {
            this.closeConnection();
        }
        return true;
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

            if (rs.next()) {
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
        try ( PreparedStatement stat = (cus.getId() != null
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

}

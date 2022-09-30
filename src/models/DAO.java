/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import controller.utilidades.DataNotFoundException;
import java.util.Set;

/**
 *
 * @author iorit
 */
public interface DAO {

    public Boolean createMovement(Customer cust, Movement mov);

    public Set<Movement> checkMovement(Account ac) throws DataNotFoundException;

    public Boolean createAccount(Account ac);

    public Boolean addCustomer(Account ac);

    public Boolean checkAccountData(Account ac);

    public Boolean createCustomer(Customer cus);

    public Customer checkCustomerData(Customer cus) throws DataNotFoundException;

    public Set<Account> checkCustomerAccounts(Customer cus) throws DataNotFoundException;
}

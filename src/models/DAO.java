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

    public Boolean createAccount(Account ac, Customer cus);

    public Boolean addAccountToCustomer(Customer cus, Account ac);
    
    /**
     * Este metodo muestra la informacion sobre una cuenta
     * @param ac Esta es la cuenta de la que queremos ver la informacion
     * @return Devuelve una coleccion con los datos de la cuenta
     * @throws DataNotFoundException 
     */
    public Account checkAccountData(Account ac) throws DataNotFoundException;
    
    /**
     * Este metodo crea un Cliente
     * @param cus Es el Cliente que se va a crear
     * @return Devuelve el Cliente ya creado
     */
    public Boolean createCustomer(Customer cus);

    /**
     * Este metodo muestra la informacion de un Cliente
     * @param cus Es el cliente del cual se va a mostrar la informacion
     * @return Devuelve una coleccion con los datos del Cliente
     * @throws DataNotFoundException 
     */
    public Customer checkCustomerData(Customer cus) throws DataNotFoundException;

    /**
     * Este metodo muestra las cuentas que posee un Cliente
     * @param cus Es el cliente del que se quiere ver las cuentas
     * @return Devuelve una coleccion con las cuentas del Cliente
     * @throws DataNotFoundException 
     */
    public Set<Account> checkCustomerAccounts(Customer cus) throws DataNotFoundException;
}

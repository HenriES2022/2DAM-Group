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
    /**
     * Este metodo crea un movimiento en una cuenta
     * @param cust Este cliente sirve para poder acceder a sus cuentas
     * @param mov Este es el movimiento que se va a guardar
     * @return Devuelve true si se ha creado correctamente, false si ha habido algun error al crear
     */
    public Boolean createMovement(Customer cust, Movement mov);

    /**
     * Este metodo obtiene los movimientos de una cuenta
     * @param ac Es la cuenta de la que queremos obtener los movimientos
     * @return Devuelve una coleccion con los movimientos
     * @throws DataNotFoundException En caso de que no se haya encontrado la informacion necesaria se lanzara la excepcion con un mensaje
     */
    public Set<Movement> checkMovement(Account ac) throws DataNotFoundException;
    
    /**
     * Este metodo crea una cuenta
     * @param ac Es la cuenta que se va a crear
     * @param cus Es el cliente al que se le va a crear la cuenta
     * @return Devuelve un true si se ha creado correctamente, false si ha habido algun error al crear
     */
    public Boolean createAccount(Account ac, Customer cus);

    /**
     * Este metodo añade una cuenta a un cliente
     * @param cus El cliente al que queremos añadir la cuenta
     * @param ac La cuenta que queremos añadir al cliente
     * @return Devuelve un true si se ha añadido correctamente, false si ha habido algun error al añadir
     */
    public Boolean addAccountToCustomer(Customer cus, Account ac);

    public Account checkAccountData(Account ac) throws DataNotFoundException;

    public Boolean createCustomer(Customer cus);

    public Customer checkCustomerData(Customer cus) throws DataNotFoundException;

    public Set<Account> checkCustomerAccounts(Customer cus) throws DataNotFoundException;
}

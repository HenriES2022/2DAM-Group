/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import controller.utilidades.Util;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import models.Account;
import models.Customer;
import models.DAO;
import models.Movement;

/**
 *
 * @author yeguo
 */
public class Controller {
    private DAO myDao = DAOFactory.getDao();
    /**
     * Este metodo obtiene la informacion para  crear un movimiento en una cuenta determinada
     */
    public void createMovement(){
        Integer idCustomer;
        Integer accountIdSelected;
        Float movementAmount;
        String movementType = null;
        Movement mov = null;
        Account ac = null;
        List<Account> accounts = null;
        Customer cus = new Customer();
        
        idCustomer = Util.leerInt("Introduce el id del cliente del que quiere ver las cuentas");
        cus.setId(idCustomer);
        
        
        accounts = myDao.checkAccount(cus);
        
        //Cabeceras de la informacion de las cuentas
        System.out.println("---CUENTAS---");
        System.out.println("ID    DESCRIPCION    BALANCE    LINEA_CREDITO    SALDO_INICIAL    FECHA_SALDO_INICIAL    TIPO");
        
        showAccounts(accounts);
        accountIdSelected = Util.leerInt("Introduce el id de la cuenta en la que quiere crear un movimiento");
        
        ac = searchAccount(accountIdSelected, accounts);
        
        mov = new Movement();
        mov.setAccount_id(accountIdSelected);
        
        //Este bucle comprueba que en el caso de que la opcion seleccionada no sea correcta o que se ha introducido mal siga pidiendole al usuario que vuelva a introducir una opcion
        do {            
            movementType = Util.introducirCadena("Que tipo de movimiento quiere(Deposito/pago)");
            if (movementType.equalsIgnoreCase("Deposito")) {
                mov.setDescription("Deposit");
            }
            else if(movementType.equalsIgnoreCase("Pago")){
                mov.setDescription("Payment");
            } else{
                movementType = null;
            }
        } while (movementType == null || movementType.isEmpty());
        
        mov.setBalance(ac.getBalance());
        
        movementAmount = Util.leerFloat("Introduce la cantidad del movimiento");
        mov.setAmount(movementAmount);
        
        mov.setDate(Timestamp.valueOf(LocalDateTime.now()));
        myDao.createMovement(cus, mov);
    }
    
    /**
     * Este metodo mustra la informacion de los movimientos de una cuenta
     */
    public void checkMovements() {
        Integer idCustomer;
        Integer accountIdSelected;
        Customer cus = new Customer();
        Account ac = null;
        List<Account> accounts = null;
        
        
        idCustomer = Util.leerInt("Introduce el id del cliente del que quiere ver las cuentas");
        cus.setId(idCustomer);
        
        accounts = myDao.checkAccount(cus);

        //Cabeceras de la informacion de las cuentas
        System.out.println("---CUENTAS---");
        System.out.println("ID    DESCRIPCION    BALANCE    LINEA_CREDITO    SALDO_INICIAL    FECHA_SALDO_INICIAL    TIPO");
        

        showAccounts(accounts);
        
        accountIdSelected = Util.leerInt("Introduce el id de la cuenta de la que quiere ver los movimientos");
        try {
             ac = searchAccount(accountIdSelected, accounts);
             ac.showMovements();
        } catch (NullPointerException e) {
            System.out.println("No se ha encontrado una cuenta con el id introducido");
        }
       
        
    }
    /**
     * Este metodo busca una cuenta en una lista de cuentas
     * @param id Es el id de la cuenta que buscamos
     * @param accounts Es la lista de cuentas de este cliente
     * @return Retorna la cuenta que corresponde al id enviado, Retorna nulo en el caso de que no se encuentre la cuenta con ese id
     */
    public Account searchAccount(Integer id, List<Account> accounts){
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getId().equals(id)) {
                return accounts.get(i);
            }
        }
        return null;
    }
    /**
     * Este metodo muestra la informacion de las cuentas en la lista de las cuentas
     * @param accounts Es la lista de cuentas que se va a mostrar
     */
    public void showAccounts(List<Account> accounts){
         for (Account account : accounts) {
            System.out.println(account.getId() +"    "+ account.getDescription() 
                    +"    "+ account.getBalance() + "    " + 
                    account.getCreditLine() +"    "+ account.getBeginBalance()+"    "+
                    account.getBeginBalanceTimestamp()+"    "+ account.getType()
            );
        }
    }
}

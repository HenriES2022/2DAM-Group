/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import controller.utilidades.Util;
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
    
    
    public void createAccount() {
        Account ac = new Account();
        ac.setId(Util.leerInt("Insertar ID: "));
        ac.setDescription(Util.introducirCadena("Insertar Descripcion: "));
        ac.setBalance(Util.leerFloat("Introducir Balance: "));
        ac.setCreditLine(Util.leerFloat("Introducir Linea de Credito: "));
        ac.setBeginBalance(Util.leerFloat("Introducir Begin Balance: "));
        ac.setBeginBalanceTimestamp(Util.leerFloat("Introducir Begin Balance Timestamp: "));
        
        myDao.createAccount(ac);
    }

    public void checkAccountData(Account ac) {
        int id = Util.leerInt("Insertar ID de una Cuenta");
        if(ac.getId().equals(id)){
            System.out.println("ID: " + ac.getId());
            System.out.println("Description: " + ac.getDescription());
            System.out.println("Balance: " +ac.getBalance());
            System.out.println("Credit Line: " +ac.getCreditLine());
            System.out.println("Begin Balance: " +ac.getBeginBalance());
            System.out.println("Begin Balance Timestamp: " + ac.getBeginBalanceTimestamp());
            System.out.println("Type: " +ac.getType());
        }else{
            System.out.println("La Cuenta introducida no existe");
        }
    }

    public void checkMovements() {
        Integer idCustomer;
        Integer accountIdSelected;
        Customer cus = new Customer();
        Account ac = null;
        List<Account> accounts = null;
        
        
        idCustomer = Util.leerInt("Introduce el id del cliente del que quiere ver las cuentas");
        cus.setId(idCustomer);
        
        accounts = myDao.checkAccount(cus);

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

    public void createMovement() {
        Movement mov = null;
        String customerId;
        Account ac = null;
        
    }
    
    public Account searchAccount(Integer id, List<Account> accounts){
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getId().equals(id)) {
                return accounts.get(i);
            }
        }
        return null;
    }
    
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

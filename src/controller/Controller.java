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

    public void checkMovements() {
        Integer idCustomer;
        Integer accountIdSelected;
        Customer cus = new Customer();
        List<Account> accounts = null;
        DAO myDao = DAOFactory.getDao();
        
        
        idCustomer = Util.leerInt("Introduce el id del cliente del que quiere ver las cuentas");
        cus.setId(idCustomer);
        
        accounts = myDao.checkAccount(cus);

        System.out.println("---CUENTAS---");
        System.out.println("ID    DESCRIPCION    BALANCE    LINEA_CREDITO    SALDO_INICIAL    FECHA_SALDO_INICIAL    TIPO");
        

        for (Account account : accounts) {
            System.out.println(account.getId() +"    "+ account.getDescription() 
                    +"    "+ account.getBalance() + "    " + 
                    account.getCreditLine() +"    "+ account.getBeginBalance()+"    "+
                    account.getBeginBalanceTimestamp()+"    "+ account.getType()
            );
        }
        
        accountIdSelected = Util.leerInt("Introduce el id de la cuenta de la que quiere ver los movimientos");
        
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getId().equals(accountIdSelected)) {
                accounts.get(i).showMovements();
            }
        }
        
        
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import controller.utilidades.Util;
import models.Account;

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
    
    public void addCustomer(Account ac){
        
    }
}

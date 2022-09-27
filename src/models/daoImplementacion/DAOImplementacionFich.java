/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.daoImplementacion;

import controller.utilidades.Util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Account;
import models.Customer;
import models.DAO;
import models.Movement;

/**
 *
 * @author iorit
 */
public class DAOImplementacionFich implements DAO {
    private File fich = new File("BankFich.obj");

    @Override
    public Boolean createMovement(Customer cust, Movement mov) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Set<Movement> checkMovement(Account ac) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean createAccount(Account ac) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean addCustomer(Account ac) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean checkAccountData(Account ac) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean createCustomer(Customer cus) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Customer checkCustomerData(Customer cus) {
        return null;
    }

    @Override
    public Set<Account> checkCustomerAccounts(Customer cus) {
        return null;
    }
    
    private Set<Customer> dumpFileToSet(){
        Set<Customer> customers = new HashSet<>();
        
        if (fich.exists()) {
            Customer cus = null;
            FileInputStream fis = null;
            ObjectInputStream ois = null;
            int fileSize = Util.calculoFichero(fich);
            
            try {
                fis = new FileInputStream(fich);
                ois = new ObjectInputStream(ois);
                
                for (int i = 0; i < fileSize; i++) {
                    cus = (Customer) ois.readObject();
                    customers.add(cus);
                }
            } catch (FileNotFoundException ex) {
                System.out.println("El fichero no existe");
                ex.printStackTrace();
            } catch (IOException ex) {
                System.out.println("Error lectura de fichero");
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            } finally{
                try {
                    ois.close();
                    fis.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                
            }
            
            
        } else{
            System.out.println("El fichero no existe");
        }
        
        return customers;
    }
}

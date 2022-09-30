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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    public boolean volcarSetFichero(Set<Customer> customers) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;

        if (!customers.isEmpty() && customers != null) {
            try {
                fos = new FileOutputStream(fich);
                oos = new ObjectOutputStream(fos);

                for (Customer cus : customers) {
                    oos.writeObject(cus);
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(DAOImplementacionFich.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(DAOImplementacionFich.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    oos.flush();
                    oos.close();
                    fos.close();
                } catch (IOException ex) {
                    Logger.getLogger(DAOImplementacionFich.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public Boolean createMovement(Customer cust, Movement mov) {
        Boolean created = false;
        Set<Customer> customers = dumpFileToSet();
        List<Movement> movements = chargeAllMovements(customers);
        
        
        for (Customer customer : customers) {
            if (customer.getId().equals(cust.getId())) {
                for (Account customerAccount : customer.getCustomerAccounts()) {
                    if (customerAccount.getId().equals(mov.getAccount_id())) {
                        if (movements.isEmpty()) {
                            mov.setId((long) 1);
                        } else{
                            mov.setId((long)movements.get(movements.size()-1).getId()+1);
                        }
                        customerAccount.getAccountMovements().add(mov);
                        updateBalance(customerAccount, mov);
                        created = true;
                    }
                }
            }
        }
        
        if (created) {
            volcarSetFichero(customers);
        }
        
        return created;
    }

    @Override
    public Set<Movement> checkMovement(Account ac) {
        Set<Customer> customers = dumpFileToSet();
        Set<Movement> movements = new HashSet<>();

        for (Customer customer : customers) {
            for (Account account : customer.getCustomerAccounts()) {
                if (ac.getId() != null && account.getId().equals(ac.getId())) {
                    for (Movement accountMovement : account.getAccountMovements()) {
                        movements.add(accountMovement);
                    }
                    return movements;
                }
            }
            

        }

        return null;
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

    private Set<Customer> dumpFileToSet() {
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
            } finally {
                try {
                    ois.close();
                    fis.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }

        } else {
            System.out.println("El fichero no existe");
        }

        return customers;
    }

    private void updateBalance(Account customerAccount, Movement mov) {
        if (mov.getDescription().equalsIgnoreCase("Deposit")) {
            customerAccount.setBalance(customerAccount.getBalance() + mov.getAmount());
        }
        else if(mov.getDescription().equalsIgnoreCase("Payment")){
            customerAccount.setBalance(customerAccount.getBalance() - mov.getAmount());
        }
    }

    private List<Movement> chargeAllMovements(Set<Customer> customers) {
        List<Movement> ret = new ArrayList<>();
        
        for (Customer customer : customers) {
            for (Account customerAccount : customer.getCustomerAccounts()) {
                for (Movement accountMovement : customerAccount.getAccountMovements()) {
                    ret.add(accountMovement);
                }
            }
        }
        
        return ret;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.daoImplementacion;

import controller.utilidades.DataNotFoundException;
import controller.utilidades.Util;
import java.io.File;
import java.io.FileInputStream;
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

    private final File fich = new File("BankFich.obj");
    private static Set<Customer> customerSet = null;

    /**
     * Este metodo obtiene los datos de una coleccion set y los vuelca a al
     * fichero
     *
     * @param customers La coleccion de clientes que se quiere volcar
     * @return Devuelve un true si se ha hecho el volcado correctamente , false
     * si ha habido algun error
     */
    private boolean volcarSetFichero(Set<Customer> customers) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;

        if (customers != null) {
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
                    if (oos != null) {
                        oos.flush();
                        oos.close();
                    }
                    if (fos != null) {
                        fos.close();
                    }
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
        customerSet = dumpFileToSet();
        Customer customer;
        try {
            customer = checkCustomerData(cust);
            List<Movement> movements = chargeAllMovements(customerSet);

            for (Account customerAccount : customer.getCustomerAccounts()) {
                if (customerAccount.getId().equals(mov.getAccount_id())) {
                    if (movements.isEmpty()) {
                        mov.setId((long) 1);
                    } else {
                        mov.setId((long) movements.get(movements.size() - 1).getId() + 1);
                    }
                    customerAccount.getAccountMovements().add(mov);
                    updateBalance(customerAccount, mov);
                    created = true;
                }
            }
            if (created) {
                volcarSetFichero(customerSet);
            }
        } catch (DataNotFoundException ex) {
            Logger.getLogger(DAOImplementacionFich.class.getName()).log(Level.SEVERE, null, ex);
        }

        return created;
    }

    @Override
    public Set<Movement> checkMovement(Account ac) {
        customerSet = dumpFileToSet();
        Set<Movement> movements = new HashSet<>();

        for (Customer customer : customerSet) {
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
    public Boolean createAccount(Account ac, Customer cus) {
        boolean created = false;
        customerSet = dumpFileToSet();
        Long id = 0L;

        try {
            Customer customer = checkCustomerData(cus);
            customerSet.remove(customer);

            if (!customerSet.isEmpty()) {
                for (Customer customerForEach : customerSet) {
                    for (Account customerAccount : customerForEach.getCustomerAccounts()) {
                        if (id < customerAccount.getId()) {
                            id = customerAccount.getId();
                        }
                    }
                }
            }

            ac.setId(++id);

            customer.getCustomerAccounts().add(ac);

            customerSet.add(customer);
            created = true;
        } catch (DataNotFoundException ex) {
            Logger.getLogger(DAOImplementacionFich.class.getName()).log(Level.SEVERE, "No se ha encontrado al Cliente", ex);
            created = false;
        }
        if (created) {
            volcarSetFichero(customerSet);
        }
        return created;
    }

    @Override
    public Boolean addAccountToCustomer(Customer cus, Account ac) {
        Boolean modified = false;
        Customer customer = null;

        try {
            customer = checkCustomerData(cus);

            for (Customer cusSet : customerSet) {
                for (Account customerAccount : cusSet.getCustomerAccounts()) {
                    if (customerAccount.getId().equals(ac.getId())) {
                        ac = customerAccount;
                        modified = true;
                    }
                }
            }
            if (modified) {
                customerSet.remove(cus);
                customer.getCustomerAccounts().add(ac);
                customerSet.add(cus);
                volcarSetFichero(customerSet);
            } else {
                return false;
            }
        } catch (DataNotFoundException ex) {
            Logger.getLogger(DAOImplementacionFich.class.getName()).log(Level.SEVERE, null, ex);
            modified = false;
        }

        return modified;
    }

    @Override
    public Account checkAccountData(Account ac) throws DataNotFoundException {
        customerSet = dumpFileToSet();

        for (Customer customer : customerSet) {
            for (Account customerAccount : customer.getCustomerAccounts()) {
                if (customerAccount.getId().equals(ac.getId())) {
                    return customerAccount;
                }
            }
        }

        throw new DataNotFoundException("No se ha encontrado la cuenta que busca");
    }

    /**
     * Este método añadirá el nuevo cliente al fichero donde se guardarán todos
     * los clientes registrados
     *
     * @param cus Se le pasa el objecto Customer con todos los datos
     * introducidos menos el ID
     * @return Boolean devuelve un booleano dependiendo si se ha creado
     * correctamente
     */
    @Override
    public Boolean createCustomer(Customer cus) {

        Set<Customer> customers = dumpFileToSet();
        Long id = 0L;

        if (!customers.isEmpty()) {
            for (Customer customer : customers) {
                if (id < customer.getId()) {
                    id = customer.getId();
                }
            }
        }
        cus.setId(id + 1);
        customers.add(cus);

        return volcarSetFichero(customers);
    }

    @Override
    public Customer checkCustomerData(Customer cus) throws DataNotFoundException {

        Set<Customer> customers = dumpFileToSet();

        if (cus.getId() != null) {
            Long id = cus.getId();
            for (Customer customer : customers) {
                if (customer.getId().equals(id)) {
                    return customer;
                }
            }
            throw new DataNotFoundException("No se ha encontrado el cliente con ese ID");
        } else {
            String firstName = cus.getFirstName();
            String lastName = cus.getLastName();
            for (Customer customer : customers) {
                if (customer.getFirstName().equalsIgnoreCase(firstName)
                        && customer.getLastName().equalsIgnoreCase(lastName)) {
                    return customer;
                }
            }
            throw new DataNotFoundException("No se ha encontrado el cliente con ese Nombre y apellido");
        }
    }

    @Override
    public Set<Account> checkCustomerAccounts(Customer cus) {

        try {
            Customer customer = checkCustomerData(cus);
            if (!customer.getCustomerAccounts().isEmpty()) {
                return customer.getCustomerAccounts();
            }

        } catch (DataNotFoundException ex) {
            Logger.getLogger(DAOImplementacionFich.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    /**
     * Este metodo vuelca el fichero a una coleccion tipo Set
     *
     * @return Devielve un set con los datos del fichero
     */
    private Set<Customer> dumpFileToSet() {

        if (customerSet != null) {
            return customerSet;
        } else {
            customerSet = new HashSet<>();
        }
        if (fich.exists()) {
            Customer cus = null;
            FileInputStream fis = null;
            ObjectInputStream ois = null;
            int fileSize = Util.calculoFichero(fich);

            try {
                fis = new FileInputStream(fich);
                ois = new ObjectInputStream(fis);

                for (int i = 0; i < fileSize; i++) {
                    cus = (Customer) ois.readObject();
                    customerSet.add(cus);
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
                    if (ois != null) {
                        ois.close();
                    }
                    if (fis != null) {
                        fis.close();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        return customerSet;
    }

    /**
     * Este metodo actualiza el balance cuando se crea un movimiento en esa
     * cuenta
     *
     * @param customerAccount La cuenta que se va a actualizar
     * @param mov El movimiento con la informacion de tipo de movimiento y
     * cantidad
     */
    private void updateBalance(Account customerAccount, Movement mov) {
        if (mov.getDescription().equalsIgnoreCase("Deposit")) {
            customerAccount.setBalance(customerAccount.getBalance() + mov.getAmount());
        } else if (mov.getDescription().equalsIgnoreCase("Payment")) {
            customerAccount.setBalance(customerAccount.getBalance() - mov.getAmount());
        }
    }

    /**
     * Un metodo que carga una lista con todos los movimientos de todas las
     * cuentas
     *
     * @param customers La lista de clientes de las que se obtendran las cuentas
     * @return Devuelve una lista con los movimientos de todas las cuentas
     */
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

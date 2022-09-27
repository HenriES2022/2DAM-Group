/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import controller.utilidades.DataNotFoundException;
import controller.utilidades.Util;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import models.Account;
import models.Customer;
import models.DAO;
import models.Movement;

/**
 *
 * @author yeguo
 */
public class Controller {

    private final DAO dao = DAOFactory.getDAO();

    /**
     * Método para crear un cliente
     *
     */
    public void createCustomer() {
        // Pedir todos los datos del cliente
        Customer customer = new Customer();
        customer.setFirstName(Util.introducirCadena("Introduzca el 1º nombre: "));
        customer.setMiddleName(Util.introducirCadena("Introduzca el 2º nombre: "));
        customer.setLastName(Util.introducirCadena("Introduzca el apellido: "));
        customer.setStreetName(Util.introducirCadena("Introduzca el nombre de la calle: "));
        customer.setCity(Util.introducirCadena("Introduzca la ciudad: "));
        customer.setState(Util.introducirCadena("Introduzca la region: "));
        customer.setPhone(Util.leerInt("Introduzca el número de teléfono: "));
        customer.setZip(Util.leerInt("Introduzca el número Zip: "));
        customer.setEmail(Util.introducirCadena("Introduzca el email: "));
        // Crear cliente
        dao.createCustomer(customer);
    }

    /**
     * Mostrar los datos de un cliente
     *
     * @return Customer
     */
    public Customer checkCustomer() {
        boolean retry = false;
        Customer foundCustomer = null;

        do {
            Customer cus = searchCustomerMenu();
            if (cus != null) {
                try {
                    // Buscar cliente
                    foundCustomer = dao.checkCustomerData(cus);
                } catch (DataNotFoundException e) {
                    // Si no encuentra al cliente preguntar volver a intentar
                    System.out.print("No se ha encontrado al cliente con los datos introducidos.\n"
                            + "¿Quieres volver a intentarlo?");
                    retry = Util.esBoolean();
                }
            }
        } while (retry && foundCustomer == null);

        return foundCustomer;
    }

    /**
     * Mostrar las cuentas de los clientes
     *
     * @param cus Customer
     * @return Set<Account>
     */
    public Set<Account> checkCustomerAccounts(Customer cus) {
        if (cus != null) {
            System.out.print("¿Quieres también consultar las cuentas de este cliente?");
            if (Util.esBoolean()) {
                return cus.getCustomerAccounts();
            }
        } else {
            cus = checkCustomer();
            if (cus != null) {
                return dao.checkCustomerAccounts(cus);
            }
        }
        return null;
    }

    /**
     * Método privado que solo sirve para quitar código repetido para los métodos
     * {@link #checkCustomer} y {@link #checkCustomerAccounts}
     * 
     * @return Customer
    **/
    private Customer searchCustomerMenu() {
        // Pedir la ID del cliente
        Customer cus = new Customer();
        Integer opc = Util.leerInt("-----Buscar un cliente-----"
                + "Elija una opción: \n"
                + "1. Buscar por ID\n"
                + "2. Buscar por nombre y apellido\n"
                + "4. Salir", 0, 5);
        switch (opc) {
            case 1:
                cus.setId(Util.leerInt("Introduzca el ID del cliente: "));
                return new Customer();
            case 2:
                String nombreApellido = Util.introducirCadena("Introduzca el nombre y apellido del cliente: ");
                cus.setFirstName(nombreApellido.split("\\s+")[0]);
                cus.setLastName(nombreApellido.split("\\s+")[1]);
                return cus;
            case 4:
                System.out.println("Volviendo al menú principal...");
                break;
        }
        return null;
    }

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
        
        
        accounts = (List<Account>) dao.checkCustomerAccounts(cus);
        
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
        dao.createMovement(cus, mov);
    }
    
    /**
     * 
     */
    public void createAccount() {
        Account ac = new Account();
        ac.setId(Util.leerInt("Insertar ID: "));
        ac.setDescription(Util.introducirCadena("Insertar Descripcion: "));
        ac.setBalance(Util.leerFloat("Introducir Balance: "));
        ac.setCreditLine(Util.leerFloat("Introducir Linea de Credito: "));
        ac.setBeginBalance(Util.leerFloat("Introducir Begin Balance: "));
        ac.setBeginBalanceTimestamp(Util.leerFloat("Introducir Begin Balance Timestamp: "));

        dao.createAccount(ac);
    }

    /**
     * Este metodo muestra la informacion sobre una cuenta en concreto
     * 
     * @param ac 
     */
    public void checkAccountData(Account ac) {
        int id = Util.leerInt("Insertar ID de una Cuenta");
        if (ac.getId().equals(id)) {
            System.out.println("ID: " + ac.getId());
            System.out.println("Description: " + ac.getDescription());
            System.out.println("Balance: " + ac.getBalance());
            System.out.println("Credit Line: " + ac.getCreditLine());
            System.out.println("Begin Balance: " + ac.getBeginBalance());
            System.out.println("Begin Balance Timestamp: " + ac.getBeginBalanceTimestamp());
            System.out.println("Type: " + ac.getType());
        } else {
            System.out.println("La Cuenta introducida no existe");
        }
        dao.checkAccountData(ac);
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

        accounts = (List<Account>) dao.checkCustomerAccounts(cus);

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

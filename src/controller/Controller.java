/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import controller.utilidades.DataNotFoundException;
import controller.utilidades.Util;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Account;
import models.Customer;
import models.DAO;
import models.Movement;
import models.Type;

/**
 *
 * @author yeguo
 */
public class Controller {

    private final DAO dao = DAOFactory.getDAO();

    /**
     * Método para crear un cliente
     *
     * @return
     */
    public void createCustomer() {
        // Pedir todos los datos del cliente
        Customer customer = new Customer();
        customer.setFirstName(Util.introducirCadena("Introduzca el 1º nombre: "));
        customer.setMiddleName(Util.introducirCadena("Introduzca la inicial del 2º nombre: "));
        customer.setLastName(Util.introducirCadena("Introduzca el apellido: "));
        customer.setStreetName(Util.introducirCadena("Introduzca el nombre de la calle: "));
        customer.setCity(Util.introducirCadena("Introduzca la ciudad: "));
        customer.setState(Util.introducirCadena("Introduzca la region: "));
        customer.setPhone(Util.leerLong("Introduzca el número de teléfono: "));
        customer.setZip(Util.leerInt("Introduzca el número Zip: "));
        customer.setEmail(Util.introducirCadena("Introduzca el email: "));
        // Crear cliente
        System.out.println(dao.createCustomer(customer) == true
                ? "Se ha creado correctamente" : "Error. No se ha podido crear el cliente.");
    }

    /**
     * Mostrar los datos de un cliente
     *
     */
    public void checkCustomer() {
        Customer customer = getCustomer();
        if (customer != null) {
            System.out.println(customer.toString());
            System.out.print("\n¿Quieres ver las cuentas del cliente? ");
            if (Util.esBoolean()) {
                int i = 0;
                for (Account account : customer.getCustomerAccounts()) {
                    i += 1;
                    System.out.printf("---Cuenta %d---\n", i);
                    account.getDatos();
                    i++;
                }
                if (i == 0) {
                    System.out.println("Este cliente no tiene ninguna cuenta\n");
                }
            }
        }
    }

    /**
     * Método para buscar un cliente
     *
     * @return Customer
     */
    private Customer getCustomer() {
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
                    System.out.print(e + "\n¿Quieres volver a intentarlo? ");
                    retry = Util.esBoolean();
                }
            }

        } while (retry && foundCustomer == null);

        return foundCustomer;
    }

    /**
     * Mostrar las cuentas de los clientes
     *
     */
    public void checkCustomerAccounts() {
        int i = 0;
        for (Account account : getCustomer().getCustomerAccounts()) {
            i += 1;
            System.out.printf("---Cuenta %d---\n", i);
            account.getDatos();
            i++;
        }
        if (i == 0) {
            System.out.println("Este cliente no tiene ninguna cuenta");

        }

    }

    /**
     * Método privado que solo sirve para quitar código repetido para los
     * métodos {@link #checkCustomer} y {@link #checkCustomerAccounts}
     *
     * @return Customer
     *
     */
    private Customer searchCustomerMenu() {
        // Pedir la ID del cliente
        Customer cus = new Customer();
        Integer opc = Util.leerInt("-----Buscar cliente-----\n"
                + "Elija una opción: \n"
                + "1. Buscar por ID\n"
                + "2. Buscar por nombre y apellido\n"
                + "3. Salir", 0, 4);
        switch (opc) {
            case 1:
                cus.setId(Util.leerLong("Introduzca el ID del cliente: "));
                return cus;
            case 2:
                String nombreApellido = Util.introducirCadena("Introduzca el nombre y apellido del cliente: ");
                cus.setFirstName(nombreApellido.split("\\s+")[0]);
                cus.setLastName(nombreApellido.split("\\s+")[1]);
                return cus;
            case 3:
                System.out.println("Volviendo al menú principal...");
                break;
        }
        return null;
    }

    /**
     * Este metodo obtiene la informacion para crear un movimiento en una cuenta
     * determinada
     */
    public void createMovement() {
        Long idCustomer;
        Long accountIdSelected;
        Boolean created = false;
        Double movementAmount;
        String movementType = null;
        Movement mov = null;
        Account ac = null;
        Set<Account> accounts = null;
        Customer cus = new Customer();

        cus = searchCustomerMenu();
        try {
            accounts = dao.checkCustomerAccounts(cus);
        } catch (DataNotFoundException ex) {
            System.err.println(ex);
        }

        //Cabeceras de la informacion de las cuentas
        System.out.println("---CUENTAS---");
        System.out.println("ID    DESCRIPCION    BALANCE    LINEA_CREDITO    SALDO_INICIAL    FECHA_SALDO_INICIAL    TIPO");

        showAccounts(accounts);
        accountIdSelected = Util.leerLong("Introduce el id de la cuenta en la que quiere crear un movimiento");

        ac = searchAccount(accountIdSelected, accounts);

        mov = new Movement();
        mov.setAccount_id(accountIdSelected);

        //Este bucle comprueba que en el caso de que la opcion seleccionada no sea correcta o que se ha introducido mal siga pidiendole al usuario que vuelva a introducir una opcion
        do {
            movementType = Util.introducirCadena("Que tipo de movimiento quiere(Deposito/pago)");
            if (movementType.equalsIgnoreCase("Deposito")) {
                mov.setDescription("Deposit");
            } else if (movementType.equalsIgnoreCase("Pago")) {
                mov.setDescription("Payment");
            } else {
                movementType = null;
            }
        } while (movementType == null || movementType.isEmpty());

        mov.setBalance(ac.getBalance());

        movementAmount = Util.leerDouble("Introduce la cantidad del movimiento");
        mov.setAmount(movementAmount);

        mov.setDate(Timestamp.valueOf(LocalDateTime.now()));
        created = dao.createMovement(cus, mov);
        if (created) {
            System.out.println("El movimiento ha sido creado con exito");
        } else {
            System.out.println("Ha habido un error al crear el movimiento");
        }
    }

    /**
     *
     */
    public void createAccount() {
        System.out.println("¿A que cliente quieres crear la cuenta?");
        Customer cus = getCustomer();

        if (cus != null) {
            Account ac = new Account();
            ac.setDescription(Util.introducirCadena("Insertar Descripcion: "));
            ac.setBeginBalance(Util.leerDouble("Introducir Balance Inicial: "));
            ac.setBalance(ac.getBeginBalance());
            ac.setCreditLine(Util.leerDouble("Introducir Linea de Credito: "));
            ac.setBeginBalanceTimestamp(Timestamp.valueOf(LocalDateTime.now()));
            if (Util.leerInt("¿Que tipo de cuenta es:? \n\t1.Estandar \n\t2.Credito", 1, 2) == 1) {
                ac.setType(Type.STANDAR);
            } else {
                ac.setType(Type.CREDIT);
            }

            if (dao.createAccount(ac, cus)) {
                System.out.println("Se ha creado la cuenta al cliente correctamente");
            } else {
                System.out.println("Error. No se ha podido crear la cuenta al cliente");
            }

        }
    }

    /**
     * Este metodo muestra la informacion sobre una cuenta en concreto
     *
     */
    public void checkAccountData() {
        try {
            Account acc = new Account();
            acc.setId(Util.leerLong("Inserta ID de una Cuenta:"));
            dao.checkAccountData(acc).getDatos();
        } catch (DataNotFoundException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, "La cuenta con la ID insertada no se existe", ex);
        }
    }

    /**
     * Este metodo mustra la informacion de los movimientos de una cuenta
     */
    public void checkMovements() {
        Customer cus = null;
        Long accountIdSelected;
        Account ac = null;
        Set<Account> accounts = null;
        Set<Movement> movements = null;

        cus = searchCustomerMenu();
        try {
            accounts = dao.checkCustomerAccounts(cus);

            //Cabeceras de la informacion de las cuentas
            System.out.println("---CUENTAS---");
            System.out.println("ID    DESCRIPCION    BALANCE    LINEA_CREDITO    SALDO_INICIAL    FECHA_SALDO_INICIAL    TIPO");

            showAccounts(accounts);

            accountIdSelected = Util.leerLong("Introduce el id de la cuenta de la que quiere ver los movimientos");
            try {
                ac = searchAccount(accountIdSelected, accounts);
                movements = dao.checkMovement(ac);

                System.out.println("ID    " + "CANTIDAD    " + "BALANCE    " + "DESCRIPCION    " + "FECHA");
                for (Movement mov : movements) {
                    mov.getDatos();
                }
            } catch (NullPointerException e) {
                System.out.println("No se ha encontrado una cuenta con el id introducido");
            } catch (DataNotFoundException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (DataNotFoundException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Este metodo busca una cuenta en una lista de cuentas
     *
     * @param id Es el id de la cuenta que buscamos
     * @param accounts Es la lista de cuentas de este cliente
     * @return Retorna la cuenta que corresponde al id enviado, Retorna nulo en
     * el caso de que no se encuentre la cuenta con ese id
     */
    public Account searchAccount(Long id, Set<Account> accounts) {
        for (Account account : accounts) {
            if (account.getId().equals(id)) {
                return account;
            }
        }
        return null;
    }

    /**
     * Este metodo muestra la informacion de las cuentas en la lista de las
     * cuentas
     *
     * @param accounts Es la lista de cuentas que se va a mostrar
     */
    public void showAccounts(Set<Account> accounts) {
        for (Account account : accounts) {
            System.out.println(account.getId() + "    " + account.getDescription()
                    + "    " + account.getBalance() + "    "
                    + account.getCreditLine() + "    " + account.getBeginBalance() + "    "
                    + account.getBeginBalanceTimestamp() + "    " + account.getType()
            );
        }
    }

    public void addAccountToCustomer() {
        System.out.println("¿A que cliente quieres añadir la cuenta?");
        Customer cus = getCustomer();

        System.out.println("Inserte la ID de la cuenta que quieres añadir al cliente");
        Account acc = new Account();
        acc.setId(Util.leerLong("Inserta ID de una Cuenta:"));

        if (dao.addAccountToCustomer(cus, acc)) {
            System.out.println("Se ha añadido la cuenta al cliente correctamente");
        } else {
            System.out.println("Error. No se ha podido añadir la cuenta al cliente");
        }

    }

}

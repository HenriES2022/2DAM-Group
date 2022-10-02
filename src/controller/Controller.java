/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import controller.utilidades.DataNotFoundException;
import controller.utilidades.Util;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
     * Este método pide los datos de un cliente para crearlo
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
     * Este método primero llama a {@link getCustomer()} para buscar el cliente
     * que se quiere mostrar sus datos y se le pregunta si también si quiere
     * mostrar sus cuentas
     *
     */
    public void checkCustomer() {
        Customer customer = getCustomer();
        if (customer != null) {
            System.out.println(customer.toString());
            System.out.print("\n¿Quieres ver las cuentas del cliente? ");
            if (Util.esBoolean()) {
                try {
                    int i = 0;
                    for (Account account : dao.checkCustomerAccounts(customer)) {
                        i += 1;
                        System.out.printf("---Cuenta %d---\n", i);
                        account.getDatos();
                        i++;
                    }
                    if (i == 0) {
                        System.out.println("Este cliente no tiene ninguna cuenta\n");
                    }
                } catch (DataNotFoundException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Método que se comunica con el DAO para recoger los datos del cliente que
     * se quiere buscar
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
     * Método que mostrará la información de las cuentas del cliente buscado
     *
     */
    public void checkCustomerAccounts() {
        int i = 0;

        try {
            for (Account account : dao.checkCustomerAccounts(getCustomer())) {
                i += 1;
                System.out.printf("---Cuenta %d---\n", i);
                account.getDatos();
            }
            if (i == 0) {
                System.out.println("Este cliente no tiene ninguna cuenta");

            }
        } catch (DataNotFoundException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
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
        Boolean ok = false;
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
                String nombre;
                String apellido;
                do {
                    nombre = Util.introducirCadena("Introduzca el nombre del cliente: ");
                    apellido = Util.introducirCadena("Introduzca el apellido del cliente: ");
                    if ((!nombre.isEmpty() && !apellido.isEmpty()) && (!nombre.contains(" ") && !apellido.contains(" "))) {
                        cus.setFirstName(nombre);
                        cus.setLastName(apellido);
                        ok = true;
                    } else {
                        System.out.println("Debe de introducir un nombre y apellido válido");
                        ok = false;
                    }
                } while (!ok);
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
        Boolean correctAccountId = false;
        Double movementAmount;
        Movement mov = null;
        Account ac = null;
        Set<Account> accounts = null;
        Customer cus = new Customer();

        cus = getCustomer();
        try {
            accounts = dao.checkCustomerAccounts(cus);

            //Cabeceras de la informacion de las cuentas
            System.out.println("---CUENTAS---");
            System.out.println("ID    DESCRIPCION    BALANCE    LINEA_CREDITO    SALDO_INICIAL    FECHA_SALDO_INICIAL    TIPO");

            showAccounts(accounts);
            accountIdSelected = Util.leerLong("Introduce el id de la cuenta en la que quiere crear un movimiento");
            for (Account account : accounts) {
                if (account.getId().equals(accountIdSelected)) {
                    correctAccountId = true;
                }
            }
            if (correctAccountId) {
                ac = searchAccount(accountIdSelected, accounts);
                mov = new Movement();
                mov.setAccount_id(accountIdSelected);

                if (Util.leerInt("Que tipo de movimiento quiere (1.Deposito/2.Pago)", 1, 2) == 1) {
                    mov.setDescription("Deposit");
                } else {
                    mov.setDescription("Payment");
                }

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
            } else{
                System.out.println("No existe ninguna cuenta con ese id");
            }

        } catch (DataNotFoundException | NullPointerException ex) {
            System.err.println(ex);
        }
    }

    /**
     * Este método pide a que cliente se le quiera crear la cuenta, y se le pide
     * que inserte la información de la cuenta
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
            if (Util.leerInt("¿Que tipo de cuenta es? \n\t1.Estandar \n\t2.Credito", 1, 2) == 1) {
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

        cus = getCustomer();
        try {
            accounts = dao.checkCustomerAccounts(cus);
            if (accounts != null) {

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
                    System.err.println(ex);
                }
            }
        } catch (DataNotFoundException | NullPointerException ex) {
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
    private Account searchAccount(Long id, Set<Account> accounts) {
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

    /**
     * Método para añadir una cuenta existente a un cliente
     */
    public void addAccountToCustomer() {
        System.out.println("¿A que cliente quieres añadir la cuenta?");
        Customer cus = getCustomer();

        if (cus != null) {
            Account acc = new Account();
            acc.setId(Util.leerLong("Inserta ID de la cuenta que quieres añadir al cliente:"));

            if (dao.addAccountToCustomer(cus, acc)) {
                System.out.println("Se ha añadido la cuenta al cliente correctamente");
            } else {
                System.out.println("Error. No se ha podido añadir la cuenta al cliente");
            }
        }

    }

}

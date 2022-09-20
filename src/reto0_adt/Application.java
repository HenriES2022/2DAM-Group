/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package reto0_adt;

import controller.utilidades.Util;
import javax.xml.transform.OutputKeys;

/**
 *
 * @author yeguo
 */
public class Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int opc;

        do {
            opc = menu();
            switch (opc) {
                case 1:

                    break;
                case 2:

                    break;
                case 3:

                    break;
                case 4:

                    break;
                case 5:

                    break;
                case 6:

                    break;
                case 7:

                    break;
                case 8:

                    break;
                case 9:

                    break;
                default:
                    System.out.println();
            }

        } while (opc != 9);
    }

    private static int menu() {
        System.out.println("-----------Menú-----------"
                + "\n 1. Crear cliente"
                + "\n 2. Consultar datos de un cliente"
                + "\n 3. Consultar cuentas de un cliente"
                + "\n 4. Crear cuenta para cliente"
                + "\n 5. Agregar cliente a cuenta"
                + "\n 6. Consultar datos de una cuenta"
                + "\n 7. Realizar movimiento sobre una cuenta"
                + "\n 8. Consultar movimientos de cuenta"
                + "\n 9. Salir");
        return Util.leerInt("Seleccione una opción: ");

    }

}

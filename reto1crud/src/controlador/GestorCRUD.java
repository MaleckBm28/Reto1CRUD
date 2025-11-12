package controlador;

import dao.DaoImplementacion;
import manejoHilos.*;

/**
 * Clase encargada de gestionar la ejecución de los distintos hilos
 * del CRUD: Insertar, Leer, Modificar y Eliminar.
 */
public class GestorCRUD {

    private DaoImplementacion dao;

    public GestorCRUD() {
        dao = new DaoImplementacion();
    }

    /**
     * Ejecuta los 4 hilos del CRUD simultáneamente.
     */
    public void ejecutarOperaciones() {

        Thread hiloInsertar = new Thread(new HiloInsertar(dao));
        Thread hiloLeer = new Thread(new HiloLeer(dao));
        Thread hiloModificar = new Thread(new HiloModificar(dao));
        Thread hiloEliminar = new Thread(new HiloEliminar(dao));

        // Iniciar todos los hilos
        hiloInsertar.start();
        hiloLeer.start();
        hiloModificar.start();
        hiloEliminar.start();

        // Esperar a que todos terminen (opcional)
        try {
            hiloInsertar.join();
            hiloLeer.join();
            hiloModificar.join();
            hiloEliminar.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("⚠️ Error al esperar los hilos: " + e.getMessage());
        }

        System.out.println("\n✅ Todos los hilos del CRUD han finalizado su ejecución.");
    }
}

package manejoHilos;

import controlador.DaoImplementacion;

/**
 * 🟢 Hilo que simula la inserción de un usuario en la base de datos.
 */
public class HiloInsertar implements Runnable {

    private DaoImplementacion dao;

    public HiloInsertar(DaoImplementacion dao) {
        this.dao = dao;
    }

    @Override
    public void run() {
        synchronized (this) {
            System.out.println("🟢 [" + Thread.currentThread().getName() + "] Iniciando inserción...");
            try {
                // Simulación de trabajo
                Thread.sleep(2000);
                // Aquí más adelante se llamará a dao.insertarUsuario(...)
                System.out.println("🟢 [" + Thread.currentThread().getName() + "] Inserción completada.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

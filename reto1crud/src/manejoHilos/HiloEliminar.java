package manejoHilos;
import controlador.DaoImplementacion;

public class HiloEliminar implements Runnable {
    private DaoImplementacion dao;

    public HiloEliminar(DaoImplementacion dao) {
        this.dao = dao;
    }

    @Override
    public void run() {
        synchronized (this) {
            System.out.println("🔴 [HiloEliminar] Eliminando usuario...");
            // Aquí luego se añadirá dao.eliminarUsuario(...)
        }
    }
}

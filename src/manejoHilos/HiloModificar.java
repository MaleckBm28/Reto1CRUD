package manejoHilos;

import controlador.DaoImplementacion;
import modelo.Usuario;

public class HiloModificar implements Runnable {

    private DaoImplementacion dao;
    private Usuario usuario;

    public HiloModificar(DaoImplementacion dao) {
        this.dao = dao;
        this.usuario = new Usuario(); // o null si lo prefieres
    }

    public HiloModificar(DaoImplementacion dao, Usuario usuario) {
        this.dao = dao;
        this.usuario = usuario;
    }

    @Override
    public void run() {
        synchronized (this) {
            System.out.println("🟠 [HiloModificar] Iniciando modificación...");
            if (usuario != null)
                dao.modificarUsuario(usuario); // ✅ método correcto
            System.out.println("🟢 [HiloModificar] Finalizada modificación.");
        }
    }
}

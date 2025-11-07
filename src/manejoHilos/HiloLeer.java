package manejoHilos;

import controlador.DaoImplementacion;
import modelo.Usuario;

/**
 * 📘 Hilo que puede leer datos de usuarios.
 * Se puede usar para:
 *  - Verificar login (con correo y contraseña)
 *  - Leer todos los usuarios (futuro panel admin)
 */
public class HiloLeer implements Runnable {

    private DaoImplementacion dao;
    private String correo;
    private String contrasena;
    private Usuario usuario;
    private boolean autenticado;

    // Constructor para login (usa correo y contraseña)
    public HiloLeer(DaoImplementacion dao, String correo, String contrasena) {
        this.dao = dao;
        this.correo = correo;
        this.contrasena = contrasena;
    }

    // Constructor futuro (para leer lista de usuarios)
    public HiloLeer(DaoImplementacion dao) {
        this.dao = dao;
    }

    @Override
    public void run() {
        synchronized (dao) {
            if (correo != null && contrasena != null) {
                // Modo login
                System.out.println("🔐 [" + Thread.currentThread().getName() + "] Verificando credenciales...");
                autenticado = dao.autenticar(correo, contrasena);
                if (autenticado) {
                    usuario = dao.obtenerUsuarioPorEmail(correo);
                    System.out.println("✅ Login correcto para: " + correo);
                } else {
                    System.out.println("❌ Login fallido para: " + correo);
                }
            } else {
                // Modo lectura general (por ahora simulado)
                System.out.println("📘 [" + Thread.currentThread().getName() + "] Leyendo todos los usuarios...");
                try {
                    Thread.sleep(1500);
                    System.out.println("📘 Lectura completada.");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public boolean isAutenticado() {
        return autenticado;
    }

    public Usuario getUsuario() {
        return usuario;
    }
}

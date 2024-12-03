package dad.practicaparcial;

import java.util.*;

public class UsuarioConfiguraciones {
    public Map<String, List<ConfiguracionFTP>> configuraciones; // Se introduce una lista de configuraciones a un usuario

    public UsuarioConfiguraciones() {
        configuraciones = new HashMap<>();
    }

    // Agregar configuración a un usuario
    public void agregarConfiguracion(String usuario, ConfiguracionFTP config) {
        configuraciones.putIfAbsent(usuario, new ArrayList<>());
        configuraciones.get(usuario).add(config);
    }

    // Obtener configuraciones de un usuario
    public List<ConfiguracionFTP> obtenerConfiguraciones(String usuario) {
        return configuraciones.getOrDefault(usuario, new ArrayList<>());
    }

    // Listar configuraciones de un usuario
    public void listarConfiguraciones(String usuario) {
        if (!configuraciones.containsKey(usuario)) {
            System.out.println("Usuario no registrado en el sistema o no tiene configuraciones.");
            return;
        }
        List<ConfiguracionFTP> configs = obtenerConfiguraciones(usuario);
        if (configs.isEmpty()) {
            System.out.println("No hay configuraciones guardadas para el usuario: " + usuario);
        } else {
            System.out.println("Configuraciones para el usuario " + usuario + ":");
            for (int i = 0; i < configs.size(); i++) {
                System.out.println((i + 1) + ". " + configs.get(i));
            }
        }
    }
    
    public void eliminarConfig(String usuario)
    {
    	configuraciones.remove(usuario);
    }
}

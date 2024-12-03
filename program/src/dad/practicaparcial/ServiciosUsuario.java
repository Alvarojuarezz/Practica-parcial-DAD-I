package dad.practicaparcial;

import java.io.*;
import java.util.*;


public class ServiciosUsuario {
	
	protected static Hashtable<String, Usuario> usuarios = new Hashtable<String, Usuario>();
	protected static UsuarioConfiguraciones configuracionesUsuario = new UsuarioConfiguraciones(); // Usamos la clase UsuarioConfiguraciones
	
	// Método para validar las credenciales del usuario
	public boolean validarCredenciales(String usuario, String password) {
	    if (usuarios.containsKey(usuario)) {
	        Usuario u = usuarios.get(usuario); 
	        return u.getPassword().equals(password); // si son iguales retorna true , si no lo son retornara false
	    }
	    // Si el usuario no existe
	    return false;
	}
	
	// Método para cargar usuarios desde un archivo
	public static void cargarUsuarios() {
	    try (BufferedReader br = new BufferedReader(new FileReader("Recursos/Usuarios.txt"))) {
	        String linea;
	        while ((linea = br.readLine()) != null) {
	            String[] partes = linea.split("-");
	            boolean esAdmin = Boolean.parseBoolean(partes[2]); // Obtenemos el rol del usuario
	            Usuario usuario = new Usuario(partes[0], partes[1], esAdmin); // Creamos el objeto Usuario
	            usuarios.put(partes[0], usuario); // Guardamos el objeto Usuario en el mapa
	        }
	    } catch (IOException e) {
	        System.err.println("Error al cargar el archivo de usuarios: " + e.getMessage());
	    }
	}

    
	//MÉTODOS ADMIN
	public String agregarUsuario(String usuario, String password, String admin)
	{
		if(!usuarios.containsKey(usuario))
		{
			Usuario u = new Usuario(usuario, password, Boolean.parseBoolean(admin));
			usuarios.put(usuario, u);			
			return "Usuario creado con exito.";
		}
		else
		{
			return "El usuario "+usuario+" ya se encuentra en el sistema";
		}
	}
	
	public String eliminarUsuario(String usuario)
	{
		if(usuarios.containsKey(usuario))
		{
			usuarios.remove(usuario);
			return "Usuario eliminado con exito."; 
		}
		else
		{
			return "Usuario: "+usuario+" no se encuentra en el sistema.";
		}
	}
	
	public String modificarContra(String usuario, String nuevaContraseña)
	{
		if(usuarios.containsKey(usuario))
		{
			usuarios.get(usuario).setPassword(nuevaContraseña);
			return "Usuario modificado con exito.";
		}
		else
		{
			return "Usuario no encontrado.";
		}
	}
	
	public String modificarRol(String usuario)
	{
		if(usuarios.containsKey(usuario))
		{
			if(usuarios.get(usuario).isAdmin()==true)
			{
				usuarios.get(usuario).setAdmin(false);
			}
			else
			{
				usuarios.get(usuario).setAdmin(false);
			}
			
			return "Usuario modificado con exito.";
		}
		else
		{
			return "Usuario no encontrado.";
		}
	}
	
	public String mostrarUsuario(String usuario)
	{
		if(usuarios.containsKey(usuario))
		{
			String rol = usuarios.get(usuario).isAdmin() ? " Administrador." : " Usuario sin permisos.";
			return "Usuario: "+usuarios.get(usuario).getUsuario()+" Contraseña: "+usuarios.get(usuario).getPassword()+" Rol:"+rol;
		}
		else
		{
			return "Usuario: "+usuario+" no se encuentra en el sistema.";
		}
	}
	
	// Método para guardar los usuarios en el archivo
	public static void guardarUsuarios() {
	    try (BufferedWriter bw = new BufferedWriter(new FileWriter("Recursos/Usuarios.txt"))) {
	        for (Usuario usuario : usuarios.values()) {
	            // Escribir cada usuario en el archivo en el formato: usuario-contraseña-admin
	            bw.write(usuario.getUsuario() + "-" + usuario.getPassword() + "-" + usuario.isAdmin());
	            bw.newLine(); // Añadir un salto de línea después de cada usuario
	        }
	    } catch (IOException e) {
	        System.err.println("Error al guardar el archivo de usuarios: " + e.getMessage());
	    }
	}

	
	//Métodos usuarios 
	
	
	// Métodos de configuración FTP 
	public String agregarConfiguracionFTP(String usuario, String direccionIP, String puerto, String usuarioFTP, String contrasenaFTP) {
        if (usuarios.containsKey(usuario)) {
            // Crear la nueva configuración FTP
        	int port = Integer.parseInt(puerto);
            ConfiguracionFTP nuevaConfig = new ConfiguracionFTP(direccionIP, port, usuarioFTP, contrasenaFTP);
            // Usar la clase UsuarioConfiguraciones para agregar la configuración al usuario
            configuracionesUsuario.agregarConfiguracion(usuario, nuevaConfig);
            
            return "Configuración de servidor FTP agregada correctamente.";
        } else {
            return "El usuario no existe en el sistema.";
        }
    }

	// Método para listar las configuraciones FTP de un usuario
    public static String listarConfiguracionesFTP(String usuario) {
        if (!usuarios.containsKey(usuario)) {
            return "El usuario no existe en el sistema.";
        }
        System.out.println("Listando configuraciones para el usuario: " + usuario);
        configuracionesUsuario.listarConfiguraciones(usuario);
        return "Fin del listado.";
    }
	
    public String eliminarConfig(String usuario)
    {
    	if(usuarios.containsKey(usuario))
    	{
    		if(configuracionesUsuario.configuraciones.containsKey(usuario))
    		{
    			configuracionesUsuario.eliminarConfig(usuario);
        		return "La configuración de servidor del usuario "+usuario+" eliminado con éxito.";
    		}
    		else
    		{
    			return "El usuario no tiene configuraciones guardadas.";
    		}
    	}
    	else
    	{
    		return "Usuario no encontrado.";
    	}
    }
}


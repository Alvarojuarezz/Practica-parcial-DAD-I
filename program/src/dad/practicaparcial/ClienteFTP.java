package dad.practicaparcial;

import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Scanner;

public class ClienteFTP {

    private static final int PUERTO = 21;  // Puerto FTP por defecto
    private static final String SERVER_HOST = "127.0.0.1"; // Dirección IP de FileZilla Server
    private String usuarioAuth;
    
    public void ejecutar() {
        try {
			Socket socket = new Socket(SERVER_HOST,PUERTO);
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			Scanner sc = new Scanner(System.in);
			
            // Leer y mostrar todas las líneas iniciales del servidor
			leerIntro(br);

			System.out.println("///Bienvenido al servidor/// ");
			System.out.print("Introduzca el usuario: ");
			String usuario = sc.nextLine();
			pw.println("USER "+usuario);
			pw.flush();
			String respuesta = br.readLine();
			
			System.out.print("Introduzca la contraseña: ");
			String password = sc.nextLine();
			pw.println("PASS "+password);
			pw.flush();
			respuesta=br.readLine();
			
			// Verificar si la autenticación fue exitosa
            if (!respuesta.startsWith("230")) {
                System.out.println("Autenticación fallida.");
                socket.close(); 
                sc.close();
                return ;
            }
            usuarioAuth=usuario;
            System.out.println("\nAutenticación exitosa. Bienvenido, " + usuarioAuth);
			
            ServiciosUsuario.cargarUsuarios();
            String opt="";
			// Verificar si el usuario autenticado es administrador o no
            Usuario user = ServiciosUsuario.usuarios.get(usuario); // Obtener el objeto Usuario
            if (user == null) {
                System.out.println("El usuario no está registrado en el sistema.");
                socket.close();
                return;
            }

            // Verificar el rol del usuario
            if (user.isAdmin()) {
                mostrarMenuAdmin(sc, pw, br);
            } else {
                mostrarMenuUsuario(sc, pw, br);
            }
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}        
    }
    
 // Mostrar el menú para administradores
    private void mostrarMenuAdmin(Scanner sc, PrintWriter pw, BufferedReader br) throws IOException {
        String opt = "";
        Menu menu = new Menu();
        while (!opt.equals("0")) {
            menu.menuAdmin();
            opt = sc.nextLine();
            procesarOpcionAdmin(opt, sc);
        }
        ServiciosUsuario.guardarUsuarios();
    }

    // Mostrar el menú para usuarios sin permisos
    private void mostrarMenuUsuario(Scanner sc, PrintWriter pw, BufferedReader br) throws IOException {
        String opt = "";
        Menu menu = new Menu();
        while (!opt.equals("0")) {
            menu.menuUsuario();
            opt = sc.nextLine();
            procesarOpcionUsuario(opt, sc);
        }
    }

    public static void main(String[] args) {
        ClienteFTP cl = new ClienteFTP();
        cl.ejecutar();
    }
    
    //Función para utilizar una opción u otra
    private void procesarOpcionAdmin(String opt, Scanner sc) throws IOException {
    	ServiciosUsuario su = new ServiciosUsuario();
        switch (opt) {
            case "1": // Agregar usuario
                System.out.print("Introduzca el nombre de usuario: ");
                String nuevoUsuario = sc.nextLine();
                System.out.print("Introduzca la contraseña: ");
                String nuevaContrasena = sc.nextLine();
                System.out.print("¿Es administrador? (true/false): ");
                String esAdmin = sc.nextLine();

                String resultado = su.agregarUsuario(nuevoUsuario, nuevaContrasena, esAdmin);
                System.out.println(resultado);
                break;

            case "2": // Modificar contraseña
                System.out.print("Introduzca el nombre de usuario: ");
                String usuario = sc.nextLine();
                System.out.print("Introduzca la nueva contraseña: ");
                String nuevaPassword = sc.nextLine();

                resultado = su.modificarContra(usuario, nuevaPassword);
                System.out.println(resultado);
                break;

            case "3": // Modificar rol
                System.out.print("Introduzca el nombre de usuario: ");
                String usuarioModificarRol = sc.nextLine();

                resultado = su.modificarRol(usuarioModificarRol);
                System.out.println(resultado);
                break;

            case "4": // Eliminar usuario
                System.out.print("Introduzca el nombre de usuario a eliminar: ");
                String usuarioEliminar = sc.nextLine();

                resultado = su.eliminarUsuario(usuarioEliminar);
                System.out.println(resultado);
                break;

            case "5": // Listar un usuario
                System.out.print("Introduzca el nombre de usuario: ");
                String usuarioListar = sc.nextLine();

                resultado = su.mostrarUsuario(usuarioListar);
                System.out.println(resultado);
                break;

            case "0"://Opción salir
                System.out.println("Saliendo del menú de administrador.");
                break;

            default:
                System.out.println("Opción no válida.");
                break;
        }
    }

    //Función para utilizar una opción u otra
    private void procesarOpcionUsuario(String opt, Scanner sc) throws IOException {
        switch (opt) {
            case "1": // Crear configuración
                System.out.print("Introduzca la dirección IP: ");
                String ip = sc.nextLine();
                System.out.print("Introduzca el puerto: ");
                int puerto = Integer.parseInt(sc.nextLine());
                System.out.print("Introduzca el usuario FTP: ");
                String usuarioFTP = sc.nextLine();
                System.out.print("Introduzca la contraseña FTP: ");
                String contrasenaFTP = sc.nextLine();

                ConfiguracionFTP config = new ConfiguracionFTP(ip, puerto, usuarioFTP, contrasenaFTP);
                ServiciosUsuario.configuracionesUsuario.agregarConfiguracion(usuarioAuth, config);
                //System.out.println("Configuración creada con éxito.");
                break;
            case "2": // Conectar al servidor FTP y listar archivos
                System.out.print("Introduzca el nombre del usuario: ");
                String usuarioConectar = sc.nextLine();
                List<ConfiguracionFTP> configs = ServiciosUsuario.configuracionesUsuario.obtenerConfiguraciones(usuarioConectar);

                if (configs.isEmpty()) {
                    System.out.println("No se encontró una configuración para este usuario.");
                    return;
                }

                // Seleccionar configuración para conectar
                System.out.println("Seleccione una configuración para conectarse:");
                for (int i = 0; i < configs.size(); i++) {
                    System.out.println((i + 1) + ". " + configs.get(i));
                }
                int opcion = Integer.parseInt(sc.nextLine()) - 1;

                if (opcion >= 0 && opcion < configs.size()) {
                    listarArchivos(configs.get(opcion));
                } else {
                    System.out.println("Selección inválida.");
                }
                break;

            case "0":
                System.out.println("Saliendo del menú de usuario.");
                break;

            default:
                System.out.println("Opción no válida.");
                break;
        }
    }
    private void leerIntro(BufferedReader br) throws IOException
    {
    	String linea;
        do {
            linea = br.readLine();
        } while (linea != null && (linea.length() < 4 || linea.charAt(3) == '-')); // Continuar si es parte de una línea multilínea (código-)
    }

    //Función para listar archivos
    private void listarArchivos(ConfiguracionFTP config) {
        try (Socket socket = new Socket(config.getIp(), config.getPuerto())) {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            
            System.out.println("Servidor: ");
            leerIntro(br);
            
            // Enviar comando USER
            pw.println("USER " + config.getUsuarioFTP());
            pw.flush();
            String respuesta = br.readLine();
            System.out.println("Respuesta USER: " + respuesta);
            if (!respuesta.startsWith("331")) { // 331 = Usuario OK, esperar contraseña
                System.out.println("Error al enviar el usuario. Respuesta: " + respuesta);
                return;
            }

            // Enviar comando PASS
            pw.println("PASS " + config.getContrasenaFTP());
            pw.flush();
            respuesta = br.readLine();
            System.out.println("Respuesta PASS: " + respuesta);
            if (!respuesta.startsWith("230")) { // 230 = Login exitoso
                System.out.println("Error de autenticación. Respuesta: " + respuesta);
                return;
            }

            // Enviar comando PASV para entrar en modo pasivo
            pw.println("PASV");
            pw.flush();
            respuesta = br.readLine();
            System.out.println("Respuesta PASV: " + respuesta);
            if (!respuesta.startsWith("227")) { // 227 = Modo pasivo activado
                System.out.println("Error al activar el modo pasivo. Respuesta: " + respuesta);
                return;
            }
            // Extraer la IP y el puerto del modo pasivo de la respuesta
            String[] datos = extraerIPyPuerto(respuesta);
            String ipPasiva = datos[0];
            int puertoPasivo = Integer.parseInt(datos[1]);

            // Conectar al socket de datos
            Socket dataSocket = new Socket(ipPasiva, puertoPasivo);
            BufferedReader dataReader = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));

            // Enviar comando LIST para listar archivos
            pw.println("LIST");
            pw.flush();
            respuesta = br.readLine();
            System.out.println("Respuesta LIST: " + respuesta);
            if (!respuesta.startsWith("150")) { // 150 = Abriendo conexión de datos
                System.out.println("Error al listar archivos. Respuesta: " + respuesta);
                return;
            }

            // Leer y mostrar la lista de archivos desde el socket de datos
            String linea;
            System.out.println("Archivos en el servidor:");
            while ((linea = dataReader.readLine()) != null) {
                System.out.println(linea);
            }

            // Cerrar el socket de datos
            dataSocket.close();

            // Leer el mensaje final del servidor (226 = Transferencia completa)
            System.out.println("Servidor: " + br.readLine());
            
        } catch (IOException e) {
            System.out.println("Error al conectar al servidor FTP: " + e.getMessage());
        }
    }
    
    // Método auxiliar para extraer IP y puerto de la respuesta PASV
    private String[] extraerIPyPuerto(String respuestaPASV) {
        // La respuesta PASV 
        int inicio = respuestaPASV.indexOf('('); //Para encontrar el inicio de la respuestaPASV
        int fin = respuestaPASV.indexOf(')');//Para encontrar el fin de la respuestaPASV
        String datos = respuestaPASV.substring(inicio + 1, fin);//Extraer la cadena del paréntesis
        String[] partes = datos.split(","); // Dividir con una coma
        String ip = partes[0] + "." + partes[1] + "." + partes[2] + "." + partes[3]; //Formamos la ip
        int puerto = Integer.parseInt(partes[4]) * 256 + Integer.parseInt(partes[5]);
        return new String[]{ip, String.valueOf(puerto)}; //Devolución de un array con la ip y el puerto
    }

	
}

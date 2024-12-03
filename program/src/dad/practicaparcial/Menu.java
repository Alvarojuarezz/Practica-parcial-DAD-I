package dad.practicaparcial;

public class Menu {
	
	public void menuAdmin()
	{
		System.out.println("\n///Bienvenido al menú de administrador///");
		System.out.println("1. Agregar usuario.");
		System.out.println("2. Modificar contraseña de usuario.");
		System.out.println("3. Modificar rol de usuario.");
		System.out.println("4. Eliminar usuario.");
		System.out.println("5. Listar un usuario.");
		System.out.println("0. Salir");
		System.out.print("Seleccione una opción: ");
	}
	
	public void menuUsuario() 
	{
	    System.out.println("\n/// Bienvenido al menú de usuario ///");
	    System.out.println("1. Crear configuración.");
	    System.out.println("2. Conectar a un servidor FTP y listar archivos.");
	    System.out.println("0. Salir.");
	    System.out.print("Seleccione una opción: ");
	}
}

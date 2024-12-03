package dad.practicaparcial;

public class Usuario {
    private String usuario;
    private String password;
    private boolean Admin; // True si es administrador, false si es cliente
    
    public Usuario(String usuario, String password, boolean Admin) {
        this.usuario = usuario;
        this.password = password;
        this.Admin = Admin;
    }

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAdmin() {
		return Admin;
	}

	public void setAdmin(boolean admin) {
		Admin = admin;
	}
}

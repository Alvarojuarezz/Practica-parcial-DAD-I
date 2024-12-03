package dad.practicaparcial;

public class ConfiguracionFTP {
    private String ip;
    private int puerto;
    private String usuarioFTP;
    private String contrasenaFTP;

    public ConfiguracionFTP(String ip, int puerto, String usuarioFTP, String contrasenaFTP) {
        this.ip = ip;
        this.puerto = puerto;
        this.usuarioFTP = usuarioFTP;
        this.contrasenaFTP = contrasenaFTP;
    }

    // Getters y Setters
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPuerto() {
        return puerto;
    }

    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }

    public String getUsuarioFTP() {
        return usuarioFTP;
    }

    public void setUsuarioFTP(String usuarioFTP) {
        this.usuarioFTP = usuarioFTP;
    }

    public String getContrasenaFTP() {
        return contrasenaFTP;
    }

    public void setContrasenaFTP(String contrasenaFTP) {
        this.contrasenaFTP = contrasenaFTP;
    }

    @Override
    public String toString() {
        return "IP: " + ip + ", Puerto: " + puerto + ", Usuario: " + usuarioFTP;
    }
}

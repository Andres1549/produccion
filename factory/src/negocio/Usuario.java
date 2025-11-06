
package negocio;

public class Usuario {
    private int id;
    private String nombre;
    private String correo;

    public Usuario() {}
    public Usuario(int id, String nombre, String correo) { this.id = id; this.nombre = nombre; this.correo = correo; }

    public int getId() { return id; } public String getNombre() { return nombre; } public String getCorreo() { return correo; }
    public void setId(int id) { this.id = id; } public void setNombre(String nombre) { this.nombre = nombre; } public void setCorreo(String correo) { this.correo = correo; }

    @Override public String toString() { return "Usuario { id=" + id + ", nombre='" + nombre + "', correo='" + correo + "' }"; }
}

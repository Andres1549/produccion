
package negocio;

public class Reporte {
    private TipoReporte tipo;
    private String contenido;

    public Reporte(TipoReporte tipo, String contenido) { this.tipo = tipo; this.contenido = contenido; }
    public TipoReporte getTipo() { return tipo; } public String getContenido() { return contenido; }
    @Override public String toString() { return "[REPORTE: " + tipo + "]\n" + contenido; }
}

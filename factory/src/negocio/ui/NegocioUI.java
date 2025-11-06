
package negocio.ui;

import proyecto.Database;
import proyecto.DatabaseFactory;
import proyecto.MySQLFactory;
import proyecto.PostgresFactory;
import proyecto.SQLiteFactory;

import javax.swing.*;
import java.awt.*;

public class NegocioUI extends JFrame {

    private proyecto.DatabaseFactory currentFactory;

    public NegocioUI() {
        setTitle("Gestión de Negocio - GUI"); setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600); setLocationRelativeTo(null);

        // default factory (SQLite local file)
        this.currentFactory = new SQLiteFactory();

        // menu bar for DB selection
        JMenuBar menuBar = new JMenuBar();
        JMenu menuDB = new JMenu("Base de Datos");

        JMenuItem mysql = new JMenuItem("MySQL");
        mysql.addActionListener(e -> cambiarBD(new MySQLFactory()));

        JMenuItem postgres = new JMenuItem("PostgreSQL");
        postgres.addActionListener(e -> cambiarBD(new PostgresFactory()));

        JMenuItem sqlite = new JMenuItem("SQLite");
        sqlite.addActionListener(e -> cambiarBD(new SQLiteFactory()));

        menuDB.add(mysql); menuDB.add(postgres); menuDB.add(sqlite);
        menuBar.add(menuDB);
        setJMenuBar(menuBar);

        // panels as tabs
        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Usuarios", new UsuarioPanel(() -> currentFactory.createDatabase()));
        tabs.add("Productos", new ProductoPanel(() -> currentFactory.createDatabase()));
        tabs.add("Ventas", new VentaPanel(() -> currentFactory.createDatabase()));
        tabs.add("Reportes", new ReportePanel(() -> currentFactory.createDatabase()));

        add(tabs, BorderLayout.CENTER);
    }

    private void cambiarBD(proyecto.DatabaseFactory factory) {
        this.currentFactory = factory;
        JOptionPane.showMessageDialog(this, "Base de datos cambiada a: " + factory.getClass().getSimpleName());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new NegocioUI().setVisible(true));
    }
}

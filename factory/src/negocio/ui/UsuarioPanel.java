
package negocio.ui;

import proyecto.Database;
import proyecto.negocio.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class UsuarioPanel extends JPanel {
    private final java.util.function.Supplier<Database> dbSupplier;
    private JTable table;

    public UsuarioPanel(java.util.function.Supplier<Database> dbSupplier) {
        this.dbSupplier = dbSupplier;
        setLayout(new BorderLayout());
        table = new JTable();
        add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel south = new JPanel();
        JButton btnLoad = new JButton("Actualizar");
        btnLoad.addActionListener(e -> cargarUsuarios());
        south.add(btnLoad);
        add(south, BorderLayout.SOUTH);
        cargarUsuarios();
    }

    private void cargarUsuarios() {
        Database db = dbSupplier.get();
        DefaultTableModel model = new DefaultTableModel(new Object[]{"ID","Nombre","Correo"},0);
        if (db == null) { table.setModel(model); return; }
        try (Connection conn = db.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, nombre, correo FROM usuarios")) {
            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt(1), rs.getString(2), rs.getString(3)});
            }
            table.setModel(model);
        } catch (Exception e) {
            // show error and keep model empty
            JOptionPane.showMessageDialog(this, "Error al cargar usuarios: " + e.getMessage());
            table.setModel(model);
        }
    }
}

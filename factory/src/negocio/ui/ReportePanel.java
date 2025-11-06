package factory.negocio.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.function.Supplier;

import factory.Database;

public class ReportePanel extends JPanel {

    private final Supplier<Database> dbSupplier;
    private JTable table;

    public ReportePanel(Supplier<Database> dbSupplier) {
        this.dbSupplier = dbSupplier;
        setLayout(new BorderLayout());

        table = new JTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnLoad = new JButton("Actualizar");
        btnLoad.addActionListener(e -> cargarReportes());
        add(btnLoad, BorderLayout.SOUTH);

        cargarReportes();
    }

    private void cargarReportes() {
        Database db = dbSupplier.get();

        try (Connection conn = db.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM reporte")) {

            DefaultTableModel model = new DefaultTableModel();
            int cols = rs.getMetaData().getColumnCount();

            for (int i = 1; i <= cols; i++) model.addColumn(rs.getMetaData().getColumnName(i));

            while (rs.next()) {
                Object[] row = new Object[cols];
                for (int i = 0; i < cols; i++) row[i] = rs.getObject(i + 1);
                model.addRow(row);
            }

            table.setModel(model);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error cargando reportes: " + e.getMessage());
        }
    }
}

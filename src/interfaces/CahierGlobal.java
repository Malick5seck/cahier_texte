/*package interfaces;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class CahierGlobal extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    public CahierGlobal() {
        setTitle("Vue globale du cahier de texte");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Colonnes du tableau
        String[] columns = {"Enseignant", "Cours", "Date", "Contenu", "Statut", "Commentaire"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        chargerSeances();
    }

    private void chargerSeances() {
        String sql = "SELECT " +
                " u.prenom, " +
                " u.nom, " +
                " c.nom AS cours, " +
                " s.dateseance AS date_seance," +
                " s.contenu, " +
                " s.statut, " +
                " s.commentaire_refus AS commentaire" +
                "FROM " +
                "    seance s" +
                "JOIN " +
                "    utilisateur u ON s.Enseignant_id = u.id" +
                "JOIN " +
                "    cours c ON s.cours_id = c.id" +
                "ORDER BY " +
                "    s.dateseance DESC;";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/teste", "root", "");
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                String enseignant = rs.getString("prenom") + " " + rs.getString("nom");
                String cours = rs.getString("cours");
                String date = rs.getString("date_seance");
                String contenu = rs.getString("contenu");
                String statut = rs.getString("statut");
                String commentaire = rs.getString("commentaire");

                model.addRow(new Object[]{enseignant, cours, date, contenu, statut, commentaire});
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des séances.");
        }
    }
}

 */

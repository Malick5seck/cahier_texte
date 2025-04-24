/*package interfaces;

import net.miginfocom.swing.MigLayout;
import DBO.DBconnect;

import javax.swing.*;
import java.sql.*;
import java.util.HashMap;
import java.io.FileOutputStream;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

public class GenerationPDF extends JDialog {
    private JComboBox<String> coursCombo;
    private JTextArea ficheArea;
    private HashMap<String, Integer> coursMap = new HashMap<>();

    public GenerationPDF (JFrame parent) {
        super(parent, "Fiche pédagogique", true);
        setSize(600, 400);
        setLocationRelativeTo(parent);
        setLayout(new MigLayout("", "[grow]", "[][][grow]20[]"));

        coursCombo = new JComboBox<>();
        ficheArea = new JTextArea();
        ficheArea.setEditable(false);

        chargerCours();

        add(new JLabel("Sélectionner un cours :"), "wrap");
        add(coursCombo, "growx, wrap");
        add(new JScrollPane(ficheArea), "grow, span, wrap");

        JButton btnAfficher = new JButton("Afficher la fiche");
        JButton btnExportPDF = new JButton("Exporter en PDF");
        add(btnAfficher, "split 2");
        add(btnExportPDF);

        btnAfficher.addActionListener(e -> genererFiche());
        btnExportPDF.addActionListener(e -> exporterPDF());

        setVisible(true);
    }

    private void chargerCours() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT id, name FROM courses";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                int id = rs.getInt("id");
                coursCombo.addItem(name);
                coursMap.put(name, id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getNomEnseignant(int courseId) {
        String nom = "";
        try (Connection conn = DBConnect.getConnection()) {
            String sql = "SELECT u.username FROM users u JOIN courses c ON u.id = c.teacher_id WHERE c.id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, courseId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) nom = rs.getString("username");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nom;
    }

    private void genererFiche() {
        ficheArea.setText("");
        String cours = (String) coursCombo.getSelectedItem();
        if (cours == null) return;

        int courseId = coursMap.get(cours);
        String enseignant = getNomEnseignant(courseId);

        ficheArea.append("FICHE PÉDAGOGIQUE - " + cours + "\n");
        ficheArea.append("Enseignant : " + enseignant + "\n\n");

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT date, content, validated FROM sessions WHERE course_id = ? ORDER BY date ASC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, courseId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String date = rs.getString("date");
                String content = rs.getString("content");
                boolean validated = rs.getBoolean("validated");
                ficheArea.append(date + " : " + content + (validated ? " [Validée]" : " [Non validée]") + "\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void exporterPDF() {
        String cours = (String) coursCombo.getSelectedItem();
        if (cours == null) return;

        int courseId = coursMap.get(cours);
        String enseignant = getNomEnseignant(courseId);

        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new java.io.File("fiche_" + cours + ".pdf"));
        int option = chooser.showSaveDialog(this);

        if (option == JFileChooser.APPROVE_OPTION) {
            try (Connection conn = DBConnection.getConnection()) {
                String sql = "SELECT date, content, validated FROM sessions WHERE course_id = ? ORDER BY date ASC";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, courseId);
                ResultSet rs = stmt.executeQuery();

                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(chooser.getSelectedFile()));
                document.open();

                document.add(new Paragraph("FICHE PÉDAGOGIQUE - " + cours, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
                document.add(new Paragraph("Enseignant : " + enseignant));
                document.add(new Paragraph(" "));

                while (rs.next()) {
                    String date = rs.getString("date");
                    String content = rs.getString("content");
                    boolean validated = rs.getBoolean("validated");
                    document.add(new Paragraph(date + " : " + content + (validated ? " [Validée]" : " [Non validée]")));
                }

                document.close();
                JOptionPane.showMessageDialog(this, "PDF généré avec succès !");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erreur lors de l'export : " + e.getMessage());
            }
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GenerationPDF(JFrame parent ).setVisible(true);
        });
    }
}



 */

package interfaces;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import DBO.DBconnect;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GenererPDF {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final float[] COLUMN_WIDTHS = {2, 2, 3, 3, 2}; // Largeurs des colonnes du tableau emploi du temps

    // Génère le PDF de l'emploi du temps des 30 prochains jours
    public static void genererEmploiDuTempsPDF(Window parent) {
        File file = choisirFichier(parent, "EmploiDuTemps");
        if (file == null) return;

        try (PdfWriter writer = new PdfWriter(file);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            // En-tête du document
            ajouterEnTete(document, "EMPLOI DU TEMPS ");

            Table table = new Table(COLUMN_WIDTHS);
            table.setWidth(100);

            // En-tête du tableau
            ajouterCelluleEnTete(table, "Date");
            ajouterCelluleEnTete(table, "Heure");
            ajouterCelluleEnTete(table, "Cours");
            ajouterCelluleEnTete(table, "Enseignant");


            try (Connection conn = DBconnect.getconnection();
                 PreparedStatement ps = conn.prepareStatement(
                         "SELECT s.dateseance, s.heure_debut, s.heure_fin, c.nom AS cours, " +
                                 "CONCAT(u.prenom, ' ', u.nom) AS enseignant " +
                                 "FROM seance s " +
                                 "JOIN cours c ON s.cours_id = c.id " +
                                 "JOIN utilisateur u ON s.Enseignant_id = u.id " +
                                 "WHERE s.dateseance BETWEEN CURRENT_DATE AND DATE_ADD(CURRENT_DATE, INTERVAL 7 DAY) " +
                                 "ORDER BY s.dateseance, s.heure_debut"
                 );
            ) {

                ResultSet rs = ps.executeQuery();
                boolean alternate = false;

                // Parcours des résultats pour remplir le tableau
                while (rs.next()) {
                    ajouterCelluleContenu(table, rs.getDate("dateseance").toLocalDate().format(DATE_FORMAT), alternate);
                    ajouterCelluleContenu(table, rs.getTime("heure_debut") + " - " + rs.getTime("heure_fin"), alternate); // Correction ici
                    ajouterCelluleContenu(table, rs.getString("cours"), alternate);
                    ajouterCelluleContenu(table, rs.getString("enseignant"), alternate);
                    alternate = !alternate;
                }
            }

            document.add(table);
            ajouterPiedDePage(document);
            ouvrirPDF(parent, file);

        } catch (SQLException | IOException e) {
            afficherErreur(parent, "Erreur lors de la génération du PDF", e);
        }
    }

    // Génère le PDF de la liste des enseignants
    public static void genererListeEnseignantsPDF(Window parent) {
        File file = choisirFichier(parent, "ListeEnseignants");
        if (file == null) return;

        try (PdfWriter writer = new PdfWriter(file);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            ajouterEnTete(document, "LISTE DES ENSEIGNANTS");

            int totalEnseignants = compterEnseignants();
            document.add(new Paragraph("Nombre total d'enseignants : " + totalEnseignants)
                    .setTextAlignment(TextAlignment.LEFT)
                    .setMarginBottom(15));

            Table table = new Table(new float[]{3, 3, 4});
            table.setWidth(100);

            ajouterCelluleEnTete(table, "Nom");
            ajouterCelluleEnTete(table, "Prénom");
            ajouterCelluleEnTete(table, "Login");

            try (Connection conn = DBconnect.getconnection();
                 PreparedStatement ps = conn.prepareStatement(
                         "SELECT nom, prenom, login FROM utilisateur " +
                                 "WHERE role = 'Enseignant' ORDER BY nom, prenom"
                 );
            ) {

                ResultSet rs = ps.executeQuery();
                boolean alternate = false;
                while (rs.next()) {
                    ajouterCelluleContenu(table, rs.getString("nom"), alternate);
                    ajouterCelluleContenu(table, rs.getString("prenom"), alternate);
                    ajouterCelluleContenu(table, rs.getString("login"), alternate);
                    alternate = !alternate;
                }
            }

            document.add(table);
            ajouterPiedDePage(document);
            ouvrirPDF(parent, file);

        } catch (SQLException | IOException e) {
            afficherErreur(parent, "Erreur lors de la génération du PDF", e);
        }
    }

    // Ajoute un en-tête avec le titre et la date
    private static void ajouterEnTete(Document document, String titre) {
        Paragraph p = new Paragraph(titre)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(18)
                .setBold()
                .setMarginBottom(5);
        document.add(p);

        document.add(new Paragraph("Généré le " + LocalDate.now().format(DATE_FORMAT))
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(10)
                .setItalic()
                .setMarginBottom(20));
    }


    private static void ajouterCelluleEnTete(Table table, String texte) {
        Cell cell = new Cell()
                .add(new Paragraph(texte).setBold())
                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);
        table.addHeaderCell(cell);
    }

    private static void ajouterCelluleContenu(Table table, String texte, boolean alternate) {
        Cell cell = new Cell()
                .add(new Paragraph(texte))
                .setBackgroundColor(alternate ? ColorConstants.WHITE : new DeviceRgb(240, 240, 240))
                .setPadding(5)
                .setTextAlignment(TextAlignment.LEFT)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);
        table.addCell(cell);
    }

    // Pied de page informatif
    private static void ajouterPiedDePage(Document document) {
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Document généré par l'application de gestion du cahier de texte")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(8)
                .setItalic());
    }

    // Compte du nombre total d'enseignants
    private static int compterEnseignants() throws SQLException {
        try (Connection conn = DBconnect.getconnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT COUNT(*) FROM utilisateur WHERE role = 'Enseignant'")) {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // Affiche une boîte de dialogue pour choisir l'emplacement de sauvegarde du fichier
    private static File choisirFichier(Window parent, String nomBase) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Enregistrer le PDF");
        fileChooser.setSelectedFile(new File(nomBase + "_" + LocalDate.now() + ".pdf"));

        int userSelection = fileChooser.showSaveDialog(parent);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (!selectedFile.getName().toLowerCase().endsWith(".pdf")) {
                return new File(selectedFile.getAbsolutePath() + ".pdf");
            }
            return selectedFile;
        }
        return null;
    }

    // Ouvre le fichier PDF généré si possible
    private static void ouvrirPDF(Window parent, File file) {
        if (Desktop.isDesktopSupported() && file.exists()) {
            int response = JOptionPane.showConfirmDialog(parent,
                    "PDF généré avec succès !\nVoulez-vous ouvrir le document ?",
                    "Succès",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (response == JOptionPane.YES_OPTION) {
                try {
                    Desktop.getDesktop().open(file);
                } catch (IOException e) {
                    afficherErreur(parent, "Impossible d'ouvrir le PDF", e);
                }
            }
        } else {
            JOptionPane.showMessageDialog(parent,
                    "PDF généré avec succès !\nEmplacement : " + file.getAbsolutePath(),
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Affiche un message d'erreur en cas d'exception
    private static void afficherErreur(Window parent, String message, Exception e) {
        JOptionPane.showMessageDialog(parent,
                message + ":\n" + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}

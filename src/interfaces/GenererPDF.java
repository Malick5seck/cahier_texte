
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
import DBO.DBconnect;
import com.itextpdf.layout.property.UnitValue;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class GenererPDF {

    // Formats et configurations constants
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final float[] COLUMN_WIDTHS_EDT = {2, 2, 3, 3};
    private static final float[] COLUMN_WIDTHS_ENS = {3, 3, 4};
    private static final DeviceRgb COULEUR_ENTETE = new DeviceRgb(220, 220, 220);
    private static final DeviceRgb COULEUR_ALTERNANCE = new DeviceRgb(240, 240, 240);

    // Point d'entrée principal du programme

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                configurerApparence();
                JFrame frame = creerFenetreInvisible();
                afficherMenuPrincipal(frame);
            } catch (Exception e) {
                afficherErreur("Erreur initiale", e);
            }
        });
    }


    // methode pour le PDF d'emploi du temps

    public static void genererEmploiDuTempsPDF(Window parent) {
        File fichier = choisirFichier(parent, "EmploiDuTemps");
        if (fichier == null) return;

        try (PdfWriter writer = new PdfWriter(fichier);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            // Configuration du document
            ajouterEnTeteDocument(document, "EMPLOI DU TEMPS");
            ajouterPeriode(document);

            // Création du tableau
            Table tableau = creerTableauEDT();
            int nbSeances = remplirTableauEDT(tableau);
            document.add(tableau);

            if (nbSeances == 0) {
                ajouterMessageAucuneDonnee(document, "Aucun cours prévu cette semaine");
            }

            ajouterPiedDePage(document);
            ouvrirPDF(parent, fichier);

        } catch (Exception e) {
            gérerErreurGeneration(parent, e);
        }
    }

    // methode pour le PDF de la liste des Enseignants

    public static void genererListeEnseignantsPDF(Window parent) {
        File fichier = choisirFichier(parent, "ListeEnseignants");
        if (fichier == null) return;

        try (PdfWriter writer = new PdfWriter(fichier);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            ajouterEnTeteDocument(document, "LISTE DES ENSEIGNANTS");
            ajouterDateGeneration(document);

            int nbEnseignants = compterEnseignants();
            document.add(new Paragraph("Nombre total d'Enseignants : " + nbEnseignants)
                    .setMarginBottom(15));

            if (nbEnseignants > 0) {
                Table tableau = creerTableauEnseignants();
                remplirTableauEnseignants(tableau);
                document.add(tableau);
            } else {
                ajouterMessageAucuneDonnee(document, "Aucun Enseignant trouvé");
            }

            ajouterPiedDePage(document);
            ouvrirPDF(parent, fichier);

        } catch (Exception e) {
            gérerErreurGeneration(parent, e);
        }
    }

    // les Méthodes d'initialisation

    private static void configurerApparence() throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }

    private static JFrame creerFenetreInvisible() {
        JFrame frame = new JFrame();
        frame.setUndecorated(true);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        return frame;
    }

    // les Méthodes de menu

    private static void afficherMenuPrincipal(Window parent) {
        String[] options = {
                "Générer l'emploi du temps",
                "Générer la liste des Enseignants",
                "Quitter"
        };

        int choix = JOptionPane.showOptionDialog(
                parent,
                "Sélectionnez l'option souhaitée:",
                "Générateur PDF",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        traiterChoixMenu(parent, choix);
    }

    private static void traiterChoixMenu(Window parent, int choix) {
        switch (choix) {
            case 0 -> genererEmploiDuTempsPDF(parent);
            case 1 -> genererListeEnseignantsPDF(parent);
            default -> System.exit(0);
        }
    }

    // Méthodes pour la generation PDF

    private static Table creerTableauEDT() {
        Table tableau = new Table(UnitValue.createPercentArray(new float[]{20f, 20f, 30f, 30f}));
        tableau.setWidth(UnitValue.createPercentValue(100));
        ajouterCelluleEnTete(tableau, "Date");
        ajouterCelluleEnTete(tableau, "Heure");
        ajouterCelluleEnTete(tableau, "Cours");
        ajouterCelluleEnTete(tableau, "Enseignant");
        return tableau;
    }


    private static Table creerTableauEnseignants() {
        Table tableau = new Table(UnitValue.createPercentArray(new float[]{20f, 30f, 50f}));

        tableau.setWidth(UnitValue.createPercentValue(100));
        ajouterCelluleEnTete(tableau, "Nom");
        ajouterCelluleEnTete(tableau, "Prénom");
        ajouterCelluleEnTete(tableau, "Login");
        return tableau;
    }

    private static void ajouterEnTeteDocument(Document doc, String titre) {
        doc.add(new Paragraph(titre)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(18)
                .setBold());
    }

    private static void ajouterPeriode(Document doc) {
        LocalDate aujourdhui = LocalDate.now();
        doc.add(new Paragraph("Période du " + aujourdhui.format(DATE_FORMAT) + " au "
                + aujourdhui.plusDays(7).format(DATE_FORMAT))
                .setTextAlignment(TextAlignment.CENTER));
    }

    private static void ajouterDateGeneration(Document doc) {
        doc.add(new Paragraph("Généré le " + LocalDate.now().format(DATE_FORMAT))
                .setTextAlignment(TextAlignment.CENTER)
                .setItalic());
    }

    private static void ajouterMessageAucuneDonnee(Document doc, String message) {
        doc.add(new Paragraph(message)
                .setFontColor(ColorConstants.RED)
                .setTextAlignment(TextAlignment.CENTER));
    }

    private static void ajouterPiedDePage(Document doc) {
        doc.add(new Paragraph("\n"));
        doc.add(new Paragraph("Document généré par l'application cahier de texte")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(11)
                .setItalic());
    }

    // Méthodes de données

    private static int remplirTableauEDT(Table tableau) throws SQLException {
        int nbLignes = 0;
        boolean alternance = false;

        try (Connection conn = DBconnect.getconnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT s.dateseance, s.heure_debut, s.heure_fin, c.nom AS cours, " +
                             "CONCAT(u.prenom, ' ', u.nom) AS Enseignant " +
                             "FROM seance s " +
                             "JOIN cours c ON s.cours_id = c.id " +
                             "JOIN utilisateur u ON s.Enseignant_id = u.id " +
                             "WHERE s.dateseance BETWEEN CURRENT_DATE AND DATE_ADD(CURRENT_DATE, INTERVAL 7 DAY) " +
                             "ORDER BY s.dateseance, s.heure_debut")) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                nbLignes++;
                ajouterLigneEDT(tableau, rs, alternance);
                alternance = !alternance;
            }
        }
        return nbLignes;
    }

    private static void ajouterLigneEDT(Table tableau, ResultSet rs, boolean alternance) throws SQLException {
        String date = formaterDate(rs.getDate("dateseance"));
        String heure = formaterHeure(rs.getTime("heure_debut"), rs.getTime("heure_fin"));
        String cours = getStringSafe(rs, "cours");
        String Enseignant = getStringSafe(rs, "Enseignant");

        ajouterCelluleContenu(tableau, date, alternance);
        ajouterCelluleContenu(tableau, heure, alternance);
        ajouterCelluleContenu(tableau, cours, alternance);
        ajouterCelluleContenu(tableau, Enseignant, alternance);
    }

    private static void remplirTableauEnseignants(Table tableau) throws SQLException {
        boolean alternance = false;

        try (Connection conn = DBconnect.getconnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT nom, prenom, login FROM utilisateur " +
                             "WHERE role = 'Enseignant' ORDER BY nom, prenom")) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ajouterLigneEnseignant(tableau, rs, alternance);
                alternance = !alternance;
            }
        }
    }

    private static void ajouterLigneEnseignant(Table tableau, ResultSet rs, boolean alternance) throws SQLException {
        String nom = getStringSafe(rs, "nom");
        String prenom = getStringSafe(rs, "prenom");
        String login = getStringSafe(rs, "login");

        ajouterCelluleContenu(tableau, nom, alternance);
        ajouterCelluleContenu(tableau, prenom, alternance);
        ajouterCelluleContenu(tableau, login, alternance);
    }

    private static int compterEnseignants() throws SQLException {
        try (Connection conn = DBconnect.getconnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT COUNT(*) FROM utilisateur WHERE role = 'Enseignant'")) {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }


    private static void ajouterCelluleEnTete(Table table, String texte) {
        table.addHeaderCell(new Cell()
                .add(new Paragraph(texte)
                        .setBold()
                        .setTextAlignment(TextAlignment.CENTER)
                        .setFontColor(ColorConstants.BLACK))
                .setBackgroundColor(COULEUR_ENTETE)
                .setPadding(5)
                .setBorder(new com.itextpdf.layout.borders.SolidBorder(ColorConstants.GRAY, 1f)));
    }



    private static void ajouterCelluleContenu(Table table, String texte, boolean alternance) {
        table.addCell(new Cell()
                .add(new Paragraph(texte)
                        .setTextAlignment(TextAlignment.LEFT))
                .setBackgroundColor(alternance ? COULEUR_ALTERNANCE : ColorConstants.WHITE)
                .setPadding(5)
                .setBorder(new com.itextpdf.layout.borders.SolidBorder(ColorConstants.LIGHT_GRAY, 0.5f)));
    }


    private static String formaterDate(Date date) {
        return date != null ? date.toLocalDate().format(DATE_FORMAT) : "N/A";
    }

    private static String formaterHeure(Time debut, Time fin) {
        String strDebut = debut != null ? debut.toLocalTime().toString().substring(0, 5) : "";
        String strFin = fin != null ? fin.toLocalTime().toString().substring(0, 5) : "";
        return strDebut + " - " + strFin;
    }

    private static String getStringSafe(ResultSet rs, String colonne) throws SQLException {
        return rs.getString(colonne) != null ? rs.getString(colonne) : "";
    }

    private static File choisirFichier(Window parent, String nomBase) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Enregistrer le PDF");
        fileChooser.setSelectedFile(new File(nomBase + "_" + LocalDate.now() + ".pdf"));

        if (fileChooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File fichier = fileChooser.getSelectedFile();
            return fichier.getName().toLowerCase().endsWith(".pdf")
                    ? fichier
                    : new File(fichier.getAbsolutePath() + ".pdf");
        }
        return null;
    }

    private static void ouvrirPDF(Window parent, File fichier) {
        try {
            if (Desktop.isDesktopSupported() && fichier.exists() && fichier.length() > 0) {
                Desktop.getDesktop().open(fichier);
            } else {
                JOptionPane.showMessageDialog(parent,
                        "PDF généré avec succès à :\n" + fichier.getAbsolutePath(),
                        "Succès",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException e) {
            afficherErreur("Impossible d'ouvrir le PDF", e);
        }
    }

    //  Gestion des erreurs

    private static void gérerErreurGeneration(Window parent, Exception e) {
        afficherErreur("Erreur lors de la génération du PDF", e);
    }

    private static void afficherErreur(String message, Exception e) {
        JOptionPane.showMessageDialog(null,
                message + ":\n" + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }

}

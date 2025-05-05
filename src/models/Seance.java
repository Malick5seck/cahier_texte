
package models;

import java.time.LocalDate;
import java.time.LocalTime;

public class Seance {

    private int id;
    private String nomCours;
    private LocalDate date;
    private LocalTime heureDebut;
    private String contenu;
    private String statut;
    private String commentaire;
    private Object commentaireRefus;

    //  Constructeur complet avec ID (pour validation/responsable)
    public Seance(int id, String nomCours, LocalDate date, LocalTime heureDebut, String contenu, String statut, String commentaire) {
        this.id = id;
        this.nomCours = nomCours;
        this.date = date;
        this.heureDebut = heureDebut;
        this.contenu = contenu;
        this.statut = statut;
        this.commentaire = commentaire;
    }

    // Constructeur sans ID
    public Seance(String nomCours, LocalDate date, LocalTime heureDebut, String contenu, String statut, String commentaire) {
        this.nomCours = nomCours;
        this.date = date;
        this.heureDebut = heureDebut;
        this.contenu = contenu;
        this.statut = statut;
        this.commentaire = commentaire;
    }

    //  Getters et Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomCours() {
        return nomCours;
    }

    public void setNomCours(String nomCours) {
        this.nomCours = nomCours;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(LocalTime heureDebut) {
        this.heureDebut = heureDebut;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }


    @Override
    public String toString() {
        return "Seance{" +
                "id=" + id +
                ", nomCours='" + nomCours + '\'' +
                ", date=" + date +
                ", heureDebut=" + heureDebut +
                ", contenu='" + contenu + '\'' +
                ", statut='" + statut + '\'' +
                ", commentaire='" + commentaire + '\'' +
                '}';
    }


    public Object getCommentaireRefus() {
        return commentaireRefus;
    }

    public void setCommentaireRefus(Object commentaireRefus) {
        this.commentaireRefus = commentaireRefus;
    }
}

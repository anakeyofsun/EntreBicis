package cat.copernic.AnnaSerrano.EntreBicisAnna.Entity;

import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;

/**
 * Entitat que representa una recompensa que pot ser obtinguda per un usuari.
 *
 * Inclou informació sobre la seva disponibilitat, estat, imatge, saldo
 * necessari i punts de bescanvi.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "recompensa")
public class Recompensa {

    /**
     * Identificador únic de la recompensa.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idRecompensa;

    /**
     * Descripció breu de la recompensa.
     */
    private String descripcio;

    /**
     * Observacions addicionals sobre la recompensa.
     */
    private String observacions;

    /**
     * Saldo necessari per poder reclamar la recompensa.
     */
    @Column(name = "saldo_necessari")
    private double saldoNecessari;

    /**
     * Imatge de la recompensa en format binari (LONGBLOB).
     */
    @Lob
    @Column(name = "imatge_recompensa", columnDefinition = "LONGBLOB")
    private byte[] imatgeRecompensa;

    /**
     * Representació de la imatge en Base64, no persistent.
     */
    @Transient // No es guarda a la base de dades
    private String imatgeBase64;

    /**
     * Estat actual de la recompensa.
     */
    private EstatRecompensaType estatRecompensa;

    /**
     * Nom del punt de bescanvi.
     */
    @Column(name = "nom_punt_bescanvi")
    private String nomPuntBescanvi;

    /**
     * Adreça física del punt de bescanvi.
     */
    @Column(name = "adresa_punt_bescanvi")
    private String adresaPuntBescanvi;

    /**
     * Data de creació de la recompensa.
     */
    @Column(name = "data_creacio")
    private LocalDate dataCreacio;

    /**
     * Data en què l’usuari va reservar la recompensa.
     */
    @Column(name = "data_reserva")
    private LocalDate dataReserva;

    /**
     * Data en què la recompensa va ser assignada definitivament.
     */
    @Column(name = "data_assignacio")
    private LocalDate dataAssignacio;

    /**
     * Data en què la recompensa va ser recollida.
     */
    @Column(name = "data_recollida")
    private LocalDate dataRecollida;

    /**
     * Usuari que ha reservat o recollit la recompensa.
     */
    @ManyToOne
    @JoinColumn(name = "email") // Clau forana cap a Usuari
    private Usuari usuari; // Pot ser null si no ha estat reclamada

}

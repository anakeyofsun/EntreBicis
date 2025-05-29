package cat.copernic.AnnaSerrano.EntreBicisAnna.Entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entitat que representa una ruta feta per un usuari.
 *
 * Inclou informació sobre distància, velocitat, saldo obtingut, estat de
 * validació,
 * la data de realització i els punts GPS registrats durant el recorregut.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ruta")
public class Ruta {

    /**
     * Identificador únic de la ruta.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idRuta;

    /**
     * Estat actual de la ruta (pendent o finalitzada).
     */
    private EstatRutaType estat;

    /**
     * Distància total recorreguda (en km).
     */
    @Column(name = "distancia_total")
    private double distanciaTotal;

    /**
     * Temps total invertit a la ruta (en minuts).
     */
    @Column(name = "temps_total")
    private double tempsTotal;

    /**
     * Velocitat mitjana de la ruta (en km/h).
     */
    @Column(name = "velocitat_mitjana")
    private double velocitatMitjana;

    /**
     * Velocitat màxima registrada durant la ruta.
     */
    @Column(name = "velocitat_maxima")
    private double velocitatMaxima;

    /**
     * Saldo obtingut a partir de la ruta, segons els paràmetres del sistema.
     */
    @Column(name = "saldo_obtingut")
    private double saldoObtingut;

    /**
     * Indica si la ruta ha estat validada per l'administrador.
     */
    private boolean validada;

    /**
     * Data en què es va realitzar la ruta.
     */
    private LocalDate data;

    /**
     * Usuari que ha realitzat la ruta.
     */
    @ManyToOne
    @JoinColumn(name = "usuari_id", nullable = false)
    private Usuari usuari;

    /**
     * Llista de punts GPS registrats durant la ruta.
     */
    @OneToMany(mappedBy = "ruta", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PuntsGPS> puntsGPS = new ArrayList<>();

    @Override
    public String toString() {
        return "Ruta{" +
                "id=" + idRuta +
                ", data=" + data +
                ", distancia=" + distanciaTotal +
                '}';
    }
}

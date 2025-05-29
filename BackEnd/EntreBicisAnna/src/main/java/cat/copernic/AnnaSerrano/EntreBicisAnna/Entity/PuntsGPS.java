package cat.copernic.AnnaSerrano.EntreBicisAnna.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;

/**
 * Entitat que representa un punt GPS dins d’una ruta.
 *
 * Cada punt conté coordenades i un temps associat, i pertany a una ruta
 * concreta.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "puntsgps")
public class PuntsGPS {

    /**
     * Identificador únic del punt GPS.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idpuntsgps")
    private long idPuntsGPS;

    /**
     * Longitud geogràfica del punt.
     */
    private double longitud;

    /**
     * Latitud geogràfica del punt.
     */
    private double latitud;

    /**
     * Temps (en mil·lisegons) en què es va registrar el punt.
     */
    private double temps;

    /**
     * Ruta a la qual pertany aquest punt GPS.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ruta_id", nullable = false)
    @JsonBackReference
    private Ruta ruta;
}

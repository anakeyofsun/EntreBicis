package cat.copernic.AnnaSerrano.EntreBicisAnna.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.InheritanceType;

/**
 * Entitat que representa els paràmetres de configuració global del sistema.
 *
 * Aquests valors es fan servir per validar les rutes, calcular saldo, i
 * gestionar recompenses.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "parametres")
public class Parametres {

    /**
     * Identificador únic dels paràmetres.
     */
    @Id
    private long idParametres;

    /**
     * Velocitat màxima vàlida (en km/h).
     */
    private double velocitatMaxValida;

    /**
     * Temps màxim d'aturada permès (en minuts).
     */
    private double tempsMaxAturada;

    /**
     * Factor de conversió entre km recorreguts i saldo obtingut.
     */
    private double conversioSaldoKm;

    /**
     * Temps màxim (en hores) perquè una recompensa pugui ser recollida després de
     * ser assignada.
     */
    private double tempsMaxRecompensa;

}

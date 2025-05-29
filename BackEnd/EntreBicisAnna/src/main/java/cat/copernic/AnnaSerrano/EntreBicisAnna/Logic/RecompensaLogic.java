package cat.copernic.AnnaSerrano.EntreBicisAnna.Logic;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.copernic.AnnaSerrano.EntreBicisAnna.Entity.EstatRecompensaType;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Entity.Recompensa;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Entity.Usuari;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Repository.RecompensaRepository;

/**
 * Servei que gestiona la lògica de negoci relacionada amb les recompenses.
 *
 * Permet crear, llistar, reservar, assignar i eliminar recompenses,
 * tenint en compte l'estat i el saldo dels usuaris.
 */
@Service
public class RecompensaLogic {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RecompensaRepository recompensaRepository;

    @Autowired
    private UsuariLogic UsuariLogic;

    /**
     * Retorna totes les recompenses registrades al sistema.
     *
     * @return llista de recompenses.
     */
    public List<Recompensa> getAllRecompenses() {
        List<Recompensa> recompenses = recompensaRepository.findAll();
        log.info("Recompenses trobades: " + recompenses.size());
        return recompenses;
    }

    /**
     * Busca una recompensa pel seu identificador.
     *
     * @param idRecompensa identificador de la recompensa.
     * @return Optional amb la recompensa trobada o buit si no existeix.
     */
    public Optional<Recompensa> findById(Long idRecompensa) {
        return recompensaRepository.findById(idRecompensa);
    }

    /**
     * Recupera totes les recompenses associades a un usuari.
     *
     * @param email correu electrònic de l'usuari.
     * @return llista de recompenses de l'usuari.
     */
    public List<Recompensa> getAllRecompensesByUsuari(String email) {
        List<Recompensa> recompenses = recompensaRepository.findByUsuariEmail(email);
        log.info("Recompenses trobades per l'usuari " + email + ": " + recompenses.size());
        return recompenses;
    }

    /**
     * Retorna totes les recompenses amb un estat determinat.
     *
     * @param estatRecompensa estat a filtrar.
     * @return llista de recompenses amb l’estat indicat.
     */
    public List<Recompensa> getAllByEstat(EstatRecompensaType estatRecompensa) {
        List<Recompensa> recompenses = recompensaRepository.findAllByEstatRecompensa(estatRecompensa);
        log.info("Recompenses trobades amb l'estat " + estatRecompensa + ": " + recompenses.size());
        return recompenses;
    }

    /**
     * Dona d’alta una nova recompensa amb estat inicial DISPONIBLE.
     *
     * @param recompensa objecte a desar.
     * @return recompensa desada.
     */
    public Recompensa altaRecompensa(Recompensa recompensa) throws Exception {
        log.info("S'ha entrat al mètode altaRecompensa");

        recompensa.setEstatRecompensa(EstatRecompensaType.DISPONIBLE);
        log.info("S'ha donat d'alta una recompensa.");

        return recompensaRepository.save(recompensa);

    }

    /**
     * Elimina una recompensa si no està reclamada per cap usuari.
     *
     * @param idRecompensa identificador de la recompensa.
     * @throws RuntimeException si la recompensa està assignada.
     */
    public void esborrarRecompensa(Long idRecompensa) {
        log.info("S'ha entrat al metode d'esborrarRecompensa de la lògica.");

        Recompensa recompensa = recompensaRepository.findById(idRecompensa)
                .orElseThrow(() -> {
                    log.warn("No existeix cap recompensa amb id: {}", idRecompensa);
                    return new RuntimeException("No existeix cap recompensa amb id: " + idRecompensa);
                });

        // Verifica si la recompensa té un usuari associat
        if (recompensa.getUsuari() != null) {
            log.warn("No es pot esborrar la recompensa amb id {} perquè està associada a l'usuari {}",
                    idRecompensa, recompensa.getUsuari().getEmail());
            throw new RuntimeException("No es pot esborrar una recompensa que ja ha estat reclamada per un usuari.");
        }
        recompensaRepository.deleteById(idRecompensa);
        log.info("Recompensa amb id {} eliminada correctament.", idRecompensa);
    }

    /**
     * Retorna la recompensa reservada actual per a un usuari (si existeix).
     *
     * @param emailUsuari correu electrònic de l'usuari.
     * @return Optional amb la recompensa reservada o buit.
     */
    public Optional<Recompensa> getRecompensesPerUsuariIDisponibilitat(String emailUsuari) {
        log.info("S'ha entrat al mètode getRecompensesPerUsuariIDisponibilitat amb email: " + emailUsuari);
        return recompensaRepository.findByUsuariEmailAndEstatRecompensa(emailUsuari, EstatRecompensaType.RESERVADA);
    }

    /**
     * Comprova si una recompensa pot ser reservada per un usuari.
     *
     * @param idRecompensa identificador de la recompensa.
     * @param emailUsuari  correu de l’usuari.
     * @return true si es pot reservar, false en cas contrari.
     */
    public boolean reservarRecompensa(Long idRecompensa, String emailUsuari) {
        log.info("S'ha entrat al mètode reservarRecompensa amb id: " + idRecompensa);
        Recompensa recompensa = recompensaRepository.findById(idRecompensa).orElse(null);
        Usuari usuari = UsuariLogic.findByEmail(emailUsuari).orElse(null);
        if (recompensa == null) {
            log.warn("No existeix cap recompensa amb id: {}", idRecompensa);
            return false;
        }
        if (recompensa.getSaldoNecessari() > usuari.getSaldo()) {
            log.warn(
                    "No es pot reservar la recompensa amb id {} perquè el saldo necessari és superior al saldo de l'usuari.",
                    idRecompensa);
            return false;
        }
        Recompensa recompensaReservadaExistent = getRecompensesPerUsuariIDisponibilitat(emailUsuari).orElse(null);
        if (recompensaReservadaExistent != null) {
            log.warn("L'usuari ja té una recompensa reservada amb id {}.",
                    recompensaReservadaExistent.getIdRecompensa());
            return false;
        }
        return true;
    }

    /**
     * Assigna definitivament una recompensa a un usuari si té saldo suficient.
     *
     * @param idRecompensa identificador de la recompensa.
     * @return recompensa assignada o `null` si no es pot assignar.
     * @throws IllegalArgumentException si el saldo de l’usuari és insuficient.
     */
    public Recompensa assignarRecompensa(Long idRecompensa) {
        log.info("S'ha entrat al mètode assignarRecompensa amb id: " + idRecompensa);
        Recompensa recompensa = recompensaRepository.findById(idRecompensa).orElse(null);
        if (recompensa == null) {
            log.warn("No existeix cap recompensa amb id: {}", idRecompensa);
            return null;
        }
        if (recompensa.getUsuari().getSaldo() < recompensa.getSaldoNecessari()) {
            log.warn(
                    "No es pot assignar la recompensa amb id {} perquè el saldo necessari és superior al saldo de l'usuari.",
                    idRecompensa);
            throw new IllegalArgumentException("No hi ha prou saldo per invalidar la ruta.");
        }
        recompensa.setEstatRecompensa(EstatRecompensaType.ASSIGNADA);
        double saldoResultant = recompensa.getUsuari().getSaldo() - recompensa.getSaldoNecessari();
        recompensa.getUsuari().setSaldo(saldoResultant);
        recompensa.setDataAssignacio(LocalDate.now());
        return recompensaRepository.save(recompensa);
    }

}

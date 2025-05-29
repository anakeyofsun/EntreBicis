package cat.copernic.AnnaSerrano.EntreBicisAnna.Logic;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.copernic.AnnaSerrano.EntreBicisAnna.Entity.PuntsGPS;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Entity.Ruta;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Entity.Usuari;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Repository.PuntsGPSRepository;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Repository.RutaRepository;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Repository.UsuariRepository;

/**
 * Servei que gestiona la lògica de negoci relacionada amb les rutes.
 *
 * Inclou funcionalitats com iniciar/finalitzar rutes, afegir punts GPS,
 * canviar l’estat de validació i obtenir rutes per usuari.
 */
@Service
public class RutesLogic {

    Logger log = Logger.getLogger(RutesLogic.class.getName());

    @Autowired
    private RutaRepository rutaRepository;

    @Autowired
    private PuntsGPSRepository puntsGPSRepository;

    @Autowired
    private UsuariRepository usuariRepository;

    @Autowired
    private ParametresLogic parametresLogic;

    /**
     * Retorna totes les rutes del sistema.
     *
     * @return llista de rutes.
     */
    public List<Ruta> getAllRutes() {
        List<Ruta> rutes = rutaRepository.findAll();
        log.info("Rutes trobades: " + rutes.size());
        return rutes;
    }

    /**
     * Retorna totes les rutes associades a un usuari.
     *
     * @param email correu electrònic de l’usuari.
     * @return llista de rutes.
     */
    public List<Ruta> getAllRutesByUsuari(String email) {
        List<Ruta> rutes = rutaRepository.findByUsuariEmail(email);
        log.info("Rutes trobades per l'usuari " + email + ": " + rutes.size());
        return rutes;
    }

    /**
     * Inicia una nova ruta assignant la data actual.
     *
     * @param ruta ruta nova a desar.
     * @return ruta iniciada.
     */
    public Ruta iniciarRuta(Ruta ruta) {
        ruta.setData(LocalDate.now());
        return rutaRepository.save(ruta);
    }

    /**
     * Finalitza una ruta existent, actualitzant estadístiques i punts GPS.
     *
     * També calcula el saldo obtingut segons els paràmetres del sistema.
     *
     * @param ruta ruta amb les dades actualitzades.
     * @return ruta finalitzada i desada.
     */
    public Ruta finalitzarRuta(Ruta ruta) {
        Ruta rutaExistent = rutaRepository.findById(ruta.getIdRuta())
                .orElseThrow(() -> new RuntimeException("No s'ha trobat la ruta amb id: " + ruta.getIdRuta()));

        // Actualitzem només els camps que han de canviar
        rutaExistent.setEstat(ruta.getEstat());
        rutaExistent.setDistanciaTotal(ruta.getDistanciaTotal());
        rutaExistent.setTempsTotal(ruta.getTempsTotal());
        rutaExistent.setVelocitatMitjana(ruta.getVelocitatMitjana());
        rutaExistent.setVelocitatMaxima(ruta.getVelocitatMaxima());
        double saldoRuta = ruta.getSaldoObtingut() * parametresLogic.getParametres().getConversioSaldoKm();
        ;
        rutaExistent.setSaldoObtingut(saldoRuta);
        rutaExistent.setValidada(ruta.isValidada());

        // També has d'afegir els punts GPS a la ruta existents
        if (ruta.getPuntsGPS() != null) {
            for (PuntsGPS punt : ruta.getPuntsGPS()) {
                punt.setRuta(rutaExistent); // Important: Assignar la ruta als punts
            }
            rutaExistent.getPuntsGPS().clear(); // Borrem els antics
            rutaExistent.getPuntsGPS().addAll(ruta.getPuntsGPS());
        }

        return rutaRepository.save(rutaExistent);
    }

    /**
     * Afegeix punts GPS a una ruta concreta.
     *
     * @param rutaId identificador de la ruta.
     * @param punts  llista de punts GPS a afegir.
     */
    public void afegirPuntsGPS(Long rutaId, List<PuntsGPS> punts) {
        // Busca la ruta
        Ruta ruta = rutaRepository.findById(rutaId)
                .orElseThrow(() -> new RuntimeException("Ruta no trobada"));

        // Assigna els punts a la ruta
        for (PuntsGPS punt : punts) {
            punt.setRuta(ruta); // assuming tens una relació many-to-one a PuntsGPS -> Ruta
        }

        // Guarda tots els punts (hauries de tenir un PuntsGPSRepository)
        puntsGPSRepository.saveAll(punts);
    }

    /**
     * Recupera una ruta pel seu identificador.
     *
     * @param idRuta identificador de la ruta.
     * @return objecte Ruta.
     * @throws RuntimeException si no es troba.
     */
    public Ruta findById(Long idRuta) {
        return rutaRepository.findById(idRuta)
                .orElseThrow(() -> new RuntimeException("Ruta no trobada amb id: " + idRuta));
    }

    /**
     * Canvia l’estat de validació d’una ruta i actualitza el saldo de l’usuari.
     *
     * @param idRuta   identificador de la ruta.
     * @param validada nou estat de validació.
     * @throws IllegalArgumentException si no hi ha prou saldo per invalidar.
     */
    public void canviarEstatValidacio(Long idRuta, boolean validada) {
        Ruta ruta = rutaRepository.findById(idRuta)
                .orElseThrow(() -> new RuntimeException("Ruta no trobada"));

        Usuari usuari = ruta.getUsuari();
        double saldoRuta = ruta.getSaldoObtingut();
        double saldoActual = usuari.getSaldo();

        // Només modifica saldo si hi ha un canvi d’estat
        if (validada && !ruta.isValidada()) {
            usuari.setSaldo(saldoActual + saldoRuta);
        } else if (!validada && ruta.isValidada()) {
            if (saldoActual < saldoRuta) {
                throw new IllegalArgumentException("No hi ha prou saldo per invalidar la ruta.");
            }
            usuari.setSaldo(saldoActual - saldoRuta);
        }

        ruta.setValidada(validada);

        usuariRepository.save(usuari);
        rutaRepository.save(ruta);
    }
}

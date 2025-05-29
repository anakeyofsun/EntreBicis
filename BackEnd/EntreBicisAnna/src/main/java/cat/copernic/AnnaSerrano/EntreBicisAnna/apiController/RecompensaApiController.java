package cat.copernic.AnnaSerrano.EntreBicisAnna.apiController;

import java.util.List;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Entity.EstatRecompensaType;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Entity.Recompensa;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Entity.Usuari;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Logic.RecompensaLogic;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Logic.UsuariLogic;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Repository.RecompensaRepository;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Repository.UsuariRepository;

/**
 * Controlador REST per a la gestió de recompenses a través de l'API.
 * 
 * Proporciona funcionalitats per consultar, reservar i recollir recompenses.
 */
@RestController
@RequestMapping("/api/recompensa")
@CrossOrigin(origins = "*")
public class RecompensaApiController {

    @Autowired
    private RecompensaLogic recompensaLogic;

    @Autowired
    private RecompensaRepository recompensaRepository;

    @Autowired
    private UsuariLogic usuariLogic;

    @Autowired
    private UsuariRepository usuariRepository;

    /**
     * Obté totes les recompenses amb estat "DISPONIBLE".
     *
     * @return llista de recompenses disponibles.
     */
    @GetMapping("/get/disponibles")
    public ResponseEntity<List<Recompensa>> getRecompensesDisponibles() {
        List<Recompensa> llista = recompensaLogic.getAllByEstat(EstatRecompensaType.DISPONIBLE);
        return ResponseEntity.ok(llista);
    }

    /**
     * Obté totes les recompenses associades a un usuari concret.
     *
     * @param email correu electrònic de l'usuari.
     * @return llista de recompenses de l'usuari.
     */
    @GetMapping("/usuari/{email}")
    public ResponseEntity<List<Recompensa>> getRecompensesUsuari(@PathVariable String email) {
        List<Recompensa> llista = recompensaLogic.getAllRecompensesByUsuari(email);
        return ResponseEntity.ok(llista);
    }

    /**
     * Consulta una recompensa pel seu identificador.
     *
     * @param id identificador de la recompensa.
     * @return la recompensa trobada o un error 404 si no existeix.
     */
    @GetMapping("/consultar/{id}")
    public ResponseEntity<Recompensa> getRecompensaById(@PathVariable Long id) {
        return recompensaLogic.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Permet a un usuari reservar una recompensa concreta.
     *
     * @param idRecompensa identificador de la recompensa.
     * @param emailUsuari  correu electrònic de l'usuari.
     * @return recompensa reservada o error si la reserva no és possible.
     */
    @PostMapping("/reservar/{idRecompensa}/{emailUsuari}")
    public ResponseEntity<?> reservarRecompensa(@PathVariable Long idRecompensa, @PathVariable String emailUsuari) {

        Recompensa recompensa = recompensaLogic.findById(idRecompensa).get();
        Usuari usuari = usuariLogic.findByEmail(emailUsuari).get();
        Boolean esPotReservar = recompensaLogic.reservarRecompensa(idRecompensa, emailUsuari);
        if (esPotReservar) {
            recompensa.setUsuari(usuari);
            recompensa.setDataReserva(LocalDate.now());
            recompensa.setEstatRecompensa(EstatRecompensaType.RESERVADA);
            recompensaRepository.save(recompensa);
            usuariRepository.save(usuari);
            return ResponseEntity.ok(recompensa);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No es pot reservar la recompensa.");
        }
    }

    /**
     * Marca una recompensa com a recollida si ja havia estat assignada.
     *
     * @param idRecompensa identificador de la recompensa.
     * @return recompensa actualitzada o error si no es pot recollir.
     */
    @PostMapping("/recollir/{idRecompensa}")
    public ResponseEntity<?> recollirRecompensa(@PathVariable Long idRecompensa) {
        Recompensa recompensa = recompensaLogic.findById(idRecompensa).get();
        if (recompensa.getEstatRecompensa() == EstatRecompensaType.ASSIGNADA) {
            recompensa.setEstatRecompensa(EstatRecompensaType.RECOLLIDA);
            recompensa.setDataRecollida(LocalDate.now());
            recompensaRepository.save(recompensa);
            return ResponseEntity.ok(recompensa);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No es pot recollir la recompensa.");
        }
    }
}

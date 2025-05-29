package cat.copernic.AnnaSerrano.EntreBicisAnna.apiController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Entity.PuntsGPS;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Entity.Ruta;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Logic.RutesLogic;

/**
 * Controlador REST per a la gestió de rutes.
 * 
 * Permet iniciar i finalitzar rutes, afegir punts GPS,
 * consultar rutes per usuari i obtenir detalls d'una ruta.
 */
@RestController
@RequestMapping("/api/ruta")
@CrossOrigin(origins = "*")
public class RutaApiController {

    @Autowired
    private RutesLogic rutaLogic;

    /**
     * Inicia una nova ruta amb les dades proporcionades.
     *
     * @param novaRuta objecte Ruta a iniciar.
     * @return ruta iniciada amb identificador i timestamps.
     */
    @PostMapping("/iniciar")
    public ResponseEntity<Ruta> iniciarRuta(@RequestBody Ruta novaRuta) {
        Ruta rutaIniciada = rutaLogic.iniciarRuta(novaRuta);
        return ResponseEntity.ok(rutaIniciada); // Retornem la ruta sencera, no un text
    }

    /**
     * Finalitza una ruta activa i n'actualitza les dades (temps, saldo, etc.).
     *
     * @param ruta objecte Ruta a finalitzar.
     * @return ruta finalitzada amb informació actualitzada.
     */
    @PutMapping("/finalitzar")
    public ResponseEntity<Ruta> finalitzarRuta(@RequestBody Ruta ruta) {
        Ruta rutaFinalitzada = rutaLogic.finalitzarRuta(ruta);
        return ResponseEntity.ok(rutaFinalitzada);
    }

    /**
     * Afegeix una llista de punts GPS a una ruta ja iniciada.
     *
     * @param rutaId identificador de la ruta.
     * @param punts  llista de punts GPS a afegir.
     * @return resposta buida si s’ha fet correctament.
     */
    @PostMapping("/guardarPunts/{rutaId}/punts")
    public ResponseEntity<?> afegirPuntsGPS(@PathVariable Long rutaId, @RequestBody List<PuntsGPS> punts) {
        rutaLogic.afegirPuntsGPS(rutaId, punts);
        return ResponseEntity.ok().build();
    }

    /**
     * Recupera totes les rutes associades a un usuari.
     *
     * @param email correu electrònic de l’usuari.
     * @return llista de rutes o resposta buida si no n'hi ha cap.
     */
    @GetMapping("/llistar/{email}")
    public ResponseEntity<List<Ruta>> getRutesPerUsuari(@PathVariable String email) {
        List<Ruta> rutes = rutaLogic.getAllRutesByUsuari(email);
        if (rutes.isEmpty()) {
            return ResponseEntity.noContent().build(); // Opcional: pots retornar 200 amb llista buida si prefereixes
        } else {
            return ResponseEntity.ok(rutes);
        }
    }

    /**
     * Recupera totes les rutes associades a un usuari.
     *
     * @param email correu electrònic de l’usuari.
     * @return llista de rutes o resposta buida si no n'hi ha cap.
     */
    @GetMapping("/consulta/{idRuta}")
    public ResponseEntity<Ruta> getRutaById(@PathVariable Long idRuta) {
        Ruta ruta = rutaLogic.findById(idRuta);
        if (ruta != null) {
            return ResponseEntity.ok(ruta);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
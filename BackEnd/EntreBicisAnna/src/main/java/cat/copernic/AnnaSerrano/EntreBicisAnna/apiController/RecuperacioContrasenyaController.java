package cat.copernic.AnnaSerrano.EntreBicisAnna.apiController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Logic.CorreuLogic;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Logic.UsuariLogic;

/**
 * Controlador REST per a la recuperació de contrasenya via API.
 * 
 * Permet generar i enviar una nova contrasenya al correu de l'usuari.
 */
@RestController
@RequestMapping("/api/auth")
public class RecuperacioContrasenyaController {

    @Autowired
    private CorreuLogic correuLogic;

    @Autowired
    private UsuariLogic usuariLogic;

    /**
     * Genera i envia una nova contrasenya a l'usuari especificat pel correu.
     *
     * @param email correu electrònic de l'usuari que ha perdut l'accés.
     * @return missatge de confirmació o error si no es pot enviar.
     */
    @PutMapping("/recuperarContrasenya")
    public ResponseEntity<?> recuperarContrasenya(@RequestParam String email) {
        try {
            usuariLogic.enviarNovaContrasenya(email);
            return ResponseEntity.ok().body("S'ha enviat una nova contrasenya al correu.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}

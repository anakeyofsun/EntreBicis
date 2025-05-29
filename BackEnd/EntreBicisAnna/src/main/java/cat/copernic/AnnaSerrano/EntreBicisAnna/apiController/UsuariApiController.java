package cat.copernic.AnnaSerrano.EntreBicisAnna.apiController;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Entity.Usuari;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Logic.UsuariLogic;


/**
 * Controlador REST per gestionar els usuaris.
 */
@RestController
@RequestMapping("/api/usuari")
@CrossOrigin(origins = "*")
public class UsuariApiController {
    
    @Autowired
    private UsuariLogic usuariLogic;
      /**
     * Obté un usuari pel seu ID.
     *
     * @param userId L'ID de l'usuari.
     * @return L'usuari amb l'ID especificat.
     */
    @GetMapping("/mostrar/{email}")
    public Usuari getUserById(@PathVariable String email) {
        Usuari usuari =  usuariLogic.findByEmail(email).get();
        if (usuari.getImatgePerfil() != null) {
            String base64 = Base64.getEncoder().encodeToString(usuari.getImatgePerfil());
            usuari.setImatgePerfilBase64(base64);
        }
        return usuari;
    }

}

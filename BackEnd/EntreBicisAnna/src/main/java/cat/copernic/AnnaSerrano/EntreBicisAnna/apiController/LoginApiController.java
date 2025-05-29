package cat.copernic.AnnaSerrano.EntreBicisAnna.apiController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Entity.LoginRequest;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Entity.LoginResponse;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Entity.Usuari;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Repository.UsuariRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Optional;

/**
 * Controlador API per a la verificació d'inici de sessió.
 */
@RestController
@RequestMapping("/api/login")
@CrossOrigin(origins = "*")
public class LoginApiController {
    
    @Autowired
    private UsuariRepository usuariRepository;

    /**
     * Verifica les credencials de l'usuari.
     *
     * @param loginRequest Les credencials d'inici de sessió.
     * @return ResponseEntity amb l'estat de la verificació.
     */
    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody LoginRequest loginRequest) {
        if (loginRequest.getEmail() == null || loginRequest.getEmail().isBlank() ||
            loginRequest.getContrasenya() == null || loginRequest.getContrasenya().isBlank()) {
            return ResponseEntity.badRequest().body("Cal omplir tots els camps");
        }
    
        Optional<Usuari> optionalUser = usuariRepository.findByEmail(loginRequest.getEmail());
    
        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuari no trobat");
        }
    
        Usuari usuari = optionalUser.get();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
        if (!passwordEncoder.matches(loginRequest.getContrasenya(), usuari.getContrasenya())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contrasenya incorrecta");
        }
    
        LoginResponse response = new LoginResponse(
                usuari.getEmail(),
                usuari.getRol()
        );
    
        return ResponseEntity.ok(response);
    }

}

package cat.copernic.AnnaSerrano.EntreBicisAnna.WebController;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cat.copernic.AnnaSerrano.EntreBicisAnna.Entity.Usuari;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Logic.UsuariLogic;

/**
 * Controlador web encarregat de gestionar el procés d'inici de sessió
 * de l'usuari a través de formularis HTML.
 *
 * Aquesta classe s'encarrega de mostrar la vista de login, validar les
 * credencials introduïdes i redirigir l'usuari en funció del resultat
 * de l'autenticació.
 *
 * Es manté l'estat de la sessió activa durant la sessió d'usuari.
 */
@Controller
@Scope("session")
public class LoginWebController {

    @Autowired
    private UsuariLogic usuariLogic;

    @Autowired
    private PasswordEncoder passwordEncoder;

    Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Gestiona les peticions GET al formulari d'inici de sessió.
     *
     * Mostra la pàgina de login i, si escau, afegeix missatges d'error o de
     * tancament de sessió
     * a la vista, segons els paràmetres rebuts.
     *
     * @param error  indica si hi ha hagut un error d'autenticació prèvia.
     * @param logout indica si l'usuari ha tancat la sessió.
     * @param model  model que s’utilitza per passar dades a la vista.
     * @return la vista "login" per mostrar el formulari.
     */
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {

        if (error != null) {
            log.warn("Error d'autenticació: Usuari o contrasenya incorrectes.");
            model.addAttribute("error", "Usuari o contrasenya incorrectes, torna a intentar");
        }
        if (logout != null) {
            log.info("Sessió tancada correctament.");
            model.addAttribute("logout", "Sessió tancada correctament");
        }
        return "login";
    }

    /**
     * Gestiona les peticions POST per verificar les credencials d'inici de sessió.
     *
     * Comprova si les dades de l'usuari són correctes i redirigeix a la vista de
     * rutes si
     * l'autenticació té èxit. Si les credencials són incorrectes, redirigeix
     * novament a la
     * pàgina de login amb un missatge d'error.
     *
     * @param email       correu electrònic introduït per l'usuari.
     * @param contrasenya contrasenya introduïda per l'usuari.
     * @param model       model utilitzat per passar missatges a la vista.
     * @return redirecció a la vista corresponent segons l'estat de l'autenticació.
     */
    @PostMapping("/verificar")
    public String postMethodName(@RequestParam("email") String email,
            @RequestParam("contrasenya") String contrasenya,
            Model model) {

        log.info("Iniciant verificació de credencials per a l'usuari: {}", email);
        Optional<Usuari> user = usuariLogic.findByEmail(email);
        if (user.isEmpty() || !passwordEncoder.matches(contrasenya, user.get().getContrasenya())) {
            log.warn("Error d'autenticació per a l'usuari: {}", email);
            String error = "Usuari o contrasenya incorrectes, torna a intentar";
            model.addAttribute("error", error);
            return "redirect:/login?error=true";
        }
        log.info("Usuari autenticat correctament: {}", email);
        // session.setAttribute("usuari", user.get()); // Desa l'usuari en sessió

        return "redirect:/llistarRutes";

    }

}

package cat.copernic.AnnaSerrano.EntreBicisAnna.WebController;

import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Entity.Usuari;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Logic.UsuariLogic;

/**
 * Controlador web per a la gestió dels usuaris.
 *
 * Permet afegir, modificar, consultar i llistar usuaris,
 * així com visualitzar la seva imatge de perfil.
 */
@Controller
@RequestMapping("/usuaris")
public class UsuarisWebController {

    Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UsuariLogic usuarisLogic;

    /**
     * Mostra una llista de tots els usuaris registrats.
     *
     * @param model model per passar la llista d’usuaris a la vista.
     * @return la vista "llistarUsuaris".
     */
    @GetMapping("/llistar")
    public String llistarUsuaris(Model model) {
        log.info("➡ Cridant llistarUsuaris()...");

        List<Usuari> usuaris = usuarisLogic.getAllUsuaris();
        // Obtenim la llista de rutes des de la lògica

        for (Usuari usuari : usuaris) {
            if (usuari.getImatgePerfil() != null) {
                String base64Image = Base64.getEncoder().encodeToString(usuari.getImatgePerfil());
                usuari.setImatgePerfilBase64(base64Image);
            }
        }
        model.addAttribute("usuaris", usuaris);

        return "llistarUsuaris";
    }

    /**
     * Mostra el formulari per crear un nou usuari.
     *
     * @param model model utilitzat per passar un usuari buit a la vista.
     * @return la vista "afegirUsuari".
     */

    @GetMapping("/afegir")
    public String mostrarFormulariAfegirUsuari(Model model) {
        log.info("➡ Cridant mostrarFormulariAfegirUsuari()...");

        Usuari usuari = new Usuari();
        model.addAttribute("usuari", usuari);

        return "afegirUsuari";
    }

    /**
     * Dona d’alta un nou usuari amb les dades introduïdes al formulari.
     *
     * @param model     model utilitzat per mostrar dades i missatges d’error.
     * @param usuari    objecte usuari amb les dades del formulari.
     * @param imageFile imatge de perfil en format MultipartFile.
     * @return redirecció a la llista d’usuaris o retorn al formulari amb error.
     */
    @PostMapping("/afegir")
    public String afegirUsuari(Model model, @ModelAttribute("usuari") Usuari usuari,
            @RequestParam("imageFile") MultipartFile imageFile) {
        log.info("➡ Cridant afegirUsuari()...");

        try {

            usuari.setDataAlta(LocalDate.now());
            usuari.setSaldo(0.0);
            if (!imageFile.isEmpty()) {
                usuari.setImatgePerfil(imageFile.getBytes());
            }

            usuarisLogic.altaUsuari(usuari);
            log.info("Client registrat correctament amb email: {}", usuari.getEmail());

            log.info("Documentació del client guardada correctament per al email: {}", usuari.getEmail());
        } catch (Exception e) {
            log.error("Error durant el registre del client amb email: " + usuari.getEmail() + " " + e);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("client", usuari);

            if (usuari.getImatgePerfil() != null) {
                String imatgeBase64 = Base64.getEncoder().encodeToString(usuari.getImatgePerfil());
                model.addAttribute("imatgeBase64", imatgeBase64);
            }

            return "afegirUsuari";
        }
        log.info("Procés d'alta finalitzat amb èxit per al client amb email: {}", usuari.getEmail());
        return "redirect:/usuaris/llistar";

    }

    /**
     * Converteix una imatge en format byte array a una cadena codificada en Base64.
     *
     * @param imatge imatge en bytes.
     * @return la representació Base64 de la imatge.
     */
    public String convertirImatgeABase64(byte[] imatge) {
        if (imatge == null) {
            return ""; // O devuelve una imagen por defecto en Base64 si lo prefieres
        }
        return Base64.getEncoder().encodeToString(imatge);
    }

    /**
     * Mostra els detalls d’un usuari concret.
     *
     * @param email correu electrònic de l’usuari.
     * @param model model per passar l’usuari a la vista.
     * @return la vista "consultaUsuari" o redirecció si no existeix.
     */
    @GetMapping("/consulta/{email}")
    public String visualitzarUsuari(@PathVariable String email, Model model) {

        log.info("Iniciant consulta per a l'usuari amb email: {}", email);

        Optional<Usuari> optionalUsuari = usuarisLogic.findByEmail(email);

        if (optionalUsuari.isEmpty()) {
            log.warn("No s'ha trobat cap usuari amb email: {}", email);
            model.addAttribute("error", "No s'ha trobat cap usuari amb email: " + email);
            return "redirect:/usuaris/llistar";
        }

        Usuari usuari = optionalUsuari.get();

        usuari.setImatgePerfilBase64(
                usuari.getImatgePerfil() != null ? convertirImatgeABase64(usuari.getImatgePerfil()) : "");

        model.addAttribute("usuari", usuari);
        log.info("Consulta finalitzada correctament per a l'usuari amb email: {}", email);

        return "consultaUsuari";
    }

    /**
     * Carrega el formulari amb les dades d’un usuari per ser modificades.
     *
     * @param email correu electrònic de l’usuari.
     * @param model model utilitzat per carregar les dades a la vista.
     * @return la vista "modificarUsuari".
     */
    @GetMapping("/modificar/{email}")
    public String modificarUsuari(@PathVariable("email") String email, Model model) {
        log.info("S'està accedint al formulari de modificació per a l'usuari amb email: {}", email);
        Usuari usuari = usuarisLogic.findByEmail(email).get();
        if (usuari == null) {
            log.error("No existeix cap usuari amb aquest email: {}", email);
            throw new RuntimeException("No existeix cap usuari amb aquest email.");

        }
        String imatgeBase64 = null;
        if (usuari.getImatgePerfil() != null) {
            imatgeBase64 = convertirImatgeABase64(usuari.getImatgePerfil());
        }

        model.addAttribute("imatgeBase64", imatgeBase64);
        model.addAttribute("usuari", usuari);
        log.info("Dades carregades per a l'usuari amb email {}", email);
        log.info("Rol recuperat: " + usuari.getRol());
        return "modificarUsuari";
    }

    /**
     * Desa els canvis realitzats a un usuari.
     *
     * @param usuari       objecte amb les dades modificades.
     * @param imatgePerfil imatge actualitzada si s’ha seleccionat una nova.
     * @param model        model per mostrar errors si cal.
     * @return redirecció a la llista d’usuaris o retorn a la vista amb error.
     */
    @PostMapping("/modificar")
    public String guardarUsuariModificat(@ModelAttribute("usuari") Usuari usuari,
            @RequestParam(value = "imageFile", required = false) MultipartFile imatgePerfil, Model model) {

        try {

            Usuari existent = usuarisLogic.findByEmail(usuari.getEmail())
                    .orElseThrow(() -> new RuntimeException("Usuari no trobat"));

            if (imatgePerfil != null && !imatgePerfil.isEmpty()) {
                existent.setImatgePerfil(imatgePerfil.getBytes());
            }

            usuarisLogic.modificarUsuari(usuari);
            log.info("L'usuari amb email {} s'ha modificat correctament.", usuari.getEmail());

        } catch (Exception e) {
            log.error("Error al desar l'usuari amb email {}: {}", usuari.getEmail(), e.getMessage());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("usuari", usuari);
            return "modificarUsuari";
        }

        return "redirect:/usuaris/llistar";
    }

    /**
     * Retorna la imatge de perfil d’un usuari en format byte array.
     *
     * @param email correu electrònic de l’usuari.
     * @return resposta amb el contingut de la imatge o error si no existeix.
     */
    @GetMapping("/imatge/{email}")
    public ResponseEntity<byte[]> obtenirImatgeUsuari(@PathVariable String email) {
        Usuari usuari = usuarisLogic.findByEmail(email).orElse(null);

        if (usuari == null || usuari.getImatgePerfil() == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(usuari.getImatgePerfil());
    }
}

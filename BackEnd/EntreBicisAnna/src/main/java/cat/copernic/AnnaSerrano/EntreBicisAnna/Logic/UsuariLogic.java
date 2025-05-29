package cat.copernic.AnnaSerrano.EntreBicisAnna.Logic;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import cat.copernic.AnnaSerrano.EntreBicisAnna.Entity.Usuari;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Repository.UsuariRepository;

/**
 * Servei que encapsula la lògica de negoci relacionada amb els usuaris.
 *
 * Proporciona funcionalitats per donar d'alta, modificar, recuperar i
 * gestionar usuaris, inclosa la generació i enviament de contrasenyes.
 */
@Service
public class UsuariLogic {

    @Autowired
    private UsuariRepository usuariRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CorreuLogic correuLogic;

    Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Busca un usuari pel seu correu electrònic.
     *
     * @param email el correu electrònic de l'usuari.
     * @return un Optional amb l’usuari si existeix.
     */
    public Optional<Usuari> findByEmail(String email) {
        return usuariRepository.findByEmail(email);
    }

    /**
     * Retorna tots els usuaris registrats al sistema.
     *
     * @return llista d’usuaris.
     */
    public List<Usuari> getAllUsuaris() {
        List<Usuari> usuaris = usuariRepository.findAll();
        log.info("Usuaris trobats: " + usuaris.size());
        return usuaris;
    }

    /**
     * Dona d'alta un nou usuari al sistema.
     * Comprova si ja existeix, encripta la contrasenya i desa l’usuari.
     *
     * @param usuari objecte Usuari a registrar.
     * @return usuari desat.
     * @throws Exception si ja existeix un usuari amb el mateix correu.
     */
    public Usuari altaUsuari(Usuari usuari) throws Exception {
        log.info("S'ha entrat al mètode altaClient");

        log.info("S'ha donat d'alta a un client.");

        Optional<Usuari> existent = usuariRepository.findByEmail(usuari.getEmail());

        if (existent.isPresent()) {
            log.error("Ja existeix un usuari amb l'email: {}", usuari.getEmail());
            throw new Exception("Ja existeix un usuari registrat amb aquest correu electrònic.");
        }

        String contrasenyaEncriptada = passwordEncoder.encode(usuari.getContrasenya());
        usuari.setContrasenya(contrasenyaEncriptada);

        log.info("S'ha encriptat la contrasenya");

        return usuariRepository.save(usuari);

    }

    /**
     * Modifica un usuari existent. Manté els camps antics si no han estat
     * modificats explícitament.
     *
     * @param usuari objecte Usuari amb els canvis aplicats.
     * @return usuari actualitzat i desat.
     * @throws RuntimeException si no existeix l’usuari amb el correu indicat.
     */
    public Usuari modificarUsuari(Usuari usuari) {
        log.info("S'ha entrat al mètode modificarUsuari");

        Optional<Usuari> usuariExistent = usuariRepository.findByEmail(usuari.getEmail());

        if (usuariExistent.isEmpty()) {
            log.error("No existeix cap usuari amb aquest email: {}", usuari.getEmail());
            throw new RuntimeException("No existeix cap usuari amb aquest email.");
        }
        log.info("Rol recuperat: " + usuari.getRol());
        Usuari usuariNou = usuariExistent.get();

        // Manté els camps importants si no han estat modificats
        usuariNou.setNomComplet(usuari.getNomComplet());
        usuariNou.setTelefon(usuari.getTelefon());
        usuariNou.setPoblacio(usuari.getPoblacio());
        usuariNou.setObservacions(usuari.getObservacions());
        usuariNou.setSaldo(usuari.getSaldo());

        // Manté la contrasenya si no s'ha canviat
        if (usuari.getContrasenya() == null || usuari.getContrasenya().isEmpty()) {
            usuariNou.setContrasenya(usuariExistent.get().getContrasenya());
        } else {
            String contrasenyaEncriptada = passwordEncoder.encode(usuari.getContrasenya());
            usuariNou.setContrasenya(contrasenyaEncriptada);
        }

        // Manté la data d'alta
        usuariNou.setDataAlta(usuariExistent.get().getDataAlta());

        // Manté la imatge si no s'ha canviat
        if (usuari.getImatgePerfil() != null) {
            usuariNou.setImatgePerfil(usuari.getImatgePerfil());
        }

        // Manté el rol si no s'ha canviat
        if (usuari.getRol() != null) {
            usuariNou.setRol(usuari.getRol());
        } else {
            usuariNou.setRol(usuariExistent.get().getRol());
        }

        log.info("S'ha modificat l'usuari amb email: {}", usuari.getEmail());

        return usuariRepository.save(usuariNou);
    }

    /**
     * Genera una nova contrasenya aleatòria per a l’usuari, la desa codificada
     * i l’envia per correu electrònic.
     *
     * @param email correu electrònic de l’usuari.
     * @throws IllegalArgumentException si l’usuari no existeix.
     */
    public void enviarNovaContrasenya(String email) {
        Optional<Usuari> usuariOptional = usuariRepository.findByEmail(email);
        if (usuariOptional.isEmpty()) {
            throw new IllegalArgumentException("No existeix cap usuari amb aquest correu.");
        }

        Usuari usuari = usuariOptional.get();
        String novaContrasenya = generarContrasenyaAleatoria();
        String contrasenyaEncriptada = passwordEncoder.encode(novaContrasenya);
        usuari.setContrasenya(contrasenyaEncriptada);
        usuariRepository.save(usuari);

        correuLogic.enviarEmail(
                usuari.getEmail(),
                "Nova contrasenya - entreBicis",
                "Hola " + usuari.getNomComplet() + ",\n\nLa teva nova contrasenya és:\n\n" + novaContrasenya
                        + "\n\nEt recomanem que la canviïs tan aviat com puguis.");
    }

    /**
     * Genera una contrasenya aleatòria amb caràcters alfanumèrics i símbols.
     *
     * @return nova contrasenya aleatòria.
     */
    private String generarContrasenyaAleatoria() {
        int longitud = 10;
        String caracters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#&*!";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < longitud; i++) {
            int index = random.nextInt(caracters.length());
            sb.append(caracters.charAt(index));
        }
        return sb.toString();
    }
}

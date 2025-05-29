package cat.copernic.AnnaSerrano.EntreBicisAnna.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Entity.Usuari;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Entity.UsuariType;
import cat.copernic.AnnaSerrano.EntreBicisAnna.Repository.UsuariRepository;

/**
 * Servei que implementa la validació dels usuaris mitjançant
 * `UserDetailsService`.
 *
 * S'encarrega de carregar les dades de l'usuari per a l'autenticació a partir
 * del seu email,
 * i verifica que tingui el rol d'administrador per accedir al panell.
 */
@Service
public class ValidadorUsuaris implements UserDetailsService {

    @Autowired
    private UsuariRepository usuariRepository;

    /**
     * Carrega les dades de l’usuari a partir del correu electrònic proporcionat.
     *
     * Només permet l’accés als usuaris amb rol ADMIN.
     *
     * @param email correu electrònic de l’usuari.
     * @return objecte UserDetails amb la informació d’autenticació.
     * @throws UsernameNotFoundException si l’usuari no existeix o no és
     *                                   administrador.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Usuari usuari = usuariRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuari no trobat: " + email));

        if (usuari.getRol() != UsuariType.ADMIN) {
            throw new UsernameNotFoundException("L'usuari no es troba en la llista d'administradors.");
        }
        return User.builder()
                .username(usuari.getEmail())
                .password(usuari.getContrasenya()) // Ja ha d'estar codificada
                .roles(usuari.getRol().toString()) // ADMIN, CLIENT
                .build();

    }

}

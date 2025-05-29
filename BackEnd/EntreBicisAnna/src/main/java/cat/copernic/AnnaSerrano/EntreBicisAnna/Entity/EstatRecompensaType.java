package cat.copernic.AnnaSerrano.EntreBicisAnna.Entity;

/**
 * Enum que representa els diferents estats d’una recompensa.
 *
 * - DISPONIBLE: la recompensa està disponible per ser reservada.
 * - RESERVADA: la recompensa ha estat reservada per un usuari.
 * - ASSIGNADA: la recompensa ha estat assignada definitivament.
 * - RECOLLIDA: la recompensa ha estat recollida físicament.
 */
public enum EstatRecompensaType {
    DISPONIBLE, RESERVADA, ASSIGNADA, RECOLLIDA
}
package com.anna.entrebicisanna.Core.Models

/**
 * Classe que representa una sol·licitud d'inici de sessió.
 *
 * @param email L'email per a l'inici de sessió.
 * @param contrasenya La contrasenya per a l'inici de sessió.
 */
data class LoginRequest(
    val email: String,
    val contrasenya: String
)

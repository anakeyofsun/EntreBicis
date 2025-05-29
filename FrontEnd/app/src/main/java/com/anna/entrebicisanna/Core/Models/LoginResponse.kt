package com.anna.entrebicisanna.Core.Models

/**
 * Classe que representa una resposta d'inici de sessió.
 *
 * @param email L'email de la resposta d'inici de sessió.
 * @param usuariType El tipus d'usuari de la resposta d'inici de sessió.
 */
data class LoginResponse(
    val email: String,
    val usuariType: UsuariType
)

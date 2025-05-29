package com.anna.entrebicisanna.Usuaris.UI.ViewModel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Patterns
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anna.entrebicisanna.Core.Models.Usuari
import com.anna.entrebicisanna.Usuaris.Domain.UsuariUseCases
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Base64

/**
 * ViewModel encarregat de gestionar les operacions relacionades amb l'usuari, com ara l'autenticació,
 * la recuperació d'informació de l'usuari actual, el tancament de sessió i la recuperació de contrasenyes.
 *
 * Utilitza `UsuariUseCases` per accedir a la capa de domini i comunicar-se amb el backend.
 */
class UsuariViewModel(private val useCases: UsuariUseCases) : ViewModel() {

    private val _loginSuccess = MutableStateFlow<Boolean?>(null)
    val loginSuccess: StateFlow<Boolean?> = _loginSuccess

    private val _currentUser = MutableStateFlow<Usuari?>(null)
    val currentUser: StateFlow<Usuari?> get() = _currentUser

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError

    /**
     * Inicia sessió amb les credencials proporcionades.
     *
     * @param email L'email de l'usuari.
     * @param contrasenya La contrasenya.
     */
    fun loginUser(email: String, contrasenya: String) {
        viewModelScope.launch {
            // Validació del format de l'email
            if (!isValidEmail(email)) {
                _loginError.value = "Format d'email no vàlid"
                return@launch
            }

            val response = useCases.loginUser(email, contrasenya)
            _loginSuccess.value = response.isSuccessful

            if (response.isSuccessful) {
                val userResponse = useCases.getUser(email)
                if (userResponse.isSuccessful) {
                    val user = userResponse.body()
                    _currentUser.value = user
                    _loginSuccess.value = true
                } else {
                    _currentUser.value = null
                    _loginSuccess.value = false
                    _loginError.value = "No es pot obtenir informació de l'usuari"
                }
            } else {
                _loginSuccess.value = false
                _loginError.value = "Credencials incorrectes"
            }
        }
    }

    /**
     * Valida si l'email introduït té un format correcte.
     *
     * @param email L'email a validar.
     * @return `true` si el format és vàlid, `false` altrament.
     */
    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /**
     * Tanca la sessió de l'usuari actual.
     */
    fun tancarSessio() {
        _currentUser.value = null
        _loginSuccess.value = false
    }

    /**
     * Converteix una cadena codificada en base64 en un objecte [Bitmap].
     *
     * @param base64Str Cadena base64 que representa una imatge.
     * @return [Bitmap] si la conversió és correcta, `null` si hi ha un error.
     */
    fun base64ToBitmap(base64Str: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Envia una sol·licitud per recuperar la contrasenya associada a l'email indicat.
     * Si la petició té èxit, executa [onSuccess]; si falla, executa [onError].
     *
     * @param email Correu electrònic de l'usuari que ha oblidat la contrasenya.
     * @param onSuccess Callback que s'executa si l'enviament del correu té èxit.
     * @param onError Callback que s'executa si hi ha algun error durant l'operació.
     */
    fun recuperarContrasenya(
        email: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = useCases.enviarCorreuRecuperacio(email)
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError()
                }
            } catch (e: Exception) {
                onError()
            }
        }
    }


}
package br.com.correios.ppm.login.data

import br.com.correios.ppm.login.application.Usuario
import kotlin.math.log

class LoginRepository(
    private val service : LoginService
) {

    suspend fun getUsuarioLogado(): UsuarioRaw?{

        //Por enquanto chamar somente a service. Ver se teremos esses dados no banco.
        return fetchUsuarioLogado()
    }


    private suspend fun fetchUsuarioLogado(): UsuarioRaw? {

        var usuarioLogado : UsuarioRaw? = null

          try {

           usuarioLogado =  service.getUsuarioLogado()

        } catch (e: Exception) {

            println(e.message)

        }


        return usuarioLogado
    }
}
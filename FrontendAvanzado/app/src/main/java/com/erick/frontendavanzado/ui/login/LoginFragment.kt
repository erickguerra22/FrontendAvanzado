package com.erick.frontendavanzado.ui.login

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.erick.frontendavanzado.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var mailInputText: TextInputLayout
    private lateinit var passwordInputText: TextInputLayout
    private lateinit var btnLogin: MaterialButton
    private val validMail = "gue21781@uvg.edu.gt"

    companion object{
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mailInputText = view.findViewById(R.id.inputText_mail_loginFragment)
        passwordInputText = view.findViewById(R.id.inputText_password_loginFragment)
        btnLogin = view.findViewById(R.id.btn_login_loginFragment)

        userLogged()
        setListeners()
    }

    private fun userLogged() {
        val dataStoreKey = stringPreferencesKey("mail")

        CoroutineScope(Dispatchers.IO).launch {
            val preferences = requireActivity().dataStore.data.first()
            if(preferences[dataStoreKey] != null){
                CoroutineScope(Dispatchers.Main).launch {
                    requireView().findNavController().navigate(R.id.action_loginFragment_to_charactersFragment)
                }
            }
        }
    }

    private fun setListeners() {
        btnLogin.setOnClickListener {
            var mail = mailInputText.editText?.text.toString()
            var password = passwordInputText.editText?.text.toString()

            if(mail == validMail && password == validMail){
                CoroutineScope(Dispatchers.IO).launch {
                    saveKeyValue(
                        key = "mail",
                        value = mail
                    )
                }
                requireView().findNavController().navigate(R.id.action_loginFragment_to_charactersFragment)
            }else{
                Toast.makeText(activity, "Error: Las credenciales no coinciden o no son válidas", Toast.LENGTH_LONG).show()
            }
        }
    }

    public suspend fun deleteMail(context: Context){
        val dataStoreKey = stringPreferencesKey("mail")
        context.dataStore.edit { settings ->
            settings.remove(dataStoreKey)
        }
    }

    private suspend fun saveKeyValue(key:String, value:String) {
        val dataStoreKey = stringPreferencesKey(key)
        requireActivity().dataStore.edit { settings ->
            settings[dataStoreKey] = value
        }
    }
}
package cl.desafiolatam.desafiounobase

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

val tag = "MainActivity"

class MainActivity : AppCompatActivity() {
    private val usersKey = "userKey"
    private val tag = "MainActivity"

    private val filename = "cl.desafiolatam.desafiounobase"

    lateinit var advance: Button
    lateinit var container: ConstraintLayout
    lateinit var preferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preferences = getSharedPreferences(filename, Context.MODE_PRIVATE)
        Log.d(tag, "onCreate >>> ${preferences.all}")

        advance = findViewById(R.id.login_button)
        container = findViewById(R.id.container)
        setUpListeners()
    }

    private fun setUpListeners() {
        advance.setOnClickListener {
            val inputName = name_input.text!!.toString()
            if (inputName.isNotEmpty()) {
                val intent: Intent
                if (hasSeenWelcome(inputName)) {
                    intent = Intent(this, HomeActivity::class.java)
                } else {
                    saveUser(inputName)
                    intent = Intent(this, WelcomeActivity::class.java)
                }
                startActivity(intent)
            } else {
                Snackbar.make(container, "El nombre no puede estar vacío", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun hasSeenWelcome(name: String): Boolean {
        val stringSet = preferences.getStringSet(usersKey, setOf())
        return stringSet.contains(name)
    }

    private fun saveUser(username: String) {
        Log.d(tag, "save user $username")
        val stringSet = preferences.getStringSet(usersKey, setOf())

        Log.d(tag, "saveUser before saving >>> ${preferences.all}")
        val mutableSetOf = mutableSetOf<String>()
        // todos los datos existentes guardados en el nuevo Set
        mutableSetOf.addAll(stringSet)
        // El nuevo nombre agregado a los datos anteriores
        mutableSetOf.add(username)

        preferences.put(usersKey, mutableSetOf.toSet())
        Log.d(tag, "saveUser after saving >>> ${preferences.all}")
    }
}

fun SharedPreferences.put(key: String, value: Any) {
    with(edit()) {
        when (value) {
            is Int -> putInt(key, value)
            is String -> putString(key, value)
            is Set<*> -> putStringSet(key, value as Set<String>?)
        }
        apply()
    }
}

package com.dam.musicapp.ui

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.dam.musicapp.R
import com.dam.musicapp.model.PanelButton
import com.dam.musicapp.ui.fragments.HomeFragment
import com.dam.musicapp.ui.fragments.ProfileFragment
import com.dam.musicapp.ui.fragments.ProgressFragment
import com.dam.musicapp.ui.fragments.RehabFragment

class PanelPrincipalActivity : AppCompatActivity() {

    private lateinit var btnHome : LinearLayout
    private lateinit var btnRehab : LinearLayout
    private lateinit var btnProgress : LinearLayout
    private lateinit var btnProfile : LinearLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_panel_principal)

        btnHome = findViewById<LinearLayout>(R.id.btnHome)
        btnRehab = findViewById<LinearLayout>(R.id.btnRehab)
        btnProgress = findViewById<LinearLayout>(R.id.btnProgress)
        btnProfile = findViewById<LinearLayout>(R.id.btnProfile)


        val buttons = listOf(
            PanelButton(btnHome, HomeFragment.newInstance()),
            PanelButton(btnRehab, RehabFragment.newInstance()),
            PanelButton(btnProgress, ProgressFragment.newInstance()),
            PanelButton(btnProfile, ProfileFragment.newInstance())
        )

        loadFragment(HomeFragment.newInstance())
        setActiveButton(btnHome)
        buttons.forEach { button ->
            button.container.setOnClickListener {
                val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
                if (currentFragment?.javaClass == button.fragment.javaClass) return@setOnClickListener
                loadFragment(button.fragment)
                setActiveButton(button.container)
            }
        }












        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun setActiveButton(activeButton: LinearLayout) {
        val allButtons = listOf(btnHome, btnRehab, btnProgress, btnProfile)

        allButtons.forEach { button ->
            val isActive = button == activeButton
            button.isSelected = isActive

            val iconView = button.getChildAt(0) as? ImageView
            val textView = button.getChildAt(1) as? TextView

            if (isActive) {
                iconView?.setColorFilter(getColor(R.color.color_active_text))
                textView?.setTextColor(getColor(R.color.color_active_text))
            } else {
                iconView?.setColorFilter(getColor(R.color.color_inactive_text))
                textView?.setTextColor(getColor(R.color.color_inactive_text))
            }
        }
    }

}
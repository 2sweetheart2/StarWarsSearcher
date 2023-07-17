package me.sweetie.starwarssearcher

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import me.sweetie.starwarssearcher.requests.HTTPSRequests
import me.sweetie.starwarssearcher.databinding.ActivityMainBinding
import me.sweetie.starwarssearcher.interfaces.IPeoples
import me.sweetie.starwarssearcher.interfaces.IStarships
import me.sweetie.starwarssearcher.objects.ObjPeople
import me.sweetie.starwarssearcher.objects.ObjStarship

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        Saves.INSTANCE = Saves(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // загрузка сохранёных объектов (сохраняются только ссылки) и кеширование их в Saves
        loadPeoples {
            Saves.peopleCache = it
            loadStarships { it2 ->
                Saves.starShipCache = it2
            }
        }

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNavigationView.setupWithNavController(navController)
        binding.root.findViewById<ConstraintLayout>(R.id.content_holder_main).maxHeight =
            setNeededHeightOfInclude()

    }

    private fun setNeededHeightOfInclude(): Int {
        val top = binding.root.findViewById<MaterialToolbar>(R.id.toolbar).minimumHeight
        val bottom = binding.bottomNavigationView.minimumHeight
        return windowManager.defaultDisplay.height - top - bottom
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun loadPeoples(callback: IPeoples) {
        val ar = ArrayList<ObjPeople>()
        val ar_ = Saves.INSTANCE.getSaveObj("peoples")
        for (link in ar_) {
            HTTPSRequests.sendGet(link, { json ->
                val people = ObjPeople(json)
                people.favorite = true
                ar.add(people)
                if (ar.size == ar_.size)
                    callback.getPeoples(ar)
            }, "format=json")
        }
    }

    private fun loadStarships(callback: IStarships) {
        val ar = ArrayList<ObjStarship>()
        val ar_ = Saves.INSTANCE.getSaveObj("starships")
        for (link in ar_) {
            HTTPSRequests.sendGet(link, { json ->
                val star = ObjStarship(json)
                star.favorite = true
                ar.add(star)
                if (ar.size == ar_.size)
                    callback.getStarships(ar)
            }, "format=json")
        }
    }
}
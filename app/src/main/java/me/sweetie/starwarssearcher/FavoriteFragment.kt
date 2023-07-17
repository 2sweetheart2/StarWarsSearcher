package me.sweetie.starwarssearcher

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import me.sweetie.starwarssearcher.databinding.FragmentFavoriteBinding
import me.sweetie.starwarssearcher.objects.ObjPeople


class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root

    }

    /**
     * убирает всё из [R.id.linear_list] и вызывает метод [для показа информации из [Saves]][show]
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.linearList.removeAllViews()
        show()
    }

    /**
     * сделал этот метод отдельно, чтобы не дублировать в [show]
     */
    @SuppressLint("SetTextI18n", "InflateParams", "UseCompatLoadingForDrawables")
    private fun pastePeopleInfo(people: ObjPeople): View? {
        val layout =
            layoutInflater.inflate(R.layout.activity_people, null)
        layout.findViewById<TextView>(R.id.people_name).text =
            "name: ${people.name}"
        layout.findViewById<TextView>(R.id.people_gender).text =
            "gender: ${people.gender}"
        layout.findViewById<TextView>(R.id.people_planet_name).text =
            "name: ${people.homeworld!!.name}"
        layout.findViewById<TextView>(R.id.people_planet_diametr).text =
            "diameter: ${people.homeworld!!.diameter} kilometers"
        layout.findViewById<TextView>(R.id.people_planet_population).text =
            "population: ${people.homeworld!!.population}"
        layout.findViewById<TextView>(R.id.people_starship_count).text =
            "number of manned starships: ${people.starships.size}"
        if (people.favorite)
            layout.findViewById<ImageView>(R.id.add_to_favorite)
                .setImageDrawable(requireContext().getDrawable(R.drawable.star))
        layout.findViewById<ImageView>(R.id.add_to_favorite)
            .setOnClickListener {
                people.favorite = !people.favorite
                if (!people.favorite) {
                    layout.findViewById<ImageView>(R.id.add_to_favorite)
                        .setImageDrawable(requireContext().getDrawable(R.drawable.star_outline))
                    Saves.INSTANCE.removePeople(people)
                } else {
                    layout.findViewById<ImageView>(R.id.add_to_favorite)
                        .setImageDrawable(requireContext().getDrawable(R.drawable.star))
                    Saves.INSTANCE.savePeople(people)
                }

            }
        return layout
    }


    /**
     * создание [R.layout.content_placeholder] для людей и кораблей.
     * Если в [ObjPeople] [ObjPeople.homeworld] != null, то добавить его в [R.layout.content_placeholder], иначе
     * [ObjPeople.loadHome] -> добавить его в [R.layout.content_placeholder]
     */
    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n", "InflateParams")
    fun show() {
        Saves.INSTANCE.changed = false
        val objsPeople = Saves.peopleCache
        val objsStarships = Saves.starShipCache
        if (objsPeople.size > 0) {
            val holderPeople =
                layoutInflater.inflate(R.layout.content_placeholder, null) as ConstraintLayout
            for (people in objsPeople)
                try {
                    if (people.homeworld != null) {
                        holderPeople.findViewById<LinearLayout>(R.id.holder_list)
                            .addView(pastePeopleInfo(people))
                    } else {
                        people.loadHome {
                            try {
                                requireActivity().runOnUiThread {
                                    holderPeople.findViewById<LinearLayout>(R.id.holder_list)
                                        .addView(pastePeopleInfo(people))
                                }
                            } catch (_: Exception) {
                            }
                        }
                    }
                } catch (_: Exception) {
                }
            requireActivity().runOnUiThread {
                holderPeople.findViewById<TextView>(R.id.holder_name).text = "Peoples:"
                binding.linearList.addView(holderPeople)
            }
        }
        if (objsStarships.size > 0) {
            val holderStarships =
                layoutInflater.inflate(R.layout.content_placeholder, null) as ConstraintLayout
            for (starship in objsStarships)
                requireActivity().runOnUiThread {
                    val layout =
                        layoutInflater.inflate(R.layout.activity_starship, null)
                    layout.findViewById<TextView>(R.id.starship_name).text =
                        "name: ${starship.name}"
                    layout.findViewById<TextView>(R.id.starship_model).text =
                        "model: ${starship.model}"
                    layout.findViewById<TextView>(R.id.starship_manufacturer).text =
                        "manufacturer: ${starship.manufacturer}"
                    layout.findViewById<TextView>(R.id.starship_passanger).text =
                        "passenger count: ${starship.passengers}"

                    if (starship.favorite)
                        layout.findViewById<ImageView>(R.id.add_to_favorite)
                            .setImageDrawable(requireContext().getDrawable(R.drawable.star))
                    layout.findViewById<ImageView>(R.id.add_to_favorite).setOnClickListener {
                        starship.favorite = !starship.favorite
                        if (!starship.favorite) {
                            layout.findViewById<ImageView>(R.id.add_to_favorite)
                                .setImageDrawable(requireContext().getDrawable(R.drawable.star_outline))
                            Saves.INSTANCE.removeStarship(starship)
                        } else {
                            layout.findViewById<ImageView>(R.id.add_to_favorite)
                                .setImageDrawable(requireContext().getDrawable(R.drawable.star))
                            Saves.INSTANCE.saveStarShip(starship)
                        }
                    }

                    holderStarships.findViewById<LinearLayout>(R.id.holder_list).addView(layout)
                }
            requireActivity().runOnUiThread {
                holderStarships.findViewById<TextView>(R.id.holder_name).text = "Starships:"
                binding.linearList.addView(holderStarships)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
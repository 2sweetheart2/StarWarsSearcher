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
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import me.sweetie.starwarssearcher.requests.SwAPI
import me.sweetie.starwarssearcher.databinding.FragmentSearchBinding
import me.sweetie.starwarssearcher.objects.ObjPeople
import me.sweetie.starwarssearcher.objects.ObjStarship

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root

    }

    /**
     * Запос к api по поиску звездолётов по тексту, и если людей нашёл 0 и звездолётов 0, то
     * [показать, что нет результатов][showEmpty], иначе [показать результаты][show]
     */
    private fun searchStarships(text: String, peopleZero: Boolean) {
        SwAPI.searchStarships(text) { starships ->
            if (starships.size == 0 && peopleZero) {
                showEmpty()
            } else {
                var loadedStarships = 0
                for (starship in starships) {
                    if (Saves.INSTANCE.getSaveObj("starships").contains(starship.url))
                        starship.favorite = true
                    objsStarships.add(starship)
                    loadedStarships += 1
                    if (loadedStarships == starships.size)
                        show()
                }
            }
        }
    }

    /**
     * функция, очищает всё, что находится в [binding.linearList][binding], очищает списки результатов прошлого поиска
     * [людей][objsPeople] и [кораблей][objsStarships], прячет [progressBar (waiter)][android.widget.ProgressBar].
     * Если текста запрос 2 или более символов, то начинать искать людей по тексту [SwAPI.searchPeoples]
     */
    fun search(text: String) {
        binding.linearList.removeAllViews()
        objsPeople.clear()
        objsStarships.clear()
        binding.waiter.visibility = View.VISIBLE
        if (text.length >= 2) {
            SwAPI.searchPeoples(text) { peoples ->
                if (peoples.size == 0) {
                    searchStarships(text, true)
                } else {

                    var loadedpeople = 0
                    for (people in peoples) {
                        people.loadHome { home ->
                            if (Saves.INSTANCE.getSaveObj("peoples").contains(people.url))
                                people.favorite = true
                            objsPeople.add(people)
                            loadedpeople += 1
                            if (loadedpeople == peoples.size)
                                searchStarships(text, false)
                        }
                    }

                }
            }


        }
    }

    /**
     * [listener][TextView.doOnTextChanged] на текст, если текст меньше 2, то показывать ошибку, иначе [search]
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchEditText.editText!!.doOnTextChanged { inputText, _, _, _ ->
            if (inputText!!.length < 2) {
                if (inputText.isNotEmpty()) binding.searchEditText.error = "length less than 2"
                else
                    binding.searchEditText.error = null
            } else {
                binding.searchEditText.error = null
                search(binding.searchEditText.editText!!.text.toString())
            }
        }
    }

    /**
     * убрать [progressBar (waiter)][android.widget.ProgressBar] и поместить [R.layout.content_placeholder] с текстом
     * "no results :("
     */
    @SuppressLint("SetTextI18n", "InflateParams")
    fun showEmpty() {
        requireActivity().runOnUiThread {
            binding.waiter.visibility = View.GONE
            val layout = layoutInflater.inflate(R.layout.content_placeholder, null)
            val text_: TextView = layout.findViewById(R.id.holder_name)
            text_.text = "no results :("
            text_.textAlignment = View.TEXT_ALIGNMENT_CENTER
            binding.linearList.addView(layout)
        }
    }

    private var objsPeople: ArrayList<ObjPeople> = ArrayList()
    private var objsStarships: ArrayList<ObjStarship> = ArrayList()


    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n", "InflateParams")
            /**
             * создание [R.layout.content_placeholder] в которых находятся объекты [людей][R.layout.activity_people]
             * или [кораблей][R.layout.activity_starship]
             */
    fun show() {
        requireActivity().runOnUiThread {
            binding.waiter.visibility = View.GONE
        }

        if (objsPeople.size > 0) {
            val holderPeople =
                layoutInflater.inflate(R.layout.content_placeholder, null) as ConstraintLayout
            for (people in objsPeople)
                requireActivity().runOnUiThread {
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
                    layout.findViewById<ImageView>(R.id.add_to_favorite).setOnClickListener {
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
                    holderPeople.findViewById<LinearLayout>(R.id.holder_list).addView(layout)
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


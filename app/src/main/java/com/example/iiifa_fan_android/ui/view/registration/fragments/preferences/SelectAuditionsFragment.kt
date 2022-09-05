package com.example.iiifa_fan_android.ui.view.registration.fragments.preferences

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.iiifa_fan_android.R
import com.example.iiifa_fan_android.data.models.Preferences
import com.example.iiifa_fan_android.databinding.FragmentSelectAuditionsBinding
import com.example.iiifa_fan_android.ui.view.registration.adapters.GenreAdapter
import com.example.iiifa_fan_android.utils.extensions.hide
import com.example.iiifa_fan_android.utils.extensions.onClick
import com.example.iiifa_fan_android.utils.extensions.show

class SelectAuditionsFragment : Fragment() {
    private lateinit var binding: FragmentSelectAuditionsBinding
    private lateinit var navController: NavController
    private var list = ArrayList<Preferences>()
    private lateinit var genreAdapter: GenreAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentSelectAuditionsBinding.inflate(inflater, container, false)
        initViews()
        initListener()
        return binding.root
    }

    private fun initViews() {
        navController = Navigation.findNavController(requireActivity(), R.id.fragment_main)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        list.run {
            clear()
//            add(Preferences("Drama & Romance","1"))
//            add(Preferences("Mimicry","2"))
//            add(Preferences("Action & War","3"))
//            add(Preferences("Comedy","4"))
//            add(Preferences("Singing & Music","5"))
//            add(Preferences("Other","6"))
        }
        setChips()
    }

    private fun onBack() {
        if (binding.rvGenre.isVisible){
            binding.ibBack.hide()
            binding.rvGenre.hide()
            binding.btnSave.hide()
            binding.btnYes.show()
            binding.btnNo.show()
            binding.txtTitle.text = resources.getString(R.string.are_you_interested_in_auditions)
        }else{
            navController.navigateUp()
        }
    }

    private fun initListener() {
        binding.ibBack.onClick {
            onBack()
        }
        binding.btnYes.onClick {
            binding.ibBack.show()
            binding.rvGenre.show()
            binding.btnSave.show()
            binding.btnYes.hide()
            binding.btnNo.hide()
            binding.txtTitle.setText(resources.getString(R.string.which_genre_you_prefer))
        }
        binding.tvSkip.onClick {
            goNextToSecond()
        }
        binding.btnSave.onClick {
            goNextToSecond()
        }
    }


    private fun setChips() {
        genreAdapter = GenreAdapter(requireContext(),list)
        binding.rvGenre.adapter = genreAdapter

    }
    private fun goNextToSecond() {
//        val bundle = Bundle()
//        navController.navigate(R.id.action_selectDecadeFragment_to_selectGenreFragment, bundle)
    }


}
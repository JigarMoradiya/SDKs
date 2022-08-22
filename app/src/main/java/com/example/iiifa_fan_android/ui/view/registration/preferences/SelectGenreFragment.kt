package com.example.iiifa_fan_android.ui.view.registration.preferences

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.iiifa_fan_android.R
import com.example.iiifa_fan_android.data.models.Preferences
import com.example.iiifa_fan_android.databinding.FragmentSelectDecadeBinding
import com.example.iiifa_fan_android.databinding.FragmentSelectGenreBinding
import com.example.iiifa_fan_android.utils.extensions.onClick
import com.google.android.material.chip.Chip

class SelectGenreFragment : Fragment() {
    private lateinit var binding: FragmentSelectGenreBinding
    private lateinit var navController: NavController
    private var list = ArrayList<Preferences>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentSelectGenreBinding.inflate(inflater, container, false)
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
            add(Preferences("Action","1"))
            add(Preferences("Drama","2"))
            add(Preferences("Crime","3"))
            add(Preferences("Science Fiction","4"))
            add(Preferences("Romance","5"))
            add(Preferences("Comedy","6"))
            add(Preferences("Biography","7"))
            add(Preferences("Fantasy","8"))
            add(Preferences("Horror","9"))
            add(Preferences("Adventure","10"))
            add(Preferences("Musical","11"))
            add(Preferences("Documentary","12"))
            add(Preferences("Suspense & Triller","13"))
            add(Preferences("Noir","14"))
            add(Preferences("Historical","15"))
            add(Preferences("Other","16"))
        }
        setChips()
    }

    private fun onBack() {
        navController.navigateUp()
    }

    private fun initListener() {
        binding.btnNext.onClick {
            goNextToSecond()
        }
    }

    fun getSelectedChips(): ArrayList<String> {

        val tags = ArrayList<String>()

        binding.chipGroup.children
            .toList()
            .filter { (it as Chip).isChecked }
            .forEach {
                (it as Chip).tag?.toString()?.let {
                    tags.add(it)
                }
            }

        return tags
    }

    private fun setChips() {
        binding.chipGroup.removeAllViews()
        for (emotion in list) {
            val mChip = LayoutInflater.from(context).inflate(R.layout.layout_chip_preference, null, false) as Chip
            mChip.text = emotion.name
            mChip.tag = emotion.id

            val paddingDp = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 20f,
                requireContext().resources.displayMetrics
            ).toInt()

            val paddingDpTop = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 12f,
                requireContext().resources.displayMetrics
            ).toInt()

            mChip.isChecked = emotion.is_selected

            mChip.setPadding(paddingDp, paddingDpTop, paddingDp, paddingDpTop)
            binding.chipGroup.addView(mChip)
        }
    }
    private fun goNextToSecond() {
        val bundle = Bundle()
        navController.navigate(R.id.action_selectGenreFragment_to_selectAuditionsFragment, bundle)
    }


}
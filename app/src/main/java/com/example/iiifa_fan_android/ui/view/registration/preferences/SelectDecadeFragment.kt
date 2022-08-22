package com.example.iiifa_fan_android.ui.view.registration.preferences

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.children
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.iiifa_fan_android.R
import com.example.iiifa_fan_android.data.models.Preferences
import com.example.iiifa_fan_android.databinding.FragmentSelectDecadeBinding
import com.example.iiifa_fan_android.utils.extensions.onClick
import com.google.android.material.chip.Chip

class SelectDecadeFragment : Fragment() {
    private lateinit var binding: FragmentSelectDecadeBinding
    private lateinit var navController: NavController
    private var list = ArrayList<Preferences>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentSelectDecadeBinding.inflate(inflater, container, false)
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
            add(Preferences("50s","1"))
            add(Preferences("60s","2"))
            add(Preferences("70s","3"))
            add(Preferences("80s","4"))
            add(Preferences("90s","5"))
            add(Preferences("2000-2010","6"))
            add(Preferences("2011 to till now","7"))
        }
        setChips()
    }

    private fun onBack() {
        navController.navigateUp()
    }

    private fun initListener() {
        binding.tvSkip.onClick {
            goNextToSecond()
        }
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
        navController.navigate(R.id.action_selectDecadeFragment_to_selectGenreFragment, bundle)
    }


}
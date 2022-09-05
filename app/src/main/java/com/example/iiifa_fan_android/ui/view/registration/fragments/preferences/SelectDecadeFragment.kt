package com.example.iiifa_fan_android.ui.view.registration.fragments.preferences

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.iiifa_fan_android.R
import com.example.iiifa_fan_android.data.models.FanUser
import com.example.iiifa_fan_android.data.models.Preferences
import com.example.iiifa_fan_android.databinding.FragmentSelectDecadeBinding
import com.example.iiifa_fan_android.ui.viewmodel.FanViewModel
import com.example.iiifa_fan_android.ui.viewmodel.SettingsViewModel
import com.example.iiifa_fan_android.utils.Constants
import com.example.iiifa_fan_android.utils.CustomFunctions
import com.example.iiifa_fan_android.utils.CustomViews
import com.example.iiifa_fan_android.utils.Resource
import com.example.iiifa_fan_android.utils.extensions.onClick
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*
import kotlin.collections.ArrayList

class SelectDecadeFragment : Fragment() {
    private lateinit var binding: FragmentSelectDecadeBinding
    private lateinit var navController: NavController
    private var list = ArrayList<Preferences>()
    private val viewModel by activityViewModels<SettingsViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()
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

        getGenre()
    }
    private fun initObserver() {
        viewModel.manageEntitiesResponse.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    CustomViews.startButtonLoading(requireContext(), false)
                }
                is Resource.Success -> {
                    CustomViews.hideButtonLoading()
                    if (it.value.code == 200) {
                        CustomViews.hideButtonLoading()
                        list.clear()
                        val theList = Gson().fromJson<ArrayList<Preferences>>(it.value.content!![Constants.DATA], object :
                            TypeToken<ArrayList<Preferences>>(){}.type)
                        list.addAll(theList)
                        setChips()
                    } else{
                        CustomViews.hideButtonLoading()
                        CustomViews.showFailToast(layoutInflater, it.value.error?.message)
                    }
                }
                is Resource.Failure -> {
                    CustomViews.hideButtonLoading()
                    CustomViews.showFailToast(layoutInflater, getString(R.string.something_went_wrong))
                }
            }
        }

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

    private fun getGenre() {
        CustomViews.startButtonLoading(requireContext(), false)
        val params: MutableMap<String?, Any?> = HashMap()
        params["entity_type"] = Constants.ENTITY_TYPE_DECADE
        params["action"] = Constants.ACTION_TYPE_LIST
        viewModel.manageEntities(params)
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
        Log.e("getSelectedChips()","== "+Gson().toJson(getSelectedChips()))
        val bundle = Bundle()
//        navController.navigate(R.id.action_selectDecadeFragment_to_selectGenreFragment, bundle)
    }


}
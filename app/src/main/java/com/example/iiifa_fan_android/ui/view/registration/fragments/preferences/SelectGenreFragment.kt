package com.example.iiifa_fan_android.ui.view.registration.fragments.preferences

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.children
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.iiifa_fan_android.R
import com.example.iiifa_fan_android.data.models.Preferences
import com.example.iiifa_fan_android.databinding.FragmentSelectGenreBinding
import com.example.iiifa_fan_android.ui.view.base.BaseFragment
import com.example.iiifa_fan_android.ui.viewmodel.SettingsViewModel
import com.example.iiifa_fan_android.utils.Constants
import com.example.iiifa_fan_android.utils.CustomViews
import com.example.iiifa_fan_android.utils.extensions.onClick
import com.google.android.material.chip.Chip
import java.util.HashMap

class SelectGenreFragment : BaseFragment() {
    private lateinit var binding: FragmentSelectGenreBinding
    private lateinit var navController: NavController
    private var list = ArrayList<Preferences>()
    private val viewModel by activityViewModels<SettingsViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        initObserver()
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
//            add(Preferences("Action","1"))
//            add(Preferences("Drama","2"))
//            add(Preferences("Crime","3"))
//            add(Preferences("Science Fiction","4"))
//            add(Preferences("Romance","5"))
//            add(Preferences("Comedy","6"))
//            add(Preferences("Biography","7"))
//            add(Preferences("Fantasy","8"))
//            add(Preferences("Horror","9"))
//            add(Preferences("Adventure","10"))
//            add(Preferences("Musical","11"))
//            add(Preferences("Documentary","12"))
//            add(Preferences("Suspense & Triller","13"))
//            add(Preferences("Noir","14"))
//            add(Preferences("Historical","15"))
//            add(Preferences("Other","16"))
        }
        getGenreList()
        setChips()
    }
//    private fun initObserver() {
//        viewModel.addFanResponse.observe(this) {
//
//            when (it) {
//                is Resource.Loading -> {
//                    CustomViews.startButtonLoading(requireContext(), false)
//                }
//                is Resource.Success -> {
//                    CustomViews.hideButtonLoading()
//                    if (it.value.code == 200)
//                    {
//                        CustomViews.hideButtonLoading()
//                        val gson = GsonBuilder().create()
//                        val data = gson.fromJson(it.value.content!![Constants.DATA], FanUser::class.java)
//                        Log.e("passwordFragment","fanUserData : "+Gson().toJson(data))
//                        prefManager.setUserData(Gson().toJson(data))
//                        data?.id?.let { prefManager.setUserId(it) }
//                        data?.email?.let { prefManager.setUserEmail(it) }
//                        data?.secret?.let { prefManager.setToken(it) }
//
//                        Log.e("loginActivity","getUserData : "+Gson().toJson(prefManager.getUserData()))
//
////                MainDashboardActivity.getInstance(requireContext())
////                requireActivity().finish()
//                    }
//                    else{
//                        CustomViews.hideButtonLoading()
//                        CustomViews.showFailToast(layoutInflater, it.value.error?.message)
//                    }
//
//                }
//
//                is Resource.Failure -> {
//                    CustomViews.hideButtonLoading()
//                    CustomViews.showFailToast(layoutInflater, getString(R.string.something_went_wrong))
//                }
//            }
//        }
//
//    }
    private fun getGenreList() {
        CustomViews.startButtonLoading(requireContext(), false)
        val params: MutableMap<String?, Any?> = HashMap()
        params["entity_type"] = Constants.ENTITY_TYPE_DECADE
        params["action"] = Constants.ACTION_TYPE_LIST
        viewModel.manageEntities(params)
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
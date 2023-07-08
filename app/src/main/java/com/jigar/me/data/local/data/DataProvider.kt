package com.jigar.me.data.local.data

import android.content.Context
import android.graphics.Color
import com.jigar.me.R
import com.jigar.me.data.model.pages.*
import com.jigar.me.data.pref.AppPreferencesHelper
import com.jigar.me.utils.AppConstants
import com.jigar.me.utils.Constants
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random.Default.nextInt


object DataProvider {
    fun getAvatarList() : ArrayList<AvatarImages>{
        val list = ArrayList<AvatarImages>()
        with(list){
            add(AvatarImages(1,R.drawable.ic_avatar_girl_01))
            add(AvatarImages(2,R.drawable.ic_avatar_girl_02))
            add(AvatarImages(3,R.drawable.ic_avatar_girl_03))
            add(AvatarImages(4,R.drawable.ic_avatar_girl_04))
            add(AvatarImages(5,R.drawable.ic_avatar_girl_05))
            add(AvatarImages(6,R.drawable.ic_avatar_girl_06))
            add(AvatarImages(1001,R.drawable.ic_avatar_man_01))
            add(AvatarImages(1002,R.drawable.ic_avatar_man_02))
            add(AvatarImages(1003,R.drawable.ic_avatar_man_03))
            add(AvatarImages(1004,R.drawable.ic_avatar_man_04))
            add(AvatarImages(1005,R.drawable.ic_avatar_man_05))
            add(AvatarImages(1006,R.drawable.ic_avatar_man_06))
            add(AvatarImages(1007,R.drawable.ic_avatar_man_07))
            add(AvatarImages(1008,R.drawable.ic_avatar_man_08))
        }
        return list
    }
    fun getHomeMenuRandomIntro() : HomeMenuIntroType{
        val list = ArrayList<HomeMenuIntroType>()
        with(list){
            add(HomeMenuIntroType.freeMode)
            add(HomeMenuIntroType.videoTutorial)
            add(HomeMenuIntroType.exercise)
            add(HomeMenuIntroType.exam)
            add(HomeMenuIntroType.numberPuzzle)
            add(HomeMenuIntroType.purchase)
        }
        list.shuffle()
        list.shuffle()
        return list.first()
    }
    private fun getMultipleDimensions(abacusBeadType: AbacusBeadType = AbacusBeadType.None) : Float{
        return when (abacusBeadType) {
            AbacusBeadType.ExamResult -> {
                0.4f
            }
            AbacusBeadType.Exam -> {
                0.5f
            }
            AbacusBeadType.SettingPreview -> {
                0.7f
            }
            AbacusBeadType.FreeMode, AbacusBeadType.Exercise -> {
                0.9f
            }
            else -> {
                1f
            }
        }
    }
    fun getAbacusThemeFreeTypeList(context: Context,abacusBeadType: AbacusBeadType) : ArrayList<AbacusContent>{
        val list = ArrayList<AbacusContent>()
        val multiply = getMultipleDimensions(abacusBeadType)
        val height = (context.resources.getDimension(R.dimen.poligon_height) * multiply).toInt()
        val width = (context.resources.getDimension(R.dimen.poligon_width) * multiply).toInt()
        val space = (context.resources.getDimension(R.dimen.poligon_space) * multiply).toInt()
        with(list){
            add(AbacusContent(AppConstants.Settings.theam_Poligon_default,R.drawable.poligon_black,R.drawable.bg_abacus_frame_large_black,R.drawable.bg_abacus_frame_large_black_exam,R.color.abacus_rod_black,R.color.abacus_rod_black_dark,height,width,space,R.drawable.poligon_gray,R.drawable.poligon_black,arrayListOf(),arrayListOf()))
            add(AbacusContent(AppConstants.Settings.theam_Poligon_Blue,R.drawable.poligon_blue,R.drawable.bg_abacus_frame_large_blue,R.drawable.bg_abacus_frame_large_blue_exam,R.color.abacus_rod_blue,R.color.abacus_rod_blue_dark,height,width,space,R.drawable.poligon_gray,R.drawable.poligon_blue,arrayListOf(),arrayListOf()))
            add(AbacusContent(AppConstants.Settings.theam_Poligon_Purple,R.drawable.poligon_purple,R.drawable.bg_abacus_frame_large_purple,R.drawable.bg_abacus_frame_large_purple_exam,R.color.abacus_rod_purple,R.color.abacus_rod_purple_dark,height,width,space,R.drawable.poligon_gray,R.drawable.poligon_purple,arrayListOf(),arrayListOf()))
            add(AbacusContent(AppConstants.Settings.theam_Poligon_Blue_Sky,R.drawable.poligon_blue_sky,R.drawable.bg_abacus_frame_large_blue_sky,R.drawable.bg_abacus_frame_large_blue_sky_exam,R.color.abacus_rod_blue_sky,R.color.abacus_rod_blue_sky_dark,height,width,space,R.drawable.poligon_gray,R.drawable.poligon_blue_sky,arrayListOf(),arrayListOf()))
            add(AbacusContent(AppConstants.Settings.theam_Poligon_Orange,R.drawable.poligon_orange,R.drawable.bg_abacus_frame_large_orange,R.drawable.bg_abacus_frame_large_orange_exam,R.color.abacus_rod_orange,R.color.abacus_rod_orange_dark,height,width,space,R.drawable.poligon_gray,R.drawable.poligon_orange,arrayListOf(),arrayListOf()))
            add(AbacusContent(AppConstants.Settings.theam_Poligon_Green,R.drawable.poligon_green,R.drawable.bg_abacus_frame_large_green,R.drawable.bg_abacus_frame_large_green_exam,R.color.abacus_rod_green,R.color.abacus_rod_green_dark,height,width,space,R.drawable.poligon_gray,R.drawable.poligon_green,arrayListOf(),arrayListOf()))
            add(AbacusContent(AppConstants.Settings.theam_Poligon_Red,R.drawable.poligon_red,R.drawable.bg_abacus_frame_large_red,R.drawable.bg_abacus_frame_large_red_exam,R.color.abacus_rod_red,R.color.abacus_rod_red_dark,height,width,space,R.drawable.poligon_gray,R.drawable.poligon_red,arrayListOf(),arrayListOf()))
            add(AbacusContent(AppConstants.Settings.theam_Poligon_Tint,R.drawable.poligon_tint,R.drawable.bg_abacus_frame_large_tint,R.drawable.bg_abacus_frame_large_tint_exam,R.color.abacus_rod_tint,R.color.abacus_rod_tint_dark,height,width,space,R.drawable.poligon_gray,R.drawable.poligon_tint,arrayListOf(),arrayListOf()))
            add(AbacusContent(AppConstants.Settings.theam_Poligon_Pink,R.drawable.poligon_pink,R.drawable.bg_abacus_frame_large_pink,R.drawable.bg_abacus_frame_large_pink_exam,R.color.abacus_rod_pink,R.color.abacus_rod_pink_dark,height,width,space,R.drawable.poligon_gray,R.drawable.poligon_pink,arrayListOf(),arrayListOf()))
            add(AbacusContent(AppConstants.Settings.theam_Poligon_Yellow,R.drawable.poligon_yellow,R.drawable.bg_abacus_frame_large_yellow,R.drawable.bg_abacus_frame_large_yellow_exam,R.color.abacus_rod_yellow,R.color.abacus_rod_yellow_dark,height,width,space,R.drawable.poligon_gray,R.drawable.poligon_yellow,arrayListOf(),arrayListOf()))
            add(AbacusContent(AppConstants.Settings.theam_Poligon_Silver,R.drawable.poligon_silver,R.drawable.bg_abacus_frame_large_silver,R.drawable.bg_abacus_frame_large_silver_exam,R.color.abacus_rod_silver,R.color.abacus_rod_silver_dark,height,width,space,R.drawable.poligon_gray,R.drawable.poligon_silver,arrayListOf(),arrayListOf()))
            add(AbacusContent(AppConstants.Settings.theam_Poligon_Brown,R.drawable.poligon_brown,R.drawable.bg_abacus_frame_large_brown,R.drawable.bg_abacus_frame_large_brown_dark,R.color.abacus_rod_brown,R.color.abacus_rod_brown_dark,height,width,space,R.drawable.poligon_gray,R.drawable.poligon_brown,arrayListOf(),arrayListOf()))
        }
        return list
    }

    fun getAbacusThemePaidTypeList(context: Context,abacusBeadType: AbacusBeadType) : ArrayList<AbacusContent>{
        val list = ArrayList<AbacusContent>()
        val multiply = getMultipleDimensions(abacusBeadType)
        val faceCloseList = if (abacusBeadType == AbacusBeadType.Exam || abacusBeadType == AbacusBeadType.ExamResult){
            arrayListOf(R.drawable.face_gray_close,R.drawable.face_gray_close,R.drawable.face_gray_close,R.drawable.face_gray_close)
        }else{
            arrayListOf(R.drawable.face_pink_close,R.drawable.face_orange_close,R.drawable.face_blue_close,R.drawable.face_green_close)
        }

        val faceCloseTop = if (abacusBeadType == AbacusBeadType.Exam || abacusBeadType == AbacusBeadType.ExamResult){
            R.drawable.face_gray_close
        }else{
            R.drawable.face_red_close
        }
        val faceOpenList = arrayListOf(R.drawable.face_pink_open,R.drawable.face_orange_open,R.drawable.face_blue_open,R.drawable.face_green_open)
        val starCloseList = arrayListOf(R.drawable.star_gray_close,R.drawable.star_gray_close,R.drawable.star_gray_close,R.drawable.star_gray_close)
        val starOpenList = arrayListOf(R.drawable.star_yellow_open,R.drawable.star_blue_open,R.drawable.star_orange_open,R.drawable.star_green_open)
        val diamondCloseList = arrayListOf(R.drawable.diamond_gray,R.drawable.diamond_gray,R.drawable.diamond_gray,R.drawable.diamond_gray)
        val diamondOpenList = arrayListOf(R.drawable.diamond_purple,R.drawable.diamond_yellow,R.drawable.diamond_blue,R.drawable.diamond_green)
        val garnetCloseList = arrayListOf(R.drawable.garnet_gray,R.drawable.garnet_gray,R.drawable.garnet_gray,R.drawable.garnet_gray)
        val garnetOpenList = arrayListOf(R.drawable.garnet_purple,R.drawable.garnet_orange,R.drawable.garnet_blue,R.drawable.garnet_green)
        val shapeCloseList = arrayListOf(R.drawable.shape_stone_gray,R.drawable.shape_triangle_gray,R.drawable.shape_circle_gray,R.drawable.shape_hexagon_gray)
        val shapeOpenList = arrayListOf(R.drawable.shape_stone,R.drawable.shape_triangle,R.drawable.shape_circle,R.drawable.shape_hexagon)
        val eggCloseList = arrayListOf(R.drawable.egg,R.drawable.egg,R.drawable.egg,R.drawable.egg)
        val eggOpenList = arrayListOf(R.drawable.egg1,R.drawable.egg4,R.drawable.egg2,R.drawable.egg3)
        with(list){
            add(AbacusContent(AppConstants.Settings.theam_face,R.drawable.face_red_open,R.drawable.bg_abacus_frame_large_red_eye,R.drawable.bg_abacus_frame_large_red_eye_exam,R.color.abacus_rod_red,R.color.abacus_rod_red_dark
                ,(context.resources.getDimension(R.dimen.face_height) * multiply).toInt(),(context.resources.getDimension(R.dimen.face_width) * multiply).toInt(),(context.resources.getDimension(R.dimen.face_space) * multiply).toInt(),faceCloseTop,R.drawable.face_red_open,faceCloseList,faceOpenList))
            add(AbacusContent(AppConstants.Settings.theam_Star,R.drawable.star_red_open,R.drawable.bg_abacus_frame_large_gray,R.drawable.bg_abacus_frame_large_gray_exam,R.color.abacus_rod_gray,R.color.abacus_rod_gray_dark
                ,(context.resources.getDimension(R.dimen.star_height) * multiply).toInt(),(context.resources.getDimension(R.dimen.star_width) * multiply).toInt(),(context.resources.getDimension(R.dimen.star_space) * multiply).toInt(),R.drawable.star_gray_close,R.drawable.star_red_open,starCloseList,starOpenList))
            add(AbacusContent(AppConstants.Settings.theam_diamond,R.drawable.diamond_red,R.drawable.bg_abacus_frame_large_silver,R.drawable.bg_abacus_frame_large_silver_exam,R.color.abacus_rod_silver,R.color.abacus_rod_silver_dark
                ,(context.resources.getDimension(R.dimen.diamond_height) * multiply).toInt(),(context.resources.getDimension(R.dimen.diamond_width) * multiply).toInt(),(context.resources.getDimension(R.dimen.diamond_space) * multiply).toInt(),R.drawable.diamond_gray,R.drawable.diamond_red,diamondCloseList,diamondOpenList))
            add(AbacusContent(AppConstants.Settings.theam_garnet,R.drawable.garnet_red,R.drawable.bg_abacus_frame_large_gray,R.drawable.bg_abacus_frame_large_gray_exam,R.color.abacus_rod_gray,R.color.abacus_rod_gray_dark
                ,(context.resources.getDimension(R.dimen.garnet_height) * multiply).toInt(),(context.resources.getDimension(R.dimen.garnet_width) * multiply).toInt(),(context.resources.getDimension(R.dimen.garnet_space) * multiply).toInt(),R.drawable.garnet_gray,R.drawable.garnet_red,garnetCloseList,garnetOpenList))
            add(AbacusContent(AppConstants.Settings.theam_shape,R.drawable.shape_stone,R.drawable.bg_abacus_frame_large_gray,R.drawable.bg_abacus_frame_large_gray_exam,R.color.abacus_rod_gray,R.color.abacus_rod_gray_dark
                ,(context.resources.getDimension(R.dimen.square_width_height) * multiply).toInt(),(context.resources.getDimension(R.dimen.square_width_height) * multiply).toInt(),(context.resources.getDimension(R.dimen.square_space) * multiply).toInt(),R.drawable.shape_square_gray,R.drawable.shape_square,shapeCloseList,shapeOpenList))
            add(AbacusContent(AppConstants.Settings.theam_Egg,R.drawable.egg0,R.drawable.bg_abacus_frame_large_gray,R.drawable.bg_abacus_frame_large_gray_exam,R.color.abacus_rod_gray,R.color.abacus_rod_gray_dark
                ,(context.resources.getDimension(R.dimen.square_width_height) * multiply).toInt(),(context.resources.getDimension(R.dimen.square_width_height) * multiply).toInt(),(context.resources.getDimension(R.dimen.square_space) * multiply).toInt(),R.drawable.egg,R.drawable.egg0,eggCloseList,eggOpenList))
        }
        return list
    }

    private val abacusThemeList = ArrayList<AbacusContent>()
    fun getAllAbacusThemeTypeList(context: Context, isPaidThemeAdd : Boolean = true,abacusBeadType: AbacusBeadType) : ArrayList<AbacusContent>{
        val list = ArrayList<AbacusContent>()
        list.addAll(getAbacusThemeFreeTypeList(context,abacusBeadType))
        if (isPaidThemeAdd){
            list.addAll(getAbacusThemePaidTypeList(context,abacusBeadType))
        }
        abacusThemeList.clear()
        abacusThemeList.addAll(list)
        list.shuffle()
        return list
    }
    fun findAbacusThemeType(context: Context, theme : String, abacusBeadType: AbacusBeadType) : AbacusContent{
        getAllAbacusThemeTypeList(context,abacusBeadType = abacusBeadType)
        val content : AbacusContent? = abacusThemeList.find { it.type == theme }
        return content ?: abacusThemeList.first()
    }
    fun getHomeMenuList(context: Context) : ArrayList<HomeMenu>{
        val list = ArrayList<HomeMenu>()
        with(list){
            add(HomeMenu(AppConstants.HomeClicks.Menu_Starter,R.drawable.home_menu_starter,context.getString(R.string.beginner)))
            add(HomeMenu(AppConstants.HomeClicks.Menu_Number,R.drawable.home_menu_number))
            add(HomeMenu(AppConstants.HomeClicks.Menu_Addition,R.drawable.home_menu_addition))
            add(HomeMenu(AppConstants.HomeClicks.Menu_Addition_Subtraction,R.drawable.home_menu_subtraction))
            add(HomeMenu(AppConstants.HomeClicks.Menu_Multiplication,R.drawable.home_menu_multiplication))
            add(HomeMenu(AppConstants.HomeClicks.Menu_Division,R.drawable.home_menu_division))
            add(HomeMenu(AppConstants.HomeClicks.Menu_Exercise,R.drawable.home_menu_exercise,context.getString(R.string.popular)))
            add(HomeMenu(AppConstants.HomeClicks.Menu_DailyExam,R.drawable.home_menu_exam,context.getString(R.string.favourite)))
            add(HomeMenu(AppConstants.HomeClicks.Menu_PractiseMaterial,R.drawable.home_menu_material))
            add(HomeMenu(AppConstants.HomeClicks.Menu_Number_Puzzle,R.drawable.home_menu_number_sequence))
            add(HomeMenu(AppConstants.HomeClicks.Menu_Click_Youtube,R.drawable.home_menu_tutorial,context.getString(R.string.useful)))
            add(HomeMenu(AppConstants.HomeClicks.Menu_Purchase,R.drawable.home_menu_purchase))
        }
        return list
    }
    fun getHomeMenuItemPosition(type : Int,context: Context) : Int?{
        val list = getHomeMenuList(context)
        var position : Int? = null
        for (i in list.indices){
            if (list[i].type == type){
                position = i
                break
            }
        }
        return position
    }
    fun getOtherAppList(): ArrayList<OtherApps> {
        val list = ArrayList<OtherApps>()
        with(list) {
            add(OtherApps(AppConstants.HomeClicks.OtherApp_Number, R.drawable.logo_number,"Number learning with abacus","https://play.google.com/store/apps/details?id=com.abacus.soroban&hl=en&utm_source=ref-abacus&utm_medium=related-app&utm_campaign=app"))
            add(OtherApps(AppConstants.HomeClicks.OtherApp_Sudoku, R.drawable.logo_sudoku,"Sudoku Puzzle","https://play.google.com/store/apps/details?id=com.sudoku.puzzle.maths.number&hl=en&utm_source=ref-abacus&utm_medium=related-app&utm_campaign=app"))
        }
        return list
    }
    /* exercise */
    fun getExerciseList(context: Context) : ArrayList<ExerciseLevel>{
        val list = ArrayList<ExerciseLevel>()
        val listAddition = ArrayList<ExerciseLevelDetail>()
        with(listAddition){
            add(ExerciseLevelDetail("1",5,5,1,3))
            add(ExerciseLevelDetail("2",10,10,1,5))
            add(ExerciseLevelDetail("3",5,5,2,4))
            add(ExerciseLevelDetail("4",10,10,2,8))
            add(ExerciseLevelDetail("5",5,5,3,4))
            add(ExerciseLevelDetail("6",10,10,3,8))
            add(ExerciseLevelDetail("7",5,5,4,5))
            add(ExerciseLevelDetail("8",10,10,4,10))
            add(ExerciseLevelDetail("9",5,5,5,5))
            add(ExerciseLevelDetail("10",10,10,5,10))
            add(ExerciseLevelDetail("11",5,5,6,5))
            add(ExerciseLevelDetail("12",10,10,6,10))
        }
        val listMultiplication = ArrayList<ExerciseLevelDetail>()
        with(listMultiplication){
            add(ExerciseLevelDetail("13",5,0,3,3))
            add(ExerciseLevelDetail("14",10,0,3,5))
            add(ExerciseLevelDetail("15",5,0,4,3))
            add(ExerciseLevelDetail("16",10,0,4,5))
            add(ExerciseLevelDetail("17",5,0,5,3))
            add(ExerciseLevelDetail("18",10,0,5,5))
            add(ExerciseLevelDetail("19",5,0,6,3))
            add(ExerciseLevelDetail("20",10,0,6,5))
            add(ExerciseLevelDetail("21",5,0,7,3))
            add(ExerciseLevelDetail("22",10,0,7,5))
        }
        val listDivision = ArrayList<ExerciseLevelDetail>()
        with(listDivision){
            add(ExerciseLevelDetail("23",5,0,4,3))
            add(ExerciseLevelDetail("24",5,0,4,2))
            add(ExerciseLevelDetail("25",10,0,4,6))
            add(ExerciseLevelDetail("26",10,0,4,4))
            add(ExerciseLevelDetail("27",5,0,5,3))
            add(ExerciseLevelDetail("28",5,0,5,2))
            add(ExerciseLevelDetail("29",10,0,5,6))
            add(ExerciseLevelDetail("30",10,0,5,4))
            add(ExerciseLevelDetail("31",10,0,6,6))
            add(ExerciseLevelDetail("32",10,0,6,4))
        }
        with(list) {
            add(ExerciseLevel("1",context.getString(R.string.AdditionSubtraction),listAddition))
            add(ExerciseLevel("2",context.getString(R.string.Multiplication),listMultiplication))
            add(ExerciseLevel("3",context.getString(R.string.Division),listDivision))
        }
        return list
    }
    /* purchase colors */
    fun getColorsList() : ArrayList<ColorData>{
        val list = ArrayList<ColorData>()
        with(list) {
            add(ColorData(R.color.color3,R.color.darkColor3))
            add(ColorData(R.color.color1,R.color.darkColor1))
            add(ColorData(R.color.color2,R.color.darkColor2))
            add(ColorData(R.color.color5,R.color.darkColor5))
            add(ColorData(R.color.color4,R.color.darkColor4))
            add(ColorData(R.color.color6,R.color.darkColor6))
            add(ColorData(R.color.color9,R.color.darkColor9))
            add(ColorData(R.color.color8,R.color.darkColor8))
            add(ColorData(R.color.color10,R.color.darkColor10))
            add(ColorData(R.color.color7,R.color.darkColor7))
            add(ColorData(R.color.color11,R.color.darkColor11))
//            add(ColorData(R.color.color12,R.color.darkColor12))
            add(ColorData(R.color.color13,R.color.darkColor13))
            add(ColorData(R.color.color14,R.color.darkColor14))
            add(ColorData(R.color.color15,R.color.darkColor15))
        }
        return list
    }

    fun getDataObjectsList(): MutableList<ImagesDataObjects>{
        val listDataObjects = addDataObjectsList()
        listDataObjects.shuffle()
        listDataObjects.shuffle()
        return listDataObjects
    }
    fun addDataObjectsList(): MutableList<ImagesDataObjects>{
        val listDataObjects: MutableList<ImagesDataObjects> = arrayListOf()
        with(listDataObjects){
            add(ImagesDataObjects(DataObjectsType.Objects,"Airplane","objects_airplane"))
            add(ImagesDataObjects(DataObjectsType.Objects,"Ball","objects_ball"))
            add(ImagesDataObjects(DataObjectsType.Objects,"Balloon","objects_balloon"))
            add(ImagesDataObjects(DataObjectsType.Objects,"Bus","objects_bus"))
            add(ImagesDataObjects(DataObjectsType.Objects,"Cake","objects_cake"))
            add(ImagesDataObjects(DataObjectsType.Objects,"Cap","objects_cap"))
            add(ImagesDataObjects(DataObjectsType.Objects,"Car","objects_car"))
            add(ImagesDataObjects(DataObjectsType.Objects,"Chair","objects_chair"))
            add(ImagesDataObjects(DataObjectsType.Objects,"Crown","objects_crown"))
            add(ImagesDataObjects(DataObjectsType.Objects,"Guitar","objects_guitar"))
            add(ImagesDataObjects(DataObjectsType.Objects,"Hammer","objects_hammer"))
            add(ImagesDataObjects(DataObjectsType.Objects,"Hat","objects_hat"))
            add(ImagesDataObjects(DataObjectsType.Objects,"Helicopter","objects_helicopter"))
            add(ImagesDataObjects(DataObjectsType.Objects,"Ice Cream","objects_ice_cream"))
            add(ImagesDataObjects(DataObjectsType.Objects,"Jeep","objects_jeep"))
            add(ImagesDataObjects(DataObjectsType.Objects,"Kite","objects_kite"))
            add(ImagesDataObjects(DataObjectsType.Objects,"Lock","objects_lock"))
            add(ImagesDataObjects(DataObjectsType.Objects,"Mirror","objects_mirror"))
            add(ImagesDataObjects(DataObjectsType.Objects,"Parachute","objects_parachute"))
            add(ImagesDataObjects(DataObjectsType.Objects,"Pencil","objects_pencil"))
            add(ImagesDataObjects(DataObjectsType.Objects,"Radio","objects_radio"))
            add(ImagesDataObjects(DataObjectsType.Objects,"Robot","objects_robot"))
            add(ImagesDataObjects(DataObjectsType.Objects,"Table","objects_table"))
            add(ImagesDataObjects(DataObjectsType.Objects,"Teddy Bear","objects_teddy_bear"))
            add(ImagesDataObjects(DataObjectsType.Objects,"Train","objects_train"))
            add(ImagesDataObjects(DataObjectsType.Objects,"Umbrella","objects_umbrella"))
            add(ImagesDataObjects(DataObjectsType.Objects,"Vas","objects_vas"))
            add(ImagesDataObjects(DataObjectsType.Objects,"Watch","objects_watch"))
            add(ImagesDataObjects(DataObjectsType.Objects,"Wheel","objects_wheel"))

            add(ImagesDataObjects(DataObjectsType.Animal,"Ant","animal_ant"))
            add(ImagesDataObjects(DataObjectsType.Animal,"Bee","animal_bee"))
            add(ImagesDataObjects(DataObjectsType.Animal,"Butterfly","animal_butterfly"))
            add(ImagesDataObjects(DataObjectsType.Animal,"Camel","animal_camel"))
            add(ImagesDataObjects(DataObjectsType.Animal,"Cat","animal_cat"))
            add(ImagesDataObjects(DataObjectsType.Animal,"Cow","animal_cow"))
            add(ImagesDataObjects(DataObjectsType.Animal,"Deer","animal_deer"))
            add(ImagesDataObjects(DataObjectsType.Animal,"dinosaur","animal_dinosaur"))
            add(ImagesDataObjects(DataObjectsType.Animal,"Dog","animal_dog"))
            add(ImagesDataObjects(DataObjectsType.Animal,"Duck","animal_duck"))
            add(ImagesDataObjects(DataObjectsType.Animal,"Elephant","animal_elephant"))
            add(ImagesDataObjects(DataObjectsType.Animal,"Fox","animal_fox"))
            add(ImagesDataObjects(DataObjectsType.Animal,"Frog","animal_frog"))
            add(ImagesDataObjects(DataObjectsType.Animal,"Giraffe","animal_giraffe"))
            add(ImagesDataObjects(DataObjectsType.Animal,"Goat","animal_goat"))
            add(ImagesDataObjects(DataObjectsType.Animal,"Horse","animal_horse"))
            add(ImagesDataObjects(DataObjectsType.Animal,"Kangaroo","animal_kangaroo"))
            add(ImagesDataObjects(DataObjectsType.Animal,"Ladybug","animal_ladybug"))
            add(ImagesDataObjects(DataObjectsType.Animal,"Lion","animal_lion"))
            add(ImagesDataObjects(DataObjectsType.Animal,"Monkey","animal_monkey"))
            add(ImagesDataObjects(DataObjectsType.Animal,"Pig","animal_pig"))
            add(ImagesDataObjects(DataObjectsType.Animal,"Rabbit","animal_rabbit"))
            add(ImagesDataObjects(DataObjectsType.Animal,"Rhinoceros","animal_rhinoceros"))
            add(ImagesDataObjects(DataObjectsType.Animal,"Sheep","animal_sheep"))
            add(ImagesDataObjects(DataObjectsType.Animal,"Snake","animal_snake"))
            add(ImagesDataObjects(DataObjectsType.Animal,"Tiger","animal_tiger"))
            add(ImagesDataObjects(DataObjectsType.Animal,"Turtle","animal_turtle"))
            add(ImagesDataObjects(DataObjectsType.Animal,"Unicorn","animal_unicorn"))
            add(ImagesDataObjects(DataObjectsType.Animal,"Zebra","animal_zebra"))

            add(ImagesDataObjects(DataObjectsType.Bird,"Eagle","bird_eagle"))
            add(ImagesDataObjects(DataObjectsType.Bird,"Hen","bird_hen"))
            add(ImagesDataObjects(DataObjectsType.Bird,"Ostrich","bird_ostrich"))
            add(ImagesDataObjects(DataObjectsType.Bird,"Owl","bird_owl"))
            add(ImagesDataObjects(DataObjectsType.Bird,"Parrot","bird_parrot"))
            add(ImagesDataObjects(DataObjectsType.Bird,"Quail","bird_quail"))
            add(ImagesDataObjects(DataObjectsType.Bird,"Vulture","bird_vulture"))
            add(ImagesDataObjects(DataObjectsType.Bird,"Wood Pecker","bird_wood_pecker"))

            add(ImagesDataObjects(DataObjectsType.Fruit,"Apple","fruit_apple"))
            add(ImagesDataObjects(DataObjectsType.Fruit,"Banana","fruit_banana"))
            add(ImagesDataObjects(DataObjectsType.Fruit,"Grapes","fruit_grapes"))
            add(ImagesDataObjects(DataObjectsType.Fruit,"Kiwi","fruit_kiwi"))
            add(ImagesDataObjects(DataObjectsType.Fruit,"Mango","fruit_mango"))
            add(ImagesDataObjects(DataObjectsType.Fruit,"Orange","fruit_orange"))
            add(ImagesDataObjects(DataObjectsType.Fruit,"Pear","fruit_pear"))
            add(ImagesDataObjects(DataObjectsType.Fruit,"Pineapple","fruit_pineapple"))
            add(ImagesDataObjects(DataObjectsType.Fruit,"Watermelon","fruit_watermelon"))

            add(ImagesDataObjects(DataObjectsType.SeaAnimal,"Dolphin","sea_animal_dolphin"))
            add(ImagesDataObjects(DataObjectsType.SeaAnimal,"Fish","sea_animal_fish"))
            add(ImagesDataObjects(DataObjectsType.SeaAnimal,"Jelly Fish","sea_animal_jellyfish"))
            add(ImagesDataObjects(DataObjectsType.SeaAnimal,"Octopus","sea_animal_octopus"))
            add(ImagesDataObjects(DataObjectsType.SeaAnimal,"Penguin","sea_animal_penguin"))

            add(ImagesDataObjects(DataObjectsType.Shape,"Circle","shape_circle"))
            add(ImagesDataObjects(DataObjectsType.Shape,"Cube","shape_cube"))
            add(ImagesDataObjects(DataObjectsType.Shape,"Diamond","shape_diamond"))
            add(ImagesDataObjects(DataObjectsType.Shape,"Heart","shape_heart"))
            add(ImagesDataObjects(DataObjectsType.Shape,"Square","shape_square"))
            add(ImagesDataObjects(DataObjectsType.Shape,"Star","shape_star"))
            add(ImagesDataObjects(DataObjectsType.Shape,"Triangle","shape_triangle"))

            add(ImagesDataObjects(DataObjectsType.Other,"Boy","other_doctor"))
            add(ImagesDataObjects(DataObjectsType.Other,"Christmas Tree","other_christmas_tree"))
            add(ImagesDataObjects(DataObjectsType.Other,"Doctor","other_doctor"))
            add(ImagesDataObjects(DataObjectsType.Other,"Ear","other_ear"))
            add(ImagesDataObjects(DataObjectsType.Other,"Earth","other_earth"))
            add(ImagesDataObjects(DataObjectsType.Other,"Fairy","other_fairy"))
            add(ImagesDataObjects(DataObjectsType.Other,"Girl","other_girl"))
            add(ImagesDataObjects(DataObjectsType.Other,"Joker","other_joker"))
            add(ImagesDataObjects(DataObjectsType.Other,"King","other_king"))
            add(ImagesDataObjects(DataObjectsType.Other,"Leaf","other_leaf"))
            add(ImagesDataObjects(DataObjectsType.Other,"Lotus","other_lotus"))
            add(ImagesDataObjects(DataObjectsType.Other,"Moon","other_moon"))
            add(ImagesDataObjects(DataObjectsType.Other,"Nose","other_nose"))
            add(ImagesDataObjects(DataObjectsType.Other,"Nurse","other_nurse"))
            add(ImagesDataObjects(DataObjectsType.Other,"Plant","other_plant"))
            add(ImagesDataObjects(DataObjectsType.Other,"Queen","other_queen"))
            add(ImagesDataObjects(DataObjectsType.Other,"Rainbow","other_rainbow"))
            add(ImagesDataObjects(DataObjectsType.Other,"Rose","other_rose"))
            add(ImagesDataObjects(DataObjectsType.Other,"Sun","other_sun"))
            add(ImagesDataObjects(DataObjectsType.Other,"Tree","other_tree"))
            add(ImagesDataObjects(DataObjectsType.Other,"Volcano","other_volcano"))
        }
        return listDataObjects
    }
    fun generateBegginerExamPaper(context: Context, examLevel : String) : List<BeginnerExamPaper>{
        val examList: MutableList<BeginnerExamPaper> = arrayListOf()
        var listDataObjects = getDataObjectsList()
        val pref = AppPreferencesHelper(context, AppConstants.PREF_NAME)
        // exam completed count level wise
        val previousTotalExamCount = pref.getCustomParamInt(AppConstants.Extras_Comman.examGivenCount + examLevel,0)
        val listCounter: MutableList<Int> = arrayListOf()
        var totalQuestions = 10
        var countQuestions = 6
        var endNumber = 6
        val begginerExamLevel1 = 3
        val begginerExamLevel2 = 6
        val begginerExamLevel3 = 10
        val begginerExamLevel4 = 15

        if (previousTotalExamCount < begginerExamLevel1){ // 1st 3 exam
            endNumber = 6
            countQuestions = 6
        }else if (previousTotalExamCount < begginerExamLevel2){ // next 4,5,6 exam
            endNumber = 8
            countQuestions = 6
        }else if (previousTotalExamCount < begginerExamLevel3){ // next 7,8,9,10 exam
            totalQuestions = 15
            endNumber = 10
            countQuestions = 8
        }else if (previousTotalExamCount < begginerExamLevel4){ // next 11 to 15 exam
            totalQuestions = 15
            endNumber = 10
            countQuestions = 7
        }else{
            totalQuestions = 20
            endNumber = 15
            countQuestions = 6
        }


        listCounter.clear()
        for (i in 1..endNumber){ // end number = 5 then counter que generate from 1 to 5
            listCounter.add(i)
        }
        for (i in 1..countQuestions){
            listCounter.shuffle()
            listCounter.shuffle()
            if (listDataObjects.isEmpty()){
                listDataObjects = getDataObjectsList()
            }
            examList.add(BeginnerExamPaper(BeginnerExamQuestionType.Count,listCounter.first().toString(),"",listDataObjects.first()))
            listDataObjects.removeAt(0)
        }

        // additions questions
        if (previousTotalExamCount < begginerExamLevel1){ // 1st 3 exam
            endNumber = 3 // additions end number
            countQuestions = (totalQuestions - 1) - examList.size // addition question counts, 1 questions for subtraction
        }else if (previousTotalExamCount < begginerExamLevel2){ // next 4,5,6 exam
            endNumber = 4
            countQuestions = (totalQuestions - 1) - examList.size
        }else if (previousTotalExamCount < begginerExamLevel3){ // next 7,8,9,10 exam
            endNumber = 5
            countQuestions = (totalQuestions - 2) - examList.size
        }else if (previousTotalExamCount < begginerExamLevel4){ // next 11 to 15 exam
            endNumber = 9
            countQuestions = (totalQuestions - 3) - examList.size
        }else{
            endNumber = 10
            countQuestions = (totalQuestions - examList.size)/2
        }

        for (i in 1..countQuestions){
            listCounter.clear()
            for (j in 1..endNumber){
                listCounter.add(j)
            }
            listCounter.shuffle()
            listCounter.shuffle()
            val number1 = listCounter.first()
            listCounter.clear()
            if (previousTotalExamCount in begginerExamLevel3 until begginerExamLevel4){
                if (number1 > 6){
                    val tempEndNumber = 10 - number1
                    for (k in 1..tempEndNumber){
                        listCounter.add(k)
                    }
                }else{
                    for (k in 1..endNumber){
                        listCounter.add(k)
                    }
                }
            }else{
                for (k in 1..endNumber){
                    listCounter.add(k)
                }
            }

            listCounter.shuffle()
            listCounter.shuffle()
            val number2 = listCounter.first()

            if (listDataObjects.isEmpty()){
                listDataObjects = getDataObjectsList()
            }
            examList.add(BeginnerExamPaper(BeginnerExamQuestionType.Additions,number1.toString(),number2.toString(),listDataObjects.first()))
            listDataObjects.removeAt(0)
        }
        // subtraction questions
        if (previousTotalExamCount < begginerExamLevel1){ // 1st 3 exam
            endNumber = 3 // subtraction end number
            countQuestions = totalQuestions - examList.size // subtraction question counts
        }else if (previousTotalExamCount < begginerExamLevel2){ // next 4,5,6 exam
            endNumber = 5
            countQuestions = totalQuestions - examList.size
        }else if (previousTotalExamCount < begginerExamLevel3){ // next 7,8,9,10 exam
            endNumber = 7
            countQuestions = totalQuestions - examList.size
        }else if (previousTotalExamCount < begginerExamLevel4){ // next 11 to 15 exam
            endNumber = 7
            countQuestions = totalQuestions - examList.size
        }else{
            endNumber = 10
            countQuestions = totalQuestions - examList.size
        }
        for (i in 1..countQuestions){
            listCounter.clear()
            for (j in 1..endNumber){
                listCounter.add(j)
            }
            listCounter.shuffle()
            listCounter.shuffle()
            val number1 = listCounter.first()
            listCounter.clear()
            for (k in 1..number1){
                listCounter.add(k)
            }
            listCounter.shuffle()
            listCounter.shuffle()
            val number2 = listCounter.first()

            if (listDataObjects.isEmpty()){
                listDataObjects = getDataObjectsList()
            }
            examList.add(BeginnerExamPaper(BeginnerExamQuestionType.Subtractions,number1.toString(),number2.toString(),listDataObjects.first()))
            listDataObjects.removeAt(0)
        }


        if (previousTotalExamCount > 6){
            (examList as ArrayList<BeginnerExamPaper>).shuffle()
        }
        examList.map {
            val index = generateIndex(3)
            if (index == 1){
                it.isAbacusQuestion = true
            }
        }
        return examList
    }
    fun getColorList(): MutableList<Int>{
        val listColor: MutableList<Int> = arrayListOf()
        with(listColor) {
            add(Color.parseColor("#FFEBEE"))
            add(Color.parseColor("#EDE7F6"))
            add(Color.parseColor("#E0F2F1"))
            add(Color.parseColor("#FFF8E1"))
            add(Color.parseColor("#F9FBE7"))
            add(Color.parseColor("#F3E5F5"))
            add(Color.parseColor("#EFEBE9"))
            add(Color.parseColor("#E0F7FA"))
            add(Color.parseColor("#FFF3E0"))
            add(Color.parseColor("#FBE9E7"))
            add(Color.parseColor("#E8F5E9"))
            add(Color.parseColor("#E3F2FD"))
            add(Color.parseColor("#FCE4EC"))
            add(Color.parseColor("#E8EAF6"))
            add(Color.parseColor("#ECEFF1"))
        }
        return listColor
    }

    val listColorRandom: MutableList<Int> = arrayListOf()
    fun getRandomColor() : Int{
        if (listColorRandom.isEmpty()){
            listColorRandom.addAll(getColorList())
        }
        var color = Color.parseColor("#FFEBEE")
        if (listColorRandom.isNotEmpty()){
//            listColorRandom.shuffle()
            color = listColorRandom.first()
            listColorRandom.removeAt(0)
        }
        return color
    }
    // TODO SingleDigit Pages
    fun getSingleDigitPages(context: Context): MutableList<CategoryPages>{
        val listCategory: MutableList<CategoryPages> = arrayListOf()
        listCategory.add(CategoryPages(category_name = context.getString(R.string.number_1_50),
            pages = listOf(Pages("1", from = 1, to = 10),
                Pages("2",from = 11, to = 20),
                Pages("3",from = 21, to = 30),
                Pages("4",from = 31, to = 40),
                Pages("5",from = 41, to = 50),
                Pages("6",from = 1, to = 50, type_random = true, page_name = context.getString(R.string.random))
            )))
        listCategory.add(CategoryPages(category_name = context.getString(R.string.number_51_100),
            pages = listOf(Pages("7",from = 51, to = 60),
                Pages("8",from = 61, to = 70),
                Pages("9",from = 71, to = 80),
                Pages("10",from = 81,to = 90),
                Pages("11",from = 91, to = 100),
                Pages("12",from = 51, to = 100, type_random = true, page_name = context.getString(R.string.random))
            )))
        listCategory.add(CategoryPages(category_name = context.getString(R.string.number_101_200),
            pages = listOf(Pages("14",from = 101, to = 150),
                Pages("15",from = 151, to = 200),
                Pages("37",from = 101, to = 200, type_random = true,page_name = context.getString(R.string.random)) // new
            )))
        listCategory.add(CategoryPages(category_name = context.getString(R.string.random_numbers),
            pages = listOf(Pages("13",from = 1, to = 100, type_random = true),
                Pages("38",from = 101, to = 200, type_random = true), // new
                Pages("16",from = 1, to = 200, type_random = true)
            )))
        listCategory.add(CategoryPages(category_name = context.getString(R.string.number_201_300),
            pages = listOf(Pages("17",from = 201, to = 250),
                Pages("18",from = 251, to = 300),
                Pages("39",from = 201, to = 300, type_random = true,page_name = context.getString(R.string.random)), // new
                Pages("19",from = 101, to = 300, type_random = true,page_name = context.getString(R.string.random))
            )))
        listCategory.add(CategoryPages(category_name = context.getString(R.string.number_301_500),
            pages = listOf(Pages("20",from = 301, to = 350),
                Pages("21",from = 351, to = 400),
                Pages("22",from = 401, to = 450),
                Pages("23",from = 451, to = 500)
            )))
        listCategory.add(CategoryPages(category_name = context.getString(R.string.random_numbers),
            pages = listOf(Pages("24",from = 301, to = 500, type_random = true),
                Pages("25",from = 101, to = 500, type_random = true),
                Pages("26",from = 1, to = 500, type_random = true)
            )))
        listCategory.add(CategoryPages(category_name = context.getString(R.string.number_501_1000),
            pages = listOf(Pages("27",from = 501, to = 600),
                Pages("28",from = 601, to = 700),
                Pages("29",from = 701, to = 800),
                Pages("30",from = 501, to = 800, type_random = true, page_name = context.getString(R.string.random)),
                Pages("31",from = 801, to = 900),
                Pages("32",from = 901, to = 1000)
            )))
        listCategory.add(CategoryPages(category_name = context.getString(R.string.random_numbers),
            pages = listOf(Pages("33",from = 501, to = 700, type_random = true),
                Pages("34",from = 701, to = 900, type_random = true),
                Pages("40",from = 801, to = 1000, type_random = true),
                Pages("35",from = 501, to = 1000, type_random = true),
                Pages("41",from = 301, to = 800, type_random = true),
                Pages("36",from = 1, to = 1000, type_random = true)
            )))
        return listCategory
    }
    // TODO Multiplication Pages
    fun getMultiplicationPages(context: Context): MutableList<CategoryPages>{
        val listCategory: MutableList<CategoryPages> = arrayListOf()
        listCategory.add(CategoryPages(category_name = context.getString(R.string.multiplications_2d_1d),
            pages = listOf(Pages(page_name = context.getString(R.string.Multiplicationpage1), que2_str = "2", que2_type = "", que1_digit_type = 2, page_id = "1"),
                Pages(page_name = context.getString(R.string.Multiplicationpage2), que2_str = "3", que2_type = "", que1_digit_type = 2,page_id = "2"),
                Pages(page_name = context.getString(R.string.Multiplicationpage3), que2_str = "4", que2_type = "", que1_digit_type = 2,page_id = "3"),
                Pages(page_name = context.getString(R.string.Multiplicationpage4), que2_str = "", que2_type = "234", que1_digit_type = 2,page_id = "4"),
                Pages(page_name = context.getString(R.string.Multiplicationpage5), que2_str = "5", que2_type = "", que1_digit_type = 2,page_id = "5"),
                Pages(page_name = context.getString(R.string.Multiplicationpage6), que2_str = "6", que2_type = "", que1_digit_type = 2,page_id = "6"),
                Pages(page_name = context.getString(R.string.Multiplicationpage7), que2_str = "7", que2_type = "", que1_digit_type = 2,page_id = "7"),
                Pages(page_name = context.getString(R.string.Multiplicationpage8), que2_str = "", que2_type = "567", que1_digit_type = 2,page_id = "8"),
                Pages(page_name = context.getString(R.string.Multiplicationpage9), que2_str = "8", que2_type = "", que1_digit_type = 2,page_id = "9"),
                Pages(page_name = context.getString(R.string.Multiplicationpage10), que2_str = "9", que2_type = "", que1_digit_type = 2,page_id = "10"),
                Pages(page_name = context.getString(R.string.Multiplicationpage11),que2_str =  "", que2_type = "89", que1_digit_type = 2,page_id = "11"),
                Pages(page_name = context.getString(R.string.Multiplicationpage12), que2_str = "", que2_type = "1..9", que1_digit_type = 2,page_id = "12")
            )))

        listCategory.add(CategoryPages(category_name = context.getString(R.string.multiplications_3d_1d),
            pages = listOf(Pages(page_name = context.getString(R.string.Multiplicationpage13), que2_str = "2", que2_type = "", que1_digit_type = 3, page_id = "13"),
                Pages(page_name = context.getString(R.string.Multiplicationpage14), que2_str = "3", que2_type = "", que1_digit_type = 3,page_id = "14"),
                Pages(page_name = context.getString(R.string.Multiplicationpage15), que2_str = "4", que2_type = "", que1_digit_type = 3,page_id = "15"),
                Pages(page_name = context.getString(R.string.Multiplicationpage16), que2_str = "", que2_type = "234", que1_digit_type = 3,page_id = "16"),
                Pages(page_name = context.getString(R.string.Multiplicationpage17), que2_str = "5", que2_type = "", que1_digit_type = 3,page_id = "17"),
                Pages(page_name = context.getString(R.string.Multiplicationpage18), que2_str = "6", que2_type = "", que1_digit_type = 3,page_id = "18"),
                Pages(page_name = context.getString(R.string.Multiplicationpage19), que2_str = "7", que2_type = "", que1_digit_type = 3,page_id = "19"),
                Pages(page_name = context.getString(R.string.Multiplicationpage20), que2_str = "", que2_type = "567", que1_digit_type = 3,page_id = "20"),
                Pages(page_name = context.getString(R.string.Multiplicationpage21), que2_str = "8", que2_type = "", que1_digit_type = 3,page_id = "21"),
                Pages(page_name = context.getString(R.string.Multiplicationpage22), que2_str = "9", que2_type = "", que1_digit_type = 3,page_id = "22"),
                Pages(page_name = context.getString(R.string.Multiplicationpage23), que2_str = "", que2_type = "89", que1_digit_type = 3,page_id = "23"),
                Pages(page_name = context.getString(R.string.Multiplicationpage24), que2_str = "", que2_type = "1..9", que1_digit_type = 3,page_id = "24")
            )))

        listCategory.add(CategoryPages(category_name = context.getString(R.string.multiplications_2d_2d),
            pages = listOf(Pages(page_name = context.getString(R.string.Multiplicationpage25), que2_str = "", que2_type = "02", que1_digit_type = 2,page_id = "25"),
                Pages(page_name = context.getString(R.string.Multiplicationpage26), que2_str = "", que2_type = "03", que1_digit_type = 2,page_id = "26"),
                Pages(page_name = context.getString(R.string.Multiplicationpage27), que2_str = "", que2_type = "04", que1_digit_type = 2,page_id = "27"),
                Pages(page_name = context.getString(R.string.Multiplicationpage28), que2_str = "", que2_type = "05", que1_digit_type = 2,page_id = "28"),
                Pages(page_name = context.getString(R.string.Multiplicationpage29), que2_str = "", que2_type = "06", que1_digit_type = 2,page_id = "29"),
                Pages(page_name = context.getString(R.string.Multiplicationpage30), que2_str = "", que2_type = "07", que1_digit_type = 2,page_id = "30"),
                Pages(page_name = context.getString(R.string.Multiplicationpage31), que2_str = "", que2_type = "08", que1_digit_type = 2,page_id = "31"),
                Pages(page_name = context.getString(R.string.Multiplicationpage32), que2_str = "", que2_type = "09", que1_digit_type = 2,page_id = "32"),
                Pages(page_name = context.getString(R.string.Multiplicationpage33), que2_str = "ran2", que2_type = "", que1_digit_type = 2,page_id = "33")
            )))

        listCategory.add(CategoryPages(category_name = context.getString(R.string.multiplications_3d_2d),
            pages = listOf(Pages(page_name = context.getString(R.string.Multiplicationpage34), que2_str = "", que2_type = "02", que1_digit_type = 3,page_id = "34"),
                Pages(page_name = context.getString(R.string.Multiplicationpage35), que2_str = "", que2_type = "03", que1_digit_type = 3,page_id = "35"),
                Pages(page_name = context.getString(R.string.Multiplicationpage36), que2_str = "", que2_type = "04", que1_digit_type = 3,page_id = "36"),
                Pages(page_name = context.getString(R.string.Multiplicationpage37), que2_str = "", que2_type = "05", que1_digit_type = 3,page_id = "37"),
                Pages(page_name = context.getString(R.string.Multiplicationpage38), que2_str = "", que2_type = "06", que1_digit_type = 3,page_id = "38"),
                Pages(page_name = context.getString(R.string.Multiplicationpage39), que2_str = "", que2_type = "07", que1_digit_type = 3,page_id = "39"),
                Pages(page_name = context.getString(R.string.Multiplicationpage40), que2_str = "", que2_type = "08", que1_digit_type = 3,page_id = "40"),
                Pages(page_name = context.getString(R.string.Multiplicationpage41), que2_str = "", que2_type = "09", que1_digit_type = 3,page_id = "41"),
                Pages(page_name = context.getString(R.string.Multiplicationpage42), que2_str = "ran2", que2_type = "", que1_digit_type = 3,page_id = "42")
            )))

        return listCategory
    }
    // TODO Division Pages
    fun getDivisionPages(context: Context): MutableList<CategoryPages>{
        val listCategory: MutableList<CategoryPages> = arrayListOf()
        listCategory.add(CategoryPages(category_name = context.getString(R.string.divide_by_single_digit),
            pages = listOf(Pages(page_name = context.getString(R.string.Devidepage1), que2_str = "2", que2_type = "", page_id = "1"),
                Pages(page_name = context.getString(R.string.Devidepage2), que2_str = "3", que2_type = "",page_id = "2"),
                Pages(page_name = context.getString(R.string.Devidepage3), que2_str = "4", que2_type = "",page_id = "3"),
                Pages(page_name = context.getString(R.string.Devidepage4), que2_str = "", que2_type = "234",page_id = "4"),
                Pages(page_name = context.getString(R.string.Devidepage5), que2_str = "5", que2_type = "",page_id = "5"),
                Pages(page_name = context.getString(R.string.Devidepage6), que2_str = "6", que2_type = "",page_id = "6"),
                Pages(page_name = context.getString(R.string.Devidepage7), que2_str = "7", que2_type = "",page_id = "7"),
                Pages(page_name = context.getString(R.string.Devidepage8), que2_str = "", que2_type = "567",page_id = "8"),
                Pages(page_name = context.getString(R.string.Devidepage9), que2_str = "8", que2_type = "",page_id = "9"),
                Pages(page_name = context.getString(R.string.Devidepage10), que2_str = "9", que2_type = "",page_id = "10"),
                Pages(page_name = context.getString(R.string.Devidepage11), que2_str = "", que2_type = "89",page_id = "11"),
                Pages(page_name = context.getString(R.string.Devidepage12), que2_str = "", que2_type = "1..9",page_id = "12")
            )))

        listCategory.add(CategoryPages(category_name = context.getString(R.string.divide_by_two_digit),
            pages = listOf(
                Pages(page_name = context.getString(R.string.Devidepage22), que2_str = "", que2_type = "00",page_id = "22"),
                Pages(page_name = context.getString(R.string.Devidepage23), que2_str = "", que2_type = "01",page_id = "23"),
                Pages(page_name = context.getString(R.string.Devidepage13), que2_str = "", que2_type = "02",page_id = "13"),
                Pages(page_name = context.getString(R.string.Devidepage14), que2_str = "", que2_type = "03",page_id = "14"),
                Pages(page_name = context.getString(R.string.Devidepage15), que2_str = "", que2_type = "04",page_id = "15"),
                Pages(page_name = context.getString(R.string.Devidepage16), que2_str = "", que2_type = "05",page_id = "16"),
                Pages(page_name = context.getString(R.string.Devidepage17), que2_str = "", que2_type = "06",page_id = "17"),
                Pages(page_name = context.getString(R.string.Devidepage18), que2_str = "", que2_type = "07",page_id = "18"),
                Pages(page_name = context.getString(R.string.Devidepage19), que2_str = "", que2_type = "08",page_id = "19"),
                Pages(page_name = context.getString(R.string.Devidepage20), que2_str = "", que2_type = "09",page_id = "20"),
                Pages(page_name = context.getString(R.string.Devidepage21), que2_str = "ran2", que2_type = "",page_id = "21"),
                Pages(page_name = context.getString(R.string.Devidepage21), que2_str = "ran2_1", que2_type = "",page_id = "24"),
            )))

        return listCategory
    }

    fun generateSingleDigit(min: Int, max: Int): Int {// min = to
        return  Random().nextInt(max - min + 1) + min
    }

    fun generateIndex(endNumber : Int = 2): Int {
        return nextInt(0, endNumber)
    }

    private fun generateTotalMinusSign(): Int {
        return nextInt(1, 5)
    }
    fun generateDivisionExercise(child: ExerciseLevelDetail) : MutableList<ExerciseList>{
        return generateDivision(child)
    }
    fun generateMultiplicationExercise(child: ExerciseLevelDetail) : MutableList<ExerciseList>{
        var listExercise: MutableList<ExerciseList> = arrayListOf()
        if (child.digits == 3 && child.totalQue == 5){
            listExercise.addAll(generateMulDigit3Que5(child))
        }else{
            listExercise = generateMultiplication3(child)
        }
        return listExercise
    }

    private fun generateDivision(child: ExerciseLevelDetail): MutableList<ExerciseList>{
        val listExercise: MutableList<ExerciseList> = arrayListOf()
        (0 until child.totalQue).forEach { j ->
            val que1 = if (child.digits == 4){
                if (j < 2){
                    generateSingleDigit(2,19)
                }else{
                    generateSingleDigit(2,99)
                }
            }else if (child.digits == 5){
                if (j < 2){
                    generateSingleDigit(2,19)
                }else{
                    generateSingleDigit(2,299)
                }
            }else { // 6 digit
                if (child.totalQue > 5){
                    if (j < 5){
                        generateSingleDigit(2,299)
                    }else{
                        generateSingleDigit(300,999)
                    }
                }else{
                    if (j < 2){
                        generateSingleDigit(2,19)
                    }else{
                        generateSingleDigit(2,399)
                    }
                }

            }

            val que22 = if (child.digits == 4){
                val min = 1000 / que1
                val max : Int = 9999 / que1
                generateSingleDigit(min,max)
            }else if (child.digits == 5){
                val min = 10000 / que1
                val max : Int = 99999 / que1
                generateSingleDigit(min,max)
            }else { // 6 digit
                val min = 100000 / que1
                val max : Int = 999999 / que1
                generateSingleDigit(min,max)
            }
            val que2 : Int = que22
            var answer = 0
            var question = ""
            if (que2 > que1){
                val answerTemp = que2 * que1
                question = "${answerTemp}/$que1"
                answer = answerTemp / que1
            }else{
                val answerTemp = que1 * que2
                question = "${answerTemp}/$que2"
                answer = answerTemp / que2
            }

            listExercise.add(ExerciseList(question,answer))
        }
        listExercise.shuffle()
        return listExercise
    }

    private fun generateMultiplication3(child: ExerciseLevelDetail): MutableList<ExerciseList>{
        val listExercise: MutableList<ExerciseList> = arrayListOf()
        (0 until child.totalQue).forEach { j ->
            val que1 = if (child.digits == 3){
                generateSingleDigit(2,99)
            }else if (child.digits == 4){
                generateSingleDigit(2,999)
            }else if (child.digits == 5){
                generateSingleDigit(2,9999)
            }else if (child.digits == 6){
                generateSingleDigit(2,99999)
            }else { // 7 digit
                generateSingleDigit(2,999999)
            }

            val que22 = if (child.digits == 3){
                val min = 100 / que1
                val max : Int = 999 / que1
                generateSingleDigit(min,max)
            }else if (child.digits == 4){
                val min = 1000 / que1
                val max : Int = 9999 / que1
                generateSingleDigit(min,max)
            }else if (child.digits == 5){
                val min = 10000 / que1
                val max : Int = 99999 / que1
                generateSingleDigit(min,max)
            }else if (child.digits == 6){
                val min = 100000 / que1
                val max : Int = 999999 / que1
                generateSingleDigit(min,max)
            }else { // 7 digit
                val min = 1000000 / que1
                val max : Int = 9999999 / que1
                generateSingleDigit(min,max)
            }
            val que2 : Int = que22
            val isInvert = generateIndex()
            var answer = 0
            var question = ""
            if (isInvert == 0){
                answer = que2 * que1
                question = "${que2}x$que1"
            }else{
                answer = que1 * que2
                question = "${que1}x$que2"
            }
            listExercise.add(ExerciseList(question,answer))
        }

        return listExercise
    }

    private fun generateMulDigit3Que5(child: ExerciseLevelDetail) : MutableList<ExerciseList>{
        val listExercise: MutableList<ExerciseList> = arrayListOf()
        var isLongNotDone2 = true
        var isLongNotDone3 = true
        var isLongNotDone4 = true
        var isLongNotDone5 = true
        var isLongNotDone6 = true
        var isLongNotDone7 = true
        var isLongNotDone8 = true
        var isLongNotDone9 = true
        for (j in 0 until child.totalQue){
            val que1 = generateSingleDigit(2,9)
            val que2 = if (que1 == 2){
                val index = generateIndex(3)
                if (isLongNotDone2 && index == 0){
                    isLongNotDone2 = false
                    generateSingleDigit(100,499)
                }else{
                    generateSingleDigit(50,99)
                }
            }else if (que1 == 3){
                val index = generateIndex(3)
                if (isLongNotDone3 && index == 0){
                    isLongNotDone3 = false
                    generateSingleDigit(100,333)
                }else{
                    generateSingleDigit(34,99)
                }
            }else if (que1 == 4){
                val index = generateIndex(3)
                if (isLongNotDone4 && index == 0){
                    isLongNotDone4 = false
                    generateSingleDigit(100,249)
                }else{
                    generateSingleDigit(25,99)
                }
            }else if (que1 == 5){
                val index = generateIndex(3)
                if (isLongNotDone5 && index == 0){
                    isLongNotDone5 = false
                    generateSingleDigit(100,199)
                }else{
                    generateSingleDigit(20,99)
                }
            }else if (que1 == 6){
                val index = generateIndex(3)
                if (isLongNotDone6 && index == 0){
                    isLongNotDone6 = false
                    generateSingleDigit(100,166)
                }else{
                    generateSingleDigit(17,99)
                }
            }else if (que1 == 7){
                val index = generateIndex(3)
                if (isLongNotDone7 && index == 0){
                    isLongNotDone7 = false
                    generateSingleDigit(100,142)
                }else{
                    generateSingleDigit(15,99)
                }
            }else if (que1 == 8){
                val index = generateIndex(3)
                if (isLongNotDone8 && index == 0){
                    isLongNotDone8 = false
                    generateSingleDigit(100,124)
                }else{
                    generateSingleDigit(13,99)
                }
            }else { // if (que1 == 9)
                val index = generateIndex(3)
                if (isLongNotDone9 && index == 0){
                    isLongNotDone9 = false
                    generateSingleDigit(100,111)
                }else{
                    generateSingleDigit(12,99)
                }
            }

            val isInvert = generateIndex()
            var answer = 0
            var question = ""
            if (isInvert == 0){
                answer = que2 * que1
                question = "${que2}x$que1"
            }else{
                answer = que1 * que2
                question = "${que1}x$que2"
            }
            listExercise.add(ExerciseList(question,answer))
        }
        return listExercise
    }

    fun generateAdditionSubExercise(child: ExerciseLevelDetail) : MutableList<ExerciseList>{
        val listExercise: MutableList<ExerciseList> = arrayListOf()
        val max = if (child.digits == 2){
            99
        }else if (child.digits == 3){
            999
        }else if (child.digits == 4){
            9999
        }else if (child.digits == 5){
            99999
        }else if (child.digits == 6){
            999999
        }else{
            9
        }
        val min = if (child.digits == 2){
            10
        }else if (child.digits == 3){
            100
        }else if (child.digits == 4){
            1000
        }else if (child.digits == 5){
            10000
        }else if (child.digits == 6){
            100000
        }else{
            1
        }

        for (j in 0 until child.totalQue){
            var maxMinusSignCount = 2
            if (child.queLines > 5){
                maxMinusSignCount = generateTotalMinusSign()
            }else{
                val index = generateIndex()
                if (index == 0){
                    maxMinusSignCount = 1
                }
            }
            var answer = 0
            var minusSignCount = 0
            var question = ""
            for (i in 0 until child.queLines){
                if (i == 0){
                    answer = generateSingleDigit(min, max)
                    question = answer.toString()
                }else{
                    if (minusSignCount == maxMinusSignCount){
                        val nextValues = generateSingleDigit(min, max)
                        question = "$question+$nextValues"
                        answer += nextValues
                    }else{
                        val index = generateIndex()
                        if (index == 0 || answer < min) { // 0 = add +
                            val nextValues = generateSingleDigit(min, max)
                            question = "$question+$nextValues"
                            answer += nextValues
                        }else{ // minus -
                            minusSignCount++
                            val nextValues = if ((answer + 1) > max){
                                nextInt(min, max)
                            }else{
                                nextInt(min, answer + 1)
                            }

                            val temp = answer - nextValues
                            if (i == (child.queLines -1) && temp == 0){
                                val nextValuesTemp = nextValues - 1
                                question = "$question-$nextValuesTemp"
                                answer -= nextValuesTemp
                            }else{
                                question = "$question-$nextValues"
                                answer -= nextValues
                            }

                        }
                    }

                }
            }
            listExercise.add(ExerciseList(question,answer))
        }
        return listExercise
    }

    fun genrateMultiplication(
        que2_str1: String,
        que2_type: String,
        que1_digit_type: Int
    ): ArrayList<HashMap<String, String>> {
        var que2_str = que2_str1
        val list_abacus = ArrayList<HashMap<String, String>>()
        var que2 = 0
        val que1 = if (que1_digit_type == 2) {
            val max = 100
            val min = 10
            Random().nextInt(max - min + 1) + min
        } else {
            val max = 1000
            val min = 100
            Random().nextInt(max - min + 1) + min
        }
        var data: HashMap<String, String> = HashMap()
        data[Constants.Que] = que1.toString()
        data[Constants.Sign] = ""
        list_abacus.add(data)
        when {
            que2_str.isEmpty() -> {
                var strArray: Array<String>? = null
                when {
                    que2_type.equals("234", ignoreCase = true) -> {
                        strArray = arrayOf("2", "3", "4")
                    }
                    que2_type.equals("567", ignoreCase = true) -> {
                        strArray = arrayOf("5", "6", "7")
                    }
                    que2_type.equals("89", ignoreCase = true) -> {
                        strArray = arrayOf("8", "9")
                    }
                    que2_type.equals("1..9", ignoreCase = true) -> {
                        strArray = arrayOf("2", "3", "4", "5", "6", "7", "8", "9")
                    }
                    que2_type.equals("02", ignoreCase = true) -> {
                        strArray = arrayOf("12", "22", "32", "42", "52", "62", "72", "82", "92")
                    }
                    que2_type.equals("03", ignoreCase = true) -> {
                        strArray = arrayOf("13", "23", "33", "43", "53", "63", "73", "83", "93")
                    }
                    que2_type.equals("04", ignoreCase = true) -> {
                        strArray = arrayOf("14", "24", "34", "44", "54", "64", "74", "84", "94")
                    }
                    que2_type.equals("05", ignoreCase = true) -> {
                        strArray = arrayOf("15", "25", "35", "45", "55", "65", "75", "85", "95")
                    }
                    que2_type.equals("06", ignoreCase = true) -> {
                        strArray = arrayOf("16", "26", "36", "46", "56", "66", "76", "86", "96")
                    }
                    que2_type.equals("07", ignoreCase = true) -> {
                        strArray = arrayOf("17", "27", "37", "47", "57", "67", "77", "87", "97")
                    }
                    que2_type.equals("08", ignoreCase = true) -> {
                        strArray = arrayOf("18", "28", "38", "48", "58", "68", "78", "88", "98")
                    }
                    que2_type.equals("09", ignoreCase = true) -> {
                        strArray = arrayOf("19", "29", "39", "49", "59", "69", "79", "89", "99")
                    }
                }
                que2_str = strArray!![Random().nextInt(strArray.size)]
                que2 = que2_str.toInt()
            }
            que2_str.equals("ran2", ignoreCase = true) -> {
                val max = 100
                val min = 10
                que2 = Random().nextInt(max - min + 1) + min

            }
            que2_str.equals("ran3", ignoreCase = true) -> {
                val max = 1000
                val min = 100
                que2 = Random().nextInt(max - min + 1) + min
            }
            else -> {
                que2 = que2_str.toInt()
            }
        }
        data = HashMap()
        data[Constants.Que] = que2.toString()
        data[Constants.Sign] = "*"
        list_abacus.add(data)
        return list_abacus
    }


    fun genrateDevide(
        que2_str1: String,
        que2_type: String,
        position: Int
    ): ArrayList<HashMap<String, String>> {
        var que2_str = que2_str1
        val list_abacus = ArrayList<HashMap<String, String>>()
        var que2: Int
        var que1 = when {
            position < 10 -> {
                val max = 111
                val min = 1
                Random().nextInt(max - min + 1) + min
            }
            position < 20 -> {
                val max = 500
                val min = 1
                Random().nextInt(max - min + 1) + min
            }
            else -> {
                val max = 1000
                val min = 1
                Random().nextInt(max - min + 1) + min
            }
        }
        when {
            que2_str.isEmpty() -> {
                var strArray: Array<String>? = null
                when {
                    que2_type.equals("234", ignoreCase = true) -> {
                        strArray = arrayOf("2", "3", "4")
                    }
                    que2_type.equals("567", ignoreCase = true) -> {
                        strArray = arrayOf("5", "6", "7")
                    }
                    que2_type.equals("89", ignoreCase = true) -> {
                        strArray = arrayOf("8", "9")
                    }
                    que2_type.equals("1..9", ignoreCase = true) -> {
                        strArray = arrayOf("2", "3", "4", "5", "6", "7", "8", "9")
                    }
                    que2_type.equals("00", ignoreCase = true) -> {
                        strArray = arrayOf("40", "10", "20", "30")
                    }
                    que2_type.equals("01", ignoreCase = true) -> {
                        strArray = arrayOf("41", "11", "21", "31")
                    }
                    que2_type.equals("02", ignoreCase = true) -> {
                        strArray = arrayOf("42", "12", "22", "32")
                    }
                    que2_type.equals("03", ignoreCase = true) -> {
                        strArray = arrayOf("43", "13", "23", "33")
                    }
                    que2_type.equals("04", ignoreCase = true) -> {
                        strArray = arrayOf("44", "14", "24", "34")
                    }
                    que2_type.equals("05", ignoreCase = true) -> {
                        strArray = arrayOf("45", "15", "25", "35")
                    }
                    que2_type.equals("06", ignoreCase = true) -> {
                        strArray = arrayOf("46", "16", "26", "36")
                    }
                    que2_type.equals("07", ignoreCase = true) -> {
                        strArray = arrayOf("47", "17", "27", "37")
                    }
                    que2_type.equals("08", ignoreCase = true) -> {
                        strArray = arrayOf("48", "18", "28", "38")
                    }
                    que2_type.equals("09", ignoreCase = true) -> {
                        strArray = arrayOf("49", "19", "29", "39")
                    }
                }
                que2_str = strArray!![Random().nextInt(strArray.size)]
                que2 = que2_str.toInt()
            }
            que2_str.equals("ran2", ignoreCase = true) -> {
                val max = 30
                val min = 10
                que2 = Random().nextInt(max - min + 1) + min
            }
            que2_str.equals("ran2_1", ignoreCase = true) -> {
                val max = 60
                val min = 10
                que2 = Random().nextInt(max - min + 1) + min
            }
            que2_str.equals("ran3", ignoreCase = true) -> {
                val max = 1000
                val min = 100
                que2 = Random().nextInt(max - min + 1) + min
            }
            else -> {
                que2 = que2_str.toInt()
            }
        }
//        que1 = 999
//        que2 = 9
        var data: HashMap<String, String> = HashMap()
        val final_que1 = que1 * que2
        data[Constants.Que] = final_que1.toString()
        data[Constants.Sign] = ""
        list_abacus.add(data)
        data = HashMap()
        data[Constants.Que] = que2.toString()
        data[Constants.Sign] = "/"
        list_abacus.add(data)
        return list_abacus
    }


    fun getTensList(context: Context) : java.util.ArrayList<String> {
        val tens : java.util.ArrayList<String> = arrayListOf()
        tens.add("")
        tens.add("")
        tens.add(context.resources.getString(R.string.Twenty))
        tens.add(context.resources.getString(R.string.Thirty))
        tens.add(context.resources.getString(R.string.Forty))
        tens.add(context.resources.getString(R.string.Fifty))
        tens.add(context.resources.getString(R.string.Sixty))
        tens.add(context.resources.getString(R.string.Seventy))
        tens.add(context.resources.getString(R.string.Eighty))
        tens.add(context.resources.getString(R.string.Ninety))
        return tens
    }
    fun getUnitsList(context: Context) : java.util.ArrayList<String> {
        val units : java.util.ArrayList<String> = arrayListOf()
        units.add("")
        units.add(context.resources.getString(R.string.One))
        units.add(context.resources.getString(R.string.Two))
        units.add(context.resources.getString(R.string.Three))
        units.add(context.resources.getString(R.string.Four))
        units.add(context.resources.getString(R.string.Five))
        units.add(context.resources.getString(R.string.Six))
        units.add(context.resources.getString(R.string.Seven))
        units.add(context.resources.getString(R.string.Eight))
        units.add(context.resources.getString(R.string.Nine))
        units.add(context.resources.getString(R.string.Ten))
        units.add(context.resources.getString(R.string.Eleven))
        units.add(context.resources.getString(R.string.Twelve))
        units.add(context.resources.getString(R.string.Thirteen))
        units.add(context.resources.getString(R.string.Fourteen))
        units.add(context.resources.getString(R.string.Fifteen))
        units.add(context.resources.getString(R.string.Sixteen))
        units.add(context.resources.getString(R.string.Seventeen))
        units.add(context.resources.getString(R.string.Eighteen))
        units.add(context.resources.getString(R.string.Nineteen))
        return units
    }

}
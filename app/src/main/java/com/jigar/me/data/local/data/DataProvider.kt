package com.jigar.me.data.local.data

import android.content.Context
import android.graphics.Color
import com.jigar.me.data.model.pages.*
import com.jigar.me.data.pref.AppPreferencesHelper
import com.jigar.me.utils.AppConstants
import com.jigar.me.utils.Constants
import com.jigar.me.utils.sudoku.SudokuConst4
import com.jigar.me.utils.sudoku.SudokuConst6
import com.jigar.me.utils.sudoku.SudukoConst
import java.util.*


object DataProvider {
    /* sudoku */
    fun getGridList() : ArrayList<String>{
        val list_grid = ArrayList<String>()
        with(list_grid) {
            add(SudukoConst.str_Grid0)
            add(SudukoConst.str_Grid1)
            add(SudukoConst.str_Grid2)
            add(SudukoConst.str_Grid3)
            add(SudukoConst.str_Grid4)
            add(SudukoConst.str_Grid5)
            add(SudukoConst.str_Grid6)
            add(SudukoConst.str_Grid7)
            add(SudukoConst.str_Grid8)
        }
        return list_grid
    }
    fun getColumnList() : ArrayList<String>{
        val list_column = ArrayList<String>()
        with(list_column) {
            add(SudukoConst.str_Column0)
            add(SudukoConst.str_Column1)
            add(SudukoConst.str_Column2)
            add(SudukoConst.str_Column3)
            add(SudukoConst.str_Column4)
            add(SudukoConst.str_Column5)
            add(SudukoConst.str_Column6)
            add(SudukoConst.str_Column7)
            add(SudukoConst.str_Column8)
        }
        return list_column
    }
    fun getRowList() : ArrayList<String>{
        val list_row = ArrayList<String>()
        with(list_row) {
            add(SudukoConst.str_Row0)
            add(SudukoConst.str_Row1)
            add(SudukoConst.str_Row2)
            add(SudukoConst.str_Row3)
            add(SudukoConst.str_Row4)
            add(SudukoConst.str_Row5)
            add(SudukoConst.str_Row6)
            add(SudukoConst.str_Row7)
            add(SudukoConst.str_Row8)
        }
        return list_row
    }

    fun getGridList_ForSudoku4() : ArrayList<String>{
        val list_grid = ArrayList<String>()
        with(list_grid) {
            add(SudokuConst4.str_Grid0)
            add(SudokuConst4.str_Grid1)
            add(SudokuConst4.str_Grid2)
            add(SudokuConst4.str_Grid3)
        }
        return list_grid
    }
    fun getColumnList_ForSudoku4() : ArrayList<String>{
        val list_column = ArrayList<String>()
        with(list_column) {
            add(SudokuConst4.str_Column0)
            add(SudokuConst4.str_Column1)
            add(SudokuConst4.str_Column2)
            add(SudokuConst4.str_Column3)
        }
        return list_column
    }
    fun getRowList_ForSudoku4() : ArrayList<String>{
        val list_row = ArrayList<String>()
        with(list_row) {
            add(SudokuConst4.str_Row0)
            add(SudokuConst4.str_Row1)
            add(SudokuConst4.str_Row2)
            add(SudokuConst4.str_Row3)
        }
        return list_row
    }

    fun getGridList_ForSudoku6() : ArrayList<String>{
        val list_grid = ArrayList<String>()
        with(list_grid) {
            add(SudokuConst6.str_Grid0)
            add(SudokuConst6.str_Grid1)
            add(SudokuConst6.str_Grid2)
            add(SudokuConst6.str_Grid3)
            add(SudokuConst6.str_Grid4)
            add(SudokuConst6.str_Grid5)
        }
        return list_grid
    }
    fun getColumnList_ForSudoku6() : ArrayList<String>{
        val list_column = ArrayList<String>()
        with(list_column) {
            add(SudokuConst6.str_Column0)
            add(SudokuConst6.str_Column1)
            add(SudokuConst6.str_Column2)
            add(SudokuConst6.str_Column3)
            add(SudokuConst6.str_Column4)
            add(SudokuConst6.str_Column5)
        }
        return list_column
    }
    fun getRowList_ForSudoku6() : ArrayList<String>{
        val list_row = ArrayList<String>()
        with(list_row) {
            add(SudokuConst6.str_Row0)
            add(SudokuConst6.str_Row1)
            add(SudokuConst6.str_Row2)
            add(SudokuConst6.str_Row3)
            add(SudokuConst6.str_Row4)
            add(SudokuConst6.str_Row5)
        }
        return list_row
    }
    /* end sudoku */


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
        var endNumber = 5
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
    fun getSingleDigitPages(): MutableList<SingleDigitCategory>{
        val listCategory: MutableList<SingleDigitCategory> = arrayListOf()
        listCategory.add(SingleDigitCategory("1 to 50 Numbers",
            listOf(SingleDigitPages("1",1, 10, false),
                SingleDigitPages("2",11, 20, false),
                SingleDigitPages("3",21, 30, false),
                SingleDigitPages("4",31, 40, false),
                SingleDigitPages("5",41, 50, false),
                SingleDigitPages("6",1, 50, true,"Random")
            )))
        listCategory.add(SingleDigitCategory("51 to 100 Numbers",
            listOf(SingleDigitPages("7",51, 60, false),
                SingleDigitPages("8",61, 70, false),
                SingleDigitPages("9",71, 80, false),
                SingleDigitPages("10",81, 90, false),
                SingleDigitPages("11",91, 100, false),
                SingleDigitPages("12",51, 100, true,"Random")
            )))
        listCategory.add(SingleDigitCategory("101 to 200 Numbers",
            listOf(SingleDigitPages("14",101, 150, false),
                SingleDigitPages("15",151, 200, false),
                SingleDigitPages("37",101, 200, true,"Random") // new
            )))
        listCategory.add(SingleDigitCategory("Random Numbers",
            listOf(SingleDigitPages("13",1, 100, true),
                SingleDigitPages("38",101, 200, true), // new
                SingleDigitPages("16",1, 200, true)
            )))
        listCategory.add(SingleDigitCategory("201 to 300 Numbers",
            listOf(SingleDigitPages("17",201, 250, false),
                SingleDigitPages("18",251, 300, false),
                SingleDigitPages("39",201, 300, true,"Random"), // new
                SingleDigitPages("19",101, 300, true,"Random")
            )))
        listCategory.add(SingleDigitCategory("301 to 500 Numbers",
            listOf(SingleDigitPages("20",301, 350, false),
                SingleDigitPages("21",351, 400, false),
                SingleDigitPages("22",401, 450, false),
                SingleDigitPages("23",451, 500, false)
            )))
        listCategory.add(SingleDigitCategory("Random Numbers",
            listOf(SingleDigitPages("24",301, 500, true),
                SingleDigitPages("25",101, 500, true),
                SingleDigitPages("26",1, 500, true)
            )))
        listCategory.add(SingleDigitCategory("501 to 1000 Numbers",
            listOf(SingleDigitPages("27",501, 600, false),
                SingleDigitPages("28",601, 700, false),
                SingleDigitPages("29",701, 800, false),
                SingleDigitPages("30",501, 800, true,"Random"),
                SingleDigitPages("31",801, 900, false),
                SingleDigitPages("32",901, 1000, false)
            )))
        listCategory.add(SingleDigitCategory("Random Numbers",
            listOf(SingleDigitPages("33",501, 700, true),
                SingleDigitPages("34",701, 900, true),
                SingleDigitPages("40",801, 1000, true),
                SingleDigitPages("35",501, 1000, true),
                SingleDigitPages("41",301, 800, true),
                SingleDigitPages("36",1, 1000, true)
            )))
        return listCategory
    }
    // TODO Multiplication Pages
    fun getMultiplicationPages(): MutableList<MultiplicationCategory>{
        val listCategory: MutableList<MultiplicationCategory> = arrayListOf()
        listCategory.add(MultiplicationCategory("2D x 1D multiplications",
            listOf(MultiplicationPages(Constants.Multiplicationpage1, "2", "", 2,"1"),
                MultiplicationPages(Constants.Multiplicationpage2, "3", "", 2,"2"),
                MultiplicationPages(Constants.Multiplicationpage3, "4", "", 2,"3"),
                MultiplicationPages(Constants.Multiplicationpage4, "", "234", 2,"4"),
                MultiplicationPages(Constants.Multiplicationpage5, "5", "", 2,"5"),
                MultiplicationPages(Constants.Multiplicationpage6, "6", "", 2,"6"),
                MultiplicationPages(Constants.Multiplicationpage7, "7", "", 2,"7"),
                MultiplicationPages(Constants.Multiplicationpage8, "", "567", 2,"8"),
                MultiplicationPages(Constants.Multiplicationpage9, "8", "", 2,"9"),
                MultiplicationPages(Constants.Multiplicationpage10, "9", "", 2,"10"),
                MultiplicationPages(Constants.Multiplicationpage11, "", "89", 2,"11"),
                MultiplicationPages(Constants.Multiplicationpage12, "", "1..9", 2,"12")
            )))

        listCategory.add(MultiplicationCategory("3D x 1D multiplications",
            listOf(MultiplicationPages(Constants.Multiplicationpage13, "2", "", 3,"13"),
                MultiplicationPages(Constants.Multiplicationpage14, "3", "", 3,"14"),
                MultiplicationPages(Constants.Multiplicationpage15, "4", "", 3,"15"),
                MultiplicationPages(Constants.Multiplicationpage16, "", "234", 3,"16"),
                MultiplicationPages(Constants.Multiplicationpage17, "5", "", 3,"17"),
                MultiplicationPages(Constants.Multiplicationpage18, "6", "", 3,"18"),
                MultiplicationPages(Constants.Multiplicationpage19, "7", "", 3,"19"),
                MultiplicationPages(Constants.Multiplicationpage20, "", "567", 3,"20"),
                MultiplicationPages(Constants.Multiplicationpage21, "8", "", 3,"21"),
                MultiplicationPages(Constants.Multiplicationpage22, "9", "", 3,"22"),
                MultiplicationPages(Constants.Multiplicationpage23, "", "89", 3,"23"),
                MultiplicationPages(Constants.Multiplicationpage24, "", "1..9", 3,"24")
            )))

        listCategory.add(MultiplicationCategory("2D x 2D multiplications",
            listOf(MultiplicationPages(Constants.Multiplicationpage25, "", "02", 2,"25"),
                MultiplicationPages(Constants.Multiplicationpage26, "", "03", 2,"26"),
                MultiplicationPages(Constants.Multiplicationpage27, "", "04", 2,"27"),
                MultiplicationPages(Constants.Multiplicationpage28, "", "05", 2,"28"),
                MultiplicationPages(Constants.Multiplicationpage29, "", "06", 2,"29"),
                MultiplicationPages(Constants.Multiplicationpage30, "", "07", 2,"30"),
                MultiplicationPages(Constants.Multiplicationpage31, "", "08", 2,"31"),
                MultiplicationPages(Constants.Multiplicationpage32, "", "09", 2,"32"),
                MultiplicationPages(Constants.Multiplicationpage33, "ran2", "", 2,"33")
            )))

        listCategory.add(MultiplicationCategory("3D x 2D multiplications",
            listOf(MultiplicationPages(Constants.Multiplicationpage34, "", "02", 3,"34"),
                MultiplicationPages(Constants.Multiplicationpage35, "", "03", 3,"35"),
                MultiplicationPages(Constants.Multiplicationpage36, "", "04", 3,"36"),
                MultiplicationPages(Constants.Multiplicationpage37, "", "05", 3,"37"),
                MultiplicationPages(Constants.Multiplicationpage38, "", "06", 3,"38"),
                MultiplicationPages(Constants.Multiplicationpage39, "", "07", 3,"39"),
                MultiplicationPages(Constants.Multiplicationpage40, "", "08", 3,"40"),
                MultiplicationPages(Constants.Multiplicationpage41, "", "09", 3,"41"),
                MultiplicationPages(Constants.Multiplicationpage42, "ran2", "", 3,"42")
            )))

        return listCategory
    }
    // TODO Division Pages
    fun getDivisionPages(): MutableList<DivisionCategory>{
        val listCategory: MutableList<DivisionCategory> = arrayListOf()
        listCategory.add(DivisionCategory("Divide by single digit",
            listOf(DivisionPages(Constants.Devidepage1, "2", "","1"),
                DivisionPages(Constants.Devidepage2, "3", "","2"),
                DivisionPages(Constants.Devidepage3, "4", "","3"),
                DivisionPages(Constants.Devidepage4, "", "234","4"),
                DivisionPages(Constants.Devidepage5, "5", "","5"),
                DivisionPages(Constants.Devidepage6, "6", "","6"),
                DivisionPages(Constants.Devidepage7, "7", "","7"),
                DivisionPages(Constants.Devidepage8, "", "567","8"),
                DivisionPages(Constants.Devidepage9, "8", "","9"),
                DivisionPages(Constants.Devidepage10, "9", "","10"),
                DivisionPages(Constants.Devidepage11, "", "89","11"),
                DivisionPages(Constants.Devidepage12, "", "1..9","12")
            )))

        listCategory.add(DivisionCategory("Divide by two digit",
            listOf(
                DivisionPages(Constants.Devidepage22, "", "00","22"),
                DivisionPages(Constants.Devidepage23, "", "01","23"),
                DivisionPages(Constants.Devidepage13, "", "02","13"),
                DivisionPages(Constants.Devidepage14, "", "03","14"),
                DivisionPages(Constants.Devidepage15, "", "04","15"),
                DivisionPages(Constants.Devidepage16, "", "05","16"),
                DivisionPages(Constants.Devidepage17, "", "06","17"),
                DivisionPages(Constants.Devidepage18, "", "07","18"),
                DivisionPages(Constants.Devidepage19, "", "08","19"),
                DivisionPages(Constants.Devidepage20, "", "09","20"),
                DivisionPages(Constants.Devidepage21, "ran2", "","21"),
                DivisionPages(Constants.Devidepage21, "ran2_1", "","24"),
            )))

        return listCategory
    }

    fun genrateSingleDigit(min: Int, max: Int): Int {// min = to
        return  Random().nextInt(max - min + 1) + min
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
        val que2: Int
        val que1 = when {
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
//        que2 = 39
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

}
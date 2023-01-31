package com.jigar.me.utils.sudoku

object SudukoConst {
    val SelectedBox = "SelectedBox"
    val totalSudoku = "totalSudoku"
    var Level = "level"

    var Level_4By4 = "4 By 4"
    var Level_6By6 = "6 By 6"
    var Level_9By9 = "9 By 9"
    var Level_Easy = "Easy"
    var Level_Medium = "Medium"
    var Level_Hard = "Hard"
    var Level_Very_Hard = "Very Hard"

    var Notes = "Notes"
    var Hintdate = "Hintdate"
    var RoomId = "roomId"

    var str_Grid0 = "'00','01','02','03','04','05','06','07','08'"
    var str_Grid1 = "'10','11','12','13','14','15','16','17','18'"
    var str_Grid2 = "'20','21','22','23','24','25','26','27','28'"
    var str_Grid3 = "'30','31','32','33','34','35','36','37','38'"
    var str_Grid4 = "'40','41','42','43','44','45','46','47','48'"
    var str_Grid5 = "'50','51','52','53','54','55','56','57','58'"
    var str_Grid6 = "'60','61','62','63','64','65','66','67','68'"
    var str_Grid7 = "'70','71','72','73','74','75','76','77','78'"
    var str_Grid8 = "'80','81','82','83','84','85','86','87','88'"

    var str_Row1_G2_G0 = "'20','21','22','06','07','08'"
    var str_Column1_G0_G6 = "'60','63','66','02','05','08'"
    var str_44_45 = "'44','45'"
    var str_43_45 = "'43','45'"
    var str_43_44 = "'43','44'"
    var str_C3_G1 = "'10','13','16'"
    var str_C4_G1 = "'11','14','17'"
    var str_C5_G1 = "'12','15','18'"
    var str_Row3_G5 = "'50','51','52','36','37','38'"
    var str_Row0 = "'00','01','02','10','11','12','20','21','22'"
    var str_Row1 = "'03','04','05','13','14','15','23','24','25'"
    var str_Row2 = "'06','07','08','16','17','18','26','27','28'"
    var str_Row3 = "'30','31','32','40','41','42','50','51','52'"
    var str_Row4 = "'33','34','35','43','44','45','53','54','55'"
    var str_Row5 = "'36','37','38','46','47','48','56','57','58'"
    var str_Row6 = "'60','61','62','70','71','72','80','81','82'"
    var str_Row7 = "'63','64','65','73','74','75','83','84','85'"
    var str_Row8 = "'66','67','68','76','77','78','86','87','88'"

    var str_Column0 = "'00','03','06','30','33','36','60','63','66'"
    var str_Column1 = "'01','04','07','31','34','37','61','64','67'"
    var str_Column2 = "'02','05','08','32','35','38','62','65','68'"
    var str_Column3 = "'10','13','16','40','43','46','70','73','76'"
    var str_Column4 = "'11','14','17','41','44','47','71','74','77'"
    var str_Column5 = "'12','15','18','42','45','48','72','75','78'"
    var str_Column6 = "'20','23','26','50','53','56','80','83','86'"
    var str_Column7 = "'21','24','27','51','54','57','81','84','87'"
    var str_Column8 = "'22','25','28','52','55','58','82','85','88'"

    var str_X = "'66','64','62','46','44','42','26','24','22'"

    var Room1 = "1"
    var Cell_00 = "00"
    var Cell_01 = "01"
    var Cell_02 = "02"
    var Cell_03 = "03"
    var Cell_04 = "04"
    var Cell_05 = "05"
    var Cell_06 = "06"
    var Cell_07 = "07"
    var Cell_08 = "08"

    var Cell_10 = "10"
    var Cell_11 = "11"
    var Cell_12 = "12"
    var Cell_13 = "13"
    var Cell_14 = "14"
    var Cell_15 = "15"
    var Cell_16 = "16"
    var Cell_17 = "17"
    var Cell_18 = "18"

    var Cell_20 = "20"
    var Cell_21 = "21"
    var Cell_22 = "22"
    var Cell_23 = "23"
    var Cell_24 = "24"
    var Cell_25 = "25"
    var Cell_26 = "26"
    var Cell_27 = "27"
    var Cell_28 = "28"

    var Cell_30 = "30"
    var Cell_31 = "31"
    var Cell_32 = "32"
    var Cell_33 = "33"
    var Cell_34 = "34"
    var Cell_35 = "35"
    var Cell_36 = "36"
    var Cell_37 = "37"
    var Cell_38 = "38"

    var Cell_40 = "40"
    var Cell_41 = "41"
    var Cell_42 = "42"
    var Cell_43 = "43"
    var Cell_44 = "44"
    var Cell_45 = "45"
    var Cell_46 = "46"
    var Cell_47 = "47"
    var Cell_48 = "48"

    var Cell_50 = "50"
    var Cell_51 = "51"
    var Cell_52 = "52"
    var Cell_53 = "53"
    var Cell_54 = "54"
    var Cell_55 = "55"
    var Cell_56 = "56"
    var Cell_57 = "57"
    var Cell_58 = "58"

    var Cell_60 = "60"
    var Cell_61 = "61"
    var Cell_62 = "62"
    var Cell_63 = "63"
    var Cell_64 = "64"
    var Cell_65 = "65"
    var Cell_66 = "66"
    var Cell_67 = "67"
    var Cell_68 = "68"

    var Cell_70 = "70"
    var Cell_71 = "71"
    var Cell_72 = "72"
    var Cell_73 = "73"
    var Cell_74 = "74"
    var Cell_75 = "75"
    var Cell_76 = "76"
    var Cell_77 = "77"
    var Cell_78 = "78"

    var Cell_80 = "80"
    var Cell_81 = "81"
    var Cell_82 = "82"
    var Cell_83 = "83"
    var Cell_84 = "84"
    var Cell_85 = "85"
    var Cell_86 = "86"
    var Cell_87 = "87"
    var Cell_88 = "88"
}
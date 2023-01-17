package com.jigar.me.utils

object AppConstants {
    internal const val DB_NAME = "kotlin_basic.db"
    internal const val PREF_NAME = "kotlin_basic_pref"
    internal const val YOUTUBE_URL = "https://www.youtube.com/channel/UC9MSzIbLkuzffqepgOqBLhw"

    const val BLINK_ICON_ANIMATION_DURATION: Long = 700
    const val BLINK_ICON_ANIMATION_ALPHA: Float = 0.2F

    const val WORK_MANAGER_SUDUKO_CREATE_STATUS = "intent_work_manager_create_suduko_status"
    const val WORK_MANAGER_SUDUKO_START_AGAIN = "intent_work_manager_create_again"

    const val NUMBER_PUZZLE_SAVE = "number_puzzle_save"
    const val NUMBER_PUZZLE_CURRENT_SCORE = "number_puzzle_current_score"
    const val NUMBER_PUZZLE_BEST_SCORE = "number_puzzle_best_score"
    annotation class HomeBannerTypes {
        companion object {
            var banner_rate_us = "banner_rate_us"
            var banner_share = "banner_share"
            var banner_purchase = "banner_purchase"
            var banner_offer = "banner_offer"
        }
    }

    interface apiTimeout {
        companion object {
            const val Timeout = 60L
        }
    }
    annotation class FirebaseEvents {
        companion object {
            var MaterialDownloaded = "abacus_material_download"

            var InAppPurchase = "abacus_purchase"
            var InAppPurchaseSKU = "abacus_sku"
            var InAppPurchaseOrderId = "abacus_order_id"

            var DailyExam = "abacus_daily_exam"
            var DailyExamLevel = "exam_level"

            var NumberPuzzleSequence = "number_puzzle_squence"
            var Sudoku = "sudoku"
            var SudokuLevel = "sudoku_level"
            var SudokuLevelComplete = "sudoku_level_complete"

            var deviceId = "deviceId"
        }
    }
    annotation class Purchase {
        companion object {
            var Purchase_limit = 9999
            var Purchase_limit_free = 20

            var AdsShow = "Y"
            var AdsShowCount = "AdsShowCount"
            var AdsShowNumberPuzzleStep = 20

            var Purchase_All = "Purchase_All"
            var Purchase_Ads = "Purchase_Ads"
            var Purchase_Toddler_Single_digit_level1 = "Purchase_Toddler_Single_digit_level1"
            var Purchase_Add_Sub_level2 = "Purchase_Add_Sub_level2"
            var Purchase_Mul_Div_level3 = "Purchase_Mul_Div_level3"

            var Purchase_Material_Maths = "Purchase_Material_Maths"
            var Purchase_Material_Nursery = "Purchase_Material_Nursery"

        }
    }

    annotation class rateSetting {
        companion object {
            const val shouldShowRatePopup: String = "RatePopup"
            const val PREF_KEY_IS_RATED: String = "is_rated"
            const val PREF_KEY_IS_ALREADY_RATED: String = "is_already_rated"
            const val PREF_KEY_RATE_DIALOG_COUNT: String = "rate_dialog_count"
        }
    }

    annotation class AbacusProgress {
        companion object {
            var PREF_PAGE_SUM = "pageSum"
            var CompleteAbacusPos = "CompleteAbacusPos"

            var TrackFetch = "TrackFetch"

            // firebase database field
//            var Track = "TrackJigar"
            var Track = "TrackNew"
            var Position = "Position"

            var Settings = "Settings"


            var BaseUrl = "BaseUrl"
            var Discount = "Discount"
            var Ads = "Ads"
            var reset = "reset"
            var resetImage = "resetImage"
            var Privacy_Policy_data = "Privacy_Policy_data"

            var OfferBannerValue = "offer_banner.png"
        }
    }

    annotation class Settings {
        companion object {
            const val Setting_sound = "Setting_sound"
            const val Setting_SudokuVolume = "Setting_SudokuVolume"
            const val Setting_NumberPuzzleVolume = "Setting_NumberPuzzleVolume"
            const val Setting__hint_sound = "Setting_hint_sound"
            const val Setting_display_abacus_number = "Setting_display_abacus_number"
            const val Setting_display_help_message = "Setting_display_help_message"
            const val Setting_hide_table = "Setting_hide_table"
            const val Setting_auto_reset_abacus = "Setting_auto_reset_abacus"
            const val Setting_left_hand = "Setting_left_hand"
            var Setting_answer = "Setting_answer"
            const val Setting_answer_Step = "Step"
            const val Setting_answer_Final = "Final"

            var abacus_colorful = "abacus_colorful"

            var Theam = "Theam"
            var TheamTempView = "TheamTempView"
            const val theam_Poligon = "Poligon"
            const val theam_Default = theam_Poligon
            const val theam_eyes = "Eyes"
            const val theam_Egg = "Egg"
            const val theam_shape = "Shape"
            const val theam_Star = "Star"

            var Toddler_No = "Toddler_No"
            var Toddler_No_Count = "Toddler_No_Count"
            var SW_Random = "SW_Random"
            var SW_Reset = "SW_Reset"
            var SW_Range_min = "SW_Range_min"
            var SW_Range_max = "SW_Range_max"
        }

    }

    annotation class HomeClicks {
        companion object {
            const val TYPE = "type"
            const val TYPE_EXAM_HISTORY = "type_exam_history"

            const val Menu_Tutorial = 1
            const val Menu_Addition = 2
            const val Menu_Addition_Subtraction = 3
            const val Menu_Starter = 4
            const val Menu_Number = 5
            const val Menu_AboutUs = 6
            const val Menu_Multiplication = 7
            const val Menu_Division = 8
            const val Menu_Purchase = 9
            const val Menu_DailyExam = 10
            const val Menu_PractiseMaterial = 11
            const val Menu_Setting = 12

            const val Menu_Click_Back = 13
            const val Menu_Click_ResetProgress = 14
            const val Menu_Click_Youtube = 15
            const val Menu_Click_Table = 16
            const val Menu_Share = 17

            const val Menu_Sudoku = 18
            const val Menu_Number_Puzzle = 19

        }
    }

    annotation class Extras_Comman {
        companion object {
            var FROM = "from"
            var Title = "Title"
            var AbacusType = "AbacusType"
            var AbacusTypeNumber = "Number"
            var AbacusTypeAdditionSubtraction = "AdditionSubtraction"
            var AbacusTypeMultiplication = "Multiplication"
            var AbacusTypeDivision = "Division"

            var Que2_str = "Que2_str"
            var Que2_type = "Que2_type"
            var Que1_digit_type = "Que1_digit_type"

            var From = "From"
            var To = "To"
            var isType_random = "isType_random"

            var examGivenCount = "examGivenCount"
            var examLevel = "examLevel"
            var examLevelLable = "examLevelLable"
            var examResult = "ExamResult"

            var DownloadType_Maths = "maths"
            var DownloadType_Nursery = "nursery"
            var DownloadType = "downloadType"

            var type = "type"
            var order = "order"

            var tour = "tour"

            var Level = "LevelNew" // level page store
        }
    }

    annotation class StatusCodes {
        companion object {
            var SUCCESS = 200
            var CREATED = 201
            var ACCEPTED = 202
            var NO_CONTENT = 204
            var BAD_REQUEST = 400
            var AUTHORIZATION_FAILED = 401
            var FORBIDDEN = 403
            var NOT_FOUND = 404
            var METHOD_NOT_ALLOWED = 405
            var NOT_ACCEPTED = 406
            var PROXY_AUTHENTICATION_REQUIRED = 407
            var CONFLICT = 409
            var PREECONDITION_FAILED = 412
            var UNSUPPORDER_EDIA_TYPE = 415
            var INTERNAL_SERVER_ERROR = 500
            var NOT_IMPLEMENTED = 501
            var LOCAL_ERROR = 0
        }
    }


    // TODO api param
    interface apiHeader {
        companion object {
            const val consumer_key = "consumer-key"
            const val consumer_secret = "consumer-secret"
            const val consumer_nonce = "consumer-nonce"
        }
    }

    interface apiParams {
        companion object {
            const val levelId = "level_id"
            const val pageId = "page_id"
            const val limit = "limit"
            const val total = "total"
            const val level = "level"
            const val type = "type"
        }
    }


}
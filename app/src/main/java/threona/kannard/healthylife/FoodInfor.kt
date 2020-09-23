package threona.kannard.healthylife

class FoodInfor(id: String, name: String, nutri : String, meal : String) {
        private var mId : String = id
        private var mName = name
        private var mNutri = nutri
        private var mMeal = meal

        fun getName(): String {
            return mName
        }

        fun getNutri() : String {
            return mNutri
        }

        fun getMeal() : String {
            return mMeal
        }
}
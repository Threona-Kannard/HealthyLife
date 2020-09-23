package threona.kannard.healthylife

class Food(name: String, imageUrl: String) {
    private val mName : String = name
    private val mImageUrl : String = imageUrl

    fun getName(): String {
        return mName
    }
    fun getImageUrl() : String{
        return mImageUrl
    }
}
package threona.kannard.healthylife

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.squareup.picasso.Picasso

class FoodDetail : AppCompatActivity() {
//    private val imageView : ImageView = findViewById(R.id.imageDetails)
//    private val nameDetail : TextView = findViewById(R.id.nameDetails)
//    private val nutriDetail : TextView = findViewById(R.id.nutriDetail)
//    private val mealDetail : TextView = findViewById(R.id.mealDetail)
//    private val goalDetail : TextView = findViewById(R.id.goalDetails)

    // Access a Cloud Firestore instance from your Activity
    private var db: FirebaseFirestore? = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_detail)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);

        val text = intent.getIntExtra("position", 0)
        val imageUrl : String? = intent.getStringExtra("url")
        val allFoodRef: CollectionReference? = db?.collection("Food_Details")
    }
}
package threona.kannard.healthylife

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class FoodDetail : AppCompatActivity() {

    // Access a Cloud Firestore instance from your Activity
    private var db: FirebaseFirestore? = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_detail)

        //Setup Toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);

        //Progress Circle
        val mProgressCircle : ProgressBar = findViewById(R.id.progress_circle)

        //Component on Layout
        val nameDetail : TextView = findViewById(R.id.nameDetail)
        val nutriDetail : TextView = findViewById(R.id.nutriDetail)
        val mealDetail : TextView = findViewById(R.id.mealDetail)

        val id = intent.getIntExtra("position",0)
        val imageUrl = intent.getStringExtra("url")
        val imageDetail : ImageView = findViewById(R.id.imageDetail)
        Picasso.with(this)
            .load(imageUrl)
            .fit()
            .centerCrop()
            .into(imageDetail)

        val noteRef = db?.collection("Food_Details")?.document(id.toString())
        noteRef?.get()?.addOnSuccessListener { snapshot ->
            if (snapshot.exists())
            {
                val name = snapshot.getString("name")
                nameDetail.text = name
                val nutri = snapshot.getString("nutri")
                nutriDetail.text = nutri
                val meal = snapshot.getString("meal")
                mealDetail.text = meal
                mProgressCircle.visibility = ProgressBar.INVISIBLE
            }
            else
            {
                Toast.makeText(this, "Cannot load information", Toast.LENGTH_SHORT).show()
                mProgressCircle.visibility = ProgressBar.INVISIBLE
            }
        }
    }
}
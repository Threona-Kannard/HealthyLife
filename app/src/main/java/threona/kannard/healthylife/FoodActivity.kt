package threona.kannard.healthylife

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.*
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_food.*
import kotlinx.coroutines.delay
import threona.kannard.healthylife.FoodAdapter.onItemClickListener

class FoodActivity : AppCompatActivity() {
    private lateinit var mDatabaseReference : DatabaseReference

    private lateinit var mAdapter: FoodAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mProgressCircle : ProgressBar
    private var mFoods  = arrayListOf<Food>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);

        mDatabaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://healthylife-dae75.firebaseio.com/Food")

        mProgressCircle = findViewById(R.id.progress_circle)

        mRecyclerView = findViewById<RecyclerView>(R.id.test_view)
        mRecyclerView.setHasFixedSize(true)
        val lm = LinearLayoutManager(this@FoodActivity)
        lm.orientation = LinearLayoutManager.VERTICAL
        mRecyclerView.layoutManager = lm
        mAdapter = FoodAdapter(this@FoodActivity, mFoods)


        mDatabaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (postSnapshot in snapshot.children) {
                        val food: Food? = Food(
                            postSnapshot.child("name").value.toString(),
                            postSnapshot.child("imageUrl").value.toString()
                        )
                        mFoods.add(food!!)
                    }
                    //Debugging
                    for(a : Food in mFoods) {
                        Log.d("TESTING-NAME", a.getName())
                        Log.d("TESTING-URL", a.getImageUrl())
                    }
                    mAdapter.notifyDataSetChanged()
                    mProgressCircle.visibility = ProgressBar.INVISIBLE
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@FoodActivity, error.message, Toast.LENGTH_SHORT).show()
                    mProgressCircle.visibility = ProgressBar.INVISIBLE
                }
        })
        mRecyclerView.adapter = mAdapter
        mAdapter.setOnItemClickListener(object : onItemClickListener {
            override fun onItemClick(position: Int) {
                val food = mFoods[position]
                Toast.makeText(this@FoodActivity, food.getName(), Toast.LENGTH_SHORT).show()
                val intent = Intent(this@FoodActivity, FoodDetail::class.java)
                intent.putExtra("position", position)
                intent.putExtra("url", mFoods[position].getImageUrl())
                startActivity(intent)
            }
        })
    }
}
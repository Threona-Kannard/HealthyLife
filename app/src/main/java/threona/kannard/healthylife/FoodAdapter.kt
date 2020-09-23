package threona.kannard.healthylife

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import threona.kannard.healthylife.FoodAdapter.FoodViewHolder

class FoodAdapter(context: Context, foods: List<Food>) :
    RecyclerView.Adapter<FoodViewHolder>() {

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    class FoodViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.textView_name)
        val imageDisplay : ImageView = itemView.findViewById(R.id.imageViewUrl)
        val item = itemView.setOnClickListener {
            if (listener != null)
            {
                val position : Int = adapterPosition
                if(position != RecyclerView.NO_POSITION)
                {
                    listener.onItemClick(position)
                }
            }
        }
    }
    private val mContext : Context = context
    private val mFoods : List<Food> = foods
    private lateinit var mListener : FoodAdapter.onItemClickListener

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        return FoodViewHolder(LayoutInflater.from(mContext).inflate(R.layout.food_item, parent, false),mListener)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val currentFood : Food = mFoods[position]
        holder.textViewName.text = currentFood.getName()
        Picasso.with(mContext)
            .load(currentFood.getImageUrl())
            .fit()
            .centerCrop()
            .into(holder.imageDisplay)
    }

    override fun getItemCount(): Int {
        return mFoods.size
    }
}
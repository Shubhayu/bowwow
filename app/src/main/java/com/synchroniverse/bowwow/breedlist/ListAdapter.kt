package com.synchroniverse.bowwow.breedlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.synchroniverse.bowwow.R
import com.synchroniverse.bowwow.models.DogModel

class ListAdapter(
    val listFragment: OnItemClickListener,
    dogList: List<DogModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_BREED = 0
    private val TYPE_SUBBREED = 1
    private var dataList : MutableList<DogModel> = mutableListOf()

    init {
        setData(dogList)
    }

    private fun setData(dogList: List<DogModel>) {
        if (dogList.isNotEmpty()) {
            for (dog: DogModel in dogList) {
                dataList.add(dog)
                if (dog.subBreedList.size > 0) {
                    for (sub in dog.subBreedList)
                        dataList.add(sub)
                }
            }
        }
    }

    fun updateList(dogList: List<DogModel>) {
        setData(dogList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = dataList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            TYPE_BREED -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_view_breed, parent, false)
                BreedViewHolder(view)
            }
            TYPE_SUBBREED -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_view_subbreed, parent, false)
                SubBreedViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_view_breed, parent, false)
                BreedViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(getItemViewType(position)) {
            TYPE_BREED -> {
                (holder as BreedViewHolder).bind(dataList[position], listFragment)
            }
            TYPE_SUBBREED -> {
                (holder as SubBreedViewHolder).bind(dataList[position], listFragment)
            }
            else -> {
                (holder as BreedViewHolder).bind(dataList[position], listFragment)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (dataList[position].isSubBreed)
            TYPE_SUBBREED
        else
            TYPE_BREED
    }

    class BreedViewHolder(val view : View) : RecyclerView.ViewHolder(view){
        val textView: TextView = view.findViewById(R.id.txt_breed_name)

        fun bind(item: DogModel, clickListener: OnItemClickListener) {
            textView.text = item.breedName.capitalize()
            view.setOnClickListener { clickListener.OnItemClick(item) }
        }
    }

    class SubBreedViewHolder(val view : View) : RecyclerView.ViewHolder(view){
        val textView: TextView = view.findViewById(R.id.txt_subbreed_name)

        fun bind(item: DogModel, clickListener: OnItemClickListener) {
            textView.text = item.subBreedName.toUpperCase()
            view.setOnClickListener { clickListener.OnItemClick(item) }
        }
    }

    interface OnItemClickListener {
        fun OnItemClick(item: DogModel)
    }

}
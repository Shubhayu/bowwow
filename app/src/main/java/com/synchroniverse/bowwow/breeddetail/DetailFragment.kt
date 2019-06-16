package com.synchroniverse.bowwow.breeddetail


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.synchroniverse.bowwow.R
import com.synchroniverse.bowwow.repository.DogRepository
import kotlinx.android.synthetic.main.fragment_detail.*

/**
 * A simple [Fragment] subclass.
 *
 */
class DetailFragment : Fragment() {

    private var breed: String? = null
    private var subBreed: String? = null

    private lateinit var recyclerViewList: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_detail, container, false)

        val args by navArgs<DetailFragmentArgs>()
        breed = args.Breed
        subBreed = args.SubBreed

        viewManager = LinearLayoutManager(this.context)
        viewAdapter = ImageAdapter(listOf())

        recyclerViewList = view.findViewById<RecyclerView>(R.id.recycler_view_image_list).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        val viewModel = ViewModelProviders.of(this, DetailViewModelFactory(DogRepository()))
            .get(DetailViewModel::class.java)

        viewModel.imageList.observe(this, Observer<List<String>> { images ->
            run {
                if (images != null) {
                    (viewAdapter as ImageAdapter).updateList(images)
                    recyclerViewList.visibility = View.VISIBLE
                    txt_empty_view.visibility = View.GONE
                }
            }
        })

        viewModel.error.observe(this, Observer { error ->
            run {
                if (error != null) {
                    recyclerViewList.visibility = View.GONE
                    txt_empty_view.visibility = View.VISIBLE
                }
            }
        })

        viewModel.loadingIndicator.observe(this, Observer { state -> if (state) progressBar.visibility = View.VISIBLE else progressBar.visibility = View.GONE })

        viewModel.fetchImages(breed!!, subBreed)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (subBreed.isNullOrEmpty()) {
            activity?.title = "$breed".capitalize()
        } else {
            activity?.title = "${breed?.capitalize()} - ${subBreed?.capitalize()}"
        }

    }

}

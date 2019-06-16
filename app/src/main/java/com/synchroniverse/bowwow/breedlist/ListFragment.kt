package com.synchroniverse.bowwow.breedlist


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.synchroniverse.bowwow.*
import com.synchroniverse.bowwow.models.DogModel
import com.synchroniverse.bowwow.repository.DogRepository
import kotlinx.android.synthetic.main.fragment_list.*

/**
 * A simple [Fragment] subclass.
 *
 */
class ListFragment : Fragment(), ListAdapter.OnItemClickListener {

    private lateinit var recyclerViewList: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        viewManager = LinearLayoutManager(this.context)
        viewAdapter = ListAdapter(this, listOf())

        recyclerViewList = view.findViewById<RecyclerView>(R.id.recycler_view_list).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        val viewModel = ViewModelProviders.of(this,
            ListViewModelFactory(DogRepository())
        ).get(ListViewModel::class.java)
        viewModel.breedList.observe(this, Observer { breeds ->
            run {
                if (breeds != null) {
                    (viewAdapter as ListAdapter).updateList(breeds)
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

        return view
    }

    override fun OnItemClick(item: DogModel) {
        val navigation = ListFragmentDirections.actionListFragmentToDetailFragment(
            item.breedName,
            item.subBreedName
        )
        findNavController().navigate(navigation)
    }
}

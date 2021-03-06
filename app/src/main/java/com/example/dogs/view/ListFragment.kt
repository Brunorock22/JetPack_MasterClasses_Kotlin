package com.example.dogs.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dogs.R
import com.example.dogs.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : Fragment() {

    private lateinit var viewModel: ListViewModel
    private var dogsListAdapter = DogsListAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        viewModel.refresh()

        dogsList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = dogsListAdapter
        }

        observeViewModel()

        refreshLayout.setOnRefreshListener {
            dogsList.visibility = View.GONE
            textError.visibility = View.GONE
            loadingView.visibility = View.GONE
            viewModel.refreshBypassCache()
            refreshLayout.isRefreshing = false
        }
    }

    private fun observeViewModel() {
        viewModel.dogs.observe(this, Observer { dogs ->
            dogs?.let {
                dogsList.visibility = View.VISIBLE
                dogsListAdapter.updateDogList(dogs)
            }

        })

        viewModel.dogsLoadError.observe(this, Observer {
            it?.let {
                if (it) {

                    textError.visibility = View.VISIBLE
                    textError.text = "Error while getting datas"
                } else {
                    textError.visibility = View.INVISIBLE

                }

            }
        })

        viewModel.loading.observe(this, Observer {
            it?.let {
                loadingView.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    textError.visibility = View.GONE
                    dogsList.visibility = View.GONE

                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.actionSettings -> {
                view?.let {  Navigation.findNavController(it).navigate(ListFragmentDirections.actionListFragmentToSettingsFragment())}
            }
        }
        return super.onOptionsItemSelected(item)
    }

}

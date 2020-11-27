package com.example.dogs.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.example.dogs.R
import com.example.dogs.util.getProgressDrawable
import com.example.dogs.util.loadImage
import com.example.dogs.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.item_dog.view.*


class DetailFragment : Fragment() {

    private lateinit var viewModel: DetailViewModel
    private var dogId: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)

        arguments?.let {
            dogId = DetailFragmentArgs.fromBundle(it).dogUuid
            viewModel.fetch(dogId)
        }
        observeViewModel()

    }

    private fun observeViewModel() {
        viewModel.dogLiveDate.observe(this, Observer { dog ->

            dog?.let {
                dogContainer.visibility = View.VISIBLE
                loadingViewDetail.visibility = View.GONE

                dogName.text = it.dogBreed
                dogPurpose.text = it.bredFor
                dogTemperament.text = it.temperament
                dogLifeStyle.text = it.lifeSpan
                dogImageDetails.loadImage(it.imgUrl,  getProgressDrawable(dogImageDetails.context))
            }

        })
        viewModel.progressingLiveDate.observe(this, Observer { progressing ->
            progressing?.let {
                dogContainer.visibility = View.GONE
                loadingViewDetail.visibility = View.VISIBLE
            }

        })
    }

}
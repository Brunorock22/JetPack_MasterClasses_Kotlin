package com.example.dogs.view

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.palette.graphics.Palette
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.dogs.model.DogPalette

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.dogs.R
import com.example.dogs.databinding.FragmentDetailBinding
import com.example.dogs.databinding.SendSmsDialogBinding
import com.example.dogs.model.Dog
import com.example.dogs.model.SmsInfo
import com.example.dogs.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.fragment_detail.*


class DetailFragment : Fragment() {

    private lateinit var viewModel: DetailViewModel
    private var dogId: Int = 0
    lateinit var binding: FragmentDetailBinding
    private var sendSms = false
    private var currentDog: Dog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)

        return binding.root
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
            currentDog = dog
            dog?.let { it ->
                dogContainer.visibility = View.VISIBLE
                loadingViewDetail.visibility = View.GONE
                binding.dogDataBidingDetails = it

//                dogName.text = it.dogBreed
//                dogPurpose.text = it.bredFor
//                dogTemperament.text = it.temperament
//                dogLifeStyle.text = it.lifeSpan
//                dogImageDetails.loadImage(it.imgUrl, getProgressDrawable(dogImageDetails.context))
                it.imgUrl?.let { dogImage ->
                    setupBackgroundColor(dogImage)
                }
            }

        })
        viewModel.progressingLiveDate.observe(this, Observer { progressing ->
            progressing?.let {
                dogContainer.visibility = View.GONE
                loadingViewDetail.visibility = View.VISIBLE
            }

        })
    }

    private fun setupBackgroundColor(url: String) {
        Glide.with(this).asBitmap().load(url).into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                Palette.from(resource).generate { palette ->
                    val intColor = palette?.vibrantSwatch?.rgb ?: 0
                    val myPalette = DogPalette(intColor)
                    binding.palette = myPalette

                }
            }

            override fun onLoadCleared(placeholder: Drawable?) {

            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.detail_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_send_smsm -> {
                sendSms = true
                (activity as MainActivity).checkSmsPermission()
            }

            R.id.action_share -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun onPermissionResult(permissionGranted: Boolean) {
        if (sendSms && permissionGranted) {
            context?.let {


                    val smsInfo = SmsInfo("", "${currentDog?.dogBreed}", currentDog?.imgUrl)

                    val dialogBiding = DataBindingUtil.inflate<SendSmsDialogBinding>(
                        LayoutInflater.from(it),
                        R.layout.send_sms_dialog,
                        null,
                        false
                    )

                    AlertDialog.Builder(it).setView(dialogBiding.root)
                        .setPositiveButton("Send SMS") { dialog, which ->

                            if (!dialogBiding.smsDestination.text.isNullOrBlank()) {
                                smsInfo.to = dialogBiding.smsDestination.text.toString()
                                smsSms(smsInfo)
                            }

                        }.setNegativeButton("Cancel") { dialog, which ->
                        }.show()

                    dialogBiding.smsInfo = smsInfo


            }

        }
    }

    private fun smsSms(smsInfo: SmsInfo) {


    }
}
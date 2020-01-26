package com.devtides.dogs.view


import android.app.AlertDialog.Builder
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.telephony.SmsManager
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.devtides.dogs.R
import com.devtides.dogs.databinding.FragmentDetailBinding
import com.devtides.dogs.databinding.SendSmsDialogBinding
import com.devtides.dogs.model.DogBreed
import com.devtides.dogs.model.DogPalette
import com.devtides.dogs.model.SmsInfo
import com.devtides.dogs.viewmodel.DetailViewModel

/**
 * A simple [Fragment] subclass.
 */
class DetailFragment : Fragment() {

    private var dogUuid = 0
    private var currentDog: DogBreed? = null
    private var sendSmsStarted = false
    private lateinit var viewModel: DetailViewModel
    private lateinit var dataBinding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        dataBinding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_detail, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            dogUuid = DetailFragmentArgs.fromBundle(it).dogUuid
        }
        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        viewModel.fetch(dogUuid)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.dogLiveData.observe(this, Observer { dog ->
            currentDog = dog
            dog?.let { it ->
                dataBinding.dog = dog
                it.imageUrl?.let { url -> setupBackgroundColor(url) }
            }
        })

    }

    private fun setupBackgroundColor(url: String) {
        Glide.with(this)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>(){
                override fun onLoadCleared(placeholder: Drawable?) {}

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    Palette.from(resource)
                        .generate {palette ->
                            val intColor = palette?.lightMutedSwatch?.rgb ?: 0
                            dataBinding.palette = DogPalette(intColor)
                        }
                }
            })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.detail_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_send_sms -> {
                sendSmsStarted = true
                (activity as MainActivity).checkSmsSelfPermission()
            }
            R.id.action_share -> {
                currentDog?.let {
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = "text/plain"
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Check out this dog breed")
                    intent.putExtra(Intent.EXTRA_TEXT, "${it.dogBreed} bread for ${it.bredFor}")
                    intent.putExtra(Intent.EXTRA_STREAM, it.imageUrl)
                    startActivity(Intent.createChooser(intent, "Share with"))
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun onPermissionResult(permissionGranted: Boolean) {
        if(sendSmsStarted && permissionGranted) {
            context?.let { context ->
                currentDog?.let {
                    val smsInfo = SmsInfo(
                        to = "",
                        text = "${it.dogBreed} bread for ${it.bredFor}",
                        imageUrl = "${it.imageUrl}"
                    )
                    val dialogBinding = DataBindingUtil.inflate<SendSmsDialogBinding>(
                        LayoutInflater.from(context),
                        R.layout.send_sms_dialog,
                        null,
                        false
                    )
                    Builder(context)
                        .setView(dialogBinding.root)
                        .setPositiveButton("Send SMS") { _, _ ->
                            val destination = dialogBinding.smsDestination.text.toString()
                            if(destination.isNotEmpty()) {
                                smsInfo.to = destination
                                sendSms(smsInfo)
                            }
                        }
                        .setNegativeButton("Cancel") { _, _ -> }
                        .show()
                    dialogBinding.smsInfo = smsInfo
                }
            }
        }
    }

    private fun sendSms(smsInfo: SmsInfo) {
        val intent = Intent(context, MainActivity::class.java)
        val pi = PendingIntent.getActivity(context, 0, intent, 0)
        val sms = SmsManager.getDefault()
        sms.sendTextMessage(smsInfo.to, null, smsInfo.text, pi, null)
    }
}

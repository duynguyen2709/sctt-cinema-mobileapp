package com.example.cgv.ui.history

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.StyleSpan
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.example.cgv.BaseActivity
import com.example.cgv.R
import com.example.cgv.databinding.ActivityTicketDetailBindingImpl
import com.example.cgv.ext.QRCodeHelper
import com.example.cgv.model.Ticket
import com.google.gson.Gson
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel


class DetailHistoryActivity : BaseActivity<ActivityTicketDetailBindingImpl>() {
    override val layoutId: Int = R.layout.activity_ticket_detail
    private var ticket: Ticket? = null
    /* private lateinit var viewModel : UserViewModel
     private lateinit var adapter: HistoryTransactionAdapter*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        viewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
//        viewModel.getListHistory(CoreApplication.instance.user?.email?:"")
        ticket = intent.getSerializableExtra("TICKET") as Ticket
        viewBinding.ticket = ticket
        viewBinding.activity = this
        val serializeString = Gson().toJson(ticket)
        val bitmap = QRCodeHelper
            .newInstance(this)
            .setContent(serializeString)
            .setErrorCorrectionLevel(ErrorCorrectionLevel.Q)
            .qrcOde
        viewBinding.ivQRCode.setImageBitmap(bitmap)
        createGradientText()
        viewBinding.tvRoom.text=getFormatSpannable("ROOM\n",ticket?.roomNumber)
        viewBinding.tvSeatCode.text=getFormatSpannable("SEAT\n",ticket?.seatCodes)
    }

    override fun bindView() {
        super.bindView()
    }

    private fun getFormatSpannable(
        firstWord: String,
        secondWord: String?
    ): SpannableString {
        val spannable = SpannableString(firstWord + secondWord)
        val tf = ResourcesCompat.getFont(this, R.font.montserrat_medium)
        val tf1 = ResourcesCompat.getFont(this, R.font.montserrat_bold)
        spannable.setSpan(
            tf?.style?.let {
                StyleSpan(it)
                AbsoluteSizeSpan(20)
            }, 0, firstWord.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            tf1?.style?.let {
                StyleSpan(it)
                AbsoluteSizeSpan(35)
            },
            firstWord.length,
            firstWord.length + (secondWord?.length ?: 0),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannable
    }


    private fun createGradientText() {
        val face = ResourcesCompat.getFont(this, R.font.montserrat_bold)
        viewBinding.tvMovieName.setTypeface(face)
        val paint = viewBinding.tvMovieName.getPaint()
        val width = paint.measureText(viewBinding.tvMovieName.text.toString())

        val textShader = LinearGradient(
            0f, 0f, width, viewBinding.tvMovieName.getTextSize(),
            Color.parseColor("#6799d4"),
            Color.parseColor("#4252a0"), Shader.TileMode.CLAMP
        )
        viewBinding.tvMovieName.getPaint().setShader(textShader)
    }

    private fun clickEvent(item: Ticket?, position: Int?) {

    }

    fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivBack -> {
                finish()
            }
        }
    }

    override fun bindViewModel() {
        super.bindViewModel()
//        viewModel.historyLiveData.observe(this, Observer {
//            when (it?.status) {
//                Resource.SUCCESS -> {
//                    it.data?.let { response ->
//                        viewBinding.swipeContainer.isRefreshing=false
//                        adapter.addData(ArrayList(response))
//                    }
//                }
//                Resource.LOADING -> {
//
//                }
//                Resource.ERROR -> {
//                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
//                }
//            }
//        })
    }

}
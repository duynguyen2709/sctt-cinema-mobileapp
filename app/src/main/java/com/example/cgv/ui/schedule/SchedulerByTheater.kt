package com.example.cgv.ui.schedule

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.cgv.R
import com.example.cgv.model.Resource
import com.example.cgv.model.Theater
import com.example.cgv.ui.ticket.TicketActivity
import kotlinx.android.synthetic.main.activity_ticketbytheater.*
import kotlinx.android.synthetic.main.list_item.view.*
import java.io.Serializable

class TicketByTheater : AppCompatActivity() {
    private lateinit var listAdapter: ExpandableListAdapter

    private lateinit var viewModel: SchedulerByTheaterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticketbytheater)
        viewModel = ViewModelProviders.of(this).get(SchedulerByTheaterViewModel::class.java)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        listAdapter = ExpandableListAdapter(this)
        listAdapter.setClickListener(object : ExpandableListAdapter.ClickListener {
            override fun onClick(item: Theater) {
                val intent = Intent(this@TicketByTheater, TicketActivity::class.java)
                intent.putExtra("item", item as Serializable)
                startActivity(intent)
            }

        })
        viewModel.listTheater.observe(
            this,
            Observer<Resource<Map<String, List<Theater>>>> { t ->
                when (t.status) {
                    Resource.LOADING -> {

                    }
                    Resource.SUCCESS -> {
                        listAdapter.setListDataHeader(t.data?.keys?.toList())
                        listAdapter.setListDataChild(t.data)
                    }
                    Resource.ERROR -> {

                    }
                }
            })
        lvTheater.setAdapter(listAdapter)

    }
}

class ExpandableListAdapter(
    private val _context: Context
) : BaseExpandableListAdapter() {
    private val _listDataHeader = mutableListOf<String>() // header titles
    // child data in format of header title, child title
    private val _listDataChild = HashMap<String, List<Theater>>()

    private var clickListener: ClickListener? = null

    override fun getChild(groupPosition: Int, childPosititon: Int): Theater {
        return this._listDataChild[this._listDataHeader[groupPosition]]!![childPosititon]
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getChildView(
        groupPosition: Int, childPosition: Int,
        isLastChild: Boolean, convertView: View?, parent: ViewGroup
    ): View {
        var convertView = convertView

        val childText = getChild(groupPosition, childPosition)

        if (convertView == null) {
            val infalInflater = this._context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = infalInflater.inflate(R.layout.list_item, null)
            convertView!!.setOnClickListener {
                clickListener?.onClick(childText)
            }
        }

        convertView.lblListItem.text = childText.theaterName
        return convertView
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return this._listDataChild[this._listDataHeader[groupPosition]]!!.size
    }

    override fun getGroup(groupPosition: Int): Any {
        return this._listDataHeader[groupPosition]
    }

    override fun getGroupCount(): Int {
        return this._listDataHeader.size
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getGroupView(
        groupPosition: Int, isExpanded: Boolean,
        convertView: View?, parent: ViewGroup
    ): View {
        var convertView = convertView
        val headerTitle = getGroup(groupPosition) as String
        if (convertView == null) {
            val infalInflater = this._context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = infalInflater.inflate(R.layout.list_group, null)
        }

        val lblListHeader = convertView!!
            .findViewById(R.id.lblListHeader) as TextView
        lblListHeader.setTypeface(null, Typeface.BOLD)
        lblListHeader.text = headerTitle

        return convertView
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    fun setListDataHeader(list: List<String>?) {
        _listDataHeader.clear()
        if (!list.isNullOrEmpty()) {
            _listDataHeader.addAll(list)
        }
        notifyDataSetChanged()
    }

    fun setListDataChild(list: Map<String, List<Theater>>?) {
        _listDataChild.clear()
        if (!list.isNullOrEmpty()) {
            _listDataChild.putAll(list)
        }
        notifyDataSetChanged()
    }

    fun setClickListener(listener: ClickListener) {
        clickListener = listener
    }

    interface ClickListener {
        fun onClick(item: Theater)
    }
}

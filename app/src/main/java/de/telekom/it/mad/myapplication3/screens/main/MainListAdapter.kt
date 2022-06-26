package de.telekom.it.mad.myapplication3.screens.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.telekom.it.mad.myapplication3.R
import de.telekom.it.mad.myapplication3.data.AppData
import de.telekom.it.mad.myapplication3.data.ImageAppData
import de.telekom.it.mad.myapplication3.data.TextAppData
import java.lang.IllegalArgumentException

class MainListAdapter(

) : RecyclerView.Adapter<MainViewHolder>() {

    var dataList: List<AppData> = emptyList()

    companion object {
        private const val IMAGE_VIEW_TYPE = 1
        private const val TEXT_VIEW_TYPE = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        IMAGE_VIEW_TYPE -> ImageMainViewHolder(inflate(parent, R.layout.card_image_app_data))
        TEXT_VIEW_TYPE -> TextMainViewHolder(inflate(parent, R.layout.card_text_app_data))
        else -> throw IllegalArgumentException()
    }

    private fun inflate(parent: ViewGroup, cardId: Int) =
        LayoutInflater.from(parent.context).inflate(cardId, parent, false)

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        when (holder) {
            is ImageMainViewHolder -> holder.bind(dataList[position] as ImageAppData)
            is TextMainViewHolder -> holder.bind(dataList[position] as TextAppData)
        }
    }

    override fun getItemCount() = dataList.size

    override fun getItemViewType(position: Int) = when (dataList[position]) {
        is ImageAppData -> IMAGE_VIEW_TYPE
        is TextAppData -> TEXT_VIEW_TYPE
    }
}

sealed class MainViewHolder(view: View) : RecyclerView.ViewHolder(view)

class ImageMainViewHolder(view: View) : MainViewHolder(view) {

    private val imageView: ImageView = view.findViewById(R.id.imageView)
    private val textView: TextView =  view.findViewById(R.id.textView)

    fun bind(item: ImageAppData) {
        imageView.visibility = if (item.error.isEmpty()) View.VISIBLE else View.GONE
        textView.visibility = if (item.error.isNotEmpty()) View.VISIBLE else View.GONE

        if (item.error.isEmpty()) {
            imageView.setImageBitmap(item.bitmap)
            textView.text = ""
        } else {
            imageView.setImageBitmap(null)
            textView.text = item.error
        }
    }
}


class TextMainViewHolder(view: View) : MainViewHolder(view) {

    private val textView: TextView =  view.findViewById(R.id.textView)

    fun bind(item: TextAppData) {
        textView.text = item.string
    }
}
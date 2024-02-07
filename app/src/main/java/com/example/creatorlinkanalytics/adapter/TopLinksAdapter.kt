package com.example.creatorlinkanalytics.adapter

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.creatorlinkanalytics.R
import com.example.creatorlinkanalytics.databinding.LinksItemsBinding
import com.example.creatorlinkanalytics.model.TopLinks
import java.text.SimpleDateFormat
import java.util.*


class TopLinksAdapter(
    private var context: Context,
    private val websiteItemClickListener: WebsiteItemClickListener
) :
    RecyclerView.Adapter<TopLinksAdapter.ViewHolder>() {

    private val items: ArrayList<TopLinks> = arrayListOf()

    fun setData(itemsData: ArrayList<TopLinks>) {
        items.clear()
        items.addAll(itemsData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LinksItemsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], context, websiteItemClickListener)
    }

    override fun getItemCount() = items.size

    class ViewHolder(private val binding: LinksItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            links: TopLinks,
            context: Context,
            websiteItemClickListener: WebsiteItemClickListener
        ) {
            with(binding) {
                Glide.with(context).load(links.original_image).apply(
                    RequestOptions.placeholderOf(R.drawable.ic_today_click)
                        .error(R.drawable.ic_today_click)
                ).into(icon)
                title.text = links.title
                clicks.text = links.total_clicks.toString()
                link.text = links.web_link
                date.text = links.created_at?.let { convertDateFormat(it) }
            }

            binding.link.setOnClickListener {
                links.web_link?.let { it1 -> websiteItemClickListener.onWebsiteItemClicked(it1) }
            }
            binding.icCopy.setOnClickListener {
                links.web_link?.let { it1 -> copyToClipboard(it1, context) }
            }
        }

        @SuppressLint("SimpleDateFormat")
        private fun convertDateFormat(inputDateStr: String): String? {
            val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val outputDateFormat = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)

            val inputDate = inputDateFormat.parse(inputDateStr)
            return inputDate?.let { outputDateFormat.format(it) }
        }

        private fun copyToClipboard(text: String, context: Context) {
            val clipboardManager =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("text", text)
            clipboardManager.setPrimaryClip(clipData)
            Toast.makeText(context, "copied link", Toast.LENGTH_LONG).show()
        }
    }

    interface WebsiteItemClickListener {
        fun onWebsiteItemClicked(url: String)
    }
}



package com.example.voicesearch.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.voicesearch.R
import com.example.voicesearch.room.entity.RecentSearch

class RecentSearchAdapter(var searchItem:List<RecentSearch>, var context: Context) : androidx.recyclerview.widget.RecyclerView.Adapter<RecentSearchAdapter.RecentSearchListViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecentSearchListViewHolder {
        return RecentSearchListViewHolder(LayoutInflater.from(context).inflate(R.layout.recent_search_list_item,p0,false))
    }

    override fun getItemCount(): Int {
        return searchItem.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(p0: RecentSearchListViewHolder, p1: Int) {
        p0.bind(searchItem[p1])
        p0.setIsRecyclable(false)
    }

    class RecentSearchListViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView){
        fun bind( searchItem: RecentSearch) {
            val itemView = itemView.findViewById<TextView>(R.id.tv_recent_search)
            itemView.text  = searchItem.search
        }

    }
}
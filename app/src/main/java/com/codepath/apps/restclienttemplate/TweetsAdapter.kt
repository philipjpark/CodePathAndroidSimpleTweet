package com.codepath.apps.restclienttemplate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codepath.apps.restclienttemplate.models.Tweet

class TweetsAdapter(val tweets: ArrayList<Tweet>) : RecyclerView.Adapter<TweetsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetsAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        // Inflate our item layout
        val view = inflater.inflate(R.layout.item_tweet, parent, false)

        return ViewHolder(view)
    }
    //populate data into the item through holder
    override fun onBindViewHolder(holder: TweetsAdapter.ViewHolder, position: Int) {
        // get the data model based on the position
        val tweet: Tweet = tweets.get(position)

        // set item views based on views and data model

        holder.ivtvUserName.text = tweet.user?.name
        holder.ivTweetBody.text = tweet.body
        holder.tvTimeStamp.setText(tweet.getFormattedTimestamp(tweet.createdAt))

        Glide.with(holder.itemView).load(tweet.user?.publicImageUrl).into(holder.ivProfileImage)
    }

    override fun getItemCount(): Int {
        return tweets.size
    }

    // Clean all elements of the recycler
    fun clear() {
        tweets.clear()
        notifyDataSetChanged()
    }

    // Add a list of items -- change to type used
    fun addAll(tweetList: List<Tweet>) {
        tweets.addAll(tweetList)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivProfileImage = itemView.findViewById<ImageView>(R.id.ivProfileImage)
        val ivtvUserName = itemView.findViewById<TextView>(R.id.tvUserName)
        val ivTweetBody = itemView.findViewById<TextView>(R.id.tvTweetBody)
        val tvTimeStamp = itemView.findViewById<TextView>(R.id.tvTimestamp)
    }
}
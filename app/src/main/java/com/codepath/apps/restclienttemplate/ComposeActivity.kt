package com.codepath.apps.restclienttemplate

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class  ComposeActivity : AppCompatActivity() {

    lateinit var etCompose: EditText
    lateinit var btnTweet: Button
    lateinit var charCount: TextView


    lateinit var client: TwitterClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        etCompose = findViewById(R.id.etTweetCompose)
        btnTweet = findViewById(R.id.btnTweet)
        charCount = findViewById(R.id.charCount)


        client = TwitterApplication.getRestClient(this)

        etCompose.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Fires right as the text is being changed (even supplies the range of text)
                if (s.length < 281) {
                    charCount.setTextColor(Color.BLACK)
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Fires right before text is changing
                val increase = s.length.toString()
                charCount.text = increase + "/280"
            }

            override fun afterTextChanged(s: Editable) {
                // Fires right after the text has changed
                if (s.length > 280) {
                    charCount.setTextColor(Color.RED)
                }
            }
        })

        //Handling the user's click on the tweet button
        btnTweet.setOnClickListener {

            //Grab the content of edittext (etCompose)
            val tweetContent = etCompose.text.toString()

            //1. Make sure the tweet isn't empty
            if (tweetContent.isEmpty()) {
                Toast.makeText(
                    this,
                    "Tweet is too Long! Limit is 140 characters",
                    Toast.LENGTH_SHORT
                ).show()
                //look into displaying SnackBar message
            } else

            //2. Make sure the tweet is under a character count
                if (tweetContent.length > 140) {
                    Toast.makeText(
                        this,
                        "Tweet is too long! Limit this to 140 characters",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    //Make an api call to Twitter to actually publish it
                    //Toast.makeText(this, tweetContent, Toast.LENGTH_SHORT).show()
                    client.publishTweet(tweetContent, object : JsonHttpResponseHandler() {
                        override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                            Log.e(TAG, "Successfully published tweet!")
                            //Send the tweet back to TimelineActivity

                            val tweet = Tweet.fromJson(json.jsonObject)

                            val intent = Intent()
                            intent.putExtra("tweet", tweet)
                            setResult(RESULT_OK, intent)
                            finish()
                        }

                        override fun onFailure(
                            statusCode: Int,
                            headers: Headers?,
                            response: String?,
                            throwable: Throwable?
                        ) {
                            Log.e(TAG, "Failed to publish tweet!", throwable)
                        }
                    })
                }
        }
    }
    companion object {
        val TAG = "ComposeActivity"
    }
}
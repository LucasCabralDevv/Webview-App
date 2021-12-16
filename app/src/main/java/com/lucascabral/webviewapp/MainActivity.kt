package com.lucascabral.webviewapp

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.widget.SearchView.*
import androidx.core.content.ContextCompat
import com.lucascabral.webviewapp.databinding.ActivityMainBinding
import java.net.InetAddress
import java.net.UnknownHostException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Thread {
                    val url = try {
                        InetAddress.getByName(query)
                        "https://$query"
                    }catch (e: UnknownHostException) {
                        "https://www.google.com/search?query=$query"
                    }

                    runOnUiThread {
                        binding.webView.loadUrl(url)
                    }
                }.start()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        binding.webView.apply {
            settings.javaScriptEnabled = true
            loadUrl("https://google.com.br")
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    binding.apply {
                        imgBack.isEnabled = webView.canGoBack()
                        imgForward.isEnabled = webView.canGoForward()

                        searchView.setQuery(url, false)

                        imgBack.imageTintList = if (imgBack.isEnabled)
                            ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.teal_200))
                        else
                            ColorStateList.valueOf(Color.GRAY)

                        imgForward.imageTintList = if (imgForward.isEnabled)
                            ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.teal_200))
                        else
                            ColorStateList.valueOf(Color.GRAY)
                    }
                }
            }
        }

        binding.imgBack.setOnClickListener {
            binding.webView.goBack()
        }
        binding.imgForward.setOnClickListener {
            binding.webView.goForward()
        }
    }
}

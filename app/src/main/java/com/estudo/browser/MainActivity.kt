package com.estudo.browser

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import com.estudo.browser.databinding.ActivityMainBinding
import java.net.InetAddress
import java.net.UnknownHostException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var back: ImageView
    private lateinit var forward: ImageView
    private lateinit var webView: WebView
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        back = binding.imageBack
        forward = binding.imageForward
        webView = binding.webView
        searchView = binding.search

        setupWebView()
        searchWebSite()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        navListeners()
        webView.settings.javaScriptEnabled = true
        webView.loadUrl("https://google.com")
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                back.isEnabled = webView.canGoBack()
                forward.isEnabled = webView.canGoForward()
                searchView.setQuery(url, false)

                back.imageTintList = if (back.isEnabled)
                    ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.teal_200))
                else
                    ColorStateList.valueOf(Color.GRAY)

                forward.imageTintList = if (forward.isEnabled)
                    ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.teal_200))
                else
                    ColorStateList.valueOf(Color.GRAY)

            }
        }
    }

    private fun searchWebSite() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Thread {
                    val url = try {
                        InetAddress.getByName(query)
                        "https://$query"
                    } catch (e: UnknownHostException) {
                        "https://www.google.com/search?query=$query"
                    }
                    runOnUiThread {
                        webView.loadUrl(url)
                    }
                }.start()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
    }

    private fun navListeners() {
        back.setOnClickListener { webView.goBack() }
        forward.setOnClickListener { webView.goForward() }
    }
}
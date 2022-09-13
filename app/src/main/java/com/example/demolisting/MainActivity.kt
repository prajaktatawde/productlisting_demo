package com.example.demolisting

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demolisting.databinding.ActivityMainBinding
import com.example.demolisting.models.RecyclerList
import com.example.demolisting.viewmodel.MainActivityViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = initViewModel()
        setupBinding(viewModel)
        setData()
    }

    private fun setData() {
        viewModel.getRecyclerListObserver().observe(this, Observer<RecyclerList> {
            activityMainBinding.progressBar.visibility = View.INVISIBLE
            if (it != null) {
                //update our apdater
                activityMainBinding.recyclerView.visibility = View.VISIBLE
                activityMainBinding.noInternet.visibility = View.GONE
                viewModel.setAdapterData(it.products)
            } else {
                activityMainBinding.recyclerView.visibility = View.GONE
                activityMainBinding.noInternet.visibility = View.VISIBLE
                if (!checkForInternet(applicationContext)) {
                    activityMainBinding.wifiImage.visibility = View.VISIBLE
                    activityMainBinding.txtNoInternet.setText("No Internet Connection")
                } else {
                    activityMainBinding.wifiImage.visibility = View.INVISIBLE
                    activityMainBinding.txtNoInternet.setText("Error in getting data")
                }
            }
        })
        activityMainBinding.progressBar.visibility = View.VISIBLE
        viewModel.makeApiCall()
    }

    private fun checkForInternet(context: Context): Boolean {

        // register activity with the connectivity manager service
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // if the android version is equal to M
        // or greater we need to use the
        // NetworkCapabilities to check what type of
        // network has the internet connection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Returns a Network object corresponding to
            // the currently active default data network.

            val network = connectivityManager.activeNetwork ?: return false

            // Representation of the capabilities of an active network.
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                // Indicates this network uses a Wi-Fi transport,
                // or WiFi has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                // Indicates this network uses a Cellular transport. or
                // Cellular has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                // else return false
                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

    fun initViewModel(): MainActivityViewModel {

        val viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        return viewModel
    }

    private fun setupBinding(viewModel: MainActivityViewModel) {
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        activityMainBinding.setVariable(BR.viewModel, viewModel)
        activityMainBinding.executePendingBindings()

        activityMainBinding.recyclerView.apply {
            this.layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

}
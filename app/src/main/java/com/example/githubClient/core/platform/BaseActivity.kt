package com.example.githubClient.core.platform

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.airbnb.mvrx.MavericksView
import com.example.githubClient.core.architecture.viewModel.QViewEvent
import com.example.githubClient.core.architecture.viewModel.ViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class BaseActivity<VB : ViewBinding> : ComponentActivity(), MavericksView {

    private lateinit var viewModelFactory: ViewModelProvider.Factory

    protected val activityViewModelProvider
        get() = ViewModelProvider(this, viewModelFactory)

    protected val fragmentViewModelProvider
        get() = ViewModelProvider(this, viewModelFactory)

    private var _binding: VB? = null

    protected val views: VB
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getBinding().root)
    }

    abstract fun getBinding(): VB


    override fun invalidate() {
        // no-ops by default
        Timber.v("invalidate() method has not been implemented")
    }

    protected fun <T : QViewEvent> ViewModel<*, *, T>.observeViewEvents(
            observer: (T) -> Unit,
    ) {
        val tag = this@BaseActivity::class.simpleName.toString()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewEvents
                        .stream(tag)
                        .collect {
                            observer(it)
                        }
            }
        }
    }
}

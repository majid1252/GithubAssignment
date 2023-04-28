package com.example.githubClient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.githubClient.core.platform.BaseActivity
import com.example.githubClient.databinding.ActivityMainBinding
import com.example.githubClient.ui.theme.GithubAssignmentTheme

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun getBinding() = ActivityMainBinding.inflate(layoutInflater)
}
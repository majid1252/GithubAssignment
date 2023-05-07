@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.githubClient.screen.userDetails

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.githubClient.data.model.GithubDetailedUser
import com.example.githubClient.data.model.GithubUserLocalData
import com.example.githubClient.ui.theme.AppTheme
import com.example.githubClient.ui.utils.formatDescriptiveCounter
import com.example.githubClient.viewModel.GithubUserDetailsViewModel
import com.example.githubClient.viewModel.viewState.DataFetchState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GithubUserDetailsScreen(viewModel: GithubUserDetailsViewModel, id: Int, username: String) {

    val userDetailsState by viewModel.userDetails.observeAsState(initial = null)
    val userNoteState by viewModel.userNote.observeAsState(initial = "")

    LaunchedEffect(key1 = id) {
        viewModel.getUserDetails(id, username)
    }

    AppTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            val focusManager = LocalFocusManager.current
            val rememberTitle = remember { mutableStateOf("") }
            Column {
                UserNameAppBar(rememberTitle.value)
                when (userDetailsState) {
                    is DataFetchState.Success -> {
                        (userDetailsState as DataFetchState.Success<GithubDetailedUser>).data.let { userDetails ->
                            rememberTitle.value = userDetails.name ?: ""
                            LazyColumn(modifier = Modifier
                                .padding(16.dp)) {
                                // Round image view inside card for avatar
                                item {
                                    Card(modifier = Modifier.padding(10.dp)) {
                                        RoundCornerImageCard(imageUrl = userDetails.avatar_url)
                                        SocialDataContainer(userDetails = userDetails)
                                    }
                                }
                                // User data (name, login, etc)
                                item {
                                    UserProperties(userDetails)
                                }
                                // User biography
                                item {
                                    UserBiography(userDetails.bio)
                                }
                                // User note layout
                                item {
                                    val note = remember { mutableStateOf(userNoteState) }

                                    // User note text field
                                    OutlinedTextField(
                                        value = note.value,
                                        onValueChange = { newValue -> note.value = newValue },
                                        label = { Text("Write a note for ${userDetails.name.takeIf { it.isNullOrEmpty().not() } ?: "the developer"}") },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 10.dp),
                                        shape = RoundedCornerShape(16.dp),
                                        singleLine = false,
                                        keyboardOptions = KeyboardOptions(autoCorrect = true, imeAction = ImeAction.Done),
                                        keyboardActions = KeyboardActions(onDone = {
                                            // update user note by new model
                                            viewModel.updateUserNote(
                                                GithubUserLocalData(
                                                    userId = userDetails.id.toString(),
                                                    note = note.value
                                                )
                                            )
                                            // clear focus and hide keyboard
                                            focusManager.clearFocus()
                                        })
                                    )
                                }
                            }
                        }
                    }

                    else                      -> {
                        // Show loading in any other state
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserNameAppBar(name: String) {
    if (name.isNotEmpty())
        CenterAlignedTopAppBar(
            modifier = Modifier.height(44.dp),
            title = {
                Text(
                    modifier = Modifier.padding(top = 18.dp),
                    text = name,
                    style = MaterialTheme.typography.titleLarge,
                )
            }
        )
}

@Composable
fun UserBiography(bio: String?) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .background(MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Biography", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = bio ?: "No biography",
                fontSize = 16.sp)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun RoundCornerImageCard(
    imageUrl: String,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    cornerRadius: Dp = 12.dp
) {
    Card(
        shape = RoundedCornerShape(cornerRadius),
        modifier = modifier.heightIn(min = 160.dp, max = 320.dp)
    ) {
        GlideImage(
            model = imageUrl,
            modifier = Modifier
                .fillMaxWidth(),
            contentDescription = contentDescription,
            contentScale = ContentScale.FillWidth,
        )
    }
}

@Composable
fun CounterLayout(followingCount: Int, followersCount: Int, publicRepo: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // create data counter cards components with count and label
        CountCard(count = followingCount, label = "Following")
        CountCard(count = followersCount, label = "Followers")
        CountCard(count = publicRepo, label = "Public Repos")
    }
}

@Composable
fun CountCard(count: Int, label: String) {
    OutlinedCard(
        modifier = Modifier
            .widthIn(min = 100.dp)
            .wrapContentHeight()
            .padding(8.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)) // Define the border
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = count.formatDescriptiveCounter(),
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun UserProfileCard(userProfileData: Map<String, String>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 8.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            ProfileDataColumn(data = userProfileData.entries.toList())
        }
    }
}

@Composable
fun ProfileDataColumn(data: List<Map.Entry<String, String>>) {
    Column {
        for (entry in data) {
            ProfileDataRow(label = entry.key, value = entry.value)
        }
    }
}

@Composable
fun ProfileDataRow(label: String, value: String) {
    Column(
        modifier = Modifier.padding(vertical = 4.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(end = 8.dp, top = 4.dp)
        )
        OutlinedCard(modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer)) {
            Text(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp),
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun SocialDataContainer(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 8.dp,
    userDetails: GithubDetailedUser? = null
) {
    Card(
        shape = RoundedCornerShape(cornerRadius),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = modifier
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        CounterLayout(
            followingCount = userDetails?.following ?: 0,
            followersCount = userDetails?.followers ?: 0,
            publicRepo = userDetails?.public_repos ?: 0
        )
    }
}

@Composable
fun UserProperties(userDetails: GithubDetailedUser) {
    // Extract user details to show in the profile card
    val userProfileData = mapOf(
        "Email" to (userDetails.email ?: "No email found"),
        "Company" to (userDetails.company ?: "No company found"),
    ).apply {
        if (userDetails.location.isNullOrEmpty().not())
            plus("Location" to userDetails.location)
    }

    // Pass the data to user detail component which shows data in a column
    UserProfileCard(userProfileData = userProfileData)
}

@Preview
@Composable
fun SocialData() {
    SocialDataContainer()
}

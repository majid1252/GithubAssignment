package com.example.githubClient.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.githubClient.data.db.GithubDatabase
import com.example.githubClient.data.model.GithubBaseUser
import com.example.githubClient.data.model.GithubUserLocalData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GithubUserDatabaseTest {

    private lateinit var userDatabase: GithubDatabase
    private val user = sampleUser()

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        userDatabase = Room.databaseBuilder(
            context, GithubDatabase::class.java, "test.db"
        ).fallbackToDestructiveMigration().allowMainThreadQueries().build()
    }

    @Test
    fun testUserCreation() = runBlockingTest {
        // Prepare a sample user for creation
        val user = sampleUser()

        // Insert the user into the database
        userDatabase.githubUserDao.insertAll(listOf(user))

        // Retrieve the user from the database
        val userList = userDatabase.githubUserDao.getUsersRaw()

        // Assert that the retrieved user matches the inserted user
        assertThat(userList.size, equalTo(1))
        assertThat(userList[0].id, equalTo(user.id))
        assertThat(userList[0].avatar_url, equalTo(user.avatar_url))
        assertThat(userList[0].login, equalTo(user.login))
    }

    @Test
    fun testUserUpdate() = runBlockingTest {
        // Prepare a sample user for creation
        val user = sampleUser()

        // Insert the user into the database
        userDatabase.githubUserDao.insertAll(listOf(user))

        // Update the user's login
        val updatedLogin = "Updated Login"
        val updatedUser = user.copy(login = updatedLogin)
        userDatabase.githubUserDao.insertAll(listOf(updatedUser))

        // Retrieve the updated user from the database
        val userList = userDatabase.githubUserDao.getUsersRaw()

        // Assert that the retrieved user has the updated login
        assertThat(userList.size, equalTo(1))
        assertThat(userList[0].id, equalTo(updatedUser.id))
        assertThat(userList[0].avatar_url, equalTo(updatedUser.avatar_url))
        assertThat(userList[0].login, equalTo(updatedUser.login))
    }

    @Test
    fun addUserNote() = runBlockingTest {
        val note = "This is a note"
        userDatabase.githubUserDao.insertLocalUserData(GithubUserLocalData(user.id.toString(), note))
        val userList = userDatabase.githubUserDao.getUsersWithLocalDataRaw()
        val correspondingUser = userList.find { it.githubUser.id == user.id }

        assertUserFields(correspondingUser?.githubUser)
        assertThat(userList.size, equalTo(1))
        assertThat(correspondingUser?.localData?.note, equalTo(note))
    }

    @Test
    fun updateUserNote() = runBlockingTest {
        val note = "This is a note"
        val newNote = "This is a new note"
        userDatabase.githubUserDao.insertLocalUserData(GithubUserLocalData(user.id.toString(), note))
        userDatabase.githubUserDao.insertLocalUserData(GithubUserLocalData(user.id.toString(), newNote))
        val userList = userDatabase.githubUserDao.getUsersWithLocalDataRaw()
        val correspondingUser = userList.find { it.githubUser.id == user.id }

        assertUserFields(correspondingUser?.githubUser)
        assertThat(userList.size, equalTo(1))
        assertThat(correspondingUser?.localData?.note, equalTo(newNote))
    }

    @Test
    fun deleteUserNote() = runBlockingTest {
        val note = "This is a note"
        userDatabase.githubUserDao.insertLocalUserData(GithubUserLocalData(user.id.toString(), note))
        userDatabase.githubUserDao.deleteLocalUserData(GithubUserLocalData(user.id.toString(), note))
        val userList = userDatabase.githubUserDao.getUsersWithLocalDataRaw()
        val correspondingUser = userList.find { it.githubUser.id == user.id }

        assertThat(userList.size, equalTo(1))
        assertThat(correspondingUser?.localData?.note, equalTo(null))
    }

    @Test
    fun userSearchTest() = runBlockingTest {
        // add 4 users to the database
        val users = listOf(
            sampleUser().copy(id = 1, login = "user1"),
            sampleUser().copy(id = 2, login = "user2"),
            sampleUser().copy(id = 3, login = "user3note1"),
            sampleUser().copy(id = 4, login = "user4"),
        )
        userDatabase.githubUserDao.insertAll(users)
        // add note for two of them
        userDatabase.githubUserDao.insertLocalUserData(GithubUserLocalData("1", "***note1"))
        userDatabase.githubUserDao.insertLocalUserData(GithubUserLocalData("2", "note2"))
        // search for users with "user" in their login
        val searchResults = userDatabase.githubUserDao.queryUsersByUsernameAndNote("*note1*")
        // assert that the results contain the two users with notes
        assertThat(searchResults.size, equalTo(2))
    }

    private fun sampleUser() = GithubBaseUser(
        0,
        "John Doe",
        "https://avatars.githubusercontent.com/u/1?v=4",
        "https://api.github.com/users/mojombo",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        false,
    )

    private fun assertUserFields(user: GithubBaseUser?) {
        assertThat(user?.id, equalTo(this.user.id))
        assertThat(user?.avatar_url, equalTo(this.user.avatar_url))
        assertThat(user?.login, equalTo(this.user.login))
    }

    private fun runBlockingTest(block: suspend () -> Unit) = runBlocking { block.invoke() }

}


import com.example.githubClient.data.api.GithubEndpoint
import org.junit.Assert.assertTrue
import org.junit.Test

class GithubApiTest {
    private val api: GithubApi = GithubEndpoint.githubApi

    @Test
    fun `print API response with input`() {
        // Call API
        val response = api.getUsers(0).execute()

        // Check if the response is successful
        assertTrue(response.isSuccessful)

        // Print API response
        val users: List<GithubUser>? = response.body()
        users?.forEach { user ->
            println("User: $user\n")
        }
    }
}
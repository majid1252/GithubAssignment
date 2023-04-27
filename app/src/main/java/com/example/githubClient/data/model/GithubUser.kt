data class GithubUser(
    // The GitHub user's username
    val login: String,

    // The unique ID assigned to the user by GitHub
    val id: Int,

    // The Node ID, a unique identifier used within the GitHub GraphQL API
    val node_id: String,

    // The URL of the user's avatar image
    val avatar_url: String,

    // The user's Gravatar ID (not used as GitHub now uses avatar_url)
    val gravatar_id: String,

    // The URL of the user's API endpoint
    val url: String,

    // The URL of the user's GitHub profile page
    val html_url: String,

    // The URL of the user's followers API endpoint
    val followers_url: String,

    // The URL of the user's following API endpoint (with a placeholder for the other user's username)
    val following_url: String,

    // The URL of the user's gists API endpoint (with a placeholder for the gist ID)
    val gists_url: String,

    // The URL of the user's starred repositories API endpoint (with placeholders for owner and repo names)
    val starred_url: String,

    // The URL of the user's subscriptions (watched repositories) API endpoint
    val subscriptions_url: String,

    // The URL of the user's organizations API endpoint
    val organizations_url: String,

    // The URL of the user's repositories API endpoint
    val repos_url: String,

    // The URL of the user's events API endpoint (with a placeholder for privacy settings)
    val events_url: String,

    // The URL of the events the user received (e.g., from other users) API endpoint
    val received_events_url: String,

    // The type of the account (e.g., "User" or "Organization")
    val type: String,

    // A boolean indicating whether the user is a site administrator or not
    val site_admin: Boolean
)

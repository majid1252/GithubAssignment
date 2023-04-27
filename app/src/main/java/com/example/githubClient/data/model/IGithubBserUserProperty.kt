package com.example.githubClient.data.model

interface IGithubBserUserProperty {
    // The name of the user
    val name: String?

    // The company or organizations the user is associated with
    val company: String?

    // The user's personal website or blog
    val blog: String?

    // The user's location
    val location: String?

    // The user's email address
    val email: String?

    // The user's availability for hire (true/false/null)
    val hireable: String?

    // A short bio of the user
    val bio: String?

    // The user's Twitter username
    val twitter_username: String?

    // The number of public repositories the user has
    val public_repos: Int

    // The number of public gists the user has
    val public_gists: Int

    // The number of users following this user
    val followers: Int

    // The number of users this user is following
    val following: Int

    // The timestamp of when the user's account was created
    val created_at: String

    // The timestamp of when the user's account was last updated
    val updated_at: String
}

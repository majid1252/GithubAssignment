import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.githubClient.data.api.GithubApi
import com.example.githubClient.data.db.GithubDatabase
import com.example.githubClient.data.model.GithubBaseUser
import com.example.githubClient.data.model.GithubUserWithLocalData
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber
import kotlin.math.pow

@OptIn(ExperimentalPagingApi::class) class GithubUsersRemoteMediator(
    private val githubApiService: GithubApi,
    private val githubDatabase: GithubDatabase
) : RemoteMediator<Int, GithubUserWithLocalData>() {

    // using mutex to ensure always one network call is running
    private val networkChannel = Mutex()

    // exponential back-off initializers
    // max retry on any type of network problem
    private val maxRetries = 3
    private val initialBackoffMillis = 1000L

    // current back off power by backoffFactor creates the next back off period
    private val backoffFactor = 2.0
    override suspend fun load(loadType: LoadType, state: PagingState<Int, GithubUserWithLocalData>): MediatorResult {
        try {
            // Determine the start key based on the LoadType
            val since = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND  -> {
                    val lastItem = state.lastItemOrNull()
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                    lastItem.githubUser.id
                }
            }
            Timber.d("loadType: $loadType, since: $since")
            var currentRetry = 0
            while (currentRetry < maxRetries) {
                val response = githubApiService.getUsers(since ?: 0)
                if (response.isSuccessful) {
                    val users = response.body() ?: emptyList()
                    // Save the data in the database
                    githubDatabase.withTransaction {
                        if (loadType == LoadType.REFRESH) {
                            githubDatabase.githubUserDao.clearAll()
                        }
                        githubDatabase.githubUserDao.insertAll(users)
                    }

                    return MediatorResult.Success(endOfPaginationReached = users.isEmpty())
                } else {
                    if (currentRetry >= maxRetries - 1) {
                        throw Exception("Failed to load data after $maxRetries retries")
                    }
                    val backoffMillis = initialBackoffMillis * (backoffFactor.pow(currentRetry))
                    delay(backoffMillis.toLong())
                    currentRetry++
                }
            }
        } catch (e: Throwable) {
            return MediatorResult.Error(e)
        }
        return MediatorResult.Error(Throwable())
    }
}


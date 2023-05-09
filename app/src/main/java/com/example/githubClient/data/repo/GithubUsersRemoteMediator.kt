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
    override suspend fun load(loadType: LoadType, state: PagingState<Int, GithubUserWithLocalData>): MediatorResult {
        try {
            // Determine the start key based on the LoadType
            val since = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = false)
                LoadType.APPEND  -> {
                    val lastItem = state.lastItemOrNull()
                        ?: return MediatorResult.Success(endOfPaginationReached = false)
                    lastItem.githubUser.id
                }
            }
            Timber.d("loadType: $loadType, since: $since")

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

            }
        } catch (e: Throwable) {
            return MediatorResult.Error(e)
        }
        return MediatorResult.Error(Throwable())
    }
}


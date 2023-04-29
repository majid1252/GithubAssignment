import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.bumptech.glide.load.HttpException
import com.example.githubClient.data.api.GithubApi
import com.example.githubClient.data.db.GithubDatabase
import com.example.githubClient.data.model.GithubBaseUser
import java.io.IOException

@OptIn(ExperimentalPagingApi::class) class GithubRemoteMediator(
    private val githubApiService: GithubApi,
    private val githubDatabase: GithubDatabase
) : RemoteMediator<Int, GithubBaseUser>() {
    override suspend fun load(loadType: LoadType, state: PagingState<Int, GithubBaseUser>): MediatorResult {
        return try {
            // Determine the start key based on the LoadType
            val since = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                    lastItem.id
                }
            }

            // Fetch data from the API
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

                MediatorResult.Success(endOfPaginationReached = users.isEmpty())
            } else {
                MediatorResult.Error(Throwable(response.errorBody().toString()))
            }
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}


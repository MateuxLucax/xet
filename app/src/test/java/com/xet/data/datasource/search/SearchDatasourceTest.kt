package com.xet.data.datasource.search

import com.xet.data.datasource.friend.FriendDataSource
import com.xet.data.datasource.user.UserDataSource
import com.xet.domain.model.FriendshipStatus
import com.xet.domain.model.User
import com.xet.dsd.ErrCode
import com.xet.dsd.ErrCodeException
import junit.framework.TestSuite
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test

class SearchDatasourceTest: TestSuite() {
    open class UserData(
        open var userId: String,
        open var displayName: String,
        open var username: String,
        open var token: String
    )

    companion object Testing {
        @JvmStatic lateinit var users: List<UserData>

        @BeforeClass
        @JvmStatic
        fun setUp() = runBlocking {
            val userDs = UserDataSource()
            val friendDs = FriendDataSource()

            val user1 = UserData("", "Logged User", "logged", "")
            val user2 = UserData("", "Marcos da Silva", "marcos", "")
            val user3 = UserData("", "Maria Eduarda", "maria", "")
            val user4 = UserData("", "Administrador do sistema", "admin123", "")
            val user5 = UserData("", "Testeeee", "testee", "")

            var data = ""
            try {
                var data = userDs.signUp(user1.displayName, user1.username, "12345")
            } catch (ex: Exception) {
            } finally {
                val data = userDs.signIn(user1.username, "12345")
                user1.userId = data.userId
                user1.token = data.token
            }

            try {
                val data = userDs.signUp(user2.displayName, user2.username, "12345")
            } catch (ex: Exception) {
            } finally {
                val data = userDs.signIn(user2.username, "12345")
                user2.userId = data.userId
                user2.token = data.token
            }

            try {
                var data = userDs.signUp(user3.displayName, user3.username, "12345")
            } catch (ex: Exception) {
            } finally {
                val data = userDs.signIn(user3.username, "12345")
                user3.userId = data.userId
                user3.token = data.token
            }

            try {
                var data = userDs.signUp(user4.displayName, user4.username, "12345")
            } catch (ex: Exception) {
            } finally {
                val data = userDs.signIn(user4.username, "12345")
                user4.userId = data.userId
                user4.token = data.token
            }

            try {
                val data = userDs.signUp(user5.displayName, user5.username, "teste123")
            } catch (ex: Exception) {
            } finally {
                val data = userDs.signIn(user5.username, "teste123")
                user5.userId = data.userId
                user5.token = data.token
            }

            try {
                val data = friendDs.sendInvite(user1.token, "5")
            } catch (ex: Exception) {}

            try {
                val data = friendDs.sendInvite(user4.token, user1.userId)
            } catch (ex: Exception) {}

            try {
                var data = friendDs.sendInvite(user5.userId, user1.token)
                println(data)
            } catch (ex: Exception) {
            }
            finally {
                try {
                    val data = friendDs.updateInvite(user1.userId, user5.token, true)
                } catch (w: Exception) {}

            }

            users = listOf(user1, user2, user3, user4, user5)
        }
    }

    @Test
    fun shouldReturnCorrectFriendshipStatusWhenListingUsers() = runBlocking {
        val ds = SearchDataSource()
        val data = ds.search(users[0].token, "", 1)
        Assert.assertNotNull(data)
        Assert.assertTrue(data.isNotEmpty())

        Assert.assertEquals(FriendshipStatus.NO_FRIEND_REQUEST,
                            data.filter { item -> item.userId ==  users[1].userId}[0].friendshipStatus)



        Assert.assertEquals(FriendshipStatus.RECEIVED_FRIEND_REQUEST,
                            data.filter { item -> item.userId ==  users[2].userId}[0].friendshipStatus)



        Assert.assertEquals(data.filter { item -> item.userId ==  users[3].userId}[0].friendshipStatus,
            FriendshipStatus.SENT_FRIEND_REQUEST)

        Assert.assertEquals(data.filter { item -> item.userId ==  users[4].userId}[0].friendshipStatus,
            FriendshipStatus.IS_FRIEND)
    }

    @Test
    fun shouldFilterResultWhenQueryIsNotEmpty() = runBlocking {
        val ds = SearchDataSource()
        val data = ds.search(users[0].token, "teste", 1)
        val filteredData = data.filter { item -> item.username.contains("teste".toRegex()) }

        Assert.assertNotNull(data)
        Assert.assertTrue(data.isNotEmpty())
        Assert.assertEquals(filteredData.size, data.size)
    }
}
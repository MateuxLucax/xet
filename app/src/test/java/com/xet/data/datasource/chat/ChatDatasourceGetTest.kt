package com.xet.data.datasource.chat

import com.xet.data.datasource.friend.FriendDataSource
import com.xet.data.datasource.search.SearchDatasourceTest
import com.xet.data.datasource.user.UserDataSource
import com.xet.data.repository.chat.model.SendMessagePayload
import com.xet.domain.model.Message
import com.xet.dsd.ErrCode
import com.xet.dsd.ErrCodeException
import com.xet.presentation.ServiceLocator
import junit.framework.TestSuite
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import java.util.*

class ChatDatasourceGetTest: TestSuite() {
    open class UserData(
        open var userId: String,
        open var displayName: String,
        open var username: String,
        open var token: String,
        open var password: String
    )

    companion object Testing {
        @JvmStatic lateinit var users: List<UserData>

        @BeforeClass
        @JvmStatic
        fun setUp() = runBlocking {
            val userDs = UserDataSource()
            val friendDs = FriendDataSource()
            val chatDs = ChatDataSource()

            val user1 = UserData("", "Testeeee", "teste", "", "teste123")
            val user2 = UserData("", "Other User", "other", "", "other123")

            try {
                var data = userDs.signUp(user1.displayName, user1.username, user1.password)
            } catch (ex: Exception) {
            } finally {
                val data = userDs.signIn(user1.username, user1.password)
                user1.userId = data.userId
                user1.token = data.token
            }

            try {
                val data = userDs.signUp(user2.displayName, user2.username, user2.password)
            } catch (ex: Exception) {
            } finally {
                val data = userDs.signIn(user2.username, user2.password)
                user2.userId = data.userId
                user2.token = data.token
            }

            try {
                var data = friendDs.sendInvite(user1.token, user2.userId)
            } catch (ex: Exception) {}
            finally {
                try {
                    friendDs.updateInvite(user1.userId, user2.token, true)
                } catch (w: Exception) {}
            }

            users = listOf(user1, user2)

            try {
                ServiceLocator.setUserToken(users[0].token)
                val data = chatDs.getMessages(user = users[0].userId,
                    friend = users[1].userId,
                    offset = 0,
                    limit = 20)

                if (data.isEmpty()) {
                    for (i in 0 until 30) {
                        try {
                            chatDs.sendMessage(user = users[0].userId,
                                friend = users[1].userId,
                                SendMessagePayload(
                                    text = "Message $i",
                                    file = null,
                                    fileType = null
                                )
                            )
                        } catch (ex: Exception) {}
                    }
                }
            } catch (ex: Exception) {}
        }
    }

    @Test
    fun shouldReturnErrorWithInvalidFriend() = runBlocking {
        val ds = ChatDataSource()
        ServiceLocator.setUserToken(users[0].token)

        try {
            ds.getMessages(user = users[0].userId,
                           friend = "-1",
                           offset = 0,
                           limit = 20)
            Assert.assertTrue(false)
        } catch (ex: Exception) {
            Assert.assertEquals(ErrCode.NOT_FRIENDS.resource, (ex as ErrCodeException).code.resource)
        }
    }

    @Test
    fun shouldReturnDuplicatedMessagesWhenIncorrectOffset() = runBlocking {
        val ds = ChatDataSource()
        ServiceLocator.setUserToken(users[0].token)

        try {
            val data01 = ds.getMessages(user = users[0].userId,
                            friend = users[1].userId,
                            offset = 0,
                            limit = 20)
            val data02 = ds.getMessages(user = users[0].userId,
                            friend = users[1].userId,
                            offset = data01[10].id.toLong()-1,
                            limit = 20)

            val filteredDuplicated = data01.filter { item -> data02.contains(item) }

            Assert.assertEquals(10, filteredDuplicated.size)
        } catch (ex: Exception) {
            Assert.assertTrue(false)
        }
    }

    @Test
    fun shouldReturnMessages() = runBlocking {
        val ds = ChatDataSource()
        ServiceLocator.setUserToken(users[0].token)

        try {
            val data = ds.getMessages(user = users[0].userId,
                                        friend = users[1].userId,
                                        offset = 0,
                                        limit = 20)
            Assert.assertNotNull(data)
            Assert.assertTrue(data.isNotEmpty())
            Assert.assertTrue(data.size == 20)
        } catch (ex: Exception) {
            Assert.assertTrue(false)
        }
    }
}
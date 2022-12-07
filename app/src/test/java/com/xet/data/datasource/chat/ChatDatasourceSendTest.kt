package com.xet.data.datasource.chat

import com.xet.data.Utils
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

class ChatDatasourceSendTest: TestSuite() {
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

            try {
                val createOtherTestData = userDs.signUp("Other User", "other", "other123")
            } catch (ex: Exception) {}

            try {
                val createOtherTestData = userDs.signUp("Other User", "other", "other123")
            } catch (ex: Exception) {}

            val userTestData = userDs.signIn("teste", "teste123")
            val userOtherTestData = userDs.signIn("other", "other123")
            users = listOf(UserData(userId = userTestData.userId,
                                    displayName = userTestData.displayName,
                                    username = userTestData.username,
                                    token = userTestData.token),
                           UserData(userId = userOtherTestData.userId,
                                    displayName = userOtherTestData.displayName,
                                    username = userOtherTestData.username,
                                    token = userOtherTestData.token))
        }
    }

    @Test
    fun shouldReturnErrorWhenUserIsNotAuthenticated() = runBlocking {
        val ds = ChatDataSource()
        ServiceLocator.setUserToken("") // Remove auth token

        try {
            ds.sendMessage(users[0].userId, users[1].userId,  SendMessagePayload(
                text = "Olá amigo",
                file = null,
                fileType = null
            ))
            Assert.assertTrue(false)
        } catch (ex: Exception) {
            Assert.assertEquals(ErrCode.MALFORMED_REQUEST.resource, (ex as ErrCodeException).code.resource)
        }
    }

    @Test
    fun shouldReturnErrorWithInvalidFriend() = runBlocking {
        val ds = ChatDataSource()
        ServiceLocator.setUserToken(users[0].token)

        try {
            ds.sendMessage(
                users[0].userId, "-1", SendMessagePayload(
                    text = "Olá amigo",
                    file = null
                )
            )
            Assert.assertTrue(false)
        } catch (ex: Exception) {
            Assert.assertEquals(
                ErrCode.NOT_FRIENDS.resource,
                (ex as ErrCodeException).code.resource
            )
        }
    }

    @Test
    fun shouldReturnErrorWithTooLongMessage() = runBlocking {
        val ds = ChatDataSource()
        ServiceLocator.setUserToken(users[0].token)
        val messageContent: String = "Some too long message to be repeated 1000 times".repeat(10000)

        try {
            val data = ds.sendMessage(users[0].userId, users[1].userId, SendMessagePayload(
                text = messageContent,
                file = null,
                fileType = null
            ))
            Assert.assertTrue(false)
        } catch (ex: Exception) {
            Assert.assertTrue(true)
        }
    }

    @Test
    fun shouldSendMessage() = runBlocking {
        val ds = ChatDataSource()
        ServiceLocator.setUserToken(users[0].token)

        try {
            val data = ds.sendMessage(users[0].userId, users[1].userId, SendMessagePayload(
                text = "Olá amigo.",
                file = null,
                fileType = null
            ))
            Assert.assertNotNull(data)
            Assert.assertTrue(data.id.isNotEmpty())
        } catch (ex: Exception) {
            println(ex)
            Assert.assertTrue(false)
        }
    }
}
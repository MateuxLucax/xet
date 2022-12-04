package com.xet.data.datasource.user

import com.xet.dsd.ErrCode
import com.xet.dsd.ErrCodeException
import junit.framework.TestSuite
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class UserDataSourceTest: TestSuite() {
    @Test
    fun shouldReturnIncorrectCredentialsWhenUsernameNotExists() = runBlocking {
        val ds = UserDataSource();
        try {
            ds.signIn("abcdef", "12345678");
            Assert.assertTrue(false);
        } catch (ex: Exception) {
            Assert.assertEquals(ErrCode.INCORRECT_CREDENTIALS.resource,(ex as ErrCodeException).code.resource)
        }
    }

    @Test
    fun shouldReturnIncorrectCredencialsWhenPasswordIsWrong() = runBlocking {
        val ds = UserDataSource();
        try {
            ds.signIn("teste", "12345678");
            Assert.assertTrue(false);
        } catch (ex: Exception) {
            Assert.assertEquals(ErrCode.INCORRECT_CREDENTIALS.resource,(ex as ErrCodeException).code.resource)
        }
    }

    @Test
    fun shouldReturnValidResponseWhenLoginDataIsCorrect() = runBlocking {
        val ds = UserDataSource();
        try {
            val data = ds.signIn("teste", "teste123");
            Assert.assertNotNull(data)
            Assert.assertTrue(data.token.isNotEmpty())
            Assert.assertEquals(data.username, "teste");
        } catch (ex: Exception) {
            Assert.assertTrue(false);
        }
    }
}
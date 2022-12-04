package com.xet.dsd

import com.xet.domain.model.User
import com.xet.domain.usecase.user.*
import junit.framework.TestSuite
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.*

class RequestTest: TestSuite() {
    @Before
    fun setUp() = runBlocking {

    }

    @Test
    fun shouldReturnErrorWhenHeadersAreIncorrect() = runBlocking {

    }
}
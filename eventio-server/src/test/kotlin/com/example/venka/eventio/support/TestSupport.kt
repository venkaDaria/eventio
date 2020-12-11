package com.example.venka.eventio.support

import com.example.venka.eventio.controller.GlobalExceptionHandler
import com.example.venka.eventio.data.model.Entity
import com.example.venka.eventio.utils.format.toJson
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.testng.Assert.assertEquals

/**
 * Get file name for Kotlin
 *
 * @return filename
 */
fun Any.fileName(): String = "${javaClass.name}Kt"

/**
 * Create MockMvc for specific Controller
 *
 * @return MockMvc
 */
fun Any.createMockMvc(): MockMvc = MockMvcBuilders
        .standaloneSetup(this)
        .setControllerAdvice(GlobalExceptionHandler())
        .build()

/**
 * Assert equals of entities
 *
 * @param actual entity
 */
fun <T> T.assertEquals(expected: T) {
    assertEquals(toString().trim(), expected.toString().trim())
}

/**
 * Assert equals of mvc result and an expected value
 *
 * @param expected value
 */
fun MvcResult.assertEquals(expected: Any) {
    assertEquals(response.contentAsString, expected.toJson())
}

/**
 * Assert equals of a collection and vararg values
 *
 * @param actual vararg values
 */
fun <T : Entity<*, R>, R : Comparable<R>> Collection<T>.assertEquals(vararg expected: T) {
    assertEqualsCollection<T, R>(expected.toList())
}

/**
 * Assert equals of collections
 *
 * @param actual collection
 */
fun <K : Entity<*, R>, R : Comparable<R>> Any.assertEqualsCollection(expected: Any) {
    val actualSorted = sort<K, R> { it.getParam() }
    val expectedSorted = expected.sort<K, R> { it.getParam() }

    assertEquals(actualSorted, expectedSorted)
}

@Suppress("UNCHECKED_CAST")
private inline fun <K, R : Comparable<R>> Any.sort(crossinline comparator: (K) -> R): Any {
    return when (this) {
        is Map<*, *> -> this.toSortedMap(compareBy { comparator.invoke(it as K) })
        is Collection<*> -> this.sortedBy { comparator.invoke(it as K) }
        else -> this
    }
}
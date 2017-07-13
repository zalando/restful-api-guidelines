package de.zalando.zally.rule

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
@ActiveProfiles("test")
class RuleUniquenessTest {
    @Autowired
    lateinit var rules: List<Rule>

    @Test
    fun rulesShouldBeUnique() {
        val duplicatedCodes = rules
            .groupBy { it.code }
            .filterValues { it.size > 1 }
            .keys

        assertEquals("Duplicated rules found: " + duplicatedCodes, 0, duplicatedCodes.count())
    }
}

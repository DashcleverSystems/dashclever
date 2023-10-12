package pl.dashclever.tests.integration.commons.hibernate

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import pl.dashclever.commons.hibernate.BaseEntity
import java.util.UUID

internal class BaseEntityTest {

    @Nested
    inner class Equals {

        @Test
        fun `comparing to null returns false`() {
            // given
            val entity = TestEntity()

            // when
            val result = entity.equals(null)

            // then
            assertThat(result).isFalse()
        }

        @Test
        fun `given entity with null id comparing to other entity with null id should return false`() {
            // given
            val entity = TestEntity(null)
            val other = TestEntity(null)

            // when
            val result = entity.equals(other)

            // then
            assertThat(result).isFalse()
        }

        @Test
        fun `comparing to itself should return true`() {
            // given
            val entity = TestEntity()

            // when
            val result = entity.equals(entity)

            // then
            assertThat(result).isTrue()
        }

        @Test
        fun `comparing to other with same id returns true`() {
            // given
            val id = UUID.randomUUID()
            val entity = TestEntity(id)
            val other = TestEntity(id)

            // when
            val result = entity.equals(other)

            // then
            assertThat(result).isTrue()
        }

        @Test
        fun `comparing to other with different id returns false`() {
            // given
            val entity = TestEntity(UUID.randomUUID())
            val other = TestEntity(UUID.randomUUID())

            // when
            val result = entity.equals(other)

            // then
            assertThat(result).isFalse()
        }

        @Test
        fun `comparing to different class returns false`() {
            // given
            val entity = TestEntity()
            val other = TestEntity2()

            // when
            val result = entity.equals(other)

            // then
            assertThat(result).isFalse()
        }

        @Test
        fun `comparing to different class with same id returns false`() {
            // given
            val id = UUID.randomUUID()
            val entity = TestEntity(id)
            val other = TestEntity2(id)

            // when
            val result = entity.equals(other)

            // then
            assertThat(result).isFalse()
        }
    }

    @Nested
    inner class BehaviourInSet {

        @Test
        fun `given all entities with null id should contain all`() {
            // given
            val entity1 = TestEntity(null)
            val entity2 = TestEntity(null)

            // when
            val result = setOf(entity1, entity2)

            // then
            assertThat(result).hasSize(2)
        }

        @Test
        fun `given all entities with same id should contain one`() {
            // given
            val id = UUID.randomUUID()
            val entity1 = TestEntity(id)
            val entity2 = TestEntity(id)

            // when
            val result = setOf(entity1, entity2)

            // then
            assertThat(result).hasSize(1)
        }
    }

    private class TestEntity(
        val id: UUID? = UUID.randomUUID(),
    ) : BaseEntity<UUID>() {

        override fun getIdentifier(): UUID? = this.id
    }

    private class TestEntity2(
        val id: UUID? = UUID.randomUUID(),
    ) : BaseEntity<UUID>() {

        override fun getIdentifier(): UUID? = this.id
    }
}

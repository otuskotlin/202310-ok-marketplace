package ru.otus.otuskotlin.marketplace.models

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters
import java.util.UUID

@UserDsl
class NameContext {
    var first: String = ""
    var second: String? = null
    var last: String = ""
}

@UserDsl
class ContactsDsl {
    var email: String = ""
    var phone: String = ""
    var address: String = ""
}

@UserDsl
class ActionsBuilder {
    private val _actions: MutableSet<Action> = mutableSetOf()

    val actions: Set<Action>
        get() = _actions.toSet()

    fun add(action: Action) {
        _actions.add(action)
    }

    operator fun Action.unaryPlus() {
        add(this)
    }
}

@UserDsl
class AvailabilityContext {
    private val _availabilies: MutableList<LocalDateTime> = mutableListOf()

    val availabilies: List<LocalDateTime>
        get() = _availabilies.toList()

    fun monday(time: String) = dayTimeOfWeek(DayOfWeek.MONDAY, time)
    fun tuesday(time: String) = dayTimeOfWeek(DayOfWeek.TUESDAY, time)
    fun wednesday(time: String) = dayTimeOfWeek(DayOfWeek.WEDNESDAY, time)
    fun thursday(time: String) = dayTimeOfWeek(DayOfWeek.THURSDAY, time)
    fun friday(time: String) = dayTimeOfWeek(DayOfWeek.FRIDAY, time)
    fun saturday(time: String) = dayTimeOfWeek(DayOfWeek.SATURDAY, time)
    fun sunday(time: String) = dayTimeOfWeek(DayOfWeek.SUNDAY, time)

    private fun dayTimeOfWeek(dayOfWeek: DayOfWeek, time: String) {
        val day = LocalDate.now().with(TemporalAdjusters.next(dayOfWeek))
        val time = time.split(":")
            .map { it.toInt() }
            .let { LocalTime.of(it[0], it[1]) }

        _availabilies.add(LocalDateTime.of(day, time))
    }
}

@UserDsl
class UserContext {
    private val id = UUID.randomUUID().toString()

    private var firstName: String = ""
    private var secondName: String? = null
    private var lastName: String = ""

    private var contacts = ContactsDsl()

    private var actions: Set<Action> = emptySet()
    private var available: List<LocalDateTime> = emptyList()

    @UserDsl
    fun name(block: NameContext.() -> Unit) {
        val ctx = NameContext().apply(block)

        firstName = ctx.first
        secondName = ctx.second
        lastName = ctx.last
    }

    @UserDsl
    fun contacts(block: ContactsDsl.() -> Unit) {
        val ctx = ContactsDsl().apply(block)

        contacts = ctx
    }

    @UserDsl
    fun actions(block: ActionsBuilder.() -> Unit) {
        val ctx = ActionsBuilder().apply(block)

        actions = ctx.actions
    }

    @UserDsl
    fun availability(block: AvailabilityContext.() -> Unit) {
        val ctx = AvailabilityContext().apply(block)

        available = ctx.availabilies
    }

    fun build() = User(
        id,
        firstName,
        secondName,
        lastName,
        contacts.phone,
        contacts.email,
        actions,
        available
    )
}

@UserDsl
fun buildUser(block: UserContext.() -> Unit): User = UserContext().apply(block).build()

@DslMarker
annotation class UserDsl

//fun buildUser(block: UserDsl.() -> Unit): User {
//    val ctx = UserDsl()
//    ctx.block()
//
//    return ctx.build()
//}


//fun name(block: NameContext.() -> Unit) {
//    val ctx = NameContext()
//    ctx.block()
//}


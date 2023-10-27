package ru.otus.otuskotlin.marketplace.backend.repo.sql

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import ru.otus.otuskotlin.marketplace.common.models.*

class AdTable(tableName: String = "ad") : Table(tableName) {
    val id = varchar("id", 128)
    val title = varchar("title", 128)
    val description = text("description")
    val owner = varchar("owner", 128)
    val visibility = enumeration("visibility", MkplVisibility::class)
    val dealSide = enumeration("deal_side", MkplDealSide::class)
    val lock = varchar("lock", 50)

    override val primaryKey = PrimaryKey(id)

    fun from(res : ResultRow) = MkplAd(
        id = MkplAdId(res[id].toString()),
        title = res[title],
        description = res[description],
        ownerId = MkplUserId(res[owner].toString()),
        visibility = res[visibility],
        adType = res[dealSide],
        lock = MkplAdLock(res[lock])
    )

    fun to(it: UpdateBuilder<*>, ad: MkplAd, randomUuid: () -> String) {
        it[id] = ad.id.takeIf { it != MkplAdId.NONE }?.asString() ?: randomUuid()
        it[title] = ad.title
        it[description] = ad.description
        it[owner] = ad.ownerId.asString()
        it[visibility] = ad.visibility
        it[dealSide] = ad.adType
        it[lock] = ad.lock.takeIf { it != MkplAdLock.NONE }?.asString() ?: randomUuid()
    }

}

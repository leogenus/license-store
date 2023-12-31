package uz.kvikk.licensestore.entity

import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.SQLRestriction
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigInteger
import java.util.*


@MappedSuperclass
@SQLRestriction("deleted=false")
abstract class BaseEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        var id: UUID? = null,
        var state: String? = null,
        var version: BigInteger? = null,

        @CreationTimestamp
        var createdAt: Date? = null,

        @UpdateTimestamp
        var updatedAt: Date? = null,

        var createdBy: UUID? = null,
        var updatedBy: UUID? = null,
        var deletedBy: UUID? = null,

        var deleted: Boolean = false,
)

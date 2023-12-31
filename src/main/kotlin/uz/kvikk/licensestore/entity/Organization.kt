package uz.kvikk.licensestore.entity

import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "\${project.tables.organization}")
data class Organization(
        var name: String? = null,
        var size: Long? = null
) : BaseEntity()

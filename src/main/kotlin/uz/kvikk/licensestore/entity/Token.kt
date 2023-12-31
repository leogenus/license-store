package uz.kvikk.licensestore.entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "\${project.tables.token}")
data class Token(
        @Lob
        var jwt: String? = null,

        var expire: Date? = null,

        @ManyToOne
        @JoinColumn(name = "license_id")
        var license: License? = null,
) : BaseEntity()
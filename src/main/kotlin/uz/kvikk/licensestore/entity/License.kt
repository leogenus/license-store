package uz.kvikk.licensestore.entity

import jakarta.persistence.*

@Entity
@Table(name = "\${project.tables.license}")
data class License(
        var name: String? = null,

        @Lob
        var privateKey: String? = null,

        @Lob
        var publicKey: String? = null,

        @Lob
        var idRsaPub: String? = null,

        @ManyToOne
        @JoinColumn(name = "organization_id")
        var organization: Organization? = null,
) : BaseEntity()
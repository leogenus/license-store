package uz.kvikk.licensestore.model

import java.util.UUID

data class LicenseResponse (
    val id: UUID,
    val name: String,
    val publicKey: String,
    val idRsaPub: String,
    val organizationId: UUID,
    val organizationName: String,
    var deleted: Boolean,
)
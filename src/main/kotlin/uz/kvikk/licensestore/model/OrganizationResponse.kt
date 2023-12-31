package uz.kvikk.licensestore.model

import java.util.*

data class OrganizationResponse(
        val id: UUID,
        val name: String,
        val size: Long,
        val licenseCount: Long,
        val deleted: Boolean,
)
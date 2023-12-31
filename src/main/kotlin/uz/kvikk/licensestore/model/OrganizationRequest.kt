package uz.kvikk.licensestore.model

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class OrganizationRequest(
        @field:NotBlank(message = "name is mandatory")
        var name: String? = null,
        @field:NotNull(message = "size is mandatory")
        var size: Long? = null
)
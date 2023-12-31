package uz.kvikk.licensestore.model.converter

import jakarta.annotation.PostConstruct
import org.modelmapper.Converter
import org.modelmapper.ModelMapper
import org.springframework.context.annotation.Configuration
import uz.kvikk.licensestore.entity.License
import uz.kvikk.licensestore.model.LicenseRequest
import uz.kvikk.licensestore.model.LicenseResponse

@Configuration
class LicenseConverter(val mapper: ModelMapper) {
    @PostConstruct
    fun init() {
        mapper.apply {
            addConverter(licenseToResponse, License::class.java, LicenseResponse::class.java)
            addConverter(requestToLicense, LicenseRequest::class.java, License::class.java)
        }
    }

    private val requestToLicense = Converter<LicenseRequest, License> {
        val src = it!!.source!!
        License(
                name = src.name!!,
        )
    }

    private val licenseToResponse = Converter<License, LicenseResponse> {
        val src = it!!.source!!
        val organization = src.organization!!
        val lName = src.name!!.replace("\\s".toRegex(), "")
        val oName = organization.name!!.replace("\\s".toRegex(), "")
        val pub = if(!src.idRsaPub.isNullOrEmpty())
            "ssh-rsa ${src.idRsaPub} $lName@$oName" else ""
        LicenseResponse(
                id = src.id!!,
                name = src.name!!,
                organizationId = organization.id!!,
                organizationName = organization.name!!,
                publicKey = src.publicKey ?: "",
                idRsaPub = pub,
                deleted = src.deleted
        )
    }

}

package uz.kvikk.licensestore.model.converter

import jakarta.annotation.PostConstruct
import org.modelmapper.Converter
import org.modelmapper.ModelMapper
import org.springframework.context.annotation.Configuration
import uz.kvikk.licensestore.entity.Organization
import uz.kvikk.licensestore.model.OrganizationRequest
import uz.kvikk.licensestore.model.OrganizationResponse
import uz.kvikk.licensestore.repository.LicenseRepository
import java.util.stream.Collectors

@Configuration
class OrganizationConverter(val mapper: ModelMapper, val licenseRepository: LicenseRepository) {
    @PostConstruct
    fun init() {
        mapper.apply {
            addConverter(organizationToResponse, Organization::class.java, OrganizationResponse::class.java)
            addConverter(requestToOrganization, OrganizationRequest::class.java, Organization::class.java)
        }
    }

    private val organizationToResponse = Converter<Organization, OrganizationResponse> {
        val src = it!!.source!!
        val count = licenseRepository.countByOrganizationId(src.id!!)
        OrganizationResponse(id = src.id!!, name = src.name!!, size = src.size ?: 0,
                licenseCount = count, deleted = src.deleted)
    }

    private val requestToOrganization = Converter<OrganizationRequest, Organization> {
        val src = it!!.source!!
        Organization(name = src.name, size = src.size)
    }
}




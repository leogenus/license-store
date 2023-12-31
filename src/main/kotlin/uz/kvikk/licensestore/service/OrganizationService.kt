package uz.kvikk.licensestore.service

import org.modelmapper.ModelMapper
import org.modelmapper.TypeToken
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import uz.kvikk.licensestore.entity.Organization
import uz.kvikk.licensestore.model.OrganizationRequest
import uz.kvikk.licensestore.model.OrganizationResponse
import uz.kvikk.licensestore.repository.OrganizationRepository
import java.util.*
import java.util.stream.Collectors


@Service
class OrganizationService(val mapper: ModelMapper, val repository: OrganizationRepository) {

    fun list(): Any {
        val list =  repository.findAllByDeletedFalse()
        return list
                .stream()
                .map { mapper.map(it, OrganizationResponse::class.java) }
                .collect(Collectors.toList())
    }

    fun one(id: UUID): OrganizationResponse {
        val one = findOrganizationById(id)
        return mapper.map(one, OrganizationResponse::class.java)
    }

    fun create(req: OrganizationRequest): OrganizationResponse {
        val data = mapper.map(req, Organization::class.java)
        val saved = repository.save(data)
        return mapper.map(saved, OrganizationResponse::class.java)
    }

    fun update(id: UUID, req: OrganizationRequest): OrganizationResponse {
        findOrganizationById(id)
        val data = mapper.map(req, Organization::class.java).apply { this.id = id }
        val saved = repository.save(data)
        return mapper.map(saved, OrganizationResponse::class.java)
    }

    fun delete(id: UUID): OrganizationResponse {
        val old = findOrganizationById(id)
        val saved = repository.save(old.apply { deleted = true })
        return mapper.map(saved, OrganizationResponse::class.java)
    }

    fun findOrganizationById(id: UUID): Organization {
        val one = repository.findById(id)
                .orElseThrow { ResponseStatusException(HttpStatus.BAD_REQUEST, "Organization not found") }
        if (one.deleted)
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Organization not found")
        return one
    }
}
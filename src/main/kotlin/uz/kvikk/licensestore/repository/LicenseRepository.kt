package uz.kvikk.licensestore.repository


import org.springframework.data.repository.CrudRepository
import uz.kvikk.licensestore.entity.License
import java.util.*

interface LicenseRepository : CrudRepository<License, UUID> {
    fun countByOrganizationId(organizationId: UUID): Long
    fun findAllByOrganizationIdAndDeletedFalse(organizationId: UUID): List<License>
}
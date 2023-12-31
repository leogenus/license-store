package uz.kvikk.licensestore.repository


import org.springframework.data.repository.CrudRepository
import uz.kvikk.licensestore.entity.Organization
import java.util.*

interface OrganizationRepository : CrudRepository<Organization, UUID> {
    fun findAllByDeletedFalse(): List<Organization>
}
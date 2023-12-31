package uz.kvikk.licensestore.resource

import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*
import uz.kvikk.licensestore.model.LicenseRequest
import uz.kvikk.licensestore.model.OrganizationRequest
import uz.kvikk.licensestore.repository.LicenseRepository
import uz.kvikk.licensestore.repository.OrganizationRepository
import uz.kvikk.licensestore.service.LicenseService
import uz.kvikk.licensestore.service.OrganizationService
import java.util.UUID

@RestController
@RequestMapping("license")
class LicenseResource(val service: LicenseService) {

    @GetMapping("list")
    fun list(@RequestParam organizationId: UUID): Any = service.list(organizationId)

    @GetMapping("one")
    fun one(@RequestParam organizationId: UUID,
            @RequestParam id: UUID): Any = service.one(organizationId, id)

    @PostMapping("create")
    fun create(@RequestParam organizationId: UUID,
               @Valid @RequestBody req: LicenseRequest): Any = service.create(organizationId, req)

    @PutMapping("update")
    fun update(@RequestParam organizationId: UUID,
               @RequestParam id: UUID,
               @RequestBody req: LicenseRequest): Any = service.update(organizationId, id, req)

    @DeleteMapping("delete")
    fun delete(@RequestParam organizationId: UUID,
               @RequestParam id: UUID): Any = service.delete(organizationId, id)

    @GetMapping("token")
    fun token(@RequestParam organizationId: UUID,
               @RequestParam id: UUID): Any = service.token(organizationId, id)

}
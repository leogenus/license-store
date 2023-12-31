package uz.kvikk.licensestore.resource

import jakarta.validation.Valid
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import uz.kvikk.licensestore.model.OrganizationRequest
import uz.kvikk.licensestore.service.OrganizationService
import java.util.*

@RestController
@RequestMapping("organization")
@Validated
class OrganizationResource(val service: OrganizationService) {

    @GetMapping("list")
    fun list(): Any = service.list()

    @GetMapping("one")
    fun one(@RequestParam id: UUID): Any = service.one(id)

    @PostMapping("create")
    fun create(@Valid @RequestBody req: OrganizationRequest): Any = service.create(req)

    @PutMapping("update")
    fun update(@RequestParam id: UUID,
               @RequestBody req: OrganizationRequest): Any = service.update(id, req)

    @DeleteMapping("delete")
    fun delete(@RequestParam id: UUID): Any = service.delete(id)

}
package uz.kvikk.licensestore.resource

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("index")
class IndexResource {
    @GetMapping
    fun index(): Any {
        return mapOf("status" to "ok")
    }
}
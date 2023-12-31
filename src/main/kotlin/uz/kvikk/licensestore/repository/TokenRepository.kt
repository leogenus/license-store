package uz.kvikk.licensestore.repository


import org.springframework.data.repository.CrudRepository
import uz.kvikk.licensestore.entity.License
import uz.kvikk.licensestore.entity.Token
import java.util.*

interface TokenRepository : CrudRepository<Token, UUID> {
}
package uz.kvikk.licensestore

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableJpaRepositories
@SpringBootApplication
class LicenseStoreApplication

fun main(args: Array<String>) {
    runApplication<LicenseStoreApplication>(*args)
}

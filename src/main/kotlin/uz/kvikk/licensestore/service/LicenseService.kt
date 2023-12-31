package uz.kvikk.licensestore.service

import com.sshtools.common.publickey.SshKeyPairGenerator
import org.modelmapper.ModelMapper
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import uz.kvikk.licensestore.entity.License
import uz.kvikk.licensestore.model.LicenseRequest
import uz.kvikk.licensestore.model.LicenseResponse
import uz.kvikk.licensestore.repository.LicenseRepository
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.stream.Collectors


@Service
class LicenseService(val mapper: ModelMapper, val repository: LicenseRepository,
                     val tokenService: TokenService,
                     val organizationService: OrganizationService) {

    fun list(organizationId: UUID): Any {
        organizationService.findOrganizationById(organizationId)
        val list = repository.findAllByOrganizationIdAndDeletedFalse(organizationId)
        return list
                .stream()
                .map { mapper.map(it, LicenseResponse::class.java) }
                .collect(Collectors.toList())
    }

    fun one(organizationId: UUID, id: UUID): LicenseResponse {
        organizationService.findOrganizationById(organizationId)
        val one = findLicenseById(id)
        if (one.organization!!.id != organizationId)
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "License not found")
        return mapper.map(one, LicenseResponse::class.java)
    }

    fun create(organizationId: UUID, req: LicenseRequest): LicenseResponse {
        val organization = organizationService.findOrganizationById(organizationId)
        val keyPair = generateKeyPair()
        val pub = keyPair.second
        val data = mapper.map(req, License::class.java)
                .apply {
                    this.organization = organization
                    this.privateKey = keyPair.first
                    this.publicKey = pub.first
                    this.idRsaPub = pub.second
                }
        val saved = repository.save(data)
        return mapper.map(saved, LicenseResponse::class.java)
    }

    fun update(organizationId: UUID, id: UUID, req: LicenseRequest): LicenseResponse {
        organizationService.findOrganizationById(organizationId)
        val old = findLicenseById(id)
        if (old.organization!!.id != organizationId)
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "License not found")
        val data = mapper.map(req, License::class.java)
                .apply {
                    this.id = id
                    this.organization = old.organization
                    this.privateKey = old.privateKey
                    this.publicKey = old.publicKey
                }
        val saved = repository.save(data)
        return mapper.map(saved, LicenseResponse::class.java)
    }

    fun delete(organizationId: UUID, id: UUID): LicenseResponse {
        organizationService.findOrganizationById(organizationId)
        val old = findLicenseById(id)
        if (old.organization!!.id != organizationId)
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "License not found")
        val saved = repository.save(old.apply { deleted = true })
        return mapper.map(saved, LicenseResponse::class.java)
    }

    fun token(organizationId: UUID, id: UUID): Any {
        organizationService.findOrganizationById(organizationId)
        val old = findLicenseById(id)
        if (old.organization!!.id != organizationId)
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "License not found")

        val token = tokenService.generateJwt(old, 1, ChronoUnit.DAYS)
        return mapOf(
                "token" to token
        )
    }

    fun findLicenseById(id: UUID): License {
        val one = repository.findById(id)
                .orElseThrow { ResponseStatusException(HttpStatus.BAD_REQUEST, "License not found") }
        if (one.deleted)
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "License not found")
        return one
    }

    fun generateKeyPair(): Pair<String, Pair<String, String>> {
        val pair = SshKeyPairGenerator.generateKeyPair(SshKeyPairGenerator.SSH2_RSA, 2048)
        val privateKey = Base64.getEncoder().encodeToString(pair.privateKey.jcePrivateKey.encoded)
        val publicKey = Base64.getEncoder().encodeToString(pair.publicKey.jcePublicKey.encoded)
        val idRsaPub = Base64.getEncoder().encodeToString(pair.publicKey.encoded)
        return Pair(privateKey, Pair(publicKey, idRsaPub))
    }
}
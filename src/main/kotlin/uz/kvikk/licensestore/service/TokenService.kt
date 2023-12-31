package uz.kvikk.licensestore.service


import com.nimbusds.jose.JOSEException
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.JWSSigner
import com.nimbusds.jose.crypto.RSASSASigner
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import com.sshtools.common.publickey.SshKeyUtils
import com.sshtools.common.ssh.components.SshKeyPair
import lombok.SneakyThrows
import org.springframework.stereotype.Service
import uz.kvikk.licensestore.entity.License
import uz.kvikk.licensestore.entity.Token
import uz.kvikk.licensestore.repository.TokenRepository
import uz.kvikk.licensestore.util.Log
import java.io.ByteArrayInputStream
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*


@Service
class TokenService(val repository: TokenRepository) {

    @SneakyThrows
    fun generateJwt(license: License, add: Long, unit: ChronoUnit): String {
        val expire = Date.from(Date().toInstant().plus(add, unit))
        val token = repository.save(Token(license = license, expire = expire))
        val publicKey = genPublicKey(license.publicKey!!)
        val privateKey = genPrivateKey(license.privateKey!!)
        val rsaKey = RSAKey.Builder(publicKey).privateKey(privateKey).build()
        val header: JWSHeader = JWSHeader.Builder(JWSAlgorithm.RS256)
                .build()

        val claims: JWTClaimsSet = JWTClaimsSet.Builder()
                .jwtID(token.id.toString())
                .issueTime(Date.from(Instant.now()))
                .expirationTime(expire)
                .subject(license.name)
                .build()

        val signedJWT = SignedJWT(header, claims)
        try {
            val signer: JWSSigner = RSASSASigner(Objects.requireNonNull(rsaKey.toRSAPrivateKey()))
            signedJWT.sign(signer)
            val jwt = signedJWT.serialize()
            log.info("JWT: {}", jwt)
            repository.save(token.apply {
                this.jwt = jwt
                this.expire = expire
            })
            return jwt
        } catch (e: JOSEException) {
            throw RuntimeException("Error while signing the JWT", e)
        }
    }

    fun genPublicKey(licensePublicKey: String): RSAPublicKey {
        return KeyFactory.getInstance("RSA").generatePublic(
                X509EncodedKeySpec(Base64.getDecoder().decode(licensePublicKey))
        ) as RSAPublicKey
    }

    fun genPrivateKey(licensePrivateKey: String): PrivateKey {
        val keySpec = PKCS8EncodedKeySpec(Base64.getDecoder().decode(licensePrivateKey));
        return KeyFactory.getInstance("RSA").generatePrivate(keySpec)
//        val pbeSpec = PBEKeySpec(null)
//        val publicKeyBytes = Base64.getDecoder().decode(licensePrivateKey)
//        val pkinfo = EncryptedPrivateKeyInfo(publicKeyBytes)
//        val skf = SecretKeyFactory.getInstance(pkinfo.algName)
//        val keySpec: PKCS8EncodedKeySpec = pkinfo.getKeySpec(skf.generateSecret(pbeSpec))
//        return KeyFactory.getInstance("RSA").generatePrivate(keySpec)
    }

    companion object : Log()
}
package uz.kvikk.licensestore.config


import org.hibernate.boot.model.naming.Identifier
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import uz.kvikk.licensestore.util.Log
import java.io.Serializable
import java.util.regex.Matcher
import java.util.regex.Pattern

@Configuration
class TableNameStrategy(val env: Environment) : PhysicalNamingStrategyStandardImpl(), Serializable {

    override fun toPhysicalTableName(name: Identifier, context: JdbcEnvironment?): Identifier {
        val result = getString(name.text)
        if(result.isNullOrEmpty())
            return name

        if(result == name.text)
            return name

        if (!alphaNumericPattern.matcher(result).matches())
            return name

        if(name.text == "\${$result}")
            return name

        val quoted = result != result.lowercase()
        return Identifier(result, quoted)
    }

    // Constructors, as needed
    fun getString(key: String): String? {
        val result = StringBuffer()
        val m: Matcher = curlyBracesPattern.matcher(key)
        while (m.find()) {
            val refKey: String = m.group(1)
            val refValue: String? = env[refKey]
            m.appendReplacement(result, refValue ?: refKey)
        }
        m.appendTail(result)
        return result.toString()
    }

    companion object : Log(){
        val curlyBracesPattern = Pattern.compile("\\$\\{([^}]+)}")!!
        val alphaNumericPattern = Pattern.compile("[a-zA-z0-9]*")!!
    }
}
//class TableNameStrategy: org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy()

/*: PhysicalNamingStrategy, Serializable {

    //    @Autowired
//    var env: Environment? = null
//    fun toPhysicalTableName(name: Identifier, context: JdbcEnvironment?): Identifier {
//        return Identifier(name.toIdentifier(env.getProperty("tableName")).getText(),
//                name.isQuoted())
//    }
//
//    companion object {
//        val INSTANCE = PhysicalNamingStrategyStandardImpl()
//    }
//    open fun toPhysicalCatalogName(logicalName: Identifier?, jdbcEnvironment: JdbcEnvironment): Identifier? {
//        return apply(logicalName, jdbcEnvironment)
//    }

    override fun toPhysicalTableName(name: Identifier, jdbcEnvironment: JdbcEnvironment?): Identifier? {
        return Identifier(toIdentifier(getTableName()).getText(), name.isQuoted)
    }

    private fun getTableName(): String? {
        return environment.getRequiredProperty(PROPERTY_TABLE_NAME)
    }

    override fun toPhysicalCatalogName(logicalName: Identifier?, jdbcEnvironment: JdbcEnvironment?): Identifier {
        return apply(logicalName, jdbcEnvironment!!)!!
    }

    override fun toPhysicalSchemaName(logicalName: Identifier?, jdbcEnvironment: JdbcEnvironment): Identifier? {
        return apply(logicalName, jdbcEnvironment)
    }

    override fun toPhysicalTableName(logicalName: Identifier?, jdbcEnvironment: JdbcEnvironment): Identifier? {
        return apply(logicalName, jdbcEnvironment)
    }

    override fun toPhysicalSequenceName(logicalName: Identifier?, jdbcEnvironment: JdbcEnvironment): Identifier? {
        return apply(logicalName, jdbcEnvironment)
    }

    override fun toPhysicalColumnName(logicalName: Identifier?, jdbcEnvironment: JdbcEnvironment): Identifier? {
        return apply(logicalName, jdbcEnvironment)
    }

    private fun apply(name: Identifier?, jdbcEnvironment: JdbcEnvironment): Identifier? {
        if (name == null) {
            return null
        }
        val builder = StringBuilder(name.text.replace('.', '_'))
        var i = 1
        while (i < builder.length - 1) {
            if (isUnderscoreRequired(builder[i - 1], builder[i], builder[i + 1])) {
                builder.insert(i++, '_')
            }
            i++
        }
        return getIdentifier(builder.toString(), name.isQuoted, jdbcEnvironment)
    }

    */
/**
 * Get an identifier for the specified details. By default this method will return an identifier
 * with the name adapted based on the result of [.isCaseInsensitive]
 *
 * @param name the name of the identifier
 * @param quoted if the identifier is quoted
 * @param jdbcEnvironment the JDBC environment
 *
 * @return an identifier instance
 *//*
    protected fun getIdentifier(name: String, quoted: Boolean, jdbcEnvironment: JdbcEnvironment?): Identifier? {
        var name = name
        if (isCaseInsensitive(jdbcEnvironment)) {
            name = name.lowercase()
        }
        return Identifier(name, quoted)
    }

    */
/**
 * Specify whether the database is case sensitive.
 *
 * @param jdbcEnvironment the JDBC environment which can be used to determine case
 *
 * @return true if the database is case insensitive sensitivity
 *//*
    protected fun isCaseInsensitive(jdbcEnvironment: JdbcEnvironment?): Boolean {
        return true
    }

    private fun isUnderscoreRequired(before: Char, current: Char, after: Char): Boolean {
        return Character.isLowerCase(before) && Character.isUpperCase(current) && Character.isLowerCase(after)
    }
}*/
package uz.kvikk.licensestore.config.handler

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange
import java.util.*
import java.util.stream.Collectors


@ControllerAdvice
class ValidationHandler {
    @ExceptionHandler(WebExchangeBindException::class)
    fun webExchangeBindException(web: ServerWebExchange, error: WebExchangeBindException): ResponseEntity<Any> {
        val status = HttpStatus.BAD_REQUEST
        val message = "Invalid body or params"
        val errors = error.bindingResult
                .allErrors
                .stream()
                .map { obj: ObjectError -> obj.defaultMessage }
                .collect(Collectors.toList())
        val errorAttributes = linkedMapOf(
                "requestId" to web.request.id,
                "timestamp" to Date(),
                "path" to web.request.path.value(),
                "status" to status.value(),
                "error" to status.reasonPhrase,
                "message" to message,
                "reason" to errors
        )
        return ResponseEntity
                .status(status)
                .body(errorAttributes)
    }

    @ExceptionHandler(ResponseStatusException::class)
    fun responseStatusException(web: ServerWebExchange, error: ResponseStatusException): ResponseEntity<Any> {
        val status = HttpStatus.valueOf(error.statusCode.value())
        val message = error.reason

        val errorAttributes = linkedMapOf(
                "requestId" to web.request.id,
                "timestamp" to Date(),
                "path" to web.request.path.value(),
                "status" to status.value(),
                "error" to status.reasonPhrase,
                "message" to message,
        )
        return ResponseEntity
                .status(status)
                .body(errorAttributes)
    }

}
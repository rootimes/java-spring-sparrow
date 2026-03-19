package sparrow.exception;

import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 400 - Bad Request: 參數驗證失敗
     */
    @ExceptionHandler({ MethodArgumentNotValidException.class, IllegalArgumentException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleBadRequest(Exception ex) {
        String message;
        if (ex instanceof MethodArgumentNotValidException) {
            message = ((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors().get(0)
                    .getDefaultMessage();
        } else {
            message = ex.getMessage();
        }
        return Map.of("error", message);
    }

    /**
     * 401 - Unauthorized: 認證失敗
     */
    @ExceptionHandler({ AuthenticationException.class, BadCredentialsException.class })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, String> handleUnauthorized(Exception ex) {
        return Map.of("error", ex.getMessage());
    }

    /**
     * 404 - Not Found: 資源不存在
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFound(ResourceNotFoundException ex) {
        return Map.of("error", ex.getMessage());
    }

    /**
     * 409 - Conflict: 資料衝突
     */
    @ExceptionHandler({ ConflictException.class, DataIntegrityViolationException.class })
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleConflict(Exception ex) {
        return Map.of("error", ex.getMessage());
    }
}

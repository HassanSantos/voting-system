package com.dbserver.voting_system.config;

import com.dbserver.voting_system.domain.exception.AgendaNotFoundException;
import com.dbserver.voting_system.domain.exception.DuplicateVoteException;
import com.dbserver.voting_system.domain.exception.InvalidAgendaTitleException;
import com.dbserver.voting_system.domain.exception.InvalidCpfException;
import com.dbserver.voting_system.domain.exception.InvalidVoteValueException;
import com.dbserver.voting_system.domain.exception.InvalidVotingSessionDurationException;
import com.dbserver.voting_system.domain.exception.UnableToVoteException;
import com.dbserver.voting_system.domain.exception.VotingSessionAlreadyOpenException;
import com.dbserver.voting_system.domain.exception.VotingSessionClosedException;
import com.dbserver.voting_system.domain.exception.VotingSessionNotFoundException;
import com.dbserver.voting_system.domain.exception.VotingSessionResultUnavailableException;
import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AgendaNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAgendaNotFound(AgendaNotFoundException exception) {
        return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(VotingSessionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleSessionNotFound(VotingSessionNotFoundException exception) {
        return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler({InvalidCpfException.class, UnableToVoteException.class})
    public ResponseEntity<ErrorResponse> handleCpfNotFound(RuntimeException exception) {
        return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler({VotingSessionClosedException.class, DuplicateVoteException.class, VotingSessionAlreadyOpenException.class})
    public ResponseEntity<ErrorResponse> handleConflict(RuntimeException exception) {
        return buildResponse(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(VotingSessionResultUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleResultUnavailable(VotingSessionResultUnavailableException exception) {
        return buildResponse(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler({
            InvalidAgendaTitleException.class,
            InvalidVotingSessionDurationException.class,
            InvalidVoteValueException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequest(RuntimeException exception) {
        return buildResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage() == null ? error.getField() + " is invalid" : error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return buildResponse(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException exception) {
        return buildResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMessageNotReadable(HttpMessageNotReadableException exception) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Malformed request body");
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(new ErrorResponse(message));
    }

    public record ErrorResponse(String message) {
    }
}

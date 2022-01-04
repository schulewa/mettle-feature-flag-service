package com.mettle.mettlefeatureflagservice.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalRestExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<Object> handleRuntimeException(RuntimeException rte, WebRequest request) {
    String errMsg = extractErrorMessage(rte);

    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", ZonedDateTime.now());
    body.put("error", errMsg);

    log.error("Error caught processing request [{}]. Error [{}]", request, errMsg, rte);

    return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Override
  protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                       HttpHeaders headers,
                                                                       HttpStatus status,
                                                                       WebRequest request) {
    String errMsg = extractErrorMessage(ex);

    log.error("Method not supported for processing request [{}], status [{}]. Error [{}]", request, status, errMsg, ex);

    return super.handleHttpRequestMethodNotSupported(ex, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                HttpHeaders headers, HttpStatus status, WebRequest request) {
    String errMsg = extractErrorMessage(ex);

    log.error("Message  not readable when processing request [{}], status [{}]. Error [{}]", request, status, errMsg, ex);

    return super.handleHttpMessageNotReadable(ex, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex,
                                                                HttpHeaders headers,
                                                                HttpStatus status,
                                                                WebRequest request) {
    String errMsg = extractErrorMessage(ex);

    log.error("Conversion not supported for processing request [{}], status [{}]. Error [{}]", request, status, errMsg, ex);

    return super.handleConversionNotSupported(ex, headers, status, request);
  }

  private String extractErrorMessage(Exception ex) {
    return ex == null ? "<null exception>" : ex.getCause() == null ? ex.getMessage() : ex.getCause().getMessage();
  }
}

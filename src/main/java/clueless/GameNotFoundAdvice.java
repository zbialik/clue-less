package clueless;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class GameNotFoundAdvice {

  @ResponseBody
  @ExceptionHandler(GameNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String gamaeNotFoundHandler(GameNotFoundException ex) {
    return ex.getMessage();
  }
}
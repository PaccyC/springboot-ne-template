package com.paccy.templates.springboot.helpers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponse <T>{

    String message;
    HttpStatus status;
    T data;

    public  ApiResponse(String message, HttpStatus status, T data) {
        this.message = message;
        this.status = status;
        this.data = data;
    }

    public ResponseEntity<ApiResponse<T>> toResponseEntity(){
        assert  status != null;
        return ResponseEntity.status(status).body(this);
    }



}

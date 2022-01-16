package com.reactiverx.rxjavapoc.controller;

import com.reactiverx.rxjavapoc.beans.StreamPocBean;
import com.reactiverx.rxjavapoc.service.StreamPocService;
import io.reactivex.rxjava3.core.Observable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@CrossOrigin("*")
@RestController
public class PocController {

    @Autowired
    private StreamPocService streamPocService;

    @GetMapping(value = "/stream/get", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter emitResponse(){
        return streamPocService.getSseEmitter();
    }
}

package com.reactiverx.rxjavapoc.service;

import com.reactiverx.rxjavapoc.beans.StreamPocBean;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class StreamPocService {

    /*public ResponseBodyEmitter getResponseEmitter(){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        streamResponseObjects(emitter);
        return emitter;
    }*/

    public SseEmitter getSseEmitter(){
        SseEmitter emitter = new SseEmitter();
        streamResponseObjects(emitter);
        return emitter;
    }

    public void streamResponseObjects(SseEmitter emitter){
        List<CompletableFuture<StreamPocBean>> asyncs = new ArrayList<>();
        for(int i=0; i < 20; i++){
            int counter = i;
            CompletableFuture<StreamPocBean> async = CompletableFuture.supplyAsync(() -> getStreamPocBean(counter));
            asyncs.add(async);
        }
        CompletableFuture.runAsync(() -> {
            asyncs.forEach(async -> {
                try{
                    StreamPocBean pocBean = async.get();
                    emitter.send(pocBean);
                    try{
                        Thread.sleep(1000*3);
                    }catch(Exception exception){
                        log.error("Exception while putting thread to sleep");
                    }
                }catch(Exception exception){
                    log.error("Error while fetching async value: {}", exception.getMessage());
                    exception.printStackTrace();
                }
            });
            emitter.complete();
        });
    }

    public StreamPocBean getStreamPocBean(int counter){
        StreamPocBean bean = new StreamPocBean(counter, counter+" has been constructed");
        return bean;
    }
}

package com.ubrillo.ubrillodeliverysystem.DatabaseAPI;

import com.ubrillo.ubrillodeliverysystem.Logic.Request;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DatabaseAPI {
    private final DatabaseInterface databaseItf;
    public DatabaseAPI(DatabaseInterface orderDb){
        this.databaseItf = orderDb;
    }

    public List<Request> getOrders(){
        return databaseItf.findAll();
        //return orderDb.findAll().stream().map(); if you want to filter some details to show
    }

    public void insertOrder(Request request){
        databaseItf.save(request);
    }
    //public
//    public Optional<SoftwareEngineer> getSoftwareEngineerById(Integer id) {
//        return Optional.of(softwareEngineerRepository.findById(id)
//                .orElseThrow(() -> new IllegalStateException(
//                        id + "not found")));
//    }
}

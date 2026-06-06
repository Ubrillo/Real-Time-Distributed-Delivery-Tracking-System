package com.ubrillo.ubrillodeliverysystem.DatabaseAPI;

import com.ubrillo.ubrillodeliverysystem.Logic.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DatabaseInterface extends JpaRepository<Request, String> {}

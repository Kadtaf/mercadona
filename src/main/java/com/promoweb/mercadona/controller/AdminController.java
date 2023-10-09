package com.promoweb.mercadona.controller;


import com.promoweb.mercadona.model.Admin;
import com.promoweb.mercadona.service.AdminService;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService){

        this.adminService = adminService;

    }

    //Read
    @GetMapping("/{id}")
    public ResponseEntity<Admin> getAdminById(@PathVariable Long id) {
        Admin admin = adminService.getAdminById(id);
        if (admin != null) {
            return ResponseEntity.ok(admin);
        } else {
            throw new EntityNotFoundException("L'Administrateur avec l'id : " +id + " n'existe pas");
        }

    }

    //Create
    @PostMapping
    public ResponseEntity<Admin> createAdmin(@RequestBody Admin admin) {
        Admin createAdmin = adminService.createAdmin(admin);
        return ResponseEntity.status(HttpStatus.CREATED).body(createAdmin);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Admin>> getAllAdmins() {
        List<Admin> admins = adminService.getAllAdmins();
        return ResponseEntity.ok(admins);
    }

    //Update
    @PutMapping("/{id}")
    public ResponseEntity<Admin> updateUser(@PathVariable Long id, @RequestBody Admin admin) {
        Admin updateAdmin = adminService.updateAdmin(id, admin);
        if (updateAdmin !=null) {
            return ResponseEntity.ok(updateAdmin);
        } else {
            throw new EntityNotFoundException("L'administrateur avec l'id : " + id + " n'existe pas");
        }
    }

    //Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
        adminService.deleteAdmin(id);
        return ResponseEntity.noContent().build();
    }
}

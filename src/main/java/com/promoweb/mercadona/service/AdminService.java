package com.promoweb.mercadona.service;

import com.promoweb.mercadona.model.Admin;
import com.promoweb.mercadona.repository.AdminRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {


    private final AdminRepository adminRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository) {

        this.adminRepository = adminRepository;
    }

    public Admin getAdminById(Long id) {
        return adminRepository.findById(id).orElse(null);
    }

    public Admin createAdmin(Admin admin) {
        // Ajoutez ici la logique de validation ou de traitement avant d'enregistrer dans la base de données
        return adminRepository.save(admin);
    }

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public Admin updateAdmin(Long id, Admin admin) {
        Admin existingAdmin = getAdminById(id);
        if (existingAdmin != null) {
            // Ajoutez ici la logique de validation ou de traitement avant d'enregistrer dans la base de données
            existingAdmin.setUsername(admin.getUsername());
            existingAdmin.setPassword(admin.getPassword());
            return adminRepository.save(existingAdmin);
        } else {
            throw new EntityNotFoundException("L'Administrateur avec l'id : " +id + " n'existe pas");
        }
    }

    public void deleteAdmin(Long id) {
        if (adminRepository.existsById(id)) {
            adminRepository.deleteById(id);
        }  else {
            throw new EntityNotFoundException("L'Administrateur avec l'id : " +id + " n'existe pas");
        }

    }
}

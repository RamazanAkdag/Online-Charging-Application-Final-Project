package com.ramobeko.accountordermanagement.controller;

import com.ramobeko.accountordermanagement.model.dto.OraclePackageDTO;
import com.ramobeko.accountordermanagement.model.entity.oracle.OraclePackage;
import com.ramobeko.accountordermanagement.service.abstrct.oracle.IOraclePackageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/packages")
public class PackageController {

    private final IOraclePackageService oraclePackageService;

    public PackageController(IOraclePackageService oraclePackageService) {
        this.oraclePackageService = oraclePackageService;
    }

    @GetMapping
    public ResponseEntity<List<OraclePackage>> getAllPackages() {
        List<OraclePackage> packages = oraclePackageService.readAll();
        return ResponseEntity.ok(packages);
    }
}

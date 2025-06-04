package com.ramobeko.accountordermanagement.init;

import com.ramobeko.accountordermanagement.model.entity.oracle.OraclePackage;
import com.ramobeko.accountordermanagement.repository.oracle.OraclePackageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class PackageDataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(PackageDataLoader.class);
    private final OraclePackageRepository packageRepository;

    public PackageDataLoader(OraclePackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    @Override
    public void run(String... args) {
        if (packageRepository.count() == 0) {
            logger.info("Initializing package data...");

            List<OraclePackage> packages = List.of(
                    new OraclePackage("İstanbul Paketi", 750L, 300L, 5000L, new BigDecimal("29.99")),
                    new OraclePackage("Ankara Paketi", 600L, 250L, 4000L, new BigDecimal("24.99")),
                    new OraclePackage("İzmir Paketi", 800L, 350L, 6000L, new BigDecimal("34.99")),
                    new OraclePackage("Bursa Paketi", 550L, 200L, 3500L, new BigDecimal("19.99")),
                    new OraclePackage("Antalya Paketi", 900L, 400L, 7000L, new BigDecimal("39.99"))
            );

            packageRepository.saveAll(packages);
            logger.info("Package data inserted successfully.");
        } else {
            logger.info("Package data already exists, skipping initialization.");
        }
    }
}

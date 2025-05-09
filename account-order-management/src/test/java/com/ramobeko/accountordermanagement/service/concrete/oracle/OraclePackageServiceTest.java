package com.ramobeko.accountordermanagement.service.concrete.oracle;

import com.ramobeko.accountordermanagement.model.dto.OraclePackageDTO;
import com.ramobeko.accountordermanagement.model.entity.oracle.OraclePackage;
import com.ramobeko.accountordermanagement.repository.oracle.OraclePackageRepository;
import com.ramobeko.accountordermanagement.util.mapper.dto.OraclePackageMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OraclePackageServiceTest {

    private OraclePackageRepository repository;
    private OraclePackageMapper mapper;
    private OraclePackageService service;

    @BeforeEach
    void setUp() {
        repository = mock(OraclePackageRepository.class);
        mapper = mock(OraclePackageMapper.class);
        service = new OraclePackageService(repository, mapper);
    }

    @Test
    void readAll_shouldReturnPackageList() {
        when(repository.findAll()).thenReturn(Collections.singletonList(new OraclePackage()));

        assertFalse(service.readAll().isEmpty());
    }

    @Test
    void create_shouldSaveMappedEntity() {
        OraclePackageDTO dto = new OraclePackageDTO();
        dto.setName("Test Package");

        OraclePackage entity = new OraclePackage();
        entity.setName("Test Package");

        when(mapper.toEntity(dto)).thenReturn(entity);

        service.create(dto);

        verify(repository).save(entity);
    }

    @Test
    void readById_shouldReturnPackage_whenExists() {
        OraclePackage entity = new OraclePackage();
        entity.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        OraclePackage result = service.readById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void readById_shouldReturnNull_whenNotExists() {
        when(repository.findById(2L)).thenReturn(Optional.empty());

        OraclePackage result = service.readById(2L);

        assertNull(result);
    }

    @Test
    void update_shouldUpdateAndSave_whenPackageExists() {
        OraclePackageDTO dto = new OraclePackageDTO();
        dto.setId(3L);
        dto.setName("Updated Package");

        OraclePackage existing = new OraclePackage();
        existing.setId(3L);

        when(repository.findById(3L)).thenReturn(Optional.of(existing));

        service.update(dto);

        verify(mapper).updateFromDto(dto, existing);
        verify(repository).save(existing);
    }

    @Test
    void update_shouldDoNothing_whenPackageNotExists() {
        OraclePackageDTO dto = new OraclePackageDTO();
        dto.setId(4L);

        when(repository.findById(4L)).thenReturn(Optional.empty());

        service.update(dto);

        // updateFromDto ve save çağrılmamalı
        verify(mapper, never()).updateFromDto(any(), any());
        verify(repository, never()).save(any());
    }

    @Test
    void delete_shouldCallDeleteById() {
        service.delete(5L);

        verify(repository).deleteById(5L);
    }
}


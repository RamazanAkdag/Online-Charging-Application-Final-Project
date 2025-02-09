package com.ramobeko.accountordermanagement.service.concrete;

import com.ramobeko.accountordermanagement.model.dto.RegisterRequest;
import com.ramobeko.accountordermanagement.model.entity.Customer;
import com.ramobeko.accountordermanagement.repository.ignite.IgniteCustomerRepository;
import com.ramobeko.accountordermanagement.service.abstrct.ignite.IIgniteCustomerService;
import com.ramobeko.accountordermanagement.util.model.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class IgniteCustomerService implements IIgniteCustomerService {

    private final IgniteCustomerRepository igniteCustomerRepository;
    private final PasswordEncoder passwordEncoder;

    public IgniteCustomerService(IgniteCustomerRepository igniteCustomerRepository, PasswordEncoder passwordEncoder) {
        this.igniteCustomerRepository = igniteCustomerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void register(RegisterRequest request) {
        System.out.println("🔥 Registering new customer: " + request.getEmail());

        Customer customer = new Customer();
        customer.setId(System.currentTimeMillis()); // Generate unique ID
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setAddress(request.getAddress());
        customer.setStatus("ACTIVE");
        customer.setStartDate(new Date());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customer.setRole(request.getRole() != null ? request.getRole() : Role.USER);

        igniteCustomerRepository.save(customer.getId(), customer);

        System.out.println("✅ Customer registered successfully: " + customer.getEmail());
    }

    @Override
    public void create(Customer entity) {
        System.out.println("📝 Creating new customer: " + entity.getEmail());
        igniteCustomerRepository.save(entity.getId(), entity);
        System.out.println("✅ Customer created: " + entity.getEmail());
    }

    @Override
    public void update(Customer entity) {
        System.out.println("🔄 Updating customer: " + entity.getId());

        if (igniteCustomerRepository.existsById(entity.getId())) {
            igniteCustomerRepository.save(entity.getId(), entity);
            System.out.println("✅ Customer updated: " + entity.getId());
        } else {
            System.out.println("❌ Customer not found with ID: " + entity.getId());
            throw new RuntimeException("Customer not found with ID: " + entity.getId());
        }
    }

    @Override
    public void delete(Long id) {
        System.out.println("🗑 Deleting customer with ID: " + id);
        igniteCustomerRepository.deleteById(id);
        System.out.println("✅ Customer deleted: " + id);
    }
}

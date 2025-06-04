package com.ramobeko.accountordermanagement.security;

import com.ramobeko.accountordermanagement.model.entity.oracle.OracleCustomer;
import com.ramobeko.accountordermanagement.repository.oracle.OracleCustomerRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private final OracleCustomerRepository customerRepository;

    public UserDetailsServiceImpl(OracleCustomerRepository oracleCustomerRepository) {
        this.customerRepository = oracleCustomerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        OracleCustomer oracleCustomer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                oracleCustomer.getEmail(),
                oracleCustomer.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + oracleCustomer.getRole().name()))
        );
    }
}

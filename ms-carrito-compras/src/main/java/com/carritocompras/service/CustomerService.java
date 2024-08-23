package com.carritocompras.service;

import com.carritocompras.model.Customer;
import com.carritocompras.model.Product;
import com.carritocompras.repository.ICustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class CustomerService {
    private final ICustomerRepository iCustomerRepository;

    public Flux<Customer> getAllCustomers() {
        return iCustomerRepository.findAll();
    }

    public Mono<Customer> getCustomerById(Integer idCustomer) {
        return iCustomerRepository.findById(idCustomer);
    }

    public Mono<Customer> createCustomer(Customer customer) {
        return iCustomerRepository.save(customer);
    }

    public Mono<Customer> updateCustomer(Integer idCustomer, Customer customer) {
        return iCustomerRepository.findById(idCustomer)
                .flatMap(existingCustomer -> {
                    existingCustomer.setFirstName(customer.getFirstName());
                    existingCustomer.setLastName(customer.getLastName());
                    existingCustomer.setPhoneNumber(customer.getPhoneNumber());
                    existingCustomer.setJobType(customer.getJobType());
                    existingCustomer.setEmail(customer.getEmail());
                    existingCustomer.setCountry(customer.getCountry());
                    existingCustomer.setCity(customer.getCity());
                    existingCustomer.setAddress(customer.getAddress());
                    return iCustomerRepository.save(existingCustomer);
                });
    }

    public Mono<Void> deleteCustomer(Integer idCustomer) {
        return iCustomerRepository.deleteById(idCustomer);
    }


}

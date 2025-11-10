package com.customer.service;

import com.customer.dto.CustomerDto;
import com.customer.dto.VehicleDto;
import com.customer.entity.Customer;
import com.customer.entity.Vehicle;
import com.customer.repository.CustomerRepository;
import com.customer.repository.VehicleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerManagementService {

    private final CustomerRepository customerRepository;
    private final VehicleRepository vehicleRepository;

    public CustomerManagementService(CustomerRepository customerRepository, VehicleRepository vehicleRepository) {
        this.customerRepository = customerRepository;
        this.vehicleRepository = vehicleRepository;
    }

    // ### Logic cho CustomerController ###

    /**
     * Lấy thông tin chi tiết cơ bản của một khách hàng.
     */
    @Transactional(readOnly = true)
    public CustomerDto getCustomerDetails(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + customerId));
        return mapToCustomerDto(customer);
    }

    /**
     * Cập nhật thông tin khách hàng.
     * (Giả sử customerId đã tồn tại, được tạo từ AccountService).
     */
    @Transactional
    public CustomerDto updateCustomerDetails(Long customerId, CustomerDto customerDto) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + customerId));

        // Cập nhật các trường có thể thay đổi
        customer.setName(customerDto.getName());
        customer.setEmail(customerDto.getEmail());
        customer.setAddress(customerDto.getAddress());
        customer.setPaymentInfo(customerDto.getPaymentInfo());

        Customer updatedCustomer = customerRepository.save(customer);
        return mapToCustomerDto(updatedCustomer);
    }

    // ### Logic cho VehicleController ###

    /**
     * Thêm một phương tiện mới cho một khách hàng.
     */
    @Transactional
    public VehicleDto addVehicle(VehicleDto vehicleDto) {
        // Tìm khách hàng sở hữu
        Customer customer = customerRepository.findById(vehicleDto.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + vehicleDto.getCustomerId()));

        Vehicle vehicle = new Vehicle();
        vehicle.setVin(vehicleDto.getVin());
        vehicle.setMake(vehicleDto.getMake());
        vehicle.setModel(vehicleDto.getModel());
        vehicle.setYear(vehicleDto.getYear());
        vehicle.setCustomer(customer); // Gán quan hệ

        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return mapToVehicleDto(savedVehicle);
    }

    /**
     * Lấy tất cả phương tiện của một khách hàng cụ thể.
     */
    @Transactional(readOnly = true)
    public List<VehicleDto> getVehiclesForCustomer(Long customerId) {
        List<Vehicle> vehicles = vehicleRepository.findByCustomerCustomerId(customerId);
        return vehicles.stream()
                .map(this::mapToVehicleDto)
                .collect(Collectors.toList());
    }

    /**
     * Cập nhật thông tin của một phương tiện.
     */
    @Transactional
    public VehicleDto updateVehicle(Long vehicleId, VehicleDto vehicleDto) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle not found with id: " + vehicleId));

        vehicle.setVin(vehicleDto.getVin());
        vehicle.setMake(vehicleDto.getMake());
        vehicle.setModel(vehicleDto.getModel());
        vehicle.setYear(vehicleDto.getYear());
        // (Không cho phép thay đổi chủ sở hữu ở đây)

        Vehicle updatedVehicle = vehicleRepository.save(vehicle);
        return mapToVehicleDto(updatedVehicle);
    }


    // ### Các phương thức Mapper (Ánh xạ) private ###

    private CustomerDto mapToCustomerDto(Customer customer) {
        CustomerDto dto = new CustomerDto();
        dto.setCustomerId(customer.getCustomerId());
        dto.setName(customer.getName());
        dto.setEmail(customer.getEmail());
        dto.setAddress(customer.getAddress());
        dto.setPaymentInfo(customer.getPaymentInfo());
        return dto;
    }

    private VehicleDto mapToVehicleDto(Vehicle vehicle) {
        VehicleDto dto = new VehicleDto();
        dto.setVehicleId(vehicle.getVehicleId());
        dto.setVin(vehicle.getVin());
        dto.setMake(vehicle.getMake());
        dto.setModel(vehicle.getModel());
        dto.setYear(vehicle.getYear());
        dto.setCustomerId(vehicle.getCustomer().getCustomerId());
        return dto;
    }
}

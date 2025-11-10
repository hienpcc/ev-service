package com.customer.service;

import com.customer.client.MaintenanceServiceClient;
import com.customer.dto.CustomerProfileDto;
import com.customer.dto.ServiceRecordDto;
import com.customer.dto.VehicleProfileDto;
import com.customer.entity.Customer;
import com.customer.entity.Vehicle;
import com.customer.repository.CustomerRepository;
import com.customer.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerProfileService {

    private final CustomerRepository customerRepository;
    private final VehicleRepository vehicleRepository;
    private final MaintenanceServiceClient maintenanceServiceClient;
    // (Bạn cũng có thể inject một Mapper, ví dụ MapStruct, để code sạch hơn)

    public CustomerProfileService(CustomerRepository customerRepository,
                                  VehicleRepository vehicleRepository,
                                  MaintenanceServiceClient maintenanceServiceClient) {
        this.customerRepository = customerRepository;
        this.vehicleRepository = vehicleRepository;
        this.maintenanceServiceClient = maintenanceServiceClient;
    }

    public CustomerProfileDto getCustomerProfile(Long customerId) {
        // 1. Lấy thông tin Customer
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // 2. Lấy danh sách xe của Customer
        List<Vehicle> vehicles = vehicleRepository.findByCustomerCustomerId(customerId);

        // 3. Với mỗi xe, gọi Maintenance-Service để lấy lịch sử
        List<VehicleProfileDto> vehicleProfiles = vehicles.stream()
                .map(this::mapVehicleToProfile)
                .collect(Collectors.toList());

        // 4. Tổng hợp kết quả
        return mapCustomerToProfile(customer, vehicleProfiles);
    }

    private VehicleProfileDto mapVehicleToProfile(Vehicle vehicle) {
        // 3.1. Gọi API qua Feign Client
        List<ServiceRecordDto> history;
        try {
            history = maintenanceServiceClient.getServiceRecordsByVehicleId(vehicle.getVehicleId());
        } catch (Exception e) {
            // Xử lý lỗi nếu service kia bị sập (ví dụ: trả về list rỗng)
            history = List.of();
        }

        // 3.2. Map dữ liệu
        VehicleProfileDto profileDto = new VehicleProfileDto();
        profileDto.setVehicleId(vehicle.getVehicleId());
        profileDto.setVin(vehicle.getVin());
        profileDto.setMake(vehicle.getMake());
        profileDto.setModel(vehicle.getModel());
        profileDto.setYear(vehicle.getYear());
        profileDto.setServiceHistory(history);
        return profileDto;
    }

    private CustomerProfileDto mapCustomerToProfile(Customer customer, List<VehicleProfileDto> vehicleProfiles) {
        CustomerProfileDto profileDto = new CustomerProfileDto();
        profileDto.setCustomerId(customer.getCustomerId());
        profileDto.setName(customer.getName());
        profileDto.setEmail(customer.getEmail());
        profileDto.setAddress(customer.getAddress());
        profileDto.setVehicles(vehicleProfiles);
        return profileDto;
    }
}

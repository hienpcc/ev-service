import axios from 'axios';

// Giả định base URL cho Customer Service
const CUSTOMER_API_URL = 'http://localhost:8080/api/customer/v1';

/**
 * Lấy danh sách xe của khách hàng hiện tại.
 */
export const fetchCustomerVehicles = async () => {
    try {
        // Endpoint: /customers/me/vehicles
        const response = await axios.get(`${CUSTOMER_API_URL}/customers/me/vehicles`);
        return response.data;
    } catch (error) {
        console.error("Error fetching customer vehicles:", error);
        // Trả về dữ liệu mock
        return [
            { id: 'VEHICLE_001', model: 'VinFast VF e34', vin: 'VF987654321', odometer: 9800, lastServiceDate: '2025-06-01' },
        ];
    }
};
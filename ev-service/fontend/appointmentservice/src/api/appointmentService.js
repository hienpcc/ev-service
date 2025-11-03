const centers = [
  { id: 1, name: "EV Center - Quận 1", address: "12 Nguyễn Huệ, Q1", distance: "2.3km" },
  { id: 2, name: "EV Center - Gò Vấp", address: "45 Phan Văn Trị, Gò Vấp", distance: "6.8km" },
  { id: 3, name: "EV Center - Thủ Đức", address: "88 Võ Văn Ngân, Thủ Đức", distance: "12.1km" },
];

const services = [
  { id: "svc1", name: "Bảo dưỡng định kỳ" },
  { id: "svc2", name: "Thay pin" },
  { id: "svc3", name: "Kiểm tra hệ thống" },
];

export const appointmentService = {
  getCenters: async () => new Promise((resolve) => setTimeout(() => resolve(centers), 400)),
  getServices: async (centerId) => new Promise((resolve) => setTimeout(() => resolve(services), 300)),
  createAppointment: async (booking) => {
    console.log("Thông tin đặt lịch:", booking);
    return new Promise((resolve) => setTimeout(() => resolve({ success: true }), 500));
  },
};

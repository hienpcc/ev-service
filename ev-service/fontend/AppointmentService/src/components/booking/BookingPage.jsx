import React, { useState, useEffect } from "react";
import { appointmentService } from "../../api/appointmentService";

// Component input có label
function Field({ label, children }) {
  return (
    <div className="mb-4">
      <label className="block text-sm font-medium text-gray-700 mb-1">
        {label}
      </label>
      {children}
    </div>
  );
}

// Danh sách trung tâm
function CenterList({ centers, selectedId, onSelect }) {
  return (
    <div className="grid md:grid-cols-3 gap-3">
      {centers.map((c) => (
        <button
          key={c.id}
          onClick={() => onSelect(c)}
          className={`p-4 border rounded-xl text-left transition-all ${
            selectedId === c.id
              ? "border-blue-600 bg-blue-50 shadow-md"
              : "border-gray-200 hover:border-blue-400 hover:bg-gray-50"
          }`}
        >
          <div className="font-semibold text-gray-900">{c.name}</div>
          <div className="text-sm text-gray-500 mt-1">{c.address}</div>
          <div className="text-xs text-blue-600 mt-1">{c.distance}</div>
        </button>
      ))}
    </div>
  );
}

// Danh sách dịch vụ
function ServiceList({ services, selectedIds, onToggle }) {
  if (!services.length)
    return <div className="text-gray-500 italic">Chọn trung tâm để hiển thị dịch vụ...</div>;

  return (
    <div className="grid md:grid-cols-2 gap-3">
      {services.map((s) => (
        <label
          key={s.id}
          className={`flex justify-between items-center p-3 border rounded-xl cursor-pointer transition-all ${
            selectedIds.includes(s.id)
              ? "border-blue-600 bg-blue-50 shadow-sm"
              : "border-gray-200 hover:border-blue-400 hover:bg-gray-50"
          }`}
        >
          <span className="font-medium text-gray-800">{s.name}</span>
          <input
            type="checkbox"
            checked={selectedIds.includes(s.id)}
            onChange={() => onToggle(s.id)}
            className="w-4 h-4 accent-blue-600"
          />
        </label>
      ))}
    </div>
  );
}

// Trang chính
export default function BookingPage() {
  const [centers, setCenters] = useState([]);
  const [selectedCenter, setSelectedCenter] = useState(null);
  const [services, setServices] = useState([]);
  const [selectedServices, setSelectedServices] = useState([]);
  const [customerName, setCustomerName] = useState("");
  const [vehicleModel, setVehicleModel] = useState("");
  const [vin, setVin] = useState("");
  const [date, setDate] = useState("");
  const [time, setTime] = useState("");

  // Lấy danh sách trung tâm
  useEffect(() => {
    appointmentService.getCenters().then(setCenters);
  }, []);

  // Khi chọn trung tâm -> tải dịch vụ
  useEffect(() => {
    if (selectedCenter) {
      appointmentService.getServices(selectedCenter.id).then(setServices);
    } else {
      setServices([]);
      setSelectedServices([]);
    }
  }, [selectedCenter]);

  const toggleService = (id) => {
    setSelectedServices((prev) =>
      prev.includes(id) ? prev.filter((x) => x !== id) : [...prev, id]
    );
  };

  const handleBooking = async () => {
    if (
      !customerName ||
      !vehicleModel ||
      !vin ||
      !selectedCenter ||
      !date ||
      !time ||
      selectedServices.length === 0
    ) {
      alert("⚠️ Vui lòng nhập đầy đủ thông tin!");
      return;
    }

    await appointmentService.createAppointment({
      customerName,
      vehicleModel,
      vin,
      center: selectedCenter,
      services: services.filter((s) => selectedServices.includes(s.id)),
      date,
      time,
    });
    alert("✅ Đặt lịch thành công!");
  };

  // ---- Giao diện chính ----
  return (
    <div className="min-h-screen bg-gray-100 py-10 px-4">
      <div className="max-w-6xl mx-auto bg-white rounded-2xl shadow-lg p-8 space-y-8">
        <h1 className="text-3xl font-bold text-center text-blue-700 mb-6">
          Đặt lịch dịch vụ EV
        </h1>

        <div className="grid lg:grid-cols-3 gap-8">
          {/* === Cột trái: Trung tâm + khách hàng === */}
          <div className="space-y-6">
            <div className="bg-gray-50 p-5 rounded-xl border">
              <h2 className="text-lg font-semibold text-gray-800 mb-3">
                Chọn trung tâm
              </h2>
              <CenterList
                centers={centers}
                selectedId={selectedCenter?.id}
                onSelect={setSelectedCenter}
              />
            </div>

            <div className="bg-gray-50 p-5 rounded-xl border">
              <h2 className="text-lg font-semibold text-gray-800 mb-3">
                Thông tin khách hàng
              </h2>

              <Field label="Họ tên">
                <input
                  value={customerName}
                  onChange={(e) => setCustomerName(e.target.value)}
                  className="w-full p-2 border rounded-md focus:ring-2 focus:ring-blue-500 focus:outline-none"
                />
              </Field>

              <Field label="Mẫu xe">
                <input
                  value={vehicleModel}
                  onChange={(e) => setVehicleModel(e.target.value)}
                  className="w-full p-2 border rounded-md focus:ring-2 focus:ring-blue-500 focus:outline-none"
                />
              </Field>

              <Field label="Mã VIN">
                <input
                  value={vin}
                  onChange={(e) => setVin(e.target.value)}
                  className="w-full p-2 border rounded-md focus:ring-2 focus:ring-blue-500 focus:outline-none"
                />
              </Field>
            </div>
          </div>

          {/* === Cột phải: Dịch vụ + thời gian === */}
          <div className="lg:col-span-2 space-y-6">
            <div className="bg-gray-50 p-5 rounded-xl border">
              <h2 className="text-lg font-semibold text-gray-800 mb-3">
                Chọn dịch vụ
              </h2>
              <ServiceList
                services={services}
                selectedIds={selectedServices}
                onToggle={toggleService}
              />
            </div>

            <div className="bg-gray-50 p-5 rounded-xl border">
              <h2 className="text-lg font-semibold text-gray-800 mb-3">
                Chọn thời gian
              </h2>
              <div className="flex gap-3">
                <input
                  type="date"
                  value={date}
                  onChange={(e) => setDate(e.target.value)}
                  className="p-2 border rounded-md focus:ring-2 focus:ring-blue-500 focus:outline-none"
                />
                <input
                  type="time"
                  value={time}
                  onChange={(e) => setTime(e.target.value)}
                  className="p-2 border rounded-md focus:ring-2 focus:ring-blue-500 focus:outline-none"
                />
              </div>
            </div>

            <div className="flex justify-end">
              <button
                onClick={handleBooking}
                className="px-6 py-3 bg-blue-600 hover:bg-blue-700 text-white rounded-lg shadow-md transition-all"
              >
                Đặt lịch ngay
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}


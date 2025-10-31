import React from 'react';
// (Bạn có thể tạo file CSS riêng cho TripList)
// import styles from './TripList.module.css'; 

// Nhận props từ Cha
const TripList = ({ trips, isLoading, error }) => {
  
  if (isLoading) {
    return <div>Đang tải danh sách chuyến xe... 🚌</div>;
  }

  if (error) {
    return <div style={{ color: 'red' }}>Lỗi: {error}</div>;
  }

  if (trips.length === 0) {
    return <div>Không tìm thấy chuyến xe nào phù hợp.</div>;
  }

  // Nếu có dữ liệu, render danh sách
  return (
    <div className="trip-list-container">
      <h2>Kết quả tìm kiếm ({trips.length} chuyến)</h2>
      {trips.map((trip) => (
        <div key={trip.id} className="trip-item">
          <h3>{trip.origin} → {trip.destination}</h3>
          <p>Giờ khởi hành: {trip.departureTime}</p>
          <p>Giá vé: {trip.price} VNĐ</p>
          <button>Chọn chuyến</button>
        </div>
      ))}
    </div>
  );
};

export default TripList;
import React, { useState } from 'react';
import BusFilter from '../../components/BusFilter/BusFilter';
import TripList from '../../components/TripList/TripList';
import apiClient from '../../api/api'; // Import file api.js của bạn

const SearchPage = () => {
  // 1. Cha quản lý state của kết quả, loading và lỗi
  const [trips, setTrips] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);

  // 2. Cha định nghĩa hàm gọi API
  const handleSearch = async (searchParams) => {
    console.log('SearchPage nhận được dữ liệu:', searchParams);
    
    // Bắt đầu quá trình tải
    setIsLoading(true);
    setError(null);
    setTrips([]); // Xóa kết quả cũ

    try {
      // Gọi API thật sự
      const response = await apiClient.post('/search-trips', searchParams);
      
      // Giả sử API trả về một mảng các chuyến đi
      // (Trong ví dụ này, response đã là data do interceptor)
      setTrips(response || []); 
    
    } catch (err) {
      // Xử lý lỗi
      setError(err.message || 'Đã có lỗi xảy ra khi tìm kiếm.');
    } finally {
      // Dù thành công hay thất bại, cũng dừng loading
      setIsLoading(false);
    }
  };

  return (
    <div className="search-page-container">
      
      {/* Truyền hàm `handleSearch` và state `isLoading` xuống BusFilter 
      */}
      <BusFilter onSearch={handleSearch} isLoading={isLoading} />
      
      <hr /> {/* Thêm một đường kẻ ngăn cách */}

      {/* Truyền kết quả, loading, lỗi xuống TripList 
      */}
      <TripList trips={trips} isLoading={isLoading} error={error} />

    </div>
  );
};

export default SearchPage;
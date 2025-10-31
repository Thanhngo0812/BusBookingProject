import React, { useState } from 'react';
import PropTypes from 'prop-types'; 
import styles from './BusFilter.module.css'; // File CSS

// Import các thư viện
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css'; 

// THÊM MỚI 1: Import toast
import { toast } from 'react-toastify';

// Import các component/dữ liệu tùy chỉnh
import LocationSelect from './LocationSelect'; 
import { provinceOptions, findProvinceByLabel } from '../../../data/Province'; 

const BusFilter = ({ onSearch, isLoading }) => {
  // --- STATE QUẢN LÝ FORM ---
  
  const [tripType, setTripType] = useState('mot-chieu'); 
  const [origin, setOrigin] = useState(findProvinceByLabel('Bình Thuận'));
  const [destination, setDestination] = useState(findProvinceByLabel('TP. Hồ Chí Minh'));
  const [departureDate, setDepartureDate] = useState(new Date());
  const [returnDate, setReturnDate] = useState(() => {
    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    return tomorrow;
  });
  const [tickets, setTickets] = useState(1); 

  // --- EVENT HANDLERS ---

  const handleDepartureDateChange = (date) => {
    setDepartureDate(date);
    if (tripType === 'khu-hoi' && returnDate && date > returnDate) {
      const nextDay = new Date(date);
      nextDay.setDate(nextDay.getDate() + 1);
      setReturnDate(nextDay); 
    }
  };

  /**
   * Xử lý khi submit form (ĐÃ CẬP NHẬT VỚI VALIDATION)
   */
  const handleSubmit = (event) => {
    event.preventDefault(); 
    
    // --- THÊM MỚI 2: Logic Validation ---
    
    const numTickets = Number(tickets);

    // 1. Kiểm tra Điểm đi/đến (react-select có thể clear)
    if (!origin) {
      toast.error("Vui lòng chọn điểm đi.");
      return; // Dừng hàm
    }
    if (!destination) {
      toast.error("Vui lòng chọn điểm đến.");
      return; // Dừng hàm
    }

    // 2. Kiểm tra số vé
    if (numTickets > 5) {
      toast.warn("Để đặt hơn 5 vé, vui lòng liên hệ tổng đài trực tiếp.");
      return; // Dừng hàm
    }
    if (numTickets < 1) {
      toast.error("Số vé phải ít nhất là 1.");
      return; // Dừng hàm
    }
    if(origin==destination){
      toast.error("Điểm đi không được trùng với điểm đến!")
      return;
    }
    // 3. Kiểm tra ngày khứ hồi
    if (tripType === 'khu-hoi') {
      // So sánh 2 Date object (đã được làm tròn về 0h:0m:0s)
      const depDay = new Date(departureDate.setHours(0, 0, 0, 0));
      const retDay = new Date(returnDate.setHours(0, 0, 0, 0));

      if (retDay < depDay) {
        toast.error("Ngày về không thể sớm hơn ngày đi.");
        return; // Dừng hàm
      }
    }

    // --- Nếu tất cả validation OK, tiếp tục ---

    // Chuẩn hóa dữ liệu để gửi đi
    const searchParams = {
      tripType,
      origin: origin.label,         
      destination: destination.label, 
      departureDate: departureDate.toISOString().split('T')[0], 
      tickets: numTickets,
    };

    if (tripType === 'khu-hoi') {
      searchParams.returnDate = returnDate.toISOString().split('T')[0];
    }

    console.log('Dữ liệu gửi đi:', searchParams);
    
    // Gọi hàm của component cha
    // onSearch(searchParams);
  };

  // --- RENDER ---
  
  return (
    <div className={styles.filterBox}>
      {/* ... (Phần Tabs và các nút) ... */}
      <div className={styles.tabs}>
        <button
          className={`${styles.tab} ${tripType === 'mot-chieu' ? styles.active : ''}`}
          onClick={() => setTripType('mot-chieu')}
          disabled={isLoading}
        >
          Một chiều
        </button>
        <button
          className={`${styles.tab} ${tripType === 'khu-hoi' ? styles.active : ''}`}
          onClick={() => setTripType('khu-hoi')}
          disabled={isLoading}
        >
          Khứ hồi
        </button>
        <a href="#" className={styles.guideLink}>Hướng dẫn mua vé</a>
      </div>

      <form 
        onSubmit={handleSubmit} 
        className={styles.formGrid}
        style={{
          gridTemplateColumns: tripType === 'khu-hoi' 
            ? '2fr 2fr 1.2fr 1.2fr 1fr' 
            : '2fr 2fr 1.5fr 1fr'
        }}
      >
        {/* Điểm đi */}
        <div className={styles.formField}>
          <label>Điểm đi</label>
          <LocationSelect
            options={provinceOptions}
            value={origin}
            onChange={setOrigin}
            placeholder="Chọn điểm đi"
            isDisabled={isLoading}
            // 'required' của HTML không hoạt động tốt với react-select
            // nên chúng ta đã kiểm tra trong handleSubmit
          />
        </div>
        
        {/* Điểm đến */}
        <div className={styles.formField}>
          <label>Điểm đến</label>
          <LocationSelect
            options={provinceOptions}
            value={destination}
            onChange={setDestination}
            placeholder="Chọn điểm đến"
            isDisabled={isLoading}
          />
        </div>

        {/* Ngày đi */}
        <div className={styles.formField}>
          <label>Ngày đi</label>
          <DatePicker
            selected={departureDate} 
            onChange={handleDepartureDateChange}
            dateFormat="dd/MM/yyyy"
            className={styles.datePickerInput} 
            disabled={isLoading}
            required
            minDate={new Date()} 
            // maxDate={tripType === 'khu-hoi' ? returnDate : null} 
          />
        </div>

        {/* Ngày về */}
        {tripType === 'khu-hoi' && (
          <div className={styles.formField}>
            <label>Ngày về</label>
            <DatePicker
              selected={returnDate} 
              onChange={(date) => setReturnDate(date)} 
              dateFormat="dd/MM/yyyy"
              className={styles.datePickerInput}
              disabled={isLoading}
              required 
              minDate={new Date()} 
              />
          </div>
        )}

        {/* Số vé */}
        <div style={{width:'50px'}} className={styles.formField}>
          <label>Số vé</label>
          <input
            type="number"
            min="1"
             // SỬA ĐỔI 3: Thay đổi max từ 10 thành 5
            value={tickets}
            onChange={(e) => setTickets(e.target.value)}
            disabled={isLoading}
            required
            className={styles.numberInput} 
          />
        </div>

        {/* Nút Submit */}
        <button 
          type="submit" 
          className={styles.submitButton}
          disabled={isLoading}
        >
          {isLoading ? 'ĐANG TÌM...' : 'TÌM CHUYẾN XE'}
        </button>
      </form>

      {/* ... (Phần recentSearches) ... */}
      <div className={styles.recentSearches}>
        <strong>Tìm kiếm gần đây</strong>
        <div className={styles.recentList}>
          <div className={styles.recentItem}>
            <span>Bình Thuận - TP. Hồ Chí Minh</span>
            <span>20/10/2025</span>
          </div>
          <div className={styles.recentItem}>
            <span>TP. Hồ Chí Minh - Đà Lạt</span>
            <span>27/05/2025</span>
          </div>
        </div>
      </div>
    </div>
  );
};

// --- PropTypes và DefaultProps ---
BusFilter.propTypes = {
  onSearch: PropTypes.func.isRequired,
  isLoading: PropTypes.bool,
};

BusFilter.defaultProps = {
  isLoading: false,
};

export default BusFilter;
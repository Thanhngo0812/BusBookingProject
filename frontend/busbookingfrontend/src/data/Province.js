/**
 * src/data/provinces.js
 * * Chứa danh sách đầy đủ 63 tỉnh thành Việt Nam
 * theo định dạng { value: '...', label: '...' } để dùng với react-select.
 * Cũng bao gồm các hàm helper để tìm kiếm.
 */

// --- MASTER LIST ---
// Danh sách đầy đủ 63 tỉnh thành
const allProvincesList = [
    // 5 Thành phố Trực thuộc Trung ương
    { value: 'hanoi', label: 'Hà Nội' },
    { value: 'ho-chi-minh', label: 'TP. Hồ Chí Minh' },
    { value: 'hai-phong', label: 'Hải Phòng' },
    { value: 'da-nang', label: 'Đà Nẵng' },
    { value: 'can-tho', label: 'Cần Thơ' },
  
    // 58 Tỉnh
    { value: 'an-giang', label: 'An Giang' },
    { value: 'ba-ria-vung-tau', label: 'Bà Rịa - Vũng Tàu' },
    { value: 'bac-giang', label: 'Bắc Giang' },
    { value: 'bac-kan', label: 'Bắc Kạn' },
    { value: 'bac-lieu', label: 'Bạc Liêu' },
    { value: 'bac-ninh', label: 'Bắc Ninh' },
    { value: 'ben-tre', label: 'Bến Tre' },
    { value: 'binh-dinh', label: 'Bình Định' },
    { value: 'binh-duong', label: 'Bình Dương' },
    { value: 'binh-phuoc', label: 'Bình Phước' },
    { value: 'binh-thuan', label: 'Bình Thuận' },
    { value: 'ca-mau', label: 'Cà Mau' },
    { value: 'cao-bang', label: 'Cao Bằng' },
    { value: 'dak-lak', label: 'Đắk Lắk' },
    { value: 'dak-nong', label: 'Đắk Nông' },
    { value: 'dien-bien', label: 'Điện Biên' },
    { value: 'dong-nai', label: 'Đồng Nai' },
    { value: 'dong-thap', label: 'Đồng Tháp' },
    { value: 'gia-lai', label: 'Gia Lai' },
    { value: 'ha-giang', label: 'Hà Giang' },
    { value: 'ha-nam', label: 'Hà Nam' },
    { value: 'ha-tinh', label: 'Hà Tĩnh' },
    { value: 'hai-duong', label: 'Hải Dương' },
    { value: 'hau-giang', label: 'Hậu Giang' },
    { value: 'hoa-binh', label: 'Hòa Bình' },
    { value: 'hung-yen', label: 'Hưng Yên' },
    { value: 'khanh-hoa', label: 'Khánh Hòa' },
    { value: 'kien-giang', label: 'Kiên Giang' },
    { value: 'kon-tum', label: 'Kon Tum' },
    { value: 'lai-chau', label: 'Lai Châu' },
    { value: 'lam-dong', label: 'Lâm Đồng' },
    { value: 'lang-son', label: 'Lạng Sơn' },
    { value: 'lao-cai', label: 'Lào Cai' },
    { value: 'long-an', label: 'Long An' },
    { value: 'nam-dinh', label: 'Nam Định' },
    { value: 'nghe-an', label: 'Nghệ An' },
    { value: 'ninh-binh', label: 'Ninh Bình' },
    { value: 'ninh-thuan', label: 'Ninh Thuận' },
    { value: 'phu-tho', label: 'Phú Thọ' },
    { value: 'phu-yen', label: 'Phú Yên' },
    { value: 'quang-binh', label: 'Quảng Bình' },
    { value: 'quang-nam', label: 'Quảng Nam' },
    { value: 'quang-ngai', label: 'Quảng Ngãi' },
    { value: 'quang-ninh', label: 'Quảng Ninh' },
    { value: 'quang-tri', label: 'Quảng Trị' },
    { value: 'soc-trang', label: 'Sóc Trăng' },
    { value: 'son-la', label: 'Sơn La' },
    { value: 'tay-ninh', label: 'Tây Ninh' },
    { value: 'thai-binh', label: 'Thái Bình' },
    { value: 'thai-nguyen', label: 'Thái Nguyên' },
    { value: 'thanh-hoa', label: 'Thanh Hóa' },
    { value: 'thua-thien-hue', label: 'Thừa Thiên Huế' },
    { value: 'tien-giang', label: 'Tiền Giang' },
    { value: 'tra-vinh', label: 'Trà Vinh' },
    { value: 'tuyen-quang', label: 'Tuyên Quang' },
    { value: 'vinh-long', label: 'Vĩnh Long' },
    { value: 'vinh-phuc', label: 'Vĩnh Phúc' },
    { value: 'yen-bai', label: 'Yên Bái' },
  ];
  
  // --- GROUPING ---
  // Định nghĩa các tỉnh/thành phố "phổ biến" (dựa trên value)
  const popularValues = [
    'ho-chi-minh',
    'ha-noi',
    'da-nang',
    'lam-dong', // Đà Lạt
    'khanh-hoa', // Nha Trang
    'ba-ria-vung-tau',
    'binh-thuan', // Phan Thiết
    'can-tho',
    'quang-ninh', // Hạ Long
    'thua-thien-hue', // Huế
    'kien-giang', // Phú Quốc
    'lao-cai', // Sapa
  ];
  
  // Lọc ra 2 danh sách
  const popularProvinces = allProvincesList.filter(p => 
    popularValues.includes(p.value)
  );
  
  const otherProvinces = allProvincesList.filter(p => 
    !popularValues.includes(p.value)
  );
  
  // --- EXPORTS ---
  // Xuất ra danh sách đã nhóm để react-select sử dụng
  export const provinceOptions = [
    {
      label: 'TỈNH/THÀNH PHỐ PHỔ BIẾN',
      options: popularProvinces
    },
    {
      label: 'TẤT CẢ TỈNH/THÀNH PHỐ',
      options: otherProvinces
    }
  ];
  
  /**
   * Hàm helper để tìm một đối tượng tỉnh/thành phố
   * dựa trên 'label' (tên đầy đủ).
   * Dùng để set giá trị khởi tạo cho react-select.
   * @param {string} label - Tên tỉnh/thành phố (ví dụ: "Bình Thuận")
   * @returns {object | undefined} - Object { value: '...', label: '...' }
   */
  export const findProvinceByLabel = (label) => {
      return allProvincesList.find(p => p.label === label);
  }
  
  /**
   * Hàm helper để tìm một đối tượng tỉnh/thành phố
   * dựa trên 'value' (tên mã hóa).
   * @param {string} value - Mã tỉnh/thành phố (ví dụ: "binh-thuan")
   * @returns {object | undefined} - Object { value: '...', label: '...' }
   */
  export const findProvinceByValue = (value) => {
      return allProvincesList.find(p => p.value === value);
  }
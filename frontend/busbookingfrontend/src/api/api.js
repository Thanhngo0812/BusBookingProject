import axios from 'axios';

// 1. Lấy API URL từ biến môi trường (environment variable)
// Giúp bạn có thể dùng URL khác nhau cho môi trường dev và production
const API_BASE_URL = process.env.REACT_APP_API_URL || 'https://api.yourdomain.com/v1';

// 2. Tạo một instance Axios
const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000, // Thời gian chờ (timeout) 10 giây
  headers: {
    'Content-Type': 'application/json',
    // Bạn cũng có thể thêm các header cố định khác ở đây
  },
});

// 3. Cấu hình Interceptors (Rất quan trọng!)

// Request Interceptor: Chạy TRƯỚC KHI request được gửi đi
apiClient.interceptors.request.use(
  (config) => {
    // Lấy token (ví dụ: từ localStorage hoặc Context)
    const token = localStorage.getItem('authToken');
    
    // Nếu có token, gắn nó vào header Authorization
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    
    return config; // Trả về config đã chỉnh sửa
  },
  (error) => {
    // Xử lý lỗi khi thiết lập request
    return Promise.reject(error);
  }
);

// Response Interceptor: Chạy SAU KHI nhận được response
apiClient.interceptors.response.use(
  (response) => {
    // Bất kỳ mã trạng thái nào nằm trong phạm vi 2xx đều vào đây
    // Bạn có thể xử lý/biến đổi dữ liệu response trước khi trả về
    return response.data; // Thường chỉ trả về 'data' cho gọn
  },
  (error) => {
    // Bất kỳ mã trạng thái nào nằm ngoài phạm vi 2xx đều vào đây
    // Đây là nơi tuyệt vời để xử lý lỗi 401 (Unauthorized), 403 (Forbidden)
    // hoặc 500 (Internal Server Error) một cách tập trung.
    
    if (error.response && error.response.status === 401) {
      // Ví dụ: Token hết hạn, xóa token cũ và redirect về trang login
      localStorage.removeItem('authToken');
      window.location.href = '/login'; 
      // (Trong thực tế nên dùng navigate của React Router)
    }

    // Trả về lỗi để các hàm .catch() ở nơi gọi API có thể xử lý tiếp
    return Promise.reject(error);
  }
);

// 4. Export cái instance đã cấu hình
export default apiClient;
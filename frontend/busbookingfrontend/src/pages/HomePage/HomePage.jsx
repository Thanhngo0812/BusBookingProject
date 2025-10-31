import React from 'react';
import BusFilter from '../../components/common/BusFilter/BusFilter';

// Giả sử bạn cũng có component Banner
// import Banner from '../../components/Banner/Banner';

const HomePage = () => {
  return (
    // Sử dụng React.Fragment <>...</> vì không cần thẻ div bọc ngoài
    <>
      {/* Banner (Phần "24 Năm Vững Tin") */}
      <div className="banner-placeholder">
        {/* <Banner /> */}
        <img src="./banner.png" style={{ width: "100%" }} />

      </div>

      {/* Component tìm kiếm chuyến xe */}
      <BusFilter />

      {/* Các component khác của trang chủ như "Khuyến mãi" sẽ ở đây */}
      {/* <Promotions /> */}
    </>
  );
};

export default HomePage;
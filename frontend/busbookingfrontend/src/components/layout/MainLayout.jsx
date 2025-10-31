import React from 'react';
import { Outlet } from 'react-router-dom'; // Quan trọng: Dùng để render các trang con
import Header from '../common/Header/Header';
// import Footer from '../Footer/Footer'; // Bạn có thể thêm Footer ở đây

const MainLayout = () => {
  return (
    <div className="layout">
      <Header />
      
      <main className="container">
        {/* <Outlet /> là nơi nội dung của HomePage (hoặc các page khác) sẽ được hiển thị */}
        <Outlet /> 
      </main>

      {/* <Footer /> */}
    </div>
  );
};

export default MainLayout;
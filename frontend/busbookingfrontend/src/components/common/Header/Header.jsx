import React, { useState } from 'react';
import styles from './Header.module.css';

// Bạn có thể thay thế 'logo.png' bằng đường dẫn đến file logo thật

const Header = () => {
  // THÊM MỚI: State để quản lý việc đóng/mở menu mobile
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);

  // THÊM MỚI: Hàm để đóng menu (dùng khi nhấn vào 1 link)
  const closeMobileMenu = () => {
    setIsMobileMenuOpen(false);
  };

  return (
    <header className={styles.header}>
      {/* Top Bar (sẽ bị ẩn trên mobile) */}
      <div className={styles.topBar}>
        <div className={styles.topLeft}>
          <span>Tải ứng dụng</span>
          <span>Về FUTA Group</span>
        </div>
        <div className={styles.topRight}>
          <button className={styles.authButton}>Đăng nhập/Đăng ký</button>
        </div>
      </div>

      {/* Main Navigation */}
      <nav className={styles.mainNav}>
        <div className={styles.logoContainer}>
          <span className={styles.logoText}>FUTA Bus Lines</span>
        </div>

        {/* THÊM MỚI: Nút Hamburger
          Nút này chỉ hiển thị trên mobile (nhờ CSS)
        */}
        <button
          className={styles.hamburger}
          onClick={() => setIsMobileMenuOpen(!isMobileMenuOpen)}
          aria-label="Mở menu"
        >
          <span /> {/* 3 vạch của icon */}
          <span />
          <span />
        </button>

        {/* SỬA ĐỔI: Thêm class 'mobileMenuOpen' khi state là true
          Điều này sẽ cho CSS biết khi nào cần hiển thị menu
        */}
        <ul 
          className={`${styles.navLinks} ${isMobileMenuOpen ? styles.mobileMenuOpen : ''}`}
        >
          <li onClick={closeMobileMenu}><a href="#" className={styles.active}>TRANG CHỦ</a></li>
          <li onClick={closeMobileMenu}><a href="#">LỊCH TRÌNH</a></li>
          <li onClick={closeMobileMenu}><a href="#">TRA CỨU VÉ</a></li>
          <li onClick={closeMobileMenu}><a href="#">TIN TỨC</a></li>
          <li onClick={closeMobileMenu}><a href="#">HÓA ĐƠN</a></li>
          <li onClick={closeMobileMenu}><a href="#">LIÊN HỆ</a></li>
          <li onClick={closeMobileMenu}><a href="#">VỀ CHÚNG TÔI</a></li>
        </ul>
      </nav>
    </header>
  );
};

export default Header;
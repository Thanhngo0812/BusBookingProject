import { Routes, Route } from 'react-router-dom';

// Import các Layout
import MainLayout from '../components/layout/MainLayout'; 
// (Layout này chứa Header, Footer, và <Outlet />)

// Import các Pages (Views)
import HomePage from '../pages/HomePage/HomePage';
// import HomePage from '../pages/HomePage/HomePage';
// import ProductListPage from '../pages/ProductListPage/ProductListPage';
// import ProductDetailPage from '../pages/ProductDetailPage/ProductDetailPage';
// import ProfilePage from '../pages/ProfilePage/ProfilePage';
// import LoginPage from '../pages/LoginPage/LoginPage';
// import NotFoundPage from '../pages/NotFoundPage/NotFoundPage';

// // Import component bảo vệ route
// import ProtectedRoute from './ProtectedRoute';

const AppRoutes = () => {
  return (
    <Routes>
      {/* Các route sử dụng MainLayout (có Header/Footer) 
        Chúng được "lồng" (nested) bên trong <MainLayout />
      */}
      <Route path="/" element={<MainLayout />}>
        {/* Route cho trang chủ */}
        <Route index element={<HomePage />} /> 
    </Route>
       
    </Routes>
  );
};

export default AppRoutes;


import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Navbar from './components/Common/Navbar/Navbar.jsx';
import Footer from './components/Common/Footer/Footer.jsx';
import HomePage from './components/Pages/Home/HomePage.jsx';
import ProductDetailsPage from './components/Pages/ProductDetails/ProductDetailsPage.jsx';
import { CartProvider } from './components/Context/CartContext.jsx';
import CategoryListPage from './components/Pages/CategoryList/CategoryListPage.jsx';
import CartPage from './components/Pages/Cart/CartPage.jsx';
import RegisterPage from './components/Pages/Register/RegisterPage.jsx';
import LoginPage from './components/Pages/Login/LoginPage.jsx';
import ProfilePage from './components/Pages/Profile/ProfilePage.jsx';
import AddressPage from './components/Pages/Address/AddressPage.jsx';
import AdminPage from './components/Admin/AdminPage/AdminPage.jsx';
import AdminCategoryPage from './components/Admin/AdminCategory/AdminCategoryPage.jsx';
import AddCategory from './components/Admin/AddCategory/AddCategory.jsx';
import EditCategory from './components/Admin/EditCategory/EditCategory.jsx';
import AdminProductPage from './components/Admin/AdminProducts/AdminProductsPage.jsx';
import AddProductPage from './components/Admin/AddProduct/AddProductPage.jsx';
import EditProductPage from './components/Admin/EditProducts/EditProductsPage.jsx';
import AdminOrdersPage from './components/Admin/AdminOrders/AdminOrdersPage.jsx';
import AdminOrderDetailsPage from './components/Admin/AdminOrderDetails/AdminOrderDetailsPage.jsx';
import CategoryProductsPage from './components/Pages/CategoryProducts/CategoryProductsPage.jsx';
import { ProtectedRoute, AdminRoute } from './services/ApiRoute.jsx';

function App() {

  return (
    <>
      <BrowserRouter>
        <CartProvider>
          <Navbar />
          <Routes>
            {/* OUR ROUTES */}
            <Route exact path='/' element={<HomePage />} />
            <Route path='/product/:productId' element={<ProductDetailsPage />} />
            <Route path='/categories' element={<CategoryListPage />} />
            <Route path='/category/:categoryId' element={<CategoryProductsPage />} />
            <Route path='/cart' element={<CartPage />} />
            <Route path='/register' element={<RegisterPage />} />
            <Route path='/login' element={<LoginPage />} />


            {/* <Route path='/profile' element={<ProtectedRoute element={<ProfilePage />} />} />
            <Route path='/add-address' element={<ProtectedRoute element={<AddressPage />} />} />
            <Route path='/edit-address' element={<ProtectedRoute element={<AddressPage />} />} /> */}
            <Route path="/profile" element={<ProtectedRoute><ProfilePage /></ProtectedRoute>} />
            <Route path="/add-address" element={<ProtectedRoute><AddressPage /></ProtectedRoute>} />
            <Route path="/edit-address" element={<ProtectedRoute><AddressPage /></ProtectedRoute>} />


            {/* <Route path='/admin' element={<AdminRoute element={<AdminPage />} />} />
            <Route path='/admin/categories' element={<AdminRoute element={<AdminCategoryPage />} />} />
            <Route path='/admin/add-category' element={<AdminRoute element={<AddCategory />} />} />
            <Route path='/admin/edit-category/:categoryId' element={<AdminRoute element={<EditCategory />} />} />
            <Route path='/admin/products' element={<AdminRoute element={<AdminProductPage />} />} />
            <Route path='/admin/add-product' element={<AdminRoute element={<AddProductPage />} />} />
            <Route path='/admin/edit-product/:productId' element={<AdminRoute element={<EditProductPage />} />} />
            <Route path='/admin/orders' element={<AdminRoute element={<AdminOrdersPage />} />} />
            <Route path='/admin/order-details/:itemId' element={<AdminRoute element={<AdminOrderDetailsPage />} />} /> */}

            <Route path="/admin" element={<AdminRoute><AdminPage /></AdminRoute>} />
            <Route path="/admin/categories" element={<AdminRoute><AdminCategoryPage /></AdminRoute>} />
            <Route path="/admin/add-category" element={<AdminRoute><AddCategory /></AdminRoute>} />
            <Route path="/admin/edit-category/:categoryId" element={<AdminRoute><EditCategory /></AdminRoute>} />
            <Route path="/admin/products" element={<AdminRoute><AdminProductPage /></AdminRoute>} />
            <Route path="/admin/add-product" element={<AdminRoute><AddProductPage /></AdminRoute>} />
            <Route path="/admin/edit-product/:productId" element={<AdminRoute><EditProductPage /></AdminRoute>} />
            <Route path="/admin/orders" element={<AdminRoute><AdminOrdersPage /></AdminRoute>} />
            <Route path="/admin/order-details/:itemId" element={<AdminRoute><AdminOrderDetailsPage /></AdminRoute>} />

          </Routes>
          <Footer />
        </CartProvider>
      </BrowserRouter>
    </>
  )
}

export default App

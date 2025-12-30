import React from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import logo from '../assets/logo.png';   // ✅ LOGO IMPORT
import './Navbar.css';

const Navbar = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { user, logout, isAdmin } = useAuth();

  const isActive = (path) => {
    return location.pathname === path ? 'active' : '';
  };

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav className="navbar">
      <div className="nav-container">

        {/* ✅ LOGO + TEXT */}
        <Link to="/" className="nav-logo">
          <img src={logo} alt="Blue Bus Logo" className="logo-img" />
          <span className="logo-text">Blue Bus</span>
        </Link>

        <ul className="nav-menu">
          <li>
            <Link to="/" className={`nav-link ${isActive('/')}`}>
              Home
            </Link>
          </li>
          <li>
            <Link to="/buses" className={`nav-link ${isActive('/buses')}`}>
              Buses
            </Link>
          </li>
          <li>
            <Link to="/my-bookings" className={`nav-link ${isActive('/my-bookings')}`}>
              My Bookings
            </Link>
          </li>

          {isAdmin && (
            <li>
              <Link to="/admin" className={`nav-link ${isActive('/admin')}`}>
                Admin
              </Link>
            </li>
          )}
        </ul>

        <div className="nav-user">
          <span className="user-name">{user?.name}</span>
          <button onClick={handleLogout} className="logout-btn">
            Logout
          </button>
        </div>

      </div>
    </nav>
  );
};

export default Navbar;

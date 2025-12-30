import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './Home.css';

const Home = () => {
  const navigate = useNavigate();
  const [searchData, setSearchData] = useState({
    source: '',
    destination: '',
    journeyDate: '',
  });

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setSearchData({
      ...searchData,
      [name]: value,
    });
  };

  const handleSearch = (e) => {
    e.preventDefault();
    if (searchData.source && searchData.destination && searchData.journeyDate) {
      navigate('/buses', { state: { searchData } });
    } else {
      alert('Please fill all fields');
    }
  };

  return (
    <div className="home-container">
      <div className="hero-section">
        <div className="hero-content">
          <h1 className="hero-title">Bus Ticket Management System</h1>
          <p className="hero-subtitle">Book your journey with ease and comfort</p>
          
          <div className="search-box">
            <form onSubmit={handleSearch} className="search-form">
              <div className="form-row">
                <div className="form-group">
                  <label htmlFor="source">From</label>
                  <input
                    type="text"
                    id="source"
                    name="source"
                    placeholder="Enter source city"
                    value={searchData.source}
                    onChange={handleInputChange}
                    required
                  />
                </div>
                
                <div className="form-group">
                  <label htmlFor="destination">To</label>
                  <input
                    type="text"
                    id="destination"
                    name="destination"
                    placeholder="Enter destination city"
                    value={searchData.destination}
                    onChange={handleInputChange}
                    required
                  />
                </div>
                
                <div className="form-group">
                  <label htmlFor="journeyDate">Date</label>
                  <input
                    type="date"
                    id="journeyDate"
                    name="journeyDate"
                    value={searchData.journeyDate}
                    onChange={handleInputChange}
                    min={new Date().toISOString().split('T')[0]}
                    required
                  />
                </div>
                
                <button type="submit" className="search-btn">
                  Search Buses
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>

      <div className="features-section">
        <div className="container">
          <h2 className="section-title">Why Choose Us?</h2>
          <div className="features-grid">
            <div className="feature-card">
              <div className="feature-icon">🚌</div>
              <h3>Wide Network</h3>
              <p>Connect to multiple cities across the country</p>
            </div>
            <div className="feature-card">
              <div className="feature-icon">💰</div>
              <h3>Best Prices</h3>
              <p>Competitive pricing for all routes</p>
            </div>
            <div className="feature-card">
              <div className="feature-icon">✅</div>
              <h3>Easy Booking</h3>
              <p>Simple and quick booking process</p>
            </div>
            <div className="feature-card">
              <div className="feature-icon">🔄</div>
              <h3>Easy Cancellation</h3>
              <p>Cancel your bookings anytime</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Home;


import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { busAPI } from '../services/api';
import './BusList.css';

const BusList = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const [buses, setBuses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const searchData = location.state?.searchData;

  useEffect(() => {
    fetchBuses();
  }, []);

  const fetchBuses = async () => {
    try {
      setLoading(true);
      let response;
      if (searchData) {
        response = await busAPI.searchBuses(searchData);
      } else {
        response = await busAPI.getAllBuses();
      }
      setBuses(response.data);
      setError(null);
    } catch (err) {
      setError(err.response?.data?.error || 'Failed to fetch buses');
      console.error('Error fetching buses:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleBookNow = (busId) => {
    navigate(`/booking/${busId}`);
  };

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', {
      weekday: 'short',
      year: 'numeric',
      month: 'short',
      day: 'numeric',
    });
  };

  const formatTime = (timeString) => {
    const [hours, minutes] = timeString.split(':');
    const date = new Date();
    date.setHours(parseInt(hours), parseInt(minutes));
    return date.toLocaleTimeString('en-US', {
      hour: '2-digit',
      minute: '2-digit',
    });
  };

  if (loading) {
    return (
      <div className="bus-list-container">
        <div className="loading-spinner">Loading buses...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="bus-list-container">
        <div className="error-message">{error}</div>
        <button onClick={fetchBuses} className="retry-btn">Retry</button>
      </div>
    );
  }

  return (
    <div className="bus-list-container">
      <div className="container">
        <div className="header-section">
          <h1 className="page-title">Available Buses</h1>
          {searchData && (
            <div className="search-info">
              <span>
                {searchData.source} → {searchData.destination}
              </span>
              <span>{formatDate(searchData.journeyDate)}</span>
            </div>
          )}
        </div>

        {buses.length === 0 ? (
          <div className="no-buses">
            <p>No buses available for the selected route and date.</p>
            <button onClick={() => navigate('/')} className="back-btn">
              Search Again
            </button>
          </div>
        ) : (
          <div className="buses-grid">
            {buses.map((bus) => (
              <div key={bus.id} className="bus-card">
                <div className="bus-header">
                  <div className="route-info">
                    <div className="route">
                      <span className="city">{bus.source}</span>
                      <span className="arrow">→</span>
                      <span className="city">{bus.destination}</span>
                    </div>
                    <div className="datetime">
                      <span className="date">{formatDate(bus.journeyDate)}</span>
                      <span className="time">{formatTime(bus.journeyTime)}</span>
                    </div>
                  </div>
                </div>

                <div className="bus-details">
                  <div className="detail-item">
                    <span className="label">Available Seats:</span>
                    <span className={`value ${bus.availableSeats < 5 ? 'low' : ''}`}>
                      {bus.availableSeats} / {bus.totalSeats}
                    </span>
                  </div>
                  <div className="detail-item">
                    <span className="label">Fare per Seat:</span>
                    <span className="value fare">₹{bus.farePerSeat}</span>
                  </div>
                </div>

                <div className="bus-footer">
                  <button
                    onClick={() => handleBookNow(bus.id)}
                    className="book-btn"
                    disabled={bus.availableSeats === 0}
                  >
                    {bus.availableSeats === 0 ? 'Sold Out' : 'Book Now'}
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default BusList;


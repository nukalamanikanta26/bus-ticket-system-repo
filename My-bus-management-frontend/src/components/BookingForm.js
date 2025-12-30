import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { busAPI, bookingAPI } from '../services/api';
import './BookingForm.css';

const BookingForm = () => {
  const { busId } = useParams();
  const navigate = useNavigate();
  const { user } = useAuth();
  const [bus, setBus] = useState(null);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [formData, setFormData] = useState({
    numberOfSeats: 1,
  });

  useEffect(() => {
    fetchBusDetails();
  }, [busId]);

  const fetchBusDetails = async () => {
    try {
      const response = await busAPI.getBusById(busId);
      setBus(response.data);
    } catch (err) {
      alert(err.response?.data?.error || 'Failed to fetch bus details');
      navigate('/buses');
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: name === 'numberOfSeats' ? parseInt(value) || 1 : value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    // Check if user is authenticated
    const token = localStorage.getItem('token');
    if (!token) {
      alert('Please login to make a booking');
      navigate('/login');
      return;
    }
    
    if (formData.numberOfSeats > bus.availableSeats) {
      alert(`Only ${bus.availableSeats} seats available`);
      return;
    }

    setSubmitting(true);
    try {
      const bookingData = {
        busId: parseInt(busId),
        numberOfSeats: formData.numberOfSeats,
      };
      
      const response = await bookingAPI.createBooking(bookingData);
      alert('Booking successful! Your booking ID is: ' + response.data.id);
      navigate('/my-bookings');
    } catch (err) {
      console.error('Booking error:', err);
      const errorMessage = err.response?.data?.error || err.message || 'Failed to create booking';
      // Don't redirect if it's a 401 - the interceptor will handle it
      if (err.response?.status !== 401) {
        alert(errorMessage);
      }
    } finally {
      setSubmitting(false);
    }
  };

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', {
      weekday: 'long',
      year: 'numeric',
      month: 'long',
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

  const calculateTotalFare = () => {
    if (!bus) return 0;
    return bus.farePerSeat * formData.numberOfSeats;
  };

  if (loading) {
    return (
      <div className="booking-container">
        <div className="loading-spinner">Loading...</div>
      </div>
    );
  }

  if (!bus) {
    return null;
  }

  return (
    <div className="booking-container">
      <div className="container">
        <h1 className="page-title">Book Your Ticket</h1>
        
        <div className="booking-layout">
          <div className="bus-summary-card">
            <h2>Journey Details</h2>
            <div className="summary-content">
              <div className="summary-row">
                <span className="summary-label">Route:</span>
                <span className="summary-value">
                  {bus.source} → {bus.destination}
                </span>
              </div>
              <div className="summary-row">
                <span className="summary-label">Date:</span>
                <span className="summary-value">{formatDate(bus.journeyDate)}</span>
              </div>
              <div className="summary-row">
                <span className="summary-label">Time:</span>
                <span className="summary-value">{formatTime(bus.journeyTime)}</span>
              </div>
              <div className="summary-row">
                <span className="summary-label">Available Seats:</span>
                <span className="summary-value">{bus.availableSeats}</span>
              </div>
              <div className="summary-row">
                <span className="summary-label">Fare per Seat:</span>
                <span className="summary-value">₹{bus.farePerSeat}</span>
              </div>
            </div>
          </div>

          <div className="booking-form-card">
            <h2>Passenger Information</h2>
            <div className="user-info">
              <div className="info-row">
                <span className="info-label">Name:</span>
                <span className="info-value">{user?.name}</span>
              </div>
              <div className="info-row">
                <span className="info-label">Email:</span>
                <span className="info-value">{user?.email}</span>
              </div>
              <div className="info-row">
                <span className="info-label">Phone:</span>
                <span className="info-value">{user?.phone || 'N/A'}</span>
              </div>
            </div>
            <form onSubmit={handleSubmit} className="booking-form">
              <div className="form-group">
                <label htmlFor="numberOfSeats">Number of Seats *</label>
                <input
                  type="number"
                  id="numberOfSeats"
                  name="numberOfSeats"
                  value={formData.numberOfSeats}
                  onChange={handleInputChange}
                  required
                  min="1"
                  max={bus.availableSeats}
                  placeholder="Enter number of seats"
                />
                <small>Maximum {bus.availableSeats} seats available</small>
              </div>

              <div className="fare-summary">
                <div className="fare-row">
                  <span>Seats:</span>
                  <span>{formData.numberOfSeats} × ₹{bus.farePerSeat}</span>
                </div>
                <div className="fare-row total">
                  <span>Total Fare:</span>
                  <span>₹{calculateTotalFare()}</span>
                </div>
              </div>

              <div className="form-actions">
                <button
                  type="button"
                  onClick={() => navigate(-1)}
                  className="cancel-btn"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  className="submit-btn"
                  disabled={submitting || formData.numberOfSeats > bus.availableSeats}
                >
                  {submitting ? 'Booking...' : 'Confirm Booking'}
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default BookingForm;


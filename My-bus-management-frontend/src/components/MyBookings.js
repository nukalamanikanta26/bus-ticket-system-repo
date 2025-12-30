import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { bookingAPI } from '../services/api';
import './MyBookings.css';

const MyBookings = () => {
  const { user, isAdmin, isAuthenticated } = useAuth();
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (isAuthenticated) {
      fetchBookings();
    } else {
      setError('Please login to view your bookings');
      setLoading(false);
    }
  }, [isAuthenticated]);

  const fetchBookings = async () => {
    try {
      setLoading(true);
      setError(null);
      const response = await bookingAPI.getAllBookings();
      setBookings(response.data);
    } catch (err) {
      console.error('Error fetching bookings:', err);
      // Don't set error if it's a 401 (redirect will happen via interceptor)
      if (err.response?.status !== 401) {
        const errorMessage = err.response?.data?.error || err.message || 'Failed to fetch bookings';
        setError(errorMessage);
      }
      setBookings([]);
    } finally {
      setLoading(false);
    }
  };

  const handleCancelBooking = async (bookingId) => {
    if (!window.confirm('Are you sure you want to cancel this booking?')) {
      return;
    }

    try {
      await bookingAPI.cancelBooking(bookingId);
      alert('Booking cancelled successfully!');
      fetchBookings();
    } catch (err) {
      alert(err.response?.data?.error || 'Failed to cancel booking');
    }
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

  const formatDateTime = (dateTimeString) => {
    const date = new Date(dateTimeString);
    return date.toLocaleString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    });
  };

  return (
    <div className="my-bookings-container">
      <div className="container">
        <h1 className="page-title">{isAdmin ? 'All Bookings' : 'My Bookings'}</h1>

        {error && !loading && (
          <div className="error-message">{error}</div>
        )}

        {loading && (
          <div className="loading-spinner">Loading bookings...</div>
        )}

        {!loading && !error && bookings.length === 0 && (
          <div className="no-bookings">
            <p>No bookings found.</p>
          </div>
        )}

        {!loading && !error && bookings.length > 0 && (
          <div className="bookings-list">
            {bookings.map((booking) => (
              <div
                key={booking.id}
                className={`booking-card ${booking.bookingStatus.toLowerCase()}`}
              >
                <div className="booking-header">
                  <div className="booking-id">
                    <span className="label">Booking ID:</span>
                    <span className="value">#{booking.id}</span>
                  </div>
                  <div className={`status-badge ${booking.bookingStatus.toLowerCase()}`}>
                    {booking.bookingStatus}
                  </div>
                </div>

                <div className="booking-details">
                  <div className="detail-section">
                    <h3>Journey Details</h3>
                    <div className="detail-row">
                      <span className="label">Route:</span>
                      <span className="value">
                        {booking.source} → {booking.destination}
                      </span>
                    </div>
                    {booking.journeyDate && (
                      <div className="detail-row">
                        <span className="label">Journey Date:</span>
                        <span className="value">{formatDate(booking.journeyDate)}</span>
                      </div>
                    )}
                    {booking.journeyTime && (
                      <div className="detail-row">
                        <span className="label">Journey Time:</span>
                        <span className="value">
                          {new Date('2000-01-01T' + booking.journeyTime).toLocaleTimeString('en-US', {
                            hour: '2-digit',
                            minute: '2-digit',
                          })}
                        </span>
                      </div>
                    )}
                  </div>

                  <div className="detail-section">
                    <h3>Passenger Information</h3>
                    <div className="detail-row">
                      <span className="label">Name:</span>
                      <span className="value">{booking.passengerName}</span>
                    </div>
                    <div className="detail-row">
                      <span className="label">Email:</span>
                      <span className="value">{booking.passengerEmail}</span>
                    </div>
                    <div className="detail-row">
                      <span className="label">Phone:</span>
                      <span className="value">{booking.passengerPhone}</span>
                    </div>
                  </div>

                  <div className="detail-section">
                    <h3>Booking Information</h3>
                    <div className="detail-row">
                      <span className="label">Seats:</span>
                      <span className="value">{booking.numberOfSeats}</span>
                    </div>
                    <div className="detail-row">
                      <span className="label">Total Fare:</span>
                      <span className="value fare">₹{booking.totalFare}</span>
                    </div>
                    <div className="detail-row">
                      <span className="label">Booking Date:</span>
                      <span className="value">{formatDateTime(booking.bookingDate)}</span>
                    </div>
                    {booking.cancellationDate && (
                      <div className="detail-row">
                        <span className="label">Cancelled On:</span>
                        <span className="value cancelled">
                          {formatDateTime(booking.cancellationDate)}
                        </span>
                      </div>
                    )}
                  </div>
                </div>

                {booking.bookingStatus === 'CONFIRMED' && (
                  <div className="booking-actions">
                    <button
                      onClick={() => handleCancelBooking(booking.id)}
                      className="cancel-booking-btn"
                    >
                      Cancel Booking
                    </button>
                  </div>
                )}
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default MyBookings;


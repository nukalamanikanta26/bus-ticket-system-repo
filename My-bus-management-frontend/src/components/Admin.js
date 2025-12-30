import React, { useState, useEffect } from 'react';
import { busAPI } from '../services/api';
import './Admin.css';

const Admin = () => {
  const [buses, setBuses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [editingBus, setEditingBus] = useState(null);
  const [formData, setFormData] = useState({
    source: '',
    destination: '',
    journeyDate: '',
    journeyTime: '',
    totalSeats: '',
    farePerSeat: '',
  });

  useEffect(() => {
    fetchBuses();
  }, []);

  const fetchBuses = async () => {
    try {
      setLoading(true);
      const response = await busAPI.getAllBuses();
      setBuses(response.data);
    } catch (err) {
      alert('Failed to fetch buses');
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const busData = {
        ...formData,
        totalSeats: parseInt(formData.totalSeats),
        farePerSeat: parseFloat(formData.farePerSeat),
      };

      if (editingBus) {
        await busAPI.updateBus(editingBus.id, busData);
        alert('Bus updated successfully!');
      } else {
        await busAPI.createBus(busData);
        alert('Bus created successfully!');
      }

      resetForm();
      fetchBuses();
    } catch (err) {
      alert(err.response?.data?.error || 'Failed to save bus');
    }
  };

  const handleEdit = (bus) => {
    setEditingBus(bus);
    setFormData({
      source: bus.source,
      destination: bus.destination,
      journeyDate: bus.journeyDate,
      journeyTime: bus.journeyTime,
      totalSeats: bus.totalSeats.toString(),
      farePerSeat: bus.farePerSeat.toString(),
    });
    setShowForm(true);
  };

  const handleDelete = async (busId) => {
    if (!window.confirm('Are you sure you want to delete this bus?')) {
      return;
    }

    try {
      await busAPI.deleteBus(busId);
      alert('Bus deleted successfully!');
      fetchBuses();
    } catch (err) {
      alert(err.response?.data?.error || 'Failed to delete bus');
    }
  };

  const resetForm = () => {
    setFormData({
      source: '',
      destination: '',
      journeyDate: '',
      journeyTime: '',
      totalSeats: '',
      farePerSeat: '',
    });
    setEditingBus(null);
    setShowForm(false);
  };

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', {
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

  return (
    <div className="admin-container">
      <div className="container">
        <div className="admin-header">
          <h1 className="page-title">Bus Management</h1>
          <button onClick={() => setShowForm(!showForm)} className="add-btn">
            {showForm ? 'Cancel' : '+ Add New Bus'}
          </button>
        </div>

        {showForm && (
          <div className="form-card">
            <h2>{editingBus ? 'Edit Bus' : 'Add New Bus'}</h2>
            <form onSubmit={handleSubmit} className="admin-form">
              <div className="form-grid">
                <div className="form-group">
                  <label htmlFor="source">Source *</label>
                  <input
                    type="text"
                    id="source"
                    name="source"
                    value={formData.source}
                    onChange={handleInputChange}
                    required
                    placeholder="Enter source city"
                  />
                </div>

                <div className="form-group">
                  <label htmlFor="destination">Destination *</label>
                  <input
                    type="text"
                    id="destination"
                    name="destination"
                    value={formData.destination}
                    onChange={handleInputChange}
                    required
                    placeholder="Enter destination city"
                  />
                </div>

                <div className="form-group">
                  <label htmlFor="journeyDate">Journey Date *</label>
                  <input
                    type="date"
                    id="journeyDate"
                    name="journeyDate"
                    value={formData.journeyDate}
                    onChange={handleInputChange}
                    required
                    min={new Date().toISOString().split('T')[0]}
                  />
                </div>

                <div className="form-group">
                  <label htmlFor="journeyTime">Journey Time *</label>
                  <input
                    type="time"
                    id="journeyTime"
                    name="journeyTime"
                    value={formData.journeyTime}
                    onChange={handleInputChange}
                    required
                  />
                </div>

                <div className="form-group">
                  <label htmlFor="totalSeats">Total Seats *</label>
                  <input
                    type="number"
                    id="totalSeats"
                    name="totalSeats"
                    value={formData.totalSeats}
                    onChange={handleInputChange}
                    required
                    min="1"
                    placeholder="Enter total seats"
                  />
                </div>

                <div className="form-group">
                  <label htmlFor="farePerSeat">Fare per Seat (₹) *</label>
                  <input
                    type="number"
                    id="farePerSeat"
                    name="farePerSeat"
                    value={formData.farePerSeat}
                    onChange={handleInputChange}
                    required
                    min="0"
                    step="0.01"
                    placeholder="Enter fare per seat"
                  />
                </div>
              </div>

              <div className="form-actions">
                <button type="button" onClick={resetForm} className="cancel-btn">
                  Cancel
                </button>
                <button type="submit" className="submit-btn">
                  {editingBus ? 'Update Bus' : 'Create Bus'}
                </button>
              </div>
            </form>
          </div>
        )}

        {loading ? (
          <div className="loading-spinner">Loading buses...</div>
        ) : (
          <div className="buses-table">
            {buses.length === 0 ? (
              <div className="no-buses">No buses found. Add a new bus to get started.</div>
            ) : (
              <table>
                <thead>
                  <tr>
                    <th>Route</th>
                    <th>Date</th>
                    <th>Time</th>
                    <th>Seats</th>
                    <th>Available</th>
                    <th>Fare</th>
                    <th>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {buses.map((bus) => (
                    <tr key={bus.id}>
                      <td>
                        <strong>{bus.source}</strong> → <strong>{bus.destination}</strong>
                      </td>
                      <td>{formatDate(bus.journeyDate)}</td>
                      <td>{formatTime(bus.journeyTime)}</td>
                      <td>{bus.totalSeats}</td>
                      <td className={bus.availableSeats < 5 ? 'low-seats' : ''}>
                        {bus.availableSeats}
                      </td>
                      <td className="fare">₹{bus.farePerSeat}</td>
                      <td>
                        <div className="action-buttons">
                          <button
                            onClick={() => handleEdit(bus)}
                            className="edit-btn"
                          >
                            Edit
                          </button>
                          <button
                            onClick={() => handleDelete(bus.id)}
                            className="delete-btn"
                          >
                            Delete
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
          </div>
        )}
      </div>
    </div>
  );
};

export default Admin;


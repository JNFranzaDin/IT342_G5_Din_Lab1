import React, { useEffect, useState } from 'react';
import { authService } from '../services/authService';
import './Dashboard.css';

function Dashboard({ onLogout }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadUserData();
  }, []);

  const loadUserData = async () => {
    try {
      const response = await authService.getCurrentUser();
      if (response.success) {
        setUser(response.user);
      } else {
        onLogout();
      }
    } catch (error) {
      console.error('Error loading user:', error);
      onLogout();
    } finally {
      setLoading(false);
    }
  };

  const handleLogout = async () => {
    await authService.logout();
    onLogout();
  };

  if (loading) {
    return (
      <div className="dashboard-container">
        <div className="loading">Loading...</div>
      </div>
    );
  }

  return (
    <div className="dashboard-container">
      <header className="dashboard-header">
        <h1 className="dashboard-title">Dashboard</h1>
        <button onClick={handleLogout} className="btn-logout">
          Logout
        </button>
      </header>

      <div className="dashboard-content">
        <div className="welcome-card">
          <h2 className="welcome-message">Welcome, {user?.fullname}!</h2>
          <p className="welcome-subtitle">
            We're glad to have you here. Your dashboard is ready and you're successfully logged in. 
            Feel free to explore and enjoy your experience with us!
          </p>
        </div>
      </div>
    </div>
  );
}

export default Dashboard;

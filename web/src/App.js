import React, { useState, useEffect } from 'react';
import Login from './components/Login';
import Register from './components/Register';
import Dashboard from './components/Dashboard';
import { authService } from './services/authService';
import './App.css';

function App() {
  const [currentPage, setCurrentPage] = useState('login');
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  useEffect(() => {
    // Check if user is already logged in
    if (authService.isAuthenticated()) {
      setIsAuthenticated(true);
      setCurrentPage('dashboard');
    }
  }, []);

  const handleLoginSuccess = () => {
    setIsAuthenticated(true);
    setCurrentPage('dashboard');
  };

  const handleRegisterSuccess = () => {
    setCurrentPage('login');
  };

  const handleLogout = () => {
    setIsAuthenticated(false);
    setCurrentPage('login');
  };

  // Simple routing based on currentPage state
  const renderPage = () => {
    if (isAuthenticated && currentPage === 'dashboard') {
      return <Dashboard onLogout={handleLogout} />;
    }

    switch (currentPage) {
      case 'register':
        return <Register onRegisterSuccess={handleRegisterSuccess} />;
      case 'login':
      default:
        return <Login onLoginSuccess={handleLoginSuccess} />;
    }
  };

  // Update navigation to use state instead of href
  useEffect(() => {
    const handleClick = (e) => {
      const href = e.target.getAttribute('href');
      if (href === '/login') {
        e.preventDefault();
        setCurrentPage('login');
      } else if (href === '/register') {
        e.preventDefault();
        setCurrentPage('register');
      }
    };

    document.addEventListener('click', handleClick);
    return () => document.removeEventListener('click', handleClick);
  }, []);

  return (
    <div className="App">
      {renderPage()}
    </div>
  );
}

export default App;

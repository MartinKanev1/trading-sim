import React, { useState } from 'react';
import '../styles/ResetAccount.css';
import UserService from '../services/UserService'; 


const ResetAccount = ({ onResetComplete }) => {
  const [statusMessage, setStatusMessage] = useState('');
  const [showConfirm, setShowConfirm] = useState(false);
  

  const handleInitialClick = () => {
    setShowConfirm(true);
    setStatusMessage('');
  };

  const handleConfirmReset = async () => {
    const userId = Number(localStorage.getItem('userId'));

    if (!userId) {
      setStatusMessage('User ID not found. Please log in again.');
      setShowConfirm(false);
      return;
    }

    const result = await UserService.resetAccount(userId);
    if (result) {
      setStatusMessage('Account has been successfully reset.');
     
     if (onResetComplete) {
  onResetComplete(); 
}


     
      
    } else {
      setStatusMessage('Failed to reset account. Please try again.');
    }

    setShowConfirm(false);
  };

  const handleCancel = () => {
    setShowConfirm(false);
    setStatusMessage('');
  };

  return (
    <div className="reset-container">
      <h3>Reset Your Account</h3>
      <p>
        Clicking the button below will reset your virtual account balance to the initial value (e.g., $10,000)
        and clear all current cryptocurrency holdings and transaction history.
      </p>

      {!showConfirm ? (
        <button onClick={handleInitialClick} className="reset-button">
          Reset Account
        </button>
      ) : (
        <div className="confirm-reset">
          <p>Are you sure you want to reset your account?</p>
          <button onClick={handleConfirmReset} className="reset-button">
            Yes, Reset
          </button>
          <button onClick={handleCancel} className="cancel-button">
            Cancel
          </button>
        </div>
      )}

      
    </div>
  );
};

export default ResetAccount;


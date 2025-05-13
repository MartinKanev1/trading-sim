

import React, { useState } from "react";
import "../styles/TradeCalculator.css";

const TradeCalculator = ({ coin, type, onTrade }) => {
  const [coinAmount, setCoinAmount] = useState("");
  const [usdAmount, setUsdAmount] = useState("");

  const price = coin.currentPrice;

  const handleCoinChange = (e) => {
    const value = e.target.value;
    setCoinAmount(value);
    setUsdAmount(value ? (value * price).toFixed(2) : "");
  };

  const handleUsdChange = (e) => {
    const value = e.target.value;
    setUsdAmount(value);
    setCoinAmount(value ? (value / price).toFixed(8) : "");
  };

  const handleConfirm = () => {
    if (!coinAmount || isNaN(coinAmount) || Number(coinAmount) <= 0) {
      alert("Please enter a valid amount.");
      return;
    }

    onTrade(coinAmount);
  };

  return (
    <div className="calculator-container">
      <h3>{type === "buy" ? "Buy" : "Sell"} {coin.name}</h3>

      <div className="calc-input-group">
        <label>{coin.symbol}</label>
        <input
          type="number"
          value={coinAmount}
          onChange={handleCoinChange}
          placeholder="0"
        />
      </div>

      <div className="arrow">⇅</div>

      <div className="calc-input-group">
        <label>USD</label>
        <input
          type="number"
          value={usdAmount}
          onChange={handleUsdChange}
          placeholder="0"
        />
      </div>

      <p className="current-rate">
        1 {coin.symbol} = ${price.toFixed(price < 1 ? 6 : 2)} USD
      </p>

      <button className="confirm-btn" onClick={handleConfirm}>
        Confirm {type}
      </button>
    </div>
  );
};

export default TradeCalculator;


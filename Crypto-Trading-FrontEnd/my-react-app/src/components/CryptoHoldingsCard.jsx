import React from "react";
import { Link } from "react-router-dom";
import "../styles/CryptoHoldingsCard.css";

const CryptoHoldingsCard = ({ name, symbol, price, logoUrl, quantity }) => {
  const safePrice = typeof price === "number" ? price : 0;
  const safeQuantity = typeof quantity === "number" ? quantity : 0;
  const totalValue = safePrice * safeQuantity;

  return (
    


<div className="holdings-card">
  <div className="card-left">
    <img src={logoUrl} alt={`${name} logo`} className="coin-logo" />
    <div className="coin-info">
      <span className="coin-name">{name}</span>
      <span className="coin-symbol">{symbol.toUpperCase()}</span>
    </div>
  </div>

  <div className="price">
    <span className="label-mobile">Price:</span> ${price.toFixed(4)}
  </div>
  <div className="quantity">
    <span className="label-mobile">Quantity:</span> {quantity.toFixed(6)}
  </div>
  <div className="total">
    <span className="label-mobile">Total:</span> ${(price * quantity).toFixed(2)}
  </div>

  <div className="card-right">
    <Link to={`/coin/${symbol}`} className="trade-btn">Trade</Link>
  </div>
</div>


  );
};

export default CryptoHoldingsCard;

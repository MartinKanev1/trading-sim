import React from "react";
import "../styles/CryptoCard.css";
import MiniChart from "../components/MiniChart";
import { Link } from "react-router-dom";



const CryptoCard = ({ rank, name, symbol, price, logoUrl, snapshots }) => {
    return (
      <div className="crypto-table-row">
        <div className="rank-cell">{rank}</div>
  
        <div className="name-cell">
          <img src={logoUrl} alt={`${name} logo`} className="coin-icon" />
          <div className="coin-name">
            <strong>{name}</strong>
            <span className="symbol">{symbol.toUpperCase()}</span>
          </div>
        </div>
  
        
        <div className="price-cell">
  ${price.toLocaleString(undefined, {
    minimumFractionDigits: price < 1 ? 6 : 2,
    maximumFractionDigits: 10
  })}
</div>

  
        <div className="chart-cell">
          <MiniChart data={snapshots} />
        </div>
  
        
        <div className="button-cell">
          <Link to={`/coin/${symbol}`} className="trade-button">
            Trade
          </Link>
        </div>

      </div>
    );
  };
  
  
  
  export default CryptoCard;

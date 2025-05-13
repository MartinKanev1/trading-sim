import React, { useEffect, useState } from "react";
import WalletService from "../services/WalletService";
import CryptoHoldingsCard from "./CryptoHoldingsCard";
import "../styles/HoldingsList.css";

const HoldingsList = () => {
  const [holdings, setHoldings] = useState([]);

  

  useEffect(() => {
    const userId = localStorage.getItem("userId");

    const fetchData = async () => {
      const data = await WalletService.getUserHoldings(userId);
      setHoldings(data);
    };

    fetchData(); 

    const interval = setInterval(() => {
      fetchData();
    }, 10000); 

     return () => clearInterval(interval); 
  }, []);

  
  return (
  <div className="holdings-container">
    {holdings.length === 0 ? (
      <p className="no-holdings-msg">
        You currently don’t own any cryptocurrencies.
      </p>
    ) : (
      <>
        <h2>Your Crypto Holdings</h2>

        <div className="holdings-header">
          <span>Name</span>
          <span>Price</span>
          <span>Quantity</span>
          <span>Total Value</span>
          <span></span>
        </div>

        {holdings.map((coin) => (
          <CryptoHoldingsCard
            key={coin.symbol}
            name={coin.name}
            symbol={coin.symbol}
            price={coin.currentPrice}
            logoUrl={coin.logoUrl}
            quantity={coin.quantity}
          />
        ))}
      </>
    )}
  </div>
);




  

};

export default HoldingsList;

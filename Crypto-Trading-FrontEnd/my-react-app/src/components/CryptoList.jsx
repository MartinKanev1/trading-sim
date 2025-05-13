import React, { useEffect, useState } from "react";
import CryptoCard from "../components/CryptoCard";
import "../styles/CryptoList.css";
import CryptoService from "../services/CryptoService";

 

const CryptoList = () => {
    const [cryptos, setCryptos] = useState([]);
  
    useEffect(() => {
      const fetchData = async () => {
        const data = await CryptoService.getAllCryptocurrencies();
        setCryptos(data);
      };
    
    fetchData();
    const interval = setInterval(fetchData, 10000); 

  return () => clearInterval(interval); 
    }, []);
  
    return (
      <div className="crypto-table-wrapper">
        <h2>Today's Cryptocurrency Prices</h2>
  
        <div className="crypto-table-header">
          <span>#</span>
          <span>Name</span>
          <span>Price</span>
          <span>7D Chart</span>
          <span></span>
        </div>
        
  
        {cryptos.map((coin) => (
  <CryptoCard
    key={coin.rank}
    rank={coin.rank}
    name={coin.name}
    symbol={coin.symbol}
    price={coin.currentPrice}
    logoUrl={coin.logoUrl}
    snapshots={coin.snapshots} 
  />
))}

      </div>
    );
  };
  
  export default CryptoList;
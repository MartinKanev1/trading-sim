import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import CryptoService from "../services/CryptoService";
import TradingService from "../services/tradingService";
import CoinChart from "../components/CoinChart";
import TradeCalculator from "../components/TradeCalculator";
import "../styles/CoinDetailsPage.css";
import Header from "../components/Header";
import useBalance from "../hooks/useBalance";


const CoinDetailsPage = () => {
  const { symbol } = useParams();
  const [coin, setCoin] = useState(null);
  const [action, setAction] = useState(null);

  const { balance, fetchBalance } = useBalance();
  
  useEffect(() => {
    if (!symbol) return;

    const fetchCoin = async () => {
      const data = await CryptoService.getCoinBySymbol(symbol);
      setCoin(data);
    };

    fetchCoin();
    const interval = setInterval(fetchCoin, 10000);
    return () => clearInterval(interval);
  }, [symbol]);

  const handleTrade = async (quantity) => {
    try {
      const userId = localStorage.getItem("userId"); 

      if (!userId) {
        alert("User not logged in.");
        return;
      }

      let result;
      if (action === "buy") {
        result = await TradingService.buyCrypto(userId, coin.symbol, quantity);
      } else if (action === "sell") {
        result = await TradingService.sellCrypto(userId, coin.symbol, quantity);
      }

      alert(result); 
      setAction(null); 
      await fetchBalance();
    } catch (error) {
      const message = error?.response?.data || "An error occurred during the trade.";
      alert(message);
    }
  };

  if (!coin) return <div>Loading...</div>;

  const formattedPrice = coin.currentPrice.toLocaleString(undefined, {
    minimumFractionDigits: coin.currentPrice < 1 ? 6 : 2,
    maximumFractionDigits: 10,
  });

  return (
    <>
     
      <Header balance={balance} />


      <div className="coin-container">
        <div className="coin-header">
          <img src={coin.logoUrl} alt={coin.name} className="coin-logo" />
          <div className="coin-meta">
            <span className="coin-name">{coin.name}</span>
            <span className="coin-symbol">({coin.symbol})</span>
            <span className="coin-price">${formattedPrice}</span>
          </div>
        </div>

        <div className="coin-layout">
          <div className="chart-box">
            <div className="chart-inner">
              <h3 className="chart-title">{coin.name} 24h Price Chart</h3>
              <CoinChart snapshots={coin.snapshots} />
            </div>
          </div>

          <div className="right-panel">
            <div className="trade-buttons">
              <button onClick={() => setAction("buy")}>Buy</button>
              <button onClick={() => setAction("sell")}>Sell</button>
            </div>

            {action && (
              <TradeCalculator coin={coin} type={action} onTrade={handleTrade} />
            )}
          </div>
        </div>
      </div>
    </>
  );
};

export default CoinDetailsPage;


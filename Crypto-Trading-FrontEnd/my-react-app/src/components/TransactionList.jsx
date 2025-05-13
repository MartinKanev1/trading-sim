import React, { useEffect, useState } from "react";
import transactionService from "../services/TransactionService"; 
import "../styles/TransactionList.css"; 

const TransactionList = ({ userId }) => {
  const [transactions, setTransactions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filterType, setFilterType] = useState("ALL"); 

  useEffect(() => {
    const fetchTransactions = async () => {
      const data = await transactionService.getUserTransactions(userId);
      setTransactions(data);
      setLoading(false);
    };

    fetchTransactions();
  }, [userId]);

  const handleFilterChange = (e) => {
    setFilterType(e.target.value);
  };

  const filteredTransactions = transactions.filter(tx =>
    filterType === "ALL" ? true : tx.type === filterType
  );

  if (loading) return <p>Loading transactions...</p>;
  

  return (
  <div className="transaction-list">
    {transactions.length === 0 ? (
      <p className="no-transactions-msg">
        You don’t have any transaction history yet.
      </p>
    ) : (
      <>
        <h2>Transaction History</h2>

        <div className="filter-container">
          <label htmlFor="filter">Filter by Type: </label>
          <select id="filter" value={filterType} onChange={handleFilterChange}>
            <option value="ALL">All</option>
            <option value="BUY">Buy</option>
            <option value="SELL">Sell</option>
          </select>
        </div>

        <div className="table-wrapper">
          <table className="transaction-table">
            <thead>
              <tr>
                <th>Coin</th>
                <th>Type</th>
                <th>Quantity</th>
                <th>Price</th>
                <th>Total</th>
                <th>Profit/Loss</th>
                <th>Date</th>
              </tr>
            </thead>
            <tbody>
              {filteredTransactions.map((tx) => (
                <tr key={tx.id} className={tx.type === "BUY" ? "buy-row" : "sell-row"}>
                  <td data-label="Coin">{tx.coinSymbol}</td>
                  <td data-label="Type">{tx.type}</td>
                  <td data-label="Quantity">{tx.quantity.toFixed(8)}</td>
                  <td data-label="Price">${tx.priceAtTime.toFixed(2)}</td>
                  <td data-label="Total">${tx.totalValue.toFixed(2)}</td>
                  
                  <td
  data-label="Profit/Loss"
  style={{
    color: tx.profitLoss < 0 ? "red" : "green",
    fontWeight: "500"
  }}
>
  {tx.profitLoss !== null ? `${tx.profitLoss.toFixed(2)}$` : "N/A"}
</td>

                  <td data-label="Date">{new Date(tx.timestamp).toLocaleString()}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </>
    )}
  </div>
);

};

export default TransactionList;


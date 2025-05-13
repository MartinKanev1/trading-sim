import React from "react";
import CryptoList from "../components/CryptoList";
import "../styles/Home.css";
import Header from "../components/Header";
import useBalance from "../hooks/useBalance";


const Home = () => {

  const { balance } = useBalance();
  
  return (
    
    <>
      <Header balance={balance} />

      <CryptoList />
    
    </>
  );
};

export default Home;

import React, { useState } from 'react';
import Header from "../components/Header";
import useBalance from "../hooks/useBalance";
import HoldingsList from "../components/HoldingsList";
import TransactionList from "../components/TransactionList";
import ResetAccount from "../components/ResetAccount";
import { Box } from "@mui/material";

const Holdings = () => {

const { balance} = useBalance();

const userId = localStorage.getItem("userId");

const [resetKey, setResetKey] = useState(0);

  const handleReset = () => {
    setResetKey(prev => prev + 1); 
  };
  
  return (
    
    <>
      <Header balance={balance} />
      
      <Box
        component="main"
        sx={{
          minHeight: 'calc(100vh - 80px)', 
          px: { xs: 2, sm: 3, md: 4 },
          py: { xs: 3, sm: 4, md: 5 },
          backgroundColor: "#f9f9f9", 
        }}
      >
        

        <HoldingsList key={resetKey} />
        <TransactionList key={resetKey} userId={userId} />
        <ResetAccount onResetComplete={handleReset} />
        
        
      </Box>
    </>
  );
};

export default Holdings;


import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Home from "./pages/Home";
import CoinDetailsPage from "./pages/CoinDetailsPage";
import RegisterPage from "./pages/RegisterPage";
import LoginPage from "./pages/LoginPage";
import Holdings from "./pages/UserHoldingsandTransctions";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/home" element={<Home />} />
        <Route path="/coin/:symbol" element={<CoinDetailsPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/holdings-transactions" element={<Holdings />} />

      </Routes>
    </BrowserRouter>
  );
}

export default App;

import { useEffect, useState } from "react";
import UserService from "../services/UserService";

const useBalance = () => {
  const [balance, setBalance] = useState(null);
  const [loading, setLoading] = useState(true);

  const fetchBalance = async () => {
    const userId = localStorage.getItem("userId");
    console.log("🔍 userId from localStorage:", userId);
    
    if (!userId) {
      setLoading(false);
      return;
    }

    setLoading(true);
    const fetched = await UserService.getUserBalance(Number(userId));
    setBalance(fetched);
    setLoading(false);
    console.log("📥 fetched balance:", fetched);
  };

  useEffect(() => {
    fetchBalance();
  }, []);

  return {
    balance,
    loading,
    fetchBalance, 
  };
};

export default useBalance;

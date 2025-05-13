import axios from "axios";

const BASE_URL = "http://localhost:8080/api/cryptocurrencies";

export const getAllCryptocurrencies = async () => {
  const token = localStorage.getItem("jwt");

  try {
    const response = await axios.get(BASE_URL, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    console.error("Failed to fetch cryptocurrencies:", error);
    return [];
  }
};

export const getCoinBySymbol = async (symbol) => {
  const token = localStorage.getItem("jwt");

  try {
    const response = await axios.get(`${BASE_URL}/${symbol}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    console.error("Error fetching coin:", error);
    return null;
  }
};

export default {
    getAllCryptocurrencies,
    getCoinBySymbol
  };
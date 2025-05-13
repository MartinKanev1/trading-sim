import axios from "axios";

const BASE_URL = "http://localhost:8080/api/trade";

const getAuthHeaders = () => {
  const token = localStorage.getItem("jwt");
  return {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  };
};

export const buyCrypto = async (userId, coinSymbol, quantity) => {
  try {
    const response = await axios.post(
      `${BASE_URL}/buy`,
      {
        userId: String(userId),
        coinSymbol,
        quantity: String(quantity),
      },
      getAuthHeaders()
    );
    return response.data;
  } catch (error) {
    console.error("Error buying crypto:", error);
    throw error;
  }
};

export const sellCrypto = async (userId, coinSymbol, quantity) => {
  try {
    const response = await axios.post(
      `${BASE_URL}/sell`,
      {
        userId: String(userId),
        coinSymbol,
        quantity: String(quantity),
      },
      getAuthHeaders()
    );
    return response.data;
  } catch (error) {
    console.error("Error selling crypto:", error);
    throw error;
  }
};

export default {
  buyCrypto,
  sellCrypto,
};
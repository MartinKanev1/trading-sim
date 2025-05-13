
import axios from "axios";

const BASE_URL = "http://localhost:8080/api/transactions";

const getAuthHeaders = () => {
  const token = localStorage.getItem("jwt");
  return {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  };
};

const getUserTransactions = async (userId) => {
  try {
    const response = await axios.get(`${BASE_URL}/user/${userId}`, getAuthHeaders());
    return response.data;
  } catch (error) {
    console.error("Failed to fetch user transactions:", error);
    return [];
  }
};

export default {
  getUserTransactions,
};

import axios from "axios";

const BASE_URL = "http://localhost:8080/api/wallet";

const getAuthHeaders = () => {
  const token = localStorage.getItem("jwt");
  return {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  };
};

const getUserHoldings = async (userId) => {
  try {
    const response = await axios.get(`${BASE_URL}/${userId}/holdings`, getAuthHeaders());
    return response.data;
  } catch (error) {
    console.error("Failed to fetch user holdings:", error);
    return [];
  }
};

export default {
  getUserHoldings,
};

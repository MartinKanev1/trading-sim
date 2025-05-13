import axios from "axios";

const BASE_URL = "http://localhost:8080/api/users";

const getAuthHeaders = () => {
  const token = localStorage.getItem("jwt");
  return {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  };
};


const getUserBalance = async (userId) => {
  try {
    const response = await axios.get(`${BASE_URL}/${userId}/balance`, getAuthHeaders());
    return response.data;
  } catch (error) {
    console.error("Failed to fetch user balance:", error);
    return null;
  }
};


const resetAccount = async (userId) => {
    try {
    const response = await axios.post(`${BASE_URL}/${userId}/reset`, null, getAuthHeaders());

    return response.data;
  } catch (error) {
    console.error("Failed to reset account:", error);
    return null;
  }
};


export default {
  getUserBalance,
  resetAccount,
};

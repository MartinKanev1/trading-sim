import { useState } from "react";
import '../styles/RegisterPage.css';
import registerImage from "../assets/cryptoImage.png";
import { registerUser } from "../services/AuthService"; 
import { Link,useNavigate } from "react-router-dom";


const RegisterPage = () => {
  
    const navigate = useNavigate();
  const [error, setError] = useState("");
  const [formData, setFormData] = useState({
    email: "",
    password: "",
    phoneNumber: "",
    firstName: "",
    lastName: ""
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      const response = await registerUser(formData);
      console.log("Registration successful:", response);
      alert("Registration successful!");
      navigate("/home"); 
    } catch (error) {
      console.error("Registration error:", error);
      setError("Registration failed. Please try again.");
    }
  };

  return (
    <div className="register-page">
      <h1 className="register-welcome">Welcome to CryptoSim – Start Your Journey!</h1>

      <div className="register-wrapper">
        <div className="register-container">
          <h2>Create an Account</h2>
          <form className="register-form" onSubmit={handleSubmit}>
            <input
  type="email"
  name="email"
  placeholder="Email"
  value={formData.email}
  onChange={handleChange}
  required
/>
<input
  type="password"
  name="password"
  placeholder="Password"
  value={formData.password}
  onChange={handleChange}
  required
/>

            <button type="submit">Register</button>
          </form>

          <p className="login-link">
            Already have an account? <Link to="/login">Go to Log in</Link>
          </p>
        </div>

        <div className="register-image">
          <img src={registerImage} alt="Crypto visual" />
        </div>
      </div>
    </div>
  );
};

export default RegisterPage;

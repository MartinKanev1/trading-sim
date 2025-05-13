import { useState } from "react";
import '../styles/RegisterPage.css';
import registerImage from "../assets/cryptoImage.png";
import { loginUser } from "../services/AuthService"; 
import { Link, useNavigate } from "react-router-dom";


const LoginPage = () => {

  const navigate = useNavigate();
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");



  const handleSubmit = async (e) => {
    e.preventDefault();
    

    try {
        const response = await loginUser(email, password);
        console.log("Login successful:", response);
        alert("Login successful!");
        navigate("/home"); 
      } catch (error) {
        console.error("Login error:", error);
        setError("Invalid email or password. Please try again.");
      }
  };

  return (
    <div className="register-page">
        
      <h1 className="register-welcome">Welcome to CryptoSim – Start Your Journey!</h1>

      <div className="register-wrapper">
        <div className="register-container">
          <h2>Log in</h2>
          <form className="register-form" onSubmit={handleSubmit}>
  <input
    type="email"
    placeholder="Email"
    value={email}
    onChange={(e) => setEmail(e.target.value)}
    required
  />
  <input
    type="password"
    placeholder="Password"
    value={password}
    onChange={(e) => setPassword(e.target.value)}
    required
  />
  <button type="submit">Log in</button>
</form>


          <p className="login-link">
            Don't have an account? <Link to="/register">Go to register</Link>
          </p>
        </div>

        <div className="register-image">
          <img src={registerImage} alt="Crypto visual" />
        </div>
      </div>
    </div>
    
  );
};

export default LoginPage;

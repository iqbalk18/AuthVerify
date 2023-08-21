import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.css';
import SignupService from '../service/SignupService';

function Signup() {
  const navigate  = useNavigate();
  const [firstname, setFirstname] = useState('');
  const [lastname, setLastname] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');


  const handleSignup = async (e) => {
    e.preventDefault();

    try {
      const Token = await SignupService.register(firstname, lastname, email, password);
      console.log('Registration successful');
      navigate('/'); 
    } catch (error) {
      console.error('Registration error:', error);
      setError("Registration failed. Please try again.");
    }
  };


  return (
    <div className="container mt-5">
      <div className="row justify-content-center">
        <div className="col-md-6">
          <div className="card">
            <div className="card-body">
              <h3 className="card-title text-center">Sign Up</h3>
              {error && <div className="text-danger">{error}</div>}
              <form>
                <div className="mb-3">
                  <label htmlFor="firstname" className="form-label">First Name</label>
                  <input type="text" className="form-control" id="firstname" value={firstname} onChange={e => setFirstname(e.target.value)} />
                </div>
                <div className="mb-3">
                  <label htmlFor="lastname" className="form-label">Last Name</label>
                  <input type="text" className="form-control" id="lastname" value={lastname} onChange={e => setLastname(e.target.value)} />
                </div>
                <div className="mb-3">
                  <label htmlFor="email" className="form-label">Email or Phone</label>
                  <input type="text" className="form-control" id="email" value={email} onChange={e => setEmail(e.target.value)} />
                </div>
                <div className="mb-3">
                  <label htmlFor="password" className="form-label">Password</label>
                  <input type="password" className="form-control" id="password" value={password} onChange={e => setPassword(e.target.value)} />
                </div>
                <div className="d-grid">
                  <button type="submit" className="btn btn-primary" onClick={handleSignup}>Sign Up</button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Signup;

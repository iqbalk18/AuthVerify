import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.css';
import SignupService from '../service/SignupService';

function Signup() {
  const navigate  = useNavigate();
  const [firstname, setFirstname] = useState('');
  const [lastname, setLastname] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSignup = async (e) => {
    e.preventDefault();

    try {
      setLoading(true); 
      const Token = await SignupService.register(firstname, lastname, email, password);
      console.log('Registration successful');
      setFirstname('');
      setLastname('');
      setEmail('');
      setPassword('');
      setError('');
    } catch (error) {
      console.error('Registration error:', error);
      setError("Registration failed. Please try again.");
    } finally {
      setLoading(false);
    }
  };


  return (
    <div className="container mt-5">
      <div className="row justify-content-center">
        <div className="col-md-6">
          <div className="card">
            <div className="card-body">
              <h3 className="card-title text-center">Sign Up</h3>
              {error && <div className="text-danger text-center">{error}</div>}
              <form>
              <div class="form-floating mb-3">
                  <input type="firstname" class="form-control" id="firstname" placeholder="" value={firstname} onChange={e => setFirstname(e.target.value)} />
                  <label for="floatingInput">First Name</label>
                </div>
                <div class="form-floating mb-3">
                  <input type="lastname" class="form-control" id="lastname" placeholder="" value={lastname} onChange={e => setLastname(e.target.value)} />
                  <label for="floatingInput">Last Name</label>
                </div>
                <div class="form-floating mb-3">
                  <input type="email" class="form-control" id="email" placeholder="" value={email} onChange={e => setEmail(e.target.value)} />
                  <label for="floatingInput">Email or Phone</label>
                </div>
                <div class="form-floating mb-3">
                  <input type="password" class="form-control" id="password" placeholder="" value={password} onChange={e => setPassword(e.target.value)} />
                  <label for="floatingInput">Password</label>
                </div>
                <div className="d-grid">
                  <button type="submit" className="btn btn-primary" onClick={handleSignup} disabled={loading}>
                  {loading ? (
                      <div className="spinner-border" role="status">
                        <span className="visually-hidden">Loading...</span>
                      </div>
                    ) : (
                      'Sign Up'
                    )}
                  </button>
                </div>
                <div className="text-center mt-3">
                  Back to <Link to="/">SignIn</Link>
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

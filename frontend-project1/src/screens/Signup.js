import React from 'react';
import { useNavigate } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.css';

function Signup() {
  const navigate  = useNavigate();

  const handleSignup = () => {
    navigate('/');
  };

  return (
    <div className="container mt-5">
      <div className="row justify-content-center">
        <div className="col-md-6">
          <div className="card">
            <div className="card-body">
              <h3 className="card-title text-center">Sign Up</h3>
              <form>
                <div className="mb-3">
                  <label htmlFor="firstname" className="form-label">First Name</label>
                  <input type="text" className="form-control" id="firstname" />
                </div>
                <div className="mb-3">
                  <label htmlFor="lastname" className="form-label">Last Name</label>
                  <input type="text" className="form-control" id="lastname" />
                </div>
                <div className="mb-3">
                  <label htmlFor="emailorphone" className="form-label">Email or Phone</label>
                  <input type="text" className="form-control" id="emailorphone" />
                </div>
                <div className="mb-3">
                  <label htmlFor="password" className="form-label">Password</label>
                  <input type="password" className="form-control" id="password" />
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

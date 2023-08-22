import React, { useState } from 'react';
import { Link, useNavigate  } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.css';
import SigninService from '../service/SigninService';
import '../styles/General.css';

function Signin() {
    const navigate = useNavigate(); 
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    const handleLogin = async (e) => {
        e.preventDefault();

        try {
            const accessToken = await SigninService.authenticate(username, password);
            navigate('/dashboard'); 
        } catch (error) {
            console.error('Login error:', error);
            setError("Invalid username or password.");
        }
    };

    return (
        <div className="container mt-5">
            <div className="row justify-content-center">
                <div className="col-md-6">
                    <div className="card">
                        <div className="card-body">
                            <h3 className="card-title text-center">Login</h3>
                            {error && <div className="text-danger text-center">{error}</div>}
                            <form>
                            <div class="form-floating mb-3">
                                <input type="email" class="form-control" id="floatingInput" placeholder="name@example.com" value={username} onChange={e => setUsername(e.target.value)} />
                                <label for="floatingInput">Email address</label>
                            </div>
                            <div class="form-floating">
                                <input type="password" class="form-control" id="floatingPassword" placeholder="Password" value={password} onChange={e => setPassword(e.target.value)} />
                                <label for="floatingPassword">Password</label>
                            </div>
                                <div className="d-grid">
                                    <button type="submit" className="btn btn-primary" onClick={handleLogin}>Login</button>
                                </div>
                                <div className="text-center mt-3">
                                    Don't have an account? <Link to="/signup">Sign Up</Link>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Signin;
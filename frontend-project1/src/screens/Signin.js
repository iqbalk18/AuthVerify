import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.css';
import SigninService from '../service/SigninService';

function Signin() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    const handleLogin = async (e) => {
        e.preventDefault();

        try {
            const accessToken = await SigninService.authenticate(username, password);
            // Lakukan navigasi ke halaman setelah login berhasil
            // Contoh: window.location.href = '/dashboard';
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
                            {error && <div className="text-danger">{error}</div>}
                            <form>
                                <div className="mb-3">
                                    <label htmlFor="username" className="form-label">Username</label>
                                    <input type="text" className="form-control" id="username" value={username} onChange={e => setUsername(e.target.value)} />
                                </div>
                                <div className="mb-3">
                                    <label htmlFor="password" className="form-label">Password</label>
                                    <input type="password" className="form-control" id="password" value={password} onChange={e => setPassword(e.target.value)} />
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
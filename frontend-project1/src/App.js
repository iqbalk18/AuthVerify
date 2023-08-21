import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Header from './components/Header';
import Footer from './components/Footer';
import Signup from './screens/Signup';
import Signin from './screens/Signin';

export default function App() {
  return (
    <div className="App" id="main">
      <Router>
      <Header />
        <Routes>
          <Route path="/" element={<Signin />} />
          <Route path="/signup" element={<Signup />} />
        </Routes>
        <Footer />
      </Router>
    </div>
  );
}
import React, { useState } from 'react';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';


function Dashboard() {
  return (
    <nav class="navbar navbar-expand-lg bg-dark ">
      <div class="container-fluid">
        <a class="navbar-brand text-white" href="#">Navbar scroll</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarScroll" aria-controls="navbarScroll" aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarScroll">
          <ul class="navbar-nav me-auto my-2 my-lg-0 navbar-nav-scroll" style={{'--bs-scroll-height': '100px'}}>
            <li class="nav-item">
              <a class="nav-link active text-white" aria-current="page" href="#">Home</a>
            </li>
            <li class="nav-item dropdown">
              <a class="nav-link dropdown-toggle text-white" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                Daily
              </a>
              <ul class="dropdown-menu bg-dark">
                <li><a class="dropdown-item text-white" href="#">Departure</a></li>
                <li><a class="dropdown-item text-white" href="#">Arrival</a></li>
                <li><hr class="dropdown-divider" /></li>
                <li><a class="dropdown-item text-white" href="#">Something else here</a></li>
              </ul>
            </li>
          
          </ul>
          <form class="d-flex" role="search">
            <input class="form-control me-2" type="search" placeholder="Search" aria-label="Search" />
            <button class="btn btn-outline-success" type="submit">Search</button>
          </form>
        </div>
      </div>
    </nav>
  );
}

export default Dashboard;

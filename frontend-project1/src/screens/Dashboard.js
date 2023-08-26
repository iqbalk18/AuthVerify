import React, { useState } from 'react';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import '../styles/Dashboard.css';
import { BsSearch } from 'react-icons/bs';

function Dashboard() {

  const [data, setData] = useState([]);

  const handleAddRow = () => {
    setData([...data, {
      column1: '',
      column2: '',
      column3: '',
      column4: '',
      column5: '',
      column6: '',
      column7: '',
      column8: '',
      column9: '',
      column10: '',
      column11: '',
      column12: '',
      column13: ''
    }]);
  };

  const handleDeleteRow = (index) => {
    const newData = [...data];
    newData.splice(index, 1);
    setData(newData);
  };

  const handleInputChange = (event, index, column) => {
    const { value } = event.target;
    const newData = [...data];
    newData[index][column] = value;
    setData(newData);
  };

  return (
    <div>
      <nav className="navbar navbar-expand-lg">
        <div className="container-fluid">
          <a className="navbar-brand text-white" href="#">
            Navbar scroll
          </a>
          <button
            className="navbar-toggler"
            type="button"
            data-bs-toggle="collapse"
            data-bs-target="#navbarScroll"
            aria-controls="navbarScroll"
            aria-expanded="false"
            aria-label="Toggle navigation"
          >
            <span className="navbar-toggler-icon"></span>
          </button>
          <div className="collapse navbar-collapse" id="navbarScroll">
            <ul
              className="navbar-nav me-auto my-2 my-lg-0 navbar-nav-scroll"
              style={{ '--bs-scroll-height': '100px' }}
            >
              <li className="nav-item">
                <a
                  className="nav-link active text-white"
                  aria-current="page"
                  href="#"
                >
                  Home
                </a>
              </li>
              <li className="nav-item dropdown">
                <a
                  className="nav-link dropdown-toggle text-white"
                  href="#"
                  role="button"
                  data-bs-toggle="dropdown"
                  aria-expanded="false"
                >
                  Daily
                </a>
                <ul className="dropdown-menu">
                  <li>
                    <a className="dropdown-item text-white" href="#">
                      Departure
                    </a>
                  </li>
                  <li>
                    <a className="dropdown-item text-white" href="#">
                      Arrival
                    </a>
                  </li>
                  <li>
                    <hr className="dropdown-divider" />
                  </li>
                  <li>
                    <a className="dropdown-item text-white" href="#">
                      Something else here
                    </a>
                  </li>
                </ul>
              </li>
            </ul>
            <form className="d-flex" role="search">
              <input className="form-control me-2" type="search" placeholder="Search" aria-label="Search" />
              <button className="btn btn-outline-success" type="submit">
                <BsSearch /> Search
              </button>
            </form>
          </div>
        </div>
      </nav>
      <table className="table">
        <thead>
          <tr>
            <th>Travel Number</th>
            <th>Travel Status</th>
            <th>Remark</th>
            <th>Service Type</th>
            <th>Destination</th>
            <th>Departure Date</th>
            <th>Departure Time</th>
            <th>Estimated Departure Date</th>
            <th>Estimated Departure Time</th>
            <th>Actual Departure Date</th>
            <th>Actual Departure Time</th>
            <th>Cancel Date</th>
            <th>Cancel Time</th>
          </tr>
        </thead>
        <tbody>
          {data.map((row, index) => (
            <tr key={index}>
              {Array.from({ length: 13 }).map((_, columnIndex) => (
                <td
                  className={`border-right ${columnIndex < 6 ? '' : 'border-right'}`}
                  key={columnIndex}
                >
                  <input
                    type="text"
                    className="form-control table-input"
                    value={row[`column${columnIndex + 1}`] || ''}
                    onChange={(event) =>
                      handleInputChange(event, index, `column${columnIndex + 1}`)
                    }
                  />
                </td>
              ))}
              <td>
                <button className="btn btn-danger btn-sm"onClick={() => handleDeleteRow(index)}>
                  -
                </button>
              </td>
          </tr>
          ))}
        </tbody>
      </table>
      <button className="btn btn-primary mb-3" onClick={handleAddRow}>Add Row</button>
    </div>
  );
}

export default Dashboard;
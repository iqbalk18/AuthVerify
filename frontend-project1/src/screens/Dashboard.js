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
      column6: ''
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
              <input
                className="form-control me-2"
                type="search"
                placeholder="Search"
                aria-label="Search"
              />
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
            <th>Departure Date</th>
            <th>Departure Time</th>
            <th>Estimated Date</th>
            <th>Estimated Time</th>
            <th>Actual Date</th>
            <th>Actual Time</th>
          </tr>
        </thead>
        <tbody>
        {data.map((row, index) => (
          <tr key={index}>
          <td>
            <input
              type="text"
              className="form-control"
              placeholder={`Data ${index + 1}`}
              value={row.column1}
              onChange={(event) => handleInputChange(event, index, 'column1')}
            />
            </td>
            <td>
                <input
                  type="text"
                  className="form-control"
                  placeholder={`Data ${index + 1}`}
                  value={row.column2}
                  onChange={(event) => handleInputChange(event, index, 'column2')}
                />
              </td>
              <td>
                <input
                  type="text"
                  className="form-control"
                  placeholder={`Data ${index + 1}`}
                  value={row.column3}
                  onChange={(event) => handleInputChange(event, index, 'column3')}
                />
              </td>
              <td>
                <input
                  type="text"
                  className="form-control"
                  placeholder={`Data ${index + 1}`}
                  value={row.column4}
                  onChange={(event) => handleInputChange(event, index, 'column4')}
                />
              </td>
              <td>
                <input
                  type="text"
                  className="form-control"
                  placeholder={`Data ${index + 1}`}
                  value={row.column5}
                  onChange={(event) => handleInputChange(event, index, 'column5')}
                />
              </td>
              <td>
                <input
                  type="text"
                  className="form-control"
                  placeholder={`Data ${index + 1}`}
                  value={row.column6}
                  onChange={(event) => handleInputChange(event, index, 'column6')}
                />
              </td>
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
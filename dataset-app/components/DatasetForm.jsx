import React, { useState, useEffect } from "react";
import axios from "axios";

const DatasetForm = () => {
    const [rows, setRows] = useState([
        { name: "", description: "", records: 0 },
    ]);
    const [datasets, setDatasets] = useState([]);
    const [message, setMessage] = useState("");
const [totalPages, setTotalPages] = useState(0);

const [page, setPage] = useState(0); // Current page
const [size, setSize] = useState(10); // Page size

    const fetchDatasets = async () => {
    try {
        const response = await axios.get(`http://localhost:8080/api/datasets`, {
            params: { page, size },
        });
        setDatasets(response.data.content); // Assuming a paginated API response
        setRows(response.data.content); // Populate form with existing datasets
    } catch (error) {
        console.error("Error fetching datasets", error);
    }
};



    useEffect(() => {
        fetchDatasets();
    }, []);

    const handleChange = (index, e) => {
        const { name, value } = e.target;
        const updatedRows = [...rows];
        updatedRows[index][name] = value;
        setRows(updatedRows);
    };

    const addRow = () => {
        setRows([...rows, { name: "", description: "", records: 0 }]);
    };

    const removeRow = (index) => {
        const updatedRows = rows.filter((_, i) => i !== index);
        setRows(updatedRows);
    };

    const handleChunkedSubmit = async () => {
    const chunkSize = 1000; // Define chunk size
    for (let i = 0; i < rows.length; i += chunkSize) {
        const chunk = rows.slice(i, i + chunkSize);
        try {
            await axios.post("http://localhost:8080/api/datasets/batch", chunk);
        } catch (error) {
            console.error("Error uploading chunk", error);
        }
    }
    setMessage("Datasets uploaded successfully in chunks!");
};


    return (
        <div>
            <h1>Create or Update Datasets</h1>
            <button onClick={fetchDatasets}>Load Existing Datasets</button>
            <form onSubmit={handleChunkedSubmit}>
                <table border="1">
                    <thead>
                        <tr>
                            <th>Name</th>
                            <th>Description</th>
                            <th>Records</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {rows.map((row, index) => (
                            <tr key={index}>
                                <td>
                                    <input
                                        type="text"
                                        name="name"
                                        value={row.name}
                                        onChange={(e) => handleChange(index, e)}
                                        required
                                    />
                                </td>
                                <td>
                                    <input
                                        type="text"
                                        name="description"
                                        value={row.description}
                                        onChange={(e) => handleChange(index, e)}
                                        required
                                    />
                                </td>
                                <td>
                                    <input
                                        type="number"
                                        name="records"
                                        value={row.records}
                                        onChange={(e) => handleChange(index, e)}
                                        required
                                    />
                                </td>
                                <td>
                                    <button type="button" onClick={() => removeRow(index)}>
                                        Remove
                                    </button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
                <button type="button" onClick={addRow}>Add Row</button>
                <button type="submit">Submit</button>
            </form>
            {message && <p>{message}</p>}
        </div>
    );
};

export default DatasetForm;
import React, {Component} from 'react';
import EmployeeService from "../services/EmployeeService";

class ListEmployeeComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            employees: [],
            search: "",
            page: 0,
            totalPages: 0
        }
        this.addEmployee = this.addEmployee.bind(this)
        this.editEmployee = this.editEmployee.bind(this)
        this.deleteEmployee=this.deleteEmployee.bind(this)
        this.changeSearchHandler = this.changeSearchHandler.bind(this)
        this.searchEmployees = this.searchEmployees.bind(this)
    }

    componentDidMount() {
        this.loadEmployees();
    }

    loadEmployees(page = 0) {
        EmployeeService.getEmployees(this.state.search, page)
            .then((res) =>
                this.setState({
                    employees: res.data.content || [],
                    page: res.data.number || 0,
                    totalPages: res.data.totalPages || 0
                })
            )
    }

    addEmployee() {
        this.props.history.push('/add-employee');
    }

    editEmployee(id) {
        this.props.history.push(`/update-employee/${id}`);
    }
    deleteEmployee(id){
    EmployeeService.deleteEmployee(id)
        .then((res)=>
        this.setState({employees:this.state.employees.filter(employee=>employee.id !== id)})
        )
    }
    viewEmployee(id){
        this.props.history.push(`/view-employee/${id}`)

    }

    changeSearchHandler(event) {
        this.setState({search: event.target.value})
    }

    searchEmployees(event) {
        event.preventDefault();
        this.loadEmployees(0);
    }


    render() {
        return (


            <div>
                <h2 className="text-center text-dark ">Employee List</h2>
                <div className="row">
                    <button style={{marginBottom:"20px"}} className="btn btn-secondary border-primary " onClick={this.addEmployee}>Add Employee</button>
                </div>
                <form className="row" onSubmit={this.searchEmployees}>
                    <div className="input-group mb-3">
                        <input
                            className="form-control"
                            placeholder="Search by name or email"
                            value={this.state.search}
                            onChange={this.changeSearchHandler}
                        />
                        <div className="input-group-append">
                            <button className="btn btn-outline-secondary" type="submit">Search</button>
                        </div>
                    </div>
                </form>
                <div className="row">
                    <table className="table table-striped table-bordered">

                        <thead>
                        <tr>
                            <th>Employee First Name</th>
                            <th>Employee Last Name</th>
                            <th>Employee Email Id</th>
                            <th>Actions</th>
                        </tr>
                        </thead>

                        <tbody>
                        {
                            this.state.employees.map(
                                employee =>
                                    <tr key={employee.id}>
                                        <td>{employee.firstName}</td>
                                        <td>{employee.lastName}</td>
                                        <td>{employee.emailId}</td>
                                        <td>
                                            <button onClick={() => this.editEmployee(employee.id)}
                                                    className={"btn btn-info"}>Update
                                            </button>
                                            <button style={{marginLeft:"10px"}} onClick={() => this.deleteEmployee(employee.id)}
                                                    className={"btn btn-danger"}>Delete
                                            </button>
                                            <button style={{marginLeft:"10px"}} onClick={()=>this.viewEmployee(employee.id)}
                                                    className={'btn btn-success'}>View</button>
                                        </td>
                                    </tr>
                            )
                        }
                        </tbody>
                    </table>

                </div>
                <div className="row justify-content-between">
                    <button
                        className="btn btn-outline-secondary"
                        disabled={this.state.page === 0}
                        onClick={() => this.loadEmployees(this.state.page - 1)}
                    >
                        Previous
                    </button>
                    <span>Page {this.state.page + 1} of {Math.max(this.state.totalPages, 1)}</span>
                    <button
                        className="btn btn-outline-secondary"
                        disabled={this.state.page + 1 >= this.state.totalPages}
                        onClick={() => this.loadEmployees(this.state.page + 1)}
                    >
                        Next
                    </button>
                </div>
            </div>
        );
    }
}

export default ListEmployeeComponent;

import axios from 'axios';

const EMPLOYEE_API_BASE_URL = process.env.REACT_APP_EMPLOYEE_API_URL || "http://localhost:8080/api/v1/employees";


class EmployeeService {

    getEmployees(search = "", page = 0, size = 10) {
        return axios.get(EMPLOYEE_API_BASE_URL, {
            params: {
                search,
                page,
                size,
                sort: "id,desc"
            }
        })
    }

    createEmployee(employee) {
        return axios.post(EMPLOYEE_API_BASE_URL, employee)
    }

    getEmployeeById(employeeId) {
        return axios.get(EMPLOYEE_API_BASE_URL + '/' + employeeId);
    }

    updateEmployee(employee, employeeId) {
        return axios.put(EMPLOYEE_API_BASE_URL + '/' + employeeId, employee)
    }
    deleteEmployee(employeeId){
        return axios.delete(EMPLOYEE_API_BASE_URL+'/'+employeeId)
    }

}

export default new EmployeeService()

import axios from "axios";

class ApiService {

    /** BASE URL */
    static BASE_URL = "http://localhost:8081";

    static getHeader() {
        const token = localStorage.getItem("token");
        return {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json"
        };
    }

    /**AUTh && USERS API */

    /** REGISTRATION */
    static async registerUser(registration) {
        const response = await axios.post(`${this.BASE_URL}/auth/register`, registration);
        return response.data;
    }

    /** LOGIN */
    static async loginUser(login) {
        const response = await axios.post(`${this.BASE_URL}/auth/login`, login);
        return response.data;
    }

    /** GET LOGGEDIN USER DETAILS */
    static async loggedinUserDetails() {
        const response = await axios.post(`${this.BASE_URL}/user/loggedInUserInfo`,
            { headers: this.getHeader() }
        );
        return response.data;
    }

} export default ApiService;
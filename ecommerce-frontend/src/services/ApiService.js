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
    static async getLoggedInUserInfo() {
        const response = await axios.post(`${this.BASE_URL}/user/loggedInUserInfo`,
            { headers: this.getHeader() }
        );
        return response.data;
    }

    /** PRODUCT ENDPOINTS */

    /** CREATE PRODUCT */
    static async addProduct(formData) {
        const response = await axios.post(`${this.BASE_URL}/product/createProduct`, formData, {
            headers: {
                ...this.getHeader(),
                "Content-Type": "multipart/form-data"
            }
        });
        return response.data;
    }

    /** UPDATE PRODUCT */
    static async updateProduct(formData) {
        const response = await axios.put(`${this.BASE_URL}/product/createProduct`, formData, {
            headers: {
                ...this.getHeader(),
                "Content-Type": "multipart/form-data"
            }
        });
        return response.data;
    }

    /** GET ALL PRODUCT */
    static async getAllProducts() {
        const response = await axios.get(`${this.BASE_URL}/product/getAllProducts`);
        return response.data;
    }

    /** SEARCH PRODUCT */
    static async searchProducts(searchValue) {
        const response = await axios.get(`${this.BASE_URL}/product/search`, {
            params: { searchValue }
        });
        return response.data;
    }

    /** GET PRODUCT BY PRODUCT ID */
    static async getProductById(productid) {
        const response = await axios.get(`${this.BASE_URL}/product/getProductById/${productid}`);
        return response.data;
    }

    /** GET PRODUCT BY CATEGORY ID */
    static async getAllProductsByCategoryId(categoryId) {
        const response = await axios.get(`${this.BASE_URL}/product/getProductByCategoryId/${categoryId}`);
        return response.data;
    }

    /** GET PRODUCT BY ID */
    static async deleteProduct(productId) {
        const response = await axios.get(`${this.BASE_URL}/product/delete/${productId}`, {
            headers: this.getHeader()
        });
        return response.data;
    }

    /** SEARCH PRODUCT BY NAME */
    static async searchProductsByName(searchValue) {
        const response = await axios.get(`${this.BASE_URL}/product/searchProduct`, {
            params: { searchValue }
        });
        return response.data;
    }

    /** CATEGORY ENDPOINTS */

    /** CCREATE CATEGORY */
    static async createCategory(body) {
        const response = await axios.post(`${this.BASE_URL}/category/createCategory`, body, {
            headers: this.getHeader()
        });
        return response.data;
    }

    /** GET ALL CATEGORIES */
    static async getAllCategory() {
        const response = await axios.get(`${this.BASE_URL}/category/getAllCategories`);
        return response.data;
    }

    /** UPDATE CATEGORY BY ID */
    static async updateCategory(categoryId, body) {
        const response = await axios.put(`${this.BASE_URL}/category/update/${categoryId}`, body);
        return response.data;
    }

    /** GET CATEGORY BY ID */
    static async getCategoryById(categoryId) {
        const response = await axios.get(`${this.BASE_URL}/category/getCategoryById/${categoryId}`);
        return response.data;
    }

    /** DELETE CATEGORY BY ID */
    static async deleteCategory(categoryId) {
        const response = await axios.delete(`${this.BASE_URL}/category/delete/${categoryId}`, {
            headers: this.getHeader()
        });
        return response.data;
    }

    /** ORDER ENDPOINTS */

    /** CREATE ORDER */
    static async createOrder(body) {
        const response = await axios.post(`${this.BASE_URL}/order/create`, body, {
            headers: this.getHeader()
        });
        return response.data;
    }

    /** GET ALL ORDERS */
    static async getAllOrders() {
        const response = await axios.get(`${this.BASE_URL}/order/filter`, {
            headers: this.getHeader()
        });
        return response.data;
    }

    /** GET ORDER ITEM BY ID */
    static async getOrderItemById(itemId) {
        const response = await axios.get(`${this.BASE_URL}/order/filter`, {
            headers: this.getHeader(),
            params: { itemId }
        });
        return response.data;
    }

    /** GET ORDER ITEMS BY STATUS */
    static async getAllOrderItemsByStatus(status) {
        const response = await axios.get(`${this.BASE_URL}/order/filter`, {
            headers: this.getHeader(),
            params: { status }
        });
        return response.data;
    }

    /** GET ORDER STATUS BY ORDER ID */
    static async updateOrderitemStatus(orderItemId, status) {
        const response = await axios.put(`${this.BASE_URL}/order/updateItemStatus/${orderItemId}`, {}, {
            headers: this.getHeader(),
            params: { status }
        });
        return response.data;
    }

    /** ADDRESS ENDPOINTS */

    /** SAVE ADDRESS */
    static async saveAddress(body) {
        const response = await axios.post(`${this.BASE_URL}/address/saveAddress`, body, {
            headers: this.getHeader()
        });
        return response.data;
    }

    /** AUTHENTICATON CHECKER */
    static logout() {
        localStorage.removeItem('token');
        localStorage.removeItem('role');
    }

    static isAuthenticated() {
        const token = localStorage.getItem('token');

        return !!token;
    }

    static isAdmin() {
        const role = localStorage.getItem('role');

        return role == 'ADMIN';
    }

} export default ApiService;
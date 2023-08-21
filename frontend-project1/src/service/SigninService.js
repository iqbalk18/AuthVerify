import axios from 'axios';

class SigninService {
    constructor() {
        this.api = axios.create({
            baseURL: 'http://localhost:8080/api/v1/auth', // Ganti dengan URL backend Anda
        });
    }

    async authenticate(username, password) {
        try {
            const response = await this.api.post('/authenticate', {
                emailOrPhone: username,
                password: password,
            });

            const accessToken = response.data.accessToken;
            localStorage.setItem('accessToken', accessToken);

            return accessToken;
        } catch (error) {
            console.error('Login error:', error);
            throw error;
        }
    }
}

export default new SigninService();

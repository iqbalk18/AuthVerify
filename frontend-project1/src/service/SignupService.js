import axios from 'axios';


class SignupService {
    constructor() {
        this.api = axios.create({
            baseURL: 'http://localhost:8080/api/v1/auth',
        });
    }

    async register(firstname, lastname, email, password, role) {
        try {
            const response = await this.api.post('/register/mail', {
                firstname: firstname,
                lastname: lastname,
                email: email,
                password: password,
                role : role,
            });

            const Token = response.data.Token;
            localStorage.setItem('Token', Token);

            return Token;
        } catch (error) {
            console.error('Register error:', error);
            throw error;
        }
    }
}

export default new SignupService();
import axios from 'axios';

const axiosInstance = axios.create({
    baseURL: 'http://10.100.0.114:8080/api/v1/file/'
});

axiosInstance.interceptors.request.use(config => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

export default axiosInstance;
import Axios, { AxiosInstance, InternalAxiosRequestConfig } from 'axios';
import { useNavigate } from 'react-router-dom';
import { useAuthStore } from '../stores/useAuthStore.ts';

const useAxios = (isAuth: boolean = true): AxiosInstance => {
    const { accessToken, clearAuth } = useAuthStore();
    const navigate = useNavigate();

    const instance = Axios.create({
        baseURL: import.meta.env.VITE_API_BASE_URL,
        withCredentials: true,
    });

    if (isAuth) {
        instance.interceptors.request.use((config: InternalAxiosRequestConfig) => {
            if (!accessToken) {
                navigate('/');
                throw new Axios.Cancel("로그인이 필요한 요청입니다.");
            }
            config.headers = config.headers || {};
            config.headers.Authorization = `Bearer ${accessToken}`;
            return config;
        });
    }

    instance.interceptors.response.use(
        (response) => response,
        async (error) => {
            if (error.response && error.response.status === 401) {
                clearAuth();
                navigate('/');
                return Promise.reject(error);
            }
            return Promise.reject(error);
        },
    );

    return instance;
};

export { useAxios };

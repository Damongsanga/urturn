import { useAuthStore } from '../stores/useAuthStore.ts';
import { useNavigate } from "react-router-dom";

export const useLogout = () => {
    const { clearAuth } = useAuthStore();
    const navigate = useNavigate();
    return () => {
        clearAuth();
        //쿠키 삭제 로직 추가?
        navigate('/');
    };
};

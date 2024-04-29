import {useNavigate} from "react-router-dom";
import {useAuthStore} from "../stores/useAuthStore.ts";
import {useAxios} from "./useAxios.ts";
import {useEffect} from "react";

export const GithubCallback = () =>{
    const navigate = useNavigate();
    const authStore = useAuthStore();
    const axios = useAxios(false);

    useEffect(() => {
        const code =  new URL(document.location.toString()).searchParams.get('code');
        if (authStore.accessToken) {
            console.log("이미 로그인");
            console.log(authStore.accessToken);
            navigate('/main');
            return;
        }
        if (code) {
        axios
            .get('/auth/oauth2/login/github', {params: {"code" : code}})
            .then((response) => {
                console.log(code);
                authStore.setAuth(response.data);
                console.log("로그인 완료");
                navigate('/main');
            })
            .catch((e) => {
                console.error(e);
                console.log(code);
                console.log("실패");
                navigate('/');
            });
    } else {
        console.error("권한 없음");
        navigate('/');
    }
}, []);
    return <></>;
};
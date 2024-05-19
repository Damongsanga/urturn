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
            navigate('/main');
            return;
        }
        if (code) {
        axios
            .get('/auth/oauth2/login/github', {params: {"code" : code}})
            .then((response) => {
                authStore.setAuth(response.data);
                navigate('/main');
            })
            .catch((_e) => {
                //console.error(e);
                navigate('/');
            });
    } else {
        //console.error("권한 없음");
        navigate('/');
    }
}, []);
    return <></>;
};
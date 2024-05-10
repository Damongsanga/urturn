import {useNavigate} from "react-router-dom";
import {useAuthStore} from "../stores/useAuthStore.ts";
import {useAxios} from "./useAxios.ts";
import {useEffect} from "react";

export const GithubRepoCallback = () =>{
    const navigate = useNavigate();
    const authStore = useAuthStore();
    const axios = useAxios(true);

    useEffect(() => {
        const code =  new URL(document.location.toString()).searchParams.get('code');

        if (code) {
        axios
            .get('/github/upload', {params: {"code" : code}})
            .then((response) => {
                console.log("code",code);
                console.log("accesstoken",authStore.accessToken);
                const data = response.data;
                console.log(data);
                console.log("jechul");
                navigate('/main');
            })
            .catch((e) => {
                console.error(e);
                console.log("accesstoken",authStore.accessToken);
                console.log("code: ",code);
                console.log("업로드 실패");
                navigate('/main');
            });
    } else {
        console.error("권한 없음");
        navigate('/');
    }
}, []);
    return <><div>payloadpayloadpayloadpayload</div></>;
};
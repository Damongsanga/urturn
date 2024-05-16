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
                alert("성공적으로 깃허브에 업로드되었습니다. \n마이페이지에서 확인해 주세요.");
                navigate('/myPage');
            })
            .catch((e) => {
                console.error(e);
                console.log("accesstoken",authStore.accessToken);
                console.log("code: ",code);
                alert("리뷰 제출은 성공했으나 깃허브 업로드에 실패했습니다. \n등록하신 레포지토리 명을 확인해 주세요.");
                navigate('/myPage');
            });
    } else {
        console.error("권한 없음");
        navigate('/');
    }
}, []);
    return <><div>깃허브 레포지토리에 회고 등록 중...</div></>;
};
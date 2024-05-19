import {useNavigate} from "react-router-dom";
import {useAxios} from "./useAxios.ts";
import {useEffect} from "react";

export const GithubRepoCallback = () =>{
    const navigate = useNavigate();
    const axios = useAxios(true);

    useEffect(() => {
        const code =  new URL(document.location.toString()).searchParams.get('code');

        if (code) {
        axios
            .get('/github/upload', {params: {"code" : code}})
            .then(() => {
                // alert("성공적으로 깃허브에 업로드되었습니다. \n마이페이지에서 확인해 주세요.");
                navigate('/trans/myPage');
            })
            .catch((e) => {
                console.error(e);
                alert("리뷰 제출은 성공했으나 깃허브 업로드에 실패했습니다. \n등록하신 레포지토리 명을 확인해 주세요.");
                navigate('/myPage');
            });
    } else {
        console.error("권한 없음");
        navigate('/');
    }
}, []);
    return <></>;
};
import { useAuthStore } from "../../stores/useAuthStore"
import { useAxios } from "../../utils/useAxios"
import { useState } from "react"
import { useNavigate } from "react-router-dom";

export const TestLogin = () => {
    const auth = useAuthStore();
    const axi = useAxios(false);
    const [ id, setId ] = useState('');
    const [ pwd, setPwd ] = useState('');
    const navi = useNavigate();

    const forceInjectToken = async () => {
        await axi.post('/auth/test/login', { nickname:id, password:pwd })
        .then(res => {
            auth.setAuth(res.data);
        })
        console.log(auth)
        
        navi('/main');
    }
    
    return (
        <>
        아이디
        <input value={id} onChange={e => setId(e.target.value)}/>
        비번
        <input value={pwd} onChange={e => setPwd(e.target.value)}/>
        <button onClick={forceInjectToken}> 짜치는 테스트 로그인 버튼</button>
        </>
    )
}
import { useEffect, useRef } from "react";
import { useParams } from "react-router-dom";
import { useNavigate } from "react-router-dom";


/*

Transition Page

Path variable: next로 다음 네비게이션 위치를 입력하면 3초간의 전환 화면 후 next 위치로 이동

check, solve, pairsolve, review: 화면 전환 후 즉시 이동

solveSwitch, pairSolveSwitch -> 전환 화면 후 solve, pairsolve로 이동 (전환 애니메이션은 달라야함)

*/

export const TransPage = () => {
    const { next } = useParams();
    const sec  = useRef(0);
    const navigate = useNavigate();

    const START_TIME = 3;

    useEffect(() => {
        sec.current = START_TIME;
        const timer = setInterval(() => {
            sec.current -= 1;
            if (sec.current <= 0) {
                let n = next;
                if(n === "solveSwitch") n = "solve";
                if(n === "pairSolveSwitch") n = "pairsolve";
                navigate("/" + n);
                clearInterval(timer);
            }
        }, 1000)
    }, [])
    
    return (
        <div>
            <h1>{next}</h1>
            <h1>여기에 애니메이션이 들어가야 합니다</h1>
            <h1>애니메이션은 다음 목적지에 따라서 달라져야 합니다.</h1>
        </div>
    )
}
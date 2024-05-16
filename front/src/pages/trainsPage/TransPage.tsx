import { useEffect, useRef } from 'react';
import { useParams } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';
import { Header, Image } from 'semantic-ui-react';

import running from '../../assets/images/running.gif';
import pairRunning from '../../assets/images/pair_running.gif';
import review from '../../assets/images/review.gif'
import solved from '../../assets/images/solved.gif'
import ready from '../../assets/images/ready.gif'
import start from '../../assets/images/start.gif'

import './TransPage.css';
import { useRoomStore } from '../../stores/room';

/*

Transition Page

Path variable: next로 다음 네비게이션 위치를 입력하면 3초간의 전환 화면 후 next 위치로 이동

check, solve, pairsolve, review: 화면 전환 후 즉시 이동
메인 -> check -> solve -> pairsolve - > review (1)

solve -> solve / pairsolve -> pairsolve (2)

solveSwitch, pairSolveSwitch -> 전환 화면 후 solve, pairsolve로 이동 (전환 애니메이션은 달라야함)

*/

export const TransPage = () => {
	const roomStore = useRoomStore();

	const { next } = useParams();
	const sec = useRef(0);
	const navigate = useNavigate();

	const START_TIME = 3;

	useEffect(() => {
        sec.current = START_TIME;

				roomStore.setConsole("");

				if(next === 'solve') {
					sec.current = 4;
				}

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

	const contentArray = [
		{
			key: 'check',
			message: '문제 풀이를 시작합니다.',
			detailMessage: '잠시 후 문제 확인 페이지로 이동합니다.',
			image: start,
		},
		{
			key: 'solve',
			message: '준비가 완료되었습니다.',
			detailMessage: '잠시 후 문제 풀이 화면으로 이동합니다.',
			image: ready,
		},
		{
			key: 'pairsolve',
			message: '문제 하나를 해결하였습니다.',
			detailMessage: '잠시 후 페어 프로그래밍 모드로 변환합니다.',
			image: solved,
		},
		{
			key: 'review',
			message: '문제 풀이가 종료되었습니다.',
			detailMessage: '잠시 후 회고 페이지로 이동합니다.',
			image: review,
		},
		{
			key: 'solveSwitch',
			message: '라운드가 종료되었습니다.',
			detailMessage: '잠시 후 다음 문제로 이동합니다.',
			image: running,
		},
		{
			key: 'pairSolveSwitch',
			message: '라운드가 종료되었습니다.',
			detailMessage: '잠시 후 역할이 교체됩니다.',
			image: pairRunning,
		},
	];

	return (
		<>
			<div className='TransPage'>
				<div className='TransPageEntire'>
					{contentArray
						.filter((item) => item.key === next)
						.map((item, index) => (
							// 상단 메시지
							<div key={index} className='TransAlign'>
								<div className='TransPageTitle' style={{ marginBottom: '5vh' }}>
									<Header as='h3' textAlign='center'>
										{item.message}
									</Header>
								</div>
								{/* 이미지 */}
								{item.image && (
									<div style={{ width: '20vw', height: 'auto' }}>
										<Image src={item.image} style={{ width: '20vw', height: 'auto' }}></Image>
									</div>
								)}
								{/* 자세한 메시지 */}
								<div className='TransPageContent' style={{ marginTop: '5vh' }}>
									<Header as='h3' textAlign='center'>
										{item.detailMessage}
									</Header>
								</div>
							</div>
						))}
				</div>
			</div>
		</>
	);
};

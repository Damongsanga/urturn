import { useEffect, useRef } from 'react';
import { gsap } from 'gsap';
import { Header, Image } from 'semantic-ui-react';
import './SecondLanding.css';

import pair from '../../../../assets/images/pair.png';

interface LandingProp {
	isActive?: boolean;
}

export const SecondLandingPage = ({ isActive }: LandingProp) => {
	const refs = useRef<HTMLDivElement[]>([]);

	// 요소를 refs 배열에 추가하는 함수
	const addToRefs = (el: HTMLDivElement) => {
		if (el && !refs.current.includes(el)) {
			refs.current.push(el);
		}
	};

	useEffect(() => {
		if (isActive) {
			refs.current.forEach((el, index) => {
				gsap.to(el, {
					opacity: 1,
					duration: 0.5, // 애니메이션 지속 시간
					delay: index * 0.5, // 각 요소가 순차적으로 나타나도록 지연 시간 설정
				});
			});
		} else {
			// 슬라이드가 비활성화되면 모든 요소의 투명도를 0으로 설정
			refs.current.forEach((el) => {
				gsap.to(el, { opacity: 0, duration: 0.5 });
			});
		}
	}, [isActive]);

	return (
		<>
			<div className='Entire'>
				<div className='SecondPage'>
					<div className='SecondContent'>
						<div className='SecondText' ref={addToRefs} style={{ opacity: 0 }}>
							<Header as='h1' textAlign='left' style={{ marginTop: '5vh' }}>
								GIT으로 시작하는 당신의 코딩 여정!
							</Header>
							<p style={{ textAlign: 'left' }}>
								바로 본인만의 GIT 저장소를 설정하고, 새로운 코딩 과제를 시작하세요. <br />
                단 한 번의 클릭으로 개발 경험의 새 장을 엽니다.
							</p>
						</div>
						<div ref={addToRefs} style={{ opacity: 0 }}>
							<Image src={pair} size='large'></Image>
						</div>
					</div>
				</div>
			</div>
		</>
	);
};

import { useEffect, useRef } from 'react';
import { gsap } from 'gsap';
import { Header, Image } from 'semantic-ui-react';
import './FourthLanding.css';

import pair from '../../../../assets/images/pair.png';

interface LandingProp {
	isActive?: boolean;
}

export const FourthLandingPage = ({ isActive }: LandingProp) => {
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
					delay: 0.2 + index * 0.5, // 각 요소가 순차적으로 나타나도록 지연 시간 설정
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
				<div className='FourthPage'>
					<div className='FourthContent'>
						<div className='FourthText' ref={addToRefs} style={{ opacity: 0 }}>
							<Header as='h1' textAlign='left' style={{ marginTop: '5vh' }}>
								답답하신가요? 이모티콘을 사용해봐요!
							</Header>
							<p style={{ textAlign: 'left' }}>
								페어의 코드가 깔끔하고 기발한가요? 아니면 코드에서 냄새가 나나요? <br />
								이모티콘을 사용하여 감정을 표현해보세요. <br />
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

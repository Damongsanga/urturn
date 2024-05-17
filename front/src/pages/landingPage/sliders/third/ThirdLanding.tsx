import { useEffect, useRef } from 'react';
import { gsap } from 'gsap';
import { Header, Image } from 'semantic-ui-react';
import './ThirdLanding.css';

import coWorking from '../../../../assets/images/co-working.jpg';

interface LandingProp {
	isActive?: boolean;
}

export const ThirdLandingPage = ({ isActive }: LandingProp) => {
	const refs = useRef<HTMLDivElement[]>([]);

		// 요소를 refs 배열에 추가하는 함수 -> 역순
		const addToRefs = (el: HTMLDivElement) => {
			if (el && !refs.current.includes(el)) {
				refs.current.unshift(el);
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
				<div className='ThirdPage'>
					<div className='ThirdContent'>
						<div ref={addToRefs} style={{ opacity: 0 }}>
							<Image src={coWorking} size='large'></Image>
						</div>
						<div className='ThirdText' ref={addToRefs} style={{ opacity: 0 }}>
							<Header as='h1' textAlign='left' style={{ marginTop: '5vh' }}>
								페어 프로그래밍을 적용한 서비스를 경험해보세요.
							</Header>
							<p style={{ textAlign: 'left', lineHeight: '1.8'}}>
								한 문제를 해결하셨나요? 축하드립니다! <br />
								이제는 한 명은 드라이버, 한 명은 네비게이터가 되어 남은 문제를 함께 해결하게 됩니다. <br />
								음성 채팅을 통해 소통하며 숨겨진 잠재력을 발휘해봐요.
							</p>
						</div>
					</div>
				</div>
			</div>
		</>
	);
};

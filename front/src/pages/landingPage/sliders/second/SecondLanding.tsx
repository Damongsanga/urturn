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
								페어와 함께 번갈아가며 알고리즘 문제를 해결해보세요!
							</Header>
							<p style={{ textAlign: 'left' }}>
								선택한 난이도의 문제가 2개 주어집니다. <br />
								당신은 10분마다 페어가 작성한 코드만을 이어받아서 문제를 해결해야 합니다. <br />
								페어가 쉽게 이해할 수 있도록 가독성 좋은 코드를 작성해서 문제를 해결해보세요!
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

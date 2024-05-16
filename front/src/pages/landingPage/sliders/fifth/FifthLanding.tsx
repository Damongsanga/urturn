import { useEffect, useRef } from 'react';
import { gsap } from 'gsap';
import { Button, Header, Icon, Image } from 'semantic-ui-react';
import logo from '../../../../assets/images/logo.svg';
import './FifthLanding.css';

import hiFive from '../../../../assets/images/hi_five.jpg'

interface LandingProp {
	isActive?: boolean;
}

export const FifthLandingPage = ({ isActive }: LandingProp) => {
	const githubAuth = () => {
		location.href = `https://github.com/login/oauth/authorize?client_id=a82095fde8aa68bb396d&scope=user:email&redirect_uri=${
			import.meta.env.VITE_GITHUB_REDIRECT_URI
		}`;
	};

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
				<div className='FifthPage'>
					<div className='FifthContent'>
						<div ref={addToRefs} style={{ opacity: 0 }}>
							<Header as='h1' textAlign='center' style={{ marginTop: '5vh' }}>
								코드로 말하는 회고, GITHUB에 당신의 성장을 기록하세요. <br />
							</Header>
							<p style={{ textAlign: 'center' }}>
								문제 풀이 후, 회고 페이지에서 라운드 별 성과를 확인하고 GITHUB에 자동으로 업로드 됩니다.
								<br />
								당신의 놀라운 발전을 쉽게 기록해봐요!
							</p>
							<small>* git 저장소를 등록해야만 저장할 수 있어요. </small>
						</div>
						<div ref={addToRefs} style={{ opacity: 0, marginTop: '-5vh' }}>
							<Image src={hiFive} style= {{width: '23vw', height: 'auto'}}></Image>
						</div>
						<div className='CTABox' ref={addToRefs} style={{ opacity: 0, marginTop: '-3vh' }}>
							<div className='CTAText'>
								<Header as='h3' textAlign='left' style={{ color: 'white' }}>
									준비 되셨나요? 다음은 여러분의 차례입니다.
								</Header>
							</div>

							<Button
								circular
								className='BottomCTAButton'
								onClick={githubAuth}
								style={{ marginRight: '2vw' }}
							>
								<div className='InnerButton'>
									<Icon name='github' size='huge' />
									<span className='FontSize'>Github로 시작하기</span>
								</div>
							</Button>
						</div>
					</div>
					<div className='Copyright'>
						<div
							style={{
								width: '74vw',
								display: 'flex',
								alignItems: 'center',
								justifyContent: 'space-between',
							}}
						>
							<img alt='URTurn' src={logo} style={{ width: '125px', height: 'auto' }} />
							<div className='CopyrightText'>
								<small>서울 강남구 테헤란로 212 역삼 멀티캠퍼스 8층 802</small>
								<small>Copyright © URTurn 2024. All Rights Reserved.</small>
							</div>
							<div className='CopyrightText'>
								<p style={{ marginBottom: '0vh' }}>Contact Us</p>
								<small>SSAFY A206 코코</small>
							</div>
						</div>
					</div>
				</div>
			</div>
		</>
	);
};

import { useEffect, useRef } from 'react';
import { gsap } from 'gsap';
import { Button, Header, Icon, Image } from 'semantic-ui-react';
import logo from '../../../../assets/images/logo.svg';
import './FourthLandingPage.css';

import pair from '../../../../assets/images/pair.png';

interface LandingProp {
	isActive?: boolean;
}

export const FourthLandingPage = ({ isActive }: LandingProp) => {

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
				<div className='FourthPage'>
					<div className='FourthContent' ref={addToRefs} style={{ opacity: 0 }}>
						<Header as='h3' textAlign='center'>
							테스트 문구3
						</Header>
						<Image src={pair} size='large'></Image>
						<div className='CTABox'>
							<div className='CTAText'>
								<Header as='h3' textAlign='left' style={{ color: 'white' }}>
									준비 되셨나요?
								</Header>
								<Header as='h4' textAlign='left' style={{ color: 'white' }}>
                  다음은 여러분의  차례입니다.
								</Header>
							</div>
							<Button circular className='BottomCTAButton' onClick={githubAuth} style={{ marginRight:'2vw' }}>
								<Icon name='github' size='huge' /> <span className='FontSize'>Github로 시작하기</span>
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

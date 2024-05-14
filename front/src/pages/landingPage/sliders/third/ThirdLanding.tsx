import { useEffect, useRef } from 'react';
import { gsap } from 'gsap';
import { Header, Image } from 'semantic-ui-react';
import './ThirdLandingPage.css';

import pair from '../../../../assets/images/pair.png';

interface LandingProp {
  isActive?: boolean;
}

export const ThirdLandingPage = ({ isActive }: LandingProp) => {
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
        <div className='ThirdPage'>
          <div className='ThirdContent' ref={addToRefs} style={{ opacity: 0 }}>
            <Image src={pair} size='large'></Image>
            <Header as='h3' textAlign='center'>
              테스트 문구2
            </Header>
          </div>
        </div>
      </div>
    </>
  );
};

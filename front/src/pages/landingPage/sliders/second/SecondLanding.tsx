import { useEffect, useRef } from 'react';
import { gsap } from 'gsap';
import './SecondLanding.css';

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
    } 
    else {
      // 슬라이드가 비활성화되면 모든 요소의 투명도를 0으로 설정
      refs.current.forEach((el) => {
        gsap.to(el, { opacity: 0, duration: 0 });
      });
    }
  }, [isActive]);

  return (
    <>
      <div className='Entire'>
        <div className='SecondPage'>
          <div className='SecondContent'>
            <div ref={addToRefs} style={{ opacity: 0 }}>테스트 문구1</div>
            <div ref={addToRefs} style={{ opacity: 0 }}>테스트 문구2</div>
          </div>
        </div>
      </div>
    </>
  );
};

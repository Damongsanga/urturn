import { useEffect, useRef } from 'react';
import { gsap } from 'gsap';
import { ScrollTrigger } from 'gsap/ScrollTrigger';
import './SecondLanding.css';

export const SecondLandingPage = () => {
  const refs = useRef<(HTMLDivElement | null)[]>([]);

  useEffect(() => {
    gsap.registerPlugin(ScrollTrigger);

    refs.current.forEach((el, index) => {
      if (!el) return; 

      gsap.fromTo(el, { opacity: 0 }, {
        opacity: 1,
        duration: 1,
        delay: (index + 1) * 0.6,
        scrollTrigger: {
          trigger: el, 
          start: "top center+=100%", 
          toggleActions: "play none none none",
        },
      });
    });

  }, []); // 초기 마운트 시에만 실행됩니다.


  return (
    <>
      <div className='Entire'>
        <div className='SecondPage'>
          <div className='SecondContent'>
            {/* ref에 요소를 할당할 때, 각 요소마다 고유의 인덱스를 사용하여 할당합니다. */}
            <div ref={el => refs.current[0] = el}>으앙1</div>
            <div ref={el => refs.current[1] = el}>으앙2</div>
          </div>
        </div>
      </div>
    </>
  );
};

import './LandingPage.css';
import { useState } from 'react';
import { Swiper, SwiperSlide } from 'swiper/react';
import { Mousewheel, Pagination } from 'swiper/modules';
import { FirstLandingPage } from './sliders/first/FirstLanding';
import { SecondLandingPage } from './sliders/second/SecondLanding';
import { ThirdLandingPage } from './sliders/third/ThirdLanding';
import { FourthLandingPage } from './sliders/fourth/FourthLanding';
import { FifthLandingPage } from './sliders/fifth/FifthLanding';

import 'swiper/css';
import 'swiper/css/pagination';

export const LandingPage = () => {

	const [activeSlide, setActiveSlide] = useState(0);

	return (
		<>
		{/* swiper 세팅 */}
			<Swiper
				direction={'vertical'}
				slidesPerView={1}
				spaceBetween={0}
				mousewheel={true}
				modules={[Mousewheel, Pagination]}
				className='mySwiper'
				onSlideChange={(swiper) => setActiveSlide(swiper.activeIndex)}
			>
				<SwiperSlide>
					{/* 최상단 페이지 */}
					<FirstLandingPage />
				</SwiperSlide>
				<SwiperSlide>
					<SecondLandingPage isActive={activeSlide === 1} />
				</SwiperSlide>
				<SwiperSlide><ThirdLandingPage isActive={activeSlide === 2} /></SwiperSlide>
				<SwiperSlide><FourthLandingPage isActive={activeSlide === 3 } /> 4</SwiperSlide>
				<SwiperSlide><FifthLandingPage isActive={activeSlide === 4} /></SwiperSlide>
			</Swiper>
		</>
	);
};

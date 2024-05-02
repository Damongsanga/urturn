import { Swiper, SwiperSlide } from 'swiper/react';
import { Mousewheel, Pagination } from 'swiper/modules';
import { FirstLandingPage } from './sliders/first/FirstLanding';

import 'swiper/css';
import 'swiper/css/pagination';
import './LandingPage.css';

export const LandingPage = () => {

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
			>
				<SwiperSlide>
					{/* 최상단 페이지 */}
					<FirstLandingPage />
				</SwiperSlide>
				<SwiperSlide>Slide 2</SwiperSlide>
				<SwiperSlide>Slide 3</SwiperSlide>
				<SwiperSlide>Slide 4</SwiperSlide>
				<SwiperSlide>Slide 5</SwiperSlide>
			</Swiper>
		</>
	);
};

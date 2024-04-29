import 'semantic-ui-css/semantic.min.css';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import  MainPage  from './pages/mainPage/MainPage.tsx';
import { LandingPage} from "./pages/landingPage/LandingPage.tsx";
import {GithubCallback} from "./utils/Github.tsx";
import { MyPage } from './pages/myPage/MyPage.tsx';

function App() {

	return (
		<>
			<BrowserRouter>
				<Routes>
					<Route path='/' element={<LandingPage />} />
					<Route path='/main' element={<MainPage />} />
					<Route path='/auth/github' element={<GithubCallback/>}/>
					<Route path='/myPage' element={<MyPage />} />
				</Routes>
			</BrowserRouter>
		</>
	);
}

export default App;

import { BrowserRouter, Route, Routes } from 'react-router-dom';
import { LandingPage} from "./pages/landingPage/LandingPage.tsx";
import {GithubCallback} from "./utils/Github.tsx";
import { MyPage } from './pages/myPage/MyPage.tsx';
import  MainPage  from './pages/mainPage/MainPage.tsx';
import SolvePage from './pages/solvePage/SolvePage.tsx';
import CheckPage from './pages/solvePage/CheckPage.tsx';
import ReviewPage from './pages/solvePage/ReviewPage.tsx';
import PairSolvePage from './pages/solvePage/PairSolvePage.tsx';
import { TransPage } from './pages/trainsPage/TransPage.tsx';

function App() {

	return (
		<>
			<BrowserRouter>
				<Routes>
					<Route path='/' element={<LandingPage />} />
					<Route path='/trans/:next' element={<TransPage />} />
					<Route path='/main' element={<MainPage />} />
					<Route path='/auth/github' element={<GithubCallback/>}/>
					<Route path='/myPage' element={<MyPage />} />
					<Route path='/solve' element={<SolvePage />} />
					<Route path='/pairsolve' element={<PairSolvePage />} />
					<Route path='/check' element={<CheckPage />} />
					<Route path='/review' element={<ReviewPage />} />
				</Routes>
			</BrowserRouter>
		</>
	);
}

export default App;

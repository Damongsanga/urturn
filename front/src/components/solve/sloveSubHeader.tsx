import { Header } from "semantic-ui-react";
import './sloveSubHeader.css';

export const SolveSubHeader = ({ $mode }: { $mode: number }) => {

    return (
        <div>
            <div className='HeaderBar Ide' style={{ border: '7px solid white', height: '120px'}}>
                    {/* 모드별 헤더 문구 변경 */}
                    {$mode === 1 && (
                        <Header as='h1' textAlign='center' className='FontColor'>
                            스위칭 모드
                        </Header>
                    )}
                    {$mode === 2 && (
                        <Header as='h1' textAlign='center' className='FontColor'>
                            페어 프로그래밍 모드
                        </Header>
                    )}
			</div>
        </div>
    );
}
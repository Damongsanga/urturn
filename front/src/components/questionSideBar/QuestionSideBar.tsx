import { Menu, Popup, Header, Grid, GridRow, GridColumn,  Icon, MenuItem  } from "semantic-ui-react";
import './QuestionSideBar.css'
import { useRoomStore } from "../../stores/room";
import { useRtcStore } from "../../stores/rtc";
import { emojis } from "../../types/emojiTypes";
import { useEmojiStore } from "../../stores/emoji";
import { useEffect, useState} from "react";
import { AnimationContainer, useReactionAnimation } from "reaction-animation";
//import { AngryReactionObj, DislikeReactionObj, HahaReactionObj, LikeReactionObj, SadReactionObj, WowReactionObj } from "../emoji/emojiElement";

let ReactionObj = () => {
	return (
	  <div>
		👍
	  </div>
	);
};

export const QuestionSideBar = () => {
	const roomStore = useRoomStore();
	const rtcStore = useRtcStore();
	const emojiStore = useEmojiStore();

	const { reactionDetails, addReaction } = useReactionAnimation();

	const [emojiFlux, setEmojiFlux] = useState<string>('');

	useEffect(() => {
		if(rtcStore.getOpenVidu()?.session.off('signal:emoji'))
		rtcStore.getOpenVidu()?.session.on('signal:emoji', (event) => {
			if(event.data)
				setEmojiFlux(event.data);
		})
	}, [])

	useEffect(() => {
		const key = emojiFlux;
		setEmojiFlux('');

		printEmoji(key);
	}, [emojiFlux])

	const sendEmoji = (key: string) => {
		sendEmojiOV(key);
		//printEmoji(key);
	}

	const sendEmojiOV = async (key: string) => {
		rtcStore.getOpenVidu()?.session.signal({
			data: key,
			to: [],
			type: 'emoji'
		})
	}


	const printEmoji = (key: string) => {

		//addReaction({ ReactionObj, animationDuration: 5 });

		// //let ReactionObj = undefined;
		
		if(key==='like'){
			ReactionObj = () => {
				return (
				  <div>
					👍
				  </div>
				);
			};
			console.log("???")
			addReaction({ ReactionObj, animationDuration: 5 });
		}
		else if(key==='dislike'){
			ReactionObj = () => {
				return (
				  <div>
					👎
				  </div>
				);
			};
			addReaction({ ReactionObj, animationDuration: 5 });
		}
		else if(key==='angry'){
			ReactionObj = () => {
				return (
				  <div>
					😠
				  </div>
				);
			};
			addReaction({ ReactionObj, animationDuration: 5 });
		}
		else if(key==='haha'){
			ReactionObj = () => {
				return (
				  <div>
					😂
				  </div>
				);
			};
			addReaction({ ReactionObj, animationDuration: 5 });
		}
		else if(key==='wow'){
			ReactionObj = () => {
				return (
				  <div>
					😮
				  </div>
				);
			};
			addReaction({ ReactionObj, animationDuration: 5 });
		}
		else if(key==='sad'){
			ReactionObj = () => {
				return (
				  <div>
					😭
				  </div>
				);
			};
			addReaction({ ReactionObj, animationDuration: 5 });
		}
		else if(key==='heart'){
			ReactionObj = () => {
				return (
				  <div>
					❤️
				  </div>
				);
			};
			addReaction({ ReactionObj, animationDuration: 5 });
		}
	}
  
	return (
		<>
			<Menu secondary icon vertical borderless style={{ marginTop: 'auto' ,backgroundColor: 'transparent' }}>
				<Popup
					className='PopUp'
					position='right center'
					// pop 내용
					content={
						<>
							<Header as='h2' textAlign='left' className='FitContent'>
								볼륨
							</Header>
							<Grid columns={1} style={{ marginLeft : '-2em'}}>
								<GridRow className='Row'>
									<GridColumn width={4} verticalAlign={'middle'} >
										{/* 스피커 아이콘 */}
										<Icon className='Icon' name='volume up' size='big' />
									</GridColumn>
									<GridColumn width={4} verticalAlign={'middle'} style={{ marginLeft : '2em'}}>
										{/* 마이크 아이콘 */}
										<Icon className='Icon' name='microphone' size='big' />
									</GridColumn>
								</GridRow>
							</Grid>
						</>
					}
					on='click'
					pinned
					// pop을 띄울 버튼
					trigger={
						<MenuItem name='PopupVolume'>
							<Icon
								className='Mic'
								name='microphone'
								size='big'
								color='blue'
							/>
						</MenuItem>
					}
				/>
				<Popup
					className="PopUp"
					position='right center'
					content={
						<>
							<Menu>
								{
									Object.keys(emojis).map((key) => (
										<MenuItem key={key} onClick={() => sendEmoji(key)}>
											{emojis[key as keyof typeof emojis]}
										</MenuItem>
									))
								}
							</Menu>
						</>
					}
					on={'click'}
					pinned
					trigger={
						<MenuItem id='questionSideBarMyProfileImg' name='Profile'>
							<img alt='profile' src={roomStore.getUserInfo()?.myUserProfileUrl} />
						</MenuItem>
					}
				/>
				<MenuItem id='questionSideBarOtherProfileImg' name='Profile'>
					<img
						alt='profile'
						src={roomStore.getUserInfo()?.relativeUserProfileUrl}
						style={{ marginBottom: '5vh' }}
					/>
				</MenuItem>
			</Menu>

			{
				emojiStore.getMdInContainerInfo().top?
				<AnimationContainer
				reactionDetails={reactionDetails}
				style={{ position: 'absolute', top: emojiStore.getMdInContainerInfo().top, left: emojiStore.getMdInContainerInfo().left, width: 200, height: emojiStore.getMdInContainerInfo().height }}
				/>
				:
				<></>
			}
		</>
	);
};

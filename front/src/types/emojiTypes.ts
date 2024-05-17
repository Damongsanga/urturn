type Emojis = {
    [key: string]: string;
  }
  

export const emojis: Emojis = {
    like: '👍',
    dislike: '👎',
    angry: '😠',
    haha: '😂',
    wow: '😮',
    sad: '😭',
    heart: '❤️'
}

export interface MarkDownContainerInfo{
    top: number;
    left: number;
    width: number;
    height: number;
}

export interface EmojiState {

    mdInContainerInfo: MarkDownContainerInfo;

    setMdInContainerInfo: (info: MarkDownContainerInfo) => void;
    getMdInContainerInfo: () => MarkDownContainerInfo;
    
    clearEmoji: () => void;
}
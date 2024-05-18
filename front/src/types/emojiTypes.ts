type Emojis = {
    [key: string]: string;
  }
  

export const emojis: Emojis = {
    like: '👍',
    angry: '😵',
    haha: '😂',
    wow: '🍝',
    heart: '❤️',
    sad: '😈',
    dislike: '💩',
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
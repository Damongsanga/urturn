import { create } from 'zustand';
import { EmojiState, MarkDownContainerInfo } from '../types/emojiTypes';

export const useEmojiStore = create<EmojiState>() (
    // persist(
    (set, get) => ({
        mdInContainerInfo: {
            top: 0,
            left: 0,
            width: 0,
            height: 0
        },

        setMdInContainerInfo: (info: MarkDownContainerInfo) => {set({ mdInContainerInfo: info }); },
        getMdInContainerInfo: () => {return get().mdInContainerInfo},

        clearEmoji: () => {

        }
    }),
);

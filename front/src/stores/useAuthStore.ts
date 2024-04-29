import { create } from 'zustand';
import { persist } from 'zustand/middleware';

interface JwtToken {
    grantType: string;
    accessToken: string;
}

interface LoginResponse {
    memberId: number;
    nickname: string;
    profileImage: string;
    jwtToken: JwtToken;
}

interface AuthState {
    accessToken: string | undefined;
    memberId: number | undefined;
    nickname: string | undefined;
    profileImage: string | undefined;
    tokenExpireAt: number | undefined;

    setAuth: (loginResponse: LoginResponse) => void;
    setAccessToken: (accessToken: string) => void;
    clearAuth: () => void;
}

const useAuthStore = create<AuthState>()(
    persist(
        (set) => ({
            accessToken: undefined,
            memberId: undefined,
            nickname: undefined,
            profileImage: undefined,
            tokenExpireAt: undefined,

            setAuth: ({ memberId, nickname, profileImage, jwtToken }) => {
                set({
                    accessToken: jwtToken.accessToken,
                    memberId,
                    nickname,
                    profileImage,
                    tokenExpireAt: Date.now() + Number(import.meta.env.VITE_JWT_ACCESS_EXPIRE_TIME),
                });
            },
            setAccessToken: (accessToken: string) => {
                set((state) => ({ ...state, accessToken, tokenExpireAt: Date.now() + Number(import.meta.env.VITE_JWT_ACCESS_EXPIRE_TIME) }));
            },
            clearAuth: () => {
                set({
                    accessToken: undefined,
                    memberId: undefined,
                    nickname: undefined,
                    profileImage: undefined,
                    tokenExpireAt: undefined,
                });
            },
        }),
        {
            name: 'auth-storage',
        },
    ),
);

export { useAuthStore };

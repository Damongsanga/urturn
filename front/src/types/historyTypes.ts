export interface HistoryEntry {
    id: number;
    pair: {
        id: number;
        nickname: string;
        profileImage: string;
    };
    problem1: {
        id: number;
        title: string;
        level: string;
    };
    problem2: {
        id: number;
        title: string;
        level: string;
    };
    code1: string;
    code2: string;
    result: string;
    totalRound: number;
    startTime: string;
    endTime: string;
}

export interface PageableResponse {
    content: HistoryEntry[];
    last: boolean;
    totalElements: number;
    totalPages: number;
    size: number;
    number: number;
}
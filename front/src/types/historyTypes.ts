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
        level: string;  // Assuming "LEVEL1" to "LEVEL5"
    };
    problem2: {
        id: number;
        title: string;
        level: string;  // Assuming "LEVEL1" to "LEVEL5"
    };
    code1: string;
    code2: string;
    result: 'SUCCESS' | 'FAILURE' | 'SURRENDER';
    totalRound: number;
    startTime: string;  // ISO Date format
    endTime: string;    // ISO Date format
}

export interface PageableResponse {
    content: HistoryEntry[];
    pageable: {
        pageNumber: number;
        pageSize: number;
        offset: number;
        paged: boolean;
        unpaged: boolean;
    };
    totalPages: number;
    totalElements: number;
    first: boolean;
    last: boolean;
    size: number;
    number: number;
    sort: {
        empty: boolean;
        sorted: boolean;
        unsorted: boolean;
    };
    numberOfElements: number;
    empty: boolean;
}
import { AxiosInstance } from "axios";
import { PageableResponse } from "../../types/historyTypes.ts";

export const fetchHistory = async (page: number, size: number = 6, axiosInstance: AxiosInstance): Promise<PageableResponse> => {
    const response = await axiosInstance.get<PageableResponse>(`/history?page=${page}&size=${size}`);
    return response.data;
};
// export const fetchHistory = async (page: number, size: number = 6): Promise<PageableResponse> => {
//     const response = await axios.get<PageableResponse>(`/history?page=${page}&size=${size}`);
//     return response.data;
// };
